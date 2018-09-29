package com.psit.struts.entity;

import java.util.Date;

/**
 * 
 * 订单附件 <br>
 *
 * create_date: Aug 27, 2010,3:13:29 PM<br>
 * @author zjr
 */

public class AttOrd extends Attachment implements java.io.Serializable {
	private SalOrdCon salOrdCon;
    /** default constructor */
    public AttOrd() {
    }
    
    /** full constructor */
    public AttOrd(SalOrdCon salOrdCon) {
        this.salOrdCon=salOrdCon;
    }

	public SalOrdCon getSalOrdCon() {
		return salOrdCon;
	}

	public void setSalOrdCon(SalOrdCon salOrdCon) {
		this.salOrdCon = salOrdCon;
	}
}