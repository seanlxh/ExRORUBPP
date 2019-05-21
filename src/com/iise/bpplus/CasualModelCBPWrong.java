package com.iise.bpplus;

import com.iise.shudi.bp.BehavioralProfileSimilarity;
import com.iise.util.PetriNetConversion;
import org.jbpt.petri.NetSystem;
import org.jbpt.petri.Place;
import org.jbpt.petri.Transition;
import org.jbpt.petri.unfolding.ProperCompletePrefixUnfolding;
import org.processmining.exporting.DotPngExport;
import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.framework.plugin.ProvidedObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CasualModelCBPWrong {
    public static void main(String[] args){
        NetSystem net = new NetSystem();
        Place p0 = new Place("start");
        Place p1 = new Place("p1");
        Place p2 = new Place("p2");
        Place p3 = new Place("p3");
        Place p4 = new Place("pend");
        Transition a = new Transition("A");
        Transition b = new Transition("B");
        Transition c = new Transition("C");
        Transition d = new Transition("D");
        Transition e = new Transition("slient");

        net.addNode(a);
        net.addNode(b);
        net.addNode(c);
        net.addNode(d);
        net.addNode(e);
        net.addNode(p0);
        net.addNode(p1);
        net.addNode(p2);
        net.addNode(p3);
        net.addNode(p4);

        net.addFlow(p0, a);
        net.addFlow(a, p1);
        net.addFlow(p1, b);
        net.addFlow(b, p2);
        net.addFlow(p2, c);
        net.addFlow(c, p3);
        net.addFlow(p3, d);
        net.addFlow(d, p4);
        net.addFlow(p3, e);
        net.addFlow(e, p1);
        final NetSystem net1copy1 = net;
        net1copy1.getSourcePlaces().stream().forEach(p -> net1copy1.getMarking().put(p, 1));


        NetSystem net1 = new NetSystem();
        Place net1p0 = new Place("start");
        Place net1p1 = new Place("p1");
        Place net1p2 = new Place("p2");
        Place net1p3 = new Place("p3");
        Place net1p4 = new Place("p4");
        Place net1p5 = new Place("p5");
        Place net1p6 = new Place("p6");
        Place net1p8 = new Place("pend");
        Transition net1a = new Transition("A");
        Transition net1b = new Transition("B");
        Transition net1c = new Transition("C");
        Transition net1d = new Transition("D");
        Transition net1s1 = new Transition("slient1");
        Transition net1s2 = new Transition("slient2");
        Transition net1s3 = new Transition("slient3");

        net1.addNode(net1p0);
        net1.addNode(net1p1);
        net1.addNode(net1p2);
        net1.addNode(net1p3);
        net1.addNode(net1p4);
        net1.addNode(net1p5);
        net1.addNode(net1p6);
        net1.addNode(net1p8);
        net1.addNode(net1a);
        net1.addNode(net1b);
        net1.addNode(net1c);
        net1.addNode(net1d);
        net1.addNode(net1s1);
        net1.addNode(net1s2);
        net1.addNode(net1s3);

        net1.addFlow(net1p0, net1a);
        net1.addFlow(net1a, net1p1);
        net1.addFlow(net1p1, net1s1);
        net1.addFlow(net1s1, net1p2);
        net1.addFlow(net1s1, net1p3);
        net1.addFlow(net1p2, net1b);
        net1.addFlow(net1p2, net1c);
        net1.addFlow(net1b, net1p4);
        net1.addFlow(net1c, net1p5);
        net1.addFlow(net1p4, net1s2);
        net1.addFlow(net1p5, net1s2);
        net1.addFlow(net1s2, net1p6);
        net1.addFlow(net1p6, net1d);
        net1.addFlow(net1d, net1p8);
        net1.addFlow(net1p6, net1s3);
        net1.addFlow(net1s3, net1p1);




        final NetSystem net1copy2 = net1;
        net1copy2.getSourcePlaces().stream().forEach(p -> net1copy2.getMarking().put(p, 1));
        BehavioralProfileSimilarity bp = new BehavioralProfileSimilarity();
        float simBP = bp.similarity(net1copy1, net1copy2);
        System.out.println(simBP);


        BPPlusSimilarity bpp = new BPPlusSimilarity();
        bpp.initialize(net1copy1);
        bpp.initialize(net1copy2);
        bpp.numberingNodes(net1copy1, false);
        bpp.numberingNodes(net1copy2, false);
        bpp.deduceRelationMatrix(net1copy1);
        bpp.deduceRelationMatrix(net1copy2);
        float simBPP = bpp.similarity(net1copy1, net1copy2);
        System.out.println(simBPP);


        ProperCompletePrefixUnfolding pcp1 = new ProperCompletePrefixUnfolding(net1copy1);
        PetriNet pn1 = PetriNetConversion.convert(pcp1);
        ProvidedObject po2 = new ProvidedObject("petrinet", pn1);
        DotPngExport dpe2 = new DotPngExport();
        OutputStream image2 = null;
        try {
            image2 = new FileOutputStream("models/a.png");
            dpe2.export(po2, image2);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }
}
