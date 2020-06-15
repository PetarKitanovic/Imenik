package petarkitanovic.androidkurs.imenik;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import petarkitanovic.androidkurs.imenik.db.Broj;
import petarkitanovic.androidkurs.imenik.db.DatabaseHelper;
import petarkitanovic.androidkurs.imenik.db.Kontakt;

import static petarkitanovic.androidkurs.imenik.Tools.NOTIF_CHANNEL_ID;

public class DetaljiActivity extends AppCompatActivity {

    private TextView imePrezime;
    private TextView adresaDetalji;
    private ImageView image;
    private FloatingActionButton btn_add_number;
    private Kontakt kontakt;
    private int kontakt_id;
    private List<Broj> brojTelefona;
    private RecyclerView recyclerView;
    private AdapterBrojeva adapterBrojeva;

    private EditText number;
    private Spinner spinner;
    private String kategorija;

    String splashTime;
    boolean splash;

    private SharedPreferences prefs;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalji);

        createNotificationChannel();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);


        setupToolbar();

        imePrezime = findViewById(R.id.ime_prezime_detalji);
        adresaDetalji = findViewById(R.id.adresa_detalji);
        image = findViewById(R.id.slika_detalji);
        brojTelefona = new ArrayList<>();
        recyclerView = findViewById(R.id.detalji_rv);
        btn_add_number = findViewById(R.id.detail_btn_add_number);

        recyclerView.addItemDecoration(new DividerKlasa(recyclerView.getContext(), DividerKlasa.VERTICAL));
        recyclerView.setHasFixedSize(true);


        kontakt_id = getIntent().getIntExtra("kontakt_id", -1);
        if (kontakt_id < 0) {
            Toast.makeText(this, "Greska! " + kontakt_id + " ne pstoji", Toast.LENGTH_SHORT).show();
            finish();
        }

        try {
            kontakt = getDatabaseHelper().getKontaktDao().queryForId(kontakt_id);
            brojTelefona = getDatabaseHelper().getBrojDao().queryForEq("kontakt", kontakt_id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        imePrezime.setText(kontakt.getmIme() + " " + kontakt.getmPrezime());
        adresaDetalji.setText(kontakt.getmAdresa());
        image.setImageBitmap(BitmapFactory.decodeFile(kontakt.getmSlika()));

        btn_add_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNumber();
            }
        });

    }

    private void addNumber() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_number_layout);
        dialog.setTitle("Unesite podatke");
        dialog.setCanceledOnTouchOutside(false);

        number = dialog.findViewById(R.id.add_number_telefon);
        spinner = dialog.findViewById(R.id.add_number_spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                DetaljiActivity.this, android.R.layout.simple_spinner_dropdown_item, fillSpinner());
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kategorija = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button add = dialog.findViewById(R.id.btn_add_number);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Tools.validateInput(number)) {

                    kontakt_id = getIntent().getIntExtra("kontakt_id", 1);

                    try {

                        kontakt = getDatabaseHelper().getKontaktDao().queryForId(kontakt_id);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                    Broj broj = new Broj();
                    broj.setmBroj(number.getText().toString());
                    broj.setmKategorija(kategorija);
                    broj.setmKontakt(kontakt);


                    try {
                        getDatabaseHelper().getBrojDao().createIfNotExists(broj);
                        refresh();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();
                }

            }

        });

        Button cancel = dialog.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    private List<String> fillSpinner() {
        List<String> list = new ArrayList<>();
        list.add("Home");
        list.add("Work");
        list.add("Mobile");

        return list;
    }

    public void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorText));
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back);

            actionBar.setHomeButtonEnabled(true);
            actionBar.show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.detalji_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_edit:
                editKontakt();
                break;

            case R.id.action_delete:
                deleteKontakt();
                break;

            case R.id.action_settings:
                startActivity(new Intent(DetaljiActivity.this,SettingsActivity.class));
                break;

            case R.id.action_about:
                AboutDialog dialog = new AboutDialog(this);
                dialog.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void editKontakt() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.edit_layout);
        dialog.setCanceledOnTouchOutside(false);

        final EditText ime = dialog.findViewById(R.id.edit_ime);
        final EditText prezime = dialog.findViewById(R.id.edit_prezime);
        final EditText adresa = dialog.findViewById(R.id.edit_adresa);

        ime.setText(kontakt.getmIme());
        adresa.setText(kontakt.getmAdresa());
        prezime.setText(kontakt.getmPrezime());


        Button add = dialog.findViewById(R.id.edit_nekretnina);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Tools.validateInput(ime)
                        && Tools.validateInput(prezime)
                        && Tools.validateInput(adresa)
                ) {


                    kontakt.setmIme(ime.getText().toString());
                    kontakt.setmPrezime(prezime.getText().toString());
                    kontakt.setmAdresa(adresa.getText().toString());


                    try {
                        getDatabaseHelper().getKontaktDao().update(kontakt);

                        refreshKontakt();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                    splashTime = prefs.getString(getString(R.string.settings_key), "toast_key");
                    splash = prefs.getBoolean(getString(R.string.obavestavanje_key), false);
                    String tekstNotifikacije = "Kontakt promenjen";

                    if (splash){
                        if (splashTime.equals("toast_key")){
                            Toast.makeText(DetaljiActivity.this, tekstNotifikacije, Toast.LENGTH_LONG).show();
                        }

                        if (splashTime.equals("notif_key")){
                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(DetaljiActivity.this, NOTIF_CHANNEL_ID);
                            builder.setSmallIcon(R.drawable.edit);
                            builder.setContentTitle("Notifikacija");
                            builder.setContentText(tekstNotifikacije);

                            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

                            builder.setLargeIcon(bitmap);
                            notificationManager.notify(1, builder.build());

                        }

                    }

                    dialog.dismiss();

                }
            }

        });

        Button cancel = dialog.findViewById(R.id.cancel_edit);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void deleteKontakt() {
        AlertDialog dialogDelete = new AlertDialog.Builder(this)
                .setTitle("Brisanje kontakta")
                .setMessage("Da li zelite da obrisete \"" + kontakt.getmIme() + " " + kontakt.getmPrezime() + "\"?")
                .setPositiveButton("DA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            List<Broj> brojevi = getDatabaseHelper().getBrojDao().queryForEq(Broj.FIELD_KONTAKT, kontakt.getmId());

                            getDatabaseHelper().getKontaktDao().delete(kontakt);
                            for (Broj broj : brojevi) {
                                getDatabaseHelper().getBrojDao().delete(broj);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        String tekstNotifikacije = "Kontakt obrisan";
                        splashTime = prefs.getString(getString(R.string.settings_key), "toast_key");
                        splash = prefs.getBoolean(getString(R.string.obavestavanje_key), false);
                        if (splash){
                            if (splashTime.equals("toast_key")){
                                Toast.makeText(DetaljiActivity.this, tekstNotifikacije, Toast.LENGTH_LONG).show();
                            }

                            if (splashTime.equals("notif_key")){
                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(DetaljiActivity.this, NOTIF_CHANNEL_ID);
                                builder.setSmallIcon(R.drawable.delete);
                                builder.setContentTitle("Notifikacija");
                                builder.setContentText(tekstNotifikacije);

                                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

                                builder.setLargeIcon(bitmap);
                                notificationManager.notify(1, builder.build());

                            }

                        }
                        finish();
                    }
                })
                .setNegativeButton("NE", null)
                .show();
    }

    private void refreshKontakt() {

        try {
            kontakt = getDatabaseHelper().getKontaktDao().queryForId(kontakt_id);

            imePrezime.setText(kontakt.getmIme() + " " + kontakt.getmPrezime());
            adresaDetalji.setText(kontakt.getmAdresa());


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void refresh() {
        List<Broj> listaBrojeva = null;
        try {
            listaBrojeva = getDatabaseHelper().getBrojDao().queryBuilder()
                    .where()
                    .eq(Broj.FIELD_KONTAKT, kontakt.getmId())
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (adapterBrojeva != null) {
            adapterBrojeva.clear();
            adapterBrojeva.addAll(listaBrojeva);
            adapterBrojeva.notifyDataSetChanged();
        } else {
            adapterBrojeva = new AdapterBrojeva(listaBrojeva);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapterBrojeva);


        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        setTitle("");
        refresh();
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

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Channel";
            String description = "Description of My Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIF_CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
