/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import EPS.Usuario;
import EPS.Vacuna;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Camilo
 */
public class Persistence {
    private final SecretKey secretKey;
    private final String ourkey = "IPSSuperSecretPW";
    
    public Persistence() throws NoSuchAlgorithmException, UnsupportedEncodingException{
        byte[] key = ourkey.getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        this.secretKey = new SecretKeySpec(key, "AES");
    }
    
    //EPS
    public List<Usuario> leerUsuarios(String ruta){
        String s1;
        BufferedReader br;
        List<Usuario> lectura = new ArrayList<>();
        try {
            br = new BufferedReader(new FileReader(ruta));
            try {
                while ((s1 = br.readLine()) != null) {
                    String[] token = new String[3];
                    token = s1.trim().split(",", 3);
                    try{
                        Usuario u1 = new Usuario(token[0].trim(), token[1].trim(), false, Vacuna.valueOf(token[2].trim()));
                        lectura.add(u1);
                    }catch(IllegalArgumentException i)
                    {
                        i.printStackTrace();
                        return null;
                    }
                }
                return lectura;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (FileNotFoundException f) {
            f.printStackTrace();
            return null;
        }
    }
    
    
    //GESTOR DE RECUPERACIÓN
    public void initEPSRecord() throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, FileNotFoundException, InvalidKeyException
    {
        EncryptorAESFile fileEncrypterDecrypter = new EncryptorAESFile(secretKey, "AES/CBC/PKCS5Padding");
        fileEncrypterDecrypter.encrypt("", "EPS.txt");
    }
    
    public void writeEPSRecord(String user, String password) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, FileNotFoundException, InvalidKeyException, InvalidAlgorithmParameterException {
        EncryptorAESFile fileEncrypterDecrypter = new EncryptorAESFile(secretKey, "AES/CBC/PKCS5Padding");
        String decryptedContent = fileEncrypterDecrypter.decrypt("EPS.txt");
        String newContent = decryptedContent + user +" ,"+password+";";
        fileEncrypterDecrypter.encrypt(newContent, "EPS.txt");
        System.out.println(newContent);
    }
    
    public String[] existsInRecord(String user) throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, FileNotFoundException, InvalidKeyException, InvalidAlgorithmParameterException
    {
        String users[];
        boolean found = false;
        EncryptorAESFile fileEncrypterDecrypter = new EncryptorAESFile(secretKey, "AES/CBC/PKCS5Padding");
        String decryptedContent = fileEncrypterDecrypter.decrypt("EPS.txt");
        users = decryptedContent.split(";");
        for(String usuario: users)
        {
            String[] parts = new String[2];
            parts = usuario.split(",");
            if(user.equals(parts[0].trim()))
            {
                found = true;
                return parts;
            }
        }
        return null;
    }
    //IPS/Gestor Control Concurrencia
    public void initIPSRecord() throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, FileNotFoundException, InvalidKeyException
    {
        EncryptorAESFile fileEncrypterDecrypter = new EncryptorAESFile(secretKey, "AES/CBC/PKCS5Padding");
        fileEncrypterDecrypter.encrypt("", "IPS.txt");
    }
    
    public String existsInStates(String IPaddress) throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, FileNotFoundException, InvalidKeyException, InvalidAlgorithmParameterException
    {
        String IPSs[];
        boolean found = false;
        EncryptorAESFile fileEncrypterDecrypter = new EncryptorAESFile(secretKey, "AES/CBC/PKCS5Padding");
        String decryptedContent = fileEncrypterDecrypter.decrypt("IPS.txt");
        System.out.println(decryptedContent);
        IPSs = decryptedContent.split(";");
        for(String ips: IPSs)
        {
            String[] parts = new String[4];
            parts = ips.split(",", 4);
            if(IPaddress.equals(parts[0].trim()))
            {
                found = true;
                return ips;
            }
        }
        return null;
    }
    
    public void addIPS(String IPaddress, int vac1, int vac2, int vac3) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, FileNotFoundException, InvalidKeyException, InvalidAlgorithmParameterException
    {
        EncryptorAESFile fileEncrypterDecrypter = new EncryptorAESFile(secretKey, "AES/CBC/PKCS5Padding");
        String decryptedContent = fileEncrypterDecrypter.decrypt("IPS.txt");
        String newContent = decryptedContent + IPaddress +","+vac1+","+vac2+","+vac3+";";
        fileEncrypterDecrypter.encrypt(newContent, "IPS.txt");
    }
    
    public void editIPS(String IPaddress, int vac1, int vac2, int vac3) throws NoSuchAlgorithmException, NoSuchPaddingException, FileNotFoundException, InvalidKeyException, InvalidAlgorithmParameterException, IOException
    {
        String IPSs[];
        boolean found = false;
        EncryptorAESFile fileEncrypterDecrypter = new EncryptorAESFile(secretKey, "AES/CBC/PKCS5Padding");
        String decryptedContent = fileEncrypterDecrypter.decrypt("IPS.txt");
        String newContent = "";
        IPSs = decryptedContent.split(";");
        for(String ips: IPSs)
        {
            String[] parts = new String[4];
            parts = ips.split(",", 4);
            if(IPaddress.equals(parts[0].trim()))
            {
                found = true;
                newContent = newContent + IPaddress +","+vac1+","+vac2+","+vac3+";";
            }else{
                newContent = newContent + ips + ";";
            }
        }
        fileEncrypterDecrypter.encrypt(newContent, "IPS.txt");
    }
}
