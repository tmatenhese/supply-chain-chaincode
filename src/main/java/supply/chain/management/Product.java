package supply.chain.management;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import java.util.Objects;

@DataType()
public final class Product {

	@Property()
	private final String id;

	@Property()
	private final String description;

	@Property()
	private final String variant;
	
	@Property()
	private final String prize;

	@Property()
	private final String quantity;
	@Property
	private final String owner;
	
	@Property
	private final String status;	
	
	@Property
	private final String productCode;
	
	public String getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public String getVariant() {
		return variant;
	}

	public String getOwner() {
		return owner;
	}

	public String getPrize() {
		return prize;
	}

	public String getQuantity() {
		return quantity;
	}

	public String getStatus() {
		return status;
	}
	public String getProductCode() {
		return productCode;
	}
	public Product(@JsonProperty("id") final String id, @JsonProperty("description") final String description,
			@JsonProperty("quantity") final String quantity, @JsonProperty("prize") final String prize,@JsonProperty("variant") 
	        final String variant,@JsonProperty("owner") final String owner,
	        @JsonProperty("status") final String status, @JsonProperty("productCode") final String productCode) {
		this.id = id;
		this.description = description;
		this.quantity = quantity;
		this.prize = prize;
		this.variant=variant;
		this.owner=owner;
		this.status=status;
		this.productCode=productCode;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}

		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}

		Product other = (Product) obj;

		return Objects.deepEquals(new String[] { getId(), getDescription(), getQuantity()+"", getPrize()+"" ,getVariant()+"",getStatus()+"",getProductCode()},
				new String[] { other.getId(), other.getDescription(), other.getQuantity()+"", other.getPrize()+"",other.getVariant(),other.getOwner(),other.getStatus(),other.getProductCode() });
	}
	@Override
	public int hashCode() {
		return Objects.hash(getId(), getDescription(),getQuantity(), getPrize(),getVariant(),getOwner(),getStatus(),getProductCode());
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [id=" + id + ", description=" + description
				+ ", prize=" + prize + ", quantity=" + quantity + ", variant=" + variant + ", owner=" + owner + ", status=" + status + ", productCode=" + productCode +"]";
	}

}