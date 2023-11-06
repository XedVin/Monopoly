package org.monopoly;
import java.io.File;

public class Main{
    public static void main(String[] args) {
            Monopoly m = new Monopoly(new File("tablero"));
            m.run();
   }
}


