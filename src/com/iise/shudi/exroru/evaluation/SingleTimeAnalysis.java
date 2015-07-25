package com.iise.shudi.exroru.evaluation;

import com.iise.shudi.exroru.RefinedOrderingRelation;
import com.iise.shudi.exroru.RormSimilarity;
import org.jbpt.petri.NetSystem;
import org.jbpt.petri.io.PNMLSerializer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class SingleTimeAnalysis {
    public static final String ROOT_FOLDER = "C:\\Users\\Shudi\\Desktop\\rorm\\";
//    public static final String ROOT_FOLDER = "E:\\wangshuhao\\Documents\\ExRORU\\";

    public static void main(String[] args) throws Exception {
        RefinedOrderingRelation.SDA_WEIGHT = 0.0;
        RefinedOrderingRelation.IMPORTANCE = true;
        SingleTimeAnalysis sta = new SingleTimeAnalysis();

        BufferedWriter writer = new BufferedWriter(new FileWriter(ROOT_FOLDER +
            "ExRORU_SingleTimeAnalysis_150625a.csv"));
        writer.write(",totalTime,init1,causal1,concurrent1,sda1,importance1"
            + ",init1,causal2,concurrent2,sda2,importance2,similarity");
        writer.newLine();
        sta.analyze(writer);
        writer.close();
    }

    public void analyze(BufferedWriter writer) throws Exception {
        File[] dgFiles = new File(ROOT_FOLDER + "DG").listFiles();
        File[] tcFiles = new File(ROOT_FOLDER + "TC").listFiles();
        File[] sapFiles = new File(ROOT_FOLDER + "SAP").listFiles();

        // load net systems
        List<NetSystem> dgNets = loadNets(dgFiles);
        List<NetSystem> tcNets = loadNets(tcFiles);
        List<NetSystem> sapNets = loadNets(sapFiles);

        // compute time
        computeTime(writer, dgNets);
        computeTime(writer, tcNets);
        computeTime(writer, sapNets);
    }

    private List<NetSystem> loadNets(File[] files) throws Exception {
        List<NetSystem> nets = new ArrayList<>();
        PNMLSerializer pnmlSerializer = new PNMLSerializer();
        for(int i = 0; i < files.length; ++i) {
            NetSystem net = pnmlSerializer.parse(files[i].getAbsolutePath());
            net.setName(files[i].getName());
            nets.add(net);
        }
        return nets;
    }

    private void computeTime(BufferedWriter writer, List<NetSystem> nets) throws Exception {
        int totalCount = nets.size() * (nets.size() - 1) / 2, finish = 0;
        RormSimilarity rorm = new RormSimilarity();
        for(int p = 0; p < nets.size(); ++p) {
            for(int q = p + 1; q < nets.size(); ++q) {
                System.out.println((++finish) + "/" + totalCount + " " + nets.get(p).getName() + " & " + nets.get(q).getName());
                long[] times = rorm.similarityWithTime(nets.get(p), nets.get(q));
                writer.write(nets.get(p).getName() + " & " + nets.get(q).getName());
                for (long t : times) {
                    writer.write("," + t);
                }
                writer.newLine();
            }
        }
    }
}
