import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.text.*;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public class TextForm {
    static JFrame frame = new JFrame("TextForm");
    private JPanel panel1;
    private JButton clearButton;
    private JButton submitButton;
    private JLabel longestWord;
    private JTextPane text;
    private JLabel repeated;
    private JLabel letterCount;
    private JLabel shortestWord;
    private JLabel Repeated;
    private JLabel wordCount;
    private JButton generateTextButton;
    private JLabel sequentialTime;
    private JLabel threadedTime;
    private JLabel speedup;

    public TextForm() {
        // limit the number of words to 300
        ((AbstractDocument) text.getDocument()).setDocumentFilter(new DocumentFilter() {
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String newText = fb.getDocument().getText(0, fb.getDocument().getLength()) + text;
                if (newText.split("\\s+").length <= 300) {
                    super.replace(fb, offset, length, text, attrs);
                    wordCount.setText("Word Count: " + newText.split("\\s+").length);
                } else {
                    JOptionPane.showMessageDialog(null, "You have reached the maximum word limit of 300.");
                }
            }
        });
        submitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String textString = text.getText();
                if (textString.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter some text.");
                    return;
                }

                try {

                    Map<String, String> dict = getAnalysis(textString);

                    Repeated.setText("Repeated: " + dict.get("repeatedWord"));
                    letterCount.setText("Letter Count: " + dict.get("letterCount"));
                    longestWord.setText("Longest Word: " + dict.get("longestWord"));
                    shortestWord.setText("Shortest Word: " + dict.get("shortestWord"));
                    repeated.setText("Frequency: " + dict.get("frequency"));
                    threadedTime.setText("Threaded Time: " + dict.get("threadedTime") + " ns");
                    sequentialTime.setText("Sequential Time: " + dict.get("sequentialTime") + " ns");
                    speedup.setText("Speedup: " + dict.get("speedup") + "x");
                } catch (Exception ex) {
                    System.err.println("Client exception: " + ex);
                    ex.printStackTrace();
                }

            }
        });
        clearButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                text.setText("");
                wordCount.setText("Word Count: 0");
                Repeated.setText("Repeated: ");
                letterCount.setText("Letter Count: ");
                longestWord.setText("Longest Word: ");
                shortestWord.setText("Shortest Word: ");
                repeated.setText("Frequency: ");
                threadedTime.setText("Threaded Time: ");
                sequentialTime.setText("Sequential Time: ");
                speedup.setText("Speedup: ");
            }
        });
        generateTextButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    String textString = getTextFromServer();
                    text.setText(textString);

                } catch (Exception ex) {
                    System.err.println("Client exception: " + ex.toString());
                    ex.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getAnalysis(String textString) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("localhost");
        DataService stub = (DataService) registry.lookup("Data");
        stub.setData(textString);
        return stub.getData();
    }

    private String getTextFromServer() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("localhost");
        DataService stub = (DataService) registry.lookup("Data");
        return stub.generateText(300);
    }

    public static void main(String[] args) {
        prepareFrame();
    }

    public static void prepareFrame() {
        frame.setContentPane(new TextForm().panel1);
        frame.setSize(800, 600);
        // make the form start in the center of the screen
        frame.setLocationRelativeTo(null);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}

interface DataService extends Remote {
    Map<String, String> getData() throws RemoteException;

    String generateText(int length) throws RemoteException;

    void setData(String data) throws RemoteException;
}
