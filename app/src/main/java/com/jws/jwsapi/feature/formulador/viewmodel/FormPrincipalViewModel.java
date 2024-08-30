package com.jws.jwsapi.feature.formulador.viewmodel;

import static com.jws.jwsapi.utils.Utils.format;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.jws.jwsapi.feature.formulador.data.preferences.PreferencesManager;
import com.jws.jwsapi.feature.formulador.data.repository.RecipeRepository;
import com.jws.jwsapi.feature.formulador.di.LabelManager;
import com.jws.jwsapi.feature.formulador.di.RecetaManager;
import com.jws.jwsapi.feature.formulador.manager.BalanzaManager;
import com.jws.jwsapi.feature.formulador.models.FormModelReceta;
import com.jws.jwsapi.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FormPrincipalViewModel extends ViewModel {
    private final RecetaManager recetaManager;
    private final PreferencesManager preferencesManager;
    private final RecipeRepository recipeRepository;
    private final LabelManager labelManager;
    private final MutableLiveData<String> mensajeError = new MutableLiveData<>();
    private final BalanzaManager balanzaManager;
    public LiveData<String> mensajeToastError = mensajeError;
    private final int MODO_USO_BATCH=0;
    private final int MODO_USO_PEDIDO=0;

    @Inject
    public FormPrincipalViewModel(RecetaManager recetaManager, PreferencesManager preferencesManager,RecipeRepository recipeRepository, LabelManager labelManager) {
        this.recetaManager = recetaManager;
        this.preferencesManager = preferencesManager;
        this.recipeRepository = recipeRepository;
        this.labelManager = labelManager;
        this.balanzaManager = new BalanzaManager(preferencesManager);
        preferencesManager.setIndice(0);
    }


    public boolean ejecutarReceta(List<FormModelReceta> lista) {
        recetaManager.listRecetaActual = lista;
        preferencesManager.setPasosRecetaActual(recetaManager.listRecetaActual);
        if(recetaManager.listRecetaActual.size()>0){
            setupModoUso();
            return true;
        }else{
            mostrarMensajeDeError("Error en la receta elegida");
            return false;
        }
    }

    private void setupModoUso() {
        int mododeuso=preferencesManager.getModoUso();
        if(mododeuso==MODO_USO_BATCH)setupModoUso(false);
        if(mododeuso==MODO_USO_PEDIDO)setupModoUso(true);
        configurarRecetaParaPedido();
    }

    public boolean modoKilos() {
        if(preferencesManager.getModoUso()==MODO_USO_BATCH||!preferencesManager.getRecetaComoPedidoCheckbox()){
            return true;
        }else{  //por pedido
            if(recetaManager.realizadas.getValue()!=null&&recetaManager.realizadas.getValue()==0){
                return true;
            }else {
                mostrarMensajeDeError("Ingrese una cantidad");
                return false;
            }
        }
    }

    public void actualizarBarraProceso(int num, String unidad) {
        if(isManualOEstadoProceso()){
            String kilosIng=recetaManager.listRecetaActual.get(recetaManager.pasoActual - 1).getKilosIng();
            String kilosDesc=recetaManager.listRecetaActual.get(recetaManager.pasoActual - 1).getDescIng();
            if(Utils.isNumeric(kilosIng)){
                if(Float.parseFloat(kilosIng)==0){
                    String texto="Ingrese "+ kilosDesc +" en balanza "+ num;
                    if(recetaManager.automatico){
                        texto="Cargando "+ kilosDesc +" en salida "+ preferencesManager.getSalida();
                    }
                    recetaManager.estadoMensajeStr.setValue(texto);
                }else{
                    String texto="Ingrese "+ kilosIng + unidad+ " de "+ kilosDesc + " en balanza "+ num;
                    if(recetaManager.automatico){
                        texto="Cargando "+ kilosIng + unidad+ " de "+ kilosDesc + " en salida "+ preferencesManager.getSalida();
                    }
                    recetaManager.estadoMensajeStr.setValue(texto);
                }
            }
        }
    }

    private boolean isManualOEstadoProceso() {
        return !recetaManager.automatico || recetaManager.estadoBalanza == RecetaManager.PROCESO;
    }


    public int determinarBalanza() {
        String kilos = recetaManager.listRecetaActual.get(recetaManager.pasoActual - 1).getKilosIng();
        return balanzaManager.determinarBalanza(kilos);
    }


    private void configurarRecetaParaPedido() {
        if((preferencesManager.getRecetaComoPedido()||preferencesManager.getRecetaComoPedidoCheckbox())&&recetaManager.cantidad.getValue()!=null){
            float kilosTotalesFloat=0;
            List<FormModelReceta> nuevareceta=new ArrayList<>();
            kilosTotalesFloat = getKilosTotales(kilosTotalesFloat, nuevareceta);
            updateRecetaYTotales(kilosTotalesFloat, nuevareceta);
        }
    }

    private float getKilosTotales(float kilosTotalesFloat, List<FormModelReceta> nuevareceta) {
        for(int i = 0; i<recetaManager.listRecetaActual.size(); i++){
            kilosTotalesFloat = instanciaPorCantidad(kilosTotalesFloat, nuevareceta, i);
        }
        return kilosTotalesFloat;
    }

    private float instanciaPorCantidad(float kilosTotalesFloat, List<FormModelReceta> nuevaReceta, int i) {
        if(recetaManager.cantidad.getValue()!=null) return 0;
        for(int k = 0; k<recetaManager.cantidad.getValue(); k++){
            FormModelReceta nuevaInstancia = getNuevaInstancia(i);
            nuevaReceta.add(nuevaInstancia); // si en vez de crear la nueva instancia le pasamos mainActivity.mainClass.listRecetaActual.get(i) entonces apuntara a las mismas direcciones de memoria
            if(Utils.isNumeric(recetaManager.listRecetaActual.get(i).getKilosIng())){
                kilosTotalesFloat = kilosTotalesFloat +Float.parseFloat(recetaManager.listRecetaActual.get(i).getKilosIng());
            }
        }
        return kilosTotalesFloat;
    }

    private void updateRecetaYTotales(float kilosTotalesFloat, List<FormModelReceta> nuevaReceta) {
        for(int i = 0; i< nuevaReceta.size(); i++){
            nuevaReceta.get(i).setKilos_totales(String.valueOf(kilosTotalesFloat));
        }
        recetaManager.listRecetaActual = nuevaReceta;
        preferencesManager.setPasosRecetaActual(recetaManager.listRecetaActual);
    }

    private FormModelReceta getNuevaInstancia(int i) {
        return new FormModelReceta(
                recetaManager.listRecetaActual.get(i).getCodigo(),
                recetaManager.listRecetaActual.get(i).getNombre(),
                recetaManager.listRecetaActual.get(i).getKilosTotales(),
                recetaManager.listRecetaActual.get(i).getCodigoIng(),
                recetaManager.listRecetaActual.get(i).getDescIng(),
                recetaManager.listRecetaActual.get(i).getKilosIng(),
                recetaManager.listRecetaActual.get(i).getKilosRealesIng(),
                recetaManager.listRecetaActual.get(i).getToleranciaIng()
        );
    }

    public void nuevoLoteFecha() {
        String lote = recipeRepository.getNuevoLoteFecha();
        if (lote != null) {
            labelManager.olote.value = lote;
        }
    }

    public void verificarNuevoLoteFecha() {
        String lote = recipeRepository.verificarNuevoLoteFecha();
        if (lote != null) {
            labelManager.olote.value = lote;
        }
    }


    public void restaurarDatos() {
        boolean aRealizarFinalizados=aRealizarFinalizados();
        restaurarLote(aRealizarFinalizados);
        restaurarVencimiento(aRealizarFinalizados);
        restaurarCampo1(aRealizarFinalizados);
        restaurarCampo2(aRealizarFinalizados);
        restaurarCampo3(aRealizarFinalizados);
        restaurarCampo4(aRealizarFinalizados);
        restaurarCampo5(aRealizarFinalizados);
    }

    private void restaurarCampo5(boolean arealizarfinalizados) {
        if(preferencesManager.getResetCampo5()==1){
            if(arealizarfinalizados){
                preferencesManager.setCampo5Valor("");
                labelManager.ocampo5.value="";
            }
        }
        if(preferencesManager.getResetCampo5()==2){
            preferencesManager.setCampo5Valor("");
            labelManager.ocampo5.value="";
        }
    }

    private void restaurarCampo4(boolean arealizarfinalizados) {
        if(preferencesManager.getResetCampo4()==1){
            if(arealizarfinalizados){
                preferencesManager.setCampo4Valor("");
                labelManager.ocampo4.value="";
            }
        }
        if(preferencesManager.getResetCampo4()==2){
            preferencesManager.setCampo4Valor("");
            labelManager.ocampo4.value="";
        }
    }

    private void restaurarCampo3(boolean arealizarfinalizados) {
        if(preferencesManager.getResetCampo3()==1){
            if(arealizarfinalizados){
                preferencesManager.setCampo3Valor("");
                labelManager.ocampo3.value="";
            }
        }
        if(preferencesManager.getResetCampo3()==2){
            preferencesManager.setCampo3Valor("");
            labelManager.ocampo3.value="";
        }
    }

    private void restaurarCampo2(boolean arealizarfinalizados) {
        if(preferencesManager.getResetCampo2()==1){
            if(arealizarfinalizados){
                preferencesManager.setCampo2Valor("");
                labelManager.ocampo2.value="";
            }
        }
        if(preferencesManager.getResetCampo2()==2){
            preferencesManager.setCampo2Valor("");
            labelManager.ocampo2.value="";
        }
    }

    private void restaurarCampo1(boolean arealizarfinalizados) {
        if(preferencesManager.getResetCampo1()==1){
            if(arealizarfinalizados){
                preferencesManager.setCampo1Valor("");
                labelManager.ocampo1.value="";
            }
        }
        if(preferencesManager.getResetCampo1()==2){
            preferencesManager.setCampo1Valor("");
            labelManager.ocampo1.value="";
        }
    }

    private void restaurarLote(boolean arealizarfinalizados) {
        if(preferencesManager.getResetLote()==1){
            if(arealizarfinalizados){
                preferencesManager.setLote("");
                labelManager.olote.value="";
            }
        }
        if(preferencesManager.getResetLote()==2){
            preferencesManager.setLote("");
            labelManager.olote.value="";
        }
    }

    private void restaurarVencimiento(boolean aRealizarFinalizados) {
        if(preferencesManager.getResetVencimiento()==1){
            if(aRealizarFinalizados){
                preferencesManager.setVencimiento("");
                labelManager.ovenci.value="";
            }
        }
        if(preferencesManager.getResetVencimiento()==2){
            preferencesManager.setVencimiento("");
            labelManager.ovenci.value="";
        }
    }

    private boolean aRealizarFinalizados() {
        boolean aRealizarFinalizados;
        aRealizarFinalizados = isCantidadFinalizada();
        aRealizarFinalizados = isArealizarfinalizados(aRealizarFinalizados);
        return aRealizarFinalizados;
    }

    private boolean isCantidadFinalizada() {
        if(recetaManager.cantidad.getValue()!=null&&recetaManager.cantidad.getValue()>0&&recetaManager.realizadas.getValue()!=null&&(recetaManager.cantidad.getValue()-recetaManager.realizadas.getValue()>0)){
            recetaManager.realizadas.setValue(recetaManager.realizadas.getValue()+1);
            preferencesManager.setRealizadas(recetaManager.realizadas.getValue());
            return recetaManager.cantidad.getValue() <= recetaManager.realizadas.getValue();
        }
        return false;
    }

    private boolean isArealizarfinalizados(boolean aRealizarFinalizados) {
        if(preferencesManager.getModoUso()==1||preferencesManager.getRecetaComoPedidoCheckbox()){//receta como pedido
            recetaManager.realizadas.setValue(recetaManager.cantidad.getValue());
            preferencesManager.setRealizadas(recetaManager.cantidad.getValue());
            aRealizarFinalizados =true;
        }
        return aRealizarFinalizados;
    }


    public void calculaPorcentajeError(float neto, String netoStr, String unidad) {
        recetaManager.listRecetaActual.get(recetaManager.pasoActual - 1).setKilos_reales_ing(netoStr);
        labelManager.okilosreales.value= netoStr +unidad;
        float porcentaje=((neto*100)/recetaManager.setPoint)-100;
        if(porcentaje<0){
            porcentaje=porcentaje*(-1);
        }
        recetaManager.listRecetaActual.get(recetaManager.pasoActual - 1).setTolerancia_ing(format(String.valueOf(porcentaje),2) + "%");
    }


    public void setearModoUsoDialogo(String texto, boolean checkbox, int modoDeUso) {
        if(recetaManager.cantidad.getValue()!=null&&Float.parseFloat(texto)>0){
            recetaManager.cantidad.setValue ((int) Float.parseFloat(texto));
            preferencesManager.setCantidad(recetaManager.cantidad.getValue());
            recetaManager.realizadas.setValue(0);
            preferencesManager.setRealizadas(0);
            if(modoDeUso==1){
                setupModoUsoCheckBox(true);
            }else{
                setupModoUsoCheckBox(false);
                if(checkbox){
                    setupModoUsoCheckBox(true);
                }
            }
        }
    }

    private void setupModoUsoCheckBox(boolean modo) {
        recetaManager.recetaComoPedido =modo;
        setupModoUso(modo);

    }

    public void setupModoUso(boolean modo) {
        preferencesManager.setRecetacomopedido(modo);
        preferencesManager.setRecetacomopedidoCheckbox(modo);
    }

    public void setEstadoPesar() {
        recetaManager.estado = 2;
        preferencesManager.setEstado(2);
    }

    public void setupValoresParaInicio() {
        recetaManager.ejecutando.setValue(true);
        recetaManager.pasoActual=1;
        recetaManager.netoTotal.setValue("0");
        preferencesManager.setNetototal("0");
        preferencesManager.setEjecutando(true);
        preferencesManager.setPasoActual(recetaManager.pasoActual);
        labelManager.onetototal.value = "0";
        labelManager.opaso.value=recetaManager.pasoActual;

    }

    public void detener(){
        labelManager.oidreceta.value="0";
        preferencesManager.setRecetaId(0);
        preferencesManager.setPedidoId(0);
        preferencesManager.setEstado(0);
        preferencesManager.setEjecutando(false);
        preferencesManager.setAutomatico(false);
        recetaManager.estado=0;
        recetaManager.ejecutando.setValue(false);
        recetaManager.estadoBalanza=RecetaManager.DETENIDO;
        recetaManager.automatico=false;
    }

    public boolean verificarComienzo() {
        boolean empezar=true;
        if(labelManager.olote.value==""||labelManager.ovenci.value==""||faltanCampos()){
            mostrarMensajeDeError("Faltan ingresar datos");
            empezar=false;
        }
        if(recetaManager.recetaActual.isEmpty()){
            mostrarMensajeDeError("Debe seleccionar una receta para comenzar");
            empezar=false;
        }
        return empezar;
    }

    private boolean faltanCampos() {
        String[] campos = {preferencesManager.getCampo1(), preferencesManager.getCampo2(), preferencesManager.getCampo3(), preferencesManager.getCampo4(), preferencesManager.getCampo5()};
        String[] valores = {
                (String)labelManager.ocampo1.value,
                (String)labelManager.ocampo2.value,
                (String)labelManager.ocampo3.value,
                (String)labelManager.ocampo4.value,
                (String)labelManager.ocampo5.value
        };
        for (int i = 0; i < campos.length; i++) {
            if (!campos[i].isEmpty() && valores[i].isEmpty()) return true;
        }
        return false;
    }


    public void mostrarMensajeDeError(String mensaje) {
        mensajeError.setValue(mensaje);
    }

    public LiveData<String> getNetoTotal() {
        return recetaManager.netoTotal;
    }

    public LiveData<Integer> getCantidad() {
        return recetaManager.cantidad;
    }

    public LiveData<Integer> getRealizadas() {
        return recetaManager.realizadas;
    }

    public LiveData<String> getEstadoMensajeStr() {
        return recetaManager.estadoMensajeStr;
    }

    public LiveData<Boolean> getEjecutando() {
        return recetaManager.ejecutando;
    }

}