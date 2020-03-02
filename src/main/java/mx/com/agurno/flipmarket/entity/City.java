package mx.com.agurno.flipmarket.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "city")
public class City {

	/** The country id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_city")
	private Long idCity;
	
	/** The position Description. */
	@Column(name = "city_name", unique = false, nullable = true)
	@JsonProperty(value = "city_name")
	private String cityName;

	public Long getIdCity() {
		return idCity;
	}

	public void setIdCity(Long idCity) {
		this.idCity = idCity;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	
	
}