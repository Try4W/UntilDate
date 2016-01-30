package org.flycraft.android.untildate.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NotesStorage {

    private static final String DATA_KEY = "notesStorageData";

    private static NotesStorage instance;

    private final SharedPreferences prefs;
    private final Gson gson = Converters.registerDateTime(new GsonBuilder()).create();

    private List<Note> notes = new ArrayList<>();
    private Type notesType = new TypeToken<List<Note>>() {}.getType();

    private NotesStorage(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        loadData();
    }

    public static void init(Context context) {
        if(instance == null) {
            instance = new NotesStorage(context);
        }
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void remove(int position) {
        notes.remove(position);
        saveData();
    }

    public void add(Note note) {
        notes.add(0, note);
        saveData();
    }

    public void remove(Note note) {
        notes.remove(note);
    }

    private void loadData() {
        String notesJson = prefs.getString(DATA_KEY, "[]");
        notes = gson.fromJson(notesJson, notesType);
    }

    public void saveData() {
        String notesJson = gson.toJson(notes, notesType);
        prefs.edit().putString(DATA_KEY, notesJson).apply();
    }

    public static NotesStorage getInstance() {
        return instance;
    }
}
