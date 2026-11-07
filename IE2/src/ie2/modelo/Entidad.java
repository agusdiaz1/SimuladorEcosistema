/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parcial1;

/**
 *
 * @author Usuario
 */
public abstract class Entidad {
    private String nombre;
    private double energia;
    private int edad;
    private boolean viva;

    public Entidad(String nombre, double energia) {
        this.nombre = nombre;
        setEnergia(energia);
        this.edad = 0;
        this.viva = true;
    }

    public abstract void actuar(Ecosistema eco);
    public abstract void mostrarEstado();

    public void envejecer() {
        this.edad++;
        setEnergia(this.energia - 5); // Descuento de energía base por existir
    }

    // Getters y Setters con validaciones
    public String getNombre() {
        return nombre;
    }

    public double getEnergia() {
        return energia;
    }

    public void setEnergia(double energia) {
        if (energia <= 0) {
            this.energia = 0;
            this.viva = false;
        } else {
            this.energia = energia;
        }
    }

    public int getEdad() {
        return edad;
    }

    public boolean isViva() {
        return viva;
    }

    public void setViva(boolean viva) {
        this.viva = viva;
    }
}