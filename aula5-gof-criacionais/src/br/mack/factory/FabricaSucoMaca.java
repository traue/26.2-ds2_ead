package br.mack.factory;

class FabricaDeSucoMaca extends FabricaDeSuco {

    @Override
    public Suco fabricaSuco() {
        return new SucoMaca();
    }

}