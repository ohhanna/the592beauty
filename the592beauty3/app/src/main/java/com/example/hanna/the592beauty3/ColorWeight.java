package com.example.hanna.the592beauty3;

import android.app.Application;

/**
 * Created by nightprimula on 2017-04-11.
 */

public class ColorWeight extends Application {

    private int cool;
    private int warm;
    private int weight;

    private int which;

    @Override
    public void onCreate() {
        //전역 변수 초기화
        cool = 0;
        warm = 0;
        weight = 0;
        which = 0;

        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void setColor(int cool, int warm) {
        this.cool = cool;
        this.warm = warm;
    }

    public void setCool(int cool) {
        this.cool = cool;
    }

    public void setWarm(int warm) {
        this.warm = warm;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setWhich(int which) {
        this.which = which;
    }

    public int getCool() {
        return cool;
    }

    public int getWarm() {
        return warm;
    }

    public int getWeight() {
        return weight;
    }

    public int getWhich() {
        return which;
    }
}