/**
 * Window
 * @author 4IF-4114
 */
package view;

import controller.Controller;
import model.CityMap;

import java.awt.*;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.border.Border;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * The current window of the application
 */
public class Window extends JFrame {


    // Titles of window buttons
    protected static final String LOAD_CITY_MAP = "Load a city map";
    protected static final String LOAD_DISTRIBUTION = "Load a distribution";
    protected static final String COMPUTE_TOUR = "Compute a tour";
    protected static final String MODIFY = "Add request";
    protected static final String REMOVE = "Remove";
    protected static final String REDO = "Redo";
    protected static final String UNDO = "Undo";
    public static final String UP = "up" ;
    public static final String DOWN = "down" ;
    protected static final String GENERATE_ROADMAP = "Generate roadmap";

    protected static final String ZOOM_IN = "+";
    protected static final String ZOOM_OUT = "-";
    protected static final String RECENTER = "=";


    private final String[] buttonTexts = new String[]{LOAD_CITY_MAP, LOAD_DISTRIBUTION, COMPUTE_TOUR, MODIFY, REMOVE,UNDO,REDO,GENERATE_ROADMAP};

    private final String[] buttonTextsZoom = new String[]{ZOOM_IN,ZOOM_OUT,RECENTER};

    private JTextPane messageFrame;
    private JPanel helpPanel;
    private JTextField durationJText;

    private MapView mapView;
    private RoadmapView roadmapView;
    private ArrayList<JButton> buttons;

    private ButtonListener buttonListener;
    private MouseListener mouseListener;
    private KeyboardListener keyboardListener;

    private final int BUTTON_HEIGHT = 60;
    private final int BUTTON_WIDTH = 200;

    /**
     * Constructor of Window
     * @param cityMap the current citymap
     * @param controller the controller for every action performed
     */
    public Window(CityMap cityMap, Controller controller) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        setLayout(null);
        mapView = new MapView(cityMap, this);
        messageFrame = new JTextPane();
        durationJText = new JTextField(50);
        roadmapView = new RoadmapView(cityMap, this);
        helpPanel = new JPanel(null);
        mouseListener = new MouseListener(controller, mapView, this);
        keyboardListener = new KeyboardListener(controller);
        messageFrame.setBorder(BorderFactory.createTitledBorder("Messages"));
        messageFrame.setFont(new Font("Segoe UI", Font.BOLD, 18));
        messageFrame.setBounds(10,10,280,80);
        messageFrame.setEditable(false);
        StyledDocument docu = messageFrame.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        docu.setParagraphAttributes(0, docu.getLength(), center, false);

        helpPanel.add(messageFrame);
        helpPanel.add(durationJText);
        getContentPane().add(helpPanel);

        JLabel jl = new JLabel("Delivelo");
        jl.setFont(new Font("Segoe UI", Font.PLAIN, 30));
        jl.setBounds(50,20,180,30);
        getContentPane().add(jl);

        createButtons(controller);
        addMouseListener((java.awt.event.MouseListener) mouseListener);
        addMouseWheelListener((MouseWheelListener) mouseListener);
        addMouseMotionListener((MouseMotionListener) mouseListener);
        addKeyListener(keyboardListener);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setWindowSize();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    /**
     * Adapt the size of every elements (buttons, map) to the size of the window
     */
    private void setWindowSize() {
        int allBUTTON_HEIGHT = BUTTON_HEIGHT * buttonTexts.length;
        int windowHeight = Math.max(mapView.getViewHeight(),allBUTTON_HEIGHT);
        int windowWidth = mapView.getViewWidth() + BUTTON_WIDTH + roadmapView.getViewWidth() + 15;
        setSize(windowWidth, windowHeight);
        mapView.setLocation(BUTTON_WIDTH, 0);
        roadmapView.setLocation(mapView.getViewWidth() + BUTTON_WIDTH,160);
        helpPanel.setBounds(mapView.getViewWidth() + BUTTON_WIDTH, 0,300 ,160);
        durationJText.setBounds(20,110,150,30);

        mapView.setLocation(BUTTON_WIDTH, 0);
    }

    /**
     * Create buttons corresponding to buttonTexts, and the corresponding button listener
     * @param controller the current controller for performed actions
     */
    private void createButtons(Controller controller){
        buttonListener = new ButtonListener(controller);
        buttons = new ArrayList<JButton>();
        for ( int i=0; i<buttonTexts.length; i++ ) {
            JButton button = new JButton(buttonTexts[i]);
            buttons.add(button);
            button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            button.setForeground(Color.BLACK);
            button.setBounds(10,100+(BUTTON_HEIGHT+10)*i,BUTTON_WIDTH-20,BUTTON_HEIGHT);
            button.addActionListener(buttonListener);
            getContentPane().add(button);

        }
        for ( int i=0; i<buttonTextsZoom.length; i++ ){
            JButton button = new JButton(buttonTextsZoom[i]);
            buttons.add(button);
            button.setSize(30,30);
            button.setMargin(new Insets(0,0,5,0));
            button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            button.setLocation( mapView.getViewWidth() - 60, mapView.getViewHeight() - 170 + i * 40);
            button.addActionListener(buttonListener);
            mapView.add(button);
        }
    }

    public MapView getMapView() {
        return mapView;
    }

    public ButtonListener getButtonListener() {
        return buttonListener;
    }

    /**
     * Convert the Jtext duration to a string and empty it
     * @return the converted duration as a string
     */
    public String getDuration() {
        String result = durationJText.getText();
        durationJText.setText("");
        return result ;

    }
    public RoadmapView getRoadmapView() { return roadmapView; }

    /**
     * show the error on the JOptionPane
     * @param message the error message
     */
    public void parsingError(String message) {
        final JPanel panel = new JPanel();
        JOptionPane.showMessageDialog(panel, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void displayMessage(String m) {
        messageFrame.setText(m);
    }

    /**
     * Disable/Enable a button if it should be clickable or not (wrong/right state)
     * @param buttonLabel the name of the button
     * @param bool change the status of a button
     */
    public void enableButton(String buttonLabel, boolean bool) {
        int i = 0;
        for (String text : buttonTexts) {
            if (text.equals(buttonLabel)) {
                this.buttons.get(i).setEnabled(bool);
            }
            i += 1;
        }
    }

    public String[] getButtonTests() {
        return buttonTexts;
    }

    public void resetDurationInserted() {
        durationJText.setText("300");
    }
}