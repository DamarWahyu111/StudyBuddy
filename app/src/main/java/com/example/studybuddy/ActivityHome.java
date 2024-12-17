package com.example.studybuddy;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;


import com.google.android.material.navigation.NavigationView;

public class ActivityHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar); // The toolbar is used to display the Navigation button
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout); // This allows navigation with a "drawer" that
                                                        // can be slid in/out from the side of the screen.
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( // The link between the toolbar and drawerLayout
                                                            // can set the menu button (usually a three-line icon)
                                                             // that opens/closes the drawer.
                this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState(); //  The toolbar is synchronized with the DrawerLayout state (open/closed).
        if (savedInstanceState == null) { // == comparison operator, checks savedInstanceState value is not null
            replaceFragment(new HomeFragment()); // we use Fragment to make UI management dynamic
            navigationView.setCheckedItem(R.id.nav_home);
        }
        updateNavHeader(navigationView);
    }

    private void updateNavHeader(NavigationView navigationView) {
        android.view.View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.nav_username);
        SharedPreferences sharedPreferences = getSharedPreferences("StudyBuddyPrefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("logged_in", "Pengguna");
        navUsername.setText(username);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        String title = item.getTitle().toString();

        if (title.equals("Home")) {
            selectedFragment = new HomeFragment();
        } else if (title.equals("Create Task")) {
            selectedFragment = new CreateFragment();
        } else if (title.equals("About")) {
            selectedFragment = new AboutFragment();
        } else if (title.equals("Logout")) {
            SharedPreferences sharedPreferences = getSharedPreferences("StudyBuddyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("logged_in");
            editor.apply();

            Toast.makeText(this, "Logged out!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, ActivityLogin.class); // Replace with your login activity class
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
                                    // open new activity            //Removes all previous activities from the stack or returns to the previous screen
            startActivity(intent);
            finish();
            return true;
        } else {
            Toast.makeText(this, "Unknown menu item!", Toast.LENGTH_SHORT).show();
        }

        // Replace fragment if one is selected
        if (selectedFragment != null) { // is performed when the value is not null or checks the value whether it is null
            replaceFragment(selectedFragment);
        }

        // Close drawer after selection
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        // Close drawer if open, otherwise proceed with default back press behavior
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Helper method to replace fragments
     *
     * @param fragment Fragment to replace in the container
     */
    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
