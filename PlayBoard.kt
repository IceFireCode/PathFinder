package nl.ns.pathfinder

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import nl.ns.pathfinder.Extensions.getSmallestScreenSize

/**
 * Created by paulvc on 20-12-17.
 */
class PlayBoard @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BoxDrawingView(context, attrs) {
    val TAG = PlayBoard::class.java.simpleName

    // field selection state
    var activeFieldType = FieldType.DEFAULT

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

    var eventIsProcessed: Boolean = false

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
        super.onTouchEvent(event)

        if (event.action == MotionEvent.ACTION_DOWN) {
            Log.i(TAG, "$event")
            if (event.historySize > 0) {
                val touchedField = getFieldFromPositionOnCanvas(event.x.toInt(), event.y.toInt())
                touchedField?.let {
                    updateFieldCollections(it)?.let {
                        invalidate()
                    }
                }
            }
            eventIsProcessed = true
        }

        if (event.action == MotionEvent.ACTION_MOVE) {
            Log.i(TAG, event.toString())
            if (!eventIsProcessed) {
                if (event.historySize > 0) {
                    val touchedFieldStart = getFieldFromPositionOnCanvas(event.getHistoricalX(0).toInt(), event.getHistoricalY(0).toInt())
                    touchedFieldStart?.let {
                        updateFieldCollections(it)?.let {
                            invalidate()
                        }
                    }
                }
            }
            eventIsProcessed = true
        }

//            if (event.action == MotionEvent.ACTION_MOVE){
//                Log.i(TAG, "$event")
//                if (event.historySize > 0) {
//                    val touchedFieldStart = getFieldFromPositionOnCanvas(event.getHistoricalX(0).toInt(), event.getHistoricalY(0).toInt())
//                    touchedFieldStart?.let {
//                        updateFieldCollections(it)?.let {
//                            invalidate()
//                        }
//                    }
//                } else {
//                    val touchedFieldStart = getFieldFromPositionOnCanvas(event.x.toInt(), event.y.toInt())
//                    touchedFieldStart?.let {
//                        updateFieldCollections(it)?.let {
//                            invalidate()
//                        }
//                    }
//                }
//                eventIsProcessed = true
//            }

        if (event.action == MotionEvent.ACTION_UP) {
            Log.i(TAG, event.toString())
            emptyBoxes()
            val touchedField = getFieldFromPositionOnCanvas(event.x.toInt(), event.y.toInt())
            touchedField?.let {
                updateFieldCollections(it)?.let {
                    invalidate()
                }
            }
            eventIsProcessed = false
        }

        return true
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
                }
                return true
            }
            else -> return null
        }
        return null
    }

    fun clearBoard() {
        startField = null; endField = null; wallFields.clear(); clearPath()
        for (i in 0.until(numberOfFieldsInOneRow)) {
            for (j in 0.until(numberOfFieldsInOneRow)) {
                allFields[i][j].fieldType = FieldType.DEFAULT
            }
        }
        invalidate()
    }

    fun clearPath() {
        pathFields.forEach {
            if (it != startField && it != endField) {
                it.fieldType = FieldType.DEFAULT
            }
        }
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

    private fun getFieldFromPositionOnCanvas(x: Int, y: Int): FieldInBord? {
        val row = x / fieldSize
        val column = y / fieldSize
        var field: FieldInBord? = null
        try {
            field = allFields[column][row]
        } finally {
            return field // the click is somehow IN the view, but outside the actual board
        }
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


