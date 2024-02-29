package com.myapp.booknow.business;

import static android.app.PendingIntent.getActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalDateTime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.myapp.booknow.Customer.C_Login;
import com.myapp.booknow.FirestoreCallback;
import com.myapp.booknow.Utils.Appointment;
import com.myapp.booknow.Utils.AppointmentAdapter;
import com.myapp.booknow.R;
import com.myapp.booknow.Utils.DBHelper;
import com.myapp.booknow.Utils.ServiceProviderSetNameAndServicesActivity;
import com.myapp.booknow.Utils.User;

import java.util.ArrayList;
import java.util.List;


/**
 * Dashboard (Main page) for a customer user.
 */
public class BusinessDashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final float END_SCALE = 0.7f;// For sliding animation (navigation view)
    private FirebaseAuth mAuth;
    private RecyclerView appointmentsRecycler_B;
    private RecyclerView specialOffers_recycle;
    private List<BusinessSpecialOffers> specialOffers;
    private AppointmentAdapter appointmentAdapter;
    //    private offersAdapter offersAdapter;
    private List<Appointment> appointmentList;
    private DBHelper dbHelper;
    public ImageView menu_button;
    private TextView textViewWelcome;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private static final String TAG = "HotSpotsFragment";
    private MapView mapView;
    private GoogleMap map;


    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_business_dashboard);
        dbHelper = new DBHelper();

        appointmentsRecycler_B = findViewById(R.id.appointments_recycler);
        specialOffers_recycle = findViewById(R.id.featured_recycler);

        appointmentList = new ArrayList<>();
        appointmentAdapter = new AppointmentAdapter(appointmentList);
        appointmentAdapter = new AppointmentAdapter(appointmentList);
        appointmentsRecycler_B.setAdapter(appointmentAdapter);
        menu_button = findViewById(R.id.menu_button);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);


        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        //navigationDrawer();


        // Apply OnClickListener  to imageView to
        // switch from one activity to another
        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });







        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //specialOffers_recycle();
        appointmentsRecycler_B();
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }


    // Called when the activity is resumed after being paused or stopped
    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_home);// to ensure that the drawer is set ti "Home"
    }

//    private void specialOffers_recycle(){
//        specialOffers_recycle.setHasFixedSize(true);
//        specialOffers_recycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//
//        specialOffers = new ArrayList<BusinessSpecialOffers>();
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser curr_user = mAuth.getCurrentUser();
//        specialOffers.add(curr_user);
//        String businessId = null;
//        if (curr_user != null) {
//            businessId = curr_user.getUid();
//        }
//        assert curr_user != null;
//
//    }



//    private void specialOffers_recycle() {
//
//        specialOffers_recycle.setHasFixedSize(true);
//        specialOffers_recycle.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
//
//        specialOffers = new ArrayList<BusinessSpecialOffers>();
//
//        dbHelper.fetchSpecialOfeer(new FirestoreCallback<List<BusinessSpecialOffers>>() {
//            public void onSuccess(List<User> result) {
//                for(BusinessSpecialOffers offers : result){
//                    Log.d("Check businesses (RecyclerView) ",offers.toString()+" ");
//
//                    specialOffers.clear();
//                    specialOffers.addAll(result);
//                    specialOffers.notifyAll();
//                    Log.d("Check businesses (RecyclerView) size : ",""+result.size());
//
//                }
//            }



//            @Override
//            public void onSuccess(List<BusinessSpecialOffers> result) {
//
//            }

//            @Override
//            public void onFailure(Exception e) {//error fetching businesses
//                Log.d("Checking businesses (RecyclerView) ",e.getMessage());
//            }
//        });

//        business_adapter = new BusinessAdapter(businesses);
//
//        businessesRecycler.setAdapter(business_adapter);




    private void appointmentsRecycler_B() {
        appointmentsRecycler_B.setHasFixedSize(true);
        appointmentsRecycler_B.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        appointmentList = new ArrayList<>();

        //getting the curr customer id
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser curr_user = mAuth.getCurrentUser();
        String businessId = null;
        if (curr_user != null) {
            businessId = curr_user.getUid();
        }
        String status = "waiting";
        LocalDateTime today = LocalDate.now().atStartOfDay();


        dbHelper.fetchAppointmentsForDate_business(businessId, status,
                new FirestoreCallback<List<Appointment>>() {
                    @Override
                    public void onSuccess(List<Appointment> result) {

                        //checking:
                        for (Appointment appointment : result) {
                            Log.d("AppointmentListCheck", "appointment = " + appointment.toString());
                        }

                        appointmentList.clear();
                        appointmentList.addAll(result);
                        appointmentAdapter.notifyDataSetChanged();
                        Log.d("appointmentsList", "Number of appointments fetched: " + result.size());
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("Check appointments (RecyclerView) ", e.getMessage());
                    }
                }
        );
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(Menu.NONE, R.id.working_hours, 0, "show setting");

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int item_id = (item.getItemId());


        if (item_id == R.id.working_hours) {


            Intent show = new Intent(this, BusinessRegularHoursEditActivity.class);
            startActivity(show);
            return true;


        } else if (item_id == R.id.edit_service) {

            Intent intent = new Intent(BusinessDashboardActivity.this, BusinessServicesManagementActivity.class);
            startActivity(intent);


        } else if (item_id == R.id.service_provider) {

            Intent intent = new Intent(BusinessDashboardActivity.this, ServiceProviderSetNameAndServicesActivity.class);
            startActivity(intent);


        } else if (item_id == R.id.specific_dates) {

            Intent intent = new Intent(BusinessDashboardActivity.this, BusinessSpecialHoursEditActivity.class);
            startActivity(intent);


        } else if (item_id == R.id.log_out) {
            Intent edit_specific_dates = new Intent(BusinessDashboardActivity.this, C_Login.class);
            startActivity(edit_specific_dates);

        }else if (item_id == R.id.special_offers) {
            Intent edit_offer= new Intent(BusinessDashboardActivity.this, EditSpecialOffers.class);
            startActivity(edit_offer);

        }


        drawerLayout.closeDrawer(GravityCompat.START);
        return true;


    }
}

