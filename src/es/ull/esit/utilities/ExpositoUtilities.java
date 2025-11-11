/**
 * @file ExpositoUtilities.java
 * @brief Contiene una colección de métodos de utilidad estáticos para diversas tareas.
 */
package es.ull.esit.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @class ExpositoUtilities
 * @brief Proporciona un conjunto de herramientas estáticas de propósito general.
 * @details Incluye métodos para formateo de cadenas, operaciones con archivos,
 * validación de tipos de datos y manipulación de matrices.
 */
public class ExpositoUtilities {

    /**
     * @brief Ancho de columna por defecto para el formateo de texto.
     */
    public static final int DEFAULT_COLUMN_WIDTH = 10;
    /**
     * @brief Constante para alineación a la izquierda.
     */
    public static final int ALIGNMENT_LEFT = 1;
    /**
     * @brief Constante para alineación a la derecha.
     */
    public static final int ALIGNMENT_RIGHT = 2;

    /**
     * @brief Encuentra la primera aparición de un elemento en un vector.
     * @param vector El array de enteros donde buscar.
     * @param element El elemento a buscar.
     * @return El índice de la primera aparición, o -1 si no se encuentra.
     */
    private static int getFirstAppearance(int[] vector, int element) {
        for (int i = 0; i < vector.length; i++) {
            if (vector[i] == element) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @brief Imprime el contenido de un archivo en la consola.
     * @param file La ruta al archivo a imprimir.
     */
    public static void printFile(String file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(ExpositoUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @brief Simplifica una cadena de texto.
     * @details Reemplaza tabulaciones y múltiples espacios por un único espacio, y elimina espacios al inicio y al final.
     * @param string La cadena a simplificar.
     * @return La cadena simplificada.
     */
    public static String simplifyString(String string) {
        string = string.replaceAll("\t", " ");
        for (int i = 0; i < 50; i++) {
            string = string.replaceAll("  ", " ");
        }
        string = string.trim();
        return string;
    }

    /**
     * @brief Multiplica dos matrices de dobles.
     * @param a La primera matriz (izquierda).
     * @param b La segunda matriz (derecha).
     * @return La matriz resultante de la multiplicación, o `null` si las dimensiones son incompatibles.
     */
    public static double[][] multiplyMatrices(double a[][], double b[][]) {
        if (a.length == 0) {
            return new double[0][0];
        }
        if (a[0].length != b.length) {
            return null;
        }
        int n = a[0].length;
        int m = a.length;
        int p = b[0].length;
        double ans[][] = new double[m][p];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < p; j++) {
                for (int k = 0; k < n; k++) {
                    ans[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return ans;
    }

    /**
     * @brief Escribe un texto en un archivo.
     * @param file La ruta al archivo.
     * @param text El contenido a escribir.
     * @throws IOException Si ocurre un error de E/S.
     */
    public static void writeTextToFile(String file, String text) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(text);
        }
    }

    /**
     * @brief Formatea una cadena que representa un número.
     * @details Si la cadena es un número doble, lo formatea con tres decimales.
     * @param string La cadena a formatear.
     * @return La cadena formateada.
     */
    public static String getFormat(String string) {
        if (!ExpositoUtilities.isInteger(string)) {
            if (ExpositoUtilities.isDouble(string)) {
                double value = Double.parseDouble(string);
                string = ExpositoUtilities.getFormat(value);
            }
        }
        return string;
    }

    /**
     * @brief Formatea un valor doble a una cadena con tres decimales.
     * @param value El valor a formatear.
     * @return La cadena formateada.
     */
    public static String getFormat(double value) {
        DecimalFormat decimalFormatter = new DecimalFormat("0.000");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        decimalFormatter.setDecimalFormatSymbols(symbols);
        return decimalFormatter.format(value);
    }

    /**
     * @brief Formatea un valor doble a una cadena con un número específico de decimales.
     * @param value El valor a formatear.
     * @param zeros El número de dígitos decimales.
     * @return La cadena formateada.
     */
    public static String getFormat(double value, int zeros) {
        String format = "0.";
        for (int i = 0; i < zeros; i++) {
            format += "0";
        }
        DecimalFormat decimalFormatter = new DecimalFormat(format);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        decimalFormatter.setDecimalFormatSymbols(symbols);
        return decimalFormatter.format(value);
    }

    /**
     * @brief Formatea una cadena para que ocupe un ancho fijo, con alineación a la derecha.
     * @param string La cadena a formatear.
     * @param width El ancho total de la cadena resultante.
     * @return La cadena formateada.
     */
    public static String getFormat(String string, int width) {
        return ExpositoUtilities.getFormat(string, width, ExpositoUtilities.ALIGNMENT_RIGHT);
    }

    /**
     * @brief Formatea una cadena para que ocupe un ancho fijo con una alineación específica.
     * @param string La cadena a formatear.
     * @param width El ancho total.
     * @param alignment La alineación (ALIGNMENT_LEFT o ALIGNMENT_RIGHT).
     * @return La cadena formateada.
     */
    public static String getFormat(String string, int width, int alignment) {
        String format = "";
        if (alignment == ExpositoUtilities.ALIGNMENT_LEFT) {
            format = "%-" + width + "s";
        } else {
            format = "%" + 1 + "$" + width + "s";
        }
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        String[] data = new String[]{string};
        return String.format(format, (Object[]) data);
    }

    /**
     * @brief Formatea una lista de cadenas, cada una con un ancho fijo.
     * @param strings La lista de cadenas.
     * @param width El ancho para cada cadena.
     * @return Una única cadena con todos los elementos formateados y concatenados.
     */
    public static String getFormat(ArrayList<String> strings, int width) {
        String format = "";
        for (int i = 0; i < strings.size(); i++) {
            format += "%" + (i + 1) + "$" + width + "s";
        }
        String[] data = new String[strings.size()];
        for (int t = 0; t < strings.size(); t++) {
            data[t] = "" + ExpositoUtilities.getFormat(strings.get(t));
        }
        return String.format(format, (Object[]) data);
    }

    /**
     * @brief Formatea una lista de enteros con el ancho de columna por defecto.
     * @param strings La lista de enteros.
     * @return Una única cadena con todos los elementos formateados.
     */
    public static String getFormat(ArrayList<Integer> strings) {
        String format = "";
        for (int i = 0; i < strings.size(); i++) {
            format += "%" + (i + 1) + "$" + DEFAULT_COLUMN_WIDTH + "s";
        }
        Integer[] data = new Integer[strings.size()];
        for (int t = 0; t < strings.size(); t++) {
            data[t] = strings.get(t);
        }
        return String.format(format, (Object[]) data);
    }

    /**
     * @brief Formatea un array de cadenas, cada una con un ancho fijo.
     * @param strings El array de cadenas.
     * @param width El ancho para cada cadena.
     * @return Una única cadena con todos los elementos formateados.
     */
    public static String getFormat(String[] strings, int width) {
        int[] alignment = new int[strings.length];
        Arrays.fill(alignment, ExpositoUtilities.ALIGNMENT_RIGHT);
        int[] widths = new int[strings.length];
        Arrays.fill(widths, width);
        return ExpositoUtilities.getFormat(strings, widths, alignment);
    }
    
    /**
     * @brief Formatea una matriz de cadenas en una tabla.
     * @param matrixStrings La matriz de cadenas.
     * @param width El ancho para cada columna.
     * @return Una cadena de texto que representa la tabla formateada.
     */
        public static String getFormat(String[][] matrixStrings, int width) {
        String result = "";
        for (int i = 0; i < matrixStrings.length; i++) {
            String[] strings = matrixStrings[i];
            int[] alignment = new int[strings.length];
            Arrays.fill(alignment, ExpositoUtilities.ALIGNMENT_RIGHT);
            int[] widths = new int[strings.length];
            Arrays.fill(widths, width);
            result += ExpositoUtilities.getFormat(strings, widths, alignment);
            if (i < (matrixStrings.length - 1)) {
                result += "\n";
            }
        }
        return result;
    }

    /**
     * @brief Formatea un array de cadenas con el ancho de columna por defecto.
     * @param strings El array de cadenas.
     * @return Una única cadena con todos los elementos formateados.
     */
    public static String getFormat(String[] strings) {
        int[] alignment = new int[strings.length];
        Arrays.fill(alignment, ExpositoUtilities.ALIGNMENT_RIGHT);
        int[] widths = new int[strings.length];
        Arrays.fill(widths, ExpositoUtilities.DEFAULT_COLUMN_WIDTH);
        return ExpositoUtilities.getFormat(strings, widths, alignment);
    }

    /**
     * @brief Formatea un array de cadenas con anchos de columna individuales.
     * @param strings El array de cadenas.
     * @param width Un array con el ancho para cada columna correspondiente.
     * @return Una única cadena con todos los elementos formateados.
     */
    public static String getFormat(String[] strings, int[] width) {
        int[] alignment = new int[strings.length];
        Arrays.fill(alignment, ExpositoUtilities.ALIGNMENT_RIGHT);
        return ExpositoUtilities.getFormat(strings, width, alignment);
    }

    /**
     * @brief Formatea un array de cadenas con anchos y alineaciones individuales.
     * @param strings El array de cadenas.
     * @param width Un array con el ancho para cada columna.
     * @param alignment Un array con la alineación para cada columna.
     * @return Una única cadena con todos los elementos formateados.
     */
    public static String getFormat(String[] strings, int[] width, int[] alignment) {
        String format = "";
        for (int i = 0; i < strings.length; i++) {
            if (alignment[i] == ExpositoUtilities.ALIGNMENT_LEFT) {
                format += "%" + (i + 1) + "$-" + width[i] + "s";
            } else {
                format += "%" + (i + 1) + "$" + width[i] + "s";
            }
        }
        String[] data = new String[strings.length];
        for (int t = 0; t < strings.length; t++) {
            data[t] = "" + ExpositoUtilities.getFormat(strings[t]);
        }
        return String.format(format, (Object[]) data);
    }

    /**
     * @brief Comprueba si una cadena puede ser parseada como un entero.
     * @param str La cadena a comprobar.
     * @return `true` si es un entero, `false` en caso contrario.
     */
    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * @brief Comprueba si una cadena puede ser parseada como un doble.
     * @param str La cadena a comprobar.
     * @return `true` si es un doble, `false` en caso contrario.
     */
    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * @brief Comprueba si un grafo representado por una matriz de adyacencia es acíclico.
     * @param distanceMatrix La matriz de adyacencia.
     * @return `true` si es acíclico, `false` si se detecta un ciclo.
     */
    public static boolean isAcyclic(int[][] distanceMatrix) {
        int numRealTasks = distanceMatrix.length - 2;
        int node = 1;
        boolean acyclic = true;
        while (acyclic && node <= numRealTasks) {
            if (ExpositoUtilities.thereIsPath(distanceMatrix, node)) {
                return false;
            }
            node++;
        }
        return true;
    }

    /**
     * @brief Comprueba si existe un camino desde un nodo de vuelta a sí mismo (un ciclo).
     * @param distanceMatrix La matriz de adyacencia.
     * @param node El nodo de inicio y fin del ciclo a comprobar.
     * @return `true` si se encuentra un ciclo, `false` en caso contrario.
     */
    public static boolean thereIsPath(int[][] distanceMatrix, int node) {
        HashSet<Integer> visits = new HashSet<>();
        HashSet<Integer> noVisits = new HashSet<>();
        for (int i = 0; i < distanceMatrix.length; i++) {
            if (i != node) {
                noVisits.add(i);
            }
        }
        visits.add(node);
        while (!visits.isEmpty()) {
            Iterator<Integer> it = visits.iterator();
            int toCheck = it.next();
            visits.remove(toCheck);
            for (int i = 0; i < distanceMatrix.length; i++) {
                if (toCheck != i && distanceMatrix[toCheck][i] != Integer.MAX_VALUE) {
                    if (i == node) {
                        return true;
                    }
                    if (noVisits.contains(i)) {
                        noVisits.remove(i);
                        visits.add(i);
                    }
                }
            }
        }
        return false;
    }
}
