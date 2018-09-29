<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	//清缓存
	response.setHeader("Pragma","No-cache");   
	response.setHeader("Cache-Control","no-cache");   
	response.setDateHeader("Expires", 0);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>最近两天新建的客户服务</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    

    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
    
    <script type="text/javascript" src="js/cus.js"></script>
    <script type="text/jscript">
		function chooseCus(){
			addDivBrow(2);
		}
		
		//传入ajax的回调方法
		function toLoadTab(){
			//加载头部标签样式
			loadTabType('0');
		}
		function changeListType(range){
			self.location.href="cusServAction.do?op=toListServ&range="+range;
		}
		
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			dataId = obj.serCode;
			var dblFunc = "descPop('cusServAction.do?op=showCusServInfo&serCode="+obj.serCode+"')";
			var relFunc ="descPop('customAction.do?op=showCompanyCusDesc&corCode="+obj.cusCorCus.corCode+"')";
			var title="<a href='javascript:void(0)' onclick=\""+dblFunc+"\">"+obj.serTitle+"</a>";
			var customer = "<span class='mLink'><a title=\""+obj.cusCorCus.corName+"\" href='javascript:void(0)' onclick=\""+relFunc+"\"><img src='images/content/showDesc.gif' alt='查看客户详情' class='imgAlign' style='border:0;'>"+obj.cusCorCus.corName+"</a></span>";
            var date = "";
   			if(obj.serExeDate !=undefined && obj.serState == "待处理"){
   				date = obj.serExeDate.substring(0,10)+"<img src='images/content/execute.gif' style='cursor:pointer; vertical-align:middle' alt='点击执行' onClick=\"cusPopDiv(7,'"+obj.serCode+"')\"/></span>";
   			}else if(obj.serExeDate  == undefined){
   				date = "<span class='gray'>未设定</span>&nbsp;<img src='images/content/execute.gif' style='cursor:pointer; vertical-align:middle' alt='点击执行' onClick=\"cusPopDiv(7,'"+obj.serCode+"')\"/></span>";
   			}else{
   				date = obj.serExeDate.substring(0,10);
   			} 
			var funcCol = "<img src='images/content/detail.gif' onClick=\""+dblFunc+"\" class='hand' alt='查看详细'/>&nbsp;&nbsp;<img onClick=\"cusPopDiv(51,"+obj.serCode+")\" style='cursor:pointer' src='images/content/edit.gif' border='0' alt='编辑'/>&nbsp;&nbsp;<img onClick=\"cusDelDiv(5,"+obj.serCode+")\" style='cursor:pointer' src='images/content/del.gif' border='0' alt='删除'/>";
			datas = [title, customer, obj.serMethod, obj.serContent, obj.salEmp?obj.salEmp.seName:"<span class='gray'>未设定</span>", date, funcCol ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "cusServAction.do";
			var pars = $("searchForm").serialize(true);
			pars.op = "listServ";
			pars.range="${range}";
			
			var loadFunc = "loadList";
			var cols=[
				{name:"主题",align:"left"},
				{name:"对应客户",align:"left"},
				{name:"服务方式"},
				{name:"客服内容",align:"left"},
				{name:"处理人"},
				{name:"处理日期"},
				{name:"操作",isSort:false}
			];

			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
    	var gridEl = new MGrid("servListTab","dataList");
    	gridEl.config.hasCheckBox = true;
		createProgressBar();
		window.onload=function(){
			//表格内容省略
			loadList();
			//增加清空按钮
			createCancelButton(loadList,'searchForm',-50,5);
			//closeProgressBar();
			toLoadTab();
		}
  </script>
  </head>
  <body>
  <div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;客服管理 > <logic:equal value="0" name="range">待办客服</logic:equal><logic:notEqual value="0" name="range">客服记录</logic:notEqual>
            </div>
            <table class="mainTab" cellpadding="0" cellspacing="0">
            <tr>
            	<th>
                <div id="tabType">
                	<logic:equal value="0" name="range">
                		 <div id="tabType1" onClick="changeListType(0)">待办客服</div>   
                	</logic:equal>
                    <logic:notEqual value="0" name="range">
                		 <div id="tabType1" onClick="changeListType(1)">客服记录</div>   
                	</logic:notEqual>               
                   <!--<div id="tabType2" onClick="self.location.href='cusServAction.do?op=listServ&range=1'">下属客户服务</div>--> 
                   <!--<script type="text/javascript">writeLimAllow("r032","<div id='tabType2' onClick='changeListType(1)'>已处理</div>","tabType","bottom",toLoadTab);</script> -->
           		</div>
                </th>
                <td>
                <a href="javascript:void(0)" onClick="cusPopDiv(5);return false;" class="newBlueButton">新建客户服务</a>
                </td>
            </tr>
            </table>
              <script type="text/javascript">loadTabTypeWidth();</script>
            <div id="listContent">
                <div class="listSearch">
                    <form style="margin:0; padding:0;" id="searchForm" onSubmit="loadList();return false;">
                       <!-- <input type="text" style="display:none" name="cusCode" id="cusCode" value="${code}" /> -->
                       主题：<input type="text" name="servTitle"  class="inputSize2 inputBoxAlign" style="width:100px"/>&nbsp;&nbsp;
                         对应客户：<input id="cusId" name="cusName" class="inputSize2 inputBoxAlign lockBack" type="text" style="cursor:hand" ondblClick="clearInput(this,'cusCode')" title="此处文本无法编辑，双击可清空文本" readonly/>&nbsp;<button class="butSize2 inputBoxAlign" onClick="chooseCus()">选择</button>&nbsp;&nbsp;
                        <!-- 状态：<select class="inputBoxAlign" id="serState" name="serState">
                             	<option value="">请选择</option>
                              	<option value="待处理">待处理</option>
                              	<option value="已处理">已处理</option>
                            </select>&nbsp;&nbsp; -->
                       	客服方式：<select class="inputBoxAlign" id="serMethod" name="serMethod">
                              		<option value="">请选择</option>
                              		<option value="电话">电话</option>
                              		<option value="传真">传真</option>
                              		<option value="邮寄">邮寄</option>
                              		<option value="上门">上门</option>
                              		<option value="其他">其他</option>
                            	</select>&nbsp;&nbsp;
                           处理人： <input type="text" name="seName"  class="inputSize2 inputBoxAlign" style="width:100px"/>&nbsp;&nbsp;   	
                       	处理日期：<input name="startTime" ondblClick="clearInput(this)" type="text" class="inputBoxAlign inputSize2 Wdate" style="cursor:hand;width:82px" readonly="readonly" id="pid"  onFocus="var pid1=$('pid1');WdatePicker({onpicked:function(){pid1.focus();},maxDate:'#F{$dp.$D(\'pid1\')}'})"/>&nbsp;到&nbsp;<input name="endTime"  ondblClick="clearInput(this)" type="text" class="inputBoxAlign inputSize2 Wdate" style="cursor:hand; width:82px" readonly="readonly" id="pid1" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'pid\')}'})"/>&nbsp;&nbsp;
                        <input type="submit" class="butSize3 inputBoxAlign" value="查询"/>&nbsp;&nbsp;
                     </form>
                </div>	
               	<div class="bottomBar">
                                批量操作：&nbsp;&nbsp;<span class="grayBack" onMouseOver="this.className='orangeBox red'" onMouseOut="this.className='grayBack'" onClick="checkBatchDel(5,'客户服务')">批量删除</span>
                </div>
                <div id="dataList" class="dataList"></div>
            </div>
        </div>
  	</div>
  </body>
</html>
