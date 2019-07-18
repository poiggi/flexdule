package com.flexdule.core.dtos;

import java.io.Serializable;
import java.util.Objects;

public class Schedule implements Serializable {

    private Integer idSchedule;
    private String name;
    private String color;

    public Schedule(){}

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "idSchedule=" + idSchedule +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return Objects.equals(idSchedule, schedule.idSchedule) &&
                Objects.equals(name, schedule.name) &&
                Objects.equals(color, schedule.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSchedule, name, color);
    }
}
