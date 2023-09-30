package org.monopoly;

import java.util.Random;

public class Avatar{
    public enum TipoAvatar{
        Sombrero,
        Coche,
        Pelota,
        Esfinge
    }
    private char id;
    private TipoAvatar tipo;
    private Casilla casilla;
    private Jugador jugador;
    /** 
     *
     * Constructor para un nuevo avatar
     *  @param jugador Jugador asignado al avatar
     *  @param avatar Nombre del avatar en String
     *  @param salida Casilla de salida
     *
     * */
    public Avatar(Jugador jugador,String avatar,Casilla salida){
        Random r = new Random();
        switch(avatar){
            case "coche":
                this.tipo = TipoAvatar.Coche;
                break;
            case "sombrero":
                this.tipo = TipoAvatar.Sombrero;
                break;
            case "esfinge":
                this.tipo = TipoAvatar.Esfinge;
                break;
            case "pelota":
                this.tipo = TipoAvatar.Pelota;
                break;
            default:
                //TODO
        }
        int randomInt = (r.nextInt(90-65)+65);
        this.id = (char)randomInt;
        this.casilla = salida;
        this.jugador = jugador;
    }
    public char getId(){
        return this.id;
    }
}
