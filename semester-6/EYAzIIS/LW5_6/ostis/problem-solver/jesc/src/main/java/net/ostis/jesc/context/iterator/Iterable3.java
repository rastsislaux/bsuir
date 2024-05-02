package net.ostis.jesc.context.iterator;

import lombok.RequiredArgsConstructor;
import net.ostis.jesc.util.Triplet;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
public class Iterable3 implements Iterable<Triplet<Long>> {

    private final List<List<Long>> addrs;

    @Override
    public Iterator<Triplet<Long>> iterator() {
        return new Iterator3();
    }

    public class Iterator3 implements Iterator<Triplet<Long>> {

        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < addrs.size();
        }

        @Override
        public Triplet<Long> next() {
            if (index >= addrs.size()) {
                throw new NoSuchElementException();
            }

            var tripletList = addrs.get(index++);
            return new Triplet<>(
                    tripletList.get(0),
                    tripletList.get(1),
                    tripletList.get(2)
            );
        }

    }

}
