{
  tbar: [{
    id: 'close'
  }, {
    id: 'more',
    preventDefault: true,
    handler: function(e, t, p) {
      openUrl({
        text: "日程安排",
        url: contextPath + "/core/funcs/calendar/index.jsp"
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
      url: (function() {
        var currDate = new Date();
        var currDay = currDate.getDate();  
        var currMonth = currDate.getMonth();
        var currMonth = currMonth+1;
        var currYear = currDate.getFullYear(); 
        if (currMonth < 10) {
          currMonth = "0" + currMonth;
        }
        if (currDay < 10) {
          currDay = "0" + currDay;
        }
        var date = currYear + "-" + currMonth + "-" + currDay;
        return contextPath + "/yh/core/funcs/calendar/act/YHCalendarAct/selectCalendarToDisk.act?date=" + date + "&index=1";
      })()
    },
    rowRender: function(i, prc) {
      var currDate = new Date();
      var currDay = currDate.getDate();  
      var currMonth = currDate.getMonth();
      var currMonth = currMonth+1;
      var currYear = currDate.getFullYear(); 
      if(currMonth < 10){
        currMonth = "0" + currMonth;
      }
      if(currDay < 10){
        currDay = "0" + currDay;
      }
      var date = currYear + "-" + currMonth + "-" + currDay;
      var seqId = prc.seqId;
      var calTime = prc.calTime;
      var endTime = prc.endTime;
      var content = prc.content;
      var overStatus = prc.overStatus;
      var overStatusName = "<a href='javascript:updateStatus("+seqId+",2)'>完成</a> &nbsp;";
      var updateCalendarStr = "<a href='javascript:updateCalendar("+seqId+")'>修改</a> &nbsp;";
      var status = "";
      if (calTime.substr(0, 10) == endTime.substr(0, 10) && calTime.substr(0, 10) == date) {
        calTime = calTime.substr(11, 5);
        endTime = endTime.substr(11, 5);
      }
      function myNote(){
        var URL = contextPath + "/core/funcs/calendar/mynote.jsp?seqId=" + seqId + "&status=" + status;
        new YH.Window({
          layer: 'upper',
          width: 200,
          height: 200,
          src: URL,
          tbar: [{
            'id': 'close',
            preventDefault: true,
            handler: function(e, t, p) {
              p.destroy();
            }
          }]
        }).show();
      }
      
      var overStatusName = $('<a href="javascript: void(0)">完成</a>').click(function() {
        updateStatus(seqId, 1);
      });
      
      var self = this;
      function updateStatus(seqId, status){
        var URL =  contextPath + "/yh/core/funcs/calendar/act/YHCalendarAct/updateStatusById.act?seqId="+seqId+"&status="+status;
        var rtJson = getJsonRs(URL);
        if(rtJson.rtState == "1"){
          alert(rtJson.rtMsrg); 
          return ;
        }
        window.location.reload();
        $.post(URL, function(r) {
          if (r.rtState == "0") {
            this.reload();
          }
          else {
            alert(r.rtMsrg);
          }
        })
      }
      
      var overStatusName;
      var status;
      if (overStatus != 1) {
        overStatusName = $('<a href="javascript: void(0)">完成</a>').click(function() {
          updateStatus(seqId, 1);
        });
      }
      else {
        status = $('<span style="color:#00AA00">已完成</span>');
        overStatusName = $('<a href="javascript: void(0)">未完成</a>').click(function() {
          updateStatus(seqId, 0);
        });
      }
      var self = this;
      function updateStatus(seqId, status){
        var URL = contextPath + "/yh/core/funcs/calendar/act/YHCalendarAct/updateStatusById.act?seqId="+seqId+"&status="+status;
        $.post(URL, function(r) {
          r = YH.parseJson(r);
          if (r.rtState == "0") {
            self.reload();
          }
          else {
            alert(r.rtMsrg);
          }
        })
      }
      
      var updateCalendarStr = $('<a href="javascript:void(0)">修改</a>').click(function() {
        top.openUrl({
          url: contextPath + "/core/funcs/calendar/editcalendardisk.jsp?seqId=" + seqId,
          text: "编辑事务"
        });
      });
      
      var title = $('<span class="title"></span>');
      overStatusName.css({
        display: "none"
      });
      updateCalendarStr.css({
        display: "none"
      });
      title.hover(function() {
        updateCalendarStr.show();
        overStatusName.show();
      }, function() {
        updateCalendarStr.hide();
        overStatusName.hide();
      });
      
      title.append(calTime + "-" + endTime + "&nbsp;");
      var c = $('<a href="javascript: void(0)"></a>').append(content).click(myNote);
      title.append(c);
      title.append('&nbsp;');
      title.append(status);
      title.append('&nbsp;');
      title.append(overStatusName);
      title.append('&nbsp;');
      title.append(updateCalendarStr);
      return title;
    }
  }, {
    xtype: "grid",
    title: '近日',
    tabBtn: {
      btnText: '近日',
      xtype: 'button',
      normalCls: 'tab-normal',
      activeCls: 'tab-active'
    },
    loader: {
      url: (function() {
        var currDate = new Date();
        var currDay = currDate.getDate();  
        var currMonth = currDate.getMonth();
        var currMonth = currMonth+1;
        var currYear = currDate.getFullYear(); 
        if (currMonth < 10) {
          currMonth = "0" + currMonth;
        }
        if (currDay < 10) {
          currDay = "0" + currDay;
        }
        var date = currYear + "-" + currMonth + "-" + currDay;
        return contextPath + "/yh/core/funcs/calendar/act/YHCalendarAct/selectCalendarToDisk.act?date=" + date + "&index=2";
      })()
    },
    rowRender: function(i, prc) {
      var currDate = new Date();
      var currDay = currDate.getDate();  
      var currMonth = currDate.getMonth();
      var currMonth = currMonth+1;
      var currYear = currDate.getFullYear(); 
      if(currMonth < 10){
        currMonth = "0" + currMonth;
      }
      if(currDay < 10){
        currDay = "0" + currDay;
      }
      var date = currYear + "-" + currMonth + "-" + currDay;
      var seqId = prc.seqId;
      var calTime = prc.calTime;
      var endTime = prc.endTime;
      var content = prc.content;
      var overStatus = prc.overStatus;
      var overStatusName = "<a href='javascript:updateStatus("+seqId+",2)'>完成</a> &nbsp;";
      var updateCalendarStr = "<a href='javascript:updateCalendar("+seqId+")'>修改</a> &nbsp;";
      var status = "";
      if (calTime.substr(0, 10) == endTime.substr(0, 10) && calTime.substr(0, 10) == date) {
        calTime = calTime.substr(11, 5);
        endTime = endTime.substr(11, 5);
      }
      function myNote(){
        var URL = contextPath + "/core/funcs/calendar/mynote.jsp?seqId=" + seqId + "&status=" + status;
        new YH.Window({
          layer: 'upper',
          width: 200,
          height: 200,
          src: URL,
          tbar: [{
            'id': 'close',
            preventDefault: true,
            handler: function(e, t, p) {
              p.destroy();
            }
          }]
        }).show();
      }
      
      var overStatusName = $('<a href="javascript: void(0)">完成</a>').click(function() {
        updateStatus(seqId, 1);
      });
      
      var self = this;
      function updateStatus(seqId, status){
        var URL = "<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/updateStatusById.act?seqId="+seqId+"&status="+status;
        var rtJson = getJsonRs(URL);
        if(rtJson.rtState == "1"){
          alert(rtJson.rtMsrg); 
          return ;
        }
        window.location.reload();
        $.post(URL, function(r) {
          if (r.rtState == "0") {
            this.reload();
          }
          else {
            alert(r.rtMsrg);
          }
        })
      }
      
      var overStatusName;
      var status;
      if (overStatus != 1) {
        overStatusName = $('<a href="javascript: void(0)">完成</a>').click(function() {
          updateStatus(seqId, 1);
        });
      }
      else {
        status = $('<span style="color:#00AA00">已完成</span>');
        overStatusName = $('<a href="javascript: void(0)">未完成</a>').click(function() {
          updateStatus(seqId, 0);
        });
      }
      var self = this;
      function updateStatus(seqId, status){
        var URL = contextPath + "/yh/core/funcs/calendar/act/YHCalendarAct/updateStatusById.act?seqId="+seqId+"&status="+status;
        $.post(URL, function(r) {
          r = YH.parseJson(r);
          if (r.rtState == "0") {
            self.reload();
          }
          else {
            alert(r.rtMsrg);
          }
        })
      }
      
      var updateCalendarStr = $('<a href="javascript:void(0)">修改</a>').click(function() {
        top.dispParts(contextPath + "/core/funcs/calendar/editcalendardisk.jsp?seqId=" + seqId, 1);
      });
      
      var title = $('<span class="title"></span>');
      overStatusName.css({
        display: "none"
      });
      updateCalendarStr.css({
        display: "none"
      });
      title.hover(function() {
        updateCalendarStr.show();
        overStatusName.show();
      }, function() {
        updateCalendarStr.hide();
        overStatusName.hide();
      });
      
      title.append(calTime + "-" + endTime + "&nbsp;");
      var c = $('<a href="javascript: void(0)"></a>').append(content).click(myNote);
      title.append(c);
      title.append('&nbsp;');
      title.append(status);
      title.append('&nbsp;');
      title.append(overStatusName);
      title.append('&nbsp;');
      title.append(updateCalendarStr);
      return title;
    }
  }],
  "xtype": "panel",
  height: "auto",
  "cmpCls": "jq-window",
  "title": "日程安排"
}
