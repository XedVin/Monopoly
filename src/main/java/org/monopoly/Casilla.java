package org.monopoly;
import org.monopoly.ColorString.Color;

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
    public enum TipoCasillaEspecial{
        CARCEL,
        PARKING,
        SALIDA,
        IRCARCEL
    }
    //TipoCasillaEspecial devolvera NULL si no es una casilla especial
    private TipoCasillaEspecial tipoEspecial;
    private String nombre;
    private TipoCasilla tipo;
    private Color grupo;
    private float valor;
    private float alquiler;

    /** 
     *  Genera una nueva casilla segun el siguiente formato: <NombreCasilla> <TipoCasilla> <GrupoCasilla> <Opcional:TipoCasillaEspecial>
     *  @param line linea de texto en el formato correcto
     *
     * */
    public Casilla(String line){
        String[] props = line.split(" ");

        this.nombre = props[0];
        this.tipo = toTipoCasilla(props[1]);
        this.grupo = toColor(props[2]);
        this.valor = 1;
        this.alquiler = 1;       
        if(this.tipo == TipoCasilla.ESPECIAL){
            this.tipoEspecial = toTipoCasillaEspecial(props[3]); 
        }
    }
    /** 
     *  @return Devuelve el nombre de la casilla
     * */
    public String getNombre(){
        return this.nombre;
    }
    /** 
     *  @return Devuelve el grupo de la casilla
     * */
    public Color getGrupo(){
        return this.grupo;
    }
    /** 
     *  @return Devuelve si es la salida o no
     * */
    public boolean esSalida(){
        return this.tipoEspecial != null && this.tipoEspecial == TipoCasillaEspecial.SALIDA;
    }

    @Override
    public String toString() {
        return this.getNombre();
    }
    /** 
     *  Devuelve el grupo de una casilla segun una cadena de texto
     *  TODO errores
     * */
    private Color toColor(String s){
        switch(s){
            case "Rojo":
                return Color.Rojo;
            case "Verde":
                return Color.Verde;
            case "Amarillo":
                return Color.Amarillo; 
            case "Azul":
                return Color.Azul; 
            case "Negro":
                return Color.Negro; 
            case "Blanco":
                return Color.Blanco; 
            case "Cian":
                return Color.Cian; 
            case "Magenta":
                return Color.Magenta;
            default:
                return null;
        }

    }
    private TipoCasilla toTipoCasilla(String s){
        switch(s){
            case "Solar":
                return TipoCasilla.SOLAR;
            case "Transporte":
                return TipoCasilla.TRANSPORTE;
            case "Suerte":
                return TipoCasilla.SUERTE; 
            case "Especial":
                return TipoCasilla.ESPECIAL; 
            case "Comunidad":
                return TipoCasilla.COMUNIDAD; 
            case "Impuestos":
                return TipoCasilla.IMPUESTOS; 
            case "Servicios":
                return TipoCasilla.SERVICIOS; 
            default:
                return TipoCasilla.SOLAR;
        }

    }
    private TipoCasillaEspecial toTipoCasillaEspecial(String s){
        switch(s){
            case "Carcel":
                return TipoCasillaEspecial.CARCEL;
            case "Parking":
                return TipoCasillaEspecial.PARKING;
            case "Salida":
                return TipoCasillaEspecial.SALIDA;
            case "IrCarcel":
                return TipoCasillaEspecial.IRCARCEL;
            default:
                // TODO
                return TipoCasillaEspecial.CARCEL;
        }

    }
}
