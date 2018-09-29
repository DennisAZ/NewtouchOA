/**
 * RoleInformation.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bjca.uums.client.bean;

public class RoleInformation implements java.io.Serializable {
  private java.lang.String urFlowno;

  private java.lang.String userRoleCode;

  private java.lang.String userRoleDescribe;

  private java.lang.String userRoleName;

  private java.lang.String userRoleState;
 
  public RoleInformation() {
  }

  public RoleInformation(java.lang.String urFlowno,
      java.lang.String userRoleCode, java.lang.String userRoleDescribe,
      java.lang.String userRoleName, java.lang.String userRoleState) {
    this.urFlowno = urFlowno;
    this.userRoleCode = userRoleCode;
    this.userRoleDescribe = userRoleDescribe;
    this.userRoleName = userRoleName;
    this.userRoleState = userRoleState;
  }

  /**
   * Gets the urFlowno value for this RoleInformation.
   * 
   * @return urFlowno
   */
  public java.lang.String getUrFlowno() {
    return urFlowno;
  }

  /**
   * Sets the urFlowno value for this RoleInformation.
   * 
   * @param urFlowno
   */
  public void setUrFlowno(java.lang.String urFlowno) {
    this.urFlowno = urFlowno;
  }

  /**
   * Gets the userRoleCode value for this RoleInformation.
   * 
   * @return userRoleCode
   */
  public java.lang.String getUserRoleCode() {
    return userRoleCode;
  }

  /**
   * Sets the userRoleCode value for this RoleInformation.
   * 
   * @param userRoleCode
   */
  public void setUserRoleCode(java.lang.String userRoleCode) {
    this.userRoleCode = userRoleCode;
  }

  /**
   * Gets the userRoleDescribe value for this RoleInformation.
   * 
   * @return userRoleDescribe
   */
  public java.lang.String getUserRoleDescribe() {
    return userRoleDescribe;
  }

  /**
   * Sets the userRoleDescribe value for this RoleInformation.
   * 
   * @param userRoleDescribe
   */
  public void setUserRoleDescribe(java.lang.String userRoleDescribe) {
    this.userRoleDescribe = userRoleDescribe;
  }

  /**
   * Gets the userRoleName value for this RoleInformation.
   * 
   * @return userRoleName
   */
  public java.lang.String getUserRoleName() {
    return userRoleName;
  }

  /**
   * Sets the userRoleName value for this RoleInformation.
   * 
   * @param userRoleName
   */
  public void setUserRoleName(java.lang.String userRoleName) {
    this.userRoleName = userRoleName;
  }

  /**
   * Gets the userRoleState value for this RoleInformation.
   * 
   * @return userRoleState
   */
  public java.lang.String getUserRoleState() {
    return userRoleState;
  }

  /**
   * Sets the userRoleState value for this RoleInformation.
   * 
   * @param userRoleState
   */
  public void setUserRoleState(java.lang.String userRoleState) {
    this.userRoleState = userRoleState;
  }

  private java.lang.Object __equalsCalc = null;

  public synchronized boolean equals(java.lang.Object obj) {
    if (!(obj instanceof RoleInformation))
      return false;
    RoleInformation other = (RoleInformation) obj;
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
        && ((this.urFlowno == null && other.getUrFlowno() == null) || (this.urFlowno != null && this.urFlowno
            .equals(other.getUrFlowno())))
        && ((this.userRoleCode == null && other.getUserRoleCode() == null) || (this.userRoleCode != null && this.userRoleCode
            .equals(other.getUserRoleCode())))
        && ((this.userRoleDescribe == null && other.getUserRoleDescribe() == null) || (this.userRoleDescribe != null && this.userRoleDescribe
            .equals(other.getUserRoleDescribe())))
        && ((this.userRoleName == null && other.getUserRoleName() == null) || (this.userRoleName != null && this.userRoleName
            .equals(other.getUserRoleName())))
        && ((this.userRoleState == null && other.getUserRoleState() == null) || (this.userRoleState != null && this.userRoleState
            .equals(other.getUserRoleState())));
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
    if (getUrFlowno() != null) {
      _hashCode += getUrFlowno().hashCode();
    }
    if (getUserRoleCode() != null) {
      _hashCode += getUserRoleCode().hashCode();
    }
    if (getUserRoleDescribe() != null) {
      _hashCode += getUserRoleDescribe().hashCode();
    }
    if (getUserRoleName() != null) {
      _hashCode += getUserRoleName().hashCode();
    }
    if (getUserRoleState() != null) {
      _hashCode += getUserRoleState().hashCode();
    }
    __hashCodeCalc = false;
    return _hashCode;
  }

  // Type metadata
  private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
      RoleInformation.class, true);

  static {
    typeDesc.setXmlType(new javax.xml.namespace.QName(
        "http://bean.client.uums.bjca.com", "RoleInformation"));
    org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("urFlowno");
    elemField.setXmlName(new javax.xml.namespace.QName("", "urFlowno"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("userRoleCode");
    elemField.setXmlName(new javax.xml.namespace.QName("", "userRoleCode"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("userRoleDescribe");
    elemField.setXmlName(new javax.xml.namespace.QName("", "userRoleDescribe"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("userRoleName");
    elemField.setXmlName(new javax.xml.namespace.QName("", "userRoleName"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("userRoleState");
    elemField.setXmlName(new javax.xml.namespace.QName("", "userRoleState"));
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
