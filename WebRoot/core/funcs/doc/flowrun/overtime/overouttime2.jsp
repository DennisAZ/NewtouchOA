<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/core/inc/header.jsp" %>

<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<title>超时工作统计结果</title>
<%
String endtime =  (String)request.getAttribute("endtime");
String starttime =   (String)request.getAttribute("starttime");
%>
<script type="text/Javascript"><!--
/**
 * 超时工作统计 实现步骤方法
 一 大部:
 1 先把所有人名保存到数组
 2 在把所有流程步骤的名字保存到数组
 二大部：
 1 把头部拼好：如 名称，督查督办，(收文，测试...流程步骤)，小计
 三大部：
 1 分别把相对应名称，流程步骤， 的各个值迭代出来
 2 在根据流程步骤的值 相加求出小计
 3 在根据小计的和求出总计
 */
var k = 0;
var endtime = "<%=endtime%>";
var starttime = "<%=starttime%>";
function doInit(){
  var date = <%= request.getAttribute("date")%>;
 // alert(date.listData);
  var names = new Array();  // 纵向名称
  var namesId = new Array();  // 纵向名称的ID  用id 目的是 ：0（1）这个链接 需要用到这个名称的ID 
  date.listData.each(function(tmp,i){
    var tdname = null;
    //alert(names.length);
    /*  if (!names.length){   // 如果获得的名称不为空  就把 获得的名称放在names【0】数组中??
			names[0] = tmp.namekey;
    }*/
    var k,nameFlag = 0;
    for(k = 0; k < namesId.length; k++){ //否侧获得 多个名称  放在数组names[k]中
     // alert(namesId[]);
			if (namesId[k] == tmp.namekey.split(":")[1]){ //namesId[k] 存放的是 第几个的值
				nameFlag = 1;
			}
    }
    if (!nameFlag){         //!0 为true 所以第一步先走到这里
			names.push(tmp.namekey.split(":")[0]);
			//namesId.push(tmp.namekey.split(":")[1]);// 这个方法和下个方法一样
			namesId[k]= tmp.namekey.split(":")[1];
    }
  });
  var lcNames = new Array(); // 横向步骤名称
  date.listData.each(function(tmp,i){
    var tdname = null;
    if (!lcNames.length){
    	lcNames[0] = tmp.lcbzname;
    }
    var k,nameFlag = 0;
    for(k = 0; k < lcNames.length; k++){
			if (lcNames[k] == tmp.lcbzname){
				nameFlag = 1;
			}
    }
    if (!nameFlag){
      lcNames[k] = tmp.lcbzname;
    }
  });
  
  var titleTr = new Element('tr',{});
  titleTr.className = 'TableHeader';
  titleTr.insert(new Element('td',{nowrap:true,align:'center'}).update("姓名")); //如果名称不为空，就插入一个名称
  lcNames.each(function(e,i){    //在循环流程步骤名称 lcName 然后插入到tr  e??
    var td = new Element('td',{nowrap:true,align:'center'}); 
    td.update(e); 
    titleTr.insert(td);
  });
  titleTr.insert(new Element('td',{nowrap:true,align:'center'}).update("小计")); 
   $('dataBody').insert(titleTr);
  var sum0 = 0;
  var sum1 = 0;
  var className="";
  for(var i = 0; i < names.length ; i++){//names.length（系统管理员） 和namesId.length（233） 是相对应的如：系统管理员:233     
    className = "TableLine2";    
  	if(i%2 == 0){
  	  className = "TableLine1";
  	  }
    var tr = new Element('tr',{"class" : className});
   // tr.className = 'className';
    var tdName = new Element('td',{nowrap:true,align:'center'}).update(names[i]);
    tr.insert(tdName);   // 取得名称值
    
	  var tdRowSum = new Element('td',{nowrap:true,align:'center'});
		var rowSum0 = 0;
		var rowSum1 = 0;
		
    for(var j = 0; j < lcNames.length ; j++){
			var td = new Element('td',{nowrap:true,align:'center'}).update("0(0)"); //流程步骤为空的 先用0(0) 形式表示
			date.listData.each(function(value,index){
			  if (value.namekey.split(":")[1] == namesId[i] && value.lcbzname == lcNames[j]){// 如果从act接收过来的名称值 和 循环名称的值相同   ，步骤名称一样                 var ahref = "<a onclick='viewDetail(" +  namesId[i] + ", " + value.lcbz + ")' href='javascript:void(0)'>" + value.lcvalue + "</a>";
				 td.update(ahref);  // 就获得act 中的 流程步骤值				}
				
		  });
		  tr.insert(td); // 取得流程步骤值  	}
  	
    date.listData.each(function(value,index){
	    if(value.namekey.split(":")[1] == namesId[i]){//取得小计   如果流程步骤同属于一个人的名字（或名称的id）横向才可相加  （value.lcvalue流程步骤的值）
			  rowSum0 += value.lcvalue.replace(/\((\w+)\)/,'')*1; // 0(0) 小括号前面的 值相加   *1 代表整数   
			  rowSum1 += value.lcvalue.replace(/(\w+)\(/,'').replace(/\)/,'')*1; // 小括号后面的相加
		  }
	  });
    tdRowSum.update(rowSum0 + "(" + rowSum1 + ")");
	  sum0 += rowSum0;
	  sum1 += rowSum1;
	  tr.insert(tdRowSum);   //取得小计的值
    $('dataBody').appendChild(tr);
  }
  var tdSum = new Element('td',{nowrap:true,align:'center'}).update(sum0 + "(" + sum1 + ")"); 
  var trSum = new Element('tr',{"class" : className}).insert(new Element('td',{'align':'center','colspan':lcNames.length + 1}).update("总计"));//合并单元格
  trSum.insert(tdSum); // 获得总计
  $('dataBody').insert(trSum);
  var tdCsv = new Element('td',{'nowrap':true,'align':'left','colspan':lcNames.length+2}).insert(new Element('input',{'type':'button','value':'导出CSV', 'class':'BigButton','name':'button','onClick':'ex(alert("开发中..."))'})); 
  var trCsv = new Element('tr',{});  
  trCsv.className = 'TableControl';
  trCsv.insert(tdCsv);
  $('dataBody').insert(trCsv);
 }
function ex(){
  return false;
}

function viewDetail(userId , flowId) {
  var url = contextPath + "<%=moduleContextPath %>/flowrun/overtime/viewDetail.jsp?" 
     + "flowId=" + flowId
      + "&userId=" + userId
       + "&prcsDate1Query=" + starttime
       + "&prcsDate2Query=" + endtime;
  window.open(url);
}
function addRow(tmp,i){
    var td = "<td nowrap align=center>姓名</td>"
    + "<td nowrap align=center>" + tmp.lcbzname +"</td><br>"
    + "<td nowrap align=center>" + tmp.namekey +"</td>"
    + "<td nowrap align=center>" + tmp.lcvalue +"</td>";
	var className = "TableLine2";    
	if(i%2 == 0){
	  className = "TableLine1";
	  }
	var tr = new Element("tr" , {"class" : className});
	$('dataBody').appendChild(tr);  
	tr.update(td);
	//bindDesc([{cntrlId:"type_" + obj.seqId, dsDef:"CODE_ITEM,CLASS_CODE,CLASS_DESC,SMS_REMIND"}]); 
	for(var j = 0 ; j</b> <= k ; j++){  // 把表中的数字 转换成名字 如：1 换成 登陆日志  别忘了k的作用  日志类型的转换
	  //alert($("fromId_" + j).value); bindDesc 是写好的函数，type_ 对应上面的<input id=\"type_ 数据库中的表名
	  bindDesc([{cntrlId:"type_" +j, dsDef:"CODE_ITEM,CLASS_CODE,CLASS_DESC,SYS_LOG"}]); 
	}
}

--></script>
<%String ref = request.getHeader("REFERER");%>  <!--返回上一级页面 接收固定的格式  -->
</head>
<body onload="doInit()">
  <table id="flow_table" width="100%" class="TableList" align="center" style="border-bottom:0px;table-layout:fixed;">
  <!--  <tbody id="dataBody"></tbody>  -->

   <tbody id="dataBody"> </tbody>
</table>
  
 
  <table align="center">
<tr>
<td>
<center>
<input type="button" class="BigButton"  value="返回" onclick="javascript:window.location='<%=ref%>'">
</center></td></tr>
  </table>
  <div id="noData" align=center style="display:none">
  <table class="MessageBox" width="300">
  <tbody>
  <tr>
  <td id="msgInfo" class="msg info"> 没有检索到数据
  </td>
  </tr>
  </tbody>
  </table>
  <div><input type="button" value="返回 " class="Log_submit" onclick="javascript:window.location='<%=ref%>'"/></div>
  </div>
</body>
</html>