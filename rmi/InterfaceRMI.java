import java.rmi.*;  
public interface InterfaceRMI extends Remote {
    public String mayusculas (String name) throws RemoteException;
    public int suma (int a, int b) throws RemoteException;
    public long checksum (int[][] m) throws RemoteException;
}
