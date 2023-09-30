package org.monopoly;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
public class Monopoly{
    private HashMap<String,Jugador> jugadores;
    private ArrayList<Casilla> casillas;
    private ArrayList<Avatar> avatares;
    /**
     *  Crea un objeto Monopoly a partir de un archivo. Recupera las casillas del archivo con el siguiente formato:
     *  <Nombre de la casilla> <Tipo de casilla> <Grupo al que pertenece>
     *
     *  @param  f   archivo del que se va a crear el objeto
     *  @return     objeto Monopoly con las casillas especificadas en el archivo
     *
     */
    Monopoly(File f){
        try {
            Scanner sc = new Scanner(f);
            this.casillas = new ArrayList<>();
            
            while(sc.hasNextLine()){
                String line = sc.nextLine();
                Casilla nuevaCasilla = new Casilla(line);
                this.casillas.add(nuevaCasilla);
            }
            sc.close();
        }
        catch(FileNotFoundException e){
            System.out.println("An error occurred");
            e.printStackTrace();
        }

    }

    /**
     *  @return devuelve una tablero con las casillas del objeto Monopoly
     */
    public Tablero getTablero(){
        return new Tablero(this.casillas);
    }
    /**
     *  @return devuelve la casilla de salida, si es que existe
     */
    public Casilla getSalida(){
        for(Casilla c : this.casillas){
            if(c.esSalida()){
                return c;
            }
        }
        return null;
    }
    /** 
     *  @summary Añade un nuevo jugador
     * */
    public void setJugador(Jugador jugador){
        this.jugadores.put(jugador.getNombre(),jugador);
    }
    /**
     *  @summary Añade un nuevo avatar
     * */
    public void setAvatar(Avatar avatar){
        this.avatares.add(avatar);
    }

    /**
     *
     *  Empieza el juego
     *
     * */
    public void run(){
        Prompt prompt = new Prompt();
        prompt.run(this);
    }
}
