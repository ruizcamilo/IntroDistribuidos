/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IPS;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Camilo
 */
public class Tentativa {
    private Date init;
    private boolean writeVac1;
    private boolean writeVac2;
    private boolean writeVac3;

    Tentativa(Date date, boolean w1, boolean w2, boolean w3) {
        init = date;
        writeVac1 = w1;
        writeVac2 = w2;
        writeVac3 = w3; 
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.init);
        hash = 97 * hash + (this.writeVac1 ? 1 : 0);
        hash = 97 * hash + (this.writeVac2 ? 1 : 0);
        hash = 97 * hash + (this.writeVac3 ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tentativa other = (Tentativa) obj;
        if (this.writeVac1 != other.writeVac1) {
            return false;
        }
        if (this.writeVac2 != other.writeVac2) {
            return false;
        }
        if (this.writeVac3 != other.writeVac3) {
            return false;
        }
        if (!Objects.equals(this.init, other.init)) {
            return false;
        }
        return true;
    }

    public Date getInit() {
        return init;
    }

    public void setInit(Date init) {
        this.init = init;
    }

    public boolean isWriteVac1() {
        return writeVac1;
    }

    public void setWriteVac1(boolean writeVac1) {
        this.writeVac1 = writeVac1;
    }

    public boolean isWriteVac2() {
        return writeVac2;
    }

    public void setWriteVac2(boolean writeVac2) {
        this.writeVac2 = writeVac2;
    }

    public boolean isWriteVac3() {
        return writeVac3;
    }

    public void setWriteVac3(boolean writeVac3) {
        this.writeVac3 = writeVac3;
    }
    
    
}
