package ggc.core;

import ggc.core.exception.DuplicatePartnerCoreException;
import ggc.core.exception.DuplicateProductCoreException;
import ggc.core.exception.ImportFileException;
import ggc.core.exception.InvalidDateCoreException;
import ggc.core.exception.MissingFileAssociationException;
import ggc.core.exception.UnavailableFileException;
import ggc.core.exception.UnavailableProductCoreException;
import ggc.core.exception.UnknownUserCoreException;
import ggc.core.exception.UnknownProductCoreException;
import ggc.core.exception.UnknownTransactionCoreException;
import ggc.core.exception.AlreadyPaidTransactionCoreException;
import ggc.core.exception.BadEntryException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;

/** Facade for access. */
public class WarehouseManager {

	/** Name of file storing current warehouse. */
	private String _filename = "";

	/** The wharehouse itself. */
	private Warehouse _warehouse = new Warehouse();

	public String getFilename() {
		return _filename;
	}

	public Warehouse getWarehouse() {
		return _warehouse;
	}

	/*
	 * Time management of the WarehouseManager
	 */
	public int displayDate() {
		return _warehouse.displayDate();
	}

	/**
	 * 
	 * @param days
	 * @return advancedDate
	 * @throws InvalidDateCoreException
	 */
	public int advanceDate(int days) throws InvalidDateCoreException {
		return _warehouse.advanceDate(days);
	}

	/*
	 * Currency management of the WarehouseManager
	 */
	public double getAccountingBalance() {
		return _warehouse.getAccountingBalance();
	}

	public double getAvailableBalance() {
		return _warehouse.getAvailableBalance();
	}

	/*
	 * Partners management of the WarehouseManager
	 */

	public List<Partner> getSortedPartners() {
		return _warehouse.getSortedPartners();
	}

	public Partner getPartner(String id) throws UnknownUserCoreException {
		return _warehouse.getPartner(id);
	}

	public void registerPartner(String name, String id, String address) throws DuplicatePartnerCoreException {
		_warehouse.registerPartner(name, id, address);
	}

	public Collection<Transaction> getPartnerAcquistions(String partnerId) throws UnknownUserCoreException {
		return _warehouse.getPartnerAcquistions(partnerId);
	}

	public List<Transaction> getPartnerSales(String partnerId) throws UnknownUserCoreException {
		return _warehouse.getPartnerSales(partnerId);
	}

	/*
	 * Product management of the WarehouseManager
	 */

	public List<Product> getSortedProducts() {
		return _warehouse.getSortedProducts();
	}

	public Product getProduct(String id) throws UnknownProductCoreException {
		return _warehouse.getProduct(id);
	}

	/**
	 * @param product
	 * @throws DuplicateProductCoreException
	 * @throws UnknownProductCoreException
	 */

	public void createAggregateProduct(String productId, Double alpha, List<String> productIds, List<Integer> quantitys,
			int numComponents) throws DuplicateProductCoreException {
		_warehouse.createAggregateProduct(productId, alpha, productIds, quantitys, numComponents);
	}

	public void createSimpleProduct(String id) throws DuplicateProductCoreException {
		_warehouse.createSimpleProduct(id);
	}

	public boolean isAggregateProduct(String id) {
		return _warehouse.isAggregateProduct(id);
	}

	public int getAvailableStock(String productId) {
		return _warehouse.getAvailableStock(productId);
	}

	/*
	 * Batch management of the WarehouseManager
	 */

	public List<Batch> getSortedBatches() {
		return _warehouse.getSortedBatches();
	}

	public List<Batch> getSortedBatchesUnderLimit(double priceLimit) {
		return _warehouse.getSortedBatchesUnderLimit(priceLimit);
	}

	public List<Batch> getBatchesByPartner(String id) throws UnknownUserCoreException {
		return _warehouse.getBatchesByPartner(id);
	}

	public List<Batch> getBatchesByProduct(String id) throws UnknownProductCoreException {
		return _warehouse.getBatchesByProduct(id);
	}

	/*
	 * Transaction management of the WarehouseManager
	 */

	public Transaction getTransaction(int id) throws UnknownTransactionCoreException {
		return _warehouse.getTransaction(id);
	}

	public Collection<Transaction> getPaidTransactionsByPartner(String partnerId) throws UnknownUserCoreException {
		return _warehouse.getPaidTransactionsByPartner(partnerId);
	}

	public void registerAcquisition(String partnerId, String productId, double price, int quantity)
			throws UnknownProductCoreException, UnknownUserCoreException {
		_warehouse.registerAcquisition(partnerId, productId, price, quantity);
	}

	public void registerSaleByCredit(String productId, String partnerId, int deadline, int quantity)
			throws UnavailableProductCoreException, UnknownUserCoreException, UnknownProductCoreException {
		_warehouse.registerSaleByCredit(productId, partnerId, deadline, quantity);
	}

	public void saleAggProduct(String partnerId, String productId, int deadline, int quantity)
			throws UnavailableProductCoreException, UnknownProductCoreException, UnknownUserCoreException {
		_warehouse.saleAggProduct(partnerId, productId, deadline, quantity);
	}

	public void registerBreakdown(String partnerId, String productId, int quantity)
			throws UnavailableProductCoreException {
		_warehouse.registerBreakdown(partnerId, productId, quantity);
	}

	public void payTransaction(int transactionId) throws UnknownTransactionCoreException, AlreadyPaidTransactionCoreException {
		_warehouse.payTransaction(transactionId);
	}

	/*
	 * Notification management of the Warehouse
	 */

	public List<Notification> getNotifications(String partnerId) throws UnknownUserCoreException {
		return _warehouse.getNotifications(partnerId);
	}

	public void clearAllNotifications(String partnerId) throws UnknownUserCoreException {
		_warehouse.clearAllNotifications(partnerId);
	}

	public void toogleProductNotifications(String partnerId, String productId)
			throws UnknownUserCoreException, UnknownProductCoreException {
		_warehouse.toogleProductNotifications(partnerId, productId);
	}

	/**
	 * @@throws IOException
	 * @@throws FileNotFoundException
	 * @@throws MissingFileAssociationException
	 */
	public void save() throws IOException, FileNotFoundException, MissingFileAssociationException {
		ObjectOutputStream obOut = null;
		try {
			if (_filename == "") { // if file has no name
				throw new FileNotFoundException();
			}
			ObjectOutputStream dOut = new ObjectOutputStream(new FileOutputStream(_filename));
			obOut = new ObjectOutputStream(dOut);
			obOut.writeObject(_warehouse);
		} finally {
			if (obOut != null)
				obOut.close();
		}
	}

	/**
	 * @@param filename
	 * @@throws MissingFileAssociationException
	 * @@throws IOException
	 * @@throws FileNotFoundException
	 */
	public void saveAs(String filename) throws MissingFileAssociationException, FileNotFoundException, IOException {
		_filename = filename;
		save();
	}

	/**
	 * @@param filename
	 * @@throws UnavailableFileException
	 */
	public void load(String filename) throws UnavailableFileException, ClassNotFoundException, IOException {
		ObjectInputStream obIn = null;
		try {
			FileInputStream fpin = new FileInputStream(filename);
			ObjectInputStream objIn = new ObjectInputStream(fpin);
			obIn = new ObjectInputStream(objIn);
			Object anObject = obIn.readObject();
			_warehouse = (Warehouse) anObject;
			_filename = filename;

		} finally {
			if (obIn != null)
				obIn.close();
		}
	}

	/**
	 * @param textfile
	 * @throws ImportFileException
	 */
	public void importFile(String textfile) throws ImportFileException {
		try {
			_warehouse.importFile(textfile);
		} catch (IOException | BadEntryException e) {
			throw new ImportFileException(textfile, e);
		}
	}

}
