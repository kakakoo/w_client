package com.greit.weys.main;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface MainDao {

	List<MainRsvVO> selectMainRsvList(Map<String, Object> reqMap);

	int selectMainRsvCnt(String userKey);

	int selectMainArmCnt(String userKey);

	int selectMainCpnCnt(String userKey);

	int selectMainEventCnt(String userKey);

	List<MainAlarmVO> selectAlarmList(MainReqVO reqVO);

	void updateAlarmSt(String usrId);

	List<BannerVO> selectBannerInfo(Map<String, Object> reqMap);

	List<BannerVO> selectCopBnrList();

	List<Map<String, Object>> selectRsvUnit(Map<String, Object> reqMap);

	List<Map<String, Object>> selectContInfo();

	List<Map<String, Object>> selectContList(Map<String, Object> reqMap);

	void insertUsrLog(Map<String, Object> reqMap);

	String selectMainTitle();

	String selectUsrAk(int usrId);

	int selectUsrJoinEvent(Map<String, Object> reqMap);

	List<BannerVO> selectBannerInfoV9(String sServerVersion);

	Map<String, Object> selectGuideInfo(String userKey);

	int updateGuideLog(int logId);

}
