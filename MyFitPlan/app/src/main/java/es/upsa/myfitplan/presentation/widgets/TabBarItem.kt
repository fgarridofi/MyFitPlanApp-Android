package es.upsa.myfitplan.presentation.widgets

data class TabBarItem(
    val title: String,
    val destination: String,
    val selectedIcon: Any,
    val unselectedIcon: Any,
    val badgeAmount: Int? = null
)
