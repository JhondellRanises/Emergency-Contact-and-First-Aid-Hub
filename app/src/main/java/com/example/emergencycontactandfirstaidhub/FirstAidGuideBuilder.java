package com.example.emergencycontactandfirstaidhub;

import android.content.Context;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;

final class FirstAidGuideBuilder {

    static final String GUIDE_BURN_FIRST = "burn_first";
    static final String GUIDE_BURN_SECOND = "burn_second";
    static final String GUIDE_BURN_THIRD = "burn_third";
    static final String GUIDE_BLEEDING_CAPILLARY = "bleeding_capillary";
    static final String GUIDE_BLEEDING_VENOUS = "bleeding_venous";
    static final String GUIDE_BLEEDING_ARTERIAL = "bleeding_arterial";
    static final String GUIDE_CHOKING_ABOVE_ONE = "choking_above_one";
    static final String GUIDE_CHOKING_INFANT = "choking_infant";
    static final String GUIDE_CPR = "cpr";
    static final String GUIDE_FRACTURES = "fractures";
    static final String GUIDE_FAINTING = "fainting";

    private FirstAidGuideBuilder() {
    }

    static void populate(Context context, LinearLayout sections, String guideKey) {
        switch (guideKey) {
            case GUIDE_BURN_FIRST:
                addHtmlSection(context, sections, R.string.burn_guide_first_body);
                break;
            case GUIDE_BURN_SECOND:
                addHtmlSection(context, sections, R.string.burn_guide_second_body);
                break;
            case GUIDE_BURN_THIRD:
                addHtmlSection(context, sections, R.string.burn_guide_third_body);
                break;
            case GUIDE_BLEEDING_CAPILLARY:
                addHtmlSection(context, sections, R.string.bleeding_guide_capillary_body);
                break;
            case GUIDE_BLEEDING_VENOUS:
                addHtmlSection(context, sections, R.string.bleeding_guide_venous_body);
                break;
            case GUIDE_BLEEDING_ARTERIAL:
                addHtmlSection(context, sections, R.string.bleeding_guide_arterial_body);
                break;
            case GUIDE_CHOKING_ABOVE_ONE:
                populateChokingAboveOne(context, sections);
                break;
            case GUIDE_CHOKING_INFANT:
                populateChokingInfant(context, sections);
                break;
            case GUIDE_CPR:
                populateCpr(context, sections);
                break;
            case GUIDE_FRACTURES:
                populateFractures(context, sections);
                break;
            case GUIDE_FAINTING:
                populateFainting(context, sections);
                break;
            default:
                break;
        }
    }

    private static void populateChokingAboveOne(Context context, LinearLayout sections) {
        addHtmlSection(context, sections, R.string.choking_above_one_step1);
        addHtmlSection(context, sections, R.string.choking_above_one_back_blows_heading);
        addGuideImage(context, sections, R.drawable.back_blows, R.string.choking_back_blows_image_desc);
        addHtmlSection(context, sections, R.string.choking_above_one_back_blows_steps);
        addHtmlSection(context, sections, R.string.choking_above_one_abdominal_heading);
        addGuideImage(context, sections, R.drawable.abdominal_thrust, R.string.choking_abdominal_thrust_image_desc);
        addHtmlSection(context, sections, R.string.choking_above_one_abdominal_steps);
        addHtmlSection(context, sections, R.string.choking_above_one_repeat);
    }

    private static void populateChokingInfant(Context context, LinearLayout sections) {
        addHtmlSection(context, sections, R.string.choking_infant_back_blows_heading);
        addGuideImage(context, sections, R.drawable.infant_back_blows, R.string.choking_infant_back_blows_image_desc);
        addHtmlSection(context, sections, R.string.choking_infant_back_blows_steps);
        addHtmlSection(context, sections, R.string.choking_infant_chest_heading);
        addGuideImage(context, sections, R.drawable.infant_chest_thrust, R.string.choking_infant_chest_thrust_image_desc);
        addHtmlSection(context, sections, R.string.choking_infant_chest_steps);
        addHtmlSection(context, sections, R.string.choking_infant_repeat);
    }

    private static void populateCpr(Context context, LinearLayout sections) {
        addHtmlSection(context, sections, R.string.cpr_intro);
        addGuideImage(context, sections, R.drawable.laydown, R.string.cpr_laydown_image_desc);
        addHtmlSection(context, sections, R.string.cpr_check_breathing);
        addGuideImage(context, sections, R.drawable.pulse, R.string.cpr_pulse_image_desc);
        addHtmlSection(context, sections, R.string.cpr_heading);
        addGuideImage(context, sections, R.drawable.cpr, R.string.cpr_chest_compressions_image_desc);
        addHtmlSection(context, sections, R.string.cpr_technique);
        addHtmlSection(context, sections, R.string.cpr_opening_airway);
        addHtmlSection(context, sections, R.string.cpr_rescue_heading);
        addGuideImage(context, sections, R.drawable.mouth, R.string.cpr_rescue_breaths_image_desc);
        addHtmlSection(context, sections, R.string.cpr_rescue_breaths);
        addHtmlSection(context, sections, R.string.cpr_continue);
    }

    private static void populateFractures(Context context, LinearLayout sections) {
        addHtmlSection(context, sections, R.string.fractures_get_help);
        addHtmlSection(context, sections, R.string.fractures_what_not_to_do);
        addHtmlSection(context, sections, R.string.fractures_step1);
        addHtmlSection(context, sections, R.string.fractures_step2);
        addHtmlSection(context, sections, R.string.fractures_step3_heading);
        addGuideImage(context, sections, R.drawable.splint, R.string.fractures_splint_image_desc);
        addHtmlSection(context, sections, R.string.fractures_step3_steps);
        addHtmlSection(context, sections, R.string.fractures_step4_heading);
        addGuideImage(context, sections, R.drawable.cold_compress, R.string.fractures_cold_compress_image_desc);
        addHtmlSection(context, sections, R.string.fractures_step4_steps);
        addHtmlSection(context, sections, R.string.fractures_step5);
    }

    private static void populateFainting(Context context, LinearLayout sections) {
        addHtmlSection(context, sections, R.string.fainting_intro);
        addHtmlSection(context, sections, R.string.fainting_step1_heading);
        addGuideImage(context, sections, R.drawable.faint, R.string.fainting_faint_image_desc);
        addHtmlSection(context, sections, R.string.fainting_step1_steps);
        addHtmlSection(context, sections, R.string.fainting_step2_heading);
        addGuideImage(context, sections, R.drawable.elevate_legs, R.string.fainting_elevate_legs_image_desc);
        addHtmlSection(context, sections, R.string.fainting_step2_steps);
        addHtmlSection(context, sections, R.string.fainting_step3_heading);
        addGuideImage(context, sections, R.drawable.remove_cloths, R.string.fainting_remove_clothes_image_desc);
        addHtmlSection(context, sections, R.string.fainting_step3_steps);
        addHtmlSection(context, sections, R.string.fainting_step4_heading);
        addGuideImage(context, sections, R.drawable.talk, R.string.fainting_talk_image_desc);
        addHtmlSection(context, sections, R.string.fainting_step4_steps);
    }

    static void populatePlainText(Context context, LinearLayout sections, String message) {
        TextView tv = new TextView(context);
        styleGuideText(context, tv);
        tv.setText(message);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        sections.addView(tv, params);
    }

    private static void addHtmlSection(Context context, LinearLayout parent, int htmlRes) {
        TextView tv = new TextView(context);
        styleGuideText(context, tv);
        tv.setText(HtmlCompat.fromHtml(
                context.getString(htmlRes),
                HtmlCompat.FROM_HTML_MODE_COMPACT,
                null,
                null));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = sectionSpacingPx(context);
        parent.addView(tv, params);
    }

    private static void addGuideImage(Context context, LinearLayout parent, int drawableRes, int contentDescriptionRes) {
        ImageView image = new ImageView(context);
        image.setImageResource(drawableRes);
        image.setAdjustViewBounds(true);
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        image.setContentDescription(context.getString(contentDescriptionRes));
        int maxHeight = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                200f,
                context.getResources().getDisplayMetrics());
        image.setMaxHeight(maxHeight);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = sectionSpacingPx(context);
        params.bottomMargin = sectionSpacingPx(context) / 2;
        parent.addView(image, params);
    }

    private static void styleGuideText(Context context, TextView tv) {
        tv.setTextColor(ContextCompat.getColor(context, R.color.text_primary));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
        float density = context.getResources().getDisplayMetrics().density;
        tv.setLineSpacing(4f * density, 1.12f);
        tv.setBreakStrategy(android.text.Layout.BREAK_STRATEGY_BALANCED);
        tv.setHyphenationFrequency(android.text.Layout.HYPHENATION_FREQUENCY_NONE);
    }

    private static int sectionSpacingPx(Context context) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                10f,
                context.getResources().getDisplayMetrics());
    }
}
