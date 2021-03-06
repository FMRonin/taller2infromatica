package edu.ud.informatica.taller2.logica;

import edu.ud.informatica.taller2.presentacion.Model;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public
class Sistema{


    private final int ESPERANDO_CONEXION = 0;
    private final int ESPERANDO_NOMBRE = 0;
    private final int JUGANDO = 0;
    private final int ESPERANDO_JUGADA = 0;
    private final int FINALIZADO = 0;
    private boolean comandoValido;
    private boolean turno;
    private Thread hiloCanal;
    private Servidor servidor;
    private Cliente cliente;
    // 0: esperando conexion, 1: Esperando nombre del contrincante, 2: Juego yo, 3: Esperando Jugada de adversario, 4: finalizado, 5.Analizando Jugada
    private int estadoJugada = 0;
    private Tablero tablero;
    private Boolean tipoUsuario;
    private boolean swich;
    private String nombreServidor;
    private String nombreCliente;
    private String IpServidor;
    private int filaJugada;
    private int columnaJugada;
    private int trazoJugado;

    public boolean isSwich() {
        return swich;
    }

    public void setSwich(boolean swich) {
        this.swich = swich;
    }

    public int getFilaJugada() {
        return filaJugada;
    }

    public int getColumnaJugada() {
        return columnaJugada;
    }

    public int getTrazoJugado() {
        return trazoJugado;
    }

    public void setNombreServidor(String nombreServidor) {
        this.nombreServidor = nombreServidor;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
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
                getCliente().IniciarConexion(getIpServidor());
                String respuesta = armadoCodigo(true, "INI", null);
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
                                String respuesta = armadoCodigo(false, "OK",
                                        getTablero().getFilas() + "," + getTablero().getColumnas());
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
                                String[] parts = param.split(",");
                                filaJugada = Integer.parseInt(parts[0]);
                                columnaJugada = Integer.parseInt(parts[1]);
                                trazoJugado = Integer.parseInt(parts[2]);
                                estadoJugada = 5;
                                swich = false;
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
                tablero = new Tablero(Integer.parseInt(parts[0]) , Integer.parseInt(parts[1]));
                estadoJugada = 1;
                getCliente().Enviar(armadoCodigo(true, "SNM", nombreCliente));
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
                    int estadoCelda = Integer.parseInt(parts[0]);
                    int estadoJuego = Integer.parseInt(parts[1]);
                    if(estadoJuego == 0){
                        if (estadoCelda == 0){
                            String respuesta = armadoCodigo(true, "TUR", (tipoUsuario)?"1":"2");
                            tipoUsuario = !tipoUsuario;
                            getServidor().Enviar(respuesta);
                        }else {
                            estadoJugada = JUGANDO;
                        }
                        respuestaMensaje = 1;
                        //Si estado juego es 0 , automaticamente se debe enviar TUR
                    } else {
                        estadoJugada = FINALIZADO;
                        //Si estado juego es 1, se finaliza el juego
                    }
                } else {
                    //Si no existe una coma en parametro, significa que es una respuesta de TUR
                    int turno = Integer.parseInt(param);
                    estadoJugada = ESPERANDO_JUGADA;
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

    public
    Tablero getTablero() {
        return tablero;
    }

    public
    void Jugar(int posY, int posX, int i, Boolean tipoUsuario) {
        tablero.Jugar(posY, posX, i, tipoUsuario);
        if(estadoJugada == 2) {
            String respuesta = armadoCodigo(true, "JUG", posY + "," + posX + "," + i);
            if(getTipoUsuario()) {
                getServidor().Enviar(respuesta);
            }
            else
            {
                getCliente().Enviar(respuesta);
            }
            estadoJugada = 3;
        }
        else if(estadoJugada == 5){
            String respuesta = armadoCodigo(false, "OK", "SALIDA");
            if(getTipoUsuario()) {
                getServidor().Enviar(respuesta);
            }
            else
            {
                getCliente().Enviar(respuesta);
            }
            estadoJugada = 3;
        }
    }
}
