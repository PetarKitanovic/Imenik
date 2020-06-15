package petarkitanovic.androidkurs.imenik.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Broj.TABLE_NAME)
public class Broj {

    public static final String TABLE_NAME = "brojevi";

    public static final String FIELD_ID = "id";
    public static final String FIELD_BROJ_TELEFONA = "broj_telefona";
    public static final String FIELD_KATEGORIJA = "kategorija";
    public static final String FIELD_KONTAKT = "kontakt";

    @DatabaseField(columnName = FIELD_ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = FIELD_BROJ_TELEFONA)
    private String mBroj;

    @DatabaseField(columnName = FIELD_KATEGORIJA)
    private String mKategorija;


    @DatabaseField(columnName = FIELD_KONTAKT, foreign = true, foreignAutoRefresh = true)
    private Kontakt mKontakt;

    public Broj() {
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmBroj() {
        return mBroj;
    }

    public void setmBroj(String mBroj) {
        this.mBroj = mBroj;
    }

    public String getmKategorija() {
        return mKategorija;
    }

    public void setmKategorija(String mKategorija) {
        this.mKategorija = mKategorija;
    }

    public Kontakt getmKontakt() {
        return mKontakt;
    }

    public void setmKontakt(Kontakt mKontakt) {
        this.mKontakt = mKontakt;
    }

    @Override
    public String toString() {
        return mBroj + " " + mKategorija;
    }
}
