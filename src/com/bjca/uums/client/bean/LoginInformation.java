/**
 * LoginInformation.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bjca.uums.client.bean;

public class LoginInformation implements java.io.Serializable {
  private java.lang.String loginName;

  private java.lang.String loginNickName;

  private java.lang.String loginPwd;

  private java.lang.String loginType;

  public LoginInformation() {
  }

  
  public LoginInformation(java.lang.String loginName,
      java.lang.String loginNickName, java.lang.String loginPwd,
      java.lang.String loginType) {
    this.loginName = loginName;
    this.loginNickName = loginNickName;
    this.loginPwd = loginPwd;
    this.loginType = loginType;
  }

  /**
   * Gets the loginName value for this LoginInformation.
   * 
   * @return loginName
   */
  public java.lang.String getLoginName() {
    return loginName;
  }

  /**
   * Sets the loginName value for this LoginInformation.
   * 
   * @param loginName
   */
  public void setLoginName(java.lang.String loginName) {
    this.loginName = loginName;
  }

  /**
   * Gets the loginNickName value for this LoginInformation.
   * 
   * @return loginNickName
   */
  public java.lang.String getLoginNickName() {
    return loginNickName;
  }

  /**
   * Sets the loginNickName value for this LoginInformation.
   * 
   * @param loginNickName
   */
  public void setLoginNickName(java.lang.String loginNickName) {
    this.loginNickName = loginNickName;
  }

  /**
   * Gets the loginPwd value for this LoginInformation.
   * 
   * @return loginPwd
   */
  public java.lang.String getLoginPwd() {
    return loginPwd;
  }

  /**
   * Sets the loginPwd value for this LoginInformation.
   * 
   * @param loginPwd
   */
  public void setLoginPwd(java.lang.String loginPwd) {
    this.loginPwd = loginPwd;
  }

  /**
   * Gets the loginType value for this LoginInformation.
   * 
   * @return loginType
   */
  public java.lang.String getLoginType() {
    return loginType;
  }

  /**
   * Sets the loginType value for this LoginInformation.
   * 
   * @param loginType
   */
  public void setLoginType(java.lang.String loginType) {
    this.loginType = loginType;
  }

  private java.lang.Object __equalsCalc = null;

  public synchronized boolean equals(java.lang.Object obj) {
    if (!(obj instanceof LoginInformation))
      return false;
    LoginInformation other = (LoginInformation) obj;
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
        && ((this.loginName == null && other.getLoginName() == null) || (this.loginName != null && this.loginName
            .equals(other.getLoginName())))
        && ((this.loginNickName == null && other.getLoginNickName() == null) || (this.loginNickName != null && this.loginNickName
            .equals(other.getLoginNickName())))
        && ((this.loginPwd == null && other.getLoginPwd() == null) || (this.loginPwd != null && this.loginPwd
            .equals(other.getLoginPwd())))
        && ((this.loginType == null && other.getLoginType() == null) || (this.loginType != null && this.loginType
            .equals(other.getLoginType())));
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
    if (getLoginName() != null) {
      _hashCode += getLoginName().hashCode();
    }
    if (getLoginNickName() != null) {
      _hashCode += getLoginNickName().hashCode();
    }
    if (getLoginPwd() != null) {
      _hashCode += getLoginPwd().hashCode();
    }
    if (getLoginType() != null) {
      _hashCode += getLoginType().hashCode();
    }
    __hashCodeCalc = false;
    return _hashCode;
  }

  // Type metadata
  private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
      LoginInformation.class, true);

  static {
    typeDesc.setXmlType(new javax.xml.namespace.QName(
        "http://bean.client.uums.bjca.com", "LoginInformation"));
    org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("loginName");
    elemField.setXmlName(new javax.xml.namespace.QName("", "loginName"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("loginNickName");
    elemField.setXmlName(new javax.xml.namespace.QName("", "loginNickName"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("loginPwd");
    elemField.setXmlName(new javax.xml.namespace.QName("", "loginPwd"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("loginType");
    elemField.setXmlName(new javax.xml.namespace.QName("", "loginType"));
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
