package com.swiftflyers.weightloss

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.swiftflyers.weightloss.data.ProgramDay
import com.swiftflyers.weightloss.data.Workout
import com.swiftflyers.weightloss.data.WorkoutCategory
import com.swiftflyers.weightloss.data.WorkoutRepository
import com.swiftflyers.weightloss.ui.theme.WorkoutAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkoutAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    WorkoutApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutApp(repository: WorkoutRepository = WorkoutRepository()) {
    val navController = rememberNavController()
    val selectedTab = remember { mutableStateOf("dashboard") }

    Scaffold(
        bottomBar = {
            BottomAppBar {
                BottomNavigationItem(
                    label = "Home",
                    selected = selectedTab.value == "dashboard"
                ) {
                    selectedTab.value = "dashboard"
                    navController.navigate("dashboard") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                }
                BottomNavigationItem(
                    label = "Plan",
                    selected = selectedTab.value == "plan"
                ) {
                    selectedTab.value = "plan"
                    navController.navigate("plan") { launchSingleTop = true }
                }
                BottomNavigationItem(
                    label = "Progress",
                    selected = selectedTab.value == "progress"
                ) {
                    selectedTab.value = "progress"
                    navController.navigate("progress") { launchSingleTop = true }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "dashboard",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("dashboard") {
                DashboardScreen(
                    categories = repository.getCategories(),
                    onWorkoutSelected = { navController.navigate("workout/${'$'}{it.id}") }
                )
            }
            composable("plan") {
                PlanScreen(programDays = repository.getProgram())
            }
            composable("progress") {
                ProgressScreen()
            }
            composable("workout/{id}") { entry ->
                val workoutId = entry.arguments?.getString("id") ?: ""
                val workout = repository.getWorkoutById(workoutId)
                WorkoutDetailScreen(workout = workout, onBack = { navController.popBackStack() })
            }
        }
    }
}

@Composable
private fun BottomNavigationItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
    Column(
        modifier = Modifier
            .weight(1f)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, color = color, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun DashboardScreen(categories: List<WorkoutCategory>, onWorkoutSelected: (Workout) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        item { HeroSection() }
        items(categories) { category ->
            CategorySection(category = category, onWorkoutSelected = onWorkoutSelected)
        }
        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
fun HeroSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Personalized home workouts",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Daily programs designed for weight loss and strength.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun CategorySection(category: WorkoutCategory, onWorkoutSelected: (Workout) -> Unit) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text(
            text = category.title,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            category.workouts.forEach { workout ->
                WorkoutCard(workout = workout) { onWorkoutSelected(workout) }
            }
        }
    }
}

@Composable
fun WorkoutCard(workout: Workout, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = workout.name, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = workout.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Duration: ${'$'}{workout.durationMinutes} min · ${'$'}{workout.caloriesBurned} kcal",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun WorkoutDetailScreen(workout: Workout?, onBack: () -> Unit) {
    if (workout == null) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            text = "Workout not found",
            textAlign = TextAlign.Center
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = workout.name,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = workout.description, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Equipment: ${'$'}{if (workout.equipmentRequired.isEmpty()) "None" else workout.equipmentRequired.joinToString()}"
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Exercises", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        workout.exercises.forEach { exercise ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = exercise.name, fontWeight = FontWeight.SemiBold)
                    Text(text = "${'$'}{exercise.durationSeconds} sec · ${'$'}{exercise.intensity}")
                }
            }
        }
    }
}

@Composable
fun PlanScreen(programDays: List<ProgramDay>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(programDays) { day ->
            Card(elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = day.title, style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    day.exercises.forEach { exercise ->
                        Text("• ${'$'}exercise")
                    }
                }
            }
        }
    }
}

@Composable
fun ProgressScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Track your achievements", style = MaterialTheme.typography.headlineMedium)
        Text(
            text = "Log completed workouts to unlock milestones and stay motivated.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
        ProgressStat(label = "Workouts Completed", value = "12")
        ProgressStat(label = "Total Minutes", value = "360")
        ProgressStat(label = "Calories Burned", value = "4200")
    }
}

@Composable
fun ProgressStat(label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyLarge)
            Text(text = value, style = MaterialTheme.typography.headlineMedium)
        }
    }
}
