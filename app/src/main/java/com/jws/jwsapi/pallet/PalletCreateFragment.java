package com.jws.jwsapi.pallet;

import static com.jws.jwsapi.dialog.DialogUtil.keyboardInt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jws.jwsapi.MainActivity;
import com.jws.jwsapi.R;
import com.jws.jwsapi.databinding.FragmentPalletCreateBinding;
import com.jws.jwsapi.shared.WeighRepository;
import com.jws.jwsapi.utils.ToastHelper;
import com.service.Comunicacion.ButtonProvider;
import com.service.Comunicacion.ButtonProviderSingleton;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PalletCreateFragment extends Fragment {

    MainActivity mainActivity;
    @Inject
    WeighRepository weighRepository;
    private PalletViewModel palletViewModel;
    private FragmentPalletCreateBinding binding;
    private ButtonProvider buttonProvider;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        buttonProvider = ButtonProviderSingleton.getInstance().getButtonProvider();
        binding = FragmentPalletCreateBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        palletViewModel = new ViewModelProvider(this).get(PalletViewModel.class);
        setupButtons();
        setOnClickListeners();

        palletViewModel.getPalletResponse().observe(getViewLifecycleOwner(), palletResponse -> {
            if (palletResponse != null) {
                if (palletResponse.getMessage() != null && !palletResponse.getMessage().isEmpty()) {
                    ToastHelper.message(palletResponse.getMessage(), R.layout.item_customtoast, getContext());
                }
            }
        });
        palletViewModel.getLoading().observe(getViewLifecycleOwner(), isLoading -> binding.loadingPanel.setVisibility(isLoading ? View.VISIBLE : View.GONE));

        palletViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                ToastHelper.message(error, R.layout.item_customtoasterror, getContext());
            }
        });
        palletViewModel.setScale(weighRepository.getScaleNumber());
        binding.tvScale.setText(String.valueOf(weighRepository.getScaleNumber()));
    }

    private void setOnClickListeners() {
        binding.tvPalletOrigin.setOnClickListener(v -> keyboardInt(binding.tvPalletOrigin, requireContext().getString(R.string.dialog_palleto_input), getContext(), texto -> palletViewModel.setPalletOrigin(texto)));
        binding.tvPalletDestination.setOnClickListener(v -> keyboardInt(binding.tvPalletDestination, requireContext().getString(R.string.dialog_palletd_input), getContext(), texto -> palletViewModel.setPalletDestination(texto)));
        binding.btNewPaller.setOnClickListener(v -> palletViewModel.createPallet());
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
            buttonProvider.getTitle().setText(requireContext().getString(R.string.title_new_pallet));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}