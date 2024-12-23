package com.jws.jwsapi.core.storage;

import static com.jws.jwsapi.core.storage.StoragePaths.USB_CONNECTED;
import static com.jws.jwsapi.core.storage.StoragePaths.USB_NOT_AVAIBLE;

import android.content.Context;
import android.os.Handler;

import com.jws.jwsapi.MainActivity;
import com.jws.jwsapi.utils.PackageUtils;

import java.io.File;

import javax.inject.Singleton;

@Singleton
public class StorageService {
    private static final Handler mHandler = new Handler();
    private final Context context;
    private MainActivity appCompatActivity;
    private int state = USB_NOT_AVAIBLE;
    private final Runnable startUsbRead = new Runnable() {
        @Override
        public void run() {
            verifyMemoryConnected();
            mHandler.postDelayed(this, 1000);
        }
    };

    public StorageService(Context context) {
        this.context = context;
    }

    public void initService(MainActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
        startUsbRead.run();
    }

    private void verifyMemoryConnected() {
        if (StoragePaths.DIRECTORY_MEMORY_PATHS.stream().anyMatch(File::isDirectory)) {
            for (File apk : StoragePaths.FILE_APKS) {
                if (apk.exists()) {
                    PackageUtils.installApk(context, appCompatActivity);
                }
            }
            if (StoragePaths.DIRECTORY_MEMORY_PATHS.stream().anyMatch(File::isDirectory) && state == USB_NOT_AVAIBLE) {
                StorageDialogHandler storageDialogHandler = new StorageDialogHandler(appCompatActivity);
                storageDialogHandler.showDialog();
                state = USB_CONNECTED;
            }
            if (StoragePaths.DIRECTORY_MEMORY_PATHS.stream().noneMatch(File::isDirectory) && state == USB_CONNECTED) {
                state = USB_NOT_AVAIBLE;
            }
        } else {
            state = USB_NOT_AVAIBLE;
        }
    }

    public Boolean getState() {
        return state == 1;
    }

}
