import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

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