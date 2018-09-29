<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>统计分类</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <link rel="stylesheet" type="text/css" href="css/style.css"/>
    <link rel="stylesheet" type="text/css" href="css/sta.css"/>
    <script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/prototype.js"></script>
    <style type="text/css">
		body{
			background-color:#fff;
		}
		.blueBox{
			background-color:#e9eff6;
			border:#3366cc 1px solid;
		}
    </style>
	<script language="javascript">
		function init(){
			var menuType =  parent.document.getElementById("statMenuType").value;
			var statType = parent.document.getElementById("statType").value;
			var typeTxts = new Array();
			var typeIds = new Array();
			var opMethods = new Array();
			switch(menuType){
				//客户
				case 'cus':
					jumpOp = "toCusStat";
					typeTxts =	["客户类型统计","客户来源统计","客户行业分布","开发到期客户"];
					typeIds =	["cusType","cusSou","cusInd","cusOnDate"];
					opMethods = ["statCusType","statCusSou","statCusInd","statCusOnDate"];
				    break;
				//订单
				case 'ord':
					jumpOp = "toSalStat";
					typeTxts = ["销售金额统计","未达最低销售额客户","业务提成统计","回款分析","产品提成分析","产品分析","应收款分析","销售提成分析"];
					typeIds = ["salM","lowestSals","salBack","spsAnalyse","psalBack","proAnalyse","recvAnalyse","saleAnalyse"];//["ordType","ordSou","ordEmpMon","paidEmpMon","ordPaidMon"];
					opMethods = ["statSalM","statLowestSals","statSalBack","statSpsAnalyse","statPsalBack","statProAnalyse","statRecvAnalyse","statSaleAnalyse"];//["statOrdType","statOrdSou","statOrdEmpMon","statPaidEmpMon","statOrdPaidMon"];
					break;
			}
			for(var i=0;i<typeIds.length;i++){
				var spanE = document.createElement("div");
				spanE.innerHTML=typeTxts[i];
				spanE.id=typeIds[i];
				if(typeIds[i] == parent.document.getElementById("statType").value){
					spanE.className="statMenuCur";
					if(parent.document.getElementById("typTxt")!=null){
						parent.document.getElementById("typTxt").value = typeTxts[i];
					}
					if(parent.document.getElementById("opMethod")!=null){
						parent.document.getElementById("opMethod").value = opMethods[i];
					}
				}
				else {
					spanE.className="statMenu";
					spanE.onmouseover=function(){
						this.className='statMenuOver';
					}
					spanE.onmouseout=function(){
						this.className='statMenu';
					}
				}
				spanE.opArg = typeIds[i];//"to"+opMethods[i].substring(0,1).toUpperCase( ) + opMethods[i].substring(1);
				spanE.onclick = function(){
					parent.location.href="../statAction.do?op="+jumpOp+"&statType="+this.opArg;
				};
				$("topChoose").insert(spanE);
			}
		}
		
		window.onload=function(){
			init();
		}
</script>
</head>

<body>
    <div id="topChoose" style="text-align:center;"></div>
</body>
</html>

