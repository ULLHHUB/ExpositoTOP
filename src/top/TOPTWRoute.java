/**
 * @file TOPTWRoute.java
 * @brief Contiene la definición de la clase TOPTWRoute, que representa una ruta simple.
 */
package top;

/**
 * @class TOPTWRoute
 * @brief Modela una ruta simple mediante la identificación de sus nodos predecesor y sucesor.
 * @details Esta clase es una estructura de datos básica para almacenar información sobre un segmento de ruta.
 */
public class TOPTWRoute {
    /**
     * @brief El nodo predecesor en el segmento de la ruta.
     */
    int predecessor;
    /**
     * @brief El nodo sucesor en el segmento de la ruta.
     */
    int succesor;
    /**
     * @brief Identificador de la ruta a la que pertenece este segmento.
     */
    int id;
    
    /**
     * @brief Constructor por defecto.
     */
    TOPTWRoute() {
        
    }
    
    /**
     * @brief Constructor parametrizado.
     * @param pre El nodo predecesor.
     * @param succ El nodo sucesor.
     * @param id El identificador de la ruta.
     */
    TOPTWRoute(int pre, int succ, int id) {
        this.predecessor = pre;
        this.succesor = succ;
        this.id = id;
    }
    
    /**
     * @brief Obtiene el predecesor.
     * @return El índice del nodo predecesor.
     */
    public int getPredeccesor() {
        return this.predecessor;
    }
    
    /**
     * @brief Obtiene el sucesor.
     * @return El índice del nodo sucesor.
     */
    public int getSuccesor() {
        return this.succesor;
    }
    
    /**
     * @brief Obtiene el ID de la ruta.
     * @return El identificador de la ruta.
     */
    public int getId() {
        return this.id;
    }
    
    /**
     * @brief Establece el predecesor.
     * @param pre El nuevo índice del nodo predecesor.
     */
    public void setPredeccesor(int pre) {
        this.predecessor = pre;
    }
    
    /**
     * @brief Establece el sucesor.
     * @param suc El nuevo índice del nodo sucesor.
     */
    public void setSuccesor(int suc) {
        this.succesor = suc;
    }
    
    /**
     * @brief Establece el ID de la ruta.
     * @param id El nuevo identificador de la ruta.
     */
    public void setId(int id) {
        this.id = id;
    }
}
