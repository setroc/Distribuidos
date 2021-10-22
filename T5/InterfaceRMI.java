/* 
    Dessarollo de Sistemas Distribuidos
    Tarea 5
    Realizado por Isaac Godínez Cortés
*/
import java.rmi.*;  
public interface InterfaceRMI extends Remote {
    public double[][] multiplica_matrices(double[][] A, double[][] B, int N) throws RemoteException;
}
