package edu.ud.informatica.taller2.logica;

public
class Tablero {

    Celda[][] celda;

    int filas;
    int columnas;

    public Tablero(int filas, int columnas){

        this.filas = filas;
        this.columnas = columnas;

        Celda[][] celda = new Celda[this.filas][this.columnas];

        for(int i = 0 ; i < columnas ; i++) {
            for (int j = 0; j < filas; j++) {
                celda[i][j] = new Celda();
            }
        }
    }

    public void Jugar(int fila, int columna, int jugada, int jugador){

        celda[fila][columna].seleccionarBorde(jugada, jugador);

    }

    public
    int getFilas() {
        return filas;
    }

    public
    int getColumnas() {
        return columnas;
    }

    public
    Celda getCelda(int fila , int columna) {
        return celda[fila][columna];
    }
}
