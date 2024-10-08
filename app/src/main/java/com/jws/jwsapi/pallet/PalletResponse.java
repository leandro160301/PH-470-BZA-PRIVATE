package com.jws.jwsapi.pallet;

import com.google.gson.annotations.SerializedName;

public class PalletResponse {

    @SerializedName("data")
    private PalletData data;

    public PalletData getData() {
        return data;
    }

}
