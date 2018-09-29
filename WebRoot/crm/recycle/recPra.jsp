<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="<%=basePath%>"></base>
    <title>来往记录</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="expires" content="0"/>
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="crm/css/rec.css"/>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
	
    <script type="text/javascript" src="crm/js/rec.js"></script>
    <script type="text/javascript">
       function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			dataId = obj.praId;
			var dblFunc = "descPop('cusServAction.do?op=showSalPraInfo&id="+obj.praId+"')";
            var relFunc = "descPop('customAction.do?op=showCompanyCusDesc&corCode="+obj.cusCorCus.corCode+"')";
            var remark = "<a href='javascript:void(0)' onClick=\""+dblFunc+"\">"+obj.praRemark+"</a>";
           	var customer = "<span class='mLink'><a href='javascript:void(0)' onClick=\""+relFunc+"\" ><img src='crm/images/content/showDesc.gif' alt='查看客户详情' class='imgAlign' style='border:0'/>"+obj.cusCorCus.corName+"</a></span>";
 			var state="";
			var funcCol = "<img onClick=\"recAddDiv(6,'rec','"+obj.praId+"')\" style='cursor:pointer' src='crm/images/content/restore.gif' alt='恢复'/>&nbsp;&nbsp;<img onClick=\"recAddDiv(6,'del','"+obj.praId+"')\" style='cursor:pointer' src='crm/images/content/del.gif' alt='彻底删除'/>";
			
			datas = [obj.praType, remark, customer, obj.praExeDate, obj.person?obj.person.userName:"", obj.praInsDate, funcCol ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "cusServAction.do";
			var pars = [];
			pars.op = "findDelPra";
			
			var loadFunc = "loadList";
			var cols=[
				{name:"联系方式"},
				{name:"联系内容",align:"left"},
				{name:"对应客户",align:"left"},
				{name:"联系日期",renderer:"date"},
				{name:"联系人"},
				{name:"录入日期",renderer:"date"},
				{name:"操作",isSort:false}
			];

			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("recPraListTab","dataList");
		gridEl.config.sortable=false;
		gridEl.config.hasCheckBox=true;
		createProgressBar();
   		window.onload=function(){
			//表格内容省略
			loadList();
			loadMenuIfm();
		}
    </script>
	
</head>

<body>
	<div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;系统设置 > 回收站 </div>
			    <table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                    <div id="tabType">
                        <div id="tabType1" class="tabTypeWhite" style="cursor:default">回收站</div>
                    </div>
                    </th>
                </tr>
            </table>
			
			<div id="listContent">
				<table class="normal" cellpadding="0" cellspacing="0" style="width:100%;">
                    <tr>
                       <td id="recMenu">
                        	<input type="hidden" id="typeId" value="6"/>
                        	<script type="text/javascript">createIfmLoad('ifmLoad');</script>
                        	<iframe onload="loadAutoH(this,'ifmLoad')" id="menuIfrm" src="" frameborder="0" scrolling="no"></iframe>
                        </td>
                        <td style="vertical-align:top">
                        	<div id="recTil"><span>来往记录</span></div>
                        	<div class="bottomBar">
                                	批量操作：&nbsp;&nbsp;<span class="grayBack" onMouseOver="this.className='orangeBox red'" onMouseOut="this.className='grayBack'" onClick="checkBatchDel(12,'来往记录')">批量删除</span>
                       		</div>
                       		<div id="dataList" class="dataList"></div>
						</td>
					</tr>
				</table>
			</div>
	   </div>
    </div>
</body>
</html>
