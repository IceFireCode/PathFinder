package nl.ns.pathfinder

/**
 * Created by paulvc on 07-01-18.
 */

enum class FieldType(val colorResId: Int){
    DEFAULT(R.color.colorWhite),
    START(R.color.dark_green),
    END(R.color.red_pink),
    WALL(R.color.wall),
    PATH(R.color.path)
}