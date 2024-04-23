package uk.ac.tees.mad.w9643793.screens.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CarRental
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.ac.tees.mad.w9643793.R
import uk.ac.tees.mad.w9643793.screens.profile.StudentFetchedData
import uk.ac.tees.mad.w9643793.ui.theme.componentShape

@Composable
fun ImageViewerCard(images: List<Painter>) {
    for (image in images) {
        Image(
            painter = image,
            contentDescription = "student uploaded images"
        )
    }
}

@Composable
fun MyTextFieldComponent(
    labelValue: String,
    onTextChanged: (String) -> Unit
) {

    val textValue = remember {
        mutableStateOf("")
    }
    val localFocusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(componentShape.small),
        label = { Text(text = labelValue) },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        ),
        singleLine = true,
        maxLines = 1,
        value = textValue.value,
        onValueChange = {
            textValue.value = it
            onTextChanged(it)
        }
    )
}

@Composable
fun StudentDetailsCard(studentFetchedData: StudentFetchedData, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(bottom = 8.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {

                Text(
                    text = studentFetchedData.projectTitle,
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Normal,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = studentFetchedData.projectDuration,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }
            Spacer(modifier = Modifier.height(2.dp))

            HorizontalDivider(thickness = 2.dp, color = Color.LightGray)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Person, contentDescription = null)
                Text(
                    text = studentFetchedData.name,
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun MyTextFieldComponent2Painter(
    labelValue: String,
    painterResource: Painter,
    onTextChanged: (String) -> Unit,
) {

    val textValue = remember {
        mutableStateOf("")
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(componentShape.small),
        label = { Text(text = labelValue) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        singleLine = true,
        maxLines = 1,
        value = textValue.value,
        onValueChange = {
            textValue.value = it
            onTextChanged(it)
        },
        leadingIcon = {
            Icon(painter = painterResource, contentDescription = "")
        }
    )
}
