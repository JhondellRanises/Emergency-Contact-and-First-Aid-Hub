package com.example.emergencycontactandfirstaidhub;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedHashMap;
import java.util.Map;

public class FirstAidActivity extends AppCompatActivity {
    private final Map<String, String> guides = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.applySavedTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_aid);
        setTitle("First Aid Guide");
        seedGuides();
        ImageButton btnMore = findViewById(R.id.btnMore);
        btnMore.setOnClickListener(v -> ThemeUtils.showThemeMenu(this, btnMore));

        ListView list = findViewById(R.id.lvFirstAid);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_first_aid_topic, guides.keySet().toArray(new String[0]));
        list.setAdapter(adapter);
        list.setOnItemClickListener((parent, view, position, id) -> {
            String key = adapter.getItem(position);
            if ("Burns".equals(key)) {
                showBurnDegreeDialog();
                return;
            }
            if ("Bleeding".equals(key)) {
                showBleedingTypeDialog();
                return;
            }
            if ("Choking".equals(key)) {
                showChokingAgeDialog();
                return;
            }
            if ("CPR".equals(key)) {
                FirstAidGuideActivity.start(this, R.string.cpr_guide_title, FirstAidGuideBuilder.GUIDE_CPR);
                return;
            }
            if ("Fractures".equals(key)) {
                FirstAidGuideActivity.start(this, R.string.fractures_guide_title, FirstAidGuideBuilder.GUIDE_FRACTURES);
                return;
            }
            if ("Fainting".equals(key)) {
                FirstAidGuideActivity.start(this, R.string.fainting_guide_title, FirstAidGuideBuilder.GUIDE_FAINTING);
                return;
            }
            String message = guides.get(key);
            if (message != null && !message.isEmpty()) {
                FirstAidGuideActivity.startPlain(this, key, message);
            }
        });
    }

    private void seedGuides() {
        guides.put("Burns", "");
        guides.put("Bleeding", "");
        guides.put("Choking", "");
        guides.put("Fractures", "");
        guides.put("CPR", "");
        guides.put("Fainting", "");
        guides.put("Electric shock", "1. Turn off power source first.\n2. Do not touch victim until safe.\n3. Check breathing and start CPR if needed.\nWarning: Call emergency services immediately.");
        guides.put("Heat stroke", "1. Move to cool place.\n2. Cool body with wet cloth/ice packs.\n3. Give sips of cool water if conscious.\nWarning: Call emergency services immediately.");
        guides.put("Poisoning", "1. Remove from poison source.\n2. Do not force vomiting.\n3. Keep poison container for identification.\nWarning: Call emergency services immediately.");
    }

    private void showBurnDegreeDialog() {
        View content = getLayoutInflater().inflate(R.layout.dialog_burn_degree_choice, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.burn_degree_picker_title)
                .setView(content)
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        content.findViewById(R.id.row_burn_first).setOnClickListener(v -> {
            dialog.dismiss();
            FirstAidGuideActivity.start(this, R.string.burn_guide_first_title, FirstAidGuideBuilder.GUIDE_BURN_FIRST);
        });
        content.findViewById(R.id.row_burn_second).setOnClickListener(v -> {
            dialog.dismiss();
            FirstAidGuideActivity.start(this, R.string.burn_guide_second_title, FirstAidGuideBuilder.GUIDE_BURN_SECOND);
        });
        content.findViewById(R.id.row_burn_third).setOnClickListener(v -> {
            dialog.dismiss();
            FirstAidGuideActivity.start(this, R.string.burn_guide_third_title, FirstAidGuideBuilder.GUIDE_BURN_THIRD);
        });

        dialog.show();
    }

    private void showBleedingTypeDialog() {
        View content = getLayoutInflater().inflate(R.layout.dialog_bleeding_type_choice, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.bleeding_type_picker_title)
                .setView(content)
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        content.findViewById(R.id.row_bleeding_capillary).setOnClickListener(v -> {
            dialog.dismiss();
            FirstAidGuideActivity.start(this, R.string.bleeding_guide_capillary_title, FirstAidGuideBuilder.GUIDE_BLEEDING_CAPILLARY);
        });
        content.findViewById(R.id.row_bleeding_venous).setOnClickListener(v -> {
            dialog.dismiss();
            FirstAidGuideActivity.start(this, R.string.bleeding_guide_venous_title, FirstAidGuideBuilder.GUIDE_BLEEDING_VENOUS);
        });
        content.findViewById(R.id.row_bleeding_arterial).setOnClickListener(v -> {
            dialog.dismiss();
            FirstAidGuideActivity.start(this, R.string.bleeding_guide_arterial_title, FirstAidGuideBuilder.GUIDE_BLEEDING_ARTERIAL);
        });

        dialog.show();
    }

    private void showChokingAgeDialog() {
        View content = getLayoutInflater().inflate(R.layout.dialog_choking_age_choice, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.choking_age_picker_title)
                .setView(content)
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        content.findViewById(R.id.row_choking_infant).setOnClickListener(v -> {
            dialog.dismiss();
            FirstAidGuideActivity.start(this, R.string.choking_guide_infant_title, FirstAidGuideBuilder.GUIDE_CHOKING_INFANT);
        });
        content.findViewById(R.id.row_choking_above_one).setOnClickListener(v -> {
            dialog.dismiss();
            FirstAidGuideActivity.start(this, R.string.choking_guide_above_one_title, FirstAidGuideBuilder.GUIDE_CHOKING_ABOVE_ONE);
        });

        dialog.show();
    }
}
