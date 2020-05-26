package admin.example.foodie.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpdateInfo {
    @SerializedName("name")
    private String name;
    @SerializedName("address")
    private String address;
    @SerializedName("password")
    private String password;
    @SerializedName("contactNos")
    private List<String> contactNos;

    public UpdateInfo(String name , String address , String password , List<String> contactNos) {
        this.name = name;
        this.address = address;
        this.password = password;
        this.contactNos = contactNos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
