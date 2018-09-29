<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()
+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>销售指标设置</title>
    <link rel="shortcut icon" href="favicon.ico"/>   
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <style type="text/css">
		.autoAddTh{
			background-color:#4e80c9;
			font-size:12px;
		 	font-weight:500;
		 	color: #FFFFFF;
		}
		
		.whiteBack{
			background-color:#fff;
			text-align:center !important;
		}

    </style>
    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
	<script type="text/javascript" src="js/formCheck.js"></script>
    <script type="text/javascript" >
	//获取服务器时间：
	var mon=parseInt('${month}');
	var yea=parseInt('${year}');
	  //------------------ajax动态加载原有数据---------------------------
	  function findTarget(code,r){
		  var url = "statAction.do"; 
		  var pars = "op=loadTarget&datecount="+r+"&userCode="+code+"&ran=" + Math.random();
		  new Ajax.Request(url,{
			method:'get',
			parameters: pars,
			onSuccess:function(transport){
				var docXml = transport.responseXML;
				if(docXml!=null||docXml!=""){
					var count=$("trCount");
					if(count!=null&&count.value!=0){
						for(var i=0;i<parseInt(count.value);i++){
							var root=docXml.getElementsByTagName("salAll"+i+"Task")[0];
							if(root!=null){
								var u=root.getAttribute("userCode");
								var satHtMon=root.getAttribute("satHtMon");
								var satPaidMon=root.getAttribute("satPaidMon");
								var satCusNum=root.getAttribute("satCusNum");
								var satId=root.getAttribute("satId");
								
								if(satHtMon!='null'&&satHtMon!="0.0")
									$(u+"hm"+i).value=satHtMon;
								if(satPaidMon!='null'&&satPaidMon!="0.0")
									$(u+"pa"+i).value=satPaidMon;
								if(satCusNum!='null'&&satPaidMon!="0")
									$(u+"cu"+i).value=satCusNum;
								if(satId!='null')
									$(u+"id"+i).value=satId;
							}	
						}
					}
				}
			},
			onFailure:function(transport){
				if (transport.status == 404)
					alert("您访问的url地址不存在！");
				else
					alert("Error: status code is " + transport.status);
			}
		  });
	   }
	

	function addOption(){
           var obj=$('ryear');
		   var obj2=$('tyear');
		   for(var i=0;i<2;i++){
		   	var text=(yea+i)+"年";
		   	obj.options.add(new Option(text,(yea+i)));
			obj2.options.add(new Option(text,(yea+i)));
		   }  
		   $('rmonth').value=mon;
		   $('tmonth').value=mon;
     }
	 
	
	//增加员工行
	function addTbRow(code,name){ 
		var userTb=$("userTb");
		var count=$("trCount");
		var delTb=$("delTb");
		//alert("时间段个数："+count.value)
		if(count!=null&&count.value!=0){
			var userCode=document.getElementsByName("userCode");
			for(var i=0; i<userCode.length;i++){
				if(userCode[i].value==code){
					alert("您已选择过该人员！");
					return ;
				}
			}
			//保存按钮可见
			$("dosave").disabled=false;
			
			var row =userTb.insertRow(userTb.rows.length); 
			var lenght = userTb.rows.length-1;
			var col = row.insertCell(0);
			col.innerHTML = "<span title='"+name+"' class='textOverflow'>"+name+"</span>";
			
			var row2 =delTb.insertRow(delTb.rows.length);
			var col2 = row2.insertCell(0);
			col2.innerHTML = "<img src='images/content/del.gif' onClick='delTable(this)' alt='删除' style='cursor:pointer'/><input style='display:none' type='checkbox' name='userCode' checked='checked' value="+code+">";
			col2.style.borderRight="0px";
			
			//alert("时间"+tb) 
			for(var k=0;k<parseInt(count.value);k++){
				var tb=$("dateTb"+k);
				var row =tb.insertRow(tb.rows.length);
				//var i = tb.rows.length - 3;
				var col = row.insertCell(0);
				col.className='whiteBack';
				col.innerHTML = "<input type='text' id='"+code+"hm"+k+"' name='"+code+"hm"+k+"' onKeyUp='checkIsNum(this)' onblur='checkIsNum(this)' class='inputSize2' style='width:90%'>";
				
				col = row.insertCell(1);
				col.className='whiteBack';
				col.innerHTML = "<input type='text' id='"+code+"pa"+k+"' name='"+code+"pa"+k+"' onKeyUp='checkIsNum(this)' onblur='checkIsNum(this)'  class='inputSize2' style='width:90%'>";
				
				col = row.insertCell(2);
				col.className='whiteBack';
				col.innerHTML = "<input type='text' id='"+code+"cu"+k+"' name='"+code+"cu"+k+"' onKeyUp='checkIsInt(this)' onblur='checkIsInt(this)'  class='inputSize2' style='width:90%'><input style='display:none' type='hidden' id='"+code+"id"+k+"' name='"+code+"id"+k+"'>";
			}
			findTarget(code,count.value);//加载原有数据
		}else{
			alert("请先选择销售指标的时间段以生成表格！")
		}
	} 
	
	function delTable(len){
		var userTb=$("userTb");
		var count=$("trCount");
		var delTb=$("delTb");
		if(count!=null&&count.value!=0){
			var i=len.parentNode.parentNode.rowIndex;
			if(confirm("确定删除这条数据吗?")){
				userTb.deleteRow(i);
				delTb.deleteRow(i);
				for(var k=0;k<parseInt(count.value);k++){
					var tb=$("dateTb"+k);
					tb.deleteRow(i);
				}
			}
		}
	}
	
	//得到下拉框选中值
	function GI(obj){
		return $(obj).options[$(obj).selectedIndex].value;
	}
	//生成表格 ryear开始 tyear结束
	function getTrCount(){
		var n=0;
		var startY = parseInt(GI('ryear'));
		var startM = parseInt(GI('rmonth'));
		var endY = parseInt(GI('tyear'));
		var endM = parseInt(GI('tmonth'));
		if(startY==endY){
			if(endM>=startM){
				n=(endM-startM)+1;
				
			}
			else{
				n=(startM-endM)+1;
				startM = endM;
			}
		}
		else if(endY>startY){
			n=((endY-startY)*12-startM)+endM+1;
		}
		else{
			n=((startY-endY)*12-endM)+startM+1;
			startY = endY;
			startM = endM;
		}

		
		//用户table
		var userTb=$("userTb");
		if(startY<yea){
			alert("只能设置今后的销售指标！")
			return false;
		}
		if(startY==yea&&startM<mon){
			alert("只能设置今后的销售指标！")
			return false;
		}
		if(userTb.rows.length>2){
			alert("正在设置销售指标中，如要改变时间段请先保存")
			return false;
		}else if(n>12){
			alert("时间段最长为12个月！")
			return false;
		}else{
			//插入日期
			$("trCount").value=n;
			//删除table
			var tbName=$("insertTb");
			var o=tbName.rowIndex;
			tbName.deleteRow(o);
			
			var row =tbName.insertRow(tbName.rows.length);
			
			var mm=startM;
			for(var j=0;j<n;j++){
				var col = row.insertCell(j);
				var tbDate;
				if(mm>12){
					mm=(mm-12);
					startY=startY+1;
				}
				//格式化
				if(mm<10){
					tbDate=startY+"-0"+mm;
				}
				else{
					tbDate=startY+"-"+mm;
				}
				
				var innerhtml=
				"<table class='normal rowstable' id='dateTb"+j+"' style='width:100%; border-top:0;' cellpadding='0' cellspacing='0'>"+
					"<tr>"+
						"<td colspan='3' class='autoAddTh' style='text-align:center'><span id='time"+j+"'></span></td>"+
					"</tr>"+
					"<tr>"+
						"<th><nobr>目标销售额</nobr></th>"+
						"<th><nobr>目标回款额</nobr></th>"+
						"<th style='border-right:0px'><nobr>签单数<nobr></th>"+
					"</tr>"+
				"</table>";
				col.style.padding = "0 0 0 2px";
				col.innerHTML =innerhtml;
				$("time"+j).innerText=tbDate;	
				
				mm++;
			}
		}
	}
	
	 function check(){
	 	if(userTb.rows.length>2){
			waitSubmit("dosave","保存中...");
	 		return $("tgform").submit();
		}
		else{
			alert("您还没有选择人员！");
			return;
		}
	 	
	 }
	 
	 
	 createProgressBar();	
	 window.onload=function(){
	 	
	 	//表格内容省略
		loadTabShort("userTb");
	 	addOption();
		closeProgressBar();
	 }
	</script>
</head>
  <body>
  <div id="mainbox" >
    	<div id="contentbox">
        	<div id="title">销售管理 > 销售指标</div>
            <table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                        <div id="tabType">
							<div id="tabType1" class="tabTypeBlue1" onMouseOver="changeTypeBg(this,1,1)" onMouseOut="changeTypeBg(this,0,1)" onClick="window.location.href='statAction.do?op=showSalTarget&range=1&tarName=1&isEmpty=yes'">销售指标分析</div>
							 <div id="tabType2" class="tabTypeWhite" onClick="window.location.href='statAction.do?op=salTarget&sat=sat'">销售指标设置</div>
                        </div>
                    </th>
                </tr>
            </table>
			<div id="listContent">
				<table class="normal" align="center" cellpadding="0" width="98%" cellspacing="0" style="margin:10px">
					<tr>
						<td width="20%" valign="top">
							<table width="100%" class="normal" style="text-align:left">
								<tr>
									<td>
                                    <div class="deepGray" style="width:200px; padding:2px;">①设置销售指标的时间段</div>
                                    <div class="grayBack" style="padding:5px">
                                    <select id="ryear" name="ryear" style="width:50%"></select>
                                    <select id="rmonth" name="rmonth" style="width:40%">
                                        <option value="1">1月</option>
                                        <option value="2">2月</option>
                                        <option value="3">3月</option>
                                        <option value="4">4月</option>
                                        <option value="5">5月</option>
                                        <option value="6">6月</option>
                                        <option value="7">7月</option>
                                        <option value="8">8月</option>
                                        <option value="9">9月</option>
                                        <option value="10">10月</option>
                                        <option value="11">11月</option>
                                        <option value="12">12月</option>
                                    </select>
                                    <div style=" padding:5px;">至</div>
                                    <select id="tyear" name="tyear" style="width:50%"></select>
                                    <select id="tmonth" name="tmonth" style="width:40%">
                                        <option value="1">1月</option>
                                        <option value="2">2月</option>
                                        <option value="3">3月</option>
                                        <option value="4">4月</option>
                                        <option value="5">5月</option>
                                        <option value="6">6月</option>
                                        <option value="7">7月</option>
                                        <option value="8">8月</option>
                                        <option value="9">9月</option>
                                        <option value="10">10月</option>
                                        <option value="11">11月</option>
                                        <option value="12">12月</option>
                                    </select>
                                    <div style="text-align:center; padding:4px;"><input type="button" class="butSize1" value="生成表格" onClick="getTrCount()"></div>
                                </div>
                                    </td>
								</tr>
								<tr>
                                    <td>
                                    <div class="deepGray" style=" padding:2px; padding-top:10px">②点击添加人员 </div>
                                    <script type="text/javascript">createIfmLoad('ifmLoad');</script>
                                    <iframe frameborder="0" style="border:#CCCCCC 1px solid;" onload="loadAutoH(this,'ifmLoad')" scrolling="no" width="100%" src="empAction.do?op=salEmpTree&type=addTd"></iframe>
                                    </td>
								</tr>
							</table>
						</td>
						<td width="80%" valign="top">
                        	<div class="deepGray" style="text-align:left; padding-top:5px;">&nbsp;&nbsp;③设置销售指标</div>
                                <form action="statAction.do" method="post" id="tgform" style="margin:0px; padding-left:5px;">
                                    <input type="hidden" name="op" value="saveTg"/>
                                    <input type="hidden"  id="trCount" name="trCount" value=""/>
                                	<table id="topTable" class="normal rowstable" cellpadding="0" cellspacing="0" style="width:98%">
                                        <tr>
                                            <td style=" width:20%;padding:0px; vertical-align:top; border-right:#b8b7b7 1px solid;">
                                                <table class="normal" id="userTb" cellpadding="0" cellspacing="0" style="width:100%">
                                                	<tr><th style="border-right:0px; width:80px;"><nobr>日期</nobr></th></tr>
                                                    <tr><th style="border-right:0px;"><nobr>用户</nobr></th></tr>
                                                </table>
                                            </td>
                                            <td style=" border-right:#b8b7b7 1px solid; padding:0px; vertical-align:top">
                                                <div id="dateScrollDiv" class="divWithScroll2" style="overflow:scroll; overflow-y:hidden; padding:0 2px 0 0; background-color:#ECE9D8">
                                                <table id="insertTb" cellpadding="0" cellspacing="0" style="width:100%;">
                                                    <tr><td class='autoAddTh'>&nbsp;请先设置时间段</td></tr>
                                                </table>
                                                </div>
                                            </td>
                                            <td style=" width:10%;padding:0px; vertical-align:top;">
                                                <table id="delTb" align="center" cellpadding="0" cellspacing="0" width="100%">
                                                    <tr><th style="border-right:0px;">&nbsp;</th></tr>
                                                    <tr><th style="border-right:0px;"><nobr>操作</nobr></th></tr>
                                                </table>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th colspan="3" style="border-right:0px;text-align:center">
                                            <input type="button" value="保存" class="butSize1" disabled id="dosave" style="width:80px;" onClick="check()">
                                            </th>
                                        </tr>
                                </table>
                                <script type="text/jscript">
									$("dateScrollDiv").style.width = document.body.clientWidth*(4/5)*(7/10);
								</script>
                                </form>
						</td>
				   </tr>
				</table>
        	</div>
  		</div> 
	 </div>
  </body>
</html>
