package vikash.kumar.mlkit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class TextResult extends AppCompatActivity {

    private TextView resultTextView;
    private String resultText;

    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_result);

        resultTextView =findViewById(R.id.result_textview);
        resultText = getIntent().getStringExtra(TextReconigation.RESULT_TEXT);
        resultTextView.setText(resultText);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
