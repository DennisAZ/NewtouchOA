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
    <title>采购单列表</title>
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
    <script type="text/javascript" src="crm/sup/sup.js"></script>
  	<script language="javascript" type="text/javascript">
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			dataId = obj.puoId;
			dblFunc="descPop('supAction.do?op=toDescPurOrd&puoId="+dataId+"')";
		
			var supName = "";
			if(obj.puoSup !=undefined){
				var relFunc = "descPop('supAction.do?op=toDescSup&supId="+obj.puoSup.supId+"')";
				supName = "<a href='javascript:void(0)' onClick=\""+relFunc+"\">"+obj.puoSup.supName+"</a>";
			}
			var puoCode="<a href='javascript:void(0)' onClick=\""+dblFunc+"\">"+obj.puoCode+"</a>";
			var funcCol = "<img src='crm/images/content/detail.gif' onClick=\""+dblFunc+"\" class='hand' alt='查看详细'/>&nbsp;&nbsp;<img style='cursor:pointer' src='crm/images/content/edit.gif' onClick=\"supPopDiv(3,"+dataId+")\"  alt='编辑'/>&nbsp;&nbsp;<img onClick=\"supDelDiv(2,"+dataId+")\"  style='cursor:pointer' src='crm/images/content/del.gif' alt='删除'/>";
			datas = [puoCode, obj.puoType?obj.puoType.typName:"", supName, obj.puoM,obj.puoPaidM, obj.puoPurDate,obj.puoEmp?obj.puoEmp.seName:"", funcCol ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "supAction.do";
			var pars = $("searchForm").serialize(true);
			pars.op = "listPurOrd";
			var loadFunc = "loadList";
			var cols=[
				{name:"编号"},
				{name:"类别"},
				{name:"供应商名称",align:"left"},
				{name:"采购金额",renderer:"money",align:"right"},
				{name:"已还款",renderer:"money",align:"right"},
				{name:"采购日期",renderer:"date"},
				{name:"采购人"},
				{name:"操作",isSort:false}
			];
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("puoTab","dataList");
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
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;采购管理 > 采购单</div>
  	   		<table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                        <div id="tabType"><div id="tabType1" class="tabTypeWhite" onClick="self.location.href='supAction.do?op=toListPurOrd'">采购单</div></div>
                    </th>
                    <td>
                    	<a href="javascript:void(0)"  onClick="supPopDiv(2);return false;" class="newBlueButton">新建采购单</a>
                    </td>
                </tr>
            </table>
            <script type="text/javascript">loadTabTypeWidth();</script>
            <div id="listContent">
             	<div class="listSearch">
                	<form style="margin:0; padding:0;" id="searchForm" onSubmit="loadList();return false;" >
                        采购单编号：<input class="inputSize2 inputBoxAlign" type="text" name="puoCode" onBlur="autoShort(this,100)"/>&nbsp;&nbsp;&nbsp;
                      	供应商名称：<input class="inputSize2 inputBoxAlign" type="text" name="supName" onBlur="autoShort(this,100)"/>&nbsp;
                         <input type="submit" class="butSize3 inputBoxAlign" id="searButton" value="查询" />&nbsp;&nbsp;
                    </form>
                </div>
                <div id="dataList" class="dataList" ></div>
            </div>
  		</div> 
	</div>
  </body>
</html>
