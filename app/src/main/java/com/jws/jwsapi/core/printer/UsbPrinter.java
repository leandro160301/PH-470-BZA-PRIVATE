package com.jws.jwsapi.core.printer;

import android.content.Context;
import android.hardware.usb.UsbManager;

import com.jws.jwsapi.MainActivity;
import com.jws.jwsapi.core.printer.utils.DiscoveredPrinterListAdapter;
import com.jws.jwsapi.core.printer.utils.SelectedPrinterManager;
import com.jws.jwsapi.R;
import com.jws.jwsapi.utils.ToastHelper;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.printer.FieldDescriptionData;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;
import com.zebra.sdk.printer.discovery.DiscoveredPrinter;
import com.zebra.sdk.printer.discovery.DiscoveryHandler;
import com.zebra.sdk.printer.discovery.UsbDiscoverer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsbPrinter {

    List<FieldDescriptionData> variablesList = new ArrayList<FieldDescriptionData>();
    Connection connection;
    DiscoveredPrinterListAdapter discoveredPrinterListAdapter;
    Map<Integer, String> vars;


    private MainActivity mainActivity;

    public UsbPrinter(MainActivity activity) {
        this.mainActivity = activity;
    }


    public void Imprimir(String Etiqueta,Context context,Boolean Memoria,List<String> ListaMemoria){
        Runnable myRunnable = () -> {
            try {
                discoveredPrinterListAdapter = new DiscoveredPrinterListAdapter(context);
                UsbManager usbManager = (UsbManager) mainActivity.getSystemService(Context.USB_SERVICE);

                UsbDiscoverer.findPrinters(usbManager, new DiscoveryHandler() {
                    public void foundPrinter(final DiscoveredPrinter printer) {
                        discoveredPrinterListAdapter.addPrinter(printer);
                    }
                    public void discoveryFinished() { }
                    public void discoveryError(String message) { }
                });
                Thread.sleep(300);
                if(discoveredPrinterListAdapter.getCount()>0){
                    DiscoveredPrinter printer = discoveredPrinterListAdapter.getPrinter(0);
                    SelectedPrinterManager.setSelectedPrinter(printer);
                    DiscoveredPrinter formatPrinter;
                    formatPrinter = SelectedPrinterManager.getSelectedPrinter();
                    Print(Etiqueta,Memoria,ListaMemoria);
                }

            } catch (InterruptedException e) {
                ToastHelper.message("usb init:"+e.getMessage(),R.layout.item_customtoasterror,mainActivity);
            }
        };

        Thread myThread = new Thread(myRunnable);
        myThread.start();



    }

    protected void Print(String Etiqueta,Boolean Memoria,List<String> ListaMemoria) {

        try {
            connection = SelectedPrinterManager.getPrinterConnection();
        }catch (Exception e){
            ToastHelper.message("usb 0:"+e.getMessage(), R.layout.item_customtoasterror,mainActivity);
        }


        if (connection != null) {
            try {
                connection.open();
                /*
                *  ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);
                String pruebasasasss= new String(printer.retrieveFormatFromPrinter("! U1 do \"file.dir\" \"E:\""),StandardCharsets.UTF_8);
                System.out.println("oooole1");
                System.out.println(pruebasasasss);
                String pruebasasa= new String(printer.retrieveFormatFromPrinter("^XA^HFE:ENS.ZPL^XZ"),StandardCharsets.UTF_8);
                System.out.println("oooole2");
                System.out.println(pruebasasa);
                * */

                if(Memoria){
                    ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);
                    String formatContents = new String(printer.retrieveFormatFromPrinter(Etiqueta), StandardCharsets.UTF_8);

                    FieldDescriptionData[] variables;
                    variables = printer.getVariableFields(formatContents);

                    Collections.addAll(variablesList, variables);

                    vars = new HashMap<Integer, String>();
                    for (int i = 0; i < ListaMemoria.size(); i++) {
                        FieldDescriptionData var = variablesList.get(i);
                        vars.put(var.fieldNumber, ListaMemoria.get(i));
                    }
                }

                String quantityString ="1";

                int quantity;
                try {
                    quantity = Integer.parseInt(quantityString);
                    if (quantity < 1) {
                        quantity = 1;
                    }
                } catch (NumberFormatException e) {
                    quantity = 1;
                }
                ZebraPrinter printer=null;

                try {
                    printer = ZebraPrinterFactory.getInstance(connection);
                }catch (Exception e){
                    ToastHelper.message("usb 1:"+e.getMessage(), R.layout.item_customtoasterror,mainActivity);
                }

                if(printer!=null&&Memoria){
                    printer.printStoredFormat(Etiqueta, vars);
                }else{
                    if(printer!=null){
                        printer.sendCommand(Etiqueta);
                    }else{
                        ToastHelper.message("usb 4: printer null", R.layout.item_customtoasterror,mainActivity);
                    }

                }
                    //  printer.printStoredFormat("", vars);



            } catch (ConnectionException | ZebraPrinterLanguageUnknownException e) {
                ToastHelper.message("usb 2:"+e.getMessage(), R.layout.item_customtoasterror,mainActivity);
            } finally {
                try {
                    connection.close();
                } catch (ConnectionException e) {
                    ToastHelper.message("usb 3:"+e.getMessage(), R.layout.item_customtoasterror,mainActivity);
                }
            }
        }else{
            ToastHelper.message("usb 5: connection null", R.layout.item_customtoasterror,mainActivity);
        }

    }


}