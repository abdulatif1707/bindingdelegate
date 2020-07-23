package uz.leeway.lib.bindingdelegate.internal

import androidx.annotation.RestrictTo
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

@RestrictTo(RestrictTo.Scope.LIBRARY)
@PublishedApi
internal class FragmentDataBinder<T : ViewDataBinding> {

    /**
     * Create new [ViewBinding] instance
     */
    fun bind(fragment: Fragment): T {
        val view = fragment.requireView()
        return DataBindingUtil.bind(view)!!
    }
}