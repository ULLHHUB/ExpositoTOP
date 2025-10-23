/**
 * @file PowerSet.java
 * @brief Contiene la implementación de un iterador para generar el conjunto potencia de un conjunto dado.
 */
package es.ull.esit.utilities;

import java.util.BitSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

/**
 * @class PowerSet
 * @brief Implementa un iterador para generar todos los subconjuntos (conjunto potencia) de un conjunto dado.
 * @details Utiliza un `BitSet` para generar de forma eficiente todas las combinaciones de elementos del conjunto original.
 * @param <E> El tipo de los elementos en el conjunto.
 */
// Sirve para calcular todos los subconjuntos de un conjunto dado
public class PowerSet<E> implements Iterable<Set<E>> {

    private E[] arr = null;

    /**
     * @brief Constructor de la clase PowerSet.
     * @param set El conjunto original del cual se generará el conjunto potencia.
     */
    @SuppressWarnings("unchecked")
    public PowerSet(Set<E> set) {
        this.arr = (E[]) set.toArray();
    }

    /**
     * @brief Devuelve un iterador sobre los subconjuntos.
     * @return una nueva instancia del iterador del conjunto potencia.
     */
    @Override
    public Iterator<Set<E>> iterator() {
        return new PowerSetIterator();
    }

    private class PowerSetIterator implements Iterator<Set<E>> {
        private BitSet bset;

        PowerSetIterator() {
            this.bset = new BitSet(PowerSet.this.arr.length + 1);
        }

        /**
         * @brief Comprueba si hay más subconjuntos para generar.
         * @return `true` si hay más subconjuntos, `false` en caso contrario.
         */
        @Override
        public boolean hasNext() {
            return !this.bset.get(PowerSet.this.arr.length);
        }

        /**
         * @brief Devuelve el siguiente subconjunto en la secuencia.
         * @return Un `Set<E>` que representa el siguiente subconjunto.
         * @throws NoSuchElementException si no hay más elementos.
         */
        @Override
        public Set<E> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Set<E> returnSet = new TreeSet<>();
            for (int i = 0; i < PowerSet.this.arr.length; i++) {
                if (this.bset.get(i)) {
                    returnSet.add(PowerSet.this.arr[i]);
                }
            }
            // Incrementa el bitset para la siguiente combinación
            for (int i = 0; i < this.bset.size(); i++) {
                if (!this.bset.get(i)) {
                    this.bset.set(i);
                    break;
                } else {
                    this.bset.clear(i);
                }
            }
            return returnSet;
        }

        /**
         * @brief Operación no soportada.
         * @throws UnsupportedOperationException Siempre.
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not Supported!");
        }
    }
}