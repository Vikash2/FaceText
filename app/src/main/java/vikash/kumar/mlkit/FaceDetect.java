package vikash.kumar.mlkit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import java.util.List;
import android.support.design.widget.*;
import android.widget.TextView;

import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;

public class FaceDetect extends AppCompatActivity {
    Button cameraButton;
    private final static int REQUEST_IMAGE_CAPTURE=124;
    private FirebaseVisionImage image;
    private FirebaseVisionFaceDetector detector;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode ==RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            detectFace(bitmap);
        }
    }

    private void detectFace(Bitmap bitmap) {
        FirebaseVisionFaceDetectorOptions options =
                new FirebaseVisionFaceDetectorOptions.Builder()
                .setModeType(FirebaseVisionFaceDetectorOptions.ACCURATE_MODE)
                .setClassificationType(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                .setClassificationType(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                .setMinFaceSize(0.15f)
                .setTrackingEnabled(true)
                .build();

        try {
            image = FirebaseVisionImage.fromBitmap(bitmap);
            detector = FirebaseVision.getInstance().getVisionFaceDetector(options);
        } catch (Exception e) {
            e.printStackTrace();
        }

        detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
            @Override
            public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {
                String resultText="";
                int i=1;
                for(FirebaseVisionFace face : firebaseVisionFaces){
                    resultText =resultText.concat("\n"+i+".")
                            .concat("\nSmile :" + face.getSmilingProbability()*100+"%")
                            .concat("\nLeft Eye :" + face.getLeftEyeOpenProbability()*100+"%")
                            .concat("\nRight Eye :" + face.getRightEyeOpenProbability()*100+"%")
                            .concat("\nHead Right Turn :" + face.getHeadEulerAngleY()*100+"%")
                            .concat("\nHead Left Turn :" + face.getHeadEulerAngleZ()*100+"%");
                    i++;
                }
                if(firebaseVisionFaces.size() == 0){

                    Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content),"No Face Detected",Snackbar.LENGTH_SHORT);
                    View sbView = snackBar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(WHITE);
                    sbView.setBackgroundColor(ContextCompat.getColor(FaceDetect.this, R.color.colorPrimary));
                    snackBar.show();

                }else{
                    Bundle bundle = new Bundle();
                    bundle.putString(FaceDetection.RESULT_TEXT,resultText);

                    DialogFragment resultDialog = new ResultDialog();
                    resultDialog.setArguments(bundle);
                    resultDialog.setCancelable(false);
                    resultDialog.show(getSupportFragmentManager(),FaceDetection.RESULT_DIALOG);


                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detect);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseApp.initializeApp(this);
        cameraButton = findViewById(R.id.openCamera);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictures = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // checking if the camera if opened by our app or not
                if(takePictures.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(takePictures,REQUEST_IMAGE_CAPTURE);
                }

            }

        });

    }
}
