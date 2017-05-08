package me.leefeng.library.promptview;

import android.graphics.Color;

/**
 * Created by FengTing on 2017/5/8.
 */

public class Builder {
    int backColor = Color.BLACK;
    int backAlpha = 90;
    int textColor = Color.WHITE;
    float textSize = 14;
    float padding = 15;
    float round = 10;
    int roundColor = Color.BLACK;
    int roundAlpha = 120;
    boolean touchAble = false;
    boolean withAnim = true;
    long stayDuration = 1000;

    public Builder backColor(int backColor) {
        this.backColor = backColor;
        return this;
    }

    public Builder backAlpha(int backAlpha) {
        this.backAlpha = backAlpha;
        return this;
    }

    public Builder textColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    public Builder textSize(float textSize) {
        this.textSize = textSize;
        return this;
    }

    public Builder padding(float padding) {
        this.padding = padding;
        return this;
    }

    public Builder round(float round) {
        this.round = round;
        return this;
    }

    public Builder roundColor(int roundColor) {
        this.roundColor = roundColor;
        return this;
    }

    public Builder roundAlpha(int roundAlpha) {
        this.roundAlpha = roundAlpha;
        return this;
    }

    public Builder touchAble(boolean touchAble) {
        this.touchAble = touchAble;
        return this;
    }

    public Builder withAnim(boolean withAnim) {
        this.withAnim = withAnim;
        return this;
    }

    public Builder stayDuration(long time) {
        this.stayDuration = time;
        return this;
    }

    public Builder() {
    }
}
