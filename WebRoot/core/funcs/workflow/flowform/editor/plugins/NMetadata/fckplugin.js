//得到控件对象
var FCKObject = plugins({comName:"NMetadata"
  ,item:"NMetadata"
  ,page:"nmetadata.jsp"
  ,width:350
  ,height:200
  ,dlgTitle:"配置元数据"
  ,btnTitle:"配置元数据"});
//为控件对象注测行为

//注册add 方法
function addMetadata(code){
  //FCK.InsertHtml(code);
}
FCKObject.RegisterAction('Add',addMetadata);
