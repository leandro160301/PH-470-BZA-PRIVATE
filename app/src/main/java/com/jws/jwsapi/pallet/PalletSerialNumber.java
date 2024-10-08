package com.jws.jwsapi.pallet;

import android.annotation.SuppressLint;

public class PalletSerialNumber {
    @SuppressLint("DefaultLocale")
    public static String incrementSerialNumber(String serialNumber) {
        String[] partes = serialNumber.split("-");
        if (partes.length == 2) {
            String prefijo = partes[0];
            String numero = partes[1];

            long numeroIncrementado = Long.parseLong(numero) + 1;

            return String.format("%s-%d", prefijo, numeroIncrementado);
        } else {
            return serialNumber;
        }

    }
}
