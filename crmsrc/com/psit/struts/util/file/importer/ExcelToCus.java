package com.psit.struts.util.file.importer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import com.psit.struts.BIZ.CustomBIZ;
import com.psit.struts.entity.CusArea;
import com.psit.struts.entity.CusCity;
import com.psit.struts.entity.CusContact;
import com.psit.struts.entity.CusCorCus;
import com.psit.struts.entity.CusProvince;
import com.psit.struts.entity.LimUser;
import com.psit.struts.util.format.GetDate;
import com.psit.struts.util.format.StringFormat;

public class ExcelToCus extends ExcelToEntity {
	private LimUser limUser;
	private final String[] COL_NAME = new String[] { "name", "cusNum", "hot",
			"mne", "cardType", "cardNum", "cellPhone", "phone", "fex", "net",
			"zipCode", "addr", "area1", "area2", "area3", "qq", "email",
			"cusInf", "perSize", "remark", "con1Name", "con1Sex", "con1Dep",
			"con1Pos", "con1CellPho", "con1WorkPho", "con1HomePho", "con1Addr",
			"con1Re", "con2Name", "con2Sex", "con2Dep", "con2Pos",
			"con2CellPho", "con2WorkPho", "con2HomePho", "con2Addr", "con2Re",
			"con3Name", "con3Sex", "con3Dep", "con3Pos", "con3CellPho",
			"con3WorkPho", "con3HomePho", "con3Addr", "con3Re" };// 表格列
	private final DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	CustomBIZ customBiz;
	
	public ExcelToCus(LimUser limUser, CustomBIZ customBiz){
		super();
		this.limUser = limUser;
		this.customBiz = customBiz;
	}
	public boolean convert(String[][] excelData){
		CusCorCus cusCorCus;
		int i = 0;
		HashMap<String,String> dataMap = new HashMap<String,String>();
		if(errCode==0){
			if(!dataMap.isEmpty()){
				dataMap.clear();
			}
			for(i=0; i < excelData.length; i++){
				cusCorCus = new CusCorCus();
				if(excelData[i].length != COL_NAME.length){
					errCode = 101;
					break;
				}
				for(int j=0; j<excelData[i].length; j++){
					dataMap.put(COL_NAME[j], excelData[i][j]);
				}
				
				//必填项
				if (!StringFormat.isEmpty(dataMap.get("name"))) {
//					String prefix = "CU" + dateFormat.format(new Date());// 前缀
//					CodeCreator codeCreator = new CodeCreator();
//					String corNum = codeCreator.createCode(prefix, "cus_cor_cus", 0);// 生成编号
					cusCorCus.setCorName(StringFormat.shortString(dataMap.get("name"), 100));
					cusCorCus.setCorNum(StringFormat.shortString(dataMap.get("cusNum"), 50));
					if(customBiz.checkCus(dataMap.get("name"))){
						errCode = 202;
						break;
					}
					if(!StringFormat.isEmpty(dataMap.get("area1"))){
						if(StringFormat.isNumeric(dataMap.get("area1"))){
							cusCorCus.setCusArea(new CusArea(Long.parseLong(dataMap.get("area1"))));
						}
						else{
							errCode = 301;
							break;
						}
					}else{
						cusCorCus.setCusArea(new CusArea(Long.parseLong("1")));
					}
					if(!StringFormat.isEmpty(dataMap.get("area2"))){
						if(StringFormat.isNumeric(dataMap.get("area2"))){
							cusCorCus.setCusProvince(new CusProvince(Long.parseLong(dataMap.get("area2"))));
						}
						else{
							errCode = 302;
							break;
						}
					}else{
						cusCorCus.setCusProvince(new CusProvince(Long.parseLong("1")));
					}
					if(!StringFormat.isEmpty(dataMap.get("area3"))){
						if(StringFormat.isNumeric(dataMap.get("area3"))){
							cusCorCus.setCusCity(new CusCity(Long.parseLong(dataMap.get("area3"))));
						}
						else{
							errCode = 303;
							break;
						}
					}else{
						cusCorCus.setCusCity(new CusCity(Long.parseLong("1")));
					}
					if(dataMap.get("con1Sex").length() >1 || dataMap.get("con2Sex").length()>1 || dataMap.get("con3Sex").length()>1 ){
						errCode = 304;
						break;
					}
					cusCorCus.setCorHot(StringFormat.shortString(dataMap.get("hot"), 50));
					cusCorCus.setCorMne(StringFormat.shortString(dataMap.get("mne"), 20));
					cusCorCus.setCorCardType(StringFormat.shortString(dataMap.get("cardType"), 50));
					cusCorCus.setCorCardNum(StringFormat.shortString(dataMap.get("cardNum"), 50));
					cusCorCus.setCorCellPhone(StringFormat.shortString(dataMap.get("cellPhone"), 25));
					cusCorCus.setCorPhone(StringFormat.shortString(dataMap.get("phone"), 25));
					cusCorCus.setCorFex(StringFormat.shortString(dataMap.get("fex"), 25));
					cusCorCus.setCorNet(StringFormat.shortString(dataMap.get("net"), 250));
					cusCorCus.setCorZipCode(StringFormat.shortString(dataMap.get("zipCode"), 25));
					cusCorCus.setCorAddress(StringFormat.shortString(dataMap.get("addr"), 1000));
					cusCorCus.setCorQq(StringFormat.shortString(dataMap.get("qq"), 25));
					cusCorCus.setCorEmail(StringFormat.shortString(dataMap.get("email"), 100));
					cusCorCus.setCorComInf(StringFormat.shortString(dataMap.get("cusInf"), 4000));
					cusCorCus.setCorPerSize(StringFormat.shortString(dataMap.get("perSize"), 50));
					cusCorCus.setCorRemark(StringFormat.shortString(dataMap.get("remark"), 4000));
					cusCorCus.setCorCreatDate(GetDate.getCurTime());
					cusCorCus.setCorInsUser(limUser.getUserSeName());
					cusCorCus.setCorColor(CusCorCus.C_NONE);
					cusCorCus.setCorTempTag(CusCorCus.M_UNMARK);
					cusCorCus.setCorIsdelete(CusCorCus.D_UNDEL);
					cusCorCus.setCorState(CusCorCus.S_UNDEV);
					cusCorCus.setCorIssuc(CusCorCus.IS_UNSUC);
					dataList[0].add(cusCorCus);
					//add联系人list
					CusContact cont1 = initCusContact(cusCorCus,dataMap.get("con1Name"),dataMap.get("con1Sex"),
							dataMap.get("con1Dep"),dataMap.get("con1Pos"),dataMap.get("con1CellPho"),
							dataMap.get("con1WordPho"),dataMap.get("con1HomePho"),dataMap.get("con1Addr"),
							dataMap.get("con1Re"),limUser);
					if(cont1 != null){
						dataList[1].add(cont1);
					}
					CusContact cont2 = initCusContact(cusCorCus,dataMap.get("con2Name"),dataMap.get("con2Sex"),
							dataMap.get("con2Dep"),dataMap.get("con2Pos"),dataMap.get("con2CellPho"),
							dataMap.get("con2WordPho"),dataMap.get("con2HomePho"),dataMap.get("con2Addr"),
							dataMap.get("con2Re"),limUser);
					if(cont2 != null){
						dataList[1].add(cont2);
					}
					CusContact cont3 = initCusContact(cusCorCus,dataMap.get("con3Name"),dataMap.get("con3Sex"),
							dataMap.get("con3Dep"),dataMap.get("con3Pos"),dataMap.get("con3CellPho"),
							dataMap.get("con3WordPho"),dataMap.get("con3HomePho"),dataMap.get("con3Addr"),
							dataMap.get("con3Re"),limUser);
					if(cont3 != null){
						dataList[1].add(cont3);
					}
				}
				else{
					errCode = 201;
					break;
				}
			}
		}
				
		curRowNo = i+1;//当前行号
		return (errCode==0);
	}
	public String getResult(){
		String errMsg = "";
		switch(errCode){
		case 101:
			errMsg="请使用系统提供的excel模板导入数据！";
			break;
			
		case 201:
			errMsg="必填项不能为空！";
			break;
		case 202:
			errMsg="客户姓名在系统（包括回收站）中已存在！";
			break;
			
		case 301:
			errMsg="国家ID填写不正确！";
			break;
		case 302:
			errMsg="省份ID填写不正确！";
			break;
		case 303:
			errMsg="城市ID填写不正确！";
			break;
		case 304:
			errMsg="性别填写不正确！";
			break;
		}
		if(errMsg.length()>0){
			errMsg = "[第"+Integer.toString(curRowNo+1)+"行] " + errMsg;
		}
		return errMsg;
	}
	
	/**
	 * 初始化联系人实体<br>
	 */
	public CusContact initCusContact(CusCorCus cus, String conName, String sex,String conDep,String conPos,
			String conPhone, String conWorkPho, String conHomePho, String conAddr, String conRemark, LimUser limUser){
		if(!StringFormat.isEmpty(conName) || !StringFormat.isEmpty(sex) || 
			!StringFormat.isEmpty(conDep) || !StringFormat.isEmpty(conPos) || 
			!StringFormat.isEmpty(conPhone) || !StringFormat.isEmpty(conWorkPho) || 
			!StringFormat.isEmpty(conHomePho) || !StringFormat.isEmpty(conAddr)){
			CusContact cont = new CusContact();
			cont.setCusCorCus(cus);
			cont.setConName(StringFormat.shortString(conName,50));
			cont.setConSex(StringFormat.shortString(sex,1));
			cont.setConDep(StringFormat.shortString(conDep,150));
			cont.setConPos(StringFormat.shortString(conPos,100));
			cont.setConPhone(StringFormat.shortString(conPhone, 25));
			cont.setConWorkPho(StringFormat.shortString(conWorkPho, 25));
			cont.setConHomePho(StringFormat.shortString(conHomePho, 25));
			cont.setConAdd(StringFormat.shortString(conAddr,1000));
			cont.setConRemark(StringFormat.shortString(conRemark,4000));
			cont.setConInpUser(limUser.getUserSeName());
			cont.setConCreDate(GetDate.getCurDate());
			return cont;	
		}
		else{
			return null;
		}
	}
}
