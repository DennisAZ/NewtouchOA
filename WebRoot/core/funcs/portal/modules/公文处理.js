{
  tbar: [{
    id: 'close'
  }, {
    id: 'more',
    preventDefault: true,
    handler: function(e, t, p) {
      openUrl({
        text: "公文处理"
      //  url: contextPath + "/subsys/oa/active/index.jsp"
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
    title: '待接收公文',
    tabBtn: {
      btnText: '待接收公文',
      xtype: 'button',
      normalCls: 'tab-normal',
      activeCls: 'tab-active'
    },
    loader: {
      url: contextPath + "/yh/subsys/jtgwjh/docReceive/act/YHJhDocrecvInfoAct/select.act?status=0"
    },
    rowRender: function(i, e) {
      var seqId = e.seqId;
      //var activeTime = e.activeTime;
      var activeContent = e.docTitle;
      var sendDept = e.sendDeptName;
      var time = $('<span class="title"></span>');
     // time.append(activeTime.substr(11, 5));
      
      var a = $('<a href="javascript:window.location.href=window.location.href"></a>');
      
      var title = $('<span class="title"></span>');
      //title.append(activeContent);
      var overStatusName = $('<a href="javascript: void(0)">&nbsp;&nbsp;签收</a>').click(function() {
        receive(seqId); //updateStatus(seqId, 1);
      });
      
        
      a.click(function() {
        var URL = contextPath  + "/subsys/jtgwjh/receiveDoc/manage/details.jsp?seqId=" + seqId;
        window.open(URL, "my_note", "height=400,width=600,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=600,top=300,resizable=yes");
      });
      overStatusName.css({
        display: "none"
      });
      title.hover(function() {
        overStatusName.show();
      }, function() {
        overStatusName.hide();
      });
      a.append(activeContent).append(" (" + sendDept + ")"  );
      
      return title.append(a).append(overStatusName);
      

      function receive(seqId){
        var msg = "确定要签收所选的记录吗？";
        if(window.confirm(msg)) {
          var requestURL = contextPath + "/yh/subsys/jtgwjh/docReceive/act/YHJhDocrecvInfoAct/updateStatus.act?seqId=" + seqId + "&status=1" ; 
        //  var json=getJsonRs(requestURL); 
         // if (json.rtState == '1') { 
          //  alert(json.rtMsrg); 
          //  return ; 
         // }
         // alert("签收成功！");
          window.location.reload();
          
          $.post(requestURL, function(r) {
            if (r.rtState == "0") {
              this.reload();
            }
            else {
              alert(r.rtMsrg);
            }
          })
        }
        
        
      }

    }
  }
  /*, {
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
  }*/],
  "xtype": "panel",
  height: "auto",
  "cmpCls": "jq-window",
  "title": "公文处理"
}

