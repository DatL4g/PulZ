package dev.datlag.gamechanger.ui.navigation

interface DialogComponent : ContentHolderComponent {
    fun dismiss()

    override fun dismissContent() {
        dismiss()
    }
}