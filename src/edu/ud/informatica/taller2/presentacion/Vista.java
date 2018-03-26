package edu.ud.informatica.taller2.presentacion;

import javax.swing.*;
import java.awt.*;

public
class Vista extends JFrame{
    private final Model modelo;
    private Controlador controlador;

    public Vista(Model modelo) {
        this.modelo = modelo;
        initComponents();
        asignarEventos();
    }

    private void asignarEventos() {
        btnEnviar.addActionListener(getControlador());
        btnInitGame.addActionListener(getControlador());
        btnJoinGame.addActionListener(getControlador());
    }

    private void initComponents() {

        JLabel lbFilas;
        JLabel lbColumnas;
        JLabel lbServerName;
        JLabel lbClientName;
        JLabel lbIpGame;
        JLabel lbInicio;

        lbInicio = new JLabel();
        btnInitGame = new JButton();
        btnJoinGame = new JButton();
        pnServer = new JPanel();
        lbServerName = new JLabel();
        txfServerName = new JTextField();
        lbColumnas = new JLabel();
        spColumnas = new JSpinner();
        lbFilas = new JLabel();
        spFilas = new JSpinner();
        pnClient = new JPanel();
        lbClientName = new JLabel();
        txfClientName = new JTextField();
        lbIpGame = new JLabel();
        cnvTablero = new Canvas();
        btnEnviar = new JButton();
        rivalScore = new JLabel();
        myScore = new JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);

        lbInicio.setText("Juego de Rayita");
        add(lbInicio);
        lbInicio.setBounds(80,50,100,20);

        btnInitGame.setText("Iniciar Juego");
        add(btnInitGame);
        btnInitGame.setBounds(10,100,130,20);

        btnJoinGame.setText("Unirme a un juego");
        add(btnJoinGame);
        btnJoinGame.setBounds(150,100,130,20);
    }

    private JButton btnInitGame;
    private JButton btnJoinGame;
    private JPanel pnServer;
    private JTextField txfServerName;
    private JSpinner spColumnas;
    private JSpinner spFilas;
    private JPanel pnClient;
    private JTextField txfClientName;
    private JTextField ipGame;
    private JPanel pnGame;
    private Canvas cnvTablero;
    private JButton btnEnviar;
    private JLabel rivalScore;
    private JLabel myScore;

    public Model getModelo() {
        return modelo;
    }

    public Controlador getControlador() {
        if(controlador == null)
        {
            controlador = new Controlador(this);
        }
        return controlador;
    }

    public JButton getBtnInitSeccion() {
        return btnInitGame;
    }

    public JButton getBtnJoinGame() {
        return btnJoinGame;
    }

    public JPanel getPnServer() {
        return pnServer;
    }

    public JTextField getTxfServerName() {
        return txfServerName;
    }

    public JSpinner getSpColumnas() {
        return spColumnas;
    }

    public JSpinner getSpFilas() {
        return spFilas;
    }

    public JPanel getPnClient() {
        return pnClient;
    }

    public JTextField getGetTxfClientName() {
        return txfClientName;
    }

    public JTextField getIpGame() {
        return ipGame;
    }

    public JPanel getPnGame() {
        return pnGame;
    }

    public Canvas getCnvTablero() {
        return cnvTablero;
    }

    public JButton getBtnEnviar() {
        return btnEnviar;
    }

    public JLabel getRivalScore() {
        return rivalScore;
    }

    public JLabel getMyScore() {
        return myScore;
    }
}
