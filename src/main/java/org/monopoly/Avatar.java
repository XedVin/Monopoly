package org.monopoly;

public class Avatar{
    // Se definen los avatares que existen para el juego
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

    //Constructor del avatar
    public Avatar(char id,TipoAvatar tAvatar,Casilla salida){
        this.tipo = tAvatar;
        this.id = id;
        this.casilla = salida;
        salida.addAvatar(this);
    }

    // GETTERS
    public char getId(){
        return this.id;
    }
    public Casilla getCasilla(){
        return this.casilla;
    }
    // SETTER
    public void setCasilla(Casilla c){
        this.casilla.eliminarAvatar(this);
        this.casilla = c;
        this.casilla.addAvatar(this);
    }

    @Override
    // Sirve para pasar el objeto a string
    public String toString() {
       return "%c".formatted(this.id);
    }
    public String toString(Jugador j){
        return """
        {
            id: %c,
            tipo: %s,
            casilla: %s,
            jugador: %s
        }""".formatted(this.id,this.tipo.getNombre(),this.casilla.getNombre(),j.getNombre());
    }
}
