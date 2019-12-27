package com.greit.weys.common;

public class ErrCode {

	public final static int SUCCESS = 200;					// 정상
	
	public static final int	LOGIN_FAILED = 1;				// 로그인 실패(아이디 또는 비밀번호 실패)
	public static final int	LOGIN_CLOSED = 2;				// 인증 전
	public static final int	EMAIL_NOT_EXIST = 3;			// 비밀번호 찾기 시도, 존재하지 않는 메일 
	public static final int	PWD_INCORRECT = 4;				// 비밀번호 변경 시도, 일치하지 않는 비밀번호
	public static final int INVALID_CERTIFY = 5;			// SMS 수신 실패
	public static final int	USERINFO_LACK = 6;				// 실명인증 필요
	public static final int	USRTEL_NOTEXIST = 7;			// 존재하지 않는 휴대전화
	public static final int	FRDCODE_NOTEXIST = 8;			// 존재하지 않는 친구 코드
	public static final int IDENTIFY_EXIST = 9;				// 존재하는 인증
	
	public static final int	SIGNUP_EMAIL_OVERLAP = 11;		// 이메일 중복 에러
	public static final int	SIGNUP_FAILED = 12;				// 회원 가입 실패
	public static final int	SIGNUP_NICK_OVERLAP = 13;		// 닉네임 중복 에러
	public static final int DISABLE_EMOTICON = 14;			// 이모티콘입력 불가
	public static final int	SIGNUP_TEL_OVERLAP = 15;		// 전화번호 중복 에러
	public static final int	SIGNUP_ID_OVERLAP = 16;			// 개인정보 중복 에러

	public static final int	IMG_UPLOAD_FAILED = 21;			// 이미지 업로드 실패

	public static final int	ALREADY_JOIN_EVENT = 25;		// 이미 이벤트 참여 
	public static final int	FULL_JOIN_EVENT = 26;			// 이벤트 인원 제한

	public static final int	ALREADY_REPORTED = 31;			// 이미 신고한 게시물 
	public static final int	TRADE_DONE_DELETE = 32;			// 삭제 또는 완료된 게시물 

	public static final int COUPON_NOT_EXIST = 36;			// 존재하지 않는 쿠폰
	public static final int COUPON_ALREADY_USED = 37;		// 이미 쿠폰을 사용
	public static final int COUPON_PASS_DUE = 38;			// 쿠폰 사용기간 지남
	public static final int COUPON_FULL_USE = 39;			// 사용가능 쿠폰 전부 소진

	public static final int	ALREADY_BANNED = 41;			// 이미 차단한 사용자 

	public static final int	PAYMENT_CALCEL = 51;			// 결제 진행중 오류 

	public static final int RSV_REJECT_CHANGE_DATE = 530;	// 날짜변경 거부
	
	public static final int RSV_EMPTY_BANKINFO = 540;	// 은행정보 없음 
	public static final int RSV_ENOUGH_MEMBERSHIP = 550;	// 멤버십 포인트 부족 
	public static final int RSV_ALREADY_EXIST = 555;		// 이미 존재하는 예약
	public static final int RSV_ALREADY_CANCEL = 556;		// 이미 취소된 예약
	public static final int RSV_NOT_EXIST = 560;			// 존재하지 않는 예약
	public static final int RSV_IDENTIFY_INCORRECT = 570;	// 일치하지 않는 주민번호
	public static final int RSV_REJECT_CANCEL = 580;		// 취소할 수 없는 예약
	public static final int RSV_COUPON_UNAVAILABLE = 590;	// 사용할 수 없는 쿠폰
	public static final int RSV_VBANK_FAIL = 599;			// 사용할 수 없는 쿠폰

	public static final int	POINT_LACK = 61;				// 사용할 포인트 부족

	public static final int	FAIL_INSERT_BANK = 621;			// 은행 정보 등록 실패
	public static final int	FAIL_AUTH_BANK = 622;			// 계좌주 검증 실패
	public static final int	FAIL_AUTH_CODE = 623;			// 1원 인증 실패
	public static final int	FAIL_AUTH_WLF = 624;			// WLF 검증 실패
	public static final int	FAIL_OVER_SEND = 625;			// 1원 인증 횟수 초과
	public static final int	FAIL_AUTH = 626;				// 1원 인증 횟수 초과
	
	
	public static final int GIFTYSHOW_ERROR = 666;			// 기프티쇼 에러 
	
	public static final int ACCESS_CRASH = 770;				// 다른 디바이스 접속 
	public static final int INVALID_TOKEN = 777;			// 토큰 에러
	public static final int INVALID_PARAMETER = 888;		// 파라미터 에러

	public static final int	SAMPLE_ERROR = 900;				// 임시 에러 적용
	public static final int	VERSION_ERROR = 990;			// 버전 에러
	public static final int	UNKNOWN_ERROR = 999;			// 알수없는 에러
	
	public static String getMessage (int code) {
		String result = null;
		
		switch(code) {
		case SUCCESS :
			result = "정상";
			break;
		case INVALID_PARAMETER :
			result = "파라미터 에러";
			break;
		case LOGIN_FAILED :
			result = "아이디 또는 비밀번호가 일치하지 않습니다.";
			break;
		case LOGIN_CLOSED :
			result = "이메일 인증이 완료되지 않았습니다.";
			break;
		case SIGNUP_EMAIL_OVERLAP :
			result = "이미 등록된 이메일 입니다.\n"
					+ "확인 후 다시 입력해 주세요.";
			break;
		case UNKNOWN_ERROR :
			result = "알수없는 에러가 발생하였습니다.";
			break;
		case INVALID_TOKEN :
			result = "인증기간이 만료되었습니다.\n"
					+ "다시 로그인 해주세요.";
			break;
		case SIGNUP_FAILED :
			result = "회원가입 실패! \n"
					+ "다시 시도해 주세요.";
			break;
		case IMG_UPLOAD_FAILED :
			result = "이미지 업로드 실패! \n"
					+ "다시 시도해 주세요.";
			break;
		case ALREADY_REPORTED :
			result = "이미 신고한 게시물입니다.";
			break;
		case TRADE_DONE_DELETE :
			result = "삭제 또는 완료된 게시물입니다.";
			break;
		case ALREADY_BANNED :
			result = "이미 차단한 사용자입니다.";
			break;
		case ACCESS_CRASH :
			result = "다른 디바이스에서 접속했습니다.\n"
					+ "확인 후 다시 접속해주세요.";
			break;
		case EMAIL_NOT_EXIST :
			result = "가입된 이메일이 아닙니다.";
			break;
		case SIGNUP_NICK_OVERLAP :
			result = "이미 등록된 닉네임 입니다.\n"
					+ "확인 후 다시 입력해 주세요.";
			break;
		case PAYMENT_CALCEL :
			result = "결제가 취소되었습니다.\n"
					+ "다시 시도해 주세요.";
			break;
		case PWD_INCORRECT :
			result = "비밀번호가 일치하지 않습니다.";
		break;
		case POINT_LACK :
			result = "포인트가 부족합니다.";
		break;
		case DISABLE_EMOTICON :
			result = "이모티콘은 입력할 수 없습니다.";
		break;
		case VERSION_ERROR :
			result = "서버에 일치하는 버전이 없습니다.";
		break; 
		case RSV_ALREADY_EXIST :
			result = "예약이 이미 존재합니다.";
		break;
		case RSV_ENOUGH_MEMBERSHIP :
			result = "예약가능한 멤버십 한도가 부족합니다.";
		break;
		case RSV_NOT_EXIST :
			result = "존재하지 않는 예약입니다.";
		break;
		case RSV_ALREADY_CANCEL :
			result = "이미 취소된 예약입니다.";
		break;
		case RSV_IDENTIFY_INCORRECT :
			result = "이름과 주민번호가 일치하지않습니다.";
		break;
		case INVALID_CERTIFY :
			result = "국가코드와 전화번호를 확인해주세요.";
		break;
		case RSV_REJECT_CANCEL :
			result = "취소할 수 없습니다. 예약을 확인해 주세요.";
		break;
		case USERINFO_LACK :
			result = "실명인증을 해주세요.";
		break;
		case USRTEL_NOTEXIST :
			result = "존재하지않는 번호입니다.";
		break;
		case SIGNUP_TEL_OVERLAP :
			result = "존재하는 번호입니다.";
		break;
		case RSV_COUPON_UNAVAILABLE :
			result = "사용할 수 없는 쿠폰입니다.";
		break;
		case ALREADY_JOIN_EVENT :
			result = "이미 참여하신 이벤트입니다.";
		break;
		case FULL_JOIN_EVENT :
			result = "더이상 참여할 수 없습니다.";
		break;
		case COUPON_NOT_EXIST :
			result = "존재하지 않는 쿠폰입니다.";
		break;
		case COUPON_ALREADY_USED :
			result = "이미 발급받으신 쿠폰입니다.";
		break;
		case COUPON_PASS_DUE :
			result = "쿠폰 사용 기간이 지났습니다.";
		break;
		case COUPON_FULL_USE :
			result = "사용가능한 쿠폰이 모두 소진되었습니다.";
		break;
		case RSV_VBANK_FAIL :
			result = "가상계좌생성에 실패하였습니다. 다른은행을 선택해 주세요.";
		break;
		case SIGNUP_ID_OVERLAP :
			result = "이미 존재하는 개인정보입니다.";
		break;
		case FRDCODE_NOTEXIST :
			result = "존재하지 않는 친구입니다.";
		break;
		case RSV_EMPTY_BANKINFO :
			result = "환불받을 은행정보를 입력해주세요.";
		break;
		case IDENTIFY_EXIST :
			result = "이미 존재하는 사용자 정보입니다.";
		break;
		case RSV_REJECT_CHANGE_DATE :
			result = "예약 시간 및 날짜 변경이 불가능한 예약입니다. 고객센터에 문의해 주세요.";
		break;
		case FAIL_INSERT_BANK :
			result = "은행 계좌 등록에 실패하였습니다.";
		break;
		case FAIL_AUTH_BANK :
			result = "계좌주 검증에 실패하였습니다. 실명 일치 확인을 해주세요.";
		break;
		case FAIL_AUTH_CODE :
			result = "1원 인증에 실패하였습니다. 코드를 다시 확인해주세요.";
		break;
		case FAIL_AUTH_WLF :
			result = "영문 이름을 확인해주세요.";
		break;
		case FAIL_OVER_SEND :
			result = "1원인증 횟수를 초과하셨습니다. 고객센터로 문의바랍니다.";
		case FAIL_AUTH :
			result = "인증에 실패하였습니다. 고객센터로 문의바랍니다.";
		break;
		
		}
		
		return result;
	}
}

