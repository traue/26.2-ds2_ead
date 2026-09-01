package refatorado;

/*
 * Classe nascida do PASSO 3 (Replace Array with Object).
 *
 * No legado, um item de venda era a combinação de dois arrays paralelos passados como
 * parâmetro: int[] indices e int[] quantidades. Quem garantia que os dois tinham o mesmo
 * tamanho? Ninguém. Quem garantia que indices[2] correspondia a quantidades[2]?
 * A disciplina de quem chamava o método.
 *
 * Aqui, produto e quantidade são um par indissociável - e o item sabe calcular o próprio
 * subtotal, em vez de deixar essa conta espalhada pelo sistema.
 */
public class ItemVenda {

    private final Produto produto;
    private final int quantidade;

    public ItemVenda(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    /**
     * Polimorfismo em ação: este método não sabe (nem precisa saber) se o produto é
     * manipulado ou industrializado. Não há um único "if" aqui.
     */
    public double getSubtotal() {
        return produto.getPreco() * quantidade;
    }
}
