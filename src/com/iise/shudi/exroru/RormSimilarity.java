package com.iise.shudi.exroru;

import org.jbpt.petri.NetSystem;
import org.jbpt.petri.io.PNMLSerializer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Wang Shuhao
 */
public class RormSimilarity {

    public static void main(String[] args) throws Exception {
        // String filePath = "/Users/shudi/Desktop/multi_relation_1.pnml";
        // String filePath =
        // "/Users/shudi/Desktop/parallel_A_with_outer_loop.pnml";
        // String filePath = "/Users/shudi/Desktop/M15.pnml";

//        PNMLSerializer pnmlSerializer = new PNMLSerializer();
//        String filePath =
//                "C:\\Users\\Shudi\\Desktop\\rorm\\TC\\TC-FI.300.pnml";
//        NetSystem net = pnmlSerializer.parse(filePath);
//        RefinedOrderingRelationsMatrix rorm = new
//                RefinedOrderingRelationsMatrix((NetSystem) net.clone());
//        rorm.printMatrix();
//        rorm.print();

        PNMLSerializer pnmlSerializer = new PNMLSerializer();
        String filepath1 = "C:\\Users\\Shudi\\Desktop\\rorm\\TC\\TC-FI.050_DONG.pnml";
        String filepath2 = "C:\\Users\\Shudi\\Desktop\\rorm\\TC\\TC-FI.050.pnml";
        NetSystem net1 = pnmlSerializer.parse(filepath1);
        NetSystem net2 = pnmlSerializer.parse(filepath2);
        RormSimilarity rorm = new RormSimilarity();
        float sim = rorm.similarity(net1, net2);
        if(sim == Float.MIN_VALUE) {
            System.out.println("Invalid Net System");
        } else {
            System.out.println(sim);
        }
    }

    public float similarity(NetSystem net1, NetSystem net2) {
        RefinedOrderingRelationsMatrix rorm1 = new RefinedOrderingRelationsMatrix((NetSystem) net1.clone());
        RefinedOrderingRelationsMatrix rorm2 = new RefinedOrderingRelationsMatrix((NetSystem) net2.clone());
        if (!rorm1.isValid() || !rorm2.isValid()) {
            return Float.MIN_VALUE;
        }
        return similarityWithNever(rorm1, rorm2);
    }

    public float similarityWithoutNever(RefinedOrderingRelationsMatrix matrix1, RefinedOrderingRelationsMatrix matrix2) {
        List<String> tName1 = matrix1.gettName();
        List<String> tName2 = matrix2.gettName();
        Set<String> interNames = new HashSet<>();
        interNames.addAll(tName1);
        interNames.retainAll(tName2);
        /* intersectionWithoutNever */
        double causalInter = 0.0, inverseCausalInter = 0.0, concurrentInter = 0.0;
        for (String si : interNames) {
            int idx1i = tName1.indexOf(si);
            int idx2i = tName2.indexOf(si);
            for (String sj : interNames) {
                int idx1j = tName1.indexOf(sj);
                int idx2j = tName2.indexOf(sj);
                causalInter += matrix1.getCausalMatrix()[idx1i][idx1j]
                        .intersectionWithoutNever(matrix2.getCausalMatrix()[idx2i][idx2j]);
                inverseCausalInter += matrix1.getInverseCausalMatrix()[idx1i][idx1j]
                        .intersectionWithoutNever(matrix2.getInverseCausalMatrix()[idx2i][idx2j]);
                concurrentInter += matrix1.getConcurrentMatrix()[idx1i][idx1j]
                        .intersectionWithoutNever(matrix2.getConcurrentMatrix()[idx2i][idx2j]);
            }
        }
        /* unionWithoutNever */
        double causalUnion = 0.0, inverseCausalUnion = 0.0, concurrentUnion = 0.0;
        int causalUnionSize = 0, inverseCausalUnionSize = 0, concurrentUnionSize = 0;
        for (int i = 0; i < tName1.size(); ++i) {
            int idx2i = tName2.indexOf(tName1.get(i));
            for (int j = 0; j < tName1.size(); ++j) {
                int idx2j = tName2.indexOf(tName1.get(j));
                if (idx2i != -1 && idx2j != -1) {
                    causalUnion += matrix1.getCausalMatrix()[i][j].unionWithoutNever(matrix2.getCausalMatrix()[idx2i][idx2j]);
                    inverseCausalUnion += matrix1.getInverseCausalMatrix()[i][j]
                            .unionWithoutNever(matrix2.getInverseCausalMatrix()[idx2i][idx2j]);
                    concurrentUnion += matrix1.getConcurrentMatrix()[i][j]
                            .unionWithoutNever(matrix2.getConcurrentMatrix()[idx2i][idx2j]);
                    if (matrix1.getCausalMatrix()[i][j].relation != Relation.NEVER
                            || matrix2.getCausalMatrix()[idx2i][idx2j].relation != Relation.NEVER) {
                        ++causalUnionSize;
                    }
                    if (matrix1.getInverseCausalMatrix()[i][j].relation != Relation.NEVER
                            || matrix2.getInverseCausalMatrix()[idx2i][idx2j].relation != Relation.NEVER) {
                        ++inverseCausalUnionSize;
                    }
                    if (matrix1.getConcurrentMatrix()[i][j].relation != Relation.NEVER
                            || matrix2.getConcurrentMatrix()[idx2i][idx2j].relation != Relation.NEVER) {
                        ++concurrentUnionSize;
                    }
                } else {
                    causalUnion += matrix1.getCausalMatrix()[i][j].relation == Relation.NEVER ? 0
                            : matrix1.getCausalMatrix()[i][j].importance;
                    inverseCausalUnion += matrix1.getInverseCausalMatrix()[i][j].relation == Relation.NEVER ? 0
                            : matrix1.getInverseCausalMatrix()[i][j].importance;
                    concurrentUnion += matrix1.getConcurrentMatrix()[i][j].relation == Relation.NEVER ? 0
                            : matrix1.getConcurrentMatrix()[i][j].importance;
                    causalUnionSize += matrix1.getCausalMatrix()[i][j].relation == Relation.NEVER ? 0 : 1;
                    inverseCausalUnionSize += matrix1.getInverseCausalMatrix()[i][j].relation == Relation.NEVER ? 0 : 1;
                    concurrentUnionSize += matrix1.getConcurrentMatrix()[i][j].relation == Relation.NEVER ? 0 : 1;
                }
            }
        }
        for (int i = 0; i < tName2.size(); ++i) {
            int idx1i = tName1.indexOf(tName2.get(i));
            for (int j = 0; j < tName2.size(); ++j) {
                int idx1j = tName1.indexOf(tName2.get(j));
                if (idx1i == -1 || idx1j == -1) {
                    causalUnion += matrix2.getCausalMatrix()[i][j].relation == Relation.NEVER ? 0
                            : matrix2.getCausalMatrix()[i][j].importance;
                    inverseCausalUnion += matrix2.getInverseCausalMatrix()[i][j].relation == Relation.NEVER ? 0
                            : matrix2.getInverseCausalMatrix()[i][j].importance;
                    concurrentUnion += matrix2.getConcurrentMatrix()[i][j].relation == Relation.NEVER ? 0
                            : matrix2.getConcurrentMatrix()[i][j].importance;
                    causalUnionSize += matrix2.getCausalMatrix()[i][j].relation == Relation.NEVER ? 0 : 1;
                    inverseCausalUnionSize += matrix2.getInverseCausalMatrix()[i][j].relation == Relation.NEVER ? 0 : 1;
                    concurrentUnionSize += matrix2.getConcurrentMatrix()[i][j].relation == Relation.NEVER ? 0 : 1;
                }
            }
        }
        // Jaccard
        double causalSim = causalUnion == 0 ? 0 : causalInter / causalUnion;
        double inverseCausalSim = inverseCausalUnion == 0 ? 0 : inverseCausalInter / inverseCausalUnion;
        double concurrentSim = concurrentUnion == 0 ? 0 : concurrentInter / concurrentUnion;
        int unionSize = causalUnionSize + inverseCausalUnionSize + concurrentUnionSize;
        double causalWeight = ((double) causalUnionSize) / ((double) unionSize);
        double inverseCausalWeight = ((double) inverseCausalUnionSize) / ((double) unionSize);
        double concurrentWeight = ((double) concurrentUnionSize) / ((double) unionSize);
        System.out.println(causalSim + " " + inverseCausalSim + " " + concurrentSim);
        return (float) (causalSim * causalWeight + inverseCausalSim * inverseCausalWeight
                + concurrentSim * concurrentWeight);
    }

    public float similarityWithNever(RefinedOrderingRelationsMatrix matrix1, RefinedOrderingRelationsMatrix matrix2) {
        List<String> tName1 = matrix1.gettName();
        List<String> tName2 = matrix2.gettName();
        /* intersectionWithNever */
        Set<String> interNames = new HashSet<>();
        interNames.addAll(tName1);
        interNames.retainAll(tName2);
        double causalInter = 0.0, inverseCausalInter = 0.0, concurrentInter = 0.0;
        for (String si : interNames) {
            int idx1i = tName1.indexOf(si);
            int idx2i = tName2.indexOf(si);
            for (String sj : interNames) {
                int idx1j = tName1.indexOf(sj);
                int idx2j = tName2.indexOf(sj);
                causalInter += matrix1.getCausalMatrix()[idx1i][idx1j]
                        .intersectionWithNever(matrix2.getCausalMatrix()[idx2i][idx2j]);
                inverseCausalInter += matrix1.getInverseCausalMatrix()[idx1i][idx1j]
                        .intersectionWithNever(matrix2.getInverseCausalMatrix()[idx2i][idx2j]);
                concurrentInter += matrix1.getConcurrentMatrix()[idx1i][idx1j]
                        .intersectionWithNever(matrix2.getConcurrentMatrix()[idx2i][idx2j]);
            }
        }
        /* unionWithNever */
        Set<String> unionNames = new HashSet<>();
        unionNames.addAll(tName1);
        unionNames.addAll(tName2);
        double causalUnion = 0.0, inverseCausalUnion = 0.0, concurrentUnion = 0.0;
        for (String si : unionNames) {
            int idx1i = tName1.indexOf(si);
            int idx2i = tName2.indexOf(si);
            for (String sj : unionNames) {
                int idx1j = tName1.indexOf(sj);
                int idx2j = tName2.indexOf(sj);
                if (idx1i != -1 && idx2i != -1 && idx1j != -1 && idx2j != -1) {
                    causalUnion += matrix1.getCausalMatrix()[idx1i][idx1j].unionWithNever(matrix2.getCausalMatrix()[idx2i][idx2j]);
                    inverseCausalUnion += matrix1.getInverseCausalMatrix()[idx1i][idx1j].unionWithNever(matrix2.getInverseCausalMatrix()[idx2i][idx2j]);
                    concurrentUnion += matrix1.getConcurrentMatrix()[idx1i][idx1j].unionWithNever(matrix2.getConcurrentMatrix()[idx2i][idx2j]);
                } else if (idx1i != -1 && idx1j != -1) {
                    causalUnion += matrix1.getCausalMatrix()[idx1i][idx1j].importance;
                    inverseCausalUnion += matrix1.getInverseCausalMatrix()[idx1i][idx1j].importance;
                    concurrentUnion += matrix1.getConcurrentMatrix()[idx1i][idx1j].importance;
                } else if (idx2i != -1 && idx2j != -1) {
                    causalUnion += matrix2.getCausalMatrix()[idx2i][idx2j].importance;
                    inverseCausalUnion += matrix2.getInverseCausalMatrix()[idx2i][idx2j].importance;
                    concurrentUnion += matrix2.getConcurrentMatrix()[idx2i][idx2j].importance;
                }
            }
        }
        // Jaccard
        double causalSim = causalUnion == 0 ? 0 : causalInter / causalUnion;
        double inverseCausalSim = inverseCausalUnion == 0 ? 0 : inverseCausalInter / inverseCausalUnion;
        double concurrentSim = concurrentUnion == 0 ? 0 : concurrentInter / concurrentUnion;
        System.out.println(causalSim + " " + inverseCausalSim + " " + concurrentSim);
        return (float) ((causalSim + inverseCausalSim + concurrentSim) / 3);
    }
}
