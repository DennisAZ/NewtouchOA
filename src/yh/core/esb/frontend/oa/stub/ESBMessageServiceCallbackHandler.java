
/**
 * ESBMessageServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.4  Built on : Dec 19, 2010 (08:18:42 CET)
 */

    package yh.core.esb.frontend.oa.stub;

    /**
     *  ESBMessageServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class ESBMessageServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public ESBMessageServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public ESBMessageServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for recvMessage method
            * override this method for handling normal response from recvMessage operation
            */
           public void receiveResultrecvMessage(
                    yh.core.esb.frontend.oa.stub.ESBMessageServiceStub.RecvMessageResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from recvMessage operation
           */
            public void receiveErrorrecvMessage(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for updateState method
            * override this method for handling normal response from updateState operation
            */
           public void receiveResultupdateState(
                    yh.core.esb.frontend.oa.stub.ESBMessageServiceStub.UpdateStateResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from updateState operation
           */
            public void receiveErrorupdateState(java.lang.Exception e) {
            }
                


    }
    