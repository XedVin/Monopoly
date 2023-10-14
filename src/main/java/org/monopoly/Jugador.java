package org.monopoly;

import java.util.ArrayList;

import org.monopoly.Avatar.TipoAvatar;

public class Jugador{
    private String nombre;
    private Avatar avatar;
    private float fortuna;
    private ArrayList<Casilla> propiedades;
    private ArrayList<Casilla> hipotecas;
    private boolean accion;
    private float bote;


    private int turnosEnLaCarcel;
    private int vecesDoblesDados;
    public Jugador(String nombre,TipoAvatar avatar,Casilla salida){
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
        this.bote = 0;
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
    public int getTurnosCarcel(){
        return this.turnosEnLaCarcel;
    }

    public void setFortuna(float fortuna){
        this.fortuna = fortuna;
    }
    public void setAccion(boolean a){
        this.accion = a;
    }
    public void setTurnosCarcel(int t){
        this.turnosEnLaCarcel =  t;
    }
    public void anhadirFortuna(float fortuna){
        this.fortuna += fortuna;
    }
    public void anhadirPropiedad(Casilla c){
        this.propiedades.add(c);
        c.setPropietario(this);
    }
    public void addFortuna(float cantidad){
        this.fortuna += cantidad;
    }
    public void removeFortuna(float cantidad){
        this.fortuna -= cantidad;
    }

    public void anhadirBote(float cantidad){
        this.bote += cantidad;
    }
    public float getBote(){
        float bote = this.bote;
        this.bote = 0;
        return bote;
    }
    public boolean puedePagar(float cantidad){
        return this.fortuna >= cantidad;
    }
    public int getVecesDadosDobles(){
        return this.vecesDoblesDados;
    }
    public void setVecesDadosDobles(int veces){
        this.vecesDoblesDados = veces;
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
