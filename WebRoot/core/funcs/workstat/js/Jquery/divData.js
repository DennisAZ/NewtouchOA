var loadType=0;
var minNum=0;
var loadNum=60;
var addNum=30;//异步加载数目
jQuery(window).scroll(function(){
	
    if((jQuery(window).scrollTop()+jQuery(window).height())>=(jQuery(document).height())){
    	showGif();
    	if(loadType==1){
    		 minNum=loadNum;
    		 loadNum=loadNum+addNum;
    		 setTimeout('moreLoadDataAdd()',500);
    		//moreLoadDataAdd();
    		
    	}
    	else if(loadType==2){
    		 minNum=loadNum;
    		 loadNum=loadNum+addNum;
    		 setTimeout('deptLoadDataAdd();',500);
    		// deptLoadDataAdd();
    		
    	}
    	setTimeout('hideGif()',500);
    }
});


function reSetNum(){
	loadType=0;
	minNum=0;
	loadNum=60;
	addNum=30;
}

function moreLoadDataAdd(){
	var deptId = $("deptId").value;
	var startDate = $("startDate").value;
	var endDate = $("endDate").value;
	var deptMore = $("requDept").value;

	var param = "deptId=" + deptId + "&startDate=" + startDate + "&endDate="
			+ endDate + "&deptMore=" + deptMore+"&minNum="+minNum+"&maxNum="+loadNum;

	var url = contextPath
			+ "/yh/core/funcs/workstat/act/YHWorkStatAct/getDataAct.act";
	var rtJson = getJsonRs(url, param);
	if (rtJson.rtState == "0") {
		var data = rtJson.rtData;
		tableAddChildren(data);	
	}
		
}

function deptLoadDataAdd(){
	loadType=2;
	var deptId = $("deptId").value;
	var startDate = $("startDate").value;
	var endDate = $("endDate").value;
	var deptMore = "";
	var param = "deptId=" + deptId + "&startDate=" + startDate + "&endDate="
			+ endDate + "&deptMore=" + deptMore+"&minNum="+minNum+"&maxNum="+loadNum;

	var url = contextPath
			+ "/yh/core/funcs/workstat/act/YHWorkStatAct/getDataAct.act";
	var rtJson = getJsonRs(url, param);
	if (rtJson.rtState == "0") {
		var data = rtJson.rtData;
		tableAddChildren(data);
	}
}
	

function tableAddChildren( data ){
	for ( var i = 0; i < data.userData.length; i++) {
		// ///////转换data成url
		var emailIn = (data.userData[i].emailIn == '0' ? ""
				: data.userData[i].emailIn);
		var emailOut = (data.userData[i].emailOut == '0' ? ""
				: data.userData[i].emailOut);
		var notify = (data.userData[i].notify == '0' ? ""
				: data.userData[i].notify);
		var news = (data.userData[i].news == '0' ? ""
				: data.userData[i].news);
		var calendarFinish = (data.userData[i].calendarFinish == '0' ? ""
				: "<a href=javascript:windowOpen(0,'"
						+ data.userData[i].userId + "','" + data.startDate
						+ "','" + data.endDate
						+ "','calendar_detail.jsp')>"
						+ data.userData[i].calendarFinish + "</a>");
		var calendarAll = (data.userData[i].calendarAll == '0' ? ""
				: "<a href=javascript:windowOpen(0,'"
						+ data.userData[i].userId + "','" + data.startDate
						+ "','" + data.endDate
						+ "','calendar1_detail.jsp')>"
						+ data.userData[i].calendarAll + "</a>");
		var diary = (data.userData[i].diary == '0' ? ""
				: "<a href=javascript:windowOpen(1,'"
						+ data.userData[i].userId + "','" + data.startDate
						+ "','" + data.endDate + "','diary.jsp')>"
						+ data.userData[i].diary + "</a>");
		var workFlowDeelAll = (data.userData[i].workFlowDeelAll == '0' ? ""
				: "<a href=javascript:windowOpen(2,'"
						+ data.userData[i].userId + "','" + data.startDate
						+ "','" + data.endDate + "','workflow_op1.jsp')>"
						+ data.userData[i].workFlowDeelAll + "</a>");
		var workFlowDeelFinish = (data.userData[i].workFlowDeelFinish == '0' ? ""
				: "<a href=javascript:windowOpen(2,'"
						+ data.userData[i].userId + "','" + data.startDate
						+ "','" + data.endDate + "','workflow_op.jsp')>"
						+ data.userData[i].workFlowDeelFinish + "</a>");
		var workFlowSignFinish = (data.userData[i].workFlowSignFinish == '0' ? ""
				: "<a href=javascript:windowOpen(3,'"
						+ data.userData[i].userId + "','" + data.startDate
						+ "','" + data.endDate + "','workflow_sign.jsp')>"
						+ data.userData[i].workFlowSignFinish + "</a>");
		var workFlowSignAll = (data.userData[i].workFlowSignAll == '0' ? ""
				: "<a href=javascript:windowOpen(3,'"
						+ data.userData[i].userId + "','" + data.startDate
						+ "','" + data.endDate + "','workflow_sign1.jsp')>"
						+ data.userData[i].workFlowSignAll + "</a>");

		// ///
		var tr = new Element('tr', {
			"class" : "TableData"
		});
		$('tboday').appendChild(tr);
		tr.update("<td align='center'>" + data.userData[i].deptName
				+ "</td>" + "<td align='center'>"
				+ data.userData[i].userName + "</td>"
				+ "<td align='center'>" + emailIn + "</td>"
				+ "<td align='center'>" + emailOut + "</td>"
				+ "<td align='center'>" + calendarFinish + "</td>"
				+ "<td align='center'>" + calendarAll + "</td>"
				+ "<td align='center'>" + diary + "</td>"
				+ "<td align='center'>" + workFlowDeelFinish + "</td>"
				+ "<td align='center'>" + workFlowDeelAll + "</td>"
				+ "<td align='center'>" + workFlowSignFinish + "</td>"
				+ "<td align='center'>" + workFlowSignAll + "</td>"
				+ "<td align='center'>" + notify + "</td>"
				+ "<td align='center'>" + news + "</td>");

	}


}

function showGif(){
	var heigth=jQuery(document).height();
	var width=jQuery(document).width();
	jQuery("#getingData").show();
	
}
function hideGif(){
	jQuery("#getingData").hide();
}
