package com.etouch.segment;

import java.util.Comparator;

public class EloquaToCxdFieldComp implements Comparator<FieldValue> {

    public int compare(FieldValue o1, FieldValue o2) {
        return o1.getId() - o2.getId();
    }
}
