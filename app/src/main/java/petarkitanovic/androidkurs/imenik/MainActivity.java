package petarkitanovic.androidkurs.imenik;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;

import petarkitanovic.androidkurs.imenik.db.DatabaseHelper;
import petarkitanovic.androidkurs.imenik.db.Kontakt;

public class MainActivity extends AppCompatActivity implements AdapterKontakt.OnRecyclerItemClickListener {


    private DatabaseHelper databaseHelper;
    private AdapterKontakt adapterKontakt;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setupToolbar();

        recyclerView = findViewById(R.id.rvList);
        recyclerView.addItemDecoration(new DividerKlasa(recyclerView.getContext(), DividerKlasa.VERTICAL));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);

        FloatingActionButton floatingActionButton = findViewById(R.id.add_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Add.class);
                startActivity(i);
            }
        });
    }

    public void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.kontakti);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorText));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this,SettingsActivity.class));
                break;
            case R.id.action_about:
                AboutDialog dialog = new AboutDialog(this);
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRVItemClick(Kontakt kontakt) {
        Intent intent = new Intent(this, DetaljiActivity.class);
        intent.putExtra("kontakt_id", kontakt.getmId());
        startActivity(intent);
    }

    private void refresh() {

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        try {
            adapterKontakt = new AdapterKontakt(getDatabaseHelper().getKontaktDao().queryForAll(), this);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        recyclerView.setAdapter(adapterKontakt);


    }

    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }
}
