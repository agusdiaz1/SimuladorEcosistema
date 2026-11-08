/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ie2.modelo;

/**
 *
 * @author Usuario
 */
public class PlantaVenenosa extends Planta implements Peligroso {

    public PlantaVenenosa(String nombre, double energia, int tamaño) {
        super(nombre, energia, tamaño);
    }

    @Override
    public double serComida() {
        setEnergia(0);
        return -30; 
    }

    @Override
    public int getNivelPeligro() {
        return 5;
    }
}
