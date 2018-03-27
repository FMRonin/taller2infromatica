package edu.ud.informatica.taller2.presentacion;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public
class Controlador implements ActionListener {

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
                btn == ventana.getBtnReturnIni2()){
            modelo.ReturnIni();
        }else if (btn == ventana.getBtnConnect()){
            modelo.Connect();
        }else if (btn == ventana.getBtnCreateConn()){
            modelo.CreateConn();
        }
    }
}
