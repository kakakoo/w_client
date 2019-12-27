package com.greit.weys.bank;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
@RequestMapping("/api/bank")
public class BankController {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private BankService bankService;
	@Autowired
	private UserService userService;

	/**
	 * 은행 테스트 페이지
	 */
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String frd(HttpServletRequest req, HttpServletResponse res,  Map<String, Object> reqMap,  Model model) throws Exception {

		logger.info("===================================== START ===================================");
		logger.info("url ::: " + req.getRequestURL());
		logger.info("req ::: " + reqMap.toString());
		
		logger.info("===================================== END =====================================");
		return "bank/bank";
	}

	/**
	 * redirect callback
	 */
	@RequestMapping(value = "/res", method = RequestMethod.GET)
	public String callback(HttpServletRequest req, HttpServletResponse res, Map<String, Object> reqMap, Model model) throws Exception {

		logger.info("===================================== START ===================================");
		logger.info("url ::: " + req.getRequestURL());
		logger.info("req ::: " + reqMap.toString());
		
		model.addAttribute("res", reqMap.toString());
		logger.info("===================================== END =====================================");
		return "bank/res";
	}
	/**
	 * 은행 인증
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/authorize", method = RequestMethod.POST)
	public ResValue eventList(HttpServletRequest req, HttpServletResponse res, @PathVariable String version, 
			@RequestBody BankVO reqVO) throws Exception {

		logger.info("===================================== START ===================================");
		logger.info("url ::: " + req.getRequestURL());
		logger.info("req ::: " + reqVO.toString());
		
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
		// 토큰값 체크
		TokenValues value = userService.updateCheckToken(req, res, result);
		if(value != null){
			usrId = Integer.parseInt(value.getUserKey());
		}
		
		try {
			Map<String, Object> resultMap = bankService.checkAuth(reqVO);
			result.setResData(resultMap);
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
}
