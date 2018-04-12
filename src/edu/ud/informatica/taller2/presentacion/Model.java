package edu.ud.informatica.taller2.presentacion;

import edu.ud.informatica.taller2.logica.Celda;
import edu.ud.informatica.taller2.logica.Sistema;

import java.awt.*;
import java.awt.event.MouseEvent;


public
class Model implements Runnable{

    static private final int  MARGEN = 2;
    static private final Color COLOR_SERVER = Color.BLUE;
    static private final Color COLOR_CLIENT = Color.GREEN;

    private Sistema sistema;
    private Vista ventana;
    private Thread hiloDibujo;

    private int filas;
    private int columnas;
    private int cuadro;


    private Graphics2D g;




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
        synchronized (hiloDibujo) {
            try {
                while (true) {
                    hiloDibujo.wait(500);
                    dibujarCuadros();
                }
            } catch (InterruptedException e) {
                //e.printStackTrace();
            } catch (NullPointerException e){

            }
        }
    }

    private void dibujarCuadros() {

        if(getSistema().isSwich()) {

            int thickness = 1;

            g = (Graphics2D) getVentana().getCnvTablero().getGraphics();
            g.setStroke(new BasicStroke(thickness));

            for (int i = 0; i < columnas; i++) {
                for (int j = 0; j < filas; j++) {
                    int x1 = (i * cuadro) + MARGEN;
                    int y1 = (j * cuadro) + MARGEN;

                    if ((sistema.getTablero().getCelda(j, i).getBordeCelda() & Celda.ARRIBA) > 0) {
                        g.setColor(Color.RED);
                    } else {
                        g.setColor(Color.WHITE);
                    }
                    g.drawLine(x1, y1, x1 + cuadro, y1);

                    if ((sistema.getTablero().getCelda(j, i).getBordeCelda() & Celda.DERECHA) > 0) {
                        g.setColor(Color.RED);
                    } else {
                        g.setColor(Color.WHITE);
                    }
                    g.drawLine(x1 + cuadro, y1, x1 + cuadro, y1 + cuadro);

                    if ((sistema.getTablero().getCelda(j, i).getBordeCelda() & Celda.ABAJO) > 0) {
                        g.setColor(Color.RED);
                    } else {
                        g.setColor(Color.WHITE);
                    }
                    g.drawLine(x1, y1 + cuadro, x1 + cuadro, y1 + cuadro);

                    if ((sistema.getTablero().getCelda(j, i).getBordeCelda() & Celda.IZQUIERDA) > 0) {
                        g.setColor(Color.RED);
                    } else {
                        g.setColor(Color.WHITE);
                    }
                    g.drawLine(x1, y1, x1, y1 + cuadro);

                    if (sistema.getTablero().getCelda(j, i).getBordeCelda() == Celda.LLENA) {
                        if (sistema.getTablero().getCelda(j, i).getEstadoCelda() == Celda.CERRADA_SERVIDOR) {
                            g.setColor(COLOR_SERVER);
                        } else if (sistema.getTablero().getCelda(j, i).getEstadoCelda() == Celda.CERRADA_CLIENTE) {
                            g.setColor(COLOR_SERVER);
                        }
                        g.fillRect(x1, y1, cuadro, cuadro);
                    } else {
                        g.setColor(Color.WHITE);
                    }

                    //System.out.print(sistema.getTablero().getCelda(j,i).getBordeCelda());
                }
                //System.out.println("");
            }
        } else {
            if (getSistema().getEstadoJugada() == 1){
                filas = getSistema().getTablero().getFilas();
                columnas = getSistema().getTablero().getColumnas();
                conectarCliente();
                getSistema().setSwich(true);
            }
            if (getSistema().getEstadoJugada() == 5){
                dibujarJugadaContricante();
            }
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
        getVentana().setSize( 300, 230);
        getVentana().getPnIni().setVisible(true);
        getVentana().getPnServer().setVisible(false);
        getVentana().getPnClient().setVisible(false);
        getVentana().getPnGame().setVisible(false);
        hiloDibujo = null;
    }

    public void CreateConn(){
        String nombre = getVentana().getTxfServerName().getText();
        if(nombre.equals(new String(""))){
            getVentana().mensajeAlerta("Debe ingresar un nombre primero");
        }
        else
        {
            getSistema().setSwich(true);
            getSistema().setNombreServidor(nombre);
            getSistema().setTipoUsuario(true);
            //getVentana().mensajeAlerta("Esperando que alguien se conecte");
            startGame();
            getSistema().ConexionServicio(sistema.getTipoUsuario());
            hiloDibujo = new Thread(this);
            hiloDibujo.start();
            getSistema().ConexionServicio(sistema.getTipoUsuario());
        }
    }

    public void startGame(){
        filas = Integer.parseInt(getVentana().getSpFilas().getValue().toString());
        columnas = Integer.parseInt(getVentana().getSpColumnas().getValue().toString());
        getSistema().setTablero(filas, columnas);
        reSize();
        getVentana().getPnIni().setVisible(false);
        getVentana().getPnServer().setVisible(false);
        getVentana().getPnClient().setVisible(false);
        getVentana().getPnGame().setVisible(true);
    }

    public void Connect() {
        String nombre = getVentana().getTxfClientName().getText();
        String ip= getVentana().getTxfIpGame().getText();
        if(ip.equals(new String(""))){
            getVentana().mensajeAlerta("Escriba una direccion ip");
        }
        else if (nombre.equals(new String(""))) {
            getVentana().mensajeAlerta("Escriba un nombre");
        }
        else
        {
            getSistema().setSwich(false);
            getSistema().setNombreCliente(nombre);
            getSistema().setIpServidor(ip);
            sistema.setTipoUsuario(false);
            hiloDibujo = new Thread(this);
            hiloDibujo.start();
            getSistema().ConexionServicio(sistema.getTipoUsuario());
        }
    }

    public void conectarCliente(){
        //TODO: capturar datos al conectarse
        reSize();
        getVentana().getPnIni().setVisible(false);
        getVentana().getPnServer().setVisible(false);
        getVentana().getPnClient().setVisible(false);
        getVentana().getPnGame().setVisible(true);
    }

    private void reSize(){

        if (columnas >= filas){
            cuadro = (270 - 3*columnas) / 8;
        }else {
            cuadro = (270 - 3*filas) / 8;
        }
        getVentana().setSize(((columnas + 1) * cuadro) + 170,
                ((filas + 2) * cuadro) + 20);
        getVentana().getCnvTablero().setBounds(10,10,
                (columnas * cuadro) + (MARGEN * 2),
                (filas * cuadro) + (MARGEN * 2)
        );
        getVentana().getBtnReturnIni3().setLocation(((columnas + 1) * cuadro) + 10,30);
    }

    public void Clicked(MouseEvent e) {
        int posX = (e.getX() - (MARGEN)) / cuadro;
        int posY = (e.getY() - (MARGEN)) / cuadro;
        int sensibilidad = 2;

        try {
            if(getSistema().getEstadoJugada() == 2){
                if (((posX * cuadro) + sensibilidad + MARGEN) >= e.getX() &&
                        (sistema.getTablero().getCelda(posY,posX).getBordeCelda() & Celda.IZQUIERDA) == 0) {
                    sistema.Jugar(posY,posX,3,sistema.getTipoUsuario());
                } else if (((posY * cuadro) + sensibilidad + MARGEN) >= e.getY() &&
                        (sistema.getTablero().getCelda(posY,posX).getBordeCelda() & Celda.ARRIBA) == 0) {
                    sistema.Jugar(posY,posX,0,sistema.getTipoUsuario());
                } else if (((posY * cuadro) + cuadro - sensibilidad + MARGEN) <= e.getY() &&
                        (sistema.getTablero().getCelda(posY,posX).getBordeCelda() & Celda.ABAJO) == 0) {
                    sistema.Jugar(posY,posX,2,sistema.getTipoUsuario());
                } else if (((posX * cuadro) + cuadro - sensibilidad + MARGEN) <= e.getX() &&
                        (sistema.getTablero().getCelda(posY,posX).getBordeCelda() & Celda.DERECHA) == 0) {
                    sistema.Jugar(posY,posX,1,sistema.getTipoUsuario());
                }
            }else {
                getVentana().mensajeAlerta("Esperando jugador");
            }
        }catch (ArrayIndexOutOfBoundsException err)
        {
            System.err.println(err);
        }
    }

    public void dibujarJugadaContricante() {
        int posX = getSistema().getFilaJugada();
        int posY = getSistema().getColumnaJugada();
        int sensibilidad = 2;

        try {
                sistema.Jugar(posY,posX,getSistema().getTrazoJugado(),sistema.getTipoUsuario());
        }catch (ArrayIndexOutOfBoundsException err)
        {
            System.err.println(err);
        }
    }
}
