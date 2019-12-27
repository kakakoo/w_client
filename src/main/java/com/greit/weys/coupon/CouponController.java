package com.greit.weys.coupon;

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
@RequestMapping("/api/coupon")
public class CouponController {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private CouponService couponService;
	@Autowired
	private UserService userService;

	/**
	 * 쿠폰 리스트
	 * 
	 * @param req
	 * @param res
	 * @param reqMap
	 * @return 거래 목록 리스트 반환
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}", method = RequestMethod.GET)
	public ResValue couponList(HttpServletRequest req, HttpServletResponse res, @PathVariable String version, CouponReqVO reqVO)
			throws Exception {

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
			List<CouponVO> resultList = couponService.selectCouponList(reqVO);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("dataList", resultList);
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
	 * 쿠폰 등록
	 * 
	 * @param req
	 * @param res
	 * @param reqMap
	 * @return 거래 목록 리스트 반환
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/{version}", method = RequestMethod.POST)
	public ResValue addCoupon(HttpServletRequest req, HttpServletResponse res, @PathVariable String version, @RequestBody CouponReqVO reqVO)
			throws Exception {

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
		if (!reqVO.checkCoupon()) {
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
			reqVO.setUsrId(value.getUserKey());
			String rscToken = couponService.insertCouponAdd(reqVO);
			if(rscToken == null){
				result.setResCode(ErrCode.UNKNOWN_ERROR);
				result.setResMsg(ErrCode.getMessage(ErrCode.UNKNOWN_ERROR));
			} else if(rscToken.equals("-1")){
				result.setResCode(ErrCode.COUPON_NOT_EXIST);
				result.setResMsg(ErrCode.getMessage(ErrCode.COUPON_NOT_EXIST));
			} else if(rscToken.equals("-2")){
				result.setResCode(ErrCode.COUPON_ALREADY_USED);
				result.setResMsg(ErrCode.getMessage(ErrCode.COUPON_ALREADY_USED));
			} else if(rscToken.equals("-10")){
				result.setResCode(ErrCode.COUPON_PASS_DUE);
				result.setResMsg(ErrCode.getMessage(ErrCode.COUPON_PASS_DUE));
			} else if(rscToken.equals("-100")){
				result.setResCode(ErrCode.COUPON_FULL_USE);
				result.setResMsg(ErrCode.getMessage(ErrCode.COUPON_FULL_USE));
			} else {
				Map<String, Object> resultMap = new HashMap<>();
				resultMap.put("tp", rscToken);
				result.setResData(resultMap);
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
