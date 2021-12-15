package com.precloud.deliverystar.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderResponse implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("reference_no")
    @Expose
    private String referenceNo;
    @SerializedName("source_location")
    @Expose
    private String sourceLocation;
    @SerializedName("destination_location")
    @Expose
    private String destinationLocation;
    @SerializedName("source_lat_long")
    @Expose
    private String sourceLatLong;
    @SerializedName("destination_lat_long")
    @Expose
    private String destinationLatLong;
    @SerializedName("recipient_name")
    @Expose
    private String recipientName;
    @SerializedName("recipient_number")
    @Expose
    private String recipientNumber;
    @SerializedName("address_type")
    @Expose
    private String addressType;
    @SerializedName("landmark")
    @Expose
    private String landmark;
    @SerializedName("order_status_id")
    @Expose
    private String orderStatusId;
    @SerializedName("payment_mode")
    @Expose
    private String paymentMode;
    @SerializedName("payment_amount")
    @Expose
    private String paymentAmount;
    @SerializedName("restaurant_id")
    @Expose
    private String restaurantId;
    @SerializedName("restaurant_name")
    @Expose
    private String restaurantName;
    @SerializedName("restaurant_phone")
    @Expose
    private String restaurant_phone;
    @SerializedName("order_estimated_pickup_time")
    @Expose
    private String order_estimated_pickup_time;

    @SerializedName("remark")
    @Expose
    private String remark;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getSourceLocation() {
        return sourceLocation;
    }

    public void setSourceLocation(String sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    public String getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(String destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public String getSourceLatLong() {
        return sourceLatLong;
    }

    public void setSourceLatLong(String sourceLatLong) {
        this.sourceLatLong = sourceLatLong;
    }

    public String getDestinationLatLong() {
        return destinationLatLong;
    }

    public void setDestinationLatLong(String destinationLatLong) {
        this.destinationLatLong = destinationLatLong;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientNumber() {
        return recipientNumber;
    }

    public void setRecipientNumber(String recipientNumber) {
        this.recipientNumber = recipientNumber;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(String orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurant_phone() {
        return restaurant_phone;
    }

    public void setRestaurant_phone(String restaurant_phone) {
        this.restaurant_phone = restaurant_phone;
    }

    public String getOrder_estimated_pickup_time() {
        return order_estimated_pickup_time;
    }

    public void setOrder_estimated_pickup_time(String order_estimated_pickup_time) {
        this.order_estimated_pickup_time = order_estimated_pickup_time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
