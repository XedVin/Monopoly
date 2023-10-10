package org.monopoly;

import java.util.ArrayList;

public class Jugador{
    private String nombre;
    private Avatar avatar;
    private float fortuna;
    private ArrayList<Casilla> propiedades;
    private ArrayList<Casilla> hipotecas;
    private boolean accion;
    public Jugador(String nombre,String avatar,Casilla salida){
        this.nombre = nombre;
        this.fortuna = 0;
        this.propiedades = new ArrayList<>();
        this.hipotecas = new ArrayList<>();
        this.avatar = new Avatar(avatar,this,salida);
        this.accion = true;
    }
    public Jugador(){
        this.nombre = "Banca";
        this.fortuna = -1;
        this.propiedades = new ArrayList<>();
    }
    public Avatar getAvatar(){
        return this.avatar;
    }
    public String getNombre(){
        return this.nombre;
    }
    public boolean getAccion(){
        return this.accion;
    }
    public ArrayList<Casilla> getPropiedades(){
        return this.propiedades;
    }
    public float getFortuna(){
        return this.fortuna;
    }
    public void setFortuna(float fortuna){
        this.fortuna = fortuna;
    }
    public void anhadirFortuna(float fortuna){
        this.fortuna += fortuna;
    }
    public void anhadirPropiedad(Casilla c){
        this.propiedades.add(c);
        c.setPropietario(this);
    }
    public void setAccion(boolean a){
        this.accion = a;
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
