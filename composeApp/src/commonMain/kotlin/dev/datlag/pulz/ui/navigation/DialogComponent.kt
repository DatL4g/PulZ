package dev.datlag.pulz.ui.navigation

interface DialogComponent : ContentHolderComponent {
    fun dismiss()

    override fun dismissContent() {
        dismiss()
    }
}