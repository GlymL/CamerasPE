package logic_2;

public enum Flota {
    VELOZ(1.5), ESTANDAR(1.0), PESADO(0.7), AGIL(1.2), TANQUE(0.5);

    private double vel;

    Flota(double d){
        vel = d;
    }

    public double getVel(){
        return vel;
    }

    public double tiempo(double coste){
        if(vel == 0){
            System.err.println("Error, vel == 0");
            return -1;
        }
        else    
            return coste/vel;
    }
}
