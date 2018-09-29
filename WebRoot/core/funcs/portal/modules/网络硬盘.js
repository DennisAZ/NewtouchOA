{
  cls: "window-height",
  tbar: [{
    id: 'close'
  }, {
    id: 'more',
    preventDefault: true,
    handler: function(e, t, p) {
      openUrl({
        text: "网络硬盘",
        url: contextPath + "/core/funcs/netdisk/index.jsp"
      });
    }
  }],
  width: "500px",
  "items":[{
    xtype: "grid",
    loader: {
      url: contextPath + "/yh/core/funcs/netdisk/act/YHNetDiskAct/getTreebyFileSystem.act?DISK_ID="
    },
    rowRender: function(i, e) {
      var diskId = e.nodeId;
      var extData = e.extData;
      var a = $("<a href='javascript:void(0)'></a>");
      a.click(function() {
        top.dispParts(contextPath + "/core/funcs/netdisk/index.jsp?seqId=" + extData + "&filePath=" +diskId, 1);
      });
       
      var title = $('<span class="title"></span>').html(e.name);
      var icon;
      
      return a.append(icon).append(title);
    }
  }],
  "xtype": "panel",
  height: "auto",
  "cmpCls": "jq-window",
  "title": "网络硬盘"
}
