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

    /**
     *
     *  Devuelve el ultimo comando escrito 
     * 
     * */
    public String getUltimoComando(){
        return this.ultimoComando;
    }

    /**
     *
     *  Empieza el bucle para recibir los comandos
     *  @param m Un objeto de la clase Monopoly
     *
     * */
    public void run(Monopoly m){
        while(true){
            this.ultimoComando = this.scanner.nextLine();

            if(Objects.equals(this.ultimoComando,"exit")){
                break;
            }
            
            procesarComando(m);
        }
        this.scanner.close();
    }

    /**
     *
     *  Elige que comando ejecutar segun el input, quizas se deberia crear una nueva clase Comando
     * */
    private void procesarComando(Monopoly m){
        String[] args = this.ultimoComando.split(" ");
        switch(args[0]){
            case "ver":
                comandoVer(m,args[1]);
                break;
            case "crear":
                comandoCrear(m,args);
                break;
            default:
                return;


        }
    }

    //TODO añadir errores
    private void comandoVer(Monopoly m,String target){
        if(Objects.equals("tablero",target)){
            System.out.println(m.getTablero());
        }
    }
    private void comandoCrear(Monopoly m,String[] args){
        if(args.length == 1){
            return;
        }
        switch(args[1]){
            case "jugador":
                Jugador j = new Jugador(args[2],args[3],m.getSalida());
                m.setJugador(j);
                m.setAvatar(j.getAvatar());
                System.out.println(j); //TODO
        }
    }
}
