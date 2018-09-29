package yh.setup.fis.acset.global;

import yh.core.global.YHSysProps;
import yh.oa.tools.StaticData;

public class TDAcsetConst {
  
  public static final String BUILD_NEW_ACSET_YES = "1";
  
  public static final String ACSET_FILE_LIST = "fileList";
  public static final String ACSET_INDEX = "index";
  
  public static final String ACSET_DATABASE_LOAD = "databaseLoad";
  public static final String ACSET_DATABASE_CREATE = "databaseCreate";
  public static final String ACSET_DATABASE_CREATED = "databaseCreated";
  
  public static final String ACSET_T0_INITIALIZE_DATABASE = "toInitializeDatabase";
  public static final String ACSET_DATABASE_INITIALIZED = "databaseInitialized";
  
  public static final String ACSET_RETURNCHECK = "returnCheck";
  
  public static final String CREATEDATABASE_USERDEPTID = "userDeptId";
  public static final String CREATEDATABASE_USERDEPTDESC = "userDeptDesc";
  public static final String CREATEDATABASE_IFWAIBIHESUAN = "ifWaibihesuan";
  public static final String CREATEDATABASE_ACSETNAME = "acsetName";
  public static final String CREATEDATABASE_ORGID = "orgId";
  public static final String CREATEDATABASE_RATELENGTH = "rateLength";
  public static final String CREATEDATABASE_RATEDECIAMLLEN = "rateDecimalLength";
  public static final String CREATEDATABASE_MONEYLENGTH = "moneyLength";
  public static final String CREATEDATABASE_MONEYDECIMALLEN = "moneyDecimalLength";
  public static final String CREATEDATABASE_ACCTYEAR = "acctyear";
  public static final String CREATEDATABASE_CURRDESC = "homeCurrDesc";  
  public static final String CREATEDATABASE_CURRDSIGN = "homeCurrSign";
  public static final String CREATEDATABASE_FIRSTPERIOD = "firstPeiod";
  public static final String INITIAL_BEGINDATE = "beginDate";
  public static final String INITIAL_SETDATE = "setDate";
  public static final String FORM_ACSET_NO = "acsetNo";
  
  public static final String ACTION_CLONE = "clone";
  public static final String ACTION_EXPORT = "export";
  public static final String ACTION_IMPORT2NEW = "import2New";
  public static final String ACTION_EMPORT2EXISTS = "import2Exists";
  
  public static final String INITIAL_ACCOUNTPERIOD = "accountPeriod";

  public static final String INITIAL_SUBJECTLENGTH = "subjectLength";
  public static final String INITIAL_IFFIXEDRATE = "ifFixedRate";
  public static final String INITIAL_HOMECURRENT = "homeCurrent";
  public static final String INITIAL_AIDCURRENT = "aidCurrent";
  public static final String INITIAL_RATEDEFINATION = "rateDefination";
  public static final String INITIAL_MAKEBILLPROCESS = "makeBillProcess";
  public static final String INITIAL_VOUCHERID = "voucherID";
  public static final String INITIAL_VOUCHERFORMAT = "voucherFormat";
  public static final String INITIAL_VOUCHERMANAGE = "voucherManage";
  public static final String INITIAL_VOUCHERCHECK = "vucherCheck";
  public static final String INITIAL_BUILDNEWACSET = "buildNewAcset";
  
  public static final String TDFIS_CLOSE_USER =StaticData.SOFTCOMPANY_SHORTNAME+ "财务自动记账";
  public static final String TDFIS_CLOSE_INIT = "acset.init.close";
  /**
   * 取得帐套数据库名前缀
   */
  public static String getAcsetDbPrefix() {
    String acsetDbPrefix = YHSysProps.getString("acsetDbPrefix");
    if (acsetDbPrefix == null || "".equals(acsetDbPrefix.trim())) {
      acsetDbPrefix = "TD";
    }
    return acsetDbPrefix;
  }
}
