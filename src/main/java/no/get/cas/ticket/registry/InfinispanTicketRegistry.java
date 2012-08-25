package no.get.cas.ticket.registry;

import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.registry.AbstractDistributedTicketRegistry;

import java.util.Collection;


/**
 * TicketRegistry to support Spring Cache Abstraction.
 */

public final class InfinispanTicketRegistry extends AbstractDistributedTicketRegistry {

    private EmbeddedCacheManager cacheManager;

    private Cache<String, Ticket> cache;

    private Cache<String, Ticket> getCache() {
        return cacheManager.getCache("cas-store", true);
    }

    @Override
    protected void updateTicket(Ticket ticket) {
        getCache().put(ticket.getId(), ticket);
    }

    @Override
    protected boolean needsCallback() {
        return true;
    }

    /**
     * Add a ticket to the registry. Ticket storage is based on the ticket id.
     *
     * @param ticket The ticket we wish to add to the cache.
     */
    @Override
    public void addTicket(Ticket ticket) {
        getCache().put(ticket.getId(), ticket);
    }

    /**
     * Retrieve a ticket from the registry.
     *
     * @param ticketId the id of the ticket we wish to retrieve
     * @return the requested ticket.
     */
    @Override
    public Ticket getTicket(String ticketId) {
        Ticket ticket = getCache().get(ticketId);
        return getProxiedTicketInstance(ticket);
    }

    /**
     * Remove a specific ticket from the registry.
     *
     * @param ticketId The id of the ticket to delete.
     * @return true if the ticket was removed and false if the ticket did not
     *         exist.
     */
    @Override
    public boolean deleteTicket(String ticketId) {
        if (getTicket(ticketId) == null) {
            return false;
        } else {
            getCache().evict(ticketId);
            return true;
        }
    }

    /**
     *
     * Retrieve all tickets from the registry.
     *
     * Note! Usage of this method can be computational and I/O intensive and should not be used for other than
     * debugging.
     *
     * @return collection of tickets currently stored in the registry. Tickets
     *         might or might not be valid i.e. expired.
     */
    @Override
    public Collection<Ticket> getTickets() {
        return getCache().values();
    }

    public void setCacheManager(EmbeddedCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void setCache(final Cache<String, Ticket> cache) {
        this.cache = cache;
    }
}
