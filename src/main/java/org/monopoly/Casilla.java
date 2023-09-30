package org.monopoly;

import org.example.Casilla.GrupoCasilla;
import org.example.Casilla.TipoCasilla;

public class Casilla{
    public enum TipoCasilla{
        SOLAR,
        TRANSPORTE,
        SERVICIOS,
        SUERTE,
        COMUNIDAD,
        IMPUESTOS,
        ESPECIAL
    }
    public enum GrupoCasilla{
        ROSA,
        AMARILLO,
        AZUL,
        ROJO
    }
    private String nombre;
    private TipoCasilla tipo;
    private GrupoCasilla grupo;
    private float valor;
    private float alquiler;
    Casilla(){
        this.nombre = "Solarx"; 
        this.tipo = TipoCasilla.SOLAR;
        this.grupo = GrupoCasilla.ROJO;
        this.valor = 1;
        this.alquiler = 1;
    }
    Casilla(String nombre,TipoCasilla tipo,GrupoCasilla grupo,float valor,float alquiler){
        this.nombre = nombre;
        this.tipo = tipo;
        this.grupo = grupo;
        this.valor = valor;
        this.alquiler = alquiler;
    }
    public String getNombre(){
        return this.nombre;
    }
}
