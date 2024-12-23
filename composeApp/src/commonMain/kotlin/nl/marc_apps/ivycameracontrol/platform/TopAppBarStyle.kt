package nl.marc_apps.ivycameracontrol.platform

enum class TopAppBarAlignment { Start, Center }

enum class TopAppBarUpIcon { Arrow, Chevron }

expect val topAppBarAlignment: TopAppBarAlignment

expect val topAppBarUpIcon: TopAppBarUpIcon
