package com.example.user.retrofit;


import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> name = new ArrayList<String>();
    private ArrayList<String> glucose = new ArrayList<String>();

    public interface Service{
        @FormUrlEncoded
        @POST("/users")
        Call<JsonResponse> Create(
                @Field("name") String name,
                @Field("glucose") String glucose
        );
        @FormUrlEncoded
        @PUT("/users/{id}")
        Call<JsonResponse> Update(
                @Path("id") String id,
                @Field("name") String name,
                @Field("glucose") String glucose
        );
        @GET("/users")
        Call<List<UsersResponses>> GetUsersInformations();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                PostData();
            }
        });
        Button get_button = (Button)findViewById(R.id.get_button);
        get_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetData();
            }
        });

    }

    public boolean PostData(){
        EditText editText1 = (EditText)findViewById(R.id.edittext1);
        EditText editText2 = (EditText)findViewById(R.id.edittext2);
        name.add(editText1.getText().toString());
        glucose.add(editText2.getText().toString());
        if(isNetworkConnected()) {
            Retrofit retrofit = new Retrofit
                    .Builder()
                    .baseUrl("http://jia.ee.ncku.edu.tw")
                    .addConverterFactory(GsonConverterFactory.create()).build();
            Service service = retrofit.create(Service.class);
            for(int i = 0; i < this.name.size(); i++) {
                Call<JsonResponse> post = service.Create(name.get(i), glucose.get(i));
                post.enqueue(new Callback<JsonResponse>() {
                    @Override
                    public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                        TextView textView = (TextView) findViewById(R.id.textview);
                        textView.setText(response.body().getMessage());
                    }

                    @Override
                    public void onFailure(Call<JsonResponse> call, Throwable t) {
                        TextView textView = (TextView) findViewById(R.id.textview);
                        textView.setText(t.toString());
                    }
                });
            }
            /*Call<JsonResponse> put = service.Update("5953c40f86cd4e51735883af","Hi","777");
            put.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                TextView textView = (TextView)findViewById(R.id.textview2);
                textView.setText(response.body().getMessage());
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {
                TextView textView = (TextView)findViewById(R.id.textview2);
                textView.setText(t.toString());
            }
        });*/
            name.clear();
            glucose.clear();
            return true;
        }
        else {
            TextView textView = (TextView) findViewById(R.id.textview);
            textView.setText("No internet connection");
            return false;
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }

    public boolean GetData(){
        if(isNetworkConnected()){
            Retrofit retrofit = new Retrofit
                    .Builder()
                    .baseUrl("https://api.github.com")
                    .addConverterFactory(GsonConverterFactory.create()).build();
            Service service = retrofit.create(Service.class);
            Call<List<UsersResponses>> get = service.GetUsersInformations();
            get.enqueue(new Callback<List<UsersResponses>>() {
                @Override
                public void onResponse(Call<List<UsersResponses>> call, Response<List<UsersResponses>> response) {
                    List<String> user = new ArrayList<>();
                    for(int i = 0;i < response.body().size();i++){
                        user.add(response.body().get(i).getLogin());
                    }
                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,user);
                    ListView listview = (ListView)findViewById(R.id.listview);
                    listview.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<List<UsersResponses>> call, Throwable t) {
                    TextView textView = (TextView) findViewById(R.id.textview);
                    textView.setText(t.toString());
                }
            });

            return true;
        } else{
            Toast.makeText(this, "NetWorkERROR", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
