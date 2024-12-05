package com.example.localdb;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class BookDetailsActivity extends AppCompatActivity {
    private TextView textViewName, textViewAuthor;
    private Button deleteButton, editButton;
    private DataBaseHelper dbHelper;
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        textViewName = findViewById(R.id.textViewName);
        textViewAuthor = findViewById(R.id.textViewAuthor);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);

        dbHelper = new DataBaseHelper(this);

        int bookId = getIntent().getIntExtra("book_id", -1);
        if (bookId != -1) {
            book = getBookById(bookId);
            textViewName.setText(book.getBook_Name());
            textViewAuthor.setText(book.getBook_Author());
        }

        deleteButton.setOnClickListener(v -> deleteBook());
        editButton.setOnClickListener(v -> editBook());
    }

    private Book getBookById(int bookId) {
        Cursor cursor = dbHelper.getReadableDatabase().query(
                DataBaseHelper.TABLE_NAME,
                null,
                DataBaseHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(bookId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_NAME));
            String author = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseHelper.COLUMN_AUTHOR));
            return new Book(id, name, author);
        }
        return null;
    }

    private void deleteBook() {
        if (book != null) {
            dbHelper.deleteBookById(book.getID_Book());
            Toast.makeText(this, "Книга удалена", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void editBook() {
        Intent intent = new Intent(this, EditBookActivity.class);
        intent.putExtra("book_id", book.getID_Book());
        intent.putExtra("book_name", book.getBook_Name());
        intent.putExtra("book_author", book.getBook_Author());
        startActivity(intent);
    }
}
