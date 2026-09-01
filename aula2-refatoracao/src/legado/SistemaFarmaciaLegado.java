package legado;

import java.util.Locale;

/*
 * =====================================================================================
 *  AULA 2 - REFATORAÇÃO  |  PASSO 0: O CÓDIGO LEGADO ("antes")
 * =====================================================================================
 *
 *  Este arquivo é o ponto de partida da aula ao vivo. Ele FUNCIONA: emite a nota fiscal
 *  correta, calcula o total certo e dá baixa no estoque. Não há nenhum bug aqui.
 *
 *  E é exatamente esse o ponto da aula:
 *
 *      >> Bad smell NÃO é bug. <<
 *
 *  O código abaixo está cheio de bad smells - sinais de problema de DESIGN que tornam
 *  o código difícil de ler, testar, reusar e evoluir. Nosso trabalho nas próximas
 *  6 etapas será melhorar a ESTRUTURA INTERNA sem alterar o COMPORTAMENTO EXTERNO.
 *
 *  Ao final, a classe Main compara, caractere a caractere, a saída deste código com a
 *  saída do código refatorado. Se as duas forem idênticas, provamos que refatoramos -
 *  e não reescrevemos.
 *
 *  -------------------------------------------------------------------------------------
 *  MAPA DOS BAD SMELLS (procure os marcadores [SMELL n] no código):
 *
 *   [SMELL 1] Long Method .............. emitirNotaFiscal() faz 5 coisas diferentes
 *                                        -> Passo 1: EXTRACT METHOD
 *   [SMELL 2] Large Class / baixa coesão ClienteLegado guarda pessoa + endereço + convênio
 *                                        -> Passo 2: EXTRACT CLASS
 *   [SMELL 3] Primitive Obsession ...... catálogo é uma matriz String[][] com tipos falsos
 *                                        -> Passo 3: REPLACE ARRAY WITH OBJECT
 *   [SMELL 4] Null checks espalhados ... "if (convenioNome != null)" em 2 lugares
 *                                        -> Passo 4: INTRODUCE NULL OBJECT
 *   [SMELL 5] Query + Modifier juntos .. o mesmo método altera estoque E devolve o total
 *                                        -> Passo 5: SEPARATE QUERY FROM MODIFIER
 *   [SMELL 6] Duplicated Code .......... regra de preço/dados de produto duplicada em ifs
 *                                        -> Passo 6: EXTRACT SUPERCLASS
 *  -------------------------------------------------------------------------------------
 */
public class SistemaFarmaciaLegado {

    /** Locale fixo: garante que o R$ 12,90 saia com vírgula em qualquer máquina da turma. */
    private static final Locale BR = Locale.forLanguageTag("pt-BR");

    /**
     * Data "congelada" só para a aula. Em produção seria new Date(), mas aí a saída
     * mudaria a cada execução e não daria para comparar o antes com o depois.
     * (Já é uma primeira lição: código testável evita depender do relógio.)
     */
    private static final String DATA_DA_COMPRA = "02/09/2026";

    // -----------------------------------------------------------------------------------
    // [SMELL 3] PRIMITIVE OBSESSION / REPLACE ARRAY WITH OBJECT
    //
    // O catálogo é uma matriz de String. Problemas:
    //   - Qual coluna é o preço? Só descobrindo por tentativa (catalogo[i][3]... será?).
    //   - O preço é número, mas está guardado como texto: precisa de parseDouble em todo uso.
    //   - A coluna 4 ("gramas") só faz sentido para produto MANIPULADO: fica "0" nos outros.
    //   - Se alguém trocar a ordem das colunas, o código quebra silenciosamente.
    //
    // Colunas: 0=código | 1=descrição | 2=tipo | 3=preço (unitário OU por grama!) | 4=gramas
    // -----------------------------------------------------------------------------------
    private String[][] catalogo = {
        {"1001", "Dipirona 500mg",          "INDUSTRIALIZADO", "12.90", "0"},
        {"1002", "Xarope de Guaco 120ml",   "INDUSTRIALIZADO", "28.50", "0"},
        {"2001", "Pomada de Arnica",        "MANIPULADO",      "0.85",  "60"}
    };

    /** Estoque paralelo ao catálogo: mais um acoplamento frágil por índice. */
    private int[] estoque = {10, 5, 3};

    /** Taxa fixa cobrada para manipular uma fórmula. */
    private static final double TAXA_MANIPULACAO = 8.00;

    // ===================================================================================
    // [SMELL 1] LONG METHOD + [SMELL 5] QUERY MISTURADO COM MODIFIER
    //
    // Este único método:
    //   1. imprime o cabeçalho da nota
    //   2. imprime os dados do cliente e do endereço
    //   3. percorre os itens, calcula preços (com regras diferentes por tipo)
    //   4. DÁ BAIXA NO ESTOQUE  <-- efeito colateral (modifier)
    //   5. aplica desconto de convênio, imprime o rodapé
    //   6. e ainda DEVOLVE o total  <-- consulta (query)
    //
    // Consequência prática: é impossível saber o total sem imprimir a nota e sem
    // mexer no estoque. Testar isso exige capturar a saída do console.
    // ===================================================================================
    public double emitirNotaFiscal(ClienteLegado cliente, int[] indices, int[] quantidades) {

        // ---- (1) cabeçalho -------------------------------------------------------------
        System.out.println("=======================================");
        System.out.println("         FARMACIA REFATORADA");
        System.out.println("              Nota Fiscal");
        System.out.println("         CNPJ: 998.998.998-98");
        System.out.println("=======================================");

        // ---- (2) dados do cliente ------------------------------------------------------
        // Note como os dados de ENDEREÇO estão misturados com os dados de PESSOA.
        System.out.println("Cliente: " + cliente.nome);
        System.out.println("CPF: " + cliente.cpf);
        System.out.println("Endereco:");
        System.out.println(cliente.logradouro + ", " + cliente.num + " - " + cliente.compl);
        System.out.println("Bairro: " + cliente.bairro);
        System.out.println(cliente.cidade + "/" + cliente.estado + " - " + cliente.cep);
        System.out.println("---------------------------------------");

        // ---- (3) itens, preços e baixa de estoque ---------------------------------------
        double subtotal = 0.0;
        for (int k = 0; k < indices.length; k++) {
            int i = indices[k];
            int qtd = quantidades[k];

            // [SMELL 6] DUPLICATED CODE / condicional por "tipo"
            // A regra de preço muda conforme o texto da coluna 2. Cada novo tipo de
            // produto significa mais um "else if" aqui - e em todo lugar que calcula preço.
            double precoUnitario;
            if (catalogo[i][2].equals("MANIPULADO")) {
                double precoPorGrama = Double.parseDouble(catalogo[i][3]);
                double gramas = Double.parseDouble(catalogo[i][4]);
                precoUnitario = precoPorGrama * gramas + TAXA_MANIPULACAO;
            } else {
                precoUnitario = Double.parseDouble(catalogo[i][3]);
            }

            double valorItem = precoUnitario * qtd;
            subtotal += valorItem;

            estoque[i] = estoque[i] - qtd; // <-- efeito colateral escondido no meio da impressão

            System.out.println(String.format(BR, "%d x %-28s R$ %8.2f",
                    qtd, catalogo[i][1], valorItem));
        }
        System.out.println("---------------------------------------");

        // ---- (4) desconto de convênio ---------------------------------------------------
        // [SMELL 4] NULL CHECK (primeira ocorrência)
        double percentual;
        if (cliente.convenioNome != null) {
            percentual = cliente.convenioDesconto;
        } else {
            percentual = 0.0;
        }
        double desconto = subtotal * percentual;
        double total = subtotal - desconto;

        System.out.println(String.format(BR, "Subtotal: R$ %.2f", subtotal));

        // [SMELL 4] NULL CHECK (segunda ocorrência - a mesma pergunta feita de novo)
        if (cliente.convenioNome != null) {
            System.out.println(String.format(BR, "Convenio %s (%.0f%%): -R$ %.2f",
                    cliente.convenioNome, percentual * 100, desconto));
        }
        System.out.println(String.format(BR, "TOTAL: R$ %.2f", total));

        // ---- (5) rodapé -----------------------------------------------------------------
        System.out.println("=======================================");
        System.out.println("Obrigado! Volte sempre!");
        System.out.println("Data da compra: " + DATA_DA_COMPRA);
        System.out.println("=======================================");
        System.out.println();

        return total; // query + modifier no mesmo método
    }

    /** Só para a aula: mostrar que o estoque realmente foi alterado. */
    public String estoqueFormatado() {
        StringBuilder sb = new StringBuilder("Estoque restante: ");
        for (int i = 0; i < catalogo.length; i++) {
            sb.append(catalogo[i][1]).append("=").append(estoque[i]);
            if (i < catalogo.length - 1) sb.append(" | ");
        }
        return sb.toString();
    }

    /**
     * Cenário de demonstração. É este roteiro que a versão refatorada precisa
     * reproduzir com saída IDÊNTICA.
     */
    public void rodarCenario() {
        // [SMELL 2] LARGE CLASS: uma única classe guardando pessoa + endereço + convênio
        ClienteLegado marina = new ClienteLegado();
        marina.nome = "Marina Souza";
        marina.cpf = "123.456.789-00";
        marina.logradouro = "Rua Itambe";
        marina.num = 45;
        marina.compl = "apto 71";
        marina.bairro = "Consolacao";
        marina.cidade = "Sao Paulo";
        marina.estado = "SP";
        marina.cep = "01239-001";
        marina.convenioNome = "MackSaude";   // este cliente TEM convênio
        marina.convenioDesconto = 0.15;

        ClienteLegado carlos = new ClienteLegado();
        carlos.nome = "Carlos Lima";
        carlos.cpf = "987.654.321-00";
        carlos.logradouro = "Av. Paulista";
        carlos.num = 1200;
        carlos.compl = "sala 12";
        carlos.bairro = "Bela Vista";
        carlos.cidade = "Sao Paulo";
        carlos.estado = "SP";
        carlos.cep = "01310-100";
        carlos.convenioNome = null;          // este NÃO tem -> null espalhado pelo sistema
        carlos.convenioDesconto = 0.0;

        emitirNotaFiscal(marina, new int[]{0, 2}, new int[]{2, 1});
        emitirNotaFiscal(carlos, new int[]{1},    new int[]{1});

        System.out.println(estoqueFormatado());
    }

    public static void main(String[] args) {
        new SistemaFarmaciaLegado().rodarCenario();
    }
}
