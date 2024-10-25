package com.jws.jwsapi;

import static com.jws.jwsapi.core.user.UserConstants.ROLE_ADMINISTRATOR;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.jws.jwsapi.core.container.ContainerCoreFragment;
import com.jws.jwsapi.core.container.ContainerFragment;
import com.jws.jwsapi.core.lock.LockFragment;
import com.jws.jwsapi.core.lock.LockManager;
import com.jws.jwsapi.home.HomeFragment;
import com.jws.jwsapi.shared.UserRepository;
import com.service.Balanzas.BalanzaService;
import com.service.Comunicacion.OnFragmentChangeListener;

public class MainClass implements OnFragmentChangeListener {

    public static String DB_NAME = "bza-database";
    private final Context context;
    private final MainActivity mainActivity;
    private final UserRepository userRepository;
    private final LockManager lockManager;
    public BalanzaService service;
    public BalanzaService.Balanzas bza;
    Boolean clickEnable = true;

    public MainClass(Context context, MainActivity activity, UserRepository userRepository, LockManager lockManager) {
        this.context = context;
        this.mainActivity = activity;
        this.userRepository = userRepository;
        this.lockManager = lockManager;
    }

    public void init() {
        service = new BalanzaService(mainActivity, this);
        service.init();
        Runnable myRunnable = () -> {
            bza = BalanzaService.Balanzas;
            mainActivity.runOnUiThread(this::openFragmentPrincipal);
        };
        Thread myThread = new Thread(myRunnable);
        myThread.start();
    }

    @Override
    public void openFragmentPrincipal() {
        lockManager.updateDate();
        if (!lockManager.isLocked()) {
            Fragment fragment = new HomeFragment();
            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            ContainerCoreFragment containerFragment = ContainerCoreFragment.newInstance(fragment.getClass());
            fragmentManager.beginTransaction()
                    .replace(R.id.container_fragment, containerFragment)
                    .commit();
        } else {
            openFragment(new LockFragment());
        }

    }

    public void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        ContainerFragment containerFragment = ContainerFragment.newInstance(fragment.getClass());
        fragmentManager.beginTransaction()
                .replace(R.id.container_fragment, containerFragment)
                .commit();
    }

    @Override
    public void openFragmentService(Fragment fragment, Bundle arg) {
        if (clickEnable) {
            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            boolean programador = userRepository.getLevelUser() > ROLE_ADMINISTRATOR;
            ContainerFragment containerFragment = ContainerFragment.newInstanceService(fragment.getClass(), arg, programador);
            fragmentManager.beginTransaction()
                    .replace(R.id.container_fragment, containerFragment)
                    .commit();
            clickEnable = false;
            Handler handler = new Handler();
            handler.postDelayed(() -> clickEnable = true, 1000); //arreglar problema de que mas de una optima llame a service al mismo tiempo
        }

    }


}
