package ie2.dao;

import ie2.modelo.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class EcosistemaDAO {

    private final String rutaArchivo;

    //Guarda en la raiz del proyecto por defecto
    public EcosistemaDAO() {
        this.rutaArchivo = "simulacion.txt";
    }

    public EcosistemaDAO(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    //Guarda el estado actual en el archivo
    public boolean guardarSimulacion(Ecosistema eco) {
        if (eco == null) {
            return false;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
           
            bw.write("TURNO," + eco.getTurnoActual()+ "," + eco.getClimaActual().name());
            bw.newLine();
            bw.write("STATS," + eco.getTotalNacimientosPlantas() + "," + eco.getTotalMuertesPlantas() + "," + eco.getTotalNacimientosConejos() + "," + eco.getTotalMuertesConejos() + "," + eco.getTotalNacimientosLobos() + "," + eco.getTotalMuertesLobos() + "," + eco.getTotalLobosAgregados());
            bw.newLine();

            for (Planta p : eco.getPlantas()) {
                if (p.isViva()) {
                    bw.write("PLANTA," + p.getNombre() + "," + p.getEnergia() + "," + p.getTamaño()+ "," + p.getEdad());
                    bw.newLine();
                }
            }

            for (Conejo c : eco.getConejos()) {
                if (c.estaVivo()) {
                    bw.write("CONEJO," + c.getNombre() + "," + c.getEnergia() + "," + c.getVelocidad() + "," + c.getPeso()+ "," + c.getEdad());
                    bw.newLine();
                }
            }

            for (Lobo l : eco.getLobos()) {
                if (l.estaVivo()) {
                    bw.write("LOBO," + l.getNombre() + "," + l.getEnergia() + "," + l.getVelocidad() + "," + l.getPeso()+ "," + l.getEdad() + "," + l.getExitosCaza());
                    bw.newLine();
                }
            }

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    //Carga el estado desde el archivo txt
    public boolean cargarSimulacion(Ecosistema eco) {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists() || eco == null) {
            return false;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            eco.reiniciarEcosistema();

            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(",");
                String tipo = datos[0];

                switch (tipo) {
                    case "TURNO":
                        eco.setTurnoActual(Integer.parseInt(datos[1]));
                        eco.cambiarClima(Clima.valueOf(datos[2]));
                        break;
                    
                    case "STATS":
                        eco.setTotalNacimientosPlantas(Integer.parseInt(datos[1]));
                        eco.setTotalMuertesPlantas(Integer.parseInt(datos[2]));
                        eco.setTotalNacimientosConejos(Integer.parseInt(datos[3]));
                        eco.setTotalMuertesConejos(Integer.parseInt(datos[4]));
                        eco.setTotalNacimientosLobos(Integer.parseInt(datos[5]));
                        eco.setTotalMuertesLobos(Integer.parseInt(datos[6]));
                        eco.setTotalLobosAgregados(Integer.parseInt(datos[7]));
                        break;

                    case "PLANTA":
                        String nomP = datos[1];
                        double eneP = Double.parseDouble(datos[2]);
                        int tamP = Integer.parseInt(datos[3]);
                        Planta planta = new Planta(nomP, eneP, tamP);
                        planta.setEdad(Integer.parseInt(datos[4]));
                        eco.getPlantas().add(planta);
                        break;

                    case "CONEJO":
                        String nomC = datos[1];
                        double eneC = Double.parseDouble(datos[2]);
                        int velC = Integer.parseInt(datos[3]);
                        double pesoC = Double.parseDouble(datos[4]);
                        Conejo conejo = new Conejo(nomC, eneC, velC, pesoC);
                        conejo.setEdad(Integer.parseInt(datos[5]));
                        eco.getConejos().add(conejo);
                        break;

                    case "LOBO":
                        String nomL = datos[1];
                        double eneL = Double.parseDouble(datos[2]);
                        int velL = Integer.parseInt(datos[3]);
                        double pesoL = Double.parseDouble(datos[4]);
                        Lobo lobo = new Lobo(nomL, eneL, velL, pesoL);
                        lobo.setEdad(Integer.parseInt(datos[5]));
                        lobo.setExitosCaza(Integer.parseInt(datos[6]));
                        eco.getLobos().add(lobo);
                        break;
                }
            }

            return true;
        } catch (IOException | NumberFormatException e) {
            return false;
        }
    }

    public boolean existeGuardado() {
        File archivo = new File(rutaArchivo);
        return archivo.exists() && archivo.length() > 0;
    }
}
