package mx.com.agurno.flipmarket.entity;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "itemcategory")
public class ItemCategory {

	/** The country id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_item_category")
	private Long idCountry;
	
	/** The position Description. */
	@Column(name = "item_category_name", unique = false, nullable = true)
	@JsonProperty(value = "item_category_name")
	private String itemCategoryName;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "itemCategory")
    private Set<Item> items = new HashSet<Item>();

	public Long getIdCountry() {
		return idCountry;
	}

	public Set<Item> getItems() {
		return items;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}

	public void setIdCountry(Long idCountry) {
		this.idCountry = idCountry;
	}

	public String getItemCategoryName() {
		return itemCategoryName;
	}

	public void setItemCategoryName(String itemCategoryName) {
		this.itemCategoryName = itemCategoryName;
	}

	
}