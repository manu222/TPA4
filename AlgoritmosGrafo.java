public class AlgoritmosGrafo {

    public static <Clave,InfoVertice,Coste> Par<Lista<Integer>,Lista<Clave>> dijkstra(Grafo<Clave,InfoVertice,Coste> grafo, Clave origen){

        Lista<Clave> vertices = grafo.listaVertices();
        int numeroVertices = vertices.longitud();
        Lista<Boolean> visitados = new Lista<>();
        Lista<Integer> distancias = new Lista<>();
        Lista<Clave> predecesores = new Lista<>();

        //iniciar listas
        for (int i = 1; i <= numeroVertices; i++) {
            visitados.insertar(i, false);
            predecesores.insertar(i, null);
            distancias.insertar(i, Integer.MAX_VALUE);
        }
        //distancia al origen = 0 
        int posOrigen = vertices.buscar(origen);
        distancias.modificar(posOrigen, 0);

        //recorrer todos los vertices 

        for (int i = 1; i <= numeroVertices; i++) {
            //vertice no visitado con distancia minima
            int minIndice = -1;
            int minCoste = Integer.MAX_VALUE;
            //Buscar el vertice no visitado con menor distancia
            for (int j = 1; j <= visitados.longitud(); j++) {
                if (!visitados.consultar(j)&&distancias.consultar(j)<minCoste) {
                    minIndice = j;
                    minCoste = distancias.consultar(j);
                }
                
            }
            //Si no encientro ningun vertice no visitado
            if (minIndice!=-1) {
                Clave actual = vertices.consultar(minIndice); //Obtenemos el vertice actual con menor distancia
                visitados.modificar(minIndice, true);//Marcamos como visitado 
                Lista<Clave> vecinos = grafo.listaSucesores(actual);//Obtenemos los vecinos del actual
                //recorremos los vecinos del vertice actual
                for (int j = 1; j <= vecinos.longitud(); j++) {
                    Clave vecinoActual = vecinos.consultar(j);//obtenemos el vecino en ese momento
                    int posVecino = vertices.buscar(vecinoActual);//obtenemos el indice del vecino
                    int costeVecino = (int) grafo.costeArista(actual, vecinoActual);
                    int nuevoCoste = costeVecino + distancias.consultar(minIndice);
                    if (!visitados.consultar(posVecino) &&  nuevoCoste < distancias.consultar(posVecino)) {
                        distancias.modificar(posVecino, nuevoCoste);
                        predecesores.modificar(posVecino, actual);
                    }

                }
                
            }

        }
        return new Par<>(distancias,predecesores);
    }



    public static <Clave, InfoVertice> Lista<Par<Par<Clave, Clave>, Integer>> prim(Grafo<Clave, InfoVertice, Integer> grafo, Clave origen) {

        // Listas
        Lista<Clave> vertices = grafo.listaVertices();
        int numVertices = vertices.longitud();
    
        // Lista de vértices visitados
        Lista<Boolean> visitados = new Lista<>();
        Lista<Integer> distancias = new Lista<>();
        Lista<Clave> predecesores = new Lista<>();
        // Lista de aristas del MST
        Lista<Par<Par<Clave, Clave>, Integer>> aristasMST = new Lista<>();

        //iniciar listas
        for (int i = 1; i <= numVertices; i++) {
            visitados.insertar(i, false);
            distancias.insertar(i, Integer.MAX_VALUE);
            predecesores.insertar(i, null);
        }

        //Coste al origen 0
        int posOrigen = vertices.buscar(origen);
        distancias.modificar(posOrigen, 0);

        //recorremos todos los vertices 
        for (int i = 1; i <= numVertices; i++) {
            int minIndice = -1;
            int minCoste = Integer.MAX_VALUE;
            //buscar vertice no visitado con menor coste
            for (int j = 1; j <= numVertices; j++) {
                if (!visitados.consultar(j) && distancias.consultar(j)<minCoste) {
                    minIndice = j;
                    minCoste = distancias.consultar(j);
                }
                
            }

            //Si no encontramos uno menor
            if (minIndice != -1) {
                Clave actual = vertices.consultar(minIndice);//Obtenemos el actual
                visitados.modificar(minIndice, true);//marcamos como visitado
                Clave padre = predecesores.consultar(minIndice);//padre del actual vertice
                Lista<Clave> vecinos = grafo.listaSucesores(actual);//vecinos del actual
                //si tiene padre y su conste no es nulo
                if (padre!=null) {
                    Integer coste = grafo.costeArista(actual, padre);
                    if (coste != null) {
                        aristasMST.insertar(aristasMST.longitud()+1, new Par<>(new Par<>(padre,actual),coste));//añadimos la arista 
                    }
                    
                }

                //recorremos el resto de vecinos 
                for (int j = 1; j <= vecinos.longitud(); j++) {
                    Clave vecinoActual = vecinos.consultar(j);//vecino actual
                    int posVecino = vertices.buscar(vecinoActual);// indice del vecino
                    int coste = grafo.costeArista(actual, vecinoActual);//distancia desde el vertice actual al vecino
                    //si no lo hemos visitado y el coste es menor al actual
                    if (!visitados.consultar(posVecino) && coste < distancias.consultar(posVecino)) {
                        //actualizamos distancia y predecesor
                        distancias.modificar(posVecino, coste);
                        predecesores.modificar(posVecino, actual);
                    }
                }
            }
        }

        return aristasMST;
    }

    public static <Clave, InfoVertice, Coste> Lista<Par<Par<Clave, Clave>, Coste>> kruskal(Grafo<Clave, InfoVertice, Coste> grafo) {
        // Obtener todas las aristas del grafo
        Lista<Par<Clave, Clave>> listaAristas = grafo.listaAristas();
        Lista<Clave> vertices = grafo.listaVertices();
        Lista<Par<Par<Clave, Clave>,Coste>> aristasSeleccionadas = new Lista<>();
        Lista<Par<Par<Clave, Clave>, Coste>> aristasConCoste = new Lista<>();
        Lista<Clave> representantes = new Lista<>();
        int numAristas = listaAristas.longitud();
        int numVertices = grafo.numVertices();

        //Obtener el coste de las aristas
        for (int i =  1; i <= numAristas; i++) {
            Clave origen = listaAristas.consultar(i).getAtributo();
            Clave destino = listaAristas.consultar(i).getValor();
            Coste coste = grafo.costeArista(origen, destino);

            aristasConCoste.insertar(aristasConCoste.longitud()+1, new Par<>(new Par<>(origen,destino),coste));
        }

        //Ordenar con Insertion sort
        for(int i = 2; i <= aristasConCoste.longitud(); i++){
            Par<Par<Clave,Clave>,Coste> aristaActual = aristasConCoste.consultar(i);
            int j = i - 1;
            while (j>=1 && (Integer) aristasConCoste.consultar(j).getValor() > (Integer) aristaActual.getValor()) {
                aristasConCoste.modificar(j+1, aristasConCoste.consultar(j));
                j--;
            }
            aristasConCoste.modificar(j+1,aristaActual);
        }

        //Cada vertice es su propio representante
        for (int i = 1; i <= numVertices; i++) {
            representantes.insertar(i, vertices.consultar(i));
        }

        //recorrer aristas ordenadas
        for (int i = 1; i <= aristasConCoste.longitud() && aristasSeleccionadas.longitud() < numVertices-1; i++) {
            Par<Par<Clave, Clave>, Coste> aristaActual = aristasConCoste.consultar(i);//arista actual
            Clave origen = aristaActual.getAtributo().getAtributo();
            Clave destino = aristaActual.getAtributo().getValor();
            //encontrar representantes
            Clave repOrigen = encontrarRepresentante(representantes, vertices, origen);
            Clave repDestino = encontrarRepresentante(representantes, vertices, destino);
            //si los representantes son diferentes agregrar arista
            if (!repOrigen.equals(repDestino)) {
                aristasSeleccionadas.insertar(aristasSeleccionadas.longitud()+1, aristaActual);
                unirConjuntos(representantes, vertices, repOrigen, repDestino);
            }
            

        }



        return aristasSeleccionadas;
    }

    //Funciones auxiliares
    /**
     * Encontrar el representante de un conjunto de vértices.
     * @param representantes Lista de representantes de los conjuntos.
     * @param vertices Lista de vértices.
     * @param vertice Vértice del cual se busca el representante.
     * @return El representante del conjunto al que pertenece el vértice.
     */
    private static <Clave> Clave encontrarRepresentante(Lista<Clave> representantes, Lista<Clave> vertices, Clave vertice) {
        int indice = vertices.buscar(vertice);
        Clave rep = representantes.consultar(indice);

        if (!rep.equals(vertice)) {//si el representante no es el mismo vertice
            rep = encontrarRepresentante(representantes, vertices, rep);
            representantes.modificar(indice, rep);//Actualizar representante para comprension de camino
        }
        return rep;
    }
    /**
     * Unir dos conjuntos de vértices en un solo conjunto.
     * @param representantes Lista de representantes de los conjuntos.
     * @param vertices Lista de vértices.
     * @param v1 Vértice 1.
     * @param v2 Vértice 2.
     */
    
    private static <Clave> void unirConjuntos(Lista<Clave> representantes, Lista<Clave> vertices, Clave v1, Clave v2) {
        Clave rep1 = encontrarRepresentante(representantes, vertices, v1);
        Clave rep2 = encontrarRepresentante(representantes, vertices, v2);

        representantes.modificar(vertices.buscar(rep2), rep1);
    }



    public static <Clave> Lista<Clave> reconstruirCamino(Lista<Clave> predecesores,Lista<Clave> vertices, Clave origen, Clave destino){
        Lista<Clave> camino = new Lista<>();

        while (destino != null && !destino.equals(origen)) {
            camino.insertar(1, destino);//insertamos el destino al principio
            int posActual = vertices.buscar(destino);
            if (posActual!=-1) {
                destino = predecesores.consultar(posActual);
            }else{
                destino=null;
            }
        }

        if (destino == null) {
            camino = new Lista<>();//No hay camino                  
        }else{
            camino.insertar(1, origen);
        }

        return camino;
    }

    public static void main(String[] args) {
        // Crear un grafo no dirigido para probar los algoritmos
        Grafo<String, String, Integer> grafo = new Grafo<>();
        
        // Insertar vértices
        grafo.insertarVertice("0", "Vértice 0");
        grafo.insertarVertice("1", "Vértice 1");
        grafo.insertarVertice("2", "Vértice 2");
        grafo.insertarVertice("3", "Vértice 3");
        grafo.insertarVertice("4", "Vértice 4");
        grafo.insertarVertice("5", "Vértice 5");
        
        // Insertar aristas con pesos (grafo no dirigido, insertamos en ambas direcciones)
        grafo.insertarArista("0", "1", 4);
        grafo.insertarArista("1", "0", 4);
        
        grafo.insertarArista("0", "2", 3);
        grafo.insertarArista("2", "0", 3);
        
        grafo.insertarArista("1", "2", 1);
        grafo.insertarArista("2", "1", 1);
        
        grafo.insertarArista("1", "3", 2);
        grafo.insertarArista("3", "1", 2);
        
        grafo.insertarArista("2", "3", 4);
        grafo.insertarArista("3", "2", 4);
        
        grafo.insertarArista("2", "4", 5);
        grafo.insertarArista("4", "2", 5);
        
        grafo.insertarArista("3", "4", 7);
        grafo.insertarArista("4", "3", 7);
        
        grafo.insertarArista("3", "5", 6);
        grafo.insertarArista("5", "3", 6);
        
        grafo.insertarArista("4", "5", 8);
        grafo.insertarArista("5", "4", 8);
        
        // Imprimir el grafo
        System.out.println("Grafo original:");
        System.out.println(grafo);

        // Probar algoritmo de Kruskal
        System.out.println("\nÁrbol de expansión mínima (Kruskal):");
        Lista<Par<Par<String, String>, Integer>> aristasKruskal = kruskal(grafo);
        int costeKruskal = 0;
         
        for (int i = 1; i <= aristasKruskal.longitud(); i++) {
            Par<Par<String, String>, Integer> arista = aristasKruskal.consultar(i);
            String origen = arista.getAtributo().getAtributo();
            String destino = arista.getAtributo().getValor();
            Integer coste = arista.getValor();
            
            System.out.println(origen + " - " + destino + " (Coste: " + coste + ")");
            costeKruskal += coste;
         }
         
         System.out.println("Coste total (Kruskal): " + costeKruskal);
        
        
         String[] verticesInicio = {"0", "1", "2", "3", "4", "5"};

         for (String inicio : verticesInicio) {
             System.out.println("\nÁrbol de expansión mínima (Prim) empezando desde vértice " + inicio + ":");
         
             // Ejecutar Prim desde el vértice 'inicio'
             Lista<Par<Par<String, String>, Integer>> resultadoPrim = prim(grafo, inicio);
         
             int costeTotal = 0;
         
             // Recorremos las aristas del MST
             for (int i = 1; i <= resultadoPrim.longitud(); i++) {
                 Par<Par<String, String>, Integer> arista = resultadoPrim.consultar(i);
                 String origen = arista.getAtributo().getAtributo();
                 String destino = arista.getAtributo().getValor();
                 int coste = arista.getValor();
         
                 System.out.println(origen + " - " + destino + " (Coste: " + coste + ")");
                 costeTotal += coste;
             }
         
             System.out.println("Coste total (Prim desde vértice " + inicio + "): " + costeTotal);
         }
        
        
         // Probar algoritmo de Dijkstra
         String origen = "0";
         System.out.println("\nCaminos más cortos desde el vértice " + origen + " (Dijkstra):");
         Par<Lista<Integer>, Lista<String>> resultadoDijkstra = dijkstra(grafo, origen);
         Lista<Integer> distancias = resultadoDijkstra.getAtributo();
         Lista<String> predecesores = resultadoDijkstra.getValor();
         
         Lista<String> vertices = grafo.listaVertices();
         
         for (int i = 1; i <= vertices.longitud(); i++) {
             String destino = vertices.consultar(i);
             Integer distancia = distancias.consultar(i);
         
             if (distancia == null || distancia == Integer.MAX_VALUE) {
                 System.out.println("No hay camino desde " + origen + " hasta " + destino);
             } else {
                 Lista<String> caminoCompleto = reconstruirCamino(predecesores, vertices, origen, destino);
                 System.out.println("Distancia mínima desde " + origen + " hasta " + destino + ": " + distancia + ", Camino: " + caminoCompleto);
             }
         }
         
    }
}