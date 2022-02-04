package com.example.efinder;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.annotation.NonNull;
import com.example.efinder.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
/**
 * main activity sets fragments to their respective xml and retrieves user data and defective
 * stations from the database
 */
public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener{

private ActivityMainBinding binding;
private FirebaseAuth auth = FirebaseAuth.getInstance();
private String id;
private User currentUser = new User(auth.getCurrentUser().getEmail());
public boolean adminRights;
public static ArrayList<ChargingStation> favoriteStations = new ArrayList<>();
public  static ArrayList<ChargingStation> defectStations = new ArrayList<>();
    /**
     * initializes fragments and bottom navigation
     * @param savedInstanceState Bundle passed back to onCreate if activity needs to be recreated
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_map, R.id.navigation_search, R.id.navigation_favorites,
                R.id.navigation_admin, R.id.navigation_preference)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

    }
    /**
     * check authentication status of user, redirect him to login if status is lost or
     * not available
     * @param auth the firebase instance
     */
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth auth) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
    /**
     * initializes necessary database references pointing to the right node. Starts method of
     * to retrieve information from database
     */
    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(this);
        id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final FirebaseDatabase database = FirebaseDatabase.getInstance(getResources().getString(R.string.firebase_link));
        DatabaseReference currentUserFavoritesDb = database.getReference().child("users").child(id).child("favorites");
        DatabaseReference currentUserAdminRightsDb = database.getReference().child("users").child(id).child("admin");
        DatabaseReference defectStationsDb = database.getReference().child("defectStations");

        retrieveDbInfo(currentUserFavoritesDb, currentUserAdminRightsDb, defectStationsDb);
    }
    /**
     * initializes separate methods to retrieve database information for user and the defective
     * charging stations
     * @param currentUserFavoritesDb reference to favorite stations of user
     * @param currentUserAdminRightsDb reference to admin rights status of user
     * @param defectStationsDb reference to all defective stations
     */
    private void retrieveDbInfo(DatabaseReference currentUserFavoritesDb, DatabaseReference currentUserAdminRightsDb, DatabaseReference defectStationsDb) {
        retrieveAdminStatus(currentUserAdminRightsDb);
        retrieveFavoriteStations(currentUserFavoritesDb);
        retrieveDefectiveStations(defectStationsDb);
    }
    /**
     * sets listener for state of defective stations in the database and retrieves them
     * at the start of the main activity and after the referenced database items change
     * @param defectStationsDb reference to all defective stations
     */
    private void retrieveDefectiveStations(DatabaseReference defectStationsDb) {
        defectStationsDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                defectStations.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ChargingStation station = postSnapshot.getValue(ChargingStation.class);
                    defectStations.add(station);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed");
            }
        });
    }
    /**
     * sets listener for state of users favorite stations in the database and retrieves them
     * at the start of the main activity and after the referenced database items change
     * @param currentUserFavoritesDb reference to favorite stations of user
     */
    private void retrieveFavoriteStations(DatabaseReference currentUserFavoritesDb) {
        currentUserFavoritesDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favoriteStations.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ChargingStation station = postSnapshot.getValue(ChargingStation.class);
                    favoriteStations.add(station);
                }
                currentUser.favorites = favoriteStations;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed");
            }
        });
    }
    /**
     * sets listener for state of users admin rights status in the database and retrieves it
     * at the start of the main activity and after the referenced database item changes
     * @param currentUserAdminRightsDb reference to admin rights status of user
     */
    private void retrieveAdminStatus(DatabaseReference currentUserAdminRightsDb) {
        currentUserAdminRightsDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    adminRights = (boolean) snapshot.getValue();
                    currentUser.setIsAdmin(adminRights);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed");
            }
        });
    }
    /**
     * removes auth state listener when the application stops
     */
    @Override
    public void onStop() {
        super.onStop();
            auth.removeAuthStateListener(this);

    }
    /**
     * sign out the user when method is called
     * @see com.example.efinder.ui.preference.PreferenceFragment
     */
    public void signOut(){
        auth.signOut();
    }
    /**
     * for other fragments to retrieve the information of the current user
     * @see com.example.efinder.ui.favorites.FavoritesFragment
     * @see com.example.efinder.ui.preference.PreferenceFragment
     */
    public User getCurrentUser() {
        return currentUser;
    }
}