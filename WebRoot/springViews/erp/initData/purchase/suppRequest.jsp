<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp"%>
<%
	String  proName=request.getParameter("proName");
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" href="<%=cssPath%>/style.css">
		<link rel="stylesheet" href="<%=cssPath%>/page.css">
		<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css">
		<!-- 封装表单的数据提交 -->
		<script type="text/javascript"src="<%=contextPath%>/springViews/js/jquery-1.4.2.js">
		jQuery.noConflict();
		</script>
		<script type="text/javascript" src="<%=contextPath%>/springViews/js/json.js" /></script>
		<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
		<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
		<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
		<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
		<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
		<script type="text/javascript" src="<%=contextPath %>/cms/station/js/util.js"></script>
		<script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
		<script type="text/javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
		<script type="text/javascript" src="<%=contextPath%>/core/funcs/mobilesms/js/isdatetime.js" ></script>
		<script> 
var pageMgr = null;
var parentWindowObj;
var proName="<%=proName%>";
function doInit(){
	parentWindowObj = window.dialogArguments;
	querySupplier(proName);
}

	var pageMgr = null;
	var flag="";
 function  querySupplier(parm){
 	var arrParm=parm.split(",");
 	jQuery('#productId').val(arrParm[0]);
 	flag=arrParm[2];
    var proNames=encodeURIComponent(arrParm[1]);
   
 	var url="<%=contextPath%>/SpringR/purchase/supplierList?proName="+proNames;

	 var cfgs = {
	      dataAction: url,
	      container: "listContainer",
	      colums: [
	         {type:"hidden", name:"sup_id", text:"id"},
	         {type:"data", name:"supName",  width: '5%', text:"供货商名称",render:recordCenterFunc},    
	         {type:"data", name:"supPhone",  width: '5%', text:"电话",render:recordCenterFunc},    
	         {type:"data", name:"supMob",  width: '5%', text:"手机",render:recordCenterFunc},
	         {type:"data", name:"products",  width: '5%', text:"产品",render:recordCenterFunc},
	         {type:"data", name:"price",  width: '5%', text:"单价",render:recordCenterFunc},
	         {type:"data", name:"lastDate",  width: '5%', text:"最后更新时间",render:recordCenterFunc},
	         {type:"data", name:"hasTax",  width: '5%', text:"是否含税率",render:recordCenterFunc},
	          {type:"selfdef", text:"操作", width: '10%',render:opt}
	         ]
	    };
    
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      showCntrl('listContainer');
      var mrs = " 共 " + total + " 条记录 ！";
      showCntrl('delOpt');
    }else{
      WarningMsrg('无符合条件的供货商信息', 'msrg');
    }
 
}
 function opt(cellData, recordIndex, columIndex) {
	var id = this.getCellData(recordIndex, "sup_id");
	var supName = this.getCellData(recordIndex, "supName");
	var supPhone = this.getCellData(recordIndex, "supPhone");
	var supMob = this.getCellData(recordIndex, "supMob");
	var price = this.getCellData(recordIndex, "price");
	var para = id+","+supName+","+supPhone+","+supMob+","+price;
	return "<center><input type=\"button\" name=\"deleteFlag\" value='添加' onclick=\"checkMags('"+ para +"')\" ></center>";
}
 function recordCenterFunc(cellData){
  return "<center>" + cellData + "</center>";
}
var suppIds="";
var ids="";
var name="";
var proIds="";
function checkMags(parm) {
			
			
			var valueArray=parm.split(",");
			var productId=jQuery("#productId").val();
			
			var tds = "<tr><td></td><td align='center' nowrap>"+valueArray[1]+"</td>";//供货商名称
			tds += "<td align='center'  nowrap>"+valueArray[2]+"</td>";//电话
			tds += "<td align='center' nowrap>"+valueArray[3]+"</td>";//手机
			tds += "<td align='center' nowrap>"+valueArray[4]+"</td>";//标准单价
			tds += "<td align='center' nowrap><input type='hidden' id ='"+valueArray[0]+"SupId' value='"+valueArray[0]+"'><input type='button' value='删除' class='BigButton' onclick=\"deleteSup(this,'"+productId+"','"+valueArray[0]+"','"+valueArray[1]+"')\"></td></tr>";
			
			if (suppIds != "") {
				suppIds += ",";
			}
			if (ids != "") {
				ids += ",";
			}
			ids+=valueArray[0];
			if (proIds != "") {
				proIds += ",";
			}
			proIds+=valueArray[0]+","+productId;
			suppIds +=productId+","+valueArray[0];
			if (name != "") {
				name += ",";
			}
			name+=valueArray[0]+","+valueArray[1];
			if(flag==0){
			parentWindowObj.jQuery('#'+productId+"supName").val(name);
			parentWindowObj.jQuery('#'+productId+"Pro").val(suppIds);
			parentWindowObj.jQuery('#'+productId+"supId").val(ids);
			parentWindowObj.jQuery('#'+productId+"proId").val(proIds);
			parentWindowObj.jQuery('#pros_table'+productId).append(tds);
			}else{
			parentWindowObj.jQuery('#'+productId+"supName").val(name);
			parentWindowObj.jQuery('#'+productId+"Pro").val(suppIds);
			parentWindowObj.jQuery('#'+productId+"supId").val(ids);
			parentWindowObj.jQuery('#'+productId+"proId").val(proIds);
			parentWindowObj.parent.jQuery('#oldpros_table'+productId).append(tds);
			}
}
</script>
	</head>
	<body topmargin="5" onload="doInit()">
		<input type="hidden" id="productId" name="productId" />
		<table border="0" width="100%" cellspacing="0" cellpadding="3"
			class="small">
			<tr>
				<td class="Big">
					<img src="<%=imgPath%>/infofind.gif" align="absMiddle">
					<span class="big3">&nbsp;
						供货商选择</span>
				</td>
			</tr>
		</table>
		<br>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" style="display:none">
</div>

<div id="msrg">
</div>
	</body>
</html>