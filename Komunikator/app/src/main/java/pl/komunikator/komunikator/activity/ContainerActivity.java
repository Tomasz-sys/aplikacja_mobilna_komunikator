package pl.komunikator.komunikator.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.RealmList;
import pl.komunikator.komunikator.RealmUtilities;
import pl.komunikator.komunikator.entity.Conversation;
import pl.komunikator.komunikator.entity.User;
import pl.komunikator.komunikator.fragment.ConversationsFragment;
import pl.komunikator.komunikator.fragment.ContactsFragment;
import pl.komunikator.komunikator.R;
import pl.komunikator.komunikator.fragment.SettingsFragment;
import pl.komunikator.komunikator.interfaces.OnConversationCreatedListener;

public class ContainerActivity extends AppCompatActivity implements OnConversationCreatedListener {

    private DrawerLayout mDrawer;
    private Menu mMenu;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        View v = mNavigationView.getHeaderView(0);
        
        TextView userEmailTextView = (TextView ) v.findViewById(R.id.userEmailTextView);
        userEmailTextView.setText(User.getLoggedUser().getEmail());

        TextView userNameTextView = (TextView ) v.findViewById(R.id.userNameTextView);
        userNameTextView.setText(User.getLoggedUser().getUsername());

        setupDrawerContent(mNavigationView);
    }

    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the mMenu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contacts_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        String searchViewHint = getResources().getString(R.string.search_view_hint);
        searchView.setQueryHint(searchViewHint);

        mMenu = menu;

        selectConversationsMenuItem();

        return true;
    }

    private void selectConversationsMenuItem() {
        MenuItem item = mNavigationView.getMenu().findItem(R.id.nav_conversations);
        selectDrawerItem(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.nav_settings:
                fragmentClass = SettingsFragment.class;
                setTitle(R.string.title_settings);
                break;
            case R.id.nav_contacts:
                fragmentClass = ContactsFragment.class;
                setTitle(R.string.contact_placeholder);
                break;
            case R.id.nav_conversations:
                setTitle(R.string.title_activity_conversations_list);
                fragmentClass = ConversationsFragment.class;
                break;
           case R.id.nav_search:
                setTitle(R.string.title_action_search);
                fragmentClass = ContactsFragment.class;
                break;
            case R.id.nav_help:
                setTitle(R.string.navigation_help);
                String url = "https://github.com/teneusz/aplikacja_mobilna_komunikator/wiki";

                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(this, Uri.parse(url));
                return;
            case R.id.nav_report:
                setTitle(R.string.navigation_report);
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{getString(R.string.company_email)});
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.report_subject));
                i.putExtra(Intent.EXTRA_TEXT   , "");
                try {
                    startActivity(Intent.createChooser(i, ""));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this, R.string.no_email_app, Toast.LENGTH_SHORT).show();
                }
                return;
            default:
                fragmentClass = ContactsFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        menuItem.setChecked(true);
        mDrawer.closeDrawers();
    }
    
    public Menu getMenu() {
        return mMenu;
    }

    @Override
    public void onContactSelected(User contact) {
        RealmUtilities realm = new RealmUtilities();
        User contactFromRealm = realm.getUser(contact.getId());
        User loggedUser = User.getLoggedUser();

        Conversation conversation = realm.createConversation(new RealmList<>(loggedUser, contactFromRealm));
        ConversationActivity.show(this, conversation.getId());
    }

    @Override
    public void onCreateButtonClicked() {
        /* TODO handle creating grouped conversation */

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        MenuItem conversationsMenuItem = navigationView.getMenu().findItem(R.id.nav_conversations);
        selectDrawerItem(conversationsMenuItem);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ConversationActivity.BACK_PRESS_CODE) {
            selectConversationsMenuItem();
        }
    }

}
