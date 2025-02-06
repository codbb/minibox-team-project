package dev.mini.minibox.results;

public enum CommonResult implements Result {
//    FAILURE,
//    FAILURE_UNSIGNED,
//    SUCCESS,

    FAILURE("알 수 없는 이유로 결제에 실패하였습니다. 잠시 후 다시 시도해 주세요."),
    FAILURE_UNSIGNED("회원이 아닌 사용자입니다."),
    SUCCESS("결제가 완료되었습니다."),
    SEAT_ALREADY_BOOKED("이미 선택된 좌석입니다.");

    private String message;

    // 생성자에서 메시지 초기화
    CommonResult(String message) {
        this.message = message;
    }

    // 메시지 반환
    public String getMessage() {
        return this.message;
    }

    // 메시지 설정
    public void setMessage(String message) {
         this.message = message;
    }

    // name을 소문자로 변환해서 반환하는 메서드
    public String nameToLower() {
        return this.name().toLowerCase();
    }
}
