package com.greit.weys.exc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.KeyGenerator;
import com.greit.weys.common.Utils;

@Service
public class ExcService {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private ExcDao excDao;
	
	@Cacheable(cacheName="unitExc", keyGenerator=@KeyGenerator(name="StringCacheKeyGenerator"))
	public Map<String, Object> selectExchange(String unit) {

		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("unit", unit);
		reqMap.put("startDt", Utils.getDiffDate(-1));
		reqMap.put("endDt", Utils.getTodayDate("yyyy.MM.dd"));
		
		List<Map<String, Object>> rateList = excDao.selectExchange(reqMap);
		String basicRate = "";
		String diffVal = "0";
		String diffRate = "0";
		String diffType = "=";
		String dttm = "";
		
		if(rateList.size() > 1){
			Map<String, Object> today = rateList.get(0);
			Map<String, Object> yester = rateList.get(1);
			
			basicRate = MapUtils.getString(today, "basicRate");
			dttm = MapUtils.getString(today, "dttm");
			
			double diff = MapUtils.getDoubleValue(today, "basicRate") - MapUtils.getDoubleValue(yester, "basicRate");
			double percent = diff / MapUtils.getDoubleValue(today, "basicRate") * 100;

			if(diff > 0){
				diffVal = String.format("%.2f", diff);
				diffRate = String.format("%.2f", percent);
				diffType = "+";
			} else if(diff < 0){
				diffVal = String.format("%.2f", diff * -1);
				diffRate = String.format("%.2f", percent * -1);
				diffType = "-";
			}
			
			result.put("basicRate", Double.parseDouble(basicRate));
			result.put("diffVal", Double.parseDouble(diffVal));
			result.put("diffRate", Double.parseDouble(diffRate));
			result.put("diffType", diffType);
			result.put("dttm", dttm);
		}
		
		return result;
	}
	
	public List<Map<String, Object>> selectExchangeList(String unit, String month) {
		int startDt = 7;
		if(month != null){
			startDt = 30 * Integer.parseInt(month);
		}
		
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put("unit", unit);
		reqMap.put("startDt", Utils.getDiffDate(startDt * -1));
		reqMap.put("endDt", Utils.getDiffDate(1));
		

		List<Map<String, Object>> resultList = new ArrayList<>();
		
		int index = 100;
		if(month == null){
			index = 70;
		} else if(month.equals("12")){
			index = 160;
		} else if(month.equals("3")){
			index = 130;
		}
		
		List<Map<String, Object>> excList = excDao.selectExchangeList(reqMap);
		
		int size = excList.size();
		int loop = size / index;
		
		for(int i=0 ; i<index ; i++){
			resultList.add(excList.get(i * loop));
		}
		resultList.add(excList.get(size - 1));
		
		
//		if(month == null){
//			List<Map<String, Object>> excList = excDao.selectExchangeList(reqMap);
//			
//			int size = excList.size();
//			int index = 70;
//			
//			int loop = size / index;
//			
//			for(int i=0 ; i<index ; i++){
//				resultList.add(excList.get(i * loop));
//			}
//			resultList.add(excList.get(size - 1));
//		} else {
//			int limit = 100;
//			if(month.equals("12")){
//				limit = 160;
//			} else if(month.equals("3")){
//				limit = 130;
//			}
//			reqMap.put("limit", limit);
//			
//			resultList = excDao.selectExchangeListForMonth(reqMap);
//			
//		}
		return resultList;
	}
	
	public void insertCalcLog(Map<String, Object> reqVO) {
		excDao.insertCalcLog(reqVO);
	}

	@Cacheable(cacheName="mainExc", keyGenerator=@KeyGenerator(name="StringCacheKeyGenerator"))
	public List<Map<String, Object>> selectMainExchangeList(String type) {
		
		Map<String, Object> reqMap = new HashMap<>();
		reqMap.put("type", type);
		reqMap.put("dt", Utils.getDiffDate(-1));

		List<Map<String, Object>> resultList = excDao.selectMainExchangeList(reqMap);
		
		for(Map<String, Object> tmp : resultList){

			double tRate = MapUtils.getDoubleValue(tmp, "basicRate");
			double yRate = MapUtils.getDoubleValue(tmp, "yRate");
			
			double diff = tRate - yRate;
			double percent = diff / tRate * 100;
			
			String diffVal = "0";
			String diffRate = "0";
			String diffType = "=";
			
			if(diff > 0){
				diffVal = String.format("%.2f", diff);
				diffRate = String.format("%.2f", percent);
				diffType = "+";
			} else if(diff < 0){
				diffVal = String.format("%.2f", diff * -1);
				diffRate = String.format("%.2f", percent * -1);
				diffType = "-";
			}

			tmp.put("diffVal", diffVal);
			tmp.put("diffRate", diffRate);
			tmp.put("diffType", diffType);
		}
		
		return resultList;
	}
	
}
