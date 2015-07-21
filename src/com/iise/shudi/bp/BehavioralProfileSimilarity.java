package com.iise.shudi.bp;

import org.jbpt.alignment.Alignment;
import org.jbpt.bp.BehaviouralProfile;
import org.jbpt.bp.construct.BPCreatorUnfolding;
import org.jbpt.bp.sim.*;
import org.jbpt.petri.NetSystem;
import org.jbpt.petri.Node;

public class BehavioralProfileSimilarity {

    public float similarity(NetSystem net1, NetSystem net2) {
        net1.getSourcePlaces().stream().forEach(p -> net1.getMarking().put(p, 1));
        net2.getSourcePlaces().stream().forEach(p -> net2.getMarking().put(p, 2));
        BehaviouralProfile<NetSystem, Node> bp1 = BPCreatorUnfolding.getInstance().deriveRelationSet(net1);
        BehaviouralProfile<NetSystem, Node> bp2 = BPCreatorUnfolding.getInstance().deriveRelationSet(net2);
        Alignment<BehaviouralProfile<NetSystem,Node>, Node> alignment = new Alignment<>(bp1, bp2);
        net1.getTransitions().forEach(t1 -> net2.getTransitions().stream()
                        .filter(t2 -> t1.getName().equals(net2.getName()))
                        .forEach(t2 -> alignment.addElementaryCorrespondence(t1, t2))
        );
        AggregatedSimilarity<BehaviouralProfile<NetSystem,Node>, NetSystem, Node>           agg = new AggregatedSimilarity<>();
        agg.setWeights(1, 1, 1, 1, 1);
        return (float) agg.score(alignment);
    }

}
