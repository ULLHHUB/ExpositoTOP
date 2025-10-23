/**
 * @file TOPTWEvaluator.java
 * @brief Contiene la clase para evaluar la función objetivo de una solución TOPTW.
 */
package top;

/**
 * @class TOPTWEvaluator
 * @brief Evalúa la calidad de una solución para el problema TOPTW.
 * @details Esta clase se encarga de calcular el valor de la función objetivo
 * para una solución dada. Actualmente, el método de evaluación está comentado
 * y no tiene una implementación activa.
 */
public class TOPTWEvaluator {
    /**
     * @brief Constante para indicar que una solución no ha sido evaluada.
     */
    public static double NO_EVALUATED = -1.0;

    /**
     * @brief Evalúa una solución TOPTW.
     * @details Este método debería calcular la puntuación total de las rutas de la solución,
     * pero su implementación actual está comentada.
     * @param solution La solución a evaluar.
     */
    public void evaluate(TOPTWSolution solution) {
        /*CumulativeCVRP problem = solution.getProblem();
        double objectiveFunctionValue = 0.0;
        for (int i = 0; i < solution.getIndexDepot().size(); i++) {
            double cumulative = 0;
            int depot = solution.getAnIndexDepot(i);
            int actual = depot;
            actual = solution.getSuccessor(actual);
            cumulative += problem.getDistanceMatrix(0, actual);
            objectiveFunctionValue += problem.getDistanceMatrix(0, actual);
            System.out.println("Desde " + 0 + " a " + actual + " = " + cumulative);
            while (actual != depot) {
                int ant = actual;
                actual = solution.getSuccessor(actual);
                if (actual != depot) {
                    cumulative += problem.getDistanceMatrix(ant, actual);
                    objectiveFunctionValue += cumulative;
                    System.out.println("Desde " + ant + " a " + actual + " = " + cumulative);
                } else {
                    cumulative += problem.getDistanceMatrix(ant, 0);
                    objectiveFunctionValue += cumulative;
                    System.out.println("Desde " + ant + " a " + 0 + " = " + cumulative);
                }
            }
            System.out.println("");
        }
        solution.setObjectiveFunctionValue(objectiveFunctionValue);*/
    }
}
