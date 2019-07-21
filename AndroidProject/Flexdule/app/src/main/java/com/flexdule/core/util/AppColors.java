package com.flexdule.core.util;

import java.util.ArrayList;
import java.util.List;

public class AppColors {

    public static final String COLOR_WHITE = "ffffff";
    public static final String COLOR_RED = "f28a81";
    public static final String COLOR_ORANGE = "fbbc04";
    public static final String COLOR_YELLOW = "fff474";
    public static final String COLOR_GREEN = "cdff90";
    public static final String COLOR_CYAN = "a7ffec";
    public static final String COLOR_LIGHT_BLUE = "cbf0f8";
    public static final String COLOR_BLUE = "aecbfa";
    public static final String COLOR_VIOLET = "d7aefb";
    public static final String COLOR_PINK = "fdcfe8";
    public static final String COLOR_BROWN = "e6c9a8";
    public static final String COLOR_GREY = "e8eaed";

    List<String> colors = getColors();
    int index = 0;

    public String getNextColor(){
        index++;
        if(index>=colors.size()) index = 0;
        String color = colors.get(index);
        return color;
    }

    public static List<String> getColors() {
        List<String> c = new ArrayList<>();
        c.add(COLOR_WHITE); //0
        c.add(COLOR_RED); //1
        c.add(COLOR_ORANGE); //2
        c.add(COLOR_YELLOW); //3
        c.add(COLOR_GREEN); //4
        c.add(COLOR_CYAN); //5
        c.add(COLOR_LIGHT_BLUE); //6
        c.add(COLOR_BLUE); //7
        c.add(COLOR_VIOLET); //8
        c.add(COLOR_PINK); //9
        c.add(COLOR_BROWN); //10
        c.add(COLOR_GREY); //11
        return c;
    }

    public static String getRandomColor() {
        List<String> colors = getColors();
        int i = CU.getRandomNumberInRange(0, colors.size()-1);
        return getColors().get(i);
    }
}
