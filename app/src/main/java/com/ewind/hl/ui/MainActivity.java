package com.ewind.hl.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ewind.hl.R;
import com.ewind.hl.export.ExportTask;
import com.ewind.hl.export.ImportTask;
import com.ewind.hl.model.area.AreaFactory;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.EventComparator;
import com.ewind.hl.model.event.EventDateComparator;
import com.ewind.hl.model.event.EventRelevancyComparator;
import com.ewind.hl.model.event.EventScoreComparator;
import com.ewind.hl.model.event.type.EventTypeFactory;
import com.ewind.hl.persist.EventsDao;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EventChangedListener, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getName();
    private static final int RC_SIGN_IN = 1543;
    private EventAdapter adapter;
    private boolean showAll = false;

    private GoogleSignInClient signInClient;
    private NavigationView navigator;

    private MenuItem showAllButton;
    private MenuItem showRelevantButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);

        AreaFactory.initBody(this); // FIXME
        EventTypeFactory.initEvents(this);

        setContentView(R.layout.activity_main);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(Drive.SCOPE_FILE)
                .requestScopes(Drive.SCOPE_APPFOLDER)
                .build();
        signInClient = GoogleSignIn.getClient(this, gso);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.eventTitleText);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigator = findViewById(R.id.nav_view);
        navigator.setNavigationItemSelectedListener(this);

        findViewById(R.id.addButton).setOnClickListener(v -> onEventAdd());

        refreshAccountUi();

        this.adapter = createEventsAdapter();
        refreshEvents();
    }

    @NonNull
    private EventAdapter createEventsAdapter() {
        RecyclerView eventsList = findViewById(R.id.eventsList);

        EventComparator comparator = new EventComparator(
                new EventRelevancyComparator(),
                new EventDateComparator(),
                new EventScoreComparator()
        );

        EventAdapter adapter = new EventAdapter(comparator) {
            @Override
            public EventItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                EventActionListener listener = new EventActionListener(MainActivity.this);
                return new LastEventItemViewHolder(parent, listener);
            }
        };
        eventsList.setAdapter(adapter);
        return adapter;
    }

    private void signIn() {
        Intent signInIntent = signInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        signInClient.signOut().addOnCompleteListener(t -> refreshAccountUi());
    }

    private void onShowAll(boolean showAll) {
        this.showAll = showAll;
        showAllButton.setVisible(!showAll);
        showRelevantButton.setVisible(showAll);
        refreshEvents();
    }

    private void refreshEvents() {
        List<Event> events = getEvents();
        adapter.setEvents(events);

        boolean importAvailable = events.isEmpty();
        navigator.getMenu().findItem(R.id.nav_export).setVisible(!importAvailable);
        navigator.getMenu().findItem(R.id.nav_import).setVisible(importAvailable);
    }

    private List<Event> getEvents() {
        List<Event> latestEvents = new EventsDao(this).getLatestEvents();
        List<Event> eventsToShow = new LinkedList<>();
        for (Event latestEvent : latestEvents) {
            if (showAll || latestEvent.isRelevant()) {
                eventsToShow.add(latestEvent);
            }
        }
        return eventsToShow;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event, menu);
        showAllButton = menu.findItem(R.id.action_show_all);
        showRelevantButton = menu.findItem(R.id.action_show_relevant);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_show_all:
                onShowAll(true);
                return true;
            case R.id.action_show_relevant:
                onShowAll(false);
                return true;
            case R.id.action_search:
                onEventAdd();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onImport() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setMessage("Do you want to import your entire health log?")
                .setPositiveButton(android.R.string.ok, (d, v) -> doImport())
                .setNegativeButton(android.R.string.cancel, (d, v) -> d.dismiss())
                .show();
    }

    private void doImport() {
        ImportTask task = new ImportTask(this);
        task.execute(path ->
                runOnUiThread(() ->
                        Toast.makeText(this, path, Toast.LENGTH_LONG).show()));
    }

    private void onExport() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setMessage("Do you want to export your entire health log?")
                .setPositiveButton(android.R.string.ok, (d, v) -> doExport())
                .setNegativeButton(android.R.string.cancel, (d, v) -> d.dismiss())
                .show();
    }

    private void doExport() {
        ExportTask task = new ExportTask(this);
        task.execute(path ->
                runOnUiThread(() ->
                        Toast.makeText(this, path, Toast.LENGTH_LONG).show()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String eventType = data.getStringExtra(EventTypeSearchActivity.SELECTED_EVENT_TYPE);
            if (eventType != null) {
                new EventActionListener(this).onAddNew(eventType);
            } else {
                refreshEvents();
            }
        }

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            completedTask.getResult(ApiException.class);
            refreshAccountUi();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void refreshAccountUi() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        View navigatorHeader = navigator.getHeaderView(0);
        TextView userName = navigatorHeader.findViewById(R.id.user_name);
        TextView userEmail = navigatorHeader.findViewById(R.id.user_email);
        ImageView userPhoto = navigatorHeader.findViewById(R.id.user_photo);

        boolean signedIn = account != null;
        if (signedIn) {
            userName.setText(account.getGivenName() + " " + account.getFamilyName());
            userEmail.setText(account.getEmail());
            String personPhotoUrl = account.getPhotoUrl().toString();
            Glide.with(getApplicationContext()).load(personPhotoUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(userPhoto);
        } else {
            userName.setText(R.string.nav_header_title);
            userEmail.setText("");
            userPhoto.setImageResource(R.drawable.ic_user);
        }

        navigator.getMenu().findItem(R.id.nav_import).setEnabled(signedIn);
        navigator.getMenu().findItem(R.id.nav_export).setEnabled(signedIn);
        navigator.getMenu().findItem(R.id.nav_sign_out).setVisible(signedIn);
        navigator.getMenu().findItem(R.id.nav_sign_in).setVisible(!signedIn);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void onEventAdd() {
        new EventActionListener(this).onSelectEventType();
    }

    @Override
    public void onEventCreated(Event event) {
        refreshEvents();
    }

    @Override
    public void onEventUpdated(Event event) {
        refreshEvents();
    }

    @Override
    public void onEventDeleted(Event event) {
        refreshEvents();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_export: onExport(); break;
            case R.id.nav_import: onImport(); break;
            case R.id.nav_sign_in: signIn(); break;
            case R.id.nav_sign_out: signOut(); break;
        }

//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
