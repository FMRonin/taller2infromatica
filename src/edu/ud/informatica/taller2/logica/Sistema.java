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
    private Model model;
    private int opt = 0;
    private Boolean comandoValido;

    public Servidor getServidor() {
        if (servidor == null){
            servidor = new Servidor();
        }
        return servidor;
    }

    public Model getModel() {
        if (model == null){
            model = new Model();
        }
        return model;
    }

    public Cliente getCliente() {
        if (cliente == null){
            cliente = new Cliente();
        }
        return cliente;
    }

    public int getOpt() {
        return opt;
    }

    public void setOpt(int opt) {
        this.opt = opt;
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
            codigo = accion + param;
        }

        return codigo;
    }

    public void ConexionServicio(Boolean tipoUser)
    {
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
                            setOpt(1);
                            break;
                        case "SNM":
                            setOpt(2);
                            param = mensaje.substring(20);
                            break;
                        case "TUR":
                            setOpt(3);
                            break;
                        case "JUG":
                            setOpt(4);
                            param = mensaje.substring(20);
                            break;
                    }
                }
            } catch (NumberFormatException e) {
                comandoValido = false;
            }
        }else if (mensaje.substring(0,2).equals("OK")
                || mensaje.substring(0,2).equals("NK")){
            comandoValido = true;
            param = mensaje.substring(3);
        }else {
            comandoValido = false;
        }
        System.out.println(param);
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
