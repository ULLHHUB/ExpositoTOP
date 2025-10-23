/**
 * @file TOPTWSolution.java
 * @brief Contiene la definición de la clase TOPTWSolution, que representa una solución para el problema TOPTW.
 */
package top;

import java.util.Arrays;

import es.ull.esit.utilities.ExpositoUtilities;

/**
 * @class TOPTWSolution
 * @brief Representa una solución a una instancia del problema TOPTW.
 * @details Almacena la estructura de las rutas (mediante predecesores y sucesores), los tiempos de espera,
 * y el valor de la función objetivo. Proporciona métodos para construir, modificar y evaluar la solución.
 */
public class TOPTWSolution {
    /**
     * @brief Constante para indicar que un valor no ha sido inicializado.
     */
    public static final int NO_INITIALIZED = -1;
    private TOPTW problem;
    private int[] predecessors;
    private int[] successors;
    private double[] waitingTime;
    private int[] positionInRoute;
    
    private int[] routes;
    private int availableVehicles;
    private double objectiveFunctionValue;
    
    /**
     * @brief Constructor de la clase TOPTWSolution.
     * @param problem La instancia del problema TOPTW para la cual se crea esta solución.
     */
    public TOPTWSolution(TOPTW problem) {
        this.problem = problem;
        this.availableVehicles = this.problem.getVehicles();
        this.predecessors = new int[this.problem.getPOIs()+this.problem.getVehicles()];
        this.successors = new int[this.problem.getPOIs()+this.problem.getVehicles()];
        this.waitingTime = new double[this.problem.getPOIs()];
        this.positionInRoute = new int[this.problem.getPOIs()];
        Arrays.fill(this.predecessors, TOPTWSolution.NO_INITIALIZED);
        Arrays.fill(this.successors, TOPTWSolution.NO_INITIALIZED);
        Arrays.fill(this.waitingTime, TOPTWSolution.NO_INITIALIZED);
        Arrays.fill(this.positionInRoute, TOPTWSolution.NO_INITIALIZED);
        this.routes = new int[this.problem.getVehicles()];
        this.objectiveFunctionValue = TOPTWEvaluator.NO_EVALUATED;
    }
    
    /**
     * @brief Inicializa o resetea la solución a un estado básico.
     * @details Crea una única ruta con el depósito principal (índice 0) y establece los vehículos disponibles.
     */
    public void initSolution() {
        this.predecessors = new int[this.problem.getPOIs()+this.problem.getVehicles()];
        this.successors = new int[this.problem.getPOIs()+this.problem.getVehicles()];
        Arrays.fill(this.predecessors, TOPTWSolution.NO_INITIALIZED);
        Arrays.fill(this.successors, TOPTWSolution.NO_INITIALIZED);
        this.routes = new int[this.problem.getVehicles()];
        Arrays.fill(this.routes, TOPTWSolution.NO_INITIALIZED);
        this.routes[0] = 0;
        this.predecessors[0] = 0;
        this.successors[0] = 0;
        this.availableVehicles = this.problem.getVehicles() - 1;
    }
    
    /**
     * @brief Comprueba si un nodo es un depósito.
     * @param c El índice del nodo a comprobar.
     * @return `true` si el nodo es un depósito de alguna de las rutas, `false` en caso contrario.
     */
    public boolean isDepot(int c) {
        for(int i = 0; i < this.routes.length; i++) {
            if(c==this.routes[i]) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * @brief Compara si dos soluciones son iguales basándose en sus arrays de predecesores.
     * @param otherSolution La otra solución con la que comparar.
     * @return `true` si las soluciones son idénticas, `false` en caso contrario.
     */
    public boolean equals(TOPTWSolution otherSolution) {
        for (int i = 0; i < this.predecessors.length; i++) {
            if (this.predecessors[i] != otherSolution.predecessors[i]) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * @brief Compara si dos soluciones son iguales basándose en sus arrays de predecesores.
     * @param o El otro objeto con el que comparar.
     * @return `true` si las soluciones son idénticas, `false` en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TOPTWSolution that = (TOPTWSolution) o;
        return Arrays.equals(predecessors, that.predecessors);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(predecessors);
    }

    /**
     * @brief Obtiene el número de vehículos que aún no han sido asignados a una ruta.
     * @return El número de vehículos disponibles.
     */
    public int getAvailableVehicles() {
        return this.availableVehicles;
    }
    
    /**
     * @brief Obtiene el número de rutas que han sido creadas en la solución.
     * @return El número de rutas activas.
     */
    public int getCreatedRoutes() {
        return this.problem.getVehicles() - this.availableVehicles;
    }
    
    /**
     * @brief Obtiene la distancia entre dos nodos consultando el problema asociado.
     * @param x Índice del primer nodo.
     * @param y Índice del segundo nodo.
     * @return La distancia entre x e y.
     */
    public double getDistance(int x, int y) {
        return this.problem.getDistance(x, y);
    }

    /**
     * @brief Establece el número de vehículos disponibles.
     * @param availableVehicles El nuevo número de vehículos disponibles.
     */
    public void setAvailableVehicles(int availableVehicles) {
        this.availableVehicles = availableVehicles;
    }
    
    /**
     * @brief Obtiene el predecesor de un cliente en su ruta.
     * @param customer El índice del cliente.
     * @return El índice del nodo predecesor.
     */
    public int getPredecessor(int customer) {
        return this.predecessors[customer];
    }
    
    /**
     * @brief Obtiene el array completo de predecesores.
     * @return El array de predecesores.
     */
    public int[] getPredecessors() {
        return this.predecessors;
    }

    /**
     * @brief Obtiene la instancia del problema asociada a esta solución.
     * @return La instancia de TOPTW.
     */
    public TOPTW getProblem() {
        return this.problem;
    }

    /**
     * @brief Obtiene el valor de la función objetivo (puntuación total) de la solución.
     * @return El valor de la función objetivo.
     */
    public double getObjectiveFunctionValue() {
        return this.objectiveFunctionValue;
    }

    /**
     * @brief Obtiene la posición de un cliente dentro de su ruta.
     * @param customer El índice del cliente.
     * @return La posición en la ruta.
     */
    public int getPositionInRoute(int customer) {
        return this.positionInRoute[customer];
    }

    /**
     * @brief Obtiene el sucesor de un cliente en su ruta.
     * @param customer El índice del cliente.
     * @return El índice del nodo sucesor.
     */
    public int getSuccessor(int customer) {
        return this.successors[customer];
    }
    
    /**
     * @brief Obtiene el array completo de sucesores.
     * @return El array de sucesores.
     */
    public int[] getSuccessors() {
        return this.successors;
    }

    /**
     * @brief Obtiene el índice del nodo que actúa como depósito para una ruta específica.
     * @param index El índice de la ruta (de 0 a `getCreatedRoutes()`-1).
     * @return El índice del nodo depósito.
     */
    public int getIndexRoute(int index) {
        return this.routes[index];
    }

    /**
     * @brief Obtiene el tiempo de espera en un nodo cliente.
     * @param customer El índice del cliente.
     * @return El tiempo de espera.
     */
    public double getWaitingTime(int customer) {
        return this.waitingTime[customer];
    }

    /**
     * @brief Establece el valor de la función objetivo.
     * @param objectiveFunctionValue El nuevo valor.
     */
    public void setObjectiveFunctionValue(double objectiveFunctionValue) {
        this.objectiveFunctionValue = objectiveFunctionValue;
    }

    /**
     * @brief Establece la posición de un cliente en su ruta.
     * @param customer El índice del cliente.
     * @param position La nueva posición.
     */
    public void setPositionInRoute(int customer, int position) {
        this.positionInRoute[customer] = position;
    }

    /**
     * @brief Establece el predecesor de un nodo.
     * @param customer El índice del nodo.
     * @param predecessor El índice del nuevo predecesor.
     */
    public void setPredecessor(int customer, int predecessor) {
        this.predecessors[customer] = predecessor;
    }

    /**
     * @brief Establece el sucesor de un nodo.
     * @param customer El índice del nodo.
     * @param succesor El índice del nuevo sucesor.
     */
    public void setSuccessor(int customer, int succesor) {
        this.successors[customer] = succesor;
    }

    /**
     * @brief Establece el tiempo de espera en un nodo.
     * @param customer El índice del nodo.
     * @param waitingTime El nuevo tiempo de espera.
     */
    public void setWaitingTime(int customer, int waitingTime) {
        this.waitingTime[customer] = waitingTime;
    }
    
    /**
     * @brief Genera una cadena con información detallada de la solución.
     * @details Recorre cada ruta, calcula los tiempos de llegada y salida, y verifica la factibilidad
     * de la solución con respecto a las ventanas de tiempo y el tiempo máximo por ruta.
     * @return Una cadena formateada con el resumen de la solución y los detalles de cada ruta.
     */
    public String getInfoSolution() {
        final int COLUMN_WIDTH = 15;
        String text = "\n"+"NODES: " + this.problem.getPOIs() + "\n" + "MAX TIME PER ROUTE: " + this.problem.getMaxTimePerRoute() + "\n" + "MAX NUMBER OF ROUTES: " + this.problem.getMaxRoutes() + "\n";
        String textSolution = "\n"+"SOLUTION: "+"\n";
        double costTimeSolution = 0.0, fitnessScore = 0.0;
        boolean validSolution = true;
        for(int k = 0; k < this.getCreatedRoutes(); k++) { // rutas creadas
            String[] strings = new String[]{"\n" + "ROUTE " + k };
            int[] width = new int[strings.length];
            Arrays.fill(width, COLUMN_WIDTH);
            text += ExpositoUtilities.getFormat(strings, width) + "\n";
            strings = new String[]{"CUST NO.", "X COORD.", "Y. COORD.", "READY TIME", "DUE DATE", "ARRIVE TIME", " LEAVE TIME", "SERVICE TIME"};
            width = new int[strings.length];
            Arrays.fill(width, COLUMN_WIDTH);
            text += ExpositoUtilities.getFormat(strings, width) + "\n";
            strings = new String[strings.length];
            int depot = this.getIndexRoute(k);
            int pre=-1, suc=-1;
            double costTimeRoute = 0.0, fitnessScoreRoute = 0.0;
            pre = depot;
            int index = 0;
            strings[index++] = "" + pre;
            strings[index++] = "" + this.getProblem().getX(pre);
            strings[index++] = "" + this.getProblem().getY(pre);
            strings[index++] = "" + this.getProblem().getReadyTime(pre);
            strings[index++] = "" + this.getProblem().getDueTime(pre);
            strings[index++] = "" + 0;
            strings[index++] = "" + 0;
            strings[index++] = "" + this.getProblem().getServiceTime(pre);
            text += ExpositoUtilities.getFormat(strings, width);
            text += "\n";
            do {                // recorremos la ruta
                index = 0;
                suc = this.getSuccessor(pre);
                textSolution += pre+" - ";
                strings[index++] = "" + suc;
                strings[index++] = "" + this.getProblem().getX(suc);
                strings[index++] = "" + this.getProblem().getY(suc);
                strings[index++] = "" + this.getProblem().getReadyTime(suc);
                strings[index++] = "" + this.getProblem().getDueTime(suc);
                costTimeRoute += this.getDistance(pre, suc);
                if(costTimeRoute < (this.getProblem().getDueTime(suc))) {
                    if(costTimeRoute < this.getProblem().getReadyTime(suc)) {
                        costTimeRoute = this.getProblem().getReadyTime(suc);
                    }
                    strings[index++] = "" + costTimeRoute;
                    costTimeRoute +=  this.getProblem().getServiceTime(suc);
                    strings[index++] = "" + costTimeRoute;
                    strings[index++] = "" + this.getProblem().getServiceTime(pre);
                    if(costTimeRoute > this.getProblem().getMaxTimePerRoute()) { validSolution = false; }
                    fitnessScoreRoute += this.problem.getScore(suc);
                } else { validSolution = false; }                  
                pre = suc;
                text += ExpositoUtilities.getFormat(strings, width);
                text += "\n";
            } while(suc != depot);
            textSolution += suc+"\n";
            costTimeSolution += costTimeRoute;
            fitnessScore += fitnessScoreRoute;
        }
        textSolution += "FEASIBLE SOLUTION: "+validSolution+"\n"+"SCORE: "+fitnessScore+"\n"+"TIME COST: "+costTimeSolution+"\n";
        return textSolution+text;
    }
    
    /**
     * @brief Evalúa la función objetivo de la solución (puntuación total).
     * @details Suma las puntuaciones de todos los nodos visitados en todas las rutas.
     * @return La puntuación total de la solución.
     */
    public double evaluateFitness() {
        double objectiveFunction = 0.0;
        double objectiveFunctionPerRoute = 0.0;
        for(int k = 0; k < this.getCreatedRoutes(); k++) {
            int depot = this.getIndexRoute(k);
            int pre=depot, suc = -1;
            do {
                suc = this.getSuccessor(pre);
                objectiveFunctionPerRoute = objectiveFunctionPerRoute + this.problem.getScore(suc);
                pre = suc;
            } while((suc != depot));
            objectiveFunction = objectiveFunction + objectiveFunctionPerRoute;
            objectiveFunctionPerRoute = 0.0;
        }
        return objectiveFunction;
    }
    
    /**
     * @brief Añade una nueva ruta a la solución.
     * @details Asigna un nuevo depósito a una ruta vacía, decrementa el número de vehículos disponibles
     * y establece la estructura básica de la nueva ruta (depósito apunta a sí mismo).
     * @return El índice del nuevo nodo depósito creado.
     */
    public int addRoute() {
        int depot = this.problem.getPOIs();
        depot++;
        int routePos = 1;
        for(int i = 0; i < this.routes.length; i++) {
            if(this.routes[i] != -1 && this.routes[i] != 0) {
                depot = this.routes[i];
                depot++;
                routePos = i+1;
            }
        }
        this.routes[routePos] = depot;
        this.availableVehicles--;
        this.predecessors[depot] = depot;
        this.successors[depot] = depot;
        this.problem.addNodeDepot();
        return depot;
    }
    
    /**
     * @brief Imprime la solución en la consola y devuelve la puntuación.
     * @details Muestra la secuencia de nodos para cada ruta y la puntuación total.
     * @return La puntuación (fitness) de la solución.
     */
    public double printSolution() {
        for(int k = 0; k < this.getCreatedRoutes(); k++) {
                int depot = this.getIndexRoute(k);
                int pre=depot, suc = -1;
                do {
                    suc = this.getSuccessor(pre);
                    System.out.print(pre+" - ");
                    pre = suc;
                } while((suc != depot));
                System.out.println(suc+"  ");
        }
        double fitness = this.evaluateFitness();
        System.out.println("SC="+fitness);
        return fitness;
    }

}
