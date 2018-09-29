/**
 * Department.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package yh.subsys.portal.bjca.logic.services.Department;

public interface Department extends java.rmi.Remote {
  public com.bjca.uums.client.bean.DepartmentInformation findDepartByDepartID(
      java.lang.String in0) throws java.rmi.RemoteException;

  public java.util.Collection getAllDepart() throws java.rmi.RemoteException;

  public com.bjca.uums.client.bean.DepartmentInformation findDepartByDepartCode(
      java.lang.String in0) throws java.rmi.RemoteException;

  public com.bjca.uums.client.bean.DepartmentInformation findDepartByDepartCodeForDC(
      java.lang.String in0) throws java.rmi.RemoteException;

  public java.util.Collection getAllDepartForDC()
      throws java.rmi.RemoteException;
}
