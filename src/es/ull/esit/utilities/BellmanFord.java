/**
 * @file BellmanFord.java
 * @brief Contiene la implementación del algoritmo de Bellman-Ford para encontrar el camino más corto en un grafo con pesos.
 */
package es.ull.esit.utilities;

import java.util.ArrayList;

/**
 * @class BellmanFord
 * @brief Implementa el algoritmo de Bellman-Ford.
 * @details Se utiliza para encontrar los caminos más cortos desde un único nodo origen a todos los demás nodos
 * en un grafo dirigido y ponderado. Es capaz de manejar aristas con pesos negativos.
 */
public class BellmanFord {

    /**
     * @brief Valor que representa el infinito, usado para inicializar distancias.
     */
    private static final int INFINITY = 999999;
    /**
     * @brief Matriz de adyacencia que representa las distancias (pesos) entre nodos.
     */
    private final int[][] distanceMatrix;
    /**
     * @brief Lista de nodos de origen para cada arista del grafo.
     */
    private ArrayList<Integer> edges1 = null;
    /**
     * @brief Lista de nodos de destino para cada arista del grafo.
     */
    private ArrayList<Integer> edges2 = null;
    /**
     * @brief Número total de nodos en el grafo.
     */
    private final int nodes;
    /**
     * @brief Lista para almacenar el camino más corto encontrado.
     */
    private final ArrayList<Integer> path;
    /**
     * @brief Array para almacenar las distancias más cortas desde el origen a cada nodo.
     */
    private int[] distances = null;
    /**
     * @brief El valor (coste) del camino más corto encontrado.
     */
    private int value;

    /**
     * @brief Constructor de la clase BellmanFord.
     * @param distanceMatrix La matriz de adyacencia del grafo.
     * @param nodes El número de nodos en el grafo.
     * @param path Una lista (generalmente vacía) que se llenará con el camino encontrado.
     */
    public BellmanFord(int[][] distanceMatrix, int nodes, ArrayList<Integer> path) {
        this.distanceMatrix = distanceMatrix;
        this.nodes = nodes;
        this.path = path;
        this.calculateEdges();
        this.value = BellmanFord.INFINITY;
    }

    /**
     * @brief Construye las listas de aristas a partir de la matriz de adyacencia.
     * @details Recorre la matriz de distancias y, si existe una arista (distancia no es infinita),
     * la añade a las listas `edges1` (origen) y `edges2` (destino).
     */
    private void calculateEdges() {
        this.edges1 = new ArrayList<>();
        this.edges2 = new ArrayList<>();
        for (int i = 0; i < this.nodes; i++) {
            for (int j = 0; j < this.nodes; j++) {
                if (this.distanceMatrix[i][j] != Integer.MAX_VALUE) {
                    this.edges1.add(i);
                    this.edges2.add(j);
                }
            }
        }
    }

    /**
     * @brief Obtiene el array de distancias calculadas desde el nodo origen.
     * @return Un array de enteros con las distancias.
     */
    public int[] getDistances() {
        return this.distances;
    }

    /**
     * @brief Obtiene el valor (coste) del camino más corto calculado.
     * @return El coste del camino.
     */
    public int getValue() {
        return this.value;
    }

    /**
     * @brief Ejecuta el algoritmo de Bellman-Ford.
     * @details Inicializa las distancias y predecesores, y luego relaja las aristas repetidamente
     * para encontrar los caminos más cortos. Finalmente, reconstruye el camino desde el destino
     * hasta el origen utilizando el array de predecesores.
     */
    public void solve() {
        int numEdges = this.edges1.size();
        int[] predecessor = new int[this.nodes];
        this.distances = new int[this.nodes];
        for (int i = 0; i < this.nodes; i++) {
            this.distances[i] = BellmanFord.INFINITY;
            predecessor[i] = -1;
        }
        this.distances[0] = 0;
        for (int i = 0; i < (this.nodes - 1); i++) {
            for (int j = 0; j < numEdges; j++) {
                int u = this.edges1.get(j);
                int v = this.edges2.get(j);
                if (this.distances[v] > this.distances[u] + this.distanceMatrix[u][v]) {
                    this.distances[v] = this.distances[u] + this.distanceMatrix[u][v];
                    predecessor[v] = u;
                }
            }
        }
        this.path.add(this.nodes - 1);
        int pred = predecessor[this.nodes - 1];
        while (pred != -1) {
            this.path.add(pred);
            pred = predecessor[pred];
        }
        this.value = -this.distances[this.nodes - 1];
    }
}
