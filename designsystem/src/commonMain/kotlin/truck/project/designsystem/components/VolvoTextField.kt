package truck.project.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import truck.project.designsystem.theme.DsTheme

@Composable
fun VolvoTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    keyboardOptions: androidx.compose.foundation.text.KeyboardOptions = androidx.compose.foundation.text.KeyboardOptions.Default,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: androidx.compose.ui.text.input.VisualTransformation = androidx.compose.ui.text.input.VisualTransformation.None,
    interactionSource: androidx.compose.foundation.interaction.MutableInteractionSource = androidx.compose.runtime.remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        readOnly = readOnly,
        enabled = enabled,
        interactionSource = interactionSource,
        keyboardOptions = keyboardOptions,
        label = { Text(label, style = DsTheme.typography.labelSmall, letterSpacing = 1.sp) },
        placeholder = placeholder?.let { { Text(it, style = DsTheme.typography.bodyMedium, color = DsTheme.colors.textSecondary.copy(alpha = 0.5f)) } },
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        shape = RoundedCornerShape(20.dp), // Even more rounded
        textStyle = DsTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = DsTheme.colors.primary,
            unfocusedBorderColor = DsTheme.colors.divider,
            focusedContainerColor = DsTheme.colors.surface,
            unfocusedContainerColor = DsTheme.colors.surfaceVariant.copy(alpha = 0.5f),
            disabledContainerColor = DsTheme.colors.surfaceVariant.copy(alpha = 0.3f),
            cursorColor = if (readOnly) Color.Transparent else DsTheme.colors.primary,
            focusedTextColor = DsTheme.colors.textPrimary,
            unfocusedTextColor = DsTheme.colors.textPrimary,
            disabledTextColor = DsTheme.colors.textPrimary,
            focusedLabelColor = DsTheme.colors.primary,
            unfocusedLabelColor = DsTheme.colors.textSecondary,
            disabledLabelColor = DsTheme.colors.textSecondary,
            focusedPlaceholderColor = DsTheme.colors.textSecondary.copy(alpha = 0.5f),
            unfocusedPlaceholderColor = DsTheme.colors.textSecondary.copy(alpha = 0.3f)
        )
    )
}
