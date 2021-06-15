package com.sh.kimsh_60171583_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TestActivity extends AppCompatActivity {
    String words;
    String[] wordList;
    TextView wordText1, wordText2, wordText3;
    Button btnReturn, btnCheck;
    EditText answer1, answer2, answer3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        setTitle("단어 테스트");

        wordText1 = (TextView)findViewById(R.id.wordText1);
        wordText2 = (TextView)findViewById(R.id.wordText2);
        wordText3 = (TextView)findViewById(R.id.wordText3);
        btnReturn = (Button)findViewById(R.id.btnReturn);
        btnCheck = (Button)findViewById(R.id.btnCheck);
        answer1 = (EditText)findViewById(R.id.answer1);
        answer2 = (EditText)findViewById(R.id.answer2);
        answer3 = (EditText)findViewById(R.id.answer3);

        Intent intent = getIntent();
        words = intent.getStringExtra("wordFile");
        wordList = words.split(",");

        wordText1.setText(wordList[0]);
        wordText2.setText(wordList[2]);
        wordText3.setText(wordList[4]);

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( wordList[1].equals(answer1.getText().toString())
                        && wordList[3].equals(answer2.getText().toString())
                        && wordList[5].equals(answer3.getText().toString()) ){
                    Toast.makeText(getApplicationContext(), "정답입니다!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "조금 더 공부해보세요...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}