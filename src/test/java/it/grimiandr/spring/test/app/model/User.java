package it.grimiandr.spring.test.app.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

/**
 * 
 * @author andre
 *
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {

	private static final long serialVersionUID = 6483232191109895301L;

	private Integer id;

	private String first_name;

	private String last_name;

	private String email;

	private String phone;

	private String dob;

	private String address;

	private String address_post_code;

	private String address_city;

	private String country;

	private String address_state;

	private String address_country;

	private String vin;

	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress_post_code() {
		return address_post_code;
	}

	public void setAddress_post_code(String address_post_code) {
		this.address_post_code = address_post_code;
	}

	public String getAddress_city() {
		return address_city;
	}

	public void setAddress_city(String address_city) {
		this.address_city = address_city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getAddress_state() {
		return address_state;
	}

	public void setAddress_state(String address_state) {
		this.address_state = address_state;
	}

	public String getAddress_country() {
		return address_country;
	}

	public void setAddress_country(String address_country) {
		this.address_country = address_country;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
