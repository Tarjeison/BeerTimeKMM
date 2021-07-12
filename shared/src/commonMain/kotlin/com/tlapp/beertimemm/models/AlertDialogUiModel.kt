package com.tlapp.beertimemm.models

class AlertDialogUiModel(
    val title: String,
    val message: String,
    val positiveButtonText: String,
    val negativeButtonText: String,
    val onClick: () -> Unit
)
