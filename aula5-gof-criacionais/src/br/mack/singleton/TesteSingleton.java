package br.mack.singleton;

public class TesteSingleton {

    static void main() {

        Presidente p1 = Presidente.getInstance();
        System.out.println("P1: " + p1.getNome() + " - " + p1.getSalario()); // Alice

        Presidente p2 = Presidente.getInstance();
        p2.set("Thiago", 60000.0);

        System.out.println("P2: " + p2.getNome() + " - " + p2.getSalario());

        System.out.println("P1: " + p1.getNome() + " - " + p1.getSalario());

        System.out.println("Chamando o faz alguma coisa...");

        TesteSingleton2.fazAlgumaCoisa();


    }
}
