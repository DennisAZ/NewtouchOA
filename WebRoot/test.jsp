<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script  type="text/javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script>
function doInit(){
	var cfgs1={
	db_id : 'db1',
    warehouse_id : 'wh1',
    out_number : 'n1'
	};
	var po1 = new ProductOut(cfgs1);
	var cfgs2={
	db_id : 'db2',
    warehouse_id : 'wh2',
    out_number : 'n2'
	};
	var po2 = new ProductOut(cfgs2);
	
	var poArray = new Array();
	poArray.push(po1);
	poArray.push(po2);
	
	var cfgs3={
	 pro_id : 'pro1',
     cache_pro_id : 'cpro1',
     productOutArr : poArray,
     price : '10.2',
     pod_id : 'pod1',
	};
	var pd = new PD(cfgs3);
	
	alert(pd.show());
	pd.removeProductOut(po2);
	alert(pd.show());
}
var PD = Class.create();
PD.prototype = {
    
 initialize: function(cfgs) {
  this.config(cfgs);    
 },
  /**
   * 类的配置
   */
  config : function(cfgs) {
    this.pro_id = cfgs.pro_id;//产品id
    this.cache_pro_id = cfgs.cache_pro_id;//产品缓存主键id
    this.productOutArr = cfgs.productOutArr;
    this.price = cfgs.price;//单价
    this.pod_id = cfgs.pod_id;//销售明细单id
  },
  
  removeProductOut:function(productOut){
	  var poArray = new Array();
	  for(var i=0;i<this.productOutArr.length;i++){
		  if(!this.productOutArr[i].equals(productOut)){
			  poArray.push(this.productOutArr[i]);
		  }
	  }
	  this.productOutArr = poArray;
  },
  
  show:function(){
	  var str="";
	  for(var i=0;i<this.productOutArr.length;i++){
		 if(str!=""){
			 str+=" | ";
		 }
		 str+=this.pro_id+" "+this.cache_pro_id+" "+this.price+" "+this.pod_id+" "+this.productOutArr[i].db_id;
	  }
	  return str;
  }
}
var ProductOut = Class.create();
ProductOut.prototype = {
    
 initialize: function(cfgs) {
  this.config(cfgs);    
 },
  /**
   * 类的配置
   */
  config : function(cfgs) {
    this.db_id = cfgs.db_id;//库存id
    this.warehouse_id = cfgs.warehouse_id;//仓库id
    this.out_number = cfgs.out_number;//出货数量
  },
  
  equals: function(po){
	  if(this.db_id == po.db_id && this.warehouse_id==po.warehouse_id && this.out_number == po.out_number){
		  return true;
	  }
	  return false;
  }
}
</script>
</head>
<body onload="doInit();">
</body>
</html>
