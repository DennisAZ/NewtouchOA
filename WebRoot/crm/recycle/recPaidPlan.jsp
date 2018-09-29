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
    <title>回款计划回收站</title>
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
			dataId = obj.spdId;
			var relFunc1="descPop('customAction.do?op=showCompanyCusDesc&corCode="+obj.cusCorCus.corCode+"')";
            var relFunc2="descPop('orderAction.do?op=showOrdDesc&code="+obj.salOrdCon.sodCode+"')";
           	var plCus="";
            if(obj.cusCorCus !=""){
                plCus="<a href='javascript:void(0)' title=\""+obj.cusCorCus.corName+"\" onclick=\""+relFunc1+"\"><img class='imgAlign' src='images/content/showDesc.gif' alt='查看客户详情' style='border:0;'>"+obj.cusCorCus.corName+"</a>";
            }
            var plOrd="";
            if(obj.salOrdCon !=""){
                plOrd="<a href='javascript:void(0)' title=\""+obj.salOrdCon.sodTil+"\" onclick=\""+relFunc2+"\"><img class='imgAlign' src='images/content/showDesc.gif' alt='查看订单详情' style='border:0;'>"+obj.salOrdCon.sodTil+"</a>";
            }
            var plIsP ="";
            if(obj.spdIsp =="1"){
                plIsP ="<img class='imgAlign' src='images/content/suc.gif' alt='已回款'/>";
            }
            else {
                plIsP ="<span class='red'>未回</span>";
            }
			var funcCol = "<img onClick=\"recAddDiv(10,'rec','"+obj.spdId+"')\" style='cursor:pointer' src='images/content/restore.gif' alt='恢复'/>&nbsp;&nbsp;<img onClick=\"recAddDiv(10,'del','"+obj.spdId+"')\" style='cursor:pointer' src='images/content/del.gif' alt='彻底删除'/>";
			
			datas = [plIsP, plCus, plOrd, obj.spdPayMon, obj.spdPrmDate, obj.spdResp?obj.spdResp.seName:"", obj.spdContent,funcCol ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "paidAction.do";
			var pars = [];
			pars.op = "findDelPaidPlan";
			
			var loadFunc = "loadList";
			var cols=[
				{name:"是否已回款"},
				{name:"对应客户",align:"left"},
				{name:"对应订单",align:"left"},
				{name:"回款金额",align:"right",renderer:"money"},
				{name:"回款日期",renderer:"date"},
				{name:"负责人"},
				{name:"备注",isSort:false},
				{name:"操作",isSort:false}
			];

			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("recSPPListTab","dataList");
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
                        	<input type="hidden" id="typeId" value="13"/>
                        	<script type="text/javascript">createIfmLoad('ifmLoad');</script>
                        	<iframe onload="loadAutoH(this,'ifmLoad')" id="menuIfrm" src="" frameborder="0" scrolling="no" ></iframe>
                        </td>
                        <td style="vertical-align:top">
						    <div id="recTil"><span>回款计划</span></div>
						   	<div class="bottomBar">
                                	批量操作：&nbsp;&nbsp;<span class="grayBack" onMouseOver="this.className='orangeBox red'" onMouseOut="this.className='grayBack'" onClick="checkBatchDel(14,'回款计划')">批量删除</span>
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
