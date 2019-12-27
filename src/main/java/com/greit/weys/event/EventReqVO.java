package com.greit.weys.event;

public class EventReqVO {

	private String pageType;
	private int eventId;
	private int usrId;
	public String getPageType() {
		return pageType;
	}
	public void setPageType(String pageType) {
		this.pageType = pageType;
	}
	public int getEventId() {
		return eventId;
	}
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	public int getUsrId() {
		return usrId;
	}
	public void setUsrId(int usrId) {
		this.usrId = usrId;
	}
	@Override
	public String toString() {
		return "EventReqVO [pageType=" + pageType + ", eventId=" + eventId + ", usrId=" + usrId + "]";
	}
}
