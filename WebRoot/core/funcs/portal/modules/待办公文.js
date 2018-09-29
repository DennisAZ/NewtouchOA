{
  tbar: [{
    id: 'close'
  }, {
    id: 'more',
    preventDefault: true,
    handler: function(e, t, p) {
      openUrl({
        text: "待办公文",
        url: contextPath + "/core/funcs/doc/flowrun/list/index.jsp?sortName=%2525E5%25258F%252591%2525E6%252596%252587"
      });
    }
  }],
  width: "500px",
  layout: "cardlayout",
  layoutCfg: {
    tabs: true,
    fit: false
  },
  "items":[{
    xtype: "grid",
    title: '待办发文',
    tabBtn: {
      btnText: '待办发文',
      xtype: 'button',
      normalCls: 'tab-normal',
      activeCls: 'tab-active'
    },
    loader: {
      dataRender: function(data) {
      if (data.rtState == '0' && data.rtData) {
        return data.rtData.listData;
      }
      },
      url: contextPath + "/yh/core/funcs/doc/act/YHMyWorkAct/getMyWorkList.act?showLength=10&pageIndex=1&typeStr=6&isDesk=1"
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
         if (!e.sortId) {
           e.sortId = "";
         }
         var par = "runId=" + e.runId + "&flowId=" + e.flowId + "&prcsId=" + e.prcsId + "&flowPrcs=" + e.flowPrcs + "&sortId=" + e.sortId ;
         top.dispParts(contextPath + "/core/funcs/doc/flowrun/list/inputform/index.jsp?" + par, 1);
      });

      var title = $('<span class="title"></span>').html(e.title);
      var nowUser = $('<span class="title"></span>').html(e.nowUser);
      
      return a.append(title).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;").append(nowUser);
    }
  }],
  "xtype": "panel",
  height: "auto",
  "cmpCls": "jq-window",
  "title": "待办公文"
}