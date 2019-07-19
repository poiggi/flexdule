package com.flexdule.core.util;

import java.util.ArrayList;
import java.util.List;

public class AppColors {

    List<String> colors;
    int index;

    public AppColors() {
        colors = getColors();
        index = 0;
    }

    public String getNextColor(){
        if(index>=colors.size()) index = 0;
        String color = colors.get(index);
        index++;
        return color;
    }

    public static List<String> getColors() {
        List<String> c = new ArrayList<>();
        c.add("ffffff"); //0
        c.add("f28a81"); //1
        c.add("fbbc04"); //2
        c.add("fff474"); //3
        c.add("cdff90"); //4
        c.add("a7ffec"); //5
        c.add("cbf0f8"); //6
        c.add("aecbfa"); //7
        c.add("d7aefb"); //8
        c.add("fdcfe8"); //9
        c.add("e6c9a8"); //10
        c.add("e8eaed"); //11
        return c;
    }

    public static String getRandomColor() {
        List<String> colors = getColors();
        int i = CU.getRandomNumberInRange(0, colors.size()-1);
        return getColors().get(i);
    }
}
