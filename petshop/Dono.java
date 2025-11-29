package petshop;

/**
 * Classe Dono.
 * Demonstra o conceito de Encapsulamento.
 */
public class Dono {
    // Encapsulamento: atributos privados
    private String nome;
    private String telefone;

    /**
     * Construtor da classe Dono.
     * @param nome O nome do dono.
     * @param telefone O telefone de contato do dono.
     */
    public Dono(String nome, String telefone) {
        this.nome = nome;
        this.telefone = telefone;
    }

    // Encapsulamento: Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return "Dono [nome=" + nome + ", telefone=" + telefone + "]";
    }
}
