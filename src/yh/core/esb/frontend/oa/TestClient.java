package yh.core.esb.frontend.oa;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;

import yh.core.esb.common.util.YHEsbUtil;

public class TestClient
{
    private static void invokeWebService()
    {
        try
        {
            String soapBindingAddress = "http://192.168.0.178/webservice/server.php/ESBMessage?WSDL";
            
            EndpointReference endpointReference = new EndpointReference(soapBindingAddress);
            
            //创建一个OMFactory，下面的namespace、方法与参数均需由它创建
            OMFactory factory = OMAbstractFactory.getOMFactory();
            
            //下面创建命名空间，如果你的WebService指定了targetNamespace属性的话，就要用这个
            //对应于@WebService(targetNamespace = "http://www.mycompany.com")
            OMNamespace namespace = factory.createOMNamespace("http://192.168.0.178/webservice/ESBMessage", "xsd");

            //下面创建的是参数对数，对应于@WebParam(name = "name")
            //由于@WebParam没有指定targetNamespace，所以下面创建name参数时，用了null，否则你得赋一个namespace给它
            OMElement nameElement = factory.createOMElement("name", null);
            nameElement.addChild(factory.createOMText(nameElement, "java"));

            //下面创建一个method对象，"test"为方法名
            OMElement method = factory.createOMElement("recvMessage", namespace);
            method.addChild(nameElement);
            
            Options options = new Options();
            options.setAction("urn:TD_ESBMessage/config");  //此处对应于@WebMethod(action = "http://www.mycompany.com/test")
            options.setTo(endpointReference);

            ServiceClient sender = new ServiceClient();
            sender.setOptions(options);

            //下面的输出结果为<xsd:test xmlns:xsd="http://www.mycompany.com"><name>java</name></xsd:test>
            YHEsbUtil.println(method.toString());

            //发送并得到结果，至此，调用成功，并得到了结果
            OMElement result = sender.sendReceive(method);

            //下面的输出结果为<ns2:testResponse xmlns:ns2="http://www.mycompany.com"><greeting>hello java</greeting></ns2:testResponse>
            YHEsbUtil.println(result.toString());
        }
        catch (AxisFault ex)
        {
            ex.printStackTrace();
        }

    }

    public static void main(String[] args)
    {
        TestClient.invokeWebService();
    }
} 