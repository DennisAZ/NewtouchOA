package yh.core.data;

public class YHOrganization {
  private int sqlId;
  private String unitName;
  private Long telephone;
  private String max;
  private String postcode;
  private String address;
  private String website;
  private String email;
  private String signInUser;
  private String account;
  
  public int getSqlId() {
    return sqlId;
  }
  
  public void setSqlId(int sqlId) {
    this.sqlId = sqlId;
  }
  
  public String getUnitName() {
    return unitName;
  }
  
  public void setUnitName(String unitName) {
    this.unitName = unitName;
  }
  
  public String getMax() {
    return max;
  }
  
  public void setMax(String max) {
    this.max = max;
  }
  
  public String getPostcode() {
    return postcode;
  }
  
  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }
  
  public String getAddress() {
    return address;
  }
  
  public void setAddress(String address) {
    this.address = address;
  }
  
  public String getWebsite() {
    return website;
  }
  
  public void setWebsite(String website) {
    this.website = website;
  }
  
  public String getEmail() {
    return email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
  public String getSignInUser() {
    return signInUser;
  }
  
  public void setSignInUser(String signInUser) {
    this.signInUser = signInUser;
  }
  
  public String getAccount() {
    return account;
  }
  
  public void setAccount(String account) {
    this.account = account;
  }
  
  public Long getTelephone() {
    return telephone;
  }
  
  public void setTelephone(Long telephone) {
    this.telephone = telephone;
  }
}
