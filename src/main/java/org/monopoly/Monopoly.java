package org.monopoly;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import org.monopoly.Casilla.TipoCasilla;
import org.monopoly.ColorString.Color;

public class Monopoly{
    public static final int NCASILLAS = 40;

    private ArrayList<Jugador> jugadores;
    private ArrayList<Casilla> casillas;
    private ArrayList<Avatar> avatares;
    private HashMap<Color,ArrayList<Casilla>> casillasPorColores;
    private HashMap<TipoCasilla,ArrayList<Casilla>> casillasPorTipos;
   
    private Tablero tablero;
    private Jugador jugadorActual;
    private Jugador banca;
    private boolean partidaEmpezada;
    Monopoly(File f){
        try {
            Scanner sc = new Scanner(f);

            ArrayList<Casilla> casillas = new ArrayList<>(NCASILLAS);
            Comparator<Casilla> comparador = Comparator.comparingInt(Casilla::getPos);
            this.casillasPorColores = new HashMap<>();
            this.casillasPorTipos = new HashMap<>();
            for(Color c: Color.values()){
                this.casillasPorColores.put(c,new ArrayList<>());
            }
            for(TipoCasilla c: TipoCasilla.values()){
                this.casillasPorTipos.put(c,new ArrayList<>());
            }

            int i = 0;
            while(sc.hasNextLine()){
                String line = sc.nextLine();
                Casilla nuevaCasilla = new Casilla(line,posArrayAPosTablero(i));
                casillas.add(nuevaCasilla);
                if(nuevaCasilla.getGrupo() != null){
                    this.casillasPorColores.get(nuevaCasilla.getGrupo()).add(nuevaCasilla);
                }
                this.casillasPorTipos.get(nuevaCasilla.getTipo()).add(nuevaCasilla);
                i++;
            }

            this.jugadores = new ArrayList<>();
            this.avatares = new ArrayList<>();
            this.tablero = new Tablero(casillas);
            this.partidaEmpezada = false;
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
    public float getFortunaInicial(){
        float acumulado = 0;
        for(Casilla c : this.casillasPorTipos.get(TipoCasilla.SOLAR)){
            acumulado += c.getValor();
        }
        return acumulado/3;
    }

    public void anhadirFortunaInicial(){
        float fortunaInicial = getFortunaInicial();
        for(Jugador j: this.jugadores){
            j.setFortuna(fortunaInicial);
        }
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
    public boolean esPartidaEmpezada(){
        return partidaEmpezada;
    }
    public void empezarPartida(){
        this.partidaEmpezada = true;
        anhadirFortunaInicial();
        anhadirBanca();
        this.jugadorActual = this.jugadores.get(0);
    }
    public String comprar(Jugador j,String nombreCasilla){
        Casilla c = j.getAvatar().getCasilla();
        if(!c.getNombre().equals(nombreCasilla)){
            return "%s no puede comprar %s".formatted(j.getNombre(),nombreCasilla);
        }

        if(c.getTipo() != TipoCasilla.SOLAR && c.getTipo() != TipoCasilla.TRANSPORTE && c.getTipo() != TipoCasilla.TRANSPORTE){
            return "%s no puede comprar %s porque es una casilla de tipo %s".formatted(j.getNombre(),nombreCasilla,c.getTipo());
        }
        if(j.getFortuna() < c.getValor()){
            return "%s no puede comprar %s, ya que tiene %f€ y necesita por lo menos %f€".formatted(j.getNombre(),nombreCasilla,j.getFortuna(),c.getValor());
        }
        j.setFortuna(j.getFortuna() - c.getValor());
        c.setPropietario(j);
        return "El jugador %s compra la casilla %s por %f€. Su fortuna actual es %f€.".formatted(j.getNombre(),c.getNombre(),c.getValor(),j.getFortuna());

    }


    public String mover(int cantidad){
        this.jugadorActual.setAccion(false);
        Avatar av = this.jugadorActual.getAvatar();
        Casilla oldCasilla = av.getCasilla();
        int oldPos = oldCasilla.getPos();
        int newPos = oldPos + cantidad;
        Casilla newCasilla = this.casillas.get(newPos);

        av.setCasilla(newCasilla);

        return String.format("El avatar %c avanza %d posiciones, desde %s hasta %s",av.getId(),cantidad,oldCasilla.getNombre(),newCasilla.getNombre());
    }
    public void siguienteTurno(){
        int i = this.jugadores.indexOf(this.jugadorActual);
        this.jugadorActual = this.jugadores.get((i + 1)%this.jugadores.size());
        this.jugadorActual.setAccion(true);
    }

    public void run(){
        Prompt prompt = new Prompt();
        prompt.run(this);
    }
    private void anhadirBanca(){
        this.banca = new Jugador();
        for(Casilla c: this.casillas){
            this.banca.anhadirPropiedad(c);
        }
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
