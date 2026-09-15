package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
  @PrimaryKey val id: String,
  val title: String,
  val description: String,
  val category: String, // e.g. "Mobile & Cloud", "Design System", "Infrastructure", "Marketing"
  val status: String,   // "Activo", "En Revisión", "Planificación", "Completado"
  val progress: Int,    // 0 - 100
  val memberCount: Int,
  val colorHex: Long = 0xFF234C34, // Forest green accent
  val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "tasks")
data class TaskEntity(
  @PrimaryKey val id: String,
  val projectId: String,
  val title: String,
  val description: String,
  val status: String,    // "Pendiente", "En Progreso", "En Revisión", "Completada"
  val priority: String,  // "Baja", "Media", "Alta", "Urgente"
  val assignedToName: String,
  val assignedToRole: String,
  val dueDate: String,
  val tag: String,
  val isVoiceCreated: Boolean = false,
  val voiceTranscript: String? = null,
  val voiceDurationSec: Int = 0,
  val createdAt: Long = System.currentTimeMillis(),
  val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
  @PrimaryKey val id: String,
  val projectId: String,
  val senderName: String,
  val senderRole: String,
  val message: String,
  val timestamp: Long = System.currentTimeMillis(),
  val isVoiceNote: Boolean = false,
  val voiceDurationSec: Int = 0,
  val isMine: Boolean = false
)

@Entity(tableName = "activity_logs")
data class ActivityLogEntity(
  @PrimaryKey val id: String,
  val projectId: String,
  val actorName: String,
  val actionText: String,
  val targetItem: String,
  val timestamp: Long = System.currentTimeMillis(),
  val category: String = "task" // "task", "project", "chat", "voice"
)

@Entity(tableName = "notifications")
data class NotificationEntity(
  @PrimaryKey val id: String,
  val title: String,
  val body: String,
  val timestamp: Long = System.currentTimeMillis(),
  val isRead: Boolean = false,
  val type: String = "task" // "mention", "task", "deadline", "team"
)

@Entity(tableName = "users")
data class UserEntity(
  @PrimaryKey val id: String,
  val name: String,
  val email: String,
  val role: String,        // "Project Lead / Admin", "Senior Engineer", "Product Designer", "DevOps"
  val department: String,  // "Core Platform", "UX & Brand", "Infrastructure"
  val timezone: String,    // "UTC+1 (Madrid)", "UTC-8 (San Francisco)", "UTC+9 (Tokyo)"
  val status: String,      // "En línea", "En reunión", "Ausente"
  val avatarInitials: String,
  val isCurrentUser: Boolean = false
)
