package com.jws.jwsapi.pallet;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.jws.jwsapi.shared.PalletRepository;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

@HiltViewModel
public class PalletViewModel extends ViewModel {

    private final PalletRepository palletRepository;
    private final PalletService palletService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final LiveData<List<Pallet>> pallets;
    private final MutableLiveData<PalletResponse> palletResponse = new MutableLiveData<>();
    private final MutableLiveData<PalletCloseResponse> palletCloseResponse = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Integer> scale = new MutableLiveData<>();
    private final MutableLiveData<String> palletOrigin = new MutableLiveData<>();
    private final MutableLiveData<String> palletDestination = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isClosed = new MutableLiveData<>();

    @Inject
    public PalletViewModel(PalletService palletService, PalletRepository palletRepository) {
        this.palletService = palletService;
        this.palletRepository = palletRepository;
        pallets = Transformations.switchMap(isClosed, active -> {
            if (active != null) {
                return palletService.getAllPallets(active);
            } else {
                return new MutableLiveData<>(new ArrayList<>());
            }
        });

        isClosed.setValue(false);
    }

    public LiveData<List<Pallet>> getPallets() {
        return pallets;
    }

    public LiveData<PalletResponse> getPalletResponse() {
        return palletResponse;
    }

    public LiveData<PalletCloseResponse> getPalletCloseResponse() {
        return palletCloseResponse;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void setScale(Integer scale) {
        this.scale.setValue(scale);
    }

    public void setPalletOrigin(String palletOrigin) {
        this.palletOrigin.setValue(palletOrigin);
    }

    public void setPalletDestination(String palletDestination) {
        this.palletDestination.setValue(palletDestination);
    }

    public void clearPalletData() {
        this.palletOrigin.setValue("");
        this.palletDestination.setValue("");
    }

    public void createPallet() {
        if (isValidPallet()) {
            PalletRequest palletRequest = new PalletRequest("c16c9ac1deca7c4db51e8c73800d4ced",
                    palletDestination.getValue(),
                    palletOrigin.getValue());
            createPalletRequest(palletRequest);
        } else {
            error.setValue("Complete los datos");
        }

    }

    private boolean isValidPallet() {
        return scale.getValue() != null && palletOrigin.getValue() != null && palletDestination.getValue() != null
                && !palletOrigin.getValue().isEmpty() && !palletDestination.getValue().isEmpty();
    }

    public void createPalletRequest(PalletRequest palletRequest) {
        loading.setValue(true);

        Disposable disposable = palletService.createPallet(palletRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> loading.setValue(false))
                .subscribe(
                        palletResponse -> {
                            this.palletResponse.setValue(palletResponse);
                            clearPalletData();
                        },
                        throwable -> {
                            if (throwable instanceof HttpException) {
                                ResponseBody responseBody = ((HttpException) throwable).response().errorBody();
                                try {
                                    String errorBody = responseBody.string();
                                    JSONObject jsonObject = new JSONObject(errorBody);
                                    String errorMessage = jsonObject.optString("message", "Error desconocido");
                                    error.setValue(errorMessage);
                                } catch (Exception e) {
                                    error.setValue("Error al leer el mensaje de error");
                                }
                            } else {
                                error.setValue("Error inesperado: " + throwable.getMessage());
                            }
                        }
                );

        compositeDisposable.add(disposable);
    }

    public void closePallet(String serialNumber) {
        Pallet pallet = palletRepository.getCurrentPallet().getValue();
        if (pallet != null) {
            handlePalletRequest(palletService.closePallet(new PalletCloseRequest("c16c9ac1deca7c4db51e8c73800d4ced", pallet.getOriginPallet()), serialNumber));
        }

    }

    public void deletePallet(String serialNumber) {
        Pallet pallet = palletRepository.getCurrentPallet().getValue();
        if (pallet != null) {
            handlePalletRequest(palletService.deletePallet(new PalletCloseRequest("c16c9ac1deca7c4db51e8c73800d4ced", pallet.getOriginPallet()), serialNumber));
        }
    }

    private void handlePalletRequest(Single<PalletCloseResponse> request) {
        loading.setValue(true);
        Disposable disposable = request
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> loading.setValue(false))
                .subscribe(
                        palletCloseResponse::setValue,
                        throwable -> error.setValue(throwable.getMessage())
                );

        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }

    public void setCurrentPallet(Pallet pallet) {
        palletRepository.setCurrentPallet(pallet.getId());
    }

    public void setupPalletOpen() {
        isClosed.setValue(false);
    }

    public void setupPalletClose() {
        isClosed.setValue(true);
    }

}
