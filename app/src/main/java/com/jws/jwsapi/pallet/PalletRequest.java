package com.jws.jwsapi.pallet;

import com.google.gson.annotations.SerializedName;

public class PalletRequest {
    @SerializedName("balanza")
    private String scaleNumber;

    @SerializedName("palletResultante")
    private String destinationPallet;

    @SerializedName("palletOrigen")
    private String originPallet;

    public PalletRequest(String scaleNumber, String destinationPallet, String originPallet) {
        this.scaleNumber = scaleNumber;
        this.destinationPallet = destinationPallet;
        this.originPallet = originPallet;
    }

    public String getScaleNumber() {
        return scaleNumber;
    }

    public void setScaleNumber(String scaleNumber) {
        this.scaleNumber = scaleNumber;
    }

    public String getDestinationPallet() {
        return destinationPallet;
    }

    public void setDestinationPallet(String destinationPallet) {
        this.destinationPallet = destinationPallet;
    }

    public String getOriginPallet() {
        return originPallet;
    }

    public void setOriginPallet(String originPallet) {
        this.originPallet = originPallet;
    }
}