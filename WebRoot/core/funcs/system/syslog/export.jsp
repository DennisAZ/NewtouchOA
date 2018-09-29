	<%@ page language="java" import="java.util.*" contentType="application/vnd.ms-excel;charset=GBK" pageEncoding="UTF-8"%>
	<%
	response.setHeader("Cache-control","private"); 
	response.setHeader("Cache-type","application/vnd.ms-excel"); 
	response.setHeader("Accept-Ranges","bytes");
	response.setHeader("content-Disposition","attchment;filename=dd.xlsx");
	%>
	<html xmlns:o="urn:schemas-microsoft-com:office:office" 
	 xmlns:x="urn:schemas-microsoft-com:office:excel"
	 xmlns="http://www.w3.org/TR/REC-html40">
	<head>
	<title>SWFUpload Demos - Simple Demo</title>
	<script type="text/javascript">
	function doInit(){
	 // var queryParam = $("form").serialize();
	 // var tmp = queryParam;
	//  alert("ddd");
	 // var url = contextPath+'/yh/core/funcs/system/syslog/act/YHGlSyslogAct/SysExport.act';
	 // var json = getJsonRs(url);
	 var data = <%=request.getAttribute("data")%>;
	 if(data.listData.length>0){
	  for(var i = 0; i<data.listData.length; i++){
	  var data1 = data.listData[i];
	  addRow(data1,i);
	  }
	   }   
	}
	function addRow(tmp,i){
	  var td = "<td>"+tmp.userId+"</td>"
	          + "<td>"+tmp.date+"</td>"
	          + "<td>"+tmp.Ip+"</td>"
	          + "<td>"+tmp.types+"</td>"
	          + "<td>"+tmp.remarks+"</td>";
	  var className = "TableLine2";    
	  if(i%2 == 0){
	    className = "TableLine1" ;
	    }
	  var tr = new Element("tr" , {"class" : className});
	  $('dataBody').appendChild(tr);  
	  tr.update(td);
	}
	</script>
	</head> <!-- onload="doInit()" -->
	<body>
		<table border="1" cellspacing="0" width="100%">
			<tr align="center">
				<td>用户名</td>
				<td>时间</td>
				<td>IP地址</td>
				<td>日志类型</td>
				<td>备注</td>
			</tr>
			
			
			<% 
			int k = 0;
			 List list = (List)request.getAttribute("data");
			 for(int i=0; i<list.size(); i++){
				 k++;
				   %>
				   <tr>
					   <td align="center"><%=((Map)list.get(i)).get("userName")%>  </td>
					   <td align="center"><%=((Map)list.get(i)).get("date")%>  </td>
					   <td align="center"><%=((Map)list.get(i)).get("Ip")%></td>
					   <td align="center"><%=((Map)list.get(i)).get("types")%></td>
					 <%if(((Map)list.get(i)).get("remarks")==null){%>
					       <td align="center"></td>
					 <%}else{%>
					       <td align="center"><%=((Map)list.get(i)).get("remarks")%></td>
					 <%}%>
				   </tr>
			<%}%>
			<tbody id="dataBody"> 
			</tbody>
		</table>
	</body>
	</html>