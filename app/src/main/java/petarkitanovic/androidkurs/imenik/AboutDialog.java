package petarkitanovic.androidkurs.imenik;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AboutDialog extends AlertDialog.Builder {


    public AboutDialog(Context context) {
        super( context );
        setTitle( "O aplikaciji" );
        setMessage( "App name: Imenik\nAuthor: Petar Kitanovic" );
        setPositiveButton( "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        } );
    }

    public AlertDialog prepareDialog() {
        AlertDialog dialog = create();
        dialog.setCanceledOnTouchOutside( false );
        return dialog;
    }
}
