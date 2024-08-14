package com.jws.jwsapi.feature.formulador.viewmodel;

import android.app.Application;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.jws.jwsapi.feature.formulador.data.preferences.PreferencesManager;
import com.jws.jwsapi.feature.formulador.di.RecetaManager;
import java.util.ArrayList;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class Form_PrincipalViewModel extends ViewModel {
    private final RecetaManager recetaManager;
    private PreferencesManager preferencesManager;

    @Inject
    public Form_PrincipalViewModel(RecetaManager recetaManager, Application application) {
        this.recetaManager = recetaManager;
        this.preferencesManager = new PreferencesManager(application.getApplicationContext());
        initializeRecetaManager();
    }

    private void initializeRecetaManager() {
        this.recetaManager.pasoActual=preferencesManager.getPasoActual();
        this.recetaManager.estado = preferencesManager.getEstado();
        this.recetaManager.cantidad.setValue(preferencesManager.getCantidad());
        this.recetaManager.realizadas.setValue(preferencesManager.getRealizadas());
        this.recetaManager.netoTotal.setValue(preferencesManager.getNetototal());
        this.recetaManager.recetaActual =preferencesManager.getRecetaactual();
        this.recetaManager.codigoReceta =preferencesManager.getCodigoRecetaactual();
        this.recetaManager.nombreReceta =preferencesManager.getNombreRecetaactual();
        this.recetaManager.listRecetaActual = preferencesManager.getPasosRecetaActual();
        this.recetaManager.ejecutando=preferencesManager.getEjecutando();
        this.recetaManager.recetaComoPedido =preferencesManager.getRecetacomopedido();
        this.recetaManager.porcentajeReceta=preferencesManager.getPorcentajeReceta();
        if(this.recetaManager.listRecetaActual ==null){
            this.recetaManager.listRecetaActual =new ArrayList<>();
        }
    }
    public MutableLiveData<String> getNetoTotal(){return recetaManager.netoTotal;}
    public MutableLiveData<Integer> getCantidad(){return recetaManager.cantidad;}
    public MutableLiveData<Integer> getRealizadas(){return recetaManager.realizadas;}

}