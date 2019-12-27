package com.greit.weys.main;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.KeyGenerator;
import com.greit.weys.common.Constant;
import com.greit.weys.config.TokenValues;

@Service
public class MainService {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private MainDao mainDao;
	@Value("#{props['ENC.KEY']}")
	private String ENC_KEY;
	
	public Map<String, Object> selectMainInfo(int usrId) {
		/**
		 * 사용자 기본 정보
		 */
		Map<String, Object> resultMap = new HashMap<>();
		
		/**
		 * 알람 안읽은 건수
		 */
		int armCnt = 0;
		if(usrId > 0){
			armCnt = mainDao.selectMainArmCnt(usrId + "");
		}
		
		String mainTitle = mainDao.selectMainTitle();

		resultMap.put("armCnt", armCnt);
		resultMap.put("title", mainTitle);
		
		return resultMap;
	}
	
	public Map<String, Object> selectMainInfoV7(TokenValues value) {
		/**
		 * 사용자 기본 정보
		 */
		Map<String, Object> resultMap = new HashMap<>();
		
		/**
		 * 예약 리스트
		 */
		Map<String, Object> reqMap = new HashMap<>();
		reqMap.put("usrId", value.getUserKey());
		reqMap.put("encKey", ENC_KEY);
		List<MainRsvVO> rsvList = mainDao.selectMainRsvList(reqMap);
		
		/**
		 * 예약 중인 건수
		 */
		int rsvCnt = mainDao.selectMainRsvCnt(value.getUserKey());
		
		/**
		 * 알람 안읽은 건수
		 */
		int armCnt = mainDao.selectMainArmCnt(value.getUserKey());
		
		/**
		 * 사용 가능한 쿠폰 수
		 */
		int cpnCnt = mainDao.selectMainCpnCnt(value.getUserKey());
		
		/**
		 * 진행중인 이벤트
		 */
		int evtCnt = mainDao.selectMainEventCnt(value.getUserKey());

		resultMap.put("rsvList", rsvList);
		resultMap.put("rsvCnt", rsvCnt);
		resultMap.put("armCnt", armCnt);
		resultMap.put("cpnCnt", cpnCnt);
		resultMap.put("evtCnt", evtCnt);
		
		return resultMap;
	}

	public List<MainAlarmVO> updateAlarmList(MainReqVO reqVO) {
		/**
		 * 알람리스트 호출 이후 알람 읽음처리
		 */
		reqVO.setVersion(Constant.S_SERVER_VERSION);
		List<MainAlarmVO> resultList = mainDao.selectAlarmList(reqVO);
		mainDao.updateAlarmSt(reqVO.getUsrId());
		
		return resultList;
	}

	public List<BannerVO> updateBannerInfoV9(int usrId, HttpServletRequest req) {
		
		String agent = req.getHeader("User-Agent");
		String os = "A";
		
		if (agent.toLowerCase().contains("iphone") || agent.toLowerCase().contains("ios")){
			os = "I";
		}
		
		Map<String, Object> reqMap = new HashMap<>();
		reqMap.put("os", os);
		if(usrId > 0){
			reqMap.put("usrId", usrId);
		}
		/**
		 * 이벤트 참여한 사람은 베너 안뜨게
		 */
		mainDao.insertUsrLog(reqMap);
		
		List<BannerVO> resultList = mainDao.selectBannerInfoV9(Constant.S_SERVER_VERSION); 
		Iterator<BannerVO> iter = resultList.iterator();
		
		while(iter.hasNext()){
			BannerVO tmp = iter.next();
			if(tmp.getRedirectApp().equals("event") && usrId > 0){
				String target = tmp.getTarget();
				target = target.replaceAll("/api/event/" + Constant.S_SERVER_VERSION + "/", "");
				reqMap.put("target", target);
				
				int eventUsr = mainDao.selectUsrJoinEvent(reqMap);
				if(eventUsr > 0){
					iter.remove();
				}
			}
		}
		return resultList;
	}

	public List<BannerVO> updateBannerInfo(int usrId, HttpServletRequest req, String isNew) {
		
		String agent = req.getHeader("User-Agent");
		String os = "A";
		
		if (agent.toLowerCase().contains("iphone") || agent.toLowerCase().contains("ios")){
			os = "I";
		}
		
		Map<String, Object> reqMap = new HashMap<>();
		reqMap.put("os", os);
		
		String usrAk = "_USRAK_";
		if(usrId > 0){
			usrAk = mainDao.selectUsrAk(usrId);
			reqMap.put("usrId", usrId);
		}
		/** 
		 * 이벤트 참여한 사람은 베너 안뜨게
		 */
		mainDao.insertUsrLog(reqMap);
		
		reqMap.put("rp", Constant.S_SERVER_VERSION);
		reqMap.put("tp", "A");
		reqMap.put("usrAk", usrAk);
		
		List<BannerVO> resultList = mainDao.selectBannerInfo(reqMap); 
		Iterator<BannerVO> iter = resultList.iterator();
		
		while(iter.hasNext()){
			BannerVO tmp = iter.next();
			if(tmp.getRedirectApp().equals("event") && usrId > 0){
				String target = tmp.getTarget();
				target = target.replaceAll("/api/event/" + Constant.S_SERVER_VERSION + "/", "");
				reqMap.put("target", target);
				
				int eventUsr = mainDao.selectUsrJoinEvent(reqMap);
				if(eventUsr > 0){
					iter.remove();
				}
			}
		}
		
		/**
		 * 회원가입시 배너 추가
		 */
		if(isNew != null && isNew.equals("Y")){
			reqMap.put("tp", "N");
			List<BannerVO> newList = mainDao.selectBannerInfo(reqMap);
			if(newList.size() > 0){
				resultList.add(0, newList.get(0));
			}
		}
		return resultList;
	}

	public List<BannerVO> selectCopBnrList() {
		return mainDao.selectCopBnrList();
	}

	public Map<String, Object> selectMainCont(int usrId) {
		
		List<Map<String, Object>> contList = mainDao.selectContInfo();
		
		for(Map<String, Object> tmp : contList){
			int contId = MapUtils.getIntValue(tmp, "CONT_ID");
			
			Map<String, Object> reqMap = new HashMap<>();
			reqMap.put("contId", contId);
			String usrAk = "unknown";
			if(usrId != 0){
				usrAk = mainDao.selectUsrAk(usrId);
			}
			reqMap.put("usrAk", usrAk);
			
			List<Map<String, Object>> dataList = mainDao.selectContList(reqMap);
			tmp.put("dataList", dataList);
		}
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("contList", contList);
		return resultMap;
	}

	@Cacheable(cacheName="mainUnit", keyGenerator=@KeyGenerator(name="StringCacheKeyGenerator"))
	public Map<String, Object> selectMainUnit(String type) {
		
		Map<String, Object> reqMap = new HashMap<>();
		reqMap.put("tp", type);
		List<Map<String, Object>> unitList = mainDao.selectRsvUnit(reqMap);
		
		if(unitList.size() > 5 && type.equals("M")){
			unitList = unitList.subList(0, 6);
			Map<String, Object> otherMap = new HashMap<>();
			otherMap.put("unitNm", "이외 다른 통화");
			otherMap.put("unitCd", "");
			unitList.add(otherMap);
		}
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("dataList", unitList);
		return resultMap;
	}

	public Map<String, Object> updateGuideInfo(String userKey) {
		
		Map<String, Object> resultMap = mainDao.selectGuideInfo(userKey);
		
		int logId = MapUtils.getIntValue(resultMap, "LOG_ID");
		int res = mainDao.updateGuideLog(logId);
		
		return resultMap;
	}
}
