/**
 * @file TOPTW.java
 * @brief Contiene la definición de la clase TOPTW, que modela una instancia del Team Orienteering Problem with Time Windows.
 */
package top;

import java.util.ArrayList;
import java.util.Arrays;

import es.ull.esit.utilities.ExpositoUtilities;

/**
 * @class TOPTW
 * @brief Modela una instancia del problema TOPTW (Team Orienteering Problem with Time Windows).
 * @details Esta clase almacena todos los datos de una instancia del problema, incluyendo la información
 * de los nodos (coordenadas, puntuaciones, ventanas de tiempo), el número de vehículos, y las restricciones
 * generales como el tiempo máximo por ruta. También proporciona métodos para calcular distancias y acceder a los datos.
 */
public class TOPTW {
    private int nodes;
    private double[] x;
    private double[] y;
    private double[] score;
    private double[] readyTime;
    private double[] dueTime;
    private double[] serviceTime;
    private int vehicles;
    private int depots;
    private double maxTimePerRoute;
    private double maxRoutes;
    private double[][] distanceMatrix;

    /**
     * @brief Constructor de la clase TOPTW.
     * @param nodes El número de nodos (clientes/POIs) en el problema, sin contar el depósito.
     * @param routes El número de rutas (vehículos) disponibles.
     */
    public TOPTW(int nodes, int routes) {
        this.nodes = nodes;
        this.depots = 0;
        this.x = new double[this.nodes + 1];
        this.y = new double[this.nodes + 1];
        this.score = new double[this.nodes + 1];
        this.readyTime = new double[this.nodes + 1];
        this.dueTime = new double[this.nodes + 1];
        this.serviceTime = new double[this.nodes + 1];
        this.distanceMatrix = new double[this.nodes + 1][this.nodes + 1];
        for (int i = 0; i < this.nodes + 1; i++) {
            for (int j = 0; j < this.nodes + 1; j++) {
                this.distanceMatrix[i][j] = 0.0;
            }
        }
        this.maxRoutes = routes;
        this.vehicles = routes;
    }
    
    /**
     * @brief Comprueba si un índice de nodo corresponde a un depósito.
     * @param a El índice del nodo a comprobar. Los depósitos tienen índices mayores que el número de nodos.
     * @return `true` si el nodo es un depósito, `false` en caso contrario.
     */
    public boolean isDepot(int a) {
        if(a > this.nodes) {
            return true;
        }
        return false;
    }

    /**
     * @brief Calcula la distancia total de una ruta representada por un array de enteros.
     * @param route La ruta como un array de índices de nodos.
     * @return La distancia total de la ruta.
     */
    public double getDistance(int[] route) {
        double distance = 0.0;
        for (int i = 0; i < route.length - 1; i++) {
            int node1 = route[i];
            int node2 = route[i + 1];
            distance += this.getDistance(node1, node2);
        }
        return distance;
    }

    /**
     * @brief Calcula la distancia total de una ruta representada por un ArrayList de enteros.
     * @param route La ruta como un ArrayList de índices de nodos.
     * @return La distancia total de la ruta.
     */
    public double getDistance(ArrayList<Integer> route) {
        double distance = 0.0;
        for (int i = 0; i < route.size() - 1; i++) {
            int node1 = route.get(i);
            int node2 = route.get(i + 1);
            distance += this.getDistance(node1, node2);
        }
        return distance;
    }

    /**
     * @brief Calcula la distancia total sumando las distancias de múltiples rutas.
     * @param routes Un array de ArrayLists, donde cada ArrayList es una ruta.
     * @return La distancia total acumulada de todas las rutas.
     */
    public double getDistance(ArrayList<Integer>[] routes) {
        double distance = 0.0;
        for (ArrayList<Integer> route : routes) {
            distance += this.getDistance(route);
        }
        return distance;
    }

    /**
     * @brief Calcula y almacena la matriz de distancias euclidianas entre todos los nodos.
     * @details Utiliza las coordenadas X e Y de los nodos para calcular la distancia.
     * La matriz es simétrica.
     */
    public void calculateDistanceMatrix() {
        for (int i = 0; i < this.nodes + 1; i++) {
            for (int j = 0; j < this.nodes + 1; j++) {
                if (i != j) {
                    double diffXs = this.x[i] - this.x[j];
                    double diffYs = this.y[i] - this.y[j];
                    this.distanceMatrix[i][j] = Math.sqrt(diffXs * diffXs + diffYs * diffYs);
                    this.distanceMatrix[j][i] = this.distanceMatrix[i][j];
                } else {
                    this.distanceMatrix[i][j] = 0.0;
                }
            }
        }
    }

    /**
     * @brief Obtiene el tiempo máximo permitido por ruta.
     * @return El tiempo máximo por ruta.
     */
    public double getMaxTimePerRoute() {
        return maxTimePerRoute;
    }

    /**
     * @brief Establece el tiempo máximo permitido por ruta.
     * @param maxTimePerRoute El nuevo tiempo máximo por ruta.
     */
    public void setMaxTimePerRoute(double maxTimePerRoute) {
        this.maxTimePerRoute = maxTimePerRoute;
    }

    /**
     * @brief Obtiene el número máximo de rutas (vehículos) permitidas.
     * @return El número máximo de rutas.
     */
    public double getMaxRoutes() {
        return maxRoutes;
    }

    /**
     * @brief Establece el número máximo de rutas.
     * @param maxRoutes El nuevo número máximo de rutas.
     */
    public void setMaxRoutes(double maxRoutes) {
        this.maxRoutes = maxRoutes;
    }
    
    /**
     * @brief Obtiene el número de Puntos de Interés (POIs), sin contar el depósito.
     * @return El número de nodos cliente.
     */
    public int getPOIs() {
        return this.nodes;
    }

    /**
     * @brief Obtiene la distancia entre dos nodos.
     * @details Si alguno de los nodos es un depósito, se mapea al índice 0.
     * @param i Índice del primer nodo.
     * @param j Índice del segundo nodo.
     * @return La distancia entre los nodos i y j.
     */
    public double getDistance(int i, int j) {
        if(this.isDepot(i)) { i=0; }
        if(this.isDepot(j)) { j=0; }
        return this.distanceMatrix[i][j];
    }

    /**
     * @brief Obtiene el tiempo de viaje entre dos nodos, que es igual a la distancia.
     * @param i Índice del primer nodo.
     * @param j Índice del segundo nodo.
     * @return El tiempo de viaje entre i y j.
     */
    public double getTime(int i, int j) {
        if(this.isDepot(i)) { i=0; }
        if(this.isDepot(j)) { j=0; }
        return this.distanceMatrix[i][j];
    }

    /**
     * @brief Obtiene el número total de nodos (clientes).
     * @return El número de nodos.
     */
    public int getNodes() {
        return this.nodes;
    }

    /**
     * @brief Establece el número de nodos.
     * @param nodes El nuevo número de nodos.
     */
    public void setNodes(int nodes) {
        this.nodes = nodes;
    }

    /**
     * @brief Obtiene la coordenada X de un nodo.
     * @param index Índice del nodo.
     * @return La coordenada X.
     */
    public double getX(int index) {
        if(this.isDepot(index)) { index=0; }
        return this.x[index];
    }

    /**
     * @brief Establece la coordenada X de un nodo.
     * @param index Índice del nodo.
     * @param x El valor de la coordenada X.
     */
    public void setX(int index, double x) {
        this.x[index] = x;
    }

    /**
     * @brief Obtiene la coordenada Y de un nodo.
     * @param index Índice del nodo.
     * @return La coordenada Y.
     */
    public double getY(int index) {
        if(this.isDepot(index)) { index=0; }
        return this.y[index];
    }

    /**
     * @brief Establece la coordenada Y de un nodo.
     * @param index Índice del nodo.
     * @param y El valor de la coordenada Y.
     */
    public void setY(int index, double y) {
        this.y[index] = y;
    }

    /**
     * @brief Obtiene la puntuación (score) de un nodo.
     * @param index Índice del nodo.
     * @return La puntuación del nodo.
     */
    public double getScore(int index) {
        if(this.isDepot(index)) { index=0; }
        return this.score[index];
    }
    
    /**
     * @brief Obtiene el array completo de puntuaciones de todos los nodos.
     * @return Un array de doubles con las puntuaciones.
     */
    public double[] getScore() {
        return this.score;
    }

    /**
     * @brief Establece la puntuación de un nodo.
     * @param index Índice del nodo.
     * @param score El valor de la puntuación.
     */
    public void setScore(int index, double score) {
        this.score[index] = score;
    }

    /**
     * @brief Obtiene el tiempo de inicio de la ventana de tiempo (ready time) de un nodo.
     * @param index Índice del nodo.
     * @return El ready time del nodo.
     */
    public double getReadyTime(int index) {
        if(this.isDepot(index)) { index=0; }
        return this.readyTime[index];
    }

    /**
     * @brief Establece el tiempo de inicio de la ventana de tiempo (ready time) de un nodo.
     * @param index Índice del nodo.
     * @param readyTime El valor del ready time.
     */
    public void setReadyTime(int index, double readyTime) {
        this.readyTime[index] = readyTime;
    }

    /**
     * @brief Obtiene el tiempo de fin de la ventana de tiempo (due time) de un nodo.
     * @param index Índice del nodo.
     * @return El due time del nodo.
     */
    public double getDueTime(int index) {
        if(this.isDepot(index)) { index=0; }
        return this.dueTime[index];
    }

    /**
     * @brief Establece el tiempo de fin de la ventana de tiempo (due time) de un nodo.
     * @param index Índice del nodo.
     * @param dueTime El valor del due time.
     */
    public void setDueTime(int index, double dueTime) {
        this.dueTime[index] = dueTime;
    }

    /**
     * @brief Obtiene el tiempo de servicio requerido en un nodo.
     * @param index Índice del nodo.
     * @return El tiempo de servicio.
     */
    public double getServiceTime(int index) {
        if(this.isDepot(index)) { index=0; }
        return this.serviceTime[index];
    }

    /**
     * @brief Establece el tiempo de servicio de un nodo.
     * @param index Índice del nodo.
     * @param serviceTime El valor del tiempo de servicio.
     */
    public void setServiceTime(int index, double serviceTime) {
        this.serviceTime[index] = serviceTime;
    }

    /**
     * @brief Obtiene el número de vehículos disponibles.
     * @return El número de vehículos.
     */
    public int getVehicles() {
        return this.vehicles;
    }
    
    /**
     * @brief Genera una representación en formato de cadena de la instancia del problema.
     * @return Una cadena formateada con los detalles de los nodos y vehículos.
     */
    @Override
    public String toString() {
        final int COLUMN_WIDTH = 15;
        String text = "Nodes: " + this.nodes + "\n";
        String[] strings = new String[]{"CUST NO.", "XCOORD.", "YCOORD.", "SCORE", "READY TIME", "DUE DATE", "SERVICE TIME"};
        int[] width = new int[strings.length];
        Arrays.fill(width, COLUMN_WIDTH);
        text += ExpositoUtilities.getFormat(strings, width) + "\n";
        for (int i = 0; i < this.nodes; i++) {
            strings = new String[strings.length];
            int index = 0;
            //strings[index++] = Integer.toString("" + i);
            strings[index++] = Integer.toString(i);
            strings[index++] = "" + this.x[i];
            strings[index++] = "" + this.y[i];
            strings[index++] = "" + this.score[i];
            strings[index++] = "" + this.readyTime[i];
            strings[index++] = "" + this.dueTime[i];
            strings[index++] = "" + this.serviceTime[i];
            text += ExpositoUtilities.getFormat(strings, width);
            text += "\n";
        }
        text += "Vehicles: " + this.vehicles + "\n";
        strings = new String[]{"VEHICLE", "CAPACITY"};
        width = new int[strings.length];
        Arrays.fill(width, COLUMN_WIDTH);
        text += ExpositoUtilities.getFormat(strings, width) + "\n";
        return text;
    }

    /**
     * @brief Incrementa el contador de nodos.
     * @deprecated Este método parece no ser utilizado de forma segura, ya que no redimensiona los arrays.
     * @return El nuevo número de nodos.
     */
    public int addNode() {
        this.nodes++;
        return this.nodes;
    }
    
    /**
     * @brief Incrementa el contador de depósitos.
     * @deprecated Este método parece no ser utilizado de forma segura.
     * @return El nuevo número de depósitos.
     */
    public int addNodeDepot() {
        this.depots++;
        return this.depots;
    }
}
