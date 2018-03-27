package indexing;

import com.google.code.jcbir.Constants;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;

/**
 *
 * @author eadams
 * @author denny
 */
public class ClusterWriter extends Indexer {

    private org.apache.lucene.index.IndexWriter writer;

    public void open(int id) throws CorruptIndexException, LockObtainFailedException, IOException {
        writer = new org.apache.lucene.index.IndexWriter(FSDirectory.open(new File(Constants.CLUSTER_IDX+"-"+id)),
                new SimpleAnalyzer(), true, org.apache.lucene.index.IndexWriter.MaxFieldLength.UNLIMITED);
    }

    public void addIndex(Index index) throws Exception{
        Document document = new Document();
        
        Method[] methods = Index.class.getDeclaredMethods();
        for (Method m : methods) {
            String methodName = m.getName();
            if (methodName.startsWith("get")) {
                    String value = (String) m.invoke(index, new Object[]{});
                    Field f = new Field(methodName.replaceFirst("get", ""), value, Field.Store.YES, Field.Index.NO);
                    document.add(f);
                    //System.out.println(methodName.replaceFirst("get", "")+" = " + document.get(methodName.replaceFirst("get", "")));
            }
        }
        writer.addDocument(document);
    }

    public void close() throws CorruptIndexException, IOException {
        writer.optimize();
        writer.close();
    }
}
