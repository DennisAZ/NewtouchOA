var Process = Class.create();
Process.prototype = {
    
 initialize: function(cfgs) {
  this.config(cfgs);    
 },
  config : function(cfgs) {
    this.id = cfgs.id;
    this.name = cfgs.name;
    this.processIndex = cfgs.processIndex;
  }
} 