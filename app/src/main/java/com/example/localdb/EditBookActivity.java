package com.example.localdb;

import android.content.ContentValues;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditBookActivity extends AppCompatActivity {
    private EditText editTextName, editTextAuthor;
    private Button saveButton;
    private DataBaseHelper dbHelper;
    private int bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        editTextName = findViewById(R.id.editTextName);
        editTextAuthor = findViewById(R.id.editTextAuthor);
        saveButton = findViewById(R.id.saveButton);

        dbHelper = new DataBaseHelper(this);

        bookId = getIntent().getIntExtra("book_id", -1);
        String bookName = getIntent().getStringExtra("book_name");
        String bookAuthor = getIntent().getStringExtra("book_author");

        if (bookId != -1) {
            editTextName.setText(bookName);
            editTextAuthor.setText(bookAuthor);
        }

        saveButton.setOnClickListener(v -> saveBook());
    }

    private void saveBook() {
        String bookName = editTextName.getText().toString().trim();
        String bookAuthor = editTextAuthor.getText().toString().trim();

        if (bookName.isEmpty() || bookAuthor.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.COLUMN_NAME, bookName);
        values.put(DataBaseHelper.COLUMN_AUTHOR, bookAuthor);

        int result = dbHelper.getWritableDatabase().update(
                DataBaseHelper.TABLE_NAME,
                values,
                DataBaseHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(bookId)});

        if (result > 0) {
            Toast.makeText(this, "Книга обновлена", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Ошибка обновления книги", Toast.LENGTH_SHORT).show();
        }
    }
}
