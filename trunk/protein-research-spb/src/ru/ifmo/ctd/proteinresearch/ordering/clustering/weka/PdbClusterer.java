package ru.ifmo.ctd.proteinresearch.ordering.clustering.weka;

import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.StructureException;
import ru.ifmo.ctd.proteinresearch.ordering.clustering.ClusteringUtils;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.ConformationChain;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.ConformationGraph;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.VertexEdgeAnalyzer;
import weka.clusterers.HierarchicalClusterer;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ansokolmail@gmail.com
 */
public class PdbClusterer {
    public void run(String name) throws Exception {
        ConformationGraph cg = new ConformationGraph(name + "_costs.txt", name + ".zip", name + "/2LJI_optim%d_%d.pdb", 0);
        buildDataSet(cg);
        //Instances instances = new Instances();
    }

    private Instances buildDataSet(ConformationGraph cg) throws Exception {

        ConformationChain confChain = cg.getChain(0, 1);
        int numberOfVertices = cg.graph.getN();
        Structure structure = confChain.getStructure();
        Chain someChain = structure.getModel(structure.nrModels() - 1).get(0);
        int n = someChain.getAtomLength();
        ArrayList<Attribute> atts = new ArrayList<>(n);
        for (int i = 0; i < 3 * n; i++) {
            atts.add(new Attribute("x" + i));
            atts.add(new Attribute("y" + i));
            atts.add(new Attribute("z" + i));
        }
        Instances data = new Instances("graph", atts, 100);
        List<Chain> chains = new ArrayList<>();
        Chain reference = VertexEdgeAnalyzer.getBasicChain(cg, 0);
        chains.add(reference);
        for (int i = 1; i < numberOfVertices; i++) {
            chains.add(ConformationChain.align(reference, VertexEdgeAnalyzer.getBasicChain(cg, i)));
        }
        List<Instance> instances = new ArrayList<>();
        for (int i = 0; i < numberOfVertices; i++) {
            Instance instance = createInstance(chains.get(i), data);
            instances.add(instance);
            data.add(instance);
        }
        HierarchicalClusterer HC = new HierarchicalClusterer();


        //HC.setLinkType(new SelectedTag(tagNr,TAGS_LINK_TYPE));

        HC.setNumClusters(3);
        HC.setPrintNewick(true);
        HC.setDistanceIsBranchLength(false);
        HC.buildClusterer(data);
        int counter = 0;
        for (Instance instance : instances) {

            System.out.println(counter + " " + HC.clusterInstance(instance));
            counter++;
        }

        return data;
    }

    private Instance createInstance(Chain chain, Instances data) throws IOException, StructureException {
        // ConformationGraph cg = new ConformationGraph("2LJI_optim_costs.txt", "2LJI_optim.zip", "2LJI_optim/2LJI_optim%d_%d.pdb");
        double[] array = ClusteringUtils.toArray(chain);
        Instance instance = new DenseInstance(1, array);
        instance.setDataset(data);
        return instance;
    }


    public static void main(String[] args) throws Exception {
        new PdbClusterer().run("2LJI_optim");
    }
}