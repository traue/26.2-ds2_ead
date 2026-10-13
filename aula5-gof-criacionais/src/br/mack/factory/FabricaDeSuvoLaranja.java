package br.mack.factory;

class FabricaDeSucoLaranja extends FabricaDeSuco {

    @Override
    public Suco fabricaSuco() {
        return new SucoLaranja();
    }

}
