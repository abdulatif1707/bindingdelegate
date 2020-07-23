package uz.leeway.lib.bindingdelegate

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.MainThread
import androidx.core.app.ComponentActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.databinding.ViewDataBinding
import uz.leeway.lib.bindingdelegate.internal.ActivityDataBinder
import uz.leeway.lib.bindingdelegate.internal.DialogFragmentDataBinder
import uz.leeway.lib.bindingdelegate.internal.FragmentDataBinder
import uz.leeway.lib.bindingdelegate.internal.checkIsMainThread
import uz.leeway.lib.bindingdelegate.internal.requireViewByIdCompat
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

abstract class DataBindingProperty<in R, T : ViewDataBinding>(
    private val viewBinder: (R) -> T
) : ReadOnlyProperty<R, T> {

    internal var binding: T? = null
    private val lifecycleObserver = BindingLifecycleObserver()

    protected abstract fun getLifecycleOwner(thisRef: R): LifecycleOwner

    @MainThread
    override fun getValue(thisRef: R, property: KProperty<*>): T {
        checkIsMainThread()
        binding?.let { return it }

        getLifecycleOwner(thisRef).lifecycle.addObserver(lifecycleObserver)
        return viewBinder(thisRef).also { binding = it }
    }

    private inner class BindingLifecycleObserver : DefaultLifecycleObserver {

        private val mainHandler = Handler(Looper.getMainLooper())

        @MainThread
        override fun onDestroy(owner: LifecycleOwner) {
            owner.lifecycle.removeObserver(this)
            mainHandler.post {
                binding = null
            }
        }
    }
}

@PublishedApi
internal class ActivityDataBindingProperty<A : ComponentActivity, T : ViewDataBinding>(
    viewBinder: (A) -> T
) : DataBindingProperty<A, T>(viewBinder) {

    override fun getLifecycleOwner(thisRef: A) = thisRef
}

@PublishedApi
internal class FragmentDataBindingProperty<F : Fragment, T : ViewDataBinding>(
    viewBinder: (F) -> T
) : DataBindingProperty<F, T>(viewBinder) {

    override fun getLifecycleOwner(thisRef: F) = thisRef.viewLifecycleOwner
}

@PublishedApi
internal class DialogFragmentDataBindingProperty<F : DialogFragment, T : ViewDataBinding>(
    viewBinder: (F) -> T
) : DataBindingProperty<F, T>(viewBinder) {

    override fun getLifecycleOwner(thisRef: F): LifecycleOwner {
        return if (thisRef.view == null) thisRef.viewLifecycleOwner else thisRef
    }
}

/**
 * Create new [ViewDataBinding] associated with the [Activity][this]
 *
 * @param viewBindingRootId Root view's id that will be used as root for the view binding
 */
@Suppress("unused")
inline fun <A : ComponentActivity, reified T : ViewDataBinding> A.dataBinding(
    @IdRes viewBindingRootId: Int
): DataBindingProperty<A, T> {
    val activityViewBinder =
        ActivityDataBinder<T> { it.requireViewByIdCompat(viewBindingRootId) }
    return dataBinding(activityViewBinder::bind)
}

/**
 * Create new [ViewDataBinding] associated with the [Activity][this] and allow customize how
 * a [View] will be bounded to the view binding.
 */
@Suppress("unused")
@JvmName("dataBindingActivity")
fun <A : ComponentActivity, T : ViewDataBinding> A.dataBinding(viewBinder: (A) -> T): DataBindingProperty<A, T> {
    return ActivityDataBindingProperty(viewBinder)
}

/**
 * Create new [ViewDataBinding] associated with the [Fragment][this]
 */
@Suppress("unused")
@JvmName("dataBindingFragment")
inline fun <F : Fragment, reified T : ViewDataBinding> F.dataBinding(): DataBindingProperty<Fragment, T> {
    return dataBinding(FragmentDataBinder<T>()::bind)
}

/**
 * Create new [ViewDataBinding] associated with the [Fragment][this]
 */
@Suppress("unused")
@JvmName("dataBindingFragment")
fun <F : Fragment, T : ViewDataBinding> F.dataBinding(viewBinder: (F) -> T): DataBindingProperty<F, T> {
    return FragmentDataBindingProperty(viewBinder)
}

/**
 * Create new [ViewDataBinding] associated with the [DialogFragment][this]'s view
 *
 * @param viewBindingRootId Id of the root view from your custom view
 */
@Suppress("unused")
@JvmName("dataBindingDialogFragment")
inline fun <reified T : ViewDataBinding> DialogFragment.dialogDataBinding(
    @IdRes viewBindingRootId: Int
): DataBindingProperty<DialogFragment, T> {
    return dialogDataBinding(DialogFragmentDataBinder<T>(viewBindingRootId)::bind)
}


/**
 * Create new [ViewDataBinding] associated with the [DialogFragment][this]
 */
@Suppress("unused")
@JvmName("dataBindingDialogFragment")
fun <F : DialogFragment, T : ViewDataBinding> F.dialogDataBinding(viewBinder: (F) -> T): DataBindingProperty<F, T> {
    return DialogFragmentDataBindingProperty(viewBinder)
}