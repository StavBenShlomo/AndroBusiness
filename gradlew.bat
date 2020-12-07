package com.example.coinscalculator;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    private int sum, shekel1, shekel2, shekel5, shekel10;

    private Button button0, button1, button2, button3, button4, button5,
            button6, button7, button8, button9, Clear, Calulate;
    private EditText price, coins_1shekel, coins_2shekel, coins_5shekel, coins_10shekel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button0 = (Button) findViewById(R.id.button0);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);
        button9 = (Button) findViewById(R.id.button9);
        
        Clear = (Button) findViewById(R.id.Clear);
        Calulate = (Button) findViewById(R.id.Calulate);

        price = (EditText)findViewById(R.id.price);

        coins_1shekel = (EditText)findViewById(R.id.coins_1shekel);
        coins_2shekel = (EditText)findViewById(R.id.coins_2shekel);
        coins_5shekel = (EditText)findViewById(R.id.coins_5shekel);
        coins_10shekel = (EditText)findViewById(R.id.coins_10shekel);


        button0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                price.setText(price.getText() + "0");
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                price.setText(price.getText() + "1");
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                