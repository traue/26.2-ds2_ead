package refatorado;

/*
 * =====================================================================================
 *  PASSO 6 - EXTRACT SUPERCLASS  (categoria: Dealing with Generalization)
 * =====================================================================================
 *
 *  PROBLEMA (bad smell): Duplicated Code.
 *      Depois do Passo 3, tínhamos duas classes - ProdutoIndustrializado e
 *      ProdutoManipulado - com atributos idênticos (codigo, descricao) e um método
 *      getDescricao() copiado e colado nas duas.
 *
 *  SOLUÇÃO:
 *      Criar uma superclasse com tudo o que é COMUM e declarar como ABSTRATO aquilo que
 *      existe nas duas, mas se comporta de forma diferente em cada uma.
 *
 *  Como decidir o que sobe para a superclasse:
 *      - atributo/método IDÊNTICO nas duas  -> sobe implementado (ex.: getDescricao)
 *      - existe nas duas, mas com regra diferente -> sobe como abstract (ex.: getPreco)
 *      - existe em só uma delas -> fica na subclasse (ex.: gramas, do manipulado)
 *
 *  GANHO: o resto do sistema (Venda, NotaFiscal) passa a conversar apenas com "Produto".
 *      Adicionar um ProdutoControlado amanhã não exige tocar em nenhuma dessas classes.
 *      Isso é o Princípio Aberto/Fechado aparecendo naturalmente como efeito da refatoração.
 */
public abstract class Produto {

    // protected: visível para as subclasses, invisível para o resto do sistema.
    protected final String codigo;
    protected final String descricao;

    protected Produto(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    /** Comportamento IDÊNTICO nas duas subclasses -> implementado aqui, uma vez só. */
    public String getDescricao() {
        return descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    /**
     * Comportamento que EXISTE nas duas, mas com regra própria em cada uma.
     * Declarado abstrato: a superclasse exige o método, a subclasse decide como calcular.
     */
    public abstract double getPreco();
}
