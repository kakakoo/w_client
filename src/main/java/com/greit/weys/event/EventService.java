package com.greit.weys.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.ehcache.annotations.Cacheable;
import com.greit.weys.mypage.MypageDao;

@Service
public class EventService {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private EventDao eventDao;
	@Autowired
	private MypageDao mypageDao;
	
	public List<EventVO> selectEventListUsr(EventReqVO reqVO) {
		return eventDao.selectEventListUsr(reqVO);
	}

	@Cacheable(cacheName="eventList")
	public List<EventVO> selectEventList() {
		return eventDao.selectEventList();
	}
	
	public EventVO selectEvent(EventReqVO reqVO) {
		return eventDao.selectEvent(reqVO);
	}

	public Map<String, Object> insertJoinEvent(EventVO reqVO) {
		
		Map<String, Object> resultMap = new HashMap<>();
		
//		int cnt = eventDao.selectJoinCnt(reqVO);
//		if(cnt > 0){
//			resultMap.put("result", "joined");
//			return resultMap;
//		}
		
		int cnt = eventDao.insertJoinEvent(reqVO);
		if(cnt > 0){
			resultMap.put("result", "success");
		}
		
		return resultMap;
	}

	public Map<String, Object> selectEventInfo(String ak, String eventId) {
		
		Integer usrId = 0;
		int joined = 0;
		
		int eventUser = 0;
		int eventCnt = eventDao.selectEventCnt(eventId);
		if(!ak.equals("_USRAK_")){
			usrId = mypageDao.selectUsrId(ak);
			EventVO reqVO = new EventVO();
			reqVO.setUsrId(usrId);
			reqVO.setEventId(Integer.parseInt(eventId));
			joined = eventDao.selectJoinCnt(reqVO);
			eventUser = eventDao.selectEventUsed(reqVO);
		}
		String page = eventDao.selectEventPage(eventId);
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("joined", joined);
		resultMap.put("page", page);
		resultMap.put("usrId", usrId);
		resultMap.put("eventUser", eventUser);
		resultMap.put("eventCnt", eventCnt);
		
		return resultMap;
	}

	public Map<String, Object> insertEventCoupon(EventVO reqVO) {
		
		int resCnt = eventDao.insertEventCoupon(reqVO);
		if(resCnt > 0){
			int cnt = (int) (Math.random() * 10) + 10;
			reqVO.setUsed(cnt);
			resCnt = eventDao.updateEventCnt(reqVO);
		}
		
		Map<String, Object> resultMap = new HashMap<>();
		if(resCnt > 0){
			resultMap.put("result", "success");
		}
		
		return resultMap;
	}
	
}
