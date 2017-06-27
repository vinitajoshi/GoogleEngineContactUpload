package com.etouch.sharedList;

public enum RuleType {
    always(1),
    ifNewIsNotNull(2),
    ifExistingIsNull(6),
    useFieldRule(17);
    
private int numVal;

RuleType(int numVal) {
this.numVal = numVal;
}

public int getNumVal() {
return numVal;
}
}