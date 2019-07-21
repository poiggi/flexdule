package com.flexdule.android.model.sqlite.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

@Entity(tableName = "Cookie")
public class CookieBean implements Serializable {

    @PrimaryKey @NonNull
    private String name;
    private String label;
    private String value;

    public CookieBean() {
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
    public String
    toString() {
        return "CookieBean{" +
                "name='" + name + '\'' +
                ", label='" + label + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CookieBean that = (CookieBean) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(label, that.label) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, label, value);
    }
}
