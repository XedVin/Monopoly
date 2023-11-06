package org.monopoly;
import java.util.Objects;
import java.util.Scanner;

import org.monopoly.Casilla.TipoCasillaEspecial;

public class Prompt{
    private String ultimoComando;
    private Scanner scanner;

    Prompt(){
        this.ultimoComando = "";
        this.scanner = new Scanner(System.in);
    }

    public void run(Monopoly m){
        while(true){
            System.out.print("$>");
            this.ultimoComando = this.scanner.nextLine();

            if(Objects.equals(this.ultimoComando,"salir")){
                break;
            }
            
            System.out.println(procesarComando(m));
        }
        this.scanner.close();
    }

    private String procesarComando(Monopoly m){
        String[] args = this.ultimoComando.split(" ");
        
        // Antes de que empiece la partida
        if (!m.esPartidaEmpezada()){
            if(args[0].equals("empezar")){

                if(m.getJugadores().size() >= 2){
                    m.empezarPartida();
                    return "La partida ha empezado";
                }

                return "Dos o mas jugadores son necesarios para empezar el juego";
            
            }else if(args[0].equals("crear")){
                return comandoCrear(m,args);
            }
            return "Ese comando no existe o no está disponible en esta etapa del juego";
        }
        
        //con la partida empezada
        switch(args[0]){
            case "ver":
                return comandoVer(m,args);
            case "listar":
                return comandoListar(m,args);
            case "describir":
                return comandoDescribir(m,args);
            case "comandos":
                return comandoComandos();
            case "acabar":
                return comandoAcabar(m,args);
            case "jugador":
                return comandoJugador(m);
            case "comprar":
                return comandoComprar(m,args);
            case "mover":
                return debugMover(m,args);
            case "lanzar":
                return comandoLanzarDados(m,args);
            case "edificar":
                return comandoEdificar(m,args);
            case "salir":
                return comandoSalir(m,args);
            default:
                return "Ese comando no existe";
        }

    }
    private String comandoVer(Monopoly m,String[] args){
        return args.length > 1 &&  args[1].equals("tablero") ? m.getTablero().toString() : "- ver: <tablero>";
    }

    private String comandoListar(Monopoly m,String[] args){
        if(args.length != 2){
            return "- listar <jugadores | avatares>";
        }

        String result = "";
        switch(args[1]){
            case "jugadores":
                for(Jugador j: m.getJugadores()){
                    result += j.toString();
                }
                return result;
            case "avatares":
                for(Jugador j: m.getJugadores()) {
                    result += j.getAvatar().toString(j);
                }
                return result;
            case "edificios":
            case "enventa":
                return m.getEnVenta();
            default:
                return "- listar <jugadores | avatares>";
        }
    }

    private String comandoDescribir(Monopoly m, String[] args){
        if(args.length < 2){
            return "- describir <nombreCasilla | jugador | avatar> < nombreJugador| nombreAvatar | >";
        }
        switch(args[1]){
            case "jugador":
                Jugador j = m.getJugador(args[2]);
                if(j == null){
                    return "Ese jugador no existe.";
                }
                return j.toString();
            case "avatar":
                Avatar av = m.getAvatar(args[2].charAt(0));
                if(av == null){
                    return "Ese avatar no existe.";
                }
                Jugador jAsociado = m.getJugadorPorId(av.getId());
                return av.toString(jAsociado);
            default:
                Casilla c = m.getCasillaPorNombre(args[1]);
                if(c != null){

                    if(c.esCasillaEspecial(TipoCasillaEspecial.CARCEL) || c.esCasillaEspecial(TipoCasillaEspecial.PARKING)){
                        return c.toString(m);
                    }

                    return c.toString();
                }
                return "Esa casilla no existe";
        }
    }

    private String comandoComandos(){
        return """
            - ver tablero
            - crear <jugador> <nombre> <avatar>
            - listar <jugadores | avatares>
            - describir <nombreCasilla | jugador | avatar> < nombreJugador| nombreAvatar | >
            - acabar <turno>
            - lanzar <dados>
            - jugador
            - comandos
            """;
    }

    private String comandoAcabar(Monopoly m,String[] args){
        if(args.length != 2 || !Objects.equals(args[1],"turno")){
            return "- acabar <turno>";
        }
        return m.siguienteTurno();
    }
    private String comandoJugador(Monopoly m){
        Jugador j = m.getJugadorActual();
        return String.format("""
                {
                    nombre: %s,
                    avatar: %s
                }
                """, j.getNombre(),j.getAvatar().getId());
    }
    private String comandoComprar(Monopoly m,String[] args){
        if(args.length != 2){
            return "- comprar <casilla>";
        }
        return m.comprar(m.getJugadorActual(),args[1]);
    }

    private String debugMover(Monopoly m, String[] args){
        return m.mover(Integer.parseInt(args[1]));

    }

    private String comandoLanzarDados(Monopoly m,String[] args){
        if(args.length < 2 || !args[1].equals("dados")){
            return "- lanzar <dados>";
        }
        Dado d = null;
        if(args.length >= 3 && args[2].equals("debug")){
            d = new Dado(Integer.parseInt(args[3]),Integer.parseInt(args[4]));
        }
        return m.lanzarDados(d) + "\n" + m.getTablero() + m.getJugadorActual().imprimirEstado();
    }

    private String comandoCrear(Monopoly m,String[] args){
        if(args.length != 4 && args[1].equals("jugador")){
            return "- crear <jugador> <nombre> <avatar>";
        }
        return m.anhadirNuevoJugador(args[2], args[3]);
    }
    
    private String comandoSalir(Monopoly m,String[] args){
        if(args.length != 2 || !args[1].equals("carcel")){
            return "- salir <carcel>";
        }
        Jugador j = m.getJugadorActual();
        if(!j.estaEncarcelado()){
            return "El jugador actual no esta en la carcel";
        }
        if(!j.getAccion()){
            return "%s ya ha intentado salir de la carcel lanzando los dados.".formatted(j.getNombre());
        }
        float coste = m.getPremioVuelta()*0.25f;
        if(!j.puedePagar(coste)){
            if(j.getTurnosCarcel() == 3){
                j.preguntarHipBanc();
            }
            return "%s no puede pagar salir de la carcel.".formatted(j.getNombre());
        }
        j.desencarcelar(coste);
        return "%s paga %f€ y sale de la cárcel. Puede lanzar los dados.".formatted(j.getNombre(),coste);
    }

    private String comandoEdificar(Monopoly m,String[] args){
        if(args.length != 2){
            return "- edificar <tipoEdificio>";
        }
        return m.edificar(args[1]);
    }
}
