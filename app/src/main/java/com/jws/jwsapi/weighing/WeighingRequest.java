package com.jws.jwsapi.weighing;

import com.google.gson.annotations.SerializedName;

public class WeighingRequest {

    @SerializedName("balanza")
    private String scaleNumber;

    @SerializedName("palletOrigen")
    private String palletOrigin;

    @SerializedName("numeroSerie")
    private String serialNumber;

    @SerializedName("pesoNeto")
    private String net;

    @SerializedName("pesoBruto")
    private String gross;

    public WeighingRequest(String scaleNumber, String palletOrigin, String serialNumber, String net, String gross) {
        this.scaleNumber = scaleNumber;
        this.palletOrigin = palletOrigin;
        this.serialNumber = serialNumber;
        this.net = net;
        this.gross = gross;
    }

    public String getScaleNumber() {
        return scaleNumber;
    }

    public void setScaleNumber(String scaleNumber) {
        this.scaleNumber = scaleNumber;
    }

    public String getPalletOrigin() {
        return palletOrigin;
    }

    public void setPalletOrigin(String palletOrigin) {
        this.palletOrigin = palletOrigin;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getNet() {
        return net;
    }

    public void setNet(String net) {
        this.net = net;
    }

    public String getGross() {
        return gross;
    }

    public void setGross(String gross) {
        this.gross = gross;
    }


}