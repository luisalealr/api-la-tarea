package com.example.repository

import com.example.model.FastNoteRequest
import com.example.model.FastNoteResponse

interface FastNoteRepository {
    suspend fun getAllNotes() : List<FastNoteResponse>
    suspend fun noteById(id: Int): FastNoteResponse?
    suspend fun createNote(note: FastNoteRequest)
    suspend fun deleteNote(id: Int): Boolean
    suspend fun getNotesByUser(id: String?): List<FastNoteResponse>
}