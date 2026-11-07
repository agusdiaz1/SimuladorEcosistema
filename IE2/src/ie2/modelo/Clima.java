/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parcial1;

/**
 *
 * @author Usuario
 */
public enum Clima {
    SOLEADO(1.5, 5, 0),
    LLUVIOSO(2.0, 3, -5),
    SEQUIA(0.5, -5, 0),
    INVIERNO(0.0, -8, 0);

    private final double factorPlanta;
    private final double modEnergiaConejo;
    private final double modEnergiaLobo;

    Clima(double factorPlanta, double modEnergiaConejo, double modEnergiaLobo) {
        this.factorPlanta = factorPlanta;
        this.modEnergiaConejo = modEnergiaConejo;
        this.modEnergiaLobo = modEnergiaLobo;
    }

    public double getFactorPlanta() { return factorPlanta; }
    public double getModEnergiaConejo() { return modEnergiaConejo; }
    public double getModEnergiaLobo() { return modEnergiaLobo; }
}
