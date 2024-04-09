package uk.ac.tees.mad.w9643793.screens.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import uk.ac.tees.mad.w9643793.NavigationDestination

@Composable
fun Dashboard(
    onLogout: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        Text(text = "Student Dashboard")
        Button(onClick = onLogout) {
            Text(text = "Log out")
        }
    }
}

object DashboardDestination : NavigationDestination {
    override val routeName = "dashboard"
}