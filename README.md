#CAS Spring Cache Abstraction Integration


Tested with CAS 3.5, Spring 3.1.1.RELEASE Infinispan 5.1.6

## Howto use:

Add dependencies to your pom:

    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>${spring.version}</version>
    </dependency>


Define the changed ticketRegistry.

Use your desired cache manager, remember to add dependency in pom. Any cache configuration can be placed in a configuration file.


    <bean id="ticketRegistry" class="no.get.cas.ticket.registry.InfinispanTicketRegistry">
        <property name="cacheManager" ref="cacheManager"/>
    </bean>

    <bean id="cacheManager" class="org.springframework.cache.support.SimpleCacheManager">
        <property name="caches">
            <set>
                <bean class="org.springframework.cache.concurrent.ConcurrentMapCacheFactoryBean" p:name="${cache.name}"/>
            </set>
        </property>
    </bean>

    <bean id="placeholderConfigurer" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
        <property name="fileEncoding" value="UTF-8"/>
        <property name="location" value="classpath:/META-INF/cache.properties"/>
    </bean>
