package com.etouch.sharedList;

public enum SyncActionType
{
    add(1),
    remove(2),
    subscribe(3),
    unsubscribe(4),
    markActive(5),
    markComplete(6);

private int numVal;

SyncActionType(int numVal) {
    this.numVal = numVal;
}

public int getNumVal() {
    return numVal;
}
}

