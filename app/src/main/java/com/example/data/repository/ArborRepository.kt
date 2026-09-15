package com.example.data.repository

import com.example.data.local.ArborDao
import com.example.data.model.ActivityLogEntity
import com.example.data.model.ChatMessageEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.ProjectEntity
import com.example.data.model.TaskEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.UUID

class ArborRepository(private val dao: ArborDao) {

  val projects: Flow<List<ProjectEntity>> = dao.getAllProjects()
  val tasks: Flow<List<TaskEntity>> = dao.getAllTasks()
  val messages: Flow<List<ChatMessageEntity>> = dao.getAllMessages()
  val activityLogs: Flow<List<ActivityLogEntity>> = dao.getActivityLogs()
  val notifications: Flow<List<NotificationEntity>> = dao.getNotifications()
  val users: Flow<List<UserEntity>> = dao.getAllUsers()
  val currentUser: Flow<UserEntity?> = dao.getCurrentUser()

  init {
    CoroutineScope(Dispatchers.IO).launch {
      seedInitialDataIfNeeded()
    }
  }

  suspend fun seedInitialDataIfNeeded() {
    val existing = dao.getAllProjects().firstOrNull()
    if (existing.isNullOrEmpty()) {
      populateDefaults()
    }
  }

  private suspend fun populateDefaults() {
    val defaultUsers = listOf(
      UserEntity(
        id = "user_1",
        name = "Sofia Navarro",
        email = "sofia.navarro@arbor.team",
        role = "Project Director & Architect",
        department = "Core Engineering",
        timezone = "UTC+1 (Madrid)",
        status = "En línea",
        avatarInitials = "SN",
        isCurrentUser = true
      ),
      UserEntity(
        id = "user_2",
        name = "Carlos Mendoza",
        email = "carlos.m@arbor.team",
        role = "Senior Mobile Engineer",
        department = "Android & Desktop",
        timezone = "UTC-5 (Bogotá)",
        status = "En línea",
        avatarInitials = "CM",
        isCurrentUser = false
      ),
      UserEntity(
        id = "user_3",
        name = "Elena Vance",
        email = "elena.vance@arbor.team",
        role = "Design Lead & UX",
        department = "Product Design",
        timezone = "UTC+0 (Londres)",
        status = "En reunión",
        avatarInitials = "EV",
        isCurrentUser = false
      ),
      UserEntity(
        id = "user_4",
        name = "Kenji Sato",
        email = "kenji.sato@arbor.team",
        role = "Cloud & Security Engineer",
        department = "Infrastructure",
        timezone = "UTC+9 (Tokio)",
        status = "Ausente",
        avatarInitials = "KS",
        isCurrentUser = false
      )
    )
    dao.insertUsers(defaultUsers)

    val defaultProjects = listOf(
      ProjectEntity(
        id = "proj_1",
        title = "Arbor Multiplataforma (Móvil & Escritorio)",
        description = "Arquitectura de sincronización distribuida, interfaz minimalista blanco y negro y colaboración en tiempo real.",
        category = "Core Platform",
        status = "Activo",
        progress = 78,
        memberCount = 6,
        colorHex = 0xFF234C34
      ),
      ProjectEntity(
        id = "proj_2",
        title = "Sistema de Diseño Orgánico 'Sylva'",
        description = "Componentes de alta accesibilidad con contraste limpio blanco/negro, tipografía sobria y acentos botánicos.",
        category = "Design System",
        status = "En Revisión",
        progress = 92,
        memberCount = 4,
        colorHex = 0xFF2C5E3B
      ),
      ProjectEntity(
        id = "proj_3",
        title = "Infraestructura & Nube Distribuida",
        description = "Despliegue de microservicios y WebSockets para latencia ultrabaja entre equipos de América, Europa y Asia.",
        category = "DevOps & Cloud",
        status = "Planificación",
        progress = 45,
        memberCount = 3,
        colorHex = 0xFF191C1A
      )
    )
    dao.insertProjects(defaultProjects)

    val defaultTasks = listOf(
      TaskEntity(
        id = "task_1",
        projectId = "proj_1",
        title = "Refactorizar sincronización reactiva en tiempo real",
        description = "Asegurar que los cambios de estado en kanban se propaguen inmediatamente a través del StateFlow compartido.",
        status = "En Progreso",
        priority = "Alta",
        assignedToName = "Carlos Mendoza",
        assignedToRole = "Senior Mobile Engineer",
        dueDate = "Hoy, 18:00",
        tag = "Core Sync",
        isVoiceCreated = false
      ),
      TaskEntity(
        id = "task_2",
        projectId = "proj_1",
        title = "Optimizar soporte para pantallas grandes y tabletas",
        description = "Implementar Navigation Rail adaptativo y vistas de panel dividido maestro-detalle para modo escritorio.",
        status = "En Revisión",
        priority = "Media",
        assignedToName = "Elena Vance",
        assignedToRole = "Design Lead",
        dueDate = "Mañana",
        tag = "UI/UX",
        isVoiceCreated = true,
        voiceTranscript = "Por favor verificar que el NavigationRail en modo horizontal no colapse los contenedores de las tareas.",
        voiceDurationSec = 14
      ),
      TaskEntity(
        id = "task_3",
        projectId = "proj_2",
        title = "Verificación de contraste WCAG AAA en blanco y negro",
        description = "Auditar que todos los elementos interactivos cumplan con el ratio de contraste 7:1 en temas claros.",
        status = "Completada",
        priority = "Baja",
        assignedToName = "Sofia Navarro",
        assignedToRole = "Project Director",
        dueDate = "Completada",
        tag = "A11y",
        isVoiceCreated = false
      ),
      TaskEntity(
        id = "task_4",
        projectId = "proj_1",
        title = "Módulo de registro por voz con cancelación de ruido",
        description = "Permitir a los miembros del equipo dictar tareas y notas de actividad sobre la marcha con un solo toque.",
        status = "En Progreso",
        priority = "Urgente",
        assignedToName = "Carlos Mendoza",
        assignedToRole = "Senior Mobile Engineer",
        dueDate = "16 Sep",
        tag = "Voice AI",
        isVoiceCreated = true,
        voiceTranscript = "Registro rápido: añadir soporte de guardado automático de audio al crear tareas rápidas por voz.",
        voiceDurationSec = 9
      ),
      TaskEntity(
        id = "task_5",
        projectId = "proj_3",
        title = "Auditoría de seguridad y matriz de permisos por rol",
        description = "Restringir la eliminación de proyectos y exportación de métricas a usuarios con permisos de Administrador.",
        status = "Pendiente",
        priority = "Media",
        assignedToName = "Kenji Sato",
        assignedToRole = "Security Engineer",
        dueDate = "20 Sep",
        tag = "Seguridad",
        isVoiceCreated = false
      )
    )
    dao.insertTasks(defaultTasks)

    val defaultMessages = listOf(
      ChatMessageEntity(
        id = "msg_1",
        projectId = "proj_1",
        senderName = "Carlos Mendoza",
        senderRole = "Senior Mobile Engineer",
        message = "¡Hola equipo! Acabo de enviar el PR con el soporte para pantallas grandes y el nuevo esquema blanco/negro natural.",
        timestamp = System.currentTimeMillis() - 3600000 * 3,
        isVoiceNote = false,
        isMine = false
      ),
      ChatMessageEntity(
        id = "msg_2",
        projectId = "proj_1",
        senderName = "Elena Vance",
        senderRole = "Design Lead",
        message = "Revisado. El fondo blanco puro con bordes sutiles y acentos botánicos se ve muy elegante y despejado.",
        timestamp = System.currentTimeMillis() - 3600000 * 2,
        isVoiceNote = false,
        isMine = false
      ),
      ChatMessageEntity(
        id = "msg_3",
        projectId = "proj_1",
        senderName = "Elena Vance",
        senderRole = "Design Lead",
        message = "Nota de voz sobre la paleta de iconos adaptativos:",
        timestamp = System.currentTimeMillis() - 3600000,
        isVoiceNote = true,
        voiceDurationSec = 18,
        isMine = false
      ),
      ChatMessageEntity(
        id = "msg_4",
        projectId = "proj_1",
        senderName = "Sofia Navarro",
        senderRole = "Project Director",
        message = "Excelente avance. Vamos a integrar el canal de actividades en tiempo real y la gestión de permisos para el lanzamiento.",
        timestamp = System.currentTimeMillis() - 1800000,
        isVoiceNote = false,
        isMine = true
      )
    )
    dao.insertMessages(defaultMessages)

    val defaultActivities = listOf(
      ActivityLogEntity(
        id = "act_1",
        projectId = "proj_1",
        actorName = "Carlos Mendoza",
        actionText = "actualizó el estado de la tarea a",
        targetItem = "En Progreso: Refactorizar sincronización reactiva",
        timestamp = System.currentTimeMillis() - 1000 * 60 * 25,
        category = "task"
      ),
      ActivityLogEntity(
        id = "act_2",
        projectId = "proj_1",
        actorName = "Elena Vance",
        actionText = "grabó una nota de voz de colaboración de 18 segundos",
        targetItem = "Discusión de Arquitectura Visual",
        timestamp = System.currentTimeMillis() - 1000 * 60 * 55,
        category = "voice"
      ),
      ActivityLogEntity(
        id = "act_3",
        projectId = "proj_2",
        actorName = "Sofia Navarro",
        actionText = "marcó como completado el hito",
        targetItem = "Verificación de contraste WCAG AAA",
        timestamp = System.currentTimeMillis() - 1000 * 60 * 120,
        category = "task"
      ),
      ActivityLogEntity(
        id = "act_4",
        projectId = "proj_3",
        actorName = "Kenji Sato",
        actionText = "configuró políticas de permisos de red en Tokio",
        targetItem = "Cluster Asia-Pacífico",
        timestamp = System.currentTimeMillis() - 1000 * 60 * 240,
        category = "project"
      )
    )
    dao.insertActivityLogs(defaultActivities)

    val defaultNotifications = listOf(
      NotificationEntity(
        id = "notif_1",
        title = "Nueva tarea asignada",
        body = "Elena Vance te asignó la revisión del soporte para pantallas de escritorio.",
        timestamp = System.currentTimeMillis() - 1000 * 60 * 15,
        isRead = false,
        type = "task"
      ),
      NotificationEntity(
        id = "notif_2",
        title = "Mención en Proyecto Arbor",
        body = "Carlos Mendoza: '@Sofia por favor revisa el pull request del flujo de login y registro.'",
        timestamp = System.currentTimeMillis() - 1000 * 60 * 45,
        isRead = false,
        type = "mention"
      ),
      NotificationEntity(
        id = "notif_3",
        title = "Sincronización de Equipo Remoto",
        body = "3 miembros del equipo están activos en Madrid, Bogotá y Tokio.",
        timestamp = System.currentTimeMillis() - 1000 * 60 * 180,
        isRead = true,
        type = "team"
      )
    )
    dao.insertNotifications(defaultNotifications)
  }

  suspend fun createTask(
    projectId: String,
    title: String,
    description: String,
    priority: String,
    dueDate: String,
    tag: String,
    assignedToName: String,
    isVoiceCreated: Boolean = false,
    voiceTranscript: String? = null,
    voiceDurationSec: Int = 0
  ) {
    val taskId = "task_" + UUID.randomUUID().toString().take(8)
    val task = TaskEntity(
      id = taskId,
      projectId = projectId,
      title = title,
      description = description,
      status = "Pendiente",
      priority = priority,
      assignedToName = assignedToName,
      assignedToRole = "Miembro de Equipo",
      dueDate = dueDate,
      tag = tag,
      isVoiceCreated = isVoiceCreated,
      voiceTranscript = voiceTranscript,
      voiceDurationSec = voiceDurationSec
    )
    dao.insertTask(task)

    // Activity log
    val log = ActivityLogEntity(
      id = "act_" + UUID.randomUUID().toString().take(8),
      projectId = projectId,
      actorName = "Sofia Navarro",
      actionText = if (isVoiceCreated) "creó una tarea mediante registro por voz:" else "creó una nueva tarea:",
      targetItem = title,
      category = if (isVoiceCreated) "voice" else "task"
    )
    dao.insertActivityLog(log)

    // Notification
    dao.insertNotification(
      NotificationEntity(
        id = "notif_" + UUID.randomUUID().toString().take(8),
        title = "Tarea creada en $tag",
        body = "Se registró la tarea '$title' asignada a $assignedToName",
        type = "task"
      )
    )
  }

  suspend fun updateTaskStatus(taskId: String, taskTitle: String, newStatus: String, projectId: String) {
    dao.updateTaskStatus(taskId, newStatus)

    val log = ActivityLogEntity(
      id = "act_" + UUID.randomUUID().toString().take(8),
      projectId = projectId,
      actorName = "Sofia Navarro",
      actionText = "cambió el estado de tarea a '$newStatus':",
      targetItem = taskTitle,
      category = "task"
    )
    dao.insertActivityLog(log)
  }

  suspend fun updateTaskDetails(task: TaskEntity) {
    dao.updateTask(task)

    val log = ActivityLogEntity(
      id = "act_" + UUID.randomUUID().toString().take(8),
      projectId = task.projectId,
      actorName = "Sofia Navarro",
      actionText = "actualizó los detalles de la tarea:",
      targetItem = task.title,
      category = "task"
    )
    dao.insertActivityLog(log)
  }

  suspend fun deleteTask(taskId: String) {
    dao.deleteTaskById(taskId)
  }

  suspend fun createProject(
    title: String,
    description: String,
    category: String,
    memberCount: Int = 4
  ) {
    val projId = "proj_" + UUID.randomUUID().toString().take(8)
    val project = ProjectEntity(
      id = projId,
      title = title,
      description = description,
      category = category,
      status = "Activo",
      progress = 0,
      memberCount = memberCount,
      colorHex = 0xFF234C34
    )
    dao.insertProject(project)

    dao.insertActivityLog(
      ActivityLogEntity(
        id = "act_" + UUID.randomUUID().toString().take(8),
        projectId = projId,
        actorName = "Sofia Navarro",
        actionText = "inició el nuevo proyecto:",
        targetItem = title,
        category = "project"
      )
    )
  }

  suspend fun sendChatMessage(
    projectId: String,
    message: String,
    isVoiceNote: Boolean = false,
    voiceDurationSec: Int = 0
  ) {
    val msgId = "msg_" + UUID.randomUUID().toString().take(8)
    val entity = ChatMessageEntity(
      id = msgId,
      projectId = projectId,
      senderName = "Sofia Navarro",
      senderRole = "Project Director",
      message = message,
      isVoiceNote = isVoiceNote,
      voiceDurationSec = voiceDurationSec,
      isMine = true
    )
    dao.insertMessage(entity)

    dao.insertActivityLog(
      ActivityLogEntity(
        id = "act_" + UUID.randomUUID().toString().take(8),
        projectId = projectId,
        actorName = "Sofia Navarro",
        actionText = if (isVoiceNote) "envió una nota de voz en la discusión" else "comentó en el canal de equipo",
        targetItem = if (isVoiceNote) "Audio ($voiceDurationSec s)" else message.take(40) + "...",
        category = "chat"
      )
    )
  }

  suspend fun markNotificationAsRead(id: String) {
    dao.markNotificationAsRead(id)
  }

  suspend fun markAllNotificationsAsRead() {
    dao.markAllNotificationsAsRead()
  }

  suspend fun switchActiveUser(userId: String) {
    dao.clearCurrentUser()
    dao.setCurrentUser(userId)
  }
}
