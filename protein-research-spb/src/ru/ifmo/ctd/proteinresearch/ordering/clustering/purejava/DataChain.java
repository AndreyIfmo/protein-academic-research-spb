package ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava;

import org.biojava.bio.structure.Chain;
import org.biojava.bio.structure.StructureException;
import ru.ifmo.ctd.proteinresearch.ordering.clustering.ClusteringUtils;

import java.util.Arrays;

/**
 * Created by Andrey on 24.12.13.
 */
public class DataChain {
    Chain chain;

    private boolean equals(Chain chain1, Chain chain2) throws StructureException {
        return Arrays.equals(ClusteringUtils.toArray(chain1), ClusteringUtils.toArray(chain2));
    }

    private int hashCode(Chain inputChain) throws StructureException {
        return Arrays.hashCode(ClusteringUtils.toArray(inputChain));
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DataChain dataChain = (DataChain) o;

        try {
            if (chain != null ? !equals(chain, dataChain.chain) : dataChain.chain != null) {
                return false;
            }
        } catch (StructureException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public int hashCode() {
        try {
            return chain != null ? hashCode(chain) : 0;
        } catch (StructureException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
