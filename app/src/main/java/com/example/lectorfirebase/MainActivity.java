package com.example.lectorfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private ImageView iv_capture;
    private Button btnCapturar;
    private Button btnReconized;
    private TextView tvTextDisplay;
    private Bitmap imageBitmap;
    private String textReconized = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initElements();

        btnCapturar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
                tvTextDisplay.setText("");
            }
        });

        btnReconized.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                detectTextFromImage();
            }
        });
    }

    private void initElements() {
        iv_capture = findViewById(R.id.image_view);
        btnCapturar = findViewById(R.id.btn_capture);
        btnReconized= findViewById(R.id.btn_detect_text);
        tvTextDisplay = findViewById(R.id.text_display);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            iv_capture.setImageBitmap(imageBitmap);
        }
    }

    private void detectTextFromImage() {
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextDetector firebaseVisionTextDetector = FirebaseVision.getInstance().getVisionTextDetector();
        firebaseVisionTextDetector.detectInImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                displayTextFromImage(firebaseVisionText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Error", e.getMessage());
            }
        });
    }

    private void displayTextFromImage(FirebaseVisionText firebaseVisionText) {
        List<FirebaseVisionText.Block> blockList = firebaseVisionText.getBlocks();

        if(blockList.size() == 0){
            Toast.makeText(this, "No se encontraron textos", Toast.LENGTH_SHORT).show();
        }
        else{
            for (FirebaseVisionText.Block block : firebaseVisionText.getBlocks()){
                String text = block.getText();
                tvTextDisplay.setText(text);
                textReconized += text + " - ";
            }
            Toast.makeText(this, textReconized, Toast.LENGTH_SHORT).show();
        }
    }
}
