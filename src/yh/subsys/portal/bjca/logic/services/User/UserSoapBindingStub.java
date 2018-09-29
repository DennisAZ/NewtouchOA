/**
 * UserSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package yh.subsys.portal.bjca.logic.services.User;

import yh.core.global.YHSysProps;

public class UserSoapBindingStub extends org.apache.axis.client.Stub implements
    User {
  private java.util.Vector cachedSerClasses = new java.util.Vector();
  private java.util.Vector cachedSerQNames = new java.util.Vector();
  private java.util.Vector cachedSerFactories = new java.util.Vector();
  private java.util.Vector cachedDeserFactories = new java.util.Vector();
   
  private java.lang.String User_address = YHSysProps.getString("WEB_SERVICES_URL") + "User";

  static org.apache.axis.description.OperationDesc[] _operations;

  static {
    _operations = new org.apache.axis.description.OperationDesc[41];
    _initOperationDesc1();
    _initOperationDesc2();
    _initOperationDesc3();
    _initOperationDesc4();
    _initOperationDesc5();
  }

  private static void _initOperationDesc1() {
    org.apache.axis.description.OperationDesc oper;
    org.apache.axis.description.ParameterDesc param;
    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("findUserInfosByUserSIDForDC");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName(
        "http://bean.client.uums.bjca.com", "UserInformation"));
    oper.setReturnClass(com.bjca.uums.client.bean.UserInformation.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "findUserInfosByUserSIDForDCReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[0] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("findRoleInfoByRoleId");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName(
        "http://bean.client.uums.bjca.com", "RoleInformation"));
    oper.setReturnClass(com.bjca.uums.client.bean.RoleInformation.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "findRoleInfoByRoleIdReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[1] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("findResouceInfosbyRCode");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName(
        "http://bean.client.uums.bjca.com", "ResourceInformation"));
    oper.setReturnClass(com.bjca.uums.client.bean.ResourceInformation.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "findResouceInfosbyRCodeReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[2] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("findSystemInfosAccessedByUserID");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName("http://util.java",
        "Collection"));
    oper.setReturnClass(java.util.Collection.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "findSystemInfosAccessedByUserIDReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[3] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("findDepartByDepartID");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName(
        "http://bean.client.uums.bjca.com", "DepartmentInformation"));
    oper.setReturnClass(com.bjca.uums.client.bean.DepartmentInformation.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "findDepartByDepartIDReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[4] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("findSystemInfoBySystemId");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName(
        "http://bean.client.uums.bjca.com", "SystemInformation"));
    oper.setReturnClass(com.bjca.uums.client.bean.SystemInformation.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "findSystemInfoBySystemIdReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[5] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("findUserInfosByUserID");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName(
        "http://bean.client.uums.bjca.com", "UserInformation"));
    oper.setReturnClass(com.bjca.uums.client.bean.UserInformation.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "findUserInfosByUserIDReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[6] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("findRoleInfosByUserID");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName("http://util.java",
        "Collection"));
    oper.setReturnClass(java.util.Collection.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "findRoleInfosByUserIDReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[7] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("findRoleInfosBySystemID");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName("http://util.java",
        "Collection"));
    oper.setReturnClass(java.util.Collection.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "findRoleInfosBySystemIDReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[8] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("findUserInfosBySystemID");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName("http://util.java",
        "Collection"));
    oper.setReturnClass(java.util.Collection.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "findUserInfosBySystemIDReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[9] = oper;

  }

  private static void _initOperationDesc2() {
    org.apache.axis.description.OperationDesc oper;
    org.apache.axis.description.ParameterDesc param;
    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("getAuthorityByUsernameAndPw");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in1"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    oper.setReturnClass(java.lang.String.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "getAuthorityByUsernameAndPwReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[10] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("getAuthorityByUsernameAndSerialNum");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in1"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in2"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    oper.setReturnClass(java.lang.String.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "getAuthorityByUsernameAndSerialNumReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[11] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("checkCertSerialNumIsAbolished");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in1"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in2"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in3"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "boolean"));
    oper.setReturnClass(boolean.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "checkCertSerialNumIsAbolishedReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[12] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("checkCertSerialNumIsValid");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in1"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in2"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in3"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    oper.setReturnClass(java.lang.String.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "checkCertSerialNumIsValidReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[13] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("UpdateUserpw");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in1"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "int"));
    oper.setReturnClass(int.class);
    oper
        .setReturnQName(new javax.xml.namespace.QName("", "UpdateUserpwReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[14] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("findPersonInfosByUserID");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName(
        "http://bean.client.uums.bjca.com", "PersonInformation"));
    oper.setReturnClass(com.bjca.uums.client.bean.PersonInformation.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "findPersonInfosByUserIDReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[15] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("findUnitInfosByUserID");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName(
        "http://bean.client.uums.bjca.com", "UnitInformation"));
    oper.setReturnClass(com.bjca.uums.client.bean.UnitInformation.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "findUnitInfosByUserIDReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[16] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("findRoleInfoByRoleCode");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName(
        "http://bean.client.uums.bjca.com", "RoleInformation"));
    oper.setReturnClass(com.bjca.uums.client.bean.RoleInformation.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "findRoleInfoByRoleCodeReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[17] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("findRoleInfosByUserIDForStrType");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    oper.setReturnClass(java.lang.String.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "findRoleInfosByUserIDForStrTypeReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[18] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("getAuthorityByUserIDAndPw");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in1"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    oper.setReturnClass(java.lang.String.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "getAuthorityByUserIDAndPwReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[19] = oper;

  }

  private static void _initOperationDesc3() {
    org.apache.axis.description.OperationDesc oper;
    org.apache.axis.description.ParameterDesc param;
    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("findPersonInfosByUserIDForDC");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName(
        "http://bean.client.uums.bjca.com", "PersonInformation"));
    oper.setReturnClass(com.bjca.uums.client.bean.PersonInformation.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "findPersonInfosByUserIDForDCReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[20] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("findWholePersonInfosByUserIDForDC");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName(
        "http://bean.client.uums.bjca.com", "PersonInformation"));
    oper.setReturnClass(com.bjca.uums.client.bean.PersonInformation.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "findWholePersonInfosByUserIDForDCReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[21] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("findUnitInfosByUserIDFroDC");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName(
        "http://bean.client.uums.bjca.com", "UnitInformation"));
    oper.setReturnClass(com.bjca.uums.client.bean.UnitInformation.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "findUnitInfosByUserIDFroDCReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[22] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("findUserInfosBySystemIDForDC");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName("http://util.java",
        "Collection"));
    oper.setReturnClass(java.util.Collection.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "findUserInfosBySystemIDForDCReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[23] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("findUserInfosByUserIDForDC");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName(
        "http://bean.client.uums.bjca.com", "UserInformation"));
    oper.setReturnClass(com.bjca.uums.client.bean.UserInformation.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "findUserInfosByUserIDForDCReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[24] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("findWholeUserInfosByUserIDForDC");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName(
        "http://bean.client.uums.bjca.com", "UserInformation"));
    oper.setReturnClass(com.bjca.uums.client.bean.UserInformation.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "findWholeUserInfosByUserIDForDCReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[25] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("getAuthorityAndSystemIDByUsernameAndPw");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in1"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "string"));
    oper.setReturnClass(java.lang.String.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "getAuthorityAndSystemIDByUsernameAndPwReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[26] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("findSystemInfoBySystemCode");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName(
        "http://bean.client.uums.bjca.com", "SystemInformation"));
    oper.setReturnClass(com.bjca.uums.client.bean.SystemInformation.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "findSystemInfoBySystemCodeReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[27] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("findCustomContentInfosBySystemCodeAndUserType");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in1"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName("http://util.java",
        "Collection"));
    oper.setReturnClass(java.util.Collection.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "findCustomContentInfosBySystemCodeAndUserTypeReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[28] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("findCredenceInfoByUserID");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName(
        "http://bean.client.uums.bjca.com", "UserCredenceInformation"));
    oper
        .setReturnClass(com.bjca.uums.client.bean.UserCredenceInformation.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "findCredenceInfoByUserIDReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[29] = oper;

  }

  private static void _initOperationDesc4() {
    org.apache.axis.description.OperationDesc oper;
    org.apache.axis.description.ParameterDesc param;
    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("findCredenceInfosByUserID");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName("http://util.java",
        "Collection"));
    oper.setReturnClass(java.util.Collection.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "findCredenceInfosByUserIDReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[30] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("findRoleInfosBySystemCodeAndUserID");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in1"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName("http://util.java",
        "Collection"));
    oper.setReturnClass(java.util.Collection.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "findRoleInfosBySystemCodeAndUserIDReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[31] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("findCustomContentInfoByCustomID");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName(
        "http://bean.client.uums.bjca.com", "CustomContentInfo"));
    oper.setReturnClass(com.bjca.uums.client.bean.CustomContentInfo.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "findCustomContentInfoByCustomIDReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[32] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("findAllSystemInfos");
    oper.setReturnType(new javax.xml.namespace.QName("http://util.java",
        "Collection"));
    oper.setReturnClass(java.util.Collection.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "findAllSystemInfosReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[33] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("UpdateUserpwByUserUniqueIdAndOldPwd");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in1"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in2"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName(
        "http://www.w3.org/2001/XMLSchema", "int"));
    oper.setReturnClass(int.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "UpdateUserpwByUserUniqueIdAndOldPwdReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[34] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("getLoginInformationByUserID");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName(
        "http://bean.client.uums.bjca.com", "LoginInformation"));
    oper.setReturnClass(com.bjca.uums.client.bean.LoginInformation.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "getLoginInformationByUserIDReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[35] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("findDepartsByUserID");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName("http://util.java",
        "Collection"));
    oper.setReturnClass(java.util.Collection.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "findDepartsByUserIDReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[36] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("findResourceInfosByUserID");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName("http://util.java",
        "Collection"));
    oper.setReturnClass(java.util.Collection.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "findResourceInfosByUserIDReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[37] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("findResourceInfosBySystemCodeAndUserID");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in1"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName("http://util.java",
        "Collection"));
    oper.setReturnClass(java.util.Collection.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "findResourceInfosBySystemCodeAndUserIDReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[38] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("findResourceInfosBySystemCodeAndRolecodeAndUserID");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in1"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in2"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName("http://util.java",
        "Collection"));
    oper.setReturnClass(java.util.Collection.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "findResourceInfosBySystemCodeAndRolecodeAndUserIDReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[39] = oper;

  }

  private static void _initOperationDesc5() {
    org.apache.axis.description.OperationDesc oper;
    org.apache.axis.description.ParameterDesc param;
    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("findResourceInfosByRolecode");
    param = new org.apache.axis.description.ParameterDesc(
        new javax.xml.namespace.QName("", "in0"),
        org.apache.axis.description.ParameterDesc.IN,
        new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema",
            "string"), java.lang.String.class, false, false);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName("http://util.java",
        "Collection"));
    oper.setReturnClass(java.util.Collection.class);
    oper.setReturnQName(new javax.xml.namespace.QName("",
        "findResourceInfosByRolecodeReturn"));
    oper.setStyle(org.apache.axis.constants.Style.RPC);
    oper.setUse(org.apache.axis.constants.Use.ENCODED);
    _operations[40] = oper;

  }

  public UserSoapBindingStub() throws org.apache.axis.AxisFault {
    this(null);
  }

  public UserSoapBindingStub(java.net.URL endpointURL,
      javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
    this(service);
    super.cachedEndpoint = endpointURL;
  }

  public UserSoapBindingStub(javax.xml.rpc.Service service)
      throws org.apache.axis.AxisFault {
    if (service == null) {
      super.service = new org.apache.axis.client.Service();
    } else {
      super.service = service;
    }
    ((org.apache.axis.client.Service) super.service)
        .setTypeMappingVersion("1.2");
    java.lang.Class cls;
    javax.xml.namespace.QName qName;
    javax.xml.namespace.QName qName2;
    java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
    java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
    java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
    java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
    java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
    java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
    java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
    java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
    java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
    java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
    qName = new javax.xml.namespace.QName("http://bean.client.uums.bjca.com",
        "CustomContentInfo");
    cachedSerQNames.add(qName);
    cls = com.bjca.uums.client.bean.CustomContentInfo.class;
    cachedSerClasses.add(cls);
    cachedSerFactories.add(beansf);
    cachedDeserFactories.add(beandf);

    qName = new javax.xml.namespace.QName("http://bean.client.uums.bjca.com",
        "DepartmentInformation");
    cachedSerQNames.add(qName);
    cls = com.bjca.uums.client.bean.DepartmentInformation.class;
    cachedSerClasses.add(cls);
    cachedSerFactories.add(beansf);
    cachedDeserFactories.add(beandf);

    qName = new javax.xml.namespace.QName("http://bean.client.uums.bjca.com",
        "LoginInformation");
    cachedSerQNames.add(qName);
    cls = com.bjca.uums.client.bean.LoginInformation.class;
    cachedSerClasses.add(cls);
    cachedSerFactories.add(beansf);
    cachedDeserFactories.add(beandf);

    qName = new javax.xml.namespace.QName("http://bean.client.uums.bjca.com",
        "PersonInformation");
    cachedSerQNames.add(qName);
    cls = com.bjca.uums.client.bean.PersonInformation.class;
    cachedSerClasses.add(cls);
    cachedSerFactories.add(beansf);
    cachedDeserFactories.add(beandf);

    qName = new javax.xml.namespace.QName("http://bean.client.uums.bjca.com",
        "ResourceInformation");
    cachedSerQNames.add(qName);
    cls = com.bjca.uums.client.bean.ResourceInformation.class;
    cachedSerClasses.add(cls);
    cachedSerFactories.add(beansf);
    cachedDeserFactories.add(beandf);

    qName = new javax.xml.namespace.QName("http://bean.client.uums.bjca.com",
        "RoleInformation");
    cachedSerQNames.add(qName);
    cls = com.bjca.uums.client.bean.RoleInformation.class;
    cachedSerClasses.add(cls);
    cachedSerFactories.add(beansf);
    cachedDeserFactories.add(beandf);

    qName = new javax.xml.namespace.QName("http://bean.client.uums.bjca.com",
        "SystemInformation");
    cachedSerQNames.add(qName);
    cls = com.bjca.uums.client.bean.SystemInformation.class;
    cachedSerClasses.add(cls);
    cachedSerFactories.add(beansf);
    cachedDeserFactories.add(beandf);

    qName = new javax.xml.namespace.QName("http://bean.client.uums.bjca.com",
        "UnitInformation");
    cachedSerQNames.add(qName);
    cls = com.bjca.uums.client.bean.UnitInformation.class;
    cachedSerClasses.add(cls);
    cachedSerFactories.add(beansf);
    cachedDeserFactories.add(beandf);

    qName = new javax.xml.namespace.QName("http://bean.client.uums.bjca.com",
        "UserCredenceInformation");
    cachedSerQNames.add(qName);
    cls = com.bjca.uums.client.bean.UserCredenceInformation.class;
    cachedSerClasses.add(cls);
    cachedSerFactories.add(beansf);
    cachedDeserFactories.add(beandf);

    qName = new javax.xml.namespace.QName("http://bean.client.uums.bjca.com",
        "UserInformation");
    cachedSerQNames.add(qName);
    cls = com.bjca.uums.client.bean.UserInformation.class;
    cachedSerClasses.add(cls);
    cachedSerFactories.add(beansf);
    cachedDeserFactories.add(beandf);

    qName = new javax.xml.namespace.QName("http://util.java", "Collection");
    cachedSerQNames.add(qName);
    cls = java.util.Collection.class;
    cachedSerClasses.add(cls);
    cachedSerFactories.add(beansf);
    cachedDeserFactories.add(beandf);

  }

  protected org.apache.axis.client.Call createCall()
      throws java.rmi.RemoteException {
    try {
      org.apache.axis.client.Call _call = super._createCall();
      if (super.maintainSessionSet) {
        _call.setMaintainSession(super.maintainSession);
      }
      if (super.cachedUsername != null) {
        _call.setUsername(super.cachedUsername);
      }
      if (super.cachedPassword != null) {
        _call.setPassword(super.cachedPassword);
      }
      if (super.cachedEndpoint != null) {
        _call.setTargetEndpointAddress(super.cachedEndpoint);
      }
      if (super.cachedTimeout != null) {
        _call.setTimeout(super.cachedTimeout);
      }
      if (super.cachedPortName != null) {
        _call.setPortName(super.cachedPortName);
      }
      java.util.Enumeration keys = super.cachedProperties.keys();
      while (keys.hasMoreElements()) {
        java.lang.String key = (java.lang.String) keys.nextElement();
        _call.setProperty(key, super.cachedProperties.get(key));
      }
      // All the type mapping information is registered
      // when the first call is made.
      // The type mapping information is actually registered in
      // the TypeMappingRegistry of the service, which
      // is the reason why registration is only needed for the first call.
      synchronized (this) {
        if (firstCall()) {
          // must set encoding style before registering serializers
          _call
              .setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
          _call.setEncodingStyle(org.apache.axis.Constants.URI_SOAP11_ENC);
          for (int i = 0; i < cachedSerFactories.size(); ++i) {
            java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
            javax.xml.namespace.QName qName = (javax.xml.namespace.QName) cachedSerQNames
                .get(i);
            java.lang.Object x = cachedSerFactories.get(i);
            if (x instanceof Class) {
              java.lang.Class sf = (java.lang.Class) cachedSerFactories.get(i);
              java.lang.Class df = (java.lang.Class) cachedDeserFactories
                  .get(i);
              _call.registerTypeMapping(cls, qName, sf, df, false);
            } else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
              org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory) cachedSerFactories
                  .get(i);
              org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory) cachedDeserFactories
                  .get(i);
              _call.registerTypeMapping(cls, qName, sf, df, false);
            }
          }
        }
      }
      return _call;
    } catch (java.lang.Throwable _t) {
      throw new org.apache.axis.AxisFault(
          "Failure trying to get the Call object", _t);
    }
  }

  public com.bjca.uums.client.bean.UserInformation findUserInfosByUserSIDForDC(
      java.lang.String in0) throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[0]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "findUserInfosByUserSIDForDC"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (com.bjca.uums.client.bean.UserInformation) _resp;
        } catch (java.lang.Exception _exception) {
          return (com.bjca.uums.client.bean.UserInformation) org.apache.axis.utils.JavaUtils
              .convert(_resp, com.bjca.uums.client.bean.UserInformation.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public com.bjca.uums.client.bean.RoleInformation findRoleInfoByRoleId(
      java.lang.String in0) throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[1]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "findRoleInfoByRoleId"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (com.bjca.uums.client.bean.RoleInformation) _resp;
        } catch (java.lang.Exception _exception) {
          return (com.bjca.uums.client.bean.RoleInformation) org.apache.axis.utils.JavaUtils
              .convert(_resp, com.bjca.uums.client.bean.RoleInformation.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public com.bjca.uums.client.bean.ResourceInformation findResouceInfosbyRCode(
      java.lang.String in0) throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[2]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "findResouceInfosbyRCode"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (com.bjca.uums.client.bean.ResourceInformation) _resp;
        } catch (java.lang.Exception _exception) {
          return (com.bjca.uums.client.bean.ResourceInformation) org.apache.axis.utils.JavaUtils
              .convert(_resp,
                  com.bjca.uums.client.bean.ResourceInformation.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public java.util.Collection findSystemInfosAccessedByUserID(
      java.lang.String in0) throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[3]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "findSystemInfosAccessedByUserID"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (java.util.Collection) _resp;
        } catch (java.lang.Exception _exception) {
          return (java.util.Collection) org.apache.axis.utils.JavaUtils
              .convert(_resp, java.util.Collection.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public com.bjca.uums.client.bean.DepartmentInformation findDepartByDepartID(
      java.lang.String in0) throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[4]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "findDepartByDepartID"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (com.bjca.uums.client.bean.DepartmentInformation) _resp;
        } catch (java.lang.Exception _exception) {
          return (com.bjca.uums.client.bean.DepartmentInformation) org.apache.axis.utils.JavaUtils
              .convert(_resp,
                  com.bjca.uums.client.bean.DepartmentInformation.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public com.bjca.uums.client.bean.SystemInformation findSystemInfoBySystemId(
      java.lang.String in0) throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[5]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "findSystemInfoBySystemId"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (com.bjca.uums.client.bean.SystemInformation) _resp;
        } catch (java.lang.Exception _exception) {
          return (com.bjca.uums.client.bean.SystemInformation) org.apache.axis.utils.JavaUtils
              .convert(_resp, com.bjca.uums.client.bean.SystemInformation.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public com.bjca.uums.client.bean.UserInformation findUserInfosByUserID(
      java.lang.String in0) throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[6]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "findUserInfosByUserID"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (com.bjca.uums.client.bean.UserInformation) _resp;
        } catch (java.lang.Exception _exception) {
          return (com.bjca.uums.client.bean.UserInformation) org.apache.axis.utils.JavaUtils
              .convert(_resp, com.bjca.uums.client.bean.UserInformation.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public java.util.Collection findRoleInfosByUserID(java.lang.String in0)
      throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[7]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "findRoleInfosByUserID"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (java.util.Collection) _resp;
        } catch (java.lang.Exception _exception) {
          return (java.util.Collection) org.apache.axis.utils.JavaUtils
              .convert(_resp, java.util.Collection.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public java.util.Collection findRoleInfosBySystemID(java.lang.String in0)
      throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[8]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "findRoleInfosBySystemID"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (java.util.Collection) _resp;
        } catch (java.lang.Exception _exception) {
          return (java.util.Collection) org.apache.axis.utils.JavaUtils
              .convert(_resp, java.util.Collection.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public java.util.Collection findUserInfosBySystemID(java.lang.String in0)
      throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[9]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "findUserInfosBySystemID"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (java.util.Collection) _resp;
        } catch (java.lang.Exception _exception) {
          return (java.util.Collection) org.apache.axis.utils.JavaUtils
              .convert(_resp, java.util.Collection.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public java.lang.String getAuthorityByUsernameAndPw(java.lang.String in0,
      java.lang.String in1) throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[10]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "getAuthorityByUsernameAndPw"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call
          .invoke(new java.lang.Object[] { in0, in1 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (java.lang.String) _resp;
        } catch (java.lang.Exception _exception) {
          return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(
              _resp, java.lang.String.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public java.lang.String getAuthorityByUsernameAndSerialNum(
      java.lang.String in0, java.lang.String in1, java.lang.String in2)
      throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[11]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "getAuthorityByUsernameAndSerialNum"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0, in1,
          in2 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (java.lang.String) _resp;
        } catch (java.lang.Exception _exception) {
          return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(
              _resp, java.lang.String.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public boolean checkCertSerialNumIsAbolished(java.lang.String in0,
      java.lang.String in1, java.lang.String in2, java.lang.String in3)
      throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[12]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "checkCertSerialNumIsAbolished"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0, in1,
          in2, in3 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return ((java.lang.Boolean) _resp).booleanValue();
        } catch (java.lang.Exception _exception) {
          return ((java.lang.Boolean) org.apache.axis.utils.JavaUtils.convert(
              _resp, boolean.class)).booleanValue();
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public java.lang.String checkCertSerialNumIsValid(java.lang.String in0,
      java.lang.String in1, java.lang.String in2, java.lang.String in3)
      throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[13]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "checkCertSerialNumIsValid"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0, in1,
          in2, in3 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (java.lang.String) _resp;
        } catch (java.lang.Exception _exception) {
          return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(
              _resp, java.lang.String.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public int updateUserpw(java.lang.String in0, java.lang.String in1)
      throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[14]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address, "UpdateUserpw"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call
          .invoke(new java.lang.Object[] { in0, in1 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return ((java.lang.Integer) _resp).intValue();
        } catch (java.lang.Exception _exception) {
          return ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(
              _resp, int.class)).intValue();
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public com.bjca.uums.client.bean.PersonInformation findPersonInfosByUserID(
      java.lang.String in0) throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[15]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "findPersonInfosByUserID"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (com.bjca.uums.client.bean.PersonInformation) _resp;
        } catch (java.lang.Exception _exception) {
          return (com.bjca.uums.client.bean.PersonInformation) org.apache.axis.utils.JavaUtils
              .convert(_resp, com.bjca.uums.client.bean.PersonInformation.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public com.bjca.uums.client.bean.UnitInformation findUnitInfosByUserID(
      java.lang.String in0) throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[16]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "findUnitInfosByUserID"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (com.bjca.uums.client.bean.UnitInformation) _resp;
        } catch (java.lang.Exception _exception) {
          return (com.bjca.uums.client.bean.UnitInformation) org.apache.axis.utils.JavaUtils
              .convert(_resp, com.bjca.uums.client.bean.UnitInformation.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public com.bjca.uums.client.bean.RoleInformation findRoleInfoByRoleCode(
      java.lang.String in0) throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[17]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "findRoleInfoByRoleCode"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (com.bjca.uums.client.bean.RoleInformation) _resp;
        } catch (java.lang.Exception _exception) {
          return (com.bjca.uums.client.bean.RoleInformation) org.apache.axis.utils.JavaUtils
              .convert(_resp, com.bjca.uums.client.bean.RoleInformation.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public java.lang.String findRoleInfosByUserIDForStrType(java.lang.String in0)
      throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[18]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "findRoleInfosByUserIDForStrType"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (java.lang.String) _resp;
        } catch (java.lang.Exception _exception) {
          return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(
              _resp, java.lang.String.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public java.lang.String getAuthorityByUserIDAndPw(java.lang.String in0,
      java.lang.String in1) throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[19]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "getAuthorityByUserIDAndPw"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call
          .invoke(new java.lang.Object[] { in0, in1 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (java.lang.String) _resp;
        } catch (java.lang.Exception _exception) {
          return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(
              _resp, java.lang.String.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public com.bjca.uums.client.bean.PersonInformation findPersonInfosByUserIDForDC(
      java.lang.String in0) throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[20]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "findPersonInfosByUserIDForDC"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (com.bjca.uums.client.bean.PersonInformation) _resp;
        } catch (java.lang.Exception _exception) {
          return (com.bjca.uums.client.bean.PersonInformation) org.apache.axis.utils.JavaUtils
              .convert(_resp, com.bjca.uums.client.bean.PersonInformation.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public com.bjca.uums.client.bean.PersonInformation findWholePersonInfosByUserIDForDC(
      java.lang.String in0) throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[21]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "findWholePersonInfosByUserIDForDC"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (com.bjca.uums.client.bean.PersonInformation) _resp;
        } catch (java.lang.Exception _exception) {
          return (com.bjca.uums.client.bean.PersonInformation) org.apache.axis.utils.JavaUtils
              .convert(_resp, com.bjca.uums.client.bean.PersonInformation.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public com.bjca.uums.client.bean.UnitInformation findUnitInfosByUserIDFroDC(
      java.lang.String in0) throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[22]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "findUnitInfosByUserIDFroDC"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (com.bjca.uums.client.bean.UnitInformation) _resp;
        } catch (java.lang.Exception _exception) {
          return (com.bjca.uums.client.bean.UnitInformation) org.apache.axis.utils.JavaUtils
              .convert(_resp, com.bjca.uums.client.bean.UnitInformation.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public java.util.Collection findUserInfosBySystemIDForDC(java.lang.String in0)
      throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[23]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "findUserInfosBySystemIDForDC"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (java.util.Collection) _resp;
        } catch (java.lang.Exception _exception) {
          return (java.util.Collection) org.apache.axis.utils.JavaUtils
              .convert(_resp, java.util.Collection.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public com.bjca.uums.client.bean.UserInformation findUserInfosByUserIDForDC(
      java.lang.String in0) throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[24]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "findUserInfosByUserIDForDC"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (com.bjca.uums.client.bean.UserInformation) _resp;
        } catch (java.lang.Exception _exception) {
          return (com.bjca.uums.client.bean.UserInformation) org.apache.axis.utils.JavaUtils
              .convert(_resp, com.bjca.uums.client.bean.UserInformation.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public com.bjca.uums.client.bean.UserInformation findWholeUserInfosByUserIDForDC(
      java.lang.String in0) throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[25]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "findWholeUserInfosByUserIDForDC"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (com.bjca.uums.client.bean.UserInformation) _resp;
        } catch (java.lang.Exception _exception) {
          return (com.bjca.uums.client.bean.UserInformation) org.apache.axis.utils.JavaUtils
              .convert(_resp, com.bjca.uums.client.bean.UserInformation.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public java.lang.String getAuthorityAndSystemIDByUsernameAndPw(
      java.lang.String in0, java.lang.String in1)
      throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[26]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "getAuthorityAndSystemIDByUsernameAndPw"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call
          .invoke(new java.lang.Object[] { in0, in1 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (java.lang.String) _resp;
        } catch (java.lang.Exception _exception) {
          return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(
              _resp, java.lang.String.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public com.bjca.uums.client.bean.SystemInformation findSystemInfoBySystemCode(
      java.lang.String in0) throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[27]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "findSystemInfoBySystemCode"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (com.bjca.uums.client.bean.SystemInformation) _resp;
        } catch (java.lang.Exception _exception) {
          return (com.bjca.uums.client.bean.SystemInformation) org.apache.axis.utils.JavaUtils
              .convert(_resp, com.bjca.uums.client.bean.SystemInformation.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public java.util.Collection findCustomContentInfosBySystemCodeAndUserType(
      java.lang.String in0, java.lang.String in1)
      throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[28]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "findCustomContentInfosBySystemCodeAndUserType"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call
          .invoke(new java.lang.Object[] { in0, in1 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (java.util.Collection) _resp;
        } catch (java.lang.Exception _exception) {
          return (java.util.Collection) org.apache.axis.utils.JavaUtils
              .convert(_resp, java.util.Collection.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public com.bjca.uums.client.bean.UserCredenceInformation findCredenceInfoByUserID(
      java.lang.String in0) throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[29]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "findCredenceInfoByUserID"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (com.bjca.uums.client.bean.UserCredenceInformation) _resp;
        } catch (java.lang.Exception _exception) {
          return (com.bjca.uums.client.bean.UserCredenceInformation) org.apache.axis.utils.JavaUtils
              .convert(_resp,
                  com.bjca.uums.client.bean.UserCredenceInformation.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public java.util.Collection findCredenceInfosByUserID(java.lang.String in0)
      throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[30]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "findCredenceInfosByUserID"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (java.util.Collection) _resp;
        } catch (java.lang.Exception _exception) {
          return (java.util.Collection) org.apache.axis.utils.JavaUtils
              .convert(_resp, java.util.Collection.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public java.util.Collection findRoleInfosBySystemCodeAndUserID(
      java.lang.String in0, java.lang.String in1)
      throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[31]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "findRoleInfosBySystemCodeAndUserID"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call
          .invoke(new java.lang.Object[] { in0, in1 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (java.util.Collection) _resp;
        } catch (java.lang.Exception _exception) {
          return (java.util.Collection) org.apache.axis.utils.JavaUtils
              .convert(_resp, java.util.Collection.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public com.bjca.uums.client.bean.CustomContentInfo findCustomContentInfoByCustomID(
      java.lang.String in0) throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[32]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "findCustomContentInfoByCustomID"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (com.bjca.uums.client.bean.CustomContentInfo) _resp;
        } catch (java.lang.Exception _exception) {
          return (com.bjca.uums.client.bean.CustomContentInfo) org.apache.axis.utils.JavaUtils
              .convert(_resp, com.bjca.uums.client.bean.CustomContentInfo.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public java.util.Collection findAllSystemInfos()
      throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[33]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "findAllSystemInfos"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (java.util.Collection) _resp;
        } catch (java.lang.Exception _exception) {
          return (java.util.Collection) org.apache.axis.utils.JavaUtils
              .convert(_resp, java.util.Collection.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public int updateUserpwByUserUniqueIdAndOldPwd(java.lang.String in0,
      java.lang.String in1, java.lang.String in2)
      throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[34]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "UpdateUserpwByUserUniqueIdAndOldPwd"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0, in1,
          in2 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return ((java.lang.Integer) _resp).intValue();
        } catch (java.lang.Exception _exception) {
          return ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(
              _resp, int.class)).intValue();
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public com.bjca.uums.client.bean.LoginInformation getLoginInformationByUserID(
      java.lang.String in0) throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[35]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "getLoginInformationByUserID"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (com.bjca.uums.client.bean.LoginInformation) _resp;
        } catch (java.lang.Exception _exception) {
          return (com.bjca.uums.client.bean.LoginInformation) org.apache.axis.utils.JavaUtils
              .convert(_resp, com.bjca.uums.client.bean.LoginInformation.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public java.util.Collection findDepartsByUserID(java.lang.String in0)
      throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[36]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "findDepartsByUserID"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (java.util.Collection) _resp;
        } catch (java.lang.Exception _exception) {
          return (java.util.Collection) org.apache.axis.utils.JavaUtils
              .convert(_resp, java.util.Collection.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public java.util.Collection findResourceInfosByUserID(java.lang.String in0)
      throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[37]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "findResourceInfosByUserID"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (java.util.Collection) _resp;
        } catch (java.lang.Exception _exception) {
          return (java.util.Collection) org.apache.axis.utils.JavaUtils
              .convert(_resp, java.util.Collection.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public java.util.Collection findResourceInfosBySystemCodeAndUserID(
      java.lang.String in0, java.lang.String in1)
      throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[38]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "findResourceInfosBySystemCodeAndUserID"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call
          .invoke(new java.lang.Object[] { in0, in1 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (java.util.Collection) _resp;
        } catch (java.lang.Exception _exception) {
          return (java.util.Collection) org.apache.axis.utils.JavaUtils
              .convert(_resp, java.util.Collection.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public java.util.Collection findResourceInfosBySystemCodeAndRolecodeAndUserID(
      java.lang.String in0, java.lang.String in1, java.lang.String in2)
      throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[39]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "findResourceInfosBySystemCodeAndRolecodeAndUserID"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0, in1,
          in2 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (java.util.Collection) _resp;
        } catch (java.lang.Exception _exception) {
          return (java.util.Collection) org.apache.axis.utils.JavaUtils
              .convert(_resp, java.util.Collection.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public java.util.Collection findResourceInfosByRolecode(java.lang.String in0)
      throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[40]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("");
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName(
        User_address,
        "findResourceInfosByRolecode"));

    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] { in0 });

      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException) _resp;
      } else {
        extractAttachments(_call);
        try {
          return (java.util.Collection) _resp;
        } catch (java.lang.Exception _exception) {
          return (java.util.Collection) org.apache.axis.utils.JavaUtils
              .convert(_resp, java.util.Collection.class);
        }
      }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

}
