package org.monopoly;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import org.monopoly.Casilla.TipoCasilla;
import org.monopoly.Casilla.TipoCasillaEspecial;
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

    private float premioVuelta;


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
    public Casilla getCarcel(){
        for(Casilla c: this.casillasPorTipos.get(TipoCasilla.ESPECIAL)){
            if(c.getTipoEspecial() == TipoCasillaEspecial.CARCEL){
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

    public void addJugador(Jugador jugador){
        this.jugadores.add(jugador);
        this.avatares.add(jugador.getAvatar());
    }
    public void anhadirFortunaInicial(){
        float fortunaInicial = getTotalSolares()/3;
        for(Jugador j: this.jugadores){
            j.setFortuna(fortunaInicial);
        }
    }
    private void anhadirBanca(){
        this.banca = new Jugador();
        for(Casilla c: this.casillas){
            this.banca.anhadirPropiedad(c);
        }
    }
    private void anhadirImpuesto(){
        float impuesto = this.premioVuelta;
        for(Casilla c : this.casillasPorTipos.get(TipoCasilla.IMPUESTOS)){
            c.setImpuestos(impuesto);
            impuesto /= 2;
        }
    }


    public float getPremioVuelta(){
        return premioVuelta;
    }
    public boolean esPartidaEmpezada(){
        return partidaEmpezada;
    }
    public boolean jugadorActualCarcel(){
        Avatar av = this.jugadorActual.getAvatar();
        return av.getCasilla().getTipo() == TipoCasilla.ESPECIAL && av.getCasilla().getTipoEspecial() == TipoCasillaEspecial.CARCEL;
    }

    public void empezarPartida(){
        this.partidaEmpezada = true;
        anhadirFortunaInicial();
        this.premioVuelta = getTotalSolares()/2;
        anhadirBanca();
        anhadirImpuesto();
        this.jugadorActual = this.jugadores.get(0);
    }
    public String comprar(Jugador j,String nombreCasilla){
        Casilla c = j.getAvatar().getCasilla();
        if(!c.getNombre().equals(nombreCasilla)){
            return "%s no puede comprar %s".formatted(j.getNombre(),nombreCasilla);
        }
        if(!c.getPropietario().getNombre().equals("Banca")){
            return "%s no puede comprar %s ya que pertenece a otro jugador.".formatted(j.getNombre(),nombreCasilla);
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
    public String lanzarDados(boolean debug){
        if(!this.jugadorActual.getAccion()){
            return "El jugador ya ha lanzado los dados";
        }
        this.jugadorActual.setAccion(false);
        Dado d = new Dado(); 

        int cantidad = debug ? d.debugLanzar() : d.lanzar();
        Avatar av = this.jugadorActual.getAvatar();
        if(av.getCasilla().getTipo() == TipoCasilla.ESPECIAL && av.getCasilla().getTipoEspecial() == TipoCasillaEspecial.CARCEL){
            int turnosCarcel = this.jugadorActual.getTurnosCarcel();
            if(turnosCarcel > 3){
                return "El jugador lleva más de tres turnos en la cárcel, por lo que debe pagar el precio de salir de la carcel o declararse en bancarrota.";
            }
            if(d.getFueronDobles()){
                return "El jugador ha sacado dados dobles, por lo que puede salir de la carcel." + mover(cantidad);
            }
            this.jugadorActual.setTurnosCarcel(turnosCarcel + 1);
            return "El jugador no ha sacado dados dobles, por lo que sigue en la carcel.";
        }
        if(d.getFueronDobles()){
            this.jugadorActual.setVecesDadosDobles(this.jugadorActual.getVecesDadosDobles() + 1);
            if(this.jugadorActual.getVecesDadosDobles() == 3){
                this.jugadorActual.getAvatar().setCasilla(this.getCarcel());
                return "El jugador ha sacado dados dobles tres veces, por lo que va a la carcel.";
            }
            this.jugadorActual.setAccion(true);
            return "El jugador ha sacado dados dobles, por lo tanto puede volver a lanzar los dados." + mover(cantidad);
        }
        this.jugadorActual.setVecesDadosDobles(0);
        return mover(cantidad);
    }

    public String mover(int cantidad){
        Jugador j = this.jugadorActual;
        Avatar av = j.getAvatar();
        Casilla oldCasilla = av.getCasilla();
        int oldPos = oldCasilla.getPos();
        int newPos = oldPos + cantidad;
        if( newPos > 39){
            //TODO VUELTA
            this.jugadorActual.anhadirFortuna(this.premioVuelta);
            newPos = newPos % 40;
        }
        Casilla newCasilla = this.casillas.get(newPos);

        String mensaje = String.format("El avatar %c avanza %d posiciones, desde %s hasta %s.",av.getId(),cantidad,oldCasilla.getNombre(),newCasilla.getNombre());
        if(newCasilla.getTipoEspecial() != null && newCasilla.getTipoEspecial() == TipoCasillaEspecial.IRCARCEL){
            av.setCasilla(this.getCarcel());
            return mensaje + "El avatar se coloca en la casilla Carcel.";
        }
        av.setCasilla(newCasilla);
        return mensaje + newCasilla.caer(j);
    }
    public String siguienteTurno(){
        if(this.jugadorActual.getAccion()){
            return "El jugador debe lanzar los dados antes de acabar su turno.";
        }
        int i = this.jugadores.indexOf(this.jugadorActual);
        this.jugadorActual = this.jugadores.get((i + 1)%this.jugadores.size());
        this.jugadorActual.setAccion(true);
        return "El jugador actual es %s".formatted(this.jugadorActual.getNombre());
    }

    public void run(){
        System.out.println("""
                Bienvenido al Monopoly!
                crea jugadores con 'crear jugador <nombre> <avatar>'
                y cuando ya tengas mas de dos jugadores escribe 'empezar'
                """);
        Prompt prompt = new Prompt();
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
    private float getTotalSolares(){
        float acumulado = 0;
        for(Casilla c : this.casillasPorTipos.get(TipoCasilla.SOLAR)){
            acumulado += c.getValor();
        }
        return acumulado;
    }

}
