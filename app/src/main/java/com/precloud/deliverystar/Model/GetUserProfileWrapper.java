package com.precloud.deliverystar.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetUserProfileWrapper  {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private
    List<GetUserProfileResponse> result = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<GetUserProfileResponse> getResult() {
        return result;
    }

    public void setResult(List<GetUserProfileResponse> result) {
        this.result = result;
    }


}
