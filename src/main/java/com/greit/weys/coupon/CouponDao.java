package com.greit.weys.coupon;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface CouponDao {

	List<CouponVO> selectCouponList(CouponReqVO reqVO);

	Map<String, Object> selectCouponInfo(String code);

	int checkCouponUsed(CouponReqVO reqVO);

	int insertCouponHist(Map<String, Object> couponInfo);

	Map<String, Object> selectCouponInfoGroup(String code);

	void updateCouponUsed(String code);

}
