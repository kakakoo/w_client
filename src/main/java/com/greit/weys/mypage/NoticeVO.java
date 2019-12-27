package com.greit.weys.mypage;

public class NoticeVO {

	private int id;
	private String title;
	private String content;
	private String dttm;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDttm() {
		return dttm;
	}
	public void setDttm(String dttm) {
		this.dttm = dttm;
	}
	@Override
	public String toString() {
		return "NoticeVO [title=" + title + ", content=" + content + ", dttm=" + dttm + "]";
	}
	
}
