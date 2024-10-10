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
                        pallet.setClosed(false);
                        pallet.setTotalNet("0");
                        pallet.setApiNet(palletResponse.getData().getApiNet());
                        String lastSerialNumber = palletDao.getMaxSerialNumberFromPallet(palletRequest.getOriginPallet());
                        if (lastSerialNumber != null) {
                            pallet.setSerialNumber(lastSerialNumber);
                        } else {
                            pallet.setSerialNumber(palletResponse.getData().getSerialNumber());
                        }
                        palletDao.insertPallet(pallet);
                    }
                });
    }

    public Single<PalletCloseResponse> closePallet(PalletCloseRequest palletCloseRequest, int id) {
        return palletApi.closePallet(palletCloseRequest)
                .doOnSuccess(palletCloseResponse -> {
                    if (palletCloseResponse.getStatus()) {
                        palletDao.updatePalletClosedStatus(id, true);
                    }

                });
    }

    public Single<PalletCloseResponse> deletePallet(PalletCloseRequest palletCloseRequest, int id) {
        return palletApi.closePallet(palletCloseRequest)
                .doOnSuccess(palletCloseResponse -> {
                    if (palletCloseResponse.getStatus()) {
                        palletDao.deletePallet(id);
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