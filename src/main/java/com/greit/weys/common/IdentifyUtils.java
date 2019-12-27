package com.greit.weys.common;

import common.NameCheck;

public class IdentifyUtils {

	private final String SITE_CODE;
	private final String SITE_PWD;
	
	public IdentifyUtils(String SITE_CODE, String SITE_PWD) {
		this.SITE_CODE = SITE_CODE;
		this.SITE_PWD = SITE_PWD;
	}
	
	public boolean checkIdentify(String name, String jumin){
		NameCheck nck = new NameCheck();
		nck.setChkName(name);
		String jmin = nck.setJumin(jumin + this.SITE_PWD);
		if(jmin.equals("0")){
			nck.setSiteCode(this.SITE_CODE);
			nck.setTimeOut(3000);
			jmin = nck.getRtn().trim();
			if(jmin.equals("1")){
				return true;
			}
		}
		return false;
	}
}
