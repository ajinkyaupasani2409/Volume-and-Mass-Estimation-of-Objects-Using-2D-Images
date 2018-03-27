package indexing;

import com.google.code.jcbir.Constants;

import clustering.Cluster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author eadams
 * @author denny
 */
public class IndexReader extends Indexer {

    private org.apache.lucene.index.IndexReader reader;

    public void open() throws IOException {
        reader = org.apache.lucene.index.IndexReader.open(FSDirectory.open(new File(Constants.ROOT_IDX)));
    }

    public List<Cluster> getClusters() {
        List<Cluster> clusters = new ArrayList<Cluster>();
        int numDocs = reader.numDocs();
        for (int i = 0; i < numDocs; i++) {
            try {
                Document doc = reader.document(i);
                String rep = doc.get(Constants.CENTROID_FIELD);
                Cluster c = new Cluster(this, toMatrixs(rep));
                clusters.add(c);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return clusters;
    }

    public Cluster getCluster(int id) {
        try {
            Document doc = reader.document(id);
            String rep = doc.get(Constants.CENTROID_FIELD);
            Cluster c = new Cluster(this, toMatrixs(rep));
            return c;
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }

    public int numCluster() {
        return reader.numDocs();
    }

    public void close() throws IOException {
        reader.close();
    }
}
