package yh.subsys.oa.coefficient.act;

import java.sql.Connection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.form.YHFOM;
import yh.subsys.oa.coefficient.data.YHCoefficient;
import yh.subsys.oa.coefficient.logic.YHCoefficientLogic;

public class YHCoefficientAct {
	private YHCoefficientLogic logic = new YHCoefficientLogic();

	public String addCoefficient(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			Map<String, String[]> map = request.getParameterMap();
			YHCoefficient coefficient = (YHCoefficient) YHFOM.build(map, YHCoefficient.class, "");
			int count = this.logic.isHaveScoreLogic(dbConn);
			if (count == 0) {
				this.logic.addCoefficientLogic(dbConn, coefficient);
			} else {
				int seqId = this.logic.getSeqIdLogic(dbConn);
				YHCoefficient coefficient2 = this.logic.getCoefficientLogic(dbConn, seqId);
				if (coefficient2 != null) {
					coefficient2.setYearScore(coefficient.getYearScore());
					coefficient2.setMonthScore(coefficient.getMonthScore());
					coefficient2.setChiefScore(coefficient.getChiefScore());
					coefficient2.setCheckScore(coefficient.getCheckScore());
					coefficient2.setAwardScore(coefficient.getAwardScore());
					this.logic.updateCoefficientLogic(dbConn, coefficient2);
				}
			}
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功添加数据");
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	
	/**
	 *获取系数信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String getCoefficient(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Connection dbConn = null;
		try {
			YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
			dbConn = requestDbConn.getSysDbConn();
			int seqId = this.logic.getSeqIdLogic(dbConn);
			YHCoefficient coefficient = this.logic.getCoefficientLogic(dbConn, seqId);
			StringBuffer data = YHFOM.toJson(coefficient);
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
			request.setAttribute(YHActionKeys.RET_MSRG, "成功返回数据");
			request.setAttribute(YHActionKeys.RET_DATA, data.toString());
		} catch (Exception e) {
			request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
			request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
			throw e;
		}
		return "/core/inc/rtjson.jsp";
	}
	
	
	
	
}
