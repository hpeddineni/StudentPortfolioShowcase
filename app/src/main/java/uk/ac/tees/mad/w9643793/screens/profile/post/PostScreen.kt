package uk.ac.tees.mad.w9643793.screens.profile.post

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import uk.ac.tees.mad.w9643793.ui.theme.StudentPortfolioShowcaseTheme

@Composable
fun PostScreen() {
    Column {
        IconButton(onClick = { /*TODO*/ }) {

        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PostScreenPreview() {
    StudentPortfolioShowcaseTheme {
        PostScreen()
    }
}