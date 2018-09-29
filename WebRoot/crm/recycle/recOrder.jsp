<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="<%=basePath%>"></base>
    <title>订单回收站</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="expires" content="0"/>
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <link rel="stylesheet" type="text/css" href="css/rec.css"/>
    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
	
    <script type="text/javascript" src="js/rec.js"></script>
    <script type="text/javascript">
       	function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			dataId = obj.sodCode;
			var dblFunc="descPop('orderAction.do?op=showOrdDesc&code="+obj.sodCode+"')";
          	var relFunc="descPop('customAction.do?op=showCompanyCusDesc&corCode="+obj.cusCorCus.corCode+"')";
  			var ordTil="<a href='javascript:void(0)' onclick=\""+dblFunc+"\">"+obj.sodTil+"</a>";
            var ordCus="";
            if(obj.cusCorCus != ""){
               ordCus="<a href='javascript:void(0)' title=\""+obj.cusCorCus.corName+"\" onclick=\""+relFunc+"\"><img class='imgAlign' src='images/content/showDesc.gif' alt='查看客户详情' style='border:0;'>"+obj.cusCorCus.corName+"</a>";
            }
			var funcCol = "<img onClick=\"recAddDiv(8,'rec','"+obj.sodCode+"')\" style='cursor:pointer' src='images/content/restore.gif' alt='恢复'/>&nbsp;&nbsp;<img onClick=\"recAddDiv(8,'del','"+obj.sodCode+"')\" style='cursor:pointer' src='images/content/del.gif' alt='彻底删除'/>";
			datas = [obj.salOrderType?obj.salOrderType.typName:"", ordTil, obj.sodNum, ordCus, obj.sodPaidMon, obj.sodSumMon, obj.sodConDate, obj.salEmp?obj.salEmp.seName:"", funcCol ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "orderAction.do";
			var pars = [];
			pars.op = "findDelOrder";
			
			var loadFunc = "loadList";
			var cols=[
				{name:"类别"},
				{name:"订单主题",align:"left"},
				{name:"订单号"},
				{name:"对应客户",align:"left"},
				{name:"已回款",renderer:"money",align:"right"},
				{name:"总金额",renderer:"money",align:"right"},
				{name:"签订日期",renderer:"date"},
				{name:"签单人"},
				{name:"操作",isSort:false}
			];

			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("recOrdListTab","dataList");
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
			
			<div id="listContent" style="text-align:left;">
				<table class="normal" cellpadding="0" cellspacing="0" style="width:100%;">
                    <tr>
                        <td id="recMenu">
                        	<input type="hidden" id="typeId" value="11"/>
                        	<script type="text/javascript">createIfmLoad('ifmLoad');</script>
                        	<iframe onload="loadAutoH(this,'ifmLoad')" id="menuIfrm" src="" frameborder="0" scrolling="no"></iframe>
                        </td>
                        <td style="vertical-align:top">
						    <div id="recTil"><span>订单</span></div>
						   	<div class="bottomBar">
                                	批量操作：&nbsp;&nbsp;<span class="grayBack" onMouseOver="this.className='orangeBox red'" onMouseOut="this.className='grayBack'" onClick="checkBatchDel(13,'订单')">批量删除</span>
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
