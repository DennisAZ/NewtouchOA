/**
 * DepartmentServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package yh.subsys.portal.bjca.logic.services.Department;

import yh.core.global.YHSysProps;

public class DepartmentServiceLocator extends org.apache.axis.client.Service
    implements DepartmentService {
   
  public DepartmentServiceLocator() {
  }

  public DepartmentServiceLocator(org.apache.axis.EngineConfiguration config) {
    super(config);
  }

  public DepartmentServiceLocator(java.lang.String wsdlLoc,
      javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
    super(wsdlLoc, sName);
  }

  // Use to get a proxy class for Department
  private java.lang.String Department_address = YHSysProps.getString("WEB_SERVICES_URL") + "Department";

  public java.lang.String getDepartmentAddress() {
    return Department_address;
  }

  // The WSDD service name defaults to the port name.
  private java.lang.String DepartmentWSDDServiceName = "Department";

  public java.lang.String getDepartmentWSDDServiceName() {
    return DepartmentWSDDServiceName;
  }

  public void setDepartmentWSDDServiceName(java.lang.String name) {
    DepartmentWSDDServiceName = name;
  }

  public Department getDepartment()
      throws javax.xml.rpc.ServiceException {
    java.net.URL endpoint;
    try {
      endpoint = new java.net.URL(Department_address);
    } catch (java.net.MalformedURLException e) {
      throw new javax.xml.rpc.ServiceException(e);
    }
    return getDepartment(endpoint);
  }

  public Department getDepartment(
      java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
    try {
      DepartmentSoapBindingStub _stub = new DepartmentSoapBindingStub(
          portAddress, this);
      _stub.setPortName(getDepartmentWSDDServiceName());
      return _stub;
    } catch (org.apache.axis.AxisFault e) {
      return null;
    }
  }

  public void setDepartmentEndpointAddress(java.lang.String address) {
    Department_address = address;
  }

  /**
   * For the given interface, get the stub implementation. If this service has
   * no port for the given interface, then ServiceException is thrown.
   */
  public java.rmi.Remote getPort(Class serviceEndpointInterface)
      throws javax.xml.rpc.ServiceException {
    try {
      if (Department.class
          .isAssignableFrom(serviceEndpointInterface)) {
        DepartmentSoapBindingStub _stub = new DepartmentSoapBindingStub(
            new java.net.URL(Department_address), this);
        _stub.setPortName(getDepartmentWSDDServiceName());
        return _stub;
      }
    } catch (java.lang.Throwable t) {
      throw new javax.xml.rpc.ServiceException(t);
    }
    throw new javax.xml.rpc.ServiceException(
        "There is no stub implementation for the interface:  "
            + (serviceEndpointInterface == null ? "null"
                : serviceEndpointInterface.getName()));
  }

  /**
   * For the given interface, get the stub implementation. If this service has
   * no port for the given interface, then ServiceException is thrown.
   */
  public java.rmi.Remote getPort(javax.xml.namespace.QName portName,
      Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
    if (portName == null) {
      return getPort(serviceEndpointInterface);
    }
    java.lang.String inputPortName = portName.getLocalPart();
    if ("Department".equals(inputPortName)) {
      return getDepartment();
    } else {
      java.rmi.Remote _stub = getPort(serviceEndpointInterface);
      ((org.apache.axis.client.Stub) _stub).setPortName(portName);
      return _stub;
    }
  }

  public javax.xml.namespace.QName getServiceName() {
    return new javax.xml.namespace.QName(
        Department_address,
        DepartmentWSDDServiceName);
  }

  private java.util.HashSet ports = null;

  public java.util.Iterator getPorts() {
    if (ports == null) {
      ports = new java.util.HashSet();
      ports.add(new javax.xml.namespace.QName(
          Department_address,
          DepartmentWSDDServiceName));
    }
    return ports.iterator();
  }

  /**
   * Set the endpoint address for the specified port name.
   */
  public void setEndpointAddress(java.lang.String portName,
      java.lang.String address) throws javax.xml.rpc.ServiceException {

    if ("Department".equals(portName)) {
      setDepartmentEndpointAddress(address);
    } else { // Unknown Port Name
      throw new javax.xml.rpc.ServiceException(
          " Cannot set Endpoint Address for Unknown Port" + portName);
    }
  }

  /**
   * Set the endpoint address for the specified port name.
   */
  public void setEndpointAddress(javax.xml.namespace.QName portName,
      java.lang.String address) throws javax.xml.rpc.ServiceException {
    setEndpointAddress(portName.getLocalPart(), address);
  }

}
