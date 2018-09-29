var cfg = {
    style: {},
    items:[],
    tbar: [],
    cmpCls: 'jq-window',
    width: '650px',
    height: 'auto',
    title: ''
  };
var requestUrl = contextPath + "/yh/core/funcs/portal/act/YHPortalAct";
var loaderUrl = contextPath + '/yh/core/funcs/portal/act/YHPublicPortAct/setPublicPort.act';
var panel = null;
var loader;
/**
 * 更新预览
 * @param isReloadData 是否重新加载数据
 * @return
 */
function refresh(isReloadData) {
  //解析modCfg
  covertToCfg();
  cfg.renderTo = '#preview';
  
  if (!panel) {
    panel = new YH.Panel(cfg);
    parent.$("preview").show();
  }
  else {
    $.extend(panel, cfg);
    if (isReloadData) {
      panel.reload();
    } else {
      panel.refresh();
    }
  }
  
}
function covertToCfg() {
  var modData = parent.modData;
  cfg.title = modData.contentStyle.title;
  cfg.cls = modData.borderStyle.type || '';
  cfg.autoHideBorders = modData.borderStyle.toolButAutoHide;
  cfg.autoHideTools =  modData.borderStyle.borderAutoHide;
  var loaderParam = parent.publishData;
  
  if (!loaderParam.publicPath) {
    if (modData.dataDef.type == "url") {
      if (!panel) {
        cfg.iframeSrc = modData.dataDef.url;
      } else {
        panel.iframeSrc = modData.dataDef.url;
        //alert(panel.iframeSrc);
        panel.reload();
      }
      return ;
    } else {
      if (!panel) {
        cfg.iframeSrc = '';
      } else {
        panel.iframeSrc = '';
      }
    }
  }
  if (!loaderParam.publicPath) {
    loaderParam.sql = modData.dataDef.sql;
    var ruleObjList = covertArray(parent.ruleObjList);
    var ruleStr =  YH.jsonToString(ruleObjList);
    loaderParam.ruleList = ruleStr;
    loaderParam.type = "5";
  } 
  loader = new YH.Loader({
    url: loaderUrl,
    param: loaderParam,
    dataRender: function(data) {
      if (data.rtState == '0') {
        return data.rtData.records;
      }
    }
  });
  var contentType = modData.contentStyle.type;
  var box = null;
  if (contentType == "grid") {
    box = new YH.Grid({
      loader: loader
    });
  } else if (contentType == "img") {
//    box  = new YHImage({
//      loader: loader
//    });
     box  = new YH.ImgBox({
      loader: loader
    });
  } else {
    box  = new YH.ImgBox({
      loader: loader
    });
  }
  cfg.items = [box];
}
function covertArray(ruleList) {
  var newRuleList = [];
  for (var i = 0 ;i < ruleList.length ;i++) {
    var ru = ruleList[i];
    newRuleList.push(ru);
  }
  return newRuleList;
}
function save(pId) {
  covertToCfg();
  panel.refresh();
  var modData = parent.modData;
  var ruleObjList = covertArray(parent.ruleObjList);
  var dataDef = parent.modData.dataDef ;
  if (dataDef.type == "sql") {
    panel.items[0] && (panel.items[0].loader.param.sqlKey = cfg.title);
  }
  var script = getCfg();
  var ruleStr =  YH.jsonToString(ruleObjList);
  var sql = "";
  var type = "sql";
  if ( modData.dataDef.type == "sql") {
    sql = modData.dataDef.sql;
  } else {
    type = "url"
  }
  var deptId = modData.priv.dept;
  var userId = modData.priv.user;
  var roleId = modData.priv.priv;
  //修改时有用  var oldName = parent.$("oldName").value;
  var url = requestUrl + "/newPort.act";
  if (pId) {
     url = requestUrl + "/updatePort.act?id=" + pId;
  }
  $.ajax({
    url: url,
    type: "POST",
    data: {
      'script': script,
      'title': cfg.title,
      'ruleList': ruleStr,
      'sql' : sql,
      'type' : type ,
      'deptId':deptId,
      'userId':userId,
      'roleId':roleId,
      'oldName':oldName
    },
    success: function(text) {
      var json = YH.parseJson(text);
      if (json.rtState == '0') {
        alert('成功保存配置');
        parent.location.href = "portletlist.jsp";
      }
      else {
      }
    }
  });
}
function getCfg() {
  return YH.jsonToString(YH.formatJson({
    items: [],
    xtype: null,
    loader: {
      url: null,
      param: {
        sql: 'not',
        ruleList: 'not',
        sqlKey: null,
        publicPath :null,
        picName:null,
        type:null
      }, 
      dataRender: null
    },
    autoHideBorders: null,
    autoHideTools: null,
    cls: null,
    cmpCls: null,
    title: null,
    iframeSrc:null
  }, panel));
}
 

