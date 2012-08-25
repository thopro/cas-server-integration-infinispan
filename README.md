#CAS Infinispan Integration

TODO: Refactor so Cache is injected rather than cachemanager. Should support all implementations of cachemanagers.

Tested with CAS 3.5, Spring 3.1.1.RELEASE Infinispan 5.1.6

## Howto use:

Add dependencies to your pom:

    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>${spring.version}</version>
    </dependency>
    <dependency>
        <groupId>no.get</groupId>
        <artifactId>cas-server-integration-infinispan</artifactId>
        <version>${plugin.version}</version>
    </dependency>


Define the changed ticketRegistry.

Use your desired infinispan cache manager. Any cache configuration can be placed in a configuration file.


    <bean id="ticketRegistry" class="no.get.cas.ticket.registry.InfinispanTicketRegistry">
        <property name="cacheManager" ref="cacheManager"/>
    </bean>

   <bean id="cacheManager" class="org.infinispan.spring.support.embedded.InfinispanEmbeddedCacheManagerFactoryBean">
       <property name="configurationFileLocation" value="classpath:/META-INF/infinispan-config.xml"/>
   </bean>

    <bean id="placeholderConfigurer" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
        <property name="fileEncoding" value="UTF-8"/>
        <property name="location" value="classpath:/META-INF/cache.properties"/>
    </bean>
