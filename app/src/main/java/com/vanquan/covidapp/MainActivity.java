package com.vanquan.covidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.eazegraph.lib.charts.PieChart;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {


    private TextView tv_confirmed, tv_confirmed_new, tv_active, tv_active_new, tv_recovered, tv_recovered_new, tv_death,
            tv_death_new, tv_tests, tv_tests_new, tv_date, tv_time;

    private LinearLayout lin_state_data, lin_world_data;

    private SwipeRefreshLayout swipeRefreshLayout;

    private PieChart pieChart;

    private String str_confirmed, str_confirmed_new, str_active, str_active_new, str_recovered, str_recovered_new,
            str_death, str_death_new, str_tests, str_tests_new, str_last_update_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Initialise
        Init();

        //Fetch data from API
        FetchData();

    }

    //lấy dữ liệu API
    private void FetchData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String apiUrl = "https://api.covid19india.org/data.json";

        pieChart.clearChart();

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(
                Request.Method.GET,
                apiUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

//Vì dữ liệu của json nằm trong một mảng lồng nhau, cần xác định mảng mà chúng ta muốn tìm nạp dữ liệu từ đó.
                        JSONArray all_state_jsonArray = null;
                        JSONArray testData_jsonArray = null;

                        try {
                            all_state_jsonArray=response.getJSONArray("statewise");
                            JSONObject data_india=all_state_jsonArray.getJSONObject(0);

                            testData_jsonArray=response.getJSONArray("tested");
                            JSONObject test_data_india = all_state_jsonArray.getJSONObject(0);

                            //Fetching data for India and storing it in String
                            str_confirmed = data_india.getString("confirmed");   //Confirmed cases in India
                            str_confirmed_new = data_india.getString("deltaconfirmed");   //New Confirmed cases from last update time

                            str_active = data_india.getString("active");    //Active cases in India

                            str_recovered = data_india.getString("recovered");  //Total recovered cased in India
                            str_recovered_new = data_india.getString("deltarecovered"); //New recovered cases from last update time

                            str_death = data_india.getString("deaths");     //Total deaths in India
                            str_death_new = data_india.getString("deltadeaths");    //New death cases from last update time

                            str_last_update_time = data_india.getString("lastupdatedtime"); //Last update date and time

                            str_tests = test_data_india.getString("totalsamplestested"); //Total samples tested in India
                            str_tests_new = test_data_india.getString("samplereportedtoday");   //New samples tested today

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

    }

    //new la + them
    //confirme la cu

    private void Init() {
        tv_confirmed = findViewById(R.id.activity_main_confirme);
        tv_confirmed_new = findViewById(R.id.activity_main_confirmed_new);
        tv_active = findViewById(R.id.activity_main_active);
        tv_active_new = findViewById(R.id.activity_main_active_new);
        tv_recovered = findViewById(R.id.activity_main_recovered);
        tv_recovered_new = findViewById(R.id.activity_main_recovered_new);
        tv_death = findViewById(R.id.activity_main_deaths);
        tv_death_new = findViewById(R.id.activity_main_deaths_new);
        tv_tests = findViewById(R.id.activity_main_samples);
        tv_tests_new = findViewById(R.id.activity_main_samples_new);
        tv_date = findViewById(R.id.activity_date_lastupdate_date);
        tv_time = findViewById(R.id.activity_main_lastupdate_time);

        pieChart = findViewById(R.id.activity_main_piechart);
        swipeRefreshLayout = findViewById(R.id.activity_main_swipe_refresh_layout);


    }

    //hiển thị thông tin trên thanh menu đầu
    //ctrl+O
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.menu_about){
            Toast.makeText(MainActivity.this,"About menu icon clicked",Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}