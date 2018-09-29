var userRetNameArray = null;
var deptRetNameArray = null;
var roleRetNameArray = null;
var userExternalRetNameArray = null;
/**
 * 选择人员
 * @return
 */
function selectUser(retArray , moduleId,privNoFlag , notLoginIn) {
  userRetNameArray = retArray;
  var url = contextPath + "/core/funcs/orgselect/MultiUserSelect.jsp?1=1" ;
  if (moduleId) {
    url += "&moduleId=" + moduleId;
    if (!privNoFlag) {
      privNoFlag = 0;
    }
    url += "&privNoFlag=" + privNoFlag;
  }
  if (notLoginIn) {
    url += "&notLoginIn=" + notLoginIn;
  }
  openDialogResize(url,  520, 400);
}
/**
 * 选择单人员
 * @return
 */
function selectSingleUser(retArray,moduleId,privNoFlag, notLoginIn) {
  userRetNameArray = retArray;
  var url = contextPath + "/core/funcs/orgselect/MultiUserSelect.jsp?isSingle=true"
  if (moduleId) {
    url += "&moduleId=" + moduleId;
    if (!privNoFlag) {
      privNoFlag = 0;
    }
    url += "&privNoFlag=" + privNoFlag;
  }
  if (notLoginIn) {
    url += "&notLoginIn=" + notLoginIn;
  }
  openDialogResize(url , 530, 400);
}
/**
 * 选择部门
 * @return
 */
function selectDept(retArray , moduleId,privNoFlag , noAllDept) {
  deptRetNameArray = retArray;
  var url = contextPath + "/core/funcs/orgselect/MultiDeptSelect.jsp?1=1";
  var has = false;
  if (moduleId) {
    url += "&moduleId=" + moduleId;
  }
  if (privNoFlag) {
    url += "&privNoFlag=" + privNoFlag;
  }
  if (noAllDept) {
    url += "&noAllDept=" + noAllDept;
  }
  openDialogResize(url, 530, 400);
}
/**
 * 选择角色
 * @return
 */
function selectRole(retArray, moduleId,privNoFlag , privOp) {
  roleRetNameArray = retArray;
  var url =contextPath + "/core/funcs/orgselect/MultiRoleSelect.jsp";
  var has = false;
  if (moduleId) {
    url += "?moduleId=" + moduleId;
    if (!privNoFlag) {
      privNoFlag = 0;
    }
    url += "&privNoFlag=" + privNoFlag;
    has = true;
  }
  if (privOp) {
    if (has) {
      url += "&privOp=" + privOp; 
    } else {
      url += "?privOp=" + privOp; 
    }
  }
  openDialogResize(url , 280, 400);
}
/**
 * 选择离职人员
 * @return
 */
function selectUserExternalSelect(retArray) {
  userExternalRetNameArray = retArray;
  openDialogResize(contextPath + "/core/funcs/orgselect/MultiUserExternalSelect.jsp", 520, 400);
}

/**
 * 选择部门
 * @return
 */
function selectOutDept2(retArray) {
  deptRetNameArray = retArray;
  var url = contextPath + "/core/esb/client/org/MultiDeptSelect2.jsp?1=1";
  openDialogResize(url, 750, 500);
}

/**
 * 选择部门--有权限
 * @return
 */
function selectOutDept(retArray) {
  deptRetNameArray = retArray;
  var url = contextPath + "/core/esb/client/org/MultiDeptSelect.jsp?1=1";
  openDialogResize(url, 750, 500);
}