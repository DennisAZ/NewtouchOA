/**
 * User.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package yh.subsys.portal.bjca.logic.services.User;

public interface User extends java.rmi.Remote {
  public com.bjca.uums.client.bean.UserInformation findUserInfosByUserSIDForDC(
      java.lang.String in0) throws java.rmi.RemoteException;

  public com.bjca.uums.client.bean.RoleInformation findRoleInfoByRoleId(
      java.lang.String in0) throws java.rmi.RemoteException;

  public com.bjca.uums.client.bean.ResourceInformation findResouceInfosbyRCode(
      java.lang.String in0) throws java.rmi.RemoteException;

  public java.util.Collection findSystemInfosAccessedByUserID(
      java.lang.String in0) throws java.rmi.RemoteException;

  public com.bjca.uums.client.bean.DepartmentInformation findDepartByDepartID(
      java.lang.String in0) throws java.rmi.RemoteException;

  public com.bjca.uums.client.bean.SystemInformation findSystemInfoBySystemId(
      java.lang.String in0) throws java.rmi.RemoteException;

  public com.bjca.uums.client.bean.UserInformation findUserInfosByUserID(
      java.lang.String in0) throws java.rmi.RemoteException;

  public java.util.Collection findRoleInfosByUserID(java.lang.String in0)
      throws java.rmi.RemoteException;

  public java.util.Collection findRoleInfosBySystemID(java.lang.String in0)
      throws java.rmi.RemoteException;

  public java.util.Collection findUserInfosBySystemID(java.lang.String in0)
      throws java.rmi.RemoteException;

  public java.lang.String getAuthorityByUsernameAndPw(java.lang.String in0,
      java.lang.String in1) throws java.rmi.RemoteException;

  public java.lang.String getAuthorityByUsernameAndSerialNum(
      java.lang.String in0, java.lang.String in1, java.lang.String in2)
      throws java.rmi.RemoteException;

  public boolean checkCertSerialNumIsAbolished(java.lang.String in0,
      java.lang.String in1, java.lang.String in2, java.lang.String in3)
      throws java.rmi.RemoteException;

  public java.lang.String checkCertSerialNumIsValid(java.lang.String in0,
      java.lang.String in1, java.lang.String in2, java.lang.String in3)
      throws java.rmi.RemoteException;

  public int updateUserpw(java.lang.String in0, java.lang.String in1)
      throws java.rmi.RemoteException;

  public com.bjca.uums.client.bean.PersonInformation findPersonInfosByUserID(
      java.lang.String in0) throws java.rmi.RemoteException;

  public com.bjca.uums.client.bean.UnitInformation findUnitInfosByUserID(
      java.lang.String in0) throws java.rmi.RemoteException;

  public com.bjca.uums.client.bean.RoleInformation findRoleInfoByRoleCode(
      java.lang.String in0) throws java.rmi.RemoteException;

  public java.lang.String findRoleInfosByUserIDForStrType(java.lang.String in0)
      throws java.rmi.RemoteException;

  public java.lang.String getAuthorityByUserIDAndPw(java.lang.String in0,
      java.lang.String in1) throws java.rmi.RemoteException;

  public com.bjca.uums.client.bean.PersonInformation findPersonInfosByUserIDForDC(
      java.lang.String in0) throws java.rmi.RemoteException;

  public com.bjca.uums.client.bean.PersonInformation findWholePersonInfosByUserIDForDC(
      java.lang.String in0) throws java.rmi.RemoteException;

  public com.bjca.uums.client.bean.UnitInformation findUnitInfosByUserIDFroDC(
      java.lang.String in0) throws java.rmi.RemoteException;

  public java.util.Collection findUserInfosBySystemIDForDC(java.lang.String in0)
      throws java.rmi.RemoteException;

  public com.bjca.uums.client.bean.UserInformation findUserInfosByUserIDForDC(
      java.lang.String in0) throws java.rmi.RemoteException;

  public com.bjca.uums.client.bean.UserInformation findWholeUserInfosByUserIDForDC(
      java.lang.String in0) throws java.rmi.RemoteException;

  public java.lang.String getAuthorityAndSystemIDByUsernameAndPw(
      java.lang.String in0, java.lang.String in1)
      throws java.rmi.RemoteException;

  public com.bjca.uums.client.bean.SystemInformation findSystemInfoBySystemCode(
      java.lang.String in0) throws java.rmi.RemoteException;

  public java.util.Collection findCustomContentInfosBySystemCodeAndUserType(
      java.lang.String in0, java.lang.String in1)
      throws java.rmi.RemoteException;

  public com.bjca.uums.client.bean.UserCredenceInformation findCredenceInfoByUserID(
      java.lang.String in0) throws java.rmi.RemoteException;

  public java.util.Collection findCredenceInfosByUserID(java.lang.String in0)
      throws java.rmi.RemoteException;

  public java.util.Collection findRoleInfosBySystemCodeAndUserID(
      java.lang.String in0, java.lang.String in1)
      throws java.rmi.RemoteException;

  public com.bjca.uums.client.bean.CustomContentInfo findCustomContentInfoByCustomID(
      java.lang.String in0) throws java.rmi.RemoteException;

  public java.util.Collection findAllSystemInfos()
      throws java.rmi.RemoteException;

  public int updateUserpwByUserUniqueIdAndOldPwd(java.lang.String in0,
      java.lang.String in1, java.lang.String in2)
      throws java.rmi.RemoteException;

  public com.bjca.uums.client.bean.LoginInformation getLoginInformationByUserID(
      java.lang.String in0) throws java.rmi.RemoteException;

  public java.util.Collection findDepartsByUserID(java.lang.String in0)
      throws java.rmi.RemoteException;

  public java.util.Collection findResourceInfosByUserID(java.lang.String in0)
      throws java.rmi.RemoteException;

  public java.util.Collection findResourceInfosBySystemCodeAndUserID(
      java.lang.String in0, java.lang.String in1)
      throws java.rmi.RemoteException;
  
  public java.util.Collection findResourceInfosBySystemCodeAndRolecodeAndUserID(
      java.lang.String in0, java.lang.String in1, java.lang.String in2)
      throws java.rmi.RemoteException;
   
  public java.util.Collection findResourceInfosByRolecode(java.lang.String in0)
      throws java.rmi.RemoteException;
}
