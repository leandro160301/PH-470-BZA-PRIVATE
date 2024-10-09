package com.jws.jwsapi.pallet;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PalletApi {

    @POST("/testing/apiBalanza/iniciarpesada")
    Single<PalletResponse> postNewPallet(@Body PalletRequest palletRequest);

    @POST("/testing/apiBalanza/cerrarpesada")
    Single<PalletCloseResponse> closePallet(@Body PalletCloseRequest palletCloseRequest);

}