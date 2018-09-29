<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>弹出层显示操作成功</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">   
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    
    <style type="text/css">
		#popSuc{
			background-color:#fefeee; 
			width:244px; 
			height:100%;
			text-align:center;
			
		}
    </style>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>

    <script type="text/javascript">
		//修改弹出层样式
		function loadStyle(){
			var wIdPre = "${wIdPre}";//弹出层前缀
			var parDiv = parent.document.getElementById(wIdPre + "popDiv");
			var conDiv = parent.document.getElementById(wIdPre + "popCon");
			var iframeDiv = parent.document.getElementById(wIdPre + "popInside");
			var popTop = parent.document.getElementById(wIdPre + "popTop");
			var height=90;
			
			document.body.style.overflow="hidden";
			if(popTop!=null)
			  	popTop.style.display="none";
			parDiv.style.width="250px";
			parDiv.style.height="136px";
			parDiv.style.marginLeft="-150px";
			parDiv.style.top=(parent.document.body.scrollTop+parent.document.body.clientHeight/2-130)+"px";
			parDiv.style.left="50%";
			parDiv.style.height=height+"px";
			conDiv.style.marginTop="0px";
			conDiv.style.height=(height-6)+"px";
			iframeDiv.style.height=(height-6)+"px";
			//iframeDiv.setAttribute("frameborder", "1",0);

			setTimeout("reloadPraent()",1000);
		}
		
		//刷新父页面
		function reloadPraent(){
			if('${redir}'!=''){
				switch('${redir}'){
					case 'ord1'://订单详情页面
						parent.descPop("orderAction.do?op=showOrdDesc&code=${sodCode}");
						parent.loadList();
						break;
					case 'wms_in'://入库详情页面
						parent.window.open("wmsManageAction.do?op=wwiDesc&wwiId=${eId}");
						parent.history.go(0);
						break;
					case 'wms_out'://出库详情页面
						parent.window.open("wwoAction.do?op=wwoDesc&wwoId=${eId}");
						parent.history.go(0);
						break;
					case 'wms_change'://调拨详情页面
						parent.window.open("wwoAction.do?op=wchDesc&wchId=${eId}&wmsCode=${wmsCode}");
						parent.history.go(0);
						break;
					case 'wms_check'://盘点详情页面
						parent.window.open("wwoAction.do?op=wmcDesc&wmcId=${eId}");
						parent.history.go(0);
						break;
					case 'acc1'://已入账
						parent.document.location.href="accountAction.do?op=findAccIn&listType=0";
						break;
					case 'acc2'://已出账
						parent.document.location.href="accountAction.do?op=findAccOut&listType=0";
						break;
					case 'noRf'://不刷新
						break;
				}
			}
			else if('${isIfrm}'!=''&&'${isIfrm}'=='1'){//刷新详情页面中的iframe
				if(parent.frames['ifrList']!=null){
					if(parent.frames['ifrList'].loadList!=undefined){
						parent.frames['ifrList'].loadList();
					}
					else{
						parent.frames['ifrList'].location.reload();
					}
				}
			}
			else{
				//parent.history.go(0);
				if(parent.loadList!=undefined){
					parent.loadList();
				}
				else{
					parent.history.go(0);
				}
			}
			cancel("${wIdPre}");//关闭当前层
		}
	window.onload=function(){
		//关闭二级弹出层
		if("${wIdPre}" == ""){
			parent.removeCd("brow_popDiv");	
			parent.removeCd("att_popDiv");	
		}
		loadStyle();
	}
  </script>
  </head>
  
  <body>  
      <div id="popSuc"> 
      	<br/><br/>
        <span class="title bold"><img style='vertical-align:middle;' src='crm/images/content/bigSuc.gif'/>&nbsp;&nbsp;${msg}成功!</span>
      </div> 
	</body>
</html>
