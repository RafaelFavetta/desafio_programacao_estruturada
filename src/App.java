import java.util.Scanner;

public class App {
    static class Projeto {

        int    id;
        String nome;
        String nomeAluno;
        String area;
        String semestre;
        String status; 
    }

    static final int MAX_PROJETOS = 20;

    static Projeto[] projetos   = new Projeto[MAX_PROJETOS];
    static int       quantidade = 0;      
    static int       proximoId  = 1;      

    static final Scanner sc = new Scanner(System.in);

    static final String[] AREAS = {
        "Programação", "Eletrônica",
        "Inteligência Artificial", "Desenvolvimento Web"
    };

    public static void main(String[] args) {
        int opcao;
        do {
            exibirMenu();
            opcao = lerInteiro("Opção: ");
            System.out.println();

            switch (opcao) {
                case 1 -> cadastrarProjeto();
                case 2 -> alterarProjeto();
                case 3 -> exibirTodos();
                case 4 -> excluirProjeto();
                case 5 -> buscarPorAluno();
                case 6 -> exibirConcluidos();
                case 7 -> contarPorArea();
                case 0 -> System.out.println("Sistema encerrado. Até logo!");
                default -> System.out.println("Opção inválida. Tente novamente.");
            }

            if (opcao != 0) pausar();

        } while (opcao != 0);
    }

    // ==========================================================
    // MENU
    // ==========================================================
    static void exibirMenu() {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║              SISTEMA DE PROJETOS             ║");
        System.out.println("╠══════════════════════════════════════════════╣");
        System.out.printf ("║  Projetos cadastrados: %-3d  Capacidade: %-3d  ║%n",
                            quantidade, MAX_PROJETOS);
        System.out.println("╠══════════════════════════════════════════════╣");
        System.out.println("║  1. Cadastrar projeto                        ║");
        System.out.println("║  2. Alterar dados de um projeto              ║");
        System.out.println("║  3. Exibir todos os projetos                 ║");
        System.out.println("║  4. Excluir um projeto                       ║");
        System.out.println("╠══════════════════════════════════════════════╣");
        System.out.println("║  5. Buscar projetos por aluno          (ext) ║");
        System.out.println("║  6. Exibir apenas projetos concluídos  (ext) ║");
        System.out.println("║  7. Contar projetos por área           (ext) ║");
        System.out.println("╠══════════════════════════════════════════════╣");
        System.out.println("║  0. Encerrar o programa                      ║");
        System.out.println("╚══════════════════════════════════════════════╝");
    }

    // ==========================================================
    // 1. CADASTRAR PROJETO
    // ==========================================================
    static void cadastrarProjeto() {
        System.out.println("=== CADASTRAR PROJETO ===");

        if (quantidade == MAX_PROJETOS) {
            System.out.println("! Capacidade máxima atingida (" + MAX_PROJETOS + " projetos). "
                             + "Exclua um registro antes de cadastrar.");
            return;
        }

        Projeto p  = new Projeto();
        p.id       = proximoId++;
        p.nome     = lerString("Nome do projeto: ");
        p.nomeAluno = lerString("Nome do aluno responsável: ");
        p.area     = escolherArea();
        p.semestre = lerString("Semestre (ex: 2025-1): ");
        p.status   = escolherStatus();

        projetos[quantidade] = p;
        quantidade++;

        System.out.println("\nProjeto cadastrado com sucesso! ID: " + p.id);
    }

    // ==========================================================
    // 2. ALTERAR PROJETO
    // ==========================================================
    static void alterarProjeto() {
        System.out.println("=== ALTERAR PROJETO ===");

        if (quantidade == 0) { semRegistros(); return; }

        int id = lerInteiro("Informe o ID do projeto: ");
        int pos = buscarPorId(id);

        if (pos == -1) { projetoNaoEncontrado(id); return; }

        Projeto p = projetos[pos];
        System.out.println("\nProjeto encontrado:");
        imprimirProjeto(p);

        System.out.println("\nDeixe em branco para manter o valor atual.");

        String entrada;

        entrada = lerStringOpcional("Novo nome do projeto [" + p.nome + "]: ");
        if (!entrada.isEmpty()) p.nome = entrada;

        entrada = lerStringOpcional("Novo nome do aluno [" + p.nomeAluno + "]: ");
        if (!entrada.isEmpty()) p.nomeAluno = entrada;

        System.out.println("Nova área? (S/N)");
        if (lerString("").equalsIgnoreCase("s")) p.area = escolherArea();

        entrada = lerStringOpcional("Novo semestre [" + p.semestre + "]: ");
        if (!entrada.isEmpty()) p.semestre = entrada;

        System.out.println("Alterar status? (S/N)");
        if (lerString("").equalsIgnoreCase("s")) p.status = escolherStatus();

        System.out.println("\n✔ Projeto atualizado com sucesso!");
    }

    // ==========================================================
    // 3. EXIBIR TODOS
    // ==========================================================
    static void exibirTodos() {
        System.out.println("=== TODOS OS PROJETOS CADASTRADOS ===");

        if (quantidade == 0) { semRegistros(); return; }

        for (int i = 0; i < quantidade; i++) {
            imprimirProjeto(projetos[i]);
            System.out.println("─────────────────────────────────────");
        }
        System.out.println("Total: " + quantidade + " projeto(s).");
    }

    // ==========================================================
    // 4. EXCLUIR PROJETO
    // ==========================================================
    static void excluirProjeto() {
        System.out.println("=== EXCLUIR PROJETO ===");

        if (quantidade == 0) { semRegistros(); return; }

        int id = lerInteiro("Informe o ID do projeto a excluir: ");
        int pos = buscarPorId(id);

        if (pos == -1) { projetoNaoEncontrado(id); return; }

        System.out.println("\nProjeto a ser excluído:");
        imprimirProjeto(projetos[pos]);
        System.out.print("\nConfirma exclusão? (S/N): ");
        String conf = sc.nextLine().trim();

        if (!conf.equalsIgnoreCase("s")) {
            System.out.println("Exclusão cancelada.");
            return;
        }

        // Reorganiza o vetor — desloca os elementos para frente
        for (int i = pos; i < quantidade - 1; i++) {
            projetos[i] = projetos[i + 1];
        }
        projetos[quantidade - 1] = null;
        quantidade--;

        System.out.println("✔ Projeto excluído e vetor reorganizado.");
    }

    // ==========================================================
    // 5. BUSCAR PROJETOS POR NOME DO ALUNO
    // ==========================================================
    static void buscarPorAluno() {
        System.out.println("=== BUSCA POR ALUNO ===");

        if (quantidade == 0) { semRegistros(); return; }

        String nome = lerString("Nome do aluno: ").toLowerCase();
        int encontrados = 0;

        for (int i = 0; i < quantidade; i++) {
            if (projetos[i].nomeAluno.toLowerCase().contains(nome)) {
                imprimirProjeto(projetos[i]);
                System.out.println("─────────────────────────────────────");
                encontrados++;
            }
        }

        if (encontrados == 0) {
            System.out.println("Nenhum projeto encontrado para o aluno \""
                             + nome + "\".");
        } else {
            System.out.println("Total encontrado: " + encontrados + " projeto(s).");
        }
    }

    // ==========================================================
    // 6. EXIBIR APENAS PROJETOS CONCLUÍDOS
    // ==========================================================
    static void exibirConcluidos() {
        System.out.println("=== PROJETOS CONCLUÍDOS ===");

        if (quantidade == 0) { semRegistros(); return; }

        int encontrados = 0;

        for (int i = 0; i < quantidade; i++) {
            if (projetos[i].status.equals("Concluído")) {
                imprimirProjeto(projetos[i]);
                System.out.println("─────────────────────────────────────");
                encontrados++;
            }
        }

        if (encontrados == 0) {
            System.out.println("Nenhum projeto concluído no momento.");
        } else {
            System.out.println("Total concluído: " + encontrados + " projeto(s).");
        }
    }

    // ==========================================================
    // 7. CONTAR PROJETOS POR ÁREA
    // ==========================================================
    static void contarPorArea() {
        System.out.println("=== PROJETOS POR ÁREA ===");

        if (quantidade == 0) { semRegistros(); return; }

        int[] contagens = new int[AREAS.length];

        for (int i = 0; i < quantidade; i++) {
            for (int j = 0; j < AREAS.length; j++) {
                if (projetos[i].area.equals(AREAS[j])) {
                    contagens[j]++;
                    break;
                }
            }
        }

        System.out.println();
        for (int j = 0; j < AREAS.length; j++) {
            System.out.printf("  %-30s: %d projeto(s)%n", AREAS[j], contagens[j]);
        }
        System.out.println();
    }


    /** Procura um projeto pelo ID */
    static int buscarPorId(int id) {
        for (int i = 0; i < quantidade; i++) {
            if (projetos[i].id == id) return i;
        }
        return -1;
    }

    /** Exibe os dados de um projeto */
    static void imprimirProjeto(Projeto p) {
        System.out.println();
        System.out.printf("  ID       : %d%n",   p.id);
        System.out.printf("  Nome     : %s%n",   p.nome);
        System.out.printf("  Aluno    : %s%n",   p.nomeAluno);
        System.out.printf("  Área     : %s%n",   p.area);
        System.out.printf("  Semestre : %s%n",   p.semestre);
        System.out.printf("  Status   : %s%n",   p.status);
    }

    /** seleção de área */
    static String escolherArea() {
        System.out.println("Áreas disponíveis:");
        for (int i = 0; i < AREAS.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, AREAS[i]);
        }
        while (true) {
            int op = lerInteiro("Escolha a área (1-" + AREAS.length + "): ");
            if (op >= 1 && op <= AREAS.length) return AREAS[op - 1];
            System.out.println("Opção inválida.");
        }
    }

    /** seleção de status */
    static String escolherStatus() {
        System.out.println("Status:");
        System.out.println("  1. Em andamento");
        System.out.println("  2. Concluído");
        while (true) {
            int op = lerInteiro("Escolha o status (1-2): ");
            if (op == 1) return "Em andamento";
            if (op == 2) return "Concluído";
            System.out.println("Opção inválida.");
        }
    }

    /** Lê uma String não vazia do teclado */
    static String lerString(String prompt) {
        String entrada;
        do {
            System.out.print(prompt);
            entrada = sc.nextLine().trim();
            if (entrada.isEmpty()) System.out.println("Campo obrigatório.");
        } while (entrada.isEmpty());
        return entrada;
    }

    /** Lê uma String que pode ser vazia */
    static String lerStringOpcional(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    /** Lê um inteiro que pode ter entrada inválida */
    static int lerInteiro(String prompt) {
        while (true) {
            System.out.print(prompt);
            String linha = sc.nextLine().trim();
            try {
                return Integer.parseInt(linha);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número inteiro.");
            }
        }
    }

    static void semRegistros() {
        System.out.println("Nenhum projeto cadastrado ainda.");
    }

    static void projetoNaoEncontrado(int id) {
        System.out.println("Projeto com ID " + id + " não encontrado.");
    }

    static void pausar() {
        System.out.print("\nPressione ENTER para continuar...");
        sc.nextLine();
        System.out.println();
    }
}