package com.myapp.booknow.business;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.RouteListingPreference;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.myapp.booknow.Customer.C_Login;
import com.myapp.booknow.Customer.CustomerAdapter;
import com.myapp.booknow.FirestoreCallback;
import com.myapp.booknow.Utils.Appointment;
import com.myapp.booknow.Utils.AppointmentAdapter;
import com.myapp.booknow.Utils.AppointmentDateSelectionActivity;
import com.myapp.booknow.R;
import com.myapp.booknow.Utils.AppointmentListActivity;
import com.myapp.booknow.Utils.DBHelper;
import com.myapp.booknow.Utils.ServiceProviderSetNameAndServicesActivity;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;


/**
 * Dashboard (Main page) for a customer user.
 */
public class BusinessDashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private RecyclerView appointmentsRecycler_B;
    private RecyclerView specialOffers_recycle;
    private AppointmentAdapter appointmentAdapter;
    private List<Appointment> appointmentList;
    private DBHelper dbHelper;
    public ImageView menu_button;
    private TextView textViewWelcome;
    private DrawerLayout drawerLayout;


//    private Button Working_hours_redirect_btn;
//
//    private Button Editing_services_redirect_btn;
//    private Button Editing_providers_redirect_btn;
//    private Button Edit_special_hours_btn;
//    private Button appointment_list_btn;
//
//    private Button editBusinessProfile_btn;

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
        appointmentsRecycler_B.setAdapter(appointmentAdapter);
        menu_button = findViewById(R.id.menu_button);
        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Apply OnClickListener  to imageView to
        // switch from one activity to another
        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
//                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
//                    drawerLayout.openDrawer(GravityCompat.START);
//                } else {
//
//                }
            }
        });


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //specialOffers_recycle();
        appointmentsRecycler_B();
    }


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
//        getMenuInflater().inflate(R.menu.menu_main,menu );
//       return super.onCreateOptionsMenu(menu);
        int item_id = (item.getItemId());
        View Working_hours_redirect_btn = findViewById(R.id.working_hours);
        View Editing_services_redirect_btn = findViewById(R.id.edit_service);
        View Editing_providers_redirect_btn = findViewById(R.id.service_provider);
        View Edit_special_hours_btn = findViewById(R.id.specific_dates);
        View editBusinessProfile_btn = findViewById(R.id.account);


//            Intent edit_working_hours = new Intent(BusinessDashboardActivity.this, BusinessRegularHoursEditActivity.class);
//            startActivity(edit_working_hours);
//            // break;


        if (item_id == R.id.working_hours) {
            //Working_hours_redirect_btn = findViewById(R.id.working_hours);
//            Working_hours_redirect_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(BusinessDashboardActivity.this, BusinessRegularHoursEditActivity.class);
//                    startActivity(intent);
//                }
//            });
            // Log.d(DEBUG_TAG, "Starting BaseballCardDetails");


            Intent show = new Intent(this, B_Login.class);
            startActivity(show);
            return true;
//            Intent intent = new Intent(BusinessDashboardActivity.this, BusinessRegularHoursEditActivity.class);
//            startActivity(intent);


        } else if (item_id == R.id.edit_service) {
            Editing_services_redirect_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BusinessDashboardActivity.this, BusinessServicesManagementActivity.class);
                    startActivity(intent);
                }
            });

        } else if (item_id == R.id.service_provider) {
            Editing_providers_redirect_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BusinessDashboardActivity.this, ServiceProviderSetNameAndServicesActivity.class);
                    startActivity(intent);
                }
            });

//
        } else if (item_id == R.id.specific_dates) {
            Edit_special_hours_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BusinessDashboardActivity.this, BusinessSpecialHoursEditActivity.class);
                    startActivity(intent);
                }
            });

        } else if (item_id == R.id.log_out) {
            Intent edit_specific_dates = new Intent(BusinessDashboardActivity.this, C_Login.class);
            startActivity(edit_specific_dates);
//        } else
//            throw new IllegalArgumentException("menu option not implemented!!");
        }

        return super.onOptionsItemSelected(item);



    }
}


//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.bussines_dashboard_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//
//    @SuppressLint("NonConstantResourceId")
//    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
//        int item_id = (menuItem.getItemId());
//        View Working_hours_redirect_btn = findViewById(R.id.working_hours);
//        View Editing_services_redirect_btn = findViewById(R.id.edit_service);
//        View Editing_providers_redirect_btn = findViewById(R.id.service_provider);
//        View Edit_special_hours_btn = findViewById(R.id.specific_dates);
//        View editBusinessProfile_btn = findViewById(R.id.account);
//
//
////            Intent edit_working_hours = new Intent(BusinessDashboardActivity.this, BusinessRegularHoursEditActivity.class);
////            startActivity(edit_working_hours);
////            // break;
//
//
//        if (item_id == R.id.working_hours) {
//            //Working_hours_redirect_btn = findViewById(R.id.working_hours);
////            Working_hours_redirect_btn.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    Intent intent = new Intent(BusinessDashboardActivity.this, BusinessRegularHoursEditActivity.class);
////                    startActivity(intent);
////                }
////            });
//            Intent intent = new Intent(BusinessDashboardActivity.this, BusinessRegularHoursEditActivity.class);
//                  startActivity(intent);
//
//
//        } else if (item_id == R.id.edit_service) {
//            Editing_services_redirect_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(BusinessDashboardActivity.this, BusinessServicesManagementActivity.class);
//                    startActivity(intent);
//                }
//            });
//
//        } else if (item_id == R.id.service_provider) {
//            Editing_providers_redirect_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(BusinessDashboardActivity.this, ServiceProviderSetNameAndServicesActivity.class);
//                    startActivity(intent);
//                }
//            });
//
////
//        } else if (item_id == R.id.specific_dates) {
//            Edit_special_hours_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(BusinessDashboardActivity.this, BusinessSpecialHoursEditActivity.class);
//                    startActivity(intent);
//                }
//            });
//
//        } else if (item_id == R.id.log_out) {
//            Intent edit_specific_dates = new Intent(BusinessDashboardActivity.this, C_Login.class);
//            startActivity(edit_specific_dates);
//        }
//        else
//      throw new IllegalArgumentException("menu option not implemented!!");
//
//        return true;
//
//
//
//    }



//        @Override
//            public void onSuccess(List<Appointment> result) {
//                for(Appointment appointment : result){
//                    Log.d("Check appointments (RecyclerView) ",appointment.toString()+" ");
//                }
//                appointmentList.clear();
//                appointmentList.addAll(result);
//               //customerAdapter.notifyDataSetChanged();
//                Log.d("Check appointments (RecyclerView) size :",""+result.size());
//            }
//
//            @Override
//            public void onFailure(Exception e) {//error fetching appointments
//                Log.d("Check appointments (RecyclerView) ",e.getMessage());
//
//            }
//        });

//setting the appointments adapter to the recycle and binding it with the list of appointments
//customerAdapter = new CustomerAdapter(appointmentList);
//appointmentsRecycler_B.setAdapter(customerAdapter);


//        textViewWelcome = findViewById(R.id.textViewWelcome);
//        Woking_hours_redirect_btn = findViewById(R.id.Woking_hours_redirecting);
//        Editing_services_redirect_btn = findViewById(R.id.editing_services_redirecting);
//        Editing_providers_redirect_btn = findViewById(R.id.editing_providers_redirecting);
//        Edit_special_hours_btn = findViewById(R.id.SpecialHoursEdit);
//        appointment_list_btn = findViewById(R.id.appointment_list_btn);
//        editBusinessProfile_btn = findViewById(R.id.editBusinessProfile);

//        if (currentUser != null && currentUser.getEmail() != null){
//            String welcomemsg = "Hello, " + currentUser.getEmail();
//            textViewWelcome.setText(welcomemsg);
//        }
//        else{
//            textViewWelcome.setText("Hello, User");
//        }


//        Woking_hours_redirect_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(BusinessDashboardActivity.this, BusinessRegularHoursEditActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        Editing_services_redirect_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(BusinessDashboardActivity.this,BusinessServicesManagementActivity.class);
//                startActivity(intent);
//            }
//        });
//
//
//        Editing_providers_redirect_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(BusinessDashboardActivity.this, ServiceProviderSetNameAndServicesActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        Edit_special_hours_btn.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(BusinessDashboardActivity.this,BusinessSpecialHoursEditActivity.class);
//                startActivity(intent);
//            }
//        });
//
//
//        appointment_list_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(BusinessDashboardActivity.this, AppointmentDateSelectionActivity.class);
//                startActivity(intent);
//            }
//        });
//
//
//        editBusinessProfile_btn.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(BusinessDashboardActivity.this,EditBusinessProfile.class);
//                startActivity(intent);
//            }
//        });
//
//    }