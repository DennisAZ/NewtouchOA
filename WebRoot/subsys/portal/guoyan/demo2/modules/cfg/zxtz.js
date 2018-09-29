{
  style: {
    'text-align': 'left',
    'padding':'0px 15px 0px 5px'
  },
  cls:"po-con",
  appendTo: $('#zxtz'),
  tbar:[{
    'id':'more',
    'handler': function(e, target, panel) {
      location = "modules/notify/popular-notify.jsp?typeId=1";
    }
  }],
  items:[
       {
         xtype: 'grid',
         title: '列表',
         url: contextPath + '/yh/subsys/portal/guoyan/module/act/YHPortalGridModuleNotify/loadGridData.act',
         params: {
           orderBy: 'SEND_TIME',
           sort: 'desc',
           type: '1',
           limit: 5,
           start: 0,
           paramName: 'newsId,subject,newsTime'
        },
         rowRender: function(i, e) {
          var newsTime = e.newsTime.substring(0,10);
          var hrefUrl = "modules/notify/article.jsp?newsId=" + e.newsId;
          return ['<a href="', hrefUrl, '">', e.subject, '</a>'].join('');
         }
       }
  ],
  width: '98%',
  height: '120px',
  title: ''
}