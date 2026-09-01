import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import legado.SistemaFarmaciaLegado;
import refatorado.SistemaFarmaciaRefatorado;

/*
 * =====================================================================================
 *  A PROVA DA REFATORAÇÃO
 * =====================================================================================
 *
 *  Refatorar é "melhorar a estrutura interna SEM alterar o comportamento externo".
 *  A segunda metade dessa frase é uma afirmação verificável - e é isso que este
 *  programa faz.
 *
 *  Ele executa os dois sistemas (o legado e o refatorado), captura tudo o que cada um
 *  imprime no console e compara caractere a caractere.
 *
 *      Saídas idênticas  -> foi refatoração.
 *      Saídas diferentes -> foi reescrita (ou foi um bug introduzido).
 *
 *  Este é o "teste de caracterização" (characterization test) do Michael Feathers,
 *  a rede de segurança clássica para mexer em código legado que não tem testes:
 *  primeiro você congela o comportamento atual, DEPOIS você refatora.
 *
 *  COMO RODAR (na pasta src):
 *      javac -encoding UTF-8 -d ../out legado/*.java refatorado/*.java Main.java
 *      java  -cp ../out Main
 */
public class Main {

    public static void main(String[] args) {
        String saidaLegado     = capturarSaida(() -> new SistemaFarmaciaLegado().rodarCenario());
        String saidaRefatorada = capturarSaida(() -> new SistemaFarmaciaRefatorado().rodarCenario());

        System.out.println("############ SAIDA DO CODIGO LEGADO ############\n");
        System.out.print(saidaLegado);

        System.out.println("\n########## SAIDA DO CODIGO REFATORADO ##########\n");
        System.out.print(saidaRefatorada);

        System.out.println("\n################## VEREDITO ###################\n");
        if (saidaLegado.equals(saidaRefatorada)) {
            System.out.println("OK: comportamento externo PRESERVADO.");
            System.out.println("    As duas versoes produzem exatamente a mesma saida.");
            System.out.println("    Logo, o que fizemos foi REFATORACAO.");
        } else {
            System.out.println("FALHOU: o comportamento externo MUDOU.");
            System.out.println("        Isso nao foi refatoracao - foi reescrita.");
            imprimirPrimeiraDiferenca(saidaLegado, saidaRefatorada);
        }
    }

    /** Redireciona o System.out temporariamente para capturar o que o código imprime. */
    private static String capturarSaida(Runnable acao) {
        PrintStream consoleOriginal = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            acao.run();
        } finally {
            System.setOut(consoleOriginal); // sempre devolve o console, mesmo se der erro
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }

    /** Ajuda a achar rapidamente onde as saídas divergiram (útil durante a aula ao vivo). */
    private static void imprimirPrimeiraDiferenca(String esperado, String obtido) {
        String[] linhasEsperadas = esperado.split("\n", -1);
        String[] linhasObtidas   = obtido.split("\n", -1);
        int maximo = Math.max(linhasEsperadas.length, linhasObtidas.length);
        for (int i = 0; i < maximo; i++) {
            String a = i < linhasEsperadas.length ? linhasEsperadas[i] : "<sem linha>";
            String b = i < linhasObtidas.length   ? linhasObtidas[i]   : "<sem linha>";
            if (!a.equals(b)) {
                System.out.println("        Primeira diferenca na linha " + (i + 1) + ":");
                System.out.println("          legado    : [" + a + "]");
                System.out.println("          refatorado: [" + b + "]");
                return;
            }
        }
    }
}
