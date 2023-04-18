package servicios;

import com.opencsv.bean.CsvBindByPosition;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArchivoConfiguracion {
	@CsvBindByPosition(position = 0)
    public String JDBC_DRIVER;
    @CsvBindByPosition(position = 1)
	public String DB_URL;
    @CsvBindByPosition(position = 2)
    public String USER;
    @CsvBindByPosition(position = 3)
    public String PASS;

}
