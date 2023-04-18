package servicios;

import com.opencsv.bean.CsvBindByPosition;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArchivoResultados {
	@CsvBindByPosition(position = 0)
    private Integer fase;
    @CsvBindByPosition(position = 1)
    private Integer ronda;
    @CsvBindByPosition(position = 2)
    private String equipo1;
    @CsvBindByPosition(position = 3)
    private Integer cantGoles1;
    @CsvBindByPosition(position = 4)
    private Integer cantGoles2;
    @CsvBindByPosition(position = 5)
    private String equipo2;

}
