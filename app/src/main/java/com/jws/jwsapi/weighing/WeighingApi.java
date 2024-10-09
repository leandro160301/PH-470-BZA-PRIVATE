package com.jws.jwsapi.weighing;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface WeighingApi {

    @POST("/testing/apiBalanza/recibirpesada")
    Single<WeighingResponse> postNewWeighing(@Body WeighingRequest weighingRequest);
}
