package uz.leeway.lib.bindingdelegate.internal

import android.app.Activity
import android.view.View
import androidx.annotation.RestrictTo
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

@RestrictTo(RestrictTo.Scope.LIBRARY)
@PublishedApi
internal class ActivityDataBinder<T : ViewDataBinding>(
    private val viewProvider: (Activity) -> View
) {

    /**
     * Create new [ViewBinding] instance
     */
    fun bind(activity: Activity): T {
        val view = viewProvider(activity)
        return DataBindingUtil.bind(view)!!
    }
}