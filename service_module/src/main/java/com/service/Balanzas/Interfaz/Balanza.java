package com.service.Balanzas.Interfaz;

public interface Balanza {
    interface Struct{
        void setID(int numID,int numBza);
        Integer getID(int numBza);
        Float getNeto(int numBza);
        String getNetoStr(int numBza);
        Float getBruto(int numBza);
        String getBrutoStr(int numBza);
        Float getTara(int numBza);
        String getTaraStr(int numBza);
        void setTara(int numBza);
        void setCero(int numBza);
        void setTaraDigital(int numBza, float TaraDigital);
        String getTaraDigital(int numBza);
        Boolean getBandaCero(int numBza);
        void setBandaCero(int numBza, Boolean bandaCeroi);
        Float getBandaCeroValue(int numBza);
        void setBandaCeroValue(int numBza, float bandaCeroValue);
        Boolean getEstable(int numBza);
        String format(int numero, String peso);
        String getUnidad(int numBza);
        String getPicoStr(int numBza);
        Float getPico(int numBza);
        void init(int numBza);
        void escribir(String msj,int numBza);
        void stop(int numBza);
        void start(int numBza);
        Boolean calibracionHabilitada(int numBza);
        void openCalibracion(int numBza);
        Boolean getSobrecarga(int numBza);

        Boolean getEstadoCentroCero(int numBza);

        Boolean getEstadoSobrecarga(int numBza);

        Boolean getEstadoNeto(int numBza);

        Boolean getEstadoPesoNeg(int numBza);

        Boolean getEstadoBajoCero(int numBza);

        Boolean getEstadoBzaEnCero(int numBza);

        Boolean getEstadoBajaBateria(int numBza);

        String getFiltro1(int numBza);
        String getFiltro2(int numBza);
        String getFiltro3(int numBza);
        String getFiltro4(int numBza);

        Boolean getEstadoEstable(int numBza);
        String getEstado(int numBza);
        void setEstado(int numBza, String estado);
        void onEvent();
        Boolean Itw410FrmSetear(int numero,String setPoint, int Salida);//void Itw410FrmSetear(int numero,String setPoint, int Salida);
        String Itw410FrmGetSetPoint(int numero);
        Integer Itw410FrmGetSalida(int numero);
        void Itw410FrmStart(int numero);
        Integer Itw410FrmGetEstado(int numero);
        String Itw410FrmGetUltimoPeso(int numero);
        Integer Itw410FrmGetUltimoIndice(int numero);
        void itw410FrmPause(int numero);
        void itw410FrmStop(int numero);
        void Itw410FrmSetTiempoEstabilizacion(int numero, int Tiempo);
        }

}
