package petarkitanovic.androidkurs.imenik.db;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = Kontakt.TABLE_NAME)

public class Kontakt {

    public static final String TABLE_NAME = "kontakti";

    public static final String FIELD_ID = "id";
    public static final String FIELD_IME = "ime";
    public static final String FIELD_PREZIME = "prezime";
    public static final String FIELD_ADRESA = "adresa";
    public static final String FIELD_SLIKA = "slika";
    public static final String FIELD_TELEFON = "telefon";


    @DatabaseField(columnName = FIELD_ID, generatedId = true)
    private int mId;

    @DatabaseField(columnName = FIELD_IME)
    private String mIme;

    @DatabaseField(columnName = FIELD_PREZIME)
    private String mPrezime;

    @DatabaseField(columnName = FIELD_ADRESA)
    private String mAdresa;

    @DatabaseField(columnName = FIELD_SLIKA)
    private String mSlika;

    @ForeignCollectionField(columnName = FIELD_TELEFON, eager = true)
    private ForeignCollection<Broj> mTelefon;

    public Kontakt() {
    }


    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmIme() {
        return mIme;
    }

    public void setmIme(String mIme) {
        this.mIme = mIme;
    }

    public String getmPrezime() {
        return mPrezime;
    }

    public void setmPrezime(String mPrezime) {
        this.mPrezime = mPrezime;
    }

    public String getmAdresa() {
        return mAdresa;
    }

    public void setmAdresa(String mAdresa) {
        this.mAdresa = mAdresa;
    }

    public String getmSlika() {
        return mSlika;
    }

    public void setmSlika(String mSlika) {
        this.mSlika = mSlika;
    }

    public ForeignCollection<Broj> getmTelefon() {
        return mTelefon;
    }

    public void setmTelefon(ForeignCollection<Broj> mTelefon) {
        this.mTelefon = mTelefon;
    }

    @Override
    public String toString() {
        return mIme + " " + mPrezime;
    }
}
