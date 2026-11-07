/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parcial1;

/**
 *
 * @author Usuario
 */
public class Lobo extends Animal implements Peligroso {
    private int exitosCaza;

    public Lobo(String nombre, double energia, int velocidad, double peso) {
        super(nombre, energia, velocidad, peso);
        this.exitosCaza = 0;
    }

    @Override
    public void actuar(Ecosistema eco) {
        comer(eco);
    }

    @Override
    public void comer(Ecosistema eco) {
        Conejo c = eco.obtenerConejoVivoAleatorio();
        if (c != null) {
            // Probabilidad base ajustada por la energía del lobo
            double probBase = Math.min(0.8, getEnergia() / 100.0);
            if (eco.getClimaActual() == Clima.INVIERNO) {
                probBase += 0.20; // +20% en invierno
            }

            if (Math.random() < probBase) {
                c.morir();
                exitosCaza++;
                setEnergia(getEnergia() + 30);
                System.out.println("Lobo '" + getNombre() + "' cazó a Conejo '" + c.getNombre() + "' (+30 energía)");
            } else {
                System.out.println("Lobo '" + getNombre() + "' falló la caza.");
            }
        } else {
            System.out.println("Lobo '" + getNombre() + "' no encontró conejos para cazar.");
        }
    }

    @Override
    public void mostrarEstado() {
        System.out.println("Lobo '" + getNombre() + "' | Energía: " + getEnergia() + " | Cacerías exitosas: " + exitosCaza);
    }

    @Override
    public int getNivelPeligro() {
        return 8 + exitosCaza;
    }

    public int getExitosCaza() {
        return exitosCaza;
    }
}
