package com.madlab.translatorapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import java.util.ArrayList;
import java.util.Locale;
import java.util.SplittableRandom;

public class MainActivity extends AppCompatActivity {

    private Spinner fromSpinner,toSpinner;
    private TextInputEditText sourceEdt;
    private ImageView micIV;
    private MaterialButton translateBtn;
    private TextView translatedTV;
    String[] fromlanguages={"From","English","Hindi","Kannada","Gujrati","Marathi","Tamil","Telgu","Bengali","Urdu"};
    String[] tolanguages={"To","English","Hindi","Kannada","Gujrati","Marathi","Tamil","Telgu","Bengali","Urdu"};
    private static final int REQUEST_PERMISSION_CODE=1;
    int languageCode,fromLanguageCode,toLanguageCode=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fromSpinner=findViewById(R.id.idFromSpinner);
        toSpinner=findViewById(R.id.idToSpinner);
        sourceEdt=findViewById(R.id.idEdtSource);
        micIV=findViewById(R.id.idIVMic);
        translateBtn=findViewById(R.id.idBtnTranslate);
        translatedTV=findViewById(R.id.idTranslatedTV);
        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fromLanguageCode=getLanguageCode(fromlanguages[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter fromAdapter= new ArrayAdapter(this,R.layout.spinner_item,fromlanguages);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(fromAdapter);

        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toLanguageCode=getLanguageCode(tolanguages[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter toAdapter= new ArrayAdapter(this,R.layout.spinner_item,tolanguages);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toSpinner.setAdapter(toAdapter);

        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translatedTV.setText("");
                if (sourceEdt.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter your text to translate", Toast.LENGTH_SHORT).show();
                }
                else if(fromLanguageCode==0){
                    Toast.makeText(MainActivity.this, "Please select your source language", Toast.LENGTH_SHORT).show();
                }
                else if(toLanguageCode==0){
                    Toast.makeText(MainActivity.this, "Please select your language to make translation", Toast.LENGTH_SHORT).show();
                }
                else{
                    translatetext(fromLanguageCode,toLanguageCode,sourceEdt.getText().toString());
                }
            }
        });
        micIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i= new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                    i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak to convert into text");
                    try{
                        startActivityForResult(i,REQUEST_PERMISSION_CODE);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();;
                    }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_PERMISSION_CODE){
            if(resultCode==RESULT_OK && data!=null){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                sourceEdt.setText(result.get(0));
            }
        }
    }

    private void translatetext(int fromLanguageCode, int toLanguageCode, String source){
        translatedTV.setText("Downloading Modal...");
        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                .setSourceLanguage(fromLanguageCode)
                .setTargetLanguage(toLanguageCode)
                .build();

        FirebaseTranslator translator =FirebaseNaturalLanguage.getInstance().getTranslator(options);

        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();

        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                translatedTV.setText("Translating...");
                translator.translate(source).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        translatedTV.setText(s);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"Failed to Translate : "+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failed to download lanuage Model"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    //fromlanguages={"From","English","Hindi","Kannada","Gujrati","Marathi","Tamil","Telgu","Bengali","Urdu"};
    public int getLanguageCode(String language){
        int languageCode=0;
        switch (language){
            case "English":
                languageCode = FirebaseTranslateLanguage.EN;
                break;
            case "Hindi":
                languageCode = FirebaseTranslateLanguage.HI;
                break;
            case "Kannada":
                languageCode = FirebaseTranslateLanguage.KN;
                break;
            case "Gujrati":
                languageCode = FirebaseTranslateLanguage.GU;
                break;
            case "Marathi":
                languageCode = FirebaseTranslateLanguage.MR;
                break;
            case "Tamil":
                languageCode = FirebaseTranslateLanguage.TA;
                break;
            case "Telgu":
                languageCode = FirebaseTranslateLanguage.TE;
                break;
            case "Bengali":
                languageCode = FirebaseTranslateLanguage.BN;
                break;
            case "Urdu":
                languageCode = FirebaseTranslateLanguage.UR;
                break;
            default:
                languageCode=0;
        }
        return languageCode;
    }
}