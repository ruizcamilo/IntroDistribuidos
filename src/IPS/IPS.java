package IPS;

public class IPS{
    private int totalVac1;
    private int totalVac2;
    private int totalVac3;
    /*private int tentativaVac1; 
    private int tentativaVac2;
    private int tentativaVac3;*/
    private String ip;
    private int puerto;

    public IPS()
    {
      this.ip = "25.106.234.226";
      this.puerto = 5555;
      this.totalVac1 = 500;
      this.totalVac2 = 500;
      this.totalVac3 = 500;
    }
    
    public IPS(int num1, int num2, int num3)
    {
      this.ip = "192.168.1.173";
      this.puerto = 5555;
      this.totalVac1 = num1;
      this.totalVac2 = num2;
      this.totalVac3 = num3;
    }

    public int getTotalVac1() {
        return totalVac1;
    }

    public void setTotalVac1(int totalVac1) {
        this.totalVac1 = totalVac1;
    }

    public int getTotalVac2() {
        return totalVac2;
    }

    public void setTotalVac2(int totalVac2) {
        this.totalVac2 = totalVac2;
    }

    public int getTotalVac3() {
        return totalVac3;
    }

    public void setTotalVac3(int totalVac3) {
        this.totalVac3 = totalVac3;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }
   
        
}
