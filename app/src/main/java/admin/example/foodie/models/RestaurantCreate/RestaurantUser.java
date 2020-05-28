package admin.example.foodie.models.RestaurantCreate;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestaurantUser {

    @SerializedName("name")
    private String name;
    @SerializedName("rest_id")
    private String rest_id;
    @SerializedName("address")
    private String address;
    @SerializedName("password")
    private String password;
    @SerializedName("contactNos")
    private List<String> contactNos;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRest_id() {
        return rest_id;
    }

    public void setRest_id(String rest_id) {
        this.rest_id = rest_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getContactNos() {
        return contactNos;
    }

    public void setContactNos(List<String> contactNos) {
        this.contactNos = contactNos;
    }

    public RestaurantUser(String name , String rest_id , String address , String password , List<String> contactNos) {
        this.name = name;
        this.rest_id = rest_id;
        this.address = address;
        this.password = password;
        this.contactNos = contactNos;
    }
}
