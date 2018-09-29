var PR = Class.create();
PR.prototype = {
    
 initialize: function(cfgs) {
  this.config(cfgs);    
 },
  /**
   * 类的配置
   */
  config : function(cfgs) {
    this.pro_id = cfgs.pro_id;//产品id
    this.pro_code = cfgs.pro_code;//产品编号
    this.pro_name = cfgs.pro_name;//产品名称
    this.typeName=cfgs.typeName;//规格型号
    this.unitName=cfgs.unitName//计量单位;
   	this.price=cfgs.price;//标注单价
   	this.sup=cfgs.sup;//供货商
   	
  },
  
  
  }
var SUP= Class.create();
SUP.prototype = {
    
 initialize: function(cfgs) {
  this.config(cfgs);    
 },
  /**
   * 类的配置
   */
  config : function(cfgs) {
	 this.supId=cfgs.supId;// 供货商id
  this.supName=cfgs.supName;//供货商名称
   	this.supPrice=cfgs.supPrice;//供货商产品单价
   	
  },
  
  
  }
  var PUR = Class.create();
	PUR.prototype = {
    
 initialize: function(cfgs) {
  this.config(cfgs);    
 },
  /**
   * 类的配置
   */
  config : function(cfgs) {
    this.pro_id = cfgs.pro_id;//产品id
    this.pro_code = cfgs.pro_code;//产品编号
    this.pro_name = cfgs.pro_name;//产品名称
    this.typeName=cfgs.typeName;//规格型号
    this.unitName=cfgs.unitName//计量单位;
   	this.price=cfgs.price;//标注单价
   	this.total=cfgs.total;//供货商id
   	this.date=cfgs.date;//供货商名称
  },
  
  
  }