package org.monopoly;

import java.util.Random;

public class Avatar{
    public enum TipoAvatar{
        Sombrero("sombrero"),
        Coche("coche"),
        Pelota("pelota"),
        Esfinge("esfinge");

        private String nombre;
        private TipoAvatar(String n){
            this.nombre = n;
        }

        public String getNombre(){
            return this.nombre;
        }
        public static TipoAvatar aTipoAvatar(String n){
            for(TipoAvatar tipo: TipoAvatar.values()){
                if(tipo.getNombre().equals(n)){
                    return tipo;
                }
            }
            return null;
        }
    }

    private char id;
    private TipoAvatar tipo;
    private Casilla casilla;
    private Jugador jugador;

    public Avatar(TipoAvatar avatar,Jugador jugador,Casilla salida){
        this.tipo = avatar;
        this.id = generarId();
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

    private char generarId(){
        Random r = new Random();
        int randomInt = (r.nextInt(90-65)+65);
        return (char)randomInt;
    }

}
