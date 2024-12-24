package com.jk.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class NoteEditorActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "NotePrefs";
    private static final String KEY_NOTE_COUNT = "NoteCount";
    private EditText noteTitle;
    private EditText noteContent;
    private int notePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_editor);

        noteTitle = findViewById(R.id.noteTitle);
        noteContent = findViewById(R.id.noteContent);

        notePosition = getIntent().getIntExtra("position", -1);  // Default to -1 for new note
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");

        if (notePosition != -1 && title != null && content != null) {
            noteTitle.setText(title);
            noteContent.setText(content);
        }

        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
    }

    private void saveNote() {
        String title = noteTitle.getText().toString();
        String content = noteContent.getText().toString();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Both fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (notePosition != -1) {
            editor.putString("note_title_" + notePosition, title);
            editor.putString("note_content_" + notePosition, content);
        } else {
            int noteCount = sharedPreferences.getInt(KEY_NOTE_COUNT, 0);
            editor.putString("note_title_" + noteCount, title);
            editor.putString("note_content_" + noteCount, content);
            editor.putInt(KEY_NOTE_COUNT, noteCount + 1);
        }

        editor.apply();
        Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}
