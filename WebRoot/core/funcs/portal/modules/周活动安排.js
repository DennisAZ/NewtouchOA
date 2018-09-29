{
  tbar: [{
    id: 'close'
  }, {
    id: 'more',
    preventDefault: true,
    handler: function(e, t, p) {
      openUrl({
        text: "周活动安排",
        url: contextPath + "/subsys/oa/active/index.jsp"
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
    title: '今日',
    tabBtn: {
      btnText: '今日',
      xtype: 'button',
      normalCls: 'tab-normal',
      activeCls: 'tab-active'
    },
    loader: {
      url: contextPath + "/yh/subsys/oa/active/act/YHActiveAct/selectActive.act?index=1"
    },
    rowRender: function(i, e) {
      var seqId = e.seqId;
      var activeTime = e.activeTime;
      var activeContent = e.activeContent;
      var time = $('<span class="title"></span>');
      time.append(activeTime.substr(11, 5));
      
      var a = $('<a href="javascript: void(0)"></a>');
      
      var title = $('<span class="title"></span>');
      title.append(activeContent);
      
      a.click(function() {
        var URL = contextPath + "/subsys/oa/active/activenote.jsp?seqId=" + seqId;
        window.open(URL, "my_note", "height=200,width=200,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=600,top=300,resizable=yes");
      });
      return a.append(time).append(title);
    }
  }, {
    xtype: "grid",
    title: '本周',
    tabBtn: {
      btnText: '本周',
      xtype: 'button',
      normalCls: 'tab-normal',
      activeCls: 'tab-active'
    },
    loader: {
      url: contextPath + "/yh/subsys/oa/active/act/YHActiveAct/selectActive.act?index=2"
    },
    rowRender: function(i, e) {
      var seqId = e.seqId;
      var activeTime = e.activeTime;
      var activeContent = e.activeContent;
      var time = $('<span class="title"></span>');
      time.append(activeTime.substr(0, 16));
      
      var a = $('<a href="javascript: void(0)"></a>');
      
      var title = $('<span class="title"></span>');
      title.append(activeContent);
      
      a.click(function() {
        var URL = contextPath + "/subsys/oa/active/activenote.jsp?seqId=" + seqId;
        window.open(URL, "my_note", "height=200,width=200,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=600,top=300,resizable=yes");
      });
      return a.append(time).append(title);
    }
  }],
  "xtype": "panel",
  height: "auto",
  "cmpCls": "jq-window",
  "title": "周活动安排"
}
