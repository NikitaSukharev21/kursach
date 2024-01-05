package com.sandipbhattacharya.simplediceroller;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int delayTime = 20;
    int rollAnimations = 40;
    int[] diceImages = new int[]{R.drawable.d1, R.drawable.d2, R.drawable.d3, R.drawable.d4, R.drawable.d5, R.drawable.d6};
    Random random = new Random();
    TextView tvHelp;
    ImageView die1;
    ImageView die2;
    LinearLayout diceContainer;
    MediaPlayer mp;
    RadioGroup radioGroup;
    Button rollAgainButton;

    int rollCount = 0;
    int dice1Value, dice2Value;
    int sum = 0; // Переменная для хранения суммы всех бросков

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvHelp = findViewById(R.id.tvHelp);
        diceContainer = findViewById(R.id.diceContainer);
        die1 = findViewById(R.id.die1);
        die2 = findViewById(R.id.die2);
        mp = MediaPlayer.create(this, R.raw.roll);
        radioGroup = findViewById(R.id.radioGroup);
        rollAgainButton = findViewById(R.id.rollAgainButton);

        diceContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    rollDice();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        rollAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame(); // Вызываем метод для сброса
            }
        });
    }

    private void rollDice() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < rollAnimations; i++) {
                    int newDice1 = random.nextInt(6) + 1;
                    int newDice2 = random.nextInt(6) + 1;
                    dice1Value = newDice1;
                    dice2Value = newDice2;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                            int numberOfDice = (selectedRadioButtonId == R.id.radioButton1) ? 1 : 2;

                            if (numberOfDice == 1) {
                                die2.setVisibility(View.INVISIBLE);
                                sum = dice1Value; // Сумма равна значению одного кубика
                            } else {
                                die1.setImageResource(diceImages[dice1Value - 1]);
                                die2.setImageResource(diceImages[dice2Value - 1]);
                                die2.setVisibility(View.VISIBLE);
                                sum = dice1Value + dice2Value; // Сумма равна значению двух кубиков
                            }
                        }
                    });

                    try {
                        Thread.sleep(delayTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                rollCount++;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvHelp.setText("Сумма очков: " + sum); // Отображение суммы
                        tvHelp.append("\nКоличесво бросков: " + rollCount); // Отображение количества бросков
                    }
                });
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();

        if (mp != null) {
            mp.start();
        }
    }

    // Метод для сброса счетчика и виджетов
    private void resetGame() {
        rollCount = 0;
        sum = 0;
        tvHelp.setText("Нажми на экран чтобы начать.");
        // Очищаем изображения кубиков и делаем второй кубик невидимым
        die1.setImageResource(R.drawable.d6);
        die2.setImageResource(R.drawable.d6);
        die2.setVisibility(View.INVISIBLE);
    }

}
