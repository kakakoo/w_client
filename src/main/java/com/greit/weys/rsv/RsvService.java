package com.greit.weys.rsv;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.collections.MapUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.KeyGenerator;
import com.greit.weys.ResValue;
import com.greit.weys.common.Constant;
import com.greit.weys.common.ErrCode;
import com.greit.weys.common.IdentifyUtils;
import com.greit.weys.common.KakaoClient;
import com.greit.weys.common.PayGateUtil;
import com.greit.weys.common.PaygateVO;
import com.greit.weys.common.Utils;
import com.greit.weys.config.TokenValues;
import com.greit.weys.mail.EmailVO;
import com.greit.weys.mail.Mailer;
import com.greit.weys.push.PushService;
import com.greit.weys.user.UserDao;
import com.greit.weys.user.UsrVO;

@Service
public class RsvService {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private RsvDao rsvDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private EmailVO emailVO;
	@Autowired
	private Mailer mailer;

	@Value("#{props['RSV.MAX']}")
	private int RSV_MAX;
	@Value("#{props['ENC.KEY']}")
	private String ENC_KEY;
	@Value("#{props['EMAIL.ID']}")
	private String EMAIL_ID;

	// 나이스 평가모듈 
	@Value("#{props['NAME.CHECK.CODE']}")
	private String NAME_CHECK_CODE;
	@Value("#{props['NAME.CHECK.PWD']}")
	private String NAME_CHECK_PWD;

	@Value("${SERVER.PATH}")
	private String SERVER_PATH;
	@Value("${SERVER.TYPE}")
	private String SERVER_TYPE;

	//알림톡
	@Value("#{props['IB.FROM.TEL']}")
	private String IB_FROM_TEL;
	@Value("#{props['IB.KAKAO.ID']}")
	private String IB_KAKAO_ID;
	@Value("#{props['IB.KAKAO.PWD']}")
	private String IB_KAKAO_PWD;
	@Value("#{props['IB.KAKAO.SENDER.KEY']}")
	private String IB_KAKAO_SENDER_KEY;

	// FCML
	@Value("#{props['FCM.SERVER.KEY']}")
	private String FCM_SERVER_KEY; // FCM 서버 키
	@Value("#{props['FCM.SEND.URL']}")
	private String FCM_SEND_URL; // FCM 발송 URL
	
	// FCML
	@Value("#{props['mode']}")
	private String SERVER_MODE; // FCM 서버 키
	
	//PAYGATE
	@Value("${PAY.GATE.GUID}")
	private String GUID;
	@Value("${PAY.GATE.KEY}")
	private String KEY_P;
	@Value("${PAY.GATE.URL}")
	private String API_URL;

	public Map<String, Object> selectRsvInfo() {
		/**
		 * 서버에서 예약 가능한지 확인
		 */
		String serverSt = rsvDao.selectRsvSt("R");

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("serverSt", serverSt);
		return resultMap;
	}

	public Map<String, Object> selectBonusList(int usrId, String unitCd) {
		
		List<Map<String, Object>> resultList = new ArrayList<>();
		
		if(usrId > 0){
			Map<String, Object> reqMap = new HashMap<>();
			reqMap.put("usrId", usrId);
			reqMap.put("unitCd", unitCd);

			List<Map<String, Object>> bonusList = rsvDao.selectEventBonus(reqMap);
			if(bonusList != null)
				resultList = bonusList;
			
//			if(unitCd.equals("JPY") || unitCd.equals("USD")){
//				Map<String, Object> usrBonus = rsvDao.selectUserBonus(usrId);
//				if(usrBonus != null){
//					usrBonus.put("couponImg", "coupon/1553475056332.png");
//					resultList.add(0, usrBonus);
//				}
//			}
		}
//		else {
//			Map<String, Object> unknownMap = rsvDao.selectUnknownEvent(unitCd);
//			if(unknownMap != null){
//				resultList.add(unknownMap);
//			}
//		}

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("bonusList", resultList);
		
		return resultMap;
	}

	public Map<String, Object> selectBonusListV9(int usrId, String unitCd) {
		
		List<Map<String, Object>> resultList = new ArrayList<>();
		
		if(usrId > 0){
			Map<String, Object> reqMap = new HashMap<>();
			reqMap.put("usrId", usrId);
			reqMap.put("unitCd", unitCd);

			List<Map<String, Object>> bonusList = rsvDao.selectEventBonus(reqMap);
			if(bonusList != null)
				resultList = bonusList;
			
			if(unitCd.equals("JPY") || unitCd.equals("USD")){
				Map<String, Object> usrBonus = rsvDao.selectUserBonus(usrId);
				if(usrBonus != null){
					resultList.add(0, usrBonus);
				}
			}
		} else {
			Map<String, Object> unknownMap = rsvDao.selectUnknownEvent(unitCd);
			if(unknownMap != null){
				resultList.add(unknownMap);
			}
		}

		if(resultList.size() == 0){
			Map<String, Object> emptyMap = new HashMap<>();
			emptyMap.put("bonusId", 0);
			emptyMap.put("weysBonus", 0);
			emptyMap.put("bonusNm", "쿠폰없음");
			resultList.add(emptyMap);
		} else {
			Map<String, Object> emptyMap = new HashMap<>();
			emptyMap.put("bonusId", 0);
			emptyMap.put("weysBonus", 0);
			emptyMap.put("bonusNm", "쿠폰 선택 안함");
			resultList.add(0, emptyMap);
		}
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("bonusList", resultList);
		
		return resultMap;
	}

	public Map<String, Object> selectRsvUnit(int userKey) {
		
		Map<String, Object> reqMap = new HashMap<>();
		reqMap.put("usrId", userKey);
		reqMap.put("dt", Utils.getDiffDate(0));
		
		/**
		 * 사용 가능 한도
		 */
		int usdUsed = 0;
		int otherUsed = 0;
		if(userKey != 0){
			List<Map<String, Object>> limitMap = rsvDao.selectRsvTodayHist(reqMap);
			for(Map<String, Object> tmp : limitMap){
				String unit = MapUtils.getString(tmp, "UNIT");
				int rsvAmnt = MapUtils.getIntValue(tmp, "RSV_AMNT");
				if(unit.equals("USD")){
					usdUsed += rsvAmnt;
				} else {
					double basicRate = MapUtils.getDoubleValue(tmp, "BASIC_RATE_WEYS");
					int amount = (int) (rsvAmnt * basicRate);
					if(unit.equals("JPY")){
						amount = amount / 100;
					}
					otherUsed += amount;
				}
			}
		}

		/**
		 * 지점의 사용 가능 화폐 및 해당 화폐의 상태
		 */
		List<RsvUnitVO> unitList = rsvDao.selectRsvUnit();
		double USD_BASIC_USER = 0.0;
		
		for (RsvUnitVO temp : unitList) {
			
			if(temp.getUnitCd().equals("USD")){
				USD_BASIC_USER = temp.getBasicRateWeys();
			}
			
			
			if (temp.getRsvSt().equals("Y")) {
				
				double airCommis = temp.getAirCommis();
				double basicRate = temp.getBasicRateWeys();
				
				double basicRateAir = basicRate + (basicRate * airCommis * 0.01);
				temp.setBasicRateAir(Double.parseDouble(String.format("%.2f", basicRateAir)));
				
				/**
				 * 사용 가능 보너스
				 */
				int usrBonus = 0;
				reqMap.put("unitCd", temp.getUnitCd());
				List<Map<String, Object>> bonusList = rsvDao.selectEventBonus(reqMap);
				if(bonusList.size() > 0){
					Map<String, Object> bonusMap = bonusList.get(0);
					usrBonus = MapUtils.getIntValue(bonusMap, "weysBonus", 0);
				}
				temp.setUsrBonus(usrBonus);
				
				/**
				 * 해당 화폐 최대치 구하기
				 * 기준 : USD 2,000달러
				 */
				int unitMax = 0;
				int limitAmnt = 0;
				if(temp.getUnitCd().equals("USD")){
					unitMax = RSV_MAX;
					limitAmnt = RSV_MAX - usdUsed;
					if(otherUsed > 0){
						int otherUSD = (int) (otherUsed / USD_BASIC_USER);
						limitAmnt = limitAmnt - otherUSD;
					}
				} else {
					unitMax = (int) (RSV_MAX * USD_BASIC_USER / basicRate);
					if(temp.getUnitCd().equals("JPY")){
						unitMax = unitMax * 100;
					}
					/**
					 * 페소는 최대 100만원선 까지만
					 */
					String halfSt = rsvDao.selectUnitHalf(temp.getUnitCd());
					if(halfSt.equals("Y")){
						unitMax = unitMax / 2;
					}
					
					unitMax = (unitMax / temp.getUnitSize()) * temp.getUnitSize();
					
					limitAmnt = unitMax;
					
					int otherAmnt = 0;
					if(usdUsed > 0){
						otherAmnt = (int) (usdUsed * USD_BASIC_USER / basicRate);
						if(temp.getUnitCd().equals("JPY")){
							otherAmnt = otherAmnt * 100;
						}
						limitAmnt = limitAmnt - otherAmnt;
					}
					if(otherUsed > 0){
						otherAmnt = (int) (otherUsed / basicRate);
						if(temp.getUnitCd().equals("JPY")){
							otherAmnt = otherAmnt * 100;
						}
						limitAmnt = limitAmnt - otherAmnt;
					}

					limitAmnt = (limitAmnt / temp.getUnitSize()) * temp.getUnitSize();
				}
				if(limitAmnt < 0){
					limitAmnt = 0;
				}
				
				temp.setLimitAmnt(limitAmnt);
				temp.setUnitMax(unitMax);
			}
		}

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("dataList", unitList);
		return resultMap;
	}

	public Map<String, Object> selectRsvUnitStore(String unitCd, String usrId) throws Exception {
		/**
		 * 해당 사용자의 한도 확인
		 */
		Map<String, Object> reqMap = new HashMap<>();
		reqMap.put("usrId", usrId);
		reqMap.put("encKey", ENC_KEY);
		Map<String, Object> resultMap = rsvDao.selectRsvUsrInfo(reqMap);
		
		/**
		 * 예약 가능한 지점 리스트 STORE_ST = 'Y' AND STORE_RSV = 'Y'
		 */
		List<RsvStoreVO> resultList = rsvDao.selectUnitStoreList(unitCd);
		
		/**
		 * 권종 옵션
		 */
		List<Map<String, Object>> paperList = rsvDao.selectPaperList(unitCd);
		if(paperList == null)
			paperList = new ArrayList<>();
		resultMap.put("paperList", paperList);

		/**
		 * 오늘 요일 계산.
		 */
		int dt = Utils.getTdayDay(null);

		for (RsvStoreVO temp : resultList) {
			
			String nm = temp.getStoreNm();
			nm = nm.replace("한진택배 ", "");
			temp.setStoreNm(nm);
			
			int storeId = temp.getStoreId();

			/**
			 * 당일예약 가능여부 확인
			 * 24시간 지점여부 확인
			 */
			Map<String, Object> subInfo = rsvDao.selectCheckTodayRsv(storeId);
			String todayRsv = MapUtils.getString(subInfo, "TODAY_RSV");
			String allDaySt = MapUtils.getString(subInfo, "ALL_DAY_ST");
			
			/**
			 * 예약가능한 요일을 구해 해당 요일의 시작시간과 끝시간 구하기 해당 날짜에 쉬는날이 포함되어 있는지 확인
			 */
			List<Map<String, Object>> rsvData = new ArrayList<>();

			int index = 0;
			int i = 0;
			
			int before = 0;
			int after = 0;
			
			int dayCnt = Constant.RSV_DATE;
			
			while (index < dayCnt || after < 2) {
				reqMap = new HashMap<>();
				reqMap.put("storeId", storeId);

				int val = dt + 1 + i;
				if(todayRsv.equals("Y")){
					val = dt + i;
				}

				String rsvDt = Utils.getDiffDate(val - dt);
				
				if (val > 6){
					val = val % 7;
				}
				reqMap.put("day", val);
				reqMap.put("closeDt", rsvDt);
				String closeDt = rsvDao.selectCheckClose(reqMap);
				boolean holiday = false;
				if (closeDt != null) {
					holiday = true;
				}

				Map<String, Object> timeMap = rsvDao.selectRsvStoreTime(reqMap);
				String openSt = MapUtils.getString(timeMap, "OPEN_ST");
				if(openSt.equals("N")){
					holiday = true;
				}
				
				/**
				 * 당일 배송 현재시간 + 2시간 이후 부터 예약 가능
				 * 마감 2시간전 이후로는 예약 불가
				 * 당일 예약은 오전 9시부터 가능
				 */
				String startTm = MapUtils.getString(timeMap, "startTm");
				String endTm = MapUtils.getString(timeMap, "endTm");
				if(i == 0 && todayRsv.equals("Y")){
					startTm = "09:00";
					String st = rsvDt + " " + startTm;
					String et = rsvDt + " " + endTm;
					
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.HOUR, 2);
					cal.add(Calendar.MINUTE, 15);
					Date d = cal.getTime();

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.KOREA);
					String date = sdf.format(d);

					long diffSt = Utils.diffTwoMinutes(st, date);
					long diffEt = Utils.diffTwoMinutes(et, date);

					if(diffSt < 0 && diffEt > 0){
						diffSt = diffSt * -1;
						
						int m = (int) (diffSt / 15);
						m = m * 15;
						
						cal = Calendar.getInstance();
						cal.setTime(sdf.parse(st));
						cal.add(Calendar.MINUTE, m);
						Date Nd = cal.getTime();
						
						SimpleDateFormat ndf = new SimpleDateFormat("HH:mm", Locale.KOREA);
						startTm = ndf.format(Nd);
					} else if (diffEt < 0) {
						holiday = true;
					}
				}
				
				/**
				 * 24시간 지점일 경우 첫 시작은 5시부터
				 */
				if(i == 0 && allDaySt.equals("Y") && storeId == 10){
					startTm = "05:00";
				}

				if(index == 0 && holiday){
					before = before + 1;
				}
				if(index == dayCnt){
					holiday = true;
					after = after + 1;
				}
				if(!holiday && index < dayCnt){
					index = index + 1;
				}
				
				Map<String, Object> resMap = new HashMap<>();
				resMap.put("rsvYear", rsvDt.substring(0, 4));
				resMap.put("rsvDt", rsvDt.substring(5).replace(".", "/"));
				resMap.put("holiday", holiday);
				resMap.put("rsvDay", val);
				resMap.put("startTm", startTm);
				resMap.put("endTm", endTm);
				rsvData.add(resMap);
				i = i + 1;
				
			}
			
			if(before > 2){
				for(int x=0 ; x < before - 2 ; x++){
					rsvData.remove(0);
				}
			} else if (before < 2){

				Map<String, Object> resMap = rsvData.get(0);
				String year = MapUtils.getString(resMap, "rsvYear");
				String dat = MapUtils.getString(resMap, "rsvDt");
				String [] sDat = dat.split("/");
				int day = MapUtils.getIntValue(resMap, "rsvDay");
				
				int minus = 0;
				for(int x=0 ; x < 2 - before ; x++){
					minus = minus + 1;
					String thatDay = Utils.getDateFormat( 0 - minus, Integer.parseInt(year), Integer.parseInt(sDat[0]) - 1, Integer.parseInt(sDat[1]));

					String [] sTat = thatDay.split("\\.");
					year = sTat[0];
					String sMonth = sTat[1];
					String sDay = sTat[2];
					int beforeDay = day - minus;
					beforeDay = beforeDay < 0 ? 6 : beforeDay;

					Map<String, Object> beforeMap = new HashMap<>();
					beforeMap.put("rsvYear", year);
					beforeMap.put("rsvDt", sMonth + "/" + sDay);
					beforeMap.put("holiday", true);
					beforeMap.put("rsvDay", beforeDay);
					beforeMap.put("startTm", "");
					beforeMap.put("endTm", "");
					rsvData.add(0, beforeMap);
				}
			}

			temp.setRsvDate(rsvData);
			
			List<Map<String, Object>> breakList = rsvDao.selectBreakTime(storeId);
			temp.setBreakList(breakList);
		}
		List<String> deliverUnit = rsvDao.selectDeliverUnit();
		resultMap.put("dataList", resultList);
		resultMap.put("deliverUnit", deliverUnit);
		
		/**
		 * 주소지배송여부 및 편의점 여부, 가상계좌 여부
		 * SERVER_ST C, V 
		 */
		String deliverSt = rsvDao.selectRsvSt("L");
		String deliverDesc = rsvDao.selectRsvStDesc("L");
		String convieSt = rsvDao.selectRsvSt("C");
		String vbankSt = rsvDao.selectRsvSt("V");

		resultMap.put("deliverSt", deliverSt);
		resultMap.put("deliverDesc", deliverDesc);
		resultMap.put("convieSt", convieSt);
		resultMap.put("vbankSt", vbankSt);
		
		/**
		 * 지점 안내 URL
		 */
		String url = rsvDao.selectStoreInfo();
		resultMap.put("storeInfo", url);
		
		return resultMap;
	}

	public Map<String, Object> selectRsvUnitStoreV7(String unitCd, String usrId) throws Exception {
		/**
		 * 해당 사용자의 한도 확인
		 */
		Map<String, Object> reqMap = new HashMap<>();
		reqMap.put("usrId", usrId);
		reqMap.put("encKey", ENC_KEY);
		Map<String, Object> resultMap = rsvDao.selectRsvUsrInfo(reqMap);
		
		/**
		 * 예약 가능한 지점 리스트 STORE_ST = 'Y' AND STORE_RSV = 'Y'
		 */
		List<RsvStoreVO> resultList = rsvDao.selectUnitStoreList(unitCd);

		/**
		 * 오늘 요일 계산.
		 */
		int dt = Utils.getTdayDay(null);

		for (RsvStoreVO temp : resultList) {
			int storeId = temp.getStoreId();

			/**
			 * 예약가능한 요일을 구해 해당 요일의 시작시간과 끝시간 구하기 해당 날짜에 쉬는날이 포함되어 있는지 확인
			 */
			List<Map<String, Object>> rsvData = new ArrayList<>();

			int index = 0;
			int i = 0;
			
			int before = 0;
			int after = 0;
			
			int dayCnt = Constant.RSV_DATE;
			
			while (index < dayCnt || after < 2) {
				reqMap = new HashMap<>();
				reqMap.put("storeId", storeId);

				int val = dt + 1 + i;
				
				String rsvDt = Utils.getDiffDate(val - dt);

				if (val > 6){
					val = val % 7;
				}
				reqMap.put("day", val);
				reqMap.put("closeDt", rsvDt);
				String closeDt = rsvDao.selectCheckClose(reqMap);
				boolean holiday = false;
				if (closeDt != null) {
					holiday = true;
				}

				Map<String, Object> timeMap = rsvDao.selectRsvStoreTime(reqMap);
				String openSt = MapUtils.getString(timeMap, "OPEN_ST");
				if(openSt.equals("N")){
					holiday = true;
				}
				
				/**
				 * 당일 배송 현재시간 + 2시간 이후 부터 예약 가능
				 * 마감 2시간전 이후로는 예약 불가
				 * 당일 예약은 오전 9시부터 가능
				 */
				String startTm = MapUtils.getString(timeMap, "startTm");
				String endTm = MapUtils.getString(timeMap, "endTm");

				if(index == 0 && holiday){
					before = before + 1;
				}
				if(index == dayCnt){
					holiday = true;
					after = after + 1;
				}
				if(!holiday && index < dayCnt){
					index = index + 1;
				}
				
				Map<String, Object> resMap = new HashMap<>();
				resMap.put("rsvYear", rsvDt.substring(0, 4));
				resMap.put("rsvDt", rsvDt.substring(5).replace(".", "/"));
				resMap.put("holiday", holiday);
				resMap.put("rsvDay", val);
				resMap.put("startTm", startTm);
				resMap.put("endTm", endTm);
				rsvData.add(resMap);
				i = i + 1;
				
			}
			
			if(before > 2){
				for(int x=0 ; x < before - 2 ; x++){
					rsvData.remove(0);
				}
			} else if (before < 2){

				Map<String, Object> resMap = rsvData.get(0);
				String year = MapUtils.getString(resMap, "rsvYear");
				String dat = MapUtils.getString(resMap, "rsvDt");
				String [] sDat = dat.split("/");
				int day = MapUtils.getIntValue(resMap, "rsvDay");
				
				int minus = 0;
				for(int x=0 ; x < 2 - before ; x++){
					minus = minus + 1;
					String thatDay = Utils.getDateFormat( 0 - minus, Integer.parseInt(year), Integer.parseInt(sDat[0]) - 1, Integer.parseInt(sDat[1]));

					String [] sTat = thatDay.split("\\.");
					year = sTat[0];
					String sMonth = sTat[1];
					String sDay = sTat[2];
					int beforeDay = day - minus;
					beforeDay = beforeDay < 0 ? 6 : beforeDay;

					Map<String, Object> beforeMap = new HashMap<>();
					beforeMap.put("rsvYear", year);
					beforeMap.put("rsvDt", sMonth + "/" + sDay);
					beforeMap.put("holiday", true);
					beforeMap.put("rsvDay", beforeDay);
					beforeMap.put("startTm", "");
					beforeMap.put("endTm", "");
					rsvData.add(0, beforeMap);
				}
			}

			temp.setRsvDate(rsvData);
		}
		List<String> deliverUnit = rsvDao.selectDeliverUnit();
		resultMap.put("dataList", resultList);
		resultMap.put("deliverUnit", deliverUnit);
		return resultMap;
	}

	public Map<String, Object> selectDeliverUnitStore(String unitCd, String userKey) throws Exception {
		/**
		 * 예약 가능한 지점 리스트 STORE_ST = 'Y' AND DELIVER_ST = 'Y'
		 */
		List<RsvStoreVO> resultList = rsvDao.selectUnitDeliverList(unitCd);

		/**
		 * 오늘 요일 계산.
		 */
		int dt = Utils.getTdayDay(null);

		for (RsvStoreVO temp : resultList) {
			int storeId = temp.getStoreId();

			/**
			 * 당일예약 가능여부 확인
			 */
			String todayDeliver = rsvDao.selectCheckTodayDeliver(storeId);
			
			/**
			 * 예약가능한 요일을 구해 해당 요일의 시작시간과 끝시간 구하기 해당 날짜에 쉬는날이 포함되어 있는지 확인
			 * 당일 배송은 7,700원
			 * 평일 근무시간은 5,500원
			 * 평일 저녁시간은 6,600원
			 */
			List<Map<String, Object>> rsvData = new ArrayList<>();

			int index = 0;
			int i = 0;
			
			int before = 0;
			int after = 0;
			
			int dayCnt = 2;
			
			while (index < dayCnt || after < 2) {
				Map<String, Object> reqMap = new HashMap<>();
				reqMap.put("storeId", storeId);

				/**
				 * 당일 배송 추가
				 * 현재시간 + 2시간 이후 부터 예약 가능
				 * 마감 2시간전 이후로는 예약 불가
				 */
				int val = dt + 1 + i;
				if(todayDeliver.equals("Y")){
					val = dt + i;
				}

				String rsvDt = Utils.getDiffDate(val - dt);

				if (val > 6){
					val = val % 7;
				}
				reqMap.put("day", val);
				reqMap.put("closeDt", rsvDt);
				boolean holiday = false;

				Map<String, Object> timeMap = rsvDao.selectRsvDeliverTime(reqMap);
				String openSt = MapUtils.getString(timeMap, "OPEN_ST");
				if(openSt.equals("N")){
					holiday = true;
				}
				
				/**
				 * 당일 배송 현재시간 + 2시간 이후 부터 예약 가능
				 * 마감 2시간전 이후로는 예약 불가
				 */
				String startTm = MapUtils.getString(timeMap, "startTm");
				String endTm = MapUtils.getString(timeMap, "endTm");
//				if(i == 0){
//					String st = rsvDt + " " + startTm;
//					String et = rsvDt + " " + endTm;
//					
//					Calendar cal = Calendar.getInstance();
//					cal.add(Calendar.HOUR, 2);
//					Date d = cal.getTime();
//
//					SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.KOREA);
//					String date = sdf.format(d);
//
//					long diffSt = Utils.diffTwoMinutes(st, date);
//					long diffEt = Utils.diffTwoMinutes(et, date);
//
//					if(diffSt < 0 && diffEt > 0){
//						diffSt = diffSt * -1;
//						
//						int m = (int) (diffSt / 120) + 1;
//						m = m * 120;
//						
//						cal = Calendar.getInstance();
//						cal.setTime(sdf.parse(st));
//						cal.add(Calendar.MINUTE, m);
//						Date Nd = cal.getTime();
//						
//						SimpleDateFormat ndf = new SimpleDateFormat("HH:mm", Locale.KOREA);
//						startTm = ndf.format(Nd);
//						
//						if(startTm.equals(endTm)){
//							holiday = true;
//						}
//					} else if(diffEt < 0){
//						holiday = true;
//					}
//				}

				Map<String, Object> resMap = new HashMap<>();
				resMap.put("rsvYear", rsvDt.substring(0, 4));
				resMap.put("rsvDt", rsvDt.substring(5).replace(".", "/"));
				resMap.put("rsvDay", val);
				resMap.put("startTm", startTm);
				resMap.put("endTm", endTm);
				
				int addCost = MapUtils.getInteger(timeMap, "ADD_COST");
				String addTm = MapUtils.getString(timeMap, "ADD_TM");

				String deliverHoliday = rsvDao.selectCheckCloseDeliver(reqMap);
				if(deliverHoliday != null){
					if (deliverHoliday.equals("Y")) {
						holiday = true;
					} else {
						addTm = MapUtils.getString(timeMap, "startTm");
//						addCost = 2200;
					}
				}
				
				if(index == 0 && holiday){
					before = before + 1;
				}
				if(index == dayCnt){
					holiday = true;
					after = after + 1;
				}
				if((!holiday) && index < dayCnt){
					index = index + 1;
				}
				
				
				/**
				 * 당일 배송은 7,700원 
				 */
//				if(i == 0){
//					addTm = startTm;
//					addCost = 2200;
//				}

				resMap.put("holiday", holiday);
				resMap.put("addCost", addCost);
				resMap.put("addTm", addTm);
				rsvData.add(resMap);
				i = i + 1;
			}
			
			if(before > 2){
				for(int x=0 ; x < before - 2 ; x++){
					rsvData.remove(0);
				}
			} else if (before < 2){

				Map<String, Object> resMap = rsvData.get(0);
				String year = MapUtils.getString(resMap, "rsvYear");
				String dat = MapUtils.getString(resMap, "rsvDt");
				String [] sDat = dat.split("/");
				int day = MapUtils.getIntValue(resMap, "rsvDay");
				
				int minus = 0;
				for(int x=0 ; x < 2 - before ; x++){
					minus = minus + 1;
					String thatDay = Utils.getDateFormat( 0 - minus, Integer.parseInt(year), Integer.parseInt(sDat[0]) - 1, Integer.parseInt(sDat[1]));

					String [] sTat = thatDay.split("\\.");
					year = sTat[0];
					String sMonth = sTat[1];
					String sDay = sTat[2];
					int beforeDay = day - minus;
					beforeDay = beforeDay < 0 ? 6 : beforeDay;

					Map<String, Object> beforeMap = new HashMap<>();
					beforeMap.put("rsvYear", year);
					beforeMap.put("rsvDt", sMonth + "/" + sDay);
					beforeMap.put("holiday", true);
					beforeMap.put("rsvDay", beforeDay);
					beforeMap.put("startTm", "");
					beforeMap.put("endTm", "");
					beforeMap.put("addCost", 0);
					beforeMap.put("addTm", "");
					rsvData.add(0, beforeMap);
				}
			}

			temp.setRsvDate(rsvData);
		}
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("dataList", resultList);
		return resultMap;
	}

	public Map<String, Object> selectDeliverUnitStoreV7(String unitCd, String userKey) throws Exception {
		/**
		 * 예약 가능한 지점 리스트 STORE_ST = 'Y' AND DELIVER_ST = 'Y'
		 */
		List<RsvStoreVO> resultList = rsvDao.selectUnitDeliverList(unitCd);

		/**
		 * 오늘 요일 계산.
		 */
		int dt = Utils.getTdayDay(null);

		for (RsvStoreVO temp : resultList) {
			int storeId = temp.getStoreId();

			/**
			 * 예약가능한 요일을 구해 해당 요일의 시작시간과 끝시간 구하기 해당 날짜에 쉬는날이 포함되어 있는지 확인
			 * 평일 근무시간은 5,500원
			 * 평일 저녁시간은 6,600원
			 */
			List<Map<String, Object>> rsvData = new ArrayList<>();

			int index = 0;
			int i = 0;
			
			int before = 0;
			int after = 0;
			
			int dayCnt = 2;
			
			while (index < dayCnt || after < 2) {
				Map<String, Object> reqMap = new HashMap<>();
				reqMap.put("storeId", storeId);

				/**
				 * 당일 배송 추가
				 * 현재시간 + 2시간 이후 부터 예약 가능
				 * 마감 2시간전 이후로는 예약 불가
				 */
				int val = dt + 1 + i;

				String rsvDt = Utils.getDiffDate(val - dt);

				if (val > 6){
					val = val % 7;
				}
				reqMap.put("day", val);
				reqMap.put("closeDt", rsvDt);
				boolean holiday = false;

				Map<String, Object> timeMap = rsvDao.selectRsvDeliverTime(reqMap);
				String openSt = MapUtils.getString(timeMap, "OPEN_ST");
				if(openSt.equals("N")){
					holiday = true;
				}
				
				String startTm = MapUtils.getString(timeMap, "startTm");
				String endTm = MapUtils.getString(timeMap, "endTm");

				if(index == 0 && holiday){
					before = before + 1;
				}
				if(index == dayCnt){
					holiday = true;
					after = after + 1;
				}
				if(!holiday && index < dayCnt){
					index = index + 1;
				}
				
				Map<String, Object> resMap = new HashMap<>();
				resMap.put("rsvYear", rsvDt.substring(0, 4));
				resMap.put("rsvDt", rsvDt.substring(5).replace(".", "/"));
				resMap.put("rsvDay", val);
				resMap.put("startTm", startTm);
				resMap.put("endTm", endTm);
				
				int addCost = MapUtils.getInteger(timeMap, "ADD_COST");
				String addTm = MapUtils.getString(timeMap, "ADD_TM");

				String deliverHoliday = rsvDao.selectCheckCloseDeliver(reqMap);
				if(deliverHoliday != null){
					if (deliverHoliday.equals("Y")) {
						holiday = true;
					} else {
						addTm = MapUtils.getString(timeMap, "startTm");
//						addCost = 2200;
					}
				}

				resMap.put("holiday", holiday);
				resMap.put("addCost", addCost);
				resMap.put("addTm", addTm);
				rsvData.add(resMap);
				i = i + 1;
			}
			
			if(before > 2){
				for(int x=0 ; x < before - 2 ; x++){
					rsvData.remove(0);
				}
			} else if (before < 2){

				Map<String, Object> resMap = rsvData.get(0);
				String year = MapUtils.getString(resMap, "rsvYear");
				String dat = MapUtils.getString(resMap, "rsvDt");
				String [] sDat = dat.split("/");
				int day = MapUtils.getIntValue(resMap, "rsvDay");
				
				int minus = 0;
				for(int x=0 ; x < 2 - before ; x++){
					minus = minus + 1;
					String thatDay = Utils.getDateFormat( 0 - minus, Integer.parseInt(year), Integer.parseInt(sDat[0]) - 1, Integer.parseInt(sDat[1]));

					String [] sTat = thatDay.split("\\.");
					year = sTat[0];
					String sMonth = sTat[1];
					String sDay = sTat[2];
					int beforeDay = day - minus;
					beforeDay = beforeDay < 0 ? 6 : beforeDay;

					Map<String, Object> beforeMap = new HashMap<>();
					beforeMap.put("rsvYear", year);
					beforeMap.put("rsvDt", sMonth + "/" + sDay);
					beforeMap.put("holiday", true);
					beforeMap.put("rsvDay", beforeDay);
					beforeMap.put("startTm", "");
					beforeMap.put("endTm", "");
					beforeMap.put("addCost", 0);
					beforeMap.put("addTm", "");
					rsvData.add(0, beforeMap);
				}
			}

			temp.setRsvDate(rsvData);
		}
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("dataList", resultList);
		return resultMap;
	}

	public RsvInfoVO insertRsv(String userKey, RsvInfoVO reqVO) throws Exception {
//		/**
//		 * JPY(100) 에러 수정
//		 */
//		if(reqVO.getUnitCd().equals("JPY")){
//			String dttm = reqVO.getRateDttm().replace(" JPY(100)", "");
//			reqVO.setRateDttm(dttm);
//		}
//		
//		/**
//		 * 멤버십일 경우
//		 */
//		int weysAmnt = 0;
//		if(reqVO.getRsvAmntWeys() > 0){
//			int memPoint = rsvDao.selectMemberCostAmnt(userKey);
//			weysAmnt = (int) (reqVO.getRsvAmntWeys() * reqVO.getBasicRateWeys());
//			if(reqVO.getUnitCd().equals("JPY")){
//				weysAmnt = weysAmnt / 100;
//			}
//			if(weysAmnt > memPoint){
//				reqVO.setKey("NOT ENOUGH MEMBERSHIP");
//				return reqVO;
//			}
//		}
		
		reqVO.setUsrId(Integer.parseInt(userKey));
		/**
		 * 쿠폰 사용 여부
		 * 사용 안하면 수수료 확인
		 */
		int cms = reqVO.getCms();
		int res = 0;

		if(reqVO.getCouponId() != 0){
//			int cost = rsvDao.selectCouponCost(reqVO.getCouponId());
			res = rsvDao.updateCouponUse(reqVO);
			if(res == 0){
				reqVO.setKey("NOT ENOUGH COUPON");
				return reqVO;
			}
//			cms = cms - cost;
		}

//		reqVO.setCms(cms);

		/**
		 * 예약 수수료 추가
		 */
		int val = reqVO.getGetAmnt() + cms;
		reqVO.setGetAmnt(val);

		reqVO.setKey(ENC_KEY);
		if(reqVO.getRsvNm() == null){
			String rsvNm = rsvDao.selectUsrNm(reqVO);
			reqVO.setRsvNm(rsvNm);
		}

		/**
		 * RSV_NO 생성
		 */
		String rsvNo = "";
		while(true){
			rsvNo = Utils.makeCode(6);
			
			int cnt = rsvDao.selectCheckRsvNo(rsvNo);
			if(cnt == 0)
				break;
		}
		reqVO.setRsvNo(rsvNo);

		double basicRateUser = reqVO.getBasicRateWeys() * reqVO.getWeysCommis() / 100;
		if(reqVO.getWeysBonus() > 0){
			basicRateUser = basicRateUser * (1 - (reqVO.getWeysBonus() / 100));
		}
		basicRateUser = basicRateUser + reqVO.getBasicRateWeys();
		
		reqVO.setBasicRateUser(Double.parseDouble(String.format("%.2f", basicRateUser)));
		reqVO.setRsvSt(Constant.RSV_START);
		
		/**
		 * 가상계좌 개설
		 * 1. 사용자 생성
		 * 2. 가상계좌 생성
		 */
		String type = SERVER_TYPE.equals("USER") ? "RSV" : "TSV";
		String pgUsrKey = type + userKey + "WEYS"; 
		
		String refId = type + "_" + rsvNo;
		
		Map<String, Object> pgMap = new HashMap<>();
		pgMap.put("rsvNm", reqVO.getRsvNm());
		pgMap.put("bankCd", reqVO.getVbankCd());
		pgMap.put("rsvAmnt", reqVO.getGetAmnt());
		pgMap.put("refId", refId);

//		String pgMemGuid = PayGateUtil.createMember(pgUsrKey, reqVO.getRsvNm());
//		Map<String, Object> pgResult = PayGateUtil.createVBank(pgMap, pgMemGuid);
//		String tid = MapUtils.getString(pgResult, "tid");
//		String accntNo = MapUtils.getString(pgResult, "accntNo");

		String tid = pgUsrKey;
		String accntNo = "";
		String bankNm = "";
		String holder = "";
		if(reqVO.getPayTp().equals("V")){
			String memGuid = rsvDao.selectUsrGuid(reqVO.getUsrId());
			Map<String, Object> pgResult = PayGateUtil.createVBank(API_URL, GUID, KEY_P, pgMap, memGuid);
			tid = MapUtils.getString(pgResult, "tid");
			accntNo = MapUtils.getString(pgResult, "accntNo");
			
			if(SERVER_MODE.equals("dev")){
				holder = Constant.PAYGATE_TEST_VBANK_HOLDER;
			} else {
				holder = Constant.PAYGATE_REAL_VBANK_HOLDER;
			}
			
			Map<String, Object> bankMap =  selectBanks("v");
			List<Map<String, Object>> vbankList = (List<Map<String, Object>>) bankMap.get("dataList");
			for(Map<String, Object> bnk : vbankList){
				String vbankCd = MapUtils.getString(bnk, "bankCd");
				if(reqVO.getVbankCd().equals(vbankCd)){
					bankNm = MapUtils.getString(bnk, "bankNm");
				}
			}
		} else {
			List<Map<String, Object>> vbankList = rsvDao.selectVBanks();
			for(Map<String, Object> bnk : vbankList){
				String vbankCd = MapUtils.getString(bnk, "bankCd");
				
				if(reqVO.getVbankCd().equals(vbankCd)){
					bankNm = MapUtils.getString(bnk, "bankNm");
					accntNo = MapUtils.getString(bnk, "accntNo");
					holder = MapUtils.getString(bnk, "holder");
					break;
				}
			}
		}
		
		
		/**
		 * 예약하기 등록
		 */
		Date date = new Date(Utils.getAfter30Min(30) * 1000);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.KOREA);

		reqVO.setRefId(refId);			
		reqVO.setTid(tid);					
		reqVO.setVbankCd(accntNo);
		reqVO.setVbankNm(bankNm);
		reqVO.setVbankHolder(holder);
		reqVO.setVbankDue(sdf.format(date));
		
		res = rsvDao.insertRsvInfo(reqVO);
		
		if(res == 0){
			throw new Exception("예약 등록 실패");
		}

		/**
		 * 보너스 쿠폰 사용
		 */
		if(reqVO.getBonusId() > 0){
			rsvDao.updateBonusCouponUsed(reqVO);
		}
		
		String bonusMsg = "";
		/**
		 * 예약시 바로 포인트 차감, 취소 요청시 바로 포인트 복구
		 * 멤버십일 경우 포인트 차감
		 * 포인트 사용 할 경우
		 */
//		if(reqVO.getRsvAmntWeys() > 0){
//			List<Map<String, Object>> rsvActiveList = new ArrayList<>();
//			int usrId = Integer.parseInt(userKey); 
//			int rsvId = reqVO.getRsvId(); 
//			
//			reqVO.setGetAmntWeys(weysAmnt);
//			res = rsvDao.updateMemberRsvPoint(reqVO);
//			if(res > 0){
//				res = rsvDao.insertMemberActiveRsv(reqVO);
//			}
//			
//			Map<String, Object> bonusMap = rsvDao.selectBonusInfo(usrId);
//			int remain = MapUtils.getIntValue(bonusMap, "COST");
//			String duedate = MapUtils.getString(bonusMap, "END_DT");
//			/**
//			 * [웨이즈]
//
//				#{name} 고객님의 100% 우대 잔여 한도 안내
//				
//				잔여한도: #{b_remain}원
//				만료예정일: #{b_duedate}
//				
//				※ 유의사항
//				잔여 한도를 초과하여 환전을 신청하시는 경우, 초과 금액에 대해서는 시즌 프로모션 우대가 적용됩니다.
//			 */
//			bonusMsg = "[웨이즈]\n\n";
//			bonusMsg += reqVO.getRsvNm() + " 고객님의 100% 우대 잔여 한도 안내\n\n";
//			bonusMsg += "잔여한도: " + Utils.setStringFormatInteger(remain + "") + "원\n";
//			bonusMsg += "만료예정일: " + duedate + "\n\n";
//			bonusMsg += "※ 유의사항\n";
//			bonusMsg += "잔여 한도를 초과하여 환전을 신청하시는 경우, 초과 금액에 대해서는 시즌 프로모션 우대가 적용됩니다.";
//			
//			List<Map<String, Object>> usrMemActiveList = rsvDao.selectUsrMemActList(usrId);
//			
//			for(int i=0 ; i<usrMemActiveList.size() ; i++){
//				int cost = MapUtils.getIntValue(usrMemActiveList.get(i), "USE_COST");
//				int activeId = MapUtils.getIntValue(usrMemActiveList.get(i), "ACTIVE_ID");
//				Map<String, Object> usingCost = new HashMap<>();
//				usingCost.put("activeId", activeId);
//				
//				if(cost > weysAmnt){
//					usingCost.put("cost", weysAmnt);
//					weysAmnt = 0;
//					rsvActiveList.add(usingCost);
//					break;
//				} else {
//					usingCost.put("cost", cost);
//					weysAmnt = weysAmnt - cost;
//					rsvActiveList.add(usingCost);
//				}
//			}
//			/**
//			 * 예약 멤버십 사용 내역 정리
//			 */
//			for(Map<String, Object> usingCost : rsvActiveList){
//				rsvDao.updateMemActUse(usingCost);
//			}
//			if(rsvActiveList.size() > 0){
//				Map<String, Object> rsvActMap = new HashMap<>();
//				rsvActMap.put("rsvId", rsvId);
//				rsvActMap.put("list", rsvActiveList);
//				rsvDao.insertRsvAct(rsvActMap);
//			}
//		}
		
		/**
		 * 원화금액 
		 */
		int fakeCost = (int) (reqVO.getRsvAmnt() * reqVO.getBasicRateWeys());
		if(reqVO.getUnitCd().equals("JPY")){
			fakeCost = fakeCost / 100;
		}

		/**
		 * 알림톡 전송
			조은용 고객님의 USD 1,200 환전 예약이 접수되었습니다.
			
			■환전 신청내역
			- 예약번호: WERWEW
			- 구매하실 외화: USD 1,200
			- 수령장소: #{r_delivery_location}
			- 수령일시: #{r_delivery_date}
			- 지불하실 금액: 1,302,000원
			
			■입금안내
			- 입금하실 금액: 1,302,000원
			- 입금계좌: KEB하나은행(110-311-119497)
			- 예금주: 웨이즈 조은용
			
			※ 2018/03/15 00:00까지 미입금시 자동 취소되며, 다시 환전 예약을 하셔야 합니다.
			※ 입금액 및 입금자 성명 불일치시 입금 확인 및 환전 거래가 지연될 수 있습니다.
		 */
		
		String storeNm = rsvDao.selectAddrDetail(reqVO);
		String msg = "";
		msg = reqVO.getRsvNm() + " 고객님의 " + reqVO.getUnitCd() + " " + Utils.setStringFormatInteger(reqVO.getRsvAmnt() + "") + " 환전 예약이 접수되었습니다.\n\n";

		msg += "■환전 신청내역\n";
		msg += "- 예약번호: " + reqVO.getRsvNo() + "\n";
		msg += "- 구매하실 외화: " + reqVO.getUnitCd() + " " + Utils.setStringFormatInteger(reqVO.getRsvAmnt() + "") + "\n";
		msg += "- 수령장소: " + storeNm + "\n";
		msg += "- 수령일시: " + reqVO.getRsvDt() + " " + reqVO.getRsvTm() + "\n";
		msg += "- 지불하실 금액: " + Utils.setStringFormatInteger(reqVO.getGetAmnt() + "") + "원\n\n";

		msg += "■입금안내\n";
		msg += "- 입금하실 금액: " + Utils.setStringFormatInteger(reqVO.getGetAmnt() + "") + "원\n";
		msg += "- 입금계좌: " + bankNm + "(" + accntNo + ")\n";
		msg += "- 예금주: " + holder + "\n\n";

		msg += "※ " + sdf.format(date) + "까지 미입금시 자동 취소되며, 다시 환전 예약을 하셔야 합니다.\n";
		msg += "※ 입금액 및 입금자 성명 불일치시 입금 확인 및 환전 거래가 지연될 수 있습니다.";
		
		
		/**
		 * 예약 접수 이메일 보내기
		 */
		String unitNm = rsvDao.selectUnitNm(reqVO.getUnitCd());
		String addr = "";
		String rsvDt = "";
		if(reqVO.getRsvForm().equals("D")){
			addr = reqVO.getRsvAddr() + " " + reqVO.getRsvAddrDetail();
			
			String tm = reqVO.getRsvTm();
			int deliverTm = rsvDao.selectDeliverTime(reqVO.getStoreId());
			StringTokenizer st = new StringTokenizer(tm, ":");
			String hour = st.nextToken();
			String min = st.nextToken();
			
			int iHour = Integer.parseInt(hour) + deliverTm;
			hour = iHour + ":" + min;
			
			rsvDt = reqVO.getRsvDt() + " " + reqVO.getRsvTm() + " ~ " + hour + " 경 ";
		} else {
			addr = rsvDao.selectStoreAddr(reqVO.getStoreId());
			rsvDt = reqVO.getRsvDt() + " " + reqVO.getRsvTm();
		}
		
		if(SERVER_TYPE.equals("USER")){

			Map<String, Object> mailMap = new HashMap<>();
			mailMap.put("usrNm", reqVO.getRsvNm());
			mailMap.put("rsvNo", reqVO.getRsvNo());
			mailMap.put("regDttm", Utils.getTodayDate("yyyy년 MM월 dd일 a hh시 mm분"));
			mailMap.put("unit", unitNm + "(" + reqVO.getUnitCd() + ")");
			mailMap.put("rsvAmnt", Utils.setStringFormatInteger(reqVO.getRsvAmnt() + ""));
			mailMap.put("basicRate", Utils.setStringFormatInteger(String.format("%.2f",reqVO.getBasicRateWeys())));
			mailMap.put("wonBill", Utils.setStringFormatInteger(fakeCost + ""));
			mailMap.put("cost", Utils.setStringFormatInteger(reqVO.getGetAmntWeys() + ""));
			mailMap.put("cms", Utils.setStringFormatInteger(reqVO.getCms() + ""));

			mailMap.put("weysBonus", (int)reqVO.getWeysBonus());
			mailMap.put("weysBonusVal", Utils.setStringFormatInteger(reqVO.getWeysBonusVal() + ""));
			mailMap.put("weysCommis", reqVO.getWeysCommis());
			mailMap.put("bonusNm", reqVO.getBonusNm());
			mailMap.put("weysCommisVal", Utils.setStringFormatInteger(reqVO.getWeysCommisVal() + ""));
			
			mailMap.put("getAmnt", Utils.setStringFormatInteger(reqVO.getGetAmnt() + ""));

			mailMap.put("vbank", bankNm + "(" + accntNo + ")");
			mailMap.put("vbankHolder", holder);
			mailMap.put("vbankDue", sdf.format(date));

			mailMap.put("addr", addr);
			mailMap.put("rsvDt", rsvDt);
			
			Map<String, Object> reqMailMap = new HashMap<>();
			reqMailMap.put("usrId", reqVO.getUsrId());
			reqMailMap.put("key", ENC_KEY);
			
			emailVO.setEmailMap(mailMap);
			emailVO.setSubject(reqVO.getRsvNm() + " 고객님, 환전 예약이 접수되었습니다.");
			emailVO.setVeloTmp("submit.vm");
			emailVO.setFrom(EMAIL_ID);
			emailVO.setReceiver(rsvDao.selectUsrEmail(reqMailMap));
			
			try{
				mailer.sendEmail(emailVO);
			} catch (Exception e) {
				logger.info("mail error : " + e.getMessage());
			}
		}

		Map<String, Object> reqMap = new HashMap<>();
		reqMap.put("usrId", userKey);
		reqMap.put("encKey", ENC_KEY);
		Map<String, Object> userInfo = rsvDao.selectRsvUsrInfo(reqMap);

		String usrTel = MapUtils.getString(userInfo, "usrTel");
		String nation = MapUtils.getString(userInfo, "nation");
		if(usrTel.startsWith("010"))
			usrTel = usrTel.substring(1);
		String tel = nation + usrTel;
		
		KakaoClient kakao = new KakaoClient(Constant.KAKAO_TALK_MSG, IB_KAKAO_ID, IB_KAKAO_PWD, IB_KAKAO_SENDER_KEY, IB_FROM_TEL);
		kakao.sendMsg(tel, msg, "reservation_order_v8");
		
		Map<String, Object> talk =  new HashMap<>();
		talk.put("usrId", userKey);
		talk.put("msg", msg);
		talk.put("templete", "reservation_order_v8");
		rsvDao.insertKakaoLog(talk);
		
		/**
		 * 기존회원 보너스 잔여 안내
		 */
		if(!bonusMsg.equals("")){
			kakao.sendMsg(tel, bonusMsg, "reservation_extra100");
			
			talk =  new HashMap<>();
			talk.put("usrId", userKey);
			talk.put("msg", bonusMsg);
			talk.put("templete", "reservation_extra100");
			rsvDao.insertKakaoLog(talk);
		}
		
		/**
		 * 예약 알림 등록
		 */
		String armTitle = reqVO.getUnitCd() + " " + Utils.setStringFormatInteger(reqVO.getRsvAmnt() + "");
		armTitle += "(" + Utils.setStringFormatInteger(reqVO.getGetAmnt() + "") + "원) 환전 예약이 접수되었습니다.";
		armTitle += sdf.format(date) + "까지 미입금시 예약이 자동 취소됩니다.";
		
		Map<String, Object> alarm =  new HashMap<>();
		alarm.put("usrId", reqVO.getUsrId());
		alarm.put("armTp", "I");
		alarm.put("armTitle", armTitle);
		alarm.put("armTarget", "rsv");
		alarm.put("armVal", "/api/rsv/gVerSion/" + reqVO.getRsvId());
		rsvDao.insertAlarm(alarm);
		
		/**
		 * 사용자에게 입금 처리 완료 푸쉬 발송
		 */
		Map<String, Object> uuidMap = rsvDao.selectUuidUsr(reqVO.getUsrId());
		if(uuidMap != null){
			String uuid = MapUtils.getString(uuidMap, "UUID");
			String os = MapUtils.getString(uuidMap, "OS");
			
			if(os.equals("A")){
				JSONObject dataJson = new JSONObject();
				dataJson.put("type", "reserve");
				dataJson.put("st", "insert");
				dataJson.put("val", reqVO.getRsvId());
				dataJson.put("message", armTitle);
				dataJson.put("sound", "default");
				
				JSONObject json = new JSONObject();
				json.put("to", uuid);
				json.put("data", dataJson);
				
				PushService push = new PushService(json, FCM_SERVER_KEY, FCM_SEND_URL);
				Thread t = new Thread(push);
				t.start();

			} else if(os.equals("I")){
				JSONObject pushObj = new JSONObject();
				pushObj.put("to", uuid);
				JSONObject dataJson = new JSONObject();
				dataJson.put("title", armTitle);
				dataJson.put("contents","reserve");
				dataJson.put("st", "insert");
				dataJson.put("val", reqVO.getRsvId());
				dataJson.put("img", "");
				
				JSONObject notiJson = new JSONObject();
				notiJson.put("title", "환전예약이 접수되었습니다.");
				notiJson.put("body", armTitle);
				notiJson.put("icon", "");
				notiJson.put("sound", "activated");
				notiJson.put("badge", "1");
				
				pushObj.put("content_available", true);
				pushObj.put("data", dataJson);
				pushObj.put("priority", "high");
				pushObj.put("notification", notiJson);
				
				PushService push = new PushService(pushObj, FCM_SERVER_KEY, FCM_SEND_URL);
				Thread t = new Thread(push);
				t.start();
			}
		}
		
		/**
		 * 예약 로그 등록
		 */
		Map<String, Object> logMap = new HashMap<>();
		logMap.put("rsvId", reqVO.getRsvId());
		logMap.put("usrId", reqVO.getUsrId());
		logMap.put("asIs", null);
		logMap.put("toBe", Constant.RSV_START);
		rsvDao.insertRsvLog(logMap);
		
		reqVO.setKey(null);
		reqVO.setMerchantUid(null);
		reqVO.setUsrId(0);
		reqVO.setRsvId(reqVO.getRsvId());
		reqVO.setImp_uid(null);
		if (res > 0)
			return reqVO;
		else{
			reqVO.setKey("FAIL INSERT RSV");
			return reqVO;
		}
	}

	public List<RsvListVO> selectRsvList(String userKey) {
		Map<String, Object> reqMap = new HashMap<>();
		reqMap.put("usrId", userKey);
		reqMap.put("encKey", ENC_KEY);
		return rsvDao.selectRsvList(reqMap);
	}

	public RsvInfoVO selectRsvInfo(String userKey, String rsvId) throws Exception {
		Map<String, Object> reqMap = new HashMap<>();
		reqMap.put("usrId", userKey);
		reqMap.put("rsvId", rsvId);
		reqMap.put("encKey", ENC_KEY);
		RsvInfoVO info = rsvDao.selectRsvInfo(reqMap);
		if(info == null){
			return null;
		}
		
		String rsvDt = info.getRsvDt();
		String rsvTm = info.getRsvTm();
		
		String dayString = Utils.getDayString(rsvDt);

		info.setRsvDt(rsvDt + " " + dayString + " " + rsvTm);

		String modDttm = info.getModDttm();
		if(modDttm != null){
			StringTokenizer st = new StringTokenizer(modDttm, " ");
			String modDt = st.nextToken();
			String modTm = st.nextToken();
			String modDay = Utils.getDayString(modDt);
			info.setModDttm(modDt + " " + modDay + " " + modTm);
		}
		
		String cancelDttm = info.getCancelDttm(); 
		if(cancelDttm != null){
			StringTokenizer st = new StringTokenizer(cancelDttm, " ");
			String cancelDt = st.nextToken();
			String cancelTm = st.nextToken();
			String cancelDay = Utils.getDayString(cancelDt);
			info.setCancelDttm(cancelDt + " " + cancelDay + " " + cancelTm);
		}
		
		String icmDttm = info.getIcmDttm();
		if(icmDttm != null){
			StringTokenizer st = new StringTokenizer(icmDttm, " ");
			String icmDt = st.nextToken();
			String icmTm = st.nextToken();
			String icmDay = Utils.getDayString(icmDt);
			info.setIcmDttm(icmDt + " " + icmDay + " " + icmTm);
		}
		
		/**
		 * 지점 안내 URL
		 */
		String url = rsvDao.selectStoreInfo();
		info.setStoreInfo(url);
		
		return info;
	}

	public RsvCancelVO checkCancelInfoIf(String userKey, String rsvId) {
		Map<String, Object> reqMap = new HashMap<>();
		reqMap.put("usrId", userKey);
		reqMap.put("rsvId", rsvId);

		Map<String, Object> info = rsvDao.checkCancelInfoIf(reqMap);
		String rsvSt = MapUtils.getString(info, "RSV_ST");

		RsvCancelVO result = new RsvCancelVO();

		String rsvDt = MapUtils.getString(info, "RSV_DT");
		String targetDt = "";
		
		if(rsvSt.equals("R")){
			targetDt = Utils.getDiffDate(0);
		} else if(rsvSt.equals("CR") || rsvSt.equals("CF")){
			targetDt = MapUtils.getString(info, "CANCEL_DTTM");
		}
		
		long diffChk = Utils.diffTwoDate(rsvDt, targetDt);
		
		int cancelCms = 0;
		if(diffChk <= 0){
			cancelCms = MapUtils.getIntValue(info, "CMS", 0);
		}
		
		String cancelCmt = rsvDao.selectRsvSt("A");
		
		result.setCancelCms(cancelCms);
		result.setCancelAmnt(Constant.CANCEL_CMS);
		result.setCommisTxt(MapUtils.getString(info, "COMMIS_TXT"));
		result.setCancelCmt(cancelCmt);
		return result;
	}

	public RsvCancelVO checkCancelInfoIfV7(String userKey, String rsvId) {
		Map<String, Object> reqMap = new HashMap<>();
		reqMap.put("usrId", userKey);
		reqMap.put("rsvId", rsvId);
		

		Map<String, Object> info = rsvDao.checkCancelInfoIf(reqMap);
		String rsvSt = MapUtils.getString(info, "RSV_ST");

		RsvCancelVO result = new RsvCancelVO();

		String rsvDt = MapUtils.getString(info, "RSV_DT");
		String targetDt = "";
		
		if(rsvSt.equals("R")){
			targetDt = Utils.getDiffDate(0);
		} else if(rsvSt.equals("CR") || rsvSt.equals("CF")){
			targetDt = MapUtils.getString(info, "CANCEL_DTTM");
		}
		
		long diffChk = Utils.diffTwoDate(rsvDt, targetDt);
		
		if(diffChk > 0){
			result.setCancelAmnt(0);
			result.setCancelCms(0);
		} else {
			int cms = MapUtils.getIntValue(info, "CMS", 0);
			int rsvAmntUser = MapUtils.getIntValue(info, "RSV_AMNT_USER", 0);
			double basicRateUser = MapUtils.getDoubleValue(info, "BASIC_RATE_USER");
			double basicRateWeys = MapUtils.getDoubleValue(info, "BASIC_RATE_WEYS");
			String unit = MapUtils.getString(info, "UNIT");
			
			int cancelAmnt = (int) (rsvAmntUser * (basicRateUser - basicRateWeys));
			if(unit.equals("JPY")){
				cancelAmnt = cancelAmnt / 100;
			}
			cancelAmnt = cancelAmnt / 10 * 10;

			result.setCancelAmnt(cancelAmnt);
			result.setCancelCms(cms);
		}
		
		return result;
	}

	public RsvCancelVO checkCancelInfo(String userKey, String rsvId) {
		Map<String, Object> reqMap = new HashMap<>();
		reqMap.put("usrId", userKey);
		reqMap.put("rsvId", rsvId);
		reqMap.put("encKey", ENC_KEY);

		/**
		 * 예약일 전 취소
		 *  - 전액 반환 
		 * 
		 * 예약당일 취소 및 예약일 지나서 취소
		 * - 한도는 전액 환불
		 * - 배송비, 쿠폰 차감, 50% 우대 환전 금액 수수료 차감 
		 */
		Map<String, Object> cancelInfo = rsvDao.selectRsvCheck(reqMap);

		if(cancelInfo == null)
			return null;

		String rsvDt = MapUtils.getString(cancelInfo, "RSV_DT");
		String unit = MapUtils.getString(cancelInfo, "UNIT");
		
		long diff = Utils.diffTwoDate(rsvDt, Utils.getDiffDate(0));
		int getAmnt = MapUtils.getIntValue(cancelInfo, "GET_AMNT");
		int cancelCms = 0;
		int rsvAmnt = MapUtils.getIntValue(cancelInfo, "RSV_AMNT");
		
		if(diff <= 0){
			cancelCms = MapUtils.getIntValue(cancelInfo, "CMS", 0);
		}

		RsvCancelVO info = new RsvCancelVO();

		info.setUnit(unit);
		info.setRsvAmnt(rsvAmnt);
		info.setGetAmnt(getAmnt);
		info.setCancelCms(cancelCms);
		info.setCancelAmnt(getAmnt - cancelCms - Constant.CANCEL_CMS);
		info.setRsvNm(MapUtils.getString(cancelInfo, "RSV_NM"));
		
		return info;
	}
	
	public RsvCancelVO checkCancelInfoV7(String userKey, String rsvId) {
		Map<String, Object> reqMap = new HashMap<>();
		reqMap.put("usrId", userKey);
		reqMap.put("rsvId", rsvId);
		reqMap.put("encKey", ENC_KEY);

		/**
		 * 예약일 전 취소
		 *  - 전액 반환 
		 * 
		 * 예약당일 취소 및 예약일 지나서 취소
		 * - 한도는 전액 환불
		 * - 배송비, 쿠폰 차감, 50% 우대 환전 금액 수수료 차감 
		 */
		Map<String, Object> cancelInfo = rsvDao.selectRsvCheck(reqMap);

		if(cancelInfo == null)
			return null;

		String rsvDt = MapUtils.getString(cancelInfo, "RSV_DT");
		String unit = MapUtils.getString(cancelInfo, "UNIT");
		
		long diff = Utils.diffTwoDate(rsvDt, Utils.getDiffDate(0));
		int getAmnt = MapUtils.getIntValue(cancelInfo, "GET_AMNT");
		int cancelAmnt = MapUtils.getIntValue(cancelInfo, "GET_AMNT");
		int cancelCms = 0;
		int rsvAmnt = MapUtils.getIntValue(cancelInfo, "RSV_AMNT");
		
		if(diff <= 0){
			cancelCms = MapUtils.getIntValue(cancelInfo, "CMS", 0);
			
			int rsvAmntUser = MapUtils.getIntValue(cancelInfo, "RSV_AMNT_USER", 0);
			
			double basicRateUser = MapUtils.getDoubleValue(cancelInfo, "BASIC_RATE_USER");
			double basicRateWeys = MapUtils.getDoubleValue(cancelInfo, "BASIC_RATE_WEYS");
			if(rsvAmntUser > 0){
				
				int cancelVal = (int) ((basicRateUser - basicRateWeys) * rsvAmntUser);
				if(unit.equals("JPY")){
					cancelVal = cancelVal / 100;
				}
				cancelVal = cancelVal / 10 * 10;
				cancelCms = cancelCms + cancelVal;
			}
		}

		cancelAmnt = cancelAmnt - cancelCms;
		
		RsvCancelVO info = new RsvCancelVO();

		info.setUnit(unit);
		info.setRsvAmnt(rsvAmnt);
		info.setGetAmnt(getAmnt);
		info.setCancelCms(cancelCms);
		info.setCancelAmnt(cancelAmnt);
		info.setRsvNm(MapUtils.getString(cancelInfo, "RSV_NM"));
		
		return info;
	}

	public int updateCancelRsv(RsvCancelVO reqVO) throws Exception {

		reqVO.setEncKey(ENC_KEY);
		Map<String, Object> cancelInfo = rsvDao.selectCancelSt(reqVO);
		String rsvSt = MapUtils.getString(cancelInfo, "RSV_ST");
		
		int res = 0;
		if(cancelInfo == null){
			return -1;
		} else if (rsvSt.equals("S")){
			reqVO.setRsvSt(Constant.RSV_CANCEL_BEFORE);
			res = rsvDao.updateRsvCancelBefore(reqVO);
		} else {
			if(reqVO.getBankCd() == null ||
					reqVO.getBankNm() == null ||
					reqVO.getBankCd().equals("") ||
					reqVO.getBankNm().equals("")
					){
				return -1;
			}
			reqVO.setRsvSt(Constant.RSV_CANCEL_READY);
			reqVO.setEncKey(ENC_KEY);
			res = rsvDao.updateCancelRsv(reqVO);
		}
		
		/**
		 * 예약 로그 등록
		 */
		if(res > 0){
			/**
			 * 멤버십 사용된 내역이면 사용된 멤버십 복구
			 * MEMBER COST 복구
			 * MEMBER_ACTIVE USE_COST 복구
			 * MEMBER_ACTIVE 예약내역 삭제
			 */
//			int rsvAmntWeys = rsvDao.checkRsvMember(reqVO.getRsvId());
//			if(rsvAmntWeys > 0){
//				rsvDao.updateReturnMemCost(reqVO.getRsvId());
//				rsvDao.updateReturnUseCost(reqVO.getRsvId());
//				rsvDao.insertReturnRA(reqVO.getRsvId());
//			}
			
			Map<String, Object> logMap = new HashMap<>();
			logMap.put("rsvId", reqVO.getRsvId());
			logMap.put("usrId", reqVO.getUsrId());
			logMap.put("asIs", rsvSt);
			logMap.put("toBe", Constant.RSV_CANCEL_READY);
			rsvDao.insertRsvLog(logMap);
			
			/**
			 * 취소 내용 사용자 이메일로 발송
			 * 외화, 취소시점 날짜, 환급받을 계좌 정보
			 * 
			 * 취소 수수료에 대한 변경으로 수정사항
			 */
			Map<String, Object> emailInfo = rsvDao.selectCancelEmail(reqVO);
			String rsvDt = MapUtils.getString(emailInfo, "RSV_DT");
			
			/**
			 * 예약일 당일 전 쿠폰을 사용했더라면 
			 */
			int couponId = MapUtils.getIntValue(emailInfo, "COUPON_ID", 0);
//			if(Utils.diffTwoDate(rsvDt, Utils.getDiffDate(0)) > 0 && couponId > 0){
			if(rsvSt.equals("S") && couponId > 0){
				rsvDao.updateCouponReturn(couponId);
			}

			/**
			 * 보너스 쿠폰 사용 여부
			 */
			int bonusId = MapUtils.getIntValue(emailInfo, "BONUS_ID", 0);
			if(rsvSt.equals("S") && bonusId > 0){
				rsvDao.updateCouponReturn(bonusId);
			}
			
			/**
			 * 아이트립
			 * 당일 취소시
			 * 센터 - 취소로 상태 변경. 회수가 필요함
			 */
			String storeCenter = MapUtils.getString(cancelInfo, "STORE_CENTER"); 
			if(storeCenter.equals("Y") && Utils.diffTwoDate(rsvDt, Utils.getDiffDate(0)) <= 0){
				rsvDao.insertRsvRetLog(reqVO.getRsvId());
//				rsvDao.updateRsvGrpCancelLog(reqVO.getRsvId());
			}

			/**
			 * 예약 취소 알림 등록
			 * 입금전 취소는 푸쉬만, 알림은 등록 안함
			 */
			String armTitle = MapUtils.getString(emailInfo, "UNIT") + Utils.setStringFormatInteger(MapUtils.getString(emailInfo, "RSV_AMNT"));
			if(rsvSt.equals("S")){
				armTitle += " 예약이 입금 전 취소되었습니다.";
			} else {
				armTitle += " 예약취소 및 " + Utils.setStringFormatInteger(MapUtils.getString(emailInfo, "CANCEL_AMNT"));
				armTitle += " 원 환불요청이 접수되었습니다.(영업일 기준 2일 소요)";

				Map<String, Object> alarm =  new HashMap<>();
				alarm.put("usrId", reqVO.getUsrId());
				alarm.put("armTp", "I");
				alarm.put("armTitle", armTitle);
				alarm.put("armTarget", "rsv");
				alarm.put("armVal", "/api/user/gVerSion/rsv/" + reqVO.getRsvId());
				rsvDao.insertAlarm(alarm);
			}
			
			/**
			 * 메일 보내기
			 */
			String rsvForm = MapUtils.getString(emailInfo, "RSV_FORM");
			String addr = "";
			String emailDt = "";
			if(rsvForm.equals("R")){
				addr = MapUtils.getString(emailInfo, "STORE_NM");
				emailDt = MapUtils.getString(emailInfo, "RSV_DT") + " " + MapUtils.getString(emailInfo, "RSV_TM");
			} else {
				addr = MapUtils.getString(emailInfo, "RSV_ADDR") + " " + MapUtils.getString(emailInfo, "RSV_ADDR_DETAIL");
				
				String tm = MapUtils.getString(emailInfo, "RSV_TM");
				int deliverTm = MapUtils.getIntValue(emailInfo, "DELIVER_TIME");
				StringTokenizer st = new StringTokenizer(tm, ":");
				String hour = st.nextToken();
				String min = st.nextToken();
				
				int iHour = Integer.parseInt(hour) + deliverTm;
				hour = iHour + ":" + min;
				emailDt = MapUtils.getString(emailInfo, "RSV_DT") + " " + MapUtils.getString(emailInfo, "RSV_TM") + " ~ " + hour + " 경 ";
			}

			if(SERVER_TYPE.equals("USER")){
				String template = "";
				String subject = "";
				Map<String, Object> mailMap = new HashMap<>();
				mailMap.put("usrNm", MapUtils.getString(emailInfo, "RSV_NM"));
				mailMap.put("rsvNo", MapUtils.getString(emailInfo, "RSV_NO"));
				mailMap.put("regDttm", Utils.getTodayDate("yyyy년 MM월 dd일 a hh시 mm분"));
				
				if(rsvSt.equals("S")){
					mailMap.put("unit", MapUtils.getString(emailInfo, "UNIT_NM") + "(" + MapUtils.getString(emailInfo, "UNIT") + ")");
					mailMap.put("rsvAmnt", Utils.setStringFormatInteger(MapUtils.getString(emailInfo, "RSV_AMNT")));
					mailMap.put("addr", addr);
					mailMap.put("rsvDt", emailDt);
					template = "befCancel.vm";
					subject = MapUtils.getString(emailInfo, "RSV_NM") + " 고객님, 환전 예약이 취소되었습니다.";
				} else {
					mailMap.put("getAmnt", Utils.setStringFormatInteger(MapUtils.getString(emailInfo, "GET_AMNT")));
					mailMap.put("cancelCms", "-" + Utils.setStringFormatInteger(MapUtils.getString(emailInfo, "CANCEL_CMS")));
					mailMap.put("cancelAmnt", Utils.setStringFormatInteger(MapUtils.getString(emailInfo, "CANCEL_AMNT")));
					mailMap.put("bankNm", reqVO.getBankNm());
					mailMap.put("bankCd", reqVO.getBankCd());
					template = "cancel.vm";
					subject = MapUtils.getString(emailInfo, "RSV_NM") + " 고객님, 환불이 접수되었습니다.";
				}
				
				emailVO.setEmailMap(mailMap);
				emailVO.setSubject(subject);
				emailVO.setVeloTmp(template);
				emailVO.setFrom(EMAIL_ID);
				emailVO.setReceiver(MapUtils.getString(emailInfo, "RSV_EMAIL"));
				
				try{
					mailer.sendEmail(emailVO);
				} catch (Exception e) {
					logger.info("mail error : " + e.getMessage());
				}
			}

			
			/**
			 * 알림톡 메세지 보내기
			 	[웨이즈]

				조은용 고객님의 USD 1,200 예약이 취소되었습니다.
				
				[웨이즈]
				
				조은용 고객님의 USD 1,200 예약취소 및 환불요청이 접수되었습니다.
				
				■환불 요청내역
				- 결제하신 금액: 1,300,000원
				- 취소수수료: -8,240원
				- 환불받으실 금액: 1,296,500원
				
				(영업일 기준 2일 소요)
			 */
			String templete = "";
			String msg = "[웨이즈]\n\n";
			
			int cancelCms = MapUtils.getIntValue(emailInfo, "CANCEL_CMS");

			if(rsvSt.equals("S")){
				msg += MapUtils.getString(emailInfo, "RSV_NM") + " 고객님의 " + MapUtils.getString(emailInfo, "UNIT") + " " + Utils.setStringFormatInteger(MapUtils.getString(emailInfo, "RSV_AMNT"));
				msg += " 예약이 취소되었습니다.";
				templete = "reservation_cancel_before_v6";
			} else {
				msg += MapUtils.getString(emailInfo, "RSV_NM") + " 고객님의 " + MapUtils.getString(emailInfo, "UNIT") + " " + Utils.setStringFormatInteger(MapUtils.getString(emailInfo, "RSV_AMNT"));
				msg += " 예약취소 및 환불요청이 접수되었습니다.\n\n";
				msg += "■환불 요청내역\n";
				msg += "- 결제하신 금액: " + Utils.setStringFormatInteger(MapUtils.getString(emailInfo, "GET_AMNT")) + "원\n";
				msg += "- 취소수수료: -" + Utils.setStringFormatInteger(cancelCms + "") + "원\n";
				msg += "- 환불받으실 금액: " + Utils.setStringFormatInteger(MapUtils.getString(emailInfo, "CANCEL_AMNT")) + "원\n\n";
				msg += "(영업일 기준 2일 소요)";
				templete = "reservation_cancel_after_v6";
			}
			
			String nation = MapUtils.getString(emailInfo, "NATION");
			String usrTel = MapUtils.getString(emailInfo, "USR_TEL");
			if(nation.equals("82")){
				if(usrTel.startsWith("010")){
					usrTel = usrTel.substring(1);
				}
			}
			String tel = nation + usrTel;
			
			KakaoClient kakao = new KakaoClient(Constant.KAKAO_TALK_MSG, IB_KAKAO_ID, IB_KAKAO_PWD, IB_KAKAO_SENDER_KEY, IB_FROM_TEL);
			kakao.sendMsg(tel, msg, templete);

			Map<String, Object> talk =  new HashMap<>();
			talk.put("usrId", reqVO.getUsrId());
			talk.put("msg", msg);
			talk.put("templete", templete);
			rsvDao.insertKakaoLog(talk);
			
			Map<String, Object> usrPush = rsvDao.selectUsrUuid(reqVO.getUsrId());
			if(usrPush != null){
				String uuid = MapUtils.getString(usrPush, "UUID");
				String os = MapUtils.getString(usrPush, "OS");
				
				if(os.equals("A")){
					JSONObject dataJson = new JSONObject();
					dataJson.put("type", "reserve");
					dataJson.put("st", "cancel");
					dataJson.put("message", armTitle);
					dataJson.put("sound", "default");
					if(rsvSt.equals("S")){
						dataJson.put("val", "");
					} else {
						dataJson.put("val", reqVO.getRsvId());
					}
					
					JSONObject json = new JSONObject();
					json.put("to", uuid);
					json.put("data", dataJson);
					
					PushService push = new PushService(json, FCM_SERVER_KEY, FCM_SEND_URL);
					Thread t = new Thread(push);
					t.start();

				} else if(os.equals("I")){
					JSONObject pushObj = new JSONObject();
					pushObj.put("to", uuid);
					JSONObject dataJson = new JSONObject();
					dataJson.put("title", armTitle);
					dataJson.put("contents","reserve");
					dataJson.put("st", "cancel");
					if(rsvSt.equals("S")){
						dataJson.put("val", "");
					} else {
						dataJson.put("val", reqVO.getRsvId());
					}
					dataJson.put("img", "");
					
					JSONObject notiJson = new JSONObject();
					if(rsvSt.equals("S")){
						notiJson.put("title", "환전 예약이 취소되었습니다.");
					}else {
						notiJson.put("title", "환불요청이 접수되었습니다.");
					}
					
					notiJson.put("body", armTitle);
					notiJson.put("icon", "");
					notiJson.put("sound", "activated");
					notiJson.put("badge", "1");
					
					pushObj.put("content_available", true);
					pushObj.put("data", dataJson);
					pushObj.put("priority", "high");
					pushObj.put("notification", notiJson);
					
					PushService push = new PushService(pushObj, FCM_SERVER_KEY, FCM_SEND_URL);
					Thread t = new Thread(push);
					t.start();
				}
			}
			
			if(rsvSt.equals("S")){
				return res;
			}
			
			/**
			 * 오늘 또는 내일 건에 대한 취소 푸쉬 알림
			 */
			List<Map<String, Object>> pushList = rsvDao.selectCancelPushInfo(reqVO);
			for(Map<String, Object> pushMap : pushList){
				msg = "";
				if(rsvDt.equals(Utils.getDiffDate(0))){
					msg = "오늘";
				} else if(rsvDt.equals(Utils.getDiffDate(1))){
					msg = "내일";
				} else {
					break;
				}
				msg = msg + " " + MapUtils.getString(pushMap, "RSV_TM") + " " + Constant.PUSH_MSG_ADM_CANCEL;
				
				if(!msg.equals("")){
					String uuid = MapUtils.getString(pushMap, "UUID");
					String os = MapUtils.getString(pushMap, "OS");
				
					if(os.equals("A")){
						JSONObject dataJson = new JSONObject();
						dataJson.put("type", "reserve");
						dataJson.put("st", "cancel");
						dataJson.put("message", msg);
						dataJson.put("sound", "default");
						
						JSONObject json = new JSONObject();
						json.put("to", uuid);
						json.put("data", dataJson);
						
						PushService push = new PushService(json, FCM_SERVER_KEY, FCM_SEND_URL);
						Thread t = new Thread(push);
						t.start();
		
					} else if(os.equals("I")){
						JSONObject pushObj = new JSONObject();
						pushObj.put("to", uuid);
						JSONObject dataJson = new JSONObject();
						dataJson.put("title", msg);
						dataJson.put("contents","reserve");
						dataJson.put("st", "cancel");
						dataJson.put("img", "");
						
						JSONObject notiJson = new JSONObject();
						notiJson.put("title", "");
						notiJson.put("body", msg);
						notiJson.put("icon", "");
						notiJson.put("sound", "activated");
						notiJson.put("badge", "1");
						
						pushObj.put("content_available", true);
						pushObj.put("data", dataJson);
						pushObj.put("priority", "high");
						pushObj.put("notification", notiJson);
						
						PushService push = new PushService(pushObj, FCM_SERVER_KEY, FCM_SEND_URL);
						Thread t = new Thread(push);
						t.start();
					}
				}
			}
		}
		
		return res;
	}

	public int checkRsvDone(String rsvQr) {
		return rsvDao.checkRsvDone(rsvQr);
	}

	public int updateRsvDone(String rsvQr, TokenValues value, String filePath) throws Exception {
		
		Map<String, Object> reqMap = new HashMap<>();
		reqMap.put("rsvQr", rsvQr);
		reqMap.put("filePath", filePath);
		reqMap.put("encKey", ENC_KEY);
		int res = rsvDao.updateRsvDone(reqMap);
		/**
		 * 로그 등록
		 */
		if(res > 0){
			Map<String, Object> infoMap = rsvDao.selectCompleteRsvInfo(reqMap);

			int usrId = MapUtils.getIntValue(infoMap, "USR_ID");
			
			/**
			 * 알림톡 발송
			 * 
			 	[웨이즈]

				조은용 고객님께 외화 전달을 완료하였습니다.
				즐겁고 안전한 여행되세요!
				
				■전달내역
				- 외화금액: USD 1,200
				- 전달완료시간: 2018/03/17 10:41
			 */
			String msg = "[웨이즈]\n\n";
			msg += MapUtils.getString(infoMap, "RSV_NM") + " 고객님께 외화 전달을 완료하였습니다.\n";
			msg +="즐겁고 안전한 여행되세요!\n\n";
			
			msg +="■전달내역\n";
			msg +="- 외화금액: " + MapUtils.getString(infoMap, "UNIT") + " " + Utils.setStringFormatInteger(MapUtils.getString(infoMap, "RSV_AMNT")) + "\n";
			msg +="- 전달완료시간: " + MapUtils.getString(infoMap, "MOD_DTTM");
			
			String nation = MapUtils.getString(infoMap, "NATION");
			String usrTel = MapUtils.getString(infoMap, "RSV_TEL");
			if(nation.equals("82") && usrTel.startsWith("010"))
				usrTel = usrTel.substring(1);
			String tel = nation + usrTel;
			
			KakaoClient kakao = new KakaoClient(Constant.KAKAO_TALK_MSG, IB_KAKAO_ID, IB_KAKAO_PWD, IB_KAKAO_SENDER_KEY, IB_FROM_TEL);
			kakao.sendMsg(tel, msg, "delivery_complete_v6");

			if(usrId != 0){
				Map<String, Object> talk =  new HashMap<>();
				talk.put("usrId", usrId);
				talk.put("msg", msg);
				talk.put("templete", "delivery_complete_v6");
				rsvDao.insertKakaoLog(talk);
			}
			
			String rsvForm = MapUtils.getString(infoMap, "RSV_FORM");
			String addr = "";
			String rsvDt = "";
			if(rsvForm.equals("D")){
				addr = MapUtils.getString(infoMap, "RSV_ADDR") + " " + MapUtils.getString(infoMap, "RSV_ADDR_DETAIL");
				
				String tm = MapUtils.getString(infoMap, "RSV_TM");
				int deliverTm = MapUtils.getIntValue(infoMap, "DELIVER_TIME");
				StringTokenizer st = new StringTokenizer(tm, ":");
				String hour = st.nextToken();
				String min = st.nextToken();
				
				int iHour = Integer.parseInt(hour) + deliverTm;
				hour = iHour + ":" + min;
				
				rsvDt = MapUtils.getString(infoMap, "RSV_DT") + " " + MapUtils.getString(infoMap, "RSV_TM") + " ~ " + hour + " 경 ";
			} else {
				addr = MapUtils.getString(infoMap, "STORE_NM");
				rsvDt = MapUtils.getString(infoMap, "RSV_DT") + " " + MapUtils.getString(infoMap, "RSV_TM");
			}

			if(SERVER_TYPE.equals("USER")){
				/**
				 * 이메일 발송
				 */
				Map<String, Object> mailMap = new HashMap<>();
				mailMap.put("usrNm", MapUtils.getString(infoMap, "RSV_NM"));
				mailMap.put("rsvNo", MapUtils.getString(infoMap, "RSV_NO"));
				mailMap.put("regDttm", Utils.getTodayDate("yyyy년 MM월 dd일 a hh시 mm분"));
				
				mailMap.put("unit", MapUtils.getString(infoMap, "UNIT_NM") + "(" + MapUtils.getString(infoMap, "UNIT") + ")");
				mailMap.put("rsvAmnt", Utils.setStringFormatInteger(MapUtils.getString(infoMap, "RSV_AMNT")));
				
				mailMap.put("addr", addr);
				mailMap.put("rsvDt", rsvDt);

				mailMap.put("sign", SERVER_PATH + "/imgView/" + MapUtils.getString(infoMap, "RSV_SIGN"));
				
				emailVO.setEmailMap(mailMap);
				emailVO.setSubject(MapUtils.getString(infoMap, "RSV_NM") + " 고객님, 외화를 전달하였습니다.");
				emailVO.setVeloTmp("done.vm");
				emailVO.setFrom(EMAIL_ID);
				emailVO.setReceiver(MapUtils.getString(infoMap, "RSV_EMAIL"));
				
				try{
					mailer.sendEmail(emailVO);
				} catch (Exception e) {
					logger.info("mail error : " + e.getMessage());
				}
			}

			if(usrId != 0){
				/**
				 * 거래 완료 알림 등록
				 */
				String armTitle = "고객님께 " + MapUtils.getString(infoMap, "UNIT") + " " + Utils.setStringFormatInteger(MapUtils.getString(infoMap, "RSV_AMNT"));
				armTitle += " 전달을 완료하였습니다.";
				
				Map<String, Object> alarm =  new HashMap<>();
				alarm.put("usrId", usrId);
				alarm.put("armTp", "I");
				alarm.put("armTitle", armTitle);
				alarm.put("armTarget", "rsv");
				alarm.put("armVal", "/api/user/gVerSion/rsv/" + MapUtils.getString(infoMap, "RSV_ID"));
				rsvDao.insertAlarm(alarm);

				/**
				 * 푸시 보내기
				 */
				String uuid = MapUtils.getString(infoMap, "UUID", "");
				String os = MapUtils.getString(infoMap, "OS");
				String pushSt = MapUtils.getString(infoMap, "PUSH_ST");
				
				if(pushSt.equals("Y") && !uuid.equals("")){
					if(os.equals("A")){
						JSONObject dataJson = new JSONObject();
						dataJson.put("type", "reserve");
						dataJson.put("st", "done");
						dataJson.put("message", armTitle);
						dataJson.put("val", MapUtils.getString(infoMap, "RSV_ID"));
						dataJson.put("sound", "default");
						
						JSONObject json = new JSONObject();
						json.put("to", uuid);
						json.put("data", dataJson);
						
						PushService push = new PushService(json, FCM_SERVER_KEY, FCM_SEND_URL);
						Thread t = new Thread(push);
						t.start();

					} else if(os.equals("I")){
						JSONObject pushObj = new JSONObject();
						pushObj.put("to", uuid);
						JSONObject dataJson = new JSONObject();
						dataJson.put("title", armTitle);
						dataJson.put("contents","reserve");
						dataJson.put("st", "done");
						dataJson.put("val", MapUtils.getString(infoMap, "RSV_ID"));
						dataJson.put("img", "");
						
						JSONObject notiJson = new JSONObject();
						notiJson.put("title", "즐겁고 안전한 여행되세요!");
						notiJson.put("body", armTitle);
						notiJson.put("icon", "");
						notiJson.put("sound", "activated");
						notiJson.put("badge", "1");
						
						pushObj.put("content_available", true);
						pushObj.put("data", dataJson);
						pushObj.put("priority", "high");
						pushObj.put("notification", notiJson);
						
						PushService push = new PushService(pushObj, FCM_SERVER_KEY, FCM_SEND_URL);
						Thread t = new Thread(push);
						t.start();
					}
				}
			}
		}
		return res;
	}

	public String certify(RsvInfoVO reqVO) {
		IdentifyUtils idu = new IdentifyUtils(NAME_CHECK_CODE, NAME_CHECK_PWD);
		boolean chk = idu.checkIdentify(reqVO.getRsvNm(), reqVO.getRsvNmId().replaceAll("-", ""));
		
		if(chk){
			UsrVO tmp = new UsrVO();
			tmp.setUsrNm(reqVO.getRsvNm());
			tmp.setUsrNmId(reqVO.getRsvNmId());
			tmp.setEncKey(ENC_KEY);
			
			String tel = userDao.selectCheckCertify(tmp);
			if(tel == null || tel.equals("")){
				return "success";
			} else {
				return tel;
			}
		} 
		
		return "false";
	}
	

	public boolean certifyV7(RsvInfoVO reqVO) {
		IdentifyUtils idu = new IdentifyUtils(NAME_CHECK_CODE, NAME_CHECK_PWD);
		return idu.checkIdentify(reqVO.getRsvNm(), reqVO.getRsvNmId().replaceAll("-", ""));
	}

	public int updateUsrForign(RsvInfoVO reqVO) {
		reqVO.setKey(ENC_KEY);
		return rsvDao.updateUsrForign(reqVO);
	}

	@Cacheable(cacheName="forgNatInfo")
	public Map<String, Object> selectNatInfo() {
		
		Map<String, Object> resultMap = new HashMap<>();
		List<Map<String, Object>> natList = rsvDao.selectNatInfo();
		resultMap.put("dataList", natList);
		return resultMap;
	}

	public Map<String, Object> selectRsvPaper(String unitCd) {
		return rsvDao.selectRsvPaper(unitCd);
	}

	public Map<String, Object> selectChangeDt(String rsvId) throws Exception {
		
		Map<String, Object> dateMap = rsvDao.selectRsvDtInfo(rsvId);

		String regDttm = MapUtils.getString(dateMap, "REG_DTTM");
		String rsvDt = MapUtils.getString(dateMap, "RSV_DT");
		String today = Utils.getDiffDate(0);

		long diffDay = Utils.diffTwoDate(rsvDt, today);
		
		int startDt = 0;
		int endDt = 0;
		
		if(diffDay == 0){
			/**
			 * 오늘 예약은 변경 안됨
			 */
			return null;
		} else {
			String todayLimit = today + " 18:00";
			String todayMinute = Utils.getTodayDate("yyyy.MM.dd HH:mm");
			
			long todayTime = Utils.diffTwoMinutes(todayLimit, todayMinute);
			
			/**
			 * 예약 변경은 내일부터 가능
			 */
			startDt = 1;
			
			if(diffDay > 1){
				/**
				 * 오후6시 지나면 내일로 예약변경은 못함.
				 */
				if(todayTime < 0){
					startDt = 2;
				}
			} else {
				if(todayTime < 0){
					/**
					 * 내일 예약은 오후 6시 지나서 수정 안됨
					 */
					return null;
				} 
			}
			/**
			 * 예약 최대 변경일은 등록일로부터 7일
			 */
			long diffReg = Utils.diffTwoDate(regDttm, today);
			endDt = (int) (diffReg + 7);
		}
		
		/**
		 * 예약 가능일 구하기
		 */
		int storeId = MapUtils.getIntValue(dateMap, "STORE_ID");
		List<Map<String, Object>> rsvDate = new ArrayList<>();
		
		int index = startDt - 2;
		
		while(index <= endDt + 2){
			
			String setDt = Utils.getDiffDate(index);
			int day = Utils.getDay(setDt);
			
			Map<String, Object> reqMap = new HashMap<>();
			reqMap.put("storeId", storeId);
			reqMap.put("day", day);
			reqMap.put("closeDt", setDt);
			String closeDt = rsvDao.selectCheckClose(reqMap);
			boolean holiday = false;
			
			if(index < startDt || index > endDt){
				holiday = true;
			}
			
			if (closeDt != null) {
				holiday = true;
			}

			Map<String, Object> timeMap = rsvDao.selectRsvStoreTime(reqMap);
			String openSt = MapUtils.getString(timeMap, "OPEN_ST");
			if(openSt.equals("N")){
				holiday = true;
			}
			
			String startTm = MapUtils.getString(timeMap, "startTm");
			String endTm = MapUtils.getString(timeMap, "endTm");
			
			Map<String, Object> resMap = new HashMap<>();
			resMap.put("rsvYear", setDt.substring(0, 4));
			resMap.put("rsvDt", setDt.substring(5).replace(".", "/"));
			resMap.put("holiday", holiday);
			resMap.put("rsvDay", day);
			resMap.put("startTm", startTm);
			resMap.put("endTm", endTm);
			rsvDate.add(resMap);
			index = index + 1;
		}
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("dataList", rsvDate);
		return resultMap;
	}

	public int updateChangeRsvDt(RsvInfoVO reqVO) {
		
		String today = Utils.getDiffDate(0);
		
		long diffDt = Utils.diffTwoDate(reqVO.getRsvDt(), today);
		
		if(diffDt <= 0){
			return -1;
		}
		
		Map<String, Object> originMap = rsvDao.selectRsvDtInfo(reqVO.getRsvId() + "");
		String memo = MapUtils.getString(originMap, "RSV_DT") + " : " + MapUtils.getString(originMap, "RSV_TM") + " -> "
				+ reqVO.getRsvDt() + " : " + reqVO.getRsvTm() + " 으로 시간 변경";
		
		reqVO.setMemo(memo);
		rsvDao.insertUsrMemo(reqVO);
		return rsvDao.updateRsvDt(reqVO);
	}

	public ResValue updateBankSet(PaygateVO infoVO) throws Exception {
		ResValue resultStatus = new ResValue();


		Map<String, Object> log = new HashMap<>();
		log.put("usrId", infoVO.getUsrId());
			
		Map<String, Object> resultMap = new HashMap<>();
		String status = "";
		
		/**
		 * 1. 멤버등록
		 */
		resultMap = PayGateUtil.createMember(API_URL, GUID, KEY_P, infoVO.getUsrId());
		log.put("step", "member_create");
		log.put("result", resultMap.toString());
		rsvDao.insertPGateNetLog(log);
		
		/**
		 * 멤버 이름 업데이트
		 */
		String memGuid = MapUtils.getString(resultMap, "memGuid");
		infoVO.setEncKey(ENC_KEY);
		Map<String, Object> authInfo = rsvDao.selectUsrAuth(infoVO);
		String usrNm = MapUtils.getString(authInfo, "USR_NM");
		String usrTel = MapUtils.getString(authInfo, "USR_TEL");
		resultMap = PayGateUtil.updateMember(API_URL, GUID, KEY_P, memGuid, usrNm, infoVO.getFirstNm(), infoVO.getLastNm(), usrTel);
		log.put("step", "member_update");
		log.put("result", resultMap.toString());
		rsvDao.insertPGateNetLog(log);
		
		status = MapUtils.getString(resultMap, "status", "");
		if(!status.equals("SUCCESS")){
			/**
			 * 정보 등록 실패
			 */
			resultStatus.setResCode(ErrCode.UNKNOWN_ERROR);
			resultStatus.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
			return resultStatus;
		}
		infoVO.setMemGuid(memGuid);
		rsvDao.updateMemGuid(infoVO);
		
		/**
		 * 2. 실계좌 등록
		 * result == SUCCESS
		 */
		resultMap = PayGateUtil.createRealBank(API_URL, GUID, KEY_P, infoVO.getBankCd(), infoVO.getBankNum(), memGuid);
		log.put("step", "bank_insert");
		log.put("result", resultMap.toString());
		rsvDao.insertPGateNetLog(log);
		
		status = MapUtils.getString(resultMap, "status", "");
		if(!status.equals("SUCCESS")){
			/**
			 * 계좌정보 업데이트 실패
			 */
			resultStatus.setResCode(ErrCode.FAIL_INSERT_BANK);
			resultStatus.setResMsg(ErrCode.getMessage(ErrCode.FAIL_INSERT_BANK));
			return resultStatus;
		}
		
		/**
		 * 3. 계좌주명 검증
		 * {bnkCd=KUKMIN_004, accntNo=28930****11163, tid=T102UVP, status=CHECK_BNK_NM_FINISHED, e2e=false}
		 */
		resultMap = PayGateUtil.checkBankName(API_URL, GUID, KEY_P, memGuid);
		log.put("step", "bank_auth");
		log.put("result", resultMap.toString());
		rsvDao.insertPGateNetLog(log);
		
		status = MapUtils.getString(resultMap, "status", "");
		if(!status.equals("CHECK_BNK_NM_FINISHED")){
			/**
			 * 계좌주 검증 실패
			 */
			
			resultStatus.setResCode(ErrCode.FAIL_AUTH_BANK);
			resultStatus.setResMsg(ErrCode.getMessage(ErrCode.FAIL_AUTH_BANK));
			return resultStatus;
		}
		infoVO.setAuthSt("R");
		rsvDao.updateUsrAuthDone(infoVO);
		
		/**
		 * 4. 멤버의 실 계좌 소유권 검증. 1원 보내기
		 * 계좌당 최대 5회
		 */
		int sendCnt = rsvDao.selectOneSendCnt(infoVO.getUsrId());
		if(sendCnt >= 5){
			/**
			 * 5회 초과
			 */
			log.put("step", "send_1won");
			log.put("result", "FAIL_OVER_SEND");
			rsvDao.insertPGateNetLog(log);
			
			resultStatus.setResCode(ErrCode.FAIL_OVER_SEND);
			resultStatus.setResMsg(ErrCode.getMessage(ErrCode.FAIL_OVER_SEND));
			return resultStatus;
		}
		resultMap = PayGateUtil.checkBankCode(API_URL, GUID, KEY_P, memGuid);
		log.put("step", "send_1won");
		log.put("result", resultMap.toString());
		rsvDao.insertPGateNetLog(log);
		
		status = MapUtils.getString(resultMap, "status", "");
		if(!status.equals("VRFY_BNK_CD_SENDING_1WON")){
			/**
			 * 1원 보냄 실패
			 */
			resultStatus.setResCode(ErrCode.FAIL_AUTH);
			resultStatus.setResMsg(ErrCode.getMessage(ErrCode.FAIL_AUTH));
			return resultStatus;
		}
		rsvDao.insertOneSend(infoVO.getUsrId());
		
		return resultStatus;
	}

	public ResValue updateBankCode(PaygateVO infoVO) throws Exception {
		ResValue resultStatus = new ResValue();
		/**
		 * 5. 인증 코드 전송
		 */
		String memGuid = rsvDao.selectMemGuid(infoVO);
		Map<String, Object> resultMap = PayGateUtil.sendBankAuth(API_URL, GUID, KEY_P, memGuid, infoVO.getCode());
		String activityResult = MapUtils.getString(resultMap, "activityResult", "");
		if(!activityResult.equals("ALERT_MSG_ANSWER_YES")){
			/**
			 * 인증실패
			 */
			resultStatus.setResCode(ErrCode.FAIL_AUTH_CODE);
			resultStatus.setResMsg(ErrCode.getMessage(ErrCode.FAIL_AUTH_CODE));
			return resultStatus;
		}
		Map<String, Object> log = new HashMap<>();
		log.put("usrId", infoVO.getUsrId());
		log.put("step", "auth_1won");
		log.put("result", resultMap.toString());
		rsvDao.insertPGateNetLog(log);

		infoVO.setAuthSt("A");
		rsvDao.updateUsrAuthDone(infoVO);
		/**
		 * 6. WLF Search
		 */
		resultMap = PayGateUtil.wlfSearch(API_URL, GUID, KEY_P, memGuid);
		String dataStatus = MapUtils.getString(resultMap, "dataStatus", "");
		if(!dataStatus.equals("SUCCESS")){
			/**
			 * 검증 실패
			 */
			resultStatus.setResCode(ErrCode.FAIL_AUTH_WLF);
			resultStatus.setResMsg(ErrCode.getMessage(ErrCode.FAIL_AUTH_WLF));
			return resultStatus;
		}

		log.put("usrId", infoVO.getUsrId());
		log.put("step", "auth_wlf");
		log.put("result", resultMap.toString());
		rsvDao.insertPGateNetLog(log);
		
		infoVO.setAuthSt("Y");
		rsvDao.updateUsrAuthDone(infoVO);
		return resultStatus;
	}

	@Cacheable(cacheName="bankList", keyGenerator=@KeyGenerator(name="StringCacheKeyGenerator"))
	public Map<String, Object> selectBanks(String type) {
		Map<String, Object> resultMap = new HashMap<>();
		List<Map<String, Object>> bankList = new ArrayList<>();
		if(type.equals("a")){
			bankList = PayGateUtil.getBankList(API_URL, GUID);
		} else if(type.equals("v")){
			bankList = PayGateUtil.getVirBankList(API_URL, GUID);
		} else if(type.equals("r")){
			bankList = rsvDao.selectReturnBank();
		} else if(type.equals("n")){
			bankList = rsvDao.selectVBanks();
		}
		resultMap.put("dataList", bankList);
		return resultMap;
	}

	public Map<String, Object> selectvbank(String string) {
		Map<String, Object> resultMap = new HashMap<>();
		List<Map<String, Object>> bankList = rsvDao.selectVBankOld();
		resultMap.put("dataList", bankList);
		return resultMap;
	}
	
	
}