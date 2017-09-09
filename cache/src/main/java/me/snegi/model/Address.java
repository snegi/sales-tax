package me.snegi.model;

/**
 */
public class Address {

	public final Integer number;
	public final String streetPrefix;
	public final String street;
	public final String city;
	public final String state;
	private final Integer zipCode;

	public Address(
		Integer number,
		String streetPrefix,
		String street,
		String city,
		String state,
		Integer zipCode
	) {

		//Preconditions... validate

		this.number			= number;
		this.streetPrefix 	= streetPrefix;
		this.street			= street;
		this.city			= city;
		this.state			= state;
		this.zipCode		= zipCode;
	}

	public Integer getNumber() {
		return number;
	}

	public String getStreetPrefix() {
		return streetPrefix;
	}

	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public Integer getZipCode() {
		return zipCode;
	}

	public String toString() {
		return
			""
				+number
				+ " "
				+ streetPrefix
				+ " "
				+ street
				+ ", "
				+ city
				+ ", "
				+ state
				+ ", "
				+zipCode;
	}
}
