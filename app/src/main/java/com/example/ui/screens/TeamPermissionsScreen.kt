package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.data.model.UserEntity
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
fun TeamPermissionsScreen(
  users: List<UserEntity>,
  currentUser: UserEntity?,
  onSwitchUser: (String) -> Unit,
  onAudioPermissionGranted: (Boolean) -> Unit,
  onNotificationPermissionGranted: (Boolean) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  var hasMicPermission by remember {
    mutableStateOf(
      ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    )
  }

  var hasNotifPermission by remember {
    mutableStateOf(
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
      } else {
        true
      }
    )
  }

  val micPermissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
  ) { isGranted ->
    hasMicPermission = isGranted
    onAudioPermissionGranted(isGranted)
  }

  val notifPermissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
  ) { isGranted ->
    hasNotifPermission = isGranted
    onNotificationPermissionGranted(isGranted)
  }

  var showUserSwitchDialog by remember { mutableStateOf(false) }

  // Configurable policy toggles
  var canRecordVoiceNotes by remember { mutableStateOf(true) }
  var canCreateProjects by remember { mutableStateOf(true) }
  var autoSyncCloud by remember { mutableStateOf(true) }
  var endToEndEncrypted by remember { mutableStateOf(true) }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(ArborWhite)
      .padding(horizontal = 20.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp)
  ) {
    // Active Account Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .border(1.dp, ArborBorder, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = ArborWhite)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "SESIÓN ACTIVA",
              fontSize = 10.sp,
              fontWeight = FontWeight.Black,
              letterSpacing = 1.sp,
              color = ArborTextTertiary
            )

            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(ArborBlack)
                .clickable { showUserSwitchDialog = true }
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .testTag("switch_user_button")
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.SwapHoriz,
                  contentDescription = null,
                  tint = ArborWhite,
                  modifier = Modifier.size(14.dp)
                )
                Text(
                  text = "Cambiar Usuario",
                  color = ArborWhite,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Box(
              modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(ArborBlack),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = currentUser?.avatarInitials ?: "SN",
                color = ArborWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
              )
            }

            Column {
              Text(
                text = currentUser?.name ?: "Sofia Navarro",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = ArborBlack
              )
              Text(
                text = currentUser?.role ?: "Project Director & Architect",
                fontSize = 12.sp,
                color = ArborForest,
                fontWeight = FontWeight.SemiBold
              )
              Text(
                text = "${currentUser?.email ?: "sofia.navarro@arbor.team"} • ${currentUser?.timezone ?: "UTC+1"}",
                fontSize = 11.sp,
                color = ArborTextSecondary
              )
            }
          }
        }
      }
    }

    // Android System Permissions Section (Real Permissions)
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .border(1.dp, ArborBorder, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ArborWhiteSubtle)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Shield,
              contentDescription = null,
              tint = ArborBlack,
              modifier = Modifier.size(18.dp)
            )
            Text(
              text = "Permisos del Dispositivo (Android)",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = ArborBlack
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Mic permission
          PermissionRowItem(
            icon = Icons.Default.Mic,
            title = "Grabación de Voz / Hablando",
            description = "Requerido para dictar tareas y enviar notas de voz al equipo",
            isGranted = hasMicPermission,
            onRequest = { micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO) }
          )

          Spacer(modifier = Modifier.height(10.dp))

          // Notifications permission
          PermissionRowItem(
            icon = Icons.Default.Notifications,
            title = "Notificaciones en Tiempo Real",
            description = "Alertas de menciones, cambios de estado y entregas de sprint",
            isGranted = hasNotifPermission,
            onRequest = {
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notifPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
              }
            }
          )
        }
      }
    }

    // Workspace Roles & Permissions Matrix
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .border(1.dp, ArborBorder, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ArborWhite)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Matriz de Políticas de Colaboración",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = ArborBlack
          )
          Text(
            text = "Control de acceso granular para equipos remotos",
            fontSize = 11.sp,
            color = ArborTextSecondary
          )

          Spacer(modifier = Modifier.height(14.dp))

          PolicySwitchRow(
            title = "Notas de voz y registro hablado",
            subtitle = "Permitir entrada por voz a todos los integrantes",
            checked = canRecordVoiceNotes,
            onCheckedChange = { canRecordVoiceNotes = it }
          )

          PolicySwitchRow(
            title = "Creación libre de proyectos",
            subtitle = "Solo Administradores y Leads pueden iniciar proyectos",
            checked = canCreateProjects,
            onCheckedChange = { canCreateProjects = it }
          )

          PolicySwitchRow(
            title = "Sincronización multi-zona horaria",
            subtitle = "Ajustar automáticamente fechas de entrega por huso horario",
            checked = autoSyncCloud,
            onCheckedChange = { autoSyncCloud = it }
          )

          PolicySwitchRow(
            title = "Cifrado de extremo a extremo",
            subtitle = "Protección de datos y notas en reposo y en tránsito",
            checked = endToEndEncrypted,
            onCheckedChange = { endToEndEncrypted = it }
          )
        }
      }
    }

    // Remote Team Directory
    item {
      Text(
        text = "Directorio del Equipo Remoto",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = ArborBlack
      )
    }

    items(users) { user ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .border(1.dp, ArborBorder, RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = ArborWhite)
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Box(
            modifier = Modifier
              .size(38.dp)
              .clip(CircleShape)
              .background(ArborBlack),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = user.avatarInitials,
              color = ArborWhite,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp
            )
          }

          Column(modifier = Modifier.weight(1f)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = user.name,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = ArborBlack
              )
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(if (user.status == "En línea") ArborSage else ArborWhiteSubtle)
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Text(
                  text = user.status,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (user.status == "En línea") ArborForest else ArborTextSecondary
                )
              }
            }

            Text(
              text = user.role,
              fontSize = 11.sp,
              color = ArborForest,
              fontWeight = FontWeight.Medium
            )

            Text(
              text = "${user.department} • ${user.timezone}",
              fontSize = 10.sp,
              color = ArborTextTertiary
            )
          }
        }
      }
    }
  }

  // Switch User Dialog
  if (showUserSwitchDialog) {
    AlertDialog(
      onDismissRequest = { showUserSwitchDialog = false },
      title = {
        Text("Cambiar Perfil o Iniciar Sesión", fontWeight = FontWeight.Bold, color = ArborBlack)
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text(
            text = "Selecciona el miembro del equipo remoto con el que deseas interactuar:",
            fontSize = 12.sp,
            color = ArborTextSecondary
          )
          users.forEach { user ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(if (user.id == currentUser?.id) ArborSage else ArborWhiteSubtle)
                .clickable {
                  onSwitchUser(user.id)
                  showUserSwitchDialog = false
                }
                .padding(10.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(32.dp)
                  .clip(CircleShape)
                  .background(ArborBlack),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = user.avatarInitials,
                  color = ArborWhite,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )
              }
              Column {
                Text(
                  text = user.name,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = ArborBlack
                )
                Text(
                  text = user.role,
                  fontSize = 10.sp,
                  color = ArborTextSecondary
                )
              }
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = { showUserSwitchDialog = false },
          colors = ButtonDefaults.buttonColors(containerColor = ArborBlack)
        ) {
          Text("Cerrar", color = ArborWhite)
        }
      },
      containerColor = ArborWhite
    )
  }
}

@Composable
fun PermissionRowItem(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  description: String,
  isGranted: Boolean,
  onRequest: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(10.dp))
      .background(ArborWhite)
      .border(1.dp, ArborBorder, RoundedCornerShape(10.dp))
      .padding(10.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = ArborBlack,
      modifier = Modifier.size(22.dp)
    )

    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = ArborBlack
      )
      Text(
        text = description,
        fontSize = 10.sp,
        color = ArborTextSecondary
      )
    }

    if (isGranted) {
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(6.dp))
          .background(ArborSage)
          .padding(horizontal = 8.dp, vertical = 4.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = ArborSuccess,
            modifier = Modifier.size(12.dp)
          )
          Text(
            text = "Permitido",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = ArborForest
          )
        }
      }
    } else {
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(6.dp))
          .background(ArborBlack)
          .clickable { onRequest() }
          .padding(horizontal = 10.dp, vertical = 4.dp)
      ) {
        Text(
          text = "Solicitar",
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          color = ArborWhite
        )
      }
    }
  }
}

@Composable
fun PolicySwitchRow(
  title: String,
  subtitle: String,
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 6.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = ArborBlack
      )
      Text(
        text = subtitle,
        fontSize = 10.sp,
        color = ArborTextSecondary
      )
    }

    Switch(
      checked = checked,
      onCheckedChange = onCheckedChange,
      colors = SwitchDefaults.colors(
        checkedThumbColor = ArborWhite,
        checkedTrackColor = ArborBlack,
        uncheckedThumbColor = ArborWhite,
        uncheckedTrackColor = ArborBorder
      )
    )
  }
}
