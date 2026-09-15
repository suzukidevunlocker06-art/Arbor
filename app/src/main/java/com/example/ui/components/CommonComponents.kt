package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.ArborAlert
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
import com.example.ui.theme.ArborWarning
import com.example.ui.theme.ArborWhite
import com.example.ui.theme.ArborWhiteSubtle

@Composable
fun ArborAppHeader(
  currentUserInitials: String = "SN",
  currentUserName: String = "Sofia Navarro",
  unreadNotificationsCount: Int = 2,
  onNotificationClick: () -> Unit = {},
  onProfileClick: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .background(ArborWhite)
      .padding(horizontal = 20.dp, vertical = 14.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(RoundedCornerShape(12.dp))
          .border(1.dp, ArborBorder, RoundedCornerShape(12.dp))
          .background(ArborWhite),
        contentAlignment = Alignment.Center
      ) {
        Image(
          painter = painterResource(id = R.drawable.ic_arbor_logo),
          contentDescription = "Logo Arbor",
          modifier = Modifier.size(34.dp)
        )
      }
      Column {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          Text(
            text = "ARBOR",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.5.sp,
            color = ArborBlack
          )
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(ArborSage)
              .padding(horizontal = 6.dp, vertical = 2.dp)
          ) {
            Text(
              text = "PRO",
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold,
              color = ArborForest
            )
          }
        }
        Text(
          text = "Espacio Remoto Colaborativo",
          style = MaterialTheme.typography.bodySmall,
          color = ArborTextSecondary,
          fontSize = 11.sp
        )
      }
    }

    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      IconButton(
        onClick = onNotificationClick,
        modifier = Modifier
          .size(44.dp)
          .testTag("header_notifications_button")
      ) {
        BadgedBox(
          badge = {
            if (unreadNotificationsCount > 0) {
              Badge(
                containerColor = ArborBlack,
                contentColor = ArborWhite
              ) {
                Text(text = "$unreadNotificationsCount", fontSize = 10.sp)
              }
            }
          }
        ) {
          Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "Notificaciones",
            tint = ArborBlack
          )
        }
      }

      Box(
        modifier = Modifier
          .size(38.dp)
          .clip(CircleShape)
          .background(ArborBlack)
          .clickable { onProfileClick() }
          .testTag("header_profile_button"),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = currentUserInitials,
          color = ArborWhite,
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp
        )
      }
    }
  }
}

@Composable
fun StatusBadge(status: String, modifier: Modifier = Modifier) {
  val (bgColor, textColor) = when (status) {
    "Completada", "Completado", "Aprobado" -> Pair(ArborSage, ArborSuccess)
    "En Progreso", "Activo" -> Pair(ArborBlack, ArborWhite)
    "En Revisión" -> Pair(ArborSageDark, ArborForest)
    "Planificación", "Pendiente" -> Pair(ArborWhiteSubtle, ArborTextSecondary)
    else -> Pair(ArborWhiteSubtle, ArborTextPrimary)
  }

  Box(
    modifier = modifier
      .clip(RoundedCornerShape(8.dp))
      .background(bgColor)
      .border(0.5.dp, ArborBorder, RoundedCornerShape(8.dp))
      .padding(horizontal = 10.dp, vertical = 4.dp)
  ) {
    Text(
      text = status,
      color = textColor,
      fontSize = 11.sp,
      fontWeight = FontWeight.SemiBold
    )
  }
}

@Composable
fun PriorityBadge(priority: String, modifier: Modifier = Modifier) {
  val (color, label) = when (priority) {
    "Urgente" -> Pair(ArborAlert, "Urgente")
    "Alta" -> Pair(ArborWarning, "Alta")
    "Media" -> Pair(ArborLeafGreen, "Media")
    else -> Pair(ArborTextTertiary, "Baja")
  }

  Row(
    modifier = modifier
      .clip(RoundedCornerShape(6.dp))
      .background(color.copy(alpha = 0.12f))
      .padding(horizontal = 8.dp, vertical = 3.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    Box(
      modifier = Modifier
        .size(6.dp)
        .clip(CircleShape)
        .background(color)
    )
    Text(
      text = label,
      color = color,
      fontSize = 11.sp,
      fontWeight = FontWeight.Medium
    )
  }
}

@Composable
fun ArborSearchBar(
  query: String,
  onQueryChange: (String) -> Unit,
  placeholder: String = "Buscar proyectos, tareas, personas...",
  modifier: Modifier = Modifier
) {
  OutlinedTextField(
    value = query,
    onValueChange = onQueryChange,
    placeholder = {
      Text(
        text = placeholder,
        color = ArborTextTertiary,
        fontSize = 13.sp
      )
    },
    leadingIcon = {
      Icon(
        imageVector = Icons.Default.Search,
        contentDescription = "Buscar",
        tint = ArborTextSecondary,
        modifier = Modifier.size(20.dp)
      )
    },
    singleLine = true,
    shape = RoundedCornerShape(14.dp),
    colors = OutlinedTextFieldDefaults.colors(
      focusedContainerColor = ArborWhite,
      unfocusedContainerColor = ArborWhiteSubtle,
      focusedBorderColor = ArborBlack,
      unfocusedBorderColor = ArborBorder,
      cursorColor = ArborBlack
    ),
    modifier = modifier
      .fillMaxWidth()
      .height(52.dp)
      .testTag("arbor_search_bar")
  )
}

@Composable
fun VoiceWaveformPlayer(
  durationSec: Int,
  transcript: String? = null,
  modifier: Modifier = Modifier
) {
  var isPlaying by remember { mutableStateOf(false) }

  Column(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .background(ArborWhiteSubtle)
      .border(1.dp, ArborBorder, RoundedCornerShape(12.dp))
      .padding(10.dp)
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(CircleShape)
          .background(ArborForest)
          .clickable { isPlaying = !isPlaying },
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
          contentDescription = if (isPlaying) "Detener" else "Reproducir nota de voz",
          tint = ArborWhite,
          modifier = Modifier.size(20.dp)
        )
      }

      // Simulated Soundwave
      Row(
        modifier = Modifier.weight(1f),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        val barHeights = listOf(14, 24, 18, 28, 12, 32, 20, 16, 26, 30, 14, 22, 10, 24, 18, 28)
        barHeights.forEachIndexed { index, h ->
          val barColor = if (isPlaying && index < 9) ArborBlack else ArborTextTertiary
          Box(
            modifier = Modifier
              .width(3.dp)
              .height(h.dp)
              .clip(RoundedCornerShape(2.dp))
              .background(barColor)
          )
        }
      }

      Text(
        text = "${durationSec}s",
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color = ArborTextSecondary
      )
    }

    if (!transcript.isNullOrBlank()) {
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = "\"$transcript\"",
        fontSize = 12.sp,
        color = ArborTextPrimary,
        style = MaterialTheme.typography.bodySmall
      )
    }
  }
}

@Composable
fun VoiceRecordingBanner(
  isRecording: Boolean,
  recordingSeconds: Int,
  onStartRecord: () -> Unit,
  onStopRecord: () -> Unit,
  onSaveAsTask: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = 1.25f,
    animationSpec = infiniteRepeatable(
      animation = tween(800, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse_scale"
  )

  Surface(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .border(1.dp, if (isRecording) ArborAlert else ArborBorder, RoundedCornerShape(16.dp)),
    color = if (isRecording) ArborWhiteSubtle else ArborWhite,
    tonalElevation = 2.dp
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .scale(if (isRecording) pulseScale else 1f)
              .clip(CircleShape)
              .background(if (isRecording) ArborAlert else ArborForest)
              .clickable {
                if (isRecording) onStopRecord() else onStartRecord()
              },
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
              contentDescription = if (isRecording) "Detener grabación" else "Iniciar dictado por voz",
              tint = ArborWhite,
              modifier = Modifier.size(22.dp)
            )
          }

          Column {
            Text(
              text = if (isRecording) "Grabando nota de voz..." else "Registro rápido por voz",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = if (isRecording) ArborAlert else ArborBlack
            )
            Text(
              text = if (isRecording) "Duración: ${recordingSeconds}s • Habla con claridad" else "Dicta una tarea o comentario para el equipo remoto",
              style = MaterialTheme.typography.bodySmall,
              color = ArborTextSecondary,
              fontSize = 11.sp
            )
          }
        }

        if (isRecording) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.GraphicEq,
              contentDescription = null,
              tint = ArborAlert,
              modifier = Modifier.size(20.dp)
            )
            Text(
              text = "REC",
              fontWeight = FontWeight.Black,
              fontSize = 12.sp,
              color = ArborAlert
            )
          }
        }
      }

      if (!isRecording && recordingSeconds > 0) {
        Spacer(modifier = Modifier.height(12.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Grabación lista (${recordingSeconds}s)",
            fontSize = 12.sp,
            color = ArborForest,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
          )
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(ArborBlack)
              .clickable {
                onSaveAsTask("Nota de voz de ${recordingSeconds}s convertida en tarea")
              }
              .padding(horizontal = 14.dp, vertical = 8.dp)
          ) {
            Text(
              text = "Convertir en Tarea",
              color = ArborWhite,
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }
    }
  }
}
