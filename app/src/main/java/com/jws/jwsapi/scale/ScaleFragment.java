package com.jws.jwsapi.scale;

import static com.jws.jwsapi.dialog.DialogUtil.keyboardFloat;
import static com.jws.jwsapi.dialog.DialogUtil.keyboardInt;
import static com.jws.jwsapi.utils.ToastHelper.message;
import static com.jws.jwsapi.utils.Utils.isNumeric;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jws.jwsapi.MainActivity;
import com.jws.jwsapi.R;
import com.jws.jwsapi.databinding.FragmentScaleBinding;
import com.jws.jwsapi.shared.WeighRepository;
import com.service.Comunicacion.ButtonProvider;
import com.service.Comunicacion.ButtonProviderSingleton;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ScaleFragment extends Fragment {
    @Inject
    ScalePreferences scalePreferences;
    @Inject
    WeighRepository weighRepository;
    MainActivity mainActivity;
    private FragmentScaleBinding binding;
    private ButtonProvider buttonProvider;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        buttonProvider = ButtonProviderSingleton.getInstance().getButtonProvider();
        binding = FragmentScaleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        setupButtons();

        binding.tvZeroBand.setText(String.valueOf(scalePreferences.getZeroBand()));

        binding.tvStableCount.setText(String.valueOf(scalePreferences.getStableCountThreshold()));

        weighRepository.getUnit().observe(getViewLifecycleOwner(), unit -> {
            if (unit != null) {
                binding.tvUnit.setText(unit);
            }
        });

        setOnClickListeners();

    }

    private void setOnClickListeners() {
        binding.tvZeroBand.setOnClickListener(v -> keyboardFloat(binding.tvZeroBand, getString(R.string.dialog_scale_fragment_zero_band), getContext(), value -> {
            if (isNumeric(value)) {
                scalePreferences.setZeroBand(Double.parseDouble(value));
            } else {
                message(getString(R.string.toast_message_wrong_value_scale), R.layout.item_customtoasterror, requireContext());
            }
        }));
        binding.tvStableCount.setOnClickListener(v -> keyboardInt(binding.tvStableCount, getString(R.string.dialog_scale_fragment_stable_count), getContext(), value -> {
            if (isNumeric(value)) {
                scalePreferences.setStableCountThreshold(Integer.parseInt(value));
            } else {
                message(getString(R.string.toast_message_wrong_value_scale), R.layout.item_customtoasterror, requireContext());
            }
        }));
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
            buttonProvider.getTitle().setText(R.string.fragment_scale_title);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}