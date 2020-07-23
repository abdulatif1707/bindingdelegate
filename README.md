# DataBindingDelegate

Make work with [Android View Binding](https://developer.android.com/topic/libraries/view-binding) simpler

## Add library to a project

```groovy
allprojects {
  repositories {
    maven { url 'https://jitpack.io' }
  }
}

dependencies {
    implementation 'com.github.abdulatif1707:bindingdelegate:0.0.1'
}
```

## Samples

```kotlin
class ProfileFragment : Fragment(R.layout.profile) {

    private val dataBinding: ProfileBinding by dataBinding()
}
```

```kotlin
class ProfileActivity : AppCompatActivity(R.layout.profile) {

    private val dataBinding: ProfileBinding by dataBinding(R.id.container)
}
```

```kotlin
class ProfileDialogFragment : DialogFragment() {

    private val dataBinding: ProfileBinding by dialogDataBinding(R.id.container)

    // Creating via default way will work too for that case
    // private val dataBinding: ProfileBinding by dataBinding()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setView(R.layout.profile)
            .create()
    }
}
```

```kotlin
class ProfileDialogFragment : DialogFragment() {

    private val dataBinding: ProfileBinding by dialogDataBinding(R.id.container)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.profile, container, false)
    }
}
```