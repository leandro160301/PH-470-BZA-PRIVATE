package com.jws.jwsapi.pallet;

import android.annotation.SuppressLint;

public class PalletSerialNumber {
    @SuppressLint("DefaultLocale")
    public static String incrementSerialNumber(String serialNumber) {
        String[] parts = serialNumber.split("-");
        if (parts.length == 2) {
            String pre = parts[0];
            String number = parts[1];

            int originalLenght = number.length();
            long incrementNumber = Long.parseLong(number) + 1;
            String formattedNumber = String.format("%0" + originalLenght + "d", incrementNumber);
            return String.format("%s-%s", pre, formattedNumber);
        } else {
            return serialNumber;
        }
    }
}
