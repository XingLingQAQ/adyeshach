package ink.ptms.adyeshach.common.entity.type.impl

import ink.ptms.adyeshach.common.entity.MetadataExtend
import ink.ptms.adyeshach.common.entity.element.EntityProperties
import ink.ptms.adyeshach.common.entity.type.EntityTypes
import ink.ptms.adyeshach.api.nms.NMS
import io.izzel.taboolib.internal.gson.annotations.Expose
import org.bukkit.entity.Player

/**
 * @Author sky
 * @Since 2020-08-04 19:30
 */
class AdyBlaze(owner: Player) : AdyEntityLiving(owner, EntityTypes.BLAZE), MetadataExtend {

    init {
        properties = BlazeProperties()
    }

    fun setFire(value: Boolean) {
        getProperties().fire = value
        NMS.INSTANCE.updateEntityMetadata(owner, index, NMS.INSTANCE.getMetaEntityByte(15, if (value) 0x01 else 0x00))
    }

    fun isFire(): Boolean {
        return getProperties().fire
    }

    override fun metadata(): List<Any> {
        return getProperties().run {
            listOf(NMS.INSTANCE.getMetaEntityByte(15, if (fire) 0x01 else 0x00))
        }
    }

    private fun getProperties(): BlazeProperties = properties as BlazeProperties

    private class BlazeProperties : EntityProperties() {

        @Expose
        var fire = false
    }
}