package refatorado;

/*
 * PASSO 3 - REPLACE ARRAY WITH OBJECT (nasce aqui)
 * PASSO 6 - EXTRACT SUPERCLASS      (passa a estender Produto)
 *
 * Fórmula manipulada na própria farmácia. O preço tem regra própria:
 *
 *      preço = (preço por grama x gramas da fórmula) + taxa de manipulação
 *
 * Compare com o código legado: lá essa regra vivia dentro de um "if" no meio do método
 * de impressão da nota fiscal, e a coluna "gramas" existia como "0" para todo produto
 * industrializado - um campo que não fazia sentido para metade do catálogo.
 *
 * Aqui, o atributo 'gramas' existe SÓ na classe onde ele tem significado.
 */
public class ProdutoManipulado extends Produto {

    /**
     * Replace Magic Number with Symbolic Constant: no legado esse 8.00 era uma
     * constante solta na classe do sistema, longe do conceito a que pertence.
     */
    private static final double TAXA_MANIPULACAO = 8.00;

    private final double precoPorGrama;
    private final double gramas;

    public ProdutoManipulado(String codigo, String descricao, double precoPorGrama, double gramas) {
        super(codigo, descricao);
        this.precoPorGrama = precoPorGrama;
        this.gramas = gramas;
    }

    /** Regra de preço desta subclasse: a fórmula fica encapsulada onde ela pertence. */
    @Override
    public double getPreco() {
        return precoPorGrama * gramas + TAXA_MANIPULACAO;
    }
}
