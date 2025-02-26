import java.awt.*; // set of classes used for building graphical user interfaces (GUIs) //AWT= Abstract Window Toolkit
import java.awt.event.*; //for handling events like button clicks, mouse movements, and keyboard input in GUI applications.
import java.util.ArrayList;
import java.util.Random;// to place pipes in random locations
import javax.swing.*; //to create GUIs

public class FlappyBird extends JPanel implements ActionListener,KeyListener{
    int boardWidth=360;
    int boardHeight=640;

    //images
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;

    //bird
    int birdX=boardWidth/8;
    int birdY=boardHeight/2;
    int birdWidth=34;
    int birdHeight=24;

    class Bird{
        int x=birdX;
        int y=birdY;
        int width=birdWidth;
        int height=birdHeight;
        Image img;
        
        Bird(Image img)
        {
            this.img=img;
        }
    }
    
    //pipes
    //pipes going to start at top and ryt of the screen
    int pipeX=boardWidth;
    int pipeY=0; 
    int pipeWidth=64;
    int pipeHeight=512;

    class Pipe{
        int x=pipeX;
        int y=pipeY;
        int width=pipeWidth;
        int height=pipeHeight;
        Image img;
        boolean passed=false;
        Pipe(Image img)
        {
            this.img=img;
        }
    }


    //game logic
     Bird bird;
     int velocityX=-4;//moves pipes to left speed (stimulates bird moving ryt)
     int velocityY=0; //since flappy bird moves only up and down
     int gravity=1; // bird is going to slow down by 1 pixel

     ArrayList<Pipe> pipes; //since we hav many pipes
     Random random=new Random();

     Timer gameLoop;
     Timer placePipesTimer;
     boolean gameOver = false;
     double score=0;

    FlappyBird()
    {
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        //setBackground(Color.blue);
        setFocusable(true);//make sures that this FlappyBird class is the one which responds when a key is pressed 
        addKeyListener(this);//check tht 3 key methods whenever a key is pressed

        //load images 
        backgroundImg=new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg=new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg=new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg=new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
        
        //bird
        bird = new Bird(birdImg);
        //pipes
        pipes=new ArrayList<Pipe> ();

        //place pipes timer
        placePipesTimer=new Timer(1500,new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                placePipes();
            }
        });
        placePipesTimer.start();
        
        //game timer
        gameLoop= new Timer(1000/60,this); //1000/60=16.6
        gameLoop.start();

    }

    public void placePipes()
    {
        //(0-1)*pipeHeight/2 -> (0-256)
        //128
        //0 - 128 -(0 to 256)---> 1/4 pipeheight to 3/4 of pipeheight
        int randomPipeY=(int)(pipeY-pipeHeight/4-Math.random()*(pipeHeight/2)); //pipe goes up by a quarter
        int openingSpace=boardHeight/4;

        Pipe topPipe= new Pipe(topPipeImg);
        topPipe.y=randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe=new Pipe(bottomPipeImg);
        bottomPipe.y=topPipe.y+ boardHeight+ openingSpace;
        pipes.add(bottomPipe);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);//properly refreshed before drawing
        draw(g);
    }
    public void draw(Graphics g)
    {   //starting position 0,0 = top left corner
        //ending position 360,640 =bottom ryt corner

        //background
        g.drawImage(backgroundImg,0,0,boardWidth,boardHeight,null);
        //bird
        g.drawImage(birdImg,bird.x,bird.y,bird.width,bird.height,null);
        //pipes
        for(int i=0;i<pipes.size();i++)
        {
            Pipe pipe=pipes.get(i);
            g.drawImage(pipe.img,pipe.x,pipe.y,pipe.width,pipe.height,null);
        }

        //score
        g.setColor(Color.white);
        g.setFont(new Font("Arial",Font.PLAIN,32));
        if(gameOver)
        {
            g.drawString("Game Over : " + String.valueOf((int)score),10,35);
        }
        else
        {
            g.drawString(String.valueOf((int)score),10,35);
        }
    }

    public void move() {
        //bird
    velocityY+=gravity;
     bird.y += velocityY;
     bird.y=Math.max(bird.y,0); // it can't go beyond the top most limit of screen

     //pipes
     for(int i=0;i<pipes.size();i++)
     {
         Pipe pipe=pipes.get(i);
         pipe.x+=velocityX;
         
         if(!pipe.passed && bird.x > pipe.x+pipe.width){
            pipe.passed=true;
            score+=0.5; //0.5 bcoz there are 2 pipes so 2*0.5 = 1 , 1 for each set of pipes
         }

         if(collision(bird, pipe)){
            gameOver=true;
         }
     }

     //gameover
     if(bird.y>boardHeight){
        gameOver=true;
     }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       move();
       repaint(); // to call paintComponent(Graphics g) again
       if(gameOver)
       {
        placePipesTimer.stop();
        gameLoop.stop();
       }
    }

    public boolean collision(Bird a,Pipe b)
    {
     return a.x < b.x+b.width && //a's top left corner doesn't reach b's top ryt corner
            a.x+a.width > b.x && //a's top ryt corner passes b's top left corner
            a.y < b.y+b.height && //a's top left corner doesn't reach b's bottom left corner
            a.y+a.height > b.y; //a's bottom left corner passes b's top left corner
    }

    @Override
    public void keyTyped(KeyEvent e) { // all keys 
    }

    @Override
    public void keyPressed(KeyEvent e) { //only character keys
       if(e.getKeyCode()==KeyEvent.VK_SPACE)
       {
        velocityY=-9; // velocity resets to -9 when we press space
       }
       //reset the game by resetting the conditions
       if(gameOver)
       {
        bird.y=birdY;
        velocityY=0;
        pipes.clear();
        score=0;
        gameOver=false;
        gameLoop.start();
        placePipesTimer.start();
       }
    }

    @Override
    public void keyReleased(KeyEvent e) { //Detect when a user stops pressing a key
    }
}





