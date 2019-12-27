package com.greit.weys.mypage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.greit.weys.common.AriaUtils;
import com.greit.weys.join.JoinDao;
import com.greit.weys.user.UserDao;
import com.greit.weys.user.UsrVO;

@Service
public class MypageService {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private MypageDao mypageDao;
	@Autowired
	private JoinDao joinDao;
	@Autowired
	private UserDao userDao;

	@Value("#{props['ENC.KEY']}")
	private String ENC_KEY;

	public int updateUserDeleteV7(UsrVO reqVO) throws Exception {
		String ori_pw = mypageDao.selectUsrPw(reqVO.getUsrId() + "");
		String pw = AriaUtils.encryptPassword(reqVO.getUsrPw(), String.valueOf(reqVO.getUsrId()));
		
		if(pw.equals(ori_pw)){
			mypageDao.updateUserDeleteToken(reqVO.getUsrId() + "");
			return mypageDao.updateUserDelete(reqVO.getUsrId() + "");
		}
		else 
			return -1;
	}

	public int updateUserDelete(UsrVO reqVO) throws Exception {
		String ori_pw = mypageDao.selectUsrPw(reqVO.getUsrId() + "");
		String pw = AriaUtils.encryptPassword(reqVO.getUsrPw(), String.valueOf(reqVO.getUsrId()));
		
		if(pw.equals(ori_pw)){
			Map<String, Object> reqMap = new HashMap<>();
			reqMap.put("usrId", reqVO.getUsrId());
			userDao.insertUserUnknown(reqMap);
			mypageDao.updateUserDeleteToken(reqVO.getUsrId() + "");
			mypageDao.updateUserDelete(reqVO.getUsrId() + "");
			
			int unKey = MapUtils.getIntValue(reqMap, "unKey", 0);
			return unKey;
		}
		else 
			return -1;
	}

	public List<NoticeVO> selectNoticeList() {
		List<NoticeVO> resultList = mypageDao.selectNoticeList(); 
		for(NoticeVO temp : resultList){
			String content = temp.getContent();
			content = "<!DOCTYPE HTML><html><head><meta http-equiv=\"content-type\" "
					+ "content=\"text/html; charset=UTF-8\">"
					+ "<meta name=\"viewport\" content=\"width=device-width, minimum-scale=1, initial-scale=1, shrink-to-fit=no\">"
					+ "<style type=\"text/css\">html { -webkit-text-size-adjust: none; }</style></head>"
					+ "<body>" + content + "</body></html>";
			temp.setContent(content);
		}
		return resultList;
	}

	public NoticeVO selectNotice(String notice_id) {
		NoticeVO info = mypageDao.selectNotice(notice_id); 
		String content = info.getContent();
		content = "<!DOCTYPE HTML><html><head><meta http-equiv=\"content-type\" "
				+ "content=\"text/html; charset=UTF-8\">"
				+ "<meta name=\"viewport\" content=\"width=device-width, minimum-scale=1, initial-scale=1, shrink-to-fit=no\">"
				+ "<style type=\"text/css\">html { -webkit-text-size-adjust: none; }</style></head>"
				+ "<body>" + content + "</body></html>";
		info.setContent(content);
		return info;
	}

	public int updateUsrPwd(ChangeVO reqVO) throws Exception {
		String usrPw = mypageDao.selectUsrPw(reqVO.getUsrId());
		String pw = AriaUtils.encryptPassword(reqVO.getOriginPw(), String.valueOf(reqVO.getUsrId()));
		
		if(!pw.equals(usrPw)){
			return -1;
		}
		
		String newPw = AriaUtils.encryptPassword(reqVO.getNewPw(), String.valueOf(reqVO.getUsrId()));
		reqVO.setNewPw(newPw);
		
		return mypageDao.updateUsrPw(reqVO);
	}

	public ProfileVO selectUserInfoV7(String userKey) {
		Map<String, Object> reqMap = new HashMap<>();
		reqMap.put("usrId", userKey);
		reqMap.put("encKey", ENC_KEY);
		return mypageDao.selectUserInfoV7(reqMap);
	}

	public ProfileVO selectUserInfo(String userKey) {
		Map<String, Object> reqMap = new HashMap<>();
		reqMap.put("usrId", userKey);
		reqMap.put("encKey", ENC_KEY);
		
		ProfileVO result = mypageDao.selectUserInfo(reqMap);
		int couponCnt = mypageDao.selectCouponCnt(userKey);
		result.setCouponCnt(couponCnt);
		return result;
	}

	public int updateUsrTel(ChangeVO reqVO) {
		reqVO.setEncKey(ENC_KEY);
		int res = mypageDao.checkUsrTel(reqVO);
		
		if(res > 0)
			return -1;
		return mypageDao.updateUsrTel(reqVO);
	}

	public int updateUsrEmail(ChangeVO reqVO) {
		reqVO.setEncKey(ENC_KEY);
		int res = mypageDao.checkUsrEmail(reqVO);
		
		if(res > 0)
			return -1;
		return mypageDao.updateUsrEmail(reqVO);
	}

	public int updateUsrAgree(ChangeVO reqVO) {
		mypageDao.updateUsrPush(reqVO);
		return mypageDao.updateUsrAgree(reqVO);
	}

	public BonusVO selectBonusInfo(String userKey) {
		return mypageDao.selectBonusInfo(userKey);
	}

	public int updateUnknownAgree(ChangeVO reqVO) {
		return mypageDao.updateUnknownAgree(reqVO);
	}

	public Map<String, Object> selectMoreInfo(int usrId, int unKey) {
		
		Map<String, Object> resultMap = new HashMap<>();

		int eventCnt = 0;
		String agree = "";
		if(usrId > 0){
			agree = mypageDao.selectUsrAgree(usrId);
			eventCnt = mypageDao.selectEventCntUsr(usrId);
		} else {
			agree = mypageDao.selectUnKnownAgree(unKey);
			eventCnt = mypageDao.selectEventCnt();
		}
		resultMap.put("eventCnt", eventCnt);
		resultMap.put("agree", agree);
		
		return resultMap;
	}

	public Map<String, Object> selectFrdInfo(String ak) {

		if(ak == null || ak.equals("unknown")){
			return null;
		}
		
		Integer usrId = mypageDao.selectUsrId(ak);
		if(usrId != null){

			Map<String, Object> resultMap = mypageDao.selectMyFrdCd(usrId);
			int myFrdCnt = mypageDao.selectMyFrdCnt(usrId);
			
			if(myFrdCnt > 3)
				myFrdCnt = 3;
			
			List<Map<String, Object>> onMap = new ArrayList<>();
			for(int i=1 ; i<=myFrdCnt ; i++){
				onMap.add(new HashMap<String, Object>());
			}
			List<Map<String, Object>> offMap = new ArrayList<>();
			for(int i=1 ; i<=3-myFrdCnt ; i++){
				offMap.add(new HashMap<String, Object>());
			}

			resultMap.put("onMap", onMap);
			resultMap.put("offMap", offMap);
			resultMap.put("usrId", usrId);
			
			return resultMap;
		} else {
			return null;
		}
	}

	public int insertFrd(Map<String, Object> reqMap) {
		
		int usrId = MapUtils.getIntValue(reqMap, "usrId");
		String frdCd = MapUtils.getString(reqMap, "frdCd");
		
		int targetUsrId = joinDao.selectFrdId(frdCd);
		if(targetUsrId > 0){
			Map<String, Object> frdMap = new HashMap<>();
			frdMap.put("targetUsrId", targetUsrId);
			frdMap.put("reqUsrId", usrId);
			targetUsrId = joinDao.insertFrdMap(frdMap);
		} else {
			return -1;
		}
		return targetUsrId;
	}

	public Map<String, Object> selectExcList(String userKey) {
		
		List<AlrmExcVO> resultList = mypageDao.selectExcList(userKey);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("dataList", resultList);
		return resultMap;
	}

	public int insertAlrmExc(AlrmExcVO reqVO) {
		
		return mypageDao.insertAlrmExc(reqVO);
	}

	public int updateAlrmExc(AlrmExcVO reqVO) {
		
		int resCnt = 0;
		String alrmSt = reqVO.getAlrmSt();
		if(alrmSt.equals("D")){
			resCnt = mypageDao.deleteAlrmExc(reqVO);
		} else {
			resCnt = mypageDao.updateAlrmExc(reqVO);
		}
		
		return resCnt;
	}
}
