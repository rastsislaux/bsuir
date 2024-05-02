package net.ostis.jesc.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Triplet<T> {

    private T first;

    private T second;

    private T third;

}
