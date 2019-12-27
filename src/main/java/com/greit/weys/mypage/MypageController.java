package com.greit.weys.mypage;

import java.util.HashMap;
import java.util.List;
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
import com.greit.weys.user.UsrVO;

@Controller
@RequestMapping("/api/mypage")
public class MypageController {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private MypageService mypageService;
	@Autowired
	private UserService userService;

	/**
	 * 회원 탈퇴
	 * 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}", method = RequestMethod.DELETE)
	public ResValue deleteUsr(HttpServletRequest req, HttpServletResponse res, @PathVariable String version
			, @RequestBody UsrVO reqVO) throws Exception {

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
			// 사용자 삭제
			reqVO.setUsrId(Integer.parseInt(value.getUserKey()));
			int cnt = 0;
			if(ver == 7){
				cnt = mypageService.updateUserDeleteV7(reqVO);
			} else {
				cnt = mypageService.updateUserDelete(reqVO);
			}

			if (cnt > 0) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				if(ver > 7){
					resultMap.put("unKey", cnt);
					resultMap.put("usrAk", "unknown");
				}
				result.setResData(resultMap);
			} else if (cnt == -1) {
				result.setResCode(ErrCode.PWD_INCORRECT);
				result.setResMsg(ErrCode.getMessage(ErrCode.PWD_INCORRECT));
			} else {
				result.setResCode(ErrCode.UNKNOWN_ERROR);
				result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
			}

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
	 * 공지사항 리스트
	 * 
	 * @param req
	 * @param res
	 * @param reqMap
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/notice", method = RequestMethod.GET)
	public ResValue getNotices(HttpServletRequest req, HttpServletResponse res, @PathVariable String version) throws Exception {

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
		
		try {
			// 공지사항 리스트 가져오기
			List<NoticeVO> resultList = mypageService.selectNoticeList();
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("dataList", resultList);
			result.setResData(resultMap);

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
	 * 공지사항 상세
	 * 
	 * @param req
	 * @param res
	 * @param reqMap
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/notice/{notice_id}", method = RequestMethod.GET)
	public ResValue getNoticeDetail(HttpServletRequest req, HttpServletResponse res, @PathVariable String version
			, @PathVariable String notice_id) throws Exception {

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
		
		try {
			// 공지사항 상세보기
			NoticeVO resultMap = mypageService.selectNotice(notice_id);
			result.setResData(resultMap);

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
	 * 비밀번호 변경
	 * 
	 * @param req
	 * @param res
	 * @param reqMap
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/change/pwd", method = RequestMethod.PUT)
	public ResValue changePwd(HttpServletRequest req, HttpServletResponse res, @PathVariable String version
			, @RequestBody ChangeVO reqVO) throws Exception {

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
			reqVO.setUsrId(value.getUserKey());
			int resCnt = mypageService.updateUsrPwd(reqVO);
			
			if(resCnt == -1){
				result.setResCode(ErrCode.PWD_INCORRECT);
				result.setResMsg(ErrCode.getMessage(ErrCode.PWD_INCORRECT));
			} else if(resCnt == 0){
				result.setResCode(ErrCode.UNKNOWN_ERROR);
				result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
			}
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
	 * 사용자 정보
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}", method = RequestMethod.GET)
	public ResValue userInfo(HttpServletRequest req, HttpServletResponse res, @PathVariable String version) throws Exception {

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
			ProfileVO info = new ProfileVO();
			if(ver == 7){
				info = mypageService.selectUserInfoV7(value.getUserKey());
			} else {
				info = mypageService.selectUserInfo(value.getUserKey());
			}
			result.setResData(info);
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
	 * 전화번호 변경
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/tel", method = RequestMethod.PUT)
	public ResValue changeTel(HttpServletRequest req, HttpServletResponse res, @PathVariable String version
			, @RequestBody ChangeVO reqVO) throws Exception {

		logger.info("===================================== START ===================================");
		logger.info("url ::: " + req.getRequestURL());
		logger.info("req ::: " + reqVO.getUsrTel());
		
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
			int resCnt = mypageService.updateUsrTel(reqVO);
			if(resCnt == 0){
				logger.info("error ::: 전화번호 변경 실패");
				result.setResCode(ErrCode.UNKNOWN_ERROR);
				result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
			} else if(resCnt == -1){
				logger.info("error ::: 전화번호 중복");
				result.setResCode(ErrCode.SIGNUP_TEL_OVERLAP);
				result.setResMsg(ErrCode.getMessage(ErrCode.SIGNUP_TEL_OVERLAP));
			}
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
	 * email 변경
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/email", method = RequestMethod.PUT)
	public ResValue changeEmail(HttpServletRequest req, HttpServletResponse res, @PathVariable String version
			, @RequestBody ChangeVO reqVO) throws Exception {

		logger.info("===================================== START ===================================");
		logger.info("url ::: " + req.getRequestURL());
		logger.info("req ::: " + reqVO.getUsrEmail());
		
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
			int resCnt = mypageService.updateUsrEmail(reqVO);
			if(resCnt == 0){
				logger.info("error ::: 이메일 변경 실패");
				result.setResCode(ErrCode.UNKNOWN_ERROR);
				result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
			} else if(resCnt == -1){
				logger.info("error ::: 이메일 중복");
				result.setResCode(ErrCode.SIGNUP_EMAIL_OVERLAP);
				result.setResMsg(ErrCode.getMessage(ErrCode.SIGNUP_EMAIL_OVERLAP));
			}
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
	 * 마케팅 수신동의 변경
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/agree", method = RequestMethod.PUT)
	public ResValue changeAgree(HttpServletRequest req, HttpServletResponse res, @PathVariable String version
			, @RequestBody ChangeVO reqVO) throws Exception {

		logger.info("===================================== START ===================================");
		logger.info("url ::: " + req.getRequestURL());
		logger.info("req ::: " + reqVO.getAgree());
		
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
		
		if(usrId == 0 && reqVO.getUnKey() == 0){
			result.setResCode(ErrCode.INVALID_PARAMETER);
			result.setResMsg(ErrCode.getMessage(ErrCode.INVALID_PARAMETER));
			logger.info("result ::: " + result.toString());
			logger.info("===================================== END =====================================");
			return result;
		}
		
		try {
			int resCnt = 0;
			if(usrId > 0){
				reqVO.setUsrId(value.getUserKey());
				resCnt = mypageService.updateUsrAgree(reqVO);
			} else {
				resCnt = mypageService.updateUnknownAgree(reqVO);
			}
			if(resCnt == 0){
				logger.info("error ::: 마케팅 수신동의 변경 에러 ");
				result.setResCode(ErrCode.UNKNOWN_ERROR);
				result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
			}
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
	 * 보너스 정보
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/info", method = RequestMethod.GET)
	public ResValue bonusInfo(HttpServletRequest req, HttpServletResponse res, @PathVariable String version) throws Exception {

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
			BonusVO info =  mypageService.selectBonusInfo(value.getUserKey());
			result.setResData(info);
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
	 * 더보기 정보
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/more", method = RequestMethod.GET)
	public ResValue moreInfo(HttpServletRequest req, HttpServletResponse res, @PathVariable String version
			, ChangeVO reqVO) throws Exception {

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
		// 토큰값 체크
		TokenValues value = userService.updateCheckToken(req, res, result);
		if(value != null){
			usrId = Integer.parseInt(value.getUserKey());
		}
		
		if(usrId == 0 && reqVO.getUnKey() == 0){
			result.setResCode(ErrCode.INVALID_PARAMETER);
			result.setResMsg(ErrCode.getMessage(ErrCode.INVALID_PARAMETER));
			logger.info("result ::: " + result.toString());
			logger.info("===================================== END =====================================");
			return result;
		}
		
		try {
			Map<String, Object> resultMap = mypageService.selectMoreInfo(usrId, reqVO.getUnKey());
			result.setResData(resultMap);
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
	 * 친구 추천 페이지
	 */
	@RequestMapping(value = "/{version}/frd/{ak}", method = RequestMethod.GET)
	public String frd(HttpServletRequest req, HttpServletResponse res, @PathVariable String version, @PathVariable String ak
			, Model model) throws Exception {

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
			return "redirect:/api/main/" + version;
		}

		Map<String, Object> resultMap = new HashMap<>();
		try {
//			String ak = req.getParameter("usrAk");
			resultMap = mypageService.selectFrdInfo(ak);
			
			if(resultMap == null){
				return "redirect:/api/main/" + version;
			}
			
		} catch (Exception e) {
			userService.insertErrLog(e, req, null, null);
			logger.info("error ::: " + e.getMessage());
			return "redirect:/api/main/" + version;
		}

		model.addAttribute("info", resultMap);
		logger.info("result ::: " + resultMap.toString());
		logger.info("===================================== END =====================================");
		return "mypage/frd";
	}

	/*
	 * 푸시 보내기
	 */
	@ResponseBody
	@RequestMapping(value = "/addFrd", method=RequestMethod.POST)
	public Map<String, Object> addFrd(HttpServletRequest req, HttpServletResponse res, @RequestBody Map<String, Object> reqMap) throws Exception {

		logger.info("===================================== START ===================================");
		logger.info("url ::: " + req.getRequestURL());
		logger.info("req ::: " + reqMap.toString());
		
		int resCnt = mypageService.insertFrd(reqMap);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(resCnt > 0){
			resultMap.put("result", "success");
		} else if(resCnt == -1){
			resultMap.put("result", "noFrd");
		} else {
			resultMap.put("result", "fail");
		}
		logger.info("===================================== END =====================================");
		return resultMap;
	}

	/**
	 * 환율 알림 리스트 보기
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/exc", method = RequestMethod.GET)
	public ResValue getExcList(HttpServletRequest req, HttpServletResponse res, @PathVariable String version) throws Exception {

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
			Map<String, Object> resultMap = mypageService.selectExcList(value.getUserKey());
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
	 * 예약하기
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/exc", method = RequestMethod.POST)
	public ResValue insertExc(HttpServletRequest req, HttpServletResponse res, @PathVariable String version
				, @RequestBody AlrmExcVO reqVO) throws Exception {

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

		// 필수 ::: 필수 파라미터 체크 
		if(!reqVO.checkAlrm()){
			result.setResCode(ErrCode.INVALID_PARAMETER);
			result.setResMsg(ErrCode.getMessage(ErrCode.INVALID_PARAMETER));
			logger.info("result ::: " + result.toString());
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
			reqVO.setUsrId(Integer.parseInt(value.getUserKey()));
			int resCnt = mypageService.insertAlrmExc(reqVO);
			if(resCnt == 0){
				logger.info("error ::: 알림 등록을 못함");
				result.setResCode(ErrCode.UNKNOWN_ERROR);
				result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
			}
		} catch (Exception e) {
			logger.info("error ::: " + e.getMessage());
			userService.insertErrLog(e, req, reqVO, value.getUserKey());
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}

	/**
	 * email 변경
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/exc/{ueaId}", method = RequestMethod.PUT)
	public ResValue updateExc(HttpServletRequest req, HttpServletResponse res, @PathVariable String version
			, @RequestBody AlrmExcVO reqVO, @PathVariable String ueaId) throws Exception {

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

		// 필수 ::: 필수 파라미터 체크 
		if(reqVO.getAlrmSt() == null){
			result.setResCode(ErrCode.INVALID_PARAMETER);
			result.setResMsg(ErrCode.getMessage(ErrCode.INVALID_PARAMETER));
			logger.info("result ::: " + result.toString());
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
			reqVO.setUeaId(Integer.parseInt(ueaId));
			reqVO.setUsrId(Integer.parseInt(value.getUserKey()));
			int resCnt = mypageService.updateAlrmExc(reqVO);
			if(resCnt == 0){
				logger.info("error ::: 알림 수정을 못함");
				result.setResCode(ErrCode.UNKNOWN_ERROR);
				result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
			} 
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
