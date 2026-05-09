package com.example.emergencycontactandfirstaidhub;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class ThemeUtils {
    private static final String PREFS = "ui_prefs";
    private static final String KEY_NIGHT = "night_mode";

    public static void applySavedTheme(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        boolean night = prefs.getBoolean(KEY_NIGHT, false);
        AppCompatDelegate.setDefaultNightMode(
                night ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
    }

    public static void toggleTheme(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        boolean next = !prefs.getBoolean(KEY_NIGHT, false);
        prefs.edit().putBoolean(KEY_NIGHT, next).apply();
        AppCompatDelegate.setDefaultNightMode(
                next ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );
    }

    public static boolean isNightMode(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getBoolean(KEY_NIGHT, false);
    }

    public static void showThemeMenu(AppCompatActivity activity, View anchor) {
        PopupMenu menu = new PopupMenu(activity, anchor);
        String label = isNightMode(activity) ? "Switch to Light Mode" : "Switch to Dark Mode";
        menu.getMenu().add(Menu.NONE, 1, Menu.NONE, label);
        menu.setOnMenuItemClickListener(item -> {
            toggleTheme(activity);
            activity.recreate();
            return true;
        });
        menu.show();
    }
}
