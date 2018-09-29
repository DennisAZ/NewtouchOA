var staffInfoURL = contextPath + "/yh/subsys/oa/hr/recruit/recruitment/act/YHHrRecruitRecruitmentAct";
/**
 * 获取下拉框选项--获取角色名称 getSecretFlag("T_COURSE_TYPE","tCourseTypes"); *
 * 
 * @param optionType
 *          绑定的div
 * @param extValue
 *          要选中的值
 * 
 * @return
 */
function getUserPrivName(optionType, extValue) {
	
	var requestURLStr = staffInfoURL + "/getUserPriv.act";
	var rtJson = getJsonRs(requestURLStr);
	if (rtJson.rtState == "1") {
		alert(rtJson.rtMsrg);
		return;
	}
	//alert(rsText);
	var prcs = rtJson.rtData;
	var selects = document.getElementById(optionType);
	for ( var i = 0; i < prcs.length; i++) {
		var prc = prcs[i];
		var option = document.createElement("option");
		option.value = prc.seqId;
		option.innerHTML = prc.privName;
		selects.appendChild(option);
		if (extValue && (extValue == prc.seqId)) {
			option.selected = true;
		}
	}
}
/**
 * 是否允许登录
 * 
 * @return
 */
function getHrSetUserLogin() {
	var url = staffInfoURL + "/getHrSetUserLogin.act";
	var rtJson = getJsonRs(url);
	if (rtJson.rtState == "0") {
		if (rtJson.rtData == 1) {
			document.getElementById("yesOrNotStr").checked = true;
			document.getElementById("yesOrNot").value = "on";
		} else if (rtJson.rtData == 0) {
			document.getElementById("yesOrNotStr").disabled = true;
		}
	} else {
		alert(rtJson.rtMsrg);
	}
}
/**
 * 获取下拉框选项 getSecretFlag("T_COURSE_TYPE","tCourseTypes");
 * 
 * @param parentNo
 *          代码编号
 * @param optionType
 *          绑定的div
 * @param extValue
 *          要选中的值
 * 
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
		option.value = prc.seqId;
		option.innerHTML = prc.codeName;
		selects.appendChild(option);
		if (extValue && (extValue == prc.value)) {
			option.selected = true;
		}
	}
}

/**
 * 验证身份证号（自动计算出生日期、年龄、性别）
 * @param staffCardNo 身份证号
 * @param staffBirthDiv 日期Div
 * @param staffAgeDiv 年龄Div
 * @param staffSexDiv 性别Div
 * @return
 */
function checkIdcard(staffCardNo, staffBirthDiv, staffAgeDiv, staffSexDiv) {
	var staffBirth = staffBirthDiv;
	var staffAge = staffAgeDiv;
	var staffSex = staffSexDiv;
	if (document.getElementById(staffCardNo).value != "") {
		var idcard = document.getElementById(staffCardNo).value;
		var Errors = new Array("身份证号码位数不对!", "身份证号码出生日期超出范围或含有非法字符!", "身份证号码校验错误!",
				"身份证地区错误，请重新输入!");
		var area = {
			11 : "北京",
			12 : "天津",
			13 : "河北",
			14 : "山西",
			15 : "内蒙古",
			21 : "辽宁",
			22 : "吉林",
			23 : "黑龙江",
			31 : "南京",
			32 : "江苏",
			33 : "浙江",
			34 : "安徽",
			35 : "福建",
			36 : "江西",
			37 : "山东",
			41 : "河南",
			42 : "湖北",
			43 : "湖南",
			44 : "广东",
			45 : "广西",
			46 : "海南",
			50 : "重庆",
			51 : "四川",
			52 : "贵州",
			53 : "云南",
			54 : "西藏",
			61 : "陕西",
			62 : "甘肃",
			63 : "青海",
			64 : "宁夏",
			65 : "新疆",
			71 : "台湾",
			81 : "香港",
			82 : "澳门",
			91 : "国外"
		}
		var idcard, Y, JYM;
		var S, M;
		var idcard_array = new Array();
		idcard_array = idcard.split("");
		if (area[parseInt(idcard.substr(0, 2))] == null) {
			alert(Errors[3]);
			document.getElementById(staffCardNo).focus();
			return (false);
		}

		switch (idcard.length) {
		case 15:
			if ((parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0
					|| ((parseInt(idcard.substr(6, 2)) + 1900) % 100 == 0 && (parseInt(idcard
							.substr(6, 2)) + 1900) % 4 == 0)) {
				ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;
			} else {
				ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;
			}
			if (!ereg.test(idcard)) {
				alert(Errors[1]);
				document.getElementById(staffCardNo).focus();
				return (false);
			} else {
				var birth = (parseInt(idcard.substr(6, 2)) + 1900).toString() + "-"
						+ idcard.substr(8, 2) + "-" + idcard.substr(10, 2);
				document.getElementById(staffBirth).value = birth;
				var myDate = new Date();
				var age = myDate.getYear() - (parseInt(idcard.substr(6, 2)) + 1900) - 1;
				document.getElementById(staffAge).value = age;
				var sex = parseInt(idcard.substr(14, 1));
				if (sex % 2 == 1) // 男					document.getElementById(staffSex).value = "0";
				else
					// 女					document.getElementById(staffSex).value = "1";
			}
			break;
		case 18:
			if (parseInt(idcard.substr(6, 4)) % 4 == 0
					|| (parseInt(idcard.substr(6, 4)) % 100 == 0 && parseInt(idcard
							.substr(6, 4)) % 4 == 0)) {
				ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;// 闰年出生日期的合法性正则表达式
			} else {
				ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;
			}
			if (ereg.test(idcard)) {
				S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10])) * 7
						+ (parseInt(idcard_array[1]) + parseInt(idcard_array[11])) * 9
						+ (parseInt(idcard_array[2]) + parseInt(idcard_array[12])) * 10
						+ (parseInt(idcard_array[3]) + parseInt(idcard_array[13])) * 5
						+ (parseInt(idcard_array[4]) + parseInt(idcard_array[14])) * 8
						+ (parseInt(idcard_array[5]) + parseInt(idcard_array[15])) * 4
						+ (parseInt(idcard_array[6]) + parseInt(idcard_array[16])) * 2
						+ parseInt(idcard_array[7]) * 1 + parseInt(idcard_array[8]) * 6
						+ parseInt(idcard_array[9]) * 3;
				Y = S % 11;
				M = "F";
				JYM = "10X98765432";
				M = JYM.substr(Y, 1);
				if (!(M == idcard_array[17])) {
					alert(Errors[2]);
					var birth = idcard.substr(6, 4) + "-" + idcard.substr(10, 2) + "-"
							+ idcard.substr(12, 2);
					document.getElementById(staffBirth).value = birth;
					var myDate = new Date();
					var age = myDate.getFullYear() - idcard.substr(6, 4) - 1;
					document.getElementById(staffAge).value = age;
					var sex = parseInt(idcard.substr(16, 1));
					if (sex % 2 == 1) // 男
						document.getElementById(staffSex).value = "0";
					else
						// 女						document.getElementById(staffSex).value = "1";
					/*
					 * document.form1.STAFF_CARD_NO.focus(); return (false);
					 */
				} else {
					var birth = idcard.substr(6, 4) + "-" + idcard.substr(10, 2) + "-"
							+ idcard.substr(12, 2);
					document.getElementById(staffBirth).value = birth;
					var myDate = new Date();
					var age = myDate.getFullYear() - idcard.substr(6, 4) - 1;
					document.getElementById(staffAge).value = age;
					var sex = parseInt(idcard.substr(16, 1));
					if (sex % 2 == 1) // 男
						document.getElementById(staffSex).value = "0";
					else
						// 女
						document.getElementById(staffSex).value = "1";
				}
			} else {
				alert(Errors[1]);
				document.getElementById(staffCardNo).focus();
				return (false);
			}
			break;
		default:
			alert(Errors[0]);
			document.getElementById(staffCardNo).focus();
			return (false);
			break;
		}
	}
}

//获取全体部门列表
function getDeptFunc(deptDiv){
  var url = staffInfoURL + "/selectDeptToAttendance.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var userId = rtJson.rtMsrg;
  var prcs = rtJson.rtData;
  var selects = document.getElementById(deptDiv);
  for(var i=0;i<prcs.length;i++){
    var prc = prcs[i];
    var option = document.createElement("option"); 
    option.value = prc.value; 
    option.innerHTML = prc.text; 
    selects.appendChild(option);
  }
  return userId;
}

