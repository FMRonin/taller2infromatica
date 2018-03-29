package edu.ud.informatica.taller2.presentacion;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public
class Controlador implements ActionListener, MouseListener {

    private final Vista ventana;
    private Model modelo;

    public Controlador(Vista vista) {
        ventana = vista;
        modelo = ventana.getModelo();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn;
        btn = (JButton) e.getSource();

        if (btn == ventana.getBtnInitGame()){
            modelo.StartAsServer();
        }else if (btn == ventana.getBtnJoinGame()){
            modelo.StartAsClient();
        }else if (btn == ventana.getBtnReturnIni1() ||
                btn == ventana.getBtnReturnIni2() ||
                btn == ventana.getBtnReturnIni3()){
            modelo.ReturnIni();
        }else if (btn == ventana.getBtnConnect()){
            modelo.Connect();
        }else if (btn == ventana.getBtnCreateConn()){
            modelo.CreateConn();
        }
    }

    @Override
    public
    void mouseClicked(MouseEvent e) {
        modelo.Clicked(e);
    }

    @Override
    public
    void mousePressed(MouseEvent e) {
        //modelo.Pressed();
    }

    @Override
    public
    void mouseReleased(MouseEvent e) {
        //modelo.Released();
    }

    @Override
    public
    void mouseEntered(MouseEvent e) {
        //modelo.Entered();
    }

    @Override
    public
    void mouseExited(MouseEvent e) {
        //modelo.Exited();
    }
}
