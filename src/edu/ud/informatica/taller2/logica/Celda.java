package edu.ud.informatica.taller2.logica;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

public
class Celda {

    private int bordeCelda;
    private int estadoCelda;

    // Estados del borde
    static public final int ARRIBA = 0b1;
    static public final int DERECHA = 0b10;
    static public final int ABAJO = 0b100;
    static public final int IZQUIERDA = 0b1000;
    static public final int LLENA = 0b1111;

    // Estado Celda
    static public final int CERRADA_CLIENTE = 1;
    static public final int CERRADA_SERVIDOR = 2;

    public Celda(){
        bordeCelda = 0;
        estadoCelda = 0;
    }

    public void seleccionarBorde(int borde, int jugador){
        switch (borde){
            case 0:
                bordeCelda = ((bordeCelda & ARRIBA) > 0) ? bordeCelda : ARRIBA | bordeCelda;
                break;
            case 1:
                bordeCelda = ((bordeCelda & DERECHA) > 0) ? bordeCelda : DERECHA | bordeCelda;
                break;
            case 2:
                bordeCelda = ((bordeCelda & ABAJO) > 0) ? bordeCelda : ABAJO | bordeCelda;
                break;
            case 3:
                bordeCelda = ((bordeCelda & IZQUIERDA) > 0) ? bordeCelda : IZQUIERDA | bordeCelda;
                break;
        }

        if((bordeCelda & 0b1111) == 0b1111){
            estadoCelda = jugador;
        }
    }

    public
    int getBordeCelda() {
        return bordeCelda;
    }

    public
    int getEstadoCelda() {
        return estadoCelda;
    }

    public
    void setBordeCelda(int bordeCelda) {
        this.bordeCelda = bordeCelda;
    }

    public
    void setEstadoCelda(int estadoCelda) {
        this.estadoCelda = estadoCelda;
    }
}
