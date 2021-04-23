import javax.swing.*;

public class swing_project {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                CreateNewGUI();
            }
        });
    }

    private static void CreateNewGUI() {
    }
}
