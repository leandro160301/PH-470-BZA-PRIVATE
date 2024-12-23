package com.jws.jwsapi.core.services.httpserver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.jws.jwsapi.MainActivity;
import com.jws.jwsapi.core.data.local.PreferencesManager;
import com.jws.jwsapi.core.user.UserManager;
import com.jws.jwsapi.pallet.PalletService;
import com.jws.jwsapi.weighing.WeighingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AppService extends Service {
    private static final String TAG = AppService.class.getSimpleName();
    private static final int SERVICE_ID = 101;
    private static final String NOTIFICATION_CHANNEL_ID = "WebScreenServiceChannel";
    private static final String NOTIFICATION_CHANNEL_NAME = "WebScreen notification channel";
    private static final String NOTIFICATION_TITLE = "WebScreen is running";
    private static final String NOTIFICATION_CONTENT = "Tap to stop";
    private static final String MOUSE_PARAM_X = "x";
    private static final String MOUSE_PARAM_Y = "y";
    private static boolean isRunning = false;
    private final IBinder iBinder = new AppServiceBinder();
    public MainActivity mainActivity;
    private WebRtcManager webRtcManager = null;
    private HttpServer httpServer = null;
    private boolean isWebServerRunning = false;
    private MouseAccessibilityService mouseAccessibilityService = null;
    private final HttpServer.HttpServerInterface httpServerInterface = new
            HttpServer.HttpServerInterface() {
                @Override
                public void onMouseDown(JSONObject message) {
                    int[] coordinates = getCoordinates(message);
                    if (coordinates != null && mouseAccessibilityService != null)
                        mouseAccessibilityService.mouseDown(coordinates[0], coordinates[1]);
                }

                @Override
                public void onMouseMove(JSONObject message) {
                    int[] coordinates = getCoordinates(message);
                    if (coordinates != null && mouseAccessibilityService != null)
                        mouseAccessibilityService.mouseMove(coordinates[0], coordinates[1]);
                }

                @Override
                public void onMouseUp(JSONObject message) {
                    int[] coordinates = getCoordinates(message);
                    if (coordinates != null && mouseAccessibilityService != null)
                        mouseAccessibilityService.mouseUp(coordinates[0], coordinates[1]);
                }

                @Override
                public void onMouseZoomIn(JSONObject message) {
                    int[] coordinates = getCoordinates(message);
                    if (coordinates != null && mouseAccessibilityService != null)
                        mouseAccessibilityService.mouseWheelZoomIn(coordinates[0], coordinates[1]);
                }

                @Override
                public void onMouseZoomOut(JSONObject message) {
                    int[] coordinates = getCoordinates(message);
                    if (coordinates != null && mouseAccessibilityService != null)
                        mouseAccessibilityService.mouseWheelZoomOut(coordinates[0], coordinates[1]);
                }

                @Override
                public void onButtonBack() {
                    if (mouseAccessibilityService != null)
                        mouseAccessibilityService.backButtonClick();
                }

                @Override
                public void onButtonHome() {
                    if (mouseAccessibilityService != null)
                        mouseAccessibilityService.homeButtonClick();
                }

                @Override
                public void onButtonRecent() {
                    if (mouseAccessibilityService != null)
                        mouseAccessibilityService.recentButtonClick();
                }

                @Override
                public void onButtonPower() {
                    if (mouseAccessibilityService != null)
                        mouseAccessibilityService.powerButtonClick();
                }

                @Override
                public void onButtonLock() {
                    if (mouseAccessibilityService != null)
                        mouseAccessibilityService.lockButtonClick();
                }

                @Override
                public void onJoin(HttpServer server) {
                    if (webRtcManager == null)
                        return;
                    webRtcManager.start(server);
                }

                @Override
                public void onSdp(JSONObject message) {
                    if (webRtcManager == null)
                        return;
                    webRtcManager.onAnswerReceived(message);
                }

                @Override
                public void onIceCandidate(JSONObject message) {
                    if (webRtcManager == null)
                        return;
                    webRtcManager.onIceCandidateReceived(message);
                }

                @Override
                public void onBye() {
                    if (webRtcManager == null)
                        return;
                    webRtcManager.stop();
                }

                @Override
                public void onWebSocketClose() {
                    if (webRtcManager == null)
                        return;
                    webRtcManager.stop();
                }
            };

    public static boolean isServiceRunning() {
        return isRunning;
    }

    @Override
    public void onCreate() {
        isRunning = true;
        Log.d(TAG, "service created");
    }

    @Override
    public void onDestroy() {
        serverStop();
        isRunning = false;
        Log.d(TAG, "service destroyed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        String channelId = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                createNotificationChannel() : "";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,
                channelId);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle(NOTIFICATION_TITLE)
                .setContentText(NOTIFICATION_CONTENT)
                //   .setSmallIcon(R.drawable.ic_stat_name)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(SERVICE_ID, notification);

        Log.d(TAG, "service started");
        return START_STICKY;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
        return NOTIFICATION_CHANNEL_ID;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    public boolean serverStart(Intent intent, int port,
                               boolean isAccessibilityServiceEnabled, Context context, MainActivity mainActivity, UserManager usersManager, PreferencesManager preferencesManagerBase, WeighingService weighingService, PalletService palletService) {
        this.mainActivity = mainActivity;
        if (!(isWebServerRunning = startHttpServer(port, usersManager, preferencesManagerBase, weighingService, palletService)))
            return false;

        webRtcManager = new WebRtcManager(intent, context, httpServer, mainActivity, preferencesManagerBase);

        accessibilityServiceSet(context, isAccessibilityServiceEnabled);

        return isWebServerRunning;
    }

    public void serverStop() {
        if (!isWebServerRunning)
            return;
        isWebServerRunning = false;

        accessibilityServiceSet(null, false);

        stopHttpServer();
        webRtcManager.close();
        webRtcManager = null;
    }

    public boolean isServerRunning() {
        return isWebServerRunning;
    }


    public boolean startHttpServer(int httpServerPort, UserManager usersManager, PreferencesManager preferencesManagerBase, WeighingService weighingService, PalletService palletService) {
        httpServer = new HttpServer(httpServerPort, getApplicationContext(), httpServerInterface, mainActivity, usersManager, preferencesManagerBase, weighingService, palletService);
        try {
            httpServer.start();
        } catch (IOException e) {
            // String fmt = getResources().getString(R.string.port_in_use);
            //   String errorMessage = String.format(Locale.getDefault(), fmt, httpServerPort);
            Toast.makeText(getApplicationContext(), "errorMessage", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void stopHttpServer() {
        if (httpServer == null)
            return;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Run stop in thread to avoid NetworkOnMainThreadException
                    httpServer.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        httpServer = null;
    }

    private int[] getCoordinates(JSONObject json) {
        int[] coordinates = new int[2];

        try {
            coordinates[0] = json.getInt(MOUSE_PARAM_X);
            coordinates[1] = json.getInt(MOUSE_PARAM_Y);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return coordinates;
    }

    public void accessibilityServiceSet(Context context, boolean isEnabled) {
        if (isEnabled) {
            if (mouseAccessibilityService != null)
                return;
            mouseAccessibilityService = new MouseAccessibilityService();
            mouseAccessibilityService.setContext(context);
        } else {
            mouseAccessibilityService = null;
        }
    }

    public boolean isMouseAccessibilityServiceAvailable() {
        return mouseAccessibilityService != null;
    }

    public class AppServiceBinder extends Binder {
        public AppService getService() {
            return AppService.this;
        }
    }
}
