package indexing;

import com.google.code.jcbir.Constants;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author eadams
 * @author denny
 */
public class ClusterReader extends Indexer {

    private org.apache.lucene.index.IndexReader reader;

    public void open(int id) throws IOException {
        reader = org.apache.lucene.index.IndexReader.open(FSDirectory.open(new File(Constants.CLUSTER_IDX
                + "-" + id)));
    }

    public void close() throws IOException {
        reader.close();
    }

    /**
     * @return the indexs
     */
    public List<Index> getIndexes() {
        List<Index> indexes = new ArrayList<Index>();
        //Method[] methods = Index.class.getDeclaredMethods();
        int numDocs = reader.numDocs();
        //System.out.println(numDocs);
        for (int i = 0; i < numDocs; i++) {
            try {
                Document document = reader.document(i);
                List<Fieldable> f = document.getFields();

                Index index = new Index();
                for (Fieldable fieldable : f) {
                    Field field = (Field) fieldable;
                    Method m = Index.class.getDeclaredMethod("set" + field.name(), new Class[]{String.class});
                    m.invoke(index, new Object[]{field.stringValue()});
                    //Method m2 = Index.class.getDeclaredMethod("get" + field.name(), new Class[]{});
                    //Object val = m2.invoke(index, new Object[]{});
                    //System.out.println(m2.getName()+" = "+val);
                    //System.out.println(m.getName() + " " + field.stringValue());
                }
                //System.out.println("RHAAR-"+i+" = "+index.getRHaarFeature());
                indexes.add(index);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return indexes;
    }
}
