package ink.ptms.adyeshach.common.util.path

import ink.ptms.adyeshach.api.AdyeshachSettings
import ink.ptms.adyeshach.common.entity.path.PathType
import ink.ptms.adyeshach.common.util.errorBy
import org.bukkit.Location
import org.bukkit.util.Vector
import taboolib.common.platform.function.submit
import taboolib.common5.mirrorNow
import taboolib.module.navigation.NodeEntity
import taboolib.module.navigation.RandomPositionGenerator
import taboolib.module.navigation.createPathfinder

/**
 * @author sky
 * @since 2020-08-13 16:31
 */
object PathFinderHandler {

    fun request(start: Location, target: Location, pathType: PathType = PathType.WALK_2, request: Request = Request.NAVIGATION, call: (Result) -> (Unit)) {
        if (start.world!!.name != target.world!!.name) {
            errorBy("error-different-worlds")
        }
        submit(async = !AdyeshachSettings.pathfinderSync) {
            val startTime = System.currentTimeMillis()
            if (request == Request.NAVIGATION) {
                mirrorNow("PathFinderProxy:Native:Navigation") {
                    val time = System.currentTimeMillis()
                    val pathFinder = createPathfinder(NodeEntity(start, pathType.height, pathType.width))
                    // 最大 32 格的寻路请求
                    val path = pathFinder.findPath(target, distance = 32f)
                    // 调试模式下将显示路径节点
                    if (AdyeshachSettings.debug) {
                        path?.nodes?.forEach { it.display(target.world!!) }
                    }
                    call(ResultNavigation(path?.nodes?.map { it.asBlockPos() } ?: emptyList(), startTime, time))
                }
            } else {
                mirrorNow("PathFinderProxy:Native:RandomPosition") {
                    val time = System.currentTimeMillis()
                    var vec: Vector? = null
                    // 重复最多 10 次的游荡请求
                    repeat(10) {
                        if (vec == null) {
                            vec = RandomPositionGenerator.generateLand(NodeEntity(start, pathType.height, pathType.width), 10, 7)
                        }
                    }
                    if (vec != null) {
                        call(ResultRandomPosition(vec, startTime, time))
                    }
                }
            }
        }
    }
}