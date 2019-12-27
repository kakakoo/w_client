package com.greit.weys.event;

public class EventVO {

	private int eventId;
	private int usrId;
	private String eventBnr;
	private String startDt;
	private String endDt;
	private String eventSt;
	private String eventUrl;
	private String eventTitle;
	private int used;
	public int getEventId() {
		return eventId;
	}
	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
	public String getEventBnr() {
		return eventBnr;
	}
	public void setEventBnr(String eventBnr) {
		this.eventBnr = eventBnr;
	}
	public String getStartDt() {
		return startDt;
	}
	public void setStartDt(String startDt) {
		this.startDt = startDt;
	}
	public String getEndDt() {
		return endDt;
	}
	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}
	public String getEventSt() {
		return eventSt;
	}
	public void setEventSt(String eventSt) {
		this.eventSt = eventSt;
	}
	public int getUsed() {
		return used;
	}
	public void setUsed(int used) {
		this.used = used;
	}
	public String getEventUrl() {
		return eventUrl;
	}
	public void setEventUrl(String eventUrl) {
		this.eventUrl = eventUrl;
	}
	public String getEventTitle() {
		return eventTitle;
	}
	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}
	public int getUsrId() {
		return usrId;
	}
	public void setUsrId(int usrId) {
		this.usrId = usrId;
	}
	@Override
	public String toString() {
		return "EventVO [eventId=" + eventId + ", usrId=" + usrId + ", eventBnr=" + eventBnr + ", startDt=" + startDt
				+ ", endDt=" + endDt + ", eventSt=" + eventSt + ", eventUrl=" + eventUrl + ", eventTitle=" + eventTitle
				+ ", used=" + used + "]";
	}
	
	public boolean checkJoinEvent(){
		
		if(eventId == 0 
				|| usrId == 0
				|| eventUrl == null){
			return false;
		}
		return true;
	}
}
