package yh.subsys.oa.book.data;

import yh.core.util.YHUtility;

/**
 * 图书信息
 * @author qwx110
 *
 */
public class YHBookInfo{
  
  private int seqId;         //主键
  private String isbn;       //图书的ISBN
  private int typeId =0;     //图书类型
  private int dept;          //图书所在部门id
  private String bookName;   //图书名称
  private String author ="";     //作者
  private String pubDate ="";    //出版日期 
  private String pubHouse ="";   //出版社
  private int amt =0;        //数量
  private double price =0;   //价格
  private String area;       //图书所在地区
  private String brief ="";      //简介
  private String open;       //借阅范围id
  private String lend;         //是否借出
  private String borrPerson; //借书人
  private String memo ="";       //注释
  private String bookNo;     //图书编号
  private String attachmentId; //上传附件id
  private String attachmentName; //上传附件名字
  private String deptName;       //部门名称
  private String openNames;      //查阅范围名称
  private String typeName;      //类型名
  private String saveInfo;     //导入保存信息
  private boolean isCanDel = false;

public boolean isCanDel() {
	return isCanDel;
}
public void setCanDel(boolean isCanDel) {
	this.isCanDel = isCanDel;
}
public String getSaveInfo() {
    return saveInfo;
  }
  public void setSaveInfo(String saveInfo) {
    this.saveInfo = saveInfo;
  }
  public String getTypeName(){
    if(this.typeName == null || "null".equalsIgnoreCase(typeName)){
      this.typeName ="";
    }
    return typeName;
  }
  public void setTypeName(String typeName){
    this.typeName = typeName;
  }
  public String getDeptName(){
    return deptName;
  }
  public void setDeptName(String deptName){
    this.deptName = deptName;
  }
  public String getOpenNames(){
    return openNames;
  }
  public void setOpenNames(String openNames){
    this.openNames = openNames;
  }
  public int getSeqId(){
    return seqId;
  }
  public void setSeqId(int seqId){
    this.seqId = seqId;
  }
  public String getIsbn(){
    if(this.isbn == null || "null".equalsIgnoreCase(isbn)){
      this.isbn ="";
    }
    return isbn;
  }
  public void setIsbn(String isbn){
    this.isbn = isbn;
  }
  public int getTypeId(){
    return typeId;
  }
  public void setTypeId(int typeId){
    this.typeId = typeId;
  }
  public int getDept(){
    return dept;
  }
  public void setDept(int dept){
    this.dept = dept;
  }
  public String getBookName(){
    return bookName;
  }
  public void setBookName(String bookName){
    this.bookName = bookName;
  }
  public String getAuthor(){
    if(this.author == null || "null".equalsIgnoreCase(author)){
      this.author ="";
    }
    return author;
  }
  public void setAuthor(String author){
    this.author = author;
  }
  public String getPubDate(){
    if(this.pubDate == null || "null".equalsIgnoreCase(pubDate)){
      this.pubDate ="";
    }
    return pubDate;
  }
  public void setPubDate(String pubDate){
    this.pubDate = pubDate;
  }
  public String getPubHouse(){
    if(this.pubHouse == null){
      this.pubHouse ="";
    }
    return pubHouse;
  }
  public void setPubHouse(String pubHouse){
    this.pubHouse = pubHouse;
  }
  public int getAmt(){
    return amt;
  }
  public void setAmt(int amt){
    this.amt = amt;
  }
  public double getPrice(){
    
    return price;
  }
  public void setPrice(double price){
    this.price = price;
  }
  public String getArea(){
    if(this.area == null){
       this.area = "";
    }
    return area;
  }
  public void setArea(String area){
    this.area = area;
  }
  public String getBrief(){
    if(this.brief == null){
      this.brief = "";
    }
    return brief;
  }
  public void setBrief(String brief){
    this.brief = brief;
  }
  public String getOpen(){
    return open;
  }
  public void setOpen(String open){
    this.open = open;
  }

  public String getLend(){
    return lend;
  }
  public void setLend(String lend){
    this.lend = lend;
  }
  public String getBorrPerson(){
    return borrPerson;
  }
  public void setBorrPerson(String borrPerson){
    this.borrPerson = borrPerson;
  }
  public String getMemo(){
    if(this.memo == null || "null".equalsIgnoreCase(memo)){
      this.memo ="";
    }
    return memo;
  }
  public void setMemo(String memo){
    this.memo = memo;
  }
  public String getBookNo(){
    return bookNo;
  }
  public void setBookNo(String bookNo){
    this.bookNo = bookNo;
  }
  public String getAttachmentId(){
    return attachmentId;
  }
  public void setAttachmentId(String attachmentId){
    this.attachmentId = attachmentId;
  }
  public String getAttachmentName(){
    return attachmentName;
  }
  public void setAttachmentName(String attachmentName){
    this.attachmentName = attachmentName;
  }
  
  public String toJson(){
    StringBuffer sb = new StringBuffer();
    sb.append("{");
      sb.append("seqId:").append(this.getSeqId()).append(",");
      sb.append("bookName:").append("\"").append(YHUtility.encodeSpecial(bookName.trim())).append("\"").append(",");
      sb.append("bookNo:").append("\"").append(YHUtility.encodeSpecial(bookNo.trim())).append("\"");
    sb.append("}");
    return sb.toString();
  }
  
}
