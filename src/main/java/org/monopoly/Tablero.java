package org.monopoly;

import java.util.ArrayList;
public class Tablero{
    private ArrayList<Casilla> casillas;
    
    // Estos son constantes que no se donde poner
    public int ANCHO_TABLERO = 11;
    public int ALTURA_TABLERO = 11;
    
    /** 
     *
     *  Crea un nuevo tablero
     *  @param casillas Casillas que forman el tablero
     * */
    Tablero(ArrayList<Casilla> casillas){
        this.casillas = casillas;
    }


    //TODO mejorar el codigo
    @Override
    public String toString() {
        
        //String que contiene el tablero
        String result = new String();
        
        // Primero busca la longitud de la palabra mas larga
        int maxLen = this.casillas.get(0).getNombre().length();
        for (Casilla c: this.casillas){
            if(c.getNombre().length() > maxLen){
                maxLen = c.getNombre().length();
            }
        }
        
        //Añade al tablero la primera fila que es el border superior del tablero
        String celing = new ColorString(" ".repeat(maxLen + 6)).underline() + " ";
        result += " " + celing.repeat(ANCHO_TABLERO) + " \n";

        //Cuenta que linea esta pintando del tablero
        int i = 0;
        
        //Añade la primera fila del tablero
        result += '|';
        while( i < ANCHO_TABLERO){
            Casilla c = this.casillas.get(i);
            
            int span = maxLen - c.getNombre().length();
            String spanSpaces = " ".repeat(span + 6);
            result += new ColorString(c.getNombre() + spanSpaces).setForeground(c.getGrupo()).underline() + "|";
            
            i+=1;
        }
        result += "\n";
        
        //Añade las dos columnas del tablero hasta la ultima fila
        while( i < ANCHO_TABLERO + 2*(ALTURA_TABLERO-2)){
            Casilla c1 = this.casillas.get(i);
            Casilla c2 = this.casillas.get(i+1);

            int span1 = maxLen - c1.getNombre().length();
            int span2 = maxLen - c2.getNombre().length();

            String span1Spaces = " ".repeat(span1 + 6);
            String span2Spaces = " ".repeat(span2 + 6);

            result += "|" + new ColorString(c1 + span1Spaces).setForeground(c1.getGrupo()).underline() + "|";
            if(i + 2 ==  ANCHO_TABLERO + 2*(ALTURA_TABLERO-2)){

                result += celing.repeat(ANCHO_TABLERO - 2).strip();
            }else {
                result += " ".repeat((maxLen + 7)*9 - 1);
            }
            result += "|" +  new ColorString(c2 + span2Spaces).setForeground(c2.getGrupo()).underline() + "|\n";
            
            i+=2;
        }
        
        // Añade la ultima fila del tablero
        result += '|';
        while( i < 2*ALTURA_TABLERO + 2*(ALTURA_TABLERO-2)){
            Casilla c = this.casillas.get(i);
            int span = maxLen - c.getNombre().length();
            String spanSpaces = " ".repeat(span + 6);
            result += new ColorString(c.getNombre() + spanSpaces).setForeground(c.getGrupo()).underline() + "|";
            i+=1;
        }
        return result;
    }
}
