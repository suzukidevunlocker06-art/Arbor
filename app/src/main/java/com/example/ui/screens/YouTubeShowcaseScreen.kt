package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import com.example.ui.theme.ArborSuccess
import com.example.ui.theme.ArborTextPrimary
import com.example.ui.theme.ArborTextSecondary
import com.example.ui.theme.ArborTextTertiary
import com.example.ui.theme.ArborWhite
import com.example.ui.theme.ArborWhiteSubtle
import com.example.viewmodel.NavigationTab
import kotlinx.coroutines.delay

data class AppScreenshotFeature(
  val title: String,
  val category: String,
  val description: String,
  val icon: ImageVector,
  val highlights: List<String>,
  val targetTab: NavigationTab
)

@Composable
fun YouTubeShowcaseScreen(
  onNavigateToTab: (NavigationTab) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var showVideoPlayerModal by remember { mutableStateOf(false) }

  val features = listOf(
    AppScreenshotFeature(
      title = "Tablero Kanban & Sprints Nivel 2",
      category = "Gestión Ágil",
      description = "Organización visual de entregables con 4 columnas reactivas: Pendiente, En Progreso, En Revisión y Completada. Mueve tareas al siguiente estado con un solo toque.",
      icon = Icons.Default.Assignment,
      highlights = listOf("4 Columnas Dinámicas", "Avance Instantáneo", "Filtro por Prioridad"),
      targetTab = NavigationTab.TASKS
    ),
    AppScreenshotFeature(
      title = "Registro Hablando (Voice-to-Task)",
      category = "Audio & IA",
      description = "Dicta tareas y notas de sprint en movimiento. Incluye visualizador de ondas dinámicas y conversión instantánea a tareas estructuradas.",
      icon = Icons.Default.Mic,
      highlights = listOf("Ondas de Audio en Vivo", "Reproductor Integrado", "Transcripción Rápida"),
      targetTab = NavigationTab.TASKS
    ),
    AppScreenshotFeature(
      title = "Colaboración Global en Tiempo Real",
      category = "Equipo Remoto",
      description = "Discusión de equipo con soporte para notas de voz y monitorización de zonas horarias simultáneas (Madrid, Bogotá, Londres y Tokio).",
      icon = Icons.Default.Groups,
      highlights = listOf("Husos Horarios en Vivo", "Notas de Audio en Chat", "Presencia Activa"),
      targetTab = NavigationTab.COLLABORATION
    ),
    AppScreenshotFeature(
      title = "Métricas de Sprint & Velocidad",
      category = "Analíticas Pro",
      description = "Indicadores de porcentaje de avance del sprint, conteo de tareas urgentes, balance de carga de trabajo y registro de auditoría en tiempo real.",
      icon = Icons.Default.Speed,
      highlights = listOf("Progreso Circular/Lineal", "Tasa de Finalización", "Balance de Equipo"),
      targetTab = NavigationTab.TASKS
    ),
    AppScreenshotFeature(
      title = "Matriz de Permisos & Seguridad",
      category = "Gobernanza",
      description = "Configuración granular de roles (Admin, Dev, Designer, DevOps) y solicitud interactiva de permisos nativos de Android (Micrófono y Notificaciones).",
      icon = Icons.Default.Security,
      highlights = listOf("Permisos Android Nativos", "Cambio de Perfil Activo", "Auditoría de Acceso"),
      targetTab = NavigationTab.TEAM_PERMISSIONS
    )
  )

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(ArborWhite)
      .padding(horizontal = 20.dp),
    verticalArrangement = Arrangement.spacedBy(18.dp),
    contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp)
  ) {
    // Top Official Brand Header
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Text(
              text = "Vitrina en YouTube & Demo",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Black,
              color = ArborBlack
            )
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(ArborAlert)
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = "VIDEO",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = ArborWhite
              )
            }
          }
          Text(
            text = "Logo oficial, video tour y capturas de pantalla de la aplicación",
            style = MaterialTheme.typography.bodySmall,
            color = ArborTextSecondary,
            fontSize = 12.sp
          )
        }
      }
    }

    // Hero YouTube Showcase Card with 16:9 Banner, Logo and Mockups
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .border(1.dp, ArborBorder, RoundedCornerShape(20.dp))
          .testTag("youtube_showcase_hero_card"),
        colors = CardDefaults.cardColors(containerColor = ArborWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column {
          // 16:9 Banner Image with Play overlay
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .aspectRatio(16f / 9f)
              .background(ArborBlack)
              .clickable { showVideoPlayerModal = true }
          ) {
            Image(
              painter = painterResource(id = R.drawable.youtube_showcase),
              contentDescription = "Banner Oficial de YouTube Arbor Workspace con Logo y Capturas de Pantalla",
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize()
            )

            // Dark subtle gradient overlay
            Box(
              modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.22f))
            )

            // YouTube Red Badge top left
            Box(
              modifier = Modifier
                .padding(12.dp)
                .align(Alignment.TopStart)
                .clip(RoundedCornerShape(6.dp))
                .background(ArborAlert)
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.PlayArrow,
                  contentDescription = null,
                  tint = ArborWhite,
                  modifier = Modifier.size(14.dp)
                )
                Text(
                  text = "YouTube 4K",
                  color = ArborWhite,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            // Duration badge bottom right
            Box(
              modifier = Modifier
                .padding(12.dp)
                .align(Alignment.BottomEnd)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.Black.copy(alpha = 0.8f))
                .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
              Text(
                text = "03:45",
                color = ArborWhite,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
              )
            }

            // Center Play Button with pulse
            val infiniteTransition = rememberInfiniteTransition(label = "pulse_play")
            val playScale by infiniteTransition.animateFloat(
              initialValue = 1f,
              targetValue = 1.15f,
              animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
              ),
              label = "play_scale"
            )

            Box(
              modifier = Modifier
                .align(Alignment.Center)
                .size(60.dp)
                .scale(playScale)
                .clip(CircleShape)
                .background(ArborAlert)
                .border(2.dp, ArborWhite, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Reproducir Video Demo en YouTube",
                tint = ArborWhite,
                modifier = Modifier.size(34.dp)
              )
            }
          }

          // Card details
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              // Logo icon
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(RoundedCornerShape(8.dp))
                .border(1.dp, ArborBorder, RoundedCornerShape(8.dp))
                .background(ArborWhite),
                contentAlignment = Alignment.Center
              ) {
                Image(
                  painter = painterResource(id = R.drawable.ic_arbor_logo),
                  contentDescription = "Logo Arbor",
                  modifier = Modifier.size(30.dp)
                )
              }

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "Arbor Workspace: Tour Oficial & Guía Completa",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = ArborBlack
                )
                Text(
                  text = "Arbor Tech Lab • 14.8K vistas • Calidad 4K HDR",
                  fontSize = 11.sp,
                  color = ArborTextSecondary
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
              text = "Descubre cómo Arbor Workspace unifica la gestión ágil de proyectos remotos, sprints interactivos, registro hablando por voz y sincronización de equipos globales en una interfaz minimalista de alto contraste.",
              style = MaterialTheme.typography.bodyMedium,
              color = ArborTextSecondary,
              lineHeight = 19.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Button(
                onClick = { showVideoPlayerModal = true },
                colors = ButtonDefaults.buttonColors(
                  containerColor = ArborAlert,
                  contentColor = ArborWhite
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                  .weight(1f)
                  .testTag("play_video_demo_button")
              ) {
                Icon(
                  imageVector = Icons.Default.PlayArrow,
                  contentDescription = null,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "Ver Demo",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold
                )
              }

              OutlinedButton(
                onClick = {
                  openYouTubeUrl(context)
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                  contentColor = ArborBlack
                ),
                modifier = Modifier
                  .weight(1f)
                  .border(1.dp, ArborBlack, RoundedCornerShape(12.dp))
                  .testTag("open_youtube_app_button")
              ) {
                Icon(
                  imageVector = Icons.Default.OpenInBrowser,
                  contentDescription = null,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = "En YouTube",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Bold
                )
              }

              IconButton(
                onClick = {
                  shareAppShowcase(context)
                },
                modifier = Modifier
                  .clip(RoundedCornerShape(12.dp))
                  .border(1.dp, ArborBorder, RoundedCornerShape(12.dp))
                  .background(ArborWhiteSubtle)
                  .size(42.dp)
                  .testTag("share_youtube_tour_button")
              ) {
                Icon(
                  imageVector = Icons.Default.Share,
                  contentDescription = "Compartir Vitrina",
                  tint = ArborBlack,
                  modifier = Modifier.size(18.dp)
                )
              }
            }
          }
        }
      }
    }

    // Section: Capturas de Pantalla de lo que tiene la App
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text(
            text = "Capturas & Arquitectura de la App",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = ArborBlack
          )
          Text(
            text = "Módulos interactivos integrados en Arbor Workspace Nivel 2",
            fontSize = 12.sp,
            color = ArborTextSecondary
          )
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(ArborSage)
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Text(
            text = "5 Módulos",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = ArborForest
          )
        }
      }
    }

    // List of screenshot feature modules
    items(features) { feature ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .border(1.dp, ArborBorder, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = ArborWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(ArborForest.copy(alpha = 0.12f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = feature.icon,
                contentDescription = null,
                tint = ArborForest,
                modifier = Modifier.size(22.dp)
              )
            }

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = feature.category.uppercase(),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = ArborForest,
                letterSpacing = 0.5.sp
              )
              Text(
                text = feature.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = ArborBlack
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = feature.description,
            style = MaterialTheme.typography.bodySmall,
            color = ArborTextSecondary,
            lineHeight = 18.sp
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Feature Highlights pills
          Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            feature.highlights.forEach { tag ->
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(ArborWhiteSubtle)
                  .border(0.5.dp, ArborBorder, RoundedCornerShape(6.dp))
                  .padding(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Text(
                  text = "✓ $tag",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Medium,
                  color = ArborTextPrimary
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Direct jump button to test feature in the app
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
          ) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(ArborBlack)
                .clickable { onNavigateToTab(feature.targetTab) }
                .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Text(
                  text = "Probar en la App",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = ArborWhite
                )
                Icon(
                  imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                  contentDescription = null,
                  tint = ArborWhite,
                  modifier = Modifier.size(12.dp)
                )
              }
            }
          }
        }
      }
    }

    // Technical Specs & Compilation Card
    item {
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .border(1.dp, ArborBorder, RoundedCornerShape(16.dp)),
        color = ArborWhiteSubtle
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = ArborSuccess,
              modifier = Modifier.size(20.dp)
            )
            Text(
              text = "Estado de Compilación & APK (Nivel 2)",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp,
              color = ArborBlack
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "• Arquitectura: Jetpack Compose + MVVM + Room Database\n" +
              "• Nivel de App: Nivel 2 (Tablero Kanban, Audio Interactivo, Zonas Horarias)\n" +
              "• Compilación: Gradle Android Application con soporte completo para APK debug y release\n" +
              "• Identidad: Logo botánico 'Arbor' en alta resolución y tema blanco/negro orgánico",
            fontSize = 11.sp,
            color = ArborTextSecondary,
            lineHeight = 17.sp
          )
        }
      }
    }
  }

  // Interactive Video Player Modal
  if (showVideoPlayerModal) {
    InteractiveVideoModal(
      onDismiss = { showVideoPlayerModal = false },
      onOpenExternal = {
        showVideoPlayerModal = false
        openYouTubeUrl(context)
      }
    )
  }
}

@Composable
fun InteractiveVideoModal(
  onDismiss: () -> Unit,
  onOpenExternal: () -> Unit
) {
  var isPlaying by remember { mutableStateOf(true) }
  var currentSec by remember { mutableIntStateOf(14) }
  val totalSec = 225 // 3m 45s

  LaunchedEffect(isPlaying) {
    while (isPlaying && currentSec < totalSec) {
      delay(1000)
      currentSec++
    }
  }

  val chapters = listOf(
    "00:00" to "Introducción & Logo Botánico Arbor",
    "00:45" to "Tablero Kanban Nivel 2 y Mover Tareas",
    "01:30" to "Registro Hablando & Notas de Voz",
    "02:15" to "Colaboración Global en Zonas Horarias",
    "03:00" to "Matriz de Permisos & Exportación"
  )

  AlertDialog(
    onDismissRequest = onDismiss,
    confirmButton = {
      Button(
        onClick = onOpenExternal,
        colors = ButtonDefaults.buttonColors(containerColor = ArborAlert)
      ) {
        Icon(imageVector = Icons.Default.OpenInBrowser, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("Abrir en YouTube")
      }
    },
    dismissButton = {
      OutlinedButton(onClick = onDismiss) {
        Text("Cerrar", color = ArborBlack)
      }
    },
    title = {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Box(
          modifier = Modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(if (isPlaying) ArborAlert else ArborTextTertiary)
        )
        Text(
          text = "Reproductor Demo: Arbor Workspace",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = ArborBlack
        )
      }
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // Video Preview Screen with Banner
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(12.dp))
            .background(ArborBlack)
        ) {
          Image(
            painter = painterResource(id = R.drawable.youtube_showcase),
            contentDescription = "Video Demo Preview",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )

          // Play/pause overlay button
          Box(
            modifier = Modifier
              .align(Alignment.Center)
              .size(48.dp)
              .clip(CircleShape)
              .background(Color.Black.copy(alpha = 0.7f))
              .clickable { isPlaying = !isPlaying },
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
              contentDescription = null,
              tint = ArborWhite,
              modifier = Modifier.size(28.dp)
            )
          }

          // Video bottom bar
          Column(
            modifier = Modifier
              .align(Alignment.BottomCenter)
              .fillMaxWidth()
              .background(Color.Black.copy(alpha = 0.65f))
              .padding(horizontal = 10.dp, vertical = 6.dp)
          ) {
            val progress = currentSec.toFloat() / totalSec.toFloat()
            LinearProgressIndicator(
              progress = { progress },
              modifier = Modifier
                .fillMaxWidth()
                .height(3.dp),
              color = ArborAlert,
              trackColor = Color.White.copy(alpha = 0.3f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              val curM = currentSec / 60
              val curS = currentSec % 60
              val totM = totalSec / 60
              val totS = totalSec % 60
              Text(
                text = "${curM}:${curS.toString().padStart(2, '0')} / ${totM}:${totS.toString().padStart(2, '0')}",
                fontSize = 10.sp,
                color = ArborWhite,
                fontWeight = FontWeight.SemiBold
              )
              Text(
                text = "1080p60 • HD",
                fontSize = 10.sp,
                color = ArborWhite.copy(alpha = 0.8f)
              )
            }
          }
        }

        Text(
          text = "Capítulos de la demostración:",
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          color = ArborBlack
        )

        chapters.forEach { (time, name) ->
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(6.dp))
              .background(ArborWhiteSubtle)
              .padding(horizontal = 8.dp, vertical = 6.dp)
          ) {
            Text(
              text = time,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = ArborAlert
            )
            Text(
              text = name,
              fontSize = 11.sp,
              color = ArborTextPrimary
            )
          }
        }
      }
    },
    containerColor = ArborWhite,
    shape = RoundedCornerShape(18.dp)
  )
}

fun openYouTubeUrl(context: Context) {
  try {
    val intent = Intent(
      Intent.ACTION_VIEW,
      Uri.parse("https://www.youtube.com/results?search_query=Arbor+Workspace+Remote+Project+Management+Android")
    )
    context.startActivity(intent)
  } catch (e: Exception) {
    // Fallback if browser/YouTube not available
  }
}

fun shareAppShowcase(context: Context) {
  try {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
      type = "text/plain"
      putExtra(
        Intent.EXTRA_SUBJECT,
        "Arbor Workspace — Gestión de Proyectos Remotos en Android"
      )
      putExtra(
        Intent.EXTRA_TEXT,
        "¡Mira Arbor Workspace para equipos remotos! Con gestión de sprints, tablero Kanban interactivo, registro por voz en tiempo real y colaboración global. Ver vitrina en YouTube: https://www.youtube.com/results?search_query=Arbor+Workspace+Remote+Project+Management"
      )
    }
    context.startActivity(Intent.createChooser(shareIntent, "Compartir Vitrina de Arbor"))
  } catch (e: Exception) {
    // Graceful fallback
  }
}
