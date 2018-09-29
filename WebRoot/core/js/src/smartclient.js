/**
 * 智能客户端相关JS定义
 * @author               YZQ
 * @date                 2007-11-15
 */
var rsText = null;
//窗口的大小
/**
 * 处理请求参数串中的千分符
 * @queryParams         请求参数串%2C是form序列化参数时对逗号的处理
 */
function processKiloSplit(queryParams) {
  queryParams = queryParams.replace(/(?:%2C|,)(\d{3})/g, "$1");
  return queryParams;
}

/**
 * 合并查询字符串
 * @queryMap
 * @formId
 */
function mergeQueryString() {
  var args = $A(arguments);
  var argCnt = args.length;
  var params = "";
  for (var i = 0; i < argCnt; i++) {
    var argObj = args[i];
    //字符串 
    if (typeof argObj == "string") {
      if (argObj.length < 1) {
        continue;
      }
      var pattern = /^(.+=.*)+(&.+=.*)*$/gi;
      //查询字符串
      if (pattern.test(argObj)) {
        params += "&" + argObj;
      //FORM的ID
      }else {
        params += "&" + $(argObj).serialize();
      }      
    //FORM控件对象
    }else if (argObj.tagName && argObj.tagName.toLowerCase() == "form") {
      params += "&" + $(argObj).serialize();
    //其他情况
    }else {
      params += "&" + $H(argObj).toQueryString();
    }
  }

  return params.substring(1);
}
/**
 * 同步方式，以文本形式从服务器取得响应
 * @formId            表单对象的name或者ID
 * @actionUrl         服务器端活动路径
 * @callBackFunc      响应处理函数，函数的接口是 callBackFunc()
 * @isClearKiloSplit  是否处理千分符号
 */
function getTextRs(actionUrl, queryParams, callBackFunc, isClearKiloSplit) {
  var queryOption = {
    queryParams: queryParams,
    actionUrl: actionUrl,
    callBackFunc: callBackFunc,
    isAsyn: false
  };
  return getRsBasic(queryOption, isClearKiloSplit, "text");
}

/**
 * 同步方式，以布尔值形式从服务器取得响应
 * @formId            表单对象的name或者ID
 * @actionUrl         服务器端活动路径
 * @callBackFunc      响应处理函数，函数的接口是 callBackFunc()
 * @isClearKiloSplit  是否处理千分符号
 */
function getBooleanRs(actionUrl, queryParams, callBackFunc, isClearKiloSplit) {
  var queryOption = {
    queryParams: queryParams,
    actionUrl: actionUrl,
    callBackFunc: callBackFunc,
    isAsyn: false
  };
  return getRsBasic(queryOption, isClearKiloSplit, "boolean");
}

/**
 * 同步方式，以整型形式从服务器取得响应
 * @formId            表单对象的name或者ID
 * @actionUrl         服务器端活动路径
 * @callBackFunc      响应处理函数，函数的接口是 callBackFunc()
 * @isClearKiloSplit  是否处理千分符号
 */
function getIntRs(actionUrl, queryParams, callBackFunc, isClearKiloSplit) {
  var queryOption = {
    queryParams: queryParams,
    actionUrl: actionUrl,
    callBackFunc: callBackFunc,
    isAsyn: false
  };
  return getRsBasic(queryOption, isClearKiloSplit, "int");
}

/**
 * 同步方式，以浮点型形式从服务器取得响应
 * @formId            表单对象的name或者ID
 * @actionUrl         服务器端活动路径
 * @callBackFunc      响应处理函数，函数的接口是 callBackFunc()
 * @isClearKiloSplit  是否处理千分符号
 */
function getFloatRs(actionUrl, queryParams, callBackFunc, isClearKiloSplit) {
  var queryOption = {
    queryParams: queryParams,
    actionUrl: actionUrl,
    callBackFunc: callBackFunc,
    isAsyn: false
  };
  return getRsBasic(queryOption, isClearKiloSplit, "float");
}

/**
 * 同步方式，以JSON形式从服务器取得响应
 * @formId            表单对象的name或者ID
 * @actionUrl         服务器端活动路径
 * @callBackFunc      响应处理函数，函数的接口是 callBackFunc()
 * @isClearKiloSplit  是否处理千分符号
 */
function getJsonRs(actionUrl, queryParams, callBackFunc, isClearKiloSplit) {
  var queryOption = {
    queryParams: queryParams,
    actionUrl: actionUrl,
    callBackFunc: callBackFunc,
    isAsyn: false
  };
  return getRsBasic(queryOption, isClearKiloSplit, "json");
}

/**
 * 同步方式，以xml形式从服务器取得响应
 * @formId            表单对象的name或者ID
 * @actionUrl         服务器端活动路径
 * @callBackFunc      响应处理函数，函数的接口是 callBackFunc()
 * @isClearKiloSplit  是否处理千分符号
 */
function getXmlRs(actionUrl, queryParams, callBackFunc, isClearKiloSplit) {
  var queryOption = {
    queryParams: queryParams,
    actionUrl: actionUrl,
    callBackFunc: callBackFunc,
    isAsyn: false
  };
  return getRsBasic(queryOption, isClearKiloSplit, "xml");
}

/**
 * 异步方式，以文本形式从服务器取得响应
 * @formId            表单对象的name或者ID
 * @actionUrl         服务器端活动路径
 * @callBackFunc      响应处理函数，函数的接口是 callBackFunc()
 * @isClearKiloSplit  是否处理千分符号
 */
function getTextRsAsyn(actionUrl, queryParams, callBackFunc, isClearKiloSplit) {
  var queryOption = {
    queryParams: queryParams,
    actionUrl: actionUrl,
    callBackFunc: callBackFunc,
    isAsyn: true
  };
  getRsBasic(queryOption, isClearKiloSplit, "text");
}

/**
 * 异步方式，以布尔值形式从服务器取得响应
 * @formId            表单对象的name或者ID
 * @actionUrl         服务器端活动路径
 * @callBackFunc      响应处理函数，函数的接口是 callBackFunc()
 * @isClearKiloSplit  是否处理千分符号
 */
function getBooleanRsAsyn(actionUrl, queryParams, callBackFunc, isClearKiloSplit) {
  var queryOption = {
    queryParams: queryParams,
    actionUrl: actionUrl,
    callBackFunc: callBackFunc,
    isAsyn: true
  };
  getRsBasic(queryOption, isClearKiloSplit, "boolean");
}

/**
 * 异步方式，以整型形式从服务器取得响应
 * @formId            表单对象的name或者ID
 * @actionUrl         服务器端活动路径
 * @callBackFunc      响应处理函数，函数的接口是 callBackFunc()
 * @isClearKiloSplit  是否处理千分符号
 */
function getIntRsAsyn(actionUrl, queryParams, callBackFunc, isClearKiloSplit) {
  var queryOption = {
    queryParams: queryParams,
    actionUrl: actionUrl,
    callBackFunc: callBackFunc,
    isAsyn: true
  };
  getRsBasic(queryOption, isClearKiloSplit, "int");
}

/**
 * 异步方式，以浮点型形式从服务器取得响应
 * @formId            表单对象的name或者ID
 * @actionUrl         服务器端活动路径
 * @callBackFunc      响应处理函数，函数的接口是 callBackFunc()
 * @isClearKiloSplit  是否处理千分符号
 */
function getFloatRsAsyn(actionUrl, queryParams, callBackFunc, isClearKiloSplit) {
  var queryOption = {
    queryParams: queryParams,
    actionUrl: actionUrl,
    callBackFunc: callBackFunc,
    isAsyn: true
  };
  getRsBasic(queryOption, isClearKiloSplit, "float");
}

/**
 * 异步方式，以JSON形式从服务器取得响应
 * @formId            表单对象的name或者ID
 * @actionUrl         服务器端活动路径
 * @callBackFunc      响应处理函数，函数的接口是 callBackFunc()
 * @isClearKiloSplit  是否处理千分符号
 */
function getJsonRsAsyn(actionUrl, queryParams, callBackFunc, isClearKiloSplit) {
  var queryOption = {
    queryParams: queryParams,
    actionUrl: actionUrl,
    callBackFunc: callBackFunc,
    isAsyn: true
  };
  getRsBasic(queryOption, isClearKiloSplit, "json");
}

/**
 * 异步方式，以xml形式从服务器取得响应
 * @formId            表单对象的name或者ID
 * @actionUrl         服务器端活动路径
 * @callBackFunc      响应处理函数，函数的接口是 callBackFunc()
 * @isClearKiloSplit  是否处理千分符号
 */
function getXmlRsAsyn(actionUrl, queryParams, callBackFunc, isClearKiloSplit) {
  var queryOption = {
    queryParams: queryParams,
    actionUrl: actionUrl,
    callBackFunc: callBackFunc,
    isAsyn: true
  };
  getRsBasic(queryOption, isClearKiloSplit, "xml");
}

/**
 * 以表单为输入，从服务器取得响应
 * @formId            表单对象的name或者ID
 * @actionUrl         服务器端活动路径 
 * @callBackFunc      响应处理函数，函数的接口是 callBackFunc()
 * @isClearKiloSplit  是否处理千分符号
 * @rsType            服务器响应类型
 */
function getRsBasic(queryOption, isClearKiloSplit, rsType) {
  this.retData = null;
  /**
   * 默认处理请求
   */
  this.defaultHandler = function(data) {
    if (queryOption.isAsyn) {
      return;
    }
    this.retData = data;
  }
  var queryParams = queryOption.queryParams;
  var actionUrl = queryOption.actionUrl;
  var callBackFunc = queryOption.callBackFunc ?
    queryOption.callBackFunc : bindFunc(this.defaultHandler, this);
  var isAsyn = queryOption.isAsyn ? true : false;

  if (isClearKiloSplit) {
    queryParams = processKiloSplit(queryParams);
  }
  //rsText = null;
  new Ajax.Request(actionUrl, {
    parameters: queryParams,      
    asynchronous : isAsyn,
    onSuccess: function(transport, json) {
        rsText = transport.responseText;
        //alert("rtText>>" + rtText);
      if (!rsType || rsType.toLowerCase() == "text") {
        callBackFunc(transport.responseText);
      }else if (rsType.toLowerCase() == "boolean") {
        var rtBool = true;
        var rtText = trim(transport.responseText);
        if (!rtText || rtText.toLowerCase() == "false") {
          rtBool = false;
        }
        callBackFunc(rtBool);
      }else if (rsType.toLowerCase() == "int") {
        callBackFunc(parseInt(trim(transport.responseText)));
      }else if (rsType.toLowerCase() == "float") {
        callBackFunc(parseFloat(trim(transport.responseText)));
      }else if (rsType.toLowerCase() == "json") {
        //alert("JsonText" + transport.responseText);
        try {
          if (jsonDebug) {
            showLog("JsonText" + transport.responseText);
          }
        }catch(e) {
            }
        if (json) {
          callBackFunc(json);
        }else {
          var rtJson = transport.responseText.evalJSON();
          callBackFunc(rtJson);
        }
      }else if (rsType.toLowerCase() == "xml") {
        callBackFunc(transport.responseXML);
      }else {
        callBackFunc(transport.responseText);
      }        
    },
    onFailure : function(transport, json) {
      try {
        if (jsonDebug) {
          showLog("JsonText" + transport.responseText);
        }
      }catch(e) {
      }
      rtText = transport.responseText;
      //alert("与服务器通信发生了错误，一般长时间没使用系统会出现该错误，请重新登录！");
    },
    onException : function(request, exception) {
      //alert("产生异常: " + exception.description);
    }
  });
  if (this.retData) {
    return this.retData;
  }
}

var Assertor = {
  /**
   * 金额类控件绑定格式化
   * @cntrl              控件
   * @msrg               错误消息
   * @scale              小数位数
   * @extFunc            扩展函数
   */
  needFormatAmt : function(cntrl, msrg, extFunc) {
    if (typeof cntrl == "string") {
      var nameArray = cntrl.split(",");
      for (var i = 0; i < nameArray.length; i++) {
        this.needAssertNum(nameArray[i], amtScale, true, msrg, extFunc);
      }
    }else {
      this.needAssertNum(cntrl, amtScale, true, msrg, extFunc);
    }
  },
  
  /**
   * 金额类控件绑定格式化
   * @cntrl              控件
   * @msrg               错误消息
   * @scale              小数位数
   * @extFunc            扩展函数
   */
  needFormatExchRate : function(cntrl, msrg, extFunc) {
    if (typeof cntrl == "string") {
      var nameArray = cntrl.split(",");
      for (var i = 0; i < nameArray.length; i++) {
        this.needAssertNum(nameArray[i], exchScale, true, msrg, extFunc);
      }
    }else {
      this.needAssertNum(cntrl, exchScale, true, msrg, extFunc);
    }
  },
  
  /**
   * 给控件绑定格式化数值的事件处理
   * @cntrl              控件
   * @scale              小数位数
   * @msrg               错误消息
   * @scale              小数位数
   * @extFunc            扩展函数
   */
  needFormatNum : function(cntrl, scale, msrg, extFunc) {
    this.needAssertNum(cntrl, scale, true, msrg, extFunc);
  },
  /**
   * 给控件绑定格式化数值的事件处理
   * @cntrl              控件
   * @scale              小数位数
   * @msrg               错误消息
   * @scale              小数位数
   * @extFunc            扩展函数
   */
  needFormatInt : function(cntrl, msrg, extFunc) {
    this.needAssertNum(cntrl, 0, false, msrg, extFunc);
  },
  
  /**
   * 给控件绑定检查是否是数值的事件处理
   * @cntrl              控件
   * @scale              小数位数
   * @isKiloSplit        是否插入千分符         
   * @msrg               错误消息
   * @extFunc            扩展函数
   */
  needAssertNum : function (cntrl, scale, isKiloSplit, msrg, extFunc) {
    cntrl = $(cntrl);
    cntrl.onblur = function() {
      var valueStr = clearKiloSplit(cntrl.value);
      if (!assertNumber(valueStr, cntrl, msrg)) {
        return;
      }
      if (isKiloSplit) {
        cntrl.value = insertKiloSplit(valueStr, scale);
      }
      if (extFunc) {
        extFunc(cntrl);
      }
    }
  },
  
  /**
   * 给控件绑定日期检查的事件处理
   * @cntrl              控件
   * @msrg               错误消息
   * @extFunc            扩展函数
   */
  needAssertDate : function(cntrl, msrg, extFunc) {
    if (typeof cntrl == "string") {
      var nameArray = cntrl.split(",");
      for (var i = 0; i < nameArray.length; i++) {
        this.registAssertor(assertDate, nameArray[i], msrg, extFunc);
      }
    }else {
      this.registAssertor(assertDate, cntrl, msrg, extFunc);
    }    
  },
  
  /**
   * 检查控件是否为空值
   * @cntrl          控件
   * @msrg           消息
   */
  assertFilled : function(cntrl, msrg) {
    if (typeof cntrl == "string") {
      var nameArray = cntrl.split(",");      
      for (var i = 0; i < nameArray.length; i++) {
        var currCntrl = $(nameArray[i]);
        if (!assertFilled(currCntrl.value, currCntrl, msrg)) {
          return false;
        }
      }
      return true;
    }else {
      var currCntrl = $(cntrl);
      return assertFilled(currCntrl.value, currCntrl, msrg);
    }  
  },
  
  /**
   * 事件绑定
   */
  registAssertor : function(assertFunc, cntrl, msrg, extFunc) {
    cntrl = $(cntrl);
    cntrl.onblur = function() {
      var valueStr = cntrl.value;
      if (!assertFunc(valueStr, cntrl, msrg)) {
        return;
      }
      if (extFunc) {
        extFunc(cntrl);
      }
    }
  }
}

var StateManager = {
  stateStyle : {
    defaultStyle : "input",
    input_active_number : "InputTextNum ",
    input_readOnly_number : "InputTextNumReadOnly",     
    input_disabled_number : "InputTextNumReadOnly",
    input_active_text : "InputTextTextActive",
    input_readOnly_text : "InputTextTextReadOnly",     
    input_disabled_text : "InputTextTextReadOnly",
    select_active : "input_select",
    select_readOnly : "input_select",
    select_disabled : "input_select",
    img_active : "btn_img",
    img_readOnly : "btn_img",
    img_disabled : "btn_img"
  },  
  /**
   * 取得控件的显示风格   
   * @cntrl            控件名称
   * @state            控件状态
   * @cntrlType        控件类别
   */
  getDispStyle : function(cntrl, state, cntrlType) {    
    cntrl = $(cntrl);
    if (!state) {
      state = "writable";
    }
    var tagName = cntrl.tagName.toLowerCase();
    if (["input", "textarea"].include(tagName)) {
      tagName = "input";
      if (!cntrlType) {
        cntrlType = "text";
      }
    }else if ("img" == tagName) {
      tagName = "img";
      cntrlType = "";
    }else {
      tagName = "select";
      cntrlType = "";
    }
    var selectName = tagName + "_" + state;
    if (cntrlType) {
      selectName = selectName + "_" + cntrlType;
    }
    return this.stateStyle[selectName];
  },
  _setState : function(cntrl, cntrlState, styleClass, value, otherAttrs) {
    if (cntrlState) {
      cntrl["readOnly"] = cntrlState.readOnly;
      cntrl["disabled"] = cntrlState.disabled;
    }
    if (styleClass) {
      cntrl.className = styleClass;
    }
    if (value) {
      cntrl.value = value;
    }
    if (otherAttrs) {
      var attrArray = otherAttrs.split(",");
      for (var i = 0; i < attrArray.length; i++) {
        var paramArray = attrArray[i].split("=");
        cntrl[paramArray[0]] = paramArray[1];
      }
    }
  },
  /**
   * 设置只读风格
   */
  setState : function(state, cntrlState, cntrls, cntrlType, values, otherAttrs) {
    if (typeof cntrls == "string" && cntrls.indexOf(",") > 0) {
      var cntrlArray = cntrls.split(",");
      var valueArray = null;
      if (values) {
        valueArray = values.split(",");
      }
      for (var i = 0; i < cntrlArray.length; i++) {
        var cntrl = cntrlArray[i];
        var value = null;
        if (valueArray) {
          value = valueArray[i];
        }
        this.setState(state, cntrlState, cntrl, cntrlType, value);
      }
    }else {
      var cntrl = $(cntrls);
      if (!cntrl || cntrl.style.display == "none") {
        return;
      }
      if (cntrl.type == "hidden") {
        var cntrlName = cntrl.name ? cntrl.name : cntrl.id;
        var inputName = cntrlName + "_DISP";
        var btnName = "btn" + cntrlName;
        var inputCntrl = $(inputName);
        var butnCntrl = $(btnName);
        if (inputCntrl && butnCntrl) {
          this._setState(inputCntrl,
            cntrlState,
            this.getDispStyle(inputCntrl, state, cntrlType),
            values,
            otherAttrs);
          
          if (cntrlState.readOnly) {
            cntrlState.disabled = true;
          }
          this._setState(butnCntrl,
            cntrlState,
            this.getDispStyle(butnCntrl, state, cntrlType),
            null, 
            otherAttrs);
        }
      }else {
        this._setState(cntrl,
          cntrlState,
          this.getDispStyle(cntrls, state, cntrlType),
          values, 
          otherAttrs);
      }
    }
  },
  
  /**
   * 设置激活风格
   * @cntrls       控件（列表）
   * @values       控件值（列表）
   * @cntrlType    控件类别
   */
  setActive : function(cntrls, cntrlType, values, otherAttrs) {
    this.setState("active", {readOnly: false, disabled: false}, cntrls, cntrlType, values, otherAttrs);
  },
  /**
   * 设置激活风格
   * @cntrls       控件（列表）
   * @values       控件值（列表）
   * @cntrlType    控件类别
   */
  setReadOnly : function(cntrls, cntrlType, values, otherAttrs) {
    this.setState("readOnly", {readOnly: true, disabled: false}, cntrls, cntrlType, values, otherAttrs);
  },
  /**
   * 设置激活风格
   * @cntrls       控件（列表）
   * @values       控件值（列表）
   * @cntrlType    控件类别
   */
  setDisabled : function(cntrls, cntrlType, values, otherAttrs) {
    this.setState("disabled", {readOnly: true, disabled: true}, cntrls, cntrlType, values, otherAttrs);
  }
}

var ClientCatch = new function() {
  //数据对象缓存
  var _catch = new ArrayHashMap();
  
  /**
   * 取得对象
   * @key               键值
   * @actionUrl         活动URL
   */
  this.getObj = function(key, queryParam, actionUrl) {
    var rtObj = _catch.get(key);
    if (rtObj) {
      return rtObj;
    }
    if (!actionUrl) {
      actionUrl = _urlMap[key];
    }
    if (!actionUrl) {
      actionUrl = key;
    }
    actionUrl = contextPath + actionUrl;
    getJsonRs(queryParam, actionUrl,
        function(data) {
          if (data) {            
            _catch.put(key, data.getData());
          }
        }
      );
    return _catch.get(key);
  }
  
  /**
   * 重新加载对象
   */
  this.reloadObj = function(key, queryParam, actionUrl) {
    var rtObj = _catch.get(key);
    if (rtObj) {
      _catch.remove(key);
    }
    return this.getObj(key, queryParam, actionUrl);
  }
}
