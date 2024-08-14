package com.jws.jwsapi.base.containers.clases;

import com.jws.jwsapi.base.containers.interfaces.ButtonProvider_Principal;

public class ButtonProviderSingletonPrincipal {
    private static ButtonProviderSingletonPrincipal instance;
    private ButtonProvider_Principal buttonProvider;

    private ButtonProviderSingletonPrincipal() {
        // Constructor privado para evitar instanciación directa
    }

    public static ButtonProviderSingletonPrincipal getInstance() {
        if (instance == null) {
            instance = new ButtonProviderSingletonPrincipal();
        }
        return instance;
    }

    public ButtonProvider_Principal getButtonProvider() {
        return buttonProvider;
    }

    public void setButtonProvider(ButtonProvider_Principal buttonProvider) {
        this.buttonProvider = buttonProvider;
    }
}