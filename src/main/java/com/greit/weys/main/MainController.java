package com.greit.weys.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.greit.weys.ResValue;
import com.greit.weys.common.ErrCode;
import com.greit.weys.common.VersionCheck;
import com.greit.weys.config.TokenValues;
import com.greit.weys.user.UserService;

@Controller
@RequestMapping("/api/main")
public class MainController {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private MainService mainService;
	@Autowired
	private UserService userService;

	/**
	 * 메인화면
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}", method = RequestMethod.GET)
	public ResValue main(HttpServletRequest req, HttpServletResponse res, @PathVariable String version) throws Exception {

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
			Map<String, Object> reultMap = new HashMap<>();
			if(ver == 7){
				reultMap = mainService.selectMainInfoV7(value);
			} else {
				reultMap = mainService.selectMainInfo(usrId);
			}
			result.setResData(reultMap);
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
	 * 메인화면
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/unit/{type}", method = RequestMethod.GET)
	public ResValue mainUnit(HttpServletRequest req, HttpServletResponse res, @PathVariable String version
			, @PathVariable String type) throws Exception {

		logger.info("===================================== START ===================================");
		logger.info("url ::: " + req.getRequestURL());
		
		ResValue result = new ResValue();

		// 필수 ::: API 버전 체크
		int ver = VersionCheck.checkVersion(version, 8);
		if(ver == 0){
			result.setResCode(ErrCode.VERSION_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.VERSION_ERROR));
			logger.info("result ::: " + ErrCode.getMessage(ErrCode.VERSION_ERROR));
			logger.info("===================================== END =====================================");
			return result;
		}
		
		// 필수 ::: 필수 파라미터 체크 
		if(!(type.equals("A") || type.equals("M"))){
			result.setResCode(ErrCode.INVALID_PARAMETER);
			result.setResMsg(ErrCode.getMessage(ErrCode.INVALID_PARAMETER));
			logger.info("result ::: " + result.toString());
			logger.info("===================================== END =====================================");
			return result;
		}

		try {
			Map<String, Object> reultMap = mainService.selectMainUnit(type);
			result.setResData(reultMap);
		} catch (Exception e) {
			userService.insertErrLog(e, req, null, null);
			logger.info("error ::: " + e.getMessage());
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}

	/**
	 * 컨텐츠 내용
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/cont", method = RequestMethod.GET)
	public ResValue contents(HttpServletRequest req, HttpServletResponse res, @PathVariable String version) throws Exception {

		logger.info("===================================== START ===================================");
		logger.info("url ::: " + req.getRequestURL());
		
		ResValue result = new ResValue();

		// 필수 ::: API 버전 체크
		int ver = VersionCheck.checkVersion(version, 8);
		if(ver == 0){
			result.setResCode(ErrCode.VERSION_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.VERSION_ERROR));
			logger.info("result ::: " + ErrCode.getMessage(ErrCode.VERSION_ERROR));
			logger.info("===================================== END =====================================");
			return result;
		}

		int usrId = 0;
		// 필수 ::: 토큰값 체크
		TokenValues value = userService.updateCheckToken(req, res, result);
		if(value != null){
			usrId = Integer.parseInt(value.getUserKey());
		}
		
		try {
			Map<String, Object> reultMap = mainService.selectMainCont(usrId);
			result.setResData(reultMap);
		} catch (Exception e) {
			userService.insertErrLog(e, req, null, null);
			logger.info("error ::: " + e.getMessage());
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}

	/**
	 * 알람 리스트
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/alarm", method = RequestMethod.GET)
	public ResValue alarm(HttpServletRequest req, HttpServletResponse res, @PathVariable String version, MainReqVO reqVO) throws Exception {

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

		// 필수 ::: 토큰값 체크
		TokenValues value = userService.updateCheckToken(req, res, result);
		if(value == null){
			logger.info("result ::: " + result.toString());
			logger.info("===================================== END =====================================");
			return result;
		}
		try {
			reqVO.setUsrId(value.getUserKey());
			Map<String, Object> reultMap = new HashMap<>();
			List<MainAlarmVO> resultList = mainService.updateAlarmList(reqVO);
			reultMap.put("dataList", resultList);
			result.setResData(reultMap);
		} catch (Exception e) {
			userService.insertErrLog(e, req, reqVO, value.getUserKey());
			logger.info("error ::: " + e.getMessage());
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}

	/**
	 * 배너 정보 가져오기
	 * 
	 * @param req
	 * @param res
	 * @param reqMap
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/bnr", method = RequestMethod.GET)
	public ResValue getBanner(HttpServletRequest req, HttpServletResponse res, @PathVariable String version, MainReqVO reqVO) throws Exception {

		logger.info("===================================== START ===================================");
		logger.info("url ::: " + req.getRequestURL());
		logger.info("req ::: " + reqVO.getIsNew());
		
		ResValue result = new ResValue();

		// 필수 ::: API 버전 체크
		int ver = VersionCheck.checkVersion(version, 9);
		if(ver == 0){
			result.setResCode(ErrCode.VERSION_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.VERSION_ERROR));
			logger.info("result ::: " + ErrCode.getMessage(ErrCode.VERSION_ERROR));
			logger.info("===================================== END =====================================");
			return result;
		}

		int usrId = 0;
		// 필수 ::: 토큰값 체크
		TokenValues value = userService.updateCheckToken(req, res, result);
		if(value != null){
			usrId = Integer.parseInt(value.getUserKey());
		}
		
		try {
			List<BannerVO> resultVO = new ArrayList<BannerVO>();
			// 배너 정보 가져오기
			if(ver == 9){
				resultVO = mainService.updateBannerInfoV9(usrId, req);
			} else {
				resultVO = mainService.updateBannerInfo(usrId, req, reqVO.getIsNew());
			}
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("dataList", resultVO);
			result.setResData(resultMap);

		} catch (Exception e) {
			userService.insertErrLog(e, req, null, null);
			logger.info("error ::: " + e.getMessage());
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}

	/**
	 * 제휴 배너
	 * 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/cop/bnr", method = RequestMethod.GET)
	public ResValue copBnr(HttpServletRequest req, HttpServletResponse res, @PathVariable String version) throws Exception {

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
		
		// 필수 ::: 토큰값 체크
		TokenValues value = userService.updateCheckToken(req, res, result);
		if(value == null){
			logger.info("result ::: " + result.toString());
			logger.info("===================================== END =====================================");
			return result;
		}
		try {
			List<BannerVO> resultList = mainService.selectCopBnrList();
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("dataList", resultList);
			
			result.setResData(resultMap);
		} catch (Exception e) {
			userService.insertErrLog(e, req, null, value.getUserKey());
			logger.info("error ::: " + e.getMessage());
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}

	/**
	 * 말풍선 가이드
	 * 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/guide", method = RequestMethod.GET)
	public ResValue mainGuide(HttpServletRequest req, HttpServletResponse res, @PathVariable String version) throws Exception {

		logger.info("===================================== START ===================================");
		logger.info("url ::: " + req.getRequestURL());
		ResValue result = new ResValue();

		// 필수 ::: API 버전 체크
		int ver = VersionCheck.checkVersion(version, 10);
		if(ver == 0){
			result.setResCode(ErrCode.VERSION_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.VERSION_ERROR));
			logger.info("result ::: " + ErrCode.getMessage(ErrCode.VERSION_ERROR));
			logger.info("===================================== END =====================================");
			return result;
		}
		
		// 필수 ::: 토큰값 체크
		TokenValues value = userService.updateCheckToken(req, res, result);
		if(value == null){
			logger.info("result ::: " + result.toString());
			logger.info("===================================== END =====================================");
			return result;
		}
		try {
			Map<String, Object> resultMap = mainService.updateGuideInfo(value.getUserKey());
			
			result.setResData(resultMap);
		} catch (Exception e) {
			userService.insertErrLog(e, req, null, value.getUserKey());
			logger.info("error ::: " + e.getMessage());
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}
}
