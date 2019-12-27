package com.greit.weys.rsv;

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
import com.greit.weys.common.PaygateVO;
import com.greit.weys.common.VersionCheck;
import com.greit.weys.config.TokenValues;
import com.greit.weys.user.UserService;

@Controller
@RequestMapping("/api/rsv")
public class RsvController {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private RsvService rsvService;
	@Autowired
	private UserService userService;
	
	/**
	 * 예약정보
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/info", method = RequestMethod.GET)
	public ResValue rsvInfo(HttpServletRequest req, HttpServletResponse res, @PathVariable String version) throws Exception {

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
		// 필수 ::: 토큰값 체크
		TokenValues value = userService.updateCheckToken(req, res, result);
		if(value != null){
			usrId = Integer.parseInt(value.getUserKey());
		}
		
		try {
			Map<String, Object> resultMap = rsvService.selectRsvInfo();
			result.setResData(resultMap);
		} catch (Exception e) {
			logger.info("error ::: " + e.getMessage());
			userService.insertErrLog(e, req, null, usrId == 0 ? null : usrId + "");
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}

	/**
	 * 추가 우대 정보
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/bonus", method = RequestMethod.GET)
	public ResValue bonus(HttpServletRequest req, HttpServletResponse res, @PathVariable String version, String unitCd) throws Exception {

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

		// 필수 ::: 필수 파라미터 체크 
		if(unitCd == null || unitCd.equals("")){
			result.setResCode(ErrCode.INVALID_PARAMETER);
			result.setResMsg(ErrCode.getMessage(ErrCode.INVALID_PARAMETER));
			logger.info("result ::: " + result.toString());
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
			Map<String, Object> resultMap = new HashMap<>();
			if(ver <= 9){
				resultMap = rsvService.selectBonusListV9(usrId, unitCd);
			} else {
				resultMap = rsvService.selectBonusList(usrId, unitCd);
			}
			result.setResData(resultMap);
		} catch (Exception e) {
			logger.info("error ::: " + e.getMessage());
			userService.insertErrLog(e, req, null, value.getUserKey());
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}
	
	/**
	 * 예약가능 화폐 호출
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/unit", method = RequestMethod.GET)
	public ResValue rsvUnit(HttpServletRequest req, HttpServletResponse res, @PathVariable String version) throws Exception {

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
		int usrId = 0;
		// 필수 ::: 토큰값 체크
		TokenValues value = userService.updateCheckToken(req, res, result);
		if(value != null){
			usrId = Integer.parseInt(value.getUserKey());
		}
		
		try {
			Map<String, Object> resultMap = rsvService.selectRsvUnit(usrId);
			result.setResData(resultMap);
		} catch (Exception e) {
			logger.info("error ::: " + e.getMessage());
			userService.insertErrLog(e, req, null, usrId + "");
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}
	
	/**
	 * 예약 지점 호출
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/{unitCd}/store", method = RequestMethod.GET)
	public ResValue rsvUnitStore(HttpServletRequest req, HttpServletResponse res, @PathVariable String version
			, @PathVariable String unitCd) throws Exception {

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
			Map<String, Object> resultMap = new HashMap<>();
//			if(ver == 7){
//				resultMap = rsvService.selectRsvUnitStoreV7(unitCd, value.getUserKey());
//			} else {
//				resultMap = rsvService.selectRsvUnitStore(unitCd, value.getUserKey());
//			}
			result.setResData(resultMap);
		} catch (Exception e) {
			logger.info("error ::: " + e.getMessage());
			userService.insertErrLog(e, req, null, value.getUserKey());
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}

	/**
	 * 배송 지점 호출
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/{unitCd}/deliver", method = RequestMethod.GET)
	public ResValue deliverUnitStore(HttpServletRequest req, HttpServletResponse res, @PathVariable String version
			, @PathVariable String unitCd) throws Exception {

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
			Map<String, Object> resultMap = new HashMap<>();
			if(ver == 7){
				resultMap = rsvService.selectDeliverUnitStoreV7(unitCd, value.getUserKey());
			} else {
				resultMap = rsvService.selectDeliverUnitStore(unitCd, value.getUserKey());
			}
			result.setResData(resultMap);
		} catch (Exception e) {
			logger.info("error ::: " + e.getMessage());
			userService.insertErrLog(e, req, null, value.getUserKey());
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}
	
	/**
	 * 실명확인
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/certify", method = RequestMethod.GET)
	public ResValue certify(HttpServletRequest req, HttpServletResponse res, @PathVariable String version
				, RsvInfoVO reqVO) throws Exception {

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

		// 필수 ::: 필수 파라미터 체크 
		if( !reqVO.certifyCheck() ){
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
			if(ver >= 9){
				String resString = rsvService.certify(reqVO);
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
				boolean resultBool = rsvService.certifyV7(reqVO);
				if(!resultBool){
					logger.info("error ::: IDENTIFY INCORRECT");
					result.setResCode(ErrCode.RSV_IDENTIFY_INCORRECT);
					result.setResMsg(ErrCode.getMessage(ErrCode.RSV_IDENTIFY_INCORRECT));
				}
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
	 * 예약하기
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}", method = RequestMethod.POST)
	public ResValue submit(HttpServletRequest req, HttpServletResponse res, @PathVariable String version
				, @RequestBody RsvInfoVO reqVO) throws Exception {

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

		// 필수 ::: 필수 파라미터 체크 
		if(!reqVO.insertRsvCheck()){
			result.setResCode(ErrCode.INVALID_PARAMETER);
			result.setResMsg(ErrCode.getMessage(ErrCode.INVALID_PARAMETER));
			logger.info("result ::: " + result.toString());
			logger.info("===================================== END =====================================");
			return result;
		} 

		try {
//			RsvInfoVO info = rsvService.insertRsv(value.getUserKey(), reqVO);
//			
//			if(info.getKey() == null){
//				result.setResData(info);
//			} else if(info.getKey().equals("ALREADY EXIST")){
//				logger.info("error ::: RSV ALREADY EXIST");
//				result.setResCode(ErrCode.RSV_ALREADY_EXIST);
//				result.setResMsg(ErrCode.getMessage(ErrCode.RSV_ALREADY_EXIST));
//			} else if(info.getKey().equals("NOT ENOUGH MEMBERSHIP")){
//				logger.info("error ::: RSV_ENOUGH_MEMBERSHIP");
//				result.setResCode(ErrCode.RSV_ENOUGH_MEMBERSHIP);
//				result.setResMsg(ErrCode.getMessage(ErrCode.RSV_ENOUGH_MEMBERSHIP));
//			} else if(info.getKey().equals("FAIL VBANK")){
//				logger.info("error ::: FAIL VBANK");
//				result.setResCode(ErrCode.RSV_VBANK_FAIL);
//				result.setResMsg(ErrCode.getMessage(ErrCode.RSV_VBANK_FAIL));
//			} else if(info.getKey().equals("FAIL INSERT RSV")){
//				logger.info("error ::: FAIL INSERT RSV");
//				result.setResCode(ErrCode.UNKNOWN_ERROR);
//				result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
//			} else if(info.getKey().equals("NOT ENOUGH COUPON")){
//				logger.info("error ::: NOT ENOUGH COUPON");
//				result.setResCode(ErrCode.RSV_COUPON_UNAVAILABLE);
//				result.setResMsg(ErrCode.getMessage(ErrCode.RSV_COUPON_UNAVAILABLE));
//			}

			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
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
	 * 예약내역 리스트
	 * 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}", method = RequestMethod.GET)
	public ResValue rsvList(HttpServletRequest req, HttpServletResponse res, @PathVariable String version) throws Exception {

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
			List<RsvListVO> resultList = rsvService.selectRsvList(value.getUserKey());
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
	 * 예약정보 가져오기
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/{rsvId}", method = RequestMethod.GET)
	public ResValue checkRsv(HttpServletRequest req, HttpServletResponse res, @PathVariable String version
			, @PathVariable String rsvId) throws Exception {

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
			RsvInfoVO info = rsvService.selectRsvInfo(value.getUserKey(), rsvId);
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
	 * 취소시 정보
	 * 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/{rsvId}/cancel/info", method = RequestMethod.GET)
	public ResValue cancelInfoif(HttpServletRequest req, HttpServletResponse res, @PathVariable String version
			, @PathVariable String rsvId) throws Exception {

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
			RsvCancelVO resultVO = new RsvCancelVO();
			if(ver == 7){
				resultVO = rsvService.checkCancelInfoIfV7(value.getUserKey(), rsvId);
			} else {
				resultVO = rsvService.checkCancelInfoIf(value.getUserKey(), rsvId);
			}
			result.setResData(resultVO);
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
	 * 예약 환불 요청
	 * 
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/{rsvId}/cancel", method = RequestMethod.GET)
	public ResValue cancelInfo(HttpServletRequest req, HttpServletResponse res, @PathVariable String version
			, @PathVariable String rsvId) throws Exception {

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
			RsvCancelVO resultVO = new RsvCancelVO();
			
			if(ver == 7){
				resultVO = rsvService.checkCancelInfoV7(value.getUserKey(), rsvId);
			} else {
				resultVO = rsvService.checkCancelInfo(value.getUserKey(), rsvId);
			}
			
			if(resultVO == null){
				result.setResCode(ErrCode.RSV_REJECT_CANCEL);
				result.setResMsg(ErrCode.getMessage(ErrCode.RSV_REJECT_CANCEL));
			} else{
				result.setResData(resultVO);
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
	 * 예약 취소
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/{rsvId}", method = RequestMethod.DELETE)
	public ResValue cancelRsv(HttpServletRequest req, HttpServletResponse res, @PathVariable String version
			, @PathVariable String rsvId, @RequestBody RsvCancelVO reqVO) throws Exception {

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
			reqVO.setRsvId(Integer.parseInt(rsvId));
			reqVO.setUsrId(Integer.parseInt(value.getUserKey()));
			int resCnt = rsvService.updateCancelRsv(reqVO);
			
			if(resCnt == 0){
				logger.info("error ::: 예약 취소 실패");
				result.setResCode(ErrCode.UNKNOWN_ERROR);
				result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
			} else if(resCnt == -1){
				logger.info("error ::: 은행 정보 없음");
				result.setResCode(ErrCode.RSV_EMPTY_BANKINFO);
				result.setResMsg(ErrCode.getMessage(ErrCode.RSV_EMPTY_BANKINFO));
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
	 * 외국인 정보 등록
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/forign", method = RequestMethod.PUT)
	public ResValue forign(HttpServletRequest req, HttpServletResponse res, @PathVariable String version
			, @RequestBody RsvInfoVO reqVO) throws Exception {

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

		// 필수 ::: 필수 파라미터 체크 
		if(!reqVO.checkForign()){
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
			int resCnt = rsvService.updateUsrForign(reqVO);
			
			if(resCnt == 0){
				/**
				 * 내/외국인 정보가 있을경우
				 */
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
	 * 국가 정보
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/nat", method = RequestMethod.GET)
	public ResValue nationInfo(HttpServletRequest req, HttpServletResponse res, @PathVariable String version) throws Exception {

		logger.info("===================================== START ===================================");
		logger.info("url ::: " + req.getRequestURL());
		
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
			Map<String, Object> resultMap = rsvService.selectNatInfo();
			result.setResData(resultMap);
		} catch (Exception e) {
			logger.info("error ::: " + e.getMessage());
			userService.insertErrLog(e, req, null, usrId == 0 ? null : usrId + "");
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}

	/**
	 * 권종 선택
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/paper/{unitCd}", method = RequestMethod.GET)
	public ResValue selectPaper(HttpServletRequest req, HttpServletResponse res, @PathVariable String version
			, @PathVariable String unitCd) throws Exception {

		logger.info("===================================== START ===================================");
		logger.info("url ::: " + req.getRequestURL());
		
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

		// 필수 ::: 토큰값 체크
		TokenValues value = userService.updateCheckToken(req, res, result);
		if(value == null){
			logger.info("result ::: " + result.toString());
			logger.info("===================================== END =====================================");
			return result;
		}
		try {
			Map<String, Object> resultMap = rsvService.selectRsvPaper(unitCd);
			result.setResData(resultMap);
		} catch (Exception e) {
			logger.info("error ::: " + e.getMessage());
			userService.insertErrLog(e, req, null, value.getUserKey());
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}

	/**
	 * 변경 가능한 날짜
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/{rsvId}/changeDt", method = RequestMethod.GET)
	public ResValue changeDt(HttpServletRequest req, HttpServletResponse res, @PathVariable String version
			, @PathVariable String rsvId) throws Exception {

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
			Map<String, Object> resultMap = rsvService.selectChangeDt(rsvId);
			if(resultMap == null){
				result.setResCode(ErrCode.RSV_REJECT_CHANGE_DATE);
				result.setResMsg(ErrCode.getMessage(ErrCode.RSV_REJECT_CHANGE_DATE));
			} else {
				result.setResData(resultMap);
			}
		} catch (Exception e) {
			logger.info("error ::: " + e.getMessage());
			userService.insertErrLog(e, req, null, value.getUserKey());
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}

	/**
	 * 예약날짜 변경
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/{rsvId}/changeDt", method = RequestMethod.PUT)
	public ResValue changeRsvDt(HttpServletRequest req, HttpServletResponse res, @PathVariable String version
			, @PathVariable String rsvId, @RequestBody RsvInfoVO reqVO) throws Exception {

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

		// 필수 ::: 필수 파라미터 체크 
		if(reqVO.getRsvTm() == null || reqVO.getRsvDt() == null){
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
			reqVO.setRsvId(Integer.parseInt(rsvId));
			int resCnt = rsvService.updateChangeRsvDt(reqVO);
			
			if(resCnt <= 0){
				result.setResCode(ErrCode.RSV_REJECT_CHANGE_DATE);
				result.setResMsg(ErrCode.getMessage(ErrCode.RSV_REJECT_CHANGE_DATE));
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
	 * 실사용 계좌 인증
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/vAuth/info", method = RequestMethod.POST)
	public ResValue vAuthInfo(HttpServletRequest req, HttpServletResponse res, @PathVariable String version
			, @RequestBody PaygateVO infoVO) throws Exception {

		logger.info("===================================== START ===================================");
		logger.info("url ::: " + req.getRequestURL());
		logger.info("url ::: " + infoVO.toString());
		
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
		if(!infoVO.checkInfo()){
			result.setResCode(ErrCode.INVALID_PARAMETER);
			result.setResMsg(ErrCode.getMessage(ErrCode.INVALID_PARAMETER));
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
			infoVO.setUsrId(value.getUserKey());
			result = rsvService.updateBankSet(infoVO);
			
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
	 * 1원 계좌 인증
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/vAuth/code", method = RequestMethod.POST)
	public ResValue vAuthCode(HttpServletRequest req, HttpServletResponse res, @PathVariable String version
			, @RequestBody PaygateVO infoVO) throws Exception {

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

		// 필수 ::: 필수 파라미터 체크 
		if(infoVO.getCode() == null){
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
			infoVO.setUsrId(value.getUserKey());
			result = rsvService.updateBankCode(infoVO);
			
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
	 * 은행정보
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/{type}/bank", method = RequestMethod.GET)
	public ResValue authBank(HttpServletRequest req, HttpServletResponse res, @PathVariable String version, @PathVariable String type) throws Exception {

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

		// 필수 ::: 필수 파라미터 체크 
		if(!type.equals("a") && !type.equals("v") && !type.equals("r") && !type.equals("n")){
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
			Map<String, Object> bankMap = rsvService.selectBanks(type);
			result.setResData(bankMap);
			
		} catch (Exception e) {
			logger.info("error ::: " + e.getMessage());
			userService.insertErrLog(e, req, null, value.getUserKey());
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}

	/**
	 * 은행정보
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}/vbank", method = RequestMethod.GET)
	public ResValue vbankList(HttpServletRequest req, HttpServletResponse res, @PathVariable String version) throws Exception {

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
		// 필수 ::: 토큰값 체크
		TokenValues value = userService.updateCheckToken(req, res, result);
		if(value != null){
			usrId = Integer.parseInt(value.getUserKey());
		}
		
		try {
			Map<String, Object> bankMap = rsvService.selectvbank("n");
			result.setResData(bankMap);
		} catch (Exception e) {
			logger.info("error ::: " + e.getMessage());
			userService.insertErrLog(e, req, null, usrId == 0 ? null : usrId + "");
			result.setResCode(ErrCode.UNKNOWN_ERROR);
			result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
		}

		logger.info("result ::: " + result.toString());
		logger.info("===================================== END =====================================");
		return result;
	}


}
