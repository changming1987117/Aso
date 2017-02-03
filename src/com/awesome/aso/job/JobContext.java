package com.awesome.aso.job;

import java.util.HashMap;

public class JobContext extends HashMap<Object, Object> {

    private static final long serialVersionUID = 1L;
    private JobContext _THAT = this;

    private final String mUniqueId;

    public String getUniqueId() {
        return mUniqueId;
    }

    public JobContext(String uniqueId) {
        super();
        this.mUniqueId = uniqueId;
    }

    public class ContextValue<T> {

        public ContextValue() {
        }

        public ContextValue(T defValue) {
            set(defValue);
        }

        @SuppressWarnings("unchecked")
        public T get() {
            return (T) _THAT.get(this);
        }

        public T getWithError() {
            if (get() == null) {
                throw new RuntimeException("Empty value");
            }
            return get();
        }

        public T getDefault(T def) {
            if (get() == null) {
                return def;
            }
            return get();
        }

        public void set(T t) {
            _THAT.put(this, t);
        }

    }
}
