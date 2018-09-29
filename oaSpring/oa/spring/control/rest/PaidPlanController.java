package oa.spring.control.rest;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import oa.spring.po.PaidPlan;
import oa.spring.po.SaleOrder;
import oa.spring.service.PaidPlanService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/paidPlan")
public class PaidPlanController {

	@Autowired
	private PaidPlanService paidPlanService;

	private static final Logger logger = Logger
			.getLogger(PaidPlanController.class);
	@RequestMapping(value = "paidPlanSave", method = RequestMethod.POST)
	public @ResponseBody
	String addSaleOrder(HttpServletRequest request) {
		String rtStr = "0";
		String paidCode = request.getParameter("paidCode");
		String paidName = request.getParameter("paidName");
		String total = request.getParameter("total");
		Double totals=Double.parseDouble(total);
		String paidStatus=request.getParameter("paidStatus");
		String alreadyTotal=request.getParameter("alreadyTotal");
		Double alreadyTotals=Double.parseDouble(alreadyTotal);
		String orderCode=request.getParameter("orderCode");
		String salePaid=request.getParameter("salePaid");
		Double salePaids=Double.parseDouble(salePaid);
		String remark=request.getParameter("remark");
		SaleOrder saleOrder=new SaleOrder();
		PaidPlan paidPlan=new PaidPlan(paidCode,paidName,saleOrder,paidStatus,totals,alreadyTotals,salePaids,remark);
		
		try {
			paidPlanService.addPaidPlan(paidPlan);
			
		} catch (Exception e) {
			e.printStackTrace();
			rtStr = "1";
		}
		return rtStr;
	}

}
