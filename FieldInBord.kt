package nl.ns.pathfinder

/**
 * Created by paulvc on 20-12-17.
 */
class FieldInBord(val xCoordinate: Int, val yCoordinate: Int) {

    var fieldType = FieldType.DEFAULT

    override fun toString(): String {
        return "x: $xCoordinate, y: $yCoordinate"
    }

//    fun handleOnclick() {
//        when (fieldType){
//            FieldType.DEFAULT ->
//                    fieldType = FieldType.WALL
//            FieldType.WALL ->
//                    fieldType = FieldType.DEFAULT
//        }
//    }

}