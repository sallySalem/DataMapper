import model.Item

fun main() {
    val itemModel = Item(
        itemId = 123,
        name = "item name",
        itemUrl = "url://item",
        itemDescription = " this is the description of our item",
        code = "12345"
    )

    //Using object direct
    val itemViewModel = ItemMapper(itemModel)
    println(itemViewModel)

    //using extensions function
    val itemViewModelEx = itemModel.mapToView()
    println(itemViewModelEx)
}

fun Item.mapToView() = ItemMapper(this)