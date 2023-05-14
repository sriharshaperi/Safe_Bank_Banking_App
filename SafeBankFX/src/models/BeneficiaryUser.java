package models;

import java.util.Date;
import java.util.UUID;

public class BeneficiaryUser {
	
	private UUID beneficiaryUserId;
	private Date createdAt;
	
	public UUID getBeneficiaryUserId() {
		return beneficiaryUserId;
	}
	public void setBeneficiaryUserId(UUID beneficiaryUserId) {
		this.beneficiaryUserId = beneficiaryUserId;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	@Override
	public String toString() {
		return "BeneficiaryUser [beneficiaryUserId=" + beneficiaryUserId + ", createdAt=" + createdAt
				+ "]";
	}
}
