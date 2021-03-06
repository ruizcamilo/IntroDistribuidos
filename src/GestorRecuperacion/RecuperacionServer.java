/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GestorRecuperacion;

import IPS.IPS;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import IPS.GCCInterface;

/**
 *
 * @author Camilo
 */
public class RecuperacionServer {
    public static void main(String[] args) throws Exception{
        try{
            GestorRecuperacion miGR = new GestorRecuperacion();
            System.setProperty("java.rmi.server.hostname",miGR.getMyIPS());
            Registry registry = LocateRegistry.createRegistry(5554);
            RecuperacionInterface stub = (RecuperacionInterface) UnicastRemoteObject.exportObject((Remote) miGR,0);
            registry = LocateRegistry.getRegistry(5554);
            registry.rebind("Recuperacion", (Remote) stub);
            System.out.println("Recuperation server ready");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Recuperacion Server main:" + e.getMessage());
        }
    }
}
