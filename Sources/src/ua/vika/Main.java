package ua.vika;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

/**
 * Created by zidd on 3/24/14.
 */
public class Main extends JFrame {
    private static JDialog scheduleDialog;
    private static JDialog groupDialog;
    private static Group group;
    private static JDialog subjectMarksDialog;
    //TODO
    private static final String [] SUBJECTS_NAME = {"Схемотехника", "Компьютерная графика","Экономика", "Английский язык",
        "Высшая математика","Информациооные системы", "Безопастность жизнедеятельности","Компьютерные сети",
        "Физика","Теория вероятности", "Украинский язык"};
    private static final Subject [] SUBJECTS = {Subject.CIRCUITRY, Subject.COMPUTER_GRAPHICS,Subject.ECONOMY,
            Subject.ENGLISH,Subject.HIGHER_MATHEMATICS, Subject.INFORMATION_SYSTEMS, Subject.LIFE_SAFETY,
            Subject.NETWORKING,Subject.PHYSICS, Subject.PROBABILITY_THEORY, Subject.UKRAINIAN_LANGUAGE};

    public Main() {
        super("асписание группы КС-21");
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        FrontLabel frontLabel = new FrontLabel();
        add(frontLabel, BorderLayout.CENTER);

        BottomBar bottomBar = new BottomBar(this);
        add(bottomBar, BorderLayout.SOUTH);

        group = new Group("resources/Gruppa.txt");
        groupDialog = new MyDialog(group.toString(), "Список группы", this);

        TextDiscription textDiscription = new TextDiscription("resources/Rasspisanie.txt");
        scheduleDialog = new MyDialog(textDiscription.toString(), "Расписание", this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800,500));
        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);
    }

    private static class FrontLabel extends JPanel {
        private JLabel facultyIcon;
        private JLabel universityIcon;
        private JTextPane aboutFaculty;

        private FrontLabel() {
            facultyIcon = new JLabel(new ImageIcon("resources/fks.jpg"));
            universityIcon = new JLabel(new ImageIcon("resources/hnu.jpg"));
            TextDiscription aboutFacultyText = new TextDiscription("resources/Pro_fakultet.txt");
            aboutFaculty = createCenterAlignTextPane(aboutFacultyText.toString());
            setBackground(Color.white);
            setLayout(new BorderLayout(10,10));
            JScrollPane scrollPane = new JScrollPane(aboutFaculty);
            add(facultyIcon, BorderLayout.WEST);
            add(scrollPane, BorderLayout.CENTER);
            add(universityIcon, BorderLayout.EAST);

        }
    }

    private static class BottomBar extends JPanel {
        private JButton groupButton;
        private JButton scheduleButton;
        private JComboBox<String> subjectsComboBox;
        private Component context;

        private BottomBar(Component cont) {
            this.context = cont;
            groupButton = new JButton("Список группы");
            groupButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    groupDialog.setVisible(true);
                }
            });
            scheduleButton = new JButton("Расписание");
            scheduleButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    scheduleDialog.setVisible(true);
                }
            });

            subjectsComboBox = new JComboBox<String>(SUBJECTS_NAME);
            add(groupButton);
            add(subjectsComboBox);
            add(scheduleButton);
            subjectsComboBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JComboBox<String> source = (JComboBox<String>)e.getSource();
                    String subjectName = source.getSelectedItem().toString();
                    int i;
                    for( i = 0; i < SUBJECTS_NAME.length; i++) {
                        if(SUBJECTS_NAME[i].equals(subjectName)) {
                            break;
                        }
                    }
                    StudentMarks marks = new StudentMarks(SUBJECTS[i],group);
                    subjectMarksDialog = new SubjectStudentMarksDialog(marks, context, SUBJECTS[i]);
                    subjectMarksDialog.setVisible(true);
                }
            });
            setBackground(Color.white);
        }
    }

    private static class MyDialog extends JDialog {
        private JTextPane textArea;
        private JScrollPane scrollPane;

        public MyDialog(String text, String title, Component context) {
            textArea = createCenterAlignTextPane(text);
            scrollPane = new JScrollPane(textArea);
            add(scrollPane);
            setSize(new Dimension(700, 500));
            setLocationRelativeTo(context);
            setModal(true);
            setTitle(title);
            setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        }
    }

    private static JTextPane createCenterAlignTextPane(String text) {
        SimpleAttributeSet bSet = new SimpleAttributeSet();
        StyleConstants.setAlignment(bSet, StyleConstants.ALIGN_CENTER);
        StyleConstants.setFontFamily(bSet, "lucida typewriter bold");
        StyleConstants.setFontSize(bSet, 20);
        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setText(text);
        StyledDocument doc = textPane.getStyledDocument();
        doc.setParagraphAttributes(0, doc.getLength(), bSet, false);
        return textPane;
    }

    private static class StudentMarkConverter {
        private Object [] headers;
        private Object [][] tableData;

        public Object[] getHeaders() {
            return headers;
        }

        public Object[][] getTableData() {
            return tableData;
        }

        public StudentMarkConverter(java.util.List<String> headers, java.util.Map<Integer, java.util.List<String>> data) {
            this.headers = new Object[headers.size() + 2];
            this.headers[0] = "#";
            this.headers[1] = "Имя и фамилия студента";
            System.arraycopy(headers.toArray(), 0, this.headers, 2, headers.size());
            if(data.size() == 0) {
                tableData = new Object[group.getStudentsList().size()][headers.size()+2];
                for(int i = 0; i < tableData.length; i++) {
                    tableData[i][0] = i+1;
                    tableData[i][1] = group.getStudent(i + 1).toString();
                    for(int y = 2; y < tableData[i].length; y++) {
                        tableData[i][y] = "";
                    }
                }
            } else {
                tableData = new Object[data.size()][this.headers.length];
                for(int i = 0; i < tableData.length; i++) {
                    tableData[i][0] = i+1;
                    tableData[i][1] = group.getStudent(i + 1).toString();
                    java.util.List<String> studentMarks = data.get(i + 1);
                    for(int y = 2; y < tableData[i].length; y++) {
                        tableData[i][y] = studentMarks.get(y-2);
                    }
                }
            }
        }


    }

    private static class InFileMarksSaver {
        private Object[][] data;
        private Group group;
        private Subject subject;

        public InFileMarksSaver(Object[][] data, Group group, Subject subject) {
            this.data = data;
            this.group = group;
            this.subject = subject;
        }

        public void saveInFile() {
            OutputStreamWriter osw;
            FileOutputStream fos;
            File file = new File("resources/" + subject.name().toLowerCase() + "Marks.txt");
            //FileWriter fw = null;
            try {
                //fw = new FileWriter(file);
                fos = new FileOutputStream(file);
                osw = new OutputStreamWriter(fos, "UTF8");
                for(Object[] stud : data) {
                    StringBuilder sb = new StringBuilder();
                    for(Object mark : stud) {
                        if(mark == null || mark.equals("")) {
                            mark = "-1";
                        }
                        sb.append(mark + " ");
                    }
                    sb.append("\n");
//                    fw.write(sb.toString());
//                    fw.flush();
                    osw.write(sb.toString());
                    osw.flush();

                }
                osw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private static class SubjectStudentMarksDialog extends JDialog {
        private TableModel tableModel;
        private JTable table;
        private Subject subject;

        public SubjectStudentMarksDialog(StudentMarks studentMarks, Component context, Subject subj) {
            this.subject = subj;
            StudentMarkConverter converter = new StudentMarkConverter(studentMarks.getDates(), studentMarks.getStudentMarks());
            tableModel = new DefaultTableModel(converter.getTableData(), converter.getHeaders()) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    if(column == 0 || column == 1) {
                        return false;
                    } else {
                        return true;
                    }
                }
            };
            table = new JTable(tableModel);
            table.setFillsViewportHeight(true);
            TableColumnModel columnModel = table.getColumnModel();
            for(int i =0; i < columnModel.getColumnCount(); i++) {
                TableColumn column = columnModel.getColumn(i);
                if(i == 1) {
                    column.setPreferredWidth(250);
                } else {
                    column.setPreferredWidth(50);
                }
                column.setResizable(false);
            }
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            add(scrollPane);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setMinimumSize(new Dimension(800,400));
            setPreferredSize(new Dimension(800,600));
            setModal(true);
            int i = 0;
            for(Subject sub:SUBJECTS){
                if(sub.equals(subj)) {
                    setTitle(SUBJECTS_NAME[i]);
                }
                i++;

            }

            setLocationRelativeTo(context);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    int start = 2;
                    TableModel tm = table.getModel();
                    Object[][] dataModel = new Object[tableModel.getRowCount()][tableModel.getRowCount() - 2];
                    for(int i = 0; i < tm.getRowCount(); i++) {
                        for(int y = start; y < table.getColumnCount(); y++) {
                            dataModel[i][y - start] = table.getModel().getValueAt(i, y);
                        }
                    }
                    InFileMarksSaver saver = new InFileMarksSaver(dataModel, group, subject);
                    saver.saveInFile();

                }
            });
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
    }

}
