package com.precloud.deliverystar.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetRiderStatusResponse {
    @SerializedName("online_status")
    @Expose
    private String onlineStatus;

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

}
