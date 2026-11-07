/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parcial1;

/**
 *
 * @author Usuario
 */
public abstract class Animal extends Entidad implements Mortal {
    private int velocidad;
    private double peso;

    public Animal(String nombre, double energia, int velocidad, double peso) {
        super(nombre, energia);
        this.velocidad = velocidad;
        this.peso = peso;
    }

    public abstract void comer(Ecosistema eco);

    @Override
    public boolean estaVivo() {
        return isViva() && getEnergia() > 0;
    }

    @Override
    public void morir() {
        setViva(false);
    }

    public int getVelocidad() {
        return velocidad;
    }

    public double getPeso() {
        return peso;
    }
}
