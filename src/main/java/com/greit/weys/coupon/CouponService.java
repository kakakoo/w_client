package com.greit.weys.coupon;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.greit.weys.common.Utils;

@Service
public class CouponService {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private CouponDao couponDao;
	
	public List<CouponVO> selectCouponList(CouponReqVO reqVO) {
		return couponDao.selectCouponList(reqVO);
	}

	public String insertCouponAdd(CouponReqVO reqVO) {

		Map<String, Object> couponInfo = couponDao.selectCouponInfo(reqVO.getCode());
		String type = "A";
		
		if(couponInfo == null){
			couponInfo = couponDao.selectCouponInfoGroup(reqVO.getCode());
			type = "B";
		}
		
		/**
		 * 존재하는 쿠폰이 없음
		 */
		if(couponInfo == null){
			return "-1";
		}
		
		if(type.equals("A")){
			/**
			 * 사용여부 확인
			 */
			int cnt = couponDao.checkCouponUsed(reqVO);
			if(cnt > 0){
				return "-2";
			}
			
			/**
			 * 쿠폰 수량 마감 체크
			 */
			int couponLimit = MapUtils.getIntValue(couponInfo, "COUPON_LIMIT");
			if(couponLimit > 0){
				int couponCnt = MapUtils.getIntValue(couponInfo, "COUPON_CNT");
				if(couponLimit <= couponCnt){
					return "-100";
				}
			}
			
		} else {
			/**
			 * 사용여부 확인
			 */
			String useSt = MapUtils.getString(couponInfo, "USE_ST");
			if(useSt.equals("Y")){
				return "-2";
			}
		}
		
		/**
		 * 쿠폰 기한 체크
		 */
		String endDt = MapUtils.getString(couponInfo, "END_DT");
		String today = Utils.getDiffDate(0);
		long diff = Utils.diffTwoDate(endDt, today);
		if(diff < 0){
			return "-10";
		}

		String couponTp = MapUtils.getString(couponInfo, "COUPON_TP");
		String periodTp = MapUtils.getString(couponInfo, "PERIOD_TP");
		
		String due = endDt;
		if(periodTp.equals("D")){
			int period = MapUtils.getIntValue(couponInfo, "MEMBER_PERIOD");
			due = Utils.getDiffMonth(period);
		}

		couponInfo.put("dueDt", due);
		couponInfo.put("usrId", reqVO.getUsrId());
		couponDao.insertCouponHist(couponInfo);
		
		if(type.equals("B")){
			couponDao.updateCouponUsed(reqVO.getCode());
		}
		
		return couponTp;
	}
}
