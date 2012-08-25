package no.get.cas.ticket.registry;

import org.jasig.cas.authentication.Authentication;
import org.jasig.cas.authentication.ImmutableAuthentication;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.SimpleWebApplicationServiceImpl;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.TicketGrantingTicketImpl;
import org.jasig.cas.ticket.support.NeverExpiresExpirationPolicy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/META-INF/cas-server-integration-springcache-TestContext.xml")
public class InfinispanTicketRegistryTest {

    @Autowired
    private InfinispanTicketRegistry infinispanTicketRegistry;

    @Test
    public void updateTicketShouldOverwriteTicketInStorage() {
        Ticket ticket = getTicket();
        infinispanTicketRegistry.addTicket(ticket);
        assertThat(infinispanTicketRegistry.getTicket(ticket.getId()).isExpired()).isFalse();
        TicketGrantingTicket ticket2 = (TicketGrantingTicket) ticket;
        ticket2.expire();
        infinispanTicketRegistry.updateTicket(ticket);
        assertThat(infinispanTicketRegistry.getTicket(ticket.getId()).isExpired()).isTrue();
    }

    @Test
    public void addTicketExistsInCache() {
        Ticket ticket = getTicket();
        infinispanTicketRegistry.addTicket(ticket);
        assertThat(infinispanTicketRegistry.getTicket(ticket.getId())).isEqualTo(ticket);
    }

    @Test
    public void deleteTicketRemovesFromCacheReturnsTrue() {
        Ticket ticket = getTicket();
        infinispanTicketRegistry.addTicket(ticket);
        assertThat(infinispanTicketRegistry.deleteTicket(ticket.getId())).isTrue();
        assertThat(infinispanTicketRegistry.getTicket(ticket.getId())).isNull();
    }

    @Test
    public void deleteTicketOnNonExistingTicketReturnsFalse() {
        String ticketId = "does_not_exist";
        assertThat(infinispanTicketRegistry.deleteTicket(ticketId)).isFalse();
    }

    @Test
    public void getTicketReturnsTicketFromCacheOrNull() {
        Ticket ticket = getTicket();
        infinispanTicketRegistry.addTicket(ticket);
        assertThat(infinispanTicketRegistry.getTicket(ticket.getId())).isEqualTo(ticket);
        assertThat(infinispanTicketRegistry.getTicket("")).isNull();
    }

    private Ticket getTicket() {
        Principal principal = new SimpleWebApplicationServiceImpl("http://www.get.no");
        Authentication authentication = new ImmutableAuthentication(principal);
        return new TicketGrantingTicketImpl("123", authentication, new NeverExpiresExpirationPolicy());
    }
}
