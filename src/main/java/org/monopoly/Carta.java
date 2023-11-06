package org.monopoly;

public class Carta{

    public enum TipoCarta{
        Suerte,
        Comunidad
    }
    public enum AccionSuerte{
        GanarLoteria,
        VenderBillete,
        SerPresidente,
        AumentarImpuesto,
        MultarMovil,
        BeneficiarAcciones
    }
    public enum AccionComunidad{
        AlquilarVilla,
        AlquilarJet,
        PagarViaje,
        DevolverHacienda,
        BeneficiarCompanhia,
        PagarBalneario
    }
    
    private String descripcion;
    private TipoCarta tipo;

    private AccionSuerte accionSuerte;
    private AccionComunidad accionComunidad;

    public Carta(String descripcion, AccionSuerte accion){
        this.descripcion = descripcion;
        this.tipo = TipoCarta.Suerte;
        this.accionSuerte = accion;
    }
    public Carta(String descripcion, AccionComunidad accion){
        this.descripcion = descripcion;
        this.tipo = TipoCarta.Comunidad;
        this.accionComunidad = accion;
    }

    public String getDescripcion(){
        return this.descripcion;
    }

    public String realizarAccion(Monopoly m){
        switch(tipo){
            case Suerte:
                switch(accionSuerte){
                    case GanarLoteria:
                        m.getJugadorActual().addFortuna(1_000_000);
                        break;
                    case VenderBillete:
                        m.getJugadorActual().addFortuna(500_000);
                        break;
                    case SerPresidente:
                        serPresidente(m);
                        break;
                    case AumentarImpuesto:
                        m.getJugadorActual().addFortuna(250_000);
                        break;
                    case MultarMovil:
                        multarMovil(m);
                        break;
                    case BeneficiarAcciones:
                        beneficiarAcciones(m);
                        break;
                }
                break;
            case Comunidad:
                switch(accionComunidad){
                    case AlquilarVilla:
                        alquilarVilla(m);
                        break;
                    case PagarViaje:
                        pagarViaje(m);
                        break;
                    case AlquilarJet:
                        m.getJugadorActual().addFortuna(1_000_000);
                        break;
                    case DevolverHacienda:
                        m.getJugadorActual().addFortuna(500_000);
                        break;
                    case BeneficiarCompanhia:
                        m.getJugadorActual().addFortuna(2_000_000);
                        break;
                    case PagarBalneario:
                        pagarBalneario(m);
                        break;


                }
        }
        return this.descripcion;
    }


    //Acciones cartas suerte
    private void serPresidente(Monopoly m){
        int N = m.getJugadores().size()-1;
        Jugador jA = m.getJugadorActual();
        if(!jA.puedePagar(N*250_000)){
            //TODO BANCARROTA
            jA.preguntarHipBanc();
        }
        
        for( Jugador j : m.getJugadores()){
            if(!jA.equals(j)){
                jA.removeFortuna(250_000);
                j.addFortuna(250_000);
            }
        }
    }

    private void aumetarImpuesto(Monopoly m){
        Jugador jA = m.getJugadorActual();

        //TODO cuando esten los edificios
    }

    private void multarMovil(Monopoly m){
        Jugador j = m.getJugadorActual();
        if(!j.puedePagar(150_000)){
            j.preguntarHipBanc();
        }

        j.removeFortuna(150_000);
        m.getBanca().addBote(500_000);
    }
    private void beneficiarAcciones(Monopoly m){
        m.getJugadorActual().addFortuna(1_500_00);
    }

    //Acciones cartas comunidad

    private void alquilarVilla(Monopoly m){
        int N = m.getJugadores().size()-1;
        Jugador jA = m.getJugadorActual();
        if(!jA.puedePagar(N*200_000)){
            //TODO BANCARROTA
            jA.preguntarHipBanc();
        }
        
        for( Jugador j : m.getJugadores()){
            if(!jA.equals(j)){
                jA.removeFortuna(200_000);
                j.addFortuna(200_000);
            }
        }
    }

    private void pagarViaje(Monopoly m){
        Jugador j = m.getJugadorActual();
        if(!j.puedePagar(1_000_000)){
            j.preguntarHipBanc();
        }
        j.removeFortuna(1_000_000);
        m.getBanca().addBote(1_000_000);
    }

    private void pagarBalneario(Monopoly m){
        Jugador j = m.getJugadorActual();
        if(!j.puedePagar(500_000)){
            j.preguntarHipBanc();
        }
        j.removeFortuna(500_000);
        m.getBanca().addBote(500_000);

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carta carta = (Carta) o;
        return this.descripcion.equals(carta.descripcion) && this.tipo == carta.tipo;
    }
}
