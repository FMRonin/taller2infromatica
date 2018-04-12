package edu.ud.informatica.taller2.presentacion;

import edu.ud.informatica.taller2.logica.Sistema;

import java.awt.*;
import java.awt.event.MouseEvent;

public
class Model implements Runnable{

    static private final int  MARGEN = 2;
    static private final int ARRIBA = 0b1;
    static private final int DERECHA = 0b10;
    static private final int ABAJO = 0b100;
    static private final int IZQUIERDA = 0b1000;
    static private final int TURNO_SERVER = 0b0;
    static private final int TURNO_CLIENT = 0b10000;
    static private final Color COLOR_SERVER = Color.BLUE;
    static private final Color COLOR_CLIENT = Color.GREEN;

    private Sistema sistema;
    private Vista ventana;
    private Thread hiloDibujo;

    private boolean tipoUser;
    private int filas;
    private int columnas;
    private int cuadro;
    int[][] celdas;
    private int turno = TURNO_SERVER;
    private int puntajeServer = 0;
    private int puntajeCliente = 0;

    private Graphics2D g;
    private Color colorJugador = COLOR_SERVER;




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
                    if(tipoUser)
                    {
                        hiloDibujo.wait(500);
                        dibujarCuadros();
                    }
                    else{

                    }
                }
            } catch (InterruptedException e) {
                //e.printStackTrace();
            } catch (NullPointerException e){

            }
        }
    }

    private void dibujarCuadros() {

        int thickness = 1;

        g = (Graphics2D) getVentana().getCnvTablero().getGraphics();
        g.setStroke(new BasicStroke(thickness));

        for(int i = 0 ; i < columnas ; i++) {
            for(int j = 0 ; j < filas ; j++){
                int x1 = (i * cuadro) + MARGEN;
                int y1 = (j * cuadro) + MARGEN;

                if((celdas[i][j] & ARRIBA) > 0){
                    g.setColor(Color.RED);
                }else { g.setColor(Color.WHITE); }
                g.drawLine(x1,y1,x1 + cuadro,y1);

                if((celdas[i][j] & DERECHA) > 0){
                    g.setColor(Color.RED);
                }else { g.setColor(Color.WHITE); }
                g.drawLine(x1 + cuadro, y1,x1 + cuadro,y1 + cuadro);

                if((celdas[i][j] & ABAJO) > 0){
                    g.setColor(Color.RED);
                }else { g.setColor(Color.WHITE); }
                g.drawLine(x1, y1 + cuadro,x1 + cuadro,y1 + cuadro);

                if((celdas[i][j] & IZQUIERDA) > 0){
                    g.setColor(Color.RED);
                }else { g.setColor(Color.WHITE); }
                g.drawLine(x1, y1,x1,y1 + cuadro);

                if((celdas[i][j] & 0b1111) == 0b1111){
                    if ((celdas[i][j] & 0b10000) == TURNO_SERVER) {
                        g.setColor(COLOR_SERVER);
                    }else {
                        g.setColor(COLOR_SERVER);
                    }
                    g.fillRect(x1, y1, cuadro,cuadro);
                }else { g.setColor(Color.WHITE); }

                System.out.print(celdas[i][j]);
            }
            System.out.println("");
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
            tipoUser = true;
            getSistema().setNombreServidor(nombre);
            getVentana().mensajeAlerta("Esperando que alguien se conecte");
            getSistema().ConexionServicio(tipoUser);
            startGame();
            hiloDibujo = new Thread(this);
            hiloDibujo.start();
        }
    }

    public void startGame(){
        filas = Integer.parseInt(getVentana().getSpFilas().getValue().toString());
        columnas = Integer.parseInt(getVentana().getSpColumnas().getValue().toString());
        getSistema().setTablero(filas, columnas);
        celdas = new int[columnas][filas];
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
            tipoUser = false;
            getSistema().ConexionServicio(tipoUser);
            hiloDibujo = new Thread(this);
            hiloDibujo.start();
        }
    }

    public void conectarCliente(){
        //TODO: capturar datos al conectarse
        filas = 50;
        columnas = 50;
        celdas = new int[columnas][filas];
        reSize();
        getVentana().getPnIni().setVisible(false);
        getVentana().getPnServer().setVisible(false);
        getVentana().getPnClient().setVisible(false);
        getVentana().getPnGame().setVisible(true);
        hiloDibujo = new Thread(this);
        hiloDibujo.start();
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
                if (((posX * cuadro) + sensibilidad + MARGEN) >= e.getX() && (celdas[posX][posY] & IZQUIERDA) == 0) {
                    celdas[posX][posY] += IZQUIERDA;
                    if (posX > 0) {
                        celdas[posX - 1][posY] += DERECHA;
                    }
                } else if (((posY * cuadro) + sensibilidad + MARGEN) >= e.getY() && (celdas[posX][posY] & ARRIBA) == 0) {
                    celdas[posX][posY] += ARRIBA;
                    if (posY > 0) {
                        celdas[posX][posY - 1] += ABAJO;//sistem.getTablero.ModificarCelda(posX,posY-1,sistema.turno)
                    }
                } else if (((posY * cuadro) + cuadro - sensibilidad + MARGEN) <= e.getY() && (celdas[posX][posY] & ABAJO) == 0) {
                    celdas[posX][posY] += ABAJO;
                    if (posY < filas - 1) {
                        celdas[posX][posY + 1] += ARRIBA;
                    }
                } else if (((posX * cuadro) + cuadro - sensibilidad + MARGEN) <= e.getX() && (celdas[posX][posY] & DERECHA) == 0) {
                    celdas[posX][posY] += DERECHA;
                    if (posX < columnas - 1) {
                        celdas[posX + 1][posY] += IZQUIERDA;
                    }
                }
            }else {
                getVentana().mensajeAlerta("Esperando jugador");
            }
        }catch (ArrayIndexOutOfBoundsException err)
        {
            System.err.println(err);
        }
    }
}
