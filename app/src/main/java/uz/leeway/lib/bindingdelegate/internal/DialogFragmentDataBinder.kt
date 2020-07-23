package uz.leeway.lib.bindingdelegate.internal

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.RestrictTo
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment

@RestrictTo(RestrictTo.Scope.LIBRARY)
@PublishedApi
internal class DialogFragmentDataBinder<T : ViewDataBinding>(
    @IdRes private val viewBindingRootId: Int = 0
) {

    /**
     * Create new [ViewBinding] instance
     */
    fun bind(fragment: DialogFragment): T {
        val view = getRootView(fragment)
        return DataBindingUtil.bind(view)!!
    }

    private fun getRootView(fragment: DialogFragment): View {
        val dialog = checkNotNull(fragment.dialog) { "Dialog hasn't been created yet" }
        val window = checkNotNull(dialog.window) { "Dialog has no window" }
        if (viewBindingRootId != 0) {
            return window.decorView.findViewById<View>(viewBindingRootId)
        } else {
            return window.decorView
        }
    }
}