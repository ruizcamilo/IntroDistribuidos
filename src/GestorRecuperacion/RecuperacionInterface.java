/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GestorRecuperacion;

import IPS.IPS;
import java.io.IOException;

/**
 *
 * @author Camilo
 */
public interface RecuperacionInterface extends java.rmi.Remote{
    public boolean iniciarEPS(String user, String password) throws java.rmi.RemoteException;
    public String existePreviousServerRecord(String IPaddress) throws java.rmi.RemoteException;
    public void guardarIPSCurrentState(String IPaddress, int vac1, int vac2, int vac3) throws java.rmi.RemoteException, IOException;
    public String distribuirPedidos(int vac1, int vac2, int vac3) throws java.rmi.RemoteException;
}
