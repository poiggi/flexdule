package com.flexdule.android.model.sqlite.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

@Entity(tableName = "Cookie")
public class CookieBean implements Serializable {

    @PrimaryKey
    private Integer idCookie;
    private String label;
    private String name;
    private String value;


    public CookieBean() {
    }

    public Integer getIdCookie() {
        return idCookie;
    }

    public void setIdCookie(Integer idCookie) {
        this.idCookie = idCookie;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CookieBean{" +
                "idCookie=" + idCookie +
                ", label='" + label + '\'' +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CookieBean that = (CookieBean) o;
        return Objects.equals(idCookie, that.idCookie) &&
                Objects.equals(label, that.label) &&
                Objects.equals(name, that.name) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCookie, label, name, value);
    }
}
