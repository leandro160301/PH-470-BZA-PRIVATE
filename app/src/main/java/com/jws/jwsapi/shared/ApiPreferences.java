package com.jws.jwsapi.shared;

import com.jws.jwsapi.core.data.local.PreferencesHelper;

import javax.inject.Inject;

public class ApiPreferences {

    private static final String PREF_SCALE = "api_scale";
    private static final String DEFAULT_SCALE = "c16c9ac1deca7c4db51e8c73800d4ced";

    private final PreferencesHelper preferencesHelper;

    @Inject
    public ApiPreferences(PreferencesHelper preferencesHelper) {
        this.preferencesHelper = preferencesHelper;
    }

    public String getScaleCode() {
        return preferencesHelper.getString(PREF_SCALE, DEFAULT_SCALE);
    }

    public void setScaleCode(String scaleCode) {
        preferencesHelper.putString(PREF_SCALE, scaleCode);
    }

}