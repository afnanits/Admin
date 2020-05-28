package admin.example.foodie.ServiceClass;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.example.foodie.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import admin.example.foodie.MainActivity;
import admin.example.foodie.WelcomeActvity;
import admin.example.foodie.models.Order;
import admin.example.foodie.models.OrderFood;
import admin.example.foodie.org.example.foodie.apifetch.FoodieClient;
import admin.example.foodie.org.example.foodie.apifetch.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForeGroundService extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceDemo1";

    int DEFAULT_SYNC_INTERVAL=5*1000;
    String token= WelcomeActvity.token;
    List<Order> viewList=new ArrayList<>();
    private Handler mHandler;



    String json;
    //  MainActivity activity=new MainActivity();
    Gson gson = new Gson();

    Context context=WelcomeActvity.getInstance();


    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {


        mHandler = new Handler();

        if (viewList==null)getPrefernce(getSharedPreferences("admin.example.foodie",MODE_PRIVATE));
if(token==null) {
    SharedPreferences sharedPreferences = getSharedPreferences("admin.example.foodie", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    getPrefernce(sharedPreferences);

    token = sharedPreferences.getString("token", null);
}
        mHandler.post(runnableService);
        startInForeground();
        return START_STICKY;
    }



    private Runnable runnableService = new Runnable() {
        @Override
        public void run() {
            //order=new Order(new User("8840102246",""),)

            Log.i("Running","true");



            syncData(token);



            //syncData(token);
            createNotificationChannel();

            // Repeat this runnable code block again every ... min
            mHandler.postDelayed(runnableService, DEFAULT_SYNC_INTERVAL);
        }
    };










    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startInForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Example Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Checking For new Orders")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
    }


    private synchronized void syncData(String token) {
        FoodieClient foodieClient = ServiceGenerator.createService(FoodieClient.class);
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("admin.example.foodie", MODE_PRIVATE);

        Call<List<Order>> call = foodieClient.getNotified(token);
        getPrefernce(sharedPreferences);

        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {

                Log.i("Message", String.valueOf(response.code()));

                if (response.body()!=null)
                    if (response.body().size() != 0) {
                        Log.i("ResponseUser", response.body().get(0).getUser().getName());
                        for (int i=0;i<response.body().size();i++){

                            viewList.add(response.body().get(i));
                            sendNotification(response.body().get(i));
                        }

                        saveData(sharedPreferences);

                    }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Log.i("ResponseMessage", t.getMessage());
            }
        });


        //   Toast.makeText(this, "I am running in background", Toast.LENGTH_SHORT).show();
        Log.d("service: ", "running");
        // call your rest service here
    }

    public void sendNotification(Order order) {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("notification","jfjfb");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder;
        if (order != null) {
            builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("New Order")
                    .setContentText("Customer"+" "+order.getUser().getName() + "  " + "ORDER Total:" + getTotal(order.getFoodList()))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
        } else {
            builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("New Order")
                    .setContentText("Fuck get the damn order")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Name";
            String description = "Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    public  void getPrefernce(SharedPreferences sharedPreferences) {

        if (viewList!=null)
        if (viewList.isEmpty()) {

            json = sharedPreferences.getString("orderItems", null);
            String id = sharedPreferences.getString("rest_id", null);
            if (json != null) {
                Type type = new TypeToken<List<Order>>() {
                }.getType();
                viewList = gson.fromJson(json, type);
            }


        }
    }
    public void saveData (SharedPreferences sharedPreferences){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        json = gson.toJson(viewList);
        editor.putString("orderItems", json);
        //editor.putString("rest_id", .rest_id);
        editor.commit();
    }


    public int getTotal(List<OrderFood> foods){
        int sum=0;
        for(int i=0;i<foods.size();i++){
            sum+=foods.get(i).getCount()*foods.get(i).getPrice();
        }
        return sum;
    }




    @Override
    public void onTaskRemoved(Intent rootIntent) {


        Intent restartServiceIntent = new Intent(getApplicationContext(),this.getClass());
        restartServiceIntent.putExtra("token",token);
        restartServiceIntent.setPackage(getPackageName());

        //   mHandler, Context.BIND_AUTO_CREATE);
        startService(restartServiceIntent);

        super.onTaskRemoved(rootIntent);
    }





}