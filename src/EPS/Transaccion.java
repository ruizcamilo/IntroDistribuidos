/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EPS;

import GestorRecuperacion.RecuperacionInterface;
import IPS.GCCInterface;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Camilo
 */
public class Transaccion extends Thread{
    private final Pedido send;
    private String result;
    
    public Transaccion(Pedido entrante)
    {
        send = entrante;
    }
    
    @Override
    public void run(){
            try {    
                Registry registry;
                //IP GLOBAL
                registry = LocateRegistry.getRegistry("25.106.247.240", 5554);
                RecuperacionInterface globalInterface = (RecuperacionInterface) registry.lookup("Recuperacion");
                result = globalInterface.distribuirPedidos(send.getCantVac1(), send.getCantVac2(), send.getCantVac3());
            } catch (RemoteException | NotBoundException ex) {
                Logger.getLogger(Transaccion.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
    
    
}
