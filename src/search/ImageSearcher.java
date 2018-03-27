package search;

import com.google.code.jcbir.Constants;

import clustering.Cluster;
import indexing.Index;
import featureextraction.FNormFeature;
import indexing.ClusterReader;
import indexing.IndexReader;
import util.GraphicsUtilities;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;
import org.ejml.data.DenseMatrix64F;

/**
 *
 * @author eadams
 * @author denny
 */
public class ImageSearcher {

    private FNormFeature feature = new FNormFeature();

    public List<ImageResult> search(BufferedImage queryImage, double alpha, double beta, boolean useCluster) throws IOException {
        IndexReader indexReader = new IndexReader();
        indexReader.open();
        List<Cluster> clusters = indexReader.getClusters();

        indexReader.close();
        List<Index> indexes = new ArrayList<Index>();
        ClusterReader clusterReader = new ClusterReader();
        Index queryIndex = clusterReader.getIndex(queryImage);
        if (useCluster) {
            int cid = -1;
            double dist = 0;
            double prevDist = 65280;
            for (int c = 0; c < clusters.size(); c++) {
                dist = clusters.get(c).getDistance(queryIndex);
                //System.out.println(dist + " < " + prevDist);
                if (dist < prevDist) {
                    cid = c;
                    prevDist = dist;
                }

            }
            //System.out.println(cid);
            if (cid > -1) {
                clusterReader.open(cid);
                indexes.addAll(clusterReader.getIndexes());
                clusterReader.close();
                System.out.println(indexes.size());
            }
        } else {
            for (int c = 0; c < clusters.size(); c++) {
                clusterReader.open(c);
                indexes.addAll(clusterReader.getIndexes());
                clusterReader.close();
            }
        }

        if (indexes.isEmpty()) {
            return Collections.EMPTY_LIST;
        }


        List<Index> filteredIndexes = progressiveRetrievalStrategy(indexes, queryIndex, clusterReader,
                alpha, beta);
        return rankedSimilarity(queryIndex, clusterReader, filteredIndexes);
    }

    private List<Index> progressiveRetrievalStrategy(List<Index> indexes, Index queryIndex, ClusterReader reader,
            double alpha, double beta) {
        // do progressive filtering strategy
        List<Index> filteredIndexs = new ArrayList<Index>();
        double[] RLLVarianceQ = null;
        double[] GLLVarianceQ = null;
        double[] BLLVarianceQ = null;
        double[] RLHVarianceQ = null;
        double[] GLHVarianceQ = null;
        double[] BLHVarianceQ = null;
        double[] RHLVarianceQ = null;
        double[] GHLVarianceQ = null;
        double[] BHLVarianceQ = null;
        DenseMatrix64F[] RLLFeatureQ = null;
        DenseMatrix64F[] GLLFeatureQ = null;
        DenseMatrix64F[] BLLFeatureQ = null;


        RLLVarianceQ = reader.toDoubles(queryIndex.getRLLhaarvariance());
        GLLVarianceQ = reader.toDoubles(queryIndex.getGLLhaarvariance());
        BLLVarianceQ = reader.toDoubles(queryIndex.getBLLhaarvariance());
        RLHVarianceQ = reader.toDoubles(queryIndex.getRLHhaarvariance());
        GLHVarianceQ = reader.toDoubles(queryIndex.getGLHhaarvariance());
        BLHVarianceQ = reader.toDoubles(queryIndex.getBLHhaarvariance());
        RHLVarianceQ = reader.toDoubles(queryIndex.getRHLhaarvariance());
        GHLVarianceQ = reader.toDoubles(queryIndex.getGHLhaarvariance());
        BHLVarianceQ = reader.toDoubles(queryIndex.getBHLhaarvariance());
        RLLFeatureQ = reader.toMatrixs(queryIndex.getRLLhaarfeature());
        GLLFeatureQ = reader.toMatrixs(queryIndex.getGLLhaarfeature());
        BLLFeatureQ = reader.toMatrixs(queryIndex.getBLLhaarfeature());




        for (Index index : indexes) {
            if (progressive(reader, alpha, beta, index,
                    RLLVarianceQ, GLLVarianceQ, BLLVarianceQ,
                    RLHVarianceQ, GLHVarianceQ, BLHVarianceQ,
                    RHLVarianceQ, GHLVarianceQ, BHLVarianceQ,
                    RLLFeatureQ, GLLFeatureQ, BLLFeatureQ)) {
                filteredIndexs.add(index);
            }


        }
        return filteredIndexs;
    }

    private boolean progressive(ClusterReader reader, double alpha, double beta, Index index, double[] rllvarQ,
            double[] gllvarQ, double[] bllvarQ, double[] rlhvarQ,
            double[] glhvarQ, double[] blhvarQ, double[] rhlvarQ,
            double[] ghlvarQ, double[] bhlvarQ,
            DenseMatrix64F[] rllfeatureQ, DenseMatrix64F[] gllfeatureQ,
            DenseMatrix64F[] bllfeatureQ) {

        double[] RLLVarianceD = reader.toDoubles(index.getRLLhaarvariance());
        double[] GLLVarianceD = reader.toDoubles(index.getGLLhaarvariance());
        double[] BLLVarianceD = reader.toDoubles(index.getBLLhaarvariance());
        double[] RLHVarianceD = reader.toDoubles(index.getRLHhaarvariance());
        double[] GLHVarianceD = reader.toDoubles(index.getGLHhaarvariance());
        double[] BLHVarianceD = reader.toDoubles(index.getBLHhaarvariance());
        double[] RHLVarianceD = reader.toDoubles(index.getRHLhaarvariance());
        double[] GHLVarianceD = reader.toDoubles(index.getGHLhaarvariance());
        double[] BHLVarianceD = reader.toDoubles(index.getBHLhaarvariance());
        DenseMatrix64F[] RLLFeatureD = reader.toMatrixs(index.getRLLhaarfeature());
        DenseMatrix64F[] GLLFeatureD = reader.toMatrixs(index.getGLLhaarfeature());
        DenseMatrix64F[] BLLFeatureD = reader.toMatrixs(index.getBLLhaarfeature());

        for (int j = Constants.DECOMPOSITION_LEVEL - 1; j >= 0; j--) {

            if (!roughFilter(beta, rllvarQ[j], gllvarQ[j], bllvarQ[j], RLLVarianceD[j],
                    GLLVarianceD[j], BLLVarianceD[j])) {
                return false;
            }

            if (!roughFilter(beta, rlhvarQ[j], glhvarQ[j], blhvarQ[j], RLHVarianceD[j],
                    GLHVarianceD[j], BLHVarianceD[j])) {
                return false;
            }

            if (!roughFilter(beta, rhlvarQ[j], ghlvarQ[j], bhlvarQ[j], RHLVarianceD[j],
                    GHLVarianceD[j], BHLVarianceD[j])) {
                return false;
            }

            if (preciseFilter(alpha, rllfeatureQ[j], gllfeatureQ[j], bllfeatureQ[j],
                    RLLFeatureD[j], GLLFeatureD[j], BLLFeatureD[j])) {
                return false;
            }


        }

        return true;
    }

    private boolean roughFilter(double beta, double sigmaRQ, double sigmaGQ, double sigmaBQ,
            double sigmaRD, double sigmaGD, double sigmaBD) {
        return ((beta * sigmaRQ) < sigmaRD)
                && (sigmaRD < (sigmaRQ / beta))
                && ((beta * sigmaGQ) < sigmaGD)
                && (sigmaGD < (sigmaGQ / beta))
                && ((beta * sigmaBQ) < sigmaBD)
                && (sigmaBD < (sigmaBQ / beta));
    }

    private boolean preciseFilter(double alpha, DenseMatrix64F rq, DenseMatrix64F gq, DenseMatrix64F bq,
            DenseMatrix64F rd, DenseMatrix64F gd, DenseMatrix64F bd) {
        double redSimilarity = feature.calculateSimilarity(rq, rd);
        double greenSimilarity = feature.calculateSimilarity(gq, gd);
        double blueSimilarity = feature.calculateSimilarity(bq, bd);
        double similarity = (redSimilarity + greenSimilarity + blueSimilarity) / 3;

        return similarity < alpha;
    }

    public List<ImageResult> rankedSimilarity(Index queryIndex, ClusterReader reader,
            List<Index> filteredIndexs) {
        // calculate and order results by their similarities
        //List<Index> indexes = reader.getIndexes();
        List<ImageResult> imageResults = new ArrayList<ImageResult>();
        //List<ImageResult> imageResults = new ArrayList<ImageResult>(indexes.size());
        DenseMatrix64F redFeatureA = null;
        DenseMatrix64F greenFeatureA = null;
        DenseMatrix64F blueFeatureA = null;


        redFeatureA = reader.toMatrix(queryIndex.getRHaarFeature());
        greenFeatureA = reader.toMatrix(queryIndex.getGHaarFeature());
        blueFeatureA = reader.toMatrix(queryIndex.getBHaarFeature());




        for (Index index : filteredIndexs) {
            DenseMatrix64F redFeatureB = null;
            DenseMatrix64F greenFeatureB = null;
            DenseMatrix64F blueFeatureB = null;
            redFeatureB = reader.toMatrix(index.getRHaarFeature());
            greenFeatureB = reader.toMatrix(index.getGHaarFeature());
            blueFeatureB = reader.toMatrix(index.getBHaarFeature());



            double redSimilarity = feature.calculateSimilarity(redFeatureA, redFeatureB);
            double greenSimilarity = feature.calculateSimilarity(greenFeatureA, greenFeatureB);
            double blueSimilarity = feature.calculateSimilarity(blueFeatureA, blueFeatureB);
            double similarity = (redSimilarity + greenSimilarity + blueSimilarity) / 3;

            //System.out.println("RED A "+reader.parseMatrix(redFeatureA));
            //System.out.println("RED B "+reader.parseMatrix(redFeatureB));
            //System.out.println("RSim = " + redSimilarity);
            //System.out.println(redFeatureA.getNumCols());

            ImageResult imageResult = new ImageResult();
            imageResult.setPath(index.getFilePath());
            imageResult.setSimilarity(similarity);
           
            try {
                BufferedImage bufferedImage = ImageIO.read(new File(index.getFilePath()));
                imageResult.setBufferedImage(GraphicsUtilities.resizeImage(bufferedImage, Constants.THUMB_WIDTH,
                        Constants.THUMB_HEIGHT));
                imageResults.add(imageResult);
            } catch (IOException ex) {
            }

        }
        imageResults = null;
        ImageResult[] imageResult = (ImageResult[])imageResults.toArray();
        for (int j = 0;j<imageResult.length;j++) {
			for (int i = 0;i<imageResult.length;i++)
			{
				if(imageResult[i].getSimilarity() > imageResult[j].getSimilarity())
				{
					ImageResult t = imageResult[i];
					imageResult[i] = imageResult[j];
					imageResult[j] = t;
				}
			}
			imageResults.add(imageResult[j]);
		}
        
        
        Collections.sort(imageResults);

        return imageResults;
    }
}
