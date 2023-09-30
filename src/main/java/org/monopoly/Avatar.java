package org.monopoly;
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
}
