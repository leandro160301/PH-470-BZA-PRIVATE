package com.jws.jwsapi.pallet;

import com.google.gson.annotations.SerializedName;

public class PalletResponse {

    @SerializedName("success")
    private Boolean success;

    @SerializedName("data")
    private PalletData data;

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setData(PalletData data) {
        this.data = data;
    }

    public PalletData getData() {
        return data;
    }

    public Boolean getSuccess() {
        return success;
    }

}
