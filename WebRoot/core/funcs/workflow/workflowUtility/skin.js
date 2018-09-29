function skinObjectToSpan(skinObject ) {
  //设置span标签
  if (!skinObject) {
    return ;
  }
  if (skinObject.title) {
    document.title = skinObject.title;
  }
  setSpanText(skinObject.spanText);
  setTooltipMsg(skinObject.tooltipMsg);
  setHiddenSpan(skinObject.hiddenSpan);
  setInsertSpan(skinObject.insertSpan);
  if (skinObject.loadScriptUrl) {
    loadScript(skinObject.loadScriptUrl);
  }
  if (skinObject.onloadFun) {
    skinObject.onloadFun();
  }
}
function setInsertSpan(insertSpan) {
  if ( insertSpan) {
    for (var i = 0 ;i < insertSpan.length ;i++) {
      var tmp = insertSpan[i];
      if (tmp && tmp.id) {
        var ctrl = $(tmp.id);
        if (ctrl) {
          ctrl.update(tmp.text);
        }
      }
    }
  }
}
function setHiddenSpan(hiddenSpan) {
  if ( hiddenSpan) {
    for (var i = 0 ;i < hiddenSpan.length ;i++) {
      var tmp = hiddenSpan[i];
      if (tmp) {
        var ctrl = $(tmp);
        if (ctrl) {
          ctrl.hide(); 
        }
      }
    }
  }
}
function setTooltipMsg(tooltipMsg) {
  if ( tooltipMsg) {
    for (var i = 0 ;i < tooltipMsg.length ;i++) {
      var tmp = tooltipMsg[i];
      if (tmp && tmp instanceof Array && tmp.length >= 2) {
        eval("window." + tmp[0] + "='" + tmp[1] + "'");
      }
    }
  }
}
function setTabTitle(skinObject, tab) {
  if (!skinObject || !skinObject.tabTitle) {
    return ;
  }
  var tabTitle = skinObject.tabTitle
  if (tabTitle  instanceof Array  && tabTitle.length <= tab.length) {
    for (var i = 0 ;i < tabTitle.length; i ++) {
      if (tabTitle[i]) {
        tab[i].title = tabTitle[i]; 
      }
    }
  }
}
function addTab(skinObject , tab) {
  if (!skinObject || !skinObject.addTab) {
    return ;
  }
  var tabs = skinObject.addTab;
  if (tabs  instanceof Array) {
    for (var i = 0 ;i < tabs.length; i ++) {
      if (tabs[i]) {
        insertTab(tabs[i] , tab);
      }
    }
  }
  
}
function sortTab(skinObject , tab) {
  if (!skinObject || !skinObject.sortTab) {
    return ;
  }
  var tabs = skinObject.sortTab;
  var tab2 = [];
  if (tabs instanceof Array) {
    for (var i = 0 ;i < tabs.length; i ++) {
      if (tabs[i]) {
        for (var j = 0 ;j < tab.length ;j++) {
          if (tab[j].id == tabs[i]) {
            alert(tab[j].id);
            tab2.push(tab[j]);
          }
        }
        
      }
    }
  }
  tab = tab2;
}
function insertTab(tab , tabs) {
 
  var index = tab.index;
  if (!index || index >= tabs.length) {
    tabs.push(tab);
    return;
  }
  for ( var i = tabs.length ;i > index ;i--) {
    tabs[i] = tabs[i - 1];
  }
  tabs[index] = tab;
}
function setSpanText(spanText) {
  if ( spanText) {
    for (var i = 0 ;i < spanText.length ;i++) {
      var tmp = spanText[i];
      if (tmp && tmp instanceof Array && tmp.length >= 2) {
        var ctrl = $(tmp[0]);
        if (ctrl) {
          ctrl.update(tmp[1]);
        }
      }
    }
  }
}
function loadScript(url) {
  var scriptText = getTextRs(url);
  evalFun(scriptText);
}
function evalFun(scriptText){
  if(window.execScript){
   window.execScript(scriptText);
  }else{
   window.eval(scriptText);
  }
}
