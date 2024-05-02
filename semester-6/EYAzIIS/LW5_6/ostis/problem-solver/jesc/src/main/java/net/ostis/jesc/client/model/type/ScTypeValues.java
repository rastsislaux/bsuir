package net.ostis.jesc.client.model.type;

class ScTypeValues {

    public static final int SC_TYPE_NODE = 0x1;
    public static final int SC_TYPE_LINK = 0x2;
    public static final int SC_TYPE_UEDGE_COMMON = 0x4;
    public static final int SC_TYPE_DEDGE_COMMON = 0x8;
    public static final int SC_TYPE_EDGE_ACCESS = 0x10;

    public static final int SC_TYPE_CONST = 0x20;
    public static final int SC_TYPE_VAR = 0x40;

    public static final int SC_TYPE_EDGE_POS = 0x80;
    public static final int SC_TYPE_EDGE_NEG = 0x100;
    public static final int SC_TYPE_EDGE_FUZ = 0x200;

    public static final int SC_TYPE_EDGE_TEMP = 0x400;
    public static final int SC_TYPE_EDGE_PERM = 0x800;

    public static final int SC_TYPE_NODE_TUPLE = 0x80;
    public static final int SC_TYPE_NODE_STRUCT = 0x100;
    public static final int SC_TYPE_NODE_ROLE = 0x200;
    public static final int SC_TYPE_NODE_NOROLE = 0x400;
    public static final int SC_TYPE_NODE_CLASS = 0x800;

    public static final int SC_TYPE_NODE_ABSTRACT = 0x1000;
    public static final int SC_TYPE_NODE_MATERIAL = 0x2000;

    public static final int SC_TYPE_ARC_POS_CONST_PERM = SC_TYPE_EDGE_ACCESS | SC_TYPE_CONST | SC_TYPE_EDGE_POS | SC_TYPE_EDGE_PERM;
    public static final int SC_TYPE_ARC_POS_VAR_PERM = SC_TYPE_EDGE_ACCESS | SC_TYPE_VAR | SC_TYPE_EDGE_POS | SC_TYPE_ARC_POS_CONST_PERM;


    public static final int SC_TYPE_ELEMENT_MASK = SC_TYPE_NODE | SC_TYPE_LINK | SC_TYPE_UEDGE_COMMON | SC_TYPE_DEDGE_COMMON | SC_TYPE_EDGE_ACCESS;
    public static final int SC_TYPE_CONSTANCY_MASK = SC_TYPE_CONST | SC_TYPE_VAR;
    public static final int SC_TYPE_POSITIVITY_MASK = SC_TYPE_EDGE_POS | SC_TYPE_EDGE_NEG | SC_TYPE_EDGE_FUZ;
    public static final int SC_TYPE_PERMANENCY_MASK = SC_TYPE_EDGE_PERM | SC_TYPE_EDGE_TEMP;
    public static final int SC_TYPE_STRUCT_MASK = (
            SC_TYPE_NODE_TUPLE
                    | SC_TYPE_NODE_STRUCT
                    | SC_TYPE_NODE_ROLE
                    | SC_TYPE_NODE_NOROLE
                    | SC_TYPE_NODE_CLASS
                    | SC_TYPE_NODE_ABSTRACT
                    | SC_TYPE_NODE_MATERIAL
    );
    public static final int SC_TYPE_EDGE_MASK = SC_TYPE_EDGE_ACCESS | SC_TYPE_DEDGE_COMMON | SC_TYPE_UEDGE_COMMON;

}
