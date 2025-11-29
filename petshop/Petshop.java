package petshop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * Classe principal Petshop.
 * Contém o menu interativo e a lógica de interação com o usuário e o banco de dados.
 */
public class Petshop {

    private static final String URL_CONEXAO = "jdbc:sqlite:petshop.db";
    private static boolean driverCarregado = false;

    /**
     * Tenta carregar o driver SQLite de forma segura.
     * Retorna true se o driver foi carregado com sucesso.
     */
    private static boolean carregarDriver() {
        if (driverCarregado) {
            return true;
        }
        
        // Primeiro, verifica se o driver já está registrado no DriverManager
        try {
            java.util.Enumeration<java.sql.Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                java.sql.Driver driver = drivers.nextElement();
                if (driver.getClass().getName().equals("org.sqlite.JDBC")) {
                    driverCarregado = true;
                    return true;
                }
            }
        } catch (Exception e) {
            // Ignora erros na verificação
        }
        
        // Tenta carregar a classe do driver explicitamente
        try {
            Class.forName("org.sqlite.JDBC");
            driverCarregado = true;
            return true;
        } catch (ClassNotFoundException e) {
            // Se não encontrar, tenta usar o ServiceLoader do JDBC 4.0
            try {
                java.util.ServiceLoader<java.sql.Driver> serviceLoader = 
                    java.util.ServiceLoader.load(java.sql.Driver.class);
                for (java.sql.Driver driver : serviceLoader) {
                    if (driver.getClass().getName().equals("org.sqlite.JDBC")) {
                        DriverManager.registerDriver(driver);
                        driverCarregado = true;
                        return true;
                    }
                }
            } catch (Exception e2) {
                // Ignora erros do ServiceLoader
            }
            
            // Última tentativa: tenta fazer uma conexão de teste
            // O JDBC 4.0 pode carregar o driver automaticamente ao tentar conectar
            try (Connection testConn = DriverManager.getConnection(URL_CONEXAO)) {
                driverCarregado = true;
                return true;
            } catch (SQLException e3) {
                // Se a conexão falhar com "No suitable driver", o driver não está disponível
                if (e3.getMessage().contains("No suitable driver")) {
                    return false;
                }
                // Se for outro erro (como arquivo não existe), o driver está disponível
                driverCarregado = true;
                return true;
            }
        } catch (Exception e) {
            // Se houver outros erros, assume que o driver pode estar disponível
            // O JDBC 4.0 pode carregar automaticamente
            driverCarregado = true;
            return true;
        }
    }

    /**
     * Inicializa o banco de dados, criando a tabela se ela não existir.
     */
    public static void inicializarBanco() {
        // Tenta carregar o driver, mas não falha se não conseguir
        // O JDBC 4.0 pode carregar automaticamente ao tentar conectar
        carregarDriver();
        
        try (Connection conn = DriverManager.getConnection(URL_CONEXAO);
             Statement stmt = conn.createStatement()) {

            // Cria a tabela Animal
            String sql = "CREATE TABLE IF NOT EXISTS Animal (" +
                         "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                         "nome TEXT NOT NULL," +
                         "idade INTEGER NOT NULL," +
                         "especie TEXT NOT NULL," +
                         "nome_dono TEXT NOT NULL" +
                         ");";
            stmt.execute(sql);
            System.out.println("Banco de dados inicializado com sucesso.");

        } catch (SQLException e) {
            if (e.getMessage().contains("No suitable driver")) {
                System.err.println("ERRO: Driver SQLite não encontrado. Verifique se sqlite-jdbc-3.45.1.0.jar e slf4j-api-2.0.9.jar estão no classpath.");
                System.err.println("Execute o programa com: java -cp \".;petshop;petshop\\sqlite-jdbc-3.45.1.0.jar;petshop\\slf4j-api-2.0.9.jar\" petshop.Petshop");
            } else {
                System.err.println("Erro ao inicializar o banco de dados: " + e.getMessage());
            }
        }
    }

    /**
     * Cadastra um novo animal no banco de dados.
     * @param animal O objeto Animal a ser cadastrado.
     */
    public static void cadastrarAnimal(Animal animal) {
        // Tenta carregar o driver, mas não falha se não conseguir
        carregarDriver();
        
        String sql = "INSERT INTO Animal (nome, idade, especie, nome_dono) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL_CONEXAO);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, animal.getNome());
            pstmt.setInt(2, animal.getIdade());
            pstmt.setString(3, animal.getEspecie());
            pstmt.setString(4, animal.getNomeDono());
            pstmt.executeUpdate();
            System.out.println("\nAnimal cadastrado com sucesso!");
            System.out.println("Demonstração de Polimorfismo: Som emitido: " + animal.emitirSom());

        } catch (SQLException e) {
            if (e.getMessage().contains("No suitable driver")) {
                System.err.println("ERRO: Driver SQLite não encontrado. Verifique se sqlite-jdbc-3.45.1.0.jar e slf4j-api-2.0.9.jar estão no classpath.");
            } else {
                System.err.println("Erro ao cadastrar animal: " + e.getMessage());
            }
        }
    }

    /**
     * Consulta e lista todos os animais cadastrados.
     */
    public static void listarTodosAnimais() {
        // Tenta carregar o driver, mas não falha se não conseguir
        carregarDriver();
        
        String sql = "SELECT nome, idade, especie, nome_dono FROM Animal";

        try (Connection conn = DriverManager.getConnection(URL_CONEXAO);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- Animais Cadastrados ---");
            boolean encontrou = false;
            while (rs.next()) {
                encontrou = true;
                String nome = rs.getString("nome");
                int idade = rs.getInt("idade");
                String especie = rs.getString("especie");
                String nomeDono = rs.getString("nome_dono");

                // Demonstração de Abstração e Encapsulamento
                Animal animal;
                if (especie.equalsIgnoreCase("Cachorro")) {
                    animal = new Cachorro(nome, idade, nomeDono);
                } else if (especie.equalsIgnoreCase("Gato")) {
                    animal = new Gato(nome, idade, nomeDono);
                } else {
                    // Para outras espécies, usamos a classe base (se não fosse abstrata) ou um placeholder
                    // Aqui, vamos apenas exibir os dados do banco.
                    System.out.printf("Nome: %s, Idade: %d, Espécie: %s, Dono: %s\n", nome, idade, especie, nomeDono);
                    continue;
                }
                
                // Demonstração de Polimorfismo: o método toString() e emitirSom() são chamados
                // na referência Animal, mas a implementação é da subclasse.
                System.out.println(animal.toString() + ", Som: " + animal.emitirSom());
            }

            if (!encontrou) {
                System.out.println("Nenhum animal cadastrado.");
            }
            System.out.println("---------------------------\n");

        } catch (SQLException e) {
            if (e.getMessage().contains("No suitable driver")) {
                System.err.println("ERRO: Driver SQLite não encontrado. Verifique se sqlite-jdbc-3.45.1.0.jar e slf4j-api-2.0.9.jar estão no classpath.");
            } else {
                System.err.println("Erro ao listar animais: " + e.getMessage());
            }
        }
    }

    /**
     * Consulta um animal por nome.
     * @param nomeBusca O nome do animal a ser buscado.
     */
    public static void buscarAnimalPorNome(String nomeBusca) {
        // Tenta carregar o driver, mas não falha se não conseguir
        carregarDriver();
        
        String sql = "SELECT nome, idade, especie, nome_dono FROM Animal WHERE nome LIKE ?";

        try (Connection conn = DriverManager.getConnection(URL_CONEXAO);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + nomeBusca + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("\n--- Resultado da Busca por '" + nomeBusca + "' ---");
                boolean encontrou = false;
                while (rs.next()) {
                    encontrou = true;
                    String nome = rs.getString("nome");
                    int idade = rs.getInt("idade");
                    String especie = rs.getString("especie");
                    String nomeDono = rs.getString("nome_dono");

                    // Demonstração de Abstração e Encapsulamento
                    Animal animal;
                    if (especie.equalsIgnoreCase("Cachorro")) {
                        animal = new Cachorro(nome, idade, nomeDono);
                    } else if (especie.equalsIgnoreCase("Gato")) {
                        animal = new Gato(nome, idade, nomeDono);
                    } else {
                        System.out.printf("Nome: %s, Idade: %d, Espécie: %s, Dono: %s\n", nome, idade, especie, nomeDono);
                        continue;
                    }
                    
                    // Demonstração de Polimorfismo
                    System.out.println(animal.toString() + ", Som: " + animal.emitirSom());
                }

                if (!encontrou) {
                    System.out.println("Nenhum animal encontrado com o nome: " + nomeBusca);
                }
                System.out.println("-----------------------------------------\n");
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("No suitable driver")) {
                System.err.println("ERRO: Driver SQLite não encontrado. Verifique se sqlite-jdbc-3.45.1.0.jar e slf4j-api-2.0.9.jar estão no classpath.");
            } else {
                System.err.println("Erro ao buscar animal: " + e.getMessage());
            }
        }
    }

    /**
     * Exibe o menu e processa a escolha do usuário.
     */
    public static void menu(Scanner scanner) {
        int opcao = -1;
        while (opcao != 0) {
            System.out.println("=====================================");
            System.out.println("       Sistema Petshop - Menu");
            System.out.println("=====================================");
            System.out.println("1. Cadastrar Novo Animal");
            System.out.println("2. Consultar Animais Cadastrados (Listar Todos)");
            System.out.println("3. Consultar Animal por Nome");
            System.out.println("0. Sair");
            System.out.println("=====================================");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1; // Opção inválida
            }

            switch (opcao) {
                case 1:
                    cadastrarNovoAnimal(scanner);
                    break;
                case 2:
                    listarTodosAnimais();
                    break;
                case 3:
                    System.out.print("Digite o nome (ou parte do nome) do animal para buscar: ");
                    String nomeBusca = scanner.nextLine();
                    buscarAnimalPorNome(nomeBusca);
                    break;
                case 0:
                    System.out.println("Saindo do sistema. Obrigado!");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    /**
     * Coleta os dados do usuário para cadastrar um novo animal.
     */
    public static void cadastrarNovoAnimal(Scanner scanner) {
        System.out.println("\n--- Cadastro de Novo Animal ---");
        System.out.print("Nome do Animal: ");
        String nome = scanner.nextLine();

        int idade = -1;
        while (idade < 0) {
            System.out.print("Idade do Animal (anos): ");
            try {
                idade = Integer.parseInt(scanner.nextLine());
                if (idade < 0) {
                    System.out.println("Idade não pode ser negativa.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
            }
        }

        System.out.print("Espécie do Animal (Cachorro/Gato): ");
        String especie = scanner.nextLine();

        System.out.print("Nome do Dono: ");
        String nomeDono = scanner.nextLine();
        
        // Criação do objeto Dono (apenas para demonstração da classe, não persistido separadamente)
        // Dono dono = new Dono(nomeDono, "N/A"); 

        Animal novoAnimal = null;
        if (especie.equalsIgnoreCase("Cachorro")) {
            novoAnimal = new Cachorro(nome, idade, nomeDono);
        } else if (especie.equalsIgnoreCase("Gato")) {
            novoAnimal = new Gato(nome, idade, nomeDono);
        } else {
            // Para espécies não mapeadas, criamos um Animal genérico (apenas para persistir)
            // Na vida real, Animal não seria abstrato ou teríamos uma classe Genérico.
            // Aqui, vamos apenas avisar e não cadastrar para manter a didática.
            System.out.println("Espécie não suportada para demonstração de Herança/Polimorfismo. Cadastrando como " + especie + ".");
            // Criamos um Animal genérico para persistir no BD
            novoAnimal = new Animal(nome, idade, especie, nomeDono) {
                @Override
                public String emitirSom() {
                    return "Som Desconhecido";
                }
            };
        }

        cadastrarAnimal(novoAnimal);
    }

    public static void main(String[] args) {
        // O driver JDBC do SQLite deve ser carregado automaticamente (JDBC 4.0+)
        // Se não funcionar, o erro será tratado na inicialização do banco

        inicializarBanco();

        try (Scanner scanner = new Scanner(System.in)) {
            menu(scanner);
        }
    }
}
