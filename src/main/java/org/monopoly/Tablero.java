package org.monopoly;

import java.util.ArrayList;

public class Tablero{
    private ArrayList<Casilla> casillas;

    Tablero(){
        this.casillas = new ArrayList<>();
        for(int i = 0; i< 40; i++){
            this.casillas.add(new Casilla());
        }
    }

    @Override
    public String toString() {
        String result = new String();
        this.casillas.forEach((casilla)->result.concat("|" +casilla.getNombre() +  "|\n"));
        return result;
    }
}
