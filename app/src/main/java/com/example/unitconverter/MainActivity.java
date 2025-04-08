package com.example.unitconverter;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Spinner categorySpinner, fromUnitSpinner, toUnitSpinner;
    private EditText inputValue;
    private Button convertButton;
    private TextView resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categorySpinner = findViewById(R.id.categorySpinner);
        fromUnitSpinner = findViewById(R.id.fromUnitSpinner);
        toUnitSpinner = findViewById(R.id.toUnitSpinner);
        inputValue = findViewById(R.id.inputValue);
        convertButton = findViewById(R.id.convertButton);
        resultView = findViewById(R.id.resultView);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateUnitSpinners(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        convertButton.setOnClickListener(view -> performConversion());
    }

    private void updateUnitSpinners(int categoryIndex) {
        int unitArrayId;
        if (categoryIndex == 0) {
            unitArrayId = R.array.length_units;
        } else if (categoryIndex == 1) {
            unitArrayId = R.array.weight_units;
        } else {
            unitArrayId = R.array.temperature_units;
        }

        ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(this, unitArrayId, android.R.layout.simple_spinner_item);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromUnitSpinner.setAdapter(unitAdapter);
        toUnitSpinner.setAdapter(unitAdapter);
    }

    private void performConversion() {
        String fromUnit = fromUnitSpinner.getSelectedItem().toString();
        String toUnit = toUnitSpinner.getSelectedItem().toString();
        String input = inputValue.getText().toString().trim();

        // 1. Check for empty input
        if (input.isEmpty()) {
            Toast.makeText(this, "Please enter a value", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Check for non-numeric input
        double value;
        try {
            value = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Check if source and destination units are the same
        if (fromUnit.equals(toUnit)) {
            Toast.makeText(this, "Source and destination units must be different", Toast.LENGTH_SHORT).show();
            return;
        }

        // 4. Try conversion and display result
        try {
            double result = convertUnits(value, fromUnit, toUnit);
            resultView.setText(String.format("%.2f %s", result, toUnit));
        } catch (Exception e) {
            Toast.makeText(this, "Conversion error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private double convertUnits(double value, String fromUnit, String toUnit) {
        if (fromUnit.equals(toUnit)) {
            return value;
        }
        switch (fromUnit + " to " + toUnit) {
            case "Inch to Cm": return value * 2.54;
            case "Cm to Inch": return value / 2.54;
            case "Foot to Cm": return value * 30.48;
            case "Cm to Foot": return value / 30.48;
            case "Yard to Cm": return value * 91.44;
            case "Cm to Yard": return value / 91.44;
            case "Mile to Km": return value * 1.60934;
            case "Km to Mile": return value / 1.60934;
            case "Pound to Kg": return value * 0.453592;
            case "Kg to Pound": return value / 0.453592;
            case "Ounce to Gram": return value * 28.3495;
            case "Gram to Ounce": return value / 28.3495;
            case "Ton to Kg": return value * 907.185;
            case "Kg to Ton": return value / 907.185;
            case "Celsius to Fahrenheit": return (value * 1.8) + 32;
            case "Fahrenheit to Celsius": return (value - 32) / 1.8;
            case "Celsius to Kelvin": return value + 273.15;
            case "Kelvin to Celsius": return value - 273.15;
            default: return value;
        }
    }
}
