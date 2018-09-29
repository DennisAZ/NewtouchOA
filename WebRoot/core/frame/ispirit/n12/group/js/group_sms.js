
//发送群消息
function sendGroupSMS(msgGroupID,content)
{
   document.getElementById("MSG_GROUP_ID").value = msgGroupID;
   document.getElementById("MSG_CONTENT").value = content;
   //document.form1.MSG_GROUP_ID.value= msgGroupID;
   //document.form1.MSG_CONTENT.value= content;
   document.form1.submit();
   appendMsg(userName, new Date().toLocaleTimeString(), content);
}

function sendFile(file_path)
{
   document.form1.submit();
}

function imgClick(theVar)
{

}

function imgErr(theVar)
{

}