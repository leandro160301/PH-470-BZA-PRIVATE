package com.jws.jwsapi.core.container.button;

import android.view.View;

public class ButtonConfig {
    private final Integer titleResId;
    private final Integer[] buttonBackgroundResIds;
    private final Boolean[] buttonVisibilities;
    private final View.OnClickListener[] buttonClickListeners;
    private final View.OnClickListener homeClickListener;

    public ButtonConfig(Integer titleResId,
                        Integer[] buttonBackgroundResIds,
                        Boolean[] buttonVisibilities,
                        View.OnClickListener[] buttonClickListeners,
                        View.OnClickListener homeClickListener) {
        this.titleResId = titleResId;
        this.buttonBackgroundResIds = buttonBackgroundResIds;
        this.buttonVisibilities = buttonVisibilities;
        this.buttonClickListeners = buttonClickListeners;
        this.homeClickListener = homeClickListener;
    }

    public Integer getTitleResId() {
        return titleResId;
    }

    public Integer[] getButtonBackgroundResIds() {
        return buttonBackgroundResIds;
    }

    public Boolean[] getButtonVisibilities() {
        return buttonVisibilities;
    }

    public View.OnClickListener[] getButtonClickListeners() {
        return buttonClickListeners;
    }

    public View.OnClickListener getHomeClickListener() {
        return homeClickListener;
    }
}