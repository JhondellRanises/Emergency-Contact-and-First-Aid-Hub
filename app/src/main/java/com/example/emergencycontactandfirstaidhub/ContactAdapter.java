package com.example.emergencycontactandfirstaidhub;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.VH> {
    private static final int REQ_CALL_PHONE = 2001;

    public interface ContactActions {
        void onEdit(EmergencyContact contact);
        void onDelete(EmergencyContact contact);
        void onTogglePriority(EmergencyContact contact);
    }

    private final Context context;
    private final ContactActions actions;
    private List<EmergencyContact> data = new ArrayList<>();

    public ContactAdapter(Context context, ContactActions actions) {
        this.context = context;
        this.actions = actions;
    }

    public void setData(List<EmergencyContact> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        EmergencyContact item = data.get(position);
        h.tvName.setText(item.name);
        h.tvType.setText(item.type);
        h.tvPhone.setText(item.phone);
        h.btnPriority.setImageResource(item.isPriority == 1 ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off);
        h.itemView.setOnClickListener(v -> {
            List<String> rawNumbers = extractRawNumbers(item.phone);
            List<String> dialNumbers = extractDialCandidates(rawNumbers);
            if (dialNumbers.isEmpty()) return;

            if (dialNumbers.size() == 1) {
                startCall(dialNumbers.get(0));
                return;
            }

            String[] options = rawNumbers.toArray(new String[0]);
            new AlertDialog.Builder(context)
                    .setTitle("Select number to call")
                    .setItems(options, (dialog, which) -> startCall(dialNumbers.get(which)))
                    .setNegativeButton("Cancel", null)
                    .show();
        });
        h.btnPriority.setOnClickListener(v -> actions.onTogglePriority(item));
        h.btnEdit.setOnClickListener(v -> actions.onEdit(item));
        h.btnDelete.setOnClickListener(v -> actions.onDelete(item));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void startCall(String number) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (context instanceof AppCompatActivity) {
                ActivityCompat.requestPermissions((AppCompatActivity) context, new String[]{Manifest.permission.CALL_PHONE}, REQ_CALL_PHONE);
                Toast.makeText(context, "Allow phone permission, then tap again to call.", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        context.startActivity(intent);
    }

    private List<String> extractRawNumbers(String phoneRaw) {
        List<String> raw = new ArrayList<>();
        if (phoneRaw == null) return raw;
        String[] parts = phoneRaw.split("/");
        for (String part : parts) {
            String value = part.trim();
            if (!value.isEmpty()) {
                raw.add(value);
            }
        }
        if (raw.isEmpty() && !phoneRaw.trim().isEmpty()) {
            raw.add(phoneRaw.trim());
        }
        return raw;
    }

    private List<String> extractDialCandidates(List<String> rawNumbers) {
        List<String> cleaned = new ArrayList<>();
        for (String value : rawNumbers) {
            StringBuilder number = new StringBuilder();
            for (int i = 0; i < value.length(); i++) {
                char ch = value.charAt(i);
                if (Character.isDigit(ch)) {
                    number.append(ch);
                } else if (ch == '+' && number.length() == 0) {
                    number.append(ch);
                }
            }
            if (number.length() > 0) {
                cleaned.add(number.toString());
            }
        }
        return cleaned;
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvType, tvPhone;
        ImageButton btnPriority, btnEdit, btnDelete;
        VH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvType = itemView.findViewById(R.id.tvType);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            btnPriority = itemView.findViewById(R.id.btnPriority);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
