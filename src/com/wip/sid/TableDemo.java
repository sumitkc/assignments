package com.wip.sid;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class TableDemo extends JFrame {

    private DefaultTableModel dtm;
    private JTable table;

    private JTextField tfName;
    private JTextField tfAge;
    private JTextField tfDept;
    private JTextField tfIsMale;

    public TableDemo() {
        initgui();
        initData();
    }

    private void initData() {
        createRow("Andrew", 27, "Physics", true);
        createRow("Jack", 24, "Maths", true);
        createRow("Sid", 12, "CS", true);
        createRow("Sim", 7, "Chem", false);

    }

    private void createRow(String name, int age, String dept, Boolean isMale) {
        Vector row = new Vector();
        row.add(name);
        row.add(age);
        row.add(dept);
        row.add(isMale);
        dtm.addRow(row);
    }

    private void insertRow(int index, String name, int age, String dept, Boolean isMale) {
        Vector row = new Vector();
        row.add(name);
        row.add(age);
        row.add(dept);
        row.add(isMale);
        dtm.insertRow(index, row);
    }

    private void updateRow(int index, String name, int age, String dept, Boolean isMale) {
        Vector allRows = dtm.getDataVector();
        Vector targetRow = (Vector) allRows.get(index);
        targetRow.setElementAt(name, 0);
        targetRow.setElementAt(age, 1);
        targetRow.setElementAt(dept, 2);
        targetRow.setElementAt(isMale, 3);
        dtm.fireTableDataChanged();

        //dtm.removeRow(index);
        //insertRow(index,name,age,dept,isMale);
    }
    private void cleanUp(){
        
    }

    private void initgui() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel tablePanel = new JPanel(new BorderLayout());
        JPanel tfPanel = new JPanel(new FlowLayout());
        topPanel.add(tablePanel, BorderLayout.CENTER);
        topPanel.add(tfPanel, BorderLayout.SOUTH);
        tfPanel.setPreferredSize(new Dimension(30, 30));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setPreferredSize(new Dimension(50, 50));
        JButton btnAdd = new JButton("Add");

        buttonPanel.add(btnAdd);
        JButton btnUpdate = new JButton("Update");
        buttonPanel.add(btnUpdate);
        JButton btnDelete = new JButton("Delete");
        buttonPanel.add(btnDelete);

        tfName = new JTextField();
        tfName.setPreferredSize(new Dimension(200, 30));
        tfPanel.add(tfName);
        tfAge = new JTextField();
        tfAge.setPreferredSize(new Dimension(200, 30));
        tfPanel.add(tfAge);
        tfDept = new JTextField();
        tfDept.setPreferredSize(new Dimension(200, 30));
        tfPanel.add(tfDept);
        tfIsMale = new JTextField();
        tfIsMale.setPreferredSize(new Dimension(200, 30));
        tfPanel.add(tfIsMale);

        this.add(topPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);

        ActionListener al = new MyActionListener();
        btnAdd.addActionListener(al);
        btnUpdate.addActionListener(al);
        btnDelete.addActionListener(al);

        dtm = new DefaultTableModel() {
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1) {
                    return Integer.class;
                } else if (columnIndex == 3) {
                    return Boolean.class;
                } else {
                    return String.class;
                }
            }
        };

        dtm.addColumn("Name");
        dtm.addColumn("Age");
        dtm.addColumn("Dept");
        dtm.addColumn("isMale");

        table = new JTable(dtm);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ListSelectionListener lsl = new MyListSelectionListener();
        table.getSelectionModel().addListSelectionListener(lsl);

        JScrollPane jsp = new JScrollPane(table);
        tablePanel.add(jsp);
    }

    class MyActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            if (button.getText().equals("Add")) {
                String name = tfName.getText();
                String dept = tfDept.getText();
                int age = Integer.parseInt(tfAge.getText());
                Boolean isMale = Boolean.parseBoolean(tfIsMale.getText());
                createRow(name, age, dept, isMale);
            } else if (button.getText().equals("Update")) {
                int n = table.getSelectedRow();
                if (n < 0) {
                    return;
                }

                String name = tfName.getText();
                String dept = tfDept.getText();
                int age = Integer.parseInt(tfAge.getText());
                Boolean isMale = Boolean.parseBoolean(tfIsMale.getText());
                updateRow(n, name, age, dept, isMale);

            } else if (button.getText().equals("Delete")) {
                int n = table.getSelectedRow();
                if (n >= 0) {
                    dtm.removeRow(n);
                }
            }
        }
    }

    class MyListSelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting()) {
                return;
            }

            ListSelectionModel lsm = (ListSelectionModel) e.getSource();
            int minIndex = lsm.getMinSelectionIndex();
            int maxIndex = lsm.getMaxSelectionIndex();
            System.out.println(minIndex + " , " + maxIndex);

            if (minIndex < 0) {
                return;
            }
            Vector allRows = dtm.getDataVector();
            Vector row = (Vector) allRows.get(minIndex);

            tfName.setText(row.get(0).toString());
            tfAge.setText(row.get(1).toString());
            tfDept.setText(row.get(2).toString());
            tfIsMale.setText(row.get(3).toString());
        }
    }

    public static void main(String[] args) {
        TableDemo td = new TableDemo();
        td.setSize(1000, 800);
        td.setVisible(true);
    }
}
