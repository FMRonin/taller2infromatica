package edu.ud.informatica.taller2.logica;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public
class Cliente implements Runnable {

    static private final int PUERTO_SALIDA = 9090;
    static private final int TIMEOUT = 3000; // 3 segundos

    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private Socket cliente;
    private Thread hiloConexion;

    private String respuesta;
    private String mensaje;

    public
    void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public
    String getRespuesta() {
        return respuesta;
    }

    public void Enviar(){

    }

    public void IniciarConexion(){

    }

    public void Escuchar(){

    }

    public void FinalizarConexion(){

    }


    @Override
    public
    void run() {
        if(bufferRespuesta != ""){
            restpuesta = bufferRespuesta;
            Sistema.validar();
            Enviar();
        }

    }

}
