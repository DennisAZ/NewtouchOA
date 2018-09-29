<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<html>
	<head>
	<%@ include file="/core/inc/header.jsp" %>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
	<link rel="stylesheet" type="text/css" href="/inc/js/jquery/page/css/page.css"/>
	<link rel="stylesheet" type="text/css" href="/theme/<?=$LOGIN_THEME?>/calendar.css"/>
	<link rel="stylesheet" href = "<%=cssPath%>/style.css">
	<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
	<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
	<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
	<title>系统日志查询</title>
	<script type="text/Javascript"><!--
	//全选实现功能	 function checkAll(field) {
	   var deleteFlags = document.getElementsByName("run_select") ;
	   for(var i = 0 ; i < deleteFlags.length ; i++) {
	 	    deleteFlags[i].checked = field.checked ;
	   }
	 }
	 // 全选按钮当全选后  取消某行后   全选按钮响应 
	 function check_one(el){
	   if(!el.checked){   
	     //document.getElementsByName("allbox").checked =false;
	     document.all("allbox").checked=false;
	   }
	 }
	/*
	 function delete_mail(pageIndex,showLength)
	 {  var deleteAllFlags = document.getElementsByName("deleteFlag");
	 var delete_str="";
	 for(i=0;i<deleteAllFlags.length;i++)
	 {if(deleteAllFlags[i].checked) {
	 		  delete_str += deleteAllFlags[i].value + "," ;   //SMS_BODY  seqID
	 		  }	}
	 if(delete_str=="")
	 {alert("要删除，请至少选择其中一条。");
	    return;}
	 msg='确认要删除所选吗？';
	 if(window.confirm(msg))
	 {    var par = 'pageIndex='+pageIndex+'&showLength=' +showLength+'&delete_str=' +delete_str;
	     var url =contextPath+'/yh/core/funcs/news/act/YHNewsHandleAct/deleteCheckNews.act';
	 	  var json = getJsonRs(url,par);
	 	  if (json.rtState == "0"){
	 		  window.location.reload();} else{
	 	      alert(json.rtMsrg);}}}
	 	      */
	//删除所选日志	
	function delete_mail(){
		var deleteFlags = document.getElementsByName("run_select");
		var delete_str = ""; 
		for(var i = 0; i < deleteFlags.length; i++){
		  if(deleteFlags[i].checked){
		     delete_str += deleteFlags[i].value + ",";
		    }
		 } 
		if(delete_str == ""){
		  alert("要删除日志，请至少选择其中一条");
		  return;
		}
		msg='确认要删除所选日志吗？';
		if(window.confirm(msg)){
			  var par = 'delete_str='+delete_str;
			  var url = contextPath+'/yh/core/funcs/system/syslog/act/YHGlSyslogAct/deleteChecklog.act';
			  var json = getJsonRs(url, par); 
			  if (json.rtState == "0"){
					 window.location.reload();// 删除后返回到 当前页面
			  } else{
	         alert(json.rtMsrg);
	      }
		}
  }
	 var k = 0;
	 var data = null;
	function doInit(){
	 data = <%= request.getAttribute("data")%>;  //获得act中data的对象		//alert(data.listData.length);
		if(data.listData.length > 0){  
			for(var i = 0; i < data.listData.length; i ++){
			  var data1 = data.listData[i];
			  addRow(data1, i);
			  k ++;
			  }
			for(var m = 0 ; m <k ; m++){  // 把表中的数字 转换成名字 如：233 换成 系统管理员  别忘了k的作用
				 // if($("fromId_" + m).value.trim()!=null){
				 // bindDesc([{cntrlId:"fromId_"+ m, dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
				//}else{
				//  bindDesc([{cntrlId:"fromId_"+ m+"1", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
				//}
			
			
			   var userName = data.listData[m].userName;
	       if(!userName){
	         bindDesc([{cntrlId:"fromId_" + m, dsDef:"PERSON,SEQ_ID,USER_NAME"}]); 
	       }else{
	         $("fromId_" + m + "Desc").update(userName);
	       }
				}
				
				for(var j = 0 ; j < k ; j++){  // 把表中的数字 转换成名字 如：1 换成 登陆日志  别忘了k的作用  日志类型的转换				
					  //bindDesc 是写好的函数，type_ 对应上面的<input id=\"type_ 数据库中的表名					 //if($("type_" + j).value.trim()!=null){
					  bindDesc([{cntrlId:"type_"+j, dsDef:"CODE_ITEM,CLASS_CODE,CLASS_DESC,SYS_LOG"}]); 
					//}else{
					 // bindDesc([{cntrlId:"type_"+j, dsDef:"CODE_ITEM,CLASS_CODE,CLASS_DESC,SYS_LOG"}]);
					//}
				  
				}
		   $('yesData').show();
		} else {
			$('flow_table').hide();
			$('Table').hide(); 
		  $('noData').show();
		  $('msgInfo').update('没有检索到数据');
		}
	}
	function addRow(tmp,i){
	  // value 值 就是我们要显示的值，Desc 就是在span中显示出来		  var td = "<td nowrap align=center><input type=checkbox name=run_select onClick=check_one(this) value='"+ tmp.seqId +"'></td>"
		  if(tmp.userId=='0'){
		      td+= "<td nowrap align=center>" + tmp.userId + "</td>";
		  }else{
		      td+=  "<td nowrap align=center><input id=\"fromId_"+k+"\" type=hidden value="+ tmp.userId +"><span id=\"fromId_"+k+"Desc\" ></span></td>";
		  }

		  td+= "<td nowrap align=center>" + tmp.date +"</td>";
		  td+= "<td nowrap align=center>" + tmp.Ip +"</td>";
		  td+= "<td nowrap align=center><input id=\"type_"+k+"\" type=hidden value=\""+tmp.types +"\"><span id=\"type_"+k+"Desc\"></span></td>";
      var remark = tmp.remarks;
      
      if(remark.length > 15){ 
        var tmk = remark.substring(0,14);
        var las = remark.substring(0,remark.length);
       //  alert(tmk);
        td += "<td nowrap align=center><a href=javascript:; title='" +las + "' style='cursor:text;'>" + tmk.replace('null','') +"</a></td>";
      }else{
       td += "<td nowrap align=center>" + tmp.remarks.replace('null','') +"</td>";
      }
		var className = "TableLine2";    
		if(i%2 == 0){
		  className = "TableLine1" ;
		}
		var tr = new Element("tr" , {"class" : className});
		$('dataBody').appendChild(tr);  
		tr.update(td);
	}
	
	
	--></script>
	<%String ref = request.getHeader("REFERER");%>  <!--返回上一级页面 接收固定的格式  -->
	</head>
	<body onload="doInit();">
	
	<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
	  <tr>
	    <td class="Big"><img src="<%=imgPath%>/system.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 查询结果 （最多显示300条记录）</span>
	    </td>
	  </tr>
	</table>
	 
	 <table id="flow_table" width="100%" class="TableList" style="table-layout:fixed;">
		 <tbody>
		  <tr class="TableHeader">
			   <td nowrap align="center" width="40">选择</td> 
		     <td nowrap align="center" width="90">用户姓名</td>
		     <td nowrap align="center">时间 </td>
		     <td nowrap align="center">IP地址</td>
		     <!--   <td nowrap align="center">IP所在地</td> -->
		     <td nowrap align="center">日志类型</td>
		     <td nowrap align="center">备注</td>
		  </tr>
		 </tbody>
		   <!--  <tbody id="dataBody"></tbody>  -->
		
		  <tbody id="dataBody"> </tbody>
	 </table>
	  <table class="TableList" id="Table" style="display:none;" border=0 width="100%" style="margin:0;">
		  <tr class="TableControl">
			  <td colspan="9">
				 	&nbsp;<input type="checkbox" name="allbox" id="allbox_for" onClick="javascript:checkAll(this);">
				  <label for="allbox_for" style="cursor:pointer"><u><b>全选</b></u></label> &nbsp;
				 
				 <input type="button"  value="删除" class="BigButton" onClick="delete_mail();" title="删除所选日志"> &nbsp;
			  </td>
		  </tr>
	  </table>
	  
	  <div id="yesData" align=center style="display:none">  
	  <table align="center">
			<tr>
				<td>
					<center>
					   <input type="button" class="BigButton"  value="返回" onclick="javascript:window.location='<%=ref%>'">
					</center>
				</td>
			</tr>
	  </table>
	  </div>
	  <div id="noData" align=center style="display:none">
	  <table class="MessageBox" width="300">
			  <tbody>
					  <tr>
						  <td id="msgInfo" class="msg info"> 没有检索到数据
						  </td>
					  </tr>
			  </tbody>
	  </table>
	  <div><input type="button" value="返回 " class="BigButton" onclick="javascript:window.location='<%=ref%>'"/></div>
	  </div>
	</body>
	</html>