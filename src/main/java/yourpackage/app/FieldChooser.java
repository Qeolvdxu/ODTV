package yourpackage.app;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import yourpackage.parsing.DataField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FieldChooser {
    private JPanel mainPanelFC;
    private JButton addButton;
    private JButton removeButton;
    private JList selectedFieldsJList;
    private JScrollPane foundFieldsScrollPane;
    private JList foundFieldsJList;
    private JTextField searchTextField;
    private JButton searchButton;
    private JButton doneButton;
    private static FieldChooser instance;
    private final JFrame frame;
    private static ArrayList<DataField> foundFields; // Fields found by the parser
    private ArrayList<DataField> visibleFoundFields; // Found fields currently visible to the user
    private ArrayList<DataField> selectedFields; // Fields selected by the user
    private ArrayList<DataField> timeStampField; // ArrayList to hold the timestamp field

    public FieldChooser() {
        frame = new JFrame();
        foundFields = new ArrayList<>();
        visibleFoundFields = new ArrayList<>();
        selectedFields = new ArrayList<>();
        timeStampField = new ArrayList<>();
        String iconPath = System.getProperty("user.dir") + "/src/main/resources/drone.png";
        ImageIcon img = new ImageIcon(iconPath);
        frame.setIconImage(img.getImage()); // Get and set a custom icon for the GUI.
        frame.setContentPane(mainPanelFC);
        frame.setDefaultCloseOperation(0); // Window shouldn't be closeable, or else it will mess up the rest of the program's execution.
        frame.pack();
        frame.setResizable(false);
        frame.setTitle("Choose Data Fields");
        frame.setVisible(true);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int selectedFieldIndex = foundFieldsJList.getSelectedIndex();
                DataField selectedField = visibleFoundFields.get(selectedFieldIndex);
                if (selectedFields.size() < 10 && !fieldExists(selectedField)) {
                    addSelectedField(selectedField);
                }
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int selectedFieldIndex = selectedFieldsJList.getSelectedIndex();
                removeSelectedField(selectedFieldIndex);
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String searchText = searchTextField.getText();
                searchFields(searchText);
            }
        });
        doneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                selectedFields.addAll(timeStampField); // Add the timestamp field before disposing.
                // TODO ADD CODE HERE TO PASS THE FIELDS TO ANY OTHER CLASS THAT NEEDS IT.
                frame.dispose(); // Close the window when the user is done.
            }
        });
    }

    public void setFoundFields(ArrayList<DataField> fields) {
        foundFields.addAll(fields);
        isolateTimeStampField();
        visibleFoundFields.addAll(foundFields);
        populateFoundFieldsJList();
    }

    private boolean fieldExists(DataField field) {
        boolean exists = false;
        for (DataField i : selectedFields) {
            if (i.getFieldName() == field.getFieldName()) {
                exists = true;
            }
        }
        return exists;
    }

    private void addSelectedField(DataField field) {
        selectedFields.add(field);
        populateSelectedFieldsJList();
    }

    private void removeSelectedField(int index) {
        selectedFields.remove(index);
        populateSelectedFieldsJList();
    }

    private void populateFoundFieldsJList() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (DataField item : foundFields) {
            listModel.addElement(String.valueOf(item));
        }
        foundFieldsJList.setModel(listModel);
    }

    private void populateSelectedFieldsJList() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (DataField item : selectedFields) {
            listModel.addElement(String.valueOf(item));
        }
        selectedFieldsJList.setModel(listModel);
    }

    private void searchFields(String text) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        String searchQuery = text.toLowerCase();
        visibleFoundFields.clear();
        for (DataField item : foundFields) {
            String fieldName = item.getFieldName().toLowerCase();
            if (fieldName.contains(searchQuery)) {
                listModel.addElement(String.valueOf(item));
                visibleFoundFields.add(item);
            } else if (text == null) {
                populateFoundFieldsJList();
                visibleFoundFields.addAll(foundFields);
            }
        }
        foundFieldsJList.setModel(listModel);
    }

    private void isolateTimeStampField() {
        for (DataField item : foundFields) {
            if (item.getFieldName().equals("DETAILS.timestamp")) {
                timeStampField.add(item);
                foundFields.remove(item);
                break;
            }
        }
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanelFC = new JPanel();
        mainPanelFC.setLayout(new GridLayoutManager(8, 4, new Insets(0, 0, 0, 0), -1, -1));
        final Spacer spacer1 = new Spacer();
        mainPanelFC.add(spacer1, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        mainPanelFC.add(spacer2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        addButton = new JButton();
        addButton.setText("Add");
        mainPanelFC.add(addButton, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Selected Fields");
        mainPanelFC.add(label1, new GridConstraints(4, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeButton = new JButton();
        removeButton.setText("Remove");
        mainPanelFC.add(removeButton, new GridConstraints(6, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        foundFieldsScrollPane = new JScrollPane();
        mainPanelFC.add(foundFieldsScrollPane, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        foundFieldsJList = new JList();
        foundFieldsScrollPane.setViewportView(foundFieldsJList);
        final JScrollPane scrollPane1 = new JScrollPane();
        mainPanelFC.add(scrollPane1, new GridConstraints(5, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        selectedFieldsJList = new JList();
        scrollPane1.setViewportView(selectedFieldsJList);
        searchTextField = new JTextField();
        searchTextField.setText("");
        mainPanelFC.add(searchTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        searchButton = new JButton();
        searchButton.setText("Search");
        mainPanelFC.add(searchButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Add Fields");
        mainPanelFC.add(label2, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        doneButton = new JButton();
        doneButton.setText("Done");
        mainPanelFC.add(doneButton, new GridConstraints(7, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanelFC;
    }

}
