package org.monopoly;

import java.util.ArrayList;

public class Jugador{
    private String nombre;
    private Avatar avatar;
    private float fortuna;
    private ArrayList<Casilla> propiedades;
    private ArrayList<Casilla> hipotecas;
    /** 
     *
     *  @param nombre Nombre del nuevo jugador
     *  @param avatar Avatar en texto que se le debe asignar al jugador
     *  @param salida Casilla de Salida
     * 
     * */
    public Jugador(String nombre,String avatar,Casilla salida){
        this.nombre = nombre;
        //TODO
        this.fortuna = 0;
        this.propiedades = new ArrayList<>();
        this.hipotecas = new ArrayList<>();
        this.avatar = new Avatar(this,avatar,salida);

    }

    /** 
     *
     *  @return Devuelve el avatar
     *
     * */
    public Avatar getAvatar(){
        return this.avatar;
    }
    /**
     *  @return Devuleve el nombre del jugador 
     * */
    public String getNombre(){
        return this.nombre;
    }


    @Override
    public String toString() {
        //TODO añadir edificios
        return """
        {
            nombre: %s,
            avatar: %c,
            fortuna: %d,
            propiedades: %s,
            hipotecas: %s,
            edificios: todo
        }\n""".formatted(this.nombre,this.avatar,this.fortuna,this.propiedades,this.hipotecas);
    }
}
