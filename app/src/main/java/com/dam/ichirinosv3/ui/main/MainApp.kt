package com.dam.ichirinosv3.ui.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dam.ichirinosv3.R
import com.dam.ichirinosv3.ui.screens.armarios.ArmariosBus
import com.dam.ichirinosv3.ui.screens.armarios.ArmariosMto
import com.dam.ichirinosv3.ui.screens.armarios.ArmariosVM
import com.dam.ichirinosv3.ui.screens.aulas.AulasBus
import com.dam.ichirinosv3.ui.screens.aulas.AulasFiltro
import com.dam.ichirinosv3.ui.screens.aulas.AulasMto
import com.dam.ichirinosv3.ui.screens.aulas.AulasVM
import com.dam.ichirinosv3.ui.screens.dptos.DptosBus
import com.dam.ichirinosv3.ui.screens.dptos.DptosMto
import com.dam.ichirinosv3.ui.screens.dptos.DptosVM
import com.dam.ichirinosv3.ui.screens.productos.ProductosBus
import com.dam.ichirinosv3.ui.screens.productos.ProductosFiltro
import com.dam.ichirinosv3.ui.screens.productos.ProductosMto
import com.dam.ichirinosv3.ui.screens.productos.ProductosVM
import com.dam.ichirinosv3.ui.screens.productosNS.ProductosNSBus
import com.dam.ichirinosv3.ui.screens.productosNS.ProductosNSMto
import com.dam.ichirinosv3.ui.screens.productosNS.ProductosNSVM
import com.dam.ichirinosv3.ui.theme.IChirinosv3Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MainApp(
    mainVM: MainVM,
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = AppScreens.valueOf(backStackEntry?.destination?.route ?: AppScreens.Home.name)

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val dptosVM: DptosVM = viewModel(factory = DptosVM.Factory)
    val aulasVM: AulasVM = viewModel(factory = AulasVM.Factory)
    val armariosVM: ArmariosVM = viewModel(factory = ArmariosVM.Factory)
    val productosVM: ProductosVM = viewModel(factory = ProductosVM.Factory)
    val productosNSVM: ProductosNSVM = viewModel(factory = ProductosNSVM.Factory)

    val uiMainState = mainVM.uiMainState

    var topScreenSelected by rememberSaveable { mutableStateOf(AppScreens.Splash) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = modifier,
        drawerContent = {
            DynamicDrawerSheet(
                mainVM = mainVM,
                topScreenSelected = topScreenSelected,
                onTopScreenSelectedChange = { onNavTopScreen(
                    mainVM,
                    dptosVM,
                    aulasVM,
                    armariosVM,
                    productosVM,
                    productosNSVM,
                    it,
                    navController,
                    scope,
                    drawerState
                ) },
                modifier = modifier
            )
        }
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                MainTopBar(
                    uiMainState = uiMainState,
                    topScreenSelected = currentScreen,
                    onDrawerClick = { scope.launch { drawerState.open() } },
                    onNavHome = {
                        navController.navigate(AppScreens.Home.name) {
                            popUpTo(AppScreens.Splash.name) {
                                inclusive = true
                            }
                        }
                    },
                    onNavUp = { navController.navigateUp() },
                    onNavFiltroAulas = {
                        onNavTopScreen(
                            mainVM,
                            dptosVM,
                            aulasVM,
                            armariosVM,
                            productosVM,
                            productosNSVM,
                            AppScreens.AulasFiltro,
                            navController,
                            scope,
                            drawerState
                        )
                    },
                    onNavFiltroProductos = {
                        onNavTopScreen(
                            mainVM,
                            dptosVM,
                            aulasVM,
                            armariosVM,
                            productosVM,
                            productosNSVM,
                            AppScreens.ProductosFiltro,
                            navController,
                            scope,
                            drawerState
                        )
                    },
                    onMenuLoginClick = {
                        if (uiMainState.login == null) mainVM.setShowLogin(true)
                        else mainVM.setLogout()
                    },
                    onMenuPrefsClick = { mainVM.setShowPrefs(true) },
                    onDlgConfirmacionSalir = { mainVM.setShowDlgSalir(true) },
                    windowSize = windowSize,
                    modifier = modifier
                )
            },
            bottomBar = {
                if (currentScreen == AppScreens.Home && windowSize.equals(WindowWidthSizeClass.Compact)) {
                    MainBottomBar(
                        uiMainState = mainVM.uiMainState,
                        topScreenSelected = topScreenSelected,
                        onTopScreenSelectedChange = {
                            onNavTopScreen(
                                mainVM,
                                dptosVM,
                                aulasVM,
                                armariosVM,
                                productosVM,
                                productosNSVM,
                                it,
                                navController,
                                scope,
                                drawerState
                            )
                        },
                        modifier = modifier
                    )
                }
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            floatingActionButton = {
                if (currentScreen != AppScreens.Home) {
                    FloatingActionButton(
                        onClick = { },
                        modifier = Modifier
                            .offset(y = (-64).dp),
                        containerColor = Color.Transparent,
                        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
                    ) { }
                }
            }
        ) { innerPadding ->

            NavHost(
                navController = navController,
                startDestination = AppScreens.Splash.name,
                modifier = modifier.padding(innerPadding)
            ) {
                composable(route = AppScreens.Splash.name) {
                    SplashScreen(
                        mainVM = mainVM,
                        onNavHome = { navController.navigate(AppScreens.Home.name) {
                            popUpTo(AppScreens.Splash.name) {
                                inclusive = true
                            }
                        }
                        },
                        modifier = modifier
                    )
                }

                composable(route = AppScreens.Home.name) {
                    HomeScreen(
                        mainVM = mainVM,
                        onNavLogin = { navController.navigate(AppScreens.Login.name) },
                        onNavPrefs = { navController.navigate(AppScreens.Prefs.name) },
                        modifier = modifier
                    )
                }

                composable(route = AppScreens.Login.name) {
                    LoginScreen(
                        mainVM = mainVM,
                        dptosVM = dptosVM,
                        onShowSnackbar = { scope.launch { snackbarHostState.showSnackbar(it) } },
                        onNavUp = { navController.navigateUp() },
                        modifier = modifier
                    )
                }

                composable(route = AppScreens.Prefs.name) {
                    PrefScreen(
                        mainVM = mainVM,
                        onShowSnackbar = { scope.launch { snackbarHostState.showSnackbar(it) } },
                        onNavUp = { navController.navigateUp() },
                        modifier = modifier
                    )
                }

                composable(route = AppScreens.DptosBus.name) {
                    DptosBus(
                        dptosVM = dptosVM,
                        onShowSnackbar = { scope.launch { snackbarHostState.showSnackbar(it) } },
                        onNavUp = { navController.navigateUp() },
                        onNavDown = {
                            mainVM.resetStates()
                            navController.navigate(AppScreens.DptosMto.name)
                        },
                        modifier = modifier
                    )
                }

                composable(route = AppScreens.DptosMto.name) {
                    DptosMto(
                        dptosVM = dptosVM,
                        onShowSnackbar = { scope.launch { snackbarHostState.showSnackbar(it) } },
                        onNavUp = { navController.navigateUp() },
                        modifier = modifier
                    )
                }

                composable(route = AppScreens.AulasBus.name) {
                    AulasBus(
                        mainVM = mainVM,
                        aulasVM = aulasVM,
                        onShowSnackbar = { scope.launch { snackbarHostState.showSnackbar(it) } },
                        onNavUp = { navController.navigateUp() },
                        onNavDown = { navController.navigate(AppScreens.AulasMto.name) },
                        onNavArmariosBus = {
                            mainVM.resetStates()
                            navController.navigate(AppScreens.ArmariosBus.name)
                        },
                        modifier = modifier
                    )
                }

                composable(route = AppScreens.AulasFiltro.name) {
                    AulasFiltro(
                        mainVM = mainVM,
                        dptosVM = dptosVM,
                        aulasVM = aulasVM,
                        onNavUp = { navController.navigateUp() },
                        modifier = modifier
                    )
                }

                composable(route = AppScreens.AulasMto.name) {
                    AulasMto(
                        aulasVM = aulasVM,
                        dptosVM = dptosVM,
                        onShowSnackbar = { scope.launch { snackbarHostState.showSnackbar(it) } },
                        onNavUp = { navController.navigateUp() },
                        modifier = modifier
                    )
                }

                composable(route = AppScreens.ArmariosBus.name) {
                    ArmariosBus(
                        mainVM = mainVM,
                        aulasVM = aulasVM,
                        armariosVM = armariosVM,
                        onShowSnackbar = { scope.launch { snackbarHostState.showSnackbar(it) } },
                        onNavUp = { navController.navigateUp() },
                        onNavDown = { navController.navigate(AppScreens.ArmariosMto.name) },
                        modifier = modifier
                    )
                }

                composable(route = AppScreens.ArmariosMto.name) {
                    ArmariosMto(
                        dptosVM = dptosVM,
                        aulasVM = aulasVM,
                        armariosVM = armariosVM,
                        onShowSnackbar = { scope.launch { snackbarHostState.showSnackbar(it) } },
                        onNavUp = { navController.navigateUp() },
                        modifier = modifier
                    )
                }

                composable(route = AppScreens.ProductosBus.name) {
                    ProductosBus(
                        mainVM = mainVM,
                        productosVM = productosVM,

                        onShowSnackbar = { scope.launch { snackbarHostState.showSnackbar(it) } },
                        onNavUp = { navController.navigateUp() },
                        onNavDown = { navController.navigate(AppScreens.ProductosMto.name) },
                        onNavProductosNS = { navController.navigate(AppScreens.ProductosNSBus.name) },
                        modifier = modifier
                    )
                }

                composable(route = AppScreens.ProductosFiltro.name) {
                    ProductosFiltro(
                        mainVM = mainVM,
                        dptosVM = dptosVM,
                        productosVM = productosVM,
                        aulasVM = aulasVM,
                        armariosVM = armariosVM,
                        onNavUp = { navController.navigateUp() },
                        modifier = modifier
                    )
                }

                composable(route = AppScreens.ProductosMto.name) {
                    ProductosMto(
                        dptosVM = dptosVM,
                        productosVM = productosVM,
                        aulasVM = aulasVM,
                        armariosVM = armariosVM,
                        onShowSnackbar = { scope.launch { snackbarHostState.showSnackbar(it) } },
                        onNavUp = { navController.navigateUp() },
                        modifier = modifier
                    )
                }

                composable(route = AppScreens.ProductosNSBus.name) {
                    ProductosNSBus(
                        mainVM = mainVM,
                        productosVM = productosVM,
                        productosNSVM = productosNSVM,
                        onShowSnackbar = { scope.launch { snackbarHostState.showSnackbar(it) } },
                        onNavUp = { navController.navigateUp() },
                        onNavDown = { navController.navigate(AppScreens.ProductosNSMto.name) },
                        modifier = modifier
                    )
                }

                composable(route = AppScreens.ProductosNSMto.name) {
                    ProductosNSMto(
                        dptosVM = dptosVM,
                        productosVM = productosVM,
                        productosNSVM = productosNSVM,
                        onShowSnackbar = { scope.launch { snackbarHostState.showSnackbar(it) } },
                        onNavUp = { navController.navigateUp() },
                        modifier = modifier
                    )
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    uiMainState: MainState,
    topScreenSelected: AppScreens,
    onDrawerClick: () -> Unit,
    onNavHome: () -> Unit,
    onNavUp: () -> Unit,
    onNavFiltroAulas: () -> Unit,
    onNavFiltroProductos: () -> Unit,
    onMenuLoginClick: () -> Unit,
    onMenuPrefsClick: () -> Unit,
    onDlgConfirmacionSalir: () -> Unit,
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    BackHandler {
        when (topScreenSelected) {
            AppScreens.Splash -> {
                onNavHome()
            }

            AppScreens.Home -> {
                onDlgConfirmacionSalir()
            }

            else -> {
                onNavUp()
            }
        }
    }

    CenterAlignedTopAppBar(
        title = {
            Text(text = stringResource(topScreenSelected.title))
        },
        modifier = modifier,
        navigationIcon = {
            if (topScreenSelected == AppScreens.Home && windowSize.equals(WindowWidthSizeClass.Expanded)) {
                IconButton(
                    onClick = { onDrawerClick() }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_menu_32dp),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
            } else if (topScreenSelected == AppScreens.Home) {
                IconButton(
                    onClick = { }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_menu_32dp),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
            } else if (topScreenSelected == AppScreens.Splash) {
                IconButton(
                    onClick = { onNavHome() }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back_32dp),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }

            } else {
                IconButton(
                    onClick = { onNavUp() }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrow_back_32dp),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        },
        actions = {
            when (topScreenSelected) {
                AppScreens.Home -> {
                    MainOptionMenu(
                        uiMainState = uiMainState,
                        onDlgConfirmacionSalir = { onDlgConfirmacionSalir() },
                        onMenuLoginClick = { onMenuLoginClick() },
                        onMenuPrefsClick = { onMenuPrefsClick() },
                        modifier = modifier
                    )
                }

                AppScreens.AulasBus -> {
                    IconButton(onClick = { onNavFiltroAulas() }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_filter_alt_32dp),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                AppScreens.ProductosBus -> {
                    IconButton(onClick = { onNavFiltroProductos() }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_filter_alt_32dp),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                else -> {}
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
private fun MainOptionMenu(
    uiMainState: MainState,
    onMenuLoginClick: () -> Unit,
    onMenuPrefsClick: () -> Unit,
    onDlgConfirmacionSalir: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showOptionsMenu by rememberSaveable { mutableStateOf(false) }

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        IconButton(onClick = { showOptionsMenu = !showOptionsMenu }) {
            Icon(
                painter = painterResource(R.drawable.ic_more_vert_32dp),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
        }
        DropdownMenu(
            expanded = showOptionsMenu,
            onDismissRequest = { showOptionsMenu = false }
        ) {
            if (uiMainState.login == null) {
                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.menu_login)) },
                    onClick = {
                        onMenuLoginClick()
                        showOptionsMenu = false
                    }
                )
            } else {
                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.menu_logout)) },
                    onClick = {
                        onMenuLoginClick()
                        showOptionsMenu = false
                    }
                )
            }
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.menu_prefs)) },
                onClick = {
                    onMenuPrefsClick()
                    showOptionsMenu = false
                }
            )
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.menu_salir)) },
                onClick = {
                    onDlgConfirmacionSalir()
                    showOptionsMenu = false
                }
            )
        }
    }
}

@Composable
fun MainBottomBar(
    uiMainState: MainState,
    topScreenSelected: AppScreens,
    onTopScreenSelectedChange: (AppScreens) -> Unit,
    modifier: Modifier = Modifier
) {

    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        NavigationBarItem(
            selected = topScreenSelected == AppScreens.Home,
            onClick = { onTopScreenSelectedChange(AppScreens.Home) },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_home_32dp),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            },
            label = { Text(text = stringResource(R.string.label_home)) }
        )
        NavigationBarItem(
            selected = topScreenSelected == AppScreens.DptosBus,
            onClick = { onTopScreenSelectedChange(AppScreens.DptosBus) },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_storefront_32dp),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            },
            enabled = uiMainState.login != null && uiMainState.login.id == 0,   // admin
            label = { Text(text = stringResource(R.string.label_dptos)) }
        )
        NavigationBarItem(
            selected = topScreenSelected == AppScreens.AulasBus,
            onClick = { onTopScreenSelectedChange(AppScreens.AulasBus) },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_store_32dp),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            },
            enabled = uiMainState.login != null, // && uiMainState.login.id == 0,   // admin
            label = { Text(text = stringResource(R.string.label_aulas)) }
        )
        NavigationBarItem(
            selected = topScreenSelected == AppScreens.ProductosBus,
            onClick = { onTopScreenSelectedChange(AppScreens.ProductosBus) },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_shoppingmode_32dp),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            },
            enabled = uiMainState.login != null, // && uiMainState.login.id == 0,   // admin
            label = { Text(text = stringResource(R.string.label_productos)) }
        )
    }
}

fun onNavTopScreen(
    mainVM: MainVM,
    dptosVM: DptosVM,
    aulasVM: AulasVM,
    armariosVM: ArmariosVM,
    productosVM: ProductosVM,
    productosNSVM: ProductosNSVM,
    topScreenSelected: AppScreens,
    navController: NavController,
    scope: CoroutineScope,
    drawerState: DrawerState
) {
    when (topScreenSelected) {
        AppScreens.Splash -> {
            mainVM.resetStates()
            navController.navigate(AppScreens.Splash.name)
            scope.launch { drawerState.close() }
        }
        AppScreens.Home -> {
            mainVM.resetStates()
            navController.navigate(AppScreens.Home.name);
            scope.launch { drawerState.close() }
        }
        AppScreens.Login -> {
            mainVM.resetStates()
            navController.navigate(AppScreens.Login.name)
            scope.launch { drawerState.close() }
        }
        AppScreens.Prefs -> {
            mainVM.resetStates()
            navController.navigate(AppScreens.Prefs.name)
            scope.launch { drawerState.close() }
        }
        AppScreens.DptosBus -> {
            mainVM.resetStates()
            dptosVM.resetBusState()
            navController.navigate(AppScreens.DptosBus.name)
            scope.launch { drawerState.close() }
        }
        AppScreens.DptosMto -> {
            mainVM.resetStates()
            navController.navigate(AppScreens.DptosBus.name)
            scope.launch { drawerState.close() }
        }
        AppScreens.AulasBus -> {
            mainVM.resetStates()
            aulasVM.resetBusState()
            aulasVM.resetStates()
            // Filtro
            val idFiltro = if (mainVM.uiMainState.login?.id == 0) "" else mainVM.uiMainState.login?.id.toString()
            aulasVM.setIdDptoFiltro(idFiltro)
            aulasVM.filtrarAulas()
            navController.navigate(AppScreens.AulasBus.name)
            scope.launch { drawerState.close() }
        }
        AppScreens.AulasFiltro -> {
            mainVM.resetStates()
            navController.navigate(AppScreens.AulasFiltro.name)
            scope.launch { drawerState.close() }
        }
        AppScreens.AulasMto -> {
            mainVM.resetStates()
            navController.navigate(AppScreens.AulasMto.name)
            scope.launch { drawerState.close() }
        }
        AppScreens.ArmariosBus -> {
            mainVM.resetStates()
            armariosVM.resetBusState()
            navController.navigate(AppScreens.ArmariosBus.name)
            scope.launch { drawerState.close() }
        }
        AppScreens.ArmariosMto -> {
            mainVM.resetStates()
            navController.navigate(AppScreens.ArmariosMto.name)
            scope.launch { drawerState.close() }
        }
        AppScreens.ProductosBus -> {
            mainVM.resetStates()
            productosVM.resetBusState()
            productosVM.resetStates()
            aulasVM.resetStates()
            // Filtro
            val idFiltro = if (mainVM.uiMainState.login?.id == 0) "" else mainVM.uiMainState.login?.id.toString()
            aulasVM.setIdDptoFiltro(idFiltro)
            aulasVM.filtrarAulas()
            productosVM.setIdDptoFiltro(idFiltro)
            productosVM.filtrarProductos()
            navController.navigate(AppScreens.ProductosBus.name)
            scope.launch { drawerState.close() }
        }
        AppScreens.ProductosFiltro -> {
            mainVM.resetStates()
            productosVM.resetBusState()
            navController.navigate(AppScreens.ProductosFiltro.name)
            scope.launch { drawerState.close() }
        }
        AppScreens.ProductosMto -> {
            mainVM.resetStates()
            navController.navigate(AppScreens.ProductosMto.name)
            scope.launch { drawerState.close() }
        }
        AppScreens.ProductosNSBus -> {
            mainVM.resetStates()
            productosNSVM.resetBusState()
            navController.navigate(AppScreens.ProductosNSBus.name)
            scope.launch { drawerState.close() }
        }
        AppScreens.ProductosNSMto -> {
            mainVM.resetStates()
            navController.navigate(AppScreens.ProductosNSMto.name)
            scope.launch { drawerState.close() }
        }

        else -> {}
    }
}

@Composable
fun DynamicDrawerSheet(
    mainVM: MainVM,
    topScreenSelected: AppScreens,
    onTopScreenSelectedChange : (AppScreens) -> Unit,
    modifier: Modifier = Modifier
) {

    ModalDrawerSheet(
        modifier = modifier
    ) {
        Text(stringResource(R.string.label_home), modifier = Modifier.padding(16.dp))
        HorizontalDivider()
        NavigationDrawerItem(
            selected = topScreenSelected == AppScreens.Home,
            onClick = { onTopScreenSelectedChange(AppScreens.Home) },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.ic_home_32dp),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text(text = stringResource(R.string.label_home)) })
        if (mainVM.uiMainState.login?.id == 0) {
            NavigationDrawerItem(
                selected = topScreenSelected == AppScreens.DptosBus,
                onClick = { onTopScreenSelectedChange(AppScreens.DptosBus) },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_storefront_32dp),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text(text = stringResource(R.string.label_dptos)) })
        }
        NavigationDrawerItem(selected = topScreenSelected == AppScreens.AulasBus, onClick = { onTopScreenSelectedChange(AppScreens.AulasBus) }, icon = {
            Icon(
                painter = painterResource(R.drawable.ic_store_32dp),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }, label = { Text(text = stringResource(R.string.label_aulas)) })
        NavigationDrawerItem(selected = topScreenSelected == AppScreens.ProductosBus, onClick = { onTopScreenSelectedChange(AppScreens.ProductosBus) }, icon = {
            Icon(
                painter = painterResource(R.drawable.ic_shoppingmode_32dp),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }, label = { Text(text = stringResource(R.string.label_productos)) })
    }
}

@PreviewScreenSizes
@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun MainAppPreview() {
    IChirinosv3Theme {
        MainApp(
            mainVM = viewModel(),
            windowSize = WindowWidthSizeClass.Compact
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun OptionMenuPreview() {
    IChirinosv3Theme {
        MainOptionMenu(
            uiMainState = MainState(),
            onDlgConfirmacionSalir = { },
            onMenuLoginClick = { },
            onMenuPrefsClick = { },
            modifier = Modifier
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MainTopBarPreview() {
    IChirinosv3Theme {
        MainTopBar(
            uiMainState = MainState(),
            topScreenSelected = AppScreens.Home,
            onDrawerClick = { },
            onNavHome = { },
            onNavUp = { },
            onNavFiltroAulas = { },
            onNavFiltroProductos = { },
            onMenuLoginClick = { },
            onMenuPrefsClick = { },
            onDlgConfirmacionSalir = { },
            windowSize = WindowWidthSizeClass.Compact
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun MainBottomBarPreview() {
    IChirinosv3Theme {
        MainBottomBar(
            uiMainState = MainState(),
            topScreenSelected = AppScreens.Home,
            onTopScreenSelectedChange = {}
        )
    }
}
