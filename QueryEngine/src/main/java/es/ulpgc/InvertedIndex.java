package es.ulpgc;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InvertedIndex {
    private final Map<String, Set<String>> index;
    private final DatamartDataSource datamartDataSource;

    public InvertedIndex(DataSource dataSource) {
        if (dataSource instanceof DatamartDataSource) {
            this.index = null; // No cargamos un índice en memoria para el datamart
            this.datamartDataSource = (DatamartDataSource) dataSource;
        } else {
            this.index = dataSource.loadIndex();
            this.datamartDataSource = null;
        }
    }

    public Set<String> search(String term) {
        if (datamartDataSource != null) {
            // Buscar en el datamart
            return datamartDataSource.searchWord(term);
        } else {
            // Buscar en el índice en memoria
            return index.getOrDefault(term, new HashSet<>());
        }
    }
}
