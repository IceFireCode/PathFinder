package nl.ns.pathfinder

/**
 * Created by paulvc on 07-01-18.
 */

enum class FieldType(val colorResId: Int, val isTraversable: Boolean = true, val isMultipleFieldsAllowed: Boolean = false) {
    DEFAULT(R.color.colorWhite),
    START(R.color.dark_green),
    END(R.color.red_pink),
    WALL(R.color.wall, false, true),
    PATH(R.color.path);
}