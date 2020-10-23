package Grafos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DiGrafo<T extends Comparable<T>> extends Grafo<T> {

    public DiGrafo() {
        super();
    }

    @Override
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
    }

    @Override
    public int gradoDe(T vertice) throws ExcepcionVerticeNoExiste {
        throw new UnsupportedOperationException("No soportado en grafos dirigidos");
    }

    public int gradoDeEntrada(T vertice) throws ExcepcionVerticeNoExiste {
        int posicionVertice = posicionDeVertice(vertice);
        int grado = 0;
        for (int i = 0; i < this.listasDeAdyacencia.size(); i++) {
            List<Integer> adyacentes = this.listasDeAdyacencia.get(i);
            if (adyacentes.contains(posicionVertice)) {
                grado = grado + 1;
            }
        }
        return grado;
    }

    public int gradoDeSalida(T vertice) throws ExcepcionVerticeNoExiste {
        return super.gradoDe(vertice);
    }

    @Override
    public void eliminarArista(T verticeOrigen, T verticeDestino) throws ExcepcionAristaNoExiste, ExcepcionVerticeNoExiste {
        super.eliminarArista(verticeOrigen, verticeDestino); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int cantidadDeAristas() {
        return super.cantidadDeAristas(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean esConexo() {
        throw new UnsupportedOperationException("No soportado en grafos dirigidos");
    }

    public boolean esFuertementeConexo() {
        List<T> elRecorrido;
        for (T unVertice : this.listaDeVertices) {
            elRecorrido = dfs(unVertice);
            if (elRecorrido.size() != this.cantidadDeVertices()) {
                return false;
            }
        }
        return true;
    }

    //Ejercicio 2;
    public boolean esDebilmenteConexo() {
        List<T> recorrido = new ArrayList<>();
        List<Boolean> marcados = inicializarMarcados();
        int posicionDeVerticeInicial = 0;
        T sgteVertice;
        do {
            dfs(recorrido, marcados, posicionDeVerticeInicial);
            if (estanTodosMarcados(marcados)) {
                return true;
            }
            sgteVertice = buscarVerticeNoMarcadoConAdyacenteMarcado(marcados);
            if (sgteVertice != null) {
                posicionDeVerticeInicial = posicionDeVertice(sgteVertice);
            }
        } while (sgteVertice != null);
        return false;
    }

    public T buscarVerticeNoMarcadoConAdyacenteMarcado(List<Boolean> marcados) {
        for (int i = 0; i < marcados.size(); i++) {
            if (!marcados.get(i)) {
                List<Integer> adyacencias = this.listasDeAdyacencia.get(i);
                for (Integer posicionAdyacente : adyacencias) {
                    if (estaMarcadoElVertice(marcados, posicionAdyacente)) {
                        return this.listaDeVertices.get(i);
                    }
                }
            }
        }
        return null;
    }

    //Ejercicio 1
    public boolean existeCiclo() throws ExcepcionAristaYaExiste, ExcepcionVerticeYaExiste, ExcepcionVerticeNoExiste {
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

    //Ejercicio 5 
    public int cantidadDeIslas() throws ExcepcionVerticeYaExiste {
        Grafo grafoAux = new Grafo();
        for (int i = 0; i < this.listaDeVertices.size(); i++) {
            grafoAux.insertarVertice(this.listaDeVertices.get(i));
        }
        return grafoAux.numeroDeIslas();
    }

    //Ejercicio 12
    public List<Integer> ordenTopologico() throws ExcepcionVerticeNoExiste {
        Queue<Integer> colaDeVertices = new LinkedList<>();
        List<T> vertices = this.listaDeVertices;
        List<Integer> orden = new ArrayList<>();
        List<Integer> listaDeGrado = new ArrayList<>();
        obtenerGrados(listaDeGrado);
        añadirVerticeConGradoEntrada0(colaDeVertices, vertices, listaDeGrado);
        while (!colaDeVertices.isEmpty()) {
            int vertice = colaDeVertices.poll();
            orden.add(vertice);            
            restarGrado(vertice, vertices, listaDeGrado);
            añadirVerticeConGradoEntrada0(colaDeVertices, vertices, listaDeGrado);           
        }
        return orden;
    }
    
    private void restarGrado(int vertice, List<T> vertices, List<Integer> listaDeGrado){
        List<Integer> adyacentes = this.listasDeAdyacencia.get(vertice);
        for (int i=0; i < vertices.size(); i++) {
            T verticeAdyacente = vertices.get(i);
            if (adyacentes.contains(posicionDeVertice(verticeAdyacente))) {
                int grado = listaDeGrado.get(i);
                listaDeGrado.set(i, grado-1);
            }
            
        }
    }
    
    private void obtenerGrados(List<Integer> listaDeGradoDe) throws ExcepcionVerticeNoExiste {
        for (int i = 0; i < this.listaDeVertices.size(); i++) {
            T verticeActual = this.listaDeVertices.get(i);
            listaDeGradoDe.add(gradoDeEntrada(verticeActual));
        }
    }
    
    private void añadirVerticeConGradoEntrada0(Queue<Integer> colaDeVertices, List<T> vertices, List<Integer> listaDeGrados) throws ExcepcionVerticeNoExiste {
        for (int i = 0; i < vertices.size(); i++) {
            T verticeActual = vertices.get(i);
            if (verticeActual != null) {
                if (listaDeGrados.get(i) == 0) {
                    colaDeVertices.add(posicionDeVertice(verticeActual));
                    vertices.set(i, null);
                }
            }
        }
    }
}
