package com.psit.struts.entity;
/**
 * 
 * 客户附件实体 <br>
 *
 * create_date: Aug 27, 2010,3:12:25 PM<br>
 * @author zjr
 */
public class AttAllCus extends Attachment implements java.io.Serializable{
		private CusCorCus cusCorCus;
		
		public AttAllCus(){}
		
		public AttAllCus(CusCorCus cuscorcus){
			this.cusCorCus = cuscorcus;
		}
		
		public CusCorCus getCusCorCus(){
			return this.cusCorCus ;
		}
		
		public void setCusCorCus(CusCorCus cuscorcus){
			this.cusCorCus = cuscorcus;
		}
}
