package refatorado;

/*
 * PASSO 3 - REPLACE ARRAY WITH OBJECT (nasce aqui)
 * PASSO 6 - EXTRACT SUPERCLASS      (passa a estender Produto)
 *
 * Produto pronto, comprado da indústria: o preço é simplesmente o preço de etiqueta.
 * Repare que agora o preço é um double de verdade - não uma String que precisa de
 * Double.parseDouble() toda vez que alguém quiser somar dois itens.
 */
public class ProdutoIndustrializado extends Produto {

    private final double precoUnitario;

    public ProdutoIndustrializado(String codigo, String descricao, double precoUnitario) {
        super(codigo, descricao); // codigo e descricao agora moram na superclasse
        this.precoUnitario = precoUnitario;
    }

    /** Regra de preço desta subclasse: valor de etiqueta, direto. */
    @Override
    public double getPreco() {
        return precoUnitario;
    }
}
