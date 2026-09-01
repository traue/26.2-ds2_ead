package refatorado;

import java.util.LinkedHashMap;
import java.util.Map;

/*
 * Resultado combinado do PASSO 3 (Replace Array with Object) e do PASSO 5
 * (Separate Query from Modifier).
 *
 * No legado existiam DOIS arrays paralelos:
 *
 *      String[][] catalogo = { ... };
 *      int[]      estoque  = {10, 5, 3};
 *
 * ...ligados apenas pelo índice. Inserir um produto no meio do catálogo e esquecer de
 * inserir no estoque produziria um desalinhamento silencioso: o sistema continuaria
 * "funcionando", dando baixa no produto errado. É o tipo de bug que a estrutura de dados
 * ruim convida a acontecer.
 *
 * Aqui, produto e quantidade estão amarrados em uma única estrutura, e a classe expõe
 * claramente o que é comando e o que é consulta.
 */
public class Estoque {

    // LinkedHashMap preserva a ordem de inserção - importante para a saída ficar estável.
    private final Map<Produto, Integer> quantidades = new LinkedHashMap<>();

    // ---------------------------------------------------------------------------------
    // MODIFIERS (comandos): alteram estado, retornam void
    // ---------------------------------------------------------------------------------

    public void cadastrar(Produto produto, int quantidadeInicial) {
        quantidades.put(produto, quantidadeInicial);
    }

    public void darBaixa(Produto produto, int quantidade) {
        quantidades.put(produto, disponivel(produto) - quantidade);
    }

    // ---------------------------------------------------------------------------------
    // QUERIES (consultas): só leem
    // ---------------------------------------------------------------------------------

    public int disponivel(Produto produto) {
        return quantidades.getOrDefault(produto, 0);
    }

    public String formatado() {
        StringBuilder sb = new StringBuilder("Estoque restante: ");
        int i = 0;
        for (Map.Entry<Produto, Integer> entrada : quantidades.entrySet()) {
            sb.append(entrada.getKey().getDescricao()).append("=").append(entrada.getValue());
            if (++i < quantidades.size()) {
                sb.append(" | ");
            }
        }
        return sb.toString();
    }
}
