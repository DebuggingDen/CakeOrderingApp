package com.dden.cakeorderingapp.test

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.dden.cakeorderingapp.CakeApp
import com.dden.cakeorderingapp.CakeScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CupcakeScreenNavigationTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    lateinit var navController: TestNavHostController

    @Before
    fun setupCakeNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            CakeApp(navController = navController)
        }
    }
    @Test
    fun cakeNavHost_verifyStartDestination(){
        navController.assertCurrentRouteName(CakeScreen.Start.name)
    }
    @Test
    fun cakeNavHost_verifyBackNavigationNotShownOnStartOrderScreen(){
        val backText = composeTestRule.activity.getString(com.dden.cakeorderingapp.R.string.back_button)
        composeTestRule.onNodeWithContentDescription(backText).assertDoesNotExist()
    }
    @Test
    fun cakeNavHost_clickOneCake_navigatesToSelectFlavourScreen(){
        composeTestRule.onNodeWithStringId(com.dden.cakeorderingapp.R.string.one_cake)
            .performClick()
        navController.assertCurrentRouteName(CakeScreen.Flavour.name )
    }
    @Test
    fun cakeNavHost_clickNextOnFlavourScreen_navigatesToPickupScreen(){
        navigateToFlavourScreen()
        composeTestRule.onNodeWithStringId(com.dden.cakeorderingapp.R.string.next)
            .performClick()
        navController.assertCurrentRouteName(CakeScreen.Pickup.name)
    }
    @Test
    fun cakeNavHost_clickBackOnFlavourScreen_navigatesToStartOrderScreen(){
        navigateToFlavourScreen()
        performNavigateUp()
        navController.assertCurrentRouteName(CakeScreen.Flavour.name)
    }
    @Test
    fun cupcakeNavHost_clickCancelOnFlavorScreen_navigatesToStartOrderScreen() {
        navigateToFlavourScreen()
        composeTestRule.onNodeWithStringId(com.dden.cakeorderingapp.R.string.cancel)
            .performClick()
        navController.assertCurrentRouteName(CakeScreen.Start.name)
    }

    @Test
    fun cupcakeNavHost_clickNextOnPickupScreen_navigatesToSummaryScreen() {
        navigateToPickupScreen()
        composeTestRule.onNodeWithText(getFormattedDate())
            .performClick()
        composeTestRule.onNodeWithStringId(com.dden.cakeorderingapp.R.string.next)
            .performClick()
        navController.assertCurrentRouteName(CakeScreen.Summary.name)
    }

    @Test
    fun cupcakeNavHost_clickBackOnPickupScreen_navigatesToFlavorScreen() {
        navigateToPickupScreen()
        performNavigateUp()
        navController.assertCurrentRouteName(CakeScreen.Flavour.name)
    }

    @Test
    fun cupcakeNavHost_clickCancelOnPickupScreen_navigatesToStartOrderScreen() {
        navigateToPickupScreen()
        composeTestRule.onNodeWithStringId(com.dden.cakeorderingapp.R.string.cancel)
            .performClick()
        navController.assertCurrentRouteName(CakeScreen.Start.name)
    }

    @Test
    fun cupcakeNavHost_clickCancelOnSummaryScreen_navigatesToStartOrderScreen() {
        navigateToSummaryScreen()
        composeTestRule.onNodeWithStringId(com.dden.cakeorderingapp.R.string.cancel)
            .performClick()
        navController.assertCurrentRouteName(CakeScreen.Start.name)
    }
    fun navigateToFlavourScreen(){
        composeTestRule.onNodeWithStringId(com.dden.cakeorderingapp.R.string.one_cake)
            .performClick()
        composeTestRule.onNodeWithStringId(com.dden.cakeorderingapp.R.string.chocolate)
            .performClick()
    }
    private fun getFormattedDate():String{
        val calendar = Calendar.getInstance()
        calendar.add(java.util.Calendar.DATE,1)
        val formatter = SimpleDateFormat("E MM d", Locale.getDefault())
        return formatter.format(calendar.time)
    }
    private fun navigateToPickupScreen() {
        navigateToFlavourScreen()
        composeTestRule.onNodeWithStringId(com.dden.cakeorderingapp.R.string.next)
            .performClick()
    }

    private fun navigateToSummaryScreen() {
        navigateToPickupScreen()
        composeTestRule.onNodeWithText(getFormattedDate())
            .performClick()
        composeTestRule.onNodeWithStringId(com.dden.cakeorderingapp.R.string.next)
            .performClick()
    }

    private fun performNavigateUp() {
        val backText = composeTestRule.activity.getString(com.dden.cakeorderingapp.R.string.back_button)
        composeTestRule.onNodeWithContentDescription(backText).performClick()
    }
}
