package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.ArborDatabase
import com.example.data.model.ActivityLogEntity
import com.example.data.model.ChatMessageEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.ProjectEntity
import com.example.data.model.TaskEntity
import com.example.data.model.UserEntity
import com.example.data.repository.ArborRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class NavigationTab(val label: String, val iconName: String) {
  PROJECTS("Proyectos", "folder"),
  TASKS("Tareas", "assignment"),
  COLLABORATION("Discusión", "chat"),
  TEAM_PERMISSIONS("Equipo & Permisos", "security"),
  NOTIFICATIONS("Alertas", "notifications")
}

class ArborViewModel(application: Application) : AndroidViewModel(application) {

  private val database = ArborDatabase.getDatabase(application)
  private val repository = ArborRepository(database.arborDao())

  // Navigation & Filters
  private val _selectedTab = MutableStateFlow(NavigationTab.PROJECTS)
  val selectedTab: StateFlow<NavigationTab> = _selectedTab.asStateFlow()

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _selectedProjectFilter = MutableStateFlow<String?>(null)
  val selectedProjectFilter: StateFlow<String?> = _selectedProjectFilter.asStateFlow()

  private val _selectedTaskStatusFilter = MutableStateFlow("Todos")
  val selectedTaskStatusFilter: StateFlow<String> = _selectedTaskStatusFilter.asStateFlow()

  // Voice recording state
  private val _isRecordingVoice = MutableStateFlow(false)
  val isRecordingVoice: StateFlow<Boolean> = _isRecordingVoice.asStateFlow()

  private val _voiceSeconds = MutableStateFlow(0)
  val voiceSeconds: StateFlow<Int> = _voiceSeconds.asStateFlow()

  private val _voiceTranscriptDraft = MutableStateFlow("")
  val voiceTranscriptDraft: StateFlow<String> = _voiceTranscriptDraft.asStateFlow()

  private var voiceTimerJob: Job? = null

  // Chat input
  private val _chatInputText = MutableStateFlow("")
  val chatInputText: StateFlow<String> = _chatInputText.asStateFlow()

  // Dialogs
  private val _showCreateTaskDialog = MutableStateFlow(false)
  val showCreateTaskDialog: StateFlow<Boolean> = _showCreateTaskDialog.asStateFlow()

  private val _showCreateProjectDialog = MutableStateFlow(false)
  val showCreateProjectDialog: StateFlow<Boolean> = _showCreateProjectDialog.asStateFlow()

  private val _showUserAuthDialog = MutableStateFlow(false)
  val showUserAuthDialog: StateFlow<Boolean> = _showUserAuthDialog.asStateFlow()

  private val _showVoiceModal = MutableStateFlow(false)
  val showVoiceModal: StateFlow<Boolean> = _showVoiceModal.asStateFlow()

  // System permissions state
  private val _hasAudioPermission = MutableStateFlow(false)
  val hasAudioPermission: StateFlow<Boolean> = _hasAudioPermission.asStateFlow()

  private val _hasNotificationPermission = MutableStateFlow(false)
  val hasNotificationPermission: StateFlow<Boolean> = _hasNotificationPermission.asStateFlow()

  // Data streams from repository
  val projects: StateFlow<List<ProjectEntity>> = repository.projects
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val allTasks: StateFlow<List<TaskEntity>> = repository.tasks
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val messages: StateFlow<List<ChatMessageEntity>> = repository.messages
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val activityLogs: StateFlow<List<ActivityLogEntity>> = repository.activityLogs
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val notifications: StateFlow<List<NotificationEntity>> = repository.notifications
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val users: StateFlow<List<UserEntity>> = repository.users
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val currentUser: StateFlow<UserEntity?> = repository.currentUser
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  // Filtered tasks based on search, project and status
  val filteredTasks: StateFlow<List<TaskEntity>> = combine(
    allTasks,
    searchQuery,
    selectedProjectFilter,
    selectedTaskStatusFilter
  ) { tasksList, query, projId, status ->
    tasksList.filter { task ->
      val matchesQuery = query.isBlank() ||
        task.title.contains(query, ignoreCase = true) ||
        task.description.contains(query, ignoreCase = true) ||
        task.assignedToName.contains(query, ignoreCase = true) ||
        task.tag.contains(query, ignoreCase = true)

      val matchesProject = projId == null || task.projectId == projId
      val matchesStatus = status == "Todos" || task.status == status

      matchesQuery && matchesProject && matchesStatus
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  init {
    startSimulatedRemoteCollaborationHeartbeat()
  }

  // Periodic heartbeat simulating remote teammates' live collaboration
  private fun startSimulatedRemoteCollaborationHeartbeat() {
    viewModelScope.launch {
      while (true) {
        delay(45000) // Every 45 seconds add an organic collaboration update if app is running
        val currentProjects = projects.value
        if (currentProjects.isNotEmpty()) {
          val randomProject = currentProjects.first()
          val sampleTeammates = listOf("Carlos Mendoza", "Elena Vance", "Kenji Sato")
          val randomActor = sampleTeammates.random()
          val sampleActions = listOf(
            "comprobó la cobertura de pruebas unitarias",
            "sincronizó cambios desde el repositorio de GitHub",
            "aprobó los criterios de aceptación del sprint",
            "dejó una reacción en la discusión de arquitectura"
          )
          database.arborDao().insertActivityLog(
            ActivityLogEntity(
              id = "act_hb_" + System.currentTimeMillis(),
              projectId = randomProject.id,
              actorName = randomActor,
              actionText = sampleActions.random(),
              targetItem = randomProject.title,
              category = "task"
            )
          )
        }
      }
    }
  }

  // Navigation
  fun selectTab(tab: NavigationTab) {
    _selectedTab.value = tab
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun selectProjectFilter(projectId: String?) {
    _selectedProjectFilter.value = projectId
  }

  fun setTaskStatusFilter(status: String) {
    _selectedTaskStatusFilter.value = status
  }

  // Voice recording & Speaking notes
  fun startVoiceRecording() {
    _isRecordingVoice.value = true
    _voiceSeconds.value = 0
    _voiceTranscriptDraft.value = ""

    voiceTimerJob?.cancel()
    voiceTimerJob = viewModelScope.launch {
      val samplePhrases = listOf(
        "Verificar integración en tiempo real",
        "Alinear iconos con la estética de naturaleza",
        "Revisar permisos de despliegue para equipo remoto",
        "Actualizar documentación de repositorio en GitHub"
      )
      while (_isRecordingVoice.value) {
        delay(1000)
        _voiceSeconds.value += 1
        if (_voiceSeconds.value == 3) {
          _voiceTranscriptDraft.value = samplePhrases.random()
        }
      }
    }
  }

  fun stopVoiceRecording() {
    _isRecordingVoice.value = false
    voiceTimerJob?.cancel()
    if (_voiceTranscriptDraft.value.isBlank()) {
      _voiceTranscriptDraft.value = "Nota de voz grabada (${_voiceSeconds.value} seg): Revisión de sprint remoto"
    }
  }

  fun cancelVoiceRecording() {
    _isRecordingVoice.value = false
    voiceTimerJob?.cancel()
    _voiceSeconds.value = 0
    _voiceTranscriptDraft.value = ""
  }

  fun createVoiceTask(projectId: String? = null) {
    viewModelScope.launch {
      val targetProject = projectId ?: projects.value.firstOrNull()?.id ?: "proj_1"
      val transcript = if (_voiceTranscriptDraft.value.isNotBlank()) {
        _voiceTranscriptDraft.value
      } else {
        "Tarea de voz: Revisión de colaboración remota"
      }
      val duration = if (_voiceSeconds.value > 0) _voiceSeconds.value else 8

      repository.createTask(
        projectId = targetProject,
        title = transcript,
        description = "Creada mediante entrada de voz / registro hablando en Arbor Workspace.",
        priority = "Alta",
        dueDate = "Hoy",
        tag = "Voz / Sync",
        assignedToName = currentUser.value?.name ?: "Sofia Navarro",
        isVoiceCreated = true,
        voiceTranscript = transcript,
        voiceDurationSec = duration
      )

      _voiceSeconds.value = 0
      _voiceTranscriptDraft.value = ""
      _showVoiceModal.value = false
      _selectedTab.value = NavigationTab.TASKS
    }
  }

  // Tasks actions
  fun updateTaskStatus(taskId: String, taskTitle: String, newStatus: String, projectId: String) {
    viewModelScope.launch {
      repository.updateTaskStatus(taskId, taskTitle, newStatus, projectId)
    }
  }

  fun deleteTask(taskId: String) {
    viewModelScope.launch {
      repository.deleteTask(taskId)
    }
  }

  fun createManualTask(
    projectId: String,
    title: String,
    description: String,
    priority: String,
    dueDate: String,
    tag: String,
    assignedToName: String
  ) {
    viewModelScope.launch {
      repository.createTask(
        projectId = projectId,
        title = title,
        description = description,
        priority = priority,
        dueDate = dueDate,
        tag = tag,
        assignedToName = assignedToName,
        isVoiceCreated = false
      )
      _showCreateTaskDialog.value = false
    }
  }

  // Projects actions
  fun createProject(title: String, description: String, category: String) {
    viewModelScope.launch {
      repository.createProject(title, description, category)
      _showCreateProjectDialog.value = false
    }
  }

  // Chat actions
  fun setChatInputText(text: String) {
    _chatInputText.value = text
  }

  fun sendChatMessage(projectId: String? = null) {
    val text = _chatInputText.value.trim()
    if (text.isEmpty()) return

    val targetProject = projectId ?: selectedProjectFilter.value ?: projects.value.firstOrNull()?.id ?: "proj_1"
    viewModelScope.launch {
      repository.sendChatMessage(
        projectId = targetProject,
        message = text,
        isVoiceNote = false
      )
      _chatInputText.value = ""

      // Simulate a teammate reply after a short natural delay
      delay(2000)
      val autoResponses = listOf(
        "Recibido @Sofia. Ya lo tengo en mi panel de tareas para hoy.",
        "Sincronizado. Los cambios se ven limpios en la vista de escritorio.",
        "Anotado. Estoy revisando la documentación en GitHub."
      )
      repository.sendChatMessage(
        projectId = targetProject,
        message = autoResponses.random(),
        isVoiceNote = false
      )
    }
  }

  fun sendVoiceMessage(durationSec: Int = 12, projectId: String? = null) {
    val targetProject = projectId ?: selectedProjectFilter.value ?: projects.value.firstOrNull()?.id ?: "proj_1"
    viewModelScope.launch {
      repository.sendChatMessage(
        projectId = targetProject,
        message = "Nota de voz de colaboración remota",
        isVoiceNote = true,
        voiceDurationSec = durationSec
      )
    }
  }

  // Notifications
  fun markNotificationAsRead(id: String) {
    viewModelScope.launch {
      repository.markNotificationAsRead(id)
    }
  }

  fun markAllNotificationsAsRead() {
    viewModelScope.launch {
      repository.markAllNotificationsAsRead()
    }
  }

  // Switch user
  fun switchUser(userId: String) {
    viewModelScope.launch {
      repository.switchActiveUser(userId)
      _showUserAuthDialog.value = false
    }
  }

  // Dialog visibility setters
  fun setShowCreateTaskDialog(show: Boolean) {
    _showCreateTaskDialog.value = show
  }

  fun setShowCreateProjectDialog(show: Boolean) {
    _showCreateProjectDialog.value = show
  }

  fun setShowUserAuthDialog(show: Boolean) {
    _showUserAuthDialog.value = show
  }

  fun setShowVoiceModal(show: Boolean) {
    _showVoiceModal.value = show
  }

  fun setAudioPermissionGranted(granted: Boolean) {
    _hasAudioPermission.value = granted
  }

  fun setNotificationPermissionGranted(granted: Boolean) {
    _hasNotificationPermission.value = granted
  }
}
