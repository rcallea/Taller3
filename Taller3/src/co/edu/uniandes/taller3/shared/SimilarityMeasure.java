package co.edu.uniandes.taller3.shared;

public interface SimilarityMeasure extends java.io.Serializable{
    
    public double similarity(String[] x, String[] y);

}