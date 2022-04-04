package com.example.myroom.data.repository

import androidx.lifecycle.LiveData
import com.example.myroom.data.database.Note
import com.example.myroom.data.database.NotesDao

class NoteRepository(private val notesDao: NotesDao) {
    val allNotes: LiveData<List<Note>> = notesDao.getAllNotes()

    suspend fun insert(note: Note) {
        notesDao.insert(note)
    }

    suspend fun delete(note: Note){
        notesDao.delete(note)
    }

    suspend fun update(note: Note){
        notesDao.update(note)
    }
}