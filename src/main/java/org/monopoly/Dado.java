package org.monopoly;
import java.util.Random;

public class Dado {

    public int lanzar() {

        Random random = new Random();

        int aleatorio = random.nextInt(6);

        // Devuelve un valor entre 1 y 6
        return aleatorio+1;

    }

    public int resultado() {

        // primer lanzamiento
        int x = lanzar();
        int y = lanzar();
        if(x != y){
            return x+y;
        }

        // segundo lanzamiento
        x = lanzar();
        y = lanzar();
        if(x != y){
            return x+y;
        }

        // tercer lanzamiento
        x = lanzar();
        y = lanzar();
        if(x != y){
            return x+y;
        }

        // si se repite tres veces se va a la cárcel
        return -1;
    }

    // función para ver quien empieza al principio de la partida

    // función para ver si se sale de la cárcel

}
