package servicios;

import com.opencsv.bean.CsvToBeanBuilder;

import modelo.Equipo;
import modelo.Fase;
import modelo.Partido;
import modelo.Ronda;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LectorArchivos {

    String rutaArchivo;
    List<ArchivoResultados> lineasArchivo1;

    public LectorArchivos(String ruta) {
        this.rutaArchivo = ruta;
        this.lineasArchivo1 = new ArrayList<>();
    }

    public void parsearArchivo() {
    	
    	List<ArchivoResultados> listaDeResultados = null;
        try {
        	// En esta primera línea definimos el archivos que va a ingresar
            listaDeResultados = new CsvToBeanBuilder(new FileReader(this.rutaArchivo))
            // con esta configuración podemos skipear la primera línea de nuestro archivo CSV
            .withSkipLines(1)
            // con esta configuración podemos elegir cual es el caracter que vamos a usar para delimitar
            .withSeparator(';')
            // Es necesario definir el tipo de dato que va a generar el objeto que estamos queriendo parsear a partir del CSV
            .withType(ArchivoResultados.class)
            .build()
            .parse();

        } catch (IOException e) {
           	e.printStackTrace();
        }
            
        this.lineasArchivo1 = listaDeResultados;
    	
    } 

    public ArrayList<Equipo> crearEquipos(){
        boolean equipoYaCargado1 = false;
        boolean equipoYaCargado2 = false;
        ArrayList<Equipo> equipos = new ArrayList<>();

        for (ArchivoResultados lineaArchivoResultados : this.lineasArchivo1) {
        	equipoYaCargado1 = false;
        	equipoYaCargado2 = false;
        	
            Equipo nuevoEquipo1 = new Equipo(lineaArchivoResultados.getEquipo1());
            Equipo nuevoEquipo2 = new Equipo(lineaArchivoResultados.getEquipo2());
                
            for (Equipo equipoGuardado1 : equipos) {          	
            	if (nuevoEquipo1.getNombre().equals(equipoGuardado1.getNombre())) {
                	equipoYaCargado1 = true;
                    break;
                }
            }
            
            if (!equipoYaCargado1) {
              	equipos.add(nuevoEquipo1);
            }
                
            for (Equipo equipoGuardado2 : equipos) {
            	if (nuevoEquipo2.getNombre().equals(equipoGuardado2.getNombre())) {              	
            	    equipoYaCargado2 = true;
                    break;
                }
            }
            
            if (!equipoYaCargado2) {
              	equipos.add(nuevoEquipo2);
            }
               
        }

        equipos.add(new Equipo("Empate")); 
        
        return equipos;
        
    }

	public ArrayList<Partido> crearPartidos(ArrayList<Equipo> equipos) {

		ArrayList<Partido> partidos = new ArrayList<>();
		
		for (ArchivoResultados lineaArchivoResultados : this.lineasArchivo1) {
			
			int i = 0;
			int j = 0;
			
			int fase = lineaArchivoResultados.getFase();
			int ronda = lineaArchivoResultados.getRonda();
			
			while(lineaArchivoResultados.getEquipo1().compareTo(equipos.get(i).getNombre()) != 0 && i < equipos.size()) {
							
				i++;

				if(i == equipos.size()) {
					break;
				}
			}
		
			while(lineaArchivoResultados.getEquipo2().compareTo(equipos.get(j).getNombre()) != 0 && j < equipos.size()) {

				j++;

				if(j == equipos.size()) {
					break;
				}
			}
			
			partidos.add(new Partido(fase, ronda, equipos.get(i), equipos.get(j), lineaArchivoResultados.getCantGoles1(), lineaArchivoResultados.getCantGoles2()));

		}
		
		return partidos;
		
	}
	
	public ArrayList<Fase> crearFases(ArrayList<Partido> partidos) {

		ArrayList<Fase> fases = new ArrayList<Fase>();

		int contFases = 0;
		int contPartidos = 0;
		int Fase = 0;
		Map<Integer, Integer> cantPartPorFase = new HashMap<Integer, Integer>();

		for(Partido partido : partidos) {
			if(contFases == 0) {
				Fase = partido.getFase();
				contFases++;
			}
			contPartidos++;
			if(partido.getFase() != Fase) {
				cantPartPorFase .put(Fase, contPartidos - 1);
				contFases++;
				contPartidos = 1;
				Fase = partido.getFase();
			}

		}
		cantPartPorFase.put(Fase, contPartidos);

		int c = 0;
		int partido = 0;

		for(int i = 0; i < contFases; i++) {
            fases.add(new Fase(i +1));
			c = 0;
			while(c < cantPartPorFase.get(i + 1)) {
				fases.get(i).agregarPartido(partidos.get(partido));
				c++;
				partido++;
			}

		}

		return fases;
	}
	

	public ArrayList<Ronda> crearRondas(ArrayList<Partido> partidos) {

		ArrayList<Ronda> rondas = new ArrayList<Ronda>();
		
		int contRondas = 0;
		int contPartidos = 0;
		int ronda = 0;
		Map<Integer, Integer> cantPartPorRonda = new HashMap<Integer, Integer>();
		
		for(Partido partido : partidos) {
			if(contRondas == 0) {
				ronda = partido.getRonda();
				contRondas++;
			}
			contPartidos++;
			if(partido.getRonda() != ronda) {
				cantPartPorRonda.put(ronda, contPartidos - 1);
				contRondas++;
				contPartidos = 1;
				ronda = partido.getRonda();
			}
			
		}
		cantPartPorRonda.put(ronda, contPartidos);
		
		int c = 0;
		int partido = 0;
		
		for(int i = 0; i < contRondas; i++) {
			rondas.add(new Ronda(i + 1));
			
			c = 0;
			while(c < cantPartPorRonda.get(i + 1)) {
				rondas.get(i).agregarPartido(partidos.get(partido));
				c++;
				partido++;
			}
			
		}
		
		return rondas;
	}
}
	