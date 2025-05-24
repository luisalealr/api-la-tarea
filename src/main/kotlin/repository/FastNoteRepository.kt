package com.example.repository

import com.example.model.FastNote

interface FastNoteRepository {
    suspend fun getAllNotes() : List<FastNote>
    suspend fun noteById(id: Int): FastNote?
    suspend fun createNote(note: FastNote)
    suspend fun updateNote(id: Int, note: FastNote)
    suspend fun deleteNote(id: Int): Boolean
}