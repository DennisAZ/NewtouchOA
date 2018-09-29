/**
 * 分页导航条
 */
var YHJsPagePilot2 = Class.create();
YHJsPagePilot2.prototype = {
  initialize: function(cfgs) {
    this.config(cfgs);    
   },
   /**
    * 类的配置
    */
   config : function(cfgs) {
     this.dataAction = cfgs.dataAction;
     this.container = cfgs.container ? $(cfgs.container) : $(document.body);
     this.pageInfo.pageSize = parseInt(cfgs.pageSize);
     if(this.pageInfo.pageSize){
       var sizeList = cfgs.sizeList ? cfgs.sizeList : [20, 10, 30, 40, 2];
       var arry = new Array();
       arry[0] = this.pageInfo.pageSize;
       for(var i = 0 , j = 0 ; i < sizeList.length ; i ++){
         if(this.pageInfo.pageSize == sizeList[i]){
          continue;
         }
         arry[j+1] = sizeList[i];
         j++;
       }
       this.sizeList = arry;
     }
     this.pageInfo.totalRecord = cfgs.totalRecord ? parseInt(cfgs.totalRecord) : 0;
     this.pageInfo.pageIndex = cfgs.pageIndex ? parseInt(cfgs.pageIndex) : 0;
     this.opt = cfgs.opt;
     this.loadData = cfgs.loadData;
     this.isInit = true;
   },
   show: function() {
     if (this.isInit) {
       this.buildNevBar();    
       this.bindNevAction();  
      // alert(this.pageInfo.pageIndex);
       this.setPageInfo(this.pageInfo.pageIndex);
       this.refreshNevBar(this.pageInfo.pageIndex);
       this.isInit = false;
     }else {
       $("pgTopPanel2").show();
     }
     if($("pgTopPanel2")){
       $("pgMsrgPanel2").hide();
     }
   },
   /**
    * 页面信息
    */
   pageInfo: {
     totalRecord: 0,
     totalPage: 0,
     pageIndex: 0,
     pageSize: 0
   },
   /**
    * 设置页面信息
    */
   setPageInfo: function(pageIndex) {
     //var pageJson = this.dataCatch[pageIndex];
     if (!this.pageInfo.totalRecord) {
       return;
     }
     var pageCnt = parseInt((parseInt(this.pageInfo.totalRecord) / parseInt(this.pageInfo.pageSize)));
     if (this.pageInfo.totalRecord % this.pageInfo.pageSize != 0) {
       pageCnt++;
     }
     this.pageInfo.totalPage = pageCnt;
     this.pageInfo.pageIndex = pageIndex;
   },
   /**
    * 构造导航栏
    */
   buildNevBar: function() {
     if (!this.isInit) {
       return;
     }
     var nevBarText = getTextRs(contextPath + "/subsys/portal/guoyan/inc/pagenevbar2.jsp");
     this.container.innerHTML = nevBarText;
     
     //加载页面记录大小列表
     var selectArray = new Array();
     for (var i = 0; i < this.sizeList.size(); i++) {
       var size = this.sizeList.get(i);
       var record = new CodeRecord();
       record.code = size;
       record.desc = size;
       selectArray.add(record);
     }
     var selectSize = $("selectPageSize2");
     //alert(selectSize);
     loadSelectData(selectSize, selectArray);
   },
   /**
    * 变换页面记录多好
    */
   changePageSize: function() {
     var selectSize = $("selectPageSize2");
     if (!selectSize) {
       return;
     }
     this.pageInfo.pageSize = selectSize.value ? parseInt(selectSize.value) : 20;
     this.setPageInfo(0);
     this.showPage(0);
     //this.refreshAll();
   },
   /**
    * 为导航栏绑定事件处理
    */
   bindNevAction: function() {
     if (!this.isInit) {
       return;
     }
     //pageSize选择
     $("selectPageSize2").observe("change", this.changePageSize.bind(this));
     //第一页
     $("btnPgFirst2").observe("click", this.firstPage.bind(this));
     //前一页
     $("btnPgPre2").observe("click", this.prePage.bind(this));
     //页面跳转页码输入
     $("pageIndex2").observe("blur", this.gotoPage.bind(this));
     //后一页
     $("btPgNext2").observe("click", this.nextPage.bind(this));
     //最后一页
     $("btnPgLast2").observe("click", this.lastPage.bind(this));
     //刷新当前页
     $("btnRefresh2").observe("click", this.refreshPage.bind(this));
     //全选/取消
   },
   /**
    * 显示某页数据
    */
   showPage: function(pageIndex) {
     if(pageIndex >= this.pageInfo.totalPage){
       pageIndex = this.pageInfo.totalPage;
     }
     if(pageIndex <= 0){
       pageIndex = 0;
     }
     this.pageInfo.pageIndex = pageIndex;
     this.setPageInfo(pageIndex);
     this.refreshNevBar(pageIndex);
     if(this.loadData){
       this.loadData.bind(this, this.pageInfo)();
     }
   },
   /**
    * 第一页


    */
   firstPage: function() {
     this.showPage(0);
   },
   /**
    * 前一页


    */
   prePage: function() {
     this.showPage(this.pageInfo.pageIndex - 1);
   },
   /**
    * 跳转到


    */
   gotoPage: function() {
     var pageNo = $("pageIndex2").value;
     if (!pageNo) {
       return;
     }
     if (!isNumber(pageNo)) {
       alert("页码需要是数值");
       selectLast($("pageIndex2"));
       return;
     }
     pageNo = parseInt(pageNo);
     if (pageNo < 1 || pageNo > this.pageInfo.totalPage) {
       alert("请输入合理页码");
       selectLast($("pageIndex2"));
       return;
     }
     this.showPage(pageNo - 1);
   },
   /**
    * 后一页


    */
   nextPage: function() {
     this.showPage(this.pageInfo.pageIndex + 1);
   },
   /**
    * 最后一页
    */
   lastPage: function() {
     this.showPage(this.pageInfo.totalPage - 1);
   },
   /**
    * 刷新列表
    * @index           传递时刷新指定页面,否则刷新当前页
    */
   refreshPage: function(dom,index) {
     if (!index && index!== 0) {
       index = this.pageInfo.pageIndex;
     }
    // this.clearCatch(index);
     this.showPage(index);
    },
    refreshNevBar: function() {
     //第一页


     if (this.pageInfo.pageIndex < 1) {
       this.switchClass($("btnPgFirst2"), "pgFirst", "pgFirstDisabled");
       $("btnPgFirst2").disabled = true;
     }else {
       this.switchClass($("btnPgFirst2"), "pgFirstDisabled", "pgFirst");
       $("btnPgFirst2").disabled = false;
     }
     //前一页


     if (this.pageInfo.pageIndex < 1) {
       this.switchClass($("btnPgPre2"), "pgPrev", "pgPrevDisabled");
       $("btnPgPre2").disabled = true;
     }else {
       this.switchClass($("btnPgPre2"), "pgPrevDisabled", "pgPrev");
       $("btnPgPre2").disabled = false;
     }
     //页面跳转页码输入
     if (this.pageInfo.totalPage < 1) {
       $("pageIndex2").disabled = true;
       $("pageIndex2").value = "";
     }else {
       $("pageIndex2").disabled = false;
       $("pageIndex2").value = this.pageInfo.pageIndex + 1;
     }
     //后一页

     if (this.pageInfo.pageIndex >= this.pageInfo.totalPage - 1) {
       this.switchClass($("btPgNext2"), "pgNext", "pgNextDisabled");
       $("btPgNext2").disabled = true;
     }else {
       this.switchClass($("btPgNext2"), "pgNextDisabled", "pgNext");
       $("btPgNext2").disabled = false;
     }
     //最后一页
     if (this.pageInfo.pageIndex >= this.pageInfo.totalPage - 1) {
       this.switchClass($("btnPgLast2"), "pgLast", "pgLastDisabled");
       $("btnPgLast2").disabled = true;
     }else {
       this.switchClass($("btnPgLast2"), "pgLastDisabled", "pgLast");
       $("btnPgLast2").disabled = false;
     }
     //共XXX页
     $("pageCount2").innerHTML = this.pageInfo.totalPage;
   },
   /**
    * 切换样式
    */
   switchClass: function(cntrl, srcCls, destCls) {
     cntrl.removeClassName(srcCls);
     cntrl.addClassName(destCls);
   }
   
}