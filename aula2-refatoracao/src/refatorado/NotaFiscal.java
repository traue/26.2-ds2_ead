package refatorado;

import java.util.Locale;

/*
 * =====================================================================================
 *  PASSO 1 - EXTRACT METHOD  (categoria: Composing Methods)
 * =====================================================================================
 *
 *  PROBLEMA (bad smell): Long Method.
 *      No legado, um único método de ~50 linhas fazia cabeçalho + dados do cliente +
 *      itens + cálculo + desconto + rodapé + baixa de estoque. Para entender qualquer
 *      parte, era preciso ler o todo.
 *
 *  SOLUÇÃO: mover cada bloco lógico para um método próprio, com nome que diz o QUE ele
 *      faz. O método original vira um resumo legível da operação.
 *
 *  A REGRA PRÁTICA: se você sente vontade de escrever um comentário do tipo
 *      "// agora imprime o cabeçalho"
 *  ...esse comentário é o nome de um método esperando para nascer.
 *
 *  GANHO: imprimeCabecalho() e imprimeRodape() agora podem ser reaproveitados por um
 *      recibo, um orçamento ou uma segunda via - sem copiar e colar uma linha sequer.
 *
 *  Bônus desta classe: EXTRACT CLASS também aconteceu aqui. A responsabilidade de
 *  IMPRIMIR saiu do sistema de vendas. Vender e imprimir são assuntos diferentes.
 */
public class NotaFiscal {

    private static final Locale BR = Locale.forLanguageTag("pt-BR");
    private static final String LINHA_DUPLA = "=======================================";
    private static final String LINHA_SIMPLES = "---------------------------------------";

    private final Venda venda;
    private final String dataDaCompra;

    public NotaFiscal(Venda venda, String dataDaCompra) {
        this.venda = venda;
        this.dataDaCompra = dataDaCompra;
    }

    /**
     * O método público agora cabe na tela e se lê quase como uma frase em português.
     * Este é o objetivo do Extract Method: o método de alto nível conta a HISTÓRIA,
     * os métodos privados contam os DETALHES.
     */
    public void imprimir() {
        imprimeCabecalho();
        imprimeDadosDoCliente();
        imprimeItens();
        imprimeTotais();
        imprimeRodape();
    }

    // ---------------------------------------------------------------------------------
    // Cada bloco extraído vira um método privado, pequeno e com nome autoexplicativo.
    // ---------------------------------------------------------------------------------

    private void imprimeCabecalho() {
        System.out.println(LINHA_DUPLA);
        System.out.println("         FARMACIA REFATORADA");
        System.out.println("              Nota Fiscal");
        System.out.println("         CNPJ: 80.970.084/0001-25"); // inventado
        System.out.println(LINHA_DUPLA);
    }

    private void imprimeDadosDoCliente() {
        Cliente cliente = venda.getCliente();
        System.out.println("Cliente: " + cliente.getNome());
        System.out.println("CPF: " + cliente.getCpf());
        System.out.println("Endereco:");
        // A nota fiscal não sabe COMO formatar um endereço - ela pergunta ao endereço.
        System.out.println(cliente.getEndereco().formatado());
        System.out.println(LINHA_SIMPLES);
    }

    private void imprimeItens() {
        for (ItemVenda item : venda.getItens()) {
            // Sem "if (tipo == MANIPULADO)": o polimorfismo resolve o preço.
            System.out.println(String.format(BR, "%d x %-28s R$ %8.2f",
                    item.getQuantidade(),
                    item.getProduto().getDescricao(),
                    item.getSubtotal()));
        }
        System.out.println(LINHA_SIMPLES);
    }

    private void imprimeTotais() {
        System.out.println(String.format(BR, "Subtotal: R$ %.2f", venda.getSubtotal()));

        // Única condicional que sobrou - e ela pergunta sobre COMPORTAMENTO, não sobre null.
        Convenio convenio = venda.getCliente().getConvenio();
        if (convenio.deveAparecerNaNota()) {
            System.out.println(String.format(BR, "Convenio %s (%.0f%%): -R$ %.2f",
                    convenio.getNome(),
                    convenio.getPercentualDesconto() * 100,
                    venda.getDesconto()));
        }

        System.out.println(String.format(BR, "TOTAL: R$ %.2f", venda.getTotal()));
    }

    private void imprimeRodape() {
        System.out.println(LINHA_DUPLA);
        System.out.println("Obrigado! Volte sempre!");
        System.out.println("Data da compra: " + dataDaCompra);
        System.out.println(LINHA_DUPLA);
        System.out.println();
    }
}
