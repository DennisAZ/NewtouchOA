package oa.spring.util;

public class StaticData {
	//销售订单状态
	//其实销售只有3种状态：新建、执行中、完成
	//审核的状态在工作流中体现
	public static String HAND_CREATE = "-1";//新建未启用状态
	public static String NEW_CREATE = "0";//新建状态
	public static String AUDITING = "1";//审核中
	public static String PASSING = "2";//审核通过
	public static String NO_PASSING = "3";//审核没通过
	public static String RUNNING = "4";//执行中
	public static String OVER = "5";//已完成
	public static String ALREADY_SEND = "6";//已发货，客户还没验收
	public static String CUSTOMER_NO_AGREE = "7";//客户验收不通过，有退货
	public static String PRODUCE_PICK_STATUS = "8";//生产计划已到达生产领料步骤
	public static String CHECK_IN_STATUS = "9";//生产计划已到达请检入库步骤
	public static String PRODUCE_RETURN_STATUS = "10";//生产计划已到达生产退料步骤
	
	
	//-------------db_log和cache_product中flag值-------------------
	public static String PUR = "1";//采购入库
	public static String DB = "2";//手动新建入库
	public static String SALE = "3";//销售出库
	public static String SALE_RETURN = "4";//退货入库（销售退货）
	public static String PUR_RETURN = "5";//退货出库库（采购退货）
	public static String LOSS = "6";//库存损耗
	public static String PRODUCE_PICK = "7";//生产领料
	public static String CHECK_IN = "8";//生产请检入库
	public static String PRODUCE_RETURN = "9";//生产退料
	//-------------crafts中is_using是否选中-------------------
	public static String YES_USING="1";//启用
	public static String NO_USING="2";//不启用
	public static String getOrderStatus(String status){
		if(NEW_CREATE.equals(status)){
			return "新建状态";
		}
		else if(AUDITING.equals(status)){
			return "审核中";
		}
		else if(PASSING.equals(status)){
			return "审核通过";
		}
		else if(NO_PASSING.equals(status)){
			return "审核没通过";
		}
		else if(RUNNING.equals(status)){
			return "执行中";
		}
		else if(OVER.equals(status)){
			return "已完成";
		}
		else if(ALREADY_SEND.equals(status)){
			return "已发货，客户还没验收";
		}
		else if(CUSTOMER_NO_AGREE.equals(status)){
			return "客户验收不通过，有退货";
		}
		else if(PRODUCE_PICK_STATUS.equals(status)){
			return "已领料生产";
		}
		else if(CHECK_IN_STATUS.equals(status)){
			return "已生产入库";
		}
		else if(PRODUCE_RETURN_STATUS.equals(status)){
			return "已完成，有退料";
		}
		return "";
	}
	
	//付款方式
	public static String CHECK = "支票";
	public static String CASH = "现金";
	public static String POSTAL_REMITTANCE = "邮政回款";
	public static String BANK_WIRE_TRANSFER = "银行电汇";
	public static String ONLINE_PAYMENT = "网上支付";
	public static String ACCEPTANCES = "承兑汇票";
	public static String OTHERS = "其他";
	
	//编号汇总
	public static String CODE1 = "仓库发货单编号";
	public static String CODE2 = "仓库收货单编号";
	public static String CODE3 = "销售订单编号";
	public static String CODE4 = "销售货单编号";
	public static String CODE5 = "销售回款单编号";
	public static String CODE6 = "采购申请单编号";
	public static String CODE7 = "采购单订单编号";
	public static String CODE8 = "采购货单编号";
	public static String CODE9 = "采购出款单编号";
	public static String CODE10 = "财务应收单编号";
	public static String CODE11 = "财务应收明细编号";
	public static String CODE12 = "财务应付单编号";
	public static String CODE13 = "财务应付明细编号";
	public static String CODE14 = "合同编号";
	public static String CODE15 = "图号";
	
}
