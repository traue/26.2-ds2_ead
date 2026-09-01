package refatorado;

/*
 * =====================================================================================
 *  RESULTADO FINAL: o mesmo cenário do código legado, depois das 6 refatorações.
 * =====================================================================================
 *
 *  Compare este arquivo com legado/SistemaFarmaciaLegado.java:
 *
 *    legado                                   refatorado
 *    -----------------------------------------------------------------------------
 *    1 classe grande + 1 classe de dados      11 classes pequenas e coesas
 *    método de ~50 linhas                     nenhum método de negócio passa de 10 linhas
 *    matriz String[][] + int[] paralelo       objetos Produto e Estoque
 *    2 verificações de null                   0 verificações de null
 *    2 "ifs" por tipo de produto              0 - polimorfismo
 *    query e modifier no mesmo método         comandos e consultas separados
 *    -----------------------------------------------------------------------------
 *    SAÍDA NO CONSOLE ...................... EXATAMENTE A MESMA
 *
 *  Essa última linha é a definição de refatoração.
 */
public class SistemaFarmaciaRefatorado {

    private static final String DATA_DA_COMPRA = "01/09/2026";

    private final Estoque estoque = new Estoque();

    // O catálogo agora é um conjunto de objetos com significado, não uma matriz de texto.
    private final Produto dipirona =
            new ProdutoIndustrializado("1001", "Dipirona 500mg", 12.90);
    private final Produto xarope =
            new ProdutoIndustrializado("1002", "Xarope de Guaco 120ml", 28.50);
    private final Produto pomada =
            new ProdutoManipulado("2001", "Pomada de Arnica", 0.85, 60);

    public SistemaFarmaciaRefatorado() {
        estoque.cadastrar(dipirona, 10);
        estoque.cadastrar(xarope, 5);
        estoque.cadastrar(pomada, 3);
    }

    /**
     * MODIFIER: dá baixa no estoque. Não imprime nada, não devolve nada.
     * Repare que "registrar a venda" e "imprimir a nota" viraram duas operações
     * independentes - podemos fazer uma sem a outra.
     */
    public void registrarVenda(Venda venda) {
        for (ItemVenda item : venda.getItens()) {
            estoque.darBaixa(item.getProduto(), item.getQuantidade());
        }
    }

    public void rodarCenario() {
        // --- Cliente COM convênio -------------------------------------------------------
        Cliente marina = new Cliente(
                "Marina Souza",
                "123.456.789-00",
                new Endereco("Rua Itambe", 45, "apto 71", "Consolacao", "Sao Paulo", "SP", "01239-001"),
                new Convenio("MackSaude", 0.15));

        // --- Cliente SEM convênio -------------------------------------------------------
        // Nenhum null aqui: o construtor sem convênio já instala um ConvenioNulo.
        Cliente carlos = new Cliente(
                "Carlos Lima",
                "987.654.321-00",
                new Endereco("Av. Paulista", 1200, "sala 12", "Bela Vista", "Sao Paulo", "SP", "01310-100"));

        Venda vendaMarina = new Venda(marina);
        vendaMarina.adicionarItem(dipirona, 2);
        vendaMarina.adicionarItem(pomada, 1);

        Venda vendaCarlos = new Venda(carlos);
        vendaCarlos.adicionarItem(xarope, 1);

        // Cada passo é uma operação com um propósito só:
        registrarVenda(vendaMarina);                                  // modifier
        new NotaFiscal(vendaMarina, DATA_DA_COMPRA).imprimir();       // apresentação

        registrarVenda(vendaCarlos);
        new NotaFiscal(vendaCarlos, DATA_DA_COMPRA).imprimir();

        System.out.println(estoque.formatado());                      // query
    }

    public static void main(String[] args) {
        new SistemaFarmaciaRefatorado().rodarCenario();
    }
}
