package com.jws.jwsapi.pallet;

import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Single;

public class PalletService {

    private final PalletApi palletApi;
    private final PalletDao palletDao;

    public PalletService(PalletApi palletApi, PalletDao palletDao) {
        this.palletApi = palletApi;
        this.palletDao = palletDao;
    }

    public Single<PalletResponse> createPallet(PalletRequest palletRequest) {
        return palletApi.postNewPallet(palletRequest)
                .doOnSuccess(palletResponse -> {
                    if (palletResponse.getSuccess()) {
                        Pallet pallet = new Pallet();
                        pallet.setOriginPallet(palletRequest.getOriginPallet());
                        pallet.setDestinationPallet(palletRequest.getDestinationPallet());
                        pallet.setScaleNumber(1);
                        pallet.setCode(palletResponse.getData().getCode());
                        pallet.setName(palletResponse.getData().getName());
                        pallet.setQuantity(palletResponse.getData().getQuantity());
                        pallet.setSerialNumber(palletResponse.getData().getSerialNumber());
                        pallet.setClosed(false);
                        pallet.setTotalNet("0");
                        pallet.setApiNet(palletResponse.getData().getApiNet());
                        palletDao.insertPallet(pallet);
                    }
                });
    }

    public Single<PalletCloseResponse> closePallet(PalletCloseRequest palletCloseRequest, String serialNumber) {
        return palletApi.closePallet(palletCloseRequest)
                .doOnSuccess(palletCloseResponse -> {
                    if (palletCloseResponse.getStatus()) {
                        palletDao.updatePalletClosedStatus(serialNumber, true);
                    }

                });
    }

    public Single<PalletCloseResponse> deletePallet(PalletCloseRequest palletCloseRequest, String serialNumber) {
        return palletApi.closePallet(palletCloseRequest)
                .doOnSuccess(palletCloseResponse -> {
                    if (palletCloseResponse.getStatus()) {
                        palletDao.deletePalletBySerialNumber(serialNumber);
                    }
                });
    }

    public LiveData<List<Pallet>> getAllPallets(Boolean open) {
        return palletDao.getAllPallets(open);
    }

    public List<Pallet> getAllPalletsStatic(Boolean open) {
        return palletDao.getAllPalletsStatic(open);
    }

}