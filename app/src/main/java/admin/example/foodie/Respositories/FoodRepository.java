package admin.example.foodie.Respositories;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import admin.example.foodie.LoginActivity;
import admin.example.foodie.MainActivity;
import admin.example.foodie.models.Food;
import admin.example.foodie.models.ResponseRestaurant;
import admin.example.foodie.models.Restaurant;
import admin.example.foodie.org.example.foodie.apifetch.FoodieClient;
import admin.example.foodie.org.example.foodie.apifetch.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodRepository {

    public static FoodRepository foodRepository;
    private FoodieClient foodieClient;

    public FoodRepository() {
        foodieClient = ServiceGenerator.createService(FoodieClient.class);
    }

    public static FoodRepository getInstance() {
        if (foodRepository == null) {
            foodRepository = new FoodRepository();
        }

        return foodRepository;

    }

    public static FoodRepository getFoodRepository() {
        return foodRepository;
    }

    public MutableLiveData<List<Food>> getFoods() {

        MutableLiveData<List<Food>> restaurantData = new MutableLiveData<>();



        Call<ResponseRestaurant> call = foodieClient.getFood(MainActivity.rest_id);

        call.enqueue(new Callback<ResponseRestaurant>() {
            @Override
            public void onResponse(Call<ResponseRestaurant> call, Response<ResponseRestaurant> response) {

                Log.i("Response", String.valueOf(response.code()));
                if (response.isSuccessful()) {
                    restaurantData.setValue(response.body().getRestaurant().getFoods());

                }

            }

            @Override
            public void onFailure(Call<ResponseRestaurant> call, Throwable t) {

                Log.i("response", String.valueOf(LoginActivity.rest_id));
                restaurantData.setValue(null);
            }
        });


        return restaurantData;


    }

}
