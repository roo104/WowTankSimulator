package com.wowtanksim.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wowtanksim.model.TalentDef
import com.wowtanksim.model.TalentState
import com.wowtanksim.model.TalentTreeDef
import com.wowtanksim.model.TalentTrees

private val CELL_SIZE = 56.dp
private val ICON_SIZE = 40.dp
private val GRID_COLS = 4
private val GRID_ROWS = 9

private val COLOR_MAXED = Color(0xFFFFD700)     // gold
private val COLOR_AVAILABLE = Color(0xFF00CC00)  // green
private val COLOR_LOCKED = Color(0xFF666666)     // grey

private val TREE_BACKGROUNDS = mapOf(
    "Balance" to Color(0xFF0D1526),
    "Feral Combat" to Color(0xFF1A1308),
    "Restoration" to Color(0xFF0A1A0D),
)

@Composable
fun TalentTreePanel(
    talentState: TalentState,
    onTalentStateChange: (TalentState) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        // Summary bar
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "Points: ${talentState.totalPoints}/61",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TalentTrees.allTrees.forEach { tree ->
                    Text(
                        "${tree.name}: ${talentState.pointsInTree(tree.name)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Button(onClick = { onTalentStateChange(talentState.resetAll()) }) {
                Text("Reset All")
            }
        }

        // Three trees side by side, scrollable both directions
        Box(modifier = Modifier.weight(1f)) {
            val verticalScroll = rememberScrollState()
            val horizontalScroll = rememberScrollState()
            Row(
                modifier = Modifier
                    .verticalScroll(verticalScroll)
                    .horizontalScroll(horizontalScroll),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                TalentTrees.allTrees.forEach { tree ->
                    SingleTreePanel(
                        treeDef = tree,
                        talentState = talentState,
                        onTalentStateChange = onTalentStateChange,
                    )
                }
            }
        }
    }
}

@Composable
private fun SingleTreePanel(
    treeDef: TalentTreeDef,
    talentState: TalentState,
    onTalentStateChange: (TalentState) -> Unit,
) {
    val bgColor = TREE_BACKGROUNDS[treeDef.name] ?: Color(0xFF111111)
    val pointsInTree = talentState.pointsInTree(treeDef.name)

    Column {
        // Tree header
        Row(
            modifier = Modifier.width(CELL_SIZE * GRID_COLS).padding(bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "${treeDef.name} ($pointsInTree)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
            )
            TextButton(onClick = { onTalentStateChange(talentState.resetTree(treeDef.name)) }) {
                Text("Reset", style = MaterialTheme.typography.labelSmall)
            }
        }

        // Grid
        Box(
            modifier = Modifier
                .width(CELL_SIZE * GRID_COLS)
                .height(CELL_SIZE * GRID_ROWS)
                .clip(RoundedCornerShape(8.dp))
                .background(bgColor)
                .drawBehind {
                    // Draw prerequisite arrows
                    for (talent in treeDef.talents) {
                        if (talent.prerequisiteId != null) {
                            val prereq = TalentTrees.byId[talent.prerequisiteId] ?: continue
                            val fromX = (prereq.col + 0.5f) * CELL_SIZE.toPx()
                            val fromY = (prereq.row + 0.5f) * CELL_SIZE.toPx() + ICON_SIZE.toPx() / 2
                            val toX = (talent.col + 0.5f) * CELL_SIZE.toPx()
                            val toY = (talent.row + 0.5f) * CELL_SIZE.toPx() - ICON_SIZE.toPx() / 2

                            val prereqPts = talentState.points[talent.prerequisiteId] ?: 0
                            val prereqDef = TalentTrees.byId[talent.prerequisiteId]!!
                            val arrowColor = if (prereqPts >= prereqDef.maxPoints) COLOR_AVAILABLE else COLOR_LOCKED

                            if (prereq.col == talent.col) {
                                // Straight vertical arrow
                                drawLine(arrowColor, Offset(fromX, fromY), Offset(toX, toY), strokeWidth = 2f)
                            } else {
                                // L-shaped: go down from prereq, then across to talent
                                val midY = toY - CELL_SIZE.toPx() * 0.3f
                                drawLine(arrowColor, Offset(fromX, fromY), Offset(fromX, midY), strokeWidth = 2f)
                                drawLine(arrowColor, Offset(fromX, midY), Offset(toX, midY), strokeWidth = 2f)
                                drawLine(arrowColor, Offset(toX, midY), Offset(toX, toY), strokeWidth = 2f)
                            }

                            // Arrowhead
                            val arrowSize = 5f
                            drawLine(arrowColor, Offset(toX - arrowSize, toY - arrowSize * 2), Offset(toX, toY), strokeWidth = 2f)
                            drawLine(arrowColor, Offset(toX + arrowSize, toY - arrowSize * 2), Offset(toX, toY), strokeWidth = 2f)
                        }
                    }
                },
        ) {
            // Talent nodes
            for (talent in treeDef.talents) {
                val pts = talentState.points[talent.id] ?: 0
                val canAdd = talentState.canAddPoint(talent.id)
                val isMaxed = pts >= talent.maxPoints
                val isLocked = !canAdd && pts == 0

                Box(
                    modifier = Modifier
                        .offset(
                            x = CELL_SIZE * talent.col + (CELL_SIZE - ICON_SIZE) / 2,
                            y = CELL_SIZE * talent.row + (CELL_SIZE - ICON_SIZE) / 2,
                        )
                        .size(ICON_SIZE),
                ) {
                    TalentNode(
                        talent = talent,
                        points = pts,
                        canAdd = canAdd,
                        isMaxed = isMaxed,
                        isLocked = isLocked,
                        onAddPoint = {
                            talentState.addPoint(talent.id)?.let { onTalentStateChange(it) }
                        },
                        onRemovePoint = {
                            talentState.removePoint(talent.id)?.let { onTalentStateChange(it) }
                        },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TalentNode(
    talent: TalentDef,
    points: Int,
    canAdd: Boolean,
    isMaxed: Boolean,
    isLocked: Boolean,
    onAddPoint: () -> Unit,
    onRemovePoint: () -> Unit,
) {
    val borderColor = when {
        isMaxed -> COLOR_MAXED
        canAdd || points > 0 -> COLOR_AVAILABLE
        else -> COLOR_LOCKED
    }
    val nodeAlpha = if (isLocked) 0.4f else 1.0f

    val iconUrl = "https://wow.zamimg.com/images/wow/icons/medium/${talent.icon}.jpg"
    val rankDesc = if (points > 0 && points <= talent.description.size) {
        talent.description[points - 1]
    } else if (talent.description.isNotEmpty()) {
        talent.description[0]
    } else ""

    TooltipArea(
        tooltip = {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFF1A1A2E),
                shadowElevation = 4.dp,
            ) {
                Column(modifier = Modifier.padding(10.dp).widthIn(max = 250.dp)) {
                    Text(
                        talent.name,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                    )
                    Text(
                        "Rank ${points}/${talent.maxPoints}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isMaxed) COLOR_MAXED else Color(0xFFAAAAAA),
                    )
                    if (rankDesc.isNotEmpty()) {
                        Spacer(Modifier.height(4.dp))
                        Text(
                            rankDesc,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFCCCCCC),
                        )
                    }
                    if (points in 1 until talent.maxPoints && points < talent.description.size) {
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "Next rank:",
                            style = MaterialTheme.typography.labelSmall,
                            color = COLOR_AVAILABLE,
                        )
                        Text(
                            talent.description[points],
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFCCCCCC),
                        )
                    }
                }
            }
        },
        tooltipPlacement = TooltipPlacement.CursorPoint(offset = DpOffset(12.dp, 12.dp)),
    ) {
        Box(
            modifier = Modifier
                .size(ICON_SIZE)
                .alpha(nodeAlpha)
                .clip(RoundedCornerShape(4.dp))
                .border(2.dp, borderColor, RoundedCornerShape(4.dp))
                .pointerInput(talent.id, points, canAdd, isMaxed) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            if (event.type == androidx.compose.ui.input.pointer.PointerEventType.Press) {
                                if (event.buttons.isSecondaryPressed) {
                                    onRemovePoint()
                                    event.changes.forEach { it.consume() }
                                } else {
                                    // Left-click: add if possible, otherwise remove
                                    if (canAdd) onAddPoint() else if (points > 0) onRemovePoint()
                                    event.changes.forEach { it.consume() }
                                }
                            }
                        }
                    }
                },
            contentAlignment = Alignment.BottomEnd,
        ) {
            IconImage(url = iconUrl, size = ICON_SIZE)

            // Points overlay
            if (points > 0 || !isLocked) {
                Text(
                    "${points}/${talent.maxPoints}",
                    modifier = Modifier
                        .background(Color(0xCC000000), RoundedCornerShape(2.dp))
                        .padding(horizontal = 2.dp),
                    color = if (isMaxed) COLOR_MAXED else if (points > 0) COLOR_AVAILABLE else Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                )
            }
        }
    }
}
