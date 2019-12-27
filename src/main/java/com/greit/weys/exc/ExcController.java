package com.greit.weys.exc;

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
@RequestMapping("/api/exc")
public class ExcController {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private ExcService excService;
	@Autowired
	private UserService userService;

	/**
	 * 환율정보 가져오기 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value ="/{version}/{unit}", method=RequestMethod.GET)
	public ResValue getExchange(HttpServletRequest req, HttpServletResponse res, @PathVariable String version
			, @PathVariable String unit) throws Exception {

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

		// 토큰값 체크
		TokenValues value = userService.updateCheckToken(req, res, result);
		
		try{
			// 최근 특정 화폐 정보 가져오기 
			Map<String, Object> resultMap = excService.selectExchange(unit);
			result.setResData(resultMap);
		} catch(Exception e) {
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
	 * 환율정보 리스트 가져오기 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value ="/{version}/{unit}/latest", method=RequestMethod.GET)
	public ResValue getExchangeList(HttpServletRequest req, HttpServletResponse res, @PathVariable String version
			, @PathVariable String unit, String month) throws Exception {

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

		// 토큰값 체크
		TokenValues value = userService.updateCheckToken(req, res, result);
		
		try{
			// 최근 특정 화폐 정보 가져오기 
			List<Map<String, Object>> rateList = excService.selectExchangeList(unit, month);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("dataList", rateList);
			
			result.setResData(resultMap);
			
		} catch(Exception e) {
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
	 * 메인 리스트
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value ="/{version}/list/{type}", method=RequestMethod.GET)
	public ResValue mainList(HttpServletRequest req, HttpServletResponse res, @PathVariable String version
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
		
		try{
			// 최근 특정 화폐 정보 가져오기 
			List<Map<String, Object>> rateList = excService.selectMainExchangeList(type);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("dataList", rateList);
			
			result.setResData(resultMap);
			
		} catch(Exception e) {
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
	 * 환율 계산 로그 저장
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/calc", method = RequestMethod.POST)
	public ResValue calcLog(HttpServletRequest req, HttpServletResponse res, @PathVariable String version
			,@RequestBody Map<String, Object> reqVO) throws Exception {

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
			reqVO.put("usrId", value.getUserKey());
			excService.insertCalcLog(reqVO);
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
}
