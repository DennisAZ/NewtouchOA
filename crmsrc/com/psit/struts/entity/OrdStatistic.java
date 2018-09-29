package com.psit.struts.entity;

import java.util.Date;
/**
 * 
 * 统计实体 <br>
 *
 * create_date: Aug 27, 2010,3:26:10 PM<br>
 * @author zjr
 */
public class OrdStatistic {
	private Long id;//；类别ID
	private Long lnum;//int 数量
	private Double dnum;//double 金额
	private String name;//类别名称
	private String  type;//统计类型
	public OrdStatistic()
	{	
	}
	//type:1
	public OrdStatistic(Long id,Long lnum,String name,String  type)
	{
		this.id=id;
		this.lnum=lnum;
		this.name=name;
		this.type=type;
	}
	//type:2
	public OrdStatistic(Long id,Double dnum,String name,String  type)
	{
		this.id=id;
		this.dnum=dnum;
		this.name=name;
		this.type=type;
	}
	//type:3
	public OrdStatistic(Double dnum,String name,String  type)
	{
		this.dnum=dnum;
		this.name=name;
		this.type=type;
	}
	//type:4
	public OrdStatistic(Long lnum,String name,String  type)
	{
		this.lnum=lnum;
		this.name=name;
		this.type=type;
	}
	//type:
	public OrdStatistic(Long lnum,Long id,String  type)
	{
		this.lnum=lnum;
		this.id=id;
		this.type=type;
	}
	public OrdStatistic(Double dnum,Long id,String  type)
	{
		this.dnum=dnum;
		this.id=id;
		this.type=type;
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getLnum() {
		return lnum;
	}
	public void setLnum(Long lnum) {
		this.lnum = lnum;
	}
	public Double getDnum() {
		return dnum;
	}
	public void setDnum(Double dnum) {
		this.dnum = dnum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
