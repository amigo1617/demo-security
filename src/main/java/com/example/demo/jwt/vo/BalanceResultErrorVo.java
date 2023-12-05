package com.example.demo.jwt.vo;

public class BalanceResultErrorVo {

    private String reason;

    private String result = "failed";

    public BalanceResultErrorVo(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getResult() {
        return result;
    }
}
