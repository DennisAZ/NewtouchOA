	<%@ page language="java" contentType="text/html; charset=UTF-8"
	    pageEncoding="UTF-8"%>
	<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<html>
	<head>
	<%@ include file="/core/inc/header.jsp" %>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href = "<%=cssPath%>/style.css">
	<link rel="stylesheet" href = "<%=cssPath%>/style.css">
	<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
	<title>Insert title here</title>
	<script><!--
	function doInit1(){
	  var url = contextPath+'/yh/core/funcs/system/syslog/act/YHSysYearLogSearchAct/getMySysHourLog.act';
	  var rtJson = getJsonRs(url);
	  var sum = 0;
	  if(rtJson.rtState == "0"){
		    var rtData = rtJson.rtData;
		    rtData.each(function(e,i){
		      sum += e.value;   // 将一天每个小时的值 相加
		      sum = parseInt(sum); 
		      if(sum == 0){
		        sum = 1;
		      }
		    });
		    var j=0;  var ss = new Array();
		    var yue = new Array(); 
		    var s=0; var n = 0;
		    var len8=0;
		    var len6=0; var len4=0;
		    var len2=0; var len10=0;
		    rtData.each(function(e,i){
		      var hoursTable = $('HOURS');
		      var row = hoursTable.insertRow(hoursTable.rows.length);
		      row.className = 'TableData';
		      var td1 = row.insertCell(row.cells.length);
		      td1.nowrap = true;
		      td1.align = 'center';
		      td1.width = '80';
		      var td2 = row.insertCell(row.cells.length);
		      td2.nowrap = true;
		      td2.align = 'center';
		      td2.width = '80';
		      var td3 = row.insertCell(row.cells.length);
		      td3.nowrap = true;
		      td3.align = 'center';
		      td3.width = '80';
		      //var td2 = new Element('td',{nowrap:true,align:'center',width:'80'});
		      //var td3 = new Element('td',{nowrap:true,align:'center',width:'80'});
		      //row.appendChild(td1);
		      //row.appendChild(td2);
		      //row.appendChild(td3);
		     //$('HOURS').appendChild(row);
		      var tem = Math.round(e.value/sum*100);
		      td1.innerHTML = e.hour;
		      td2.innerHTML = tem +"%";
		      td3.innerHTML = e.value;
		
		      ss[j++]=e.value;
		      for(var m = 0; m < ss.length; m++){ 
		         yue[n]= Math.round((ss[m]));
		         if(yue[n]==0){
		            yue[n]=1;
		          }
		         if(s<=yue[n]){
		           s = yue[n];  
		         }
		       }
		    });
		    len10 = s;
		    len8 = Math.round(s*0.8);
		    len6 = Math.round(s*0.6);
		    len4 = Math.round(s*0.4);
		    len2 = Math.round(s*0.2);
		    var titleTr = new Element('tr',{});
		    titleTr.className = 'TableData';
		    var td9= new Element('td',{colspan:'12'});
		    titleTr.insert(td9);
		    var table1= new Element('table',{border:0,height:'100%',cellspacing:2,cellpadding:0});
		    td9.insert(table1);
		    var td0 = new Element('tr',{});
		    var trMonth = new Element('tr',{});
		    table1.insert(td0);
		    table1.insert(trMonth);
		    var tdMonth0 = new Element('td',{align:'right'}).update('0');
		    trMonth.insert(tdMonth0);
		    var tr3 = new Element('td',{width:42,height:'0', valign:'top'})
		    td0.insert(tr3);
		    //style:'margin:-3px 0px;',火狐需要添加，ie不需要 
		    var tr0= new Element('table',{ border:0,height:'100%',width:'100%',align:'right',cellspacing:0,cellpadding:0});
		    tr3.insert(tr0); 
		    tr0.insert(new Element('tr',{}).insert(new Element('td',{width:'100%',height:'22',align:'right'}).insert('span',{style:'font-family: 宋体'}).update(len10)));
		    tr0.insert(new Element('tr',{}).insert(new Element('td',{width:'100%',height:'22',align:'right'}).insert('span',{style:'font-family: 宋体'}).update(len8)));
		    tr0.insert(new Element('tr',{}).insert(new Element('td',{width:'100%',height:'22',align:'right'}).insert('span',{style:'font-family: 宋体'}).update(len6)));
		    tr0.insert(new Element('tr',{}).insert(new Element('td',{width:'100%',height:'22',align:'right'}).insert('span',{style:'font-family: 宋体'}).update(len4)));
		    tr0.insert(new Element('tr',{}).insert(new Element('td',{width:'100%',height:'22',align:'right'}).insert('span',{style:'font-family: 宋体'}).update(len2)));
		
		    for(var i=0; i<ss.length; i++){
		        if(ss[i]==0){
		          ss[i]=1;
		         }  
		        var hei = Math.round(ss[i]*100/s); //alert(hei);
		        var td3 = new Element('td',{width:'42',height:'0',align:'center',valign:'bottom'});
		        td0.insert(td3);
		        var table2 = new Element('table',{border:0,width:15,cellspacing:0,cellpadding:0});
		        td3.insert(table2);
		        
		        var tr2 = new Element('tr',{});
		        if(s!=1){//如果等于1的话 1*100/100 每个图形都是满格
		           var td2 = new Element('td',{width:'100%',height:Math.round(ss[i]*100/s),background:'<%=imgPath%>/column.gif',valign:'bottom'});
		        }else{
		           var td2 = new Element('td',{width:'100%',height:Math.round(1),background:'<%=imgPath%>/column.gif',valign:'bottom'});
		        }
		         tr2.insert(td2);
		         table2.insert(tr2);
		         var tr1 = new Element('tr',{});
		         var td1 = new Element('td',{width:'100%',valign:'bottom',align:'center'}).insert(new Element('img',{border:0,src:'<%=imgPath%>/column.gif',width:15}));
		         tr1.insert(td1);
		         table2.insert(tr1);
	           if((i)%2 == 0){
	              var tdMonth = new Element('td',{align:'center'}).update(i+1); 
	           }else{
		             if(i==ss.length-1){
		                var tdMonth = new Element('td',{align:'center'}).update(ss.length);
		             }else{
		                var tdMonth = new Element('td',{align:'center'}).update(""); 
		           }  
	           }
		          trMonth.insert(tdMonth);
		      }
		   // $('dataBodys').update("");
		    //$('dataBodys').insert(titleTr);
	  }
	}
	--></script>
	</head>
	<body onload="doInit1()">
	<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
	  <tr>
	    <td class="Big"><img src="<%=imgPath%>/system.gif" WIDTH="22" HEIGHT="20" align="top"><span class="big3"> 时段统计</span>
	    </td>
	  </tr>
	</table>
	<br>
	<table  class="TableList" width="70%" align="center">
	  <tbody id="dataBodys"></tbody>
	</table>
	<table  class="TableList" width="70%" align="center">
	  <tr class="TableHeader" align="center">
	    <td>总访问量小时分布数据</td>
	  </tr>
	</table>
	<table class="TableList no-top-border" width="70%" align="center" id="HOURS">
	   
	</table>
	
	</body>
	</html>