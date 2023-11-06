package org.monopoly;

import java.util.ArrayList;
import java.util.Iterator;

// Se definen los atributos de cada jugador
public class Jugador{

    private String nombre;
    private Avatar avatar;
    private float fortuna;
    private ArrayList<Casilla> propiedades;
    private boolean accion;

    private boolean encarcelado;
    private int turnosEnLaCarcel;

    private int vecesDoblesDados;
    private int vueltas;

    private float gastosTotales;

    private float bote;
    
    private ArrayList<Casilla> hipotecas;
    // Constructor de un jugador normal
    public Jugador(String nombre,Avatar avatar){
        this.nombre = nombre;
        this.fortuna = 0;
        this.propiedades = new ArrayList<>();
        this.hipotecas = new ArrayList<>();
        this.avatar = avatar; 
        this.accion = true;
        this.encarcelado = false;
        this.gastosTotales = 0;
    }
    // Constructor de la banca
    public Jugador(){
        this.nombre = "Banca";
        this.fortuna = -1;
        this.propiedades = new ArrayList<>();
        this.bote = 0;
    }

    // Getters
    public Avatar getAvatar(){
        return this.avatar;
    }
    public String getNombre(){
        return this.nombre;
    }
    public boolean getAccion(){
        return this.accion;
    }
    public float getFortuna(){
        return this.fortuna;
    }
    public int getTurnosCarcel(){
        return this.turnosEnLaCarcel;
    }
    public int getVueltas(){
        return this.vueltas;
    }
    public int getVecesDadosDobles(){
        return this.vecesDoblesDados;
    }
    public float getBote(){
        float bote = this.bote;
        this.bote = 0;
        return bote;
    }

    // Setters
    public void setFortuna(float fortuna){
        this.fortuna = fortuna;
    }
    public void setAccion(boolean a){
        this.accion = a;
    }
    public void setVecesDadosDobles(int veces){
        this.vecesDoblesDados = veces;
    }
    public void setTurnosCarcel(int t){
        this.turnosEnLaCarcel =  t;
    }
    
    public void addFortuna(float fortuna){
        this.fortuna += fortuna;
    }
    public void addPropiedad(Casilla c){
        this.propiedades.add(c);
        c.setPropietario(this);
    }
    public void addVueltas(int vueltas){
        this.vueltas +=vueltas;
    }
    public void addBote(float cantidad){
        this.bote += cantidad;
    }
    public void incrementarDadosDobles(){
        this.vecesDoblesDados += 1;
    }
    
    public void removeFortuna(float cantidad){
        gastosTotales += cantidad;
        this.fortuna -= cantidad;
    }
    public void resetVueltas(){
        this.vueltas = 0;
    }

    public void encarcelar(){ 
        this.turnosEnLaCarcel = 0;
        this.encarcelado = true;
    }
    public String desencarcelar(float coste){
        if(coste == 0){
            return "El jugador ha sacado dados dobles, por lo que sale de la carcel.";
        }
        if(!this.puedePagar(coste)){
            this.preguntarHipBanc();
        }
        this.removeFortuna(coste);
        this.encarcelado = false;
        return "%s paga %.2f€ y sale de la cárcel. Puede lanzar los dados.".formatted(this.nombre,coste);
    }
    public boolean estaEncarcelado(){ return this.encarcelado; };
    public boolean puedePagar(float cantidad){
        return this.fortuna >= cantidad;
    }
    
    public void preguntarHipBanc(){
        System.out.printf("%s no tiene dinero suficiente, debe hipotecar alguna propiedad o declararse en bancarrota.\n",this.nombre);
        System.exit(0);
    }
    public String imprimirEstado(){
        return """
            \n\nSituacion de %s{
            fortuna:%.2f,
            propiedades:%s,
            gastos:%.2f
            }""".formatted(this.nombre,this.fortuna,this.propiedadesToString(),this.gastosTotales);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(obj == null){
            return false;
        }
        if(getClass() != obj.getClass()){
            return false;
        }
        final Jugador otro = (Jugador) obj;
        return this.nombre.equals(otro.getNombre());
    }

    @Override
    public String toString() {
        return """
        {
            nombre: %s,
            avatar: %c,
            fortuna: %.2f,
            propiedades: %s,
        }\n""".formatted(this.nombre,this.avatar.getId(),this.fortuna,this.propiedadesToString());
    }

    private String propiedadesToString(){
        String result = "[";
        Iterator<Casilla> it = this.propiedades.iterator();
        while(it.hasNext()){
            Casilla c = it.next();
            if(!it.hasNext()){
                result += c.getNombre();
            }else{
                result += c.getNombre() + ",";
            }
        }
        result += "]";
        return result;
    }
}
