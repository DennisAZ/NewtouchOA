{
  //cls: "window-height",
  tbar: [{
    id: 'close'
  }, {
    id: 'more',
    preventDefault: true,
    handler: function(e, t, p) {
      openUrl({
        text: "新闻",
        url: contextPath + "/core/funcs/news/show/index.jsp"
      });
    }
  }],
  getIts:function(t) {
    var url = contextPath + '/yh/core/funcs/news/act/YHNewsHandleAct/getnewsType.act';
    t.items = [];
    var text = jQuery.ajax({
    type: "GET",
    dataType: "text",
    url: url,
    async : false
    }).responseText;
    var json = YH.parseJson(text);
      if(json.rtState == "0"){        
        var rtData = json.rtData;
        var typeData = rtData.typeData;
        t.items[0] = t.getIt("","");
        if(typeData.length > 0){
          for(var i = 0 ;i < typeData.length ;i ++){
            var optionStr = typeData[i];
            t.items[i+1] = t.getIt(optionStr.typeId,optionStr.typeDesc);
          }
        }
    　　} else {
      t.items[0] = t.getIt("","");
    　　}
  },
  getIt:function(sort,desc){
    var obj = {
        xtype: "grid",
        tabBtn: {
          btnText: '全部',
          xtype: 'button',
          normalCls: 'tab-normal',
          activeCls: 'tab-active'
        },
        loader: {
          url: contextPath + "/yh/core/funcs/news/act/YHNewsShowAct/getDeskNewsAllList.act?type=" + sort
        },
        rowRender: function(i, e) {
          function deskNotify(seqId){
            var img = document.getElementById("img_"+seqId+"");
            if(img){
              document.getElementById("img_"+seqId+"").style.display="none";
            }
            var URL = contextPath + "/core/funcs/news/show/readNews.jsp?seqId="+seqId;
            myleft=(screen.availWidth-500)/2;
            window.open(URL,"deskNews","height=600,width=600,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=100,left="+myleft+",resizable=yes");
          }
          var a = $("<a href='javascript:void(0)'></a>");
           a.click(function() {
            deskNotify(e.seqId)
          });
           
          var title = $('<span class="title"></span>').html(e.subject);
          var desc = $('<span class="desc"></span>').html("(评论: " + e.commentCount + ")");
          var time = $('<span class="time"></span>').html(e.newsTIme);
          var icon;
          if (e.iread == "no") {
            icon = $('<img class="icon"></img>');
            icon.attr("src", imgPath + "/email_new.gif");
          }
          return a.append(icon).append(title).append(desc).append(time);
        }
      };
    if (desc) {
      obj.tabBtn.btnText = desc;
    }
    return obj;
  },
  width: "500px",
  layout: "cardlayout",
  layoutCfg: {
    tabs: true,
    fit: false
  },
  //items:this.getIts(),
  "xtype": "panel",
  height: "auto",
  "cmpCls": "jq-window",
  "title": "新闻",
  listeners: {
    initComponent: {
      before: function(e, t) {
         t.getIts(t);
        // alert(t.items);
      }
    }
  }
}