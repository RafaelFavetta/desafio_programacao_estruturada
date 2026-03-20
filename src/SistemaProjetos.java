import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SistemaProjetos {

    static class Projeto {
        public int id;
        public String nome;
        public String nomeAluno;
        public String area;
        public String semestre;
        public String status;
    }

    public static final int MAX = 20;
    public static Projeto[] projetos = new Projeto[MAX];
    public static int qtd = 0;
    public static int proximoId = 1;
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {
        int op;
        do {
            System.out.println("\n--- PROJETOS (" + qtd + "/" + MAX + ") ---");
            System.out.println("1. Cadastrar\n2. Alterar\n3. Exibir todos\n4. Excluir\n5. Encerrar");
            op = lerInt("Opção: ");
            switch (op) {
                case 1 -> cadastrar();
                case 2 -> alterar();
                case 3 -> exibirTodos();
                case 4 -> excluir();
                case 5 -> System.out.println("\nEncerrando...");
                default -> System.out.println("\nOpção inválida.");
            }
        } while (op != 5);
    }

    public static void cadastrar() {
        if (qtd == MAX) {
            System.out.println("\nCadastro cheio!");
            return;
        }
        Projeto p = new Projeto();
        p.id = proximoId++;
        p.nome = lerStr("\nNome do projeto: ");
        p.nomeAluno = lerStr("Nome do aluno: ");
        p.area = escolherArea();
        p.semestre = lerStr("Semestre (ex: 2025-1): ");
        p.status = escolherStatus();
        projetos[qtd++] = p;
        System.out.println("\nProjeto cadastrado! ID: " + p.id);
    }

    public static void alterar() {
        int pos = buscarPorId(lerInt("\nID do projeto: "));
        if (pos == -1) {
            System.out.println("\nNao encontrado.");
            return;
        }
        Projeto p = projetos[pos];
        p.nome = lerStrOpcional("Novo nome", p.nome);
        p.nomeAluno = lerStrOpcional("Novo aluno", p.nomeAluno);
        p.area = escolherAreaAtual(p.area);
        p.semestre = lerStrOpcional("Novo semestre", p.semestre);
        p.status = escolherStatusAtual(p.status);
        System.out.println("\nProjeto atualizado.");
    }

    public static void exibirTodos() {
        if (qtd == 0) {
            System.out.println("\nNenhum projeto.");
            return;
        }
        for (int i = 0; i < qtd; i++) {
            Projeto p = projetos[i];
            System.out.printf("%nID: %d | %s | %s | %s | %s | %s%n",
                p.id, p.nome, p.nomeAluno, p.area, p.semestre, p.status);
        }
    }

    public static void excluir() {
        int pos = buscarPorId(lerInt("\nID do projeto: "));
        if (pos == -1) {
            System.out.println("\nNao encontrado.");
            return;
        }
        for (int i = pos; i < qtd - 1; i++) {
            projetos[i] = projetos[i + 1];
        }
        projetos[--qtd] = null;
        System.out.println("\nProjeto excluído.");
    }

    public static int buscarPorId(int id) {
        for (int i = 0; i < qtd; i++) {
            if (projetos[i].id == id) {
                return i;
            }
        }
        return -1;
    }

    public static String escolherArea() {
        String[] opcoes = {"Programacao", "Eletronica", "Inteligencia Artificial", "Web"};
        return escolherOpcao("Area", opcoes);
    }

    public static String escolherAreaAtual(String atual) {
        String[] opcoes = {"Programacao", "Eletronica", "Inteligencia Artificial", "Web"};
        return escolherOpcaoAtual("Area", opcoes, atual);
    }

    public static String escolherStatus() {
        String[] opcoes = {"Em andamento", "Concluido"};
        return escolherOpcao("Status", opcoes);
    }

    public static String escolherStatusAtual(String atual) {
        String[] opcoes = {"Em andamento", "Concluido"};
        return escolherOpcaoAtual("Status", opcoes, atual);
    }

    public static String escolherOpcao(String titulo, String[] opcoes) {
        while (true) {
            System.out.println(titulo + ":");
            for (int i = 0; i < opcoes.length; i++) {
                System.out.println((i + 1) + ". " + opcoes[i]);
            }
            int escolha = lerInt("Escolha uma opção: ");
            if (escolha >= 1 && escolha <= opcoes.length) {
                return opcoes[escolha - 1];
            }
            System.out.println("\nOpção inválida.");
        }
    }

    public static String escolherOpcaoAtual(String titulo, String[] opcoes, String atual) {
        while (true) {
            System.out.println(titulo + " atual: " + atual);
            for (int i = 0; i < opcoes.length; i++) {
                System.out.println((i + 1) + ". " + opcoes[i]);
            }
            String entrada = lerStr("Escolha uma opção (Enter para manter): ");
            if (entrada.isEmpty()) {
                return atual;
            }
            try {
                int escolha = Integer.parseInt(entrada);
                if (escolha >= 1 && escolha <= opcoes.length) {
                    return opcoes[escolha - 1];
                }
            } catch (NumberFormatException ignored) {
            }
            System.out.println("\nOpção inválida.");
        }
    }

    public static String lerStrOpcional(String campo, String atual) {
        String valor = lerStr(campo + " [" + atual + "] (Enter para manter): ");
        return valor.isEmpty() ? atual : valor;
    }

    public static String lerStr(String prompt) {
        System.out.print(prompt);
        try {
            String linha = br.readLine();
            return linha == null ? "" : linha.trim();
        } catch (IOException e) {
            System.out.println("\nErro de leitura.");
            return "";
        }
    }

    public static int lerInt(String prompt) {
        while (true) {
            try { return Integer.parseInt(lerStr(prompt)); }
            catch (NumberFormatException e) { System.out.println("\nDigite um número."); }
        }
    }
}