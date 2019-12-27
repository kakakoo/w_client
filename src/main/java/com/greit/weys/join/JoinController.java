package com.greit.weys.join;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.greit.weys.ResValue;
import com.greit.weys.common.ErrCode;
import com.greit.weys.common.VersionCheck;
import com.greit.weys.user.UserDetailVO;
import com.greit.weys.user.UserService;

@Controller
@RequestMapping("/api")
public class JoinController {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private JoinService joinService;
	@Autowired
	private UserService userService;

	/**
	 * 회원가입
	 * @param req
	 * @param res
	 * @param reqMap(id, password)
	 * @return 사용자 로그인 후 Token값 반환
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value ="/{version}/signup", method=RequestMethod.POST)
	public ResValue signup(HttpServletRequest req, HttpServletResponse res, @RequestBody UserDetailVO reqVO
			,@PathVariable String version) throws Exception {

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

		if(!reqVO.checkSignup()){
			result.setResCode(ErrCode.INVALID_PARAMETER);
			result.setResMsg(ErrCode.getMessage(ErrCode.INVALID_PARAMETER));
			logger.info("result ::: " + result.toString());
			logger.info("===================================== END =====================================");
			return result;
		}
		
		try{
			// 회원가입 
//			result = joinService.insertJoinUser(reqVO, ver);
		} catch (DataAccessException e) {
			userService.insertErrLog(e, req, null, null);
			logger.info("DataAccessException error ::: " + e.getMessage());
			result.setResCode(ErrCode.DISABLE_EMOTICON);
			result.setResMsg(ErrCode.getMessage(ErrCode.DISABLE_EMOTICON));
		} catch(Exception e) {
			userService.insertErrLog(e, req, reqVO, null);
			logger.info("error ::: " + e.getMessage());
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}

	/**
	 * 친구초대 코드 확인
	 * @param req
	 * @param res
	 * @param reqMap(id, password)
	 * @return 사용자 로그인 후 Token값 반환
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value ="/{version}/frd", method=RequestMethod.POST)
	public ResValue frd(HttpServletRequest req, HttpServletResponse res, @RequestBody UserDetailVO reqVO
			,@PathVariable String version) throws Exception {

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

		if(reqVO.getFrdCd() == null){
			result.setResCode(ErrCode.INVALID_PARAMETER);
			result.setResMsg(ErrCode.getMessage(ErrCode.INVALID_PARAMETER));
			logger.info("result ::: " + result.toString());
			logger.info("===================================== END =====================================");
			return result;
		}
		
		try{
			int resCnt = joinService.checkFrd(reqVO.getFrdCd());
			
			if(resCnt == 0){
				result.setResCode(ErrCode.FRDCODE_NOTEXIST);
				result.setResMsg(ErrCode.getMessage(ErrCode.FRDCODE_NOTEXIST));
			}
			
		} catch(Exception e) {
			userService.insertErrLog(e, req, reqVO, null);
			logger.info("error ::: " + e.getMessage());
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}

}
