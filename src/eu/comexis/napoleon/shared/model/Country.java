package eu.comexis.napoleon.shared.model;

import javax.persistence.Id;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Indexed;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.annotation.Unindexed;
@Unindexed
public class Country implements IsSerializable {
  @Id
  private String id;
  @Parent
  private Key<Company> company;
  @Indexed
  private String name;

  public Country() {
    // TODO Auto-generated constructor stub
  }

  public Key<Company> getCompany() {
    return company;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setCompany(Key<Company> company) {
    this.company = company;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

}
