package com.example.service

import com.example.db.NoteDAO
import com.example.db.NoteTable
import com.example.db.TaskDAO
import com.example.db.UserDAO
import com.example.db.daoToModel
import com.example.db.utils.suspendTransaction
import com.example.model.FastNote
import com.example.repository.FastNoteRepository
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

class FastNoteService : FastNoteRepository {
    override suspend fun getAllNotes(): List<FastNote> = suspendTransaction {
        NoteDAO.all().map(:: daoToModel)
    }

    override suspend fun noteById(id: Int): FastNote? = suspendTransaction {
        NoteDAO
            .find { (NoteTable.id eq id) }
            .limit(1)
            .map(:: daoToModel)
            .firstOrNull()
    }

    override suspend fun createNote(note: FastNote): Unit = suspendTransaction {
        val userDao = note.userId?.let { UserDAO.findById(it) }

        NoteDAO.new {
            title = note.title
            description = note.description
            createdAt = note.createdAt
            user = userDao
        }
    }

    override suspend fun updateNote(id: Int, note: FastNote): Unit = suspendTransaction {
        val noteToUpdate = NoteDAO.findById(id)
            ?: throw IllegalArgumentException("No se encontró la nota con id $id")

        noteToUpdate.title = note.title
        noteToUpdate.description = note.description
    }

    override suspend fun deleteNote(id: Int): Boolean = suspendTransaction {
        val rowsDeleted = NoteTable.deleteWhere {
            NoteTable.id eq id
        }
        rowsDeleted == 1
    }
}