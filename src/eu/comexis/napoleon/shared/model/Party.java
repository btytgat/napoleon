package eu.comexis.napoleon.shared.model;

import java.util.Date;

import javax.persistence.Id;

import com.googlecode.objectify.annotation.Indexed;

/**
 * Une des parties d'un contract de bail. C'est la classe de base étendue par la
 * classe proprio (Owner) et locataire (Tenant)
 * 
 * @author xavier
 * 
 */
public abstract class Party {

	private String bankAccountNumber;
	private String bic;
	private String city;
	private String country;
	private Date dateOfBirth;
	private String email;
	private String fax;
	private String firstName;
	private String iban;
	@Id
	private Long id;
	private String jobTitle;
	@Indexed
	private String lastName;
	private MaritalStatus maritalStatus;
	private MatrimonialRegime matrimonialRegime;
	private String mobilePhoneNumber;
	private String nationality;
	private String nationalRegisterNumber;
	private String phoneNumber;
	private String placeOfBirth;
	private String postalCode;
	private String status;
	private String street;
	private String title;

	/**
	 * 
	 */
	public Party() {

	}
	
	
	// TODO is it really mandatory ???
	public Party(String name) {
		this.lastName = name;
	}

	public String getBankAccountNumber() {
		return bankAccountNumber;
	}

	public String getBIC() {
		return bic;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public String getEmail() {
		return email;
	}

	public String getFax() {
		return fax;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getIBAN() {
		return iban;
	}

	public Long getId() {
		return id;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public String getLastName() {
		return lastName;
	}

	public MaritalStatus getMaritalStatus() {
		return maritalStatus;
	}

	public MatrimonialRegime getMatrimonialRegime() {
		return matrimonialRegime;
	}
	/**
	 * Auto-increment version # whenever persisted
	 */

	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}

	public String getNationality() {
		return nationality;
	}

	public String getNationalRegisterNumber() {
		return nationalRegisterNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getPlaceOfBirth() {
		return placeOfBirth;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getStatus() {
		return status;
	}

	public String getStreet() {
		return street;
	}

	public String getTitle() {
		return title;
	}

	public void setBankAccountNumber(String value) {
		bankAccountNumber = value;
	}

	public void setBIC(String value) {
		bic = value;
	}

	public void setCity(String value) {
		city = value;
	}

	public void setCountry(String value) {
		country = value;
	}

	public void setDateOfBirth(Date value) {
		dateOfBirth = value;
	}

	public void setEmail(String value) {
		email = value;
	}

	public void setFax(String value) {
		fax = value;
	}

	public void setFirstName(String value) {
		firstName = value;
	}

	public void setIBAN(String value) {
		iban = value;
	}

	public void setJobTitle(String value) {
		jobTitle = value;
	}

	public void setLastName(String value) {
		lastName = value;
	}

	public void setMaritalStatus(MaritalStatus value) {
		maritalStatus = value;
	}

	public void setMatrimonialRegime(MatrimonialRegime value) {
		matrimonialRegime = value;
	}

	public void setMobilePhoneNumber(String value) {
		mobilePhoneNumber = value;
	}

	public void setNationality(String value) {
		nationality = value;
	}

	public void setNationalRegisterNumber(String value) {
		nationalRegisterNumber = value;
	}

	public void setPhoneNumber(String value) {
		phoneNumber = value;
	}

	public void setPlaceOfBirth(String value) {
		placeOfBirth = value;
	}

	public void setPostalCode(String value) {
		postalCode = value;
	}

	public void setStatus(String value) {
		status = value;
	}

	public void setStreet(String value) {
		street = value;
	}

	public void setTitle(String value) {
		title = value;
	}
}
