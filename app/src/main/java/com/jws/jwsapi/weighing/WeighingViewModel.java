package com.jws.jwsapi.weighing;

import static com.jws.jwsapi.pallet.PalletSerialNumber.incrementSerialNumber;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.jws.jwsapi.pallet.Pallet;
import com.jws.jwsapi.shared.ApiPreferences;
import com.jws.jwsapi.shared.PalletRepository;
import com.jws.jwsapi.shared.UserRepository;
import com.jws.jwsapi.utils.Utils;
import com.jws.jwsapi.utils.date.DateUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import au.com.bytecode.opencsv.CSVWriter;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class WeighingViewModel extends ViewModel {

    private final PalletRepository repository;
    private final WeighingService weighingService;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final LiveData<List<Weighing>> weighings;
    private final MutableLiveData<WeighingResponse> weighingResponse = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<String> errorRequest = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final LiveData<Pallet> currentPallet;
    private final UserRepository userRepository;
    private final ApiPreferences apiPreferences;

    @Inject
    public WeighingViewModel(PalletRepository repository, WeighingService weighingService, UserRepository userRepository, ApiPreferences apiPreferences) {
        this.repository = repository;
        this.weighingService = weighingService;
        this.userRepository = userRepository;
        this.apiPreferences = apiPreferences;
        this.weighings = weighingService.getAllWeighings();
        this.currentPallet = repository.getCurrentPallet();

    }

    public LiveData<Pallet> getCurrentPallet() {
        return currentPallet;
    }

    public LiveData<List<Weighing>> getWeighings() {
        return weighings;
    }

    public LiveData<WeighingResponse> getWeighingResponse() {
        return weighingResponse;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> getErrorRequest() {
        return errorRequest;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void createWeighing(String gross, String net, String tare, String unit) {
        try {
            Weighing weighing = new Weighing();
            Pallet pallet = getCurrentPallet().getValue();
            if (pallet != null) {
                weighing.setCode(pallet.getCode());
                weighing.setGross(gross);
                weighing.setTare(tare);
                weighing.setNet(net);
                weighing.setUnit(unit);
                weighing.setName(pallet.getName());
                weighing.setOperator(userRepository.getCurrentUser());
                weighing.setIdPallet(pallet.getId());
                weighing.setScaleNumber(pallet.getScaleNumber());
                weighing.setQuantity(pallet.getQuantity());
                weighing.setSerialNumber(incrementSerialNumber(pallet.getSerialNumber()));
                WeighingRequest weighingRequest = new WeighingRequest(apiPreferences.getScaleCode(), pallet.getOriginPallet(), incrementSerialNumber(pallet.getSerialNumber()), net, gross);
                createWeighingRequest(weighingRequest, weighing);
            } else {
                error.setValue("Error de pallet");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createWeighingRequest(WeighingRequest weighingRequest, Weighing weighing) throws IOException {
        loading.setValue(true);
        Integer id = repository.getCurrentPalletId();
        Pallet pallet = currentPallet.getValue();
        if (pallet != null && id != null && id > -1) {
            System.out.println("Agregando nuevo dato a csv "+ DateUtils.getHour());
            writeCsv();
            Disposable disposable = weighingService.newWeighing(weighingRequest, weighing, id, pallet)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally(() -> loading.setValue(false))
                    .subscribe(
                            weighingResponse::setValue,
                            throwable -> errorRequest.setValue(throwable.getMessage())
                    );
            compositeDisposable.add(disposable);
        } else {
            error.setValue("id null");
        }
    }

    private void writeCsv() throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(("/storage/emulated/0/Memoria/log.csv"),true),',');
        writer.writeNext(new String[]{DateUtils.getHour()});
        writer.close();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
