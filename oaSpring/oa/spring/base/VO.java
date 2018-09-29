/**
 * 
 */
package oa.spring.base;

/**
 * @author lanjinsheng
 *
 */
public class VO {
	String tableName;
	String []fieldName;
	String []fieldType;
	String []fieldValue;
	String []whereTag;
	String []whereValue;
	int perRecords=0;
	int nowTimes=0;
    int totalNum=-1;
    
    public boolean hasNext(){
    	return totalNum>perRecords*nowTimes;
    }
    public void next(){
    	
    	 nowTimes++;
    }
	/**
	 * @return the fieldName
	 */
	public String[] getFieldName() {
		return fieldName;
	}

	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String[] fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return the fieldType
	 */
	public String[] getFieldType() {
		return fieldType;
	}

	/**
	 * @param fieldType the fieldType to set
	 */
	public void setFieldType(String[] fieldType) {
		this.fieldType = fieldType;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return the fieldValue
	 */
	public String[] getFieldValue() {
		return fieldValue;
	}

	/**
	 * @param fieldValue the fieldValue to set
	 */
	public void setFieldValue(String[] fieldValue) {
		this.fieldValue = fieldValue;
	}

	/**
	 * @return the perRecords
	 */
	public int getPerRecords() {
		return perRecords;
	}

	/**
	 * @param perRecords the perRecords to set
	 */
	public void setPerRecords(int perRecords) {
		this.perRecords = perRecords;
	}

	/**
	 * @return the nowTimes
	 */
	public int getNowTimes() {
		return nowTimes;
	}

	/**
	 * @param nowTimes the nowTimes to set
	 */
	public void setNowTimes(int nowTimes) {
		this.nowTimes = nowTimes;
	}

	/**
	 * @return the totalNum
	 */
	public int getTotalNum() {
		return totalNum;
	}

	/**
	 * @param totalNum the totalNum to set
	 */
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	public String[] getWhereTag() {
		return whereTag;
	}
	public void setWhereTag(String[] whereTag) {
		this.whereTag = whereTag;
	}
	public String[] getWhereValue() {
		return whereValue;
	}
	public void setWhereValue(String[] whereValue) {
		this.whereValue = whereValue;
	}

	

}
