package com.example.service

import com.example.db.NoteDAO
import com.example.db.NoteTable
import com.example.db.TaskDAO
import com.example.db.TaskTable
import com.example.db.UserDAO
import com.example.db.fastNoteDao
import com.example.db.taskResponseDao
import com.example.db.utils.suspendTransaction
import com.example.model.FastNoteRequest
import com.example.model.FastNoteResponse
import com.example.repository.FastNoteRepository
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

class FastNoteService : FastNoteRepository {
    override suspend fun getAllNotes(): List<FastNoteResponse> = suspendTransaction {
        NoteDAO.all().map(:: fastNoteDao)
    }

    override suspend fun noteById(id: Int): FastNoteResponse? = suspendTransaction {
        NoteDAO
            .find { (NoteTable.id eq id) }
            .limit(1)
            .map(:: fastNoteDao)
            .firstOrNull()
    }

    override suspend fun createNote(note: FastNoteRequest): Unit = suspendTransaction {
        val userDao = note.userId?.let { UserDAO.findById(it) }
        if (userDao == null) throw IllegalArgumentException("User not found")

        NoteDAO.new {
            title = note.title
            description = note.description
            user = userDao
        }
    }

    override suspend fun deleteNote(id: Int): Boolean = suspendTransaction {
        val rowsDeleted = NoteTable.deleteWhere {
            NoteTable.id eq id
        }
        rowsDeleted == 1
    }

    override suspend fun getNotesByUser(id: String?): List<FastNoteResponse> = suspendTransaction{
        NoteDAO
            .find { (NoteTable.userId eq id) }
            .map(:: fastNoteDao)
    }
}