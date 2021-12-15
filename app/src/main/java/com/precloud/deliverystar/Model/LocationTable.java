package com.precloud.deliverystar.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationTable  {


    @SerializedName("accuracy")
    @Expose
    private float accuracy;
    @SerializedName("altitude")
    @Expose
    private double altitude;
    @SerializedName("bearing")
    @Expose
    private float bearing;
    @SerializedName("bearingAccuracyDegrees")
    @Expose
    private int bearingAccuracyDegrees;
    @SerializedName("complete")
    @Expose
    private boolean complete;
    @SerializedName("elapsedRealtimeNanos")
    @Expose
    private int elapsedRealtimeNanos;

    @SerializedName("fromMockProvider")
    @Expose
    private boolean fromMockProvider;
    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("longitude")
    @Expose
    private double longitude;
    @SerializedName("provider")
    @Expose
    private String provider;
    @SerializedName("speed")
    @Expose
    private float speed;
    @SerializedName("speedAccuracyMetersPerSecond")
    @Expose
    private int speedAccuracyMetersPerSecond;
    @SerializedName("time")
    @Expose
    private long time;
    @SerializedName("verticalAccuracyMeters")
    @Expose
    private int verticalAccuracyMeters;

    @SerializedName("OrderId")
    @Expose
    private String OrderId;

    @SerializedName("Online_status")
    @Expose
    private String Online_status;

    public LocationTable(String OrderId,float accuracy, double altitude, double latitude, double longitude, String provider, float speed, long time) {

      this.OrderId = OrderId;
        this.accuracy = accuracy;
        this.altitude = altitude;
        this.latitude = latitude;
        this.longitude = longitude;
        this.provider = provider;
        this.speed = speed;
        this.time = time;

    }

    public LocationTable(double latitude, double longitude, String orderId, String online_status) {
        this.latitude = latitude;
        this.longitude = longitude;
        OrderId = orderId;
        Online_status = online_status;
    }

    public String getOnline_status() {
        return Online_status;
    }

    public void setOnline_status(String online_status) {
        Online_status = online_status;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public float getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    public int getBearingAccuracyDegrees() {
        return bearingAccuracyDegrees;
    }

    public void setBearingAccuracyDegrees(int bearingAccuracyDegrees) {
        this.bearingAccuracyDegrees = bearingAccuracyDegrees;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public int getElapsedRealtimeNanos() {
        return elapsedRealtimeNanos;
    }

    public void setElapsedRealtimeNanos(int elapsedRealtimeNanos) {
        this.elapsedRealtimeNanos = elapsedRealtimeNanos;
    }

    public boolean isFromMockProvider() {
        return fromMockProvider;
    }

    public void setFromMockProvider(boolean fromMockProvider) {
        this.fromMockProvider = fromMockProvider;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getSpeedAccuracyMetersPerSecond() {
        return speedAccuracyMetersPerSecond;
    }

    public void setSpeedAccuracyMetersPerSecond(int speedAccuracyMetersPerSecond) {
        this.speedAccuracyMetersPerSecond = speedAccuracyMetersPerSecond;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getVerticalAccuracyMeters() {
        return verticalAccuracyMeters;
    }

    public void setVerticalAccuracyMeters(int verticalAccuracyMeters) {
        this.verticalAccuracyMeters = verticalAccuracyMeters;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }
}
