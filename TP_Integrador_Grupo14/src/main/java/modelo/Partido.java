package modelo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Partido {

	private int fase;
	private int ronda;
	private Equipo equipo1;
	private Equipo equipo2;
	private int golesEquipo1;
	private int golesEquipo2;
	
	public Partido(int fase, int ronda, Equipo equipo1, Equipo equipo2, int golesEquipo1, int golesEquipo2) {
		this.setFase(fase);
		this.setRonda(ronda);
		this.setEquipo1(equipo1);
		this.setEquipo2(equipo2);
		this.setGolesEquipo1(golesEquipo1);
		this.setGolesEquipo2(golesEquipo2);
	}	
	
	public String ganadorReal() {
		
		if(this.getGolesEquipo1() > this.getGolesEquipo2()) {
			return this.getEquipo1().getNombre();
		} else if(this.getGolesEquipo2() > this.getGolesEquipo1()) {
			return this.getEquipo2().getNombre();
		} else {
			return "Empate";
		}
		
	}	
}