<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
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
    <title>联系人列表</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>

    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/cus.js"></script>
    
    <script type="text/jscript">

		function chooseCus(){
			addDivBrow(2);
		}
	
		function toAssignC(){
			if(checkBoxIsEmpty("priKey")){
				cusPopDiv(12,"cont");
			}
		}

		function out(type){
		    var range = "${range}";
			var conName = $("conName").value;
			var cusName = $("cusId").value;
			var conLev = $("conLev").value;
			var seName = "";
			if(range == "1"){
				seName = $("uName").value;
			}
			if(type == '1'){
				if(checkBoxIsEmpty("priKey")){
					cusPopDiv(25,[range,conName,cusName,conLev,seName,type]);
				}
			}
			else{
				cusPopDiv(25,[range,conName,cusName,conLev,seName,type]);
			}
		}
		//标签跳转
		function tabReload(){
			self.location.href="customAction.do?op=toListContacts&range=${range}";
		}
		
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			dataId = obj.conId;
			var dblFunc="descPop('customAction.do?op=showContactInfo&id="+dataId+"')";
			var relFunc="descPop('customAction.do?op=showCompanyCusDesc&corCode="+obj.cusCorCus.corCode+"')";
			var conName="";
			var conCus="";
			if(obj.conLev == "失效"){
				className = "gray";
				conName="<span class='mGray'><a href=\"javascript:void(0)\" onclick=\""+dblFunc+"\">"+obj.conName+"</span></a>";
				conCus="<span class='mGray'><a href='javascript:void(0)' onclick=\""+relFunc+"\"><img src='crm/images/content/showDesc.gif' alt='查看客户详情' class='imgAlign' style='border:0;'>"+obj.cusCorCus.corName+"</a></span>";
			}else{
				conName="<a href=\"javascript:void(0)\" onclick=\""+dblFunc+"\">"+obj.conName+"</a>";
				conCus="<a href='javascript:void(0)' onclick=\""+relFunc+"\"><img src='crm/images/content/showDesc.gif' alt='查看客户详情' class='imgAlign' style='border:0;'>"+obj.cusCorCus.corName+"</a>";
			}

			var conEm=""; 
			if(obj.conEmail != undefined && obj.conEmail !=""){
				conEm="<img src='crm/images/content/email.gif'  title='点击发送电子邮件'/><a href='javascript:void(0)' onClick=\"mailTo('"+obj.conEmail+"');return false;\">"+obj.conEmail+"</a>";
			}
			var conQq="";
			if(obj.conQq != undefined && obj.conQq != ""){
				conQq="<img src='crm/images/content/qq.gif'  title='点击开始qq对话'/><a href='javascript:void(0)' onClick=\"qqTo('"+obj.conQq+"');return false;\">"+obj.conQq+"</a>";
			}
			var funcCol = "<img src='crm/images/content/detail.gif'  onClick=\""+dblFunc+"\" class='hand' alt='查看详细'/>&nbsp;&nbsp;<img onClick=\"cusPopDiv(21,["+dataId+"])\" style='cursor:pointer' src='crm/images/content/edit.gif' border='0' alt='编辑'/>&nbsp;&nbsp;<img onClick=\"cusDelDiv(2,"+dataId+")\" style='cursor:pointer' src='crm/images/content/del.gif' border='0' alt='删除'/>";
			datas = [conName, obj.conSex, obj.conLev, conCus, obj.conDep, obj.conPos, obj.conPhone, obj.conWorkPho, conEm, conQq, funcCol ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "customAction.do";
			var pars = $("searchForm").serialize(true);
			pars.op = "listContacts";
			pars.range="${range}";
			
			var sortFunc = "loadList";
			var cols=[
				{name:"姓名"},
				{name:"性别"},
				{name:"分类"},
				{name:"对应客户",align:"left"},
				{name:"部门"},
				{name:"职务"},
				{name:"手机"},
				{name:"办公电话"},
				{name:"电子邮件"},
				{name:"QQ"},
				{name:"操作",isSort:false}
			];
			gridEl.init(url,pars,cols,sortFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("contactListTab","dataList");
    	gridEl.config.hasCheckBox = true;
		createProgressBar();
		window.onload=function(){
			loadList();
			//增加清空按钮
			createCancelButton(loadList,'searchForm',-50,5,'searButton','after');
			//loadIfmSrc("conIfm","customAction.do?op=getContactByBirth&range=${range}");
		}
  </script></head>
  
  <body>
  <div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;客户管理 > 联系人</div>
            <input type="hidden" id="today" value="${TODAY}"/>
            <input type="hidden" id="tomorrow" value="${TMR}"/>
            <input type="hidden" id="afTom" value="${AF_TMR}"/>
            <input type="hidden" id="yesterday" value="${YDAY}"/>
            <input type="hidden" id="bfYesd" value="${BF_YDAY}"/>
            <table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                        <div id="tabType">
                             <div id="tabType1" class="tabTypeWhite" onClick="tabReload()"><c:if test="${range==0}">我的</c:if><c:if test="${range==1}">全部</c:if>联系人</div>
                        </div>
                    </th>
                   <!--  <td>
                        <a href="javascript:void(0)" onClick="cusPopDiv(2);return false;" class="newBlueButton">新建联系人</a>
                    </td>
                     -->
                </tr>
            </table>
            <script type="text/javascript">loadTabTypeWidth();</script>
            <div id="listContent">
            	<div class="listSearch">
                	<form style="margin:0; padding:0;" id="searchForm" onSubmit="loadList();return false;" >
                        姓名：<input style="width:100px" class="inputSize2 inputBoxAlign" type="text" id="conName" name="conName" onBlur="autoShort(this,50)"/>&nbsp;
                        对应客户：<input id="cusId" name="cusName" class="inputSize2 inputBoxAlign lockBack" title="此处文本无法编辑，双击可清空文本" style="width:142px" type="text" readonly ondblClick="clearInput(this,'cusCode')" />&nbsp;<button class="butSize2 inputBoxAlign" onClick="chooseCus()">选择</button>&nbsp;&nbsp;&nbsp;
                        分类：<select class="inputBoxAlign" id="conLev" name="conLev">
                            <option value="">请选择</option>
                            <option value="特别重要">特别重要</option>
                            <option value="重要">重要</option>
                            <option value="普通">普通</option>
                            <option value="不重要">不重要</option>
                            <option value="失效">失效</option>
                        </select>&nbsp;
                        <c:if test="${range==1}">负责人：<input type="text" style="width:90px" class="inputSize2 inputBoxAlign" id="uName" name="uName" onBlur="autoShort(this,50)"/>&nbsp;</c:if>
                        <input id="searButton" class="inputBoxAlign butSize3" type="submit" value="查询"/>&nbsp;&nbsp;
                         <!--<a class="supSearButton inputBoxAlign" href="customAction.do?op=toCusSupSearch&range=${range}" target="_blank" style="float:none">高级查询</a>-->
                    </form>
                </div>
                <div class="bottomBar">
                   批量操作：&nbsp;&nbsp;<span class="grayBack" onMouseOver="this.className='orangeBox red'" onMouseOut="this.className='grayBack'" onClick="checkBatchDel(2,'联系人')">批量删除</span>
                   <c:if test="${range==1}">
                         <span class="grayBack" onMouseOver="this.className='orangeBox red'" onMouseOut="this.className='grayBack'" onClick="toAssignC()">分配负责人</span>
                   </c:if>
                   &nbsp;&nbsp;<span class="grayBack" onMouseOver="this.className='orangeBox red'" onMouseOut="this.className='grayBack'" onClick="out('0')">导出查询结果</span>
                   &nbsp;&nbsp;<span style="width:100px;" class="grayBack" onMouseOver="this.className='orangeBox red'" onMouseOut="this.className='grayBack'" onClick="out('1')">导出所选联系人</span>
                </div>
                <div id="dataList" class="dataList"></div>
            </div>
        </div>
  	</div>
  </body>
  
</html>
