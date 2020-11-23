/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IPS;

import IPS.Pedido;
import java.rmi.RemoteException;

/**
 *
 * @author Camilo
 */
public interface GCCInterface extends java.rmi.Remote{
    public String manejarPedido(int vac1, int vac2, int vac3) throws RemoteException;
}
