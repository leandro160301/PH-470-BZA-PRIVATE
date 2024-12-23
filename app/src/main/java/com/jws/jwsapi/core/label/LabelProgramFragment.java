package com.jws.jwsapi.core.label;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jws.jwsapi.MainActivity;
import com.jws.jwsapi.R;
import com.jws.jwsapi.core.printer.PrinterPreferences;
import com.jws.jwsapi.utils.file.FileExtensionUtils;
import com.service.Comunicacion.ButtonProvider;
import com.service.Comunicacion.ButtonProviderSingleton;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LabelProgramFragment extends Fragment implements LabelProgramAdapter.ItemClickListener {

    public RecyclerView rc_lista_ingredientes;
    @Inject
    PrinterPreferences printerPreferences;
    @Inject
    LabelManager labelManager;
    MainActivity mainActivity;
    LabelProgramAdapter adapter;
    List<LabelProgramModel> lista_ingredientes;
    int posicion_recycler = -1;
    private ButtonProvider buttonProvider;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.standar_etiquetasdeprograma, container, false);
        buttonProvider = ButtonProviderSingleton.getInstance().getButtonProvider();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        rc_lista_ingredientes = view.findViewById(R.id.lista_ingredientes);
        List<String> nombreetiquetas = labelManager.nameLabelList;
        lista_ingredientes = new ArrayList<>();
        for (int i = 0; i < nombreetiquetas.size(); i++) {
            lista_ingredientes.add(new LabelProgramModel(nombreetiquetas.get(i), printerPreferences.getLabel(i)));
        }
        configuracionBotones();
        cargarRecyclerView();

    }


    private void cargarRecyclerView() {
        rc_lista_ingredientes.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LabelProgramAdapter(getContext(), lista_ingredientes, FileExtensionUtils.getFilesExtension(".prn"), printerPreferences);
        adapter.setClickListener(this);
        rc_lista_ingredientes.setAdapter(adapter);

    }


    private void configuracionBotones() {
        if (buttonProvider != null) {
            buttonProvider.getTitle().setText(R.string.title_label_program_fragment);
            buttonProvider.getButton1().setVisibility(View.INVISIBLE);
            buttonProvider.getButton2().setVisibility(View.INVISIBLE);
            buttonProvider.getButton1().setBackgroundResource(R.drawable.boton_buscar_i);
            buttonProvider.getButton2().setBackgroundResource(R.drawable.boton_add_i);
            buttonProvider.getButton3().setVisibility(View.INVISIBLE);
            buttonProvider.getButton4().setVisibility(View.INVISIBLE);
            buttonProvider.getButton5().setVisibility(View.INVISIBLE);
            buttonProvider.getButton6().setVisibility(View.INVISIBLE);
            buttonProvider.getButtonHome().setOnClickListener(view -> mainActivity.mainClass.openFragmentPrincipal());

        }
    }


    @Override
    public void onItemClick(View view, int position) {
        posicion_recycler = position;
    }

}


