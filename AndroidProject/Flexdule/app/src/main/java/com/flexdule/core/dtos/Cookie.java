package com.flexdule.core.dtos;

import java.io.Serializable;
import java.util.Objects;

public class Cookie implements Serializable {

    private Integer idCookie;
    private String label;
    private String name;
    private String value;


    public Cookie() {
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
        return "Cookie{" +
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
        Cookie cookie = (Cookie) o;
        return Objects.equals(idCookie, cookie.idCookie) &&
                Objects.equals(label, cookie.label) &&
                Objects.equals(name, cookie.name) &&
                Objects.equals(value, cookie.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCookie, label, name, value);
    }
}
