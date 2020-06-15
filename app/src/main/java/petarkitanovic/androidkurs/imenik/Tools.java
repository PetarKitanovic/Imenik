package petarkitanovic.androidkurs.imenik;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.widget.EditText;

import petarkitanovic.androidkurs.imenik.db.Kontakt;


public class Tools {
    public static final String CHANNEL_ID = "Channel";
    public static final int notificationID = 111;

    public static final String NOTIF_CHANNEL_ID = "notif_1234";

    public static boolean proveraPrefsPodesavanja(String text, Context context){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(text, false);
    }

    public static void setupNotification(Kontakt objekat, String text, Context context, int notificationID) {
        String textForTheNotification = text;
        Notification builder = new Notification.Builder(context)
                .setContentTitle("Notification")
                .setContentText(objekat.toString() + textForTheNotification)
                .setSmallIcon( R.drawable.delete)
                .build();
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService( Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(notificationID, builder);

    }

    public static boolean validateInput(EditText editText) {
        String titleInput = editText.getText().toString().trim();

        if (titleInput.isEmpty()) {
            editText.setError("Polje ne moze da bude prazno");
            return false;
        }else {
            editText.setError(null);
            return true;
        }
    }

    public static void dialPhoneNumber(String phoneNumber, Context context) {
        Intent intent = new Intent( Intent.ACTION_DIAL);
        intent.setData( Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public static void sendMessege(String phoneNumber, Context context) {
        Intent intent = new Intent( Intent.ACTION_SENDTO);
        intent.setData( Uri.parse("smsto:" + phoneNumber));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public static void createNotification(Context context, String radnja) {



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int ikona = 0;
            if(radnja.equals("add")){
                ikona = R.drawable.add;
            }
            if(radnja.equals("delete")){
                ikona = R.drawable.delete;
            }



                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CharSequence name = "My Channel";
                    String description = "Description of My Channel";
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel channel = new NotificationChannel(NOTIF_CHANNEL_ID, name, importance);
                    channel.setDescription(description);

                    NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);
                }


//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Telefonski imenik", importance);
//
//
//            NotificationManager notificationManager = context.getSystemService( NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//            String textForTheNotification = "";
//
//            Notification builder = new Notification.Builder(context)
//                    .setContentTitle("Notification")
//                    .setContentText(kontakt.toString() + textForTheNotification)
//                    .setChannelId(CHANNEL_ID)
//                    .setSmallIcon(ikona)
//                    .build();
//            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService( Context.NOTIFICATION_SERVICE);

        }
    }


}
