package com.jws.jwsapi.pallet;

import com.google.gson.annotations.SerializedName;

public class PalletCloseResponse {

    @SerializedName("success")
    private Boolean status;

    @SerializedName("message")
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
