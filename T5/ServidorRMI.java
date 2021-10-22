/* 
    Dessarollo de Sistemas Distribuidos
    Tarea 5
    Realizado por Isaac Godínez Cortés
*/
import java.rmi.Naming;

public class ServidorRMI {
    public static void main(String[] args) throws Exception {
        String url = "rmi://localhost/prueba";
        // System.out.println(url);
        ClaseRMI obj = new ClaseRMI();
        Naming.rebind(url, obj);
    }
}
