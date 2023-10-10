package org.monopoly;
import java.util.Objects;
import java.util.Scanner;

/**
 *
 *  @summary La finalidad de esta clase es controlar el input por teclado y 
 *  devolver el output por pantalla procesando los comandos 
 *
 * */
public class Prompt{

    private String ultimoComando;
    private Scanner scanner;

    Prompt(){
        this.ultimoComando = new String();
        this.scanner = new Scanner(System.in);
    }
    public void run(Monopoly m){
        while(true){
            System.out.print("$>");
            this.ultimoComando = this.scanner.nextLine();

            if(Objects.equals(this.ultimoComando,"exit")){
                break;
            }
            
            System.out.println(procesarComando(m));
        }
        this.scanner.close();
    }

    public void preguntarNuevoJugador(Monopoly m){
        System.out.print("Creando jugador\nNombre: ");
        String nombre = this.scanner.nextLine();
        System.out.print("Avatar(coche | pelota | esfinge | sombrero): ");
        String avatar = this.scanner.nextLine();
        m.getJugadores().add(new Jugador(nombre,avatar,m.getSalida()));
    }

    private String procesarComando(Monopoly m){
        String[] args = this.ultimoComando.split(" ");
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
            case "dados":
                return debugDados(m,args);
            case "lanzar":
                return comandoLanzarDados(m,args);
            default:
                return "Ese comando no existe";
        }

    }
    private String comandoVer(Monopoly m,String[] args){
        return args.length > 1 &&  args[1].equals("tablero") ? m.getTablero().toString() : "ver: <tablero>";
    }

    private String comandoComprar(Monopoly m,String[] args){
        return m.comprar(m.getJugadorActual(),args[1]);
    }
    private String comandoCrear(Monopoly m,String[] args){
        if(args.length == 1){
            return """
                crear:
                        <jugador> <nombre> <avatar>
                """;
        }

        switch(args[1]){
            case "jugador":
                return comandoCrearJugador(args[2],args[3],m);
            default:
                return """
                crear:
                        <jugador> <nombre> <avatar>
                """;
        }
    }

    private String comandoCrearJugador(String nombre,String avatar,Monopoly m){
        Jugador j = new Jugador(nombre,avatar,m.getSalida());
        m.setJugador(j);
        m.setAvatar(j.getAvatar());
        return """
            {
                nombre: %s,
                avatar: %c
            }""".formatted(nombre,j.getAvatar().getId());
    }
    
    private String comandoListar(Monopoly m,String[] args){
        if(args.length != 2){
            return """
                {
                    listar:
                            <jugadores>
                            <avatares>
                }""";
        }
        if(args[1].equals("jugadores")){
            String result = "";
            for(Jugador j: m.getJugadores()){
                result += j.toString();
            }
            return result;
        }else if(args[1].equals("avatares")){
            String result = "";
            for(Avatar a: m.getAvatares()) {
                result += a.toString();
            }
            return result;
        }
        return """
                {
                    listar:
                            <jugadores>
                            <avatares>
                }""";

    }
    private String comandoDescribir(Monopoly m, String[] args){
        if(args.length != 2){
            return """
                describir:
                            <casilla>
                """;
        }
        Casilla c = m.getCasillaPorNombre(args[1]);
        if(c != null){
            return c.toString();
        }
        return "Esa casilla no existe";
    }
    private String comandoComandos(){
        return """
            - ver tablero
            - crear <jugador> <nombre> <avatar>
            - listar <jugadores | avatares>
            - describir <casilla>
            """;

    }
    private String comandoAcabar(Monopoly m,String[] args){
       if(args.length != 2 && !Objects.equals(args[1],"turno")){
        return """
                acabar <turno>
            """;
       }
       m.siguienteTurno();
       return "El jugador actual es %s".formatted(m.getJugadorActual().getNombre());
    }
    private String comandoJugador(Monopoly m){
        return String.format("""
                {
                    nombre: %s,
                    avatar: %s
                }
                """, m.getJugadorActual().getNombre(),m.getJugadorActual().getAvatar().getId());

    }
    private String comandoLanzarDados(Monopoly m,String[] args){
        if(args.length != 2 || !args[1].equals("dados")){
            return "- lanzar <dados>";
        }
        if(!m.getJugadorActual().getAccion()){
            return "El jugador ya ha lanzado los dados";
        }

        int resultado = (new Dado()).resultado();
        if(resultado == -1){
            //TODO
            return "A la carcel";
        }
        return m.mover(resultado);
    }
    private String debugMover(Monopoly m, String[] args){
        return m.mover(Integer.parseInt(args[1]));
        
    }
    private String debugDados(Monopoly m, String[] args){
        return String.format("-> %d",(new Dado()).resultado());
        
    }
}
