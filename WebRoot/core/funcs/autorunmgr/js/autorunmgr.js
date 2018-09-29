function checkServiceIsInValidity(cls){
  var url = contextPath + "/yh/core/funcs/autorunmgr/act/YHAutoRunManagerAct/checkClassIsInvalidity.act?cls=" + cls;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    if(rtJson.rtData.isIncalidity == "1"){
      return true;
    }
  }
  return false;
}

function bindHourAndMinSelect(hourCntrl,minCntrl){
  var hourHtml = ""; 
  var minHtml = ""; 
  for( var i = 0 ; i <= 59 ; i ++){
    
    var temp = "";
    if(i < 10){
      temp = "0" + i;
    }else{
      temp = i;
    }
    if( i <= 23){
      hourHtml += "<option value=\"" + temp + "\">" + temp + "</option>";
    }
    minHtml += "<option value=\"" + temp + "\">" + temp + "</option>";
  }
  $(minCntrl).insert(minHtml,"content");
  $(hourCntrl).insert(hourHtml,"content");
}

function reBindHourAndMinSelect(hourCntrl,minCntrl,runTime){
  var runTimeArray = runTime.split(":");
  if(runTimeArray.length < 2){
    return;
  }
  $(hourCntrl).value = runTimeArray[0];
  $(minCntrl).value = runTimeArray[1];
}