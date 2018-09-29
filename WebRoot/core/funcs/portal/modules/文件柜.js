{
  tbar: [{
    id: 'close'
  }, {
    id: 'more',
    preventDefault: true,
    handler: function(e, t, p) {
      openUrl({
        text: "文件柜",
        url: contextPath + "/core/funcs/filefolder/index.jsp"
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
    title: '最新文件',
    tabBtn: {
      btnText: '最新文件',
      xtype: 'button',
      normalCls: 'tab-normal',
      activeCls: 'tab-active'
    },
    loader: {
      url: contextPath + "/yh/core/funcs/filefolder/act/YHFileContentAct/selectFolderInfoToDisk.act?index=1"
    },
    rowRender: function(i, prc) {
      var returnContentId = prc.contentId;
      var returnSortId = prc.sortId;
      var subject = prc.subject;
      var sendTime = prc.sendTime;
      
      if (sendTime && typeof sendTime == "string" && sendTime.length > 10) {
        sendTime = sendTime.substring(0, 10);
      }
      
      var sortName = prc.sortName;
      var limitSubject = "";
      if (subject.length >= 20){
        limitSubject = subject.substring(0, 20) + " ...";
      } else {
        limitSubject = subject;
      }
      
      var title = $('<span class="title"></span>');
      title.append(sortName);
      var title2 = $('<span class="title"></span>');
      title2.append(limitSubject);
      
      var a = $('<a href="javascript:void(0)"></a>');
      a.attr("title", subject);
      var desc = $('<span class="desc"></span>');
      desc.append("(" + sendTime + ")");
      a.click(function() {
        top.dispParts(contextPath + "/core/funcs/filefolder/index.jsp?showFlag=CONTENT&sortId=" + returnSortId + "&contentId=" + returnContentId, 1)
      });
      return a.append(title).append(title2).append(desc);
    }
  }, {
    xtype: "grid",
    title: '文件柜',
    tabBtn: {
      btnText: '文件柜',
      xtype: 'button',
      normalCls: 'tab-normal',
      activeCls: 'tab-active'
    },
    loader: {
      url: contextPath + "/yh/core/funcs/filefolder/act/YHFileContentAct/selectFolderInfoToDisk.act?index=2"
    },
    rowRender: function(i, prc) {
      var returnContentId = prc.contentId;
      var returnSortId = prc.sortId;
      var subject = prc.subject;
      
      var sortName = prc.sortName;
      
      var title = $('<span class="title"></span>');
      title.append(sortName);
      
      var a = $('<a href="javascript:void(0)"></a>');
      a.attr("title", subject);
      
      a.click(function() {
        top.dispParts(contextPath + "/core/funcs/filefolder/index.jsp?sortId=" + returnSortId, 1);
      });
      return a.append(title);
    }
  }],
  "xtype": "panel",
  height: "auto",
  "cmpCls": "jq-window",
  "title": "文件柜"
}
