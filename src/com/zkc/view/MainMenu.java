package com.zkc.view;

/**
 * Created by IT on 2015-04-27.
 */
public class MainMenu {
    private int titleResourceId;
    private int imageResourceId;
    private int bgColorResourceId;

    public MainMenu(int imageResourceId, int titleResourceId, int bgColorResourceId) {
        this.imageResourceId = imageResourceId;
        this.titleResourceId = titleResourceId;
        this.bgColorResourceId = bgColorResourceId;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public int getTitleResourceId() {
        return titleResourceId;
    }

    public int getBgColorResourceId() {
        return bgColorResourceId;
    }
}
