package com.ayesha.learningapp


import android.net.Uri
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LearningApp()
        }
    }
}


val DarkBrown = Color(0xFF38240D)
val Brown = Color(0xFF713600)
val OrangeBrown = Color(0xFFC05800)
val Cream = Color(0xFFDFDBD4)
val LightCream = Color(0xFFF5F1EB)
val White = Color.White
val LightOrange = Color(0xFFFFE1C4)
val SuccessGreen = Color(0xFFDFF2DF)


 //   MAIN APP

@Composable
fun LearningApp() {

    var selectedModule by remember {
        mutableStateOf<LearningModule?>(null)
    }

    var showSubscriptionScreen by remember {
        mutableStateOf(false)
    }

    var showLearningContent by remember {
        mutableStateOf(false)
    }

    var showTransactionHistory by remember {
        mutableStateOf(false)
    }

    var isSubscribed by remember {
        mutableStateOf(false)
    }


    MaterialTheme {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = LightCream
        ) {

            when {

                /*
                 * Subscription screen
                 */
                showSubscriptionScreen -> {

                    SubscriptionScreen(
                        onSubscriptionComplete = {

                            isSubscribed = true
                            showSubscriptionScreen = false
                        },

                        onBack = {

                            showSubscriptionScreen = false
                        }
                    )
                }


                /*
                 * Transaction history
                 */
                showTransactionHistory -> {

                    TransactionHistoryScreen(
                        onBack = {

                            showTransactionHistory = false
                        }
                    )
                }


                /*
                 * Learning content
                 */
                showLearningContent && selectedModule != null -> {

                    LearningContentScreen(
                        module = selectedModule!!,

                        onBack = {

                            showLearningContent = false
                        }
                    )
                }


                /*
                 * Module detail
                 */
                selectedModule != null -> {

                    ModuleDetailScreen(
                        module = selectedModule!!,

                        isSubscribed = isSubscribed,

                        onBack = {

                            selectedModule = null
                        },

                        onSubscribe = {

                            showSubscriptionScreen = true
                        },

                        onStartLearning = {

                            showLearningContent = true
                        }
                    )
                }


                /*
                 * Home
                 */
                else -> {

                    HomeScreen(
                        isSubscribed = isSubscribed,

                        onModuleClick = { module ->

                            selectedModule = module
                        },

                        onSubscribe = {

                            showSubscriptionScreen = true
                        },

                        onTransactionHistory = {

                            showTransactionHistory = true
                        }
                    )
                }
            }
        }
    }
}


// DATA MODEL

data class LearningModule(
    val title: String,
    val description: String,
    val isPremium: Boolean
)


   // MODULE LIST

val learningModules = listOf(

    LearningModule(
        title = "Introduction to Programming",
        description = "Learn programming fundamentals, variables, conditions, loops and functions.",
        isPremium = false
    ),

    LearningModule(
        title = "Object-Oriented Programming",
        description = "Understand classes, objects, inheritance, encapsulation and polymorphism.",
        isPremium = false
    ),

    LearningModule(
        title = "Advanced Kotlin",
        description = "Learn advanced Kotlin concepts including collections, lambdas, null safety and coroutines.",
        isPremium = true
    ),

    LearningModule(
        title = "Android App Development",
        description = "Learn how Android applications are structured and developed using Kotlin.",
        isPremium = true
    ),

    LearningModule(
        title = "Advanced Software Development",
        description = "Explore advanced software development concepts, architecture and best practices.",
        isPremium = true
    )
)


   // HOME SCREEN

@Composable
fun HomeScreen(
    isSubscribed: Boolean,
    onModuleClick: (LearningModule) -> Unit,
    onSubscribe: () -> Unit,
    onTransactionHistory: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightCream)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Spacer(
            modifier = Modifier.height(12.dp)
        )


        Text(
            text = "Learning App",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = DarkBrown
        )


        Spacer(
            modifier = Modifier.height(6.dp)
        )


        Text(
            text = "Learn. Practice. Grow.",
            fontSize = 17.sp,
            color = Brown
        )


        Spacer(
            modifier = Modifier.height(20.dp)
        )


        /*
         * Subscription status
         */

        Card(
            modifier = Modifier.fillMaxWidth(),

            shape = RoundedCornerShape(16.dp),

            colors = CardDefaults.cardColors(
                containerColor =
                    if (isSubscribed) {
                        SuccessGreen
                    } else {
                        LightOrange
                    }
            )
        ) {

            Column(
                modifier = Modifier.padding(18.dp)
            ) {

                Text(
                    text =
                        if (isSubscribed) {
                            "Premium Active"
                        } else {
                            "Free Account"
                        },

                    fontSize = 19.sp,

                    fontWeight = FontWeight.Bold,

                    color = DarkBrown
                )


                Spacer(
                    modifier = Modifier.height(6.dp)
                )


                Text(
                    text =
                        if (isSubscribed) {
                            "You have access to all premium learning modules."
                        } else {
                            "Subscribe to unlock premium learning modules."
                        },

                    fontSize = 14.sp,

                    color = Brown
                )


                if (!isSubscribed) {

                    Spacer(
                        modifier = Modifier.height(14.dp)
                    )


                    Button(
                        onClick = onSubscribe,

                        modifier = Modifier.fillMaxWidth(),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = OrangeBrown
                        ),

                        shape = RoundedCornerShape(12.dp)
                    ) {

                        Text(
                            text = "View Subscription Plans",

                            color = White,

                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }


        Spacer(
            modifier = Modifier.height(16.dp)
        )


        /*
         * Payment history
         */

        Button(
            onClick = onTransactionHistory,

            modifier = Modifier.fillMaxWidth(),

            colors = ButtonDefaults.buttonColors(
                containerColor = Brown
            ),

            shape = RoundedCornerShape(12.dp)
        ) {

            Text(
                text = "Payment History",

                color = White,

                fontWeight = FontWeight.Bold
            )
        }


        Spacer(
            modifier = Modifier.height(28.dp)
        )


        Text(
            text = "Learning Modules",

            fontSize = 23.sp,

            fontWeight = FontWeight.Bold,

            color = DarkBrown
        )


        Spacer(
            modifier = Modifier.height(14.dp)
        )


        learningModules.forEach { module ->

            ModuleCard(
                module = module,

                isSubscribed = isSubscribed,

                onClick = {

                    onModuleClick(module)
                }
            )


            Spacer(
                modifier = Modifier.height(14.dp)
            )
        }


        Spacer(
            modifier = Modifier.height(20.dp)
        )
    }
}


   // MODULE CARD

@Composable
fun ModuleCard(
    module: LearningModule,
    isSubscribed: Boolean,
    onClick: () -> Unit
) {

    val locked =
        module.isPremium && !isSubscribed


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {

                onClick()
            },

        shape = RoundedCornerShape(16.dp),

        colors = CardDefaults.cardColors(
            containerColor = White
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.SpaceBetween,

                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = module.title,

                    modifier = Modifier.weight(1f),

                    fontSize = 19.sp,

                    fontWeight = FontWeight.Bold,

                    color = DarkBrown
                )


                if (locked) {

                    Text(
                        text = "LOCKED",

                        fontSize = 12.sp,

                        fontWeight = FontWeight.Bold,

                        color = OrangeBrown
                    )

                } else if (module.isPremium) {

                    Text(
                        text = "PREMIUM",

                        fontSize = 12.sp,

                        fontWeight = FontWeight.Bold,

                        color = OrangeBrown
                    )

                } else {

                    Text(
                        text = "FREE",

                        fontSize = 12.sp,

                        fontWeight = FontWeight.Bold,

                        color = Brown
                    )
                }
            }


            Spacer(
                modifier = Modifier.height(8.dp)
            )


            Text(
                text = module.description,

                fontSize = 14.sp,

                color = Brown,

                lineHeight = 21.sp
            )


            Spacer(
                modifier = Modifier.height(12.dp)
            )


            Text(
                text =
                    if (locked) {
                        "Subscribe to unlock"
                    } else {
                        "Open module →"
                    },

                fontSize = 14.sp,

                fontWeight = FontWeight.Bold,

                color = OrangeBrown
            )
        }
    }
}


  //  MODULE DETAIL SCREEN

@Composable
fun ModuleDetailScreen(
    module: LearningModule,
    isSubscribed: Boolean,
    onBack: () -> Unit,
    onSubscribe: () -> Unit,
    onStartLearning: () -> Unit
) {

    val locked =
        module.isPremium && !isSubscribed


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightCream)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        BackButton(
            onBack = onBack
        )


        Spacer(
            modifier = Modifier.height(22.dp)
        )


        Text(
            text = module.title,

            fontSize = 28.sp,

            fontWeight = FontWeight.Bold,

            color = DarkBrown
        )


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        Text(
            text = module.description,

            fontSize = 16.sp,

            color = Brown,

            lineHeight = 24.sp
        )


        Spacer(
            modifier = Modifier.height(28.dp)
        )


        if (locked) {

            Card(
                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(16.dp),

                colors = CardDefaults.cardColors(
                    containerColor = LightOrange
                )
            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Text(
                        text = "Premium Module",

                        fontSize = 21.sp,

                        fontWeight = FontWeight.Bold,

                        color = DarkBrown
                    )


                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )


                    Text(
                        text = "This module is available with a premium subscription.",

                        fontSize = 15.sp,

                        color = Brown,

                        lineHeight = 22.sp
                    )


                    Spacer(
                        modifier = Modifier.height(18.dp)
                    )


                    Button(
                        onClick = onSubscribe,

                        modifier = Modifier.fillMaxWidth(),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = OrangeBrown
                        ),

                        shape = RoundedCornerShape(12.dp)
                    ) {

                        Text(
                            text = "Subscribe Now",

                            color = White,

                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

        } else {

            Card(
                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(16.dp),

                colors = CardDefaults.cardColors(
                    containerColor = White
                )
            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Text(
                        text = "Ready to Learn?",

                        fontSize = 21.sp,

                        fontWeight = FontWeight.Bold,

                        color = DarkBrown
                    )


                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )


                    Text(
                        text = "Start learning this module step by step.",

                        fontSize = 15.sp,

                        color = Brown
                    )


                    Spacer(
                        modifier = Modifier.height(18.dp)
                    )


                    Button(
                        onClick = onStartLearning,

                        modifier = Modifier.fillMaxWidth(),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = OrangeBrown
                        ),

                        shape = RoundedCornerShape(12.dp)
                    ) {

                        Text(
                            text = "Start Learning",

                            color = White,

                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}


   // LEARNING CONTENT SCREEN

@Composable
fun LearningContentScreen(
    module: LearningModule,
    onBack: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightCream)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        BackButton(
            onBack = onBack
        )


        Spacer(
            modifier = Modifier.height(20.dp)
        )


        Text(
            text = module.title,

            fontSize = 28.sp,

            fontWeight = FontWeight.Bold,

            color = DarkBrown
        )


        Spacer(
            modifier = Modifier.height(8.dp)
        )


        Divider(
            color = Cream
        )


        Spacer(
            modifier = Modifier.height(20.dp)
        )


        when (module.title) {

            "Introduction to Programming" -> {

                IntroductionProgrammingLesson()
            }

            "Object-Oriented Programming" -> {

                OopLesson()
            }

            "Advanced Kotlin" -> {

                AdvancedKotlinLesson()
            }

            "Android App Development" -> {

                AndroidDevelopmentLesson()
            }

            "Advanced Software Development" -> {

                AdvancedSoftwareDevelopmentLesson()
            }
        }


        Spacer(
            modifier = Modifier.height(30.dp)
        )
    }
}


  // INTRODUCTION TO PROGRAMMING

@Composable
fun IntroductionProgrammingLesson() {

    LessonHeading(
        "1. What is Programming?"
    )

    LessonText(
        "Programming is the process of writing instructions that tell a computer how to perform a task."
    )


    LessonHeading(
        "2. Variables"
    )

    LessonText(
        "A variable stores information that can be used by a program. Examples include names, ages and scores."
    )


    LessonHeading(
        "3. Conditions"
    )

    LessonText(
        "Conditional statements allow a program to make decisions. The most common example is the if statement."
    )


    LessonHeading(
        "4. Loops"
    )

    LessonText(
        "Loops allow a program to repeat instructions multiple times. Common loops include for and while loops."
    )


    LessonHeading(
        "5. Functions"
    )

    LessonText(
        "A function is a reusable block of code designed to perform a particular task."
    )


    LessonHeading(
        "Summary"
    )

    LessonText(
        "Programming fundamentals provide the foundation for learning more advanced programming languages and software development."
    )
}


//  OBJECT-ORIENTED PROGRAMMING

@Composable
fun OopLesson() {

    LessonHeading(
        "1. Classes and Objects"
    )

    LessonText(
        "A class defines the structure and behavior of an object. An object is an instance created from a class."
    )


    LessonHeading(
        "2. Encapsulation"
    )

    LessonText(
        "Encapsulation combines data and methods inside a class and controls how the data can be accessed."
    )


    LessonHeading(
        "3. Inheritance"
    )

    LessonText(
        "Inheritance allows one class to inherit properties and behavior from another class."
    )


    LessonHeading(
        "4. Polymorphism"
    )

    LessonText(
        "Polymorphism allows the same operation to behave differently depending on the object involved."
    )


    LessonHeading(
        "5. Abstraction"
    )

    LessonText(
        "Abstraction focuses on important features while hiding unnecessary implementation details."
    )


    LessonHeading(
        "Summary"
    )

    LessonText(
        "Object-oriented programming helps developers organize large programs into reusable and manageable components."
    )
}


   // ADVANCED KOTLIN

@Composable
fun AdvancedKotlinLesson() {

    LessonHeading(
        "1. Kotlin Collections"
    )

    LessonText(
        "Kotlin provides collections such as List, Set and Map for storing and organizing groups of data."
    )


    LessonHeading(
        "2. Lambda Expressions"
    )

    LessonText(
        "Lambda expressions provide a concise way to represent functions and are commonly used with Kotlin collection operations."
    )


    LessonHeading(
        "3. Null Safety"
    )

    LessonText(
        "Kotlin provides null-safety features that help developers reduce null pointer errors."
    )


    LessonHeading(
        "4. Extension Functions"
    )

    LessonText(
        "Extension functions allow developers to add functionality to an existing class without modifying its original source code."
    )


    LessonHeading(
        "5. Coroutines"
    )

    LessonText(
        "Coroutines provide a way to perform asynchronous programming while keeping code easier to read and maintain."
    )


    LessonHeading(
        "Summary"
    )

    LessonText(
        "Advanced Kotlin features help developers write concise, safe and efficient applications."
    )
}


  //  ANDROID APP DEVELOPMENT

@Composable
fun AndroidDevelopmentLesson() {

    LessonHeading(
        "1. Android Applications"
    )

    LessonText(
        "Android applications are software programs designed to run on Android devices."
    )


    LessonHeading(
        "2. Activities"
    )

    LessonText(
        "An Activity represents a screen or entry point within an Android application."
    )


    LessonHeading(
        "3. Jetpack Compose"
    )

    LessonText(
        "Jetpack Compose is Android's modern toolkit for building user interfaces using Kotlin."
    )


    LessonHeading(
        "4. State"
    )

    LessonText(
        "State represents information that can change while the application is running. Compose updates the interface when relevant state changes."
    )


    LessonHeading(
        "5. Navigation"
    )

    LessonText(
        "Navigation allows users to move between different screens or destinations in an application."
    )


    LessonHeading(
        "Summary"
    )

    LessonText(
        "Android development combines Kotlin programming, user interface design, application architecture and platform APIs."
    )
}


    // ADVANCED SOFTWARE DEVELOPMENT

@Composable
fun AdvancedSoftwareDevelopmentLesson() {

    LessonHeading(
        "1. Software Architecture"
    )

    LessonText(
        "Software architecture describes the overall structure of a software system and how its components interact."
    )


    LessonHeading(
        "2. Separation of Concerns"
    )

    LessonText(
        "Separation of concerns means dividing software into components with clearly defined responsibilities."
    )


    LessonHeading(
        "3. Maintainability"
    )

    LessonText(
        "Maintainable software is easier to understand, modify, test and extend."
    )


    LessonHeading(
        "4. Testing"
    )

    LessonText(
        "Software testing helps developers identify errors and verify that software behaves as expected."
    )


    LessonHeading(
        "5. Version Control"
    )

    LessonText(
        "Version control systems allow developers to track changes to source code and collaborate effectively."
    )


    LessonHeading(
        "Summary"
    )

    LessonText(
        "Advanced software development focuses on creating reliable, maintainable and scalable software systems."
    )
}


  //  LESSON HEADING

@Composable
fun LessonHeading(
    text: String
) {

    Text(
        text = text,

        modifier = Modifier.padding(
            top = 14.dp,
            bottom = 8.dp
        ),

        fontSize = 20.sp,

        fontWeight = FontWeight.Bold,

        color = DarkBrown
    )
}


  //  LESSON TEXT

@Composable
fun LessonText(
    text: String
) {

    Text(
        text = text,

        modifier = Modifier.padding(
            bottom = 12.dp
        ),

        fontSize = 15.sp,

        color = Brown,

        lineHeight = 23.sp
    )
}


  //  SUBSCRIPTION SCREEN

@Composable
fun SubscriptionScreen(
    onSubscriptionComplete: () -> Unit,
    onBack: () -> Unit
) {

    val context = LocalContext.current


    var monthlyPrice by remember {

        mutableStateOf(
            "Loading price..."
        )
    }


    var yearlyPrice by remember {

        mutableStateOf(
            "Loading price..."
        )
    }


    var selectedPlan by remember {

        mutableStateOf(
            "monthly"
        )
    }


    var showSuccessMessage by remember {

        mutableStateOf(
            false
        )
    }


    /*
     * FREE LOCAL DEMO BILLING
     */

    val billingManager = remember {

        BillingManager(

            context = context,

            onProductsLoaded = { monthly, yearly ->

                monthlyPrice = monthly
                yearlyPrice = yearly
            },

            onPurchaseCompleted = { transaction ->

                /*
                 * Save transaction locally
                 */

                val transactionManager =
                    TransactionManager(context)

                transactionManager.saveTransaction(
                    transaction
                )

                showSuccessMessage = true
            },

            onSubscriptionStatusChanged = { subscribed ->

                if (subscribed) {

                    onSubscriptionComplete()
                }
            }
        )
    }


    DisposableEffect(Unit) {

        onDispose {

            billingManager.endConnection()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightCream)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        BackButton(
            onBack = onBack
        )


        Spacer(
            modifier = Modifier.height(20.dp)
        )


        Text(
            text = "Premium Subscription",

            fontSize = 28.sp,

            fontWeight = FontWeight.Bold,

            color = DarkBrown
        )


        Spacer(
            modifier = Modifier.height(8.dp)
        )


        Text(
            text = "Unlock all premium learning modules",

            fontSize = 16.sp,

            color = Brown
        )


        Spacer(
            modifier = Modifier.height(24.dp)
        )


        /*
         * DEMO NOTICE
         */

        Card(
            modifier = Modifier.fillMaxWidth(),

            colors = CardDefaults.cardColors(
                containerColor = LightOrange
            ),

            shape = RoundedCornerShape(14.dp)
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = "Free Demo Subscription",

                    fontSize = 17.sp,

                    fontWeight = FontWeight.Bold,

                    color = Brown
                )


                Spacer(
                    modifier = Modifier.height(6.dp)
                )


                Text(
                    text = "This is a local demo payment system for development and testing. No real money will be charged.",

                    fontSize = 14.sp,

                    color = DarkBrown,

                    lineHeight = 21.sp
                )
            }
        }


        Spacer(
            modifier = Modifier.height(24.dp)
        )


        /*
         * MONTHLY PLAN
         */

        PlanCard(
            title = "Monthly Plan",

            price = monthlyPrice,

            description = "Premium access for one month",

            selected = selectedPlan == "monthly",

            onClick = {

                selectedPlan = "monthly"
            }
        )


        Spacer(
            modifier = Modifier.height(16.dp)
        )


        /*
         * YEARLY PLAN
         */

        PlanCard(
            title = "Yearly Plan",

            price = yearlyPrice,

            description = "Premium access for one year",

            selected = selectedPlan == "yearly",

            onClick = {

                selectedPlan = "yearly"
            }
        )


        Spacer(
            modifier = Modifier.height(28.dp)
        )


        /*
         * CONTINUE BUTTON
         */

        Button(
            onClick = {

                if (selectedPlan == "monthly") {

                    billingManager.launchMonthlyPurchase()

                } else {

                    billingManager.launchYearlyPurchase()
                }
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = OrangeBrown
            ),

            shape = RoundedCornerShape(14.dp)
        ) {

            Text(
                text = "Continue",

                fontSize = 17.sp,

                fontWeight = FontWeight.Bold,

                color = White
            )
        }


        Spacer(
            modifier = Modifier.height(16.dp)
        )


        /*
         * SUCCESS MESSAGE
         */

        if (showSuccessMessage) {

            Card(
                modifier = Modifier.fillMaxWidth(),

                colors = CardDefaults.cardColors(
                    containerColor = SuccessGreen
                ),

                shape = RoundedCornerShape(14.dp)
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = "Subscription Activated!",

                        fontSize = 17.sp,

                        fontWeight = FontWeight.Bold,

                        color = DarkBrown
                    )


                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )


                    Text(
                        text = "Demo purchase completed successfully. Your transaction has been saved and premium modules are now unlocked.",

                        fontSize = 14.sp,

                        color = DarkBrown,

                        lineHeight = 21.sp
                    )
                }
            }
        }
    }
}


  //  PLAN CARD

@Composable
fun PlanCard(
    title: String,
    price: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(16.dp),

        colors = CardDefaults.cardColors(
            containerColor =
                if (selected) {
                    LightOrange
                } else {
                    White
                }
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.SpaceBetween,

                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = title,

                    fontSize = 20.sp,

                    fontWeight = FontWeight.Bold,

                    color = DarkBrown
                )


                if (selected) {

                    Text(
                        text = "SELECTED",

                        fontSize = 12.sp,

                        fontWeight = FontWeight.Bold,

                        color = OrangeBrown
                    )
                }
            }


            Spacer(
                modifier = Modifier.height(10.dp)
            )


            Text(
                text = price,

                fontSize = 22.sp,

                fontWeight = FontWeight.Bold,

                color = OrangeBrown
            )


            Spacer(
                modifier = Modifier.height(6.dp)
            )


            Text(
                text = description,

                fontSize = 14.sp,

                color = Brown
            )
        }
    }
}


   // TRANSACTION HISTORY SCREEN

@Composable
fun TransactionHistoryScreen(
    onBack: () -> Unit
) {

    val context = LocalContext.current

    var transactions by remember {

        mutableStateOf(
            TransactionManager(context)
                .getTransactions()
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightCream)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        BackButton(
            onBack = onBack
        )


        Spacer(
            modifier = Modifier.height(20.dp)
        )


        Text(
            text = "Payment History",

            fontSize = 28.sp,

            fontWeight = FontWeight.Bold,

            color = DarkBrown
        )


        Spacer(
            modifier = Modifier.height(8.dp)
        )


        Text(
            text = "Your saved subscription transactions",

            fontSize = 16.sp,

            color = Brown
        )


        Spacer(
            modifier = Modifier.height(24.dp)
        )


        if (transactions.isEmpty()) {

            Card(
                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(16.dp),

                colors = CardDefaults.cardColors(
                    containerColor = White
                )
            ) {

                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    Text(
                        text = "No Transactions Yet",

                        fontSize = 20.sp,

                        fontWeight = FontWeight.Bold,

                        color = DarkBrown
                    )


                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )


                    Text(
                        text = "Your completed demo subscription purchases will appear here.",

                        fontSize = 14.sp,

                        color = Brown,

                        lineHeight = 21.sp
                    )
                }
            }

        } else {

            /*
             * Newest transaction first
             */

            transactions
                .asReversed()
                .forEach { transaction ->

                    TransactionCard(
                        transaction = transaction
                    )


                    Spacer(
                        modifier = Modifier.height(14.dp)
                    )
                }


            Spacer(
                modifier = Modifier.height(10.dp)
            )


            /*
             * Clear history
             */

            Button(
                onClick = {

                    TransactionManager(context)
                        .clearTransactions()

                    transactions = emptyList<Transaction>().toMutableList()
                },

                modifier = Modifier.fillMaxWidth(),

                colors = ButtonDefaults.buttonColors(
                    containerColor = Brown
                ),

                shape = RoundedCornerShape(12.dp)
            ) {

                Text(
                    text = "Clear Payment History",

                    color = White,

                    fontWeight = FontWeight.Bold
                )
            }
        }


        Spacer(
            modifier = Modifier.height(30.dp)
        )
    }
}


   // TRANSACTION CARD

@Composable
fun TransactionCard(
    transaction: Transaction
) {

    val context = LocalContext.current

    var receiptFile by remember {
        mutableStateOf<File?>(null)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(16.dp),

        colors = CardDefaults.cardColors(
            containerColor = White
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.SpaceBetween,

                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = transaction.plan + " Plan",

                    fontSize = 19.sp,

                    fontWeight = FontWeight.Bold,

                    color = DarkBrown
                )


                Text(
                    text = transaction.status,

                    fontSize = 12.sp,

                    fontWeight = FontWeight.Bold,

                    color = Color(0xFF287A35)
                )
            }


            Spacer(
                modifier = Modifier.height(12.dp)
            )


            TransactionDetail(
                label = "Amount",

                value = transaction.amount
            )


            Spacer(
                modifier = Modifier.height(6.dp)
            )


            TransactionDetail(
                label = "Transaction ID",

                value = transaction.transactionId
            )


            Spacer(
                modifier = Modifier.height(6.dp)
            )


            TransactionDetail(
                label = "Date",

                value = transaction.date
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Button(
                onClick = {
                    try {
                        receiptFile = ReceiptGenerator.generateReceipt(
                            context = context,
                            transaction = transaction
                        )

                        Toast.makeText(
                            context,
                            "Receipt generated successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            "Failed to generate receipt",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = OrangeBrown
                )
            ) {
                Text(
                    text = "Generate Receipt",
                    color = White
                )
            }

            if (receiptFile != null) {

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Button(
                    onClick = {
                        openReceipt(
                            context = context,
                            file = receiptFile!!
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Brown
                    )
                ) {
                    Text(
                        text = "View Receipt",
                        color = White
                    )
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                androidx.compose.material3.OutlinedButton(
                    onClick = {
                        shareReceipt(
                            context = context,
                            file = receiptFile!!
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Share Receipt",
                        color = Brown
                    )
                }
            }
        }
    }
}


  //  TRANSACTION DETAIL

@Composable
fun TransactionDetail(
    label: String,
    value: String
) {

    Column {

        Text(
            text = label,

            fontSize = 12.sp,

            fontWeight = FontWeight.Bold,

            color = Brown
        )


        Spacer(
            modifier = Modifier.height(2.dp)
        )


        Text(
            text = value,

            fontSize = 14.sp,

            color = DarkBrown
        )
    }
}


    // BACK BUTTON


@Composable
fun BackButton(
    onBack: () -> Unit
) {

    Row(
        modifier = Modifier
            .clickable {
                onBack()
            }
            .padding(vertical = 6.dp),

        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "←",

            fontSize = 25.sp,

            fontWeight = FontWeight.Bold,

            color = DarkBrown
        )


        Spacer(
            modifier = Modifier.width(8.dp)
        )


        Text(
            text = "Back",

            fontSize = 16.sp,

            color = DarkBrown
        )
    }
}

   // RECEIPT VIEW / SHARE HELPERS


fun openReceipt(
    context: Context,
    file: File
) {

    try {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(intent)

    } catch (e: Exception) {
        Toast.makeText(
            context,
            "No PDF viewer found",
            Toast.LENGTH_SHORT
        ).show()
    }
}

fun shareReceipt(
    context: Context,
    file: File
) {

    try {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(
            Intent.createChooser(shareIntent, "Share Receipt")
        )

    } catch (e: Exception) {
        Toast.makeText(
            context,
            "Unable to share receipt",
            Toast.LENGTH_SHORT
        ).show()

        fun openSubscriptionManagement(context: Context) {

            try {

                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        "https://play.google.com/store/account/subscriptions"
                    )
                )

                context.startActivity(intent)

            } catch (e: Exception) {

                Toast.makeText(
                    context,
                    "Unable to open subscription management",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

