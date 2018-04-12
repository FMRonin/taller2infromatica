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
    private String nombreServidor;
    private String IpServidor;

    public void setNombreServidor(String nombreServidor) {
        this.nombreServidor = nombreServidor;
    }

    public String getIpServidor() {
        return IpServidor;
    }

    public void setIpServidor(String ipServidor) {
        IpServidor = ipServidor;
    }

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
            cliente = new Cliente(this);
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
            if(param != null)
            {
                codigo = idcabecera+fecha+hora+accion+param;
            }
            else
            {
                codigo = idcabecera+fecha+hora+accion;
            }

        }
        else{
            if(param != null)
            {
                codigo = accion + "," + param;
            }
            else
            {
                codigo = accion;
            }

        }

        return codigo;
    }

    public void ConexionServicio(Boolean tipoUser)
    {
        setTipoUsuario(tipoUser);

        if(tipoUser)
        {
            try {
                //conexion como servidor
                getServidor().setConectado(true);
                getServidor().IniciarConexion();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            try {
                //connexion como cliente
                getCliente().setConectado(true);
                getCliente().IniciarConexion();
                String respuesta = armadoCodigo(true, "INI", "");
                getCliente().Enviar(respuesta);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void recepcionMensaje(String mensaje){
        int respuestaMensaje = 0;
        String param = null;
        if (mensaje.substring(0,3).equals("QDT")) {
            String fecha = mensaje.substring(3,11);
            String hora = mensaje.substring(11,17);
            String comando = mensaje.substring(17,20);
            try {
                int fechaInt = Integer.parseInt(fecha);
                int horaInt = Integer.parseInt(hora);
                if(fechaInt > 0 && horaInt > 0)
                {
                    switch (comando){
                        case "INI":
                            if(estadoJugada == 0){
                                estadoJugada = 1;
                                String respuesta = armadoCodigo(false, "OK", "10,10");
                                getServidor().Enviar(respuesta);
                            } else {
                                respuestaMensaje = 0;
                            }
                            break;
                        case "SNM":
                            if(estadoJugada == 1) {
                                estadoJugada = 2;
                                param = mensaje.substring(20);
                                String respuesta = armadoCodigo(false, "OK", nombreServidor);
                                getServidor().Enviar(respuesta);
                            }
                            break;
                        case "TUR":
                            if(estadoJugada == 3)
                            {
                                param = mensaje.substring(20);
                            }
                            break;
                        case "JUG":
                            if(estadoJugada == 3)
                            {
                                param = mensaje.substring(20);
                                //CICLO MIENTRAS EVALUA EL PARAMETRO COMPLETO
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
                //Como cliente recibo las filas y columnas del servidor y paso a estadoJugada 1
                param = mensaje.substring(3);
                String[] parts = param.split(",");
                int filas = Integer.parseInt(parts[0]);
                int columnas = Integer.parseInt(parts[1]);
                tablero = new Tablero(filas, columnas);
                estadoJugada = 1;
                respuestaMensaje = 1;
            } else if (estadoJugada == 1){
                //Como cliente recibo el nombre del servidor y paso a estadoJugada 3
                param = mensaje.substring(3);
                estadoJugada = 3;
                respuestaMensaje = 1;
            } else if (estadoJugada == 3) {
                param = mensaje.substring(3);
                int posicion = param.indexOf(',');
                if(posicion > 0){
                    //Si existe una coma en el parameatro, significa que es una respuesta de JUG
                    String[] parts = param.split(",");
                    int estadoGame = Integer.parseInt(parts[0]);
                    int estadoJuego = Integer.parseInt(parts[1]);
                    if(estadoJuego == 0){
                        respuestaMensaje = 1;
                        //Si estado juego es 0 , automaticamente se debe enviar TUR
                    } else {
                        //Si estado juego es 1, se finaliza el juego
                        estadoJugada = 4;
                    }
                } else {
                    //Si no existe una coma en parametro, significa que es una respuesta de TUR
                    int turno = Integer.parseInt(param);
                    estadoJugada = 3;
                    respuestaMensaje = 1;
                }
            }
        }else if (mensaje.substring(0,2).equals("NK")) {
            if(estadoJugada == 0)
            {
                respuestaMensaje = 0;
            } else if (estadoJugada == 1){

            } else if (estadoJugada == 3) {
                param = mensaje.substring(1);
                int posicion = param.indexOf(',');
                if(posicion > 0){
                    //Si existe una coma en el parameatro, significa que es una respuesta de JUG
                    String param2 = mensaje.substring(3);
                    int respuesta = Integer.parseInt(param2);
                    //sea la respuesta que llegue al NK, ese jugador debe volver a estado 2 para realizar otra jugada
                    estadoJugada = 2;
                } else {
                    //Si no existe una coma en parametro, significa que es una respuesta de TUR
                }
            }
        }

    }
    /*public static
    void validar() {
        respuesta = Cliente.getRestpuesta();
        .
        .
        .
        setMensaje(mensaje);
    }*/

    public
    Tablero getTablero() {
        return tablero;
    }

    public
    void Jugar(int posY, int posX, int i, Boolean tipoUsuario) {
        tablero.Jugar(posY,posX,i,tipoUsuario);
        String respuesta = armadoCodigo(true, "JUG", posY + "," + posX + "," + i);
        getServidor().Enviar(respuesta);
    }
}
