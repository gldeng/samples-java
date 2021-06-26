package net.corda.samples.pingpong.flows;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.utilities.UntrustworthyData;

@InitiatingFlow
@StartableByRPC
public class Ping extends FlowLogic<Void> {

    private final Party counterparty;

    public Ping(Party counterparty) {
        this.counterparty = counterparty;
    }

    @Suspendable
    @Override
    public Void call() throws FlowException {
        final FlowSession counterpartySession = initiateFlow(counterparty);
        getLogger().info("Pinging "+counterparty);
        final UntrustworthyData<String> counterpartyData = counterpartySession.sendAndReceive(String.class, "ping");
        counterpartyData.unwrap( msg -> {
            getLogger().info("Received "+msg+" from "+counterpartySession.getCounterparty().getName());
            assert(msg.equals("pong"));
            return true;
        });
        return null;
    }
}
