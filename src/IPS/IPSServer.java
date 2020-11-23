package IPS;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class IPSServer {
    public static void main(String[] args) throws Exception{
        try{
            GestorControlConcurrencia gcc = new GestorControlConcurrencia();
            gcc.recoverServer();
            Registry registry = LocateRegistry.createRegistry(gcc.getThisIPS().getPuerto());
            GCCInterface stub = (GCCInterface) UnicastRemoteObject.exportObject((Remote) gcc,0);
            registry = LocateRegistry.getRegistry(gcc.getThisIPS().getPuerto());
            registry.rebind("GCC", (Remote) stub);
            System.out.println(gcc.getThisIPS().getTotalVac1() + " " + gcc.getThisIPS().getTotalVac2() + " " + gcc.getThisIPS().getTotalVac3());
            System.out.println("ips/gcc server ready");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("ips/gcc Server main:" + e.getMessage());
        }
    }
}