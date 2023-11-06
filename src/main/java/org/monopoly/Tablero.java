package org.monopoly;

import java.util.ArrayList;

// Se definen las características del tablero
public class Tablero{
    private ArrayList<Casilla> casillas;
    
    public static final int ANCHO_TABLERO = 11;
    public static final int ALTURA_TABLERO = 11;
    
    Tablero(ArrayList<Casilla> casillas){
        this.casillas = casillas;
    }
    @Override
    public String toString() {
        
        //String que contiene el tablero
        String result = "";

        // Primero busca la longitud de la palabra mas larga
        int maxWordLen = this.casillas.get(0).getNombre().length();
        int numMaximoAvatares = 0;
        for (Casilla c: this.casillas){
            if (numMaximoAvatares < c.getCantidadAvataresEnCasilla()){
                numMaximoAvatares = c.getCantidadAvataresEnCasilla();
            }
            if (c.getNombre().length() > maxWordLen){
                maxWordLen = c.getNombre().length();
            }
        }

        int maxLen = numMaximoAvatares == 0 ? maxWordLen+1: maxWordLen + 1 + 1 + numMaximoAvatares;

        //Añade al tablero la primera fila que es el borde superior del tablero
        String celing = new ColorString(" ".repeat(maxLen)) + " ";
        result += " " + celing.repeat(ANCHO_TABLERO) + " \n";

        //Cuenta que linea esta pintando del tablero
        int i = 0;
        
        //Añade la primera fila del tablero
        result += '|';
        while( i < ANCHO_TABLERO){
            Casilla c = this.casillas.get(i);
            result += c.toString(maxWordLen,numMaximoAvatares);
            i+=1;
        }
        result += "\n";
        
        //Añade las dos columnas del tablero hasta la ultima fila
        while( i < ANCHO_TABLERO + 2*(ALTURA_TABLERO-2)){
            Casilla c1 = this.casillas.get(i);
            Casilla c2 = this.casillas.get(i+1);
            result += "|" + c1.toString(maxWordLen,numMaximoAvatares);
            if(i + 2 ==  ANCHO_TABLERO + 2*(ALTURA_TABLERO-2)){

                result += celing.repeat(ANCHO_TABLERO - 2).strip();
            }else {
                result += " ".repeat(maxLen * (ANCHO_TABLERO - 2) + ANCHO_TABLERO-3);
            }
            result += "|" +  c2.toString(maxWordLen,numMaximoAvatares)+ "\n";
            
            i+=2;
        }
        
        // Añade la ultima fila del tablero
        result += '|';
        while( i < 2*ALTURA_TABLERO + 2*(ALTURA_TABLERO-2)){
            Casilla c = this.casillas.get(i);
            result += c.toString(maxWordLen,numMaximoAvatares);
            i+=1;
        }
        return result;
    }
}
