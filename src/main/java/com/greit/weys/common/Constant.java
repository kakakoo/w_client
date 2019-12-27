package com.greit.weys.common;

public class Constant {

	/**
	 * 버전관리
	 */
	public static final int I_SERVER_VERSION = 10;
	public static final String S_SERVER_VERSION = "v10";

	/**
	 * 예약 가능 일자
	 */
	public static final int RSV_DATE = 7;
	
	/**
	 * 취소 수수료
	 */
	public static final int CANCEL_CMS = 0;
	
	/**
	 * 예약 상태 
	 */
	public static final String RSV_START = "S";				// 1. 예약 완료
	public static final String RSV_INCOME = "I";			// 2. 입금 완료
	public static final String RSV_READY = "R";				// 3. 준비 완료
	public static final String RSV_FINISH = "F";			// 4. 거래 완료
	
	
	public static final String RSV_MISS = "M";				// 2-1. 입금 시간 초과 
	public static final String RSV_CANCEL_BEFORE = "CB";	// 2-2. 입금 전 취소 
	public static final String RSV_CANCEL = "C";			// 4-1. 예약 취소
	public static final String RSV_CANCEL_READY = "CR";		// 4-2. 환불 대기
	public static final String RSV_CANCEL_FINISH = "CF";	// 4-3. 환불 완료
	
	/**
	 * 담당자 활동 로그
	 */
	public static final String ADM_ACT_LOGIN = "L";		// 로그인
	
	public static final String ADM_ACT_RSV_READY = "RR";		// 예약 준비 완료
	public static final String ADM_ACT_RSV_FINISH = "RF";		// 예약 거래 완료
	public static final String ADM_ACT_RSV_CANCEL = "RC";		// 예약 거래 강제 취소

	/**
	 * 예약 푸쉬 메세지
	 */
	public static final String PUSH_MSG_INCOME = "입금이 정상적으로 확인되었습니다.";
	public static final String PUSH_MSG_READY = "담당자가 배정되었습니다. 인천공항에서 만나요!";
	public static final String PUSH_MSG_SOON = " 에서 만나요!";

	public static final String PUSH_MSG_ADM_NEW = "새로운 예약이 있습니다. 확인해 주세요.";
	public static final String PUSH_MSG_ADM_TOMORROW = " 건의 예약이 있습니다.";
	public static final String PUSH_MSG_ADM_CANCEL = "님의 예약이 취소되었습니다.";
	
	/**
	 * 인포뱅크 메시지 URL TEST & REAL 
	 */
	public static final String INFO_BANK_URL_TEST = "http://rest.supersms.co:6200/sms/xml";
	public static final String INFO_BANK_URL_REAL = "https://rest.supersms.co/sms/xml";
	
	/**
	 * 카카오 알림톡 URL 
	 */
	public static final String KAKAO_TALK_MSG = "https://msggw.supersms.co:9443/v1/send/kko";
	
	/**
	 * 링크허브 세금계산서 API URL
	 */
	public static final String LIMK_HUB_DEV = "https://popbill_test.linkhub.co.kr";		// 개발 서버 
	public static final String LIMK_HUB_REAL = "https://popbill.linkhub.co.kr";			// 운영 서버 
	public static final String LIMK_HUB_AUTH = "https://auth.LinkHub.co.kr";			// 인증 서버
	
	/**
	 * 은행 오픈 API 
	 */
	public static final String BANK_URL_DEV = "https://testapi.open-platform.or.kr";		// 테스트
	public static final String BANK_URL_REAL = "https://openapi.open-platform.or.kr";		// 운영

	public static final String BANK_AUTH_URL = "/oauth/2.0/authorize2";						// 사용자 인증
	public static final String BANK_TOKEN_URL = "/oauth/2.0/token";							// 토큰 발급
	public static final String BANK_ACCOUNT_URL = "/oauth/2.0/authorize_account2";			// 계좌등록 확인
	
	public static final String BANK_STATUS_URL = "/v1.0/bank/status";						// 참가은행상태조회
	
	/**
	 * 회원가입 최초 쿠폰 코드
	 */
	public static final String JOIN_COUPON_CODE = "WEYSJOINPARTYTONIGHY";
	
	/**
	 * 페이게이트 
	 */
	public static final String PAYGATE_TEST_VBANK_HOLDER = "그레잇_페이";
	public static final String PAYGATE_REAL_VBANK_HOLDER = "Greit_페이";
}
