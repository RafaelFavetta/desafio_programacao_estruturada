import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SistemaProjetos {

    static final int MAX = 20;
    static int[] ids = new int[MAX];
    static String[] nomes = new String[MAX];
    static String[] nomesAlunos = new String[MAX];
    static String[] areas = new String[MAX];
    static String[] semestres = new String[MAX];
    static String[] status = new String[MAX];
    static int qtd = 0;
    static int proximoId = 1;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {
        int op;
        do {
            System.out.println("\n--- PROJETOS (" + qtd + "/" + MAX + ") ---");
            System.out.println("1. Cadastrar\n2. Alterar\n3. Exibir todos\n4. Excluir\n0. Sair");
            op = lerInt("Opção: ");
            switch (op) {
                case 1 -> cadastrar();
                case 2 -> alterar();
                case 3 -> exibirTodos();
                case 4 -> excluir();
                case 0 -> System.out.println("Encerrando...");
                default -> System.out.println("Opção inválida.");
            }
        } while (op != 0);
    }

    static void cadastrar() {
        if (qtd == MAX) { System.out.println("Cadastro cheio!"); return; }
        ids[qtd] = proximoId++;
        nomes[qtd] = lerStr("Nome do projeto: ");
        nomesAlunos[qtd] = lerStr("Nome do aluno: ");
        areas[qtd] = escolherArea();
        semestres[qtd] = lerStr("Semestre (ex: 2025-1): ");
        status[qtd] = escolherStatus();
        qtd++;
        System.out.println("Projeto cadastrado! ID: " + ids[qtd - 1]);
    }

    static void alterar() {
        int pos = buscarPorId(lerInt("ID do projeto: "));
        if (pos == -1) { System.out.println("Nao encontrado."); return; }
        nomes[pos] = lerStrOpcional("Novo nome", nomes[pos]);
        nomesAlunos[pos] = lerStrOpcional("Novo aluno", nomesAlunos[pos]);
        areas[pos] = escolherAreaAtual(areas[pos]);
        semestres[pos] = lerStrOpcional("Novo semestre", semestres[pos]);
        status[pos] = escolherStatusAtual(status[pos]);
        System.out.println("Projeto atualizado.");
    }

    static void exibirTodos() {
        if (qtd == 0) { System.out.println("Nenhum projeto."); return; }
        for (int i = 0; i < qtd; i++) {
            System.out.printf("%nID: %d | %s | %s | %s | %s | %s%n",
                ids[i], nomes[i], nomesAlunos[i], areas[i], semestres[i], status[i]);
        }
    }

    static void excluir() {
        int pos = buscarPorId(lerInt("ID do projeto: "));
        if (pos == -1) { System.out.println("Nao encontrado."); return; }
        for (int i = pos; i < qtd - 1; i++) {
            ids[i] = ids[i + 1];
            nomes[i] = nomes[i + 1];
            nomesAlunos[i] = nomesAlunos[i + 1];
            areas[i] = areas[i + 1];
            semestres[i] = semestres[i + 1];
            status[i] = status[i + 1];
        }
        qtd--;
        ids[qtd] = 0;
        nomes[qtd] = null;
        nomesAlunos[qtd] = null;
        areas[qtd] = null;
        semestres[qtd] = null;
        status[qtd] = null;
        System.out.println("Projeto excluído.");
    }

    static int buscarPorId(int id) {
        for (int i = 0; i < qtd; i++) if (ids[i] == id) return i;
        return -1;
    }

    static String escolherArea() {
        String[] opcoes = {"Web", "Mobile", "Eletrônica"};
        return escolherOpcao("Area", opcoes);
    }

    static String escolherAreaAtual(String atual) {
        String[] opcoes = {"Web", "Mobile", "Eletrônica"};
        return escolherOpcaoAtual("Area", opcoes, atual);
    }

    static String escolherStatus() {
        String[] opcoes = {"Em andamento", "Concluído"};
        return escolherOpcao("Status", opcoes);
    }

    static String escolherStatusAtual(String atual) {
        String[] opcoes = {"Em andamento", "Concluído"};
        return escolherOpcaoAtual("Status", opcoes, atual);
    }

    static String escolherOpcao(String titulo, String[] opcoes) {
        while (true) {
            System.out.println(titulo + ":");
            for (int i = 0; i < opcoes.length; i++) {
                System.out.println((i + 1) + ". " + opcoes[i]);
            }
            int escolha = lerInt("Escolha uma opção: ");
            if (escolha >= 1 && escolha <= opcoes.length) {
                return opcoes[escolha - 1];
            }
            System.out.println("Opção inválida.");
        }
    }

    static String escolherOpcaoAtual(String titulo, String[] opcoes, String atual) {
        while (true) {
            System.out.println(titulo + " atual: " + atual);
            for (int i = 0; i < opcoes.length; i++) {
                System.out.println((i + 1) + ". " + opcoes[i]);
            }
            String entrada = lerStr("Escolha uma opção (Enter para manter): ");
            if (entrada.isEmpty()) return atual;
            try {
                int escolha = Integer.parseInt(entrada);
                if (escolha >= 1 && escolha <= opcoes.length) {
                    return opcoes[escolha - 1];
                }
            } catch (NumberFormatException ignored) {
            }
            System.out.println("Opção inválida.");
        }
    }

    static String lerStrOpcional(String campo, String atual) {
        String valor = lerStr(campo + " [" + atual + "] (Enter para manter): ");
        return valor.isEmpty() ? atual : valor;
    }

    static String lerStr(String prompt) {
        System.out.print(prompt);
        try {
            String linha = br.readLine();
            return linha == null ? "" : linha.trim();
        } catch (IOException e) {
            System.out.println("Erro de leitura.");
            return "";
        }
    }

    static int lerInt(String prompt) {
        while (true) {
            try { return Integer.parseInt(lerStr(prompt)); }
            catch (NumberFormatException e) { System.out.println("Digite um número."); }
        }
    }
}