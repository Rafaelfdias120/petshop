package petshop;

/**
 * Classe Cachorro.
 * Demonstra o conceito de Herança (herda de Animal).
 * Demonstra o conceito de Polimorfismo (sobrescreve emitirSom()).
 */
public class Cachorro extends Animal {

    /**
     * Construtor da classe Cachorro.
     * @param nome O nome do cachorro.
     * @param idade A idade do cachorro.
     * @param nomeDono O nome do dono do cachorro.
     */
    public Cachorro(String nome, int idade, String nomeDono) {
        // Chama o construtor da superclasse (Animal)
        super(nome, idade, "Cachorro", nomeDono);
    }

    /**
     * Implementação do método abstrato emitirSom().
     * Demonstra Polimorfismo.
     * @return O som que o cachorro emite.
     */
    @Override
    public String emitirSom() {
        return "Au Au!";
    }
}
