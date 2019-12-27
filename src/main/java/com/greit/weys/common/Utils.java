package com.greit.weys.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {

	protected static Logger logger = LoggerFactory.getLogger(Utils.class);

	private static final String[] weekDay = { "일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일" };

	/* 숫자 포맷 1000 자리 넘는거 변경 */
	public static String setStringFormatInteger(String number) {

		String underNum = "";
		if (number.contains(".")) {
			underNum = number.substring(number.indexOf("."));
			number = number.substring(0, number.indexOf("."));
		}
		int length = number.length();
		if (length < 4)
			return number + underNum;

		String prefix = number.substring(0, length - 3);
		String suffix = number.substring(length - 3);

		return setStringFormatInteger(prefix) + "," + suffix + underNum;
	}

	/* 오늘 날짜 format 에 맞춰 가져오기 */
	public static String getTodayDate(String format) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.KOREA);
		return sdf.format(date);
	}

	/**
	 * Comment : 특정 날짜로 부터 날짜 계산해서 구하기
	 * 
	 * @param when
	 *            날짜 차이 ( 2일전, 4일전 )
	 * @param year
	 *            해당 년도
	 * @param month
	 *            해당 월
	 * @param day
	 *            해당 일
	 */
	public static String getDateFormat(int when, int year, int month, int day) {

		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day);
		cal.add(Calendar.DATE, when);
		Date d = cal.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
		String date = sdf.format(d);

		return date;
	}

	/**
	 * Comment : 오늘부터 몇일 날짜 차이 구하기
	 * 
	 * @param when
	 *            날짜 차이 ( 2일전, 4일전 )
	 * @param year
	 *            해당 년도
	 * @param month
	 *            해당 월
	 * @param day
	 *            해당 일
	 */
	public static String getDiffDate(int when) {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, when);
		Date d = cal.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
		return sdf.format(d);
	}
	
	public static int getDayofMonth(){
		String getDay = getDiffDate(7);
		String [] days = getDay.split("\\.");
		if(Integer.parseInt(days[1]) < 10){
			return 0;
		}
		return Integer.parseInt(days[2]);
	}

	public static String getDiffMonth(int when) {

		Calendar cal = Calendar.getInstance();
		if (when == 1) {
			cal.add(Calendar.DATE, 30);
		} else {
			cal.add(Calendar.MONTH, when);
			cal.add(Calendar.DATE, -1);
		}
		Date d = cal.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
		return sdf.format(d);
	}

	public static long diffTwoDate(String date1, String date2) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");

		long days = 0;
		try {
			Date d1 = sdf.parse(date1);
			Date d2 = sdf.parse(date2);

			long diff = d1.getTime() - d2.getTime();
			days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return days;
	}

	public static long diffTwoMinutes(String date1, String date2) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");

		long days = 0;
		try {
			Date d1 = sdf.parse(date1);
			Date d2 = sdf.parse(date2);

			long diff = d1.getTime() - d2.getTime();
			days = TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return days;
	}

	/**
	 * 오늘 요일 구하기
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static int getTdayDay(String dt) throws ParseException {
		Calendar cal = Calendar.getInstance();
		if (dt != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
			Date date = sdf.parse(dt);
			cal.setTime(date);
		}
		return cal.get(Calendar.DAY_OF_WEEK) - 1;
	}

	/**
	 * 특정일자 요일 구하기
	 * 
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static int getDay(String date) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		Date d = sdf.parse(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		return cal.get(Calendar.DAY_OF_WEEK) - 1;
	}

	/**
	 * 특정일자 요일 구하기
	 * 
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static String getDayString(String date) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		Date d = sdf.parse(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		return weekDay[cal.get(Calendar.DAY_OF_WEEK) - 1];
	}

	/**
	 * min분 이후 구하기
	 * 
	 * @return
	 */
	public static long getAfter30Min(int min) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, min);
		return cal.getTimeInMillis() / 1000;
	}

	/**
	 * 1일 이후
	 * 
	 * @return
	 */
	public static long getAfter1day() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		return cal.getTimeInMillis() / 1000;
	}

	public static String makeCode(int length) {

		String code = "";
		for (int i = 0; i < length; i++) {
			int num = (int) (Math.random() * 36);
			String str = "";
			if (num < 10) {
				str = num + "";
			} else {
				num = num + 55;
				str = (char) num + "";
			}

			if (i == 0) {
				code = str;
			} else {
				code = code + str;
			}
		}
		return code;
	}
	
	
	public static void main(String[] args) {
		
		System.out.println(getDayofMonth());
	}

}
