package org.monopoly;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Monopoly{
    public static final int NCASILLAS = 40;

    private ArrayList<Jugador> jugadores;
    //TODO cambiar por un hashmap de clase COLOR y valor ARRAYLIST<Casilla>
    private ArrayList<Casilla> casillas;
    private ArrayList<Avatar> avatares;
    private Tablero tablero;
    private Jugador jugadorActual;
    Monopoly(File f){
        try {
            Scanner sc = new Scanner(f);
            
            ArrayList<Casilla> casillas = new ArrayList<>(NCASILLAS);
            Comparator<Casilla> comparador = Comparator.comparingInt(Casilla::getPos);
            
            int i = 0;
            while(sc.hasNextLine()){
                String line = sc.nextLine();
                Casilla nuevaCasilla = new Casilla(line,posArrayAPosTablero(i));
                casillas.add(nuevaCasilla);
                i++;
            }
            
            this.jugadores = new ArrayList<>();
            this.avatares = new ArrayList<>();
            this.tablero = new Tablero(casillas);
            
            this.casillas = new ArrayList<>(casillas);
            this.casillas.sort(comparador);
            
            sc.close();
        }
        catch(FileNotFoundException e){
            System.out.println("An error occurred");
        }

    }
    public Tablero getTablero(){
        return this.tablero;
    }
    public Casilla getSalida(){
        for(Casilla c : this.casillas){
            if(c.esSalida()){
                return c;
            }
        }
        return null;
    }

    public ArrayList<Jugador> getJugadores(){
        return this.jugadores;
    }
    public ArrayList<Avatar> getAvatares(){
        return this.avatares;
    }
    public Casilla getCasillaPorNombre(String nombre){
        return this.casillas.stream().filter(c-> c.getNombre().equals(nombre)).findFirst().orElse(null);
    }
    public Jugador getJugadorActual(){
        return this.jugadorActual;
    }



    public void setJugador(Jugador jugador){
        this.jugadores.add(jugador);
    }
    public void setAvatar(Avatar avatar){
        this.avatares.add(avatar);
    }

    public ArrayList<Casilla> getCasillasPorColor(ColorString.Color color){
        return this.casillas.stream().filter(c -> c.getGrupo().compareTo(color) == 0).collect(Collectors.toCollection(ArrayList::new));
    }
    public boolean tieneTodosColor(Jugador jugador,ColorString.Color color){

        if(jugador.getPropiedades().size() < 3){
            return false;
        }
        ArrayList<Casilla> casillas = getCasillasPorColor(color);
        for(Casilla c : casillas){
            if(!jugador.getPropiedades().contains(c)){
                return false;
            }
        }
        return true;
    }
    public void mover(int cantidad){
        Avatar av = this.jugadorActual.getAvatar();
        int oldPos = av.getCasilla().getPos();
        int newPos = oldPos + cantidad;
        Casilla newCasilla = this.casillas.get(newPos);

        av.setCasilla(newCasilla);
    }
    public void siguienteTurno(){
        int i = this.jugadores.indexOf(this.jugadorActual);
        this.jugadorActual = this.jugadores.get((i + 1)%this.jugadores.size());
    }

    public void run(){
        Prompt prompt = new Prompt();
        prompt.preguntarNuevoJugador(this);
        prompt.preguntarNuevoJugador(this);
        this.jugadorActual = this.jugadores.get(0);
        prompt.run(this);
    }

    private int posArrayAPosTablero(int i){
        if(i <= 10){
            return i+ 20;
        }
        if(i < 30){
            return i % 2 != 0 ? 19-(i-11)/2 : 31 + (i-12)/2;
        }
        return 39-i;
    }

}
