			function add(){
				
				var id = document.getElementById("count");
				id.value = parseInt(id.value)+1;
				var table = document.getElementById("tbody");
				var tr = document.createElement("tr");
				var td =  document.createElement("td");
				
				var input = document.createElement("input");
				input.id = "fieldNo_"+id.value;
				input.setAttribute("type","text");
				input.setAttribute("id","fieldNo_"+id.value);
				input.setAttribute("size","7");
				input.setAttribute("name","fieldNo_"+id.value);
				input.setAttribute("value","");
				td.appendChild(input);
				tr.appendChild(td);
				

				td = document.createElement("td");
				var input = document.createElement("input");
				input.id = "fieldName_"+id.value;
				input.setAttribute("type","text");
				input.setAttribute("id","fieldName_"+id.value);
				input.setAttribute("size","7");
				input.setAttribute("name","fieldName_"+id.value);
				input.setAttribute("value","");
				td.appendChild(input);
				tr.appendChild(td);
				
				
				td = document.createElement("td");
				var input =  document.createElement("input");
				input.id = "fieldDesc_"+id.value;
				input.setAttribute("type","text");
				input.setAttribute("id","fieldDesc_"+id.value);
				input.setAttribute("size","7");
				input.setAttribute("name","fieldDesc_"+id.value);
				input.setAttribute("value","");
				td.appendChild(input);
				tr.appendChild(td);
				
				
				td = document.createElement("td");
				var input =  document.createElement("input");
				input.id = "button"+id.value;
				
				input.setAttribute("type","button");
				input.setAttribute("name","button");
				input.setAttribute("value","详情");
				input.onclick=function(){
					var id1 = this.id.substr(6);
				    show(id1);  
				}
				td.appendChild(input);
				
				
				input = document.createElement("input");
				input.id = "fkTableNo_"+id.value;
				input.setAttribute("type","hidden");
				input.setAttribute("name","fkTableNo_"+id.value);
				input.setAttribute("id","fkTableNo_"+id.value);
				input.setAttribute("value","");
				td.appendChild(input);
				
				
				
				input = document.createElement("input");
				input.setAttribute("type","hidden");
				input.setAttribute("name","fkTableNo2_"+id.value);
				input.setAttribute("id","fkTableNo2_"+id.value);
				input.setAttribute("value","");
				td.appendChild(input);
				
				
				
				input = document.createElement("input");
				input.setAttribute("type","hidden");
				input.setAttribute("name","fkRelaFieldNo_"+id.value);
				input.setAttribute("id","fkRelaFieldNo_"+id.value);
				input.setAttribute("value","");
				td.appendChild(input);
				
				
				
				input = document.createElement("input");
				input.setAttribute("type","hidden");
				input.setAttribute("name","fkNameFieldNo_"+id.value);
				input.setAttribute("id","fkNameFieldNo_"+id.value);
				input.setAttribute("value","");
				td.appendChild(input);
				
				
				
				input = document.createElement("input");
				input.setAttribute("type","hidden");
				input.setAttribute("name","fkFilter_"+id.value);
				input.setAttribute("id","fkFilter_"+id.value);
				input.setAttribute("value","");
				td.appendChild(input);
				
				
				input = document.createElement("input");
				input.setAttribute("type","hidden");
				input.setAttribute("name","codeClass_"+id.value);
				input.setAttribute("id","codeClass_"+id.value);
				input.setAttribute("value","");
				td.appendChild(input);
				
				
				
				input = document.createElement("input");
				input.setAttribute("type","hidden");
				input.setAttribute("name","defaultValue_"+id.value);
				input.setAttribute("id","defaultValue_"+id.value);
				input.setAttribute("value","");
				td.appendChild(input);
				
				
				
				input = document.createElement("input");
				input.setAttribute("type","hidden");
				input.setAttribute("name","formatMode_"+id.value);
				input.setAttribute("id","formatMode_"+id.value);
				input.setAttribute("value","number");
				td.appendChild(input);
				
				
				
			    input = document.createElement("input");
				input.setAttribute("type","hidden");
				input.setAttribute("name","formatRule_"+id.value);
				input.setAttribute("id","formatRule_"+id.value);
				input.setAttribute("value","");
				td.appendChild(input);
				
				
				input = document.createElement("input");
				input.setAttribute("type","hidden");
				input.setAttribute("name","errorMsrg_"+id.value);
				input.setAttribute("id","errorMsrg_"+id.value);
				input.setAttribute("value","");
				td.appendChild(input);
				
				
				
			    input = document.createElement("input");
				input.setAttribute("type","hidden");
				input.setAttribute("name","fieldPrecision_"+id.value);
				input.setAttribute("id","fieldPrecision_"+id.value);
				input.setAttribute("value","");
				td.appendChild(input);
				
				
				
				input = document.createElement("input");
				input.setAttribute("type","hidden");
				input.setAttribute("name","fieldScale_"+id.value);
				input.setAttribute("id","fieldScale_"+id.value);
				input.setAttribute("value","");
				td.appendChild(input);
				
				
				
				input = document.createElement("input");
				input.setAttribute("type","hidden");
				input.setAttribute("name","dataType_"+id.value);
				input.setAttribute("id","dataType_"+id.value);
				input.setAttribute("value","-7");
				td.appendChild(input);
				
				
				
				
				input = document.createElement("input");
				input.setAttribute("type","hidden");
				input.setAttribute("name","isPrimKey_"+id.value);
				input.setAttribute("id","isPrimKey_"+id.value);
				input.setAttribute("value","1");
				td.appendChild(input);
				
				
				
				input = document.createElement("input");
				input.setAttribute("type","hidden");
				input.setAttribute("name","isIdentity_"+id.value);
				input.setAttribute("id","isIdentity_"+id.value);
				input.setAttribute("value","1");
				td.appendChild(input);
				
				
				
				input = document.createElement("input");
				input.setAttribute("type","hidden");
				input.setAttribute("name","displayLen_"+id.value);
				input.setAttribute("id","displayLen_"+id.value);
				input.setAttribute("value","");
				td.appendChild(input);
				
				
				input = document.createElement("input");
				input.setAttribute("type","hidden");
				input.setAttribute("name","isMustFill_"+id.value);
				input.setAttribute("id","isMustFill_"+id.value);
				input.setAttribute("value","1");
				td.appendChild(input);
				
				tr.appendChild(td);
				table.appendChild(tr);
			}
			
			function show(id){
			
				var div = document.getElementById("table_div");
				//=document.getElementById("id");
				//var input = document.getElementById("");
				var divId = document.getElementById("divId");
				divId.value = id;
				var width = 480;
				var height = 310;
				div.style.left = (screen.width - width) / 2;
				div.style.top = (screen.height - height) / 2 - 150;
				div.style.width = width;
				div.style.height = height;
				div.style.zIndex = 100;				
				div.style.display="";
				document.getElementById("fieldNo").value = document.getElementById("fieldNo_"+id).value;
				var fieldNo = document.getElementById("fieldNo").value;
				
				document.getElementById("fieldName").value = document.getElementById("fieldName_"+id).value;
				var fieldName = document.getElementById("fieldName").value;
				
				document.getElementById("fieldDesc").value = document.getElementById("fieldDesc_"+id).value;
				var fieldDesc = document.getElementById("fieldDesc").value;
				
				document.getElementById("fkTableNo").value = document.getElementById("fkTableNo_"+id).value;
				var fkTableNo = document.getElementById("fkTableNo").value;
				
				document.getElementById("fkTableNo2").value = document.getElementById("fkTableNo2_"+id).value;
				var fkTableNo2 = document.getElementById("fkTableNo2").value;
				
				document.getElementById("fkRelaFieldNo").value = document.getElementById("fkRelaFieldNo_"+id).value;
				var fkRelaFieldNo = document.getElementById("fkRelaFieldNo").value;
				
				document.getElementById("fkNameFieldNo").value = document.getElementById("fkNameFieldNo_"+id).value;
				var fkNameFieldNo = document.getElementById("fkNameFieldNo").value;
				
				document.getElementById("fkFilter").value = document.getElementById("fkFilter_"+id).value;
				var fkFilter = document.getElementById("fkFilter").value;
				
				document.getElementById("codeClass").value = document.getElementById("codeClass_"+id).value;
				var codeClass = document.getElementById("codeClass").value;
				
				document.getElementById("defaultValue").value = document.getElementById("defaultValue_"+id).value;
				var defaultValue = document.getElementById("defaultValue").value;
				
				document.getElementById("formatRule").value = document.getElementById("formatRule_"+id).value;
				var formatRule = document.getElementById("formatRule").value;
				
				document.getElementById("errorMsrg").value = document.getElementById("errorMsrg_"+id).value;
				var errorMsrg = document.getElementById("errorMsrg").value;
				
				document.getElementById("fieldPrecision").value = document.getElementById("fieldPrecision_"+id).value;
				var fieldPrecision = document.getElementById("fieldPrecision").value;
				
				document.getElementById("fieldScale").value = document.getElementById("fieldScale_"+id).value;
				var fieldScale = document.getElementById("fieldScale").value;
				
				document.getElementById("displayLen").value = document.getElementById("displayLen_"+id).value;
				var displayLen = document.getElementById("displayLen").value;
				
				
				var select = document.getElementById("dataType");
				var option = select.getElementsByTagName("option");
				for(var i=0; i<option.length; i++){
					if(option[i].value== document.getElementById("dataType_"+id).value){
						option[i].selected=true;
					}
				}
				
				var selectisPrimKey = document.getElementById("isPrimKey");
				var option = selectisPrimKey.getElementsByTagName("option");
				for(var i=0; i<option.length; i++){
					if(option[i].value== document.getElementById("isPrimKey_"+id).value){
						option[i].selected=true;
					}
				}
				
				var selectisIdentity = document.getElementById("isIdentity");
				var option = selectisIdentity.getElementsByTagName("option");
				for(var i=0; i<option.length; i++){
					if(option[i].value== document.getElementById("isIdentity_"+id).value){
						option[i].selected=true;
					}
				}
				
				var selectisMustFill = document.getElementById("isMustFill");
				var option = selectisMustFill.getElementsByTagName("option");
				for(var i=0; i<option.length; i++){
					if(option[i].value== document.getElementById("isMustFill_"+id).value){
						option[i].selected=true;
					}
				}
				
				var selectformatMode = document.getElementById("formatMode");
				var option = selectformatMode.getElementsByTagName("option");
				for(var i=0; i<option.length; i++){
					if(option[i].value== document.getElementById("formatMode_"+id).value){
						option[i].selected=true;
					}
				}
			   
			}
			function save(){
			
			    var divId = document.getElementById("divId").value;
			    var reg=/^[0-9]*$/;
			    var fieldNo = document.getElementById("fieldNo");
				var fieldName = document.getElementById("fieldName");
				var fieldDesc = document.getElementById("fieldDesc");
				
				if(!reg.test(fieldNo.value)){
			        	alert("字段编码只能输入数字!");
			        	fieldNo.focus();
			        	return false;
			    	}
		            if((fieldNo.value.length == 0)||(fieldNo.value.length !=8 )){
						alert("字段编码输入长度为8位");
						fieldNo.focus();
						return false;
					}
					if((fieldName.value.length == 0)){
						alert("字段名称不能为空");
						fieldName.focus();
						return false;
					}
					if((fieldDesc.value.length == 0)){
						alert("字段描述不能为空");
						fieldDesc.focus();
						return false;
					}
			    
				document.getElementById("fieldNo_"+divId).value = document.getElementById("fieldNo").value;
				document.getElementById("fieldName_"+divId).value = document.getElementById("fieldName").value;
				document.getElementById("fieldDesc_"+divId).value = document.getElementById("fieldDesc").value;
				document.getElementById("fkTableNo_"+divId).value = document.getElementById("fkTableNo").value;
				document.getElementById("fkTableNo2_"+divId).value = document.getElementById("fkTableNo2").value;
				document.getElementById("fkRelaFieldNo_"+divId).value = document.getElementById("fkRelaFieldNo").value;
				document.getElementById("fkNameFieldNo_"+divId).value = document.getElementById("fkNameFieldNo").value;
				document.getElementById("fkFilter_"+divId).value = document.getElementById("fkFilter").value;
				document.getElementById("codeClass_"+divId).value = document.getElementById("codeClass").value;
				document.getElementById("defaultValue_"+divId).value = document.getElementById("defaultValue").value;
				document.getElementById("formatMode_"+divId).value = document.getElementById("formatMode").value;
				document.getElementById("formatRule_"+divId).value = document.getElementById("formatRule").value;
				document.getElementById("errorMsrg_"+divId).value = document.getElementById("errorMsrg").value;
				document.getElementById("fieldPrecision_"+divId).value = document.getElementById("fieldPrecision").value;
				document.getElementById("fieldScale_"+divId).value = document.getElementById("fieldScale").value;
				document.getElementById("dataType_"+divId).value = document.getElementById("dataType").value;
				document.getElementById("isPrimKey_"+divId).value = document.getElementById("isPrimKey").value;
				document.getElementById("isIdentity_"+divId).value = document.getElementById("isIdentity").value;
				document.getElementById("displayLen_"+divId).value = document.getElementById("displayLen").value;
				document.getElementById("isMustFill_"+divId).value = document.getElementById("isMustFill").value;
				
			    var select = document.getElementById("dataType");
				var option = select.getElementsByTagName("option");
				for(var i=0; i<option.length; i++){
					if(option[i].value== document.getElementById("dataType").value){
						option[i].selected=true;
					}
				}
				
				var selectisPrimKey = document.getElementById("isPrimKey");
				var option = selectisPrimKey.getElementsByTagName("option");
				for(var i=0; i<option.length; i++){
					if(option[i].value== document.getElementById("isPrimKey").value){
						option[i].selected=true;
					}
				}
				
				var selectisIdentity = document.getElementById("isIdentity");
				var option = selectisIdentity.getElementsByTagName("option");
				for(var i=0; i<option.length; i++){
					if(option[i].value== document.getElementById("isIdentity").value){
						option[i].selected=true;
					}
				}
				
				var selectisMustFill = document.getElementById("isMustFill");
				var option = selectisMustFill.getElementsByTagName("option");
				for(var i=0; i<option.length; i++){
					if(option[i].value== document.getElementById("isMustFill").value){
						option[i].selected=true;
					}
				}
				
				var selectformatMode = document.getElementById("formatMode");
				var option = selectformatMode.getElementsByTagName("option");
				for(var i=0; i<option.length; i++){
					if(option[i].value== document.getElementById("formatMode").value){
						option[i].selected=true;
					}
				}
				var div = document.getElementById("table_div");
				div.style.display="none";
			}
			
			function closeDiv(){
				var div = document.getElementById("table_div");
				div.style.display="none";
			}
			
			function check(){
				var id = document.getElementById("count");
				var reg=/^[0-9]*$/;
				var tableNo1 = document.dataList.tableNo1;
				var tableName = document.dataList.tableName;
				var tableDesc = document.dataList.tableDesc;
				var categoryNo = document.dataList.categoryNo;
				if(!reg.test(tableNo1.value)){
					alert("表编码请输入数字");
					tableNo1.focus();
					tableNo1.value="";
					return false;
				}
				if(!tableNo1.value.length || tableNo1.value.length != 5){
					alert("表编码输入长度为5位");
					tableNo1.focus();
					return false;
				}
				if(!tableName.value){
					alert("表名称不能为空");
					tableName.focus();
					return false;
				}
				if(!tableDesc.value){
					alert("表描述不能为空");
					tableDesc.focus();
					return false;
				}
				if(!categoryNo.value){
					alert("表类型不能为空");
					categoryNo.focus();
					return false;
				}
				
				
				for(var i=1; i<=parseInt(id.value); i++){
				
					//var tableNo = document.getElementById("tableNo_"+i);
					var fieldNo = document.getElementById("fieldNo_"+i);
					var fieldName = document.getElementById("fieldName_"+i);
					var fieldDesc = document.getElementById("fieldDesc_"+i);
					//alert(tableNo.value);
					
					
					if(!reg.test(fieldNo.value)){
			        	alert("字段编码只能输入数字!");
			        	fieldNo.focus();
			        	return false;
			    	}
		            if((fieldNo.value.length == 0)||(fieldNo.value.length !=8 )){
						alert("字段编码输入长度为8位");
						fieldNo.focus();
						return false;
					}
					if((fieldName.value.length == 0)){
						alert("字段名称不能为空");
						fieldName.focus();
						return false;
					}
					if((fieldDesc.value.length == 0)){
						alert("字段描述不能为空");
						fieldDesc.focus();
						return false;
					}
				
				}
		}