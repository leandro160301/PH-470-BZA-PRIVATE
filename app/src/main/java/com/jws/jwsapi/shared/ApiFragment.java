package com.jws.jwsapi.shared;

import static com.jws.jwsapi.dialog.DialogUtil.keyboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jws.jwsapi.MainActivity;
import com.jws.jwsapi.R;
import com.jws.jwsapi.databinding.FragmentApiBinding;
import com.service.Comunicacion.ButtonProvider;
import com.service.Comunicacion.ButtonProviderSingleton;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ApiFragment extends Fragment {
    @Inject
    ApiPreferences apiPreferences;
    MainActivity mainActivity;
    private FragmentApiBinding binding;
    private ButtonProvider buttonProvider;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        buttonProvider = ButtonProviderSingleton.getInstance().getButtonProvider();
        binding = FragmentApiBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        setupButtons();

        binding.tvApiScale.setText(String.valueOf(apiPreferences.getScaleCode()));

        binding.tvApiUrl.setText(apiPreferences.getUrl());

        setOnClickListeners();

    }

    private void setOnClickListeners() {
        binding.tvApiScale.setOnClickListener(v ->
                keyboard(binding.tvApiScale, getString(R.string.dialog_api_scale_code), getContext(), code -> apiPreferences.setScaleCode(code)));
        binding.tvApiUrl.setOnClickListener(v ->
                keyboard(binding.tvApiUrl, "Ingrese la url del servidor", getContext(), code -> apiPreferences.setUrl(code)));
    }

    private void setupButtons() {
        if (buttonProvider != null) {
            buttonProvider.getButtonHome().setOnClickListener(view -> mainActivity.mainClass.openFragmentPrincipal());
            buttonProvider.getButton1().setVisibility(View.INVISIBLE);
            buttonProvider.getButton2().setVisibility(View.INVISIBLE);
            buttonProvider.getButton3().setVisibility(View.INVISIBLE);
            buttonProvider.getButton4().setVisibility(View.INVISIBLE);
            buttonProvider.getButton5().setVisibility(View.INVISIBLE);
            buttonProvider.getButton6().setVisibility(View.INVISIBLE);
            buttonProvider.getTitle().setText(R.string.fragment_programa_title);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}