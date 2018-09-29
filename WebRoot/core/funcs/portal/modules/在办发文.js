{
  cls: "window-height",
  tbar: [{
    id: 'close'
  }, {
    id: 'more',
    preventDefault: true,
    handler: function(e, t, p) {
      openUrl({
        text: "在办发文",
        url: contextPath + "/core/funcs/doc/flowrun/list/index.jsp?type=3"
      });
    }
  }],
  width: "500px",
  "items":[{
    xtype: "grid",
    loader: {
	  dataRender: function(data) {
	    if (data.rtState == '0' && data.rtData) {
	      return data.rtData.listData;
	    }
  	  },
      url: contextPath + "/yh/core/funcs/doc/act/YHMyWorkAct/getMyWorkList.act?showLength=10&pageIndex=1&typeStr=3&isDesk=1"
    },
    rowRender: function(i, e) {
    	
      /**
       * 打印表单
       * @param runId
       * @param flowId
       * @return
       */
   	  function formView(runId , flowId) {
   	    var url = contextPath + "/core/funcs/doc/flowrun/list/print/index.jsp?runId="+runId+"&flowId="+flowId;
   	    window.open(url ,"","status=0,toolbar=no,menubar=no,width="+(screen.availWidth-12)+",height="+(screen.availHeight-38)+",location=no,scrollbars=yes,resizable=yes,left=0,top=0");
   	  }
   	  
      var a = $("<a href='javascript:window.location.href=window.location.href'></a>");
       a.click(function() {
    	   formView(e.runId , e.flowId );
      });
      var title = $('<span class="title"></span>').html(e.title);
      var nowUser = $('<span class="title"></span>').html(e.nowUser);
      var prcsNameNext = $('<span class="title"></span>').html(e.prcsNameNext);
      return a.append(title).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;").append(nowUser).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;").append(prcsNameNext);
    }
  }],
  "xtype": "panel",
  height: "auto",
  "cmpCls": "jq-window",
  "title": "正办发文"
}
