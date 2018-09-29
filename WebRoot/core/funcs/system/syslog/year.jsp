	<%@ page language="java" contentType="text/html; charset=UTF-8"
	    pageEncoding="UTF-8"%>
	 <%@ page import="java.text.DateFormat,java.text.SimpleDateFormat,java.util.Date" %>   
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
	<script type="text/javascript" src="<%=contextPath %>/core/funcs/system/syslog/js/sysyearlog.js"></script>
	<title>年度数据</title>
	<script>
	function initSelectOption(element,rtJson){ 
	  rtJson.rtData.each(function(e,i){ 
	    element.options.add(new Option(e.year + '年',e.year)); //year 是后台act集合中的对象
	  }); 
	  fillSel(); // 填充函数
	}
	function fillSel(){
	  var date = new Date();
	 // if(top.Ext.isIE) //EXT 判斷ie 
	if(top.$.browser.msie)	 //jquery 判断ie 
	  $("YEAR").value = date.getYear(); // 加载页面时 显示出当前的年份
	 // if(top.Ext.isGecko) //EXT 判斷火狐
	if(top.$.browser.mozilla) //jquery 判断火狐
	  $("YEAR").value = date.getYear()+1900;
	  $("MONTH").value = ((date.getMonth()+1)<10)? ('0'+(date.getMonth()+1)):date.getMonth()+1;//加载页面时 显示出当前的月份
	  change_date2(); //加载这个函数 目的是  让年份所有月份的显示出来  
	}
	function doInit4(){ 
	  selectYear();
	}
	//统计下拉列表中  有多少年（在系统日志第2个标签页）	
	function selectYear(){
	  var url = contextPath+'/yh/core/funcs/system/syslog/act/YHSysYearLogSearchAct/getMySysLog.act';
	  var rtJson = getJsonRs(url);
	  if(rtJson.rtState == "0"){
	    initSelectOption($('YEAR'),rtJson);
	  // $('YEARS').innerHTML = $F('YEAR') + '年度按月访问数据'; //当选中下拉菜单时 ，按月访问数据年份跟着变 
	} 
	  }
	function change_date2(){
	  //alert($F('YEAR'));
	  var year = $F('YEAR');
	  var month = $F('MONTH');
	  var par = "year="+ year +"&month="+month;
	  var url = contextPath+'/yh/core/funcs/system/syslog/act/YHSysYearLogSearchAct/getMySysYearLog.act';
	  var rtJson = getJsonRs(url,par);
	  if(rtJson.rtState == "0"){
	    var rtData = rtJson.rtData;
	    var sum = rtData.sum; // 获取12个月的总量
	   var yue = new Array(); 
	   var ss = new Array();
	   var s=0;
	   var i=0;
	   var len8=0;
	   var len6=0;
	   var len4=0;
	   var len2=0; var len10=0;
	   for(i = 0; i < rtData.months.length; i++){
	     yue[i]= Math.round((rtData.months[i].month));
	     if(yue[i]==0){
	       yue[i]=1;
	      }
	     if(s<=yue[i]){
	      s = yue[i];   
	     }
	     len10 = s;
	     len8 = Math.round(s*0.8);
	     len6 = Math.round(s*0.6);
	     len4 = Math.round(s*0.4);
	     len2 = Math.round(s*0.2);
	   }  
	   var titleTr = new Element('tr',{});
	   titleTr.className = 'TableData';
	   var td9= new Element('td',{colspan:'12'});
	   titleTr.insert(td9);
	   var table1= new Element('table',{border:0,height:'100%',cellspacing:2,cellpadding:0});
	   td9.insert(table1);
	   var td0 = new Element('tr',{});
	   var trMonth = new Element('tr',{});
	   table1.insert(td0);
	   table1.insert(trMonth);// ...
	   var tdMonth0 = new Element('td',{align:'right'}).update('0');// ...
	 	 trMonth.insert(tdMonth0);// ...
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
	  for(var j = 0; j<12; j++){
	  if(yue[j]==0){
	    yue[j]=1;
	    }
	  var hei = Math.round(yue[j]*100/s); //alert(hei);
	  var td3 = new Element('td',{width:'55',height:'0',align:'center',valign:'bottom'});
	  td0.insert(td3);
	  var table2 = new Element('table',{border:0,width:15,cellspacing:0,cellpadding:0});
	  td3.insert(table2);
	  
	  var tr2 = new Element('tr',{});
	   var td2 = new Element('td',{width:'100%',height:Math.round(yue[j]*100/s),background:'<%=imgPath%>/column.gif',valign:'bottom'});
	   tr2.insert(td2);
	   table2.insert(tr2);
	
	   var tr1 = new Element('tr',{});
	   var td1 = new Element('td',{width:'100%',valign:'bottom',align:'center'}).insert(new Element('img',{border:0,src:'<%=imgPath%>/column.gif',width:15}));
	   tr1.insert(td1);
	   table2.insert(tr1);
	
	   var tdMonth = new Element('td',{align:'center'}).update( j + 1 + '月');// ...
	  	trMonth.insert(tdMonth);//pang ...
	  }
	  //$('dataBody').update("");
	  //$('dataBody').insert(titleTr); //.insert(new Element('&nbsp',{}))
	   var one = rtData.months[0].month; // 在act中是按顺序取得， 这里按顺序获取	
	   var two = rtData.months[1].month;
	   var three = rtData.months[2].month;
	   var four = rtData.months[3].month;
	   var five = rtData.months[4].month;
	   var six = rtData.months[5].month;
	   var senver = rtData.months[6].month;
	   var eith = rtData.months[7].month;
	   var nine = rtData.months[8].month;
	   var ten = rtData.months[9].month;
	   var elwen = rtData.months[10].month;
	   var telwen = rtData.months[11].month;
	   if(sum==0){
	     sum=1;
	     }
	   var one1 = one*100/sum; 
	   var two1 = two*100/sum;
	   var three1 = three*100/sum;
	   var four1 = four*100/sum;
	   var five1 = five*100/sum;
	   var six1 = six*100/sum;
	   var senver1 = senver*100/sum;
	   var eith1 = eith*100/sum;
	   var nine1 = nine*100/sum;
	   var ten1 = ten*100/sum;
	   var elwen1 = elwen*100/sum;
	   var telwen1 = telwen*100/sum;
	   var one1 = Math.round(one1);
	   var two1 = Math.round(two1);
	   var three1 = Math.round(three1);
	   var four1 = Math.round(four1);
	   
	   var five1 = Math.round(five1);
	   var six1 = Math.round(six1);
	   var senver1 = Math.round(senver1);
	   var eith1 = Math.round(eith1);
	   
	   var nine1 = Math.round(nine1);
	   var ten1 = Math.round(ten1);
	   var elwen1 = Math.round(elwen1);
	   var telwen1 = Math.round(telwen1);

	   $('January').innerHTML = one1+'%';
	   $('February').innerHTML =two1+'%';
	   $('March').innerHTML =three1+'%';
	   $('April').innerHTML =four1+'%';
	   $('May').innerHTML = five1+'%';
	   $('June').innerHTML =six1+'%';
	   $('July').innerHTML =senver1+'%';
	   $('August').innerHTML = eith1+'%';
	   $('September').innerHTML = nine1+'%';
	   $('Octorber').innerHTML = ten1+'%';
	   $('November').innerHTML =elwen1+'%';
	   $('December').innerHTML = telwen1+'%';
	   
	   $('Jan').innerHTML = one;
	   $('Feb').innerHTML =two;
	   $('Mar').innerHTML =three;
	   $('Ap').innerHTML =four;
	   $('Ma').innerHTML = five;
	   $('Jun').innerHTML =six;
	   $('Jul').innerHTML =senver;
	   $('Aug').innerHTML = eith;
	   $('Sep').innerHTML = nine;
	   $('Octor').innerHTML = ten;
	   $('Nove').innerHTML =elwen;
	   $('Dece').innerHTML = telwen;
	  }
	  $('YEARS').innerHTML = $F('YEAR') + '年度按月访问数据';//当选中下拉菜单年  时 ，年份跟着变化
	  $('MONTHS').innerHTML = $F('MONTH') + '月份按日访问统计'; // 同时月份也跟着变	
	 // $('NIAN').innerHTML = $F('YEAR')+ '年度按月访问统计';
	 // $('MONTH1').innerHTML = $F('MONTH') + '月份按日访问数据';
	  change_date4(); //年份的下拉列表 调用月份的下拉列表， 说明年份变 月份也跟着变 
	}
	</script>
	</head>
	<body onload="doInit4()">
	<form  name="form1" id="form1">
	<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
	  <tr>
	    <td class="Big" width="100"><img src="<%=imgPath%>/system.gif" WIDTH="22" HEIGHT="20" align="top"><span class="big3"> 年度数据</span>
	    </td>
	    <td>
	    <select name="YEAR" id="YEAR" class="SmallSelect" onchange="change_date2();">
	    <!--  <option value="2010" selected>2010年</option>-->
	    </select> 
	    <% 
	    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
	    Date date = new Date();
	   String  timemonth = sdf.format(date);
	    timemonth = timemonth.substring(5,7);
	    %>
	    <select name="MONTH" id="MONTH" class="SmallSelect" onchange="change_date4();">
	      <option value="01"  selected >1月</option>
	      <option value="02" selected>2月</option>
	      <option value="03" selected>3月</option>
	      <option value="04" selected>4月</option>
	      <option value="05" selected>5月</option>
	      <option value="06" selected>6月</option>
	      <option value="07" selected>7月</option>
	      <option value="08" selected>8月</option>
	      <option value="09" selected>9月</option>
	      <option value="10" selected>10月</option>
	      <option value="11" selected>11月</option>
	      <option value="12" selected>12月</option>
	    </select> 
	    </td>
	  </tr>  
	</table>
	
	<table width="70%" align="center">
	<!-- 
	<tr class="TableHeader" align="center">
	    <td id="NIAN">年度按月访问统计</td>
	  </tr>  -->
	<tbody id="dataBody"> </tbody> 
	</table>
	<br>
	<!--<a href="javascript:alert($F('YEAR'))">dd</a>  -->
	 </form>
	<table class="TableList" width="70%" align="center">
	   <tr class="TableHeader">
	      <td nowrap colspan="3" align="center" id="YEARS" name="YEARS">年度按月访问数据</td>
	    </tr>
	    <tr class="TableData">
         <td nowrap align="center" width="80">1月</td>
         <td nowrap align="center" id="January"></td>
         <td nowrap align="center" width="100" id="Jan"></td>
	    </tr>
	    <tr class="TableData">
         <td nowrap align="center" width="80">2月</td>
         <td nowrap align="center" id="February"></td>
         <td nowrap align="center" width="100" id="Feb"></td>
	    </tr>
	    <tr class="TableData">
         <td nowrap align="center" width="80">3月</td>
         <td nowrap align="center" id="March"></td>
         <td nowrap align="center" width="100" id="Mar"></td>
	    </tr>
	    <tr class="TableData">
         <td nowrap align="center" width="80">4月</td>
         <td nowrap align="center" id="April"></td>
         <td nowrap align="center" width="100" id="Ap"></td>
	    </tr>
	    <tr class="TableData">
         <td nowrap align="center" width="80">5月</td>
         <td nowrap align="center" id="May"></td>
         <td nowrap align="center" width="100" id="Ma"></td>
	    </tr>
	    <tr class="TableData">
         <td nowrap align="center" width="80" >6月</td>
         <td nowrap align="center" id="June"></td>
         <td nowrap align="center" width="100" id="Jun"></td>
	    </tr>
	    <tr class="TableData">
	            <td nowrap align="center" width="80">7月</td>
	            <td nowrap align="center" id="July"></td>
	            <td nowrap align="center" width="100" id="Jul"></td>
	    </tr>
	    <tr class="TableData">
	            <td nowrap align="center" width="80">8月</td>
	            <td nowrap align="center" id="August"></td>
	            <td nowrap align="center" width="100" id="Aug"></td>
	    </tr>
	    <tr class="TableData">
	            <td nowrap align="center" width="80">9月</td>
	            <td nowrap align="center" id="September"></td>
	            <td nowrap align="center" width="100" id="Sep"></td>
	    </tr>
	    <tr class="TableData">
	            <td nowrap align="center" width="80">10月</td>
	            <td nowrap align="center" id="Octorber"></td>
	            <td nowrap align="center" width="100" id="Octor"></td>
	    </tr>
	    <tr class="TableData">
	            <td nowrap align="center" width="80">11月</td>
	            <td nowrap align="center" id="November"></td>
	            <td nowrap align="center" width="100" id="Nove"></td>
	    </tr>
	    <tr class="TableData">
	            <td nowrap align="center" width="80">12月</td>
	            <td nowrap align="center" id="December"></td>
	            <td nowrap align="center" width="100" id="Dece"></td>
	    </tr>
	</table><br>
	<table width="70%" align="center"> 
		 <!--  
		<tr class="TableHeader" align="center">
		    <td id="YUE">月份按日访问统计</td>
		</tr> 
		 -->
		<tbody id="dataBodys"> </tbody>
	</table>
	<br>
  <table width="70%" align="center">
	  <tr class="TableHeader" align="center">
	    <td id="MONTHS">月份按日访问数据</td>
	  </tr>
  </table>
	<table class="TableList" width="70%" align="center" id ="DAYS">
		<!-- 
		  <tr class="TableHeader" align="center">
		    <td id="MONTHS" name="MONTHS">03月份按日访问数据</td>
		  </tr>
		 -->
	</table>
	</body>
	</html>