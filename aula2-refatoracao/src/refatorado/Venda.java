package refatorado;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * =====================================================================================
 *  PASSO 5 - SEPARATE QUERY FROM MODIFIER  (categoria: Simplifying Method Calls)
 * =====================================================================================
 *
 *  PROBLEMA (bad smell): um método que MODIFICA o estado do objeto e, ao mesmo tempo,
 *      DEVOLVE um valor. No legado, emitirNotaFiscal() dava baixa no estoque, imprimia a
 *      nota e ainda retornava o total. Consequências:
 *        - não dá para consultar o total sem provocar efeitos colaterais;
 *        - não dá para chamar o método duas vezes (o estoque cairia duas vezes);
 *        - testar exige capturar a saída do console.
 *
 *  SOLUÇÃO: dividir em dois tipos de método, com uma regra simples:
 *
 *        MODIFIER (comando): altera estado, retorna void   -> adicionarItem()
 *        QUERY   (consulta): não altera nada, retorna valor-> getSubtotal(), getTotal()
 *
 *      Essa separação tem nome na literatura: Command-Query Separation (Bertrand Meyer).
 *
 *  GANHO: as queries podem ser chamadas quantas vezes você quiser, em qualquer ordem,
 *      sem medo. Isso é o que torna o código previsível - e testável.
 */
public class Venda {

    private final Cliente cliente;
    private final List<ItemVenda> itens = new ArrayList<>();

    public Venda(Cliente cliente) {
        this.cliente = cliente;
    }

    // ---------------------------------------------------------------------------------
    // MODIFIER: altera o estado do objeto. Retorna void - de propósito.
    // ---------------------------------------------------------------------------------
    public void adicionarItem(Produto produto, int quantidade) {
        itens.add(new ItemVenda(produto, quantidade));
    }

    // ---------------------------------------------------------------------------------
    // QUERIES: só leem. Podem ser chamadas 1 ou 100 vezes, sempre com o mesmo resultado.
    // ---------------------------------------------------------------------------------

    public Cliente getCliente() {
        return cliente;
    }

    /** Encapsulate Collection: devolve uma visão imutável, para ninguém alterar a lista por fora. */
    public List<ItemVenda> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public double getSubtotal() {
        double subtotal = 0.0;
        for (ItemVenda item : itens) {
            subtotal += item.getSubtotal();
        }
        return subtotal;
    }

    /** Sem null check: o ConvenioNulo devolve 0.0 e a conta simplesmente funciona. */
    public double getDesconto() {
        return getSubtotal() * cliente.getConvenio().getPercentualDesconto();
    }

    public double getTotal() {
        return getSubtotal() - getDesconto();
    }
}
