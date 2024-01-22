package com.example.scrambleword

import android.app.Activity
import android.app.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scrambleword.data.allWords
import com.example.scrambleword.ui.theme.ScrambleWordTheme

@Composable
fun GameScreen(gameViewModel: GameViewModel = viewModel()){

    val gameUiState by gameViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = "Unscramble",
            Modifier.padding(16.dp),
            fontSize = 22.sp
        )

        GameLayout(
            currentScrambledWord = gameUiState.currentScrambledWord,
            wordCount = gameUiState.currentWordCount,
            userGuess = gameViewModel.userGuess,
            onUserGuessChanged = {gameViewModel.updateUserGuess(it)},
        )

        Spacer(modifier = Modifier.height(12.dp))
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Button(
                onClick = { gameViewModel.checkUserGuess() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Submit",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = { gameViewModel.skipWord()},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Skip",
                    color = Color.Blue,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        GameScore(Score = gameUiState.score)

        if(gameUiState.isGameOver){
            FinalScoreDialog(Score = gameUiState.score, onPlayAgain = { gameViewModel.resetGame() })
        }
    }
}

@Composable
fun GameLayout(
    modifier: Modifier = Modifier,
    currentScrambledWord: String,
    wordCount: Int,
    userGuess: String,
    onUserGuessChanged: (String) -> Unit
){
    Card(
        modifier = Modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "$wordCount/${allWords.size}",
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(Color.Blue)
                    .align(Alignment.End)
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                color = Color.White
            )

            Text(
                text = currentScrambledWord,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(all = 8.dp)
            )

            Text(
                text = "Unscramble the word using all the letters.",
                fontSize = 18.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier
                    .padding(all = 8.dp)
            )
            
            OutlinedTextField(
                value = userGuess,
                onValueChange = onUserGuessChanged,
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                placeholder = {
                    Text(text = "Enter your word")
                },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                label = {
                    Text(text = "Enter your word")
                }
            )
        }
    }
}

@Composable
fun GameScore(
    modifier: Modifier = Modifier,
    Score: Int
){
    Card(
        modifier = Modifier
    ) {
        Text(text = "Score: $Score", Modifier.padding(8.dp))
    }
}


@Composable
private fun FinalScoreDialog(
    Score: Int,
    onPlayAgain:() -> Unit,
    modifier: Modifier = Modifier
){
    val activity = (LocalContext.current as Activity)

    AlertDialog(
        onDismissRequest = { },
        title = { Text(text = "Congratulations!")},
        text = { Text(text = "Your score: $Score")},
        modifier = Modifier,
        dismissButton = {
            TextButton(onClick = { activity.finish() }) {
                Text(text = "Exit")
            }
        },
        confirmButton = {
            TextButton(onClick = { onPlayAgain() }) {
                Text(text = "Play Again")
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ScrambleWordTheme {

    }
}