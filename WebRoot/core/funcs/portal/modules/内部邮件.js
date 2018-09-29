{
  tbar: [{
    id: 'close'
  }, {
    id: 'more',
    preventDefault: true,
    handler: function(e, t, p) {
      openUrl({
        text: "内部邮件",
        url: contextPath + "/core/funcs/email/index.jsp"
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
    title: '全部',
    tabBtn: {
      btnText: '全部',
      xtype: 'button',
      normalCls: 'tab-normal',
      activeCls: 'tab-active'
    },
    loader: {
      url: contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/deskMoudel.act?type=0",
      dataRender: function(data) {
        if(data.rtState == "0" && data.rtData.pageData) {
          return data.rtData.pageData;
        }
      }
    },
    rowRender: function(i, e) {
      var a = $('<a href="javascript:void(0);"></a>');
      var title = $('<span class="title"></span>');
      var  icon = "";
      a.click(function() {
        var toId = e.fromId;
        var url = contextPath + "/core/funcs/email/new/index.jsp?toId=" + toId;
        
        /**
         * 打开模态窗口,不能改变窗口大小
         */
        function openDialog(actionUrl, width, height) {
          var locX = (screen.width - width) / 2;
          var locY = (screen.height - height) / 2;
          var attrs = null;
          
          attrs = "status:no;directories:no;scroll:no;resizable:no;";
          attrs += "dialogWidth:" + width + "px;";
          attrs += "dialogHeight:" + height + "px;";
          attrs += "dialogLeft:" + locX + "px;";
          attrs += "dialogTop:" + locY + "px;";
          return window.showModalDialog(actionUrl, self, attrs);
        }
        
        openDialog(url,'800', '650');
      });
      title.append(e.fromName);
      a.append(title);
      
      var a2 = $('<a href="javascript:window.location.href=window.location.href"></a>');
      var title2 = $('<span class="title"></span>');
      
      a2.click(function() {
        top.dispParts(contextPath + "/core/funcs/email/inbox/read_email/index.jsp?seqId=" + e.emailBodyId + "&mailId=" + e.emailId, 1)
      	 var obj = $('div img');
      	 if(icon ==  null || icon == "")
        {
        	return ;
        }else if(obj != null && obj != 'undefined'){
        	obj.remove();
        	return;
        }
      });
      
      title2.append(e.subject);
      a2.append(title2);
      
      if (e.readflag == '0') {
        icon = $('<img class="icon"></img>');
        icon.attr("src", imgPath + "/email_new.gif");
      } 
      
      return $('<div></div>').append(icon).append(a).append("&nbsp;&nbsp;&nbsp;").append(a2);
    }
  }, {
    xtype: "grid",
    title: '未读',
    tabBtn: {
      btnText: '未读',
      xtype: 'button',
      normalCls: 'tab-normal',
      activeCls: 'tab-active'
    },
    loader: {
      url: contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/deskMoudel.act?type=1",
      dataRender: function(data) {
        if(data.rtState == "0" && data.rtData.pageData) {
          return data.rtData.pageData;
        }
      }
    },
    rowRender: function(i, e) {
      var a = $('<a href="javascript:void(0);"></a>');
      var title = $('<span class="title"></span>');
      var icon = "";
      
      a.click(function() {
        var toId = e.fromId;
        var url = contextPath + "/core/funcs/email/new/index.jsp?toId=" + toId;
        openDialog(url,'800', '650');
      });
      
      title.append(e.fromName);
      a.append(title);
      
      var a2 = $('<a href="javascript:window.location.href=window.location.href"></a>');
      var title2 = $('<span class="title"></span>');
      
      a2.click(function() {
        top.dispParts(contextPath + "/core/funcs/email/inbox/read_email/index.jsp?seqId=" + e.emailBodyId + "&mailId=" + e.emailId, 1)
     	var imgObj1 = $('div img');
        if(icon == null || icon == "")
        {
        	return ;
        }else if(imgObj1 != null && imgObj1 != 'undefined'){
        	imgObj1.remove();
        	return;
        }
      });
      
      title2.append(e.subject);
      a2.append(title2);
      
      if (e.readflag == '0') {
        icon = $('<img class="icon"></img>');
        icon.attr("src", imgPath + "/email_new.gif");
      } 
      
      return $('<div></div>').append(icon).append(a).append("&nbsp;&nbsp;&nbsp;").append(a2);
    }
  }, {
    xtype: "grid",
    title: '已读',
    tabBtn: {
      btnText: '已读',
      xtype: 'button',
      normalCls: 'tab-normal',
      activeCls: 'tab-active'
    },
    loader: {
      url: contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/deskMoudel.act?type=2",
      dataRender: function(data) {
        if(data.rtState == "0" && data.rtData.pageData) {
          return data.rtData.pageData;
        }
      }
    },
    rowRender: function(i, e) {
      var a = $('<a href="javascript:void(0);"></a>');
      var title = $('<span class="title"></span>');
      
      a.click(function() {
        var toId = e.fromId;
        var url = contextPath + "/core/funcs/email/new/index.jsp?toId=" + toId;
        
        /**
         * 打开模态窗口,不能改变窗口大小
         */
        function openDialog(actionUrl, width, height) {
          var locX = (screen.width - width) / 2;
          var locY = (screen.height - height) / 2;
          var attrs = null;
          
          attrs = "status:no;directories:no;scroll:no;resizable:no;";
          attrs += "dialogWidth:" + width + "px;";
          attrs += "dialogHeight:" + height + "px;";
          attrs += "dialogLeft:" + locX + "px;";
          attrs += "dialogTop:" + locY + "px;";
          return window.showModalDialog(actionUrl, self, attrs);
        }
        
        openDialog(url,'800', '650');
      });
      
      title.append(e.fromName);
      a.append(title);
      
      var a2 = $('<a href="javascript:void(0);"></a>');
      var title2 = $('<span class="title"></span>');
      
      a2.click(function() {
        top.dispParts(contextPath + "/core/funcs/email/inbox/read_email/index.jsp?seqId=" + e.emailBodyId + "&mailId=" + e.emailId, 1)
      });
      
      title2.append(e.subject);
      a2.append(title2);
      
      return $('<div></div>').append(a).append("&nbsp;&nbsp;&nbsp;").append(a2);
    }
  }],
  "xtype": "panel",
  height: "auto",
  "cmpCls": "jq-window",
  "title": "内部邮件"
}
