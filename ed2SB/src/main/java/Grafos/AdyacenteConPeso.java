package Grafos;

public class AdyacenteConPeso implements Comparable<AdyacenteConPeso>{
    private int indiceVertice;
    private double costo;
    
    public AdyacenteConPeso(int vertice) {
        this.indiceVertice = vertice;
    }
    
    public AdyacenteConPeso(int vertice, double costo) {
        this.indiceVertice = vertice;
        this.costo = costo;
    }
    
    public int getIndiceVertice() {
        return indiceVertice;
    }
    
    public void setIndiceVertice(int vertice) {
        this.indiceVertice = vertice;
    }
    
    public double getCosto() {
        return costo;
    }
    
    public void setCosto(double costo) {
        this.costo = costo;
    }
    
    @Override
    public int compareTo(AdyacenteConPeso vert) {
        Integer esteVertice = this.indiceVertice;
        Integer elOtroVertice = vert.indiceVertice;
        return esteVertice.compareTo(elOtroVertice);
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + this.indiceVertice;
        return hash;
    }
    
    @Override 
    public boolean equals(Object otro) {
        if (otro == null) {
            return false;
        }
        if (getClass() != otro.getClass()) {
            return false;
        }
        AdyacenteConPeso other = (AdyacenteConPeso)otro;
        return this.indiceVertice == other.indiceVertice;
    }
}
