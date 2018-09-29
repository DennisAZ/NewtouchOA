<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>产品列表</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/sal.js"></script>
    
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
	<script type="text/javascript">
		function loadProType(proType){
			if(document.getElementById("proType")!=null){
				document.getElementById("proType").value=proType;
			}
			loadList();
		}
		//标签跳转链接
		function tabReload(){
			self.location.href = "prodAction.do?op=toListProd";
		}
		
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			var dblFunc="descPop('prodAction.do?op=wmsProDesc&wprId="+obj.wprId+"')";
			var proName="<a href='javascript:void(0)' onclick=\""+dblFunc+"\">"+obj.wprName+"</a>";
			var prodPrc=0;
			if(obj.wprSalePrc>0){
				prodPrc = "￥"+changeTwoDecimal(obj.wprSalePrc)+"&nbsp;<span class='deepGray'>(不含税"+changeTwoDecimal(obj.wprSalePrc/(1+getSalTaxRate()))+")</span>";
			}
			else{
				prodPrc = "￥"+changeTwoDecimal(prodPrc);
			}
			var funcCol = "<img src='crm/images/content/detail.gif' alt='查看详细' style='cursor:pointer' onclick=\""+dblFunc+"\"/>&nbsp;&nbsp;<img onClick=\"ordPopDiv(51,"+obj.wprId+")\" style='cursor:pointer' src='crm/images/content/edit.gif' alt='编辑'/>&nbsp;&nbsp;<img onClick=\"ordDelDiv(6,"+obj.wprId+")\" style='cursor:pointer' src='crm/images/content/del.gif' alt='删除'/>";
			datas = [obj.wprCode, proName,obj.wmsProType?obj.wmsProType.wptName:"", prodPrc, obj.wprNormalPer, obj.wprOverPer, obj.wprDesc, obj.wprRemark, funcCol ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "prodAction.do";
			var pars = $("searchForm").serialize(true);
			pars.op = "listProd";
			var loadFunc = "loadList";
			var cols=[
				{name:"编号"},
				{name:"名称",align:"left"},
				{name:"类别"},
				{name:"标准价格"},
				{name:"正常提成率"},
				{name:"超额提成率"},
				{name:"详情",isSort:false,align:"left"},
				{name:"备注",align:"left",isSort:false},
				{name:"操作",isSort:false}
			];

			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("prodListTab","dataList");
		createProgressBar();
		window.onload=function(){
			//表格内容省略
			loadList();
			//增加清空按钮
			createCancelButton(loadList,'searchForm',-50,5);
			//closeProgressBar();
		}
  	</script>
  </head>
  
  <body>
    <div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;销售管理 > 产品管理
            </div> 
  	   		<table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                        <div id="tabType">
                            <div id="tabType1" class="tabTypeWhite" onClick="tabReload()">产品信息</div>
                        </div>
                     </th>
                     <td>
                    	<a href="javascript:void(0)" onClick="ordPopDiv(5);return false;" class="newBlueButton">添加产品</a>
                    </td>
                </tr>
            </table>
            <script type="text/javascript">loadTabTypeWidth();</script>
            <div id="listContent">
             	<div class="listSearch">
                	<form style="margin:0; padding:0;" id="searchForm" onSubmit="loadList();return false;">
                    <input type="text" id="proType" name="proType" style="display:none"/> 
                	编号：<input class="inputSize2 inputBoxAlign" type="text" name="proCode" onBlur="autoShort(this,30)"/>&nbsp;&nbsp;
                    产品名称：<input class="inputSize2 inputBoxAlign" type="text" name="proName" onBlur="autoShort(this,50)"/>&nbsp;&nbsp;
                     <input type="submit" class="butSize3 inputBoxAlign" value="查询"/>&nbsp;&nbsp;
                    </form>
                </div>
                 <table class="normal" cellpadding="0" cellspacing="0" style="width:100%">
                    <tr>
                        <td style="margin:0px;width:210px;vertical-align:top; text-align:left">
                        <script type="text/javascript">createIfmLoad('loadDiv');</script>
                         <iframe frameborder="0" name="iFrame1" id="ifm" style="border:#CCCCCC 1px solid; width:200px;" scrolling="no" src="prodAction.do?op=selProType&pro=pro"></iframe></td>
                        <td style="vertical-align:top; margin-left:10px;">
                        	<div id="dataList" class="dataList" style="margin-top:0px"></div>
                        </td>
                    </tr>
                    </table>
            </div>
  		</div> 
	</div>
  </body>
   
</html>
