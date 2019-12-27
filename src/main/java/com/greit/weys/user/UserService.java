package com.greit.weys.user;

import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.KeyGenerator;
import com.greit.weys.ResValue;
import com.greit.weys.common.AriaUtils;
import com.greit.weys.common.Constant;
import com.greit.weys.common.ErrCode;
import com.greit.weys.common.IdentifyUtils;
import com.greit.weys.common.SMSUtil;
import com.greit.weys.common.Utils;
import com.greit.weys.config.TokenHandler;
import com.greit.weys.config.TokenValues;
import com.greit.weys.join.JoinDao;

@Service
public class UserService {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private UserDao userDao;
	@Autowired
	private JoinDao joinDao;

	@Value("${SERVER.TYPE}")
	private String SERVER_TYPE;
	@Value("#{props['ENC.KEY']}")
	private String ENC_KEY;
	@Value("#{props['IB.SERVICE.ID']}")
	private String IB_SERVICE_ID;
	@Value("#{props['IB.SERVICE.PWD']}")
	private String IB_SERVICE_PWD;
	@Value("#{props['IB.FROM.TEL']}")
	private String IB_FROM_TEL;
	
	// 나이스 평가모듈 
	@Value("#{props['NAME.CHECK.CODE']}")
	private String NAME_CHECK_CODE;
	@Value("#{props['NAME.CHECK.PWD']}")
	private String NAME_CHECK_PWD;

	/**
	 * @param req
	 * @param res
	 * 로그인, 회원가입 관련 제외하고는 토큰 체크 
	 * @return
	 */
	public TokenValues updateCheckToken(HttpServletRequest req, HttpServletResponse res, ResValue resultVal) {
		
		TokenValues result = new TokenValues();
		try{
			// 토큰 인증 
			result = TokenHandler.getTokenValues(req, res);
			if(result == null)
				return null;
			
			String token = (String) req.getAttribute("HeadToken");
			if(result.getUserKey().equals("0")){} 
			else {
				// 해당 토큰 관련된 유저가 있는지 확인, 유저키 불러와서 토큰에 있는 유저키와 맞는지 비교. 
				int tokenCnt = userDao.selectCheckToken(token);
				if(tokenCnt == 0){
					resultVal.setResCode(ErrCode.ACCESS_CRASH);
					resultVal.setResMsg(ErrCode.getMessage(ErrCode.ACCESS_CRASH));
					return null;
				}
				
				/**
				 * API 로그 등록
				 */
				Map<String, Object> reqMap = new HashMap<String, Object>();
				reqMap.put("usrId", result.getUserKey());
				reqMap.put("url", req.getRequestURI());
				reqMap.put("method", req.getMethod());
				userDao.insertApiLog(reqMap);
			}
		} catch (Exception e) {
			resultVal.setResCode(ErrCode.INVALID_TOKEN);
			resultVal.setResMsg(ErrCode.getMessage(ErrCode.INVALID_TOKEN));
			return null;
		}
		
		return result;
	}
	
	public Map<String, Object> updateCheckUsrTel(UsrVO reqVO) throws Exception {
		
		/**
		 * 전화번호 중복체크 
		 */
		String nation = reqVO.getNation();
		String usrTel = reqVO.getUsrTel();
		if(nation.equals("82") && !usrTel.startsWith("0") && usrTel.length() == 10){
			usrTel = "0" + usrTel;
			reqVO.setUsrTel(usrTel);
		}
		
		reqVO.setEncKey(ENC_KEY);
		int res = userDao.checkUsrTel(reqVO);
		
		Map<String, Object> resultMap = new HashMap<>();
		if(res == 0){
			if(reqVO.getType().equals("J")){
//				resultMap.put("num", updatecCreateRandom(reqVO));
			}
			else
				resultMap.put("num", "0");
		} else {
			resultMap.put("num", "1");
		}
		
		return resultMap;
	}
	
	private String updatecCreateRandom(UsrVO reqVO) throws Exception {
		/**
		 * 휴대폰 SNS 모듈과 연동 필요
		 * 랜덤 숫자 6자리 고객 폰에게 전송. 
		 */
		int ran = (int) (Math.random() * 1000000);
		String random = "";
		
		if(ran < 10){
			random = "00000" + ran;
		} else if(ran < 100){
			random = "0000" + ran;
		} else if(ran < 1000){
			random = "000" + ran;
		} else if(ran < 10000){
			random = "00" + ran;
		} else if(ran < 100000){
			random = "0" + ran;
		} else{
			random = ran + "";
		}
		
		String msg = "[웨이즈] 인증번호 [ " + random + " ] 입력해주세요.";
		
		String tel = reqVO.getUsrTel();
		if(reqVO.getNation().equals("82") && tel.length() == 11 && tel.startsWith("010")){
			tel = tel.substring(1);
		}

		String url = SERVER_TYPE.equals("USER") ? Constant.INFO_BANK_URL_REAL : Constant.INFO_BANK_URL_TEST;
		
		url = url + "?id=" + IB_SERVICE_ID
				+ "&pwd=" + IB_SERVICE_PWD
				+ "&message=" + URLEncoder.encode(msg, "UTF-8")
				+ "&from=" + IB_FROM_TEL
				+ "&to_country=" + reqVO.getNation()
				+ "&to=" + tel
				+ "&report_req=1";
		
		String smsResult = SMSUtil.sendSms(url);
		
		Map<String, Object> reqMap = new HashMap<>();
		reqMap.put("nation", reqVO.getNation());
		reqMap.put("tel", tel);
		reqMap.put("txt", msg);
		reqMap.put("encKey", ENC_KEY);
		
		if(smsResult.contains("R000")){
			reqMap.put("res", "R000");
		} else {
			reqMap.put("res", smsResult);
		}
		userDao.insertSmsSend(reqMap);
		
		return random;
	}

	public ResValue insertUserInfoByLogin(UsrVO reqVO) throws Exception {
		
		ResValue result = new ResValue();

		String nation = reqVO.getNation();
		String usrTel = reqVO.getUsrTel();
		if(nation.equals("82") && !usrTel.startsWith("0") && usrTel.length() == 10){
			usrTel = "0" + usrTel;
			reqVO.setUsrTel(usrTel);
		}
		reqVO.setEncKey(ENC_KEY);
		UserDetailVO userInfo = userDao.selectUserLoginInfo(reqVO);
		boolean isOk = false;

		// 정보 존재 
		if(userInfo != null){
			// 이메일 로그인 비번 확인
			String ori_pw = userInfo.getUsrPw();
			String pw = AriaUtils.encryptPassword(reqVO.getUsrPw(), String.valueOf(userInfo.getUsrId()));
			
			if(pw.equals(ori_pw)){
				userInfo.setOs(reqVO.getOs());
				userInfo.setUuid(reqVO.getUuid());
				isOk = true;
			} else {
				// 비번 불일치  
				result.setResCode(ErrCode.LOGIN_FAILED);
				result.setResMsg(ErrCode.getMessage(ErrCode.LOGIN_FAILED));
			}
		} else {
			// 정보 없음 
			result.setResCode(ErrCode.LOGIN_FAILED);
			result.setResMsg(ErrCode.getMessage(ErrCode.LOGIN_FAILED));
		}
		
		// 로그인 성공시 
		if(isOk){
			/**
			 * 접속 로그
			 * --> 메인 배너 호출시로 변경
			 */
//			userDao.insertConnLog(userInfo.getUsrId());
			// 토큰 생성
			String token = TokenHandler.getToken(userInfo); 
			userInfo.setTokenWeys(token);
			userInfo.setVersion(reqVO.getVersion());
			/**
			 * 같은 uuid 존재하는것이 있다면 해당 uuid 는 공백으로 변경
			 */
			userDao.updateUuidBlank(userInfo.getUuid());
			insertTokenInfo(userInfo);
			
			/**
			 * 비회원 토큰 제거
			 */
			if(reqVO.getUnKey() > 0){
				userDao.deleteUnknownToken(reqVO.getUnKey());
			}
			
			UserReturnVO resultVO = new UserReturnVO();
			resultVO.setUsrNm(userInfo.getUsrNm());
			resultVO.setTokenWeys(userInfo.getTokenWeys());
			resultVO.setUuid("");
			resultVO.setUsrAk(userInfo.getUsrAk());
			resultVO.setNoticeId(0);
			result.setResData(resultVO);
		}
		
		return result;
	}

	public void insertTokenInfo(UserDetailVO userInfo) throws Exception {
		
		Calendar cal = Calendar.getInstance();
		Date dt = new Date();
		cal.setTime(dt);
		cal.add(Calendar.DATE, 365);
		
		Date d = cal.getTime();
		userInfo.setTokenExpirationDttm(d);
		userDao.insertTokenInfo(userInfo);
	}

	public ResValue updateAutoLogin(UserDetailVO reqVO) throws Exception {
		
		ResValue result = new ResValue();
		reqVO.setEncKey(ENC_KEY);
		UserDetailVO userInfo = userDao.selectUserInfo(reqVO);
		
		// uuid 가 빈값이 아니면 업데이트 
		if(!reqVO.getUuid().equals(""))
			userInfo.setUuid(reqVO.getUuid());
		
		/**
		 * 접속 로그
		 * --> 메인 배너 호출시로 변경
		 */
//		userDao.insertConnLog(reqVO.getUsrId());
		// 토큰 생성
		String token = TokenHandler.getToken(userInfo); 
		userInfo.setTokenWeys(token);
		
		insertTokenInfo(userInfo);

		UserReturnVO resultVO = new UserReturnVO();
		resultVO.setTokenWeys(token);
		resultVO.setUsrNm(userInfo.getUsrNm());
		resultVO.setUuid("");
		resultVO.setUsrAk(userInfo.getUsrAk());
		resultVO.setNoticeId(0);
		result.setResData(resultVO);
		
		return result;
	}

	public Map<String, Object> updateFinePwd(UsrVO reqVO) throws Exception {
		reqVO.setEncKey(ENC_KEY);
		int res = userDao.checkUsrTel(reqVO);
		
		Map<String, Object> resultMap = new HashMap<>();
		if(res > 0)
			resultMap.put("num", updatecCreateRandom(reqVO));
		else 
			return null;
		
		return resultMap;
	}

	public int updateUsrPw(UsrVO reqVO) throws Exception {
		
		reqVO.setEncKey(ENC_KEY);
		int usrId = userDao.selectUsrIdForTel(reqVO);
		String pw = AriaUtils.encryptPassword(reqVO.getUsrPw(), String.valueOf(usrId));
		
		reqVO.setUsrId(usrId);
		reqVO.setUsrPw(pw);
		
		return userDao.updateUsrPasswd(reqVO);
	}

	public String checkUsrCertify(UsrVO reqVO) {
		/**
		 * 실명인증
		 */
		IdentifyUtils idu = new IdentifyUtils(NAME_CHECK_CODE, NAME_CHECK_PWD);
		boolean chk = idu.checkIdentify(reqVO.getUsrNm(), reqVO.getUsrNmId().replaceAll("-", ""));
		if(!chk){
			return "false";
		} else {
			reqVO.setEncKey(ENC_KEY);
			String tel = userDao.selectCheckCertify(reqVO);
			
			if(tel == null || tel.equals("")){
				return "success";
			} else {
				return tel;
			}
		}
	}
	
	public int updateUsrCertify(UsrVO reqVO) {
		/**
		 * 실명인증 등록
		 */
		return updateUsrGndrBirth(reqVO);
	}
	
	public String updateUsrCertifyV9(UsrVO reqVO) {
		/**
		 * 실명인증
		 */
		IdentifyUtils idu = new IdentifyUtils(NAME_CHECK_CODE, NAME_CHECK_PWD);
		boolean chk = idu.checkIdentify(reqVO.getUsrNm(), reqVO.getUsrNmId().replaceAll("-", ""));
		if(!chk){
			return "false";
		} else {
			reqVO.setEncKey(ENC_KEY);
			String tel = userDao.selectCheckCertify(reqVO);
			
			if(tel == null || tel.equals("")){
				int res = updateUsrGndrBirth(reqVO);
				if(res > 0){
					return "success";
				}
			} else {
				return tel;
			}
		}
		return "false";
	}

	private int updateUsrGndrBirth(UsrVO reqVO) {
		/**
		 * 주민등록번호로 성별과 생년월일 구하기
		 */
		String birth = "";
		String backNum = "";
		String rsvNmId = reqVO.getUsrNmId();
		
		if(rsvNmId.contains("-")){
			StringTokenizer st = new StringTokenizer(rsvNmId, "-");
			birth = st.nextToken().trim();
			backNum = st.nextToken().trim();
		} else if(rsvNmId.length() == 13) {
			birth = rsvNmId.substring(0, 6).trim();
			backNum = rsvNmId.substring(6).trim();
		} else {
			return 0;
		}
		
		backNum = backNum.substring(0, 1);
		String gndr = "M";
		
		if(Integer.parseInt(backNum) < 5 && Integer.parseInt(backNum) > 2){
			birth = "20" + birth.substring(0, 2) + "." + birth.substring(2, 4) + "."  + birth.substring(4, 6);
		} else {
			birth = "19" + birth.substring(0, 2) + "." + birth.substring(2, 4) + "."  + birth.substring(4, 6);
		}
		
		if(Integer.parseInt(backNum) == 2 || Integer.parseInt(backNum) == 4 || Integer.parseInt(backNum) == 6){
			gndr = "F";
		}
		
		Map<String, Object> reqMap = new HashMap<>();
		reqMap.put("usrId", reqVO.getUsrId());
		reqMap.put("usrNm", reqVO.getUsrNm());
		reqMap.put("usrNmId", reqVO.getUsrNmId());
		reqMap.put("gndr", gndr);
		reqMap.put("birth", birth);
		reqMap.put("key", ENC_KEY);
		
		return userDao.updateUsrCertify(reqMap);
	}

	public int updateLogoutV7(String userKey) {
		return userDao.updateLogout(userKey);
	}

	public int updateLogout(String userKey) {
		Map<String, Object> reqMap = new HashMap<>();
		reqMap.put("usrId", userKey);
		
		int res = userDao.insertUserUnknown(reqMap);
		if(res > 0){
			userDao.updateLogout(userKey);
		}
		int unKey = MapUtils.getIntValue(reqMap, "unKey", 0);
		return unKey; 
	}

	@Cacheable(cacheName="serverSt", keyGenerator=@KeyGenerator(name="StringCacheKeyGenerator"))
	public Map<String, Object> getVersionInfo(String os) {
		return userDao.getVersionInfo(os);
	}

	public void insertErrLog(Exception e, HttpServletRequest req, Object reqVO, String userKey) throws Exception {
		
		Map<String, Object> errMap = new HashMap<>();
		errMap.put("url", "app:" + req.getRequestURI());
		errMap.put("req", reqVO == null ? null : reqVO.toString());
		errMap.put("msg", e.getMessage());
		errMap.put("usrId", userKey);
		
		userDao.insertErrLog(errMap);
	
		if(SERVER_TYPE.equals("USER")){
			String mngMsg = "서버 에러 발생 : " + req.getRequestURI();
			
			String url = Constant.INFO_BANK_URL_REAL
					+ "?id=" + IB_SERVICE_ID
					+ "&pwd=" + IB_SERVICE_PWD
					+ "&message=" + URLEncoder.encode(mngMsg, "UTF-8")
					+ "&from=" + IB_FROM_TEL
					+ "&to_country=82"
					+ "&to=" + "1032255078"
					+ "&report_req=1";
			
			String resSms = SMSUtil.sendSms(url);
		}
	}
	
	/**
	 * @param req
	 * @param res
	 * 협업 체크
	 * @return
	 */
	public TokenValues checkCopToken(HttpServletRequest req, HttpServletResponse res, ResValue resultVal) {

		TokenValues result = new TokenValues();
		try{
			// 토큰 인증 
			result = TokenHandler.getTokenValues(req, res);
			String token = (String) req.getAttribute("HeadToken");
			// 해당 토큰 관련된 유저가 있는지 확인, 유저키 불러와서 토큰에 있는 유저키와 맞는지 비교. 
			int tokenCnt = userDao.selectCheckCopToken(token);
			
			if(tokenCnt == 0){
				resultVal.setResCode(ErrCode.INVALID_TOKEN);
				resultVal.setResMsg("인증되지 않은 사용자입니다.");
				return null;
			}
		}catch (Exception e) {
			resultVal.setResCode(ErrCode.UNKNOWN_ERROR);
			resultVal.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
			return null;
		}
		
		return result;
	}

	public Map<String, Object> getUsrChannelInfo(Map<String, Object> reqMap) {
		
		String international = MapUtils.getString(reqMap, "tel");
		StringTokenizer st = new StringTokenizer(international, "-");
		
		String nation = "";
		String tel = "";
		int index = 0;
		while(st.hasMoreTokens()){
			if(index == 0){
				nation = st.nextToken();
			} else {
				tel = tel + st.nextToken();
			}
			index = index + 1;
		}
		nation = nation.replace("+", "").trim();
		if(nation.equals("82")){
			tel = "0" + tel;
		}
		reqMap.put("nation", nation);
		reqMap.put("tel", tel.trim());
		reqMap.put("encKey", ENC_KEY);
		
		Map<String, Object> usrInfo = userDao.getUsrChannelInfo(reqMap);
		if(usrInfo != null){
			int usrId = MapUtils.getIntValue(usrInfo, "USR_ID");
			reqMap.put("usrId", usrId);
		}
		
		Map<String, Object> rsvInfo = userDao.selectRsvChannelInfo(reqMap);
		
		if(rsvInfo == null){
			if(usrInfo == null){
				return new HashMap<>();
			} else {
				usrInfo.remove("USR_ID");
				return usrInfo;
			}
		} else {
			if(usrInfo != null){
				String usrNm = MapUtils.getString(usrInfo, "USR_NM");
				rsvInfo.put("usrNm", usrNm);
			}
		}
		
		return rsvInfo;
	}

	public Map<String, Object> updateUnknown(UserDetailVO reqVO) {

		if(reqVO.getUnKey() == 0){
			userDao.insertUnknown(reqVO);
		} else {
			userDao.updateUnknown(reqVO);
		}
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("unKey", reqVO.getUnKey());
		resultMap.put("usrAk", "unknown");
		
		return resultMap;
	}

	public Map<String, Object> selectTutorial() {
		return userDao.selectTutorial();
	}

	public Map<String, Object> updateFrdCd() {
		
		List<Integer> usrList = userDao.selectUsrList();
		
		for(int usrId : usrList){

			String frdCd = "";
			while(true){
				frdCd = Utils.makeCode(6);
				int frdCnt = joinDao.checkFrdCd(frdCd);
				if(frdCnt == 0){
					break;
				}
			}
			
			Map<String, Object> reqMap = new HashMap<>();
			reqMap.put("usrId", usrId);
			reqMap.put("frdCd", frdCd);
			userDao.updateUsrFrdCd(reqMap);
		}
		
		return null;
	}

	public Map<String, Object> insertNewCoupon() {
		
		String code = Constant.JOIN_COUPON_CODE;
		List<Integer> couponList = joinDao.selectJoinCouponList(code);
		
		List<Integer> usrList = userDao.selectUsrNewCoupon(code);
		
		for(int usrId : usrList){
			for(int couponId : couponList){
				Map<String, Object> joinMap = new HashMap<>();
				joinMap.put("usrId", usrId);
				joinMap.put("couponId", couponId);
				joinMap.put("dueDt", Utils.getDiffDate(30));
				userDao.insertJoinCoupon(joinMap);
			}
		}
		
		return null;
	}

	public void insertApiLog(Map<String, Object> reqMap) {
		userDao.insertApiLog(reqMap);
	}
}
