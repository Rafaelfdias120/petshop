package petshop;

/**
 * Classe Gato.
 * Demonstra o conceito de Herança (herda de Animal).
 * Demonstra o conceito de Polimorfismo (sobrescreve emitirSom()).
 */
public class Gato extends Animal {

    /**
     * Construtor da classe Gato.
     * @param nome O nome do gato.
     * @param idade A idade do gato.
     * @param nomeDono O nome do dono do gato.
     */
    public Gato(String nome, int idade, String nomeDono) {
        // Chama o construtor da superclasse (Animal)
        super(nome, idade, "Gato", nomeDono);
    }

    /**
     * Implementação do método abstrato emitirSom().
     * Demonstra Polimorfismo.
     * @return O som que o gato emite.
     */
    @Override
    public String emitirSom() {
        return "Miau!";
    }
}
