package com.uth.cajero.cajero;

public class Cliente {

    private String id;
    private String nombre;
    private double saldo;

    public Cliente(String id, String nombre, double saldo) {
        this.id = id;
        this.nombre = nombre;
        this.saldo = saldo;
    }


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getSaldo() {
        return saldo;
    }
    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}
