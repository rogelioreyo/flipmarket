package mx.com.agurno.flipmarket.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * MarketLog - MarketLog.java
 *
 * @author Rogelio Reyo Cachu
 * @version 1.0.0
 * @since 10/12/2019
 */
@Entity
@Table(name = "marketlog")
public class MarketLog {

	/** The event id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_market_log")
	private Long idMarketLog;
	
	/** The event */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_item", nullable = false, updatable = true, insertable = true)
	private Item item;

	/** The country Code. */
	@Column(name = "item_id", unique = false, nullable = false)
	@JsonProperty(value = "item_id")
	private String itemId;

	/** The country Code. */
	@Column(name = "city_name", unique = false, nullable = false)
	@JsonProperty(value = "city_name")
	private String cityName;

	/** The allow email updates. */
	@Column(name = "quality")
	private Integer quality;

	/** The allow email updates. */
	@Column(name = "sell_price_min")
	private Integer sellPriceMin;
	
	/** The created at. */
    @NotNull
    @Column(name = "sell_price_min_date")
    @DateTimeFormat(style = "S-")
    private Date sellPriceMinDate;

	/** The allow email updates. */
	@Column(name = "sell_price_max")
	private Integer sellPriceMax;
	
	/** The created at. */
    @NotNull
    @Column(name = "sell_price_max_date")
    @DateTimeFormat(style = "S-")
    private Date sellPriceMaxDate;

	/** The allow email updates. */
	@Column(name = "buy_price_min")
	private Integer buyPriceMin;
	
	/** The created at. */
    @NotNull
    @Column(name = "buy_price_min_date")
    @DateTimeFormat(style = "S-")
    private Date buyPriceMinDate;

	/** The allow email updates. */
	@Column(name = "buy_price_max")
	private Integer buyPriceMax;
	
	/** The created at. */
    @NotNull
    @Column(name = "buy_price_max_date")
    @DateTimeFormat(style = "S-")
    private Date buyPriceMaxDate;
	
	/** The created at. */
    @NotNull
    @Column(name = "created_at")
    @DateTimeFormat(style = "S-")
    private Date createdAt;
	
	/** The event */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_city", nullable = false, updatable = true, insertable = true)
	private City city;

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Long getIdMarketLog() {
		return idMarketLog;
	}

	public void setIdMarketLog(Long idMarketLog) {
		this.idMarketLog = idMarketLog;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}

	public Integer getSellPriceMin() {
		return sellPriceMin;
	}

	public void setSellPriceMin(Integer sellPriceMin) {
		this.sellPriceMin = sellPriceMin;
	}

	public Date getSellPriceMinDate() {
		return sellPriceMinDate;
	}

	public void setSellPriceMinDate(Date sellPriceMinDate) {
		this.sellPriceMinDate = sellPriceMinDate;
	}

	public Integer getSellPriceMax() {
		return sellPriceMax;
	}

	public void setSellPriceMax(Integer sellPriceMax) {
		this.sellPriceMax = sellPriceMax;
	}

	public Date getSellPriceMaxDate() {
		return sellPriceMaxDate;
	}

	public void setSellPriceMaxDate(Date sellPriceMaxDate) {
		this.sellPriceMaxDate = sellPriceMaxDate;
	}

	public Integer getBuyPriceMin() {
		return buyPriceMin;
	}

	public void setBuyPriceMin(Integer buyPriceMin) {
		this.buyPriceMin = buyPriceMin;
	}

	public Date getBuyPriceMinDate() {
		return buyPriceMinDate;
	}

	public void setBuyPriceMinDate(Date buyPriceMinDate) {
		this.buyPriceMinDate = buyPriceMinDate;
	}

	public Integer getBuyPriceMax() {
		return buyPriceMax;
	}

	public void setBuyPriceMax(Integer buyPriceMax) {
		this.buyPriceMax = buyPriceMax;
	}

	public Date getBuyPriceMaxDate() {
		return buyPriceMaxDate;
	}

	public void setBuyPriceMaxDate(Date buyPriceMaxDate) {
		this.buyPriceMaxDate = buyPriceMaxDate;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
}
