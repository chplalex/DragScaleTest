package com.chplalex.dragscaletest

data class MetroMapUiModel(
    val stations: List<MetroStationUiModel>,
    val lines: List<MetroBaseLineUiModel>,
    val transitions: Set<Pair<MetroStationUiModel, MetroStationUiModel>>,
    val canvasWidth: Int,
    val canvasHeight: Int,
    val cityName: String,
    val initialScale: Float,
    val initialXPosition: Int,
    val initialYPosition: Int
) {
    var onStationClickListener: ((stationId: Int) -> Unit)? = null
    var onLineClickListener: ((lineId: Int) -> Unit)? = null
}

data class MetroStationUiModel(
    val id: Int,
    val lineId: Int,
    val joinedStationsIds: List<Int>,
    val coordinates: MetroPointUiModel,
    val name: MetroStationNameUiModel,
    val isSelected: Boolean
)

data class MetroStationNameUiModel(
    val text: String,
    val angleFromStationCenter: Int,
    val distanceFromStationCenter: Int
)

open class MetroBaseLineUiModel(
    open val id: Int,
    open val color: String,
    open val isSelected: Boolean
)

data class MetroLineUiModel(
    override val id: Int,
    override val color: String,
    override val isSelected: Boolean,
    val segments: List<MetroLineSegmentUiModel>
) : MetroBaseLineUiModel(id, color, isSelected)

data class MetroLineSegmentUiModel(
    val startCoordinates: MetroPointUiModel,
    val endCoordinates: MetroPointUiModel,
    val bezieFirstPoint: MetroPointUiModel,
    val bezieSecondPoint: MetroPointUiModel,
    val isFinished: Boolean
)

data class MetroCircleLineUiModel(
    override val id: Int,
    override val color: String,
    override val isSelected: Boolean,
    val centerCoordinates: MetroPointUiModel,
    val radius: Int
) : MetroBaseLineUiModel(id, color, isSelected)

data class MetroPointUiModel(
    val x: Int,
    val y: Int
)