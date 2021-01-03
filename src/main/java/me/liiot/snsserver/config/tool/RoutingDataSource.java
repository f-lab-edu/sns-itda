package me.liiot.snsserver.config.tool;

import me.liiot.snsserver.util.ClientDatabases;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/*
AbstractRoutingDataSource
: 조회 키를 기반으로 대상 DataSource 중 하나를 getConnection()로 호출하여 라우팅.
  AbstractRoutingDataSource는 targetDataSources라는 Map 오브젝트를 가지고 있는데
  여기에는 조회 키-DataSource 쌍들이 담겨져 있다. getConnection()에서 determineCurrentLookupKey()를
  호출하여 조회 키를 리턴받는다. 해당 키로 targetDataSource에 저장된 DataSource 중
  라우팅할 DataSource가 결정된다. 조회 키 결정 로직은 보통 스레드 바운드 트랜잭션 컨텍스트로 구현된다.
 */
public class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {

        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();

        return isReadOnly ? ClientDatabases.SLAVE : ClientDatabases.MASTER;
    }
}
