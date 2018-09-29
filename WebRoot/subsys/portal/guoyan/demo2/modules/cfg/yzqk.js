{
  style: {
    'text-align': 'left',
    'padding':'0px 0px 0px 0px'
  },
  cls:"yzqk",
  appendTo: $('#yzqk'),
  tbar:[{
    'id':'more',
    'handler': function(e, target, panel) {
      location = "modules/notify/popular-notify.jsp?typeId=2";
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
           type: '2',
           limit: 5,
           start: 0,
           paramName: 'newsId,subject,newsTime'
        },
        // data: [{runId:1454,prcsId:1,flowId:341,prcsFlag:'1',flowPrcs:'1',flowName:'北京市人民政府外事发文',runName:'北京市人民政府外事发文(2010-07-19 09:45:00)',flowType:'1',formId:381,prcsName:'拟稿'},{runId:1457,prcsId:1,flowId:341,prcsFlag:'2',flowPrcs:'1',flowName:'北京市人民政府外事发文',runName:'北京市人民政府外事发文(2010-07-19 09:45:55)',flowType:'1',formId:381,prcsName:'拟稿'},{runId:1468,prcsId:1,flowId:81,prcsFlag:'2',flowPrcs:'1',flowName:'自由',runName:'自由(2010-07-20 17:23:13)',flowType:'2',formId:61,prcsName:''},{runId:1466,prcsId:1,flowId:402,prcsFlag:'2',flowPrcs:'1',flowName:'自由流程测试',runName:'自由流程测试(2010-07-20 16:27:40)',flowType:'2',formId:121,prcsName:''},{runId:1459,prcsId:1,flowId:402,prcsFlag:'2',flowPrcs:'1',flowName:'自由流程测试',runName:'自由流程测试(2010-07-20 15:21:11)',flowType:'2',formId:121,prcsName:''},{runId:1453,prcsId:1,flowId:381,prcsFlag:'2',flowPrcs:'1',flowName:'高级查询测试',runName:'高级查询测试(2010-07-15 17:18:25)',flowType:'1',formId:401,prcsName:''}],
         rowRender: function(i, e) {
           var newsTime = e.newsTime.substring(0,10);
           var hrefUrl = "modules/notify/article.jsp?newsId=" + e.newsId;
           return ['<a href="', hrefUrl, '">', e.subject, '</a>', '&nbsp;',  '&nbsp;','<span>',newsTime,'</span>'].join('');
         }
       }
  ],
  width: '100%',
  height: '125',
  title: '一周情况'
}