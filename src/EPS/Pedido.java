/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EPS;

import java.util.List;

/**
 *
 * @author Camilo
 */
public class Pedido{
    private int cantVac1;
    private int cantVac2;
    private int cantVac3;
    
    public Pedido(){
        cantVac1 = 0;
        cantVac2 = 0;
        cantVac3 = 0;
    }
    
    public Pedido(List<Usuario> make){
        cantVac1 = 0;
        cantVac2 = 0;
        cantVac3 = 0;
        for(Usuario u: make)
        {
            switch(u.getVacPreferencia())
            {
                case Vac1:
                    cantVac1++;
                    break;
                case Vac2:
                    cantVac2++;
                    break;
                case Vac3:
                    cantVac3++;
                    break;
            }
        }
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
    
    
}
