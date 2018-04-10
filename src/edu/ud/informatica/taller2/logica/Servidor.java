package edu.ud.informatica.taller2.logica;

import edu.ud.informatica.taller2.presentacion.Model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public
class Servidor implements Runnable{

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
    private byte buffer[] = new byte[30];
    private String ipCliente;

    public String getIpCliente() {
        return ipCliente;
    }

    public void setIpCliente(String ipCliente) {
        this.ipCliente = ipCliente;
    }

    public String getMensaje() {
        return mensaje;
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
        extraerIP();
        getSistema().recepcionMensaje(getMensaje());
    }

    public void FinalizarConexion(){

    }

    public void extraerIP(){
        String[] parts = getIpCliente().split("/");
        int posicion= parts[1].indexOf(':');
        setIpCliente(parts[1].substring(0,posicion));
        System.out.println(getIpCliente());
    }

    @Override
    public
    void run() {
        try {
            synchronized (hiloConexion){
                while (conectado) {
                    System.out.println("Esperando Conexion del Cliente");
                    cliente = serverSocket.accept();
                    cliente.setSoTimeout(TIMEOUT);
                    inputStream = new DataInputStream(cliente.getInputStream());
                    inputStream.read(buffer);
                    mensaje = new String(buffer);
                    setIpCliente(cliente.getRemoteSocketAddress().toString());
                    Escuchar();
                    hiloConexion.wait(500);
                    inputStream.close();
                    cliente.close();
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }
    }
}
