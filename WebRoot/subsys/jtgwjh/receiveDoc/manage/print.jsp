<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<title>公文打印</title>
<style type="text/css">
</style>


</head>
<body style="overflow:hidden">
<div id="returnNull"></div>
<div id="aipDiv">
<table class="" align="center" style="width:100%;height:100%;">
     <tr>
      <td class="TableData" align="center">
<input type="button" value="打印" class="BigButton" onclick="doInitPrint('0');"></input>&nbsp;
<input type="hidden" value="套打" class="BigButton" onclick="doInitPrint('1');"></input>&nbsp;
&nbsp;&nbsp;<span id="printInfo"></span>

<input type="hidden" value="" id="seqId" name="seqId"></input>

<input type="hidden" value="" id="filePath" name="filePath"></input>	    </td>

    </tr>
    <tr  style="width:100%;height:100%;">
        <td class="TableData"  style="width:100%;height:100%;">


<OBJECT id=HWPostil1  style="WIDTH:100%;HEIGHT:500px;"  classid=clsid:FF3FE7A0-0578-4FEE-A54E-FB21B277D567 codeBase='<%=contextPath %>/subsys/jtgwjh/setting/aip/HWPostil.cab#version=3.0.9.4' />
    </td>
    </tr>
    
    
</table>
<div id="11">
<input type="hidden" name="seqId" id="seqId"></input>
  <input type="hidden" name="filePath" id="filePath"></input>
</div>
</div>

</body>
</html>