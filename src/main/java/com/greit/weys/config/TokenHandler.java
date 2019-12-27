package com.greit.weys.config;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greit.weys.user.UserDetailVO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenHandler {
	
	public static final String SECREKEY = "TWoeKYesN";
	public static final String TOKEN_VALUES_KEY = "roles";
	private static final int EXPIRES_DAYS = 365; // 365일 
	
	protected static Logger logger = LoggerFactory.getLogger(TokenHandler.class);

	// 토큰 생성
	public static String getToken(UserDetailVO loginInfo) {
		
		Calendar cal = Calendar.getInstance();
		Date dt = new Date();
		cal.setTime(dt);
		cal.add(Calendar.DATE, EXPIRES_DAYS);
		
		Date d = cal.getTime();

		TokenValues values = new TokenValues();

		values.setUserKey(String.valueOf(loginInfo.getUsrId()));			// 사용자 Key
		values.setUserName(loginInfo.getUsrNm() == null ? "" : loginInfo.getUsrNm());		// 사용자 명
		values.setUserEmail(loginInfo.getUsrEmail());	// 사용자 이메일
		
		return Jwts.builder()
				.setId(values.getUserKey())
				.setSubject(values.getUserEmail())
				.setExpiration(d)
				.claim("name", values.getUserName())
				.setIssuedAt(new Date())
				.signWith(SignatureAlgorithm.HS256, SECREKEY).compact();
	}
	
	// 회원정보 리턴
	public static TokenValues getTokenValues(HttpServletRequest req, HttpServletResponse res) throws Exception  {
		TokenValues values = new TokenValues();
		
		try{
			final Claims claims = (Claims) req.getAttribute("claims");
			
			if (claims == null) {
				logger.info("claims null");
				return null;
			}
			
			String userKey = claims.getId();						// 사용자 Key
			String userEmail = claims.getSubject();					// 사용자 이메일
			String userName = claims.get("name", String.class);		// 사용자 명
			
			if (userKey == null || userEmail == null || userName == null) {
				return null;
			}
			
			values.setUserKey(userKey);
			values.setUserEmail(userEmail);
			values.setUserName(userName);
		} catch (Exception e){
			logger.info("error ::: " + e.getMessage());
			return null;
		}
		
		return values;
	}
}
