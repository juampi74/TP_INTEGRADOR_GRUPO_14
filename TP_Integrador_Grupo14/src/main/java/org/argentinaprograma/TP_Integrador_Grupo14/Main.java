package org.argentinaprograma.TP_Integrador_Grupo14;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import org.apache.commons.lang3.StringUtils;

import modelo.Equipo;
import modelo.Fase;
import modelo.Participante;
import modelo.Partido;
import modelo.Pronostico;
import modelo.Ronda;
import exceptions.NoEsNumeroEnteroException;
import exceptions.NumeroIncorrectoDeCamposException;
import servicios.LectorArchivos;

public class Main {
    
	public static void main(String[] args) {
		
        if(args.length != 2){
            System.out.println("ERROR: No ingresaste los archivos correspondientes!");
            System.exit(88);
        }
        
        
        try {
        	
        	//Valido que todo esté bien en el Archivo CSV
			boolean correcto = validarArchivos(args[0], args[1]);
			
			if(correcto) {		
				
				//Leo la configuracion (Conexion DB y Puntajes)
				String[] conf = configuracion(args[1]);
				
				//Resultados
				
				LectorArchivos lectorArchivos = new LectorArchivos(args[0]);
		        
		        //Obtengo todas las líneas del archivo CSV "Resultados"
		        lectorArchivos.parsearArchivo();
		        
		        //Creo equipos
		        ArrayList<Equipo> equipos = lectorArchivos.crearEquipos();
		        
		        //Creo Partidos            
		        ArrayList<Partido> partidos = lectorArchivos.crearPartidos(equipos);  
		        
		        //Creo Fases
                ArrayList<Fase> fases = lectorArchivos.crearFases(partidos);
		        
		        //Creo Rondas
		        ArrayList<Ronda> rondas = lectorArchivos.crearRondas(partidos);
		    
		   
		        
		        //Pronosticos
		        
		        ArrayList<String[]> pron = conexion_db(conf);
		        
		        //Creo Pronosticos           
		        ArrayList<Pronostico> pronosticos = crearPronosticos(equipos, partidos, pron, conf);
		        
		        //Creo Participantes
		        ArrayList<Participante> participantes = crearParticipantes(pronosticos);
		        
		        
		        
		        //Muestro el Puntaje
		        mostrarPuntaje(fases, rondas, pronosticos, participantes, conf);
		        				
	        }
	        		
		} catch (CsvValidationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumeroIncorrectoDeCamposException e) {
			System.out.println("\n ----- La cantidad de campos de los archivos ingresados es incorrecta! ----- ");
		} catch (NoEsNumeroEnteroException e) {
			System.out.println("\n ----- La Ronda, Cantidad de Goles, Puntaje Por Partido y Puntaje Extra deben ser Numeros Enteros! ----- ");
		}
 
	}
              

	public static String[] configuracion(String archivoConf) throws CsvValidationException, IOException {
		
		CSVReader lector = new CSVReader(new FileReader(archivoConf));			
		String[] confCSV;
		String[] configuracion = null;
		
		while((confCSV = lector.readNext()) != null) {
			configuracion = confCSV[0].split(";");
		}
		
		return configuracion;
		
	}


	public static ArrayList<String[]> conexion_db(String[] conf) {
	
		Connection conexion = null;
	    Statement consulta = null;
	    
	    try {
	        	
	    	// Abrir la conexión
	        System.out.println("Conectando a la base de datos...");
			conexion = DriverManager.getConnection(conf[1], conf[2], conf[3]);
	
			// Ejecutar Consultas
			consulta = conexion.createStatement();
			String sql = "SELECT * FROM pronostico";
				
			ResultSet resultado = consulta.executeQuery(sql);
				
			ArrayList<String[]> pronosticos = new ArrayList<String[]>();
			
			while(resultado.next()) {
				
				String pronostico[] = new String[6];
				
				pronostico[0] = resultado.getString(2);
				pronostico[1] = resultado.getString(3);
				pronostico[2] = resultado.getString(4);
				pronostico[3] = resultado.getString(5);
				pronostico[4] = resultado.getString(6);
				pronostico[5] = resultado.getString(7);
							
				pronosticos.add(pronostico);	
				
			}
			
			return pronosticos; 
							
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				conexion.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	    
	    return null;
			
	}


	private static void mostrarPuntaje(ArrayList<Fase> fases, ArrayList<Ronda> rondas, ArrayList<Pronostico> pronosticos, ArrayList<Participante> participantes, String[] conf) {
		
		@SuppressWarnings("resource")
		Scanner scn = new Scanner(System.in);
		
		System.out.println("\n¿Quiere ver el Puntaje Total del Pronostico, o el de una Fase o Ronda en Especifico? <T - F - R>");
        String mostrar = scn.nextLine();
        while(mostrar.compareToIgnoreCase("T") != 0 && mostrar.compareToIgnoreCase("F") != 0 && mostrar.compareToIgnoreCase("R") != 0) {
        	System.out.println("Error! <T - F - R>");
        	mostrar = scn.nextLine();
        }
        
        String puntajePorPartido = conf[4];
        String puntajeExtra = conf[5];
        
        System.out.println("\n-------------------------------------------------------");
        System.out.println(" - Se otorga(n) " + puntajePorPartido + " Punto(s) por cada Resultado acertado" );
        System.out.println("-------------------------------------------------------");
        System.out.println("\n---------------------------------------------------------------------------------------------------------");
        System.out.println(" - En caso de acertar todos los Resultados de una Fase o Ronda, se suma(n) " + puntajeExtra + " Punto(s) Extra por cada una" );
        System.out.println("---------------------------------------------------------------------------------------------------------");
        
        if(mostrar.equalsIgnoreCase("T")) {
        	
        	calcularPuntajeTotal(pronosticos, participantes, fases, rondas, conf);
        	
        } else if(mostrar.equalsIgnoreCase("F")) {
        	
        	System.out.print("\nFases Posibles: | ");
			for(Fase fase : fases){
				System.out.print(fase.getNro()+ " | ");
			}

			System.out.println("\nIngrese el Numero de una de las Fases listadas: ");
			String fase = scn.nextLine();

			while(!(StringUtils.isNumeric(fase)) || Integer.parseInt(fase) > (fases.size()) || Integer.parseInt(fase) <= 0) {
				System.out.println("Error! Fase Incorrecta:");
				fase = scn.nextLine();
			}

			int i = 0;
			while(Integer.parseInt(fase) != fases.get(i).getNro()) {
				i++;
			}
			
			int nroPartidosXFase = fases.get(i).getPartidos().size();
			calcularPuntajePorFase((i+1), pronosticos, participantes, nroPartidosXFase, conf);
			
		} else {
			
        	System.out.print("\nRondas Posibles: | ");
        	for(Ronda ronda : rondas) {
	        	System.out.print(ronda.getNro() + " | ");
	        }
        	
        	System.out.println("\nIngrese el Numero de una de las Rondas listadas: ");
        	String ronda = scn.nextLine();
        	
        	
        	while(!(StringUtils.isNumeric(ronda)) || Integer.parseInt(ronda) > (rondas.size()) || Integer.parseInt(ronda) <= 0) {
        		System.out.println("Error! Ronda Incorrecta:");
	        	ronda = scn.nextLine();
        	}
        	
        	int i = 0;
        	while(Integer.parseInt(ronda) != rondas.get(i).getNro()) {
        		i++;
        	}
       	
        	int nroPartidosXRonda = rondas.get(i).getPartidos().size();
        	calcularPuntajePorRonda((i + 1), pronosticos, participantes, nroPartidosXRonda, conf);
        	
        }
		
	}

	public static void calcularPuntajeTotal(ArrayList<Pronostico> pronosticos, ArrayList<Participante> participantes, ArrayList<Fase> fases, ArrayList<Ronda> rondas, String[] conf) {
		
		int punt = 0;
			
		for(int i = 0; i < pronosticos.size(); i++) {
							
			punt = pronosticos.get(i).puntos();
				
			for(Participante participante : participantes) {
					
				if(participante.getNombre().equals(pronosticos.get(i).getParticipante())) {
					
					participante.setPuntajeTotal(participante.getPuntajeTotal() + punt);
					
					int nroFase = pronosticos.get(i).getPartido().getFase();
					int nroRonda = pronosticos.get(i).getPartido().getRonda();
					
					participante.sumarPuntoFase(punt, nroFase);
					participante.sumarPuntoRonda(punt, nroRonda);
					
				}
					
			}
					
		}
		
		aplicarExtras(participantes, fases, rondas, conf);
		
		System.out.println("\n");
		
		System.out.println("----------------------------");
		System.out.println("      Puntaje Total");
		System.out.println("----------------------------");
			
		participantes = ordenarParticipantes(participantes);
		
		for(Participante participante : participantes) {
			System.out.println(" - " + participante.getNombre() + ": " + participante.getPuntajeTotal());
		}
		
		System.out.println("----------------------------");
		System.out.println("\n");
		
		
	}
	
	
	private static void aplicarExtras(ArrayList<Participante> participantes, ArrayList<Fase> fases, ArrayList<Ronda> rondas, String[] conf) {
		
		String puntajePorPartido = conf[4];
		String puntajeExtra = conf[5];
		
		for(Participante participante : participantes) {
		
			int fase = 1;
			int ronda = 1;
			
			while(fase <= participante.getPuntajePorFase().length) {
				
				if((participante.getPuntajePorFase()[fase - 1]) == ((fases.get(fase - 1).getPartidos().size()) * (Integer.parseInt(puntajePorPartido)))){
					
					participante.setPuntajeTotal(participante.getPuntajeTotal() + (Integer.parseInt(puntajeExtra)));
					
				}
				
				fase++;
				
			}
			
			
			while(ronda <= participante.getPuntajePorRonda().length) {
				
				if((participante.getPuntajePorRonda()[ronda - 1]) == ((rondas.get(ronda - 1).getPartidos().size()) * (Integer.parseInt(puntajePorPartido)))){
					
					participante.setPuntajeTotal(participante.getPuntajeTotal() + (Integer.parseInt(puntajeExtra)));
					
				}
				
				ronda++;
				
			}
			
		}
		
	}

	
	private static void calcularPuntajePorFase(int nroFase, ArrayList<Pronostico> pronosticos, ArrayList<Participante> participantes, int nroPartidosXFase, String[] conf) {
		
		puntajePorFase(nroFase, pronosticos, participantes, nroPartidosXFase, conf);

		System.out.println("\n");

		System.out.println("----------------------------");
		System.out.println("   Puntajes en la Fase " + nroFase);
		System.out.println("----------------------------");

		participantes = ordenarParticipantes(participantes);

		for(Participante participante : participantes) {
			System.out.println(" - " + participante.getNombre() + ": " + participante.getPuntajeTotal());
		}

		System.out.println("----------------------------");
		System.out.println("\n");
		
	}

	
	
	private static void puntajePorFase(int nroFase, ArrayList<Pronostico> pronosticos, ArrayList<Participante> participantes, int nroPartidosXFase, String[] conf) {
		
		int punt = 0;
		
		String puntajePorPartido = conf[4];
		String puntajeExtra = conf[5];
		
		for(int i = 0; i < pronosticos.size(); i++) {
			
			if(pronosticos.get(i).getPartido().getFase() == nroFase) {

				punt = pronosticos.get(i).puntos();

				for(Participante participante : participantes) {
					
					if(participante.getNombre().equals(pronosticos.get(i).getParticipante())) {
						
						participante.setPuntajeTotal(participante.getPuntajeTotal() + punt);
					
						participante.sumarPuntoFase(punt, nroFase);
						
					}
					
				}
				
			}
			
			for(Participante participante : participantes) {
				
				if(participante.getNombre().equals(pronosticos.get(i).getParticipante())) {
				
					if((participante.getPuntajeTotal()) == ((nroPartidosXFase) * (Integer.parseInt(puntajePorPartido)))){
					
						//System.out.println("\n" + participante.getNombre() + " le acertó a todos los partidos de la Fase " + nroFase + ", por lo que se le suman " + puntajeExtra + " Puntos Extra al puntaje total de :" + participante.getPuntajeTotal());
						participante.setPuntajeTotal(Integer.valueOf(participante.getPuntajeTotal()) + Integer.parseInt(puntajeExtra));
						//System.out.println("El Puntaje Total de " + participante.getNombre()+ " actualizado es de : " + participante.getPuntajeTotal());
					
					}
				
				}

			}
			
		}
		
		
	}


	public static void calcularPuntajePorRonda(int nroRonda, ArrayList<Pronostico> pronosticos, ArrayList<Participante> participantes, int nroPartidosXRonda, String[] conf) {
			
		puntajePorRonda(nroRonda, pronosticos, participantes, nroPartidosXRonda, conf);
		
		System.out.println("\n");
		
		System.out.println("----------------------------");
		System.out.println("   Puntajes en la Ronda " + nroRonda);
		System.out.println("----------------------------");
			
		participantes = ordenarParticipantes(participantes);
		
		for(Participante participante : participantes) {
			System.out.println(" - " + participante.getNombre() + ": " + participante.getPuntajeTotal());
		}
		
		System.out.println("----------------------------");
		System.out.println("\n");
		
	}

	private static void puntajePorRonda(int nroRonda, ArrayList<Pronostico> pronosticos, ArrayList<Participante> participantes, int nroPartidosXRonda, String[] conf) {

		int punt = 0;
		String puntajePorPartido = conf[4];
		String puntajeExtra = conf[5];
		
		for(int i = 0; i < pronosticos.size(); i++) {
			
			if(pronosticos.get(i).getPartido().getRonda() == nroRonda) {
				
				punt = pronosticos.get(i).puntos();
				
				for(Participante participante : participantes) {
						
					if(participante.getNombre().equals(pronosticos.get(i).getParticipante())) {
						participante.setPuntajeTotal(participante.getPuntajeTotal() + punt);
					}
						
				}
				
			}
			
			for(Participante participante : participantes) {
				
				if(participante.getNombre().equals(pronosticos.get(i).getParticipante())) {
				
					if((participante.getPuntajeTotal()) == ((nroPartidosXRonda) * (Integer.parseInt(puntajePorPartido)))){
					
						//System.out.println("\n" + participante.getNombre() + " le acertó a todos los partidos de la Ronda " + nroRonda + ", por lo que se le suman " + puntajeExtra + " Puntos Extra al Puntaje Total de: " + participante.getPuntajeTotal());
						participante.setPuntajeTotal(Integer.valueOf(participante.getPuntajeTotal()) + Integer.parseInt(puntajeExtra));
						//System.out.println("El Puntaje Total de " + participante.getNombre() + " actualizado es de: " + participante.getPuntajeTotal());
					
					}
				
				}

			}
			
		}
		
		
	}


	public static ArrayList<Participante> ordenarParticipantes(ArrayList<Participante> participantes) {
		
		Collections.sort(participantes, Collections.reverseOrder());
		
		return participantes;
	}
	

	public static boolean validarArchivos(String rutaArchivoResultados, String rutaArchivoConfiguracion) throws CsvValidationException, IOException, NumeroIncorrectoDeCamposException, NoEsNumeroEnteroException {
		
		CSVReader lector1 = new CSVReader(new FileReader(rutaArchivoResultados));			
		String[] resultCSV;
		String[] resultados;
		int i = 0;
		
		while((resultCSV = lector1.readNext()) != null) {
			resultados = resultCSV[0].split(";");
			
			if(resultados.length != 6) {
				throw new NumeroIncorrectoDeCamposException();
			}
			
			if(i > 0) {
				
				if(!(StringUtils.isNumeric(resultados[0])) || !(StringUtils.isNumeric(resultados[1])) || !(StringUtils.isNumeric(resultados[3])) || !(StringUtils.isNumeric(resultados[4]))) {
					throw new NoEsNumeroEnteroException();
				}
					
			}
				
			i++;
			
		}
		
		CSVReader lector2 = new CSVReader(new FileReader(rutaArchivoConfiguracion));			
		String[] confCSV;
		String[] configuracion;
		int j = 0;
		
		while((confCSV = lector2.readNext()) != null) {
			
			configuracion = confCSV[0].split(";");
				
			if(configuracion.length != 6) {
				throw new NumeroIncorrectoDeCamposException();
			}
				
			if(j > 0) {
				
				if(!(StringUtils.isNumeric(configuracion[4])) || !(StringUtils.isNumeric(configuracion[5]))) {
					throw new NoEsNumeroEnteroException();
				}
						
			}
			
			j++;
			
		}
				
		return true;
	
	}
	
	
	public static ArrayList<Pronostico> crearPronosticos(ArrayList<Equipo> equipos, ArrayList<Partido> partidos, ArrayList<String[]> pron, String[] conf) {
		
		ArrayList<Pronostico> pronosticos = new ArrayList<>();
		
		for (String[] pronostico : pron) {
			
			int i = 0;
			int j = 0;
			int k = 0;
			int l = 0;
			
			String participante = pronostico[0];
			
			while(pronostico[1].compareTo(equipos.get(i).getNombre()) != 0 && i < equipos.size()) {
				i++;
				
				if(i == equipos.size()) {
					break;
				}
			}
			
			while(pronostico[5].compareTo(equipos.get(j).getNombre()) != 0 && j < equipos.size()) {
				j++;
			
				if(j == equipos.size()) {
					break;
				}
			}
			
			Equipo ganador = null;
			
			if(pronostico[2].equalsIgnoreCase("X")) {
				ganador = equipos.get(i);
			} else if(pronostico[3].equalsIgnoreCase("X")) {
				while(equipos.get(k).getNombre().compareTo("Empate") != 0) {
					k++;
				}
				ganador = equipos.get(k);
			} else if(pronostico[4].equalsIgnoreCase("X")) {
				ganador = equipos.get(j);
			}
			
			while((partidos.get(l).getEquipo1().getNombre().compareTo(equipos.get(i).getNombre()) != 0 || partidos.get(l).getEquipo2().getNombre().compareTo(equipos.get(j).getNombre()) != 0) && l < partidos.size()) {
				l++;
				if(l == partidos.size()) {
					break;
				}
			}
			
			Partido partido = partidos.get(l);
			
			pronosticos.add(new Pronostico(participante, partido, ganador, Integer.parseInt(conf[4])));

		}
		
		return pronosticos;
		
	}
	
	
	public static ArrayList<Participante> crearParticipantes(ArrayList<Pronostico> pronosticos) {
		
		boolean participanteYaCargado = false;
        ArrayList<Participante> participantes = new ArrayList<>();

        for (Pronostico pronostico : pronosticos) {
        	participanteYaCargado = false;
        	
        	Participante nuevoParticipante = new Participante(pronostico.getParticipante(), 0);
        	
            for (Participante participante : participantes) {          	
            	if (nuevoParticipante.getNombre().equals(participante.getNombre())) {
            		participanteYaCargado = true;
                    break;
                }
            }
            
            if (!participanteYaCargado) {
            	participantes.add(nuevoParticipante);
            }
		
        }
		
		return participantes;
	
	}
	
}