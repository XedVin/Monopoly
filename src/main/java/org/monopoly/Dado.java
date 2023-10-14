package org.monopoly;
import java.util.Random;
import java.util.Scanner;

public class Dado {
    private boolean fueronDobles;
    private int lanzarDado() {

        Random random = new Random();

        int aleatorio = random.nextInt(6);

        // Devuelve un valor entre 1 y 6
        return aleatorio+1;
    }


    public boolean getFueronDobles(){
        return this.fueronDobles;
    }
    public int lanzar(){
        int d1 = lanzarDado();
        int d2 = lanzarDado();

        this.fueronDobles = d1 == d2;
        return d1+d2;
    }
    public int debugLanzar(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Dado 1:");
        int d1 = sc.nextInt();
        System.out.println("Dado 2:");
        int d2 = sc.nextInt();
        this.fueronDobles = d1 == d2;
        return d1+d2;
    }

}
