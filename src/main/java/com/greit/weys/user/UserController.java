package com.greit.weys.user;

import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.greit.weys.ResValue;
import com.greit.weys.common.ErrCode;
import com.greit.weys.common.VersionCheck;
import com.greit.weys.config.TokenValues;

@Controller
@RequestMapping("/api")
public class UserController {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private UserService userService;
	
	/**
	 * 휴대번호 확인
	 * 
	 * @param req
	 * @param res
	 * @param reqMap(nation, usrTel)
	 * @return 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/checkTel", method = RequestMethod.GET)
	public ResValue checkTel(HttpServletRequest req, HttpServletResponse res, UsrVO reqVO, @PathVariable String version) throws Exception {

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

		/**
		 * 파라메터 체크
		 */
		if (!reqVO.checkTel()) {
			result.setResCode(ErrCode.INVALID_PARAMETER);
			result.setResMsg(ErrCode.getMessage(ErrCode.INVALID_PARAMETER));
			logger.info("result ::: " + result.toString());
			logger.info("===================================== END =====================================");
			return result;
		}

		try {
			Map<String, Object> resultMap = userService.updateCheckUsrTel(reqVO);
			
			result.setResData(resultMap);
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}

	/**
	 * 로그인
	 * 
	 * @param req
	 * @param res
	 * @param reqMap(id,
	 *            password)
	 * @return 사용자 로그인 후 Token값 반환
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/login", method = RequestMethod.POST)
	public ResValue loginV5(HttpServletRequest req, HttpServletResponse res, @RequestBody UsrVO reqVO, @PathVariable String version) throws Exception {

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

		if(ver == 7){
			if (!reqVO.checkLoginV7()) {
				result.setResCode(ErrCode.INVALID_PARAMETER);
				result.setResMsg(ErrCode.getMessage(ErrCode.INVALID_PARAMETER));
				logger.info("result ::: " + result.toString());
				logger.info("===================================== END =====================================");
				return result;
			}
		} else {
			if (!reqVO.checkLogin()) {
				result.setResCode(ErrCode.INVALID_PARAMETER);
				result.setResMsg(ErrCode.getMessage(ErrCode.INVALID_PARAMETER));
				logger.info("result ::: " + result.toString());
				logger.info("===================================== END =====================================");
				return result;
			}
		}

		try {
			result = userService.insertUserInfoByLogin(reqVO);
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}

	/**
	 * 자동 로그인
	 * 
	 * @param req
	 * @param res
	 * @param reqMap(id,
	 *            password)
	 * @return 사용자 자동 로그인
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/auto", method = RequestMethod.POST)
	public ResValue auto(HttpServletRequest req, HttpServletResponse res, @RequestBody UserDetailVO reqVO,
			@PathVariable String version) throws Exception {

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
		if (value == null) {
			logger.info("result ::: " + result.toString());
			logger.info("===================================== END =====================================");
			return result;
		}

		try {
			// 자동 로그인
			reqVO.setUsrId(Integer.parseInt(value.getUserKey()));
			result = userService.updateAutoLogin(reqVO);
		} catch (Exception e) {
			logger.info("error ::: " + e.getMessage());
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}

	/**
	 * 자동 로그인
	 * 
	 * @param req
	 * @param res
	 * @param reqMap(id,
	 *            password)
	 * @return 사용자 자동 로그인
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/unknown", method = RequestMethod.POST)
	public ResValue unknown(HttpServletRequest req, HttpServletResponse res, @RequestBody UserDetailVO reqVO,
			@PathVariable String version) throws Exception {

		logger.info("===================================== START ===================================");
		logger.info("url ::: " + req.getRequestURL());
		logger.info("req ::: " + reqVO.toString());
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
		if( !reqVO.checkUnknown() ){
			result.setResCode(ErrCode.INVALID_PARAMETER);
			result.setResMsg(ErrCode.getMessage(ErrCode.INVALID_PARAMETER));
			logger.info("result ::: " + result.toString());
			logger.info("===================================== END =====================================");
			return result;
		}
		
		try {
			// 비회원 토큰 등록
			Map<String, Object> resultMap = userService.updateUnknown(reqVO);
			result.setResData(resultMap);
		} catch (Exception e) {
			logger.info("error ::: " + e.getMessage());
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}

	/**
	 * 비밀번호 찾기
	 * 
	 * @param req
	 * @param res
	 * @param reqMap(nation, usrTel)
	 * @return 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/findPw", method = RequestMethod.GET)
	public ResValue findPw(HttpServletRequest req, HttpServletResponse res, UsrVO reqVO, @PathVariable String version) throws Exception {

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

		/**
		 * 파라메터 체크
		 */
		if (!reqVO.findPw()) {
			result.setResCode(ErrCode.INVALID_PARAMETER);
			result.setResMsg(ErrCode.getMessage(ErrCode.INVALID_PARAMETER));
			logger.info("result ::: " + result.toString());
			logger.info("===================================== END =====================================");
			return result;
		}

		try {
			Map<String, Object> resultMap = userService.updateFinePwd(reqVO);
			if(resultMap == null){
				result.setResCode(ErrCode.USRTEL_NOTEXIST);
				result.setResMsg(ErrCode.getMessage(ErrCode.USRTEL_NOTEXIST));
			} else {
				result.setResData(resultMap);
			}
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}

	/**
	 * 비밀번호 변경
	 * 
	 * @param req
	 * @param res
	 * @param reqMap(nation, usrTel)
	 * @return 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/setPw", method = RequestMethod.PUT)
	public ResValue setPw(HttpServletRequest req, HttpServletResponse res, @RequestBody UsrVO reqVO, @PathVariable String version) throws Exception {

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

		/**
		 * 파라메터 체크
		 */
		if (!reqVO.checkSetPw()) {
			result.setResCode(ErrCode.INVALID_PARAMETER);
			result.setResMsg(ErrCode.getMessage(ErrCode.INVALID_PARAMETER));
			logger.info("result ::: " + result.toString());
			logger.info("===================================== END =====================================");
			return result;
		}

		try {
			int resCnt = userService.updateUsrPw(reqVO);
			if(resCnt == 0) {
				result.setResCode(ErrCode.UNKNOWN_ERROR);
				result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
			}
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}

	/**
	 * 실명인증 확인
	 * 
	 * @param req
	 * @param res
	 * @param reqMap(nation, usrTel)
	 * @return 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/check/certify", method = RequestMethod.GET)
	public ResValue checkCertify(HttpServletRequest req, HttpServletResponse res, UsrVO reqVO, @PathVariable String version) throws Exception {

		logger.info("===================================== START ===================================");
		logger.info("url ::: " + req.getRequestURL());
		logger.info("req ::: " + reqVO.toString());
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

		/**
		 * 파라메터 체크
		 */
		if (!reqVO.checkUsrCertify()) {
			result.setResCode(ErrCode.INVALID_PARAMETER);
			result.setResMsg(ErrCode.getMessage(ErrCode.INVALID_PARAMETER));
			logger.info("result ::: " + result.toString());
			logger.info("===================================== END =====================================");
			return result;
		}

		// 필수 ::: 토큰값 체크
		TokenValues value = userService.updateCheckToken(req, res, result);
		if (value == null) {
			logger.info("result ::: " + result.toString());
			logger.info("===================================== END =====================================");
			return result;
		}

		try {
			reqVO.setUsrId(Integer.parseInt(value.getUserKey()));

			String resString = userService.checkUsrCertify(reqVO);
			if(resString.equals("false")) {
				result.setResCode(ErrCode.RSV_IDENTIFY_INCORRECT);
				result.setResMsg(ErrCode.getMessage(ErrCode.RSV_IDENTIFY_INCORRECT));
			} else if (resString.equals("success")){
			} else {
				result.setResCode(ErrCode.IDENTIFY_EXIST);
				result.setResMsg(ErrCode.getMessage(ErrCode.IDENTIFY_EXIST));
				Map<String, Object> resultMap = new HashMap<>();
				resultMap.put("usrTel", resString);
				result.setResData(resultMap);
			}
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}

	/**
	 * 실명인증 등록
	 * 
	 * @param req
	 * @param res
	 * @param reqMap(nation, usrTel)
	 * @return 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/usr/certify", method = RequestMethod.PUT)
	public ResValue usrCertify(HttpServletRequest req, HttpServletResponse res, @RequestBody UsrVO reqVO, @PathVariable String version) throws Exception {

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

		/**
		 * 파라메터 체크
		 */
		if (!reqVO.checkUsrCertify()) {
			result.setResCode(ErrCode.INVALID_PARAMETER);
			result.setResMsg(ErrCode.getMessage(ErrCode.INVALID_PARAMETER));
			logger.info("result ::: " + result.toString());
			logger.info("===================================== END =====================================");
			return result;
		}

		// 필수 ::: 토큰값 체크
		TokenValues value = userService.updateCheckToken(req, res, result);
		if (value == null) {
			logger.info("result ::: " + result.toString());
			logger.info("===================================== END =====================================");
			return result;
		}

		try {
			reqVO.setUsrId(Integer.parseInt(value.getUserKey()));
			if(ver <= 9){
				String resString = userService.updateUsrCertifyV9(reqVO);
				if(resString.equals("false")) {
					result.setResCode(ErrCode.RSV_IDENTIFY_INCORRECT);
					result.setResMsg(ErrCode.getMessage(ErrCode.RSV_IDENTIFY_INCORRECT));
				} else if (resString.equals("success")){
				} else {
					result.setResCode(ErrCode.IDENTIFY_EXIST);
					result.setResMsg(ErrCode.getMessage(ErrCode.IDENTIFY_EXIST));
					Map<String, Object> resultMap = new HashMap<>();
					resultMap.put("usrTel", resString);
					result.setResData(resultMap);
				}
			} else {
				int resCnt = userService.updateUsrCertify(reqVO);
				if(resCnt == 0) {
					result.setResCode(ErrCode.UNKNOWN_ERROR);
					result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
				}
			}
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}

	/**
	 * 로그아웃
	 * 
	 * @param req
	 * @param res
	 * @param reqMap(id,
	 *            password)
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/logout", method = RequestMethod.POST)
	public ResValue logout(HttpServletRequest req, HttpServletResponse res, @PathVariable String version) throws Exception {

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
		if (value == null) {
			logger.info("result ::: " + result.toString());
			logger.info("===================================== END =====================================");
			return result;
		}

		try {
			if(ver > 7){
				int unKey = userService.updateLogout(value.getUserKey());
				if (unKey > 0) {
					Map<String, Object> resultMap = new HashMap<String, Object>();
					resultMap.put("unKey", unKey);
					resultMap.put("usrAk", "unknown");
					result.setResData(resultMap);
				} else {
					result.setResCode(ErrCode.UNKNOWN_ERROR);
					result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
				}
			} else {
				int resCnt = userService.updateLogoutV7(value.getUserKey());
				if (resCnt > 0) {
					Map<String, Object> resultMap = new HashMap<String, Object>();
					result.setResData(resultMap);
				} else {
					result.setResCode(ErrCode.UNKNOWN_ERROR);
					result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
				}
			}

		} catch (Exception e) {
			logger.info("error ::: " + e.getMessage());
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}

	/**
	 * 버전체크
	 * 
	 * @param req
	 * @param res
	 * @param reqMap(id,
	 *            password)
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/check", method = RequestMethod.GET)
	public ResValue versionCheck(HttpServletRequest req, HttpServletResponse res, @PathVariable String version,
			@RequestParam Map<String, Object> reqMap) throws Exception {

		logger.info("===================================== START ===================================");
		logger.info("url ::: " + req.getRequestURL());
		logger.info("req ::: " + reqMap.toString());
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

		try {
			String os = MapUtils.getString(reqMap, "os");
			Map<String, Object> resultMap = userService.getVersionInfo(os);
			result.setResData(resultMap);

		} catch (Exception e) {
			logger.info("error ::: " + e.getMessage());
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}

	/**
	 * 버전체크
	 * 
	 * @param req
	 * @param res
	 * @param reqMap(id,
	 *            password)
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "usrChk", method = RequestMethod.GET)
	public ResValue usrCheck(HttpServletRequest req, HttpServletResponse res,
			@RequestParam Map<String, Object> reqMap) throws Exception {

		logger.info("===================================== START ===================================");
		logger.info("url ::: " + req.getRequestURL());
		logger.info("req ::: " + reqMap.toString());
		ResValue result = new ResValue();

		try {
			Map<String, Object> resultMap = userService.getUsrChannelInfo(reqMap);
			result.setResData(resultMap);

		} catch (Exception e) {
			logger.info("error ::: " + e.getMessage());
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}
	
	/**
	 * 튜토리얼 이미지
	 * 
	 * @param req
	 * @param res
	 * @param reqMap(nation, usrTel)
	 * @return 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/tutorial", method = RequestMethod.GET)
	public ResValue tutorial(HttpServletRequest req, HttpServletResponse res, @PathVariable String version) throws Exception {

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

		try {
			Map<String, Object> resultMap = userService.selectTutorial();
			result.setResData(resultMap);
		} catch (Exception e) {
			logger.error("error ::: " + e.getMessage());
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}
	
	/**
	 * 로그인
	 * 
	 * @param req
	 * @param res
	 * @param reqMap(id,
	 *            password)
	 * @return 사용자 로그인 후 Token값 반환
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/kakao_auth", method = RequestMethod.POST)
	public ResValue kakao_auth(HttpServletRequest req, HttpServletResponse res) throws Exception {

		logger.info("===================================== START ===================================");
		logger.info("url ::: " + req.getRequestURL());
		ResValue result = new ResValue();

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}
	

	@RequestMapping(value = "/test")
	public String te(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {

		logger.info("===================================== START : " + " ===================================");
		logger.info("url ::: " + request.getRequestURL());

//		userService.updateFrdCd();
		
		logger.info("===================================== END =====================================");
		return "push/push";
	}

}
