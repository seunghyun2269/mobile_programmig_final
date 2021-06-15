package com.sh.kimsh_60171583_final;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    MaterialCalendarView calendar;
    TextView dateText, wordView1, wordView2, wordView3, answerView1, answerView2, answerView3;
    Button inputButton, deleteButton, testButton;
    View dialogView;
    EditText word1, meaning1, word2, meaning2, word3, meaning3;
    String fileName, words, str;
    LinearLayout wordView, notiView;
    String[] wordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("매일 3영단어");

        calendar = (MaterialCalendarView)findViewById(R.id.calendar);
        wordView1 = (TextView) findViewById(R.id.word1);
        wordView2 = (TextView) findViewById(R.id.word2);
        wordView3 = (TextView) findViewById(R.id.word3);
        answerView1 = (TextView) findViewById(R.id.answer1);
        answerView2 = (TextView) findViewById(R.id.answer2);
        answerView3 = (TextView) findViewById(R.id.answer3);
        dateText = (TextView) findViewById(R.id.wordDate);
        inputButton = (Button) findViewById(R.id.inputButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        testButton = (Button) findViewById(R.id.testButton);
        wordView = (LinearLayout) findViewById(R.id.wordView);
        notiView = (LinearLayout) findViewById(R.id.notiView);

        calendar.setSelectedDate(CalendarDay.today());
        // calendar.addDecorator(new EventDecorator(Color.RED, Collections.singleton(CalendarDay.today())));

        final Calendar c = Calendar.getInstance();
        int cYear = c.get(Calendar.YEAR);
        int cMonth = c.get(Calendar.MONTH) + 1;
        int cDay = c.get(Calendar.DAY_OF_MONTH);

        checkedDay(cYear, cMonth, cDay);
        dateText.setText(Integer.toString(cMonth) + "월" + Integer.toString(cDay) + "일");


        // 달력의 날짜가 바뀔때 실행되는 이벤트 리스너
        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                // 날짜를 매개변수로 주면 해당 날짜에 저장된 단어가 있는지 체크하는 메소드 호출

                // CalendarDay selectedDate = calendar.getSelectedDate();
                // calendar.addDecorator(new EventDecorator(Color.RED, Collections.singleton(selectedDate)));
                dateText.setText(Integer.toString(date.getMonth()) + "월" + Integer.toString(date.getDay()) + "일");
                checkedDay(date.getYear(), date.getMonth(), date.getDay());
            }
        });

        // 삭제버튼 클릭시 (click event listener)
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filePath = "/data/data/com.sh.kimsh_60171583_final/files/" + fileName;
                File wordFile = new File(filePath);

                if(wordFile.exists()){
                    boolean deleted = wordFile.delete();
                    if(deleted == true){
                        // 단어 삭제 후 텍스트 비워주기
                        wordView1.setText(" ");
                        wordView2.setText(" ");
                        wordView3.setText(" ");
                        answerView1.setText(" ");
                        answerView2.setText(" ");
                        answerView3.setText(" ");

                        //단어 입력 버튼 문구 수정
                        inputButton.setText("단어입력");
                        testButton.setVisibility(View.INVISIBLE); // 테스트 버튼 숨기기
                        // wordView 숨기기
                        wordView.setVisibility(View.GONE);
                        notiView.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "단어가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // 단어 입력 버튼 클릭시
        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView = (View) View.inflate(MainActivity.this, R.layout.dialog, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                // dlg.setTitle("단어 입력");
                dlg.setView(dialogView);

                dlg.setPositiveButton("입력", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        word1 = (EditText)dialogView.findViewById(R.id.word1);
                        meaning1 = (EditText)dialogView.findViewById(R.id.meaning1);
                        word2 = (EditText)dialogView.findViewById(R.id.word2);
                        meaning2 = (EditText)dialogView.findViewById(R.id.meaning2);
                        word3 = (EditText)dialogView.findViewById(R.id.word3);
                        meaning3 = (EditText)dialogView.findViewById(R.id.meaning3);

                        words = word1.getText().toString()  + "," + meaning1.getText().toString() + ","
                                 + word2.getText().toString() + "," + meaning2.getText().toString() + ","
                                 + word3.getText().toString() + "," + meaning3.getText().toString();

                        // fileName을 넣고 저장 시키는 메소드 호출
                        save(fileName, words);
                    }
                });
                dlg.setNegativeButton("취소", null);
                dlg.show();
            }
        });

        // 단어 테스트 버튼 클릭시
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TestActivity.class);
                intent.putExtra("wordFile", str);

                startActivityForResult(intent, 0);
            }
        });
    }

    // 테스트 액티비티로 부터 결과 받아와 처리하는 메소드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String cleared = data.getStringExtra("cleared");

            if(cleared.equals("true")){
                // 3단계까지 다 풀었을 때
                Toast.makeText(getApplicationContext(), "성공!", Toast.LENGTH_SHORT).show();
            }else{
                // 3단계까지 다 못풀었을 때
                Toast.makeText(getApplicationContext(), "실패...", Toast.LENGTH_SHORT).show();
            }

        }
    }

    // 단어를 입력하면 저장하는 메소드
    private void save(String fileN, String words) {
        try {
            FileOutputStream outFs = openFileOutput(fileN, Context.MODE_PRIVATE);
            outFs.write(words.getBytes());
            outFs.close();

            //저장후 set text
            FileInputStream inFs = openFileInput(fileN);
            byte[] txt = new byte[300];
            inFs.read(txt);
            str = new String(txt);
            inFs.close();

            wordList = str.split(",");

            // 저장후 오늘의 단어란에 띄우기
            wordView1.setText(wordList[0]);
            answerView1.setText(wordList[1]);
            wordView2.setText(wordList[2]);
            answerView2.setText(wordList[3]);
            wordView3.setText(wordList[4]);
            answerView3.setText(wordList[5]);

            //단어 저장 후 입력버튼 문구수정
            inputButton.setText("단어수정");
            // 테스트 버튼 활성화
            testButton.setVisibility(View.VISIBLE);
            // word view보이게 설정
            wordView.setVisibility(View.VISIBLE);
            notiView.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "단어저장", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "저장오류", Toast.LENGTH_SHORT).show();
        }
    }

    // 달력에서 날짜 변경시 -> 저장된 단어가 있는지 체크
    private void checkedDay( int year, int monthOfYear, int dayOfMonth) {
        // 파일 이름 생성 ex) "2021_06_12.txt"
        fileName = year + "_" + monthOfYear + "_" + dayOfMonth + ".txt";

        // 단어 써뒀는지 체크
        try {
            // 단어가 있을 경우 읽어오기
            FileInputStream inFs = openFileInput(fileName);
            byte[] txt = new byte[300];
            inFs.read(txt);
            str = new String(txt); //str에 해당날짜의 단어 파일 내용이 저장됨
            inFs.close();

            wordList = str.split(",");

            // 단어가 있으면 오늘의 단어란에 띄우기
            wordView1.setText(wordList[0]);
            answerView1.setText(wordList[1]);
            wordView2.setText(wordList[2]);
            answerView2.setText(wordList[3]);
            wordView3.setText(wordList[4]);
            answerView3.setText(wordList[5]);

            inputButton.setText("단어수정");
            testButton.setVisibility(View.VISIBLE); //테스트 버튼 다시 활성화
            //아래 단어 뷰 다시 보이게
            wordView.setVisibility(View.VISIBLE);
            notiView.setVisibility(View.GONE);

        } catch (Exception e) { // UnsupportedEncodingException , FileNotFoundException , IOException
            // 단어가 없으면 오류발생 (텍스트 뷰 비워주기) -> 단어 입력 필요
            wordView1.setText(" ");
            wordView2.setText(" ");
            wordView3.setText(" ");
            answerView1.setText(" ");
            answerView2.setText(" ");
            answerView3.setText(" ");

            inputButton.setText("단어입력");
            testButton.setVisibility(View.INVISIBLE); //테스트 버튼 비활성화
            //아래 단어 뷰 제거
            wordView.setVisibility(View.GONE);
            notiView.setVisibility(View.VISIBLE);
            e.printStackTrace();
        }
    }
}