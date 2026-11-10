/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ie2.modelo;

/**
 *
 * @author Usuario
 */
import java.util.ArrayList;

public class Ecosistema {
        private ArrayList<Planta> plantas;
        private ArrayList<Conejo> conejos;
        private ArrayList<Lobo> lobos;
        private ArrayList<String> eventosTurno = new ArrayList<>();
        private Clima climaActual;
        private int turnoActual;

        private int totalNacimientosPlantas = 0;
        private int totalMuertesPlantas = 0;
        private int totalNacimientosConejos = 0;
        private int totalMuertesConejos = 0;
        private int totalNacimientosLobos = 0;
        private int totalMuertesLobos = 0;
        private int totalLobosAgregados = 0;

        private ArrayList<Planta> listasParaAgregarPlantas = new ArrayList<>();
        private ArrayList<Conejo> listasParaAgregarConejos = new ArrayList<>();

    public Ecosistema(Clima climaInicial) {
        this.plantas = new ArrayList<>();
        this.conejos = new ArrayList<>();
        this.lobos = new ArrayList<>();
        this.climaActual = climaInicial;
        this.turnoActual = 1;
    }

    public void agregarEntidad(String tipo) {
        agregarEntidad(tipo, 50.0);
    }

    public boolean agregarEntidad(String tipo) {
        return agregarEntidad(tipo, 50.0);
    }

    public boolean agregarEntidad(String tipo, double energiaInicial) {
        tipo = tipo.toLowerCase();
        if (tipo.equals("planta")) {
            plantas.add(new Planta("Planta-" + (plantas.size() + 1), energiaInicial, 2));
            return true;
        } else if (tipo.equals("planta venenosa")) {
            plantas.add(new PlantaVenenosa("PlantaVen-" + (plantas.size() + 1), energiaInicial, 2));
            return true;
        } else if (tipo.equals("conejo")) {
            conejos.add(new Conejo("Conejo-" + (conejos.size() + 1), energiaInicial, 10, 2.5));
            return true;
        } else if (tipo.equals("lobo")) {
            if (totalLobosAgregados >= 5) {
                return false;
            }
            lobos.add(new Lobo("Lobo-" + (lobos.size() + 1), energiaInicial, 15, 12.0));
            totalLobosAgregados++;
            return true;
        }
        return false;
    }
    public void procesarTurno() {
        
        //eventosTurno.Clear();

        for (Conejo c : conejos) {
            if (c.estaVivo()) c.setEnergia(c.getEnergia() + climaActual.getModEnergiaConejo());
        }
        for (Lobo l : lobos) {
            if (l.estaVivo()) l.setEnergia(l.getEnergia() + climaActual.getModEnergiaLobo());
        }

        ArrayList<Reproducible> reproducibles = new ArrayList<>();
        for (Planta p : plantas) if (p.isViva()) reproducibles.add(p);
        for (Conejo c : conejos) if (c.estaVivo()) reproducibles.add(c);

        for (Reproducible r : reproducibles) {
            if (r instanceof Entidad && ((Entidad) r).isViva()) {
                r.intentarReproduccion(this);
            }
        }

        for (Conejo c : conejos) {
            if (c.estaVivo()) c.actuar(this);
        }
        for (Lobo l : lobos) {
            if (l.estaVivo()) l.actuar(this);
        }

        for (Planta p : plantas) {
            if (p.isViva()) {
                p.envejecer();
                if (p.getEnergia() <= 0) {
                    p.setViva(false);
                    totalMuertesPlantas++;
                }
            }
        }
        for (Conejo c : conejos) {
            if (c.estaVivo()) {
                c.envejecer();
                c.verificarMuerte();
                if (!c.estaVivo()) totalMuertesConejos++;
            }
        }
        for (Lobo l : lobos) {
            if (l.estaVivo()) {
                l.envejecer();
                l.verificarMuerte();
                if (!l.estaVivo()) totalMuertesLobos++;
            }
        }

        plantas.addAll(listasParaAgregarPlantas);
        totalNacimientosPlantas += listasParaAgregarPlantas.size();
        listasParaAgregarPlantas.clear();

        conejos.addAll(listasParaAgregarConejos);
        totalNacimientosConejos += listasParaAgregarConejos.size();
        listasParaAgregarConejos.clear();

        mostrarEstado();
        turnoActual++;
    }

    public String mostrarEstado() {
        return "Turno: " + (turnoActual -1) +
                "| Clima: " + climaActual +
                "| Plantas: " + contarPlantasVivas() +
                "| Conejos: " + contarConejosVivos() +
                "| Lobos: " + contarLobosVivos();
    }

    public boolean ecosistemaColapsado() {
        return contarPlantasVivas() == 0 || contarConejosVivos() == 0 || contarLobosVivos() == 0;
    }

   /* public void generarReporteFinal(int turnosTotales) {
        System.out.println("      REPORTE FINAL DE SIMULACIÓN      ");

        if (ecosistemaColapsado()) {
            System.out.println("Causa de fin: Colapso del ecosistema.");
            if (contarPlantasVivas() == 0) System.out.println("-> Población extinguida: Plantas");
            if (contarConejosVivos() == 0) System.out.println("-> Población extinguida: Conejos");
            if (contarLobosVivos() == 0) System.out.println("-> Población extinguida: Lobos");
        } else {
            System.out.println("Causa de fin: Se alcanzaron los " + turnosTotales + " turnos configurados.");
        }

        System.out.println("\n Estadísticas de Nacimientos y Muertes ");
        System.out.println("Plantas - Nacidos: " + totalNacimientosPlantas + " | Muertos: " + totalMuertesPlantas);
        System.out.println("Conejos - Nacidos: " + totalNacimientosConejos + " | Muertos: " + totalMuertesConejos);
        System.out.println("Lobos - Nacidos: " + totalNacimientosLobos + " | Muertos: " + totalMuertesLobos);

        System.out.println("\n Entidades Más Longevas ");
        System.out.println("Planta más longeva: " + obtenerMasLongevo(plantas));
        System.out.println("Conejo más longevo: " + obtenerMasLongevo(conejos));
        System.out.println("Lobo más longevo: " + obtenerMasLongevo(lobos));

        Lobo mejorLobo = null;
        for (Lobo l : lobos) {
            if (mejorLobo == null || l.getExitosCaza() > mejorLobo.getExitosCaza()) {
                mejorLobo = l;
            }
        }
        if (mejorLobo != null) {
            System.out.println("\nLobo con más cacerías exitosas: " + mejorLobo.getNombre() + " (" + mejorLobo.getExitosCaza() + " cacerías)");
        }
    }*/

    private String obtenerMasLongevo(ArrayList<? extends Entidad> lista) {
        Entidad mayor = null;
        for (Entidad e : lista) {
            if (mayor == null || e.getEdad() > mayor.getEdad()) {
                mayor = e;
            }
        }
        return (mayor != null) ? mayor.getNombre() + " (" + mayor.getEdad() + " turnos)" : "Ninguno";
    }

    public void agregarNacimientoPlanta(Planta p) { listasParaAgregarPlantas.add(p); }
    public void agregarNacimientoConejo(Conejo c) { listasParaAgregarConejos.add(c); }

    public int contarPlantasVivas() {
        int c = 0;
        for (Planta p : plantas) if (p.isViva()) c++;
        return c;
    }

    public int contarConejosVivos() {
        int c = 0;
        for (Conejo co : conejos) if (co.estaVivo()) c++;
        return c;
    }

    public int contarLobosVivos() {
        int c = 0;
        for (Lobo l : lobos) if (l.estaVivo()) c++;
        return c;
    }

    public Planta obtenerPlantaVivaAleatoria() {
        ArrayList<Planta> vivas = new ArrayList<>();
        for (Planta p : plantas) if (p.isViva()) vivas.add(p);
        if (vivas.isEmpty()) return null;
        return vivas.get((int) (Math.random() * vivas.size()));
    }

    public Conejo obtenerConejoVivoAleatorio() {
        ArrayList<Conejo> vivos = new ArrayList<>();
        for (Conejo c : conejos) if (c.estaVivo()) vivos.add(c);
        if (vivos.isEmpty()) return null;
        return vivos.get((int) (Math.random() * vivos.size()));
    }

    public Clima getClimaActual() { return climaActual; }
    public void cambiarClima(Clima nuevo) { this.climaActual = nuevo; }
    
    //MÉTODOS PARA EL DAO
   public int getTurnoActual() {
        return turnoActual;
    }

    public void setTurnoActual(int turnoActual) {
        this.turnoActual = turnoActual;
    }

    public ArrayList<Planta> getPlantas() {
        return plantas;
    }

    public ArrayList<Conejo> getConejos() {
        return conejos;
    }

    public ArrayList<Lobo> getLobos() {
        return lobos;
    }

    public int getTotalNacimientosPlantas() { return totalNacimientosPlantas; }
    public void setTotalNacimientosPlantas(int n) { this.totalNacimientosPlantas = n; }

    public int getTotalMuertesPlantas() { return totalMuertesPlantas; }
    public void setTotalMuertesPlantas(int n) { this.totalMuertesPlantas = n; }

    public int getTotalNacimientosConejos() { return totalNacimientosConejos; }
    public void setTotalNacimientosConejos(int n) { this.totalNacimientosConejos = n; }

    public int getTotalMuertesConejos() { return totalMuertesConejos; }
    public void setTotalMuertesConejos(int n) { this.totalMuertesConejos = n; }

    public int getTotalNacimientosLobos() { return totalNacimientosLobos; }
    public void setTotalNacimientosLobos(int n) { this.totalNacimientosLobos = n; }

    public int getTotalMuertesLobos() { return totalMuertesLobos; }
    public void setTotalMuertesLobos(int n) { this.totalMuertesLobos = n; }

    public int getTotalLobosAgregados() { return totalLobosAgregados; }
    public void setTotalLobosAgregados(int n) { this.totalLobosAgregados = n; }

    public void reiniciarEcosistema() {
        this.plantas.clear();
        this.conejos.clear();
        this.lobos.clear();
        this.listasParaAgregarPlantas.clear();
        this.listasParaAgregarConejos.clear();
        this.turnoActual = 1;
        this.totalNacimientosPlantas = 0;
        this.totalMuertesPlantas = 0;
        this.totalNacimientosConejos = 0;
        this.totalMuertesConejos = 0;
        this.totalNacimientosLobos = 0;
        this.totalMuertesLobos = 0;
        this.totalLobosAgregados = 0;
    }
}
