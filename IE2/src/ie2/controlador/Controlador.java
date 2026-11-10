/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ie2.controlador;

import ie2.dao.EcosistemaDAO;
import ie2.modelo.*;
import java.util.ArrayList;

public class Controlador {
    private Ecosistema ecosistema;
    private final EcosistemaDAO dao;
    private int turnosTotales;

    public Controlador() {
        this.dao = new EcosistemaDAO();
    }

    // Valida los rangos de la consigna y arranca una simulación nueva
    public String iniciarNuevaSimulacion(int numPlantas, int numConejos, int numLobos, Clima climaInicial, int turnosTotales) {
        if (numPlantas < 5 || numPlantas > 30) {
            return "Error: Las plantas iniciales deben ser entre 5 y 30.";
        }
        if (numConejos < 2 || numConejos > 15) {
            return "Error: Los conejos iniciales deben ser entre 2 y 15.";
        }
        if (numLobos < 1 || numLobos > 5) {
            return "Error: Los lobos iniciales deben ser entre 1 y 5.";
        }
        if (turnosTotales < 10 || turnosTotales > 50) {
            return "Error: Los turnos totales deben ser entre 10 y 50.";
        }

        this.turnosTotales = turnosTotales;
        this.ecosistema = new Ecosistema(climaInicial);

        for (int i = 0; i < numPlantas; i++) {
            ecosistema.agregarEntidad("planta");
        }
        for (int i = 0; i < numConejos; i++) {
            ecosistema.agregarEntidad("conejo");
        }
        for (int i = 0; i < numLobos; i++) {
            ecosistema.agregarEntidad("lobo");
        }

        dao.guardarSimulacion(ecosistema);
        return null; // Null significa sin errores
    }

    // Avanza el turno y guarda automáticamente en el DAO
    public void avanzarTurno() {
        if (ecosistema != null && !simulacionTerminada()) {
            ecosistema.procesarTurno();
            dao.guardarSimulacion(ecosistema);
        }
    }

    // Verifica si se permite intervenir (cada 3 turnos)
    public boolean sePuedeIntervenir() {
        if (ecosistema == null) return false;
        int turnoActual = ecosistema.getTurnoActual() - 1;
        return turnoActual > 0 && turnoActual % 3 == 0;
    }

    public void cambiarClima(Clima nuevoClima) {
        if (ecosistema != null) {
            ecosistema.cambiarClima(nuevoClima);
            dao.guardarSimulacion(ecosistema);
        }
    }

    public boolean agregarEntidad(String tipo) {
        if (ecosistema != null) {
            boolean exito = ecosistema.agregarEntidad(tipo);
            if (exito) {
                dao.guardarSimulacion(ecosistema);
            }
            return exito;
        }
        return false;
    }

    public boolean cargarPartidaGuardada() {
        if (dao.existeGuardado()) {
            this.ecosistema = new Ecosistema(Clima.SOLEADO);
            return dao.cargarSimulacion(ecosistema);
        }
        return false;
    }

    public boolean existeGuardado() {
        return dao.existeGuardado();
    }

    public boolean simulacionTerminada() {
        if (ecosistema == null) return true;
        return ecosistema.ecosistemaColapsado() || (ecosistema.getTurnoActual() - 1) >= turnosTotales;
    }

    // Getters para actualizar la vista
    public int getTurnoActual() { return ecosistema != null ? (ecosistema.getTurnoActual() - 1) : 0; }
    public int getTurnosTotales() { return turnosTotales; }
    public String getClimaActual() { return ecosistema != null ? ecosistema.getClimaActual().name() : "-"; }
    public int getCantPlantas() { return ecosistema != null ? ecosistema.contarPlantasVivas() : 0; }
    public int getCantConejos() { return ecosistema != null ? ecosistema.contarConejosVivos() : 0; }
    public int getCantLobos() { return ecosistema != null ? ecosistema.contarLobosVivos() : 0; }

    public ArrayList<Entidad> getEntidadesVivas() {
        ArrayList<Entidad> vivas = new ArrayList<>();
        if (ecosistema == null) return vivas;
        for (Planta p : ecosistema.getPlantas()) if (p.isViva()) vivas.add(p);
        for (Conejo c : ecosistema.getConejos()) if (c.estaVivo()) vivas.add(c);
        for (Lobo l : ecosistema.getLobos()) if (l.estaVivo()) vivas.add(l);
        return vivas;
    }

    // Genera el texto del reporte final
    public String generarTextoReporte() {
        if (ecosistema == null) return "No hay datos de simulación.";
        StringBuilder sb = new StringBuilder();
        sb.append("=== REPORTE FINAL DE LA SIMULACIÓN ===\n\n");
        
        if (ecosistema.ecosistemaColapsado()) {
            sb.append("Causa de Fin: Colapso del ecosistema.\n");
            if (ecosistema.contarPlantasVivas() == 0) sb.append("-> Extinción de Plantas.\n");
            if (ecosistema.contarConejosVivos() == 0) sb.append("-> Extinción de Conejos.\n");
            if (ecosistema.contarLobosVivos() == 0) sb.append("-> Extinción de Lobos.\n");
        } else {
            sb.append("Causa de Fin: Se completaron los ").append(turnosTotales).append(" turnos configurados.\n");
        }

        sb.append("\n-- Estadísticas de Nacimientos y Muertes --\n");
        sb.append("Plantas  | Nacidos: ").append(ecosistema.getTotalNacimientosPlantas()).append(" | Muertos: ").append(ecosistema.getTotalMuertesPlantas()).append("\n");
        sb.append("Conejos  | Nacidos: ").append(ecosistema.getTotalNacimientosConejos()).append(" | Muertos: ").append(ecosistema.getTotalMuertesConejos()).append("\n");
        sb.append("Lobos    | Nacidos: ").append(ecosistema.getTotalNacimientosLobos()).append(" | Muertos: ").append(ecosistema.getTotalMuertesLobos()).append("\n");

        sb.append("\n-- Entidades Más Longevas --\n");
        sb.append("Planta: ").append(obtenerMasLongevo(ecosistema.getPlantas())).append("\n");
        sb.append("Conejo: ").append(obtenerMasLongevo(ecosistema.getConejos())).append("\n");
        sb.append("Lobo: ").append(obtenerMasLongevo(ecosistema.getLobos())).append("\n");

        Lobo mejorLobo = null;
        for (Lobo l : ecosistema.getLobos()) {
            if (mejorLobo == null || l.getExitosCaza() > mejorLobo.getExitosCaza()) {
                mejorLobo = l;
            }
        }
        if (mejorLobo != null) {
            sb.append("\nLobo con más cacerías exitosas: ").append(mejorLobo.getNombre()).append(" (").append(mejorLobo.getExitosCaza()).append(" cacerías)\n");
        }
        return sb.toString();
    }

    private String obtenerMasLongevo(ArrayList<? extends Entidad> lista) {
        Entidad mayor = null;
        for (Entidad e : lista) {
            if (mayor == null || e.getEdad() > mayor.getEdad()) {
                mayor = e;
            }
        }
        return (mayor != null) ? mayor.getNombre() + " (" + mayor.getEdad() + " turnos)" : "Ninguno";
    }
}
