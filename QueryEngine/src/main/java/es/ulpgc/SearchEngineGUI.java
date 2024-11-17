package es.ulpgc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import com.formdev.flatlaf.FlatIntelliJLaf;

public class SearchEngineGUI extends JFrame {
    private JComboBox<String> dataSourceSelector;
    private JButton loadButton;
    private JTextField queryField;
    private JButton searchButton;
    private JTextArea resultsArea;
    private InvertedIndex invertedIndex;

    public SearchEngineGUI() {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        setTitle("Search Engine");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        JLabel dataSourceLabel = new JLabel("1. Choose your data sources:");
        dataSourceSelector = new JComboBox<>(new String[]{"CSV", "Datamart"});
        loadButton = new JButton("Load");

        topPanel.add(dataSourceLabel);
        topPanel.add(dataSourceSelector);
        topPanel.add(loadButton);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());

        JPanel queryPanel = new JPanel();
        queryPanel.setLayout(new FlowLayout());

        JLabel queryLabel = new JLabel("2. Write down your query:");
        queryField = new JTextField(30);
        searchButton = new JButton("Search");

        queryPanel.add(queryLabel);
        queryPanel.add(queryField);
        queryPanel.add(searchButton);

        resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultsArea);

        centerPanel.add(queryPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Agregar paneles a la ventana principal
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        // Configurar eventos
        configureEvents();
    }

    private void configureEvents() {
        // Evento para cargar la fuente de datos
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedSource = (String) dataSourceSelector.getSelectedItem();
                try {
                    if (selectedSource.equals("CSV")) {
                        String filePath = "index_content.csv";
                        DataSource dataSource = new CSVDataSource(filePath);
                        invertedIndex = new InvertedIndex(dataSource);
                        JOptionPane.showMessageDialog(SearchEngineGUI.this, "Data loaded from the CSV.");
                    } else if (selectedSource.equals("Datamart")) {
                        String datamartPath = "datamart_content";
                        DataSource dataSource = new DatamartDataSource(datamartPath);
                        invertedIndex = new InvertedIndex(dataSource);
                        JOptionPane.showMessageDialog(SearchEngineGUI.this, "Data loaded from thee Datamart.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(SearchEngineGUI.this, "Error loading the data source: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Evento para realizar la búsqueda
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (invertedIndex == null) {
                    JOptionPane.showMessageDialog(SearchEngineGUI.this, "You must first load a data source.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String query = queryField.getText().trim();
                if (query.isEmpty()) {
                    JOptionPane.showMessageDialog(SearchEngineGUI.this, "Write a query to search.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                QueryTokenizer tokenizer = new QueryTokenizer();
                Set<String> finalResults = null;

                try {
                    for (String token : tokenizer.tokenize(new Query(query))) {
                        Set<String> results = invertedIndex.search(token);
                        if (finalResults == null) {
                            finalResults = results;
                        } else {
                            finalResults.retainAll(results);
                        }
                    }

                    if (finalResults != null && !finalResults.isEmpty()) {
                        resultsArea.setText("Indexes found for the query \"" + query + "\":\n");
                        for (String result : finalResults) {
                            resultsArea.append(result + "\n");
                        }
                    } else {
                        resultsArea.setText("No results were found for the query \"" + query + "\".");
                    }
                } catch (Exception ex) {
                    resultsArea.setText("An error occurred during the search: " + ex.getMessage());
                }
            }
        });
    }
}
