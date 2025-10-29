package com.uth.cajero.cajero;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("menuBean")
@SessionScoped
public class MenuBean implements Serializable {

    private String mensaje;

    public String consultarSaldo() {
        mensaje = "Su saldo actual es L. 5,000.00";
        return null;
    }

    public String retirar() {
        mensaje = "Ha seleccionado la opción de retiro.";
        return null;
    }

    public String depositar() {
        mensaje = "Ha seleccionado la opción de depósito.";
        return null;
    }

    public String transferir() {
        mensaje = "Ha seleccionado la opción de transferencia.";
        return null;
    }

    public String cambiarPin() {
        mensaje = "Ha seleccionado cambiar su PIN.";
        return null;
    }

    public String salir() {
        mensaje = "Gracias por usar el cajero. Redirigiendo a inicio...";
        return "login.xhtml?faces-redirect=true";
    }

    // Getters y Setters
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
