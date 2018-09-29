/**
 * ResourceInformation.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.bjca.uums.client.bean;

public class ResourceInformation implements java.io.Serializable {
  private java.lang.String resCode;

  private java.lang.String resDesc;

  private java.lang.String resField;

  private java.lang.String resName;
 
  private java.lang.String resState;

  private java.lang.String systemCode;

  private java.util.Collection upResources;

  public ResourceInformation() {
  }

  public ResourceInformation(java.lang.String resCode,
      java.lang.String resDesc, java.lang.String resField,
      java.lang.String resName, java.lang.String resState,
      java.lang.String systemCode, java.util.Collection upResources) {
    this.resCode = resCode;
    this.resDesc = resDesc;
    this.resField = resField;
    this.resName = resName;
    this.resState = resState;
    this.systemCode = systemCode;
    this.upResources = upResources;
  }

  /**
   * Gets the resCode value for this ResourceInformation.
   * 
   * @return resCode
   */
  public java.lang.String getResCode() {
    return resCode;
  }

  /**
   * Sets the resCode value for this ResourceInformation.
   * 
   * @param resCode
   */
  public void setResCode(java.lang.String resCode) {
    this.resCode = resCode;
  }

  /**
   * Gets the resDesc value for this ResourceInformation.
   * 
   * @return resDesc
   */
  public java.lang.String getResDesc() {
    return resDesc;
  }

  /**
   * Sets the resDesc value for this ResourceInformation.
   * 
   * @param resDesc
   */
  public void setResDesc(java.lang.String resDesc) {
    this.resDesc = resDesc;
  }

  /**
   * Gets the resField value for this ResourceInformation.
   * 
   * @return resField
   */
  public java.lang.String getResField() {
    return resField;
  }

  /**
   * Sets the resField value for this ResourceInformation.
   * 
   * @param resField
   */
  public void setResField(java.lang.String resField) {
    this.resField = resField;
  }

  /**
   * Gets the resName value for this ResourceInformation.
   * 
   * @return resName
   */
  public java.lang.String getResName() {
    return resName;
  }

  /**
   * Sets the resName value for this ResourceInformation.
   * 
   * @param resName
   */
  public void setResName(java.lang.String resName) {
    this.resName = resName;
  }

  /**
   * Gets the resState value for this ResourceInformation.
   * 
   * @return resState
   */
  public java.lang.String getResState() {
    return resState;
  }

  /**
   * Sets the resState value for this ResourceInformation.
   * 
   * @param resState
   */
  public void setResState(java.lang.String resState) {
    this.resState = resState;
  }

  /**
   * Gets the systemCode value for this ResourceInformation.
   * 
   * @return systemCode
   */
  public java.lang.String getSystemCode() {
    return systemCode;
  }

  /**
   * Sets the systemCode value for this ResourceInformation.
   * 
   * @param systemCode
   */
  public void setSystemCode(java.lang.String systemCode) {
    this.systemCode = systemCode;
  }

  /**
   * Gets the upResources value for this ResourceInformation.
   * 
   * @return upResources
   */
  public java.util.Collection getUpResources() {
    return upResources;
  }

  /**
   * Sets the upResources value for this ResourceInformation.
   * 
   * @param upResources
   */
  public void setUpResources(java.util.Collection upResources) {
    this.upResources = upResources;
  }

  private java.lang.Object __equalsCalc = null;

  public synchronized boolean equals(java.lang.Object obj) {
    if (!(obj instanceof ResourceInformation))
      return false;
    ResourceInformation other = (ResourceInformation) obj;
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
        && ((this.resCode == null && other.getResCode() == null) || (this.resCode != null && this.resCode
            .equals(other.getResCode())))
        && ((this.resDesc == null && other.getResDesc() == null) || (this.resDesc != null && this.resDesc
            .equals(other.getResDesc())))
        && ((this.resField == null && other.getResField() == null) || (this.resField != null && this.resField
            .equals(other.getResField())))
        && ((this.resName == null && other.getResName() == null) || (this.resName != null && this.resName
            .equals(other.getResName())))
        && ((this.resState == null && other.getResState() == null) || (this.resState != null && this.resState
            .equals(other.getResState())))
        && ((this.systemCode == null && other.getSystemCode() == null) || (this.systemCode != null && this.systemCode
            .equals(other.getSystemCode())))
        && ((this.upResources == null && other.getUpResources() == null) || (this.upResources != null && this.upResources
            .equals(other.getUpResources())));
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
    if (getResCode() != null) {
      _hashCode += getResCode().hashCode();
    }
    if (getResDesc() != null) {
      _hashCode += getResDesc().hashCode();
    }
    if (getResField() != null) {
      _hashCode += getResField().hashCode();
    }
    if (getResName() != null) {
      _hashCode += getResName().hashCode();
    }
    if (getResState() != null) {
      _hashCode += getResState().hashCode();
    }
    if (getSystemCode() != null) {
      _hashCode += getSystemCode().hashCode();
    }
    if (getUpResources() != null) {
      _hashCode += getUpResources().hashCode();
    }
    __hashCodeCalc = false;
    return _hashCode;
  }

  // Type metadata
  private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
      ResourceInformation.class, true);

  static {
    typeDesc.setXmlType(new javax.xml.namespace.QName(
        "http://bean.client.uums.bjca.com", "ResourceInformation"));
    org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("resCode");
    elemField.setXmlName(new javax.xml.namespace.QName("", "resCode"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("resDesc");
    elemField.setXmlName(new javax.xml.namespace.QName("", "resDesc"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("resField");
    elemField.setXmlName(new javax.xml.namespace.QName("", "resField"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("resName");
    elemField.setXmlName(new javax.xml.namespace.QName("", "resName"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("resState");
    elemField.setXmlName(new javax.xml.namespace.QName("", "resState"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("systemCode");
    elemField.setXmlName(new javax.xml.namespace.QName("", "systemCode"));
    elemField.setXmlType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setNillable(true);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("upResources");
    elemField.setXmlName(new javax.xml.namespace.QName("", "upResources"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://util.java",
        "Collection"));
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
