<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base> 
    <title>来往记录列表</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
    
    <script type="text/javascript" src="crm/js/cus.js"></script>
	<script type="text/javascript">
		//传入ajax的回调方法
		/*function toLoadTab(){
			//加载头部标签样式
			loadListTab('${range}',['0','1']);
		}*/
		//标签跳转链接
		function tabReload(){
			self.location.href = "cusServAction.do?op=toListSalPra&range=${range}";
		}
		//列表筛选按钮链接
		function filterList(filter){
			$("filter").value=filter;
			loadList();
		}
		
	 	function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			dataId = obj.praId;
			var dblFunc = "descPop('cusServAction.do?op=showSalPraInfo&id="+obj.praId+"')";
            var relFunc = "descPop('customAction.do?op=showCompanyCusDesc&corCode="+obj.cusCorCus.corCode+"')";
			var remark = "<a href='javascript:void(0)' onClick=\""+dblFunc+"\">"+obj.praRemark+"</a>";
			var customer = "<span class='mLink'><a href='javascript:void(0)' onClick=\""+relFunc+"\" ><img src='crm/images/content/showDesc.gif' alt='查看客户详情' class='imgAlign' style='border:0'/>"+obj.cusCorCus.corName+"</a></span>";
            var exeDate = obj.praExeDate.substring(0,10);
            
            var seName = "<span id='"+obj.praId+"PraEmp'>"+obj.salEmp.seName+"&nbsp;</span>";
			var funcCol = "<a><img src='crm/images/content/detail.gif' class='hand' onClick=\""+dblFunc+"\" alt='查看详细'/></a>&nbsp;&nbsp;<img onClick=\"cusPopDiv(41,["+dataId+"])\" class='hand' src='crm/images/content/edit.gif' alt='编辑'/>&nbsp;&nbsp;<img onClick=\"cusDelDiv(4,"+dataId+")\" class='hand' src='crm/images/content/del.gif' alt='删除'/>";
			datas = [obj.praType, remark, customer, exeDate, seName, obj.praInsDate, funcCol ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "cusServAction.do";
			var pars = $("searchForm").serialize(true);
			pars.op = "listSalPra";
			pars.range="${range}";
			
			var sortFunc = "loadList";
			var cols=[
				{name:"联系方式"},
				{name:"联系内容",align:"left"},
				{name:"对应客户",align:"left"},
				{name:"联系日期"},
				{name:"联系人"},
				{name:"录入日期",renderer:"date"},
				{name:"操作",isSort:false}
			];
			gridEl.init(url,pars,cols,sortFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
    	var gridEl = new MGrid("salPraListTab","dataList");
	    gridEl.config.hasCheckBox = true;
		createProgressBar();
		window.onload=function(){
			loadList();
			//增加清空按钮
			createCancelButton(loadList,'searchForm',-50,5,'searButton','after');
		}

  	</script>
  </head>
  
  <body>
  <div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;客户管理 > 来往记录</div>
            <input type="hidden" id="today" value="${TODAY}"/>
            <input type="hidden" id="tomorrow" value="${TMR}"/>
            <input type="hidden" id="afTom" value="${AF_TMR}"/>
            <input type="hidden" id="yesterday" value="${YDAY}"/>
            <input type="hidden" id="bfYesd" value="${BF_YDAY}"/>
            <table class="mainTab" cellpadding="0" cellspacing="0">
            <tr>
            	<th>
                <div id="tabType">
                     <div id="tabType1" class="tabTypeWhite" onClick="tabReload()">来往记录</div>
           		</div>
                </th>
                <td>
                <a href="javascript:void(0)" onClick="cusPopDiv(4);return false;" class="newBlueButton">新建来往记录</a>
                </td>
            </tr>
            </table>
           	<script type="text/javascript">loadTabTypeWidth();</script>
            <div id="listContent">
                            <div class="listSearch">
                                <form style="margin:0; padding:0;" id="searchForm" onSubmit="loadList();return false;">
                                     	联系内容：<input type="text" name="praRemark" class="inputSize2 inputBoxAlign"/>&nbsp;
										对应客户：<input id="cusId" name="cusName" class="inputSize2 inputBoxAlign lockBack" type="text" style="width:142px" ondblClick="clearInput(this,'cusCode')" title="此处文本无法编辑，双击可清空文本" readonly/>&nbsp;<button class="butSize2 inputBoxAlign" onClick="addDivBrow(2)">选择</button>&nbsp;
										方式：<select class="inputSize2 inputBoxAlign" id="praType" name="praType">
                                                    <option value="">请选择</option>
                                                    <option value="电话">电话</option>
                                                    <option value="传真">传真</option>
                                                    <option value="电邮">电邮</option>
                                                    <option value="邮寄">邮寄</option>
                                                    <option value="网络">网络</option>
                                                    <option value="拜访">拜访</option>
                                                    <option value="其他">其他</option>
                                                </select>&nbsp;
                                                联系日期：<input name="startTime" type="text" class="inputBoxAlign inputSize2 Wdate" style="cursor:hand; width:82px" readonly="readonly" ondblClick="clearInput(this)" id="startTime" onFocus="var endTime=$('endTime');WdatePicker({onpicked:function(){endTime.focus();},maxDate:'#F{$dp.$D(\'endTime\')}'})"/>&nbsp;到&nbsp;<input name="endTime" id="endTime" type="text" class="inputBoxAlign inputSize2 Wdate" style="cursor:hand; width:82px" readonly="readonly" ondblClick="clearInput(this)" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'startTime\')}'})"/>&nbsp;
                                                联系人：<c:if test="${range==1}"><input type="text" name="uName" class="inputSize2 inputBoxAlign" onBlur="autoShort(this,50)"/></c:if><c:if test="${range==0}"><input type="text" name="uName" class="inputSize2 inputBoxAlign"  value="<c:out value="${CUR_EMP.seName}"/>" disabled onBlur="autoShort(this,50)"/></c:if>&nbsp;
                                                <input id="searButton" class="inputBoxAlign butSize3" type="submit" value="查询"/>
                                 </form>
                             </div>
                <div id="dataList" class="dataList"></div>
            </div>
        </div>
  	</div>
  </body>
</html>
