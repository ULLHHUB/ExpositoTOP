/**
 * @file Pair.java
 * @brief Contiene la definición de una clase genérica para almacenar un par de objetos.
 */
package es.ull.esit.utils;
import java.util.Objects;

/**
 * @class Pair
 * @brief Una clase genérica que almacena un par de objetos inmutables.
 * @details Proporciona una forma conveniente de tratar dos objetos como una única unidad.
 * Los objetos `first` y `second` son públicos y finales.
 * @param <F> El tipo del primer elemento.
 * @param <S> El tipo del segundo elemento.
 */
public class Pair<F, S> {
    /**
     * @brief El primer elemento del par.
     */
    public final F first;
    /**
     * @brief El segundo elemento del par.
     */
    public final S second;

    /**
     * @brief Constructor para crear un par.
     * @param first El primer objeto.
     * @param second El segundo objeto.
     */
    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    /**
     * @brief Compara este par con otro objeto para ver si son iguales.
     * @param o El objeto a comparar.
     * @return `true` si el objeto es un `Pair` y ambos elementos son iguales, `false` en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) {
            return false;
        }
        Pair<?, ?> p = (Pair<?, ?>) o;
        return Objects.equals(p.first, first) && Objects.equals(p.second, second);
    }

    /**
     * @brief Calcula el código hash para el par.
     * @return El código hash, basado en los códigos hash de los dos elementos.
     */
    @Override
    public int hashCode() {
        return (first == null ? 0 : first.hashCode()) ^ (second == null ? 0 : second.hashCode());
    }

    /**
     * @brief Método de fábrica estático para crear una instancia de `Pair`.
     * @details Permite la creación de un par con inferencia de tipos.
     * @param <A> El tipo del primer elemento.
     * @param <B> El tipo del segundo elemento.
     * @param a El primer objeto.
     * @param b El segundo objeto.
     * @return Una nueva instancia de `Pair` conteniendo `a` y `b`.
     */
    public static <A, B> Pair <A, B> create(A a, B b) {
        return new Pair<A, B>(a, b);
    }
}
