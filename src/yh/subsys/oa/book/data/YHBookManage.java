package yh.subsys.oa.book.data;

import java.util.Date;

  public class YHBookManage {
    
    private int seqId;//借阅信息ID 自增的主键
    private String buserId;//借书人
    private String bookNo; //图书编号
    
    private Date borrowDate;//借书日期
    private String borrowRemark;//借书备注
    private String ruserId; //借书审批人
    
    private Date returnDate;//还书日期
    private String bookStatus; //借书状态标记  0、待批；1、已还
    private Date realReturnTime; //实际还书日期
    
    private String deleteFlag;  //删除标记 0、未删除；1、标记删除
    private String status; //还书状态标记 0、未受理；1、已批准； 2、未准借阅
    private String regFlag;//管理员直接登记标记 0、正常借阅； 1、管理员补登记借阅
    
    private String WHO_CONFIRM; //未使用字段
    
    private String borPersonName;  //借书人姓名
     
    private String bookName;     //书名
    
    private String regUserName;//登记人姓名
    public String getRegUserName(){
      if(this.regUserName == null){
        this.regUserName ="";
      }
      return regUserName;
    }


    public void setRegUserName(String regUserName){
      this.regUserName = regUserName;
    }


    public String getBookName(){
      if(this.bookName == null){
        this.bookName = "";
      }
      return bookName;
    }


    public void setBookName(String bookName){
      this.bookName = bookName;
    }


    public String getBorPersonName(){
      if(this.borPersonName == null){
        this.borPersonName = "";
      }
      return borPersonName;
    }
   
    
    public void setBorPersonName(String borPersonName){
      this.borPersonName = borPersonName;
    }
    public int getSeqId() {
      return seqId;
    }
    public void setSeqId(int seqId) {
      this.seqId = seqId;
    }
    public String getBuserId() {
      if(this.buserId == null || buserId.equals("null") ){
        this.buserId ="";
      }
      return buserId;
    }
    public void setBuserId(String buserId) {
      this.buserId = buserId;
    }
    public String getBookNo() {
      return bookNo;
    }
    public void setBookNo(String bookNo) {
      this.bookNo = bookNo;
    }
    public Date getBorrowDate() {
      return borrowDate;
    }
    public void setBorrowDate(Date borrowDate) {
      this.borrowDate = borrowDate;
    }
    public String getBorrowRemark() {
      if(this.borrowRemark == null){
        this.borrowRemark = "";
      }
      return borrowRemark;
    }
    public void setBorrowRemark(String borrowRemark) {
      this.borrowRemark = borrowRemark;
    }
    public String getRuserId() {
      return ruserId;
    }
    public void setRuserId(String ruserId) {
      this.ruserId = ruserId;
    }
    public Date getReturnDate() {
      return returnDate;
    }
    public void setReturnDate(Date returnDate) {
      this.returnDate = returnDate;
    }
    public String getBookStatus() {
      return bookStatus;
    }
    public void setBookStatus(String bookStatus) {
      this.bookStatus = bookStatus;
    }
    public Date getRealReturnTime() {
      return realReturnTime;
    }
    public void setRealReturnTime(Date realReturnTime) {
      this.realReturnTime = realReturnTime;
    }
    public String getDeleteFlag() {
      return deleteFlag;
    }
    public void setDeleteFlag(String deleteFlag) {
      this.deleteFlag = deleteFlag;
    }
    public String getStatus() {
      return status;
    }
    public void setStatus(String status) {
      this.status = status;
    }
    public String getRegFlag() {
      return regFlag;
    }
    public void setRegFlag(String regFlag) {
      this.regFlag = regFlag;
    }
    public String getWHO_CONFIRM() {
      return WHO_CONFIRM;
    }
    public void setWHO_CONFIRM(String wHOCONFIRM) {
      WHO_CONFIRM = wHOCONFIRM;
    }  
  }
