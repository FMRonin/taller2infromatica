package edu.ud.informatica.taller2.presentacion;

import edu.ud.informatica.taller2.logica.Sistema;

public
class Model implements Runnable{

    private Sistema sistema;
    private Vista ventana;
    private Thread hiloDibujo;

    public Sistema getSistema() {
        if (sistema == null){
            sistema = new Sistema();
        }
        return sistema;
    }

    public Vista getVentana() {
        if (ventana == null){
            ventana = new Vista(this);
        }
        return ventana;
    }

    @Override
    public void run() {

    }

    public void iniciar() {
        getVentana().setSize( 300, 230);
        getVentana().setVisible(true);
    }
}
