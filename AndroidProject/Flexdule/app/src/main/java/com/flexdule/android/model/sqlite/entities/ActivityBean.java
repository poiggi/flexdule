package com.flexdule.android.model.sqlite.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "Activity",
        foreignKeys = {@ForeignKey(
                onDelete = CASCADE,
                entity = ScheduleBean.class,
                parentColumns = "idSchedule", childColumns = "idSchedule")},
        indices = {@Index("idSchedule"),
        }
)
public class ActivityBean implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private Integer idActivity;

    private Integer idSchedule;

    private String name;
    private String color;

    private String sn, sx;
    private String dn, dx;
    private String fn, fx;

    private Integer positionInSchedule;

    public ActivityBean() {
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getSx() {
        return sx;
    }

    public void setSx(String sx) {
        this.sx = sx;
    }

    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public String getDx() {
        return dx;
    }

    public void setDx(String dx) {
        this.dx = dx;
    }

    public String getFn() {
        return fn;
    }

    public void setFn(String fn) {
        this.fn = fn;
    }

    public String getFx() {
        return fx;
    }

    public void setFx(String fx) {
        this.fx = fx;
    }

    public Integer getPositionInSchedule() {
        return positionInSchedule;
    }

    public void setPositionInSchedule(Integer positionInSchedule) {
        this.positionInSchedule = positionInSchedule;
    }

    @Override
    public String toString() {
        return "ActivityBean{" +
                "idActivity=" + idActivity +
                ", idSchedule=" + idSchedule +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                ", sn='" + sn + '\'' +
                ", sx='" + sx + '\'' +
                ", dn='" + dn + '\'' +
                ", dx='" + dx + '\'' +
                ", fn='" + fn + '\'' +
                ", fx='" + fx + '\'' +
                ", positionInSchedule=" + positionInSchedule +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivityBean that = (ActivityBean) o;
        return Objects.equals(idActivity, that.idActivity) &&
                Objects.equals(idSchedule, that.idSchedule) &&
                Objects.equals(name, that.name) &&
                Objects.equals(color, that.color) &&
                Objects.equals(sn, that.sn) &&
                Objects.equals(sx, that.sx) &&
                Objects.equals(dn, that.dn) &&
                Objects.equals(dx, that.dx) &&
                Objects.equals(fn, that.fn) &&
                Objects.equals(fx, that.fx) &&
                Objects.equals(positionInSchedule, that.positionInSchedule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idActivity, idSchedule, name, color, sn, sx, dn, dx, fn, fx, positionInSchedule);
    }
}
