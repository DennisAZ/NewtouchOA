package com.psit.struts.entity;

/**
 * 贷后客户附件实体 <br>
 */
public class AttEmp extends Attachment  implements java.io.Serializable {

    // Fields    
     private SalEmp salEmp;

    // Constructors

    /** default constructor */
    public AttEmp() {
    }
    
    /** full constructor */
    public AttEmp(SalEmp salEmp) {
        this.salEmp = salEmp;
    }

	public SalEmp getSalEmp() {
		return salEmp;
	}

	public void setSalEmp(SalEmp salEmp) {
		this.salEmp = salEmp;
	}

}