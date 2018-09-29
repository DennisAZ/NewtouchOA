package com.psit.struts.BIZ;

import java.util.List;

import org.hibernate.Query;

import com.psit.struts.entity.ERPPurchase;
import com.psit.struts.entity.LimUser;
import com.psit.struts.entity.ProdIn;
import com.psit.struts.entity.ProdOut;
import com.psit.struts.entity.ProdStore;
import com.psit.struts.entity.PurOrd;
import com.psit.struts.entity.RPuoPro;
import com.psit.struts.entity.SupInvoice;
import com.psit.struts.entity.SupPaidPast;
import com.psit.struts.entity.SupProd;
import com.psit.struts.entity.Supplier;
import com.psit.struts.entity.WhRec;


public interface SupBIZ {
	public List<Supplier> listSupplier(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listSupplierCount(String[]args);
	
	public Supplier findSupplierById(String id);
	public void saveSupplier(Supplier supplier,LimUser curUser);
	public void deleteSupplier(String id);
	
	/* PurOrd */
	public List<PurOrd> listPurOrd(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public List<ERPPurchase> listPurOrdERP(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listPurOrdCount(String[]args);
	public int listPurOrdCountERP(String[]args);
	public PurOrd getPurOrdById(String id);
	public void savePurOrd(PurOrd purOrd, LimUser curUser);
	public void updatePurOrd(PurOrd purOrd);
	public void deletePurOrd(String id);
	
	/*RPuoPro*/
	public int listRPuoProCount(String []args);
	public List<RPuoPro> listRPuoPro(String[] args, String orderItem,String isDe,
			int currentPage, int pageSize );
	
	/*ProdIn*/
 	public int listProdInCount(String[] args);
	
	public List<ProdIn> listProdIn(String[] args,String orderItem, String isDe,int currentPage, int pageSize);
	public void delProdIn(String id);
	public ProdIn getProdInById(String id);
	public void saveProdIn(ProdIn prodIn,LimUser curUser);
	public void cancleProdIn(String id);
	
	/*ProdStore*/
	public ProdStore getProdStoreById(String id);
	public void saveProdStore(ProdStore prodStore, LimUser curUser);
	public int listProdStoreCount(String[] args);
	public void deleteProdStore(String id);
	public List<ProdStore> listProdStore(String[] args, String orderItem,String isDe, int currentPage,int pageSize);
	
	/*ProdOut*/
	public int listProdOutCount(String[] args);
	public ProdOut getProdOutById(String id);
	public List<ProdOut> listProdOut(String[] args,String orderItem,String isDe,int currnetPage,int pageSize);
	public void delProdOut(String pouId);
	public void saveProdOut(ProdOut prodOut, LimUser curUser);
	public void cancleProdOut(String id);
	
	/*WhRec*/
	public int listWhRecCount(String[] args);
	
	public List<WhRec> listWhRec(String[] args, String orderItem, String isDe,int currentPage, int pageSize);
	/**
	 * 锟斤拷锟斤拷晒锟斤拷锟斤拷锟较�<br>
	 */
	public void saveRpp(List<RPuoPro> rppList, String puoId);
	/**
	 * 锟缴癸拷锟斤拷锟斤拷品锟斤拷细锟叫憋拷<br>
	 * @param puoId 锟缴癸拷ID
	 * @return List<RPuoPro> 锟缴癸拷锟斤拷锟斤拷品锟斤拷细锟叫憋拷
	 */
	public List<RPuoPro> listPuoPro(String puoId);
	
	/*SupProd*/
	public int listSupProdCount(String []args);
	public List<SupProd> listSupProd(String[] args, String orderItem,String isDe,
			int currentPage, int pageSize );
	public SupProd getSupProdById(String id);
	public void saveSupProd(SupProd supProd, LimUser curUser);
	public void delSupProd(String id);
	/**
	 * 锟斤拷莨锟接︼拷锟絀d锟斤拷锟揭癸拷应锟教诧拷品<br>
	 * @param supId 锟斤拷应锟斤拷Id
	 * @return 锟斤拷应锟教诧拷品锟叫憋拷
	 */
	public List<SupProd> listBySupId(String supId);
	
	/*SupPaidPast*/
	public List<SupPaidPast> listSupPaidPast(String[]args, String orderItem,
			String isDe, int currentPage, int pageSize);
	public int listSupPaidPastCount(String[]args);
	public SupPaidPast getSupPaidPast(String id);
	public void saveSupPaidPast(SupPaidPast supPaidPast,LimUser crUser);
	public void delSupPaidPast(String id);
	
	/*SupInvoice*/
	public List<SupInvoice> listSupInvoice(String[] args, String orderItem, String isDe,
			int currentPage, int pageSize);
	public int listSupInvoiceCount(String[] args);
	public SupInvoice getSupInvoice(String suiId);
	public void saveSupInvoice(SupInvoice supInvoice,LimUser curUser);
	public void delSupInvoice(String suiId);
}