/**
 * @author Manuel Araújo Baño
 * @expediente 22298227
 * @author Daniel Gutierrez Torres
 */

class Practica4 {

    /**
     * Calcula el grado del grafo
     * @param grafo El grafo cuyo grado se quiere calcular
     * @return El grado máximo del grafo (el mayor entre todos los vértices)
     */
    static public <Clave, InfoV, Coste> int gradoGrafo(Grafo<Clave,InfoV,Coste> grafo) {
        int counter = 1;
        boolean seguir = true;
        int maxGrado = 0;
        
        Lista<Clave> listaVertices = grafo.listaVertices();
        
        do {
            if (counter <= listaVertices.longitud()) {
                // Obtener la clave del vértice actual
                Clave claveVertice = listaVertices.consultar(counter);
                
                // Calcular el grado del vértice (suma de grado de entrada y salida)
                int gradoVertice = grafo.gradoEntrada(claveVertice) + grafo.gradoSalida(claveVertice);
                
                // Actualizar el grado máximo si es necesario
                if (gradoVertice > maxGrado) {
                    maxGrado = gradoVertice;
                }
                
                // Incrementar el contador
                counter++;
            } else {
                seguir = false;
            }
        } while (counter <= listaVertices.longitud() && seguir);
        
        return maxGrado;
    }

    /**
     * Realiza un recorrido en anchura del grafo a partir del vértice v
     * e imprime todos los vértices alcanzables desde v
     * @param grafo El grafo a recorrer
     * @param v El vértice de inicio del recorrido
     */
    public static <Clave, InfoV, Coste> void recorridoAnchura(Grafo<Clave,InfoV,Coste> grafo, Clave v) {
        // Verificar si el vértice existe en el grafo
        if (!grafo.existeVertice(v)) {
            System.out.println("El vértice " + v + " no existe en el grafo");
            return;
        }
        
        // Lista para almacenar los vértices visitados
        Lista<Clave> visitados = new Lista<Clave>();
        
        // Lista utilizada como cola para el recorrido en anchura
        // Insertaremos al final (longitud+1) y eliminaremos del principio (posición 1)
        Lista<Clave> cola = new Lista<Clave>();
        
        // Añadir el vértice inicial a la cola y marcarlo como visitado
        cola.insertar(cola.longitud()+1, v);
        visitados.insertar(visitados.longitud()+1, v);
        
        System.out.println("Recorrido en anchura desde " + v + ":");
        System.out.println(v); // Imprimir el vértice inicial
        
        // Mientras la cola no esté vacía
        while (!cola.esVacia()) {
            // Extraer el primer elemento de la cola (simular dequeue)
            Clave actual = cola.consultar(1);
            cola.borrar(1);
            
            // Obtener los sucesores del vértice actual
            Lista<Clave> sucesores = grafo.listaSucesores(actual);
            
            // Para cada sucesor
            for (int i = 1; i <= sucesores.longitud(); i++) {
                Clave sucesor = sucesores.consultar(i);
                
                // Si no ha sido visitado
                if (visitados.buscar(sucesor) == 0) {
                    // Añadirlo a la cola y marcarlo como visitado
                    cola.insertar(cola.longitud()+1, sucesor);
                    visitados.insertar(visitados.longitud()+1, sucesor);
                    
                    // Imprimir el vértice
                    System.out.println(sucesor);
                }
            }
        }
    }
  
    /**
     * Calcula el número de componentes conexas de un grafo utilizando un recorrido en profundidad (DFS).
     * @param grafo El grafo cuyas componentes conexas se quieren contar.
     * @return El número de componentes conexas del grafo.
     */
    public static <Clave, InfoV, Coste> int componentesConexas(Grafo<Clave, InfoV, Coste> grafo) {
        if (grafo.esVacio()) {
            return 0;
        }

        // Lista con todos los vértices no visitados
        Lista<Clave> noVisitados = grafo.listaVertices();
        int numComponentes = 0;

        while (!noVisitados.esVacia()) {
            // Se toma un vértice no visitado y se explora toda su componente
            Clave vertice = noVisitados.consultar(1);
            System.out.println("TEST");
            Grafo.profREC(grafo, vertice, noVisitados);
            numComponentes++; // Una nueva componente fue explorada
        }

        return numComponentes;
    }

    

    
    /**
     * Calcula las paradas mínimas necesarias para que un camión llegue desde una ciudad A hasta otra B
     * siguiendo una ruta predefinida, considerando la autonomía máxima del vehículo.
     * @param ciudades Array con los nombres de las ciudades de la ruta (sin incluir la ciudad de origen A)
     * @param distancias Array con las distancias en kilómetros entre ciudades consecutivas
     * @param autonomia Kilómetros máximos que el camión puede recorrer sin repostar
     */
    public static void calcularViaje(String[] ciudades, int[] distancias, int autonomia) {
        Lista<String> resultado = new Lista<String>();
        
        int autonomiaRestante = autonomia;
        int posicion = 0;
        int N = ciudades.length;
    
        while (posicion < N) {
            int alcance = posicion;
            int suma = 0;
    
            // Avanzamos lo máximo posible con la autonomía restante
            while (alcance < N && suma + distancias[alcance] <= autonomiaRestante) {
                suma += distancias[alcance];
                alcance++;
            }
    
            if (alcance == posicion) {
                // No podemos avanzar nada desde aquí con la autonomía actual
                // Repostamos aquí mismo y consumimos el primer tramo
                autonomiaRestante = autonomia - distancias[posicion];
                // Solo insertamos si no estamos en la última ciudad
                if (posicion < N) {
                    resultado.insertar(resultado.longitud() + 1, ciudades[posicion]);
                }
                posicion++;
            } else {
                // Avanzamos hasta el punto más lejano alcanzable sin repostar
                autonomiaRestante -= suma;
                posicion = alcance;
    
                // Si no hemos llegado al final, repostamos en la última ciudad alcanzada
                if (posicion < N) {
                    resultado.insertar(resultado.longitud() + 1, ciudades[posicion - 1]);
                    autonomiaRestante = autonomia;
                }
            }
        }
    
        // Imprimir paradas
        for (int i = 0; i <= resultado.longitud() - 1; i++) {
            System.out.println("Me detengo en: " + resultado.consultar(i + 1));
        }
    
        // Mostrar autonomía sobrante
        System.out.println("autonomía sobrante: " + autonomiaRestante);
    }
    
    
    public static void main(String[] args) {
        //Ejercicio 1

        // Crear un grafo de ejemplo
        Grafo<String, Integer, Integer> grafo = new Grafo<>();

        // Agregar vértices al grafo
        grafo.insertarVertice("A", 0);
        grafo.insertarVertice("B", 0);
        grafo.insertarVertice("C", 0);
        grafo.insertarVertice("D", 0);
        

        // Agregar aristas al grafo
        grafo.insertarArista("A", "B", 1);
        grafo.insertarArista("A", "C", 1);
        grafo.insertarArista("B", "C", 1);
        grafo.insertarArista("C", "D", 1);
        

        // Calcular el grado del grafo
        int gradoMaximo = gradoGrafo(grafo);

        // Imprimir el grado máximo del grafo
        System.out.println("Grado máximo del grafo: " + gradoMaximo);

        //Ejercicio 2
        // Probar el recorrido en anchura
        recorridoAnchura(grafo, "A");

        //Ejercicio 3
        // Probar el conteo de componentes conexas
        // Crear un grafo de ejemplo
        Grafo<String, Integer, Integer> grafo2 = new Grafo<>();

        // Agregar vértices al grafo
        grafo2.insertarVertice("1", 0);
        grafo2.insertarVertice("2", 0);
        grafo2.insertarVertice("3", 0);
        grafo2.insertarVertice("4", 0);
        grafo2.insertarVertice("5", 0);
        grafo2.insertarVertice("6", 0);

        // Agregar aristas bidireccionales (grafo no dirigido)
        grafo2.insertarArista("1", "2", 1);
        grafo2.insertarArista("2", "1", 1);

        grafo2.insertarArista("2", "3", 1);
        grafo2.insertarArista("3", "2", 1);

        grafo2.insertarArista("4", "5", 1);
        grafo2.insertarArista("5", "4", 1);

        grafo2.insertarArista("5", "6", 1);
        grafo2.insertarArista("6", "5", 1);

        // Calcular número de componentes conexas
        int numComponentes = componentesConexas(grafo2);
        System.out.println("Número de componentes conexas: " + numComponentes);

        //Ejercicio 4
        // Ejemplo del enunciado
        String[] ciudades = {"Toledo", "Ciudad Real", "Jaén", "Alcalá la Real", "Granada"};
        int[] distancias = {75, 120, 173, 70, 55};
        int autonomia = 200;
        
        System.out.println("\nEjercicio 4 - Cálculo de paradas mínimas:");
        calcularViaje(ciudades, distancias, autonomia);
    }
}