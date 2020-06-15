package petarkitanovic.androidkurs.imenik;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import petarkitanovic.androidkurs.imenik.db.Broj;
import petarkitanovic.androidkurs.imenik.db.DatabaseHelper;
import petarkitanovic.androidkurs.imenik.db.Kontakt;

import static petarkitanovic.androidkurs.imenik.Tools.NOTIF_CHANNEL_ID;

public class Add extends AppCompatActivity {

    private static final int SELECT_PICTURE = 1;
    private DatabaseHelper databaseHelper;

    private SharedPreferences prefs;

    private Spinner spinner;
    private EditText ime;
    private EditText prezime;
    private EditText adresa;
    private EditText brojTelefona;
    private ImageView addSlika;
    private String kategorija;

    private Kontakt kontakt;
    private Broj broj;
    private String image_path;


    private Broj broj2;
    private String kategorija2;
    private TextView addBtn;
    LinearLayout glavni;
    LinearLayout linearLayout;
    EditText editText;
    Spinner spin;
    TextView cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);

        createNotificationChannel();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        setupToolbar();

        ime = findViewById(R.id.add_ime);
        prezime = findViewById(R.id.add_prezime);
        adresa = findViewById(R.id.add_adresa);
        brojTelefona = findViewById(R.id.add_telefon);
        addSlika = findViewById(R.id.add_slika);
        spinner = findViewById(R.id.add_spinner);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, fillSpinner());
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

        addSlika.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStoragePermissionGranted()) {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, SELECT_PICTURE);
                }
            }
        });

        addBtn = findViewById(R.id.btn_add_layout);
        glavni = findViewById(R.id.glavni_layout);

        linearLayout = new LinearLayout(Add.this);
        editText = new EditText(Add.this);
        spin = new Spinner(Add.this);
        cancel = new TextView(Add.this);

        addBtn.setOnClickListener(new View.OnClickListener() {


            public void addNaKlik() {


                glavni.addView(linearLayout);
                editText.setHint("Telefon");
                editText.setMaxEms(10);
                editText.setInputType(3);
                editText.setLayoutParams(brojTelefona.getLayoutParams());
                editText.setWidth(brojTelefona.getWidth() - 100);


                linearLayout.addView(editText);
                spin.setLayoutParams(spinner.getLayoutParams());

                spin.setAdapter(spinner.getAdapter());

                spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        kategorija2 = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                linearLayout.addView(spin);

//                cancel.setText("X");
//                cancel.setTextSize(20);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    cancel.setBackground(getDrawable(R.drawable.close));

                }
                linearLayout.addView(cancel);



                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        glavni.removeView(linearLayout);

                    }

                });


            }

            @Override
            public void onClick(View v) {
                try {
                    addNaKlik();

                } catch (Exception e) {
//                    Toast.makeText(Add.this, "Maksimalan broj telefona dodat", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private List<String> fillSpinner() {
        List<String> list = new ArrayList<>();
        list.add("Mobile");
        list.add("Home");
        list.add("Work");
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
//                finish();
                break;
            case R.id.action_add:
                add();

//                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorText));
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.close);

            actionBar.setHomeButtonEnabled(true);
            actionBar.show();

        }
    }

    private void add() {
        if (Tools.validateInput(ime)
                && Tools.validateInput(prezime)
                && Tools.validateInput(brojTelefona)
                && Tools.validateInput(adresa)

        ) {
            kategorija = spinner.getSelectedItem().toString();

            kontakt = new Kontakt();
            kontakt.setmIme(ime.getText().toString().trim());
            kontakt.setmPrezime(prezime.getText().toString().trim());
            kontakt.setmAdresa(adresa.getText().toString().trim());
            kontakt.setmSlika(image_path);

            broj = new Broj();
            broj.setmKontakt(kontakt);
            broj.setmKategorija(kategorija);
            broj.setmBroj(brojTelefona.getText().toString().trim());


            broj2 = new Broj();
            broj2.setmKontakt(kontakt);
            broj2.setmKategorija(kategorija2);
            broj2.setmBroj(editText.getText().toString().trim());

            try {
                getDatabaseHelper().getKontaktDao().create(kontakt);
                getDatabaseHelper().getBrojDao().create(broj);

                if (editText.getText().toString().isEmpty()) {
//                    Toast.makeText(this, "unet jedan broj", Toast.LENGTH_SHORT).show();
                } else {
                    getDatabaseHelper().getBrojDao().create(broj2);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            String splashTime = prefs.getString(getString(R.string.settings_key), "toast_key");
            boolean splash = prefs.getBoolean(getString(R.string.obavestavanje_key), false);

            String tekstNotifikacije = "Kontakt dodat";

            if (splash){
                if (splashTime.equals("toast_key")){
                    Toast.makeText(Add.this, tekstNotifikacije, Toast.LENGTH_LONG).show();
                }

                if (splashTime.equals("notif_key")){
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(Add.this, NOTIF_CHANNEL_ID);
                    builder.setSmallIcon(R.drawable.add);
                    builder.setContentTitle("Notifikacija");
                    builder.setContentText(tekstNotifikacije);

                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

                    builder.setLargeIcon(bitmap);
                    notificationManager.notify(1, builder.build());

                }

            }
            finish();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle("Novi kontakt");

    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            if (selectedImage != null) {
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    image_path = picturePath;
                    cursor.close();

                    addSlika.setImageBitmap(BitmapFactory.decodeFile(image_path));
                }
            }
        }
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
