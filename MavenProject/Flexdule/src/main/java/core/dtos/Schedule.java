package core.dtos;

import java.io.Serializable;

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
}
