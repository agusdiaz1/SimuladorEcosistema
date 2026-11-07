/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parcial1;

/**
 *
 * @author Usuario
 */
import java.util.ArrayList;

public class Ecosistema {
        private ArrayList<Planta> plantas;
        private ArrayList<Conejo> conejos;
        private ArrayList<Lobo> lobos;
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

    public void agregarEntidad(String tipo, double energiaInicial) {
        tipo = tipo.toLowerCase();
        if (tipo.equals("planta")) {
            plantas.add(new Planta("Planta-" + (plantas.size() + 1), energiaInicial, 2));
        } else if (tipo.equals("planta venenosa")) {
            plantas.add(new PlantaVenenosa("PlantaVen-" + (plantas.size() + 1), energiaInicial, 2));
        } else if (tipo.equals("conejo")) {
            conejos.add(new Conejo("Conejo-" + (conejos.size() + 1), energiaInicial, 10, 2.5));
        } else if (tipo.equals("lobo")) {
            if (totalLobosAgregados >= 5) {
                System.out.println("No se pueden agregar más de 5 lobos en toda la simulación.");
                return;
            }
            lobos.add(new Lobo("Lobo-" + (lobos.size() + 1), energiaInicial, 15, 12.0));
            totalLobosAgregados++;
        }
    }

    public void procesarTurno() {
        System.out.println("\n= TURNO " + turnoActual + "  Clima: " + climaActual + " =");

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

    public void mostrarEstado() {
        System.out.println("Estado actual -> Plantas: " + contarPlantasVivas() +
                " Conejos: " + contarConejosVivos() +
                " Lobos: " + contarLobosVivos());
    }

    public boolean ecosistemaColapsado() {
        return contarPlantasVivas() == 0 || contarConejosVivos() == 0 || contarLobosVivos() == 0;
    }

    public void generarReporteFinal(int turnosTotales) {
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
}
