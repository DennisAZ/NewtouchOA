/**
 * UserServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package yh.subsys.portal.bjca.logic.services.User;

import yh.core.global.YHSysProps;

public class UserServiceLocator extends org.apache.axis.client.Service
    implements UserService {

  public UserServiceLocator() {
  }

  public UserServiceLocator(org.apache.axis.EngineConfiguration config) {
    super(config);
  }

  public UserServiceLocator(java.lang.String wsdlLoc,
      javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
    super(wsdlLoc, sName);
  }

  // Use to get a proxy class for User
  private java.lang.String User_address = YHSysProps.getString("WEB_SERVICES_URL") + "User";

  public java.lang.String getUserAddress() {
    return User_address;
  }

  // The WSDD service name defaults to the port name.
  private java.lang.String UserWSDDServiceName = "User";

  public java.lang.String getUserWSDDServiceName() {
    return UserWSDDServiceName;
  }

  public void setUserWSDDServiceName(java.lang.String name) {
    UserWSDDServiceName = name;
  }

  public User getUser()
      throws javax.xml.rpc.ServiceException {
    java.net.URL endpoint;
    try {
      endpoint = new java.net.URL(User_address);
    } catch (java.net.MalformedURLException e) {
      throw new javax.xml.rpc.ServiceException(e);
    }
    return getUser(endpoint);
  }

  public User getUser(
      java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
    try {
      UserSoapBindingStub _stub = new UserSoapBindingStub(
          portAddress, this);
      _stub.setPortName(getUserWSDDServiceName());
      return _stub;
    } catch (org.apache.axis.AxisFault e) {
      return null;
    }
  }

  public void setUserEndpointAddress(java.lang.String address) {
    User_address = address;
  }

  /**
   * For the given interface, get the stub implementation. If this service has
   * no port for the given interface, then ServiceException is thrown.
   */
  public java.rmi.Remote getPort(Class serviceEndpointInterface)
      throws javax.xml.rpc.ServiceException {
    try {
      if (User.class
          .isAssignableFrom(serviceEndpointInterface)) {
        UserSoapBindingStub _stub = new UserSoapBindingStub(
            new java.net.URL(User_address), this);
        _stub.setPortName(getUserWSDDServiceName());
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
    if ("User".equals(inputPortName)) {
      return getUser();
    } else {
      java.rmi.Remote _stub = getPort(serviceEndpointInterface);
      ((org.apache.axis.client.Stub) _stub).setPortName(portName);
      return _stub;
    }
  }

  public javax.xml.namespace.QName getServiceName() {
    return new javax.xml.namespace.QName(
        User_address, UserWSDDServiceName);
  }

  private java.util.HashSet ports = null;

  public java.util.Iterator getPorts() {
    if (ports == null) {
      ports = new java.util.HashSet();
      ports.add(new javax.xml.namespace.QName(
          User_address, UserWSDDServiceName));
    }
    return ports.iterator();
  }

  /**
   * Set the endpoint address for the specified port name.
   */
  public void setEndpointAddress(java.lang.String portName,
      java.lang.String address) throws javax.xml.rpc.ServiceException {

    if ("User".equals(portName)) {
      setUserEndpointAddress(address);
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
