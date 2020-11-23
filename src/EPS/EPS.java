package EPS;

import GestorRecuperacion.RecuperacionInterface;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import Persistencia.Persistence;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import IPS.GCCInterface;
import java.io.UnsupportedEncodingException;

public class EPS implements EPSInterface {
    private String idEPS;
    //private String password;
    private Persistence persistencia;
    private List<Usuario> personasAmparadas;
    private int puerto;
    private String ip;
    private CryptoHash encrypt;

    public EPS() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        personasAmparadas = new ArrayList<Usuario>();
        encrypt = new CryptoHash();
        persistencia = new Persistence();
    }

    public boolean leerUsuarios(String ruta) {
        List<Usuario> read = persistencia.leerUsuarios(ruta);
        if(read != null)
        {
            for(Usuario dale: read)
            {
                personasAmparadas.add(dale);
            }
            return true;
        }else{
            return false;
        }
    }
    
    public String iniciarPedido(Pedido send)
    {
        Transaccion trans =  new Transaccion(send);
        trans.start();
        synchronized(trans)
        {
            try{
                trans.wait();
                return trans.getResult();
            }catch(InterruptedException e)
            {
                e.printStackTrace();
                return null;
            }catch(Exception e)
            {
                return null;
            }
        }
    }
    
    public boolean iniciarSesionEPS(String idEPS, String password) throws NoSuchAlgorithmException {
        Registry registry;
        try {
            registry = LocateRegistry.getRegistry("127.0.0.1", 5554);
            RecuperacionInterface globalInterface = (RecuperacionInterface) registry.lookup("Recuperacion");
            System.out.println("SE FUE: "+idEPS+" "+encrypt.HashString(password));
            if(globalInterface.iniciarEPS(idEPS, encrypt.HashString(password)))
            {
                System.out.println("Inicio de sesion exitoso");
                return true;
            }
            else{
                System.out.println("Fracaso el inicio de sesion");
                return false;
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (NotBoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }


    public String getidEPS() {
        return idEPS;
    }

    public void setidEPS(String username) {
        this.idEPS = username;
    }

    public List<Usuario> getPersonasAmparadas() {
        return personasAmparadas;
    }

    public void setPersonasAmparadas(List<Usuario> personasAmparadas) {
        this.personasAmparadas = personasAmparadas;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
