package com.example.lectorfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class SelectPriceActivity extends AppCompatActivity {

    private Spinner spineerOptionsText;
    private List<String> textDetected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_price);
        textDetected = (ArrayList<String>) getIntent().getSerializableExtra("text_detected");
        loadSpinnerOptionsText();
    }


    private void loadSpinnerOptionsText(){
        spineerOptionsText = findViewById(R.id.spinnerText);

        List<String> options = getItemOptions(4);
        options = textDetected;
        // creo el adapter para el spinnerJornada y asigno el origen de los datos para el adaptador del spinner
        ArrayAdapter<String> adapterOptions = new ArrayAdapter<>(this.getApplication() ,android.R.layout.simple_spinner_item, options);
        //Asigno el layout a inflar para cada elemento al momento de desplegar la lista
        adapterOptions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spineerOptionsText.setAdapter(adapterOptions);
    }

    private List<String> getItemOptions(int options){
        List<String> itemOptions = new ArrayList<>();
        itemOptions.add("Jornada");
        for (int i = 1; i <= options ; i++) {
            itemOptions.add(String.valueOf(i));
        }

        return itemOptions;
    }
}
