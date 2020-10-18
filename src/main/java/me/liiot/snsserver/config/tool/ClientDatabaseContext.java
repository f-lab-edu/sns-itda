package me.liiot.snsserver.config.tool;

import me.liiot.snsserver.util.ClientDatabases;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/*
ThreadLocal<T>
: ThreadLocal 변수 제공.
- ThreadLocal 변수는 멀티 스레드 환경에서 각 스레드마다 독립적이다. (즉, 각 스레드가 별도의 변수처럼 사용할 수 있다.)
- 스레드 영역에 변수를 저장하기 때문에, 특정 스레드가 실행하는 모든 코드에서 해당 변수 값을 사용할 수 있음
- get(), set(), remove()를 통해 값에 대해 접근할 수 있다.
 */
public class ClientDatabaseContext {
    private static ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    public static void set(String clientDatabase) {
        Assert.notNull(clientDatabase, "clientDatabase cannot be null");
        Assert.isTrue(StringUtils.equals(clientDatabase, ClientDatabases.MASTER) ||
                                StringUtils.equals(clientDatabase, ClientDatabases.SLAVE),
                      "clientDatabase can be only master or slave");
        CONTEXT.set(clientDatabase);
    }

    public static String getClientDatabase() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
