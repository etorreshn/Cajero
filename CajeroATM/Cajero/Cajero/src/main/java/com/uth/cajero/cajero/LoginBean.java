package com.uth.cajero.cajero;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ComponentSystemEvent;
import java.io.Serializable;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Named("loginBean")
@SessionScoped
public class LoginBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private String numeroCuenta;
    private String pin;
    private String nombreCliente;
    private String mensaje;
    private double saldo;
    private String montoRetiro;
    private String mensajeRetiro;
    private boolean retiroExitoso = false;
    private boolean mostrarRetireDinero = false;

    // Nuevas propiedades para depósito
    private String montoDeposito;
    private String mensajeDeposito;
    private boolean depositoExitoso = false;
    private boolean mostrarInsercionDinero = false;
    private boolean esperandoDinero = false;
    private double montoIngresado = 0;
    private double montoContado = 0;

    // Propiedades para transferencia
    private String cuentaDestino;
    private String montoTransferencia;
    private String mensajeTransferencia;
    private boolean transferenciaExitosa = false;

    // Propiedades para cambio de PIN
    private String pinActual;
    private String nuevoPin;
    private String confirmarPin;
    private String mensajePin;
    private boolean cambioPinExitoso = false;

    // Getters y setters
    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }

    public String getNombreCliente() { return nombreCliente; }

    public String getMensaje() { return mensaje; }

    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }

    public String getMontoRetiro() { return montoRetiro; }
    public void setMontoRetiro(String montoRetiro) { this.montoRetiro = montoRetiro; }

    public String getMensajeRetiro() { return mensajeRetiro; }
    public void setMensajeRetiro(String mensajeRetiro) { this.mensajeRetiro = mensajeRetiro; }

    public boolean isRetiroExitoso() { return retiroExitoso; }
    public void setRetiroExitoso(boolean retiroExitoso) { this.retiroExitoso = retiroExitoso; }

    public boolean isMostrarRetireDinero() { return mostrarRetireDinero; }
    public void setMostrarRetireDinero(boolean mostrarRetireDinero) { this.mostrarRetireDinero = mostrarRetireDinero; }

    // Getters y setters para depósito
    public String getMontoDeposito() { return montoDeposito; }
    public void setMontoDeposito(String montoDeposito) { this.montoDeposito = montoDeposito; }

    public String getMensajeDeposito() { return mensajeDeposito; }
    public void setMensajeDeposito(String mensajeDeposito) { this.mensajeDeposito = mensajeDeposito; }

    public boolean isDepositoExitoso() { return depositoExitoso; }
    public void setDepositoExitoso(boolean depositoExitoso) { this.depositoExitoso = depositoExitoso; }

    public boolean isMostrarInsercionDinero() { return mostrarInsercionDinero; }
    public void setMostrarInsercionDinero(boolean mostrarInsercionDinero) { this.mostrarInsercionDinero = mostrarInsercionDinero; }

    public boolean isEsperandoDinero() { return esperandoDinero; }
    public void setEsperandoDinero(boolean esperandoDinero) { this.esperandoDinero = esperandoDinero; }

    public double getMontoIngresado() { return montoIngresado; }
    public double getMontoContado() { return montoContado; }

    // Getters y setters para transferencia
    public String getCuentaDestino() { return cuentaDestino; }
    public void setCuentaDestino(String cuentaDestino) { this.cuentaDestino = cuentaDestino; }

    public String getMontoTransferencia() { return montoTransferencia; }
    public void setMontoTransferencia(String montoTransferencia) { this.montoTransferencia = montoTransferencia; }

    public String getMensajeTransferencia() { return mensajeTransferencia; }
    public void setMensajeTransferencia(String mensajeTransferencia) { this.mensajeTransferencia = mensajeTransferencia; }

    public boolean isTransferenciaExitosa() { return transferenciaExitosa; }
    public void setTransferenciaExitosa(boolean transferenciaExitosa) { this.transferenciaExitosa = transferenciaExitosa; }

    // Getters y setters para cambio de PIN
    public String getPinActual() { return pinActual; }
    public void setPinActual(String pinActual) { this.pinActual = pinActual; }

    public String getNuevoPin() { return nuevoPin; }
    public void setNuevoPin(String nuevoPin) { this.nuevoPin = nuevoPin; }

    public String getConfirmarPin() { return confirmarPin; }
    public void setConfirmarPin(String confirmarPin) { this.confirmarPin = confirmarPin; }

    public String getMensajePin() { return mensajePin; }
    public void setMensajePin(String mensajePin) { this.mensajePin = mensajePin; }

    public boolean isCambioPinExitoso() { return cambioPinExitoso; }
    public void setCambioPinExitoso(boolean cambioPinExitoso) { this.cambioPinExitoso = cambioPinExitoso; }

    // Método para obtener el saldo formateado con 2 decimales y Lps.
    public String getSaldoFormateado() {
        return String.format("Lps. %.2f", saldo);
    }

    // Método para obtener fecha actual
    public String getFechaActual() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(new Date());
    }

    // Método para registrar movimientos - VERSIÓN CORREGIDA
    private void registrarMovimiento(String tipo, double monto, String descripcion) {
        BufferedWriter bw = null;
        try {
            // Obtener la ruta real del archivo
            String path = FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getRealPath("/WEB-INF/data/movimientos.txt");

            System.out.println("=== INTENTANDO REGISTRAR MOVIMIENTO ===");
            System.out.println("Ruta del archivo: " + path);

            // Verificar si el archivo existe
            File file = new File(path);
            File parentDir = file.getParentFile();

            // Crear directorio si no existe
            if (!parentDir.exists()) {
                boolean dirsCreated = parentDir.mkdirs();
                System.out.println("Directorios creados: " + dirsCreated);
            }

            // Verificar permisos de escritura
            System.out.println("Puede escribir en directorio: " + parentDir.canWrite());
            System.out.println("Archivo existe: " + file.exists());
            if (file.exists()) {
                System.out.println("Puede escribir en archivo: " + file.canWrite());
            }

            // Formatear datos
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
            String fechaHora = sdf.format(new Date());
            String nuevoSaldo = String.format("%.2f", saldo);
            String montoStr = String.format("%.2f", monto);

            String movimiento = numeroCuenta + "," + tipo + "," + montoStr + "," +
                    nuevoSaldo + "," + fechaHora + "," + descripcion + System.lineSeparator();

            System.out.println("Movimiento a registrar: " + movimiento.trim());

            // Escribir en el archivo
            bw = new BufferedWriter(new FileWriter(file, true)); // true para append
            bw.write(movimiento);
            bw.flush(); // Forzar escritura inmediata

            System.out.println("=== MOVIMIENTO REGISTRADO EXITOSAMENTE ===");

        } catch (IOException e) {
            System.err.println("=== ERROR AL REGISTRAR MOVIMIENTO ===");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();

            // Intentar método alternativo
            registrarMovimientoAlternativo(tipo, monto, descripcion);

        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    System.err.println("Error al cerrar BufferedWriter: " + e.getMessage());
                }
            }
        }
    }

    // Método alternativo por si falla el principal
    private void registrarMovimientoAlternativo(String tipo, double monto, String descripcion) {
        try {
            // Intentar con ruta relativa
            String userHome = System.getProperty("user.home");
            String path = userHome + "/cajero_movimientos.txt";

            File file = new File(path);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
            String fechaHora = sdf.format(new Date());
            String montoStr = String.format("%.2f", monto);
            String saldoStr = String.format("%.2f", saldo);

            String movimiento = numeroCuenta + "," + tipo + "," + montoStr + "," +
                    saldoStr + "," + fechaHora + "," + descripcion + System.lineSeparator();

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                bw.write(movimiento);
                System.out.println("Movimiento registrado en ubicación alternativa: " + path);
            }

        } catch (IOException e) {
            System.err.println("Error en método alternativo: " + e.getMessage());
        }
    }

    // Método para registrar movimiento para otra cuenta
    private void registrarMovimientoParaCuenta(String numeroCuentaMov, String tipo, double monto, String descripcion) {
        try {
            String path = FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getRealPath("/WEB-INF/data/movimientos.txt");

            File file = new File(path);
            File parentDir = file.getParentFile();

            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
            String fechaHora = sdf.format(new Date());
            String montoStr = String.format("%.2f", monto);

            // Para movimientos de otras cuentas, el saldo se deja en 0.00 ya que no lo conocemos
            String movimiento = numeroCuentaMov + "," + tipo + "," + montoStr + "," +
                    "0.00" + "," + fechaHora + "," + descripcion + System.lineSeparator();

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                bw.write(movimiento);
                System.out.println("Movimiento registrado para cuenta " + numeroCuentaMov + ": " + movimiento.trim());
            }

        } catch (IOException e) {
            System.err.println("Error al registrar movimiento para cuenta: " + e.getMessage());
        }
    }

    // Método para actualizar saldo del cliente
    private void actualizarSaldoCliente() throws IOException {
        String clientesPath = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRealPath("/WEB-INF/data/clientes.txt");

        StringBuilder nuevoContenido = new StringBuilder();
        boolean clienteEncontrado = false;

        try (BufferedReader br = new BufferedReader(new FileReader(clientesPath))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 4 && partes[0].trim().equals(numeroCuenta)) {
                    // Actualizar solo el saldo, mantener el resto de campos
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd,HH:mm");
                    String fechaHora = sdf.format(new Date());

                    if (partes.length >= 6) {
                        linea = numeroCuenta + "," + pin + "," + nombreCliente + "," +
                                String.format("%.2f", saldo) + "," + fechaHora + "," + partes[5];
                    } else {
                        linea = numeroCuenta + "," + pin + "," + nombreCliente + "," +
                                String.format("%.2f", saldo) + "," + fechaHora + ",ACTIVO";
                    }
                    clienteEncontrado = true;
                }
                nuevoContenido.append(linea).append("\n");
            }
        }

        if (!clienteEncontrado) {
            throw new IOException("Cliente no encontrado en el archivo");
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(clientesPath))) {
            bw.write(nuevoContenido.toString());
        }

        System.out.println("Saldo actualizado correctamente para cuenta: " + numeroCuenta);
    }

    public String validarLogin() {
        nombreCliente = null;
        mensaje = null;
        saldo = 0.0;
        retiroExitoso = false;
        mostrarRetireDinero = false;
        depositoExitoso = false;
        mostrarInsercionDinero = false;
        esperandoDinero = false;
        transferenciaExitosa = false;
        cambioPinExitoso = false;

        try {
            String path = FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getRealPath("/WEB-INF/data/clientes.txt");

            System.out.println("Buscando cliente en: " + path);

            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                String linea;
                boolean clienteEncontrado = false;

                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.split(",");
                    if (partes.length >= 4) {
                        String cuenta = partes[0].trim();
                        String clave = partes[1].trim();
                        String nombre = partes[2].trim();
                        double saldoCliente = Double.parseDouble(partes[3].trim());

                        if (numeroCuenta.equals(cuenta) && pin.equals(clave)) {
                            nombreCliente = nombre;
                            saldo = saldoCliente;
                            this.numeroCuenta = cuenta;
                            this.pin = clave;
                            mensaje = "Login correcto. Bienvenido " + nombreCliente;
                            clienteEncontrado = true;

                            System.out.println("Login exitoso para: " + nombreCliente);
                            registrarMovimiento("LOGIN", 0.00, "Inicio de sesión");

                            return "index.xhtml?faces-redirect=true";
                        }
                    }
                }

                if (!clienteEncontrado) {
                    mensaje = "Número de cuenta o PIN incorrecto";
                }
            }

        } catch (IOException e) {
            mensaje = "Error al leer archivo de clientes";
            System.err.println("Error de IO: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException nfe) {
            mensaje = "Error de formato en el saldo del cliente";
            System.err.println("Error de formato: " + nfe.getMessage());
            nfe.printStackTrace();
        } catch (Exception e) {
            mensaje = "Error inesperado en el sistema";
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    // Método para realizar retiro - VERSIÓN CORREGIDA
    public void realizarRetiro() {
        mensajeRetiro = null;
        retiroExitoso = false;
        mostrarRetireDinero = false;

        if (montoRetiro == null || montoRetiro.trim().isEmpty()) {
            mensajeRetiro = "Ingrese un monto válido";
            return;
        }

        try {
            double monto = Double.parseDouble(montoRetiro);
            monto = Math.round(monto * 100.0) / 100.0;

            if (monto <= 0) {
                mensajeRetiro = "El monto debe ser mayor a cero";
                return;
            }

            if (monto > saldo) {
                mensajeRetiro = "Fondos insuficientes. Saldo disponible: " + getSaldoFormateado();
                return;
            }

            // 1. Primero registrar el movimiento
            registrarMovimiento("RETIRO", monto, "Retiro de efectivo - Cajero automático");

            // 2. Luego actualizar el saldo
            double saldoAnterior = saldo;
            saldo -= monto;
            saldo = Math.round(saldo * 100.0) / 100.0;

            System.out.println("Saldo anterior: " + saldoAnterior + ", Monto retirado: " + monto + ", Nuevo saldo: " + saldo);

            // 3. Actualizar archivo de clientes
            actualizarSaldoCliente();

            retiroExitoso = true;
            mostrarRetireDinero = true;
            mensajeRetiro = "Retiro exitoso. Monto retirado: Lps. " + String.format("%.2f", monto) +
                    ". Nuevo saldo: " + getSaldoFormateado() + ". Presione ACEPTAR para continuar";
            montoRetiro = "";

        } catch (NumberFormatException e) {
            mensajeRetiro = "Ingrese un monto válido (ej: 100.50)";
            System.err.println("Error de formato en monto: " + e.getMessage());
        } catch (IOException e) {
            mensajeRetiro = "Error al procesar el retiro";
            System.err.println("Error de IO en retiro: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            mensajeRetiro = "Error inesperado en el sistema";
            System.err.println("Error inesperado en retiro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para cancelar retiro
    public String cancelarRetiro() {
        montoRetiro = "";
        mensajeRetiro = null;
        retiroExitoso = false;
        mostrarRetireDinero = false;
        return "index.xhtml?faces-redirect=true";
    }

    // Método para finalizar retiro
    public String finalizarRetiro() {
        System.out.println("Finalizando retiro - Redirigiendo al menú principal");
        retiroExitoso = false;
        mostrarRetireDinero = false;
        montoRetiro = "";
        mensajeRetiro = null;
        return "index.xhtml?faces-redirect=true";
    }

    // MÉTODOS PARA DEPÓSITO - CORREGIDOS Y ACTUALIZADOS

    // Método principal para realizar depósito (este es el que llama tu XHTML)
    public String realizarDeposito() {
        mensajeDeposito = null;
        depositoExitoso = false;
        mostrarInsercionDinero = false;
        esperandoDinero = false;

        if (montoDeposito == null || montoDeposito.trim().isEmpty()) {
            mensajeDeposito = "Error: Ingrese un monto válido";
            return null;
        }

        try {
            double monto = Double.parseDouble(montoDeposito);
            monto = Math.round(monto * 100.0) / 100.0;

            if (monto <= 0) {
                mensajeDeposito = "Error: El monto debe ser mayor a cero";
                return null;
            }

            if (monto > 10000) {
                mensajeDeposito = "Error: El monto máximo por depósito es Lps. 10,000.00";
                return null;
            }

            // Simular proceso de depósito
            mostrarInsercionDinero = true;

            // En una aplicación real, aquí habría una pausa para la inserción física
            // Simulamos el proceso con un pequeño delay
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Actualizar saldo
            double saldoAnterior = saldo;
            saldo += monto;
            saldo = Math.round(saldo * 100.0) / 100.0;

            // Registrar movimiento
            registrarMovimiento("DEPOSITO", monto, "Depósito de efectivo - Cajero automático");

            // Actualizar archivo de clientes
            actualizarSaldoCliente();

            depositoExitoso = true;
            mostrarInsercionDinero = false;
            mensajeDeposito = "¡Depósito exitoso! Se han depositado Lps. " +
                    String.format("%.2f", monto) +
                    " en su cuenta. Nuevo saldo: " + getSaldoFormateado();

            System.out.println("Depósito exitoso - Monto: " + monto + ", Saldo anterior: " + saldoAnterior + ", Nuevo saldo: " + saldo);

        } catch (NumberFormatException e) {
            mensajeDeposito = "Error: Ingrese un monto válido (ej: 100.50)";
            System.err.println("Error de formato en depósito: " + e.getMessage());
        } catch (IOException e) {
            mensajeDeposito = "Error al procesar el depósito";
            System.err.println("Error de IO en depósito: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            mensajeDeposito = "Error inesperado en el sistema";
            System.err.println("Error inesperado en depósito: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    // Método para obtener el monto de depósito formateado (para usar en el XHTML)
    public String getMontoDepositoFormateado() {
        if (montoDeposito == null || montoDeposito.trim().isEmpty()) {
            return "0.00";
        }
        try {
            double monto = Double.parseDouble(montoDeposito);
            return String.format("%.2f", monto);
        } catch (NumberFormatException e) {
            return "0.00";
        }
    }

    // Método para cancelar depósito
    public String cancelarDeposito() {
        montoDeposito = "";
        mensajeDeposito = null;
        depositoExitoso = false;
        mostrarInsercionDinero = false;
        esperandoDinero = false;
        montoIngresado = 0;
        montoContado = 0;
        return "index.xhtml?faces-redirect=true";
    }

    // Método para finalizar depósito - CORREGIDO
    public String finalizarDeposito() {
        System.out.println("Finalizando depósito - Redirigiendo al menú principal");

        // Reiniciar todas las variables del depósito
        depositoExitoso = false;
        mostrarInsercionDinero = false;
        esperandoDinero = false;
        montoDeposito = "";
        mensajeDeposito = null;
        montoIngresado = 0;
        montoContado = 0;

        // Redirigir al menú principal
        return "index.xhtml?faces-redirect=true";
    }

    // MÉTODOS PARA TRANSFERENCIA
    public void realizarTransferencia() {
        mensajeTransferencia = null;
        transferenciaExitosa = false;

        if (cuentaDestino == null || cuentaDestino.trim().isEmpty()) {
            mensajeTransferencia = "Ingrese la cuenta destino";
            return;
        }

        if (montoTransferencia == null || montoTransferencia.trim().isEmpty()) {
            mensajeTransferencia = "Ingrese un monto válido";
            return;
        }

        try {
            double monto = Double.parseDouble(montoTransferencia);
            monto = Math.round(monto * 100.0) / 100.0;

            if (monto <= 0) {
                mensajeTransferencia = "El monto debe ser mayor a cero";
                return;
            }

            if (monto > saldo) {
                mensajeTransferencia = "Fondos insuficientes. Saldo disponible: " + getSaldoFormateado();
                return;
            }

            // Verificar que la cuenta destino existe
            String nombreDestino = verificarCuentaDestino(cuentaDestino);
            if (nombreDestino == null) {
                mensajeTransferencia = "Cuenta destino no encontrada";
                return;
            }

            // Verificar que no sea la misma cuenta
            if (cuentaDestino.equals(numeroCuenta)) {
                mensajeTransferencia = "No puede transferir a la misma cuenta";
                return;
            }

            // Realizar la transferencia
            if (realizarTransferenciaEntreCuentas(cuentaDestino, monto, nombreDestino)) {
                // Registrar movimiento para cuenta origen
                registrarMovimiento("TRANSFERENCIA_ENVIADA", monto, "Transferencia a cuenta: " + cuentaDestino);

                // Actualizar saldo local
                saldo -= monto;
                saldo = Math.round(saldo * 100.0) / 100.0;

                // Actualizar archivo de clientes
                actualizarSaldoCliente();

                transferenciaExitosa = true;
                mensajeTransferencia = "Transferencia exitosa. Monto: Lps. " + String.format("%.2f", monto) +
                        " a cuenta: " + cuentaDestino + ". Nuevo saldo: " + getSaldoFormateado();

                cuentaDestino = "";
                montoTransferencia = "";
            } else {
                mensajeTransferencia = "Error al procesar la transferencia";
            }

        } catch (NumberFormatException e) {
            mensajeTransferencia = "Ingrese un monto válido (ej: 100.50)";
        } catch (Exception e) {
            mensajeTransferencia = "Error inesperado en el sistema";
            e.printStackTrace();
        }
    }

    // Método para verificar si la cuenta destino existe
    private String verificarCuentaDestino(String numeroCuentaDestino) {
        try {
            String path = FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getRealPath("/WEB-INF/data/clientes.txt");

            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.split(",");
                    if (partes.length >= 4 && partes[0].trim().equals(numeroCuentaDestino)) {
                        return partes[2].trim(); // Retorna el nombre del cliente destino
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al verificar cuenta destino: " + e.getMessage());
        }
        return null;
    }

    // Método para realizar la transferencia entre cuentas
    private boolean realizarTransferenciaEntreCuentas(String cuentaDestino, double monto, String nombreDestino) {
        try {
            String path = FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getRealPath("/WEB-INF/data/clientes.txt");

            StringBuilder nuevoContenido = new StringBuilder();
            boolean transferenciaRealizada = false;

            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.split(",");
                    if (partes.length >= 4) {
                        // Actualizar cuenta destino
                        if (partes[0].trim().equals(cuentaDestino)) {
                            double saldoDestino = Double.parseDouble(partes[3].trim());
                            saldoDestino += monto;
                            saldoDestino = Math.round(saldoDestino * 100.0) / 100.0;

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd,HH:mm");
                            String fechaHora = sdf.format(new Date());

                            if (partes.length >= 6) {
                                linea = cuentaDestino + "," + partes[1] + "," + partes[2] + "," +
                                        String.format("%.2f", saldoDestino) + "," + fechaHora + "," + partes[5];
                            } else {
                                linea = cuentaDestino + "," + partes[1] + "," + partes[2] + "," +
                                        String.format("%.2f", saldoDestino) + "," + fechaHora + ",ACTIVO";
                            }

                            // Registrar movimiento para cuenta destino
                            registrarMovimientoParaCuenta(cuentaDestino, "TRANSFERENCIA_RECIBIDA", monto,
                                    "Transferencia de cuenta: " + numeroCuenta);
                            transferenciaRealizada = true;
                        }
                    }
                    nuevoContenido.append(linea).append("\n");
                }
            }

            if (transferenciaRealizada) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
                    bw.write(nuevoContenido.toString());
                }
                return true;
            }

        } catch (IOException e) {
            System.err.println("Error en transferencia entre cuentas: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Método para cancelar transferencia
    public String cancelarTransferencia() {
        cuentaDestino = "";
        montoTransferencia = "";
        mensajeTransferencia = null;
        transferenciaExitosa = false;
        return "index.xhtml?faces-redirect=true";
    }

    // Método para finalizar transferencia
    public String finalizarTransferencia() {
        transferenciaExitosa = false;
        cuentaDestino = "";
        montoTransferencia = "";
        mensajeTransferencia = null;
        return "index.xhtml?faces-redirect=true";
    }

    // MÉTODOS PARA CAMBIO DE PIN - MODIFICADOS
    public String cambiarPin() {
        mensajePin = null;
        cambioPinExitoso = false;

        if (pinActual == null || pinActual.trim().isEmpty()) {
            mensajePin = "Ingrese su PIN actual";
            return null;
        }

        if (nuevoPin == null || nuevoPin.trim().isEmpty()) {
            mensajePin = "Ingrese el nuevo PIN";
            return null;
        }

        if (confirmarPin == null || confirmarPin.trim().isEmpty()) {
            mensajePin = "Confirme el nuevo PIN";
            return null;
        }

        // Validar que el PIN actual sea correcto
        if (!pinActual.equals(pin)) {
            mensajePin = "PIN actual incorrecto";
            return null;
        }

        // Validar que el nuevo PIN sea numérico y de 4 dígitos
        if (!nuevoPin.matches("\\d{4}")) {
            mensajePin = "El nuevo PIN debe ser de 4 dígitos numéricos";
            return null;
        }

        // Validar que los PIN nuevos coincidan
        if (!nuevoPin.equals(confirmarPin)) {
            mensajePin = "Los PIN nuevos no coinciden";
            return null;
        }

        // Validar que no sea el mismo PIN
        if (nuevoPin.equals(pinActual)) {
            mensajePin = "El nuevo PIN debe ser diferente al actual";
            return null;
        }

        try {
            // Actualizar el PIN en el archivo de clientes
            if (actualizarPinEnArchivo(nuevoPin)) {
                // Actualizar el PIN en la sesión actual
                this.pin = nuevoPin;

                // Registrar movimiento
                registrarMovimiento("CAMBIO_PIN", 0.00, "Cambio de PIN de seguridad");

                cambioPinExitoso = true;
                mensajePin = "PIN cambiado exitosamente. Redirigiendo al menú...";

                // Limpiar campos
                pinActual = "";
                nuevoPin = "";
                confirmarPin = "";

                // Redirigir automáticamente
                return "index.xhtml?faces-redirect=true";
            } else {
                mensajePin = "Error al actualizar el PIN. Intente nuevamente.";
                return null;
            }

        } catch (Exception e) {
            mensajePin = "Error inesperado al cambiar el PIN";
            e.printStackTrace();
            return null;
        }
    }

    // Método para actualizar el PIN en el archivo
    private boolean actualizarPinEnArchivo(String nuevoPin) {
        try {
            String path = FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getRealPath("/WEB-INF/data/clientes.txt");

            StringBuilder nuevoContenido = new StringBuilder();
            boolean pinActualizado = false;

            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.split(",");
                    if (partes.length >= 4 && partes[0].trim().equals(numeroCuenta)) {
                        // Actualizar el PIN, mantener el resto de campos
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd,HH:mm");
                        String fechaHora = sdf.format(new Date());

                        if (partes.length >= 6) {
                            linea = numeroCuenta + "," + nuevoPin + "," + nombreCliente + "," +
                                    String.format("%.2f", saldo) + "," + fechaHora + "," + partes[5];
                        } else {
                            linea = numeroCuenta + "," + nuevoPin + "," + nombreCliente + "," +
                                    String.format("%.2f", saldo) + "," + fechaHora + ",ACTIVO";
                        }
                        pinActualizado = true;
                    }
                    nuevoContenido.append(linea).append("\n");
                }
            }

            if (pinActualizado) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
                    bw.write(nuevoContenido.toString());
                }
                return true;
            }

        } catch (IOException e) {
            System.err.println("Error al actualizar PIN: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Método para cancelar cambio de PIN
    public String cancelarCambioPin() {
        pinActual = "";
        nuevoPin = "";
        confirmarPin = "";
        mensajePin = null;
        cambioPinExitoso = false;
        return "index.xhtml?faces-redirect=true";
    }

    // MÉTODOS PARA MINI ESTADO DE CUENTA
    // Método para obtener los movimientos de la cuenta (solo transferencias, depósitos y retiros)
    public List<String[]> obtenerMovimientosCuenta() {
        List<String[]> movimientos = new ArrayList<>();
        try {
            String path = FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getRealPath("/WEB-INF/data/movimientos.txt");

            File file = new File(path);
            if (!file.exists()) {
                return movimientos;
            }

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.split(",");
                    if (partes.length >= 6 && partes[0].trim().equals(numeroCuenta)) {
                        String tipo = partes[1];

                        // Filtrar solo los tipos de movimiento que queremos mostrar
                        if (tipo.equals("RETIRO") || tipo.equals("DEPOSITO") ||
                                tipo.equals("TRANSFERENCIA_ENVIADA") || tipo.equals("TRANSFERENCIA_RECIBIDA")) {

                            // Formatear la fecha para mostrar mejor
                            String fechaHora = partes[4] + " " + partes[5];
                            String monto = partes[2];
                            String descripcion = partes.length > 6 ? partes[6] : "";

                            movimientos.add(new String[]{fechaHora, tipo, monto, descripcion});
                        }
                    }
                }
            }

            // Ordenar movimientos por fecha (más recientes primero)
            movimientos.sort((a, b) -> b[0].compareTo(a[0]));

            // Limitar a los últimos 10 movimientos
            if (movimientos.size() > 10) {
                movimientos = movimientos.subList(0, 10);
            }

        } catch (IOException e) {
            System.err.println("Error al leer movimientos: " + e.getMessage());
        }
        return movimientos;
    }

    // Método auxiliar para determinar la clase CSS del monto
    public String getClaseMonto(String tipo, String montoStr) {
        if (tipo.equals("DEPOSITO") || tipo.equals("TRANSFERENCIA_RECIBIDA")) {
            return "monto-positivo";
        } else if (tipo.equals("RETIRO") || tipo.equals("TRANSFERENCIA_ENVIADA")) {
            return "monto-negativo";
        } else {
            return "monto-neutral";
        }
    }

    // Método para formatear el tipo de movimiento (versión más corta)
    public String formatearTipoMovimiento(String tipo) {
        switch(tipo) {
            case "RETIRO": return "RETIRO";
            case "DEPOSITO": return "DEPÓSITO";
            case "TRANSFERENCIA_ENVIADA": return "TRANS. ENV.";
            case "TRANSFERENCIA_RECIBIDA": return "TRANS. REC.";
            default: return tipo;
        }
    }

    // Método para consultar saldo
    public String consultarSaldo() {
        registrarMovimiento("CONSULTA", 0.00, "Consulta de saldo");
        return "consultarSaldo.xhtml?faces-redirect=true";
    }

    // Método para verificar autenticación
    public void verificarAutenticacion(ComponentSystemEvent event) {
        if (nombreCliente == null || nombreCliente.isEmpty()) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
            } catch (IOException e) {
                System.err.println("Error al redirigir: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Método para limpiar sesión
    public String limpiar() {
        if (nombreCliente != null) {
            registrarMovimiento("LOGOUT", 0.00, "Cierre de sesión");
        }

        numeroCuenta = "";
        pin = "";
        mensaje = "";
        nombreCliente = null;
        saldo = 0.0;
        montoRetiro = "";
        mensajeRetiro = null;
        retiroExitoso = false;
        mostrarRetireDinero = false;
        montoDeposito = "";
        mensajeDeposito = null;
        depositoExitoso = false;
        mostrarInsercionDinero = false;
        esperandoDinero = false;
        montoIngresado = 0;
        montoContado = 0;
        cuentaDestino = "";
        montoTransferencia = "";
        mensajeTransferencia = null;
        transferenciaExitosa = false;
        pinActual = "";
        nuevoPin = "";
        confirmarPin = "";
        mensajePin = null;
        cambioPinExitoso = false;

        System.out.println("Sesión limpiada exitosamente");
        return "index.xhtml?faces-redirect=true";
    }

    // Método para depuración - verificar archivo de movimientos
    public void verificarArchivoMovimientos() {
        try {
            String path = FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getRealPath("/WEB-INF/data/movimientos.txt");

            File file = new File(path);
            System.out.println("=== VERIFICACIÓN ARCHIVO MOVIMIENTOS ===");
            System.out.println("Ruta: " + path);
            System.out.println("Existe: " + file.exists());
            System.out.println("Puede escribir: " + file.canWrite());
            System.out.println("Tamaño: " + file.length());

            if (file.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String linea;
                    System.out.println("=== CONTENIDO ACTUAL ===");
                    int count = 0;
                    while ((linea = br.readLine()) != null) {
                        System.out.println(++count + ": " + linea);
                    }
                    if (count == 0) {
                        System.out.println("ARCHIVO VACÍO");
                    }
                }
            } else {
                System.out.println("ARCHIVO NO EXISTE");
            }
        } catch (Exception e) {
            System.err.println("Error en verificación: " + e.getMessage());
            e.printStackTrace();
        }
    }
}