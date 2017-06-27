package com.etouch.sharedList;

import java.util.List;
import java.util.Map;

public class Import{
    public Import(){
         
     }
     public Map<String, String> fields;
     public String importPriorityUri;
     public String identifierFieldName;
     public boolean isSyncTriggeredOnImport;
     public String name;
     public int secondsToRetainData;
     public List<SyncAction> syncActions;
     public RuleType updateRule;
     public String uri;

}
