package logic;

public enum EnumFlota {
    VELOZ(1.5), ESTANDAR(1.0), PESADO(0.7), AGIL(1.2), TANQUE(0.5);

    private final double vel;

    EnumFlota(double d){
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
