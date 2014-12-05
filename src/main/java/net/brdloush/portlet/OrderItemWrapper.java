package net.brdloush.portlet;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@XmlRootElement(name="order")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderItemWrapper implements Serializable {

	public OrderItemWrapper() {
	}
	
	 @XmlElement
	  protected Long id;

	  
	  @XmlElement
	  protected String name;

	  @XmlElement
	  protected Integer quantity;

	  @XmlElement
	  protected MoneyWrapper retailPrice;

	  @XmlElement
	  protected MoneyWrapper salePrice;

	  @XmlElement
	  protected Long orderId;

	  @XmlElement
	  protected Long categoryId;

	  @XmlElement
	  protected Long skuId;

	  @XmlElement
	  protected Long productId;

	  @XmlElement
	  protected Boolean isBundle;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public MoneyWrapper getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(MoneyWrapper retailPrice) {
		this.retailPrice = retailPrice;
	}

	public MoneyWrapper getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(MoneyWrapper salePrice) {
		this.salePrice = salePrice;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Boolean getIsBundle() {
		return isBundle;
	}

	public void setIsBundle(Boolean isBundle) {
		this.isBundle = isBundle;
	}
	  
	  
	
}
