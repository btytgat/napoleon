package eu.comexis.napoleon.shared.model;

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Id;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Indexed;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.annotation.Unindexed;

/**
 * Une des parties d'un contract de bail. C'est la classe de base étendue par la classe proprio
 * (Owner) et locataire (Tenant)
 * 
 * @author xavier
 * 
 */
@Unindexed
public abstract class Party implements EnablableEntity, IsSerializable, Identifiable, HasFiles {

  private String bankAccountNumber;
  private String bic;

  private String city;
  @Parent
  private Key<Company> company;
  private String country;
  private Date dateOfBirth;
  private String email;
  private String fax;

  private String firstName;
  private String iban;
  @Id
  private String id;
  private String jobTitle;
  private String lastName;
  private MaritalStatus maritalStatus;
  private MatrimonialRegime matrimonialRegime;
  private String mobilePhoneNumber;
  private String nationality;
  private String nationalRegisterNumber;
  private String vatNumber;
  private String phoneNumber;
  private String placeOfBirth;
  private String postalCode;
  private String status;
  private String street;
  private String number;
  private String box;
  private Title title;  
  private EntityStatus entityStatus;

  @Indexed
  private Boolean flagActivated;

  @Embedded
  private ArrayList<FileDescriptor> files;

  public Party() {
    files = new ArrayList<FileDescriptor>();
  }

  @Override
  public void addFile(FileDescriptor file) {
    files.add(file);
  }

  public String getBankAccountNumber() {
    return bankAccountNumber;
  }

  public String getBic() {
    return bic;
  }

  public String getBIC() {
    return bic;
  }

  public String getBox() {
    return box;
  }

  public String getCity() {
    return city;
  }

  public Key<Company> getCompany() {
    return company;
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

  @Override
  public ArrayList<FileDescriptor> getFiles() {
    return files;
  }

  public String getFirstName() {
    return firstName;
  }

  public Boolean getFlagActivated() {
    return flagActivated;
  }

  public String getIban() {
    return iban;
  }

  public String getIBAN() {
    return iban;
  }

  public String getId() {
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
  
  public String getVatNumber() {
	    return vatNumber;
  }

  public String getNumber() {
    return number;
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

  public Title getTitle() {
    return title;
  }

  @Override
  public void removeFile(FileDescriptor file) {
    files.remove(file);
  }

  public void setBankAccountNumber(String value) {
    bankAccountNumber = value;
  }

  public void setBic(String bic) {
    this.bic = bic;
  }

  public void setBIC(String value) {
    bic = value;
  }

  public void setBox(String box) {
    this.box = box;
  }

  public void setCity(String value) {
    city = value;
  }

  public void setCompany(Key<Company> company) {
    this.company = company;
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

  public void setFiles(ArrayList<FileDescriptor> files) {
    this.files = files;
  }

  public void setFirstName(String value) {
    firstName = value;
  }

  public void setFlagActivated(Boolean flagActivated) {
    this.flagActivated = flagActivated;
  }

  public void setIban(String iban) {
    this.iban = iban;
  }

  public void setIBAN(String value) {
    iban = value;
  }

  public void setId(String id) {
    this.id = id;
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
  
  public void setVatNumber(String value) {
	    vatNumber = value;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public void setPhoneNumber(String value) {
    phoneNumber = value;
  }

  public void setPlaceOfBirth(String value) {
    placeOfBirth = value;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public void setStatus(String value) {
    status = value;
  }

  public void setStreet(String value) {
    street = value;
  }

  public void setTitle(Title value) {
    title = value;
  }  

  public EntityStatus getEntityStatus() {
	return entityStatus;
  }

  public void setEntityStatus(EntityStatus entityStatus) {
	this.entityStatus = entityStatus;
  }

  public String getFullAddressLine(){
    return this.street + ", " + this.number + (this.box!=null && !this.box.isEmpty()? " bte " + this.box:"");
  }


}
