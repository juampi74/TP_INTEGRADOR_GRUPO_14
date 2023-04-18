package org.argentinaprograma.TP_Integrador_Grupo14;

import org.junit.Test;

import com.opencsv.exceptions.CsvValidationException;

import exceptions.NoEsNumeroEnteroException;
import exceptions.NumeroIncorrectoDeCamposException;
import modelo.Equipo;
import modelo.Participante;
import modelo.Partido;
import modelo.Pronostico;
import servicios.LectorArchivos;

import java.io.IOException;
import java.util.ArrayList;

public class PuntajeTest {

	@Test
	public void validarCalculoDePuntaje() {
		
		String rutaArchivoResultados = "src/main/resources/resultados.csv";
		String rutaArchivoConfiguracion = "src/main/resources/conf.csv";
		
		try {
			boolean correcto = Main.validarArchivos(rutaArchivoResultados, rutaArchivoConfiguracion);
		
			if(correcto) {
		
				//Leo la configuracion (Conexion DB y Puntajes)
				String[] conf = Main.configuracion(rutaArchivoConfiguracion);
						
				//Resultados
		
				LectorArchivos lectorArchivos = new LectorArchivos(rutaArchivoResultados);
		        
		        //Obtengo todas las líneas del archivo CSV "Resultados"
		        lectorArchivos.parsearArchivo();
		        
		        //Creo equipos
		        ArrayList<Equipo> equipos = lectorArchivos.crearEquipos();
		        
		        //Creo Partidos            
		        ArrayList<Partido> partidos = lectorArchivos.crearPartidos(equipos);               
		        
			
				
				//Pronosticos        
		
		        ArrayList<String[]> pron = Main.conexion_db(conf);
		        
		        //Creo Pronosticos           
		        ArrayList<Pronostico> pronosticos = Main.crearPronosticos(equipos, partidos, pron, conf);    
		
		        //Creo Participantes
		        ArrayList<Participante> participantes = Main.crearParticipantes(pronosticos);
		        
		        ArrayList<Participante> p = new ArrayList<Participante>();
		        p.add(participantes.get(0));
		        
		        
		        //Calculo Y Muestro Puntaje de un Participante
		        Main.calcularPuntajePorRonda(1, pronosticos, p, 6, conf);
		        p.get(0).setPuntajeTotal(0);
		        Main.calcularPuntajePorRonda(2, pronosticos, p, 1, conf);
        
			}
		
		} catch (CsvValidationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumeroIncorrectoDeCamposException e) {
			System.out.println("\n ----- La cantidad de campos de los archivos es incorrecta! ----- \n");
		} catch (NoEsNumeroEnteroException e) {
			System.out.println("\n ----- La Ronda y la Cantidad de Goles deben ser Numeros Enteros! ----- \n");
		}
			
	}

}
