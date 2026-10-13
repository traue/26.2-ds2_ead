package br.mack.singleton;

public class TesteSingleton2 {

    public static void fazAlgumaCoisa() {

        int x = 10;
        // ...

        Presidente y = Presidente.getInstance();
        System.out.println("p: " + y.getNome() + " - " + y.getSalario());

    }
}
