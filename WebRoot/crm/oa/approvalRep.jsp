<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>收到的报告</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    
	<link rel="stylesheet" type="text/css" href="crm/crcss/style.css"/>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    
    <script type="text/javascript" src="crm/js/oa.js"></script>
    <script type="text/javascript">
		function changeIsApp(){
			var isApp=document.getElementById("isApp").value;
			self.location.href="messageAction.do?op=getApproRep&isApped="+isApp+"&pageSize=${page.pageSize}";
		}
		function loadIsApp(){
			if($("isApp")!=null){
				$("isApp").value="${isApped}";
			}
		}
	    function delReport(rState,n,code)
	    {
	    	if(rState==0)
	    	{
	    		alert("您还未批复该报告不能删除!");
	    		return false;
	    	}
	    	else 
	    	{
	    		oaDelDiv(n,code);
	    	}
	    }
	    
	   	function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			var dblFunc="descPop('messageAction.do?op=showRepInfo&repCode="+obj.report.repCode+"&mark=appRepInfo')";
			var title="";
			/**if( obj.report.attachments != ""){
				title+="<img style='vertical-align:middle;' src='crm/images/content/attIconS.gif' border='0px' alt='附件'/>";
			}*/
			title+="<a href='javascript:void(0)' onclick=\""+dblFunc+"\" >"+obj.report.repTitle+"</a>";
			var state ="";
			switch(obj.rrlIsappro){
				case '0': 
					state="<span class='red'>未批复</span>";
					break;
				case '1':
					state="<span class='deepGreen'>已批复</span>";
					break;
			}
			var funcCol ="";
			if(obj.rrlIsappro =="0"){
				funcCol="<img src=\"crm/images/content/approve.gif\" style=\"cursor:pointer\" alt=\"批复\" onClick=\""+dblFunc+"\"/>&nbsp;";
			}
			else if(obj.rrlIsappro =="1"){
				funcCol="<img src=\"crm/images/content/detail.gif\" style=\"cursor:pointer\" alt=\"查看\" onClick=\""+dblFunc+"\"/>&nbsp;";
			}
			funcCol+="<img src=\"crm/images/content/del.gif\" alt=\"删除\" style=\"cursor:pointer\" onClick=\"delReport("+obj.rrlIsappro+",3,"+obj.rrlId+")\"/>";
			datas = [state, obj.report.repType?obj.report.repType.typName:"", title, obj.rrlDate, obj.report?obj.report.repInsUser:"", funcCol ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "messageAction.do";
			var pars = [];
			pars.op = "getApproRep";
			pars.isApped="${isApped}";
			
			var loadFunc = "loadList";
			var cols=[
				{name:"状态"},
				{name:"类别"},
				{name:"报告主题",align:"left"},
				{name:"提交日期",renderer:"date"},
				{name:"提交人"},
				{name:"操作",isSort:false}
			];
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		
    	var gridEl = new MGrid("myRepTab","dataList");
		createProgressBar();
		window.onload=function(){
			loadIsApp();
			//表格内容省略
			loadList();
			//closeProgressBar();
		}
	 </script>
  </head>
  
  <body>
  <div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;协同办公 > 报告管理
            </div>
           	<table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                        <div id="tabType">
                        	<div id="tabType1" class="tabTypeBlue1" onClick="self.location.href='messageAction.do?op=toListHaveSenRep'" onMouseOver="changeTypeBg(this,1,1)" onMouseOut="changeTypeBg(this,0,1)" >我的报告</div>
                            <div id="tabType2" class="tabTypeWhite" onClick="self.location.href='messageAction.do?op=toListApproRep'">已收报告</div>     
                            <div id="tabType3" class="tabTypeBlue2" onClick="self.location.href='messageAction.do?op=toListReadySenRep'" onMouseOver="changeTypeBg(this,1,2)" onMouseOut="changeTypeBg(this,0,2)" >待发报告</div>
                        </div>
                     </th>
                     <td>
                    	<a href="javascript:void(0)" onClick="oaPopDiv(4);return false;" class="newBlueButton">写报告</a>
                    </td>
                </tr>
            </table>
            <script type="text/javascript">loadTabTypeWidth();</script>
            <div id="listContent">
            	<div id="dataList" class="dataList"></div>
            </div>
        </div>
  	</div>
  </body>
  
</html>
