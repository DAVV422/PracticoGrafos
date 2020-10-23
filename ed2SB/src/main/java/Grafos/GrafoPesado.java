package Grafos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GrafoPesado<T extends Comparable<T>> {

    protected List<T> listaDeVertices;
    protected List<List<AdyacenteConPeso>> listasDeAdyacencia;
    protected static final int POSICION_INVALIDA = -1;

    public GrafoPesado() {
        this.listaDeVertices = new ArrayList<>();
        this.listasDeAdyacencia = new ArrayList<>();
    }

    public void insertarVertice(T vertice)
            throws ExcepcionVerticeYaExiste {
        if (this.existeVertice(vertice)) {
            throw new ExcepcionVerticeYaExiste("Vertice ya existe en el grafo");
        }
        this.listaDeVertices.add(vertice);
        List<AdyacenteConPeso> listaDeAdyacentesDelVertice = new ArrayList<>();
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

    public void insertarArista(T verticeOrigen, T verticeDestino, double costo)
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
        List<AdyacenteConPeso> adyacentesDelOrigen = this.listasDeAdyacencia.get(posicionDeVerticeOrigen);
        AdyacenteConPeso adyacente = new AdyacenteConPeso(posicionDeVerticeDestino, costo);
        adyacentesDelOrigen.add(adyacente);
        Collections.sort(adyacentesDelOrigen);

        if (posicionDeVerticeOrigen != posicionDeVerticeDestino) {
            adyacente = new AdyacenteConPeso(posicionDeVerticeOrigen, costo);
            List<AdyacenteConPeso> adyacentesDelDestino = this.listasDeAdyacencia.get(posicionDeVerticeDestino);
            adyacentesDelDestino.add(adyacente);
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
        List<AdyacenteConPeso> adyacentesDelOrigen = this.listasDeAdyacencia.get(posicionDeVerticeOrigen);
        AdyacenteConPeso adyacente = new AdyacenteConPeso(posicionDeVerticeDestino);
        return adyacentesDelOrigen.contains(adyacente);
    }

    public int cantidadDeAristas() {
        //hacer :'v
        return 0;
    }

    public void eliminarVertice(T verticeAEliminar) throws ExcepcionVerticeNoExiste {
        if (!this.existeVertice(verticeAEliminar)) {
            throw new ExcepcionVerticeNoExiste("Vertice a eliminar no existe en el grafo");
        }
        int posicionVerticeAEliminar = this.posicionDeVertice(verticeAEliminar);
        this.listaDeVertices.remove(posicionVerticeAEliminar);
        this.listasDeAdyacencia.remove(posicionVerticeAEliminar);

        for (List<AdyacenteConPeso> adyacentesDeUnVertice : this.listasDeAdyacencia) {
            AdyacenteConPeso adyacente = new AdyacenteConPeso(posicionVerticeAEliminar);
            if (adyacentesDeUnVertice.contains(adyacente)) {
                int posicionDelVerticeComoAdyacente = adyacentesDeUnVertice.indexOf(adyacente);
                adyacentesDeUnVertice.remove(posicionDelVerticeComoAdyacente);
            }
            for (int i = 0; i < adyacentesDeUnVertice.size(); i++) {
                AdyacenteConPeso adyacenteAux = adyacentesDeUnVertice.get(i);
                int posicionDeAdyacente = adyacenteAux.getIndiceVertice();
                if (posicionDeAdyacente > posicionVerticeAEliminar) {
                    posicionDeAdyacente--;
                    adyacenteAux.setIndiceVertice(posicionDeAdyacente);
                    //adyacentesDeUnVertice.set(i, posicionDeAdyacente);
                }
            }
        }
    }

    public void eliminarArista(T verticeOrigen, T verticeDestino)
            throws ExcepcionAristaNoExiste, ExcepcionVerticeNoExiste {
        //hacer :'v
    }

    public int gradoDe(T vertice) throws ExcepcionVerticeNoExiste {
        if (!this.existeVertice(vertice)) {
            throw new ExcepcionVerticeNoExiste("Vertice no existe");
        }
        int posicionDeVertice = this.posicionDeVertice(vertice);
        List<AdyacenteConPeso> adyacentesDelVertice = this.listasDeAdyacencia.get(posicionDeVertice);
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
            List<AdyacenteConPeso> adyacenciaDeVerticeEnTurno = this.listasDeAdyacencia.get(posicionDeVerticeEnTurno);
            for (AdyacenteConPeso adyacenteYSuPeso : adyacenciaDeVerticeEnTurno) {
                if (!estaMarcadoElVertice(marcados, adyacenteYSuPeso.getIndiceVertice())) {
                    colaDeVertices.offer(this.listaDeVertices.get(adyacenteYSuPeso.getIndiceVertice()));
                    marcarVertice(marcados, adyacenteYSuPeso.getIndiceVertice());
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
        List<AdyacenteConPeso> adyacenciasDeVerticeEnTurno = this.listasDeAdyacencia.get(posicionEnTurno);
        for (AdyacenteConPeso adyacenteYSuPeso : adyacenciasDeVerticeEnTurno) {
            if (!estaMarcadoElVertice(marcados, adyacenteYSuPeso.getIndiceVertice())) {
                dfs(recorrido, marcados, adyacenteYSuPeso.getIndiceVertice());
            }
        }
    }

    public boolean esConexo() {
        List<T> elRecorrido = bfs(this.listaDeVertices.get(0));
        return elRecorrido.size() == this.cantidadDeVertices();
    }

    //Ejercicio10
    public Grafo algoritmoDeKruskal() throws ExcepcionVerticeYaExiste, ExcepcionVerticeNoExiste, ExcepcionAristaYaExiste, ExcepcionAristaNoExiste {
        Grafo grafoAux = new Grafo();
        for (int i = 0; i < this.listaDeVertices.size(); i++) {
            grafoAux.insertarVertice(this.listaDeVertices.get(i));
        }
        List<Integer> vertices = new ArrayList<>();
        List<AdyacenteConPeso> aristasOrdenadas = new ArrayList<>();
        ordenarAristas(vertices, aristasOrdenadas);
        for (int i = 0; i < aristasOrdenadas.size(); i++) {
            grafoAux.insertarArista(vertices.get(i), aristasOrdenadas.get(i).getIndiceVertice());
            if (grafoAux.existeCiclo()) {
                grafoAux.eliminarArista(vertices.get(i), aristasOrdenadas.get(i).getIndiceVertice());
            }
        }
        return grafoAux;
    }

    private void ordenarAristas(List<Integer> vertices, List<AdyacenteConPeso> aristas) {
        for (int i = 0; i < this.listasDeAdyacencia.size() - 1; i++) {
            List<AdyacenteConPeso> adyacentes = new ArrayList<>();
            for (int j = 0; j < adyacentes.size(); i++) {
                vertices.add(i);
                aristas.add(adyacentes.get(j));
            }
        }
        for (int i = 0; i < aristas.size() - 1; i++) {
            for (int j = i + 1; j < aristas.size(); j++) {
                if (aristas.get(i).getCosto() > aristas.get(j).getCosto()) {
                    AdyacenteConPeso aux
                            = new AdyacenteConPeso(aristas.get(i).getIndiceVertice(), aristas.get(i).getCosto());
                    aristas.set(i, aristas.get(j));
                    aristas.set(j, aux);
                    Integer auxVertice = vertices.get(i);
                    vertices.set(i, vertices.get(j));
                    vertices.set(j, auxVertice);
                }
            }
        }
    }

    // Ejercicio 11
    public void algoritmoPrim() throws ExcepcionVerticeYaExiste, ExcepcionAristaYaExiste, ExcepcionVerticeNoExiste {
        Grafo grafoAux = new Grafo();
        Boolean b = true;
        grafoAux.insertarVertice(this.listaDeVertices.get(0));
        while (b==true) {
            insertarAristaMenorCosto(grafoAux, b);
        }
    }

    public void insertarAristaMenorCosto(Grafo grafoAux, Boolean control) throws ExcepcionAristaYaExiste, ExcepcionVerticeNoExiste {
        List<Integer> vertices = grafoAux.listaDeVertices;
        List<List<Integer>> adyacentesAux = grafoAux.listasDeAdyacencia;
        Integer posicionVerticeOrigen = POSICION_INVALIDA;
        Integer posicionVerticeDestino = POSICION_INVALIDA;
        double costoMenor = 0;
        boolean b = false;
        for (Integer verticeActual : vertices) {
            List<AdyacenteConPeso> adyacentes = this.listasDeAdyacencia.get(verticeActual);
            List<Integer> adyacentesDeVerticeDeGrafoAux = adyacentesAux.get(verticeActual);
            for (AdyacenteConPeso adyacenteActual : adyacentes) {
                if (!adyacentesDeVerticeDeGrafoAux.contains(adyacenteActual.getIndiceVertice())) {
                    if (b == false) {
                        costoMenor = adyacenteActual.getCosto();
                        posicionVerticeOrigen = verticeActual;
                        posicionVerticeDestino = vertices.get(adyacenteActual.getIndiceVertice());
                        b = true;
                    } else {
                        if (costoMenor > adyacenteActual.getCosto()) {
                            costoMenor = adyacenteActual.getCosto();
                            posicionVerticeOrigen = verticeActual;
                            posicionVerticeDestino = vertices.get(adyacenteActual.getIndiceVertice());
                        }
                    }
                }
            }
        }
        if (posicionVerticeOrigen != POSICION_INVALIDA) {
            T verticeOrigen = this.listaDeVertices.get(posicionVerticeOrigen);
            T verticeDestino = this.listaDeVertices.get(posicionVerticeDestino);
            grafoAux.insertarArista(verticeOrigen, verticeDestino);
        } else { 
            control = false;
        }
    }    
}
