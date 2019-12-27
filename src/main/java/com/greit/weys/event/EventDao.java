package com.greit.weys.event;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface EventDao {

	List<EventVO> selectEventList();

	EventVO selectEvent(EventReqVO reqVO);

	List<EventVO> selectEventListUsr(EventReqVO reqVO);

	int selectJoinCnt(EventVO reqVO);

	int insertJoinEvent(EventVO reqVO);

	String selectEventPage(String eventId);

	int selectEventUsed(EventVO reqVO);

	int insertEventCoupon(EventVO reqVO);

	int updateEventCnt(EventVO reqVO);

	int selectEventCnt(String eventId);

}
