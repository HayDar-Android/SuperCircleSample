package io.haydar.supercircle.sample;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SuperCircleView mSuperCircleView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.tv);
        mSuperCircleView = (SuperCircleView) findViewById(R.id.superview);
        mSuperCircleView.setShowSelect(false);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 100);
        valueAnimator.setTarget(textView);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int i = Integer.valueOf(String.valueOf(animation.getAnimatedValue()));
                textView.setText(i + "");
                mSuperCircleView.setSelect((int) (360 * (i / 100f)));
            }
        });
        valueAnimator.start();
    }
}
