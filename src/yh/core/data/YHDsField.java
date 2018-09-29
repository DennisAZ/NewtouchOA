package yh.core.data;

import java.sql.Types;

import yh.core.global.YHDsFieldConst;

public class YHDsField {
  /**  **/
 private int seqId = 0;
  /** 表外键参照,编码5位长 **/
  private String tableNo = null;
  /** 编码规则5,3 **/
  private String fieldNo = null;
  /**  **/
  private String fieldName = null;
  /** Java类中的属性名字 **/
  private String propName = null;
  /**  **/
  private String fieldDesc = null;
  /** 编码外键参照表 / 主表参照表编码 / 多对多参照表编码1 **/
  private String fkTableNo = null;
  /** 多对多参照表编码2 **/
  private String fkTableNo2 = null;
  /** 外键参照编码字段编码 **/
  private String fkRelaFieldNo = null;
  /** 外键参照名称字段编码 **/
  private String fkNameFieldNo = null;
  /** 外键参照筛选条件 **/
  private String fkFilter = null;
  /** 外键参照小编码类别码 **/
  private String codeClass = null;
  /** 缺省值 **/
  private String defaultValue = null;
  /** number / text / date / amt **/
  private String formatMode = null;
  /** 格式化规则 **/
  private String formatRule = null;
  /** 错误消息 **/
  private String errorMsrg = null;
  /** 数位长度 **/
  private int fieldPrecision = 0;
  /** 小数位数 **/
  private int fieldScale = 0;
  /** 数据类型,值请参考yh.core.data.TDCDbType.getTypeName **/
  private int dataType = 0;
  /** 是否是主键 1=是,0=否 **/
  private String isPrimaryKey = null;
  /** 是否自增 1=是,0=否 **/
  private String isIdentity = null;
  /** 显示长度 **/
  private int displayLen = 0;
  /** 是否必填 1=是,0=否 **/
  private String isMustFill = null;
  /** 外键参照名称字段编码 **/
  private String fkNameFieldNo2 = null;

  public String getPropName() {
    return propName;
  }
  public void setPropName(String propName) {
    this.propName = propName;
  }
  public String getFkTableNo2() {
    return fkTableNo2;
  }
  public void setFkTableNo2(String fkTableNo2) {
    this.fkTableNo2 = fkTableNo2;
  }
  /**
   * 根据类型产生格式化模式
   * 
   * @return
   */
  public String genFormatMode() {
    if (dataType == Types.BIGINT) {
      return YHDsFieldConst.FORMAT_MODE_NUMBER;
    } else if (dataType == Types.DECIMAL) {
      return YHDsFieldConst.FORMAT_MODE_NUMBER;
    } else if (dataType == Types.DOUBLE) {
      return YHDsFieldConst.FORMAT_MODE_NUMBER;
    } else if (dataType == Types.FLOAT) {
      return YHDsFieldConst.FORMAT_MODE_NUMBER;
    } else if (dataType == Types.INTEGER) {
      return YHDsFieldConst.FORMAT_MODE_NUMBER;
    } else if (dataType == Types.NUMERIC) {
      return YHDsFieldConst.FORMAT_MODE_NUMBER;
    } else if (dataType == Types.REAL) {
      return YHDsFieldConst.FORMAT_MODE_NUMBER;
    } else if (dataType == Types.SMALLINT) {
      return YHDsFieldConst.FORMAT_MODE_NUMBER;
    } else if (dataType == Types.TINYINT) {
      return YHDsFieldConst.FORMAT_MODE_NUMBER;
    } else if (dataType == Types.DATE) {
      return YHDsFieldConst.FORMAT_MODE_DATE;
    } else if (dataType == Types.TIME) {
      return YHDsFieldConst.FORMAT_MODE_TIME;
    }
    return YHDsFieldConst.FORMAT_MODE_TEXT;
  }

  /**
   * 判断对象相等
   */
  public boolean equals(Object other) {
    if (other == null || !(other instanceof YHDsField)) {
      return false;
    }
    YHDsField otherField = (YHDsField) other;
    if (!this.fieldName.equals(otherField.getFieldName())) {
      return false;
    }
    return true;
  }

  /**
   * 判断是否需要添加引号
   * 
   * @return
   */
  public boolean isAddQuote() {
    if (dataType == Types.BIGINT) {
      return false;
    } else if (dataType == Types.DECIMAL) {
      return false;
    } else if (dataType == Types.DOUBLE) {
      return false;
    } else if (dataType == Types.FLOAT) {
      return false;
    } else if (dataType == Types.INTEGER) {
      return false;
    } else if (dataType == Types.NUMERIC) {
      return false;
    } else if (dataType == Types.REAL) {
      return false;
    } else if (dataType == Types.SMALLINT) {
      return false;
    } else if (dataType == Types.TINYINT) {
      return false;
    }

    return true;
  }

  /**
   * 取得数据类型名称
   * 
   * @return
   */
  public String getDataTypeName() {
    return YHDsType.getTypeName(dataType);
  }

  /**
   *
   */
  public int getSeqId() {
    return this.seqId;
  }

  /**
   * 
   * @param seqId
   */
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }

  /**
   *
   */
  public String getTableNo() {
    return this.tableNo;
  }

  /**
   * 
   * @param tableNo
   */
  public void setTableNo(String tableNo) {
    this.tableNo = tableNo;
  }

  /**
   *
   */
  public String getFieldNo() {
    return this.fieldNo;
  }

  /**
   * 
   * @param fieldNo
   */
  public void setFieldNo(String fieldNo) {
    this.fieldNo = fieldNo;
  }

  /**
   *
   */
  public String getFieldName() {
    return this.fieldName;
  }

  /**
   * 
   * @param fieldName
   */
  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  /**
   *
   */
  public String getFieldDesc() {
    return this.fieldDesc;
  }

  /**
   * 
   * @param fieldDesc
   */
  public void setFieldDesc(String fieldDesc) {
    this.fieldDesc = fieldDesc;
  }

  /**
   *
   */
  public String getFkTableNo() {
    return this.fkTableNo;
  }

  /**
   * 
   * @param fkTableId
   */
  public void setFkTableNo(String fkTableNo) {
    this.fkTableNo = fkTableNo;
  }

  /**
   *
   */
  public String getFkRelaFieldNo() {
    return this.fkRelaFieldNo;
  }

  /**
   * 
   * @param fkRelaFieldId
   */
  public void setFkRelaFieldNo(String fkRelaFieldNo) {
    this.fkRelaFieldNo = fkRelaFieldNo;
  }

  /**
   *
   */
  public String getFkNameFieldNo() {
    return this.fkNameFieldNo;
  }

  /**
   * 
   * @param fkNameFieldId
   */
  public void setFkNameFieldNo(String fkNameFieldNo) {
    this.fkNameFieldNo = fkNameFieldNo;
  }

  /**
   *
   */
  public String getFkFilter() {
    return this.fkFilter;
  }

  /**
   * 
   * @param fkFilter
   */
  public void setFkFilter(String fkFilter) {
    this.fkFilter = fkFilter;
  }

  /**
   *
   */
  public String getCodeClass() {
    return this.codeClass;
  }

  /**
   * 
   * @param codeClass
   */
  public void setCodeClass(String codeClass) {
    this.codeClass = codeClass;
  }

  /**
   *
   */
  public String getDefaultValue() {
    return this.defaultValue;
  }

  /**
   * 
   * @param defaultValue
   */
  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  /**
   *
   */
  public String getFormatMode() {
    return this.formatMode;
  }

  /**
   * 
   * @param formatMode
   */
  public void setFormatMode(String formatMode) {
    this.formatMode = formatMode;
  }

  /**
   *
   */
  public String getFormatRule() {
    return this.formatRule;
  }

  /**
   * 
   * @param formatRule
   */
  public void setFormatRule(String formatRule) {
    this.formatRule = formatRule;
  }

  /**
   *
   */
  public String getErrorMsrg() {
    return this.errorMsrg;
  }

  /**
   * 
   * @param errorMsrg
   */
  public void setErrorMsrg(String errorMsrg) {
    this.errorMsrg = errorMsrg;
  }

  /**
   *
   */
  public int getFieldPrecision() {
    return this.fieldPrecision;
  }

  /**
   * 
   * @param fieldPrecision
   */
  public void setFieldPrecision(int fieldPrecision) {
    this.fieldPrecision = fieldPrecision;
  }

  /**
   *
   */
  public int getFieldScale() {
    return this.fieldScale;
  }

  /**
   * 
   * @param fieldScale
   */
  public void setFieldScale(int fieldScale) {
    this.fieldScale = fieldScale;
  }

  /**
   *
   */
  public int getDataType() {
    return this.dataType;
  }

  /**
   * 
   * @param dataType
   */
  public void setDataType(int dataType) {
    this.dataType = dataType;
  }

  /**
   *
   */
  public String getIsIdentity() {
    return this.isIdentity;
  }

  /**
   * 
   * @param isIdentity
   */
  public void setIsIdentity(String isIdentity) {
    this.isIdentity = isIdentity;
  }

  /**
   *
   */
  public int getDisplayLen() {
    return this.displayLen;
  }

  /**
   * 
   * @param dsiplayLen
   */
  public void setDisplayLen(int displayLen) {
    this.displayLen = displayLen;
  }

  /**
   * 
   * @param isnullable
   */
  public void setMustFill(String isMustFill) {
    this.isMustFill = isMustFill;
  }

  /**
   * 是否必须填写
   * 
   * @return
   */
  public boolean isMustFill() {
    if (isMustFill == null) {
      return false;
    }
    if (isMustFill.equalsIgnoreCase(YHDsFieldConst.IS_MUST_FILL_YES)) {
      return true;
    }
    return false;
  }

  public String getIsMustFill() {
    return isMustFill;
  }

  public void setIsMustFill(String isMustFill) {
    this.isMustFill = isMustFill;
  }
  public String getFkNameFieldNo2() {
    return fkNameFieldNo2;
  }
  public void setFkNameFieldNo2(String fkNameFieldNo2) {
    this.fkNameFieldNo2 = fkNameFieldNo2;
  }
  /**
   * 是否是唯一主键
   * 
   * @return
   */
  public boolean isPrimaryKey() {
    if (isPrimaryKey == null) {
      return false;
    }
    if (isPrimaryKey.equalsIgnoreCase(YHDsFieldConst.PRIM_KEY_YES)) {
      return true;
    }
    return false;
  }

  public String getIsPrimaryKey() {
    return isPrimaryKey;
  }

  public void setIsPrimaryKey(String isPrimaryKey) {
    this.isPrimaryKey = isPrimaryKey;
  }
  @Override
  public String toString() {
    return "YHDsField [codeClass=" + codeClass + ", dataType=" + dataType
        + ", defaultValue=" + defaultValue + ", displayLen=" + displayLen
        + ", errorMsrg=" + errorMsrg + ", fieldDesc=" + fieldDesc
        + ", fieldName=" + fieldName + ", fieldNo=" + fieldNo
        + ", fieldPrecision=" + fieldPrecision + ", fieldScale=" + fieldScale
        + ", fkFilter=" + fkFilter + ", fkNameFieldNo=" + fkNameFieldNo
        + ", fkRelaFieldNo=" + fkRelaFieldNo + ", fkTableNo=" + fkTableNo
        + ", fkTableNo2=" + fkTableNo2 + ", formatMode=" + formatMode
        + ", formatRule=" + formatRule + ", isIdentity=" + isIdentity
        + ", isMustFill=" + isMustFill + ", isPrimaryKey=" + isPrimaryKey
        + ", propName=" + propName + ", seqId=" + seqId + ", tableNo="
        + tableNo + ", fkNameFieldNo2=" + fkNameFieldNo2 + "]";
  }
  
}
