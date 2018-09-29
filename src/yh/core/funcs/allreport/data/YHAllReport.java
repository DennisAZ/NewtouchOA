package yh.core.funcs.allreport.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import yh.core.util.YHUtility;

public class YHAllReport {

	private int seqId;
	private int moduleId;
	private String rName;
	private String listItem;
	private String queryItem;
	private String createUser;
	private Date createDate;
	private String groupType;
	private String groupField;
	public int getSeqId() {
		return seqId;
	}
	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}
	public int getModuleId() {
		return moduleId;
	}
	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}
	public String getRName() {
		return rName;
	}
	public void setRName(String rName) {
		this.rName = rName;
	}
	public String getListItem() {
		return listItem;
	}
	public void setListItem(String listItem) {
		this.listItem = listItem;
	}
	public String getQueryItem() {
		return queryItem;
	}
	public void setQueryItem(String queryItem) {
		this.queryItem = queryItem;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	public String getGroupField() {
		return groupField;
	}
	public void setGroupField(String groupField) {
		this.groupField = groupField;
	}
	
}
