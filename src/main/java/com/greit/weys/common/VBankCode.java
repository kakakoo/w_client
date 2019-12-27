package com.greit.weys.common;

public class VBankCode {
	
	/**
	 *  - 기업은행 : 003
		- 국민은행 : 004
		- KEB하나은행 : 005
		- 농협중앙회 : 011 (세틀뱅크 테스트 계정에서만 9999오류 발생)
		- 우리은행 : 020 (세틀뱅크 테스트 계정에서만 9999오류 발생)
		- SC제일은행 : 023
		- 한국씨티은행 : 027
		- 대구은행 : 031
		- 부산은행 : 032
		- 광주은행 : 034
		- 경남은행 : 039
		- 우체국 : 071
		- 신한은행 : 088 (세틀뱅크 테스트 계정에서만 9999오류 발생)
	 */

	public final static String IBK = "003";
	public final static String KB = "004";
	public final static String KEB = "005";
	public final static String NH = "011";
	public final static String WR = "020";
	public final static String SC = "023";
	public final static String CT = "027";
	public final static String DG = "031";
	public final static String BS = "032";
	public final static String GJ = "034";
	public final static String KN = "039";
	public final static String EP = "071";
	public final static String SH = "088";

	public static String getMessage(String code) {
		String result = null;

		switch (code) {
		case "003":
			result = "기업은행";
			break;
		case "004":
			result = "국민은행";
			break;
		case "005":
			result = "KEB하나은행";
			break;
		case "011":
			result = "농협중앙회";
			break;
		case "020":
			result = "우리은행";
			break;
		case "023":
			result = "SC제일은행";
			break;
		case "027":
			result = "한국씨티은행";
			break;
		case "031":
			result = "대구은행";
			break;
		case "032":
			result = "부산은행";
			break;
		case "034":
			result = "광주은행";
			break;
		case "039":
			result = "경남은행";
			break;
		case "071":
			result = "우체국";
			break;
		case "088":
			result = "신한은행";
			break;
		}
		return result;
	}
}
