{
  tbar: [{
    id: 'close'
  }, {
    id: 'more',
    preventDefault: true,
    handler: function(e, t, p) {
      openUrl({
        text: "待办工作",
        url: contextPath + "/core/funcs/workflow/flowrun/list/index.jsp"
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
    title: '待办工作',
    tabBtn: {
      btnText: '待办工作',
      xtype: 'button',
      normalCls: 'tab-normal',
      activeCls: 'tab-active'
    },
    loader: {
      url: contextPath + "/yh/core/funcs/workflow/act/YHMyWorkAct/getMyWork.act"
    },
    rowRender: function(i, e) {
      var status = "";
      var prcsFlag = e.prcsFlag;
      
      var img = $('<img class="icon"></img>');
      if (prcsFlag ==  '1') {
        img.attr("src", imgPath + "/email_close.gif");
        img.attr("alt", '未接收');
      } else if ( prcsFlag == '2' ) {
        img.attr("src", imgPath + "/email_open.gif");
        img.attr("alt", '已接收');
      } else {
        img.attr("src", imgPath + "/flow_next.gif");
        img.attr("alt", '已办结');
      }
      var a = $('<a href="javascript: void(0)"></a>');
      var title = $('<span class="title"></span>');
      title.append("[" + e.flowName + "]");
      a.append(title);
      a.click(function() {
        top.dispParts(contextPath + '/core/funcs/workflow/flowrun/list/index.jsp?flowId=' + e.flowId, 1);
      });
      
      var tmp = e.runName+'  '+e.prcsName;
      if (e.timeUse) {
        tmp += "&nbsp;<font color=red>" + e.timeUse + "</font>";
      }
      var content = $('<span class="title"></span>');
      content.append(tmp);
      var ac = $('<a href="javascript:window.location.href=window.location.href"></a>');
      ac.append("&nbsp;&nbsp;");
      ac.append(img);
      ac.append(content);
      
      ac.click(function() {
        if (prcsFlag != '3') {
          top.dispParts(contextPath + '/core/funcs/workflow/flowrun/list/inputform/index.jsp?runId=' + e.runId + '&flowId=' + e.flowId + '&prcsId=' + e.prcsId + '&flowPrcs=' + e.flowPrcs, 1);
        } else {
          var url = contextPath + "/core/funcs/workflow/flowrun/list/print/index.jsp?runId=" + e.runId + "&flowId=" + e.flowId;
          window.open(url ,"","status=0,toolbar=no,menubar=no,width="+(screen.availWidth-12)+",height="+(screen.availHeight-38)+",location=no,scrollbars=yes,resizable=yes,left=0,top=0");
        }
      });
      
      return $('<div></div>').append(a).append(ac);
    }
  }, {
    title: '会签工作',
    tabBtn: {
      btnText: '会签工作',
      xtype: 'button',
      normalCls: 'tab-normal',
      activeCls: 'tab-active'
    },
    xtype: "grid",
    loader: {
      url: contextPath + "/yh/core/funcs/workflow/act/YHMyWorkAct/getSign.act"
    },
    rowRender: function(i, e) {
      var status = "";
      var prcsFlag = e.prcsFlag;
      
      var img = $('<img class="icon"></img>');
      if (prcsFlag ==  '1') {
        img.attr("src", imgPath + "/email_close.gif");
        img.attr("alt", '未接收');
      } else if ( prcsFlag == '2' ) {
        img.attr("src", imgPath + "/email_open.gif");
        img.attr("alt", '已接收');
      } else {
        img.attr("src", imgPath + "/flow_next.gif");
        img.attr("alt", '已办结');
      }
      
      var tmp = e.flowName+'  '+e.runName;
      var content = $('<span class="title"></span>');
      content.append(tmp);
      var ac = $('<a href="javascript: window.location.href=window.location.href"></a>');
      ac.append("&nbsp;&nbsp;");
      ac.append(img);
      ac.append(content);
      
      ac.click(function() {
        if (prcsFlag != '3' && prcsFlag != '4') {
          top.dispParts(contextPath + '/core/funcs/workflow/flowrun/list/inputform/index.jsp?runId=' + e.runId + '&flowId=' + e.flowId + '&prcsId=' + e.prcsId + '&flowPrcs=' + e.flowPrcs, 1);
        } else {
          var url = contextPath + "/core/funcs/workflow/flowrun/list/print/index.jsp?runId=" + e.runId + "&flowId=" + e.flowId;
          window.open(url ,"","status=0,toolbar=no,menubar=no,width="+(screen.availWidth-12)+",height="+(screen.availHeight-38)+",location=no,scrollbars=yes,resizable=yes,left=0,top=0");
        }
      });
      
      return $('<div></div>').append(ac);
    }
  }, {
    title: '我的关注',
    tabBtn: {
      btnText: '我的关注',
      xtype: 'button',
      normalCls: 'tab-normal',
      activeCls: 'tab-active'
    },
    xtype: "grid",
    loader: {
      url: contextPath + "/yh/core/funcs/workflow/act/YHMyWorkAct/getFocusWork.act"
    },
    rowRender: function(i, e) {
      var title = $('<span class="title"></span>');
      title.append("[" + e.flowName + "]-" + e.runName);
      var ac = $('<a href="javascript: void(0)"></a>');
      ac.append(title);
      
      ac.click(function() {
        var url = contextPath + "/core/funcs/workflow/flowrun/list/print/index.jsp?runId=" + e.runId + "&flowId=" + e.flowId;
        window.open(url ,"","status=0,toolbar=no,menubar=no,width="+(screen.availWidth-12)+",height="+(screen.availHeight-38)+",location=no,scrollbars=yes,resizable=yes,left=0,top=0");
      });
      return ac;
    }
  }],
  "xtype": "panel",
  height: "auto",
  "cmpCls": "jq-window",
  "title": "待办工作"
}