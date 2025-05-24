package com.example.repository

import com.example.model.SubjectRequest
import com.example.model.SubjectResponse

interface SubjectRepository {
    suspend fun getAllSubject() : List<SubjectResponse>
    suspend fun subjectById(id: Int): SubjectResponse?
    suspend fun createSubject(subject: SubjectRequest)
    suspend fun updateSubject(id: Int, subject: SubjectRequest)
    suspend fun deleteSubject(id: Int): Boolean
    suspend fun findSubjectByUser(id: String?): List<SubjectResponse>
}