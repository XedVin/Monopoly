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
    public enum TipoCasillaEspecial{
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
    //TipoCasillaEspecial devolvera NULL si no es una casilla especial
    private int pos;
    private TipoCasillaEspecial tipoEspecial;
    private String nombre;
    private TipoCasilla tipo;
    private Jugador propietario;
    private Color grupo;
    private float valor;
    private float alquiler;
    private ArrayList<Avatar> avatares;

    public Casilla(String line,int pos){
        
        //EX: Madrid Especial Rojo Carcel
        String[] props = line.split(" ");
        
        this.nombre = props[0];
        this.tipo = aTipoCasilla(props[1]);
        this.grupo = aColor(props[2]);
        this.valor = 1;
        this.alquiler = 1;       
        if(this.tipo == TipoCasilla.ESPECIAL){
            this.tipoEspecial = aTipoCasillaEspecial(props[3]); 
        }
        this.avatares = new ArrayList<>();
        this.pos = pos;
    }

    
    public String getNombre(){
        return this.nombre;
    }
    public Color getGrupo(){
        return this.grupo;
    }
    public int getPos(){
        return this.pos;
    }
    public ArrayList<Avatar> getAvatares(){
        return this.avatares;
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
            case "Naranja":
                return Color.Naranja;
            case "Morado":
                return Color.Morado;
            case "Cyan":
                return Color.Cyan;
            case "Blanco":
                return Color.Blanco;
            case "GrisClaro":
                return Color.GrisClaro;
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
