package org.monopoly;


//Decidir como se va hacer con el valor de los edificios y el alquiler

public class Edificio{

    public enum TipoEdificio{
        CASA("casa"),
        HOTEL("hotel"),
        PISCINA("piscina"),
        PISTA_DE_DEPORTE("pista");
        private String value;

        private TipoEdificio(String value){
            this.value = value;
        }
        public String getValue(){
            return value;
        }
        public static TipoEdificio aTipoEdificio(String s){
            for (TipoEdificio tipo : TipoEdificio.values()){
                if( tipo.getValue().equals(s)){
                    return tipo;
                }
            }
            return null;
        }
    }

    private String nombre;
    private TipoEdificio tipo;
    private float valorInicial;
    private int multiplicador;

    public Edificio(TipoEdificio tipo, float valorInicial, int nEdificio){
        this.nombre = tipo.getValue() + "-" + (nEdificio + 1) ;
        this.tipo = tipo;
        switch(tipo){
            case CASA:
                this.valorInicial = 0.6f * valorInicial;
                break;
            case HOTEL:
                this.valorInicial = 0.6f * valorInicial;
                this.multiplicador = 70;
                break;
            case PISCINA:
                this.valorInicial = 0.4f * valorInicial;
                this.multiplicador = 25;
                break;
            case PISTA_DE_DEPORTE:
                this.valorInicial = 1.25f * valorInicial;
                this.multiplicador = 25;
                break;
        }
    }

    public float getValorInicial(){
        return this.valorInicial;
    }
    public float getMultiplicador(){
        return this.multiplicador;
    }
    public TipoEdificio getTipo(){
        return this.tipo;
    }

    public String toString(Casilla c){
        return """
                {
                    id: %s,
                    propietario: %s,
                    casilla:%s,
                    grupo: %s,
                    coste: %s
                }
                """.formatted(nombre,c.getPropietario().getNombre(),c.getNombre(),c.getGrupo().toString(),valorInicial);
    }
    @Override
    public String toString(){
        return this.nombre;
    }
}
