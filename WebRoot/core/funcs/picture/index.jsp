<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%

	String picId=request.getParameter("picId");
	if(picId==null){
		picId="0";
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>图片浏览</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/ExchangeSelect.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript"><!--
var requestURL="<%=contextPath%>/yh/core/funcs/picture/act/YHPictureAct";
var picIdStr='<%=picId%>';
//alert(picIdStr);
function doInit(){

	if(picIdStr =='0'){

		var url=requestURL + "/getPicFolderInfo.act";
		var json=getJsonRs(url);
		//alert("rsText>>:"+rsText);
		if(json.rtState == '1'){
			alert(json.rtMsrg);
			return ;				
		}
		var prcsJson=json.rtData;
		if(prcsJson.length>0){
			var table=new Element('table',{ "border":"0", "cellspacing":"1",  "cellpadding":"3", "bgcolor":"#000000",  "width":"80%", "class":"TableBlock", "align":"center"})
			.update("<tbody id='tbody'><tr class='TableHeader' >"
					+"<td colspan='6' align='center'>图片目录列表</td>"				
					+"</tr><tbody>");
			$('listDiv').appendChild(table);

			var lang=prcsJson.length;
			if(lang>0){
				var count=parseInt((lang-1)/6+1,10);
				//用于控制tr
				//alert(count);
				for(var i=0;i<count;i++){
					var className = (i % 2 == 0) ? "TableLine1" : "TableLine2";
					var tr=new Element('tr',{"class":className}).update("<td width='17%' id='td_"+i+"_1'></td>"
	          +"<td width='17%' id='td_"+i+"_2'></td>"
	          +"<td width='17%' id='td_"+i+"_3'></td>"
	          +"<td width='17%' id='td_"+i+"_4'></td>"
	          +"<td width='17%' id='td_"+i+"_5'></td>"
	          +"<td width='17%' id='td_"+i+"_6'></td>"
					);
					$("tbody").appendChild(tr);
				}
				//alert(lang);		
			}
			//alert(rsText);
			//alert("count:"+count);
			//alert("lang:"+lang);
			for(var i =0;i<count;i++){
	      for(var j=1;j<=6;j++){
	        if(lang>=(i*6+j)){
	        	var prcs=prcsJson[i*6+j-1];
	        	//alert(prcs);
	    			var picId=prcs.seqId;
	    			var picName=prcs.picName;
	    			var picPath=prcs.picPath;
	    			var div = new Element('div',{"align":"center", "width":"17%" }).update("<a href='<%=contextPath%>/yh/core/funcs/picture/act/YHPictureAct/getPicInfoById.act?seqId="+ +picId +"' ><img src='<%=contextPath%>/core/funcs/picture/images/disk_share.gif' border='0'></img><br>" + picName +"</a>" );
	          $("td_"+i+"_"+j).appendChild(div);
	         }else{
		         //alert("td_"+i+"_"+j);
	        	 var div1 = new Element('div',{"align":"center", "width":"17%" }).update("&nbsp;" );
		          $("td_"+i+"_"+j).appendChild(div1);
		       }
	      }
			}
			
		}else{
			$("noFolder").show();
		}	

		
	}else{

		$("tableList").hide();

		//var url=requestURL + "/getPicFolderPathBySeqId.act?seqId=<%=picId%>";
		//var json=getJsonRs(url);
		//alert("rsText>>:"+rsText);
		//if(json.rtState == '1'){
		//	alert(json.rtMsrg);
		//	return ;				
		//}
		//var prcJson=json.rtData;
		//var picPath = prcJson.folderPath;
		//location.href= contextPath + "/core/funcs/picture/picture.jsp?picPath="+picPath  +"&seqId=<%=picId%>";		
		
		location.href="<%=contextPath%>/yh/core/funcs/picture/act/YHPictureAct/getPicInfoById.act?seqId=<%=picId%>";		
		
	}

	
	
}



--></script>


</head>
<body onload="doInit();" > 
<div id='tableList'>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath%>/core/funcs/picture/images/picture.gif" WIDTH="22" HEIGHT="20" align="middle"><span class="big3"> 图片浏览</span><br>
    </td>
    </tr>
</table> 
<br>
</div>

<div id="listDiv"></div>

<div id="noFolder" style="display:none" >
<table class="MessageBox" align="center" width="220">
	<tr>
		<td class="msg info">
		<h4 class="title">提示</h4>		
		<div class="content" style="font-size: 12pt">无目录可显示</div>
	
		</td>
	</tr>
</table>

</div>



</body>
</html>