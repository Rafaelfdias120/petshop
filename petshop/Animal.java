package petshop;

/**
 * Classe abstrata Animal.
 * Demonstra o conceito de Abstração.
 * Serve como base para classes específicas de animais (Herança).
 */
public abstract class Animal {
    // Encapsulamento: atributos privados
    private String nome;
    private int idade;
    private String especie;
    private String nomeDono;

    /**
     * Construtor da classe Animal.
     * @param nome O nome do animal.
     * @param idade A idade do animal.
     * @param especie A espécie do animal.
     * @param nomeDono O nome do dono do animal.
     */
    public Animal(String nome, int idade, String especie, String nomeDono) {
        this.nome = nome;
        this.idade = idade;
        this.especie = especie;
        this.nomeDono = nomeDono;
    }

    // Encapsulamento: Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getNomeDono() {
        return nomeDono;
    }

    public void setNomeDono(String nomeDono) {
        this.nomeDono = nomeDono;
    }

    /**
     * Método abstrato para demonstrar Polimorfismo.
     * Cada subclasse deve fornecer sua própria implementação.
     */
    public abstract String emitirSom();

    /**
     * Método para exibir informações básicas do animal.
     * @return Uma string com as informações do animal.
     */
    @Override
    public String toString() {
        return "Nome: " + nome + ", Idade: " + idade + " anos, Espécie: " + especie + ", Dono: " + nomeDono;
    }
}
