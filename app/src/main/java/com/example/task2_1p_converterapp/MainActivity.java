package com.example.task2_1p_converterapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    //Declare variables for our widgets
    TextView TextResult;
    EditText EditValue;
    Button ButtonCalculate;
    Spinner SpinnerInputUnit;
    Spinner SpinnerOutputUnit;
    Double ValueToConvert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //Assign widget ID's to variables
        TextResult = findViewById(R.id.Text_result);
        EditValue = findViewById(R.id.EditText_value);
        ButtonCalculate = findViewById(R.id.Button_calculate);
        SpinnerInputUnit = findViewById(R.id.Spinner_inputUnit);
        SpinnerOutputUnit = findViewById(R.id.Spinner_outputUnit);

        //Button click
        ButtonCalculate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String input = EditValue.getText().toString();
                if (input.isEmpty()){TextResult.setText("No input entered!");}
                double inputAsDouble;
                try {ValueToConvert = Double.parseDouble(input);}
                catch (NumberFormatException e) {TextResult.setText("Invalid characters (enter a number!)"); return;}

                double output = calculateConversion(SpinnerInputUnit.getSelectedItemPosition(),SpinnerOutputUnit.getSelectedItemPosition(),
                        Double.parseDouble(String.valueOf(EditValue.getText())));
                //We have our value, set it to our text
                String stringOut = String.valueOf(output);
                //Concat unit to result
                switch(SpinnerOutputUnit.getSelectedItemPosition())
                {
                    case 0: stringOut = stringOut + " cm"; break;
                    case 1: stringOut = stringOut + " m"; break;
                    case 2: stringOut = stringOut + " km"; break;
                    case 3: stringOut = stringOut + " in"; break;
                    case 4: stringOut = stringOut + " ft"; break;
                    case 5: stringOut = stringOut + " yd"; break;
                    case 6: stringOut = stringOut + " mi"; break;

                    default: stringOut = stringOut + " units"; break;
                }
                TextResult.setText(stringOut);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public double calculateConversion(int inputUnit, int outputUnit, double inputValue)
    {
        boolean isInputMetric = false;
        double convFactor = 1.0;
        if (inputUnit <= 2)
        {
            //Convert metric input into metres
            isInputMetric = true;
            if (inputUnit == 0) {inputValue = inputValue/100;}
            if (inputUnit == 2) {inputValue = inputValue*1000;}
        }
        else
        {
            //Convert imperial input into feet
            if (inputUnit == 3) {inputValue = inputValue/12;}
            if (inputUnit == 5) {inputValue = inputValue*3;}
            if (inputUnit == 6) {inputValue = inputValue*5280;}
        }
        //Do metric -> imperial calculations
        if (isInputMetric)
        {
            if (outputUnit == 0) {convFactor = 0.01;}
            if (outputUnit == 2) {convFactor = 1000;}

            if (outputUnit == 3) {convFactor = 0.0254;}
            if (outputUnit == 4) {convFactor = 0.3048;}
            if (outputUnit == 5) {convFactor = 0.9144;}
            if (outputUnit == 6) {convFactor = 1609.344;}
        }
        else
        {
            if (outputUnit == 0) {convFactor = 0.0328;}
            if (outputUnit == 1) {convFactor = 3.2808;}
            if (outputUnit == 2) {convFactor = 3280.84;}

            if (outputUnit == 3) {convFactor = 0.0833;}
            if (outputUnit == 5) {convFactor = 3;}
            if (outputUnit == 6) {convFactor = 5280;}
        }
        //Now apply conversion and return value
        return inputValue/convFactor;
    }
}