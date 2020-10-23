package Grafos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Grafo<T extends Comparable<T>> {

    protected List<T> listaDeVertices;
    protected List<List<Integer>> listasDeAdyacencia;
    protected static final int POSICION_INVALIDA = -1;

    public Grafo() {
        this.listaDeVertices = new ArrayList<>();
        this.listasDeAdyacencia = new ArrayList<>();
    }

    public void insertarVertice(T vertice)
            throws ExcepcionVerticeYaExiste {
        if (this.existeVertice(vertice)) {
            throw new ExcepcionVerticeYaExiste("Vertice ya existe en el grafo");
        }
        this.listaDeVertices.add(vertice);
        List<Integer> listaDeAdyacentesDelVertice = new ArrayList<>();
        this.listasDeAdyacencia.add(listaDeAdyacentesDelVertice);
    }

    public int cantidadDeVertices() {
        return this.listaDeVertices.size();
    }

    public boolean existeVertice(T vertice) {
        return this.posicionDeVertice(vertice) != POSICION_INVALIDA;
    }

    protected int posicionDeVertice(T vertice) {
        for (int i = 0; i < this.listaDeVertices.size(); i++) {
            T verticeEnTurno = this.listaDeVertices.get(i);
            if (verticeEnTurno.compareTo(vertice) == 0) {
                return i;
            }
        }
        return POSICION_INVALIDA;
    }

    public void insertarArista(T verticeOrigen, T verticeDestino)
            throws ExcepcionAristaYaExiste, ExcepcionVerticeNoExiste {
        if (!this.existeVertice(verticeOrigen)) {
            throw new ExcepcionVerticeNoExiste("Vertice origen no existe");
        }
        if (!this.existeVertice(verticeDestino)) {
            throw new ExcepcionVerticeNoExiste("Vertice destino no existe");
        }
        if (this.existeAdyacencia(verticeOrigen, verticeDestino)) {
            throw new ExcepcionAristaYaExiste("La arista ya existe");
        }
        int posicionDeVerticeOrigen = this.posicionDeVertice(verticeOrigen);
        int posicionDeVerticeDestino = this.posicionDeVertice(verticeDestino);
        List<Integer> adyacentesDelOrigen = this.listasDeAdyacencia.get(posicionDeVerticeOrigen);
        adyacentesDelOrigen.add(posicionDeVerticeDestino);
        Collections.sort(adyacentesDelOrigen);

        if (posicionDeVerticeOrigen != posicionDeVerticeDestino) {
            List<Integer> adyacentesDelDestino = this.listasDeAdyacencia.get(posicionDeVerticeOrigen);
            adyacentesDelDestino.add(posicionDeVerticeOrigen);
            Collections.sort(adyacentesDelOrigen);
        }
    }

    /**
     * Retorna verdadero si existe la adyacencia. Pre-Condición: los vertices ya
     * existen en el grafo.
     *
     * @param verticeOrigen
     * @param verticeDestino
     * @return
     */
    public boolean existeAdyacencia(T verticeOrigen, T verticeDestino) {
        int posicionDeVerticeOrigen = this.posicionDeVertice(verticeOrigen);
        int posicionDeVerticeDestino = this.posicionDeVertice(verticeDestino);
        List<Integer> adyacentesDelOrigen = this.listasDeAdyacencia.get(posicionDeVerticeOrigen);
        return adyacentesDelOrigen.contains(posicionDeVerticeDestino);
    }

    public int cantidadDeAristas() {
        //hacer 
        return 0;
    }

    public void eliminarVertice(T verticeAEliminar) throws ExcepcionVerticeNoExiste {
        if (!this.existeVertice(verticeAEliminar)) {
            throw new ExcepcionVerticeNoExiste("Vertice a eliminar no existe en el grafo");
        }
        int posicionVerticeAEliminar = this.posicionDeVertice(verticeAEliminar);
        this.listaDeVertices.remove(posicionVerticeAEliminar);
        this.listasDeAdyacencia.remove(posicionVerticeAEliminar);

        for (List<Integer> adyacentesDeUnVertice : this.listasDeAdyacencia) {
            if (adyacentesDeUnVertice.contains(posicionVerticeAEliminar)) {
                int posicionDelVerticeComoAdyacente = adyacentesDeUnVertice.indexOf(posicionVerticeAEliminar);
                adyacentesDeUnVertice.remove(posicionDelVerticeComoAdyacente);
            }
            for (int i = 0; i < adyacentesDeUnVertice.size(); i++) {
                int posicionDeAdyacente = adyacentesDeUnVertice.get(i);
                if (posicionDeAdyacente > posicionVerticeAEliminar) {
                    posicionDeAdyacente--;
                    adyacentesDeUnVertice.set(i, posicionDeAdyacente);
                }
            }
        }
    }

    public void eliminarArista(T verticeOrigen, T verticeDestino)
            throws ExcepcionAristaNoExiste, ExcepcionVerticeNoExiste {
        //hacer 
    }

    public int gradoDe(T vertice) throws ExcepcionVerticeNoExiste {
        if (!this.existeVertice(vertice)) {
            throw new ExcepcionVerticeNoExiste("Vertice no existe");
        }
        int posicionDeVertice = this.posicionDeVertice(vertice);
        List<Integer> adyacentesDelVertice = this.listasDeAdyacencia.get(posicionDeVertice);
        return adyacentesDelVertice.size();
    }

    public List<T> bfs(T verticeInicial) {
        List<T> recorrido = new ArrayList<>();
        if (!this.existeVertice(verticeInicial)) {
            return recorrido;
        }
        List<Boolean> marcados = inicializarMarcados();
        Queue<T> colaDeVertices = new LinkedList<>();
        colaDeVertices.offer(verticeInicial);
        int posicionDeVerticeInicial = posicionDeVertice(verticeInicial);
        marcarVertice(marcados, posicionDeVerticeInicial);
        do {
            T verticeEnTurno = colaDeVertices.poll();
            recorrido.add(verticeEnTurno);
            int posicionDeVerticeEnTurno = posicionDeVertice(verticeEnTurno);
            List<Integer> adyacenciaDeVerticeEnTurno = this.listasDeAdyacencia.get(posicionDeVerticeEnTurno);
            for (Integer posicionDeAdyacente : adyacenciaDeVerticeEnTurno) {
                if (!estaMarcadoElVertice(marcados, posicionDeAdyacente)) {
                    colaDeVertices.offer(this.listaDeVertices.get(posicionDeAdyacente));
                    marcarVertice(marcados, posicionDeAdyacente);
                }
            }
        } while (!colaDeVertices.isEmpty());
        return recorrido;
    }

    public List<Boolean> inicializarMarcados() {
        List<Boolean> marcados = new ArrayList();
        for (int i = 0; i < this.cantidadDeVertices(); i++) {
            marcados.add(Boolean.FALSE);
        }
        return marcados;
    }

    public boolean estanTodosMarcados(List<Boolean> marcados) {
        for (int i = 0; i < marcados.size(); i++) {
            if (!marcados.get(i)) {
                return false;
            }
        }
        return true;
    }

    public void marcarVertice(List<Boolean> marcados, int posicionDeVerticeInicial) {
        marcados.set(posicionDeVerticeInicial, Boolean.TRUE);
    }

    public boolean estaMarcadoElVertice(List<Boolean> marcados, Integer posicion) {
        return marcados.get(posicion);
    }

    public List<T> dfs(T verticeInicial) {
        List<T> recorrido = new ArrayList<>();
        if (!this.existeVertice(verticeInicial)) {
            return recorrido;
        }
        List<Boolean> marcados = inicializarMarcados();
        int posicionDeVerticeInicial = posicionDeVertice(verticeInicial);
        dfs(recorrido, marcados, posicionDeVerticeInicial);
        return recorrido;
    }

    public void dfs(List<T> recorrido, List<Boolean> marcados, int posicionEnTurno) {
        marcarVertice(marcados, posicionEnTurno);
        recorrido.add(this.listaDeVertices.get(posicionEnTurno));
        List<Integer> adyacenciasDeVerticeEnTurno = this.listasDeAdyacencia.get(posicionEnTurno);
        for (Integer posicionDeAdyacente : adyacenciasDeVerticeEnTurno) {
            if (!estaMarcadoElVertice(marcados, posicionDeAdyacente)) {
                dfs(recorrido, marcados, posicionDeAdyacente);
            }
        }
    }

    public boolean esConexo() {
        List<T> elRecorrido = bfs(this.listaDeVertices.get(0));
        return elRecorrido.size() == this.cantidadDeVertices();
    }

    //Ejercicio 1
    // Implementado en DiGrafo
    //Ejercicio 2
    // Implementado en DiGrafo
    //Ejercicio 3
    public boolean existeCiclo() throws ExcepcionAristaYaExiste, ExcepcionVerticeNoExiste, ExcepcionVerticeYaExiste {
        Grafo grafoAux = new Grafo();
        for (int i = 0; i < this.listaDeVertices.size(); i++) {
            grafoAux.insertarVertice(this.listaDeVertices.get(i));
        }
        List<Boolean> marcados = inicializarMarcados();
        boolean b = existeCiclo(grafoAux, marcados);
        return b;
    }

    public boolean existeCiclo(Grafo grafoAux, List<Boolean> marcados) throws ExcepcionAristaYaExiste, ExcepcionVerticeNoExiste {
        boolean b = false;
        for (int i = 0; i < grafoAux.listaDeVertices.size() && b == false; i++) {
            if (!estaMarcadoElVertice(marcados, i)) {
                b = verificarCiclo(this.listaDeVertices.get(i), grafoAux, marcados);
            }
        }
        return b;
    }

    private boolean verificarCiclo(T vertice, Grafo grafoAux, List<Boolean> marcados) throws ExcepcionAristaYaExiste, ExcepcionVerticeNoExiste {
        int posicionVertice = grafoAux.posicionDeVertice(vertice);
        marcarVertice(marcados, posicionVertice);
        List<Integer> adyacentesDelVertice = this.listasDeAdyacencia.get(posicionVertice);
        for (Integer posicionDeAdyacente : adyacentesDelVertice) {
            T verticeAdyacente = this.listaDeVertices.get(posicionDeAdyacente);
            if (!estaMarcadoElVertice(marcados, posicionDeAdyacente)) {
                grafoAux.insertarArista(vertice, verticeAdyacente);
                verificarCiclo(verticeAdyacente, grafoAux, marcados);
            } else {
                if (!grafoAux.existeAdyacencia(vertice, verticeAdyacente)) {
                    return true;
                }
            }
        }
        return false;
    }

    //Ejercicio 4
    public int numeroDeIslas() {
        int cantidadIslas = 0;
        List<T> recorrido = new ArrayList<>();
        List<Boolean> marcados = inicializarMarcados();
        int posicionDeVerticeInicial = 0;
        T sgteVertice;
        while (!estanTodosMarcados(marcados)) {
            cantidadIslas = cantidadIslas + 1;
            dfs(recorrido, marcados, posicionDeVerticeInicial);
            sgteVertice = buscarVerticeNoMarcado(marcados);
            if (sgteVertice != null) {
                posicionDeVerticeInicial = posicionDeVertice(sgteVertice);
            }
        }
        return cantidadIslas;
    }
    
    public T buscarVerticeNoMarcado(List<Boolean> marcados) {
        for (int i=0; i< marcados.size(); i ++) {
            if (!estaMarcadoElVertice(marcados, i)) {
                return this.listaDeVertices.get(i);
            }
        }
        return null;
    }
    
    //Ejercicio 5
    //implementado en DiGrafo
    
    //Ejercicio 6
    //algoritmo de warshall 
    //no implementado
    
    //Ejercicio 7
    // no implementado
    
    //Ejercicio 8
    // no implementado
    
    //Ejercicio 9
    //no implementado
    
    //Ejericio 10
    //Implementado en GrafoPesado
    
    //Ejercicio 11
    //Implementado en GrafoPesado
    
    //Ejercicio 12
    //Implementado en DiGrafo
    
}
