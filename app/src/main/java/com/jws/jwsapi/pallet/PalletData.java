package com.jws.jwsapi.pallet;

import com.google.gson.annotations.SerializedName;

public class PalletData {

    @SerializedName("codigo")
    private String code;

    @SerializedName("nombre")
    private String name;

    @SerializedName("cantidad")
    private int quantity;

    @SerializedName("numeroserie")
    private String serialNumber;

    @SerializedName("pesoteorico")
    private String apiNet;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getApiNet() {
        return apiNet;
    }

    public void setApiNet(String apiNet) {
        this.apiNet = apiNet;
    }

}