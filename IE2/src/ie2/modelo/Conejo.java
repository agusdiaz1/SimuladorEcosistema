/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parcial1;

/**
 *
 * @author Usuario
 */
public class Conejo extends Animal implements Reproducible {

    public Conejo(String nombre, double energia, int velocidad, double peso) {
        super(nombre, energia, velocidad, peso);
    }

    @Override
    public void actuar(Ecosistema eco) {
        comer(eco);
        if (estaVivo()) {
            intentarReproduccion(eco);
        }
    }

    @Override
    public void comer(Ecosistema eco) {
        Planta p = eco.obtenerPlantaVivaAleatoria();
        if (p != null) {
            double aporte = p.serComida();
            setEnergia(getEnergia() + aporte);
            if (aporte < 0) {
                System.out.println("Conejo '" + getNombre() + "' comió una planta venenosa! (" + aporte + " energía)");
            } else {
                System.out.println("Conejo '" + getNombre() + "' comió '" + p.getNombre() + "' (+" + aporte + " energía)");
            }
        } else {
            setEnergia(getEnergia() - 15);
            System.out.println("Conejo '" + getNombre() + "' no encontró comida (-15 energía)");
        }
    }

    @Override
    public void mostrarEstado() {
        String peligro = (getEnergia() < 20) ? " [PELIGRO: energía baja]" : "";
        System.out.println("Conejo '" + getNombre() + "' | Energía: " + getEnergia() + peligro);
    }

    @Override
    public boolean puedeReproducirse() {
        return estaVivo() && getEnergia() > 60;
    }

    @Override
    public void reproducirse(Ecosistema eco) {
        if (eco.contarConejosVivos() >= 2) {
            String nuevoNombre = "Conejo-" + (int)(Math.random() * 1000);
            Conejo nuevo = new Conejo(nuevoNombre, 40, getVelocidad(), getPeso());
            eco.agregarNacimientoConejo(nuevo);
            System.out.println("Nació un nuevo conejo: " + nuevoNombre);
        }
    }
}
