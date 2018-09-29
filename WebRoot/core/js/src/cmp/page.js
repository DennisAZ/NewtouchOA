/**
 * 翻页
 * @author          YZQ
 * @date            2010-03-10
 */
function deleteAttachBackHand(pageMgr,rowIndex){
  alert('删除附件事后工作！');
}
function set_status() {
  alert("set_status");
}
/**
 * 播放附件
 * @param dom
 * @param cntrlId
 * @param extData
 * @return
 */
function playVideoAttach(dom,cntrlId,extData){
  var params = cntrlId.split("_");
  var rowIndex = params[1];
  var colIndex = params[2];
  var attrchIndex = params[3];
  var attachId = pageMgr.getCellData(rowIndex,"attachId").split(",")[attrchIndex];
  var attachName = $(cntrlId).innerHTML;
  var moudle = pageMgr.moduleName;
  playVideo(attachName,attachId,moudle);
}
/**
 * 
 * @param attachName
 * @param attachId
 * @param moudle
 * @return
 */
function playVideo(attachName,attachId,moudle){
  var mediaType = isMedia(attachName);
  var video = isVideo(attachName);
  var param = "attachmentName=" + encodeURIComponent(attachName) + "&attachmentId=" + attachId + "&moudle=" + moudle + "&mediaType=" + mediaType + "&video=" + video;
  var url = contextPath + "/core/module/mediaplayer/index.jsp?" + param;
  //url = encodeURI(url);
  openWindow(url,'在线播放器',900,600);
}
/**
 * 下载附件
 * @param dom
 * @param cntrlId
 * @param extData
 * @return
 */
function downLoadAttach(dom,cntrlId,extData){
  var params = cntrlId.split("_");
  var rowIndex = params[1];
  var colIndex = params[2];
  var attrchIndex = params[3];
  var attachId = pageMgr.getCellData(rowIndex,"attachId").split(",")[attrchIndex];
  var attachName = $(cntrlId).innerText;
  var moudle = pageMgr.moduleName;
  downLoadFile(attachName,attachId,moudle);
}
/**
 * 转存附件
 * @param dom
 * @param cntrlId
 * @param extData
 * @return
 */
function archivedAttach(dom,cntrlId,extData){
  var params = cntrlId.split("_");
  var rowIndex = params[1];
  var colIndex = params[2];
  var attrchIndex = params[3];
  var attachId = pageMgr.getCellData(rowIndex,"attachId").split(",")[attrchIndex];
  var attachName = $(cntrlId).innerHTML;
  var moudle = pageMgr.moduleName;
  archived(attachName,attachId,moudle);
}
/**
 * 
 * @param attachName
 * @param attachId
 * @param moudle
 * @return
 */
function archived(attachName,attachId,moudle){
  var URL = contextPath + "/core/funcs/savefile/index.jsp?attachId=" + attachId + "&attachName=" + encodeURIComponent(attachName) +"&module=" + moudle;
  var loc_x = screen.availWidth/2-200;
  var loc_y = screen.availHeight/2-90;
  window.open(URL,null,"height=180,width=400,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top="+loc_y+",left="+loc_x+",resizable=yes");
}
/**
 * 删除附件
 * @param dom
 * @param cntrlId
 * @param extData
 * @return
 */
function deleteAttach(dom,cntrlId,extData){
  var params = cntrlId.split("_");
  var rowIndex = params[1];
  var colIndex = params[2];
  var attrchIndex = params[3];
  var attachId = pageMgr.getCellData(rowIndex,"attachId").split(",")[attrchIndex];
  var attachName = $(cntrlId).innerHTML;
  var moudle = pageMgr.moduleName;
  deleteFile(attachName,attachId,moudle);
  deleteAttachBackHand(pageMgr,rowIndex);
}
/**
 * 只读offic文档
 * @param dom
 * @param cntrlId
 * @param extData
 * @return
 */
function readOffice(dom,cntrlId,extData){
  var params = cntrlId.split("_");
  var rowIndex = params[1];
  var colIndex = params[2];
  var attrchIndex = params[3];
  var attachId = pageMgr.getCellData(rowIndex,"attachId").split(",")[attrchIndex];
  var attrchName = $(cntrlId).innerHTML;
  var moudle = pageMgr.moduleName;
  office(attrchName,attachId,moudle,7,'','');
}
/**
 * 编辑offic文档
 * @param dom
 * @param cntrlId
 * @param extData
 * @return
 */
function editOffice(dom,cntrlId,extData){
  var params = cntrlId.split("_");
  var rowIndex = params[1];
  var colIndex = params[2];
  var attrchIndex = params[3];
  var attachId = pageMgr.getCellData(rowIndex,"attachId").split(",")[attrchIndex];
  var attrchName = $(cntrlId).innerHTML;
  var moudle = pageMgr.moduleName;
  office(attrchName,attachId,moudle,4,'','');
}
/**
 * 删除（附件）文件
 * @param attachName
 * @param attachId
 * @param moudle
 * @return
 */
function deleteFile(attachName,attachId,moudle){
  var param = "attachmentName=" + encodeURIComponent(attachName) + "&attachmentId=" + attachId + "&moudle=" + moudle;
  var url = contextPath + "/yh/core/funcs/office/ntko/act/YHNtkoAct/deleteFile.act?" + param;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    alert("附件删除成功！");
  }else{
    alert(rtJson.rtMsrg);
  }
}
/**
 * 下载附件
 * @param attachName
 * @param attachId
 * @param moudle
 * @return
 */
function downLoadFile(attachName,attachId,moudle){
  var param = "attachmentName=" + encodeURIComponent(attachName) + "&attachmentId=" + attachId + "&module=" + moudle;
  var url = contextPath + "/yh/core/funcs/office/ntko/act/YHNtkoAct/downFile.act?" + param;
  window.open(url);
}
/**
 * NTKOword操作
 * @param attachmentName 附件名称
 * @param attachmentId  附件ID
 * @param moudle 附件模块名称
 * @param op  操作类型
 * @param signKey 验证
 * @param print 打印类型
 * @return
 */
function office(attachmentName,attachmentId,moudle,op,signKey,print){
  var param = "attachmentName=" + encodeURIComponent(attachmentName) 
     + "&attachmentId=" + attachmentId 
     + "&moudle=" + moudle 
     + "&op=" + op 
     + "&signKey=" + signKey 
     + "&print=" + print ; 
  var url = contextPath + "/core/funcs/office/ntko/indexNtko.jsp?" + param;
  //url = encodeURI(url);
  openWindow(url,'在线编辑',900,600);
}
function isMedia(fileName){
  var MEDIA_REAL_TYPE = ["rm", "rmvb","ram","ra", "mpa", "mpv", "mps","m2v", "m1v", "mpe", "mov", "smi"]
  var MEDIA_MS_TYPE = ["wmv", "asf", "mp3", "mpg", "mpeg", "mp4", "avi", "wmv", "wma", "wav", "dat"];
  var MEDIA_FLASH_TYPE = ["flv", "fla"];
  var DIRECT_VIEW_TYPE = ["jpg", "jpeg", "bmp", "gif", "png", "xml", "xhtml", "html", "htm", "mid", "mht", "pdf", "swf"];
  var index = fileName.lastIndexOf(".");
  var extName = "";

  if (index >= 0) {
    extName = fileName.substring(index + 1).toLowerCase();
  }
  if(MEDIA_REAL_TYPE.contains(extName))
     return 1;
  if(MEDIA_MS_TYPE.contains(extName))
     return 2;
  if(MEDIA_FLASH_TYPE.contains(extName))
     return 4;
  if(DIRECT_VIEW_TYPE.contains(extName))
     return 3;
  return 0;
}

function isVideo(fileName){
 var videoType = ["mp4", "mpg","mpeg","avi", "wmv", "asf", "dat"];
 var index = fileName.lastIndexOf(".");
 var extName = "";
 if (index >= 0) {
   extName = fileName.substring(index + 1).toLowerCase();
 }
 if(videoType.contains(extName)){
   return 1;
 }else{
  return 0;
 }
}
/**
 * 分页类

 */
var YHJsPage = Class.create();
YHJsPage.prototype = {
  /**
   * @dataAction         必添、数据取得服务路径
   * @colums             必添、栏目定义[
   * {type               必添、类别 (check|opts|data|attach|hidden|selfdef)
   * , name              必添、名称，用于取数
   * , text              必添、显示文本
   * , width             必添、宽度
   * , dataType          数据类型(int|doube|float|date|dateTime|time)
   * , align             数据水平对齐方式
   * , title             提示信息
   * , render            自定义描画器
   * , bindAction        自定义事件绑定
   * , format            格式，dataType=(int,doube,float,date,dateTime,time)时有效
   * , opts              type=opts时有效、按钮数组[{type, text, clickFunc, overFunc, floatMenu:{menuItems},...]
   * , floatMenu         type=opts时有效、浮动菜单配置
   * , iconFunc          取得图标路径的扩展函数

   * , selfStyle         栏目样式
   * , selfTitleStyle    栏目标题样式
   * , sortDef           排序类型{type, direct} type=(1=整型;2=浮点型;其他值=字符串)，不设置，视为不排序
   * , styleFunc         字体颜色函数
   * , bindTitleFunc     绑定列表标题响应事件
   * }, ...]
   * @container          分页的容器
   * @paramFunc          取得其他参数的函数
   * @sortColum          排序的栏目索引   
   * @rowFuncs           记录相关函数扩展{dblclickFunc, clickFunc}
   * @sizeList           允许显示列表记录条数列表
   * @mouseSelect        是否需要使用鼠标选择，缺省不用
   * @isMulPage          是否分页，缺省是true
   * @moduleName         模块名称，用于文件上传传递参数

   * @sortIndex          排序栏目索引
   * @showRecordCnt      显示记录条数信息
   * @afterShow         数据显示完毕事件响应
   */
  initialize: function(cfgs) {
    this.config(cfgs);    
  },
  /**
   * 附件菜单定义
   */
  docMenus: {
    officeReadOnly: [{ name:'<div style="padding-top:5px;margin-left:10px">下载<div>',action:downLoadAttach,extData:'0'}
      ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">转存<div>',action:archivedAttach,extData:'1'}
      ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">阅读<div>',action:readOffice,extData:'2'}
    ],
    officeEditable: [{ name:'<div style="padding-top:5px;margin-left:10px">下载<div>',action:downLoadAttach,extData:'0'}
      ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">转存<div>',action:archivedAttach,extData:'1'}
      ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">阅读<div>',action:readOffice,extData:'2'}
      ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">编辑<div>',action:editOffice,extData:'3'}
      ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">设置标引<div>',action:set_status,extData:'4'}
      ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">删除<div>',action:deleteAttach,extData:'5'}
    ],
    img: [{ name:'<div style="padding-top:5px;margin-left:10px">下载<div>',action:downLoadAttach,extData:'2'}
      ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">播放<div>',action:playVideoAttach,extData:'3'}
      ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">转存<div>',action:archivedAttach,extData:'1'}],
    imgEditable: [{ name:'<div style="padding-top:5px;margin-left:10px">下载<div>',action:downLoadAttach,extData:'2'}
      ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">播放<div>',action:playVideoAttach,extData:'3'}
      ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">转存<div>',action:archivedAttach,extData:'1'}
      ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">删除<div>',action:deleteAttach,extData:'5'}
       ],
    audio: [{ name:'<div style="padding-top:5px;margin-left:10px">下载<div>',action:downLoadAttach,extData:'2'}
      ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">播放<div>',action:playVideoAttach,extData:'4'}
      ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">转存<div>',action:archivedAttach,extData:'1'}],
    audioEditable: [{ name:'<div style="padding-top:5px;margin-left:10px">下载<div>',action:downLoadAttach,extData:'2'}
      ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">播放<div>',action:playVideoAttach,extData:'4'}
      ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">转存<div>',action:archivedAttach,extData:'1'}
      ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">删除<div>',action:deleteAttach,extData:'5'}
      ],
    vedio: [{ name:'<div style="padding-top:5px;margin-left:10px">下载<div>',action:downLoadAttach,extData:'2'}
      ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">播放<div>',action:playVideoAttach,extData:'4'}
      ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">转存<div>',action:archivedAttach,extData:'1'}],  
    vedioEditable: [{ name:'<div style="padding-top:5px;margin-left:10px">下载<div>',action:downLoadAttach,extData:'2'}
      ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">播放<div>',action:playVideoAttach,extData:'4'}
      ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">转存<div>',action:archivedAttach,extData:'1'}
      ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">删除<div>',action:deleteAttach,extData:'5'}
      ],  
    others: [{ name:'<div style="padding-top:5px;margin-left:10px">下载<div>',action:downLoadAttach,extData:'5'}
      ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">转存<div>',action:archivedAttach,extData:'1'}
    ],
    othersEditable: [{ name:'<div style="padding-top:5px;margin-left:10px">下载<div>',action:downLoadAttach,extData:'5'}
    ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">转存<div>',action:archivedAttach,extData:'1'}
    ,{ name:'<div style="color:#0000FF;padding-top:5px;margin-left:10px">删除<div>',action:deleteAttach,extData:'5'}
    ]
  },
  /**
   * 分页信息
   * @totalRecord              所有记录条数
   * @totalPage                页面长度
   * @pageIndex                当前页面索引，从0开始
   * @startIndex               当前页面起始索引，从0开始
   * @endIndex                 当前页面截至索引，从0开始
   * @recordCnt                当前页面记录条数
   * @pageSize                 每页显示的记录条数
   */
  pageInfo: {totalRecord: null,
    totalPage: null,
    pageIndex: null,
    startIndex: null,
    endIndex: null,
    recordCnt: null,
    pageSize: null
  },
  /**
   * 数据缓存，按索引顺序进行存储
   * 数据格式{pageInfo:{}, pageData:{}}
   */
  dataCatch: new Array(),
  /**
   * 清除缓存
   */
  clearCatch: function(index) {
    if (index == 0 || index) {
      this.dataCatch[index] = null;
    }else {
      this.dataCatch.clear();
    }
  },
  /**
   * 类的配置
   */
  config : function(cfgs) {
    this.dataAction = cfgs.dataAction;
    this.container = cfgs.container ? $(cfgs.container) : $(document.body);
    //注意顺序需要在this.setColums之前
    this.sortIndex = cfgs.sortIndex ? cfgs.sortIndex : (cfgs.sortIndex == 0 ? 0 : null);
    this.sortDirect = cfgs.sortDirect ? cfgs.sortDirect : "asc";
    this.setColums(cfgs.colums);
    this.rowFuncs = cfgs.rowFuncs;
    this.sizeList = cfgs.sizeList ? cfgs.sizeList : [20, 10, 30, 40];
    this.pageInfo.pageSize = cfgs.pageSize ? cfgs.pageSize : 20;
    this.paramFunc = cfgs.paramFunc;
    this.mouseSelect = cfgs.mouseSelect === true ? true : false;
    this.isInit = true;
    this.isDisp = false;
    this.isMulPage = cfgs.isMulPage === false ? false : true;
    this.moduleName = cfgs.moduleName;
    this.selfStyle = cfgs.selfStyle;
    this.showRecordCnt = cfgs.showRecordCnt;
    this.afterShow = cfgs.afterShow ? cfgs.afterShow.bind(this) : null;
  },
  /**
   * 设置栏目定义
   */
  setColums : function(colums) {
    this.colums = colums;
    if (!this.colums) {
      return;
    }
    this.sortColums = [];
    var columCnt = this.colums.length;
    var nameStr = "";
    for (var i = 0; i < columCnt; i++) {
      var clm = this.colums[i];
      if (clm.type == "check") {
        this.useCheck = true;
      }else if (clm.type == "opts") {
        this.opts = clm.opts;
      //data | attach
      }else if (clm.type == "selfdef") {
        //do nothing
      //普通的数据栏
      }else {
        nameStr += "," + clm.name;
        //处理缺省排序栏目
        if (clm.sortDef) {
          this.sortColums.push(i);
          if (!this.sortIndex && this.sortIndex !== 0) {
            this.sortIndex = i;
            var direct = clm.sortDef.direct ? clm.sortDef.direct : "asc";
            this.sortDirect = direct;
          }
        }
      }
    }
    if ((this.sortIndex === 0 || this.sortIndex)) {
      if (!this.sortDirect) {
        this.sortDirect = "asc";
      }
    }
    if (nameStr.length > 1) {
      nameStr = nameStr.substring(1);
    }
    this.columNames = nameStr;
  },
  /**
   * 取得数据表格对象
   */
  getDataTableDom: function() {
    return $("dataTable");
  },
  /**
   * 加载数据
   * @pageIndex       页的索引号，从0开始计数

   */
  loadPageData: function(pageIndex) {
    if (pageIndex >= this.pageInfo.totalPage) {
      pageIndex = this.pageInfo.totalPage - 1;
    }
    if (pageIndex < 0) {
      pageIndex = 0;
    }
    //缓存中存在，使用缓存中的数据
    var dataInCatch = this.dataCatch[pageIndex];
    
    //默认的不使用列表的排序情况下使用缓存，当排序的时候重新加载数据
    if (!this.sortColumIndex && dataInCatch) {
      this.pageInfo.totalRecord = dataInCatch.totalRecord;
      this.setPageInfo(pageIndex);
      this.refreshNevBar();
      return dataInCatch;
    }
    
    //取得查询参数
    var paramMap = {};
    paramMap.dtoClass = "yh.core.data.YHPageQueryParam";
    paramMap.pageSize = this.pageInfo.pageSize;
    paramMap.pageIndex = pageIndex ? pageIndex : 0;
    paramMap.nameStr = this.columNames;
    var c = this.getColumDef(this.sortColumIndex || -1);
    paramMap.sortColumn = c && c.name;
    paramMap.direct = this.direct || "asc";
    
    var userParam = "";
    if (this.paramFunc) {
      userParam = this.paramFunc();
    }    
    var queryParam = mergeQueryString(paramMap, userParam);
    
    var rtJson = getJsonRs(this.dataAction, queryParam);
    this.dataCatch[pageIndex] = rtJson;
    this.pageInfo.totalRecord = rtJson.totalRecord;
    this.setPageInfo(pageIndex);
    this.refreshNevBar();
    return rtJson;
  },
  /**
   * 计算分页相关的数据
   */
  setPageInfo: function(pageIndex) {
    var pageJson = this.dataCatch[pageIndex];
    if (!this.dataCatch[pageIndex]) {
      return;
    }
    var pageCnt = parseInt((parseInt(this.pageInfo.totalRecord) / parseInt(this.pageInfo.pageSize)));
    if (this.pageInfo.totalRecord % this.pageInfo.pageSize != 0) {
      pageCnt++;
    }
    this.pageInfo.totalPage = pageCnt;
    this.pageInfo.pageIndex = pageIndex;
    this.pageInfo.startIndex = this.pageInfo.pageSize * pageIndex;
    this.pageInfo.endIndex = this.pageInfo.startIndex + pageJson.pageData.length - 1;
    this.pageInfo.recordCnt = pageJson.pageData.length;
  },
  getSortImg: function(direct) {
    if (direct && direct.toLowerCase() == "desc") {
      return imgPath + "/arrow_down.gif";
    }else {
      return imgPath + "/arrow_up.gif";
    }
  },
  /**
   * 构造表头

   */
  buildHeader: function() {
    if (!this.isInit) {
      return;
    }
    if (!this.colums) {
      return;
    }
    var trObj = $("dataHeader");
    if (!trObj) {
      return;
    }
    //清除原来的单元格
    var cellCnt = trObj.cells.length;
    if (cellCnt > 0) {
      return;
    }
    var columCnt = this.colums.size();
    var columIndex = 0;
    var tableWidth = 0;
    for (var i = 0; i < columCnt; i++) {
      var clm = this.colums[i];
      if (clm.type == "hidden") {
        continue;
      }
      var tdObj = trObj.insertCell(columIndex++);
      tdObj.id = "headCell_" + i;
      tdObj = $(tdObj);
      var tdTxt = "";
      if (clm.type == "check") {
        tdTxt = "<input id=\"btnCheckAll\" name=\"btnCheckAll\" title=\"选择所有\" type=\"checkbox\"/>";
      }else if (clm.type == "opts") {
        tdTxt = "操作";
      }else {
        if (clm.sortDef) {
          tdTxt = "<u id=\"sortLink_" + i + "\" style=\"cursor:pointer;\">" + clm.text + "</u>";
          if (this.sortIndex == i) {
            if (this.sortDirect) {
              clm.sortDef.direct = this.sortDirect;
            }
            var sortFlagImg = this.getSortImg(clm.sortDef.direct);
            tdTxt += "<img id=\"sortFlagImg\" src=\"" + sortFlagImg + "\"></img>";
          }
        }else {
          tdTxt = clm.text;
        }
      }
      tdObj.align = "center";
      tdObj.className = "TableHeader";
      if (clm.width) {
        if ((typeof clm.width == "string") && clm.width.indexOf("%") > 0) {
          tdObj.width = clm.width;
          tableWidth = "100%";
        }else {
          tdObj.style.width = clm.width + "px";
          tableWidth += clm.width;
        }
      }
      if (clm.bindTitleAction) {
        clm.bindTitleAction.call(this, i);
      }
      if (clm.selfTitleStyle) {
        tdObj.setStyle(clm.selfTitleStyle);
      }
      tdObj.innerHTML = tdTxt;      
    }
    if (tableWidth && typeof tableWidth == "string" && tableWidth.indexOf("%") > 0) {
      $("dataTable").width = tableWidth;
    }if (tableWidth > 0) {
      $("dataTable").style.width = tableWidth;
    }
  },
  /**
   * 设置数据列表的显示和隐藏
   */
  _setListShowState: function() {
    if (this.pageInfo.totalPage < 1) {
      $("pgTopPanel").style.display = "none";
      $("pgMsrgPanel").style.display = "";
    }else {
      $("pgTopPanel").style.display = "";
      $("pgMsrgPanel").style.display = "none";
    }
  },
  /**
   * 显示数据列表
   */
  _showTableList: function(pageData) {
    var recordCnt = pageData.length;
    if (!this.colums) {
      return;
    }
    
    var columCnt = this.colums.length;
    
    var tableObj = $("dataTable");
    if (!tableObj) {
      return;
    }
    var rowCnt = tableObj.rows.length;
    for (var i = rowCnt - 1; i >= 1; i--) {
      tableObj.deleteRow(i);
    }
    this._setListShowState();
    for (var i = 0; i < recordCnt; i++) {
      var record = pageData[i];
      var trObj = tableObj.insertRow(1 + i);
      trObj = $(trObj);
      if (trObj.rowIndex % 2 == 0) {
        trObj.className = "TableLine1";
      }else {
        trObj.className = "TableLine2";
      }
      trObj.id = "tr_" + i;
      if (this.mouseSelect) {
        trObj.observe("mouseover", setPointer.bind(window, trObj, 'over'));
        trObj.observe("mouseout", setPointer.bind(window, trObj, 'out'));
        trObj.observe("click", setPointer.bind(window, trObj, 'click'));
      }
      var columIndex = 0;
      for (var j = 0; j < columCnt; j++) {
        var clm = this.colums[j];
        
        //处理隐藏项目数据
        if (clm.type == "hidden") {
          continue;
        }

        var tdObj = trObj.insertCell(columIndex++);
        tdObj = $(tdObj);
        tdObj.style.wordWrap = "break-word";
        if (!clm.align) {
          if (clm.dataType == "double" || clm.dataType == "float" || clm.dataType == "int") {
            clm.align = "right";
          }
          if (clm.dataType == "date" || clm.dataType == "dateTime" || clm.dataType == "time") {
            clm.align = "center";
          }
        }
        
        var tdTxt = null;
        if (clm.selfdef) {
          if (clm.render) {
            tdObj.innerHTML = clm.render.call(this, record[clm.name], i, j);
          }else {
            tdObj.innerHTML = "&nbsp";
          }
        }else if (clm.render) {
          tdObj.innerHTML = clm.render.call(this, record[clm.name], i, j);          
        }else if (clm.dataType == "double" || clm.dataType == "float") {          
          tdObj.innerHTML = this.formatDouble(record[clm.name], clm.format);
        }else if (clm.dataType == "date") {          
          tdObj.innerHTML = this.formatDate(record[clm.name], clm.format);
        }else if (clm.dataType == "dateTime" && clm.format) {
          tdObj.innerHTML = this.formatDateTime(record[clm.name], clm.format);
        }else if (clm.dataType == "time" && clm.format) {
          tdObj.innerHTML = this.formatTime(record[clm.name], clm.format);
        }else if (clm.dataType == "attach") {
          tdObj.innerHTML = this.attachHtml(record[clm.name], i, j);
          this.bindAttachMenu(record[clm.name], i, j);
        }else {
          if (clm.type == "check") {
            tdTxt = "<input type=\"checkbox\" name=\"check_" + i + "\" id=\"check_" + i + "\">&nbsp;";
            tdObj.innerHTML = tdTxt;
          }else if (clm.type == "opts") {
            tdTxt = this.buildOptBtns(i);
            tdObj.innerHTML = tdTxt;
            this.bindBtnActions(i);
          }else {
            var iconHtml = "";
            if (clm.iconFunc) {
              iconHtml = "&nbsp;&nbsp;" + clm.iconFunc.call(this, record[clm.name], i, j);
            }
            tdTxt = "<span id=\"cellSpan_" + i + "_" + j + "\">" + record[clm.name] + "</span>" + iconHtml;
            tdObj.innerHTML = tdTxt;
            if (clm.floatMenu) {
              this.bindFloagMenu(i, j);
            }
          }          
        }
        if (clm.selfStyle) {
          tdObj.setStyle(clm.selfStyle);
        }
        if (clm.styleFunc) {
          tdObj.setStyle(clm.styleFunc.call(this, record[clm.name], i, j));
        }
        if (clm.align) {
          tdObj.align = clm.align;
        }
        if (clm.bindAction) {
          clm.bindAction.call(this, record[clm.name], i, j);
        }
      }
    }
    if (this.afterShow) {
      this.afterShow();
    }
  },
  /**
   * 显示某页数据
   */
  showPage: function(pageIndex) {
    var pageData = this.loadPageData(pageIndex);
    if (!pageData) {
      return;
    }
    pageData = pageData ? pageData.pageData : null;
    if (!pageData) {
      return;
    }
    this._sortPageData(this.sortIndex, this.sortDirect);
    this._showTableList(pageData);
  },
  /**
   * 执行列表排序
   */
  _sortPageData: function(sortIndex, sortDirect) {
    //当点击排序列时，不使用默认排序
    if (typeof this.sortColumIndex != undefined) {
      return;
    }
    if (this.sortColums.length < 1) {
      return;
    }
    var pageIndex = null;
    if (this.pageInfo.pageIndex >= 0) {
      pageIndex = this.pageInfo.pageIndex;
    }
    if (pageIndex !== 0 && !pageIndex) {
      return null;
    }
    //缓存中的数据
    var dataInCatch = this.dataCatch[pageIndex];
    var sortColum = this.getColumDef(sortIndex);
    if (!sortColum || !sortColum.sortDef) {
      return null;
    }
    var isDesc = false;
    if (sortDirect.toLowerCase() == "desc") {
      isDesc = true;
    }
    if (this.sortIndex == sortIndex && dataInCatch.pageData.__sortDirect && dataInCatch.pageData.__sortDirect == sortDirect) {
      return null;
    }
    this.sortIndex = sortIndex;
    this.sortDirect = sortDirect;
    sortColum.sortDef.direct = sortDirect;
    dataInCatch.pageData.sortObj(sortColum.name, isDesc, sortColum.sortDef.type);
    dataInCatch.pageData.__sortDirect = sortDirect;
    return dataInCatch.pageData;
  },
  /**
   * 列表排序
   */
  sortPage: function(columIndex) {
    var currDirect = this.getColumDef(columIndex).sortDef.direct;
    //同栏目，切换排序顺序
    if (this.sortIndex == columIndex) {
      if (currDirect && currDirect.toLowerCase() == "desc") {
        currDirect = "asc";
      }else if (currDirect && currDirect.toLowerCase() == "asc") {
        currDirect = "desc";
      }else {
        currDirect = "asc";
      }
      this.getColumDef(this.sortIndex).sortDef.direct = currDirect;
    //不同栏目
    }else {
      this.sortIndex = columIndex;
      if (!currDirect) {
        currDirect = "asc";
      }
      //调整排序图标位置
      var sortFlagImg = $("sortFlagImg");
      if (sortFlagImg) {
        sortFlagImg = sortFlagImg.remove();
        var headCell = $("headCell_" + columIndex);
        headCell.insert(sortFlagImg);
      }
    }
    //设置排序图标
    var sortFlagImg = $("sortFlagImg");
    if (sortFlagImg) {
      var imgSrc = this.getSortImg(this.getColumDef(this.sortIndex).sortDef.direct);
      sortFlagImg.src = imgSrc;
    }
    
    this.pageInfo.pageIndex = 0;
    
    var pageData = this._sortData(columIndex, currDirect);
    if (pageData) {
      this._showTableList(pageData);
    }
  },
  /**
   * 
   */
  _sortData: function(columIndex, currDirect) {
    this.pageInfo.pageIndex = 0;
    this.sortColumIndex = columIndex;
    this.direct = currDirect;
    this.loadPageData(0, columIndex, currDirect);
    return this.dataCatch[this.pageInfo.pageIndex].pageData;
  },
  /**
   * 绑定附件的浮动菜单


   */
  getImg: function(extName) {
    var imgArray = ["ai", "avi","bmp","cs", "dll", "doc", "docx","exe", "fla", "gif", "htm", "html"
                    , "jpg","js","mdb", "mp3", "pdf", "png","ppt", "pptx", "rar", "rdp", "swf"
                    , "swt","txt","vsd", "xls", "xlsx", "xml","zip","dot","mht","text" ];
    extName = extName.toLowerCase();
     if(imgArray.contains(extName)){
       return extName;
     }else{
       return "default";
     }
                         
  },
  /**
   * 取得附件的HTML
   */
  attachHtml: function(attachs, rowIndex, colIndex) {
    if (!attachs) {
      return "&nbsp;";
    }
    var rtStr = "";
    var nameArray = attachs.split("*");
    for (var i = 0; i < nameArray.length; i++) {
      var name = trim(nameArray[i]);
      if(!name.trim()){
        continue;
      }
      var index = name.lastIndexOf(".");
      var extName = "";
      if (index >= 0) {
        extName = name.substring(index + 1).toLowerCase();
      }
      var imgName = this.getImg(extName);
      rtStr += "<div>" 
        + "<img src=\"" + imgPath + "/fileExt/" + imgName + ".gif\" style=\"width:12px;height:12px\" title=\"" + name + "\">&nbsp;"
        + "<a id=\"attachLink_" + rowIndex + "_" + colIndex + "_" + i + "\" href=\"javascript:emptyFunc();\">" + name + "</a></div>";
    }
    return rtStr;
  },
  /**
   * 绑定附件的浮动菜单

   */
  bindAttachMenu: function(attachs, rowIndex, colIndex) {
    if (!attachs) {
      return;
    }
    var rtStr = "";
    var nameArray = attachs.split("*");
    for (var i = 0; i < nameArray.length; i++) {
      var name = nameArray[i];
      var id = "attachLink_" + rowIndex + "_" + colIndex + "_" + i;
      var cntrl = $(id);
      var floatMenu = this.getDocOptMenu(name, true);
      if (cntrl && floatMenu) {
        cntrl.observe("mouseover", this.showMenu.bindAsEventListener(this, cntrl, floatMenu, false));
      }
    }
  },
  /**
   * 格式化数值

   */
  formatDouble: function(strNum, format) {
    if (!strNum) {
      return "";
    }
    return insertKiloSplit(strNum, 2);
  },
  /**
   * 格式化日期

   */
  formatDate: function(strDate, format) {
    if (!strDate) {
      return "";
    }
    return strDate.substring(0, 10);
  },
  /**
   * 格式化日期 + 时间
   */
  formatDateTime: function(strDateTime, format) {
    if (!strDateTime) {
      return "";
    }
    
    var len = format.length;
    if (strDateTime.length > len) {
      return strDateTime.substring(0, len);
    }else {
      return strDateTime;
    }
  },
  /**
   * 格式化日期 + 时间
   */
  formatTime: function(strDateTime, format) {
    if (!strDate) {
      return "";
    }
    return strDate.substring(11, 19);
  },
  /**
   * 处理选择
   */
  checkRows: function(isChecked) {
    if (!this.useCheck) {
      return;
    }
    var recordCnt = this.pageInfo.recordCnt;
    for (var i = 0; i < recordCnt; i++) {
      $("check_" + i).checked = isChecked;
    }
  },
  /**
   * 全选择/全取消选择
   */
  checkAll: function() {
    var btnCheckAll = $("btnCheckAll");
    if (!btnCheckAll) {
      return;
    }
    var isChecked = btnCheckAll.checked;
    if (isChecked) {
      btnCheckAll.title = "取消所有选择";
    }else {
      btnCheckAll.title = "选择所有";
    }
    var pageIndex = this.pageInfo.pageIndex;
    if (pageIndex == null) {
      return;
    }
    this.checkRows(isChecked);
  },
  /**
   * 取得当前页面记录条数
   */
  getRecordCnt: function() {
    return this.pageInfo.recordCnt;
  },
  /**
   * 取得指定记录
   * @reocrdIndex
   */
  getRecord: function(recordIndex) {
    var pageData = this.dataCatch[this.pageInfo.pageIndex];
    if (!pageData) {
      return null;
    }
    pageData = pageData ? pageData.pageData : null;
    if (!pageData) {
      return null;
    }
    return pageData[recordIndex];
  },
  /**
   * 取得栏目定义
   */
  getColumDef: function(columIndex) {
    if (!this.colums) {
      return null;
    }
    return this.colums[columIndex];
  },
  /**
   * 取得单元格数据

   */
  getCellData: function(recordIndex, name) {
    var record = this.getRecord(recordIndex);
    if (!record) {
      return "";
    }
    return record[name];
  },
  /**
   * 显示
   */
  show: function() {
    if (this.isDisp) {
      return;
    }
    this.isDisp = true;
    if (this.isInit) {
      this.buildNevBar();    
      this.buildHeader();
      this.bindNevAction();
      this.bindSortAction();
      this.isInit = false;
      this.showPage(0);
    }else {
      $("pgTopPanel").show();
    }
  },
  /**
   * 执行新的查询
   */
  search: function() {
    this.clearCatch();
    this.isDisp = false;
    this.showPage(0);
  },
  /**
   * 隐藏
   */
  hide: function() {
    if (!this.isDisp) {
      return;
    }
    this.isDisp = false;
    $("pgTopPanel").hide();
  },
  /**
   * 第一页

   */
  firstPage: function() {
    this.showPage(0);
  },
  /**
   * 前一页

   */
  prePage: function() {
    this.showPage(this.pageInfo.pageIndex - 1);
  },
  /**
   * 跳转到

   */
  gotoPage: function() {
    var pageNo = $("pageIndex").value;
    if (!pageNo) {
      return;
    }
    if (!isNumber(pageNo)) {
      alert("页码需要是数值");
      selectLast($("pageIndex"));
      return;
    }
    pageNo = parseInt(pageNo);
    if (pageNo < 1 || pageNo > this.pageInfo.totalPage) {
      alert("请输入合理页码");
      selectLast($("pageIndex"));
      $("pageIndex").value = this.pageInfo.pageIndex + 1;
      return;
    }
    this.showPage(pageNo - 1);
  },
  /**
   * 后一页
   */
  nextPage: function() {
    this.showPage(this.pageInfo.pageIndex + 1);
  },
  /**
   * 最后一页

   */
  lastPage: function() {
    this.showPage(this.pageInfo.totalPage - 1);
  },
  /**
   * 刷新列表
   * @index           传递时刷新指定页面,否则刷新当前页

   */
  refreshPage: function(index) {
    if ((!index && index !== 0) || (typeof index == "object")) {
      index = this.pageInfo.pageIndex;
    }
    this.clearCatch(index);
    this.showPage(index);
  },
  /**
   * 刷新列表-清除缓存数据，重新加载

   */
  refreshAll: function() {
    this.dataCatch.clear();
    this.firstPage();
  },
  /**
   * 构造导航栏
   */
  buildNevBar: function() {
    if (!this.isInit) {
      return;
    }
    var nevBarText = getTextRs(contextPath + "/core/inc/pagenevbar.jsp");
    this.container.innerHTML = nevBarText;
    
    //加载页面记录大小列表
    var selectArray = new Array();
    for (var i = 0; i < this.sizeList.size(); i++) {
      var size = this.sizeList.get(i);
      var record = new CodeRecord();
      record.code = size;
      record.desc = size;
      selectArray.add(record);
    }
    var selectSize = $("selectPageSize");
    loadSelectData(selectSize, selectArray , this.pageInfo.pageSize);
  },
  /**
   * 变换页面记录多好
   */
  changePageSize: function() {
    var selectSize = $("selectPageSize");
    if (!selectSize) {
      return;
    }
    this.pageInfo.pageSize = selectSize.value ? parseInt(selectSize.value) : 20;
    this.refreshAll();
  },
  /**
   * 为导航栏绑定事件处理
   */
  bindNevAction: function() {
    if (!this.isInit) {
      return;
    }
    //pageSize选择
    $("selectPageSize").observe("change", this.changePageSize.bind(this));
    //第一页

    $("btnPgFirst").observe("click", this.firstPage.bind(this));
    //前一页

    $("btnPgPre").observe("click", this.prePage.bind(this));
    //页面跳转页码输入
    $("pageIndex").observe("blur", this.gotoPage.bind(this));
    //后一页

    $("btPgNext").observe("click", this.nextPage.bind(this));
    //最后一页

    $("btnPgLast").observe("click", this.lastPage.bind(this));
    //刷新当前页

    $("btnRefresh").observe("click", this.refreshPage.bind(this));
    //全选/取消
    var btnCheckAll = $("btnCheckAll");
    if (btnCheckAll) {
      btnCheckAll.observe("click", this.checkAll.bind(this));
    }
  },
  /**
   * 创建操作按钮
   */
  buildOptBtns: function(index) {
    if (!this.opts) {
      return;
    }
    var rtStr = "";
    var optCnt = this.opts.length;
    for (var i = 0; i < optCnt; i++) {
      var opt = this.opts[i];
      rtStr += "<a id=\"btnOpt_" + index + "_" + i + "\" name=\"btnOpt_" + index + "_" + i + "\""
        + " href=\"javascript:emptyFunc();\">" + opt.text + "</a>";
      if (i < optCnt - 1) {
        rtStr += "&nbsp;&nbsp;";
      }
    }
    return rtStr;
  },
  /**
   * 绑定排序事件
   */
  bindSortAction: function() {
    var sortCnt = this.sortColums.length;
    for (var i = 0; i < sortCnt; i++) {
      var columIndex = this.sortColums[i];
      var sortLinkCntrl = $("sortLink_" + columIndex);
      if (sortLinkCntrl) {
        sortLinkCntrl.onclick = this.sortPage.bind(this, columIndex);
      }
    }
  },
  /**
   * 显示浮动菜单
   */
  showMenu: function() {
    var event = arguments[0];
    var cntrl = arguments[1];
    var menuItems = arguments[2];
    var attachCntrl = arguments[3];
    if (!attachCntrl && attachCntrl !== false) {
      attachCntrl = true;
    }
    var menu = new Menu({bindTo: cntrl.id , menuData:menuItems , attachCtrl: attachCntrl});
    menu.show(event);
  },
  /**
   * 绑定按钮的行为

   */
  bindBtnActions: function(index) {
    if (!this.opts) {
      return;
    }
    var optCnt = this.opts.length;
    for (var i = 0; i < optCnt; i++) {
      var opt = this.opts[i];
      var cntrl = $("btnOpt_" + index + "_" + i);
      if (cntrl) {
        if (opt.floatMenu) {
          cntrl.observe("click", this.showMenu.bindAsEventListener(this, cntrl, opt.floatMenu, index));
        }else if (opt.clickFunc) {
          cntrl.observe("click", opt.clickFunc.bind(this, index));
        }
        if (opt.overFunc) {
          cntrl.observe("mouseover", opt.overFunc.bind(this, index));
        }
      }
    }
  },
  /**
   * 绑定浮动菜单
   */
  bindFloagMenu: function(rowIndex, colIndex) {
    var clm = this.getColumDef(colIndex);
    if (!clm || !clm.floatMenu) {
      return;
    }
    var cntrl = $("cellSpan_" + rowIndex + "_" + colIndex);
    if (cntrl) {
      cntrl.observe("click", this.showMenu.bindAsEventListener(this, cntrl, clm.floatMenu));
    }
  },
  /**
   * 切换样式
   */
  switchClass: function(cntrl, srcCls, destCls) {
    cntrl.removeClassName(srcCls);
    cntrl.addClassName(destCls);
  },
  /**
   * 刷新导航兰状态

   */
  refreshNevBar: function() {
    //第一页

    if (this.pageInfo.pageIndex < 1) {
      this.switchClass($("btnPgFirst"), "pgFirst", "pgFirstDisabled");
      $("btnPgFirst").disabled = true;
    }else {
      this.switchClass($("btnPgFirst"), "pgFirstDisabled", "pgFirst");
      $("btnPgFirst").disabled = false;
    }
    //前一页

    if (this.pageInfo.pageIndex < 1) {
      this.switchClass($("btnPgPre"), "pgPrev", "pgPrevDisabled");
      $("btnPgPre").disabled = true;
    }else {
      this.switchClass($("btnPgPre"), "pgPrevDisabled", "pgPrev");
      $("btnPgPre").disabled = false;
    }
    //页面跳转页码输入
    if (this.pageInfo.totalPage < 1) {
      $("pageIndex").disabled = true;
      $("pageIndex").value = "";
    }else {
      $("pageIndex").disabled = false;
      $("pageIndex").value = this.pageInfo.pageIndex + 1;
    }
    //后一页

    if (this.pageInfo.pageIndex >= this.pageInfo.totalPage - 1) {
      this.switchClass($("btPgNext"), "pgNext", "pgNextDisabled");
      $("btPgNext").disabled = true;
    }else {
      this.switchClass($("btPgNext"), "pgNextDisabled", "pgNext");
      $("btPgNext").disabled = false;
    }
    //最后一页

    if (this.pageInfo.pageIndex >= this.pageInfo.totalPage - 1) {
      this.switchClass($("btnPgLast"), "pgLast", "pgLastDisabled");
      $("btnPgLast").disabled = true;
    }else {
      this.switchClass($("btnPgLast"), "pgLastDisabled", "pgLast");
      $("btnPgLast").disabled = false;
    }
    //共XXX页

    $("pageCount").innerHTML = this.pageInfo.totalPage;
    //记录信息条数提示
    if (this.showRecordCnt === true) {
      $("pgSearchInfo").innerHTML = "共&nbsp;" + this.pageInfo.totalRecord
        + "&nbsp;条记录，显示第&nbsp;" + (this.pageInfo.startIndex + 1)
        + "&nbsp;条 -&nbsp;第 &nbsp;" + (this.pageInfo.endIndex + 1)
        + "&nbsp;条记录";
    }
  },
  /**
   * 取得文件操作类型
   */
  getDocOptType: function(name, readOnly) {
    if (!name) {
      return null;
    }
    //alert(name);
    var index = name.lastIndexOf(".");
    var extName = "";
    if (index >= 0) {
      extName = name.substring(index + 1).toLowerCase();
    }
    if (["doc", "xls", ,"ppt", "docx", "xlsx", "pptx"].contains(extName)) {
      if (readOnly) {
        return "officeReadOnly";
      }else {
        return "officeEditable";
      }
    }else if (["mp3","mht"].contains(extName)) {
      if (readOnly) {
        return "audio";
      }else {
        return "audioEditable";
      }
    }else if (["rm", "wav"].contains(extName)) {
      if (readOnly) {
        return "vedio";
      }else {
        return "vedioEditable";
      }
    }else {
      if (readOnly) {
        return "others";
      }else {
        return "othersEditable";
      }
    }
  },
  /**
   * 取得文件操作菜单
   */
  getDocOptMenu: function(name, readOnly) {
    var docOptType = this.getDocOptType(name, readOnly);
    if (!docOptType) {
      return null;
    }
    return this.docMenus[docOptType];
  }
}