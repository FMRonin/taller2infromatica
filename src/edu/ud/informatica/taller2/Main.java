package edu.ud.informatica.taller2;

import edu.ud.informatica.taller2.presentacion.Model;

public class Main {
    private Model modelo;

    public Main() {
        modelo = new Model();
        modelo.iniciar();
    }

    public static void main(String[] args) {
        new Main();
    }

}
