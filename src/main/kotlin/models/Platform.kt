package models

sealed class Platform {
    object Android : Platform()
    object iOS : Platform()
}