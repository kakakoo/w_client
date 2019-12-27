package com.greit.weys.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greit.weys.bank.BankVO;

public class BankClient {

	protected static Logger logger = LoggerFactory.getLogger(BankClient.class);
	private String TARGET_URL = null;
	private String USE_CODE = null;
	private String API_KEY = null;
	private String API_SECRET = null;
	
	public BankClient(String url, String code, String key, String secret) {
		this.TARGET_URL = url;
		this.USE_CODE = code;
		this.API_KEY = key;
		this.API_SECRET = secret;
	}
	
	public Map<String, Object> getAuth(BankVO bankVO) throws Exception{
		
		String url = this.TARGET_URL + Constant.BANK_AUTH_URL + "?"
				+ "response_type=" + "code&"
				+ "client_id=" + this.USE_CODE + "&"
				+ "redirect_url=" + "https://dev.weys.exchange/weys/api/bank/test&"
				+ "scope=" + "login inquiry transfer&"
				+ "client_info=" + "33&"
				+ "auth_type=" + "0";
//				+ "&"
//				+ "Kftc-Bfop-UserName=" + URLEncoder.encode(bankVO.getUsrNm(), "UTF-8")  + "&"
//				+ "Kftc-Bfop-UserInfo=" + bankVO.getUsrBirth() + bankVO.getUsrSex() + "&"
//				+ "Kftc-Bfop-UserCellNo=" + bankVO.getUsrTel() + "&"
//				+ "Kftc-Bfop-PhoneCarrier=" + bankVO.getUsrTelTp() + "&"
//				+ "Kftc-Bfop-UserEmail=" + bankVO.getUsrEmail() + "&"
//				+ "Kftc-Bfop-BankCodeStd=" + bankVO.getUsrBankNm() + "&"
//				+ "Kftc-Bfop-AccountNum=" + bankVO.getUsrBankCd();
		
		logger.info("url ::: " + url);
		
		Map<String, Object> data = connection(url, null, null, "GET");

		if (data == null) {
			return null;
		} else {
			int code = MapUtils.getIntValue(data, "code");
			String message = MapUtils.getString(data, "message", "");
			Map<String, Object> response = (Map<String, Object>) data.get("response");
			if(code == 0){
				return response;
			} else {
				logger.info("BANK ERROR ::: code : " + code + ", message : " + message);
				return null;
			}
		}
	}

	private static Map<String, Object> connection(String targetUrl, JSONObject obj, String SB_TOKEN, String method) {

		Map<String, Object> result = new HashMap<String, Object>();
		try {

			URL url = new URL(targetUrl);

			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();

			urlConn.setDoInput(true);
			urlConn.setDoOutput(true);

			urlConn.setRequestMethod(method);

			urlConn.setRequestProperty("Content-Type", "application/json");
			if(SB_TOKEN != null)
				urlConn.setRequestProperty("Authorization", "Bearer " + SB_TOKEN);

			OutputStreamWriter output = new OutputStreamWriter(urlConn.getOutputStream());

			if(obj != null){
				logger.info("RequestData = " + obj.toString());
				output.write(obj.toString());
			}
			output.flush();

			/* Get response data. */

			StringBuilder sb = new StringBuilder();
			int HttpResult = urlConn.getResponseCode();
			
			if (HttpResult == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "utf-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();

				String sResult = sb.toString();
				logger.info("ResponseData sResult = " + sResult);

				ObjectMapper mapper = new ObjectMapper();
				result = mapper.readValue(sResult, new TypeReference<Map<String, Object>>() {});
			} else {
				BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getErrorStream(), "utf-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();

				logger.info("ResponseData = " + sb.toString());
				throw new Exception("BANK ERROR ::: " + sb.toString());
			}
		} catch (Exception e) {
			logger.info("BANK exception ::: " + e.getMessage());
			return null;
		}

		return result;
	}
}
