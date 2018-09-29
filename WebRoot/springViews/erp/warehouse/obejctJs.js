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
    this.productOut = cfgs.productOut;
    this.price = cfgs.price;//单价
    this.pod_id = cfgs.pod_id;//采购明细单id
    this.order_num=cfgs.order_num;
   	this.already_send_num=cfgs.already_send_num;
  },
  
  removeProductOut:function(productOut){
		  if(this.productOut.equals(productOut)){
			 delete productOut.db_log_id;
			 delete productOut.wh_id;
			 delete productOut.number;
			 delete this.pro_id;
			 delete this.cache_pro_id;
			 delete this.productOut;
			 delete this.price;
			 delete this.pod_id;
			 delete this.order_num;
			 delete this.already_send_num;
	  }
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
    this.db_log_id = cfgs.db_log_id;//库存id
    this.wh_id = cfgs.wh_id;//仓库id
    this.number = cfgs.number;//出货数量
  },
  
  equals: function(po){
	  if(this.db_log_id == po.db_log_id && this.wh_id==po.wh_id && this.number == po.number){
		  return true;
	  }
	  return false;
  	}
  }