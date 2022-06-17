package ink.ptms.adyeshach.api.event

import ink.ptms.adyeshach.common.entity.EntityInstance
import org.bukkit.entity.Player
import taboolib.platform.type.BukkitProxyEvent

/**
 * @author sky
 * @since 2020-08-14 19:21
 */
class AdyeshachEntityVisibleEvent(val entity: EntityInstance, val viewer: Player, val visible: Boolean) : BukkitProxyEvent()