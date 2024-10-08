package com.jws.jwsapi.pallet;

import com.google.gson.annotations.SerializedName;

public class PalletResponse {

    @SerializedName("success")
    private Boolean success;

    @SerializedName("data")
    private PalletResponseData data;

    public PalletResponseData getData() {
        return data;
    }

    public void setData(PalletResponseData data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}
