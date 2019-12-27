package com.greit.weys.join;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.greit.weys.ResValue;
import com.greit.weys.common.AriaUtils;
import com.greit.weys.common.Barcode;
import com.greit.weys.common.Constant;
import com.greit.weys.common.ErrCode;
import com.greit.weys.common.Utils;
import com.greit.weys.config.TokenHandler;
import com.greit.weys.push.PushService;
import com.greit.weys.user.UserDao;
import com.greit.weys.user.UserDetailVO;
import com.greit.weys.user.UsrVO;

@Service
public class JoinService {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private JoinDao joinDao;
	@Autowired
	private UserDao userDao;
	
	@Value("#{props['ENC.KEY']}")
	private String ENC_KEY;
	@Value("#{props['JOIN.MEMBER.PERIOD']}")
	private int JOIN_MEMBER_PERIOD;
	
	@Value("${SERVER.TYPE}")
	private String SERVER_TYPE;
	@Value("${UPLOAD.PATH}")
	private String UPLOAD_PATH;
	@Value("${FRD.COUPON.ID}")
	private int FRD_COUPON_ID;
	
	@Value("#{props['IB.SERVICE.ID']}")
	private String IB_SERVICE_ID;
	@Value("#{props['IB.SERVICE.PWD']}")
	private String IB_SERVICE_PWD;
	@Value("#{props['IB.FROM.TEL']}")
	private String IB_FROM_TEL;

	// FCML
	@Value("#{props['FCM.SERVER.KEY']}")
	private String FCM_SERVER_KEY; // FCM 서버 키
	@Value("#{props['FCM.SEND.URL']}")
	private String FCM_SEND_URL; // FCM 발송 URL

	public ResValue insertJoinUser(UserDetailVO reqVO, int ver) throws Exception {
		
		ResValue result = new ResValue();

		/**
		 * 이모티콘 확인
		 */
//		Pattern emoticon = Pattern.compile("[\\uD83C-\\uDBFF\\uDC00-\\uDFFF]+");
//		Matcher emoMatch = emoticon.matcher(reqVO.getUsrPw());
//		if(emoMatch.find()){
//			result.setResCode(ErrCode.DISABLE_EMOTICON);
//			result.setResMsg(ErrCode.getMessage(ErrCode.DISABLE_EMOTICON));
//			return result;
//		}
		
		/**
		 * 전화번호 중복체크 
		 */
		String nation = reqVO.getNation();
		String usrTel = reqVO.getUsrTel();
		if(nation.equals("82") && !usrTel.startsWith("0") && usrTel.length() == 10){
			usrTel = "0" + usrTel;
			reqVO.setUsrTel(usrTel);
		}
		
		UsrVO telVO = new UsrVO();
		telVO.setEncKey(ENC_KEY);
		telVO.setNation(nation);
		telVO.setUsrTel(usrTel);
		int telCnt = userDao.checkUsrTel(telVO);
		if(telCnt > 0){
			result.setResCode(ErrCode.SIGNUP_TEL_OVERLAP);
			result.setResMsg(ErrCode.getMessage(ErrCode.SIGNUP_TEL_OVERLAP));
			return result;
			
		}
		
		// 이메일 중복체크
		Map<String, Object> reqMap = new HashMap<>();
		reqMap.put("usrEmail", reqVO.getUsrEmail());
		reqMap.put("encKey", ENC_KEY);
		int cnt = joinDao.checkEmail(reqMap);
		if (cnt > 0) {
			// 이메일 중복
			result.setResCode(ErrCode.SIGNUP_EMAIL_OVERLAP);
			result.setResMsg(ErrCode.getMessage(ErrCode.SIGNUP_EMAIL_OVERLAP));
			return result;
		}
		
		/**
		 * 3차부터 이메일 인증 제거
		 */
		reqVO.setEncKey(ENC_KEY);
		String tel = reqVO.getUsrTel();
		reqVO.setUsrTel(tel.replaceAll("\\-", ""));
		
		String targetFrdCd = reqVO.getFrdCd();
		
		/**
		 * 친구추천 코드 생성
		 */
		String frdCd = "";
		while(true){
			frdCd = Utils.makeCode(6);
			int frdCnt = joinDao.checkFrdCd(frdCd);
			if(frdCnt == 0){
				break;
			}
		}
		reqVO.setFrdCd(frdCd);
		int insertRes = joinDao.insertUserInfo(reqVO);
		
		// 회원 정보 입력
		if (insertRes > 0) {
			// 회원가입 이후, 설정된 키값과 비밀번호 이용해서 암호화.
			String pw = AriaUtils.encryptPassword(reqVO.getUsrPw(), String.valueOf(reqVO.getUsrId()));
			String ak = AriaUtils.encryptPassword(SERVER_TYPE, String.valueOf(reqVO.getUsrId())).replaceAll("/", "");
			ak = ak.replaceAll("\\+", "");
			reqVO.setUsrPw(pw);
			reqVO.setUsrAk(ak);

			insertRes = joinDao.updateUserPw(reqVO);
			
			/**
			 * 친구 코드 등록
			 * 2019.10.30
			 * 초대한 사람, 초대받은 사람 쿠폰 등록
			 */
			if(targetFrdCd != null && !targetFrdCd.equals("")){
				targetFrdCd = targetFrdCd.toUpperCase();
				int targetUsrId = joinDao.selectFrdId(targetFrdCd);
				if(targetUsrId > 0){
					
					Map<String, Object> frdMap = new HashMap<>();
					frdMap.put("targetUsrId", reqVO.getUsrId());
					frdMap.put("dutDt", Utils.getDiffMonth(3));
					frdMap.put("couponId", FRD_COUPON_ID);
					joinDao.insertFrdCoupon(frdMap);

					int frdCnt = joinDao.selectFrdCnt(targetUsrId);
					if(frdCnt == 0){
						frdMap.put("targetUsrId", targetUsrId);
						int fr = joinDao.insertFrdCoupon(frdMap);
						if(fr > 0){
							frdMap.put("reqUsrId", reqVO.getUsrId());
							joinDao.insertFrdMap(frdMap);
						}
					}
					
//					/**
//					 * 친구 추천한 적 이력 확인
//					 */
//					List<Integer> frdList = joinDao.selectChkFrdSubmit(reqVO.getUsrId());
//					
//					boolean chkFrd = false;
//					for(int frdUsrId : frdList){
//						if(targetUsrId == frdUsrId){
//							chkFrd = true;
//							break;
//						}
//					}
//					
//					if(!chkFrd){
//						Map<String, Object> frdMap = new HashMap<>();
//						frdMap.put("targetUsrId", targetUsrId);
//						frdMap.put("reqUsrId", reqVO.getUsrId());
//						joinDao.insertFrdMap(frdMap);
//						
//						/**
//						 * 해당 친구가 친구추천받은 횟수가 3회가 되면
//						 * 100% 우대쿠폰 발급
//						 * 개발서버는 110
//						 */
//						int frdCnt = joinDao.selectFrdCnt(targetUsrId);
//						if(frdCnt == 3){
//							frdMap.put("dutDt", Utils.getDiffMonth(3));
//							frdMap.put("couponId", FRD_COUPON_ID);
//							int couponCnt = joinDao.insertFrdCoupon(frdMap);
//							if(couponCnt > 0){
//								String msg = "미국달러/일본엔 100% 우대 쿠폰이 발급되었습니다.(" + Utils.getDiffMonth(3) + "까지 사용가능)";
//								Map<String, Object> targetMap = joinDao.selectFrduuid(targetUsrId);
//								
//								if(targetMap != null){
//									String uuid = MapUtils.getString(targetMap, "UUID");
//									String os = MapUtils.getString(targetMap, "OS");
//									
//									if(os.equals("A")){
//										JSONObject dataJson = new JSONObject();
//										dataJson.put("type", "info");
//										dataJson.put("val", "");
//										dataJson.put("message", msg);
//										
//										JSONObject json = new JSONObject();
//										json.put("to", uuid);
//										json.put("data", dataJson);
//										
//										PushService push = new PushService(json, FCM_SERVER_KEY, FCM_SEND_URL);
//										Thread t = new Thread(push);
//										t.start();
//
//									} else if(os.equals("I")){
//										JSONObject pushObj = new JSONObject();
//										pushObj.put("to", uuid);
//										JSONObject dataJson = new JSONObject();
//										dataJson.put("title", msg);
//										dataJson.put("contents","info");
//										dataJson.put("val", "");
//										dataJson.put("img", "");
//										
//										JSONObject notiJson = new JSONObject();
//										notiJson.put("title", "");
//										notiJson.put("body", msg);
//										notiJson.put("icon", "");
//										
//										pushObj.put("content_available", true);
//										pushObj.put("data", dataJson);
//										pushObj.put("priority", "high");
//										pushObj.put("notification", notiJson);
//										
//										PushService push = new PushService(pushObj, FCM_SERVER_KEY, FCM_SEND_URL);
//										Thread t = new Thread(push);
//										t.start();
//									}
//								}
//							}
//						}
//					}
				}
			}

			// 토큰 생성
			String token = TokenHandler.getToken(reqVO);
			reqVO.setTokenWeys(token);

			// uuid, os null 일 경우 빈값
			if (reqVO.getUuid() == null)
				reqVO.setUuid("");
			if (reqVO.getOs() == null)
				reqVO.setOs("");

			Calendar cal = Calendar.getInstance();
			Date dt = new Date();
			cal.setTime(dt);
			cal.add(Calendar.DATE, 30);

			// 토큰 생성
			Date d = cal.getTime();
			reqVO.setTokenExpirationDttm(d);
			userDao.insertTokenInfo(reqVO);

			Map<String, Object> resultMap = new HashMap<String, Object>();
			result.setResData(resultMap);
			
			/**
			 * 첫 회원가입이라면
			 * 회원 가입시 배송비 쿠폰 및 총 5종 발급
			 */
			if(ver >= 10){
				int existCnt = joinDao.selectUsrTelCnt(reqVO.getUsrId());
				if(existCnt == 0 || SERVER_TYPE.equals("TEST")){
					
					String code = Constant.JOIN_COUPON_CODE;
					List<Integer> couponList = joinDao.selectJoinCouponList(code);
					for(int couponId : couponList){
						Map<String, Object> joinMap = new HashMap<>();
						joinMap.put("usrId", reqVO.getUsrId());
						joinMap.put("couponId", couponId);
						joinMap.put("dueDt", Utils.getDiffDate(30) + " 23:59:59");
						int resCnt = userDao.insertJoinCoupon(joinMap);
						if(resCnt > 0){
							int eventCnt = (int) (Math.random() * 10) + 10;
							joinMap.put("eventCnt", eventCnt);
							userDao.updateEventCnt(joinMap);
						}
					}
				}
			}
			
			/**
			 * 핸드폰 번호로 가입된 멤버십이 있는지 확인
			 * 있다면 멤버십에 내용 추가
			 */
			// 바코드 생성
			// 중복 바코드 없는것 찾기
			boolean checkBarcode = true;
			String barcode = "";
			while(checkBarcode){
				barcode = Barcode.CreateQrCode();
				int checkCnt = joinDao.selectMemberBarcodeCnt(barcode);
				if(checkCnt == 0){
					checkBarcode = false;
				}
			}
			// QR CODE 이미지 생성
			String qrCodeUrl = Barcode.CreateQRCodePng(barcode, UPLOAD_PATH, "qrmem");
			if(qrCodeUrl == null){
				throw new Exception("서버 내부 에러");
			}
			
			int joinCost = 0;
			
			String endDt = Utils.getDiffMonth(JOIN_MEMBER_PERIOD);
			Map<String, Object> insertMap = new HashMap<>();
			insertMap.put("usrId", reqVO.getUsrId());
			insertMap.put("barcode", barcode);
			insertMap.put("barcodeUrl", qrCodeUrl);
			insertMap.put("endDt", endDt);
			insertMap.put("cost", joinCost);
			insertMap.put("txt", "신규가입");
			
			int res = joinDao.insertMemberInfo(insertMap);
			
			if(res > 0){
				res = joinDao.insertMemberActive(insertMap);
			}

		} else {
			// 회원 가입 실패
			result.setResCode(ErrCode.SIGNUP_FAILED);
			result.setResMsg(ErrCode.getMessage(ErrCode.SIGNUP_FAILED));
		}
		return result;
	}

	public int checkFrd(String frdCd) {
		return joinDao.checkFrdCd(frdCd);
	}

}
