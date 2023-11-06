package org.monopoly;

import java.util.ArrayList;
import java.util.List;

public class Baraja{
    private List<Carta> cartasComunidad;
    private List<Carta> cartasSuerte;

    public Baraja(){
        ArrayList<Carta> cartasSuerte = new ArrayList<>();
        ArrayList<Carta> cartasComunidad = new ArrayList<>();
        
        cartasSuerte.add(new Carta("¡Has ganado el bote de la lotería! Recibe 1000000€", Carta.AccionSuerte.GanarLoteria));
        cartasSuerte.add(new Carta("Vendes tu billete de avión para Cádiz en una subasta por Internet. Cobra 500000€", Carta.AccionSuerte.VenderBillete));
        cartasSuerte.add(new Carta("¡Has sido elegido presidente de la junta directiva! Paga a cada jugador 250000€", Carta.AccionSuerte.SerPresidente));
        cartasSuerte.add(new Carta("El ayuntamiento sube los impuestos. Paga 250000€", Carta.AccionSuerte.AumentarImpuesto));
        cartasSuerte.add(new Carta("Te multan por usar el móvil mientras conduces. Paga 250000€", Carta.AccionSuerte.MultarMovil));
        cartasSuerte.add(new Carta("Beneficio por la venta de tus acciones. Recibe 1500000€", Carta.AccionSuerte.BeneficiarAcciones));


        cartasComunidad.add(new Carta("Alquilas a tus compañeros una villa en Cannes durante una semana. Paga 200000€ a cada jugador.", Carta.AccionComunidad.AlquilarVilla));
        cartasComunidad.add(new Carta("Recibe 1000000€ de beneficios por alquilar los servicios de tu jet privado.", Carta.AccionComunidad.AlquilarJet));
        cartasComunidad.add(new Carta("Paga 500000€ por un fin de semana en un balneario de 5 estrellas.", Carta.AccionComunidad.PagarBalneario));
        cartasComunidad.add(new Carta("Devolución de Hacienda. Recibe 500000€", Carta.AccionComunidad.DevolverHacienda));
        cartasComunidad.add(new Carta("Tu compañía de Internet obtiene beneficios. Recibe 2000000€.", Carta.AccionComunidad.BeneficiarCompanhia));
        cartasComunidad.add(new Carta("Paga 1000000€ por invitar a todos tus amigos a un viaje a León.", Carta.AccionComunidad.PagarViaje));


        this.cartasSuerte = cartasSuerte;
        this.cartasComunidad = cartasComunidad;
    }

    private void barajarCartasSuerte(){
        ArrayList<Integer> indices = new ArrayList<>(this.cartasSuerte.size());
        ArrayList<Carta> nuevasCartas = new ArrayList<>(this.cartasSuerte.size());
        for(int i = 0; i < this.cartasSuerte.size(); i++){
            indices.add(i);
        }
        for(Carta c : this.cartasSuerte){
            int indice = (int) (Math.random() * indices.size());
            nuevasCartas.add(indice,c);
            indices.remove(indice);
        }
        this.cartasSuerte = nuevasCartas;

    }
    private void barajarCartasComunidad(){
        ArrayList<Integer> indices = new ArrayList<>(this.cartasComunidad.size());
        ArrayList<Carta> nuevasCartas = new ArrayList<>();
        for(int i = 0; i < this.cartasComunidad.size() + 1; i++){
            indices.add(i);
            nuevasCartas.add(null);
        }
        for(Carta c : this.cartasSuerte){
            int indice = (int) (Math.random() * indices.size());
            nuevasCartas.set(indices.get(indice),c);
            indices.remove(indice);
        }
        this.cartasComunidad = nuevasCartas;

    }

    public String elegirCartaSuerte(int i,Monopoly m){
        barajarCartasSuerte();
        Carta c = this.cartasSuerte.get(i);
        return c.realizarAccion(m);
    }
    public String elegirCartaComunidad(int i,Monopoly m){
        barajarCartasComunidad();
        Carta c = this.cartasComunidad.get(i);
        return c.realizarAccion(m);
    }
}
