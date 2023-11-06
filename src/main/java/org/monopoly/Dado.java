package org.monopoly;
import java.util.Random;

public class Dado {
    private boolean fueronDobles;
    private int valor1;
    private int valor2;
    public Dado(){
        this.fueronDobles = false;
        valor1 = 0;
        valor2 = 0;
    }
    public Dado(int v1,int v2){
        this.valor1 = v1;
        this.valor2 = v2;
        this.fueronDobles = v1 == v2;
    }

    // Sirve para lanzar un dado y que nos devuelva un número entre 1 y 6
    private int lanzarDado() {

        Random random = new Random();

        int aleatorio = random.nextInt(6);

        // Devuelve un valor entre 1 y 6
        return aleatorio+1;
    }

    public boolean getFueronDobles(){
        return this.fueronDobles;
    }

    // Sirve para lanzar los dos dados, lo que sucede en cada tirada del juego
    public int lanzar(){
        this.valor1 = lanzarDado();
        this.valor2 = lanzarDado();

        this.fueronDobles = this.valor1 == this.valor2;
        return this.valor1 + this.valor2;
    }
    public int debugLanzar(){
        return this.valor1 + this.valor2;
    }
}
