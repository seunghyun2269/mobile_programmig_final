package com.sh.kimsh_60171583_final;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;

// 앱 시작시 뜨는 split view
// 대화 상자에 아이콘 추가
// 퀴즈 잘 풀었을때, 틀렸을때 -> 토스트 꾸미기 (우선 그냥 메세지로 처리)
// 단어 - 뜻을 별개로 저장
// 버튼 클릭시 explicit activity를 사용해 데이터 전당 -> 퀴즈 풀기 기능 추가
// view container, adapterview는 어디에??

public class MainActivity extends AppCompatActivity {

    CalendarView cal;
    TextView wordText, dateText;
    Button inputButton, deleteButton, testButton;
    View dialogView;
    EditText word1, meaning1, word2, meaning2, word3, meaning3;
    String fileName, words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("매일 3영단어");

        cal = (CalendarView)findViewById(R.id.calendar);
        wordText = (TextView) findViewById(R.id.wordText);
        dateText = (TextView) findViewById(R.id.wordDate);
        inputButton = (Button) findViewById(R.id.inputButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        testButton = (Button) findViewById(R.id.testButton);

        final Calendar c = Calendar.getInstance();
        int cYear = c.get(Calendar.YEAR);
        int cMonth = c.get(Calendar.MONTH) + 1;
        int cDay = c.get(Calendar.DAY_OF_MONTH);

        checkedDay(cYear, cMonth, cDay);
        dateText.setText(Integer.toString(cYear) + "-" + Integer.toString(cMonth) + "-" + Integer.toString(cDay));

        // 달력의 날짜가 바뀔때 실행되는 이벤트 리스너
        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                checkedDay(year, month + 1, dayOfMonth);
                dateText.setText(Integer.toString(year) + "-" + Integer.toString(month + 1) + "-" + Integer.toString(dayOfMonth));
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
                        // 단어 삭제 후
                        wordText.setText(" "); //텍스트 비워주기
                        inputButton.setText("단어입력"); //단어 입력 버튼 문구 수정
                        testButton.setVisibility(View.INVISIBLE); //테스트 버튼 숨기기
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
                dlg.setTitle("오늘의 단어입력");
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

                        words = "word 1 : " + word1.getText().toString()  + " (" + meaning1.getText().toString() + ")" + "\r\n" +
                                "word 2 : " + word2.getText().toString() + " (" + meaning2.getText().toString() + ")" +  "\r\n" +
                                "word 3 : " + word3.getText().toString() + " (" + meaning3.getText().toString() + ")";

                        // fileName을 넣고 저장 시키는 메소드 호출
                        save(fileName, words);
                    }
                });
                dlg.setNegativeButton("취소", null);
                dlg.show();
            }
        });
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
            String str = new String(txt);
            inFs.close();
            wordText.setText(str);

            //단어 저장 후 입력버튼 문구수정 + 테스트 버튼 활성화
            inputButton.setText("단어수정");
            testButton.setVisibility(View.VISIBLE);
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
            String str = new String(txt);
            inFs.close();

            wordText.setText(str);
            inputButton.setText("단어수정");
            testButton.setVisibility(View.VISIBLE);

        } catch (Exception e) { // UnsupportedEncodingException , FileNotFoundException , IOException
            // 단어가 없으면 오류발생 -> 단어 입력 필요
            wordText.setText("");
            inputButton.setText("단어입력");
            testButton.setVisibility(View.INVISIBLE);
            e.printStackTrace();
        }
    }
}