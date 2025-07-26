package org.lanahub.lanahub.model;

public class ResponseMsg {
    private String message;
    private String code;

    public ResponseMsg() {
    }

    public ResponseMsg(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
