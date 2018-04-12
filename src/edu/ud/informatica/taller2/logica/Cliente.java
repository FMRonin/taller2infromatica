package edu.ud.informatica.taller2.logica;

import edu.ud.informatica.taller2.presentacion.Model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public
class Cliente implements Runnable {

    static private final int PUERTO_SALIDA = 9090;
    static private final int TIMEOUT = 3000; // 3 segundos

    private Sistema sistema;
    private Model modelo;
    private String mensaje = "";
    private ServerSocket serverSocket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private Socket cliente;
    private Thread hiloConexion;
    private boolean conectado;

    public Cliente(Sistema sistema){
        this.sistema = sistema;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isConectado() {
        return conectado;
    }

    public void setConectado(boolean conectado) {
        this.conectado = conectado;
    }

    public Sistema getSistema() {
        if (sistema == null){
            sistema = new Sistema();
        }
        return sistema;
    }

    public Model getModelo() {
        if (modelo == null){
            modelo = new Model();
        }
        return modelo;
    }

    public void IniciarConexion() throws IOException {
        serverSocket = new ServerSocket(PUERTO_SALIDA);
        hiloConexion = new Thread(this);
        hiloConexion.start();
    }

    public void Enviar(String mensaje){

    }

    public void Escuchar(){
        eliminarEspacios();
        int out = getSistema().recepcionMensaje(getMensaje());
        if(out == 0){
            FinalizarConexion();
        }
    }

    public void FinalizarConexion(){
        try {
            inputStream.close();
            cliente.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void eliminarEspacios(){
        int posicion = getMensaje().indexOf('\u0000');
        setMensaje(getMensaje().substring(0,posicion));
    }

    @Override
    public
    void run() {
        try {
            synchronized (hiloConexion){
                while (conectado) {
                    System.out.println("Esperando Conexion del servidor");
                    cliente = serverSocket.accept();
                    cliente.setSoTimeout(TIMEOUT);
                    inputStream = new DataInputStream(cliente.getInputStream());
                    byte buffer[] = new byte[30];
                    inputStream.read(buffer);
                    mensaje = new String(buffer);
                    Escuchar();
                    hiloConexion.wait(500);
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }
    }

}
