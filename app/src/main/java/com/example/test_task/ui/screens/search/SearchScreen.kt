package com.example.test_task.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.test_task.BIN_LENGTH
import com.example.test_task.Destinations
import com.example.test_task.R
import com.example.test_task.data.BinRepository
import com.example.test_task.data.RoomDB
import com.example.test_task.use_cases.GetBinListUseCase
import com.example.test_task.use_cases.InsertBinUseCase

private class SearchViewModelFactory(private val db: RoomDB) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(
            //можно сделать с DI
            insertBinUseCase = InsertBinUseCase(
                binRepository = BinRepository(
                    db.binDao()
                )
            ),
            getBinListUseCase = GetBinListUseCase(
                binRepository = BinRepository(
                    db.binDao()
                )
            ),
        ) as T
    }
}

@Composable
fun SearchScreen(
    navController: NavController,
    roomDB: RoomDB,
    viewModel: SearchViewModel = viewModel(
        factory = SearchViewModelFactory(roomDB)
    )
) {
    viewModel.update()
    val selectedBinViewState: SearchViewState by viewModel.viewState.collectAsState()
    handleSearchViewStateEvents(
        navController = navController,
        events = selectedBinViewState.events,
        viewModel = viewModel
    )
    SearchScreenSuccess(
        searchViewState = selectedBinViewState,
        action = {
            viewModel.checkBin(it)
        }
    )
}

private fun handleSearchViewStateEvents(
    navController: NavController? = null,
    events: List<SearchViewState.Event>,
    viewModel: SearchViewModel
) {
    events.forEach { event ->
        when (event) {
            is SearchViewState.Event.GoToSelectedBin -> {
                navController?.navigate("${Destinations.SELECTED_BIN}/${event.bin}")
            }
        }
    }
    viewModel.consumeEvents(events)
}

@Composable
fun SearchScreenSuccess(
    searchViewState: SearchViewState,
    action: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        var text by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(
                TextFieldValue(
                    ""
                )
            )
        }
        Column(
            modifier = Modifier
                .align(CenterHorizontally)
        ) {
            TextField(
                modifier = Modifier
                    .padding(top = 8.dp),
                value = text,
                onValueChange = { bin ->
                    if (bin.text.length <= BIN_LENGTH) {
                        text = bin
                    }
                },
                isError = searchViewState.hasError,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.search_bin_enter),
                    )
                },
            )

            if (searchViewState.hasError) {
                Text(
                    text = stringResource(id = R.string.search_bin_enter_correct),
                    color = Color.Red,
                    modifier = Modifier
                )
            }
        }
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(searchViewState.binList) { bin ->
                Text(
                    text = bin,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp)
                        .clickable { action.invoke(bin) },
                    fontSize = 16.sp
                )
            }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onClick = {
                action.invoke(text.text)
            }) {
            Text(text = stringResource(id = R.string.search_bin_search))
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SearchScreenPreview() {
    SearchScreenSuccess(
        searchViewState = SearchViewState()
    ) {

    }
}