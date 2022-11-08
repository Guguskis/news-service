package lt.liutikas.reddit.domain.port.out.cache;

public interface RemoveUserPort {

    void removeUserBySessionId(String sessionId);

}
