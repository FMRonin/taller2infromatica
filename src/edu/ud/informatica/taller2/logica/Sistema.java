package edu.ud.informatica.taller2.logica;

import edu.ud.informatica.taller2.presentacion.Model;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public
class Sistema{

    private boolean turno;
    private Thread hiloCanal;
    private Servidor servidor;
    private Cliente cliente;
    private Boolean comandoValido;
    // 0: esperando conexion, 1: Esperando nombre del contrincante, 2: Juego yo, 3: Esperando Jugada de adversario, 4: finalizado
    private int estadoJugada = 0;
    private Tablero tablero;
    private int filas, columnas;
    private Boolean tipoUsuario;

    public Boolean getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(Boolean tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public Servidor getServidor() {
        if (servidor == null){
            servidor = new Servidor(this);
        }
        return servidor;
    }

    public int getEstadoJugada(){
        return estadoJugada;
    }

    public Cliente getCliente() {
        if (cliente == null){
            cliente = new Cliente();
        }
        return cliente;
    }

    public void setTablero(int filas, int columnas){
        if (tablero == null){
            tablero = new Tablero(filas, columnas);
        }
    }

    public String armadoCodigo(Boolean tipo, String accion, String param){
        // tipo = true arma la cadena que coresponde al envio del codigo
        // tipo = false arma la cadena que corresponde a la respuesta de un codigo
        String codigo = new String();
        if(tipo){
            String idcabecera = "QDT";
            Date fechaActual = new Date();
            DateFormat formatoFecha = new SimpleDateFormat("yyyyMMdd");
            DateFormat formatoHora = new SimpleDateFormat("HHmmss");
            String fecha = formatoFecha.format(fechaActual);
            String hora = formatoHora.format(fechaActual);
            codigo = idcabecera+fecha+hora+accion+param;
        }
        else{
            codigo = accion + "," + param;
        }

        return codigo;
    }

    public void ConexionServicio(Boolean tipoUser)
    {
        setTipoUsuario(tipoUser);

        if(tipoUser)
        {
            try {
                //conecto
                getServidor().setConectado(true);
                String codigo = armadoCodigo(false,"INI","JAIRO");
                System.out.println(codigo);
                getServidor().IniciarConexion();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println("OK");
        }
    }

    public void recepcionMensaje(String mensaje){
        String fecha = mensaje.substring(3,11);
        String hora = mensaje.substring(11,17);
        String comando = mensaje.substring(17,20);
        String param = null;
        if (mensaje.substring(0,3).equals("QDT")) {
            try {
                int fechaInt = Integer.parseInt(fecha);
                int horaInt = Integer.parseInt(hora);
                if(fechaInt > 0 && horaInt > 0)
                {
                    switch (comando){
                        case "INI":
                            if(estadoJugada == 0){
                                estadoJugada = 1;
                            }
                            break;
                        case "SNM":
                            if(estadoJugada == 1){
                                if(getTipoUsuario()){
                                    estadoJugada = 2;
                                    param = mensaje.substring(20);
                                }
                                else
                                {
                                    estadoJugada = 3;
                                    param = mensaje.substring(20);
                                }
                            }
                            break;
                        case "TUR":
                            if(estadoJugada == 2 || estadoJugada == 3)
                            {
                                param = mensaje.substring(20);
                            }
                            break;
                        case "JUG":
                            if(estadoJugada == 2 || estadoJugada == 3)
                            {
                                param = mensaje.substring(20);
                            }
                            break;
                    }
                }
            } catch (NumberFormatException e) {
                comandoValido = false;
            }
        } else if (mensaje.substring(0,2).equals("OK")){
            if(estadoJugada == 0)
            {
                param = mensaje.substring(3);
                String[] parts = param.split(",");
                int filas = Integer.parseInt(parts[0]);
                int columnas = Integer.parseInt(parts[1]);
                comandoValido=false;
            }
        }else {
            comandoValido = false;
        }
        //System.out.println(param);
    }
    /*public static
    void validar() {
        respuesta = Cliente.getRestpuesta();
        .
        .
        .
        setMensaje(mensaje);
    }*/
}
