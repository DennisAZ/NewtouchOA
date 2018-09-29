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
    this.wh_id = cfgs.wh_id;//仓库id
    this.cache_pro_id = cfgs.cache_pro_id;//产品缓存主键id
    this.price = cfgs.price;//单价
    this.productOut=cfgs.productOut;//库存数组
    this.purchase_num=cfgs.purchase_num;//采购数量;
    this.pur_id=cfgs.pur_id;//采购单id
   	this.purchase_send_num=cfgs.purchase_send_num;//已经收货数量
   	this.ppro_id=cfgs.ppro_id;//产品关联id
   	this.ppo_id=cfgs.ppo_id;//出货单id
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
    this.wh_id = cfgs.wh_id;//仓库id
    this.number = cfgs.number;//出货数量
  }
}  
  