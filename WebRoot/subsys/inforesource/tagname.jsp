<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<html>
<% 
	String nodeType = request.getParameter("nodeType");
  if(nodeType == null || nodeType == ""){
    nodeType = "1";
  } 
%>	
	<title>自定义标签</title>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=Utf-8">
	    <script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
			<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
			<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
			<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
	  	<link rel="stylesheet" href = "<%=cssPath%>/style.css">	  
	  	
	  	<script type="text/javascript">
	  	
	  	    function toSave(){			    	    	
						var tagname = document.getElementById("tag").value;
						var nodes = window.opener.getNode();                  //从父元素传过id串来								
						var taglen = $("tag").value;	
						if(nodes==null || nodes.length <1){
							alert("请选择树节点！");
							return;
						}	else if(taglen.length>4){
						  alert("自定义标签长度不能超过4个");
						  $("tag").value = "";
						  $("tag").focus();
							return;
						}else if(taglen == null || taglen.length <1){
						  alert("请输入标签");
						  $("tag").focus();
							return;
						}else{			
							var param = "tagname="+tagname+"&nodes="+nodes +"nodeType="+ <%=nodeType%>;
							var url = contextPath + "/yh/subsys/inforesouce/act/YHMateNodeAct/saveNode.act";				
							var jtson = getJsonRs(url, param);												
							if(jtson.rtState=="0"){                               // 如果保存成功，刷新父页面，自己关闭
							  window.opener.refreshContent();
								window.close();                                      //刷新content.jsp, 窗口关闭
							}else{                                                 //失败
							  alert("保存失败！");
							  return false;
							}		  
			  	  }
	  	    }

		  	  function saveAjax(url, param){		  	 
						return getJsonRs(url, param);
				  }
	  	</script>
	</head>
  <body>
          <center>请输入自定义标签(1-4个字)：</center><br>
          <center><TABLE class="atb atb2" width="92%"> 
	          <tr align='center'>
	             <td colspan ='2'><input type="text" id="tag" name="tag" value="标签"></td>
	          </tr>
	           <tr align='center'>
	             <td colspan ='2'><input type="button" id="ok" name="ok" class="BigButton" onclick="javascript:toSave();return false;" value="确定">
	             <input type="button" id="fail" name="fail" class="BigButton"  value="取消" onclick="window.close();"></td>
	           </tr>
	  	      </TABLE>  	
  	     </center>
  </body>
</html>