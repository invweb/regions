package com.home.regions

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.home.regions.theme.RegionsComposeTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import com.home.regions.data.Region
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState

class MainActivity : ComponentActivity() {
    private var back: Boolean = false
    private val viewModel: MainViewModel by viewModels()
    private var content: Region? = null
    private val ctx: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setContent {
            val navController = rememberNavController()
            RegionsComposeTheme {

                Surface(color = MaterialTheme.colors.background) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(text = getString(R.string.app_name))
                                },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        if (back) {
                                            navController.popBackStack()
                                            back = false
                                        }
                                    }) {
                                        Icon(Icons.Filled.ArrowBack, "")
                                    }
                                },
                                backgroundColor = Color.Blue,
                                contentColor = Color.White,
                                elevation = 12.dp
                            )

                        }, content = {
                            NavHost(
                                navController = navController,
                                startDestination = "regions_composable"
                            ) {
                                composable("item_composable") {
                                    ItemComposable()
                                }
                                composable("regions_composable") {
                                    TableComposable(
                                        navController,
                                        viewModel.obtainRegions(
                                            this@MainActivity,
                                            lifecycleScope
                                        )
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    @Composable
    private fun TableComposable(
        navController: NavHostController,
        regions: LiveData<List<Region?>>
    ) {
        back = false
        val regionItems: List<Region?> by regions.observeAsState(listOf())
        LazyColumn(
            modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 0.dp)
        ) {
            items(items = regionItems, itemContent = { region ->
                Column(
                    modifier = Modifier.clickable(
                        onClick = {
                            Toast.makeText(ctx, "click", Toast.LENGTH_LONG).show()

                            content = region
                            navController.navigate("item_composable")
                        }
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(0.dp, 0.dp, 0.dp, 0.dp)
                    ) {
                        TitleComposable(regionTitle = region!!.region)
                    }
                    Row {
                        Divider(
                            color = Color.Black,
                            thickness = 1.dp
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(0.dp, 0.dp, 0.dp, 0.dp)
                    ) {
                        CityComposable(city = region!!.city)
                    }
                    Row {
                        Divider(
                            color = Color.Blue,
                            thickness = 2.dp
                        )
                    }
                }
            })
        }
    }

    @Composable
    fun CityComposable(city: String) {
        Text(
            text = getString(R.string.city)
                .plus(" ")
                .plus(city),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
    }

    @Composable
    fun TitleComposable(regionTitle: String) {
        Text(
            text = getString(R.string.region)
                .plus(" ")
                .plus(regionTitle),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
    }

    @Composable
    private fun ItemComposable() {
        back = true
        Scaffold(modifier = Modifier.padding(16.dp), content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    style = MaterialTheme.typography.h6,
                    text = content!!.city
                )
                Text(text = content!!.region)
            }
        })
    }
}