
//日期
function setDate() {
	var date1Parameters = {
		inputId : 'filterDateTime1',
		property : {isHaveTime : true	},
		bindToBtn : 'date1'
	};
	new Calendar(date1Parameters);
	
	var date2Parameters = {
			inputId : 'nextDateTime1',
			property : {isHaveTime : true},
			bindToBtn : 'date2'
	};
	new Calendar(date2Parameters);
	
	
	var date3Parameters = {
			inputId : 'dbFilterDateTime2',
			property : {isHaveTime : true},
			bindToBtn : 'date3'
	};
	new Calendar(date3Parameters);
	
	var date4Parameters = {
			inputId : 'dbNextDateTime2',
			property : {isHaveTime : true},
			bindToBtn : 'date4'
	};
	new Calendar(date4Parameters);
	
	var date5Parameters = {
			inputId : 'dbFilterDateTime3',
			property : {isHaveTime : true},
			bindToBtn : 'date5'
	};
	new Calendar(date5Parameters);
	
	var date6Parameters = {
			inputId : 'dbNextDateTime3',
			property : {isHaveTime : true},
			bindToBtn : 'date6'
	};
	new Calendar(date6Parameters);
	
	var date7Parameters = {
			inputId : 'dbFilterDateTime4',
			property : {isHaveTime : true},
			bindToBtn : 'date7'
	};
	new Calendar(date7Parameters);
	
	
}
function clearValue(str) {
	if (str) {
		str.value = "";
	}
}
// 设置为当前日期时间
function resetEndTime(thisDateTimeId) {
	var date = new Date();
	var y = date.getFullYear();
	var m = date.getMonth() + 1;
	m = (m > 9) ? m : '0' + m;
	var d = date.getDate();
	d = (d > 9) ? d : '0' + d;
	var time = date.toLocaleTimeString();
	$(thisDateTimeId).value = y + '-' + m + '-' + d + ' ' + time;
}

/**
 * 获取下拉框选项 getSecretFlag("T_COURSE_TYPE","tCourseTypes");
 * 
 * @param parentNo
 *          代码编号
 * @param optionType
 *          绑定的div
 * @param extValue
 *          要选中的值 s *
 * @return
 */
function getSelectedCode(parentNo, optionType, extValue) {
	var requestURLStr = contextPath
			+ "/yh/subsys/oa/hr/setting/act/YHHrCodeAct/selectChildCode.act?parentNo="
			+ parentNo;
	var rtJson = getJsonRs(requestURLStr);
	// alert(rsText);
	if (rtJson.rtState == "1") {
		alert(rtJson.rtMsrg);
		return;
	}
	var prcs = rtJson.rtData;
	var selects = document.getElementById(optionType);
	for ( var i = 0; i < prcs.length; i++) {
		var prc = prcs[i];
		var option = document.createElement("option");
		var codeNo = prc.codeNo;
		option.value = prc.seqId;
		option.innerHTML = prc.codeName;
		selects.appendChild(option);
		if (extValue && (extValue == codeNo)) {
			option.selected = true;
		}
	}
}
/**
 * 获取代码名称
 * @param seqId
 * @return
 */
function selectCodeById(seqId) {
	var str = "";
	var requestURLStr = contextPath + "/yh/subsys/oa/hr/setting/act/YHHrCodeAct/selectCodeById.act?seqId=" + seqId;
	var rtJson = getJsonRs(requestURLStr);
	//alert(rsText);
	if (rtJson.rtState == "1") {
		alert(rtJson.rtMsrg);
		return;
	}
	var prcs = rtJson.rtData;
	if(prcs.codeName){
		str = prcs.codeName;
	}
	return str;
}
function next_play3(STEP_FLAG) {
	if (STEP_FLAG == 1) {
		if (document.form1.PASS_OR_NOT1.value == 0) {
			document.getElementById("NEXT1").style.display = 'none';
			document.getElementById("BTN_NEXT").style.display = 'none';
		} else {
			document.getElementById("NEXT1").style.display = '';
			document.getElementById("BTN_NEXT").style.display = '';
		}
	}
	if (STEP_FLAG == 2) {
		if (document.form1.PASS_OR_NOT2.value == 0) {
			document.getElementById("NEXT2").style.display = 'none';
			document.getElementById("BTN_NEXT").style.display = 'none';
		} else {
			document.getElementById("NEXT2").style.display = '';
			document.getElementById("BTN_NEXT").style.display = '';
		}

	}
	if (STEP_FLAG == 3) {
		if (document.form1.PASS_OR_NOT3.value == 0) {
			document.getElementById("NEXT3").style.display = 'none';
			document.getElementById("BTN_NEXT").style.display = 'none';
		} else {
			document.getElementById("NEXT3").style.display = '';
			document.getElementById("BTN_NEXT").style.display = '';
		}
	}
}




