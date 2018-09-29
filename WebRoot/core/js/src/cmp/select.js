/**
 * Select控件管理
 * 数据加载、联动刷新、初始化值
 * config {cntrlId, tableName, codeField, nameField, value,
 *         isMustFill, filterField, filterValue, order,
 *         reloadBy, actionUrl, extFilter, extData}
 * @return
 */
function SelectMgr() { 
  //参数定义
  this.params = new ArrayHashMap();
  //数据缓存
  this.dataCatch = new ArrayHashMap();
  //关联关系定义
  this.relaMap = new ArrayHashMap();
  //添加下拉框
  this.addSelect = function(config) {
    this.params.put(config.cntrlId, config);
    var reloadBy = config.reloadBy;
    if (reloadBy) {
      var relaArray = this.relaMap.get(reloadBy);
      if (!relaArray) {
        relaArray = new Array();
        this.relaMap.put(reloadBy, relaArray);    
      }
      relaArray.add(config.cntrlId);
    }
  }
  /**
   * 绑定事件处理程序
   */
  this.regisEvenHandle = function() {
    var paramCnt = this.params.size();
    for (var i = 0; i < paramCnt; i++) {
      var cntrlId = this.params.getKey(i);
      $(cntrlId).observe("change", this.handleChange.bind(this, cntrlId));
    }
  }
  /**
   * 取得提取数据字符串
   * @param cntrlId 提供的情况下，则取得指定控件的查询参数，不指定，则所有控件的查询参数
   */
  this.getQueryParam = function(cntrlId) {
    var paramArray = [];
    if (cntrlId) {
      paramArray[0] = this.params.get(cntrlId);
    }else {
      var paramCnt = this.params.size();
      for (var i = 0; i < paramCnt; i++) {
        var param = this.params.getValue(i);
        paramArray[i] = param;
      }
    }
    var paramMap = {dtoClass : "yh.core.dto.YHCodeLoadParamSet",
          paramClass: "yh.core.dto.YHCodeLoadParam"};
    var paramCnt = paramArray.length;
    paramMap["paramCnt"] = paramCnt;
    for (var i = 0; i < paramCnt; i++) {
      var param = paramArray[i];
      paramMap["cntrlId_param_" + i] = param.cntrlId;
      if (param.tableName) {
        paramMap["tableName_param_" + i] = param.tableName;
        paramMap["codeField_param_" + i] = param.codeField;
        paramMap["nameField_param_" + i] = param.nameField;
        paramMap["filterField_param_" + i] = param.filterField ? param.filterField : "";
        paramMap["order_param_" + i] = param.order ? param.order : "";
      }
      paramMap["value_param_" + i] = param.value ? param.value : "";
      paramMap["isMustFill_param_" + i] = param.isMustFill ? param.isMustFill : "0";
      paramMap["filterValue_param_" + i] = param.filterValue ? param.filterValue : "";
      paramMap["reloadBy_param_" + i] = param.reloadBy ? param.reloadBy : "";
      paramMap["actionUrl_param_" + i] = param.actionUrl ? param.actionUrl : contextPath + "/yh/core/act/YHSelectDataAct/loadData.act";
      paramMap["extFilter_param_" + i] = param.extFilter ? param.extFilter : "";
    }
    return $H(paramMap).toQueryString();
  }
  /**
   * 批量加载数据，考虑系统优化，适用与数据库表中提取数据的场合
   * @param cntrlId      提供的情况下，加载一个控件的数据，不提供则加载所有的数据
   */
  this.loadData = function(cntrlId) {    
    var queryParam =  this.getQueryParam(cntrlId);
    var rtJson = getJsonRs(contextPath + "/yh/core/act/YHSelectDataAct/loadData.act", queryParam);

    if (rtJson.rtState != "0") {
      alert(rtJson.rtMsrg);
      return;
    }
    rtJson = rtJson.rtData;
    
    if (cntrlId) {
      var param = this.params.get(cntrlId);
      if (param.extData) {
        rtJson[cntrlId].data.addAll(param.extData, 0);
      }
      this.dataCatch.put(cntrlId, rtJson[cntrlId]);    
    }else {
      var paramCnt = this.params.size();
      for (var i = 0; i < paramCnt; i++) {
        var param = this.params.getValue(i);
        cntrlId = param.cntrlId;
        if (param.extData) {
          rtJson[cntrlId].data.addAll(param.extData, 0);
        }
        this.dataCatch.put(cntrlId, rtJson[cntrlId]);
      }
    }
  }
  /**
   * 逐个加载数据，适用于非数据库表提取数据的场合
   */
  this.loadDataOneByOne = function(cntrlId) {
    var paramArray = [];
    if (cntrlId) {
      paramArray[0] = this.params.get(cntrlId);
    }else {
      var paramCnt = this.params.size();
      for (var i = 0; i < paramCnt; i++) {
        var param = this.params.getValue(i);
        paramArray[i] = param;
      }
    }
    //首先加载非关联项目
    for (var i = 0; i < paramArray.size(); i++) {
      var param = paramArray[i];
      if (!param.actionUrl) {
        continue;
      }
      if (param.reloadBy) {
        continue;
      }      
      var currCntrlId = param.cntrlId;
      var queryParam = this.getQueryParam(currCntrlId)
      var rtJson = getJsonRs(param.actionUrl, queryParam);
      if (rtJson.rtState != "0") {
        //alert(rtJson.rtMsrg);
        continue;
      }
      rtJson = rtJson.rtData;
      if (param.extData) {
        rtJson[cntrlId].data.addAll(param.extData, 0);
      }
      this.dataCatch.put(currCntrlId, rtJson[currCntrlId]);
    }
    //然后加载关联项目
    for (var i = 0; i < paramArray.size(); i++) {
      var param = paramArray[i];
      if (!param.actionUrl) {
        continue;
      }
      if (!param.reloadBy) {
        continue;
      }      
      param.filterValue = this.dataCatch.get(param.reloadBy).value;
      if (!param.filterValue) {
        continue;
      }
      var currCntrlId = param.cntrlId;
      var queryParam = this.getQueryParam(currCntrlId)
      var rtJson = getJsonRs(param.actionUrl, queryParam);
      //alert(rsText);
      if (rtJson.rtState != "0") {
        //alert(rtJson.rtMsrg);
        continue;
      }
      rtJson = rtJson.rtData;
      if (param.extData) {
        rtJson[cntrlId].data.addAll(param.extData, 0);
      }
      this.dataCatch.put(currCntrlId, rtJson[currCntrlId]);
    }
  }
  
  /**
   * 把数据绑定到控件
   */
  this.bindData2Cntrl = function(cntrlId) {
    if (cntrlId) {
      var cntrl = $(cntrlId);
      var cntrlData = this.dataCatch.get(cntrlId);
      if (!cntrlData) {
        return;
      }
      if (!cntrlData.data) {
        return;
      }
      loadSelectData(cntrl, cntrlData.data, cntrlData.value);
    }else {
      var paramCnt = this.params.size();
      for (var i = 0; i < paramCnt; i++) {
        var param = this.params.getValue(i);
        cntrlId = param.cntrlId;
        if (!cntrlId) {
          continue;
        }
        this.bindData2Cntrl(cntrlId);
      }
    }
  }
  /**
   * 处理变化
   */
  this.handleChange = function(cntrlId) {
    var filterValue = $(cntrlId).value;
    this.dataCatch.get(cntrlId).value = filterValue;

    var relaArray = this.relaMap.get(cntrlId);
    if (!relaArray) {
      return;
    }
    if (!filterValue) {
      return;
    }
    var relaCnt = relaArray.size();
    for (var i = 0; i < relaCnt; i++) {
      var relaId = relaArray.get(i);
      var param = this.params.get(relaId);
      if (param.filterValue == filterValue) {
        continue;
      }
      //alert(filterValue);
      param.filterValue = filterValue;
      if (param.actionUrl) {
        this.loadDataOneByOne(relaId);
      }else {
        this.loadData(relaId);
      }
      this.bindData2Cntrl(relaId);
    }
  }
}
