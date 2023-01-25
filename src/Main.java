import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setTitle("Conway's Game Of Life");
        window.setResizable(false);

        Grid grid = new Grid();
        window.add(grid);
        window.pack();
        window.setVisible(true);
        window.setLocationRelativeTo(null);

    }
}
