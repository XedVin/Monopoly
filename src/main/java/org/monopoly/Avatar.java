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

    public Avatar(String avatar,Jugador jugador,Casilla salida){
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
        salida.añadirAvatar(this);
        this.jugador = jugador;
    }

    public char getId(){
        return this.id;
    }
    public TipoAvatar getTipoAvatar(){
        return this.tipo;
    }
    public Casilla getCasilla(){
        return this.casilla;
    }
    public void setCasilla(Casilla c){
        this.casilla.eliminarAvatar(this);
        this.casilla = c;
        this.casilla.añadirAvatar(this);
    }
    @Override
    public String toString() {
        return """
               {
                id: %c,
                tipo: %s,
                casilla: %s,
                jugador: %s
               }""".formatted(this.getId(),this.tipo,this.casilla.getNombre(),this.jugador.getNombre());
    }
}
