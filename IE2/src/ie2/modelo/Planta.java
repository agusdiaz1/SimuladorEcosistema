/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parcial1;

/**
 *
 * @author Usuario
 */
public class Planta extends Entidad implements Reproducible {
    private int tamaño; 

    public Planta(String nombre, double energia, int tamaño) {
        super(nombre, energia);
        this.tamaño = Math.min(5, Math.max(1, tamaño));
    }

    @Override
    public void actuar(Ecosistema eco) {
        if (eco.getClimaActual() == Clima.INVIERNO) {
            return; 
        }
        intentarReproduccion(eco);
    }

    @Override
    public void mostrarEstado() {
        System.out.println("Planta '" + getNombre() + "' | Tamaño: " + tamaño + " | Energía: " + getEnergia());
    }

    public double serComida() {
        double valorNutritivo = this.tamaño * 10;
        setEnergia(0); 
        return valorNutritivo;
    }

    @Override
    public boolean puedeReproducirse() {
        return isViva() && getEnergia() >= 30;
    }

    @Override
    public void reproducirse(Ecosistema eco) {
        double factor = eco.getClimaActual().getFactorPlanta();
        if (factor > 0) {
            String nuevoNombre = getNombre() + "-Hijo";
            Planta nueva = new Planta(nuevoNombre, 20 * factor, 1);
            eco.agregarNacimientoPlanta(nueva);
            System.out.println("Planta '" + getNombre() + "' se reprodujo -> " + nuevoNombre);
        }
    }

    public int getTamaño() {
        return tamaño;
    }
}
