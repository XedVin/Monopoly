package org.monopoly;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import org.monopoly.Avatar.TipoAvatar;
import org.monopoly.Casilla.TipoCasilla;
import org.monopoly.Casilla.TipoCasillaEspecial;
import org.monopoly.ColorString.Color;
import org.monopoly.Edificio.TipoEdificio;

public class Monopoly{
    public static final int NCASILLAS = 40;
    public static final int NJUGADORES = 6;

    private Map<String,Jugador> jugadores;
    private ArrayList<Casilla> casillas;
    private Map<Character,Avatar> avatares;
    private HashMap<Color,ArrayList<Casilla>> casillasPorColores;
    private HashMap<TipoCasilla,ArrayList<Casilla>> casillasPorTipos;

    private Queue<String> colajugadores;

    private Tablero tablero;
    private Jugador banca;
    private boolean partidaEmpezada;

    private float premioVuelta;


    Baraja baraja;


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
                String[] args = line.split(" ");
                Casilla nuevaCasilla;
                if(args.length == 2){
                    nuevaCasilla = new Casilla(args[0],args[1],posArrayAPosTablero(i));
                }else if(args.length == 3){
                    nuevaCasilla = new Casilla(args[0],args[1],args[2],posArrayAPosTablero(i));
                }else{
                    nuevaCasilla = new Casilla(args[0],args[1],args[2],Float.parseFloat(args[3]),posArrayAPosTablero(i));
                }
                casillas.add(nuevaCasilla);
                if(nuevaCasilla.getGrupo() != null){
                    this.casillasPorColores.get(nuevaCasilla.getGrupo()).add(nuevaCasilla);
                }
                this.casillasPorTipos.get(nuevaCasilla.getTipo()).add(nuevaCasilla);
                i++;
            }

            this.jugadores = new HashMap<>();
            this.avatares = new HashMap<>();
            this.tablero = new Tablero(casillas);
            this.partidaEmpezada = false;
            this.casillas = new ArrayList<>(casillas);
            this.casillas.sort(comparador);
            this.colajugadores = new ArrayDeque<>(NJUGADORES);
            this.baraja = new Baraja();
            sc.close();
        }
        catch(FileNotFoundException e){
            System.out.println("No se pudo crear el tablero");
            System.exit(1);
        }

    }

    //GETTERS
    public Tablero getTablero(){
        return this.tablero;
    }
    public Jugador getBanca(){
        return this.banca;
    }
    public Casilla getSalida(){
        for(Casilla c : this.casillasPorTipos.get(TipoCasilla.ESPECIAL)){
            if(c.esCasillaEspecial(TipoCasillaEspecial.SALIDA)){
                return c;
            }
        }
        return null;
    }
    public Casilla getCarcel(){
        for(Casilla c: this.casillasPorTipos.get(TipoCasilla.ESPECIAL)){
            if(c.esCasillaEspecial(TipoCasillaEspecial.CARCEL)){
                return c;
            }
        }
        return null;
    }
    public Collection<Jugador> getJugadores(){
        return this.jugadores.values();
    }
    public Casilla getCasillaPorNombre(String nombre){
        for(Casilla c : this.casillas){
            if(nombre.equals(c.getNombre())){
                return c;
            }
        }
        return null;
    }
    public Jugador getJugador(String nombre){
        return this.jugadores.get(nombre);
    }
    public Avatar getAvatar(char c){
        return this.avatares.get(c);
    }
    public Jugador getJugadorActual(){
        String nombreActual = this.colajugadores.peek();
        return this.jugadores.get(nombreActual);
    }
    public float getPremioVuelta(){
        return premioVuelta;
    }
    public Jugador getJugadorPorId(char id){
        for(Jugador j : this.jugadores.values()){
            if(j.getAvatar().getId() == id){
                return j;
            }
        }
        return null;
    }
    public String getEnVenta(){
        String resultado = "";
        for(Casilla c : this.casillasPorTipos.get(TipoCasilla.SOLAR)){
            if(c.getPropietario().equals(this.banca)){
                resultado += c + ",\n";
            }
        }
        for(Casilla c : this.casillasPorTipos.get(TipoCasilla.TRANSPORTE)){
            if(c.getPropietario().equals(this.banca)){
                resultado += c + ",\n";
            }
        }
        for(Casilla c : this.casillasPorTipos.get(TipoCasilla.SERVICIOS)){
            if(c.getPropietario().equals(this.banca)){
                resultado += c+ ",\n";
            }
        }
        return resultado;
    }


    public void addFortunaInicial(){
        float fortunaInicial = getTotalSolares()/3;
        for(Jugador j: this.jugadores.values()){
            j.setFortuna(fortunaInicial);
        }
    }
    private void addBanca(){
        this.banca = new Jugador();
        for(Casilla c: this.casillas){
            this.banca.addPropiedad(c);
        }
    }
    private void addImpuesto(){
        float impuesto = this.premioVuelta;
        for(Casilla c : this.casillasPorTipos.get(TipoCasilla.IMPUESTOS)){
            c.setImpuestos(impuesto);
            impuesto /= 2;
        }
    }
    private void anhadirTransporte(){
        float valorTransporte = this.premioVuelta;
        for(Casilla c : this.casillasPorTipos.get(TipoCasilla.TRANSPORTE)){
            c.setValor(valorTransporte);
            c.setAlquiler(this.premioVuelta);
        }
    }
    private void anhadirServicio(){
        float valorServicio = this.premioVuelta*0.75f;
        for(Casilla c : this.casillasPorTipos.get(TipoCasilla.SERVICIOS)){
            c.setValor(valorServicio);
            c.setAlquiler(this.premioVuelta/200);
        }
    }
    private void anhadirAlquiler(Jugador j,Color color){
        if(!this.tieneTodosSolares(j,color)){
            return;
        }
        for(Casilla c : this.casillasPorColores.get(color)){
            c.setAlquiler(c.getAlquiler()*2);
        }
    }
    private String anhadirPrecioVueltas(){
        for(Jugador j: this.jugadores.values()){
            if(j.getVueltas()  < 4){
                return "";
            }
        }
        for(Jugador j :this.jugadores.values()){
            j.resetVueltas();
        }
        for(Casilla c : this.casillasPorTipos.get(TipoCasilla.SOLAR)){
            if(c.getPropietario().equals(this.banca)){
                c.setValor(c.getValor()*1.05f);
            }
        }
        for(Casilla c : this.casillasPorTipos.get(TipoCasilla.TRANSPORTE)){
            if(c.getPropietario().equals(this.banca)){
                c.setValor(c.getValor()*1.05f);
            }
        }
        for(Casilla c : this.casillasPorTipos.get(TipoCasilla.SERVICIOS)){
            if(c.getPropietario().equals(this.banca)){
                c.setValor(c.getValor()*1.05f);
            }
        }
        return "Todos los jugadores han dado 4 vueltas, por lo tanto las casillas sin dueño incrementan su valor en un 5%";
    }
    public String anhadirNuevoJugador(String nombre,String avatar){
        
        if(this.jugadores.values().size() > NJUGADORES){
            return "No se pueden añadir mas jugadores.";
        }

        TipoAvatar tAvatar = TipoAvatar.aTipoAvatar(avatar);
        if(tAvatar == null){
            return "Este tipo de avatar no es valido. Los tipos disponibles son: sombrero, pelota, coche y esfinge.";
        }
        if(this.jugadores.containsKey(nombre)){
            return "Este jugador ya esta dado de alba";
        }
        Avatar nuevoAvatar = new Avatar(generarLetraAvatar(),tAvatar,this.getSalida());
        this.avatares.put(nuevoAvatar.getId(),nuevoAvatar);
        Jugador nuevoJugador = new Jugador(nombre,nuevoAvatar);
        this.jugadores.put(nombre, nuevoJugador);
        this.colajugadores.add(nombre);
        return 
            """
            {
            nombre: %s,
            avatar:%c
            }""".formatted(nombre,nuevoAvatar.getId());
    }

    public boolean esPartidaEmpezada(){
        return partidaEmpezada;
    }
    public boolean tieneTodosSolares(Jugador j,Color color){
        for(Casilla c : this.casillasPorColores.get(color)){
            if(!c.getPropietario().equals(j)){
                return false;
            }
        }
        return true;
    }

    /*
     *
     *      LOGICA JUEGO
     *
     */

    public void empezarPartida(){
        this.partidaEmpezada = true;
        addFortunaInicial();
        this.premioVuelta = getTotalSolares()/this.casillasPorTipos.get(TipoCasilla.SOLAR).size();
        addBanca();
        addImpuesto();
        anhadirTransporte();
        anhadirServicio();
        System.out.println(this.tablero.toString());
    }

    public String comprar(Jugador j,String nombreCasilla){
        Casilla c = j.getAvatar().getCasilla();
        if(!c.getNombre().equals(nombreCasilla)){
            return "%s no puede comprar %s".formatted(j.getNombre(),nombreCasilla);
        }
        if(!c.getPropietario().equals(this.banca)){
            return "%s no puede comprar %s ya que pertenece a otro jugador.".formatted(j.getNombre(),nombreCasilla);
        }
        if(c.getTipo() != TipoCasilla.SOLAR && c.getTipo() != TipoCasilla.TRANSPORTE && c.getTipo() != TipoCasilla.SERVICIOS){
            return "%s no puede comprar %s porque es una casilla de tipo %s".formatted(j.getNombre(),nombreCasilla,c.getTipo());
        }
        if(!j.puedePagar(c.getValor())){
            return "%s no puede comprar %s, ya que tiene %f€ y necesita por lo menos %f€".formatted(j.getNombre(),nombreCasilla,j.getFortuna(),c.getValor());
        }
        j.removeFortuna(c.getValor());
        j.addPropiedad(c);
        this.banca.addBote(c.getValor());
        if(this.tieneTodosSolares(j,c.getGrupo())){
            c.setAlquiler(c.getAlquiler()*2);
        }
        return "El jugador %s compra la casilla %s por %f€. Su fortuna actual es %f€.".formatted(j.getNombre(),c.getNombre(),c.getValor(),j.getFortuna());

    }
    public String lanzarDados(Dado d){
        Jugador jugadorActual = this.getJugadorActual();
        if(!jugadorActual.getAccion()){
            return "El jugador ya ha lanzado los dados";
        }
        
        jugadorActual.setAccion(false);
        int cantidad;
        if(d == null){
            d = new Dado();
            cantidad = d.lanzar();
        }else {
            cantidad = d.debugLanzar();
        }
        //Si esta en la carcel
        if(jugadorActual.estaEncarcelado()){
            int turnosCarcel = jugadorActual.getTurnosCarcel();
            if(turnosCarcel == 2){
                float coste = this.premioVuelta*0.25f;
                jugadorActual.setAccion(true);
                return jugadorActual.desencarcelar(coste);
            }
            if(d.getFueronDobles()){
                return mover(cantidad) + jugadorActual.desencarcelar(0);
            }
            jugadorActual.setTurnosCarcel(turnosCarcel + 1);
            return "El jugador no ha sacado dados dobles, por lo que sigue en la carcel.";
        }

        //Si saca dados dobles
        if(d.getFueronDobles()){
            jugadorActual.incrementarDadosDobles();

            //Si saco dados dobles 3 veces, a la carcel
            if(jugadorActual.getVecesDadosDobles() == 3){
                jugadorActual.getAvatar().setCasilla(this.getCarcel());
                return "El jugador ha sacado dados dobles tres veces, por lo que va a la carcel.";
            }
            //Puede volver a lanzar los dados
            jugadorActual.setAccion(true);
            return "El jugador ha sacado dados dobles, por lo tanto puede volver a lanzar los dados." + mover(cantidad);
        }

        //Tirada normal
        jugadorActual.setVecesDadosDobles(0);
        return mover(cantidad);
    }
    public String mover(int cantidad){
        Jugador j = this.getJugadorActual();
        Avatar av = j.getAvatar();
        Casilla oldCasilla = av.getCasilla();
        int oldPos = oldCasilla.getPos();
        int newPos = oldPos + cantidad;

        String mensaje = "";
        if( newPos > 39){
            j.addFortuna(this.premioVuelta);
            j.addVueltas(newPos / 40);

            mensaje += "El jugador %s recibe %f€ de premio ya que paso por la Salida.".formatted(j.getNombre(),this.premioVuelta);
            mensaje += "\n" + this.anhadirPrecioVueltas();

            newPos = newPos % 40;
        }
        Casilla newCasilla = this.casillas.get(newPos);

        mensaje += String.format("El avatar %c avanza %d posiciones, desde %s hasta %s.",av.getId(),cantidad,oldCasilla.getNombre(),newCasilla.getNombre());
        if(newCasilla.getPropietario().equals(j)){
            return mensaje;
        }
        if(newCasilla.esCasillaEspecial(TipoCasillaEspecial.IRCARCEL)){
            av.setCasilla(this.getCarcel());
            j.encarcelar();
            return mensaje + "El avatar se coloca en la casilla Carcel.";
        }
        av.setCasilla(newCasilla);

        if(newCasilla.getTipo() == TipoCasilla.SERVICIOS){
            float multiplicador = 0;
            for(Casilla c : this.casillasPorTipos.get(TipoCasilla.SERVICIOS)){
                if(c.getPropietario().equals(newCasilla.getPropietario())){
                    multiplicador += 1;
                }
            }
            multiplicador = multiplicador == 1 ? 4*cantidad : 10*cantidad;
            return mensaje + newCasilla.caer(j,multiplicador);
        }
        if(newCasilla.esTipo(TipoCasilla.SOLAR)){
            return mensaje + newCasilla.caer(j,1);
        }

        boolean check;
        if((check = newCasilla.esTipo(TipoCasilla.COMUNIDAD)) == true || newCasilla.esTipo(TipoCasilla.SUERTE)){
            System.out.println(mensaje);
            Scanner sc = new Scanner(System.in);
            System.out.print("%s elige una carta: ".formatted(j.getNombre()));
            int i = sc.nextInt();
            return check ? this.baraja.elegirCartaSuerte(i,this) : this.baraja.elegirCartaComunidad(i,this);
        }
        if(newCasilla.esTipo(TipoCasilla.TRANSPORTE)){
            float multiplicador =  0;
            for(Casilla c : this.casillasPorTipos.get(TipoCasilla.TRANSPORTE)){
                if(c.getPropietario().equals(newCasilla.getPropietario())){
                    multiplicador += 1;
                }
            }
            return mensaje + newCasilla.caer(j,multiplicador*0.25f);
        }

        return mensaje + newCasilla.caer(j);
    }

    public String siguienteTurno(){
        Jugador jugadorActual = this.getJugadorActual();
        if(jugadorActual.getAccion()){
            return "El jugador debe lanzar los dados antes de acabar su turno.";
        }

        this.colajugadores.poll();
        String nombreNuevoJugador = this.colajugadores.peek();
        this.jugadores.get(nombreNuevoJugador).setAccion(true);
        this.colajugadores.offer(jugadorActual.getNombre());
        return "El jugador actual es %s".formatted(nombreNuevoJugador);
    }

    public void run(){
        System.out.println("""
                Bienvenido al Monopoly!
                Crea jugadores con 'crear jugador <nombre> <avatar>'
                y cuando ya tengas mas de dos jugadores escribe 'empezar'
                """);
        Prompt prompt = new Prompt();
        prompt.run(this);
    }
    //Preguntar si en la piscina y pista de deporte se puede edificar si los hoteles y las casas estan en otra casilla
    public String edificar(String tipo){
        TipoEdificio t = TipoEdificio.aTipoEdificio(tipo);
        if (t == null){
            return "El tipo de edificio no es valido. Los tipos disponibles son: casa, hotel, piscina y pista de deporte.";
        }
        Jugador j = this.getJugadorActual();
        Avatar a = j.getAvatar();
        Casilla c = a.getCasilla();
        if (c.getTipo() != TipoCasilla.SOLAR){
            return "El jugador actual no esta en una casilla de tipo solar.";
        }
        if(!c.getPropietario().equals(j)){
            return "El jugador actual no es el propietario de la casilla %s".formatted(c.getNombre());
        }
        switch(t){
            case CASA:
                if(c.getNumeroEdificios(t) == 4){
                    return "La casilla %s ya tiene 4 casas.".formatted(c.getNombre());
                }
                if(!tieneTodosSolares(j,c.getGrupo()) /*TODO && no ha caido dos veces en la casilla*/){
                    return "El jugador no puede edificar casas en la casilla %s porque no tiene todos los solares del grupo %s o no ha caido más de dos veces en la casilla".formatted(c.getNombre(),c.getGrupo());
                }
                break;
            case HOTEL:
                if(c.getNumeroEdificios(t) == 1){
                    return "La casilla %s ya tiene un hotel.".formatted(c.getNombre());
                }
                if(c.getNumeroEdificios(TipoEdificio.CASA) != 4){
                    return "La casilla %s no tiene 4 casas.".formatted(c.getNombre());
                }
                break;
            case PISCINA:
                if(c.getNumeroEdificios(t) == 1){
                    return "La casilla %s ya tiene una piscina.".formatted(c.getNombre());
                }
                if(c.getNumeroEdificios(TipoEdificio.CASA) <= 2 || c.getNumeroEdificios(TipoEdificio.HOTEL) != 1){
                    return "La casilla %s no tiene 4 casas y un hotel.".formatted(c.getNombre());
                }
                break;
            case PISTA_DE_DEPORTE:
                if(c.getNumeroEdificios(t) == 1){
                    return "La casilla %s ya tiene una pista de deporte.".formatted(c.getNombre());
                }
                if(c.getNumeroEdificios(TipoEdificio.HOTEL) != 2){
                    return "La casilla %s no tiene 2 hoteles.".formatted(c.getNombre());
                }
                break;
        }

        return c.edificar(t,this.premioVuelta);
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
    private char generarLetraAvatar(){
        Random r = new Random();
        char randomChar = (char)(r.nextInt(90-65)+65);
        if(this.avatares.containsKey(randomChar)){
            return generarLetraAvatar();
        }
        return randomChar;
    }
    private float getTotalSolares(){
        float acumulado = 0;
        for(Casilla c : this.casillasPorTipos.get(TipoCasilla.SOLAR)){
            acumulado += c.getValor();
        }
        return acumulado;
    }

}
