package com.dden.cakeorderingapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dden.cakeorderingapp.R
import com.dden.cakeorderingapp.data.OrderUiState
import com.dden.cakeorderingapp.ui.components.FormattedPriceLabel
import com.dden.cakeorderingapp.ui.theme.CakeOrderingAppTheme


@Composable
fun OrderSummaryScreen(
  orderUiState: OrderUiState,
  onSendButtonClicked: (String,String) -> Unit,
  onCancelButtonClicked: () -> Unit,
  modifier: Modifier = Modifier
){
  val resources = LocalContext.current.resources

  val numberOfCakes = resources.getQuantityString(
    R.plurals.cakes,
    orderUiState.quantity,
    orderUiState.quantity
  )
  //Load and format a string resource with the parameters.
  val orderSummary = stringResource(
    R.string.order_deatails,
    numberOfCakes,
    orderUiState.flavour,
    orderUiState.date,
    orderUiState.price
  )
  val newOrder = stringResource(id = R.string.new_cake_order)
  //Create a list of order summary to display
  val items = listOf(
    // Summary line 1: display selected quantity
    Pair(stringResource(id = R.string.quantity),numberOfCakes),
    // Summary line 2: display selected flavor
    Pair(stringResource(id = R.string.flavor), orderUiState.flavour),
    // Summary line 3: display selected pickup date
    Pair(stringResource(id = R.string.pickup_date),orderUiState.date)
  )
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    Column(
      modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)),
      verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
      items.forEach{ item ->
        Text(text = item.first.uppercase())
        Text(text = item.second, fontWeight = FontWeight.Bold)
        HorizontalDivider()
      }
      FormattedPriceLabel(subtotal = orderUiState.price,modifier = Modifier.align(Alignment.End))
    }
    Column(
      modifier = Modifier.padding(20.dp),
      verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
    ) {
      Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onSendButtonClicked(newOrder, orderSummary)}
      ) {
        Text(stringResource(R.string.send))
      }
      OutlinedButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = onCancelButtonClicked
      ) {
        Text(stringResource(R.string.cancel))
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun OrderSummaryPreview() {
  CakeOrderingAppTheme {
    OrderSummaryScreen(
      orderUiState = OrderUiState(0, "Test", "Test", "$300.00"),
      onSendButtonClicked = { subject: String, summary: String -> },
      onCancelButtonClicked = {},
      modifier = Modifier.fillMaxHeight()
    )
  }
}
