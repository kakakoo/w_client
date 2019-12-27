package com.greit.weys.rsv;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.greit.weys.common.PaygateVO;

@Repository
public interface RsvDao {

	int selectMemberCostAmnt(String userKey);

	String selectRsvSt(String type);

	Map<String, Object> selectUserBonus(int usrId);

	List<RsvUnitVO> selectRsvUnit();

	Map<String, Object> selectRsvUsrInfo(Map<String, Object> reqMap);

	List<RsvStoreVO> selectUnitStoreList(String unitCd);

	String selectCheckClose(Map<String, Object> reqMap);

	Map<String, Object> selectRsvStoreTime(Map<String, Object> reqMap);

	List<RsvStoreVO> selectUnitDeliverList(String unitCd);

	String selectCheckCloseDeliver(Map<String, Object> reqMap);

	Map<String, Object> selectRsvDeliverTime(Map<String, Object> reqMap);

	List<Map<String, Object>> selectVBanks();

	int selectCouponCost(int couponId);

	int updateCouponUse(RsvInfoVO reqVO);

	String selectUsrNm(RsvInfoVO reqVO);

	int selectCheckRsvNo(String rsvNo);

	int insertRsvInfo(RsvInfoVO reqVO);

	int updateMemberRsvPoint(RsvInfoVO reqVO);

	int insertMemberActiveRsv(RsvInfoVO reqVO);

	Map<String, Object> selectBonusInfo(int usrId);

	List<Map<String, Object>> selectUsrMemActList(int usrId);

	void updateMemActUse(Map<String, Object> usingCost);

	void insertRsvAct(Map<String, Object> rsvActMap);

	String selectUnitNm(String unitCd);

	int selectDeliverTime(int storeId);

	String selectStoreAddr(int storeId);

	String selectUsrEmail(Map<String, Object> reqMailMap);

	void insertKakaoLog(Map<String, Object> talk);

	void insertAlarm(Map<String, Object> alarm);

	Map<String, Object> selectUuidUsr(int usrId);

	void insertRsvLog(Map<String, Object> logMap);

	List<RsvListVO> selectRsvList(Map<String, Object> reqMap);

	RsvInfoVO selectRsvInfo(Map<String, Object> reqMap);

	Map<String, Object> checkCancelInfoIf(Map<String, Object> reqMap);

	Map<String, Object> selectRsvCheck(Map<String, Object> reqMap);

	Map<String, Object> selectCancelSt(RsvCancelVO reqVO);

	int updateRsvCancelBefore(RsvCancelVO reqVO);

	int updateCancelRsv(RsvCancelVO reqVO);

	int checkRsvMember(int rsvId);

	void updateReturnMemCost(int rsvId);

	void updateReturnUseCost(int rsvId);

	void insertReturnRA(int rsvId);

	Map<String, Object> selectCancelEmail(RsvCancelVO reqVO);

	void updateCouponReturn(int couponId);

	Map<String, Object> selectUsrUuid(int usrId);

	List<Map<String, Object>> selectCancelPushInfo(RsvCancelVO reqVO);

	int checkRsvDone(String rsvQr);

	int updateRsvDone(Map<String, Object> reqMap);

	Map<String, Object> selectCompleteRsvInfo(Map<String, Object> reqMap);

	List<Map<String, Object>> selectEventBonus(Map<String, Object> reqMap);

	List<Map<String, Object>> selectRsvTodayHist(Map<String, Object> reqMap);

	void updateBonusCouponUsed(RsvInfoVO reqVO);

	List<String> selectDeliverUnit();

	Map<String, Object> selectUnknownEvent(String unitCd);

	Map<String, Object> selectCheckTodayRsv(int storeId);

	String selectCheckTodayDeliver(int storeId);

	int updateUsrForign(RsvInfoVO reqVO);

	List<Map<String, Object>> selectNatInfo();

	Map<String, Object> selectRsvPaper(String unitCd);

	Map<String, Object> selectRsvDtInfo(String rsvId);

	int updateRsvDt(RsvInfoVO reqVO);

	int insertUsrMemo(RsvInfoVO reqVO);

	String selectUnitHalf(String unitCd);

	String selectStoreInfo();

	String selectAddrDetail(RsvInfoVO reqVO);

	void updateRsvGrpCancelLog(int rsvId);

	void insertPGateNetLog(Map<String, Object> log);

	void updateMemGuid(PaygateVO infoVO);

	String selectMemGuid(PaygateVO infoVO);

	List<Map<String, Object>> selectReturnBank();

	String selectUsrGuid(int usrId);

	void insertOneSend(String usrId);

	int selectOneSendCnt(String usrId);

	List<Map<String, Object>> selectPaperList(String unitCd);

	Map<String, Object> selectUsrAuth(PaygateVO infoVO);

	void updateUsrAuthDone(PaygateVO info);

	List<Map<String, Object>> selectVBankOld();

	String selectRsvStDesc(String string);

	List<Map<String, Object>> selectBreakTime(int storeId);

	void insertRsvRetLog(int rsvId);

}
