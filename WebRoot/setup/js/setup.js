var isErpInstalled = false;
var isEaInstalled = false;
var isFisInstalled = false;
/**
 * 安装YHerp运行平台
 * @return
 */
function installErp(from,temp){
  var url = contextPath + "/yh/setup/erp/act/YHERPSetupAct/setup.act";
  //正在安装Erp运行平台
  getJsonRsAsyn(url,$(from).serialize(),callErpFunc,true);
  $("erpInstallDesc").innerHTML = temp;
}

function installFisAsyn(from,temp){
  if(isErpInstalled){
    installFis(from,temp);
  }else{
    setTimeout("installFisAsyn('" + from + "','" + temp + "')", 100);
  }
}

function installEaAsyn(from,temp){
  //var from = "from1";
  if(isErpInstalled && isFisInstalled){
    installEa(from,temp);
  }else{
    setTimeout("installEaAsyn('" + from + "','" + temp + "')", 100);
  }
}
/**
 * 安装YHerp财务系统
 * @return
 */
function installFis(from,temp){
  var url = contextPath + "/yh/setup/fis/act/YHFisSetupAct/setup.act";
  //正在安装ERP财务系统
  //getJsonRs(url,$(from).serialize());
  getJsonRsAsyn(url,$(from).serialize(),callFisFunc,true);
  $("fisInstallDesc").innerHTML = temp;
}
/**
 * 安装YHerp进销存系统
 * @return
 */
function installEa(from,temp){
  var url = contextPath + "/yh/setup/ea/act/YHEaSetupAct/setup.act";
  //正在安装ERP进销存系统
  getJsonRsAsyn(url,$(from).serialize(),callEaFunc,true);
  $("eaInstallDesc").innerHTML = temp;
}
function callFisFunc(rtJson){
  if(rtJson.rtState == "0"){
    $("fis").checked = false;
    $("fis").style.display = "none";
    $("fisInfo").color = "green";
    $("fisInstallDesc").innerHTML = "安装完成!";
    isFisInstalled = true;
  }else{
    $("fisInstallDesc").innerHTML = "安装失败!";
  }
}

function callEaFunc(rtJson){
  if(rtJson.rtState == "0"){
    $("ea").checked = false;
    $("ea").style.display = "none";
    $("eaInfo").color = "green";
    $("eaInstallDesc").innerHTML = "安装完成!";
    isEaInstalled = true;
  }else{
    $("eaInstallDesc").innerHTML = "安装失败!";
  }
}

function callErpFunc(rtJson){
  if(rtJson.rtState == "0"){
    $("erp").checked = false;
    $("erp").style.display = "none";
    $("erpInfo").color = "green";
    $("erpInstallDesc").innerHTML = "安装完成!";
    isErpInstalled = true;
  }else{
    $("erpInstallDesc").innerHTML = "安装失败!";
  }
}

/**
 * 查找还没有安装的系统
 * @return
 */
function findNotInstallSys(){
  var url = contextPath + "/yh/setup/act/YHSetupUtilAct/findNotStalledSys.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    return rtJson.rtData;
  }else{
    alert(rtJson.rtMsrg);
  }
}