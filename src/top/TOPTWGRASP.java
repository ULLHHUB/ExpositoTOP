/**
 * @file TOPTWGRASP.java
 * @brief Contiene la implementación de la metaheurística GRASP para el problema TOPTW.
 */
package top;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.security.SecureRandom;
import java.util.Random;

/**
 * @class TOPTWGRASP
 * @brief Implementa la metaheurística GRASP (Greedy Randomized Adaptive Search Procedure) para resolver el TOPTW.
 * @details Esta clase contiene los métodos para ejecutar el algoritmo GRASP, incluyendo la construcción
 * de soluciones aleatorizadas y la gestión de la Lista Restringida de Candidatos (RCL).
 */
public class TOPTWGRASP {
    /**
     * @brief Constante para indicar que una solución no ha sido evaluada.
     */
    public static double NO_EVALUATED = -1.0;
    private static final Random RANDOM = new SecureRandom();
    
    private TOPTWSolution solution;
    private int solutionTime;

    /**
     * @brief Constructor de la clase TOPTWGRASP.
     * @param sol La solución inicial sobre la que operará el algoritmo.
     */
    public TOPTWGRASP(TOPTWSolution sol){
        this.solution = sol;
        this.solutionTime = 0;
    }
    
    /*procedure GRASP(Max Iterations,Seed)
        1 Read Input();
        2 for k = 1, . . . , Max Iterations do
            3 Solution ← Greedy Randomized Construction(Seed);
            4 Solution ← Local Search(Solution);
            5 Update Solution(Solution,Best Solution);
        6 end;
        7 return Best Solution;
    end GRASP*/
    
    /*procedure Greedy Randomized Construction(Seed)
        Solution ← ∅;
        Evaluate the incremental costs of the candidate elements;
        while Solution is not a complete solution do
            Build the restricted candidate list (RCL);
            Select an element s from the RCL at random;
            Solution ← Solution ∪ {s};
            Reevaluate the incremental costs;
        end;
        return Solution;
    end Greedy Randomized Construction.*/
    
    /**
     * @brief Ejecuta el algoritmo GRASP durante un número determinado de iteraciones.
     * @details En cada iteración, construye una solución greedy aleatorizada, (opcionalmente aplica una búsqueda local),
     * y actualiza la mejor solución encontrada.
     * @param maxIterations El número máximo de iteraciones a ejecutar.
     * @param maxSizeRCL El tamaño máximo de la Lista Restringida de Candidatos (RCL).
     */
    public void GRASP(int maxIterations, int maxSizeRCL) {
        double averageFitness = 0.0;
        double bestSolution = 0.0;
        for(int i = 0; i < maxIterations; i++) {
            
            this.computeGreedySolution(maxSizeRCL);
            
            // IMPRIMIR SOLUCION
            double fitness = this.solution.evaluateFitness();
            System.out.println(this.solution.getInfoSolution());
            //System.out.println("Press Any Key To Continue...");
            //new java.util.Scanner(System.in).nextLine();
            averageFitness += fitness;
            if(bestSolution < fitness) {
                bestSolution = fitness;
            }
            //double fitness = this.solution.printSolution();
                   
            /******
            * 
            * BÚSQUEDA LOCAL
            * 
            */
        }
        averageFitness = averageFitness/maxIterations;
        System.out.println(" --> MEDIA: "+averageFitness);
        System.out.println(" --> MEJOR SOLUCION: "+bestSolution);
    }
    
    /**
     * @brief Selecciona un elemento aleatorio de la Lista Restringida de Candidatos (RCL).
     * @param maxTRCL El tamaño de la RCL sobre la que se hará la selección.
     * @return La posición del elemento seleccionado en la RCL.
     */
    public int aleatorySelectionRCL(int maxTRCL) {
       return RANDOM.nextInt(maxTRCL);
    }
    
    /**
     * @brief Selecciona un candidato de la RCL utilizando una función de pertenencia difusa.
     * @details Elige el candidato con el mejor valor de membresía (el más "prometedor" según la puntuación).
     * @param rcl La Lista Restringida de Candidatos.
     * @return La posición del candidato seleccionado en la RCL.
     */
    public int fuzzySelectionBestFDRCL(ArrayList< double[] > rcl) {
        int bestPosition = 0;
        double bestValue = -1.0;
        for(int i = 0; i < rcl.size(); i++) {
            if(bestValue < rcl.get(i)[2]) {
                bestValue = rcl.get(i)[2];
                bestPosition = i;
            }
        }
        return bestPosition;
    }
    
    /**
     * @brief Selecciona un candidato de la RCL utilizando un corte alfa sobre la función de pertenencia difusa.
     * @details Filtra los candidatos cuyo valor de membresía es menor o igual a `alpha` y luego elige uno aleatoriamente.
     * Si ningún candidato cumple la condición, se elige uno aleatoriamente de toda la RCL.
     * @param rcl La Lista Restringida de Candidatos.
     * @param alpha El umbral para el corte alfa.
     * @return La posición del candidato seleccionado en la RCL.
     */
    public int fuzzySelectionAlphaCutRCL(ArrayList< double[] > rcl, double alpha) {
        ArrayList<Integer> candidates = new ArrayList<>();
        for(int i = 0; i < rcl.size(); i++) {
            if(rcl.get(i)[2] <= alpha) {
                candidates.add(i);
            }
        }
        if(candidates.isEmpty()) {
            return aleatorySelectionRCL(rcl.size());
        }
        else {
            int aleatory = RANDOM.nextInt(candidates.size());
            return candidates.get(aleatory);
        }
    }

    /**
     * @brief Construye una solución greedy aleatorizada.
     * @details Itera mientras haya clientes sin asignar, evaluando los candidatos, construyendo la RCL,
     * seleccionando un candidato y actualizando la solución.
     * @param maxSizeRCL El tamaño máximo de la RCL.
     */
    public void computeGreedySolution(int maxSizeRCL) {
        // inicialización
        this.solution.initSolution();
        
        // tiempo de salida y score por ruta y cliente
        ArrayList<ArrayList<Double>> departureTimesPerClient = new ArrayList<ArrayList<Double>>();
        ArrayList<Double> init = new ArrayList<Double>();
        for(int z = 0; z < this.solution.getProblem().getPOIs()+this.solution.getProblem().getVehicles(); z++) {init.add(0.0);}
        departureTimesPerClient.add(0, init);
        
        // clientes
        ArrayList<Integer> customers = new ArrayList<Integer>();
        for(int j = 1; j <= this.solution.getProblem().getPOIs(); j++) { customers.add(j); }
        
        // Evaluar coste incremental de los elementos candidatos
        ArrayList< double[] > candidates = this.comprehensiveEvaluation(customers, departureTimesPerClient);
        
        Collections.sort(candidates, new Comparator<double[]>() {
            public int compare(double[] a, double[] b) {   
                return Double.compare(a[a.length-2], b[b.length-2]);
            }
        });

        int maxTRCL = maxSizeRCL;
        boolean existCandidates = true;
        
        while(!customers.isEmpty() && existCandidates) {
            if(!candidates.isEmpty()) {
                //Construir lista restringida de candidatos
                ArrayList< double[] > rcl = new ArrayList< double[] >();
                maxTRCL = maxSizeRCL;
                if(maxTRCL > candidates.size()) { maxTRCL = candidates.size(); }
                for(int j=0; j < maxTRCL; j++) { rcl.add(candidates.get(j)); }

                //Selección aleatoria o fuzzy de candidato de la lista restringida
                int posSelected = -1;
                int selection = 3;
                double alpha = 0.8;
                switch (selection) {
                    case 1:  posSelected = this.aleatorySelectionRCL(maxTRCL);  // Selección aleatoria
                             break;
                    case 2:  posSelected = this.fuzzySelectionBestFDRCL(rcl);   // Selección fuzzy con mejor valor de alpha
                             break;
                    case 3:  posSelected = this.fuzzySelectionAlphaCutRCL(rcl, alpha); // Selección fuzzy con alpha corte aleatoria
                             break;
                    default: posSelected = this.aleatorySelectionRCL(maxTRCL);  // Selección aleatoria por defecto
                             break;
                }
                
                double[] candidateSelected = rcl.get(posSelected);
                for(int j=0; j < customers.size(); j++) {
                    if(customers.get(j)==candidateSelected[0]) {
                        customers.remove(j);
                    }
                }              
                
                updateSolution(candidateSelected, departureTimesPerClient);

            } else { // No hay candidatos a insertar en la solución, crear otra ruta
                if(this.solution.getCreatedRoutes() < this.solution.getProblem().getVehicles()) {
                    int newDepot = this.solution.addRoute();
                    ArrayList<Double> initNew = new ArrayList<Double>();
                    for(int z = 0; z < this.solution.getProblem().getPOIs()+this.solution.getProblem().getVehicles(); z++) {initNew.add(0.0);}
                    departureTimesPerClient.add(initNew);
                } 
                else {
                    existCandidates = false;
                }
            }
            //Reevaluar coste incremental de los elementos candidatos
            candidates.clear();
            candidates = this.comprehensiveEvaluation(customers, departureTimesPerClient);
            Collections.sort(candidates, new Comparator<double[]>() {
                public int compare(double[] a, double[] b) {
                    return Double.compare(a[a.length-2], b[b.length-2]);
                }
            });
        }
        
    }
    
    /**
     * @brief Actualiza la solución insertando un candidato seleccionado.
     * @details Modifica los punteros de predecesor y sucesor para insertar el nuevo nodo en la ruta
     * y recalcula los tiempos de salida de los nodos afectados en esa ruta.
     * @param candidateSelected El candidato seleccionado, un array con la información [cliente, ruta, predecesor, coste, score].
     * @param departureTimes La estructura de datos que almacena los tiempos de salida de cada cliente.
     */
    public void updateSolution(double[] candidateSelected, ArrayList< ArrayList< Double > > departureTimes) {
        // Inserción del cliente en la ruta  return: cliente, ruta, predecesor, coste
        this.solution.setPredecessor((int)candidateSelected[0], (int)candidateSelected[2]);
        this.solution.setSuccessor((int)candidateSelected[0], this.solution.getSuccessor((int)candidateSelected[2]));
        this.solution.setSuccessor((int)candidateSelected[2], (int)candidateSelected[0]);
        this.solution.setPredecessor(this.solution.getSuccessor((int)candidateSelected[0]), (int)candidateSelected[0]);
        
        // Actualización de las estructuras de datos y conteo a partir de la posición a insertar
        double costInsertionPre = departureTimes.get((int)candidateSelected[1]).get((int)candidateSelected[2]);
        ArrayList<Double> route = departureTimes.get((int)candidateSelected[1]);
        int pre=(int)candidateSelected[2], suc=-1;
        int depot = this.solution.getIndexRoute((int)candidateSelected[1]);
        do {
            suc = this.solution.getSuccessor(pre);
            costInsertionPre += this.solution.getDistance(pre, suc);
            
            if(costInsertionPre < this.solution.getProblem().getReadyTime(suc)) {
                costInsertionPre = this.solution.getProblem().getReadyTime(suc);
            }
            costInsertionPre += this.solution.getProblem().getServiceTime(suc);
             
            if(!this.solution.isDepot(suc))
                route.set(suc, costInsertionPre);
            pre = suc;
        } while((suc != depot));
        
        // Actualiza tiempos
        departureTimes.set((int)candidateSelected[1], route);
    }

    /**
     * @brief Evalúa de forma exhaustiva todos los posibles movimientos de inserción para los clientes disponibles.
     * @details Para cada cliente no asignado, prueba a insertarlo en cada posición posible de cada ruta existente,
     * verificando la factibilidad de la inserción (ventanas de tiempo).
     * @param customers La lista de clientes aún no asignados a ninguna ruta.
     * @param departureTimes Los tiempos de salida actuales para cada nodo en la solución.
     * @return Una lista de candidatos factibles, donde cada candidato es un array con la información [cliente, ruta, predecesor, coste, score].
     */
    //return: cliente, ruta, predecesor, coste tiempo, score
    public ArrayList< double[] > comprehensiveEvaluation(ArrayList<Integer> customers, ArrayList< ArrayList< Double > > departureTimes) {
        ArrayList< double[] > candidatesList = new ArrayList< double[] >();
        double[] infoCandidate = new double[5];
        boolean validFinalInsertion = true;
        infoCandidate[0] = -1;
        infoCandidate[1] = -1;
        infoCandidate[2] = -1;
        infoCandidate[3] = Double.MAX_VALUE;
        infoCandidate[4] = -1;
        
        for(int c = 0; c < customers.size(); c++) { // clientes disponibles
            for(int k = 0; k < this.solution.getCreatedRoutes(); k++) { // rutas creadas
                validFinalInsertion = true;
                int depot = this.solution.getIndexRoute(k);
                int pre=-1, suc=-1;
                double costInsertion = 0;
                pre = depot;
                int candidate = customers.get(c);
                do {                                                // recorremos la ruta
                    validFinalInsertion = true;
                    suc = this.solution.getSuccessor(pre);
                    double timesUntilPre = departureTimes.get(k).get(pre) + this.solution.getDistance(pre, candidate);
                    if(timesUntilPre < (this.solution.getProblem().getDueTime(candidate))) {
                        double costCand = 0;
                        if(timesUntilPre < this.solution.getProblem().getReadyTime(candidate)) {
                            costCand = this.solution.getProblem().getReadyTime(candidate);
                        } else { costCand = timesUntilPre; }
                        costCand +=  this.solution.getProblem().getServiceTime(candidate);
                        if(costCand > this.solution.getProblem().getMaxTimePerRoute()) { validFinalInsertion = false; }
                        
                        // Comprobar TW desde candidate hasta sucesor
                        double timesUntilSuc = costCand + this.solution.getDistance(candidate, suc);
                        if(timesUntilSuc < (this.solution.getProblem().getDueTime(suc))) {                                
                            double costSuc = 0;
                            if(timesUntilSuc < this.solution.getProblem().getReadyTime(suc)) {
                                costSuc = this.solution.getProblem().getReadyTime(suc);
                            } else { costSuc = timesUntilSuc; }
                            costSuc +=  this.solution.getProblem().getServiceTime(suc);
                            costInsertion = costSuc;                            
                            if(costSuc > this.solution.getProblem().getMaxTimePerRoute()) { validFinalInsertion = false;}

                            int pre2=suc, suc2 = -1;
                            if(suc != depot)
                                do {
                                    suc2 = this.solution.getSuccessor(pre2);
                                    double timesUntilSuc2 = costInsertion + this.solution.getDistance(pre2, suc2);
                                    if(timesUntilSuc2 < (this.solution.getProblem().getDueTime(suc2))) {
                                        if(timesUntilSuc2 < this.solution.getProblem().getReadyTime(suc2)) {
                                            costInsertion = this.solution.getProblem().getReadyTime(suc2);
                                        } else { costInsertion = timesUntilSuc2; }
                                        costInsertion += this.solution.getProblem().getServiceTime(suc2);
                                        if(costInsertion > this.solution.getProblem().getMaxTimePerRoute()) { validFinalInsertion = false; }
                                    } else { validFinalInsertion = false; }         
                                    pre2 = suc2;
                                } while((suc2 != depot) && validFinalInsertion);
                        } else { validFinalInsertion = false; }
                    } else { validFinalInsertion = false; }

                    if(validFinalInsertion==true) { // cliente, ruta, predecesor, coste
                        if(costInsertion < infoCandidate[3]) {
                            infoCandidate[0] = candidate; infoCandidate[1] = k; infoCandidate[2] = pre; infoCandidate[3] = costInsertion; infoCandidate[4] = this.solution.getProblem().getScore(candidate); // cliente, ruta, predecesor, coste, score
                        }
                    }
                    
                    pre = suc;
                } while(suc != depot);
            } //rutas creadas
            
            // almacenamos en la lista de candidatos la mejor posición de inserción para el cliente
            if(infoCandidate[0]!=-1 && infoCandidate[1]!=-1 && infoCandidate[2]!=-1 && infoCandidate[3] != Double.MAX_VALUE && infoCandidate[4]!=-1) {
                double[] infoCandidate2 = new double[5];
                infoCandidate2[0] = infoCandidate[0];  infoCandidate2[1] = infoCandidate[1];  
                infoCandidate2[2] = infoCandidate[2];  infoCandidate2[3] = infoCandidate[3];
                infoCandidate2[4] = infoCandidate[4];
                candidatesList.add(infoCandidate2);
            }
            validFinalInsertion = true;
            infoCandidate[0] = -1;  infoCandidate[1] = -1;
            infoCandidate[2] = -1;  infoCandidate[3] = Double.MAX_VALUE;
            infoCandidate[4] = -1;
        } // cliente

        return candidatesList;        
    }
    
    /**
     * @brief Obtiene la solución actual.
     * @return La instancia de TOPTWSolution.
     */
    public TOPTWSolution getSolution() {
        return solution;
    }

    /**
     * @brief Establece la solución a utilizar.
     * @param solution La nueva instancia de TOPTWSolution.
     */
    public void setSolution(TOPTWSolution solution) {
        this.solution = solution;
    }

    /**
     * @brief Obtiene el tiempo de ejecución de la solución (actualmente no implementado).
     * @return El tiempo de solución.
     */
    public int getSolutionTime() {
        return solutionTime;
    }

    /**
     * @brief Establece el tiempo de ejecución de la solución.
     * @param solutionTime El nuevo tiempo.
     */
    public void setSolutionTime(int solutionTime) {
        this.solutionTime = solutionTime;
    }
    
    /**
     * @brief Obtiene la máxima puntuación posible entre todos los nodos del problema.
     * @return El valor de la máxima puntuación.
     */
    public double getMaxScore() {
        double maxSc = -1.0;
        for(int i = 0; i < this.solution.getProblem().getScore().length; i++) {
            if(this.solution.getProblem().getScore(i) > maxSc)
                maxSc = this.solution.getProblem().getScore(i);
        }
        return maxSc;
    }

}
