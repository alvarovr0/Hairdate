package com.example.hairdate.model;

public class ChatModel {
    String Mensaje, envioEmail;

    public ChatModel() {

    }

    public String getMensaje() {
        return Mensaje;
    }

    public void setMensaje(String mensaje) {
        Mensaje = mensaje;
    }

    public String getEnvioEmail() {
        return envioEmail;
    }

    public void setEnvioEmail(String envioEmail) {
        this.envioEmail = envioEmail;
    }
    /*
    public String getReciboEmail() {
        return reciboEmail;
    }

    public void setReciboEmail(String reciboEmail) {
        this.reciboEmail = reciboEmail;
    }
    */
}
