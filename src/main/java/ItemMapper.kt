import model.Item
import model.ItemView
import kotlin.reflect.KParameter

object ItemMapper: BaseMapper<Item, ItemView>(Item::class, ItemView::class) {
    override fun argFor(parameter: KParameter, data: Item): Any? {
        return when (parameter.name) {
            ItemView::itemName.name -> with(data) { name.capitalize() }
            ItemView::itemDescription.name -> with(data) { "$itemDescription -  itemcode: $code" }
            else -> super.argFor(parameter, data)
        }
    }
}