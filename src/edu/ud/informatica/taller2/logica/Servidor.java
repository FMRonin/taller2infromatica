package edu.ud.informatica.taller2.logica;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public
class Servidor implements Runnable{

    static private final int PUERTO_SALIDA = 9090;
    static private final int TIMEOUT = 3000; // 3 segundos

    private ServerSocket serverSocket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private Socket cliente;
    private Thread hiloConexion;

    public void IniciarConexion(){

    }

    public void Enviar(String mensaje){

    }

    public void Escuchar(){

    }

    public void FinalizarConexion(){

    }


    @Override
    public
    void run() {

    }
}
