package org.apptrueque.model;

import java.util.Date;

public class Notificacion {

    private String tipo;    // Puede ser "exito" o "error"
    private String mensaje; // El mensaje que quieres mostrar
    private Date fecha;     // Fecha de creación de la notificación

    // Constructor
    public Notificacion(String tipo, String mensaje) {
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.fecha = new Date(); // Se asigna automáticamente la fecha actual
    }

    // Métodos

    public void mostrarExito(String mensaje) {
        this.tipo = "exito";
        this.mensaje = mensaje;
        this.fecha = new Date();
        System.out.println("✅ [Éxito] " + mensaje);
    }

    public void mostrarError(String mensaje) {
        this.tipo = "error";
        this.mensaje = mensaje;
        this.fecha = new Date();
        System.out.println("❌ [Error] " + mensaje);
    }

    public void ocultar() {
        System.out.println("🧹 Notificación ocultada.");
        this.mensaje = "";
        this.tipo = "";
    }

    // Getters y Setters

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
}
