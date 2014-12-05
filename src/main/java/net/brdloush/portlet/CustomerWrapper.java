package net.brdloush.portlet;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="customer")
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomerWrapper implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6091754938586991637L;

	public CustomerWrapper() {
	}
	
	@XmlElement
	  protected Long id;

	  @XmlElement
	  protected String firstName;

	  @XmlElement
	  protected String lastName;

	  @XmlElement
	  protected String emailAddress;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	  
	  
	  
}


