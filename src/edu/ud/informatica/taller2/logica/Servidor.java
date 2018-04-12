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
    private Socket envio;
    private Thread hiloConexion;
    private boolean conectado;
    private String ipCliente;

    public Servidor(Sistema sistema){
        this.sistema = sistema;
    }

    public String getIpCliente() {
        return ipCliente;
    }

    public void setIpCliente(String ipCliente) {
        this.ipCliente = ipCliente;
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
        cliente = serverSocket.accept();
        //cliente.setSoTimeout(TIMEOUT);
        inputStream = new DataInputStream(cliente.getInputStream());
        outputStream = new DataOutputStream(cliente.getOutputStream());
        hiloConexion = new Thread(this);
        hiloConexion.start();
    }

    public void Enviar(String mensaje){
        try {
            //outputStream = new DataOutputStream(cliente.getOutputStream());
            outputStream.write(mensaje.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Escuchar(){
        extraerIP();
        eliminarEspacios();
        getSistema().recepcionMensaje(getMensaje());
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
                    //System.out.println("Esperando Conexion del Cliente");
                    byte buffer[] = new byte[255];
                    inputStream.read(buffer);
                    mensaje = new String(buffer);
                    System.out.println(mensaje);
                    setIpCliente(cliente.getRemoteSocketAddress().toString());
                    Escuchar();
                    hiloConexion.wait(500);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
