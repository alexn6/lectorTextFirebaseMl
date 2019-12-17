package com.example.lectorfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
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
//import com.karumi.dexter.Dexter;
//import com.karumi.dexter.PermissionToken;
//import com.karumi.dexter.listener.PermissionDeniedResponse;
//import com.karumi.dexter.listener.PermissionGrantedResponse;
//import com.karumi.dexter.listener.PermissionRequest;
//import com.karumi.dexter.listener.single.PermissionListener;
//import com.otaliastudios.cameraview.CameraView;
//import com.otaliastudios.cameraview.Frame;
//import com.otaliastudios.cameraview.FrameProcessor;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private ImageView iv_capture;
    private Button btnCapturar;
    private Button btnReconized;
    private TextView tvTextDisplay;
    private Bitmap imageBitmap;
    private String textReconized = "";
    private ArrayList<String> listOptionsText;

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
                // deberia mandarme a la otra pantalla(SelectPriceActivity) con los resultados de textos obtenidos
                passToSelectText(v);
            }
        });
    }

    private void initElements() {
        iv_capture = findViewById(R.id.image_view);
        btnCapturar = findViewById(R.id.btn_capture);
        btnReconized= findViewById(R.id.btn_detect_text);
        tvTextDisplay = findViewById(R.id.text_display);
        listOptionsText = new ArrayList<>();
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
                listOptionsText.add(text);
            }
            Toast.makeText(this, textReconized, Toast.LENGTH_SHORT).show();
        }
    }

    private void passToSelectText(View view){
//        Intent intent = new Intent(this, SecondActivity.class);

        Intent passToSelection = new Intent(this, SelectPriceActivity.class);
        passToSelection.putExtra("text_detected", listOptionsText);
        this.startActivity(passToSelection);
    }
//
//    CameraView camera;
//    boolean  isDetected = false;
//    Button btnCaptureAgain;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_main);
//        setContentView(R.layout.activity_main2);
//
//        Dexter.withActivity(this)
//                .withPermission(Manifest.permission.CAMERA)
//                .withListener(new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted(PermissionGrantedResponse response) {
//                        setupCamera();
//                    }
//
//                    @Override
//                    public void onPermissionDenied(PermissionDeniedResponse response) {
//                        Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
//
//                    }
//                }).check();
//    }
//
//    private void setupCamera() {
//        btnCaptureAgain = findViewById(R.id.btn_again);
//        camera = findViewById(R.id.cameraView);
//        //camera.setLifecy
//        camera.addFrameProcessor(new FrameProcessor() {
//            @Override
//            public void process(@NonNull Frame frame) {
//                Toast.makeText(MainActivity.this, "Reconocio el cuadro.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
