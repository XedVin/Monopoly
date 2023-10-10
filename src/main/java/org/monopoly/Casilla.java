package org.monopoly;
import org.monopoly.ColorString.Color;

import java.util.ArrayList;
import java.util.Objects;

public class Casilla{
    public enum TipoCasilla{
        SOLAR("Solar"),
        TRANSPORTE("Transporte"),
        SERVICIOS("Servicios"),
        SUERTE("Suerte"),
        COMUNIDAD("Comunidad"),
        IMPUESTOS("Impuestos"),
        ESPECIAL("Especial");

        private String value;

        private TipoCasilla(String value){
            this.value = value;
        }
        public String getValue(){
            return value;
        }

    }
    public static enum TipoCasillaEspecial{
        CARCEL("Carcel"),
        PARKING("Parking"),
        SALIDA("Salida"),
        IRCARCEL("IrCarcel");

        private String v;
        private TipoCasillaEspecial(String v){
            this.v = v;
        }
        public String getValue(){
            return v;
        }
    }
    private int pos;
    private String nombre;
    
    private TipoCasilla tipo;
    private TipoCasillaEspecial tipoEspecial;
    
    private Jugador propietario;
    
    private Color grupo;
    private float valor;
    
    private float alquiler;
    
    private ArrayList<Avatar> avatares;

    private float valorCasa;
    private float valorHotel;
    private float valorPiscina;
    private float valorPistaDeporte;

    public Casilla(String line,int pos){
        
        String[] props = line.split(" ");
        this.nombre = props[0];
        this.avatares = new ArrayList<>();
        this.tipo = aTipoCasilla(props[1]);
        this.pos = pos;

        switch(this.tipo){
            case SOLAR:
                this.grupo = aColor(props[2]);
                this.valor = Float.parseFloat(props[3]);
                this.alquiler = 0.1f*this.valor;
                this.valorCasa = 0.6f*this.valor;
                this.valorHotel = 0.6f*this.valor;
                this.valorPiscina = 0.4f*this.valor;
                this.valorPistaDeporte = 1.25f*this.valor;
                break;
            case TRANSPORTE:
            case SERVICIOS:
            case COMUNIDAD:
            case SUERTE:
            case IMPUESTOS:
                this.grupo = Color.Blanco;
                break;
            case ESPECIAL:
                this.tipoEspecial = aTipoCasillaEspecial(props[2]);
                this.grupo = Color.Blanco;
        }
    }
    public String getNombre(){
        return this.nombre;
    }
    public Color getGrupo(){
        return this.grupo;
    }
    public TipoCasilla getTipo(){
        return this.tipo;
    }
    public int getPos(){
        return this.pos;
    }
    public ArrayList<Avatar> getAvatares(){
        return this.avatares;
    }
    public float getValor(){
        return this.valor;
    }

    public boolean esSalida(){
        return this.tipoEspecial != null && this.tipoEspecial == TipoCasillaEspecial.SALIDA;
    }
    public void añadirAvatar(Avatar av){
        this.avatares.add(av);
    }
    public void eliminarAvatar(Avatar av){
        this.avatares.remove(av);
    }
    public void setPropietario(Jugador j){
        this.propietario = j;
    }



    @Override
    public String toString() {

        return """
        {
            tipo: %s,
            grupo: %s,
            propietario: %s,
            valor: %f,
            alquiler: %f
        }""".formatted(this.tipo,this.grupo,this.propietario != null ? this.propietario.getNombre() : "Nadie",this.valor,this.alquiler);
    }

    public String toString(int longitud,int numMaximoAvatares) {
        String nameSpan = " ".repeat(longitud - this.nombre.length());
        String avatarSpan = numMaximoAvatares == 0 ? "" : " ".repeat(numMaximoAvatares + 1);
        if (this.avatares.size() != 0){
            avatarSpan = "&";
            for(Avatar av: this.avatares){
                avatarSpan +=av.getId();
            }
        }
        return new ColorString(this.nombre + nameSpan + " " + avatarSpan).setColor(this.grupo) + "|";
    }

    private Color aColor(String s){
        switch (s) {
            case "Rojo":
                return Color.Rojo;
            case "Verde":
                return Color.Verde;
            case "Amarillo":
                return Color.Amarillo;
            case "Azul":
                return Color.Azul;
            case "Rosa":
                return Color.Rosa;
            /*case "Naranja":
                return Color.Naranja;*/
            case "Morado":
                return Color.Morado;
            case "Cyan":
                return Color.Cyan;
            case "Blanco":
                return Color.Blanco;
            /*case "GrisClaro":
                return Color.GrisClaro;*/
            case "VerdeClaro":
                return Color.VerdeClaro;
            default:
                return Color.Blanco;
        }
    }
    public TipoCasilla aTipoCasilla(String v){
        for(TipoCasilla t: TipoCasilla.values()){
            if(Objects.equals(v,t.getValue())){
                return t;
            }
        }
        return TipoCasilla.SOLAR; // TODO Valor de facto
    }
    private TipoCasillaEspecial aTipoCasillaEspecial(String s){
        for(TipoCasillaEspecial t: TipoCasillaEspecial.values()){
            if(Objects.equals(s,t.getValue())){
                return t;
            }
        }
        return TipoCasillaEspecial.CARCEL; // TODO Valor de facto

    }
}
