package com.example.hanna.the592beauty3;

import android.app.Application;

/**
 * Created by nightprimula on 2017-04-11.
 */

public class ColorWeight extends Application {

    private int cool;
    private int warm;

    private int spring;
    private int summer;
    private int fall;
    private int winter;

    private int weight;
    private int tempc;
    private int tempw;
    private int tw;
    private int which;
    private int back;
    private String username;
    private String id;
    private String tone;

    @Override
    public void onCreate() {
        //전역 변수 초기화
        cool = 0;
        warm = 0;

        spring = 0;
        summer = 0;
        fall = 0;
        winter = 0;

        weight = 0;
        which = 0;
        back = 0;
        tempc = 0;
        tempw = 0;

        tw=0;
        username = null;
        id = null;
        tone = null;
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

    public void setTemp(int tempc, int tempw){
        this.tempc = tempc;
        this.tempw = tempw;
    }

    public void setTw(int tw){
        this.tw=tw;
    }

    public void initAll(){
        cool = 0;
        warm = 0;

        spring = 0;
        summer = 0;
        fall = 0;
        winter = 0;

        weight = 0;
        which = 0;
        back = 0;
        tempc = 0;
        tempw = 0;
        tw=0;
        username = null;
        id = null;
        tone = null;
    }

    public void init() {
        this.cool = 0;
        this.warm = 0;

        this.spring = 0;
        this. summer=0;
        this.fall = 0;
        this.winter = 0;

        this.weight = 0;
        this.which = 0;
        this.back = 0;
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
    public void setBack(int back) {this.back = back; }

    public void setUsername(String name) {this.username = name; }
    public void setId(String id) {this.id = id; }
    public void setTone(String tone) {this.tone = tone;}

    public int getCool() {
        return cool;
    }
    public int getWarm() {
        return warm;
    }
    public int getSpring() { return spring;}
    public int getTempc() { return tempc; }
    public int getTempw() { return tempw; }
    public int getTw() {return tw;}
    public int getWeight() {
        return weight;
    }

    public int getWhich() {
        return which;
    }
    public int getBack() { return back; }

    public String getUsername() { return username;}
    public String getId() {return id;}
    public String getTone() { return tone;}

}