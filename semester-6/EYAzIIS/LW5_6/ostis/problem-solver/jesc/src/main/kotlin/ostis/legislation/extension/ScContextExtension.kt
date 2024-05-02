package ostis.legislation.extension

import net.ostis.jesc.client.model.element.ScReference
import net.ostis.jesc.client.model.type.ScType
import net.ostis.jesc.context.ScContext

fun ScContext.getRelationTargets(addr: Long, relAddr: Long, relType: ScType) =
    this.api.searchByTemplate()
        .references(
            ScReference.addr(addr),
            ScReference.type(relType, "edge"),
            ScReference.type(ScType.VAR)
        )
        .references(
            ScReference.addr(relAddr),
            ScReference.type(ScType.EDGE_ACCESS_VAR_POS_PERM),
            ScReference.alias("edge")
        )
        .execute()
        .payload
        .addrs
        .map { it[2] }

fun ScContext.getRelationSources(addr: Long, relAddr: Long, relType: ScType) =
    this.api.searchByTemplate()
        .references(
            ScReference.type(ScType.VAR),
            ScReference.type(relType, "edge"),
            ScReference.addr(addr)
        )
        .references(
            ScReference.addr(relAddr),
            ScReference.type(ScType.EDGE_ACCESS_VAR_POS_PERM),
            ScReference.alias("edge")
        )
        .execute()
        .payload
        .addrs
        .map { it[0] }

fun ScContext.getRelationTarget(addr: Long, relAddr: Long, relType: ScType): Long =
    getRelationTargets(addr, relAddr, relType)[0]

fun ScContext.getRelationSource(addr: Long, relAddr: Long, relType: ScType): Long =
    getRelationSources(addr, relAddr, relType)[0]

fun ScContext.getRoleRelationTargets(addr: Long, rrelAddr: Long) =
    getRelationTargets(addr, rrelAddr, ScType.EDGE_ACCESS_VAR_POS_PERM)

fun ScContext.getRoleRelationSources(addr: Long, rrelAddr: Long) =
    getRelationSources(addr, rrelAddr, ScType.EDGE_ACCESS_VAR_POS_PERM)

fun ScContext.getRoleRelationTarget(addr: Long, rrelAddr: Long) =
    getRelationTarget(addr, rrelAddr, ScType.EDGE_ACCESS_VAR_POS_PERM)

fun ScContext.getRoleRelationSource(addr: Long, rrelAddr: Long) =
    getRelationSource(addr, rrelAddr, ScType.EDGE_ACCESS_VAR_POS_PERM)

fun ScContext.getNoRoleRelationTargets(addr: Long, nrelAddr: Long) =
    getRelationTargets(addr, nrelAddr, ScType.EDGE_D_COMMON_VAR)

fun ScContext.getNoRoleRelationTarget(addr: Long, nrelAddr: Long) =
    getRelationTarget(addr, nrelAddr, ScType.EDGE_D_COMMON_VAR)

fun ScContext.getNoRoleRelationSource(addr: Long, nrelAddr: Long) =
    getRelationSource(addr, nrelAddr, ScType.EDGE_D_COMMON_VAR)

fun ScContext.getElements(setAddr: Long) = this.api.searchByTemplate().references(
    ScReference.addr(setAddr),
    ScReference.type(ScType.EDGE_ACCESS_VAR_POS_PERM),
    ScReference.type(ScType.NODE_VAR)
).execute().payload.addrs.map { it.last() }
