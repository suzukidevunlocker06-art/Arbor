package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProjectEntity
import com.example.data.model.TaskEntity
import com.example.ui.components.ArborSearchBar
import com.example.ui.components.PriorityBadge
import com.example.ui.components.StatusBadge
import com.example.ui.components.VoiceRecordingBanner
import com.example.ui.components.VoiceWaveformPlayer
import com.example.ui.theme.ArborAlert
import com.example.ui.theme.ArborBlack
import com.example.ui.theme.ArborBorder
import com.example.ui.theme.ArborForest
import com.example.ui.theme.ArborSage
import com.example.ui.theme.ArborSuccess
import com.example.ui.theme.ArborTextPrimary
import com.example.ui.theme.ArborTextSecondary
import com.example.ui.theme.ArborTextTertiary
import com.example.ui.theme.ArborWhite
import com.example.ui.theme.ArborWhiteSubtle

@Composable
fun TasksScreen(
  tasks: List<TaskEntity>,
  projects: List<ProjectEntity>,
  selectedProjectFilter: String?,
  onSelectProjectFilter: (String?) -> Unit,
  selectedStatusFilter: String,
  onSelectStatusFilter: (String) -> Unit,
  searchQuery: String,
  onSearchQueryChange: (String) -> Unit,
  isRecordingVoice: Boolean,
  recordingSeconds: Int,
  onStartVoiceRecord: () -> Unit,
  onStopVoiceRecord: () -> Unit,
  onSaveVoiceTask: (String) -> Unit,
  onUpdateTaskStatus: (String, String, String, String) -> Unit,
  onDeleteTask: (String) -> Unit,
  onOpenCreateTask: () -> Unit,
  showCreateDialog: Boolean,
  onDismissCreateDialog: () -> Unit,
  onCreateManualTaskConfirm: (String, String, String, String, String, String, String) -> Unit,
  modifier: Modifier = Modifier
) {
  val statusOptions = listOf("Todos", "Pendiente", "En Progreso", "En Revisión", "Completada")

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(ArborWhite)
      .padding(horizontal = 20.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp)
  ) {
    // Voice recording banner ("registro hablando")
    item {
      VoiceRecordingBanner(
        isRecording = isRecordingVoice,
        recordingSeconds = recordingSeconds,
        onStartRecord = onStartVoiceRecord,
        onStopRecord = onStopVoiceRecord,
        onSaveAsTask = onSaveVoiceTask
      )
    }

    // Search bar
    item {
      ArborSearchBar(
        query = searchQuery,
        onQueryChange = onSearchQueryChange,
        placeholder = "Buscar tarea por título, responsable o etiqueta..."
      )
    }

    // Project selector dropdown/chips
    item {
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "Filtrar por Proyecto",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = ArborTextTertiary,
            letterSpacing = 0.5.sp
          )
          if (selectedProjectFilter != null) {
            Text(
              text = "Limpiar filtro",
              fontSize = 11.sp,
              color = ArborForest,
              fontWeight = FontWeight.SemiBold,
              modifier = Modifier.clickable { onSelectProjectFilter(null) }
            )
          }
        }

        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          item {
            val isAll = selectedProjectFilter == null
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(if (isAll) ArborBlack else ArborWhiteSubtle)
                .border(1.dp, if (isAll) ArborBlack else ArborBorder, RoundedCornerShape(12.dp))
                .clickable { onSelectProjectFilter(null) }
                .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
              Text(
                text = "Todos los Proyectos",
                fontSize = 11.sp,
                fontWeight = if (isAll) FontWeight.Bold else FontWeight.Medium,
                color = if (isAll) ArborWhite else ArborTextSecondary
              )
            }
          }

          items(projects) { project ->
            val isSelected = selectedProjectFilter == project.id
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(if (isSelected) ArborBlack else ArborWhiteSubtle)
                .border(1.dp, if (isSelected) ArborBlack else ArborBorder, RoundedCornerShape(12.dp))
                .clickable { onSelectProjectFilter(project.id) }
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .testTag("project_filter_${project.id}")
            ) {
              Text(
                text = project.title.take(22) + if (project.title.length > 22) "..." else "",
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) ArborWhite else ArborTextSecondary
              )
            }
          }
        }
      }
    }

    // Status filter chips
    item {
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(statusOptions) { status ->
          val isSelected = status == selectedStatusFilter
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .background(if (isSelected) ArborForest else ArborWhiteSubtle)
              .border(1.dp, if (isSelected) ArborForest else ArborBorder, RoundedCornerShape(20.dp))
              .clickable { onSelectStatusFilter(status) }
              .padding(horizontal = 14.dp, vertical = 6.dp)
              .testTag("status_filter_$status")
          ) {
            Text(
              text = status,
              fontSize = 12.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isSelected) ArborWhite else ArborTextSecondary
            )
          }
        }
      }
    }

    // Action row: count and Add Task button
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = "${tasks.size} tareas en curso",
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold,
          color = ArborTextSecondary
        )

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(ArborBlack)
            .clickable { onOpenCreateTask() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .testTag("create_task_button")
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Add,
              contentDescription = "Nueva Tarea",
              tint = ArborWhite,
              modifier = Modifier.size(16.dp)
            )
            Text(
              text = "Nueva Tarea",
              color = ArborWhite,
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }
    }

    // Task items
    if (tasks.isEmpty()) {
      item {
        EmptyTasksView(onReset = {
          onSelectProjectFilter(null)
          onSelectStatusFilter("Todos")
          onSearchQueryChange("")
        })
      }
    } else {
      items(tasks, key = { it.id }) { task ->
        TaskItemCard(
          task = task,
          onAdvanceStatus = {
            val nextStatus = when (task.status) {
              "Pendiente" -> "En Progreso"
              "En Progreso" -> "En Revisión"
              "En Revisión" -> "Completada"
              else -> "Pendiente"
            }
            onUpdateTaskStatus(task.id, task.title, nextStatus, task.projectId)
          },
          onDelete = { onDeleteTask(task.id) }
        )
      }
    }
  }

  // Create Task Dialog
  if (showCreateDialog) {
    CreateTaskDialog(
      projects = projects,
      onDismiss = onDismissCreateDialog,
      onConfirm = onCreateManualTaskConfirm
    )
  }
}

@Composable
fun TaskItemCard(
  task: TaskEntity,
  onAdvanceStatus: () -> Unit,
  onDelete: () -> Unit
) {
  val isDone = task.status == "Completada"

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .border(1.dp, if (isDone) ArborBorder else ArborBorder, RoundedCornerShape(16.dp))
      .testTag("task_item_${task.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isDone) ArborWhiteSubtle else ArborWhite
    )
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(ArborSage)
              .padding(horizontal = 8.dp, vertical = 2.dp)
          ) {
            Text(
              text = task.tag,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = ArborForest
            )
          }

          PriorityBadge(priority = task.priority)

          if (task.isVoiceCreated) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(ArborBlack)
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Mic,
                  contentDescription = "Nota de voz",
                  tint = ArborWhite,
                  modifier = Modifier.size(11.dp)
                )
                Text(
                  text = "VOZ",
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold,
                  color = ArborWhite
                )
              }
            }
          }
        }

        IconButton(
          onClick = onDelete,
          modifier = Modifier.size(28.dp)
        ) {
          Icon(
            imageVector = Icons.Default.DeleteOutline,
            contentDescription = "Eliminar tarea",
            tint = ArborTextTertiary,
            modifier = Modifier.size(18.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = task.title,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = ArborBlack
      )

      if (task.description.isNotBlank()) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = task.description,
          style = MaterialTheme.typography.bodySmall,
          color = ArborTextSecondary,
          fontSize = 12.sp
        )
      }

      // Voice Waveform player if recorded with voice
      if (task.isVoiceCreated && task.voiceDurationSec > 0) {
        Spacer(modifier = Modifier.height(10.dp))
        VoiceWaveformPlayer(
          durationSec = task.voiceDurationSec,
          transcript = task.voiceTranscript
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Box(
            modifier = Modifier
              .size(26.dp)
              .clip(CircleShape)
              .background(ArborBlack),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = task.assignedToName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
              color = ArborWhite,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold
            )
          }

          Column {
            Text(
              text = task.assignedToName,
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              color = ArborBlack
            )
            Text(
              text = "Entrega: ${task.dueDate}",
              fontSize = 10.sp,
              color = ArborTextTertiary
            )
          }
        }

        // Advance Status button
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isDone) ArborSage else ArborBlack)
            .clickable { onAdvanceStatus() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .testTag("task_status_toggle_${task.id}")
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            if (isDone) {
              Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = ArborSuccess,
                modifier = Modifier.size(14.dp)
              )
            }
            Text(
              text = task.status,
              color = if (isDone) ArborSuccess else ArborWhite,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}

@Composable
fun EmptyTasksView(onReset: () -> Unit) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 32.dp)
      .clip(RoundedCornerShape(16.dp))
      .background(ArborWhiteSubtle)
      .border(1.dp, ArborBorder, RoundedCornerShape(16.dp))
      .padding(24.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Icon(
        imageVector = Icons.Default.AssignmentTurnedIn,
        contentDescription = null,
        tint = ArborTextTertiary,
        modifier = Modifier.size(40.dp)
      )
      Text(
        text = "No hay tareas con estos filtros",
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = ArborBlack
      )
      Text(
        text = "Puedes dictar una nota de voz o crear una tarea manual.",
        fontSize = 12.sp,
        color = ArborTextSecondary
      )
      Spacer(modifier = Modifier.height(8.dp))
      Button(
        onClick = onReset,
        colors = ButtonDefaults.buttonColors(containerColor = ArborBlack)
      ) {
        Text("Ver Todas las Tareas", color = ArborWhite, fontSize = 12.sp)
      }
    }
  }
}

@Composable
fun CreateTaskDialog(
  projects: List<ProjectEntity>,
  onDismiss: () -> Unit,
  onConfirm: (String, String, String, String, String, String, String) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  var selectedProjectId by remember { mutableStateOf(projects.firstOrNull()?.id ?: "proj_1") }
  var priority by remember { mutableStateOf("Media") }
  var dueDate by remember { mutableStateOf("Esta semana") }
  var tag by remember { mutableStateOf("Sprint") }
  var assignedToName by remember { mutableStateOf("Carlos Mendoza") }

  val priorities = listOf("Baja", "Media", "Alta", "Urgente")

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text("Nueva Tarea de Proyecto", fontWeight = FontWeight.Bold, color = ArborBlack)
    },
    text = {
      Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Título de la Tarea") },
          placeholder = { Text("Ej: Diseñar componentes de navegación") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = { Text("Descripción o Requerimientos") },
          modifier = Modifier.fillMaxWidth(),
          maxLines = 2
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = tag,
            onValueChange = { tag = it },
            label = { Text("Etiqueta") },
            modifier = Modifier.weight(1f),
            singleLine = true
          )
          OutlinedTextField(
            value = dueDate,
            onValueChange = { dueDate = it },
            label = { Text("Entrega") },
            modifier = Modifier.weight(1f),
            singleLine = true
          )
        }

        OutlinedTextField(
          value = assignedToName,
          onValueChange = { assignedToName = it },
          label = { Text("Asignar a") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        Text("Prioridad", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ArborTextTertiary)
        Row(
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          priorities.forEach { p ->
            val isSelected = p == priority
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) ArborBlack else ArborWhiteSubtle)
                .border(1.dp, if (isSelected) ArborBlack else ArborBorder, RoundedCornerShape(8.dp))
                .clickable { priority = p }
                .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
              Text(
                text = p,
                fontSize = 11.sp,
                color = if (isSelected) ArborWhite else ArborTextSecondary,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
              )
            }
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (title.isNotBlank()) {
            onConfirm(selectedProjectId, title, description, priority, dueDate, tag, assignedToName)
          }
        },
        enabled = title.isNotBlank(),
        colors = ButtonDefaults.buttonColors(containerColor = ArborBlack)
      ) {
        Text("Guardar Tarea", color = ArborWhite)
      }
    },
    dismissButton = {
      OutlinedButton(onClick = onDismiss) {
        Text("Cancelar", color = ArborBlack)
      }
    },
    containerColor = ArborWhite
  )
}
