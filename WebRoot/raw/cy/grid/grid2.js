/*
 *申明全局变量 
 */
var Namespace = new Object();
Namespace.register = function(fullNS)
{
    // 将命名空间切成N部分, 比如Grandsoft、GEA等
    var nsArray = fullNS.split('.');
    var sEval = "";
    var sNS = "";
    for (var i = 0; i < nsArray.length; i++)
    {
        if (i != 0) sNS += ".";
        sNS += nsArray[i];
        // 依次创建构造命名空间对象（假如不存在的话）的语句
        // 比如先创建Grandsoft，然后创建Grandsoft.GEA，依次下去
        sEval += "if (typeof(" + sNS + ") == 'undefined') " + sNS + " = new Object();"
    }
    if (sEval != "") eval(sEval);
}


/*
 * 注册grid命名空间
 */
Namespace.register(YH.grid);
/**
 * 设计一个结构良好的Grid
 * Grid包括三大部件
 *  1.ColumnModel
 *  2.Data 功能：得到一组Record数据，考虑到数据模块功能的单一性
 *  3.GridPanel
 *  4.Operation
 */

/*
 *ColumnModel部件，定义每列的信息 
 * param config=[{id:"",header:"",dataIndex:"",style:{},hidden:""}]
 */
YH.grid.ColumnModel = function(config){
  
}

/*
 *SelectModel，定义行选模式 
 */
YH.grid.SelectModel = function(){
  
}

/*
 *Data部件，定义数据信息 
 * param config{fields:[],url:""}
 */
YH.grid.Data = function(config){
  this.fields = config.fields;
  this.url = config.url;
}

/*
 *GridPanel部件，定义数据信息 
 * param config{cm:ColumnModel,data:YH.grid.Data,style:{},tbar:,bbar:,el:"",sm:YH.grid.SelectModel,listener:{type:"",events:{}}}
 */
YH.grid.GridPanel = function(config){
  this.cm = config.cm;
  this.data = config.data;
  this.style = config.style;
  this.tbar = config.tbar;
  this.bbar = config.bbar;
  this.el = config.el;
  this.sm = config.sm;
  this.listener = config.listener;s
}

//GridPanel加载函数
YH.grid.GridPanel.prototype.load = function(){
  
}
//GridPanel重新加载函数
YH.grid.GridPanel.prototype.reLoad = function(){
  
}
//GridPanel事件添加函数
//param config{type:"",events:{onclick:function(){},...}} type ["row","cell"]
YH.grid.GridPanel.prototype.addListener = function(config){
  
}

/*
 *BottomBar底部操作区
 * param config{}
 */
YH.grid.BottomBar = function(config){
  
}
/*
 *TopBar顶部操作区
 * param config{}
 */
YH.grid.TopBar = function(config){
  
}












