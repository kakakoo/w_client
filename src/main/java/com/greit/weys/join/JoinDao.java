package com.greit.weys.join;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.greit.weys.user.UserDetailVO;

@Repository
public interface JoinDao {

	int checkEmail(Map<String, Object> reqMap);

	int insertUserInfo(UserDetailVO reqVO);

	int updateUserPw(UserDetailVO reqVO);

	int selectMemberBarcodeCnt(String barcode);

	int insertMemberInfo(Map<String, Object> insertMap);

	int insertMemberActive(Map<String, Object> insertMap);

	int checkFrdCd(String frdCd);

	int selectFrdId(String targetFrdCd);

	int insertFrdMap(Map<String, Object> frdMap);

	int selectFrdCnt(int targetUsrId);

	int insertFrdCoupon(Map<String, Object> frdMap);

	Map<String, Object> selectFrduuid(int targetUsrId);

	List<Integer> selectChkFrdSubmit(int usrId);

	int selectUsrTelCnt(int usrId);

	List<Integer> selectJoinCouponList(String code);

	int selectFrdCouponCnt(Map<String, Object> frdMap);

}
