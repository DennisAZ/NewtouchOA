function checkForm(){
	var yearScore = $("yearScore").value;
	var monthScore = $("monthScore").value;
	var chiefScore = $("chiefScore").value;
	var checkScore = $("checkScore").value;
	var awardScore = $("awardScore").value;
	if(yearScore){
		if(!isNumbers(yearScore)){
			alert("年终考核分系数格式错误,应形如  10000.00");
			$("yearScore").focus();
			$("yearScore").select();  
			return false;
		}
	}
	if(monthScore){
		if(!isNumbers(monthScore)){
			alert("月考核平均分系数格式错误,应形如  10000.00");
			$("monthScore").focus();
			$("monthScore").select();  
			return false;
		}
	}
	if(chiefScore){
		if(!isNumbers(chiefScore)){
			alert("处长主观分系数格式错误,应形如  10000.00");
			$("chiefScore").focus();
			$("chiefScore").select();  
			return false;
		}
	}
	if(checkScore){
		if(!isNumbers(checkScore)){
			alert("考勤分数系数格式错误,应形如  10000.00");
			$("checkScore").focus();
			$("checkScore").select();  
			return false;
		}
	}
	if(awardScore){
		if(!isNumbers(awardScore)){
			alert("奖惩分系数格式错误,应形如  10000.00");
			$("awardScore").focus();
			$("awardScore").select();  
			return false;
		}
	}
	return true;
}
/**
 * 判断小数位后２位
 * @param aValue
 * @return
 */
function isNumbers(aValue) { 
  var digitSrc = "0123456789"; 
  aValue = "" + aValue; 
  if (aValue.substr(0, 1) == "-") { 
    aValue = aValue.substr(1, aValue.length - 1); 
  } 
  var strArray = aValue.split("."); 
  // 含有多个“.” 
  if (strArray.length > 2) { 
    return false; 
  } 
  var tmpStr = ""; 
  for (var i = 0; i < strArray.length; i++) { 
    tmpStr += strArray[i]; 
  } 
  for (var i = 0; i < tmpStr.length; i++) { 
    var tmpIndex = digitSrc.indexOf(tmpStr.charAt(i)); 
    if (tmpIndex < 0) { 
  // 有字符不是数字 
      return false; 
    } 
  } 
  if(aValue.indexOf(".") != -1){
    var str = aValue.substr(aValue.indexOf(".")+1, aValue.length-1);
    if(str.length > 2){
      return false;
    }
    if(str.length == 0){
      return false;
    }
  }
  return true;
}