package com.sh.kimsh_60171583_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TestActivity extends AppCompatActivity {
    String words;
    String[] wordList;
    TextView wordText1, wordText2, wordText3;
    Button btnReturn, btnCheck1, btnCheck2, btnCheck3;
    EditText answer1, answer2, answer3;
    LinearLayout testView1, testView2, testView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        setTitle("단어 테스트");

        wordText1 = (TextView)findViewById(R.id.wordText1);
        wordText2 = (TextView)findViewById(R.id.wordText2);
        wordText3 = (TextView)findViewById(R.id.wordText3);
        btnCheck1 = (Button)findViewById(R.id.btnCheck1);
        btnCheck2 = (Button)findViewById(R.id.btnCheck2);
        btnCheck3 = (Button)findViewById(R.id.btnCheck3);
        answer1 = (EditText)findViewById(R.id.answer1);
        answer2 = (EditText)findViewById(R.id.answer2);
        answer3 = (EditText)findViewById(R.id.answer3);
        testView1 = (LinearLayout) findViewById(R.id.testView1);
        testView2 = (LinearLayout) findViewById(R.id.testView2);
        testView3 = (LinearLayout) findViewById(R.id.testView3);
        btnReturn = (Button)findViewById(R.id.btnReturn);

        Intent intent = getIntent();
        words = intent.getStringExtra("wordFile");
        wordList = words.split(",");

        wordText1.setText(wordList[0]);
        wordText2.setText(wordList[2]);
        wordText3.setText(wordList[4]);

        btnCheck1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( wordList[1].equals(answer1.getText().toString())){
                    Toast.makeText(getApplicationContext(), "정답!", Toast.LENGTH_SHORT).show();
                    testView1.setVisibility(View.GONE);
                    testView2.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(getApplicationContext(), "다시 한번 체크 해보세요!...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCheck2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( wordList[3].equals(answer2.getText().toString())){
                    Toast.makeText(getApplicationContext(), "정답!", Toast.LENGTH_SHORT).show();
                    testView2.setVisibility(View.GONE);
                    testView3.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(getApplicationContext(), "다시 한번 체크 해보세요...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCheck3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //오또카징 ,,
                answer3.setText(wordList[5]);
                if( wordList[5].equals(answer3.getText().toString())){
                    // 3번째 단어까지 clear하면 cleared변수로 true전달하고 액티비티 종료
                    Intent outIntent = new Intent(getApplicationContext(), MainActivity.class);
                    outIntent.putExtra("cleared", "true");
                    setResult(RESULT_OK, outIntent);

                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "다시 한번 체크 해보세요!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 포기하면 cleared변수로 false전달하고 액티비티 종료
                Intent outIntent = new Intent(getApplicationContext(), MainActivity.class);
                outIntent.putExtra("cleared", "false");
                setResult(RESULT_OK, outIntent);

                finish();
            }
        });
    }
}