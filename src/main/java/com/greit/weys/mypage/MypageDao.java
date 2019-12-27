package com.greit.weys.mypage;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface MypageDao {

	String selectUsrPw(String string);

	void updateUserDeleteToken(String string);

	int updateUserDelete(String string);

	List<NoticeVO> selectNoticeList();

	NoticeVO selectNotice(String notice_id);

	int updateUsrPw(ChangeVO reqVO);

	ProfileVO selectUserInfoV7(Map<String, Object> reqMap);
	
	ProfileVO selectUserInfo(Map<String, Object> reqMap);

	int checkUsrTel(ChangeVO reqVO);

	int updateUsrTel(ChangeVO reqVO);

	int checkUsrEmail(ChangeVO reqVO);

	int updateUsrEmail(ChangeVO reqVO);

	int updateUsrAgree(ChangeVO reqVO);

	BonusVO selectBonusInfo(String userKey);

	int updateUnknownAgree(ChangeVO reqVO);

	void updateUsrPush(ChangeVO reqVO);

	int selectEventCnt();

	String selectUsrAgree(int usrId);

	String selectUnKnownAgree(int unKey);

	int selectCouponCnt(String userKey);

	int selectMyFrdCnt(int usrId);

	Map<String, Object> selectMyFrdCd(int usrId);

	Integer selectUsrId(String ak);

	List<AlrmExcVO> selectExcList(String userKey);

	int insertAlrmExc(AlrmExcVO reqVO);

	int deleteAlrmExc(AlrmExcVO reqVO);

	int updateAlrmExc(AlrmExcVO reqVO);

	int selectEventCntUsr(int usrId);

}
