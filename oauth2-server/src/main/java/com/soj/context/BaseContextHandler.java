package com.soj.context;


import java.util.concurrent.ConcurrentHashMap;


/**
 * 线程上下文
 * get和set支持在多线程中使用。
 * 
 *
 */
public class BaseContextHandler extends ConcurrentHashMap<String, Object> {
	
	private static final long serialVersionUID = 1L;

	private static final ThreadLocal<BaseContextHandler> threadLocal = new InheritableThreadLocal<BaseContextHandler>() {
        @Override
        protected BaseContextHandler initialValue() {
            return new BaseContextHandler();
        }
    };
    
	private BaseContextHandler() {
	}
	
    /**
     * 获得当前的 BaseContextHandler
     *
     * @return 当前的 BaseContextHandler
     */
    public static BaseContextHandler getContext() {
        return threadLocal.get();
    }
    
    /**
     * 设置BaseContextHandler
     * 多线程复制数据时使用。
     * 
     * @param holder 线程上下文
     */
    public static void setContext(BaseContextHandler holder) {
    	threadLocal.set(holder);
    }
    
	private void innerSet(String key, Object value) {
		super.put(key, value);
	}
	
	public static void set(String key, Object value) {
		BaseContextHandler.getContext().innerSet(key, value);
	}

	private Object innerGet(String key) {
		return super.get(key);
	}
	
	public static Object get(String key) {
		return BaseContextHandler.getContext().innerGet(key);
	}

	private void innerRemove() {
		threadLocal.remove();
	}

	public static void remove() {
		BaseContextHandler.getContext().innerRemove();
	}
}
