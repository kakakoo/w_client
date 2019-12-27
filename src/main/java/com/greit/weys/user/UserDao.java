package com.greit.weys.user;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.greit.weys.rsv.RsvInfoVO;

@Repository
public interface UserDao {

	int checkUsrTel(UsrVO reqVO);

	UserDetailVO selectUserLoginInfo(UsrVO reqVO);

	void insertConnLog(int usrId);

	void updateUuidBlank(String uuid);

	int selectLatestNotice();

	void insertTokenInfo(UserDetailVO userInfo);

	int selectCheckToken(String token);

	UserDetailVO selectUserInfo(UserDetailVO reqVO);

	int selectUsrIdForTel(UsrVO reqVO);

	int updateUsrPasswd(UsrVO reqVO);

	int updateUsrCertify(Map<String, Object> reqMap);

	int updateLogout(String userKey);

	Map<String, Object> getVersionInfo(String os);

	void insertErrLog(Map<String, Object> errMap);

	int selectCheckCopToken(String token);

	Map<String, Object> getUsrChannelInfo(Map<String, Object> reqMap);

	Map<String, Object> selectRsvChannelInfo(Map<String, Object> reqMap);

	int insertUnknown(UserDetailVO reqVO);

	int insertUserUnknown(Map<String, Object> reqMap);

	void deleteUnknownToken(int unKey);

	Integer selectUnKey(String uuid);

	Map<String, Object> selectTutorial();

	void updateUnknown(UserDetailVO reqVO);

	List<Integer> selectUsrList();

	void updateUsrFrdCd(Map<String, Object> reqMap);

	String selectCheckCertify(UsrVO reqVO);

	int insertJoinCoupon(Map<String, Object> joinMap);

	List<Integer> selectUsrNewCoupon(String code);

	void insertSmsSend(Map<String, Object> reqMap);

	void updateEventCnt(Map<String, Object> joinMap);

	void insertApiLog(Map<String, Object> reqMap);

}
