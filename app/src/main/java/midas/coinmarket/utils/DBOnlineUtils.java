package midas.coinmarket.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;

import midas.coinmarket.AppApplication;
import midas.coinmarket.R;

public class DBOnlineUtils {

    private static DatabaseReference myRef;

    public static DatabaseReference getDatabaseRef(Context context) {
        if (myRef == null)
            myRef = AppApplication.getFireBaseDb().getReference(context.getString(R.string.name_online_db));
        return myRef;
    }

    /**
     * Save db online.
     *
     * @param context
     */
    public static void setValues(Context context) {
        getDatabaseRef(context).setValue("Hello, World!").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                
            }
        });
    }

    public static void getValues(String key) {

    }
}
