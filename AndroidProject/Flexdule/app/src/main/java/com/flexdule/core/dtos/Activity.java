package com.flexdule.core.dtos;

import java.io.Serializable;

public class Activity implements Serializable {


    private Integer idActivity;
    private Integer idSchedule;
    private String name;
    private Integer positionInSchedule;
    private String color;
    private ActivityVars configVars;
    private ActivityVars finalVars;



    public Activity() {
        configVars = new ActivityVars();
        finalVars = new ActivityVars();
    }

    public Integer getIdActivity() {
        return idActivity;
    }

    public void setIdActivity(Integer idActivity) {
        this.idActivity = idActivity;
    }

    public Integer getIdSchedule() {
        return idSchedule;
    }

    public void setIdSchedule(Integer idSchedule) {
        this.idSchedule = idSchedule;
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
        if(configVars != null) {
            this.configVars = configVars;
        }else{
            this.configVars.setS(null);
            this.configVars.setD(null);
            this.configVars.setF(null);
        }
    }

    public ActivityVars getFinalVars() {
        return finalVars;
    }

    public void setFinalVars(ActivityVars finalVars) {
        if(finalVars != null) {
            this.finalVars = finalVars;
        }else{
            this.finalVars.setS(null);
            this.finalVars.setD(null);
            this.finalVars.setF(null);
        }
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getPositionInSchedule() {
        return positionInSchedule;
    }

    public void setPositionInSchedule(Integer positionInSchedule) {
        this.positionInSchedule = positionInSchedule;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "idActivity=" + idActivity +
                ", idSchedule=" + idSchedule +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", positionInSchedule=" + positionInSchedule +
                ", configVars=" + configVars +
                ", finalVars=" + finalVars +
                '}';
    }
}
