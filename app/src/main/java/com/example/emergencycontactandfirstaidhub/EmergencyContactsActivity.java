package com.example.emergencycontactandfirstaidhub;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EmergencyContactsActivity extends AppCompatActivity {
    private AppDatabase db;
    private ContactAdapter adapter;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        setTitle("Emergency Contacts");

        db = new AppDatabase(this);
        etSearch = findViewById(R.id.etSearch);
        RecyclerView rv = findViewById(R.id.rvContacts);
        FloatingActionButton fabAdd = findViewById(R.id.fabAddContact);
        FloatingActionButton fabSos = findViewById(R.id.fabSos);

        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContactAdapter(this, new ContactAdapter.ContactActions() {
            @Override
            public void onEdit(EmergencyContact contact) {
                showContactDialog(contact);
            }

            @Override
            public void onDelete(EmergencyContact contact) {
                db.deleteContact(contact.id);
                loadContacts();
            }
        });
        rv.setAdapter(adapter);
        loadContacts();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { loadContacts(); }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        fabAdd.setOnClickListener(v -> showContactDialog(null));
        fabSos.setOnClickListener(v -> showSosDialog());

        if (getIntent().getBooleanExtra("open_sos", false)) {
            showSosDialog();
        }
    }

    private void loadContacts() {
        adapter.setData(db.getAllContacts(etSearch.getText().toString().trim()));
    }

    private void showContactDialog(EmergencyContact contact) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_contact, null);
        EditText etName = view.findViewById(R.id.etName);
        EditText etPhone = view.findViewById(R.id.etPhone);
        EditText etType = view.findViewById(R.id.etType);

        boolean edit = contact != null;
        if (edit) {
            etName.setText(contact.name);
            etPhone.setText(contact.phone);
            etType.setText(contact.type);
        }

        new AlertDialog.Builder(this)
                .setTitle(edit ? "Edit Contact" : "Add Contact")
                .setView(view)
                .setPositiveButton("Save", (d, w) -> {
                    String name = etName.getText().toString().trim();
                    String phone = etPhone.getText().toString().trim();
                    String type = etType.getText().toString().trim();
                    if (name.isEmpty() || phone.isEmpty()) {
                        Toast.makeText(this, "Name and phone are required.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (edit) {
                        contact.name = name;
                        contact.phone = phone;
                        contact.type = type;
                        db.updateContact(contact);
                    } else {
                        db.insertContact(new EmergencyContact(name, phone, type));
                    }
                    loadContacts();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showSosDialog() {
        StringBuilder sb = new StringBuilder("Call emergency services immediately.\n\n");
        for (EmergencyContact c : db.getAllContacts("")) {
            if (c.name.equalsIgnoreCase("Police") || c.name.equalsIgnoreCase("Fire Department") || c.name.equalsIgnoreCase("Ambulance")) {
                sb.append(c.name).append(": ").append(c.phone).append("\n");
            }
        }
        new AlertDialog.Builder(this)
                .setTitle("SOS")
                .setMessage(sb.toString())
                .setPositiveButton("Close", null)
                .show();
    }
}
