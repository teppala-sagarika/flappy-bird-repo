import javax.swing.*; //library where u can create GUIs

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth=360;
        int boardHeight=640;
         //JFrame (for creating a window)
        JFrame frame = new JFrame("Flappy Bird");
          
        frame.setSize(boardWidth,boardHeight);
        frame.setLocationRelativeTo(null); //places window to the centre of the screen
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

        FlappyBird flappyBird =new FlappyBird();
        frame.add(flappyBird);
        frame.pack();//automatically sizes the window to fit its components
        flappyBird.requestFocus();
        frame.setVisible(true);
    }
}


