/**
 * PersonInformation.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bjca.uums.client.bean;

public class PersonInformation extends
    com.bjca.uums.client.bean.UserInformation implements java.io.Serializable {
  private java.lang.String departCode;

  private java.util.Collection departs;

  private java.lang.String userAddress;
 
  private java.lang.String userCertType;

  private java.lang.String userDefault1;

  private java.lang.String userDefault2;

  private java.lang.String userDefault3;

  private java.lang.String userDefault4;

  private java.lang.String userDefault5;

  private java.lang.String userDefault6;

  private java.lang.String userDegree;

  private java.lang.String userDuty;

  private java.lang.String userEmail;

  private java.lang.String userIdcardNum;

  private java.lang.String userMobile;

  private java.lang.String userName;

  private java.lang.String userNation;

  private java.lang.String userPhone;

  private java.lang.String userPostcode;

  private java.lang.String userTitle;

  public PersonInformation() {
  }

  @Override
  public String toString() {
    return "PersonInformation [__equalsCalc=" + __equalsCalc
        + ", __hashCodeCalc=" + __hashCodeCalc + ", departCode=" + departCode
        + ", departs=" + departs + ", userAddress=" + userAddress
        + ", userCertType=" + userCertType + ", userDefault1=" + userDefault1
        + ", userDefault2=" + userDefault2 + ", userDefault3=" + userDefault3
        + ", userDefault4=" + userDefault4 + ", userDefault5=" + userDefault5
        + ", userDefault6=" + userDefault6 + ", userDegree=" + userDegree
        + ", userDuty=" + userDuty + ", userEmail=" + userEmail
        + ", userIdcardNum=" + userIdcardNum + ", userMobile=" + userMobile
        + ", userName=" + userName + ", userNation=" + userNation
        + ", userPhone=" + userPhone + ", userPostcode=" + userPostcode
        + ", userTitle=" + userTitle + "]";
  }

  public PersonInformation(java.lang.String default1,
      java.lang.String uniqueid, java.lang.String userIdcode,
      java.lang.String userState, java.lang.String userType,
      java.lang.String departCode, java.util.Collection departs,
      java.lang.String userAddress, java.lang.String userCertType,
      java.lang.String userDefault1, java.lang.String userDefault2,
      java.lang.String userDefault3, java.lang.String userDefault4,
      java.lang.String userDefault5, java.lang.String userDefault6,
      java.lang.String userDegree, java.lang.String userDuty,
      java.lang.String userEmail, java.lang.String userIdcardNum,
      java.lang.String userMobile, java.lang.String userName,
      java.lang.String userNation, java.lang.String userPhone,
      java.lang.String userPostcode, java.lang.String userTitle) {
    super(default1, uniqueid, userIdcode, userState, userType);
    this.departCode = departCode;
    this.departs = departs;
    this.userAddress = userAddress;
    this.userCertType = userCertType;
    this.userDefault1 = userDefault1;
    this.userDefault2 = userDefault2;
    this.userDefault3 = userDefault3;
    this.userDefault4 = userDefault4;
    this.userDefault5 = userDefault5;
    this.userDefault6 = userDefault6;
    this.userDegree = userDegree;
    this.userDuty = userDuty;
    this.userEmail = userEmail;
    this.userIdcardNum = userIdcardNum;
    this.userMobile = userMobile;
    this.userName = userName;
    this.userNation = userNation;
    this.userPhone = userPhone;
    this.userPostcode = userPostcode;
    this.userTitle = userTitle;
  }

  /**
   * Gets the departCode value for this PersonInformation.
   * 
   * @return departCode
   */
  public java.lang.String getDepartCode() {
    return departCode;
  }

  /**
   * Sets the departCode value for this PersonInformation.
   * 
   * @param departCode
   */
  public void setDepartCode(java.lang.String departCode) {
    this.departCode = departCode;
  }

  /**
   * Gets the departs value for this PersonInformation.
   * 
   * @return departs
   */
  public java.util.Collection getDeparts() {
    return departs;
  }

  /**
   * Sets the departs value for this PersonInformation.
   * 
   * @param departs
   */
  public void setDeparts(java.util.Collection departs) {
    this.departs = departs;
  }

  /**
   * Gets the userAddress value for this PersonInformation.
   * 
   * @return userAddress
   */
  public java.lang.String getUserAddress() {
    return userAddress;
  }

  /**
   * Sets the userAddress value for this PersonInformation.
   * 
   * @param userAddress
   */
  public void setUserAddress(java.lang.String userAddress) {
    this.userAddress = userAddress;
  }

  /**
   * Gets the userCertType value for this PersonInformation.
   * 
   * @return userCertType
   */
  public java.lang.String getUserCertType() {
    return userCertType;
  }

  /**
   * Sets the userCertType value for this PersonInformation.
   * 
   * @param userCertType
   */
  public void setUserCertType(java.lang.String userCertType) {
    this.userCertType = userCertType;
  }

  /**
   * Gets the userDefault1 value for this PersonInformation.
   * 
   * @return userDefault1
   */
  public java.lang.String getUserDefault1() {
    return userDefault1;
  }

  /**
   * Sets the userDefault1 value for this PersonInformation.
   * 
   * @param userDefault1
   */
  public void setUserDefault1(java.lang.String userDefault1) {
    this.userDefault1 = userDefault1;
  }

  /**
   * Gets the userDefault2 value for this PersonInformation.
   * 
   * @return userDefault2
   */
  public java.lang.String getUserDefault2() {
    return userDefault2;
  }

  /**
   * Sets the userDefault2 value for this PersonInformation.
   * 
   * @param userDefault2
   */
  public void setUserDefault2(java.lang.String userDefault2) {
    this.userDefault2 = userDefault2;
  }

  /**
   * Gets the userDefault3 value for this PersonInformation.
   * 
   * @return userDefault3
   */
  public java.lang.String getUserDefault3() {
    return userDefault3;
  }

  /**
   * Sets the userDefault3 value for this PersonInformation.
   * 
   * @param userDefault3
   */
  public void setUserDefault3(java.lang.String userDefault3) {
    this.userDefault3 = userDefault3;
  }

  /**
   * Gets the userDefault4 value for this PersonInformation.
   * 
   * @return userDefault4
   */
  public java.lang.String getUserDefault4() {
    return userDefault4;
  }

  /**
   * Sets the userDefault4 value for this PersonInformation.
   * 
   * @param userDefault4
   */
  public void setUserDefault4(java.lang.String userDefault4) {
    this.userDefault4 = userDefault4;
  }

  /**
   * Gets the userDefault5 value for this PersonInformation.
   * 
   * @return userDefault5
   */
  public java.lang.String getUserDefault5() {
    return userDefault5;
  }

  /**
   * Sets the userDefault5 value for this PersonInformation.
   * 
   * @param userDefault5
   */
  public void setUserDefault5(java.lang.String userDefault5) {
    this.userDefault5 = userDefault5;
  }

  /**
   * Gets the userDefault6 value for this PersonInformation.
   * 
   * @return userDefault6
   */
  public java.lang.String getUserDefault6() {
    return userDefault6;
  }

  /**
   * Sets the userDefault6 value for this PersonInformation.
   * 
   * @param userDefault6
   */
  public void setUserDefault6(java.lang.String userDefault6) {
    this.userDefault6 = userDefault6;
  }

  /**
   * Gets the userDegree value for this PersonInformation.
   * 
   * @return userDegree
   */
  public java.lang.String getUserDegree() {
    return userDegree;
  }

  /**
   * Sets the userDegree value for this PersonInformation.
   * 
   * @param userDegree
   */
  public void setUserDegree(java.lang.String userDegree) {
    this.userDegree = userDegree;
  }

  /**
   * Gets the userDuty value for this PersonInformation.
   * 
   * @return userDuty
   */
  public java.lang.String getUserDuty() {
    return userDuty;
  }

  /**
   * Sets the userDuty value for this PersonInformation.
   * 
   * @param userDuty
   */
  public void setUserDuty(java.lang.String userDuty) {
    this.userDuty = userDuty;
  }

  /**
   * Gets the userEmail value for this PersonInformation.
   * 
   * @return userEmail
   */
  public java.lang.String getUserEmail() {
    return userEmail;
  }

  /**
   * Sets the userEmail value for this PersonInformation.
   * 
   * @param userEmail
   */
  public void setUserEmail(java.lang.String userEmail) {
    this.userEmail = userEmail;
  }

  /**
   * Gets the userIdcardNum value for this PersonInformation.
   * 
   * @return userIdcardNum
   */
  public java.lang.String getUserIdcardNum() {
    return userIdcardNum;
  }

  /**
   * Sets the userIdcardNum value for this PersonInformation.
   * 
   * @param userIdcardNum
   */
  public void setUserIdcardNum(java.lang.String userIdcardNum) {
    this.userIdcardNum = userIdcardNum;
  }

  /**
   * Gets the userMobile value for this PersonInformation.
   * 
   * @return userMobile
   */
  public java.lang.String getUserMobile() {
    return userMobile;
  }

  /**
   * Sets the userMobile value for this PersonInformation.
   * 
   * @param userMobile
   */
  public void setUserMobile(java.lang.String userMobile) {
    this.userMobile = userMobile;
  }

  /**
   * Gets the userName value for this PersonInformation.
   * 
   * @return userName
   */
  public java.lang.String getUserName() {
    return userName;
  }

  /**
   * Sets the userName value for this PersonInformation.
   * 
   * @param userName
   */
  public void setUserName(java.lang.String userName) {
    this.userName = userName;
  }

  /**
   * Gets the userNation value for this PersonInformation.
   * 
   * @return userNation
   */
  public java.lang.String getUserNation() {
    return userNation;
  }

  /**
   * Sets the userNation value for this PersonInformation.
   * 
   * @param userNation
   */
  public void setUserNation(java.lang.String userNation) {
    this.userNation = userNation;
  }

  /**
   * Gets the userPhone value for this PersonInformation.
   * 
   * @return userPhone
   */
  public java.lang.String getUserPhone() {
    return userPhone;
  }

  /**
   * Sets the userPhone value for this PersonInformation.
   * 
   * @param userPhone
   */
  public void setUserPhone(java.lang.String userPhone) {
    this.userPhone = userPhone;
  }

  /**
   * Gets the userPostcode value for this PersonInformation.
   * 
   * @return userPostcode
   */
  public java.lang.String getUserPostcode() {
    return userPostcode;
  }

  /**
   * Sets the userPostcode value for this PersonInformation.
   * 
   * @param userPostcode
   */
  public void setUserPostcode(java.lang.String userPostcode) {
    this.userPostcode = userPostcode;
  }

  /**
   * Gets the userTitle value for this PersonInformation.
   * 
   * @return userTitle
   */
  public java.lang.String getUserTitle() {
    return userTitle;
  }

  /**
   * Sets the userTitle value for this PersonInformation.
   * 
   * @param userTitle
   */
  public void setUserTitle(java.lang.String userTitle) {
    this.userTitle = userTitle;
  }

  private java.lang.Object __equalsCalc = null;

  public synchronized boolean equals(java.lang.Object obj) {
    if (!(obj instanceof PersonInformation))
      return false;
    PersonInformation other = (PersonInformation) obj;
    if (obj == null)
      return false;
    if (this == obj)
      return true;
    if (__equalsCalc != null) {
      return (__equalsCalc == obj);
    }
    __equalsCalc = obj;
    boolean _equals;
    _equals = super.equals(obj)
        && ((this.departCode == null && other.getDepartCode() == null) || (this.departCode != null && this.departCode
            .equals(other.getDepartCode())))
        && ((this.departs == null && other.getDeparts() == null) || (this.departs != null && this.departs
            .equals(other.getDeparts())))
        && ((this.userAddress == null && other.getUserAddress() == null) || (this.userAddress != null && this.userAddress
            .equals(other.getUserAddress())))
        && ((this.userCertType == null && other.getUserCertType() == null) || (this.userCertType != null && this.userCertType
            .equals(other.getUserCertType())))
        && ((this.userDefault1 == null && other.getUserDefault1() == null) || (this.userDefault1 != null && this.userDefault1
            .equals(other.getUserDefault1())))
        && ((this.userDefault2 == null && other.getUserDefault2() == null) || (this.userDefault2 != null && this.userDefault2
            .equals(other.getUserDefault2())))
        && ((this.userDefault3 == null && other.getUserDefault3() == null) || (this.userDefault3 != null && this.userDefault3
            .equals(other.getUserDefault3())))
        && ((this.userDefault4 == null && other.getUserDefault4() == null) || (this.userDefault4 != null && this.userDefault4
            .equals(other.getUserDefault4())))
        && ((this.userDefault5 == null && other.getUserDefault5() == null) || (this.userDefault5 != null && this.userDefault5
            .equals(other.getUserDefault5())))
        && ((this.userDefault6 == null && other.getUserDefault6() == null) || (this.userDefault6 != null && this.userDefault6
            .equals(other.getUserDefault6())))
        && ((this.userDegree == null && other.getUserDegree() == null) || (this.userDegree != null && this.userDegree
            .equals(other.getUserDegree())))
        && ((this.userDuty == null && other.getUserDuty() == null) || (this.userDuty != null && this.userDuty
            .equals(other.getUserDuty())))
        && ((this.userEmail == null && other.getUserEmail() == null) || (this.userEmail != null && this.userEmail
            .equals(other.getUserEmail())))
        && ((this.userIdcardNum == null && other.getUserIdcardNum() == null) || (this.userIdcardNum != null && this.userIdcardNum
            .equals(other.getUserIdcardNum())))
        && ((this.userMobile == null && other.getUserMobile() == null) || (this.userMobile != null && this.userMobile
            .equals(other.getUserMobile())))
        && ((this.userName == null && other.getUserName() == null) || (this.userName != null && this.userName
            .equals(other.getUserName())))
        && ((this.userNation == null && other.getUserNation() == null) || (this.userNation != null && this.userNation
            .equals(other.getUserNation())))
        && ((this.userPhone == null && other.getUserPhone() == null) || (this.userPhone != null && this.userPhone
            .equals(other.getUserPhone())))
        && ((this.userPostcode == null && other.getUserPostcode() == null) || (this.userPostcode != null && this.userPostcode
            .equals(other.getUserPostcode())))
        && ((this.userTitle == null && other.getUserTitle() == null) || (this.userTitle != null && this.userTitle
            .equals(other.getUserTitle())));
    __equalsCalc = null;
    return _equals;
  }

  private boolean __hashCodeCalc = false;

  public synchronized int hashCode() {
    if (__hashCodeCalc) {
      return 0;
    }
    __hashCodeCalc = true;
    int _hashCode = super.hashCode();
    if (getDepartCode() != null) {
      _hashCode += getDepartCode().hashCode();
    }
    if (getDeparts() != null) {
      _hashCode += getDeparts().hashCode();
    }
    if (getUserAddress() != null) {
      _hashCode += getUserAddress().hashCode();
    }
    if (getUserCertType() != null) {
      _hashCode += getUserCertType().hashCode();
    }
    if (getUserDefault1() != null) {
      _hashCode += getUserDefault1().hashCode();
    }
    if (getUserDefault2() != null) {
      _hashCode += getUserDefault2().hashCode();
    }
    if (getUserDefault3() != null) {
      _hashCode += getUserDefault3().hashCode();
    }
    if (getUserDefault4() != null) {
      _hashCode += getUserDefault4().hashCode();
    }
    if (getUserDefault5() != null) {
      _hashCode += getUserDefault5().hashCode();
    }
    if (getUserDefault6() != null) {
      _hashCode += getUserDefault6().hashCode();
    }
    if (getUserDegree() != null) {
      _hashCode += getUserDegree().hashCode();
    }
    if (getUserDuty() != null) {
      _hashCode += getUserDuty().hashCode();
    }
    if (getUserEmail() != null) {
      _hashCode += getUserEmail().hashCode();
    }
    if (getUserIdcardNum() != null) {
      _hashCode += getUserIdcardNum().hashCode();
    }
    if (getUserMobile() != null) {
      _hashCode += getUserMobile().hashCode();
    }
    if (getUserName() != null) {
      _hashCode += getUserName().hashCode();
    }
    if (getUserNation() != null) {
      _hashCode += getUserNation().hashCode();
    }
    if (getUserPhone() != null) {
      _hashCode += getUserPhone().hashCode();
    }
    if (getUserPostcode() != null) {
      _hashCode += getUserPostcode().hashCode();
    }
    if (getUserTitle() != null) {
      _hashCode += getUserTitle().hashCode();
    }
    __hashCodeCalc = false;
    return _hashCode;
  }

  // Type metadata
  private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
      PersonInformation.class, true);

  static {
    typeDesc.setXmlType(new javax.xml.namespace.QName(
        "http://bean.client.uums.bjca.com", "PersonInformation"));
    org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("departCode");
    elemField.setXmlName(new javax.xml.namespace.QName("", "departCode"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("departs");
    elemField.setXmlName(new javax.xml.namespace.QName("", "departs"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://util.java",
        "Collection"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("userAddress");
    elemField.setXmlName(new javax.xml.namespace.QName("", "userAddress"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("userCertType");
    elemField.setXmlName(new javax.xml.namespace.QName("", "userCertType"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("userDefault1");
    elemField.setXmlName(new javax.xml.namespace.QName("", "userDefault1"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("userDefault2");
    elemField.setXmlName(new javax.xml.namespace.QName("", "userDefault2"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("userDefault3");
    elemField.setXmlName(new javax.xml.namespace.QName("", "userDefault3"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("userDefault4");
    elemField.setXmlName(new javax.xml.namespace.QName("", "userDefault4"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("userDefault5");
    elemField.setXmlName(new javax.xml.namespace.QName("", "userDefault5"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("userDefault6");
    elemField.setXmlName(new javax.xml.namespace.QName("", "userDefault6"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("userDegree");
    elemField.setXmlName(new javax.xml.namespace.QName("", "userDegree"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("userDuty");
    elemField.setXmlName(new javax.xml.namespace.QName("", "userDuty"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("userEmail");
    elemField.setXmlName(new javax.xml.namespace.QName("", "userEmail"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("userIdcardNum");
    elemField.setXmlName(new javax.xml.namespace.QName("", "userIdcardNum"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("userMobile");
    elemField.setXmlName(new javax.xml.namespace.QName("", "userMobile"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("userName");
    elemField.setXmlName(new javax.xml.namespace.QName("", "userName"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("userNation");
    elemField.setXmlName(new javax.xml.namespace.QName("", "userNation"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("userPhone");
    elemField.setXmlName(new javax.xml.namespace.QName("", "userPhone"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("userPostcode");
    elemField.setXmlName(new javax.xml.namespace.QName("", "userPostcode"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("userTitle");
    elemField.setXmlName(new javax.xml.namespace.QName("", "userTitle"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
  }

  /**
   * Return type metadata object
   */
  public static org.apache.axis.description.TypeDesc getTypeDesc() {
    return typeDesc;
  }

  /**
   * Get Custom Serializer
   */
  public static org.apache.axis.encoding.Serializer getSerializer(
      java.lang.String mechType, java.lang.Class _javaType,
      javax.xml.namespace.QName _xmlType) {
    return new org.apache.axis.encoding.ser.BeanSerializer(_javaType, _xmlType,
        typeDesc);
  }

  /**
   * Get Custom Deserializer
   */
  public static org.apache.axis.encoding.Deserializer getDeserializer(
      java.lang.String mechType, java.lang.Class _javaType,
      javax.xml.namespace.QName _xmlType) {
    return new org.apache.axis.encoding.ser.BeanDeserializer(_javaType,
        _xmlType, typeDesc);
  }

}
