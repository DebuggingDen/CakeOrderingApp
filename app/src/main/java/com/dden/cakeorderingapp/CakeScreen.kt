package com.dden.cakeorderingapp

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.dden.cakeorderingapp.ui.OrderViewModel
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dden.cakeorderingapp.data.DataSource
import com.dden.cakeorderingapp.ui.OrderSummaryScreen
import com.dden.cakeorderingapp.ui.SelectOptionScreen
import com.dden.cakeorderingapp.ui.StartOrderingScreen

enum class CakeScreen(@StringRes val title: Int){
    Start(title = R.string.app_name),
    Flavour(title = (R.string.choose_flavour)),
    Pickup(title = (R.string.choose_pickup_date)),
    Summary(title = (R.string.order_summary))
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CakeAppBar(
    currentScreen: CakeScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
){
    TopAppBar(
        title = { Text(text = stringResource(currentScreen.title))},
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if(canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_button))
                }
            }
        }
    )
}
@Composable
fun CakeApp(
    viewModel: OrderViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navController: NavHostController = rememberNavController(),
){
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = CakeScreen.valueOf(
        backStackEntry?.destination?.route ?: CakeScreen.Start.name
    )
    Scaffold (
        topBar = {
            CakeAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = {navController.navigateUp()}
            )
        }
    ){ innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = CakeScreen.Start.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = CakeScreen.Start.name) {
                StartOrderingScreen(
                    quantityOptions = DataSource.quantityOptions,
                    onNextButtonClicked = {
                        viewModel.setQuantity(it)
                        navController.navigate(CakeScreen.Flavour.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(id = R.dimen.padding_medium)))
            }
            composable(route = CakeScreen.Flavour.name){
                val context = LocalContext.current
                SelectOptionScreen(
                    subtotal = uiState.price,
                    onNextButtonClicked = {navController.navigate(CakeScreen.Pickup.name)},
                    onCancelButtonClicked = {
                        navController.popBackStack(CakeScreen.Start.name,inclusive = false)
                    },
                    options = DataSource.flavors.map { id -> context.resources.getString(id) },
                    onSelectionChanged = { viewModel.setFlavor(it)},
                    modifier = Modifier.fillMaxSize()
                )
            }
            composable(route = CakeScreen.Pickup.name){
                SelectOptionScreen(
                    subtotal = uiState.price,
                    onNextButtonClicked = { navController.navigate(CakeScreen.Summary.name)},
                    onCancelButtonClicked = {
                        cancelOrderNavigationToStart(viewModel,navController)
                    },
                    options = uiState.pickupOptions,
                    onSelectionChanged = {viewModel.setDate(it)},
                    modifier = Modifier.fillMaxSize())
            }
            composable(route = CakeScreen.Summary.name){
                val context = LocalContext.current
                OrderSummaryScreen(
                    orderUiState = uiState,
                    onSendButtonClicked = { subject: String, summary: String ->
                        shareOrder(context, subject = subject,summary = summary)
                    },
                    onCancelButtonClicked = {cancelOrderNavigationToStart(viewModel,navController)},
                    modifier = Modifier.fillMaxSize())
            }
        }
    }
}

//CANCEL DIRECT TO STARTING SCREEN
private fun cancelOrderNavigationToStart(
    viewModel: OrderViewModel,
    navController: NavHostController
){
    viewModel.resetOrder()
    //also can be used for single screen Cancellation
    navController.popBackStack(CakeScreen.Start.name,inclusive = false)
}
private fun shareOrder(context: Context, subject: String, summary: String){
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, summary)
    }
    context.startActivity(
        Intent.createChooser(
            intent,
            context.getString(R.string.new_cake_order)
        )
    )
}
