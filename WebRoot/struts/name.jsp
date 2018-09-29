<%@ taglib prefix="s" uri="/struts-tags" %> 

<html>
  <head>  <title>Enter your name</title>   </head> 

  <body>
     <s:if test="message != null">
      <font color="red">
        <s:property value="message"/>
        </font>
     </s:if>  
    
    Please enter your name:
    <s:form action = "greeting.action">
    < s:textfield name ="name"/>
    <s:submit/>
    </s:form>
  </body>
</html>
