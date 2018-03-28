package edu.ud.informatica.taller2.presentacion;

import edu.ud.informatica.taller2.logica.Sistema;

import java.awt.*;

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
        while(true) {
            dibujarCuadros();
        }
    }

    private void dibujarCuadros() {
        Graphics2D g = (Graphics2D) getVentana().getCnvTablero().getGraphics();
        double thickness = 5;
        Stroke oldStroke = g.getStroke();
        g.setStroke(new BasicStroke((float) thickness));
        for(int i = 0 ; i < 5 ; i++) {
            g.setColor(Color.GREEN);
            g.drawRect(i*100,i*100,100,100);
        }
    }

    public void iniciar() {
        getVentana().setSize( 300, 230);
        getVentana().getPnIni().setBounds(0,0,
                getVentana().getWidth(),getVentana().getHeight());
        getVentana().getPnServer().setBounds(0,0,
                getVentana().getWidth(),getVentana().getHeight());
        getVentana().getPnClient().setBounds(0,0,
                getVentana().getWidth(),getVentana().getHeight());

        getVentana().setVisible(true);
    }

    public void StartAsServer() {
        getVentana().getPnIni().setVisible(false);
        getVentana().getPnServer().setVisible(true);
        getVentana().getPnClient().setVisible(false);
        getVentana().getPnGame().setVisible(false);
    }

    public void StartAsClient() {
        getVentana().getPnIni().setVisible(false);
        getVentana().getPnServer().setVisible(false);
        getVentana().getPnClient().setVisible(true);
        getVentana().getPnGame().setVisible(false);
    }

    public void ReturnIni() {
        getVentana().getPnIni().setVisible(true);
        getVentana().getPnServer().setVisible(false);
        getVentana().getPnClient().setVisible(false);
        getVentana().getPnGame().setVisible(false);
    }

    public void CreateConn() {
        ventana.setSize(700,550);
        getVentana().getPnIni().setVisible(false);
        getVentana().getPnServer().setVisible(false);
        getVentana().getPnClient().setVisible(false);
        getVentana().getPnGame().setVisible(true);
        hiloDibujo = new Thread(this);
        hiloDibujo.start();
    }

    public void Connect() {
        ventana.setSize(700,550);
        getVentana().getPnIni().setVisible(false);
        getVentana().getPnServer().setVisible(false);
        getVentana().getPnClient().setVisible(false);
        getVentana().getPnGame().setVisible(true);
        hiloDibujo = new Thread(this);
        hiloDibujo.start();
    }
}
