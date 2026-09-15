package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProjectEntity
import com.example.ui.components.ArborSearchBar
import com.example.ui.components.StatusBadge
import com.example.ui.theme.ArborBlack
import com.example.ui.theme.ArborBorder
import com.example.ui.theme.ArborForest
import com.example.ui.theme.ArborForestLight
import com.example.ui.theme.ArborLeafGreen
import com.example.ui.theme.ArborSage
import com.example.ui.theme.ArborSageDark
import com.example.ui.theme.ArborSuccess
import com.example.ui.theme.ArborTextPrimary
import com.example.ui.theme.ArborTextSecondary
import com.example.ui.theme.ArborTextTertiary
import com.example.ui.theme.ArborWhite
import com.example.ui.theme.ArborWhiteSubtle
import com.example.ui.theme.ArborWhiteSurface

@Composable
fun ProjectsScreen(
  projects: List<ProjectEntity>,
  searchQuery: String,
  onSearchQueryChange: (String) -> Unit,
  onSelectProjectForTasks: (String) -> Unit,
  onOpenCreateProject: () -> Unit,
  showCreateDialog: Boolean,
  onDismissCreateDialog: () -> Unit,
  onCreateProjectConfirm: (String, String, String) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedCategory by remember { mutableStateOf("Todos") }
  val categories = listOf("Todos", "Core Platform", "Design System", "DevOps & Cloud")

  val filteredProjects = projects.filter { proj ->
    val matchesCategory = selectedCategory == "Todos" || proj.category.contains(selectedCategory, ignoreCase = true)
    val matchesSearch = searchQuery.isBlank() ||
      proj.title.contains(searchQuery, ignoreCase = true) ||
      proj.description.contains(searchQuery, ignoreCase = true)
    matchesCategory && matchesSearch
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(ArborWhite)
      .padding(horizontal = 20.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp)
  ) {
    // Realtime banner & workspace status
    item {
      RealtimeWorkspaceHeader(projectCount = projects.size)
    }

    // Search bar
    item {
      ArborSearchBar(
        query = searchQuery,
        onQueryChange = onSearchQueryChange,
        placeholder = "Buscar proyectos o categorías..."
      )
    }

    // Category pills
    item {
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 4.dp)
      ) {
        items(categories) { category ->
          val isSelected = category == selectedCategory
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(20.dp))
              .background(if (isSelected) ArborBlack else ArborWhiteSubtle)
              .border(1.dp, if (isSelected) ArborBlack else ArborBorder, RoundedCornerShape(20.dp))
              .clickable { selectedCategory = category }
              .padding(horizontal = 14.dp, vertical = 8.dp)
              .testTag("category_chip_$category")
          ) {
            Text(
              text = category,
              fontSize = 12.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              color = if (isSelected) ArborWhite else ArborTextSecondary
            )
          }
        }
      }
    }

    // Section header & Add Project button
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text(
            text = "Proyectos Activos",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = ArborBlack
          )
          Text(
            text = "${filteredProjects.size} proyectos en sincronización remota",
            fontSize = 12.sp,
            color = ArborTextSecondary
          )
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(ArborBlack)
            .clickable { onOpenCreateProject() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .testTag("new_project_button")
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Add,
              contentDescription = "Crear Proyecto",
              tint = ArborWhite,
              modifier = Modifier.size(16.dp)
            )
            Text(
              text = "Nuevo",
              color = ArborWhite,
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }
    }

    // Project cards list
    if (filteredProjects.isEmpty()) {
      item {
        EmptyProjectsView(onResetFilters = {
          selectedCategory = "Todos"
          onSearchQueryChange("")
        })
      }
    } else {
      items(filteredProjects, key = { it.id }) { project ->
        ProjectCard(
          project = project,
          onOpenTasks = { onSelectProjectForTasks(project.id) }
        )
      }
    }
  }

  // Create Project Dialog
  if (showCreateDialog) {
    CreateProjectDialog(
      onDismiss = onDismissCreateDialog,
      onConfirm = onCreateProjectConfirm
    )
  }
}

@Composable
fun RealtimeWorkspaceHeader(projectCount: Int) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .border(1.dp, ArborBorder, RoundedCornerShape(18.dp)),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = ArborWhiteSurface)
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
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
              .size(8.dp)
              .clip(CircleShape)
              .background(ArborSuccess)
          )
          Text(
            text = "Tiempo Real Conectado",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = ArborForest
          )
        }

        Text(
          text = "Nodo: Global Distributed",
          fontSize = 10.sp,
          color = ArborTextTertiary
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = "Gestión de Proyectos & Equipos Remotos",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Black,
        letterSpacing = (-0.5).sp,
        color = ArborBlack
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "Colaboración avanzada entre zonas horarias con arquitectura minimalista de alto rendimiento.",
        style = MaterialTheme.typography.bodySmall,
        color = ArborTextSecondary,
        fontSize = 12.sp
      )

      Spacer(modifier = Modifier.height(14.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        WorkspaceMetric(
          title = "Proyectos",
          value = "$projectCount",
          subtitle = "Sincronizados",
          modifier = Modifier.weight(1f)
        )
        WorkspaceMetric(
          title = "Equipo Remoto",
          value = "4 Activos",
          subtitle = "Madrid/Bogotá/Tokio",
          modifier = Modifier.weight(1.3f)
        )
        WorkspaceMetric(
          title = "Sprint",
          value = "v2.4",
          subtitle = "En progreso",
          modifier = Modifier.weight(1f)
        )
      }
    }
  }
}

@Composable
fun WorkspaceMetric(
  title: String,
  value: String,
  subtitle: String,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .background(ArborWhiteSubtle)
      .border(1.dp, ArborBorder, RoundedCornerShape(12.dp))
      .padding(10.dp)
  ) {
    Column {
      Text(
        text = title,
        fontSize = 10.sp,
        color = ArborTextTertiary,
        fontWeight = FontWeight.SemiBold
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = value,
        fontSize = 13.sp,
        fontWeight = FontWeight.Black,
        color = ArborBlack
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = subtitle,
        fontSize = 9.sp,
        color = ArborTextSecondary
      )
    }
  }
}

@Composable
fun ProjectCard(
  project: ProjectEntity,
  onOpenTasks: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .border(1.dp, ArborBorder, RoundedCornerShape(16.dp))
      .clickable { onOpenTasks() }
      .testTag("project_card_${project.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = ArborWhite)
  ) {
    Column(modifier = Modifier.padding(18.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(ArborSage)
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          Text(
            text = project.category,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = ArborForest
          )
        }

        StatusBadge(status = project.status)
      }

      Spacer(modifier = Modifier.height(10.dp))

      Text(
        text = project.title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = ArborBlack
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = project.description,
        style = MaterialTheme.typography.bodySmall,
        color = ArborTextSecondary,
        fontSize = 12.sp,
        maxLines = 2
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Progress bar
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Progreso del Proyecto",
            fontSize = 11.sp,
            color = ArborTextTertiary
          )
          Text(
            text = "${project.progress}%",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = ArborBlack
          )
        }

        Spacer(modifier = Modifier.height(6.dp))

        LinearProgressIndicator(
          progress = { project.progress / 100f },
          modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(CircleShape),
          color = ArborBlack,
          trackColor = ArborBorder
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
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Groups,
            contentDescription = null,
            tint = ArborTextSecondary,
            modifier = Modifier.size(16.dp)
          )
          Text(
            text = "${project.memberCount} miembros remotos",
            fontSize = 11.sp,
            color = ArborTextSecondary
          )
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Text(
            text = "Ver Tareas",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = ArborBlack
          )
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = ArborBlack,
            modifier = Modifier.size(14.dp)
          )
        }
      }
    }
  }
}

@Composable
fun EmptyProjectsView(onResetFilters: () -> Unit) {
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
        imageVector = Icons.Default.Folder,
        contentDescription = null,
        tint = ArborTextTertiary,
        modifier = Modifier.size(40.dp)
      )
      Text(
        text = "No se encontraron proyectos",
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = ArborBlack
      )
      Text(
        text = "Prueba modificando los términos de búsqueda o filtros.",
        fontSize = 12.sp,
        color = ArborTextSecondary
      )
      Spacer(modifier = Modifier.height(8.dp))
      Button(
        onClick = onResetFilters,
        colors = ButtonDefaults.buttonColors(containerColor = ArborBlack)
      ) {
        Text("Restablecer Filtros", color = ArborWhite, fontSize = 12.sp)
      }
    }
  }
}

@Composable
fun CreateProjectDialog(
  onDismiss: () -> Unit,
  onConfirm: (String, String, String) -> Unit
) {
  var title by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  var category by remember { mutableStateOf("Core Platform") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "Crear Nuevo Proyecto",
        fontWeight = FontWeight.Bold,
        color = ArborBlack
      )
    },
    text = {
      Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(
          text = "Define los objetivos y la categoría para coordinar al equipo remoto.",
          fontSize = 12.sp,
          color = ArborTextSecondary
        )

        OutlinedTextField(
          value = title,
          onValueChange = { title = it },
          label = { Text("Nombre del Proyecto") },
          placeholder = { Text("Ej: Pasarela de Pagos Distribuida") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = { Text("Descripción") },
          placeholder = { Text("Objetivos clave y entregables...") },
          modifier = Modifier.fillMaxWidth(),
          maxLines = 3
        )

        OutlinedTextField(
          value = category,
          onValueChange = { category = it },
          label = { Text("Categoría o Área") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (title.isNotBlank()) {
            onConfirm(title, description, category)
          }
        },
        enabled = title.isNotBlank(),
        colors = ButtonDefaults.buttonColors(containerColor = ArborBlack)
      ) {
        Text("Crear Proyecto", color = ArborWhite)
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
