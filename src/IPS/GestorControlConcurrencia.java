/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IPS;

import GestorRecuperacion.RecuperacionInterface;
import IPS.Pedido;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Camilo
 */
public class GestorControlConcurrencia implements GCCInterface{
    
    private IPS thisIPS;
    private int cantVac1;
    private int cantVac2;
    private int cantVac3;
    private List<Tentativa> tentativas;
    
    public GestorControlConcurrencia()
    {
        thisIPS = new IPS();
        cantVac1 = thisIPS.getTotalVac1();
        cantVac2 = thisIPS.getTotalVac2();
        cantVac3 = thisIPS.getTotalVac3();
        tentativas = Collections.synchronizedList(new ArrayList<>());
    }
    
    public String manejarPedido(int vac1, int vac2, int vac3) throws RemoteException{
        Date date = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        IPS copia = new IPS(cantVac1,cantVac2,cantVac3);
        boolean w1 = false, w2 = false, w3 = false;
        if(copia.getTotalVac1() > vac1 && vac1 != 0)
        {
            w1 = true;
            copia.setTotalVac1(copia.getTotalVac1()-vac1);
        }
        if(copia.getTotalVac2() > vac2 && vac2 != 0)
        {
            w2 = true;
            copia.setTotalVac2(copia.getTotalVac2()-vac2);
        }
        if(copia.getTotalVac3() > vac3 && vac3 != 0)
        {
            w3 = true;
            copia.setTotalVac3(copia.getTotalVac3()-vac3);
        }
        Tentativa esta = new Tentativa (date, w1, w2, w3);
        tentativas.add(esta);
        if(OptimistConcurrency(esta))
        {
            Pedido result = commit(vac1, vac2, vac3, esta, copia);
            System.out.println(thisIPS.getTotalVac1() + " "+thisIPS.getTotalVac2() + " "+ thisIPS.getTotalVac3());
            return result.getCantVac1() + "," + result.getCantVac2() + "," + result.getCantVac3();
        }else{
            Pedido result = rollback(vac1, vac2, vac3,esta);
            return result.getCantVac1() + "," + result.getCantVac2() + "," + result.getCantVac3();
        }
    }
    
    public boolean OptimistConcurrency(Tentativa trans){
        boolean valida = true;
	for (Tentativa Tv: tentativas)
        {
            if(!Tv.equals(trans))
            {
                if(Tv.isWriteVac1()==trans.isWriteVac1() || Tv.isWriteVac2()==trans.isWriteVac2() || Tv.isWriteVac3()==trans.isWriteVac3())
                {
                    valida = false;
                }
            }
        }
        return valida;
    }
    
    public synchronized Pedido commit(int vac1, int vac2, int vac3, Tentativa tent, IPS copia)
    {
        Pedido resultado = new Pedido();
        if (tent.isWriteVac1()) 
        {
            resultado.setCantVac1(0);
        }else{
            resultado.setCantVac1(vac1);
        }
        if (tent.isWriteVac2()) 
        {
            resultado.setCantVac2(0);
        }else{
            resultado.setCantVac2(vac2);
        }
        if (tent.isWriteVac3()) 
        {
            resultado.setCantVac3(0);
        }else{
            resultado.setCantVac3(vac3);
        }
        thisIPS.setTotalVac1(copia.getTotalVac1());
        thisIPS.setTotalVac2(copia.getTotalVac2());
        thisIPS.setTotalVac3(copia.getTotalVac3());
        this.setCantVac1(copia.getTotalVac1());
        this.setCantVac2(copia.getTotalVac2());
        this.setCantVac3(copia.getTotalVac3());
        tentativas.remove(tent);
        this.saveIPSState();
        return resultado;
    }
    
    public Pedido rollback(int vac1, int vac2, int vac3,Tentativa tent)
    {
        Pedido resultado = new Pedido();
        resultado.setCantVac1(vac1);
        resultado.setCantVac2(vac2);
        resultado.setCantVac2(vac3);
        tentativas.remove(tent);
        return resultado;
    }
    
    public void recoverServer(){
        Registry registry;
        try {
            //IP GLOBAL
            registry = LocateRegistry.getRegistry("25.106.247.240", 5554);
            RecuperacionInterface globalInterface = (RecuperacionInterface) registry.lookup("Recuperacion");
            String parts[];
            String vacunas = globalInterface.existePreviousServerRecord(thisIPS.getIp());
            if(vacunas != null)
            {
                parts = vacunas.split(",",4);
                cantVac1 = Integer.parseInt(parts[1]);
                cantVac2 = Integer.parseInt(parts[2]);
                cantVac3 = Integer.parseInt(parts[3]);
                thisIPS.setTotalVac1(Integer.parseInt(parts[1]));
                thisIPS.setTotalVac2(Integer.parseInt(parts[2]));
                thisIPS.setTotalVac3(Integer.parseInt(parts[3]));
                //leer transacciones sin acabar
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
    
    public void saveIPSState(){
        Registry registry;
        try {
            registry = LocateRegistry.getRegistry("127.0.0.1", 5554);
            RecuperacionInterface globalInterface = (RecuperacionInterface) registry.lookup("Recuperacion");
            globalInterface.guardarIPSCurrentState(thisIPS.getIp(), thisIPS.getTotalVac1(),thisIPS.getTotalVac2(),thisIPS.getTotalVac3());
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(GestorControlConcurrencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public IPS getThisIPS() {
        return thisIPS;
    }

    public void setThisIPS(IPS thisIPS) {
        this.thisIPS = thisIPS;
    }

    public int getCantVac1() {
        return cantVac1;
    }

    public void setCantVac1(int cantVac1) {
        this.cantVac1 = cantVac1;
    }

    public int getCantVac2() {
        return cantVac2;
    }

    public void setCantVac2(int cantVac2) {
        this.cantVac2 = cantVac2;
    }

    public int getCantVac3() {
        return cantVac3;
    }

    public void setCantVac3(int cantVac3) {
        this.cantVac3 = cantVac3;
    }

    public List<Tentativa> getTentativas() {
        return tentativas;
    }

    public void setTentativas(List<Tentativa> tentativas) {
        this.tentativas = tentativas;
    }
    
    
}
