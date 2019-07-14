package com.flexdule.model;

import java.io.Serializable;

public class Activity implements Serializable {

    private String name;
    private ActivityVars configVars;
    private ActivityVars finalVars;
    private Integer positionInSchedule;
    private int color;

    public Activity() {
        configVars = new ActivityVars();
        finalVars = new ActivityVars();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ActivityVars getConfigVars() {
        return configVars;
    }

    public void setConfigVars(ActivityVars configVars) {
        this.configVars = configVars;
    }

    public ActivityVars getFinalVars() {
        return finalVars;
    }

    public void setFinalVars(ActivityVars finalVars) {
        this.finalVars = finalVars;
    }

    public Integer getPositionInSchedule() {
        return positionInSchedule;
    }

    public void setPositionInSchedule(Integer positionInSchedule) {
        this.positionInSchedule = positionInSchedule;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "name='" + name + '\'' +
                ", configVars=" + configVars +
                ", finalVars=" + finalVars +
                ", positionInSchedule=" + positionInSchedule +
                ", color='" + color + '\'' +
                '}';
    }
}
