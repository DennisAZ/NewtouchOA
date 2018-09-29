package yh.subsys.oa.hr.manage.staff_contract.data;

import java.util.Date;

/**
 * 收 文管理
 * @author Administrator
 *
 */
public class YHDocReceive{
  private int seq_id;             //主键
  private String docNo;           //外办文号
  private Date resDate;           //已办文号
  private String fromUnits;       //来电单位
  private String oppDocNo;        //对方文号
  private String title;           //标题
  private int copies;             //份数
  private int confLevel;          //保密级别
  private String instruct;        //领导批示
  private String recipient;       //签收人
  private int docType;            //收件类型
  private int status;             //收文状态0未签，1已签
  private int sendStauts;         //发送类型0未发送，1已发送
  private String sponsor;         //承办单位
  private int userId;             //创建人的id
  private String fromUserName;
  private String toUserName;
  private String confLevelName;
  private String docTypeName;
  private String attachNames;     //附件名称
  private String attachIds;       //附件id
  
  public String getAttachNames(){
    return attachNames;
  }
  public void setAttachNames(String attachNames){
    this.attachNames = attachNames;
  }
  public String getAttachIds(){
    return attachIds;
  }
  public void setAttachIds(String attachIds){
    this.attachIds = attachIds;
  }
  public String getConfLevelName(){
    int level = getConfLevel();
    if(level == 1){
      confLevelName = "绝密";
    }else if(level == 2){
      confLevelName = "机密";
    }else{
      confLevelName = "一般";
    }
    return confLevelName;
  }
  public void setConfLevelName(String confLevelName){
    this.confLevelName = confLevelName;
  }
  public String getDocTypeName(){
    int  docType = getDocType();
    if(docType == 0){
      docTypeName = "外办收文";
    }else if(docType == 1){
      docTypeName = "收电";
    }else if(docType == 2){
      docTypeName = "收信";
    }else if(docType == 3){
      docTypeName = "阅文";
    }else if(docType == 4){
      docTypeName = "传真";
    }
    return docTypeName;
  }
  public void setDocTypeName(String docTypeName){
    this.docTypeName = docTypeName;
  }
  public String getFromUserName(){
    return fromUserName;
  }
  public void setFromUserName(String fromUserName){
    this.fromUserName = fromUserName;
  }
  public String getToUserName(){
    return toUserName;
  }
  public void setToUserName(String toUserName){
    this.toUserName = toUserName;
  }
  public int getUserId(){
    return userId;
  }
  public void setUserId(int userId){
    this.userId = userId;
  }
  public String getSponsor(){
    return sponsor;
  }
  public void setSponsor(String sponsor){
    this.sponsor = sponsor;
  }
  public int getSendStauts(){
    return sendStauts;
  }
  public void setSendStauts(int sendStauts){
    this.sendStauts = sendStauts;
  }
  public int getStatus(){
    return status;
  }
  public void setStatus(int status){
    this.status = status;
  }
  public int getSeq_id(){
    return seq_id;
  }
  public void setSeq_id(int seqId){
    seq_id = seqId;
  }
  public String getDocNo(){
    return docNo;
  }
  public void setDocNo(String docNo){
    this.docNo = docNo;
  }
  public Date getResDate(){
    return resDate;
  }
  public void setResDate(Date resDate){
    this.resDate = resDate;
  }
  public String getFromUnits(){
    return fromUnits;
  }
  public void setFromUnits(String fromUnits){
    this.fromUnits = fromUnits;
  }
  public String getOppDocNo(){
    return oppDocNo;
  }
  public void setOppDocNo(String oppDocNo){
    this.oppDocNo = oppDocNo;
  }
  public String getTitle(){
    return title;
  }
  public void setTitle(String title){
    this.title = title;
  }
  public int getCopies(){
    return copies;
  }
  public void setCopies(int copies){
    this.copies = copies;
  }
  public int getConfLevel(){
    return confLevel;
  }
  public void setConfLevel(int confLevel){
    this.confLevel = confLevel;
  }
  public String getInstruct(){
    return instruct;
  }
  public void setInstruct(String instruct){
    this.instruct = instruct;
  }
  public String getRecipient(){
    return recipient;
  }
  public void setRecipient(String recipient){
    this.recipient = recipient;
  }
  public int getDocType(){
    return docType;
  }
  public void setDocType(int docType){
    this.docType = docType;
  }
}
