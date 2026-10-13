package br.mack.factory;

public class TesteFactory {
    public static void main(String[] args) {

        FabricaDeSuco fabrica = new FabricaDeSucoMaca();
        Suco s = fabrica.fabricaSuco();
        System.out.println("Sabor: " + s.getSabor()); // Sabor: Maca

        FabricaDeSuco fabrica2 = new FabricaDeSucoLaranja();
        Suco s2 = fabrica2.fabricaSuco();
        System.out.println("Sabor: " + s2.getSabor()); // Sabor: Maca
    }
}
