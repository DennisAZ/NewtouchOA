{
  tbar: [{
    id: 'close'
  }, {
    id: 'more',
    preventDefault: true,
    handler: function(e, t, p) {
      openUrl({
        text: "工作日志",
        url: contextPath + "/core/funcs/diary/new/index.jsp"
      });
    }
  }],
  width: "500px",
  layout: "cardlayout",
  layoutCfg: {
    tabs: true,
    fit: false
  },
  my:{
    xtype: "grid",
    title: '个人日志',
    tabBtn: {
      btnText: '个人日志',
      xtype: 'button',
      normalCls: 'tab-normal',
      activeCls: 'tab-active'
    },
    loader: {
      url: contextPath + "/yh/core/funcs/diary/act/YHDiaryAct/desktopMyDiary.act?length=10",
      dataRender: function(data) {
        if(data.rtState == "0" && data.rtData) {
          return data.rtData;
        }
      }
    },
    rowRender: function(i, e) {
      var a = $('<a href="javascript:void(0)"></a>');
      var title = $('<span class="title"></span>');
      
      a.click(function() {
        var url = contextPath + "/core/funcs/diary/comment/index.jsp?diaId="+e.data.seqId+"&type=1&desktop=1";
        window.open(url);
      });
      
      if (!e.data.subject) {
        e.data.subject ="&nbsp;";
      }
      
      title.append(e.data.subject);
      a.append(title);
      
      var isRead = e.flag;
      var img = "";
      if (isRead) {
        img = "<img src=\""+ imgPath +"/email_new.gif\" alt=\"未读\" align=\"absmiddle\">";
      }
      return $('<div></div>').append(img).append(a).append("&nbsp;&nbsp;(").append(e.tip).append(")");
    }
  },
  other:{
    xtype: "grid",
    title: '员工日志',
    tabBtn: {
      btnText: '员工日志',
      xtype: 'button',
      normalCls: 'tab-normal',
      activeCls: 'tab-active'
    },
    loader: {
      url: contextPath + "/yh/core/funcs/diary/act/YHDiaryAct/desktopDiary.act?length=10",
      dataRender: function(data) {
        if(data.rtState == "0" && data.rtData) {
          return data.rtData;
        }
      }
    },
    rowRender: function(i, e) {
      
      var a = $('<a href="javascript:void(0)"></a>');
      var title = $('<span class="title"></span>');
      
      a.click(function() {
        var url = contextPath + "/core/funcs/diary/comment/index.jsp?diaId="+e.data.seqId+"&type=1&desktop=1";
        window.open(url);
      });
      if (!e.data.subject) {
        e.data.subject ="&nbsp;";
      }
      
      title.append(e.data.subject);
      a.append(title);
      
      var isRead = e.flag;
      var img = "";
      if (isRead) {
        img = "<img src=\""+ imgPath +"/email_new.gif\" alt=\"未读\" align=\"absmiddle\">";
      }
      return $('<div></div>').append(img).append(a).append("&nbsp;&nbsp;(").append(e.tip).append(")");
    }
  },
  getPriv:function(t) {
    var url = contextPath + '/yh/core/funcs/diary/act/YHDiaryAct/desktopDiaryPriv.act';
    t.items = [];
    var text = jQuery.ajax({
    type: "GET",
    dataType: "text",
    url: url,
    async : false
    }).responseText;
    var json = YH.parseJson(text);
    if(json.rtState == "0"){
      if (json.rtData.my == 1) {
        t.items.push(t.my);
      }
      if (json.rtData.priv == 1) {
        t.items.push(t.other);
      }
    　　}
  },
  "xtype": "panel",
  height: "auto",
  "cmpCls": "jq-window",
  "title": "工作日志",
  listeners: {
    initComponent: {
      before: function(e, t) {
        t.getPriv(t);
      }
    }
  }
}
