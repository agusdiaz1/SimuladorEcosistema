/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package ie2;

import ie2.controlador.Controlador;
import ie2.vista.FrmConfiguracion;
public class IE2 {
    
    public static void main(String[] args) {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        java.awt.EventQueue.invokeLater(() -> {
            Controlador controlador = new Controlador();
            FrmConfiguracion frmConfig = new FrmConfiguracion(controlador);
            frmConfig.setVisible(true);
        });
    }
    
}
