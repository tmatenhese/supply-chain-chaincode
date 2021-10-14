package supply.chain.management;

import java.util.ArrayList;
import java.util.List;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import com.owlike.genson.Genson;

@Contract(name = "ProductTransfer", info = @Info(title = "ProductTransfer contract", description = "A Sample Seed transfer chaincode example", version = "0.0.1-SNAPSHOT"))

@Default
public final class ProductTransfer implements ContractInterface {

	private final Genson genson = new Genson();

	private enum ProductTransferErrors {
		PRODUCT_NOT_FOUND, PRODUCT_ALREADY_EXISTS
	}
	/**
	 * Add new Product to the ledger.
	 *
	 * @param ctx       the transaction context
	 * @param id        the key for the new product
	 * @param name      the name of the new product
	 * @param area      the area of the new product
	 * @param ownername the owner of the new product
	 * @param value     the value of the new product
	 * @return the created product
	 */

	@Transaction()
	public Product addNewProduct(final Context ctx, final String id, final String description, final String quantity ,final String prize,final String variant,
			final String ownername,final String status,final String productCode) {

		ChaincodeStub stub = ctx.getStub();

		String productState = stub.getStringState(id);

		if (!productState.isEmpty()) {
			String errorMessage = String.format("Product %s already exists", id);
			System.out.println(errorMessage);
			throw new ChaincodeException(errorMessage, ProductTransferErrors.PRODUCT_ALREADY_EXISTS.toString());
		}

		Product product = new Product(id, description, quantity, prize, variant,ownername,status,productCode);

		productState = genson.serialize(product);

		stub.putStringState(id, productState);

		return product;
	}

	/**
	 * Retrieves a product based upon product Id from the ledger.
	 *
	 * @param ctx the transaction context
	 * @param id  the key
	 * @return the product found on the ledger if there was one
	 */
	@Transaction()
	public Product queryProductById(final Context ctx, final String id) {
		ChaincodeStub stub = ctx.getStub();
		String productState = stub.getStringState(id);

		if (productState.isEmpty()) {
			String errorMessage = String.format("Product %s does not exist", id);
			System.out.println(errorMessage);
			throw new ChaincodeException(errorMessage, ProductTransferErrors.PRODUCT_NOT_FOUND.toString());
		}

		Product product = genson.deserialize(productState, Product.class);
		return product;
	}

	/**
	 * Changes the owner of a product on the ledger.
	 *
	 * @param ctx      the transaction context
	 * @param id       the key
	 * @param newOwner the new owner
	 * @return the updated product
	 */
	@Transaction()
	public Product changeProductOwnership(final Context ctx, final String id, final String newProductOwner) {
		ChaincodeStub stub = ctx.getStub();

		String productState = stub.getStringState(id);

		if (productState.isEmpty()) {
			String errorMessage = String.format("Product %s does not exist", id);
			System.out.println(errorMessage);
			throw new ChaincodeException(errorMessage, ProductTransferErrors.PRODUCT_NOT_FOUND.toString());
		}

		Product product = genson.deserialize(productState, Product.class);

		Product newProduct = new Product(product.getId(), product.getDescription(), product.getQuantity(), product.getPrize(),product.getVariant(), newProductOwner,product.getStatus(),product.getProductCode());

		String newProductState = genson.serialize(newProduct);

		stub.putStringState(id, newProductState);

		return newProduct;
	}
	@Transaction()
	public Product changeProductStatus(final Context ctx, final String productId, final String newStatus) {
	    ChaincodeStub stub = ctx.getStub();

	    String productState = stub.getStringState(productId);

	    if (productState.isEmpty()) {
	        String errorMessage = String.format("Product %s does not exist", productId);
	        System.out.println(errorMessage);
	        throw new ChaincodeException(errorMessage, "Product not found");
	    }

	    Product product = genson.deserialize(productState, Product.class);

		Product newProduct = new Product(product.getId(), product.getDescription(), product.getQuantity(), product.getPrize(),product.getVariant(), product.getOwner(),newStatus,product.getProductCode());	    
	    String newProductState = genson.serialize(newProduct);
	    stub.putStringState(productId, newProductState);

	    return newProduct;
	}	
	@Transaction()
	public String queryProductsByRange(final Context ctx,final String startKey, final String endKey) {
        ChaincodeStub stub = ctx.getStub();

        List<ProductQueryResult> queryResults = new ArrayList<ProductQueryResult>();

        QueryResultsIterator<KeyValue> results = stub.getStateByRange(startKey, endKey);

        for (KeyValue result: results) {
        	Product product = genson.deserialize(result.getStringValue(), Product.class);
            queryResults.add(new ProductQueryResult(result.getKey(), product));
        }

        final String response = genson.serialize(queryResults);
        return response;        
	}

}