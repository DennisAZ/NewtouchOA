function click_user(userName) {
  var to_name = TO_NAME;
  var parentWindowObj = window.parent.dialogArguments;
  var pramValue = parentWindowObj.document.getElementById(to_name).value;
  
  var ids = pram.split(',');
  var table = document.getElementById("tbody");
  alert("abc");
  var td = document.createElement("td");
  var userTable = document.getElementById("userTable");
  tr.id = "Divtr";
 
  tr.appendChild(td);
  tr.className = "TableControl";
  td.innerHTML = "全部添加";
  input.onclick = function() {
    add_all();
  }
  table.appendChild(tr);

  var tr = document.createElement("tr");
  tr.id = "Divtr1";
  tr.appendChild(td);
  tr.className = "TableControl";
  td.innerHTML = "全部删除";
  input.onclick = function() {
    del_all();
  }
  table.appendChild(tr);

  var tr = document.createElement("tr");
  for(var i=0; i<ids.length; i++){
    tr.id = "userDiv";
    tr.appendChild(td);
    tr.className = "TableControl";
    td.appendChild(ids[i]);
    table.appendChild(tr);
  }
}

function clickuser(user_id) {
  /*TO_VAL = to_id.value;
  TO_NAME = to_name.value;
  targetelement = $(user_id);
  user_name = targetelement.title;
  if (TO_VAL.indexOf("," + user_id + ",") > 0
      || TO_VAL.indexOf(user_id + ",") == 0) {
    if (TO_VAL.indexOf(user_id + ",") == 0)
      to_id.value = to_id.value.replace(user_id + ",", "");
    else if (TO_VAL.indexOf("," + user_id + ",") > 0)
      to_id.value = to_id.value.replace("," + user_id + ",", ",");

    if (TO_NAME.indexOf(user_name + ",") == 0)
      to_name.value = to_name.value.replace(user_name + ",", "");
    else if (TO_NAME.indexOf("," + user_name + ",") > 0)
      to_name.value = to_name.value.replace("," + user_name + ",", ",");

    borderize_off(targetelement);
  } else {
    to_id.value += user_id + ",";
    to_name.value += user_name + ",";
    borderize_on(targetelement);
  }
*/
}