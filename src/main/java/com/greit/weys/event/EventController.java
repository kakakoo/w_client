package com.greit.weys.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.greit.weys.ResValue;
import com.greit.weys.common.ErrCode;
import com.greit.weys.common.VersionCheck;
import com.greit.weys.config.TokenValues;
import com.greit.weys.user.UserService;

@Controller
@RequestMapping("/api/event")
public class EventController {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private EventService eventService;
	@Autowired
	private UserService userService;
	
	/**
	 * 이벤트 리스트
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}", method = RequestMethod.GET)
	public ResValue eventList(HttpServletRequest req, HttpServletResponse res, @PathVariable String version, EventReqVO reqVO) throws Exception {

		logger.info("===================================== START ===================================");
		logger.info("url ::: " + req.getRequestURL());
		logger.info("req ::: " + reqVO.toString());
		
		ResValue result = new ResValue();

		// 필수 ::: API 버전 체크
		int ver = VersionCheck.checkVersion(version, 7);
		if(ver == 0){
			result.setResCode(ErrCode.VERSION_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.VERSION_ERROR));
			logger.info("result ::: " + ErrCode.getMessage(ErrCode.VERSION_ERROR));
			logger.info("===================================== END =====================================");
			return result;
		}
		
		int usrId = 0;
		// 토큰값 체크
		TokenValues value = userService.updateCheckToken(req, res, result);
		if(value != null){
			usrId = Integer.parseInt(value.getUserKey());
		}
		
		try {
			List<EventVO> resultList = new ArrayList<EventVO>();
			
			if(ver >= 10){
				if(usrId > 0){
					reqVO.setUsrId(usrId);
					resultList = eventService.selectEventListUsr(reqVO);
				} else {
					resultList = eventService.selectEventList();
				}
			} else {
				resultList = eventService.selectEventList();
			}

			Map<String, Object> reultMap = new HashMap<>();
			reultMap.put("dataList", resultList);
			result.setResData(reultMap);
		} catch (Exception e) {
			userService.insertErrLog(e, req, reqVO, usrId == 0 ? null : usrId + "");
			logger.info("error ::: " + e.getMessage());
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}

	/**
	 * 이벤트 상세
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/{eventId}", method = RequestMethod.GET)
	public ResValue event(HttpServletRequest req, HttpServletResponse res, @PathVariable String version
			, @PathVariable String eventId) throws Exception {

		logger.info("===================================== START ===================================");
		logger.info("url ::: " + req.getRequestURL());
		
		ResValue result = new ResValue();

		// 필수 ::: API 버전 체크
		int ver = VersionCheck.checkVersion(version, 7);
		if(ver == 0){
			result.setResCode(ErrCode.VERSION_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.VERSION_ERROR));
			logger.info("result ::: " + ErrCode.getMessage(ErrCode.VERSION_ERROR));
			logger.info("===================================== END =====================================");
			return result;
		}

		int usrId = 0;
		// 토큰값 체크
		TokenValues value = userService.updateCheckToken(req, res, result);
		if(value != null){
			usrId = Integer.parseInt(value.getUserKey());
		}
		
		try {
			EventReqVO reqVO = new EventReqVO();
			reqVO.setUsrId(usrId);
			reqVO.setEventId(Integer.parseInt(eventId));
			EventVO event = eventService.selectEvent(reqVO);
			result.setResData(event);
		} catch (Exception e) {
			userService.insertErrLog(e, req, null, value == null ? null : value.getUserKey());
			logger.info("error ::: " + e.getMessage());
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}
	
	/**
	 * 이벤트 참여 페이지
	 */
	@RequestMapping(value = "/{eventId}/join/{ak}", method = RequestMethod.GET)
	public String frd(HttpServletRequest req, HttpServletResponse res, @PathVariable String eventId, @PathVariable String ak
			, Model model) throws Exception {

		logger.info("===================================== START ===================================");
		logger.info("url ::: " + req.getRequestURL());
		
		Map<String, Object> resultMap = eventService.selectEventInfo(ak, eventId);
		String page = MapUtils.getString(resultMap, "page");

		model.addAttribute("info", resultMap);
		model.addAttribute("eventId", eventId);
		logger.info("result ::: " + resultMap.toString());
		logger.info("===================================== END =====================================");
		return "event/" + page;
	}

	/**
	 * 이벤트 참여
	 */
	@ResponseBody
	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public Map<String, Object> eventJoin(HttpServletRequest req, HttpServletResponse res, @RequestBody EventVO reqVO) throws Exception {

		logger.info("===================================== START ===================================");
		logger.info("url ::: " + req.getRequestURL());
		logger.info("req ::: " + reqVO.toString());
		
		ResValue result = new ResValue();
		Map<String, Object> resultMap = new HashMap<>();

		// 필수 ::: 토큰값 체크
		if(!reqVO.checkJoinEvent()){
			resultMap.put("result", "fail");
			return resultMap;
		}
		
		try {
			resultMap = eventService.insertJoinEvent(reqVO);
			
		} catch (Exception e) {
			logger.info("error ::: " + e.getMessage());
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + resultMap.toString());
		logger.info("===================================== END =====================================");
		return resultMap;
	}
	
	/**
	 * 이벤트 참여
	 */
	@ResponseBody
	@RequestMapping(value = "/coupon", method = RequestMethod.POST)
	public Map<String, Object> eventCoupon(HttpServletRequest req, HttpServletResponse res, @RequestBody EventVO reqVO) throws Exception {

		logger.info("===================================== START ===================================");
		logger.info("url ::: " + req.getRequestURL());
		logger.info("req ::: " + reqVO.toString());
		
		ResValue result = new ResValue();
		Map<String, Object> resultMap = new HashMap<>();

		try {
			resultMap = eventService.insertEventCoupon(reqVO);
			
		} catch (Exception e) {
			logger.info("error ::: " + e.getMessage());
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + resultMap.toString());
		logger.info("===================================== END =====================================");
		return resultMap;
	}
}
