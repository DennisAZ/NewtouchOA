<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<title>Insert  title  here</title>
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/ExchangeSelect.css">
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/datastructs.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/smartclient.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/cmp/ExchangeSelect1.0.js"  ></script>

<script  type="text/javascript">
function  doInit(){
    var  selected  =  [{value:'1',text:'公告通知',parentId:2},{value:'2',text:'内部邮件'},{value:'3',text:'日程安排'}];
    var  disselected  =  [{id:1,text:'系统模块',isParent:true},{value:'6',text:'工资上报'},{value:'5',text:'车辆申请',parentId:1},{value:'7',text:'工作计划',parentId:1},{id:2,text:'功能模块',isParent:true}];
    new  ExchangeSelectbox({containerId:'div'
        ,  selectedArray:selected
        ,  disSelectedArray:disselected  
        ,isSort:true  
        ,isOneLevel:false
        ,title:'选择'
        ,selectName:'nextProcess'
            ,selectedChange:exchangeHandler});  
        
}
function  exchangeHandler(ids){
    $('selectValue').value  =  ids;
}
</script>
</head>
<body  onload="doInit()">
选中的值：<input  id="selectValue"  type="text"  value=""/>
<div  id="div"></div>
</body>
</html>
