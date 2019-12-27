package com.greit.weys.bank;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.greit.weys.common.BankClient;
import com.greit.weys.common.Constant;

@Service
public class BankService {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private BankDao bankDao;

	@Value("${SERVER.TYPE}")
	private String SERVER_TYPE;

	@Value("#{props['BANK.AGENT.CODE']}")
	private String BANK_AGENT_CODE;
	@Value("#{props['BANK.API.KEY']}")
	private String BANK_API_KEY;
	@Value("#{props['BANK.API.SECRET']}")
	private String BANK_API_SECRET;
	
	public Map<String, Object> checkAuth(BankVO reqVO) throws Exception {
		
		String url = SERVER_TYPE.equals("USER") ? Constant.BANK_URL_REAL : Constant.BANK_URL_DEV;
	
		BankClient bank = new BankClient(url, BANK_API_KEY, BANK_API_KEY, BANK_API_SECRET);
		
		return bank.getAuth(reqVO);
	}
	
}
