package com.greit.weys.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import javacryption.aes.AesCtr;

public class PayGateUtil {

	protected static Logger logger = LoggerFactory.getLogger(KakaoClient.class);
	
	private static final String COMMON_ENC = "UTF-8";
	private static final int FIXED_BITS = 256;

	public static void main(String[] args) throws Exception {

		String memGuid = "CMtRW8TL8ew8aKDJx9dFMV";
//		String memGuid = "V6AXcVNbie7xLmoZPbN3Ak";
//		String memGuid = "AGAp2fYxqDzywW84XKUSRi";
//		String memGuid = "Pv7ND6mQTFQJtcBvfZ24e4";
		
		
		/**
		 * 1. 멤버 등록
		 * memGuid = VAhTuWTW1nKXGhThSw2wa8
		 * {memGuid=V6AXcVNbie7xLmoZPbN3Ak, e2e=false}
		 */
//		Map<String, Object> resultMap = createMember("1");
//		String memGuid = MapUtils.getString(resultMap, "memGuid");
		
		/**
		 * 멤버 이름 업데이트
		 */
//		Map<String, Object> resultMap = updateMember("https://v5.paygate.net", "HxxULQGavugnKc5aKZ1WQM", "KGT9fBeXZ7Za5SRPvnKB8xrcWf7gVWryK7JJuNBTmaZ7dZz3m6iqhHLdZQEAk8HQ", memGuid, "Greit", "Greit", "Greit", "0262042170");
//		System.out.println(resultMap.toString());
		
		/**
		 * 멤버 조회
		 */
//		String result = selectMember(memGuid);
//		System.out.println(result);
		
		/**
		 * 실제 은행 계좌 리스트
		 * cdKey=KUKMIN_004, grpKey=BNK_CD, langCd=ko, cdNm=국민은행, cdDesc=
		 */
//		List<Map<String, Object>> bankList = getBankList("https://v5.paygate.net", "HxxULQGavugnKc5aKZ1WQM");
//		System.out.println(bankList.toString());
		
		/**
		 * 2. 실계좌 등록
		 * result == SUCCESS
		 */
//		Map<String, Object> resultMap = createRealBank("https://v5.paygate.net", "CMtRW8TL8ew8aKDJx9dFMV", "KGT9fBeXZ7Za5SRPvnKB8xrcWf7gVWryK7JJuNBTmaZ7dZz3m6iqhHLdZQEAk8HQ", "SC_023", "21520278719", memGuid);
//		System.out.println(resultMap.toString());
//		String status = MapUtils.getString(resultMap, "status", "");
//		if(!status.equals("SUCCESS")){
//			/**
//			 * 계좌정보 업데이트 실패
//			 */
//		}
		
		/**
		 * 3. 계좌주명 검증
		 * {bnkCd=KUKMIN_004, accntNo=28930****11163, tid=T102UVP, status=CHECK_BNK_NM_FINISHED, e2e=false}
		 */
//		Map<String, Object> resultMap = checkBankName(memGuid);
//		String status = MapUtils.getString(resultMap, "status");
//		if(status.equals("CHECK_BNK_NM_FINISHED")){
//			/**
//			 * 계좌주 검증 완료
//			 */
//			System.out.println("CHECK_BNK_NM_FINISHED");
//		}
		
		/**
		 * 4. 멤버의 실 계좌 소유권 검증. 1원 보내기
		 */
//		Map<String, Object> resultMap = checkBankCode("https://v5.paygate.net", "HxxULQGavugnKc5aKZ1WQM", "KGT9fBeXZ7Za5SRPvnKB8xrcWf7gVWryK7JJuNBTmaZ7dZz3m6iqhHLdZQEAk8HQ", memGuid);
//		System.out.println(resultMap.toString());
//		String status = MapUtils.getString(resultMap, "status");
//		if(status.equals("VRFY_BNK_CD_SENDING_1WON")){
//			/**
//			 * 1원 보냄
//			 */
//			System.out.println("1원 보냄");
//		}
		
		/**
		 * 5. 인증 코드 전송
		 */
//		String code = "8508";
//		Map<String, Object> resultMap = sendBankAuth(memGuid, code);
//		String activityResult = MapUtils.getString(resultMap, "activityResult");
//		if(activityResult.equals("ALERT_MSG_ANSWER_YES")){
//			/**
//			 * 인증성공
//			 */
//			System.out.println("1원 인증 성공");
//		}
		
		/**
		 * 6. WLF Search
		 */
		Map<String, Object> resultMap = wlfSearch("https://v5.paygate.net", "HxxULQGavugnKc5aKZ1WQM", "KGT9fBeXZ7Za5SRPvnKB8xrcWf7gVWryK7JJuNBTmaZ7dZz3m6iqhHLdZQEAk8HQ", memGuid);
		System.out.println(resultMap.toString());
	}

	public static Map<String, Object> updateMember(String URL, String GUID, String KEY_P, String memGuid, String fullname, String firstName, String lastName, String mobile) throws Exception{
		
		String req = "reqMemGuid=" + GUID + "&_method=PUT" 
				+ "&dstMemGuid=" + memGuid + "&fullname=" + URLEncoder.encode(fullname, COMMON_ENC)
//				+ "&dstMemGuid=" + memGuid + "&fullname=" + firstName + " " + lastName
				+ "&phoneNo=" + mobile + "&phoneCntryCd=KOR" + "&phoneTp=MOBILE"
				+ "&firstName=" + firstName + "&lastName=" + lastName
				+ "&nmLangCd=en";
		String reqUrl = makeEncCode(req, KEY_P);
		
		String url = URL + "/v5a/member/allInfo";
		url = url + "?"
				+ "&reqMemGuid=" + GUID
				+ "&_method=" + "PUT"
				+ "&encReq=" + reqUrl;
		
		Map<String, Object> data = connect(url);
		Map<String, Object> resultMap = new HashMap<>();
		
		if (data == null) {
			return data;
		} else {
			String resultString = data.toString();
			if(resultString.contains("ERROR")){
				return makeErrorMap(data);
			} else {
				resultMap = MapUtils.getMap(data, "data");
				resultMap.put("send_Url", url);
				resultMap.put("status", "SUCCESS");
			}
		}
		return resultMap;
	}

	public static String selectMember(String URL, String GUID, String KEY_P, String memGuid) throws Exception{
		
		String req = "reqMemGuid=" + GUID + "&_method=GET" 
				+ "&dstMemGuid=" + memGuid;
		String reqUrl = makeEncCode(req, KEY_P);
		
		String url = URL + "/v5a/member/privateInfo";
		url = url + "?"
				+ "&reqMemGuid=" + GUID
				+ "&_method=" + "GET"
				+ "&encReq=" + reqUrl;
		
		Map<String, Object> data = connect(url);
		String status = "";
		
		if (data == null) {
			return null;
		} else {
			status = MapUtils.getString(data, "status");
			System.out.println(data.toString());
		}
		
		return status;
	}

	public static Map<String, Object> wlfSearch(String URL, String GUID, String KEY_P, String memGuid) throws Exception{
		
		String req = "reqMemGuid=" + GUID + "&_method=POST" 
				+ "&dstMemGuid=" + GUID + "&orgMemGuid=" + memGuid
				+ "&dstMemType=OR_OUTBND_KOR_SND"
				+ "&wlfType=NM";
		String reqUrl = makeEncCode(req, KEY_P);
		
		String url = URL + "/v5/transaction/aml/initUserWLF";
		url = url + "?"
				+ "&reqMemGuid=" + GUID
				+ "&_method=" + "POST"
				+ "&encReq=" + reqUrl;
		
		Map<String, Object> data = connect(url);
		Map<String, Object> resultMap = new HashMap<>();
		
		System.out.println(data.toString());
		if (data == null) {
			return data;
		} else {
			String resultString = data.toString();
			if(resultString.contains("ERROR")){
				return makeErrorMap(data);
			} else {
				resultMap = MapUtils.getMap(data, "data");
				resultMap.put("send_Url", url);
			}
		}
		
		return resultMap;
	}

	public static Map<String, Object> sendBankAuth(String URL, String GUID, String KEY_P, String memGuid, String code) throws Exception{
		
		String req = "reqMemGuid=" + GUID + "&_method=PUT" 
				+ "&token=" + memGuid + "&text=" + code;
		String reqUrl = makeEncCode(req, KEY_P);
		
		String url = URL + "/v5/transaction/auth/token";
		url = url + "?"
				+ "&reqMemGuid=" + GUID
				+ "&_method=" + "PUT"
				+ "&encReq=" + reqUrl;
		
		Map<String, Object> data = connect(url);
		Map<String, Object> resultMap = new HashMap<>();
		
		if (data == null) {
			return data;
		} else {
			String resultString = data.toString();
			if(resultString.contains("ERROR")){
				return makeErrorMap(data);
			} else {
				resultMap = MapUtils.getMap(data, "data");
				resultMap.put("send_Url", url);
			}
		}
		return resultMap;
	}

	public static Map<String, Object> checkBankCode(String URL, String GUID, String KEY_P, String memGuid) throws Exception{
		
		String req = "reqMemGuid=" + GUID + "&_method=POST" 
				+ "&dstMemGuid=" + memGuid + "&authType=TOKEN_API";
		String reqUrl = makeEncCode(req, KEY_P);
		
		String url = URL + "/v5/transaction/seyfert/checkbankcode";
		url = url + "?"
				+ "&reqMemGuid=" + GUID
				+ "&_method=" + "POST"
				+ "&encReq=" + reqUrl;
		
		Map<String, Object> data = connect(url);
		Map<String, Object> resultMap = new HashMap<>();
		
		if (data == null) {
			return data;
		} else {
			String resultString = data.toString();
			if(resultString.contains("ERROR")){
				return makeErrorMap(data);
			} else {
				resultMap = MapUtils.getMap(data, "data");
				resultMap.put("send_Url", url);
			}
		}
		
		return resultMap;
	}

	public static Map<String, Object> checkBankName(String URL, String GUID, String KEY_P, String memGuid) throws Exception{
		
		String req = "reqMemGuid=" + GUID + "&_method=POST" 
				+ "&dstMemGuid=" + memGuid;
		String reqUrl = makeEncCode(req, KEY_P);
		
		String url = URL + "/v5/transaction/seyfert/checkbankname";
		url = url + "?"
				+ "&reqMemGuid=" + GUID
				+ "&_method=" + "POST"
				+ "&encReq=" + reqUrl;
		
		Map<String, Object> data = connect(url);
		Map<String, Object> resultMap = new HashMap<>();

		if (data == null) {
			return data;
		} else {
			String resultString = data.toString();
			if(resultString.contains("ERROR")){
				return makeErrorMap(data);
			} else {
				resultMap = MapUtils.getMap(data, "data");
				resultMap.put("send_Url", url);
			}
		}
		
		return resultMap;
	}
	
	public static Map<String, Object> createVBank(String URL, String GUID, String KEY_P, Map<String, Object> usrInfo, String memGuid) throws Exception{
		
		String nm = "웨이즈 " + MapUtils.getString(usrInfo, "rsvNm");
		String bankCd = MapUtils.getString(usrInfo, "bankCd");
		String refId = MapUtils.getString(usrInfo, "refId");
		int rsvAmnt = MapUtils.getIntValue(usrInfo, "rsvAmnt");
		
		String req = "reqMemGuid=" + GUID + "&_method=POST" 
				+ "&dstMemGuid=" + GUID 
				+ "&srcMemGuid=" + memGuid
				+ "&title=" + URLEncoder.encode(nm, COMMON_ENC)
				+ "&bnkCd=" + bankCd
				+ "&refId=" + refId
				+ "&amount=" + rsvAmnt
				+ "&crrncy=" + "KRW"
				+ "&timeout=" + 30;
		String reqUrl = makeEncCode(req, KEY_P);
		
		String url = URL + "/v5/transaction/seyfert/transfer/vaccount/payment";
		url = url + "?"
				+ "&reqMemGuid=" + GUID
				+ "&_method=" + "POST"
				+ "&encReq=" + reqUrl;
		
		Map<String, Object> data = connect(url);
		Map<String, Object> resultMap = new HashMap<>();
		
		if (data == null) {
			return null;
		} else {
			String status = MapUtils.getString(data, "status");
			if(status.equals("SUCCESS")){
				Map<String, Object> bankInfo = MapUtils.getMap(data, "data");
				
				System.out.println("bankInfo ::: "  + bankInfo);

				String tid = MapUtils.getString(bankInfo, "tid");
				String bnkCd = MapUtils.getString(bankInfo, "bnkCd");
				String accntNo = MapUtils.getString(bankInfo, "accntNo");
				
				Map<String, Object> info = MapUtils.getMap(bankInfo, "info");
				String title = MapUtils.getString(info, "title");
				long expireDt = MapUtils.getLongValue(info, "expireDt");

				resultMap.put("tid", tid);
				resultMap.put("bnkCd", bnkCd);
				resultMap.put("accntNo", accntNo);
				resultMap.put("title", title);
				resultMap.put("expireDt", expireDt);
			}
		}
		
		return resultMap;
	}

	public static List<Map<String, Object>> getVirBankList(String URL, String GUID){
		String url = URL + "/v5/code/listOf/availableVABanks/payment/one_time/ko";
		url = url + "?"
				+ "&reqMemGuid=" + GUID
				+ "&_method=" + "GET";
		
		Map<String, Object> data = connect(url);
		List<Map<String, Object>> bnkList = new ArrayList<>();
		
		if (data == null) {
			return null;
		} else {
			String status = MapUtils.getString(data, "status");
			if(status.equals("SUCCESS")){
				List<Map<String, Object>> bankList = (List<Map<String, Object>>) data.get("data");
				
				for(Map<String, Object> bnk : bankList){
					String vbankNm = MapUtils.getString(bnk, "cdNm");
					String vbankCd = MapUtils.getString(bnk, "bankCode");
					
					Map<String, Object> tmp = new HashMap<>();
					tmp.put("bankNm", vbankNm);
					tmp.put("bankCd", vbankCd);
					bnkList.add(tmp);
				}
			}
		}
		
		return bnkList;
	}

	public static List<Map<String, Object>> getBankList(String URL, String GUID){
		String url = URL + "/v5/code/listOf/banks/ko";
		url = url + "?"
				+ "&reqMemGuid=" + GUID
				+ "&_method=" + "GET";
		
		Map<String, Object> data = connect(url);
		List<Map<String, Object>> bnkList = new ArrayList<>();
		
		if (data == null) {
			return null;
		} else {
			String status = MapUtils.getString(data, "status");
			if(status.equals("SUCCESS")){
				System.out.println(data.toString());
				List<Map<String, Object>> bankList = (List<Map<String, Object>>) data.get("data");
				
				for(Map<String, Object> bnk : bankList){
					String vbankNm = MapUtils.getString(bnk, "cdNm");
					String vbankCd = MapUtils.getString(bnk, "cdKey");
					
					Map<String, Object> tmp = new HashMap<>();
					tmp.put("bankNm", vbankNm);
					tmp.put("bankCd", vbankCd);
					bnkList.add(tmp);
				}
			}
		}
		
		return bnkList;
	}

	public static Map<String, Object> createRealBank(String URL, String GUID, String KEY_P, String bankCd, String accntNo, String memGuid) throws Exception{
		
		String req = "reqMemGuid=" + GUID + "&_method=POST" 
				+ "&dstMemGuid=" + memGuid
				+ "&accntNo=" + accntNo.replaceAll("-", "")
				+ "&bnkCd=" + bankCd
				+ "&cntryCd=KOR";
		String reqUrl = makeEncCode(req, KEY_P);
		
		String url = URL + "/v5a/member/bnk";
		url = url + "?"
				+ "&reqMemGuid=" + GUID
				+ "&_method=" + "POST"
				+ "&encReq=" + reqUrl;
		
		System.out.println(url);
		
		Map<String, Object> data = connect(url);
		Map<String, Object> resultMap = new HashMap<>();
		
		if (data == null) {
			return data;
		} else {
			String resultString = data.toString();
			if(resultString.contains("ERROR")){
				String dataString = MapUtils.getString(data, "data").toString().replaceAll("\"", "").replaceAll("\n", "").replaceAll("\\{", "").replaceAll("\\}", "");
				
				StringTokenizer st = new StringTokenizer(dataString, ",");
				while(st.hasMoreTokens()){
					String token = st.nextToken();
					
					if(token.contains("cdKey") || token.contains("cdNm") || token.contains("cdDesc")){
						StringTokenizer nt = new StringTokenizer(token, ":");
						String key = nt.nextToken().trim();
						String val = nt.nextToken();
						resultMap.put(key, val);
					}
				}

				resultMap.put("status", "ERROR");
			} else {
				resultMap = MapUtils.getMap(data, "data");
				resultMap.put("send_Url", url);
				resultMap.put("status", "SUCCESS");
			}
		}
		return data;
	}
	
	public static Map<String, Object> createMember(String URL, String GUID, String KEY_P, String usrKey) throws Exception{
		
		String req = "reqMemGuid=" + GUID + "&_method=POST" + "&merchantGuid=" + usrKey + "&keyTp=GUID";
		String reqUrl = makeEncCode(req, KEY_P);
		
		String url = URL + "/v5a/member/createMember";
		url = url + "?"
				+ "reqMemGuid=" + GUID
				+ "&_method=" + "POST"
				+ "&encReq=" + reqUrl;
		
		Map<String, Object> data = connect(url);
		Map<String, Object> resultMap = new HashMap<>();
		
		if (data == null) {
			return data;
		} else {
			String resultString = data.toString();
			if(resultString.contains("ERROR")){
				String dataString = MapUtils.getString(data, "data").toString().replaceAll("\"", "").replaceAll("\n", "").replaceAll("\\{", "").replaceAll("\\}", "");
				
				StringTokenizer st = new StringTokenizer(dataString, ",");
				while(st.hasMoreTokens()){
					String token = st.nextToken();
					
					if(token.contains("cdKey") || token.contains("cdNm") || token.contains("cdDesc")){
						StringTokenizer nt = new StringTokenizer(token, ":");
						String key = nt.nextToken().trim();
						String val = nt.nextToken();
						resultMap.put(key, val);
					}
				}

				resultMap.put("status", "ERROR");
			} else {
				resultMap = MapUtils.getMap(data, "data");
				resultMap.put("send_Url", url);
			}
		}
		
		return resultMap;
	}
	
	private static String makeEncCode(String url, String KEY_P) throws Exception{
		String cipher = AesCtr.encrypt(url, KEY_P, FIXED_BITS);
		return URLEncoder.encode(cipher, COMMON_ENC);
	}
	
	private static Map<String, Object> connect(String target) {

		Map<String, Object> result = new HashMap<String, Object>();
		try {

			URL url = new URL(target);

			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();

			urlConn.setDoInput(true);
			urlConn.setDoOutput(true);

			urlConn.setRequestMethod("POST");

			urlConn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			urlConn.setRequestProperty("Accept", "application/json");

			OutputStreamWriter output = new OutputStreamWriter(urlConn.getOutputStream());
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
				result = new ObjectMapper().readValue(sb.toString(), HashMap.class);
			} else {
				BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getErrorStream(), "utf-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				result = new ObjectMapper().readValue(sb.toString(), HashMap.class);
			}
		} catch (Exception e) {
			logger.info("paygate error ::: " + e.getMessage());
			return null;
		}

		return result;
	}
	
	private static Map<String, Object> makeErrorMap(Map<String, Object> data) {
		
		Map<String, Object> resultMap = new HashMap<>();
		String dataString = MapUtils.getString(data, "data").toString().replaceAll("\"", "").replaceAll("\n", "").replaceAll("\\{", "").replaceAll("\\}", "");
		
		StringTokenizer st = new StringTokenizer(dataString, ",");
		while(st.hasMoreTokens()){
			String token = st.nextToken();
			
			if(token.contains("cdKey") || token.contains("cdNm") || token.contains("cdDesc")){
				StringTokenizer nt = new StringTokenizer(token, ":");
				String key = nt.nextToken().trim();
				String val = nt.nextToken();
				while(nt.hasMoreTokens()){
					val = val + " " + nt.nextToken();
				}
				resultMap.put(key, val);
			}
		}

		resultMap.put("status", "ERROR");
		
		return resultMap;
	}
}
