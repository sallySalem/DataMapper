# ModelMapper
Simple mapper using kotlin reflection to avoid write 1:1 mapping logic 

First you need to add the kotlin reflection depencies 
```
implementation "org.jetbrains.kotlin:kotlin-reflect:$kotlin_version"
```
Then base mapper class
```kotlin

typealias Mapper<I, O> = (I) -> O
open class BaseMapper<I : Any, O : Any>(
    private val inClass: KClass<I>,
    private val outClass: KClass<O>
) : Mapper<I, O> {
    private val outConstructor = outClass.primaryConstructor!!
    private val inPropertiesByName by lazy {
        inClass.memberProperties.associateBy { it.name }
    }
    override operator fun invoke(data: I): O = with(outConstructor) {
        callBy(parameters.associate { parameter ->
            parameter to argFor(parameter, data)
        })
    }

    open fun argFor(parameter: KParameter, data: I): Any? {
        return inPropertiesByName[parameter.name]?.get(data)
    }
}
```

then create your mapper as object class
###### Direct map
 - when the variables have the same name: no need to do anything `else -> super.argFor(parameter, data)`
 
###### Indirect mapp
 - when you need to create new custom variables like `itemName` and `itemDescription`

```kotlin 
object ItemMapper: BaseMapper<Item, ItemView>(Item::class, ItemView::class) {
    override fun argFor(parameter: KParameter, data: Item): Any? {
        return when (parameter.name) {
            ItemView::itemName.name -> with(data) { name.capitalize() }
            ItemView::itemDescription.name -> with(data) { "$itemDescription -  itemcode: $code" }
            else -> super.argFor(parameter, data)
        }
    }
}
```

## Use
###### Use object direct as `invoke` function in `BaseMapper` class is operator function
```kotlin
    //Using object direct
    val itemViewModel = ItemMapper(itemModel)
    println(itemViewModel)
```

###### Use extensions function 
```Kotlin
    //using extensions function
    val itemViewModelEx = itemModel.mapToView()
    println(itemViewModelEx)
```

```Kotlin
fun Item.mapToView() = ItemMapper(this)
```
## Output
![](https://github.com/sallySalem/ModelMapper/blob/master/Screen%20Shot%202020-07-09%20at%2016.58.33.png)


## Reference 
- https://medium.com/holisticon-consultants/kotlin-data-class-mapping-aa0f9f750ca1

