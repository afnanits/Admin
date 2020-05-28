package admin.example.foodie.models;

import com.google.gson.annotations.SerializedName;

public class ResponseRestaurant {

    @SerializedName("restaurant")
    Restaurant restaurant;
    @SerializedName("token")
    String token;

    public String getToken() {
        return token;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }
}
