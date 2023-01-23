package com.example.test_task.ui.screens.selected_bin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.test_task.Arguments
import com.example.test_task.R
import com.example.test_task.data.DataRepository
import com.example.test_task.goByLink
import com.example.test_task.goByUri
import com.example.test_task.ui.screens.search.SearchScreen
import com.example.test_task.use_cases.GetDataByBinUseCase

const val MAPS_LINK = "geo:"
const val PHONE_LINK = "tel:"
const val COORDINATES_DIVIDER = ","

private class MyViewModelFactory(private val bin: String) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SelectedBinViewModel(
            bin,
            //можно сделать с DI
            GetDataByBinUseCase(
                DataRepository()
            )
        ) as T
    }
}

@Composable
fun SelectedBinScreen(
    navController: NavController,
    viewModel: SelectedBinViewModel = viewModel(
        factory = MyViewModelFactory(
            navController.currentBackStackEntry?.arguments?.getString(
                "${Arguments.BIN}",
                ""
            ) ?: ""
        )
    )
) {
    val selectedBinViewState: SelectedBinViewState by viewModel.viewState.collectAsState()

    when (selectedBinViewState.state) {
        SelectedBinViewState.State.SUCCESS -> {
            SelectedBinScreenSuccess(
                selectedBinViewState = selectedBinViewState,
                navController = navController
            )
        }
        SelectedBinViewState.State.ERROR -> {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = null,
                    )
                }
                Text(
                    text = stringResource(id = R.string.selected_bin_error),
                    color = Color.Red,
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        SelectedBinViewState.State.LOADING -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }

}

@Composable
fun SelectedBinScreenSuccess(
    selectedBinViewState: SelectedBinViewState,
    navController: NavController? = null
) {
    val context = LocalContext.current as Activity
    Column {
        IconButton(onClick = { navController?.popBackStack() }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = null,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {

            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(
                    id = R.string.selected_bin_scheme,
                    selectedBinViewState.scheme
                )
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(id = R.string.selected_bin_type, selectedBinViewState.type)
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(id = R.string.selected_bin_brand, selectedBinViewState.brand)
            )

            if (selectedBinViewState.prepaid) {
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = stringResource(id = R.string.selected_bin_prepaid_yes)
                )
            } else {
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = stringResource(id = R.string.selected_bin_prepaid_no)
                )
            }
            //4571 7360
            Text(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable {
                        val uri =
                            Uri.parse(MAPS_LINK + selectedBinViewState.country.latitude.toFloat() + COORDINATES_DIVIDER + selectedBinViewState.country.longitude.toFloat())
                        goByUri(uri, Intent.ACTION_VIEW, context)

                    },
                text = stringResource(
                    id = R.string.selected_bin_country,
                    selectedBinViewState.country.name
                ),
                color = Color.Blue
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(
                    id = R.string.selected_bin_name,
                    selectedBinViewState.bank.name
                )
            )
            Text(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable {
                        goByLink(selectedBinViewState.bank.url, context)
                    },
                text = stringResource(id = R.string.selected_bin_url, selectedBinViewState.bank.url),
                color = Color.Blue
            )
            Text(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable {
                        val uri = Uri.parse(PHONE_LINK + selectedBinViewState.bank.phone)
                        goByUri(uri, Intent.ACTION_DIAL, context)
                    },
                text = stringResource(
                    id = R.string.selected_bin_phone,
                    selectedBinViewState.bank.phone
                ),
                color = Color.Blue
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(
                    id = R.string.selected_bin_city,
                    selectedBinViewState.bank.city
                )
            )
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun SelectedBinScreenSuccessPreview() {
    SelectedBinScreenSuccess(
        selectedBinViewState = SelectedBinViewState(
            scheme = "scheme",
            type = "type",
            brand = "brand",
            prepaid = true,
            country = SelectedBinViewState.Country(
                name = "USA",
                latitude = 56,
                longitude = 10
            ),
            bank = SelectedBinViewState.Bank(
                name = "alfabank",
                url = "-----",
                phone = "89999999999",
                city = "LA"
            )
        )
    )
}