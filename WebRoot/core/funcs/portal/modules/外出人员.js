{
  cls: "window-height",
  width: "500px",
  "items":[{
    xtype: "grid",
    loader: {
      url: contextPath + "/yh/core/funcs/attendance/personal/act/YHAttendOutAct/selectOutToDisk.act"
    },
    rowRender: function(i, e) {
      var seqId = e.seqId;
      var outTime1 = e.outTime1;
      var outTime2 = e.outTime2;
      var reason = e.reason;
      var userId = e.userId;
      var outType = e.outType;
      var userName = e.userName;
      return "<u title='" + outTime1 + " 至 " + outTime2 + " 原因: " + outType + "' style='cursor:pointer'>" + userName + "</u>&nbsp;";
    }
  }],
  "xtype": "panel",
  height: "auto",
  "cmpCls": "jq-window",
  "title": "外出人员"
}