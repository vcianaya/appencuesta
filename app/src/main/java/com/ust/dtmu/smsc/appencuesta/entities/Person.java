package com.ust.dtmu.smsc.appencuesta.entities;

public class Person {
    String ci, nombre;

    public Person(String ci, String nombre) {
        this.ci = ci;
        this.nombre = nombre;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
