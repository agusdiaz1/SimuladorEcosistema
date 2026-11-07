/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package parcial1;

/**
 *
 * @author Usuario
 */
public interface Reproducible {
     void reproducirse(Ecosistema eco);
    boolean puedeReproducirse();
    
    default void intentarReproduccion(Ecosistema eco) {
        if (puedeReproducirse()) {
            reproducirse(eco);
        }
    }
}
