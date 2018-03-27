package clustering;

import indexing.Index;
import indexing.Indexer;
import java.util.Collections;
import java.util.List;
import org.ejml.data.DenseMatrix64F;

/**
 *
 * @author eadams
 * @author denny
 */
public class KMeans {

    private int numCluster = 2;
    private Cluster[] clusters;
    private int maxIteration = 200;
    private double epsilon = 0;

    public void computeCluster(Indexer indexer, List<Index> indexs) {
        /**
        clusters = new Cluster[getNumCluster()];

        int iter = 0;
        // select random index as centroid for each clusters
        // then set initial mean
        //System.out.println(indexes.size());
        for (int c = 0; c < getNumCluster(); c++) {
        clusters[c] = new Cluster(indexer, indexes.get(c));
        System.out.println(indexes.get(c));
        }

        double centroidDistance = 200;

        while (iter < getMaxIteration() && centroidDistance > getEpsilon()) {
        // calculate mean


        System.out.println("ITERATION : " + iter);
        for (Cluster c : clusters) {
        c.getMembers().clear();
        }

        for (Index idx : indexes) {
        int choosedClusterNum = 0;
        double dist = 0;
        double prevDist = 1000000000;
        for (int c = 0; c < getNumCluster(); c++) {
        dist = getClusters()[c].getDistance(idx);

        //System.out.println("Distance " + idx + " from cluster-" + c + " = " + dist);
        //System.out.println(c + " " + dist);
        if (dist < prevDist) {
        choosedClusterNum = c;
        prevDist = dist;
        }
        }
        //System.out.println("Add index-" + idx + " to cluster-" + choosedClusterNum);
        getClusters()[choosedClusterNum].addMember(idx);
        }

        centroidDistance = 0;
        for (int c = 0; c < getNumCluster(); c++) {
        double prevMean = getClusters()[c].getMean();
        System.out.println("prevmean-0" + c + " = " + prevMean);
        double mean = getClusters()[c].calculateMean();
        System.out.println("mean-0" + c + " = " + mean);
        double cdist = mean - prevMean;
        centroidDistance += Math.abs(cdist);
        System.out.println("cdist-0" + c + " = " + Math.abs(cdist));
        }

        centroidDistance = centroidDistance / getNumCluster();
        System.out.println("eps " + centroidDistance);

        iter++;

        }
         */
        //Inisialisasi K titik sebagai titik2 pusat (centroids) awal dari grup
        int iter = 0;
        initializeCentroids(indexer, indexs);
        double cdist = 9999;
        while (iter < maxIteration && cdist > epsilon) {
            //System.out.println("ITERATION : " + iter);
            //Masukkan tiap objek dalam grup yang mempunyai titik pusat terdekat
            reorganizeIndexs(indexs);
            //Setelah selesai untuk semua objek, hitung kembali posisi K titik2 pusat yang baru
            cdist = calculateCentroids();
            System.out.println("cdist = " + cdist);
            //Ulangi langkah 2 dan 3 sampai titik2 pusat tidak berubah
            iter++;
        }

    }

    private void initializeCentroids(Indexer indexer, List<Index> indexs) {
        // choose index as centroid randomly
        Index selectedIdx = null;
        clusters = new Cluster[numCluster];
        Collections.shuffle(indexs);

        clusters[0] = new Cluster(indexer, indexs.get(0));
        //random.setSeed(indexs.size());
        for (int c = 1; c < numCluster; c++) {
            int numClusterHaveCentroid = c;

            double sumd = 0;
            double prevsumd = 0;

            for (Index idx : indexs) {
                for (int j = 0; j < numClusterHaveCentroid; j++) {
                    sumd += (clusters[j].getDistance(idx));
                }
                if (sumd > prevsumd) {
                    selectedIdx = idx;
                }
                prevsumd = sumd;
            }
            
            clusters[c] = new Cluster(indexer, selectedIdx);
            //System.out.println("========= INIT ====== " + c);
            //printCentroid(clusters[c].getCentroid());
        }
    }

    private double calculateCentroids() {
        DenseMatrix64F[] prevCentroid;
        double cdist = 0;
        for (Cluster c : clusters) {
            prevCentroid = c.getPrevCentroid();
            //System.out.println("========= PREV ====== " + c);
            //printCentroid(prevCentroid);
            cdist += c.getDistance(prevCentroid[0], prevCentroid[1], prevCentroid[2]);
            c.calculateMean();
            //System.out.println("========= NOW ====== " + c);
            //printCentroid(c.getCentroid());
        }

        return cdist / numCluster;
    }

    private void printCentroid(DenseMatrix64F[] centroid) {
        for (DenseMatrix64F c : centroid) {
            System.out.println("c-" + c);
            for (int i = 0; i < c.numRows; i++) {
                for (int j = 0; j < c.numCols; j++) {
                    System.out.print(c.get(i, j) + " ");
                }
                System.out.println();
            }
        }

    }

    private void reorganizeIndexs(List<Index> indexs) {
        for (Cluster c : clusters) {
            c.getMembers().clear();
        }
        for (Index index : indexs) {
            // select best cluster
            double dist = 0, prevDist = 65280;
            int cid = -1;
            for (int c = 0; c < numCluster; c++) {
                dist = clusters[c].getDistance(index);

                if (dist < prevDist) {
                    cid = c;
                    prevDist = dist;
                }
            }

            // add index to cluster
            if (cid > -1) {
                clusters[cid].addMember(index);
            }
        }
        //System.out.println("Add index-" + idx + " to cluster-" + choosedClusterNum);
    }

    /**
     * @return the numCluster
     */
    public int getNumCluster() {
        return numCluster;


    }

    /**
     * @param numCluster the numCluster to set
     */
    public void setNumCluster(int numCluster) {
        this.numCluster = numCluster;


    }

    /**
     * @return the maxIteration
     */
    public int getMaxIteration() {
        return maxIteration;






    }

    /**
     * @param maxIteration the maxIteration to set
     */
    public void setMaxIteration(int maxIteration) {
        this.maxIteration = maxIteration;






    }

    /**
     * @return the epsilon
     */
    public double getEpsilon() {
        return epsilon;






    }

    /**
     * @param epsilon the epsilon to set
     */
    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;






    }

    /**
     * @return the clusters
     */
    public Cluster[] getClusters() {
        return clusters;



    }
}
