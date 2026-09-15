package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val ArborLightColorScheme = lightColorScheme(
  primary = ArborBlack,
  onPrimary = ArborWhite,
  primaryContainer = ArborSage,
  onPrimaryContainer = ArborForest,
  secondary = ArborForest,
  onSecondary = ArborWhite,
  secondaryContainer = ArborSageDark,
  onSecondaryContainer = ArborBlack,
  tertiary = ArborMossAccent,
  onTertiary = ArborWhite,
  background = ArborWhite,
  onBackground = ArborTextPrimary,
  surface = ArborWhiteSurface,
  onSurface = ArborTextPrimary,
  surfaceVariant = ArborWhiteSubtle,
  onSurfaceVariant = ArborTextSecondary,
  outline = ArborBorder,
  outlineVariant = ArborBorderDark
)

private val ArborDarkColorScheme = darkColorScheme(
  primary = ArborWhite,
  onPrimary = ArborBlack,
  primaryContainer = ArborCharcoal,
  onPrimaryContainer = ArborSage,
  secondary = ArborSageDark,
  onSecondary = ArborBlack,
  background = ArborBlack,
  onBackground = ArborWhite,
  surface = ArborCharcoal,
  onSurface = ArborWhite,
  surfaceVariant = ArborCharcoal,
  onSurfaceVariant = ArborSage,
  outline = ArborBorderDark
)

@Composable
fun ArborTheme(
  darkTheme: Boolean = false, // White background prioritized by design mandate
  content: @Composable () -> Unit
) {
  val colorScheme = if (darkTheme) ArborDarkColorScheme else ArborLightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

