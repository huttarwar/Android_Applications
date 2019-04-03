package com.example.cs442huttarwar.temperatureconverter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private double C;
    private double F;
    private Button convert;
    private RadioButton ctof, ftoc;
    private TextView conversion,converted_value, history, historyscrollable;
    private EditText value_entered;
    private StringBuilder sb = new StringBuilder();
    private String string = "";
    private ImageView arrow;
    private double converted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        convert = findViewById(R.id.convert);
        conversion = findViewById(R.id.conversion);
        ctof = findViewById(R.id.C_F);
        ftoc = findViewById(R.id.F_C);
        value_entered = findViewById(R.id.value_entered);
        converted_value = findViewById(R.id.converted_value);
        history = findViewById(R.id.history);
        historyscrollable = findViewById(R.id.historyscrollable);
        historyscrollable.setMovementMethod(new ScrollingMovementMethod());
        /*arrow = findViewById(R.id.arrow);
        arrow.setVisibility(View.INVISIBLE);
        history.setVisibility(View.INVISIBLE);
        historyscrollable.setVisibility(View.INVISIBLE);
        converted_value.setVisibility(View.INVISIBLE);*/
        final DecimalFormat decimalFormat = new DecimalFormat("0.0");

        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hello = value_entered.getText().toString();
                if(ftoc.isChecked() && !hello.isEmpty()){
                    F = Double.parseDouble(hello);
                    converted = FtoC(F);
                    converted_value.setText(decimalFormat.format(converted));
                    arrow.setVisibility(View.VISIBLE);
                    history.setVisibility(View.VISIBLE);
                    historyscrollable.setVisibility(View.VISIBLE);
                    converted_value.setVisibility(View.VISIBLE);
                    sb.append(string);
                    sb.append("F to C: "+hello+"  ->  "+decimalFormat.format(converted)+"\n");
                    historyscrollable.setText(sb.toString());
                }
                else if(ctof.isChecked()&& !hello.isEmpty()){
                    C = Double.parseDouble(hello);
                    converted = CtoF(C);
                    converted_value.setText(decimalFormat.format(converted));
                    /*arrow.setVisibility(View.VISIBLE);
                    history.setVisibility(View.VISIBLE);
                    historyscrollable.setVisibility(View.VISIBLE);
                    converted_value.setVisibility(View.VISIBLE);*/
                    sb.append(string);
                    sb.append("C to F: "+hello+"  ->  "+decimalFormat.format(converted)+ "\n");
                    historyscrollable.setText(sb.toString());
                }
                else
                    Toast.makeText(getApplicationContext(),"Enter the value to be converted",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public double CtoF(double C){
        this.C = C;
        double conv = (C * 1.8)+32.0;
        return conv;
    }

    public double FtoC(double F){
        this.F = F;
        double conv1 = (F - 32.0)/1.8;
        return conv1;
    }

    @Override
    protected void onSaveInstanceState(Bundle out) {
        out.putString("EnteredValue", value_entered.getText().toString());
        out.putString("History", historyscrollable.getText().toString());
        out.putString("ConvertedValue",converted_value.getText().toString());
        super.onSaveInstanceState(out);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        /*arrow.setVisibility(View.VISIBLE);
        history.setVisibility(View.VISIBLE);
        historyscrollable.setVisibility(View.VISIBLE);
        converted_value.setVisibility(View.VISIBLE);*/
        converted_value.setText((savedInstanceState.getString("ConvertedValue")));
        historyscrollable.setText(savedInstanceState.getString("History"));
        value_entered.setText(savedInstanceState.getString("EnteredValue"));
        string = savedInstanceState.getString("History");
    }
}
