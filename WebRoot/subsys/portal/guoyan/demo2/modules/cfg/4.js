{
  style: {
  },
  tbar: [{
    'id' : 'more',
    handler: function(e, target, panel) {
      panel.close();
    }
  }],
  moreLink: 'some',
  //layout: 'floatLeft',
  items:[
    {
      xtype: 'grid',
      title: '列表',
      url: 'http://localhost/yh/yh/core/funcs/workflow/act/YHMyWorkAct/getMyWork.act',
      params: {
        orderBy: 'RUNID',
        sort: 'desc',
        tableName: '',
        limit: 0,
        start: 0,
        feildName: 'RUNID,PRCSID,FLOWID',
        paramName: 'runId,prcsId,flowId'
      },
      //data: [{runId:1454,prcsId:1,flowId:341,prcsFlag:'1',flowPrcs:'1',flowName:'北京市人民政府外事发文',runName:'北京市人民政府外事发文(2010-07-19 09:45:00)',flowType:'1',formId:381,prcsName:'拟稿'},{runId:1457,prcsId:1,flowId:341,prcsFlag:'2',flowPrcs:'1',flowName:'北京市人民政府外事发文',runName:'北京市人民政府外事发文(2010-07-19 09:45:55)',flowType:'1',formId:381,prcsName:'拟稿'},{runId:1468,prcsId:1,flowId:81,prcsFlag:'2',flowPrcs:'1',flowName:'自由',runName:'自由(2010-07-20 17:23:13)',flowType:'2',formId:61,prcsName:''},{runId:1466,prcsId:1,flowId:402,prcsFlag:'2',flowPrcs:'1',flowName:'自由流程测试',runName:'自由流程测试(2010-07-20 16:27:40)',flowType:'2',formId:121,prcsName:''},{runId:1459,prcsId:1,flowId:402,prcsFlag:'2',flowPrcs:'1',flowName:'自由流程测试',runName:'自由流程测试(2010-07-20 15:21:11)',flowType:'2',formId:121,prcsName:''},{runId:1453,prcsId:1,flowId:381,prcsFlag:'2',flowPrcs:'1',flowName:'高级查询测试',runName:'高级查询测试(2010-07-15 17:18:25)',flowType:'1',formId:401,prcsName:''}],
      rowRender: function(i, e) {
        return ['<a href="', e.runId, '"><font color="#727272">', e.flowName, '</font>[&nbsp;', e.runName, '&nbsp;]', '</a>', new Date()].join('');
      }
    }
  ],
  height: 'auto',
  title: '列表'
}