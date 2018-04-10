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
        btnConnect.addActionListener(getControlador());
        btnCreateConn.addActionListener(getControlador());
        btnReturnIni1.addActionListener(getControlador());
        btnReturnIni2.addActionListener(getControlador());
        btnReturnIni3.addActionListener(getControlador());
        cnvTablero.addMouseListener(getControlador());
    }

    private void initComponents() {

        //Crean los objetos que no varian dentro de la vista
        JLabel lbFilas;
        JLabel lbColumnas;
        JLabel lbServerName;
        JLabel lbClientName;
        JLabel lbIpGame;
        JLabel lbInicio;

        //Se limita el spinner desde la vista
        SpinnerModel spinnerModel1 = new SpinnerNumberModel(10,10,50,1);
        SpinnerModel spinnerModel2 = new SpinnerNumberModel(10,10,50,1);

        //Inicializan los objetos del panel de inicio
        pnIni = new JPanel();
        lbInicio = new JLabel();
        btnInitGame = new JButton();
        btnJoinGame = new JButton();

        //Inicializan los objetos del panel para conectarse como servidor
        pnServer = new JPanel();
        lbServerName = new JLabel();
        txfServerName = new JTextField();
        btnCreateConn = new JButton();
        btnReturnIni1 = new JButton();
        lbColumnas = new JLabel();
        spColumnas = new JSpinner(spinnerModel1);
        lbFilas = new JLabel();
        spFilas = new JSpinner(spinnerModel2);

        //Inicializan los objetos del panel para conectarse como cliente
        pnClient = new JPanel();
        lbClientName = new JLabel();
        txfClientName = new JTextField();
        lbIpGame = new JLabel();
        txfIpGame = new JTextField();
        btnReturnIni2 = new JButton();
        btnConnect = new JButton();

        //Inicializan los objetos del panel del juego
        pnGame = new JPanel();
        cnvTablero = new Canvas();
        btnEnviar = new JButton();
        rivalScore = new JLabel();
        myScore = new JLabel();
        btnReturnIni3 = new JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        //setLayout(new BorderLayout());


        /////////////////Configuración vista inicio

        add(pnIni);
        pnIni.setLayout(null);
        pnIni.setBackground(Color.WHITE);

        lbInicio.setText("Juego de Rayita");
        lbInicio.setFont(new Font("",Font.BOLD,20));
        pnIni.add(lbInicio);
        lbInicio.setBounds(0,20,300,30);
        lbInicio.setHorizontalAlignment(JLabel.CENTER);

        btnInitGame.setText("Iniciar Juego");
        pnIni.add(btnInitGame);
        btnInitGame.setBounds(10,100,120,30);

        btnJoinGame.setText("Unirme juego");
        pnIni.add(btnJoinGame);
        btnJoinGame.setBounds(150,100,120,30);

        ///////////////////Configuración vista server

        add(pnServer);
        pnServer.setLayout(null);
        pnServer.setBackground(Color.WHITE);
        pnServer.setVisible(false);

        lbServerName.setText("Nombre:");
        pnServer.add(lbServerName);
        lbServerName.setBounds(20,30,60,20);

        pnServer.add(txfServerName);
        txfServerName.setBounds(90,30,180,20);

        lbColumnas.setText("Columnas:");
        pnServer.add(lbColumnas);
        lbColumnas.setBounds(20,70,70,20);

        pnServer.add(spColumnas);
        spColumnas.setBounds(100,70,50,20);

        lbFilas.setText("Filas:");
        pnServer.add(lbFilas);
        lbFilas.setBounds(160,70,40,20);

        pnServer.add(spFilas);
        spFilas.setBounds(210,70,50,20);

        btnReturnIni1.setText("Regresar");
        pnServer.add(btnReturnIni1);
        btnReturnIni1.setBounds(20,120,100,30);

        btnCreateConn.setText("Conectar");
        pnServer.add(btnCreateConn);
        btnCreateConn.setBounds(150,120,100,30);

        //////////////////Configuración vista cliente

        add(pnClient);
        pnClient.setLayout(null);
        pnClient.setBackground(Color.WHITE);
        pnClient.setVisible(false);

        lbClientName.setText("Nombre:");
        pnClient.add(lbClientName);
        lbClientName.setBounds(20,60,60,20);

        pnClient.add(txfClientName);
        txfClientName.setBounds(90,60,180,20);

        lbIpGame.setText("IP:");
        pnClient.add(lbIpGame);
        lbIpGame.setBounds(20,30,60,20);

        pnClient.add(txfIpGame);
        txfIpGame.setBounds(90,30,180,20);

        btnReturnIni2.setText("Regresar");
        pnClient.add(btnReturnIni2);
        btnReturnIni2.setBounds(20,120,100,30);

        btnConnect.setText("Conectar");
        pnClient.add(btnConnect);
        btnConnect.setBounds(150,120,100,30);


        //////////////////Configuración vista juego

        add(pnGame);
        pnGame.setLayout(null);
        pnGame.setBackground(Color.WHITE);
        pnGame.setVisible(false);

        pnGame.add(cnvTablero);
        cnvTablero.setBackground(Color.GRAY);
        cnvTablero.setBounds(20,10,500,500);

        btnReturnIni3.setText("Regresar al Inicio");
        pnGame.add(btnReturnIni3);
        btnReturnIni3.setSize(150,40);

    }

    private JPanel pnIni;
    private JButton btnInitGame;
    private JButton btnJoinGame;
    private JPanel pnServer;
    private JTextField txfServerName;
    private JSpinner spColumnas;
    private JSpinner spFilas;
    private JButton btnReturnIni1;
    private JButton btnReturnIni2;
    private JButton btnReturnIni3;
    private JButton btnCreateConn;
    private JButton btnConnect;
    private JPanel pnClient;
    private JTextField txfClientName;
    private JTextField txfIpGame;
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

    public JPanel getPnIni() {
        return pnIni;
    }

    public JButton getBtnInitGame() {
        return btnInitGame;
    }

    public JTextField getTxfClientName() {
        return txfClientName;
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

    public JTextField getTxfIpGame() {
        return txfIpGame;
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

    public JButton getBtnReturnIni1() {
        return btnReturnIni1;
    }

    public JButton getBtnReturnIni2() {
        return btnReturnIni2;
    }

    public JButton getBtnReturnIni3() {
        return btnReturnIni3;
    }

    public JButton getBtnCreateConn() {
        return btnCreateConn;
    }

    public JButton getBtnConnect() {
        return btnConnect;
    }

    public void mensajeAlerta(String msg){ JOptionPane.showMessageDialog(null, msg);}
}
