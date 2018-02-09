package nl.ns.pathfinder.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import nl.ns.pathfinder.*
import nl.ns.pathfinder.Extensions.getSmallestScreenSize
import kotlin.math.max
import kotlin.math.min

/**
 * Created by paulvc on 20-12-17.
 */
class PlayBoard @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BoxDrawingView(context, attrs), ControlPanelObserver {

    override fun update(cps: ControlPanel.ControlPanelState) {
        activeFieldType = cps.activeFieldType
    }

    val TAG = PlayBoard::class.java.simpleName

    // field selection state
    var activeFieldType = FieldType.DEFAULT
        set(value) {
            newStateAction(value)
            field = value
        }

    private fun newStateAction(fieldType: FieldType) {
        when (fieldType) {
            FieldType.PATH ->
                findPath()
            FieldType.DEFAULT ->
                clearBoard()
            else -> {
            }
        }
    }

    // field collections
    lateinit var allFields: Array<Array<FieldInBord>>
    var startField: FieldInBord? = null
    var endField: FieldInBord? = null
    val wallFields: MutableSet<FieldInBord> = mutableSetOf<FieldInBord>()
    var pathFields: Set<FieldInBord> = setOf<FieldInBord>()

    val numberOfFieldsInOneRow = 12
    var fieldSize = 0
    val fieldBorderPaint = Paint()
    val fieldPaint = Paint()

    init {
        fieldSize = determineFieldSize()

        fieldBorderPaint.color = ContextCompat.getColor(context, R.color.colorPrimary)

        allFields = Array(numberOfFieldsInOneRow) { i ->
            Array(numberOfFieldsInOneRow) { j ->
                FieldInBord(i, j)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        // let the super class draw a box
        super.onTouchEvent(event)

        // use the drawn box to get the selected field(s)
        if (event.action == MotionEvent.ACTION_UP) {
            Log.i(TAG, event.toString())
            val currentBox: Box? = boxes[0]
            emptyBoxes()
            currentBox?.let {
                val touchedFieldOrigin = getFieldFromPositionOnCanvas(it.origin)
                val touchedFieldCurrent = getFieldFromPositionOnCanvas(it.current)
                if (touchedFieldOrigin != null && touchedFieldCurrent != null) {
                    val currentEqualsOrigin = touchedFieldOrigin == touchedFieldCurrent

                    // if it's about a groupselection of fields
                    if (!currentEqualsOrigin && activeFieldType.isMultipleFieldsAllowed) {
                        val touchedFieldCollection = getFieldsFromPositionOnCanvas(touchedFieldOrigin, touchedFieldCurrent)
                        updateFieldCollections(touchedFieldCollection)
                        //todo have all fields in between also be updated
                    }

                    // if it's about just one field
                    else {
                        updateFieldCollections(touchedFieldCurrent)
                    }

                    invalidate()
                }
            }
        }

        return true
    }

    private fun updateFieldCollections(touchedFieldCollection: Set<FieldInBord>) {
        touchedFieldCollection.forEach {
            updateFieldCollections(it)
        }
    }

    private fun updateFieldCollections(fieldInBord: FieldInBord): Boolean? {
        when (activeFieldType) {
            FieldType.WALL -> {
                if (wallFields.contains(fieldInBord)) {
                    fieldInBord.fieldType = FieldType.DEFAULT
                    wallFields.remove(fieldInBord)
                    return true
                } else {
                    fieldInBord.fieldType = FieldType.WALL
                    wallFields.add(fieldInBord)
                    return true
                }
            }
            FieldType.START -> {
                if (startField == fieldInBord) {
                    fieldInBord.fieldType = FieldType.DEFAULT
                    startField = null
                    return true
                } else {
                    startField?.let { it.fieldType = FieldType.DEFAULT }
                    fieldInBord.fieldType = FieldType.START
                    startField = fieldInBord
                    return true
                }
            }
            FieldType.END -> {
                if (startField == fieldInBord) {
                    fieldInBord.fieldType = FieldType.DEFAULT
                    endField = null
                    return true
                } else {
                    endField?.let { it.fieldType = FieldType.DEFAULT }
                    fieldInBord.fieldType = FieldType.END
                    endField = fieldInBord
                    return true
                }
            }
            FieldType.DEFAULT -> {
                if (fieldInBord.fieldType != FieldType.DEFAULT) {
                    fieldInBord.fieldType = FieldType.DEFAULT
                    return true
                }
            }
            else -> return null
        }
        return null
    }

    fun clearBoard() {
        wallFields.clear()
        clearPath()
        for (i in 0.until(numberOfFieldsInOneRow)) {
            for (j in 0.until(numberOfFieldsInOneRow)) {
                if (allFields[i][j] != startField && allFields[i][j] != endField) {
                    allFields[i][j].fieldType = FieldType.DEFAULT
                }
            }
        }
        invalidate()
    }

    fun clearPath() {
        pathFields.forEach {
            if (it.fieldType == FieldType.PATH && it != startField && it != endField) {
                it.fieldType = FieldType.DEFAULT
            }
        }
        pathFields = setOf()
        invalidate()
    }

    fun findPath() {
        clearPath()
        if (startField != null && endField != null) {
            val path = Path(startField!!.xCoordinate, endField!!.xCoordinate, startField!!.yCoordinate, endField!!.yCoordinate)
            pathFields = path.determinePathFields(this)
            var hasReachedWall = false
            pathFields.forEach {
                if (wallFields.contains(it)) {
                    hasReachedWall = true
                    return@forEach
                }
                if (it != startField && it != endField && !hasReachedWall) {
                    it.fieldType = FieldType.PATH
                }
            }
            invalidate()
        }
    }

    private fun getFieldFromPositionOnCanvas(point: PointF): FieldInBord? {
        val x = point.x.toInt()
        val y = point.y.toInt()
        val row = x / fieldSize
        val column = y / fieldSize
        var field: FieldInBord? = null
        try {
            field = allFields[column][row]
        } finally {
            return field // if null, the click is somehow IN the view, but outside the actual board
        }
    }

    private fun getFieldsFromPositionOnCanvas(startField: FieldInBord, endField: FieldInBord): Set<FieldInBord> {
        val minX = min(startField.xCoordinate, endField.xCoordinate)
        val maxX = max(startField.xCoordinate, endField.xCoordinate)
        val minY = min(startField.yCoordinate, endField.yCoordinate)
        val maxY = max(startField.yCoordinate, endField.yCoordinate)
        val fields = mutableSetOf<FieldInBord>()
        for (i in minX.rangeTo(maxX)) {
            for (j in minY.rangeTo(maxY)) {
                fields.add(allFields[i][j])
            }
        }
        return fields
    }

    override fun onDraw(canvas: Canvas) {
        for (i in 0.until(numberOfFieldsInOneRow)) {
            for (j in 0.until(numberOfFieldsInOneRow)) {
                val recL = j * fieldSize
                val recR = (j + 1) * fieldSize
                val recB = i * fieldSize
                val recT = (i + 1) * fieldSize
                val recLF = recL.toFloat()
                val recRF = recR.toFloat()
                val recTF = recT.toFloat()
                val recBF = recB.toFloat()

                val field = allFields[i][j]
                fieldPaint.color = ContextCompat.getColor(context, field.fieldType.colorResId)

                canvas.drawRect(recLF, recTF, recRF, recBF, fieldBorderPaint)
                canvas.drawRect(recLF + 1, recTF - 1, recRF - 1, recBF + 1, fieldPaint)

                //println(allFields[i][j]) // Prints: the String at position 0, 3
            }
        }
        super.onDraw(canvas)
    }

    private fun determineFieldSize(): Int {
        val screenSize = context.getSmallestScreenSize()
        Log.i(TAG, "screenSize: $screenSize, fieldSize: $fieldSize, for $numberOfFieldsInOneRow * $numberOfFieldsInOneRow fields.")
        return screenSize / numberOfFieldsInOneRow
    }

}


