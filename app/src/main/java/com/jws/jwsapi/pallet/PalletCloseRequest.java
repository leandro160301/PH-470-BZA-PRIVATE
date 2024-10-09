package com.jws.jwsapi.pallet;

import com.google.gson.annotations.SerializedName;

public class PalletCloseRequest {

    @SerializedName("balanza")
    private String scaleNumber;

    @SerializedName("palletOrigen")
    private String originPallet;

    public PalletCloseRequest(String scaleNumber, String originPallet) {
        this.scaleNumber = scaleNumber;
        this.originPallet = originPallet;
    }

    public String getScaleNumber() {
        return scaleNumber;
    }

    public void setScaleNumber(String scaleNumber) {
        this.scaleNumber = scaleNumber;
    }

    public String getOriginPallet() {
        return originPallet;
    }

    public void setOriginPallet(String originPallet) {
        this.originPallet = originPallet;
    }
}