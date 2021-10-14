import java.rmi.*;  
public interface InterfaceRMI extends Remote {
    public int[][] multiplica_matrices(int[][] A, int[][] B, int N) throws RemoteException;
}
