/**
 * 授权范围：（人员)
 * @param seqId
 * @param action
 * @return
 */
var jsRequestURL = contextPath + "/yh/subsys/oa/confidentialFile/act/YHSetConfidentialSortAct";
function getUser(seqId,action){
	var prcsJson; 
	var url = jsRequestURL + "/getPersonNameStr.act?seqId=" + seqId + "&action="+action;		
	var json = getJsonRs(url);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  }
  prcsJson = json.rtData;
  if(prcsJson.user != "null"){
    document.getElementById("user").value = prcsJson.user;
    document.getElementById("userDesc").value = prcsJson.userDesc;
  }
}
/**
 * 授权范围：（部门)
 * @param seqId
 * @param action
 * @return
 */
function getDept(seqId,action){
	var prcsJson; 
	var url = jsRequestURL + "/getDeptNameStr.act?seqId="+ seqId + "&action="+action;		
	var json = getJsonRs(url);
	//alert(rsText);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  }
  prcsJson = json.rtData;
  if(prcsJson.user != "null"){
    document.getElementById("dept").value = prcsJson.dept;
    var deptDesc = prcsJson.deptDesc; 
    if(prcsJson.dept=="0" ||  prcsJson.dept=="ALL_DEPT"){ 
    deptDesc = "全体部门"; 
    } 
    document.getElementById("deptDesc").value = deptDesc;
  }
}
/**
 * 授权范围：（角色)
 * @param seqId
 * @param action
 * @return
 */
function getRole(seqId,action){
	var prcsJson; 
	var url = jsRequestURL + "/getRoleNameStr.act?seqId=" + seqId + "&action="+action;		
	var json = getJsonRs(url);
	//alert(rsText);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  }
  prcsJson = json.rtData;
  if(prcsJson.user != "null"){
    document.getElementById("role").value = prcsJson.role;
    document.getElementById("roleDesc").value = prcsJson.roleDesc;
  }
}


