/**
 * UserInformation.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bjca.uums.client.bean;

public class UserInformation implements java.io.Serializable {
  private java.lang.String default1;

  private java.lang.String uniqueid;

  private java.lang.String userIdcode;

  private java.lang.String userState;

  private java.lang.String userType;

  public UserInformation() {
   }

  @Override
  public String toString() {
    return "UserInformation [__equalsCalc=" + __equalsCalc
        + ", __hashCodeCalc=" + __hashCodeCalc + ", default1=" + default1
        + ", uniqueid=" + uniqueid + ", userIdcode=" + userIdcode
        + ", userState=" + userState + ", userType=" + userType + "]";
  }

  public UserInformation(java.lang.String default1, java.lang.String uniqueid,
      java.lang.String userIdcode, java.lang.String userState,
      java.lang.String userType) {
    this.default1 = default1;
    this.uniqueid = uniqueid;
    this.userIdcode = userIdcode;
    this.userState = userState;
    this.userType = userType;
  }

  /**
   * Gets the default1 value for this UserInformation.
   * 
   * @return default1
   */
  public java.lang.String getDefault1() {
    return default1;
  }

  /**
   * Sets the default1 value for this UserInformation.
   * 
   * @param default1
   */
  public void setDefault1(java.lang.String default1) {
    this.default1 = default1;
  }

  /**
   * Gets the uniqueid value for this UserInformation.
   * 
   * @return uniqueid
   */
  public java.lang.String getUniqueid() {
    return uniqueid;
  }

  /**
   * Sets the uniqueid value for this UserInformation.
   * 
   * @param uniqueid
   */
  public void setUniqueid(java.lang.String uniqueid) {
    this.uniqueid = uniqueid;
  }

  /**
   * Gets the userIdcode value for this UserInformation.
   * 
   * @return userIdcode
   */
  public java.lang.String getUserIdcode() {
    return userIdcode;
  }

  /**
   * Sets the userIdcode value for this UserInformation.
   * 
   * @param userIdcode
   */
  public void setUserIdcode(java.lang.String userIdcode) {
    this.userIdcode = userIdcode;
  }

  /**
   * Gets the userState value for this UserInformation.
   * 
   * @return userState
   */
  public java.lang.String getUserState() {
    return userState;
  }

  /**
   * Sets the userState value for this UserInformation.
   * 
   * @param userState
   */
  public void setUserState(java.lang.String userState) {
    this.userState = userState;
  }

  /**
   * Gets the userType value for this UserInformation.
   * 
   * @return userType
   */
  public java.lang.String getUserType() {
    return userType;
  }

  /**
   * Sets the userType value for this UserInformation.
   * 
   * @param userType
   */
  public void setUserType(java.lang.String userType) {
    this.userType = userType;
  }

  private java.lang.Object __equalsCalc = null;

  public synchronized boolean equals(java.lang.Object obj) {
    if (!(obj instanceof UserInformation))
      return false;
    UserInformation other = (UserInformation) obj;
    if (obj == null)
      return false;
    if (this == obj)
      return true;
    if (__equalsCalc != null) {
      return (__equalsCalc == obj);
    }
    __equalsCalc = obj;
    boolean _equals;
    _equals = true
        && ((this.default1 == null && other.getDefault1() == null) || (this.default1 != null && this.default1
            .equals(other.getDefault1())))
        && ((this.uniqueid == null && other.getUniqueid() == null) || (this.uniqueid != null && this.uniqueid
            .equals(other.getUniqueid())))
        && ((this.userIdcode == null && other.getUserIdcode() == null) || (this.userIdcode != null && this.userIdcode
            .equals(other.getUserIdcode())))
        && ((this.userState == null && other.getUserState() == null) || (this.userState != null && this.userState
            .equals(other.getUserState())))
        && ((this.userType == null && other.getUserType() == null) || (this.userType != null && this.userType
            .equals(other.getUserType())));
    __equalsCalc = null;
    return _equals;
  }

  private boolean __hashCodeCalc = false;

  public synchronized int hashCode() {
    if (__hashCodeCalc) {
      return 0;
    }
    __hashCodeCalc = true;
    int _hashCode = 1;
    if (getDefault1() != null) {
      _hashCode += getDefault1().hashCode();
    }
    if (getUniqueid() != null) {
      _hashCode += getUniqueid().hashCode();
    }
    if (getUserIdcode() != null) {
      _hashCode += getUserIdcode().hashCode();
    }
    if (getUserState() != null) {
      _hashCode += getUserState().hashCode();
    }
    if (getUserType() != null) {
      _hashCode += getUserType().hashCode();
    }
    __hashCodeCalc = false;
    return _hashCode;
  }

  // Type metadata
  private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
      UserInformation.class, true);

  static {
    typeDesc.setXmlType(new javax.xml.namespace.QName(
        "http://bean.client.uums.bjca.com", "UserInformation"));
    org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("default1");
    elemField.setXmlName(new javax.xml.namespace.QName("", "default1"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("uniqueid");
    elemField.setXmlName(new javax.xml.namespace.QName("", "uniqueid"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("userIdcode");
    elemField.setXmlName(new javax.xml.namespace.QName("", "userIdcode"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("userState");
    elemField.setXmlName(new javax.xml.namespace.QName("", "userState"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("userType");
    elemField.setXmlName(new javax.xml.namespace.QName("", "userType"));
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
