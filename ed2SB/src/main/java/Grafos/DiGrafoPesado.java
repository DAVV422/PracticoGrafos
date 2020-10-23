package Grafos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DiGrafoPesado<T extends Comparable<T>> extends GrafoPesado<T> {

    public DiGrafoPesado() {
        super();
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
    }

    @Override
    public int gradoDe(T vertice) throws ExcepcionVerticeNoExiste {
        throw new UnsupportedOperationException("No soportado en grafos dirigidos");
    }

    public int gradoDeEntrada(T vertice) throws ExcepcionVerticeNoExiste {
        return 0;
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
        for (int i=0; i < marcados.size(); i++){
            if (!marcados.get(i)){
                List<AdyacenteConPeso> adyacentes = this.listasDeAdyacencia.get(i);                
                for (AdyacenteConPeso adyacente : adyacentes) {
                    if(estaMarcadoElVertice(marcados, adyacente.getIndiceVertice())) {
                        return this.listaDeVertices.get(i);
                    }
                }
            }
        }
        return null;
    }  

     
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
        List<AdyacenteConPeso> adyacentesDelVertice = this.listasDeAdyacencia.get(posicionVertice);
        for (AdyacenteConPeso adyacente : adyacentesDelVertice) {
            T verticeAdyacente = this.listaDeVertices.get(adyacente.getIndiceVertice());
            if (!estaMarcadoElVertice(marcados, adyacente.getIndiceVertice())) {
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
    
    public int cantidadDeIslas() throws ExcepcionVerticeYaExiste {
        Grafo grafoAux = new Grafo();
        for (int i = 0; i < this.listaDeVertices.size(); i++) {
            grafoAux.insertarVertice(this.listaDeVertices.get(i));
        }        
        return grafoAux.numeroDeIslas();
    }
    
    
    
}
