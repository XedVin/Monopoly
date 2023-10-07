package org.monopoly;

import java.util.ArrayList;

public class Jugador{
    private String nombre;
    private Avatar avatar;
    private float fortuna;
    private ArrayList<Casilla> propiedades;
    private ArrayList<Casilla> hipotecas;

    public Jugador(String nombre,String avatar,Casilla salida){
        this.nombre = nombre;
        //TODO
        this.fortuna = 0;
        this.propiedades = new ArrayList<>();
        this.hipotecas = new ArrayList<>();
        this.avatar = new Avatar(avatar,this,salida);

    }
    public Avatar getAvatar(){
        return this.avatar;
    }
    public String getNombre(){
        return this.nombre;
    }
    public ArrayList<Casilla> getPropiedades(){
        return this.propiedades;
    }
    @Override
    public String toString() {
        //TODO añadir edificios
        return """
        {
            nombre: %s,
            avatar: %c,
            fortuna: %f,
            propiedades: %s,
            hipotecas: %s,
            edificios: todo
        }\n""".formatted(this.nombre,this.avatar.getId(),this.fortuna,this.propiedades,this.hipotecas);
    }
}
