package edu.ud.informatica.taller2.logica;

public
class Tablero {

    Celda[][] celda;

    private int filas;
    private int columnas;

    private int puntajeServido;
    private int puntajeCliente;

    public Tablero(int filas, int columnas){

        this.filas = filas;
        this.columnas = columnas;

        celda = new Celda[this.filas][this.columnas];

        for(int i = 0 ; i < columnas ; i++) {
            for (int j = 0; j < filas; j++) {
                celda[j][i] = new Celda();
            }
        }
    }

    public int Jugar(int fila, int columna, int jugada, boolean jugador){

        int jug = (jugador)? 1 : 2;

        int response = 0;

        try{

            switch (jugada){
                case 0: //ARRIBA
                    response = ((celda[fila][columna].getBordeCelda() & Celda.ARRIBA) <= 0)?0:-1;
                    celda[fila][columna].setBordeCelda(((celda[fila][columna].getBordeCelda() & Celda.ARRIBA) <= 0) ?
                            (Celda.ARRIBA + celda[fila][columna].getBordeCelda()) : celda[fila][columna].getBordeCelda());
                    if (fila > 0) {
                        celda[fila - 1][columna].setBordeCelda(((celda[fila - 1][columna].getBordeCelda() & Celda.ABAJO) <= 0) ?
                                (Celda.ABAJO + celda[fila - 1][columna].getBordeCelda()) : celda[fila - 1][columna].getBordeCelda());
                        if((celda[fila - 1][columna].getBordeCelda() & Celda.LLENA) == Celda.LLENA){
                            celda[fila - 1][columna].setEstadoCelda(jug);
                            response = jug;
                        }
                    }
                    if((celda[fila][columna].getBordeCelda() & Celda.LLENA) == Celda.LLENA){
                        celda[fila][columna].setEstadoCelda(jug);
                        response = jug;
                    }
                    break;
                case 1: //DERECHA
                    response = ((celda[fila][columna].getBordeCelda() & Celda.DERECHA) <= 0)?0:-1;
                    celda[fila][columna].setBordeCelda(((celda[fila][columna].getBordeCelda() & Celda.DERECHA) > 0) ?
                            celda[fila][columna].getBordeCelda() : (Celda.DERECHA + celda[fila][columna].getBordeCelda()));
                    if (columna < columnas - 1) {
                        celda[fila][columna + 1].setBordeCelda(((celda[fila][columna + 1].getBordeCelda() & Celda.IZQUIERDA) <= 0) ?
                                (Celda.IZQUIERDA + celda[fila][columna + 1].getBordeCelda()) : celda[fila][columna + 1].getBordeCelda());
                        if((celda[fila][columna + 1].getBordeCelda() & Celda.LLENA) == Celda.LLENA){
                            celda[fila][columna + 1].setEstadoCelda(jug);
                            response = jug;
                        }
                    }
                    if((celda[fila][columna].getBordeCelda() & Celda.LLENA) == Celda.LLENA){
                        celda[fila][columna].setEstadoCelda(jug);
                        response = jug;
                    }
                    break;
                case 2: //ABAJO
                    response = ((celda[fila][columna].getBordeCelda() & Celda.ABAJO) <= 0)?0:-1;
                    celda[fila][columna].setBordeCelda(((celda[fila][columna].getBordeCelda() & Celda.ABAJO) > 0) ?
                            celda[fila][columna].getBordeCelda() : (Celda.ABAJO + celda[fila][columna].getBordeCelda()));
                    if (fila > filas - 1) {
                        celda[fila + 1][columna].setBordeCelda(((celda[fila + 1][columna].getBordeCelda() & Celda.ARRIBA) <= 0) ?
                                (Celda.ARRIBA + celda[fila + 1][columna].getBordeCelda()) : celda[fila + 1][columna].getBordeCelda());
                        if((celda[fila + 1][columna].getBordeCelda() & Celda.LLENA) == Celda.LLENA){
                            celda[fila + 1][columna].setEstadoCelda(jug);
                            response = jug;
                        }
                    }
                    if((celda[fila][columna].getBordeCelda() & Celda.LLENA) == Celda.LLENA){
                        celda[fila][columna].setEstadoCelda(jug);
                        response = jug;
                    }
                    break;
                case 3: //IZQUIERDA
                    response = ((celda[fila][columna].getBordeCelda() & Celda.IZQUIERDA) <= 0)?0:-1;
                    celda[fila][columna].setBordeCelda (((celda[fila][columna].getBordeCelda() & Celda.IZQUIERDA) > 0) ?
                            celda[fila][columna].getBordeCelda() : (Celda.IZQUIERDA + celda[fila][columna].getBordeCelda()));
                    if (columna > 0) {
                        celda[fila][columna - 1].setBordeCelda(((celda[fila][columna - 1].getBordeCelda() & Celda.DERECHA) <= 0) ?
                                (Celda.DERECHA + celda[fila][columna - 1].getBordeCelda()) : celda[fila][columna - 1].getBordeCelda());
                        if((celda[fila][columna - 1].getBordeCelda() & Celda.LLENA) == Celda.LLENA){
                            celda[fila][columna - 1].setEstadoCelda(jug);
                            response = jug;
                        }
                    }
                    if((celda[fila][columna].getBordeCelda() & Celda.LLENA) == Celda.LLENA){
                        celda[fila][columna].setEstadoCelda(jug);
                        response = jug;
                    }
                    break;
            }

            puntajeCliente = contarCeldasCliente();
            puntajeServido = contarCeldasServidor();

        }catch (ArrayIndexOutOfBoundsException err){
            response = -2;
        }
        return response;
    }

    public int contarCeldasServidor(){
        int celdasServidor = 0;
        for(int i = 0 ; i < columnas ; i++) {
            for (int j = 0; j < filas; j++) {
                celdasServidor = (celda[j][i].getEstadoCelda() == Celda.CERRADA_SERVIDOR)? celdasServidor + 1 : celdasServidor;
            }
        }
        return celdasServidor;
    }

    public int contarCeldasCliente(){
        int celdasCliente = 0;
        for(int i = 0 ; i < columnas ; i++) {
            for (int j = 0; j < filas; j++) {
                celdasCliente = (celda[j][i].getEstadoCelda() == Celda.CERRADA_CLIENTE)? celdasCliente + 1 : celdasCliente;
            }
        }
        return celdasCliente;
    }

    public boolean estaFinalizado(){
        return contarCeldasCliente() + contarCeldasServidor() == filas * columnas;
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

    public
    int getPuntajeServido() {
        return puntajeServido;
    }

    public
    int getPuntajeCliente() {
        return puntajeCliente;
    }
}
