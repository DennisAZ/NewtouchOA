/**
 * UnitInformation.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bjca.uums.client.bean;

public class UnitInformation extends com.bjca.uums.client.bean.UserInformation
    implements java.io.Serializable {
  private java.lang.String departCode;

  private java.lang.String departName;

  private java.util.Collection departs;

  private java.lang.String unitAddress;

  private java.lang.String unitCode;

  private java.lang.String unitDefault1;
 
  private java.lang.String unitDefault2;

  private java.lang.String unitDefault3;

  private java.lang.String unitDefault4;

  private java.lang.String unitDefault5;

  private java.lang.String userEmail;

  private java.lang.String userMobile;

  private java.lang.String userName;

  private java.lang.String userPhone;

  private java.lang.String userPostcode;

  public UnitInformation() {
  }

  public UnitInformation(java.lang.String default1, java.lang.String uniqueid,
      java.lang.String userIdcode, java.lang.String userState,
      java.lang.String userType, java.lang.String departCode,
      java.lang.String departName, java.util.Collection departs,
      java.lang.String unitAddress, java.lang.String unitCode,
      java.lang.String unitDefault1, java.lang.String unitDefault2,
      java.lang.String unitDefault3, java.lang.String unitDefault4,
      java.lang.String unitDefault5, java.lang.String userEmail,
      java.lang.String userMobile, java.lang.String userName,
      java.lang.String userPhone, java.lang.String userPostcode) {
    super(default1, uniqueid, userIdcode, userState, userType);
    this.departCode = departCode;
    this.departName = departName;
    this.departs = departs;
    this.unitAddress = unitAddress;
    this.unitCode = unitCode;
    this.unitDefault1 = unitDefault1;
    this.unitDefault2 = unitDefault2;
    this.unitDefault3 = unitDefault3;
    this.unitDefault4 = unitDefault4;
    this.unitDefault5 = unitDefault5;
    this.userEmail = userEmail;
    this.userMobile = userMobile;
    this.userName = userName;
    this.userPhone = userPhone;
    this.userPostcode = userPostcode;
  }

  /**
   * Gets the departCode value for this UnitInformation.
   * 
   * @return departCode
   */
  public java.lang.String getDepartCode() {
    return departCode;
  }

  /**
   * Sets the departCode value for this UnitInformation.
   * 
   * @param departCode
   */
  public void setDepartCode(java.lang.String departCode) {
    this.departCode = departCode;
  }

  /**
   * Gets the departName value for this UnitInformation.
   * 
   * @return departName
   */
  public java.lang.String getDepartName() {
    return departName;
  }

  /**
   * Sets the departName value for this UnitInformation.
   * 
   * @param departName
   */
  public void setDepartName(java.lang.String departName) {
    this.departName = departName;
  }

  /**
   * Gets the departs value for this UnitInformation.
   * 
   * @return departs
   */
  public java.util.Collection getDeparts() {
    return departs;
  }

  /**
   * Sets the departs value for this UnitInformation.
   * 
   * @param departs
   */
  public void setDeparts(java.util.Collection departs) {
    this.departs = departs;
  }

  /**
   * Gets the unitAddress value for this UnitInformation.
   * 
   * @return unitAddress
   */
  public java.lang.String getUnitAddress() {
    return unitAddress;
  }

  /**
   * Sets the unitAddress value for this UnitInformation.
   * 
   * @param unitAddress
   */
  public void setUnitAddress(java.lang.String unitAddress) {
    this.unitAddress = unitAddress;
  }

  /**
   * Gets the unitCode value for this UnitInformation.
   * 
   * @return unitCode
   */
  public java.lang.String getUnitCode() {
    return unitCode;
  }

  /**
   * Sets the unitCode value for this UnitInformation.
   * 
   * @param unitCode
   */
  public void setUnitCode(java.lang.String unitCode) {
    this.unitCode = unitCode;
  }

  /**
   * Gets the unitDefault1 value for this UnitInformation.
   * 
   * @return unitDefault1
   */
  public java.lang.String getUnitDefault1() {
    return unitDefault1;
  }

  /**
   * Sets the unitDefault1 value for this UnitInformation.
   * 
   * @param unitDefault1
   */
  public void setUnitDefault1(java.lang.String unitDefault1) {
    this.unitDefault1 = unitDefault1;
  }

  /**
   * Gets the unitDefault2 value for this UnitInformation.
   * 
   * @return unitDefault2
   */
  public java.lang.String getUnitDefault2() {
    return unitDefault2;
  }

  /**
   * Sets the unitDefault2 value for this UnitInformation.
   * 
   * @param unitDefault2
   */
  public void setUnitDefault2(java.lang.String unitDefault2) {
    this.unitDefault2 = unitDefault2;
  }

  /**
   * Gets the unitDefault3 value for this UnitInformation.
   * 
   * @return unitDefault3
   */
  public java.lang.String getUnitDefault3() {
    return unitDefault3;
  }

  /**
   * Sets the unitDefault3 value for this UnitInformation.
   * 
   * @param unitDefault3
   */
  public void setUnitDefault3(java.lang.String unitDefault3) {
    this.unitDefault3 = unitDefault3;
  }

  /**
   * Gets the unitDefault4 value for this UnitInformation.
   * 
   * @return unitDefault4
   */
  public java.lang.String getUnitDefault4() {
    return unitDefault4;
  }

  /**
   * Sets the unitDefault4 value for this UnitInformation.
   * 
   * @param unitDefault4
   */
  public void setUnitDefault4(java.lang.String unitDefault4) {
    this.unitDefault4 = unitDefault4;
  }

  /**
   * Gets the unitDefault5 value for this UnitInformation.
   * 
   * @return unitDefault5
   */
  public java.lang.String getUnitDefault5() {
    return unitDefault5;
  }

  /**
   * Sets the unitDefault5 value for this UnitInformation.
   * 
   * @param unitDefault5
   */
  public void setUnitDefault5(java.lang.String unitDefault5) {
    this.unitDefault5 = unitDefault5;
  }

  /**
   * Gets the userEmail value for this UnitInformation.
   * 
   * @return userEmail
   */
  public java.lang.String getUserEmail() {
    return userEmail;
  }

  /**
   * Sets the userEmail value for this UnitInformation.
   * 
   * @param userEmail
   */
  public void setUserEmail(java.lang.String userEmail) {
    this.userEmail = userEmail;
  }

  /**
   * Gets the userMobile value for this UnitInformation.
   * 
   * @return userMobile
   */
  public java.lang.String getUserMobile() {
    return userMobile;
  }

  /**
   * Sets the userMobile value for this UnitInformation.
   * 
   * @param userMobile
   */
  public void setUserMobile(java.lang.String userMobile) {
    this.userMobile = userMobile;
  }

  /**
   * Gets the userName value for this UnitInformation.
   * 
   * @return userName
   */
  public java.lang.String getUserName() {
    return userName;
  }

  /**
   * Sets the userName value for this UnitInformation.
   * 
   * @param userName
   */
  public void setUserName(java.lang.String userName) {
    this.userName = userName;
  }

  /**
   * Gets the userPhone value for this UnitInformation.
   * 
   * @return userPhone
   */
  public java.lang.String getUserPhone() {
    return userPhone;
  }

  /**
   * Sets the userPhone value for this UnitInformation.
   * 
   * @param userPhone
   */
  public void setUserPhone(java.lang.String userPhone) {
    this.userPhone = userPhone;
  }

  /**
   * Gets the userPostcode value for this UnitInformation.
   * 
   * @return userPostcode
   */
  public java.lang.String getUserPostcode() {
    return userPostcode;
  }

  /**
   * Sets the userPostcode value for this UnitInformation.
   * 
   * @param userPostcode
   */
  public void setUserPostcode(java.lang.String userPostcode) {
    this.userPostcode = userPostcode;
  }

  private java.lang.Object __equalsCalc = null;

  public synchronized boolean equals(java.lang.Object obj) {
    if (!(obj instanceof UnitInformation))
      return false;
    UnitInformation other = (UnitInformation) obj;
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
        && ((this.departName == null && other.getDepartName() == null) || (this.departName != null && this.departName
            .equals(other.getDepartName())))
        && ((this.departs == null && other.getDeparts() == null) || (this.departs != null && this.departs
            .equals(other.getDeparts())))
        && ((this.unitAddress == null && other.getUnitAddress() == null) || (this.unitAddress != null && this.unitAddress
            .equals(other.getUnitAddress())))
        && ((this.unitCode == null && other.getUnitCode() == null) || (this.unitCode != null && this.unitCode
            .equals(other.getUnitCode())))
        && ((this.unitDefault1 == null && other.getUnitDefault1() == null) || (this.unitDefault1 != null && this.unitDefault1
            .equals(other.getUnitDefault1())))
        && ((this.unitDefault2 == null && other.getUnitDefault2() == null) || (this.unitDefault2 != null && this.unitDefault2
            .equals(other.getUnitDefault2())))
        && ((this.unitDefault3 == null && other.getUnitDefault3() == null) || (this.unitDefault3 != null && this.unitDefault3
            .equals(other.getUnitDefault3())))
        && ((this.unitDefault4 == null && other.getUnitDefault4() == null) || (this.unitDefault4 != null && this.unitDefault4
            .equals(other.getUnitDefault4())))
        && ((this.unitDefault5 == null && other.getUnitDefault5() == null) || (this.unitDefault5 != null && this.unitDefault5
            .equals(other.getUnitDefault5())))
        && ((this.userEmail == null && other.getUserEmail() == null) || (this.userEmail != null && this.userEmail
            .equals(other.getUserEmail())))
        && ((this.userMobile == null && other.getUserMobile() == null) || (this.userMobile != null && this.userMobile
            .equals(other.getUserMobile())))
        && ((this.userName == null && other.getUserName() == null) || (this.userName != null && this.userName
            .equals(other.getUserName())))
        && ((this.userPhone == null && other.getUserPhone() == null) || (this.userPhone != null && this.userPhone
            .equals(other.getUserPhone())))
        && ((this.userPostcode == null && other.getUserPostcode() == null) || (this.userPostcode != null && this.userPostcode
            .equals(other.getUserPostcode())));
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
    if (getDepartName() != null) {
      _hashCode += getDepartName().hashCode();
    }
    if (getDeparts() != null) {
      _hashCode += getDeparts().hashCode();
    }
    if (getUnitAddress() != null) {
      _hashCode += getUnitAddress().hashCode();
    }
    if (getUnitCode() != null) {
      _hashCode += getUnitCode().hashCode();
    }
    if (getUnitDefault1() != null) {
      _hashCode += getUnitDefault1().hashCode();
    }
    if (getUnitDefault2() != null) {
      _hashCode += getUnitDefault2().hashCode();
    }
    if (getUnitDefault3() != null) {
      _hashCode += getUnitDefault3().hashCode();
    }
    if (getUnitDefault4() != null) {
      _hashCode += getUnitDefault4().hashCode();
    }
    if (getUnitDefault5() != null) {
      _hashCode += getUnitDefault5().hashCode();
    }
    if (getUserEmail() != null) {
      _hashCode += getUserEmail().hashCode();
    }
    if (getUserMobile() != null) {
      _hashCode += getUserMobile().hashCode();
    }
    if (getUserName() != null) {
      _hashCode += getUserName().hashCode();
    }
    if (getUserPhone() != null) {
      _hashCode += getUserPhone().hashCode();
    }
    if (getUserPostcode() != null) {
      _hashCode += getUserPostcode().hashCode();
    }
    __hashCodeCalc = false;
    return _hashCode;
  }

  // Type metadata
  private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
      UnitInformation.class, true);

  static {
    typeDesc.setXmlType(new javax.xml.namespace.QName(
        "http://bean.client.uums.bjca.com", "UnitInformation"));
    org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("departCode");
    elemField.setXmlName(new javax.xml.namespace.QName("", "departCode"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("departName");
    elemField.setXmlName(new javax.xml.namespace.QName("", "departName"));
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
    elemField.setFieldName("unitAddress");
    elemField.setXmlName(new javax.xml.namespace.QName("", "unitAddress"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("unitCode");
    elemField.setXmlName(new javax.xml.namespace.QName("", "unitCode"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("unitDefault1");
    elemField.setXmlName(new javax.xml.namespace.QName("", "unitDefault1"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("unitDefault2");
    elemField.setXmlName(new javax.xml.namespace.QName("", "unitDefault2"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("unitDefault3");
    elemField.setXmlName(new javax.xml.namespace.QName("", "unitDefault3"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("unitDefault4");
    elemField.setXmlName(new javax.xml.namespace.QName("", "unitDefault4"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("unitDefault5");
    elemField.setXmlName(new javax.xml.namespace.QName("", "unitDefault5"));
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
