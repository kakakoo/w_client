package com.greit.weys.exc;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface ExcDao {

	List<Map<String, Object>> selectExchange(Map<String, Object> reqMap);

	List<Map<String, Object>> selectExchangeList(Map<String, Object> reqMap);

	void insertCalcLog(Map<String, Object> reqVO);

	List<Map<String, Object>> selectMainExchangeList(Map<String, Object> reqMap);

	List<Map<String, Object>> selectExchangeListForMonth(Map<String, Object> reqMap);

}
