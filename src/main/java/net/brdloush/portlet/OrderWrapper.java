package net.brdloush.portlet;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;



@XmlRootElement(name="order")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderWrapper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4252158049603766808L;

	public OrderWrapper() {
	}
	
	 @XmlElement
	  protected Long id;

	  @XmlElement
	  protected String status;

	  @XmlElement
	  protected MoneyWrapper totalTax;

	  @XmlElement
	  protected MoneyWrapper totalShipping;

	  @XmlElement
	  protected MoneyWrapper subTotal;

	  @XmlElement
	  protected MoneyWrapper total;
	 
	  @XmlElement
	  protected CustomerWrapper customer;

	private List<OrderItemWrapper> orderItems;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public MoneyWrapper getTotalTax() {
		return totalTax;
	}

	public void setTotalTax(MoneyWrapper totalTax) {
		this.totalTax = totalTax;
	}

	public MoneyWrapper getTotalShipping() {
		return totalShipping;
	}

	public void setTotalShipping(MoneyWrapper totalShipping) {
		this.totalShipping = totalShipping;
	}

	public MoneyWrapper getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(MoneyWrapper subTotal) {
		this.subTotal = subTotal;
	}

	public MoneyWrapper getTotal() {
		return total;
	}

	public void setTotal(MoneyWrapper total) {
		this.total = total;
	}

	public CustomerWrapper getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerWrapper customer) {
		this.customer = customer;
	}

	public List<OrderItemWrapper> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItemWrapper> orderItems) {
		this.orderItems = orderItems;
	}
	
	  
	  
}
