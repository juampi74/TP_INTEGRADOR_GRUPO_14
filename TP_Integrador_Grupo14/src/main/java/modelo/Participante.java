package modelo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Participante implements Comparable<Participante>{

	private String nombre;
	private int puntajeTotal;
	private int[] puntajePorFase;
	private int[] puntajePorRonda;
	
	public Participante(String nombre, int puntajeTotal) {
		this.setNombre(nombre);
		this.setPuntajeTotal(puntajeTotal);
		this.puntajePorFase = new int[2];
		this.puntajePorRonda = new int[5];
		
	}
	
	public void sumarPuntoFase(int punt, int pos) {
		this.puntajePorFase[pos - 1] += punt ;
	}
	
	public void sumarPuntoRonda(int punt, int pos) {
		this.puntajePorRonda[pos - 1] += punt ;
	}
	
	 @Override
     public int compareTo(Participante p) {
        String a = new String(String.valueOf(this.getPuntajeTotal()));
        String b = new String(String.valueOf(p.getPuntajeTotal()));
        return a.compareTo(b);
    }
	
}
