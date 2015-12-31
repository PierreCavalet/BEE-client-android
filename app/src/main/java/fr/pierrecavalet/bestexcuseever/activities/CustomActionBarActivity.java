package fr.pierrecavalet.bestexcuseever.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;

import fr.pierrecavalet.bestexcuseever.TypefaceSpan;

/**
 * Created by pierre on 31/12/15.
 */
public class CustomActionBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SpannableString s = new SpannableString("  Best excuse ever");
        s.setSpan(new TypefaceSpan(this, "Neon.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Update the action bar title with the TypefaceSpan instance
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(s);

    }
}
