package com.iise.bpplus;
import java.util.Random;
import com.iise.shudi.bp.BehavioralProfileSimilarity;
import org.jbpt.bp.BehaviouralProfile;
import org.jbpt.bp.CausalBehaviouralProfile;
import org.jbpt.bp.construct.BPCreatorNet;
import org.jbpt.bp.construct.BPCreatorUnfolding;
import org.jbpt.bp.construct.CBPCreatorTree;
import org.jbpt.bp.construct.CBPCreatorUnfolding;
import org.jbpt.petri.NetSystem;
import org.jbpt.petri.Node;
import org.jbpt.petri.Place;
import org.jbpt.petri.Transition;
import org.jbpt.petri.io.PNMLSerializer;
import org.jbpt.bp.RelSetType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class CBPSim {

public static void main(String[] args){
    int times = 30;

    File folder = new File("models/efficiency/TC");
    File[] arModels = folder.listFiles(new FileNameSelector("pnml"));
    PNMLSerializer pnmlSerializer = new PNMLSerializer();
    ArrayList arrays = new ArrayList();
    NetSystem net = new NetSystem();
    for(int i=0;i<arModels.length; i++)
    {
        String fpModelP = arModels[i].getName();
        String filepathP = arModels[i].getAbsolutePath();
        System.out.println(i+filepathP);
        net = pnmlSerializer.parse(filepathP);
        //net.getMarking().put((Place) pnmlSerializer.nodes.get("P0"),1);
        arrays.add(net);
    }

    System.out.println(arrays.size());

    Random ran1 = new Random(10);
    int num = 0;
    double before1 = System.nanoTime();
    for (int i = 0 ; i < arrays.size(); i ++){
        for (int j = i ; j < arrays.size(); j = j + 1){
            NetSystem net1 = (NetSystem) arrays.get(i);
            NetSystem net2 = (NetSystem) arrays.get(j);
            BehavioralProfileSimilarity bpsim = new BehavioralProfileSimilarity();
            double simBP = bpsim.similarity(net1, net2);
            System.out.println(num);
            num++;
        }
    }
    double after1 = System.nanoTime();
    System.out.println("BP: " + (after1 - before1)/num);
    long before = System.nanoTime();
    long after = System.nanoTime();
//
//
//    num = 0;
//    before1 = System.nanoTime();
//    for (int i = 0 ; i < arrays.size(); i ++){
//        for (int j = i ; j < arrays.size(); j ++){
//                NetSystem net1 = (NetSystem) arrays.get(i);
//                NetSystem net2 = (NetSystem) arrays.get(j);
//                BPPlusSimilarity bpp = new BPPlusSimilarity();
//                bpp.initialize(net1);
//                bpp.initialize(net2);
//
//                bpp.numberingNodes(net1, false);
//                bpp.numberingNodes(net2, false);
//
//            bpp.deduceRelationMatrix(net1);
//            bpp.deduceRelationMatrix(net2);
//
//            double simBPP = bpp.similarity(net1, net2);
//            num++;
//            System.out.println(num);
//        }
//    }
//    after1 = System.nanoTime();
//    System.out.println("BP+: " + (after1 - before1)/num);
//

//
    num = 0;
//    HashMap<String,Integer> map1 = new HashMap<>();
//    HashMap<String,Integer> map = new HashMap<>();
//    HashMap<Integer,Integer> map2 = new HashMap<>();
//    map2.put(0,0);
//    map2.put(1,0);
    long totaltime = 0;
    for (int i = 0 ; i < arrays.size()-1; i ++){
        for (int j = i ; j < arrays.size(); j = j + 1) {
            System.out.println("1:"+i+"2:"+j);
//        for (int j = i ; j < arrays.size(); j ++){
            NetSystem net1 = (NetSystem) arrays.get(i);
            NetSystem net2 = (NetSystem) arrays.get(j);
            final NetSystem net1copy = net1;
            final NetSystem net2copy = net2;
            net1copy.getSourcePlaces().stream().forEach(p -> net1copy.getMarking().put(p, 1));
            net2copy.getSourcePlaces().stream().forEach(p -> net2copy.getMarking().put(p, 1));
            before = System.nanoTime();
//      NetSystem net2 = (NetSystem) arrays.get(j);
            CausalBehaviouralProfile<NetSystem, Node> cbp1 = CBPCreatorUnfolding.getInstance().deriveCausalBehaviouralProfile(net1copy);
            CausalBehaviouralProfile<NetSystem, Node> cbp2 = CBPCreatorUnfolding.getInstance().deriveCausalBehaviouralProfile(net2copy);
//       BehaviouralProfile<NetSystem, Node> bp = BPCreatorUnfolding.getInstance().deriveRelationSet(net1copy);
//            RelSetType[][] res1 = bp.getMatrix();
//        RelSetType[][] res2 = cbp1.getMatrix();
//        boolean[][] res3 = cbp1.getCooccurrenceMatrix();
//        RelSetType[][] res6 = cbp2.getMatrix();
//        boolean[][] res7 = cbp2.getCooccurrenceMatrix();
            after = System.nanoTime();

//            RelSetType[][] res4 = cbp2.getMatrix();
//        for (int k = 0; k < res1.length; k++) {
//            for (int row = 0; row < res1.length; row++) {
//                if(map1.containsKey(res1[k][row].toString())){
//                    map1.put(res1[k][row].toString(),map1.get(res1[k][row].toString())+1);
//                }else{
//                    map1.put(res1[k][row].toString(),1);
//                }
//            }
//        }
//        for (int k = 0; k < res2.length; k++) {
//            for (int row = 0; row < res2.length; row++) {
//                if(map.containsKey(res2[k][row].toString())){
//                    map.put(res2[k][row].toString(),map.get(res2[k][row].toString())+1);
//                }else{
//                    map.put(res2[k][row].toString(),1);
//                }
//            }
//        }
//        for (int k = 0; k < res3.length; k++) {
//            for (int row = 0; row < res3.length; row++) {
//                int tmp = (res3[k][row]==true ? 1:0);
//                map2.put(tmp,map2.get(tmp)+1);
//
//            }
//        }

//            BPPlusSimilarity bpp = new BPPlusSimilarity();
//            bpp.initialize(net1);
//            bpp.initialize(net2);
//            bpp.numberingNodes(net1, false);
//            bpp.numberingNodes(net2, false);
//            bpp.deduceRelationMatrix(net1);
//            bpp.deduceRelationMatrix(net2);
//            double simBPP = bpp.similarity(net1, net2);
            num++;

            System.out.println(num + ":" + (after - before));
            totaltime = totaltime + (after - before);
        }
        }

    ;
    System.out.println("CBP: " + totaltime/num);

//
//    宽度
//    NetSystem net = new NetSystem();
//    Place p1 = new Place("1");
//    Transition a = new Transition("a");
//    Transition b = new Transition("b");
//    Place p2 = new Place("2");
//    net.addNode(a);
//    net.addNode(p1);
//    net.getMarking().put(p1,1);
//    net.addNode(b);
//    net.addNode(p2);
//    net.addFlow(p1, a);
//    net.addFlow(b, p2);
//
//    for (int i = 0 ; i < times ; i ++){
//        Place tmpp1 = new Place("p1"+i);
//        Transition tmpt = new Transition("t"+i);
//        Place tmpp2 = new Place("p2"+i);
//        net.addNode(tmpp1);
//        net.addNode(tmpt);
//        net.addNode(tmpp2);
//        net.addFlow(a, tmpp1);
//        net.addFlow(tmpp1, tmpt);
//        net.addFlow(tmpt, tmpp2);
//        net.addFlow(tmpp2, b);
//    }
//
//
//    深度
//    NetSystem net = new NetSystem();
//    Place p1 = new Place("1");
//    Transition a = new Transition("a");
//    Transition b = new Transition("b");
//    Place p2 = new Place("2");
//    net.addNode(a);
//    net.addNode(p1);
//    net.getMarking().put(p1,1);
//    net.addNode(b);
//    net.addNode(p2);
//    net.addFlow(p1, a);
//    net.addFlow(b, p2);
//
//    for (int i = 0 ; i < 5 ; i ++){
//        Transition last = a;
//        for (int j = 1 ; j <= times ; j ++){
//            if(j < times){
//                Place tmpp1 = new Place("p1"+i);
//                Transition tmpt = new Transition("t"+i);
//                net.addNode(tmpp1);
//                net.addNode(tmpt);
//                net.addFlow(last, tmpp1);
//                net.addFlow(tmpp1, tmpt);
//                last = tmpt;
//            }
//            else{
//                Place tmpp1 = new Place("p1"+i);
//                net.addNode(tmpp1);
//                net.addFlow(last, tmpp1);
//                net.addFlow(tmpp1, b);
//            }
//        }
//    }

    //官方提供
//    NetSystem net = new NetSystem();
//    Transition a = new Transition("a");
//    Transition b = new Transition("b");
//    Transition c = new Transition("c");
//    Transition d = new Transition("d");
//    Transition e = new Transition("e");
//
//    net.addNode(a);
//    net.addNode(b);
//    net.addNode(c);
//    net.addNode(d);
//    net.addNode(e);
//
//    Place p1 = new Place("1");
//    Place p2 = new Place("2");
//    Place p3 = new Place("3");
//    Place p4 = new Place("4");
//
//    net.addNode(p1);
//    net.getMarking().put(p1,1);
//    net.addNode(p2);
//    net.addNode(p3);
//    net.addNode(p4);
//
//    net.addFlow(p1, a);
//    net.addFlow(p1, b);
//    net.addFlow(a, p2);
//    net.addFlow(b, p2);
//    net.addFlow(p2, c);
//    net.addFlow(c, p3);
//    net.addFlow(p3, d);
//    net.addFlow(d, p2);
//    net.addFlow(p3, e);
//    net.addFlow(e, p4);


    double before4 = System.nanoTime();
    CausalBehaviouralProfile<NetSystem, Node> cbp = CBPCreatorUnfolding.getInstance().deriveCausalBehaviouralProfile(net);
    boolean[][] res = cbp.getCooccurrenceMatrix();
    for (boolean[] tmp : res){
        for (boolean tmpp : tmp){
            System.out.println(tmpp);
        }
    }
    RelSetType[][] res1 = cbp.getMatrix();
    for (RelSetType[] tmp : res1){
        for (RelSetType tmpp : tmp){
            System.out.println(tmpp);
        }
    }
    double after4 = System.nanoTime();
    System.out.println("CBP: " + (after4 - before4)*2);


    double before3 = System.nanoTime();
    BehavioralProfileSimilarity bpsim = new BehavioralProfileSimilarity();
    double simBP = bpsim.similarity(net, net);
    System.out.println(simBP);
    double after3 = System.nanoTime();
    System.out.println("BP: " + (after3 - before3)*2);


//    double before2 = System.nanoTime();
//    BPPlusSimilarity bpp = new BPPlusSimilarity();
//    bpp.initialize(net);
//    bpp.numberingNodes(net, false);
//    bpp.deduceRelationMatrix(net);
//    double simBPP = bpp.similarity(net, net);
//    System.out.println(simBPP);
//    double after2= System.nanoTime();
//    System.out.println("BP+: " + (after2 - before2)*2);

//    NetSystem net = new NetSystem();

//    NetSystem net = new NetSystem();
//
//    Transition a = new Transition("a");
//    Transition b = new Transition("b");
//    Transition c = new Transition("c");
//    Transition d = new Transition("d");
//    Transition e = new Transition("e");
//
//    net.addNode(a);
//    net.addNode(b);
//    net.addNode(c);
//    net.addNode(d);
//    net.addNode(e);
//
//    Place p1 = new Place("1");
//    Place p2 = new Place("2");
//    Place p3 = new Place("3");
//    Place p4 = new Place("4");
//
//    net.addNode(p1);
//    net.getMarking().put(p1,1);
//    net.addNode(p2);
//    net.addNode(p3);
//    net.addNode(p4);
//
//    net.addFlow(p1, a);
//    net.addFlow(p1, b);
//    net.addFlow(a, p2);
//    net.addFlow(b, p2);
//    net.addFlow(p2, c);
//    net.addFlow(c, p3);
//    net.addFlow(p3, d);
//    net.addFlow(d, p2);
//    net.addFlow(p3, e);
//    net.addFlow(e, p4);
//
//    BehaviouralProfile<NetSystem, Node> cbp = CBPCreatorTree.getInstance().deriveCausalBehaviouralProfile(net);
//    BehaviouralProfile<NetSystem, Node> bp = BPCreatorNet.getInstance().deriveRelationSet(net);

////    File folder = new File("models/efficacy/TC");
////    File[] arModels = folder.listFiles(new FileNameSelector("pnml"));
//    File folder = new File("models/efficiency/TC");
//    File[] arModels = folder.listFiles(new FileNameSelector("pnml"));
//    PNMLSerializer pnmlSerializer = new PNMLSerializer();
//    for(int i=0;i<arModels.length; i++)
//    {
//        String fpModelP = arModels[i].getName();
//        String filepathP = arModels[i].getAbsolutePath();
//        NetSystem netP = pnmlSerializer.parse(filepathP);
//    //    CausalBehaviouralProfile<NetSystem, Node> cbp = CBPCreatorUnfolding.getInstance().deriveCausalBehaviouralProfile(netP);
//    //    BehaviouralProfile<NetSystem, Node> bp = BPCreatorNet.getInstance().deriveRelationSet(netP);
//        org.jbpt.bp.RelSetType[][] res1 = bp.getMatrix();
//     //   org.jbpt.bp.RelSetType[][] res2 = cbp.getMatrix();
//        //boolean[][] res = cbp.getCooccurrenceMatrix();
//        for (int t = 0 ; t < res1.length; t ++){
//            for (int j = 0 ; j < res1[0].length; j ++){
//                System.out.println(res1[t][j]);
//            }
//        }
//    }
    }
}
