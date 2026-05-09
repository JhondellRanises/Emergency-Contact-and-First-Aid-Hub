package com.example.emergencycontactandfirstaidhub;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.VH> {
    public interface ContactActions {
        void onEdit(EmergencyContact contact);
        void onDelete(EmergencyContact contact);
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
        h.btnCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + item.phone));
            context.startActivity(intent);
        });
        h.btnEdit.setOnClickListener(v -> actions.onEdit(item));
        h.btnDelete.setOnClickListener(v -> actions.onDelete(item));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvType, tvPhone;
        ImageButton btnCall, btnEdit, btnDelete;
        VH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvType = itemView.findViewById(R.id.tvType);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            btnCall = itemView.findViewById(R.id.btnCall);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
