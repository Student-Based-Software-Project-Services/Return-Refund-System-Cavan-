package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class ReturnRequest {
    private int id;
    private String orderId;
    private int customerId;
    private String customerName;
    private String productName;
    private String returnReason;
    private Date returnDate;
    private String returnStatus;
    private String refundStatus;
    private BigDecimal refundAmount;
    private int handledBy;
    private String handledByName;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public ReturnRequest() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getReturnReason() { return returnReason; }
    public void setReturnReason(String returnReason) { this.returnReason = returnReason; }
    public Date getReturnDate() { return returnDate; }
    public void setReturnDate(Date returnDate) { this.returnDate = returnDate; }
    public String getReturnStatus() { return returnStatus; }
    public void setReturnStatus(String returnStatus) { this.returnStatus = returnStatus; }
    public String getRefundStatus() { return refundStatus; }
    public void setRefundStatus(String refundStatus) { this.refundStatus = refundStatus; }
    public BigDecimal getRefundAmount() { return refundAmount; }
    public void setRefundAmount(BigDecimal refundAmount) { this.refundAmount = refundAmount; }
    public int getHandledBy() { return handledBy; }
    public void setHandledBy(int handledBy) { this.handledBy = handledBy; }
    public String getHandledByName() { return handledByName; }
    public void setHandledByName(String handledByName) { this.handledByName = handledByName; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}