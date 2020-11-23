/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GestorRecuperacion;

import IPS.GCCInterface;
import Persistencia.Persistence;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.rmi.AccessException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author Camilo
 */
public class GestorRecuperacion implements RecuperacionInterface{
    private Persistence persistencia;
    private final String myIPS = "25.106.247.240";
    private final String[] IPSs = {"25.106.234.226","25.106.234.84"};
    
    public GestorRecuperacion() throws NoSuchAlgorithmException, UnsupportedEncodingException{
        this.persistencia = new Persistence();
    }

    public Persistence getPersistencia() {
        return persistencia;
    }

    public void setPersistencia(Persistence persistencia) {
        this.persistencia = persistencia;
    }

    public String getMyIPS() {
        return myIPS;
    } 
    
    public String distribuirPedidos(int vac1, int vac2, int vac3) throws RemoteException {
        String resultado;
        String[] cants;
        for(String ip:IPSs)
        {
            resultado = mandarPedido(vac1,vac2,vac3,ip);
            if(resultado != null)
            {
                cants = resultado.split(",", 3);
                if(cants[0].equals("0") && cants[1].equals("0") && cants[2].equals("0"))
                {
                    return resultado;
                }else{
                    vac1-=Integer.parseInt(cants[0]);
                    vac2-=Integer.parseInt(cants[1]);
                    vac3-=Integer.parseInt(cants[2]);
                }
            }
        }
        return null;
    }
    
    public String mandarPedido(int vac1, int vac2, int vac3, String IPaddress) throws RemoteException
    {
        try{
            Registry registry;
            registry = LocateRegistry.getRegistry(IPaddress, 5555);
            GCCInterface IPSInterface = (GCCInterface) registry.lookup("GCC");
            return IPSInterface.manejarPedido(vac1, vac2, vac3);
        }catch(ConnectException c)
        {
            System.out.println("El servidor "+IPaddress+" no esta disponible.");
            return null;
        } catch (NotBoundException ex) {
            Logger.getLogger(GestorRecuperacion.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @Override
    public boolean iniciarEPS(String user, String password)  {
        try {
          File myObj = new File("EPS.txt");
          if (myObj.createNewFile()) { //Si no existe el archivo
                this.persistencia.initEPSRecord();
                this.persistencia.writeEPSRecord(user, password);
                System.out.println("Archivo creado, EPS registrada");
                return true;
          } else { //Si ya existe el archivo
              String userLine[];
              userLine = this.persistencia.existsInRecord(user);
              if(userLine != null)
              {
                  if(password.equals(userLine[1]) && user.equals(userLine[0].trim()))
                  {
                      System.out.println(user+": Contraseña correcta");
                      return true;
                  }else{
                      System.out.println(user+": Contraseña incorrecta");
                      return false;
                  }
              }
              else{
                  this.persistencia.writeEPSRecord(user, password);
                  return true;
              }
          }
        } catch (IOException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException e) {
          System.out.println("An error occurred.");
          return false;
        }
    }
    
    public String existePreviousServerRecord(String IPaddress) throws java.rmi.RemoteException
    {
        try {
          File myObj = new File("IPS.txt");
          if (myObj.createNewFile()) { //Si no existe el archivo
                this.persistencia.initIPSRecord();
                System.out.println("Archivo IPS creado");
                return null;
          } else { //Si ya existe el archivo
              String record;
              record = this.persistencia.existsInStates(IPaddress);
              if(record != null)
              {
                  return record;
              }
              else{
                  return null;
              }
          }
        } catch (IOException e) {
          System.out.println("An error occurred.");
          e.printStackTrace();
          return null;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException ex) {
          return null;
        }
    }
    
    public void guardarIPSCurrentState(String IPaddress, int vac1, int vac2, int vac3) throws java.rmi.RemoteException, IOException, FileNotFoundException
    {
        try {
            if(this.persistencia.existsInStates(IPaddress) == null)
            {
                this.persistencia.addIPS(IPaddress, vac1, vac2, vac3);
            }else{
                this.persistencia.editIPS(IPaddress, vac1, vac2, vac3);
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(GestorRecuperacion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(GestorRecuperacion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(GestorRecuperacion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(GestorRecuperacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
