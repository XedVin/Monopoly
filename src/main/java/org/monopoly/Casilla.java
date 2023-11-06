package org.monopoly;
import org.monopoly.ColorString.Color;
import org.monopoly.Edificio.TipoEdificio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Casilla{
    // Se definen los tipos de casillas que hay en el tablero
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
        public static TipoCasilla aTipoCasilla(String s){
            for (TipoCasilla tipo : TipoCasilla.values()){
                if( tipo.getValue().equals(s)){
                    return tipo;
                }
            }
            return null;
        }

    }
    // Se definen los tipos de casillas especiales que hay en el tablero
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
        public static TipoCasillaEspecial aTipoCasillaEspecial(String s){
            for(TipoCasillaEspecial tipo: TipoCasillaEspecial.values()){
                if(tipo.getValue().equals(s)){
                    return tipo;
                }
            }
            return null;
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

    private float impuesto;

    private boolean hipotecada;
    private float valorHipoteca;

    private Map<TipoEdificio,List<Edificio>> edificios;
    // Se definen los atributos que tiene cada casilla
    public Casilla(String line,int pos){
        
        String[] props = line.split(" ");
        this.nombre = props[0];
        this.avatares = new ArrayList<>();
        this.tipo = TipoCasilla.aTipoCasilla(props[1]);
        this.pos = pos;

        switch(this.tipo){
            case SOLAR:
                this.grupo = aColor(props[2]);
                this.valor = Float.parseFloat(props[3]);
                this.alquiler = 0.1f*this.valor;
                break;
            case TRANSPORTE:
            case SERVICIOS:
            case COMUNIDAD:
            case SUERTE:
            case IMPUESTOS:
                this.grupo = Color.Blanco;
                break;
            case ESPECIAL:
                this.tipoEspecial = TipoCasillaEspecial.aTipoCasillaEspecial(props[2]);
                this.grupo = Color.Blanco;
        }
    }
    public Casilla(String nombre,String tipo,int pos){
        this.nombre = nombre;
        this.avatares = new ArrayList<>();
        this.tipo = TipoCasilla.aTipoCasilla(tipo);
        this.pos = pos;
        this.grupo = Color.Blanco;
    }
    public Casilla(String nombre,String tipo,String tipoEspecial,int pos){
        this.nombre = nombre;
        this.avatares = new ArrayList<>();
        this.tipo = TipoCasilla.aTipoCasilla(tipo);
        this.pos = pos;
        this.tipoEspecial = TipoCasillaEspecial.aTipoCasillaEspecial(tipoEspecial);
        this.grupo = Color.Blanco;
    }
    public Casilla(String nombre,String tipo,String grupo,float valorInicial,int pos){
        this.nombre = nombre;
        this.avatares = new ArrayList<>();
        this.tipo = TipoCasilla.aTipoCasilla(tipo);
        this.pos = pos;
        this.grupo = aColor(grupo);
        this.valor = valorInicial;
        this.alquiler = 0.1f*valorInicial;
        this.hipotecada = false;
        this.valorHipoteca = 0;
        this.edificios = new HashMap<>();
        for(TipoEdificio tipoEdificio: TipoEdificio.values()){
            this.edificios.put(tipoEdificio,new ArrayList<>());
        }

    }
    //GETTERS
    public String getNombre(){
        return this.nombre;
    }
    public Color getGrupo(){
        return this.grupo;
    }
    public TipoCasilla getTipo(){
        return this.tipo;
    }
    public TipoCasillaEspecial getTipoEspecial(){
        return this.tipoEspecial;
    }
    public int getPos(){
        return this.pos;
    }
    public int getCantidadAvataresEnCasilla(){
        return this.avatares.size();
    }
    public float getValor(){
        return this.valor;
    }
    public Jugador getPropietario(){
        return this.propietario;
    }
    public float getAlquiler(){
        return this.alquiler;
    }
    public int getNumeroEdificios(TipoEdificio tipo){
        return this.edificios.get(tipo).size();
    }
    public List<Edificio> getEdificios(){
        List<Edificio> edificios = new ArrayList<>();
        for(TipoEdificio tipo: TipoEdificio.values()){
            edificios.addAll(this.edificios.get(tipo));
        }
        return edificios;
    }
    //SETTERS
    public void setValor(float v) {
        if(v <0){
            this.valor=0;
            return;
        }
        this.valor = v;
    }
    public void setPropietario(Jugador j){
        this.propietario = j;
    }
    public void setImpuestos(float v){
        if(v < 0){
            this.impuesto = 0;
            return;
        }
        this.impuesto = v;
    }
    public void setAlquiler(float v){
        this.alquiler = v;
    }

    public void addAvatar(Avatar av){
        this.avatares.add(av);
    }
    public void eliminarAvatar(Avatar av){
        this.avatares.remove(av);
    }
    public boolean esCasillaEspecial(TipoCasillaEspecial esp){
        return this.tipo == TipoCasilla.ESPECIAL && this.tipoEspecial == esp;
    }
    public boolean esTipo(TipoCasilla t){
        return this.tipo == t;

    }
    public boolean esHipotecada(){
        return this.hipotecada;
    }


    private float calcularAlquiler(float multiplicador){
        switch(this.tipo){
            case SOLAR:
                float alquiler = this.alquiler + this.getAlquilerEdificios();
                return alquiler;
            case TRANSPORTE:
            case SERVICIOS:
                return this.alquiler * multiplicador;
            default:
                return 0;
        }
    }
    private float getAlquilerEdificios(){
        float alquiler = 0;
        int nCasas = this.getNumeroEdificios(TipoEdificio.CASA);
        for(Edificio e: this.getEdificios()){
            if(e.getTipo() != TipoEdificio.CASA){
                alquiler += this.alquiler * e.getMultiplicador();
            }
        }
        switch(nCasas){
            case 1:
                alquiler += 5*this.alquiler;
                break;
            case 2:
                alquiler += 15*this.valor;
                break;
            case 3:
                alquiler += 35*this.valor;
                break;
            case 4:
                alquiler += 50*this.valor;
                break;
        }
        return alquiler;
    }

    private String caerImpuestos(Jugador j){
        if(!j.puedePagar(this.impuesto)){
            j.preguntarHipBanc();
        }
        j.removeFortuna(this.impuesto);
        this.propietario.addBote(this.impuesto);
        return "El jugador paga %.2f€.".formatted(this.impuesto);
    }

    private String caerParking(Jugador j){
        float bote = this.propietario.getBote();
        j.addFortuna(bote);
        return "El jugador %s recibe %.2f€, el bote de la banca.".formatted(j.getNombre(),bote);
    }
    public String caer(Jugador j){
        switch(this.tipo){
            case IMPUESTOS:
                return caerImpuestos(j);
            case SUERTE:
            case COMUNIDAD:
                return "";
            case ESPECIAL:
                return this.tipoEspecial == TipoCasillaEspecial.PARKING ? caerParking(j) : ""; 
            default:
                return "";
        }
    }
    public String caer(Jugador j,float multiplicador){
        if(this.propietario.getNombre().equals("Banca")){
            return "";
        }
        float costeCaer = calcularAlquiler(multiplicador);

        if(!j.puedePagar(costeCaer)){
            j.preguntarHipBanc();
        }

        this.propietario.addFortuna(costeCaer);
        j.removeFortuna(costeCaer);
        return "Se han pagado %.2f€ de alquiler.".formatted(costeCaer);

    }

    public String edificar(TipoEdificio edificio,float coste){
        Edificio e = new Edificio(edificio,coste,this.getNumeroEdificios(edificio));
        switch(edificio){
            case CASA:
                if(!this.propietario.puedePagar(e.getValorInicial())){
                    return "No se puede edificar una casa en %s porque %s no puede pagarla.".formatted(this.nombre,this.propietario.getNombre());
                }
                this.propietario.removeFortuna(e.getValorInicial());
                this.edificios.get(edificio).add(e);
                return "Se ha edificado una casa en %s.".formatted(this.nombre);
            case HOTEL:
                if(!this.propietario.puedePagar(e.getValorInicial())){
                    return "No se puede edificar un hotel en %s porque %s no puede pagarla.".formatted(this.nombre,this.propietario.getNombre());
                }
                this.propietario.removeFortuna(e.getValorInicial());
                //Elimina cuatro casas de edificios
                for(int i = 0; i < 4; i++){
                    this.edificios.get(TipoEdificio.CASA).remove(0);
                }
                this.edificios.get(edificio).add(e);
                return "Se ha edificado un hotel en %s.".formatted(this.nombre);
            case PISCINA:
            case PISTA_DE_DEPORTE:
                if(!this.propietario.puedePagar(e.getValorInicial())){
                    return "No se puede edificar un %s en %s porque %s no puede pagarla.".formatted(edificio.getValue(),this.nombre,this.propietario.getNombre());
                }
                this.propietario.removeFortuna(e.getValorInicial());
                this.edificios.get(edificio).add(e);
                return "Se ha edificado un %s en %s.".formatted(edificio.getValue(),this.nombre);
        }
        return "";
    }

    @Override
    public String toString() {
        switch(this.tipo){
            case SOLAR:
                return """
                    {
                        tipo: %s,
                        grupo: %s,
                        propietario: %s,
                        valor: %.2f,
                        alquiler: %.2f,
                        edificios:%s
                    }""".formatted(this.tipo.getValue(),this.grupo,this.propietario.getNombre(),this.valor,this.alquiler,this.edificiosToString());
            case TRANSPORTE:
                return """
                {
                    tipo: %s,
                    propietario: %s,
                    valor: %.2f,
                    alquiler: %.2f
                }""".formatted(this.tipo.getValue(),this.propietario.getNombre(),this.valor,this.alquiler);
            case SERVICIOS:
                return """
                {
                    tipo:%s,
                    propietario:%s,
                    valor: %.2f
                }""".formatted(this.tipo.getValue(),this.propietario.getNombre(),this.valor);
            case IMPUESTOS:
                return """
                {
                    tipo: %s,
                    apagar: %.2f
                }""".formatted(this.tipo.getValue(),this.impuesto);
            case SUERTE:
            case COMUNIDAD:
                return """
                {
                    tipo:%s
                }""".formatted(this.tipo.getValue());
            case ESPECIAL:
                return """
                {
                    tipo:%s
                }""".formatted(this.tipoEspecial.getValue());
        }
        return "";
    }
    public String toString(Monopoly m){
        String lista = "";
        boolean esCarcel = this.tipoEspecial == TipoCasillaEspecial.CARCEL;
        for(Avatar av: this.avatares){
            Jugador j = m.getJugadorPorId(av.getId());
            lista += esCarcel ? "[%s,%d] ".formatted(j.getNombre(),j.getTurnosCarcel()) : "[%s] ".formatted(j.getNombre());
        }

        return """
        {
            %s:%.2f,
            jugadores: %s
        }""".formatted(esCarcel ? "salir" : "bote",esCarcel ? m.getPremioVuelta()*0.25f : this.propietario.getBote(),lista);
    }

    public String toString(int longitud,int numMaximoAvatares) {
        String nameSpan = " ".repeat(longitud - this.nombre.length());
        String avatarSpan = numMaximoAvatares == 0 ? "" : " ".repeat(numMaximoAvatares + 1);
        if (!this.avatares.isEmpty()){
            avatarSpan = "&";
            for(Avatar av: this.avatares){
                avatarSpan += av;
            }
        }
        return new ColorString(this.nombre + nameSpan + " " + avatarSpan,this.grupo) + "|";
    }


    // Sirve para convertir el nombre de un color en el propio color
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
            case "Morado":
                return Color.Morado;
            case "Cyan":
                return Color.Cyan;
            case "VerdeClaro":
                return Color.VerdeClaro;
            default:
                return Color.Blanco;
        }
    }
    @Override
    public boolean equals(Object other){
        if (this == other) return true;
        if (other == null || this.getClass() != other.getClass()) return false;
        Casilla c = (Casilla) other;
        return this.nombre.equals(c.getNombre());
    }


    private String edificiosToString(){
        String result = "[";
        for(Edificio e: this.getEdificios()){
            result += e;
            if(this.getEdificios().indexOf(e) != this.getEdificios().size() - 1){
                result += ", ";
            }
        }
        return result + "]";
    }
}
