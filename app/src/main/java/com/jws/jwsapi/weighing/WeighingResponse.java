package com.jws.jwsapi.weighing;

import com.google.gson.annotations.SerializedName;

public class WeighingResponse {

    @SerializedName("success")
    private Boolean status;

    @SerializedName("error")
    private String error;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
