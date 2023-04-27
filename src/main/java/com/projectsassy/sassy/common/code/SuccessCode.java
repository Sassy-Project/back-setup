package com.projectsassy.sassy.common.code;

import lombok.Getter;

@Getter
public enum SuccessCode {
    SIGNUP_SUCCESS("SignUp", "회원가입에 성공하였습니다."),
    CAN_USE_ID("CanUseId", "사용 가능한 아이디 입니다."),
    CAN_USE_EMAIL("CanUseEmail", "사용 가능한 이메일 입니다."),

    UPDATE_PASSWORD("UpdatePassword", "비밀번호가 변경되었습니다."),
    DELETE_USER("DeleteUser", "회원이 삭제되었습니다."),
    SEND_EMAIL("SendEmail", "이메일 전송이 완료되었습니다."),
    CERTIFY_CODE("CertifyCode", "인증이 완료되었습니다."),
    LOGOUT("Logout", "로그아웃 되었습니다."),

    CREATE_ITEM("CreateItem", "아이템을 등록하였습니다."),
    DELETE_ITEM("DeleteItem", "아이템이 삭제되었습니다."),
    PURCHASE_ITEM("PurchaseItem", "아이템을 구매하였습니다."),
    CHANGE_ITEM("ChangeItem", "아이템을 변경하였습니다.");

    private final String code;
    private final String message;

    SuccessCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
