package vikash.kumar.mlkit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import static android.graphics.Color.WHITE;

public class TextDetect extends AppCompatActivity {

    Button camera_button;
    private static final int REQUEST_CAMERA_CAPTURE =124;
    private FirebaseVisionTextRecognizer textRecognizer;
    FirebaseVisionImage image;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_detect);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        camera_button = findViewById(R.id.openCamera);
        camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if(takePictureIntent.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(takePictureIntent,REQUEST_CAMERA_CAPTURE);
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(REQUEST_CAMERA_CAPTURE == requestCode && resultCode == RESULT_OK){
            Bundle extras= data.getExtras();
            Bitmap bitmap = (Bitmap)extras.get("data");
            recoginzeMyText(bitmap);
        }
    }

    private void recoginzeMyText(Bitmap bitmap) {

        try {
            image = FirebaseVisionImage.fromBitmap(bitmap);
            textRecognizer =FirebaseVision
                    .getInstance()
                    .getOnDeviceTextRecognizer();
        } catch (Exception e) {
            e.printStackTrace();
        }

        textRecognizer.processImage(image)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText firebaseVisionText) {

                        String result = firebaseVisionText.getText();

                        if(result.isEmpty()){

                            Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content),"Text Not Detected",Snackbar.LENGTH_SHORT);
                            View sbView = snackBar.getView();
                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(WHITE);
                            sbView.setBackgroundColor(ContextCompat.getColor(TextDetect.this, R.color.colorPrimary));
                            snackBar.show();

                        }else{
                            Intent intent = new Intent(TextDetect.this,TextResult.class);
                            intent.putExtra(TextReconigation.RESULT_TEXT,result);
                            startActivity(intent);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content),e.getMessage(),Snackbar.LENGTH_SHORT);
                        View sbView = snackBar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(WHITE);
                        sbView.setBackgroundColor(ContextCompat.getColor(TextDetect.this, R.color.colorPrimary));
                        snackBar.show();

                    }
                });


    }
}
