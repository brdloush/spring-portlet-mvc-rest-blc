package net.brdloush.portlet;

import java.math.BigDecimal;
import java.util.Currency;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="money")
@XmlAccessorType(XmlAccessType.FIELD)
public class MoneyWrapper {

	public MoneyWrapper() {
	}
	
	@XmlElement
	  protected BigDecimal amount;
	
    @XmlElement
    private Currency currency;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Currency getCurrency() {
		return currency;
	}

    
}
