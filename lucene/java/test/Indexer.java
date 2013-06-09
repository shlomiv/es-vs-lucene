package test;

import org.apache.lucene.analysis.*;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.store.*;
import org.apache.lucene.util.Version;
import org.apache.lucene.analysis.standard.StandardAnalyzer; 
import java.io.*;

public class Indexer {

    public void index(String filename, String indexFolderPath) throws Exception {
        Directory directory = FSDirectory.open(new File(indexFolderPath));
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_35); 

        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_35, analyzer);
        
	IndexWriter writer = new IndexWriter(directory, indexWriterConfig);
	writer.setMergePolicy(new LogByteSizeMergePolicy());
	writer.setUseCompoundFile(false);

	File file = new File(filename);

	writeFileToIndex(writer, file);
            
	writer.optimize(1);          
    }

    private void writeFileToIndex(IndexWriter writer, File file) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            String[] split = line.split("\\t");
            String ngram = split[0];
            long frequency = Long.parseLong(split[1]);
            
            Document doc = getDocument(ngram, frequency);
            writer.addDocument(doc);
        }
    }

    private Document getDocument(String ngram, long frequency) {
        Document document = new Document();
	Field gram = new Field("gram", ngram, Field.Store.YES, Field.Index.ANALYZED);
	gram.setOmitNorms(false);
	gram.setIndexOptions(FieldInfo.IndexOptions.DOCS_ONLY);
        
	NumericField frequencyField = new NumericField("freq", Field.Store.YES, true);
        frequencyField.setOmitNorms(true);
        frequencyField.setIndexOptions(FieldInfo.IndexOptions.DOCS_ONLY);
        frequencyField.setLongValue(frequency);
        document.add(gram);
        document.add(frequencyField);
	
        return document;
    }


    public static void main(String[] args) {
	Indexer i = new Indexer();
	try{
            i.index("../es/shlomi.txt", "luc-index");
	}catch(Exception e) {
            e.printStackTrace();
	}
    }
}
