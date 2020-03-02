package mx.com.agurno.flipmarket.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "item")
public class Item {

	/** The country id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_item")
	private Long idItem;

	/** The country Code. */
	@Column(name = "item_id", unique = false, nullable = false)
	@JsonProperty(value = "item_id")
	private String itemId;
	
	/** The position Description. */
	@Column(name = "item_name", unique = false, nullable = true)
	@JsonProperty(value = "item_name")
	private String itemName;
	
	/** The event */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_item_category", nullable = false, updatable = true, insertable = true)
	private ItemCategory itemCategory;


	public ItemCategory getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(ItemCategory itemCategory) {
		this.itemCategory = itemCategory;
	}

	public Long getIdItem() {
		return idItem;
	}

	public void setIdItem(Long idItem) {
		this.idItem = idItem;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
}