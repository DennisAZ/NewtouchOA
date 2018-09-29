/**
 * 清除select控件的数据
 * @selectObj  select控件
 */
function clearSelectData(selectObj) {
  if (!selectObj.options) {
    return;
  }
  for (var i = selectObj.options.length - 1; i >= 0; i--) {
    selectObj.remove(i);
  }
}

/**
 * 加载select控件的数据
 * @selectObj  select控件
 * @data       控件需要显示的数据
 *             数据结构Array(CodeRecord, ...)
 */
function loadRadioData(radioObj, data, radioValue) {
  var radiopanel = radioObj.parentNode;
  //alert(radiopanel);
  var name = radioObj.id;
  radiopanel.removeChild(radioObj);
  //alert("sss");
  if (!data) {
    return;
  }
  var dataSize = data.size();
  for (var i = 0; i < dataSize; i++) {
    var codeRecord = data.get(i);
    //alert(codeRecord.desc);
    if(!codeRecord.desc){
      continue;
    }
    var myRadio = document.createElement("INPUT");
    var mySpan = document.createElement("SPAN");
    myRadio.value = codeRecord.code;
    myRadio.type = "radio";
    myRadio.name = name;
    mySpan.innerHTML = codeRecord.desc;
    
    radiopanel.appendChild(myRadio);
    radiopanel.appendChild(mySpan);
    
    if (myRadio.value == radioValue) {
      myRadio.checked = true;
    }
  }
}

/**
 * Select控件管理
 * 数据加载、联动刷新、初始化值
 * config {cntrlId, tableName, codeField, nameField, value, isMustFill, filterField, filterValue, order, reloadBy}
 * @return
 */
function RadioMgr() {
  //参数定义
  this.params = new ArrayHashMap();
  //数据缓存
  this.dataCatch = new ArrayHashMap();
  //关联关系定义
  this.relaMap = new ArrayHashMap();
  //添加下拉框 
  this.addRadio = function(config) {
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
	var relaCnt = this.relaMap.size();
	for (var i = 0; i < relaCnt; i++) {
	  var relaId = this.relaMap.getKey(i);
	  $(relaId).onclick = this.handleChange.bind(this, relaId);
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
	  paramMap["tableName_param_" + i] = param.tableName;
	  paramMap["codeField_param_" + i] = param.codeField;
	  paramMap["nameField_param_" + i] = param.nameField;
	  paramMap["value_param_" + i] = param.value ? param.value : "";
	  paramMap["isMustFill_param_" + i] = param.isMustFill ? param.isMustFill : "0";
	  paramMap["filterField_param_" + i] = param.filterField ? param.filterField : "";
	  paramMap["filterValue_param_" + i] = param.filterValue ? param.filterValue : "";
	  paramMap["order_param_" + i] = param.order ? param.order : "";
	  paramMap["reloadBy_param_" + i] = param.reloadBy ? param.reloadBy : "";
	}
	return $H(paramMap).toQueryString();
  }
  /**
   * 加载数据
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
	  this.dataCatch.put(cntrlId, rtJson[cntrlId]);	  
	}else {
	  var paramCnt = this.params.size();
	  for (var i = 0; i < paramCnt; i++) {
		var param = this.params.getValue(i);
		cntrlId = param.cntrlId;
		this.dataCatch.put(cntrlId, rtJson[cntrlId]);
	  }
	}
  }
  /**
   * 把数据绑定到控件
   */
  this.bindData2Cntrl = function(cntrlId) {
	if (cntrlId) {
	  var cntrl = $(cntrlId);
    //alert(cntrl);

	  var cntrlData = this.dataCatch.get(cntrlId);
	  if (!cntrlData) {
		return;
	  }
	  if (!cntrlData.data) {
		return;
	  }
	  loadRadioData(cntrl, cntrlData.data, cntrlData.value);
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
	var relaArray = this.relaMap.get(cntrlId);
	if (!relaArray) {
	  return;
	}
	var filterValue = $(cntrlId).value;
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
      param.filterValue = filterValue;
      this.loadData(relaId);
      this.bindData2Cntrl(relaId);
	}
  }
}
/**
 * 选择框扩展
 */
function extSelect(selectCntrl, tableName, codeField, nameField) {
  var cntrl = $(selectCntrl);
  Object.extend(cntrl, {
	tableName: tableName,
	codeField: codeField,
	nameField: nameField,
	data: [],
	loadData: function(filter) {
	  var paramFilter = "";	  
	  paramFilter = "tableName=" + this.tableName;
	  paramFilter += "&codeField=" + this.codeField;
	  paramFilter += "&nameField=" + this.nameField;
	  if (filter) {
		paramFilter += "&filter=" + filter; 
	  }
	  var rtJson = getJsonRs(contextPath + "/yh/core/act/YHSelectDataAct/loadData.act", paramFilter);

	  if (rtJson.rtState == "0") {
		var codeData = rtJson.rtData;
		loadSelectData(this, codeData, "");
	  }
    }
  });
  return cntrl;
}