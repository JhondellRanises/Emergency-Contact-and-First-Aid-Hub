package com.example.emergencycontactandfirstaidhub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FirstAidGuideActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE_RES = "title_res";
    public static final String EXTRA_GUIDE_KEY = "guide_key";
    public static final String EXTRA_PLAIN_TITLE = "plain_title";
    public static final String EXTRA_PLAIN_MESSAGE = "plain_message";

    public static void start(Context context, int titleRes, String guideKey) {
        context.startActivity(new Intent(context, FirstAidGuideActivity.class)
                .putExtra(EXTRA_TITLE_RES, titleRes)
                .putExtra(EXTRA_GUIDE_KEY, guideKey));
    }

    public static void startPlain(Context context, String title, String message) {
        context.startActivity(new Intent(context, FirstAidGuideActivity.class)
                .putExtra(EXTRA_PLAIN_TITLE, title)
                .putExtra(EXTRA_PLAIN_MESSAGE, message));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.applySavedTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_aid_guide);

        TextView tvTitle = findViewById(R.id.tvGuideTitle);
        ImageButton btnBack = findViewById(R.id.btnGuideBack);
        LinearLayout sections = findViewById(R.id.guide_sections);

        int titleRes = getIntent().getIntExtra(EXTRA_TITLE_RES, 0);
        String plainTitle = getIntent().getStringExtra(EXTRA_PLAIN_TITLE);
        if (plainTitle != null) {
            tvTitle.setText(plainTitle);
            setTitle(plainTitle);
        } else if (titleRes != 0) {
            tvTitle.setText(titleRes);
            setTitle(titleRes);
        }

        btnBack.setOnClickListener(v -> finish());

        String plainMessage = getIntent().getStringExtra(EXTRA_PLAIN_MESSAGE);
        if (plainMessage != null) {
            FirstAidGuideBuilder.populatePlainText(this, sections, plainMessage);
            return;
        }

        String guideKey = getIntent().getStringExtra(EXTRA_GUIDE_KEY);
        if (guideKey != null) {
            FirstAidGuideBuilder.populate(this, sections, guideKey);
        }
    }
}
