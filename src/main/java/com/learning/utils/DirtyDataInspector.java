package com.learning.utils;

import org.hibernate.Interceptor;
import org.hibernate.type.Type;

import java.util.HashSet;
import java.util.Set;

public class DirtyDataInspector implements Interceptor {

    private final Set<Object> dirtyEntities = new HashSet<>();

    @Override
    public boolean onFlushDirty(Object entity, Object id, Object[] currentState,
                                Object[] previousState, String[] propertyNames, Type[] types) {
        dirtyEntities.add(entity);
        return false;
    }

    public Set<Object> getDirtyEntities() {
        return dirtyEntities;
    }

    public void clear() {
        dirtyEntities.clear();
    }
}
