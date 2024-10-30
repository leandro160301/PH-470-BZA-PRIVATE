package com.jws.jwsapi.core.container.button;

import android.view.View;
import android.widget.Button;

import com.service.Comunicacion.ButtonProvider;

public class ButtonConfigurator {
    public static void configureButtons(ButtonProvider buttonProvider, ButtonConfig config) {
        if (buttonProvider != null) {
            buttonProvider.getTitle().setText(config.getTitleResId());

            Integer[] backgroundResIds = config.getButtonBackgroundResIds();
            Boolean[] visibilities = config.getButtonVisibilities();
            View.OnClickListener[] clickListeners = config.getButtonClickListeners();

            Button[] buttons = {
                    buttonProvider.getButton1(),
                    buttonProvider.getButton2(),
                    buttonProvider.getButton3(),
                    buttonProvider.getButton4(),
                    buttonProvider.getButton5(),
                    buttonProvider.getButton6()
            };

            for (int i = 0; i < buttons.length; i++) {
                if (backgroundResIds[i] != null)
                    buttons[i].setBackgroundResource(backgroundResIds[i]);
                if (visibilities[i] != null)
                    buttons[i].setVisibility(visibilities[i] ? View.VISIBLE : View.INVISIBLE);
                if (clickListeners[i] != null) buttons[i].setOnClickListener(clickListeners[i]);
            }

            buttonProvider.getButtonHome().setOnClickListener(config.getHomeClickListener());
        }
    }
}