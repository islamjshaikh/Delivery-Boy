package com.precloud.deliverystar.Model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class LoginWrapper {
      @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("result")
       @Expose
        private List<LoginResponse> result = null;

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
      public List<LoginResponse> getResult() {
        return result;
    }

    public void setResult(List<LoginResponse> result) {
        this.result = result;
    }

    }
