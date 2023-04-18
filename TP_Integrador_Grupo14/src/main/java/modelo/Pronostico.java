package modelo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pronostico {
	
	private String participante;
	private Partido partido;
	private Equipo equipo;
	private ResultadoEnum resultado;
	private int puntaje;
	private int puntajePorPartido;
	
	public Pronostico(String participante, Partido partido, Equipo equipo, int puntajePorPartido) {
		this.setPartido(partido);
		this.setEquipo(equipo);
		this.setParticipante(participante);
		this.setPuntajePorPartido(puntajePorPartido);
	}
	
	public int puntos() {
		int puntaje = 0;
		
		String nombreGanador = this.getPartido().ganadorReal();
		
		if(nombreGanador.equals(this.getEquipo().getNombre())) {
			puntaje = puntaje + this.getPuntajePorPartido(); 
		}
		
		return puntaje;
	}
}

