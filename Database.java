/**
 * Name: Nadia Bousselmi, William Cheng, Jing Wang
 * Date: May 17, 2022
 * Description: A class allowing for the interaction between the user and the program through GUI
 */

import javax.swing.*; // imports swing
import java.awt.*; // imports awt 
import java.awt.image.BufferedImage; //imports BufferedImage class
import javax.imageio.ImageIO; // imports ImageIO class
import java.io.IOException; // imports IOException class
import java.io.File; // imports File class
import java.awt.event.*; // imports event package
import java.io.*; // imports java io packagae
import java.util.*; // imports java util

public class Database implements ActionListener {
    Color backgroundBlue = new Color(215, 227, 252); // Creates the theme colours
    Color beige = new Color(250, 249, 245);
    Color brightBlue = new Color(171, 196, 255);
    Color cornflowerBlue = new Color(100, 144, 245);

    Font button = new Font("corbel", 1, 14); // creates button font
    Font title = new Font("Serif", 1, 48); // stores title font
    ImageIcon logo = new ImageIcon("bookingLogo.png"); // creates logo

    private static JFrame splashFrame; // JFrame for the splash screen
    private JFrame menuFrame = new JFrame(), mainFrame = new JFrame();

    private Boolean cancel = false; // boolean storing if flight is cancelled
    private boolean displaySorted = false; // boolean controlling if the exported information is sorted
    private boolean[] tempSeatSelected = new boolean[10]; // stores if a seat has been selected

    protected static ArrayList<Flight> flights = new ArrayList<Flight>(); // creates an array list of flight objects
    private ArrayList<Integer> tempSeat = new ArrayList<Integer>(); // stores the indexes of all the selected seats

    private int tempDate; // stores flight date
    private int flightIndex = -1; // stores flight index
    private int flightChosen = 0; // stores which of the two flight options are selected
    private int flight1Temp = -1, flight2Temp = -1; // stores the line where each flight option is located in the textfile
    private int mode = 0; // variable storing which menu option was chosen
    private String[][] printList = new String[10][]; // stores the exported information list
    private int currentCounter = 0; // current counter variable
    
    private JPanel mainPanel = new JPanel();
    private JButton[] seats = new JButton[10]; // array of the seat buttons
    private JButton back;
    private JTextField firstField, lastField, dobField, emailField, phoneField; // input textfields

    /**
     * Constructor of the Database class
     */
    public Database() {
        // creates title font (got this from stackoverflow, link for credit is later in
        // the code)
        try {
            title = Font.createFont(Font.TRUETYPE_FONT, new File("MRSEAV_7.TTF")).deriveFont(48f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            // register the font
            ge.registerFont(title);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }

        splash(); // runs splash screen
        try {
            Thread.sleep(2000); // waits 1000 milliseconds
        } catch (Exception e) {
        }
        splashFrame.dispose(); // disposes the splash screen frame
        loadData(); // runs loadData
        frameSetUp();
        menu(); // runs menu
    }

    /**
     * method to display the splash screen
     */
    private void splash() {
        splashFrame = new JFrame(); // creates splash screen frame
        splashFrame.setSize(650, 375); // sets frame size to 650 by 375
        splashFrame.getContentPane().setBackground(backgroundBlue); // sets background to backgroundblue
        splashFrame.setLayout(new GridBagLayout()); // sets layout to GridBagLayout

        GridBagConstraints c = new GridBagConstraints(); // creates an instance of GridBagConstraints

        // ROW 1
        c.gridx = 0; // cell 1
        c.gridy = 0;
        c.ipadx = 33; // sets width of grid
        c.ipady = 330; // sets height of grid
        splashFrame.add(new Drawing("splash-l1"), c); // adds drawing to grid

        c.gridx = 1; // cell 2
        c.gridy = 0;
        c.ipadx = 584;
        c.ipady = 330;
        splashFrame.add(new Drawing("splash-t"), c);

        c.gridx = 2; // cell 3
        c.gridy = 0;
        c.ipadx = 33;
        c.ipady = 330;
        splashFrame.add(new Drawing("splash-r1"), c);

        // ROW2
        c.gridx = 0; // cell 1
        c.gridy = 1;
        c.ipadx = 33;
        c.ipady = 33;
        splashFrame.add(new Drawing("splash-l2"), c);

        c.gridx = 1; // cell 2
        c.gridy = 1;
        c.ipadx = 300;
        c.ipady = 0;
        JPanel loadPanel = new JPanel(); // creates panel containing loading bar
        loadPanel.setBackground(backgroundBlue); // sets panel background to background blue
        JProgressBar lBar = new JProgressBar(); // creates loading bar
        lBar.setForeground(Color.black);
        lBar.setBackground(beige);
        loadPanel.add(lBar); // adds loading bar to panel
        splashFrame.add(loadPanel, c); // adds panel to frame

        c.gridx = 2; // cell 3
        c.gridy = 1;
        c.ipadx = 33;
        c.ipady = 33;
        splashFrame.add(new Drawing("splash-r2"), c);

        // ROW 3
        c.gridx = 0; // cell 1
        c.gridy = 2;
        c.ipadx = 33;
        c.ipady = 57;
        splashFrame.add(new Drawing("splash-l3"), c);

        c.gridx = 1; // cell 2
        c.gridy = 2;
        c.ipadx = 584;
        c.ipady = 57;
        splashFrame.add(new Drawing("splash-b"), c);

        c.gridx = 2; // cell 3
        c.gridy = 2;
        c.ipadx = 33;
        c.ipady = 57;
        splashFrame.add(new Drawing("splash-r3"), c);

        splashFrame.setUndecorated(true); // removes the top part of the frame
        splashFrame.setLocationRelativeTo(null); // sets it to the center
        splashFrame.setVisible(true); // makes frame visible

        try {
            Thread.sleep(500); // waits 500 milliseconds before begining to load
        } catch (Exception e) {
        }
        try {
            for (int i = 0; i <= 100; i++) {
                lBar.setValue(i); // adds one to progress bar value each time loop is run
                Thread.sleep(30); // waits 10 miliseconds for each iteration
            }
        } catch (Exception e) {
        }
    }

    /**
     * Displays the main menu
     */
    private void menu() {
        menuFrame.getContentPane().setBackground(beige); // sets frame background to beige
        menuFrame.setSize(800, 515); // sets frame size to 800 by 515
        menuFrame.setLocationRelativeTo(null); // sets frame location to center of screen

        JPanel menuOptions = new JPanel(new GridLayout(1, 6)); // panel containing menu buttons
        menuOptions.setPreferredSize(new Dimension(800, 33)); // sets panel to 800 by 33
        JButton booking = new JButton("Booking"), viewData = new JButton("View Data"), export = new JButton("Export"),
                help = new JButton("Help"), exit = new JButton("Exit"), empty = new JButton(" "); // initialization of menu buttons

        empty.setBackground(backgroundBlue); // changes look of all buttons to have a border and be blue with prefered font
        empty.setOpaque(true);
        empty.setBorderPainted(true);
        empty.setBorder(BorderFactory.createLineBorder(brightBlue, 2));
        empty.setFont(button);
        empty.setEnabled(false); // makes button unlclickable (just occupies space for indent purpose)

        booking.setBackground(backgroundBlue);
        booking.setOpaque(true);
        booking.setBorderPainted(true);
        booking.setBorder(BorderFactory.createLineBorder(brightBlue, 2));
        booking.setFont(button);
        booking.addActionListener(this); // adds action listenr
        booking.setActionCommand("book"); // sets action Command to book

        viewData.setBackground(backgroundBlue);
        viewData.setOpaque(true);
        viewData.setBorderPainted(true);
        viewData.setBorder(BorderFactory.createLineBorder(brightBlue, 2));
        viewData.setFont(button);
        viewData.addActionListener(this);
        viewData.setActionCommand("view");

        export.setBackground(backgroundBlue);
        export.setOpaque(true);
        export.setBorderPainted(true);
        export.setBorder(BorderFactory.createLineBorder(brightBlue, 2));
        export.setFont(button);
        export.addActionListener(this);
        export.setActionCommand("exp");

        help.setBackground(backgroundBlue);
        help.setOpaque(true);
        help.setBorderPainted(true);
        help.setBorder(BorderFactory.createLineBorder(brightBlue, 2));
        help.setFont(button);
        help.addActionListener(this);
        help.setActionCommand("help");

        exit.setBackground(backgroundBlue);
        exit.setOpaque(true);
        exit.setBorder(BorderFactory.createLineBorder(brightBlue, 2));
        exit.setBorderPainted(true);
        exit.setFont(button);
        exit.addActionListener(this);
        exit.setActionCommand("exit");

        menuOptions.add(empty); // adds butons to panels
        menuOptions.add(booking);
        menuOptions.add(viewData);
        menuOptions.add(export);
        menuOptions.add(help);
        menuOptions.add(exit);

        JMenuBar menuBar = new JMenuBar(); // creates a JMenuBar
        menuBar.setBorderPainted(false); // removes border of menu bar
        menuBar.add(menuOptions); // adds panel to menu bar
        menuFrame.setJMenuBar(menuBar); // sets menuBar as the frame menubar
        menuFrame.add(new Drawing("menu")); // adds menu graphics

        menuFrame.setResizable(false); // does not allow frame to be resized
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // terminates program once closed
        menuFrame.setVisible(true); // makes frame visible
    }

    /**
     * sets up main frame
     */
    private void frameSetUp() {
        mainFrame.getContentPane().setBackground(beige); // sets background to beige
        mainFrame.setSize(800, 515); // sets frame size to 800 by 515
        mainFrame.setLocationRelativeTo(null); // sets it to the center

        mainFrame.setLayout(new GridBagLayout()); // sets layout to gridbag layout
        GridBagConstraints c = new GridBagConstraints(); // creates an instance of GridBagConstraints
        mainFrame.getContentPane().setBackground(beige); // sets frame background to beige
        JMenuBar backBar = new JMenuBar(); // creates back bar
        backBar.setBorderPainted(false); // removes border of menu bar
        backBar.setBackground(beige); // sets backbar background to beige

        JPanel backPanel = new JPanel(); // creates back panel
        backPanel.setPreferredSize(new Dimension(800, 33));
        backPanel.setBackground(beige);
        backPanel.setLayout(new BorderLayout());

        back = new JButton("    Back    "); // creates back button
        back.setBackground(backgroundBlue);
        back.setOpaque(true);
        back.setBorderPainted(true);
        back.setBorder(BorderFactory.createLineBorder(brightBlue, 2));
        back.setFont(button);
        back.setPreferredSize(new Dimension(110, 20));
        back.addActionListener(this);// adds actionlistener
        backPanel.add(back, BorderLayout.WEST);
        backBar.add(backPanel); // adds panel to menu bar
        mainFrame.setJMenuBar(backBar); // sets mb as the frame menubar

        // ROW 1
        c.gridx = 0; // cell 1
        c.gridy = 0;
        c.ipadx = 30;
        c.ipady = 30;
        mainFrame.add(new Drawing("border-l1"), c); // draws border for grid

        c.gridx = 1; // cell 2
        c.gridy = 0;
        c.ipadx = 710;
        c.ipady = 30;
        mainFrame.add(new Drawing("border-t"), c);

        c.gridx = 2; // cell 3
        c.gridy = 0;
        c.ipadx = 30;
        c.ipady = 30;
        mainFrame.add(new Drawing("border-r1"), c);

        // ROW2
        c.gridx = 0; // cell 1
        c.gridy = 1;
        c.ipadx = 30;
        c.ipady = 380;
        mainFrame.add(new Drawing("border-l2"), c);

        c.gridx = 1; // cell 2
        c.gridy = 1;
        c.ipadx = 0;
        c.ipady = 0;

        mainPanel.setPreferredSize(new Dimension(700, 380)); // sets Panel size to 700 by 380
        mainPanel.setBackground(beige); // sets background as beige
        mainPanel.setLayout(new FlowLayout()); // sets layout to flow layout
        mainFrame.add(mainPanel, c); // adds panel to frame

        c.gridx = 2; // cell 3
        c.gridy = 1;
        c.ipadx = 30;
        c.ipady = 380;
        mainFrame.add(new Drawing("border-r2"), c);

        // ROW 3
        c.gridx = 0; // cell 1
        c.gridy = 2;
        c.ipadx = 30;
        c.ipady = 30;
        mainFrame.add(new Drawing("border-l3"), c);

        c.gridx = 1; // cell 2
        c.gridy = 2;
        c.ipadx = 710;
        c.ipady = 30;
        mainFrame.add(new Drawing("border-b"), c);

        c.gridx = 2; // cell 3
        c.gridy = 2;
        c.ipadx = 30;
        c.ipady = 30;
        mainFrame.add(new Drawing("border-r3"), c);
        
        mainFrame.setResizable(false); // does not allow frame to be resized
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // terminates program once closed
    }

    /**
     * method to select a date
     */
    private void selectDate() {
        back.setActionCommand("menu");
        mainPanel.removeAll();
        mainPanel.revalidate();
        mainPanel.repaint();

        JPanel titlePanel = new JPanel(); // creates panel containing the title part of the frame
        titlePanel.setPreferredSize(new Dimension(690, 90)); // sets panel size to 690 by 90
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS)); // sets layout to boxlayout and aligns
                                                                           // components vertically
        titlePanel.setBackground(beige); // sets background to beige

        JLabel header = new JLabel("Flights of August 2022"); // creates title label
        header.setAlignmentX(Component.CENTER_ALIGNMENT); // aligns title to the center
        Font title = this.title.deriveFont(Font.PLAIN, 38f); // creates title font
        header.setFont(title); // sets header font to title
        header.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0)); // creates border for header
        titlePanel.add(header); // adds header to title panel

        JLabel subHeader = new JLabel("Select Date:"); // creates subheading label
        subHeader.setFont(new Font("calibri", 0, 14)); // modifeis its font
        subHeader.setAlignmentX(Component.CENTER_ALIGNMENT); // aligns subheader to the center
        titlePanel.add(subHeader); // adds subheader to title panel
        mainPanel.add(titlePanel); // adds title panel to main panel

        JPanel calendar = new JPanel(); // creates a panel for the calendar
        calendar.setBackground(beige); // sets panel background to beige
        calendar.setPreferredSize(new Dimension(600, 240)); // sets panel size to 600 by 240
        calendar.setLayout(new GridLayout(6, 7)); // sets Calendar layout to grid layout

        Font weekday = this.title.deriveFont(Font.BOLD, 16f); // creates weekday font
        JLabel sun = new JLabel("S", JLabel.CENTER); // creates weekday labels
        sun.setFont(weekday);
        JLabel mon = new JLabel("M", JLabel.CENTER);
        mon.setFont(weekday);
        JLabel tue = new JLabel("T", JLabel.CENTER);
        tue.setFont(weekday);
        JLabel wed = new JLabel("W", JLabel.CENTER);
        wed.setFont(weekday);
        JLabel thu = new JLabel("T", JLabel.CENTER);
        thu.setFont(weekday);
        JLabel fri = new JLabel("F", JLabel.CENTER);
        fri.setFont(weekday);
        JLabel sat = new JLabel("S", JLabel.CENTER);
        sat.setFont(weekday);

        JButton empty1 = new JButton("31"), empty2 = new JButton("1"), empty3 = new JButton("2"), empty4 = new JButton("3"); // creates empty buttons

        empty1.setBackground(backgroundBlue); // empty button 1
        empty1.setOpaque(true);
        empty1.setBorderPainted(true);
        empty1.setBorder(BorderFactory.createLineBorder(brightBlue, 1));
        empty1.setFont(button);
        empty1.setEnabled(false);

        empty2.setBackground(backgroundBlue); // empty button 2
        empty2.setOpaque(true);
        empty2.setBorderPainted(true);
        empty2.setBorder(BorderFactory.createLineBorder(brightBlue, 1));
        empty2.setFont(button);
        empty2.setEnabled(false);

        empty3.setBackground(backgroundBlue); // empty button 3
        empty3.setOpaque(true);
        empty3.setBorderPainted(true);
        empty3.setBorder(BorderFactory.createLineBorder(brightBlue, 1));
        empty3.setFont(button);
        empty3.setEnabled(false);

        empty4.setBackground(backgroundBlue); // empty button 4
        empty4.setOpaque(true);
        empty4.setBorderPainted(true);
        empty4.setBorder(BorderFactory.createLineBorder(brightBlue, 1));
        empty4.setFont(button);
        empty4.setEnabled(false);

        calendar.add(sun); // adds weekdays to calendar
        calendar.add(mon);
        calendar.add(tue);
        calendar.add(wed);
        calendar.add(thu);
        calendar.add(fri);
        calendar.add(sat);
        calendar.add(empty1); // adds first empty day to calendar

        JButton[] date = new JButton[31]; // creates array of all selectable date buttons
        for (int i = 0; i < date.length; i++) { // for loop to iterate through the array of buttons
            date[i] = new JButton("" + (1 + i)); // defines the button
            date[i].setBackground(Color.white); // sets button background to white
            date[i].setOpaque(true);
            date[i].setBorderPainted(true);
            date[i].setBorder(BorderFactory.createLineBorder(brightBlue, 1)); // creates button border
            date[i].setFont(button); // set button font to button
            date[i].addActionListener(this); // adds action listener
            date[i].setActionCommand(("bookDate" + (i + 1))); // sets action command
            calendar.add(date[i]); // adds button to calendar
        }
        calendar.add(empty2); // adds remaining empty buttons
        calendar.add(empty3);
        calendar.add(empty4);
        mainPanel.add(calendar); // adds calendar to panel

        mainFrame.setVisible(true); // makes frame visible
    }

    /**
     * method to select a flight
     */
    private void selectFlight() {
        // sets action command according to menu button chosen
        if (mode == 1) {
            back.setActionCommand("book");
        } else if (mode == 2) {
            back.setActionCommand("view");
        } else if (mode == 3) {
            back.setActionCommand("exp");
        }
        mainPanel.removeAll();
        mainPanel.revalidate();
        mainPanel.repaint();

        JPanel titlePanel = new JPanel(); // creates panel containing the title part of the frame
        titlePanel.setPreferredSize(new Dimension(690, 200)); // sets panel size to 690 by 200
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS)); // sets layout to boxlayout and aligns components vertically
        titlePanel.setBackground(beige); // sets background to beige

        JLabel logo = new JLabel(this.logo);
        titlePanel.add(logo); // adds logo
        logo.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0)); // creates logo border
        logo.setAlignmentX(Component.CENTER_ALIGNMENT); // aligns to the center
        JLabel header = new JLabel("Flights of August " + tempDate + ", 2022"); // creates title label
        header.setAlignmentX(Component.CENTER_ALIGNMENT); // aligns title to the center
        Font title = this.title.deriveFont(Font.PLAIN, 38f); // creates title font
        header.setFont(title); // sets header font to title
        header.setBorder(BorderFactory.createEmptyBorder(25, 0, 5, 0)); // creates border for header
        titlePanel.add(header); // adds header to title panel

        JLabel subHeader = new JLabel("Select flight:"); // creates subheading label
        subHeader.setFont(new Font("calibri", 0, 14)); // modifeis its font
        subHeader.setAlignmentX(Component.CENTER_ALIGNMENT); // aligns subheader to the center
        subHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0)); // creates border for subheader
        titlePanel.add(subHeader); // adds subheader to title panel
        mainPanel.add(titlePanel); // adds title panel to main panel

        Font label = title.deriveFont(Font.PLAIN, 20f); // creates weekday font
        JPanel flightPanel = new JPanel(), flight1Panel = new JPanel(), flight2Panel = new JPanel(); // panels to display flights
        flightPanel.setBackground(beige); // sets panels to beige
        flightPanel.setPreferredSize(new Dimension(650, 120)); // sets Panel size to 500 by 200
        flightPanel.setLayout(new FlowLayout()); // sets flight Panel layout to flowlayout

        for (int i = 0; i < flights.size(); i++) {
            if (flight1Temp == -1 && flights.get(i).getDate() == tempDate) {
                flight1Temp = i;
            }
            if (flight1Temp != -1 && i != flight1Temp && flights.get(i).getDate() == tempDate) {
                flight2Temp = i;
            }
        }

        if (flight1Temp == -1) { // if no flights are available
            JLabel error = new JLabel(" No Flights Available"); // error label
            error.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
            error.setFont(label);
            flightPanel.add(error);
        } else { // adds button for 1st flight
            flight1Panel.setBackground(beige);
            JLabel plane1 = new JLabel(flights.get(flight1Temp).getPlane());
            plane1.setPreferredSize(new Dimension(130, 30));
            plane1.setFont(label);
            JButton flight1 = new JButton(
                    flights.get(flight1Temp).getDepart() + " to " + flights.get(flight1Temp).getDestination());
            flight1.setPreferredSize(new Dimension(350, 30));
            flight1.setBackground(backgroundBlue);
            flight1.setOpaque(true);
            flight1.setBorderPainted(true);
            flight1.setBorder(BorderFactory.createLineBorder(brightBlue, 1));
            flight1.addActionListener(this);
            flight1.setActionCommand("bookFlights1");
            flight1.setFont(button);
            flight1Panel.add(plane1);
            flight1Panel.add(flight1);
            flightPanel.add(flight1Panel);
        }

        if (flight2Temp != -1 && flight1Temp != -1) { // adds button for second flight if exists
            flight2Panel.setBackground(beige);
            JLabel plane2 = new JLabel(flights.get(flight2Temp).getPlane());
            plane2.setPreferredSize(new Dimension(130, 60));
            plane2.setFont(label);
            JButton flight2 = new JButton(
                    flights.get(flight2Temp).getDepart() + " to " + flights.get(flight2Temp).getDestination());
            flight2.setPreferredSize(new Dimension(350, 30));
            flight2.setBackground(backgroundBlue);
            flight2.setOpaque(true);
            flight2.setBorderPainted(true);
            flight2.setBorder(BorderFactory.createLineBorder(brightBlue, 1));
            flight2.setFont(button);
            flight2.addActionListener(this);
            flight2.setActionCommand("bookFlights2");
            flight2Panel.add(plane2);
            flight2Panel.add(flight2);
            flightPanel.add(flight2Panel);
        }
        mainPanel.add(flightPanel); // adds buttons to frame
    }

    /**
     * method to select a seat
     */
    private void selectSeat() {
        mainPanel.removeAll();
        mainPanel.revalidate();
        mainPanel.repaint();
        back.setActionCommand("bookDate" + tempDate);

        JPanel titlePanel = new JPanel(); // creates panel containing the title part of the frame
        titlePanel.setPreferredSize(new Dimension(690, 180)); // sets panel size to 690 by 90
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS)); // sets layout to boxlayout and aligns components vertically
        titlePanel.setBackground(beige); // sets background to beige

        JLabel logo = new JLabel(this.logo);
        titlePanel.add(logo); // adds logo
        logo.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel header = new JLabel(flights.get(flightIndex).getPlane() + " Seating on August " + tempDate + ", 2022"); // creates title label
        header.setAlignmentX(Component.CENTER_ALIGNMENT); // aligns title to the center
        Font title = this.title.deriveFont(Font.PLAIN, 32f); // creates title font
        header.setFont(title); // sets header font to title
        header.setBorder(BorderFactory.createEmptyBorder(25, 0, 5, 0)); // creates border for header
        titlePanel.add(header); // adds header to title panel

        JLabel subHeader = new JLabel( flights.get(flightIndex).getDepart() + " to " + flights.get(flightIndex).getDestination()); // creates subheading label
        subHeader.setFont(new Font("calibri", 0, 14)); // modifeis its font
        subHeader.setAlignmentX(Component.CENTER_ALIGNMENT); // aligns subheader to the center
        subHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // creates border for subheader
        titlePanel.add(subHeader); // adds subheader to title panel
        mainPanel.add(titlePanel); // adds title panel to main panel

        JPanel seatPanel = new JPanel(); // panel to display seats
        seatPanel.setBackground(beige); // sets panels to beige
        seatPanel.setPreferredSize(new Dimension(325, 120)); // sets Panel size to 500 by 200
        seatPanel.setLayout(new FlowLayout()); // sets flight Panel layout to flowlayout

        for (int i = 0; i < 10; i++) { // for loop to add the seat buttons
            if (i < 9) {
                seats[i] = new JButton(0 + "" + (i + 1));
            } else {
                seats[i] = new JButton("" + (i + 1));
            }
            if (flights.get(flightIndex).getAvailable(i) == false) {
                seats[i].setBackground(cornflowerBlue); // if occupied makes button unclickable and cornflowerBlue
                seats[i].setEnabled(false);
            } else {
                seats[i].setBackground(backgroundBlue); // sets button background to backgroundBlue
            }
            seats[i].setOpaque(true);
            seats[i].setBorderPainted(true);
            seats[i].setPreferredSize(new Dimension(50, 50));
            seats[i].setBorder(BorderFactory.createLineBorder(brightBlue, 1)); // creates button border
            seats[i].setFont(button); // set button font to button
            seats[i].addActionListener(this);
            seats[i].setActionCommand("bookSeats" + (i + 1));
            tempSeatSelected[i] = false; // intializes tempSeatSelected
            seatPanel.add(seats[i]); // adds button to calendar
        }
        mainPanel.add(seatPanel); // adds seats to GUI panel

        JPanel legend = new JPanel(); // panel to display legend
        legend.setBackground(beige);
        legend.setPreferredSize(new Dimension(500, 30));

        JButton taken = new JButton(" "); // taken button and label
        taken.setBackground(cornflowerBlue);
        taken.setOpaque(true);
        taken.setBorderPainted(true);
        taken.setPreferredSize(new Dimension(15, 15));
        taken.setBorder(BorderFactory.createLineBorder(brightBlue, 1)); // creates button border
        taken.setEnabled(false);
        legend.add(taken);
        JLabel takenLabel = new JLabel("Taken");
        takenLabel.setFont(new Font("calibri", 0, 14));
        legend.add(takenLabel);

        JButton vacant = new JButton(" "); // vacant button and label
        vacant.setBackground(backgroundBlue); // sets button background to background blue
        vacant.setOpaque(true);
        vacant.setBorderPainted(true);
        vacant.setPreferredSize(new Dimension(15, 15));
        vacant.setBorder(BorderFactory.createLineBorder(brightBlue, 1)); // creates button border
        vacant.setEnabled(false);
        legend.add(vacant);
        JLabel vacantLabel = new JLabel("Vacant");
        vacantLabel.setFont(new Font("calibri", 0, 14));
        legend.add(vacantLabel);

        JButton selected = new JButton(" "); // selected button and label
        selected.setBackground(beige); // sets button background to beige
        selected.setOpaque(true);
        selected.setBorderPainted(true);
        selected.setPreferredSize(new Dimension(15, 15));
        selected.setBorder(BorderFactory.createLineBorder(brightBlue, 1)); // creates button border
        selected.setEnabled(false);
        legend.add(selected);
        JLabel selectedLabel = new JLabel("Selected");
        selectedLabel.setFont(new Font("calibri", 0, 14));
        legend.add(selectedLabel);
        mainPanel.add(legend);

        JButton submitButton = new JButton("Submit"); // creates submit button
        submitButton.setBackground(backgroundBlue);
        submitButton.setOpaque(true);
        submitButton.setBorderPainted(true);
        submitButton.setBorder(BorderFactory.createLineBorder(brightBlue, 2));
        submitButton.setFont(button);
        submitButton.addActionListener(this);
        submitButton.setActionCommand("bookSeatSubmit");

        JPanel filler7 = new JPanel(), filler8 = new JPanel(), filler9 = new JPanel(), filler10 = new JPanel(),
                filler11 = new JPanel(), filler12 = new JPanel();
        filler7.setBackground(beige); // creates filler panels
        filler8.setBackground(beige);
        filler9.setBackground(beige);
        filler10.setBackground(beige);
        filler11.setBackground(beige);
        filler12.setBackground(beige);

        JPanel submitBar = new JPanel(); // creates panel for submit button
        submitBar.setPreferredSize(new Dimension(690, 25));
        submitBar.setBackground(beige); // sets color to beige
        submitBar.setLayout(new GridLayout(1, 7)); // sets layout to grid layout
        submitBar.add(filler7); // adds filler to make button 1/7th of the frame in size and aligned to the
                                // right corner
        submitBar.add(filler8);
        submitBar.add(filler9);
        submitBar.add(filler10);
        submitBar.add(filler11);
        submitBar.add(filler12);
        submitBar.add(submitButton);
        mainPanel.add(submitBar); // adds submit button panel to title panel
    }

    /**
     * Method for user input
     */
    private void enterInfo() {
        mainPanel.removeAll();
        mainPanel.revalidate();
        mainPanel.repaint();

        if (flightChosen == 1) { // sets action command according to selected button
            back.setActionCommand("bookFlights1");
        } else if (flightChosen == 2) {
            back.setActionCommand("bookFlights2");
        }

        JPanel titlePanel = new JPanel(); // creates panel containing the title part of the frame
        titlePanel.setPreferredSize(new Dimension(690, 134)); // sets panel size to 690 by 120
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS)); // sets layout to boxlayout and aligns
                                                                           // components vertically
        titlePanel.setBackground(beige); // sets background to beige

        JLabel logo = new JLabel(this.logo); // logo
        titlePanel.add(logo);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel header = new JLabel("Client information"); // creates title label
        header.setAlignmentX(Component.CENTER_ALIGNMENT); // aligns title to the center
        Font title = this.title.deriveFont(Font.PLAIN, 38f); // creates title font
        header.setFont(title); // sets header font to title
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0)); // creates border for header
        titlePanel.add(header); // adds header to title panel

        JLabel subHeader = new JLabel("Please fill out the following fields"); // creates subheading label
        subHeader.setFont(new Font("calibri", 0, 12)); // modifeis its font
        subHeader.setAlignmentX(Component.CENTER_ALIGNMENT); // aligns subheader to the center
        subHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // creates border for subHeader
        titlePanel.add(subHeader); // adds subheader to title panel
        mainPanel.add(titlePanel); // adds title panel to main panel

        Font label = this.title.deriveFont(Font.PLAIN, 16f); // creates label font

        JPanel firstPanel = new JPanel(), lastPanel = new JPanel(), dobPanel = new JPanel(), emailPanel = new JPanel(), phonePanel = new JPanel(); // creates panels

        firstPanel.setBackground(beige); // firstName panel contents
        JLabel firstLabel = new JLabel("First Name:");
        firstLabel.setFont(label);
        firstLabel.setPreferredSize(new Dimension(110, 18));
        firstField = new JTextField(25);
        firstPanel.add(firstLabel);
        firstPanel.add(firstField);
        mainPanel.add(firstPanel);

        lastPanel.setBackground(beige); // last name panel contents
        JLabel lastLabel = new JLabel("Last Name:");
        lastLabel.setFont(label);
        lastLabel.setPreferredSize(new Dimension(110, 18));
        lastField = new JTextField(25);
        lastPanel.add(lastLabel);
        lastPanel.add(lastField);
        mainPanel.add(lastPanel);

        dobPanel.setBackground(beige); // dob panel contents
        JLabel dobLabel = new JLabel("Date of Birth:");
        dobLabel.setFont(label);
        dobLabel.setPreferredSize(new Dimension(110, 18));
        dobField = new JTextField(25);
        dobPanel.add(dobLabel);
        dobPanel.add(dobField);
        mainPanel.add(dobPanel);

        emailPanel.setBackground(beige); // email panel contents
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(label);
        emailLabel.setPreferredSize(new Dimension(110, 18));
        emailField = new JTextField(25);
        emailPanel.add(emailLabel);
        emailPanel.add(emailField);
        mainPanel.add(emailPanel);

        phonePanel.setBackground(beige); // phone panel contents
        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setFont(label);
        phoneLabel.setPreferredSize(new Dimension(110, 18));
        phoneField = new JTextField(25);
        phonePanel.add(phoneLabel);
        phonePanel.add(phoneField);
        mainPanel.add(phonePanel);

        JPanel submitBar = new JPanel(); // creates panel for submit button
        submitBar.setBackground(beige); // sets color to beige
        submitBar.setPreferredSize(new Dimension(690, 50));
        submitBar.setLayout(new FlowLayout());

        JLabel spaceLabel1 = new JLabel(""); // label to indent
        spaceLabel1.setPreferredSize(new Dimension(240, 30));
        submitBar.add(spaceLabel1);
        JLabel currentSeatLabel = new JLabel("Seat #:"); // label for current seat number
        submitBar.add(currentSeatLabel);
        currentSeatLabel.setFont(label);
        currentSeatLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JTextField currentSeat = new JTextField("0" + (tempSeat.get(currentCounter) + 1)); // textfield containing current seat number
        if ((tempSeat.get(currentCounter) + 1) == 10) {
            currentSeat.setText("10");
        }
        currentSeat.setEditable(false);
        submitBar.add(currentSeat);

        JButton submitButton = new JButton("Submit"); // creates submit button
        submitButton.setBackground(backgroundBlue);
        submitButton.setOpaque(true);
        submitButton.setBorderPainted(true);
        submitButton.setBorder(BorderFactory.createLineBorder(brightBlue, 2));
        submitButton.setFont(button);
        submitButton.setPreferredSize(new Dimension(90, 25));
        submitButton.addActionListener(this);
        submitButton.setActionCommand("submitInfo");
        submitBar.add(submitButton);
        mainPanel.add(submitBar);
    }

    /**
     * method to show ticket summary
     */
    private void summary() {
        mainPanel.removeAll();
        mainPanel.revalidate();
        mainPanel.repaint();
        back.setActionCommand("book");

        JPanel titlePanel = new JPanel(); // creates panel containing the title part of the frame
        titlePanel.setPreferredSize(new Dimension(690, 142)); // sets panel size to 690 by 90
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS)); // sets layout to boxlayout and aligns components vertically
        titlePanel.setBackground(beige); // sets background to beige

        JLabel logo = new JLabel(this.logo); // logo
        titlePanel.add(logo);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel header = new JLabel("Ticket Summary"); // creates title label
        header.setAlignmentX(Component.CENTER_ALIGNMENT); // aligns title to the center
        Font title = this.title.deriveFont(Font.PLAIN, 38f); // creates title font
        header.setFont(title); // sets header font to title
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0)); // creates border for header
        titlePanel.add(header); // adds header to title panel
        mainPanel.add(titlePanel); // adds title panel to main panel

        JPanel ticketPanel = new JPanel(); // creates a panel for the calendar
        ticketPanel.setBackground(beige); // sets panel background to beige
        ticketPanel.setPreferredSize(new Dimension(700, 200)); // sets panel size to 620by 260
        ticketPanel.setLayout(new FlowLayout()); // sets Calendar layout to grid layout

        JPanel datePanel = new JPanel(), seatPanel = new JPanel(), planePanel = new JPanel(),
                departurePanel = new JPanel(), arrivalPanel = new JPanel(), pricePanel = new JPanel(); // panels for each category
                                                                                                       // of information related to flight
        datePanel.setBackground(beige);
        seatPanel.setBackground(beige);
        planePanel.setBackground(beige);
        departurePanel.setBackground(beige);
        arrivalPanel.setBackground(beige);
        pricePanel.setBackground(beige);

        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.Y_AXIS));
        seatPanel.setLayout(new BoxLayout(seatPanel, BoxLayout.Y_AXIS));
        planePanel.setLayout(new BoxLayout(planePanel, BoxLayout.Y_AXIS));
        departurePanel.setLayout(new BoxLayout(departurePanel, BoxLayout.Y_AXIS));
        arrivalPanel.setLayout(new BoxLayout(arrivalPanel, BoxLayout.Y_AXIS));
        pricePanel.setLayout(new BoxLayout(pricePanel, BoxLayout.Y_AXIS));

        datePanel.setPreferredSize(new Dimension(90, 240));
        seatPanel.setPreferredSize(new Dimension(90, 240));
        planePanel.setPreferredSize(new Dimension(120, 240));
        departurePanel.setPreferredSize(new Dimension(140, 240));
        arrivalPanel.setPreferredSize(new Dimension(140, 240));
        pricePanel.setPreferredSize(new Dimension(90, 240));

        Font label = this.title.deriveFont(Font.PLAIN, 18f); // creates label font
        JLabel dateLabel = new JLabel("Date:"); // creates labels for each category
        dateLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        dateLabel.setFont(label);
        datePanel.add(dateLabel);

        JLabel seatLabel = new JLabel("Seat:");
        seatLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        seatLabel.setFont(label);
        seatPanel.add(seatLabel);

        JLabel planeLabel = new JLabel("Plane:");
        planeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        planeLabel.setFont(label);
        planePanel.add(planeLabel);

        JLabel departureLabel = new JLabel("Departure City:");
        departureLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        departureLabel.setFont(label);
        departurePanel.add(departureLabel);

        JLabel arrivalLabel = new JLabel("Arrival City:");
        arrivalLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        arrivalLabel.setFont(label);
        arrivalPanel.add(arrivalLabel);

        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        priceLabel.setFont(label);
        pricePanel.add(priceLabel);

        JLabel[] date = new JLabel[tempSeat.size()]; // parallel arrays of JLabels with each flight's details
        JLabel[] seat = new JLabel[tempSeat.size()];
        JLabel[] plane = new JLabel[tempSeat.size()];
        JLabel[] depart = new JLabel[tempSeat.size()];
        JLabel[] arrival = new JLabel[tempSeat.size()];
        JLabel[] price = new JLabel[tempSeat.size()];

        int sum = 0; // variable to store the total ticket cost
        int totalSum = 0;

        Font subLabel = new Font("calibri", Font.BOLD, 12); // sub label font

        for (int i = 0; i < tempSeat.size(); i++) { // for loop to display flight information for each selected seat
            date[i] = new JLabel("08/" + tempDate + "/22");
            date[i].setFont(subLabel);
            datePanel.add(date[i]);
            if (tempSeat.get(i) == 9) {
                seat[i] = new JLabel("10");
            } else {
                seat[i] = new JLabel("0" + (tempSeat.get(i) + 1));
            }

            seat[i].setFont(subLabel);
            seatPanel.add(seat[i]);
            plane[i] = new JLabel(flights.get(flightIndex).getPlane());
            plane[i].setFont(subLabel);
            planePanel.add(plane[i]);
            depart[i] = new JLabel(flights.get(flightIndex).getDepart());
            depart[i].setFont(subLabel);
            departurePanel.add(depart[i]);
            arrival[i] = new JLabel(flights.get(flightIndex).getDestination());
            arrival[i].setFont(subLabel);
            arrivalPanel.add(arrival[i]);
            if (flights.get(flightIndex).getAge(i) < 18) { // sets price to $150 for children
                sum += 170;
            } else {
                sum += 250;
            }

            if (flights.get(flightIndex).getPlane().equals("Airbus A-1784")) {
                sum += 340;
            } else {
                sum += 160;
            }

            if (flights.get(flightIndex).getDepart().equals("Stockholm")) {
                sum += 155;
            } else if (flights.get(flightIndex).getDepart().equals("Tokyo")) {
                sum += 275;
            } else {
                sum += 160;
            }

            if (flights.get(flightIndex).getDestination().equals("Stockholm")) {
                sum += 120;
            } else if (flights.get(flightIndex).getDestination().equals("Tokyo")) {
                sum += 215;
            } else {
                sum += 375;
            }
            price[i] = new JLabel("$" + sum);
            price[i].setFont(subLabel);
            pricePanel.add(price[i]);
            totalSum += sum;
            sum = 0;
        }

        ticketPanel.add(datePanel); // adds panels to frame
        ticketPanel.add(seatPanel);
        ticketPanel.add(planePanel);
        ticketPanel.add(departurePanel);
        ticketPanel.add(arrivalPanel);
        ticketPanel.add(pricePanel);

        JPanel totalPanel = new JPanel();
        totalPanel.setBackground(beige);
        totalPanel.setPreferredSize(new Dimension(100, 25));
        JLabel totalLabel = new JLabel("Total:"); // label and textfield to display total cost
        totalLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        totalLabel.setFont(label);
        totalPanel.add(totalLabel);
        JTextField total = new JTextField("$" + totalSum);
        total.setEditable(false);
        totalPanel.add(total);
        titlePanel.add(totalPanel);
        mainPanel.add(ticketPanel); // adds calendar to panel

        JButton done = new JButton("Done"); // done button
        done.setBackground(backgroundBlue);
        done.setOpaque(true);
        done.setBorderPainted(true);
        done.setBorder(BorderFactory.createLineBorder(brightBlue, 2));
        done.setFont(button);
        done.setPreferredSize(new Dimension(70, 23));
        done.addActionListener(this);
        done.setActionCommand("menu");
        mainPanel.add(done);
        tempSeat.clear();
    }

    /**
     * method to view flight contact info/cancel flight
     */
    private void viewFlight() {
        mainPanel.removeAll();
        mainPanel.revalidate();
        mainPanel.repaint();
        back.setActionCommand("bookDate" + tempDate);

        JPanel titlePanel = new JPanel(); // creates panel containing the title part of the frame
        titlePanel.setPreferredSize(new Dimension(690, 53)); // sets panel size to 690 by 90
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS)); // sets layout to boxlayout and aligns
                                                                           // components vertically
        titlePanel.setBackground(beige); // sets background to beige

        JLabel header = new JLabel(flights.get(flightIndex).getPlane() + " August " + tempDate + ", 2022"); // creates title label
        header.setAlignmentX(Component.CENTER_ALIGNMENT); // aligns title to the center
        Font title = this.title.deriveFont(Font.PLAIN, 32f); // creates title font
        header.setFont(title); // sets header font to title
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0)); // creates border for header
        titlePanel.add(header); // adds header to title panel

        JLabel subHeader = new JLabel("Flight Information"); // creates subheading label
        subHeader.setFont(new Font("calibri", 0, 14)); // modifeis its font
        subHeader.setAlignmentX(Component.CENTER_ALIGNMENT); // aligns subheader to the center
        subHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // creates border for subHeader
        titlePanel.add(subHeader); // adds subheader to title panel
        mainPanel.add(titlePanel); // adds title panel to main panel

        JPanel westPanel = new JPanel(); // creates west panel for components on left side of frame
        westPanel.setBackground(beige);
        westPanel.setPreferredSize(new Dimension(550, 330));

        JPanel labelPanel = new JPanel(); // panel containing labels
        labelPanel.setBackground(beige);
        labelPanel.setPreferredSize(new Dimension(550, 24));

        JTextField seatLabel = new JTextField("     Seat #"); // seat label
        seatLabel.setPreferredSize(new Dimension(105, 24));
        seatLabel.setFont(button);
        seatLabel.setEditable(false);
        seatLabel.setBackground(backgroundBlue);
        labelPanel.add(seatLabel);

        JTextField emailLabel = new JTextField("                    Client Email"); // email label
        emailLabel.setPreferredSize(new Dimension(300, 24));
        emailLabel.setBackground(backgroundBlue);
        emailLabel.setFont(button);
        emailLabel.setEditable(false);

        labelPanel.add(emailLabel); // empty button for spacing
        JButton empty = new JButton("      ");
        empty.setPreferredSize(new Dimension(120, 24));
        empty.setFont(button);
        empty.setEnabled(false);
        empty.setBackground(beige);
        empty.setOpaque(true);
        empty.setBorderPainted(true);
        empty.setBorder(BorderFactory.createLineBorder(beige, 0));
        labelPanel.add(empty);
        westPanel.add(labelPanel);

        JPanel[] rows = new JPanel[10]; // parallel arrays of components
        JTextField[] seats = new JTextField[10];
        JTextField[] emails = new JTextField[10];
        JButton[] cancelButtons = new JButton[10];

        for (int i = 0; i < 10; i++) {
            rows[i] = new JPanel(); // panel
            rows[i].setBackground(beige);
            rows[i].setPreferredSize(new Dimension(550, 24));
            if (i < 9) {
                seats[i] = new JTextField("      0" + (i + 1)); // seat
            } else {
                seats[i] = new JTextField("      10");
            }
            seats[i].setPreferredSize(new Dimension(105, 24));
            seats[i].setFont(button); // modifeis its font
            seats[i].setEditable(false);
            emails[i] = new JTextField("" + flights.get(flightIndex).getEmail(i));
            emails[i].setPreferredSize(new Dimension(300, 24));
            emails[i].setFont(button); // modifeis its font
            emails[i].setEditable(false);
            cancelButtons[i] = new JButton("Cancel");
            cancelButtons[i].setFont(new Font("calibri", 1, 12));
            cancelButtons[i].setHorizontalAlignment(SwingConstants.CENTER);
            cancelButtons[i].setBackground(backgroundBlue);
            cancelButtons[i].setOpaque(true);
            cancelButtons[i].setBorderPainted(true);
            cancelButtons[i].setBorder(BorderFactory.createLineBorder(brightBlue, 0));
            cancelButtons[i].setPreferredSize(new Dimension(120, 24));
            cancelButtons[i].addActionListener(this);
            cancelButtons[i].setActionCommand("cancelSeat" + (i + 1));
            rows[i].add(seats[i]);
            rows[i].add(emails[i]);
            rows[i].add(cancelButtons[i]);
            westPanel.add(rows[i]);
        }

        JPanel eastPanel = new JPanel(), fillerPanel = new JPanel(); // east panel for components on the right
        fillerPanel.setBackground(beige);
        fillerPanel.setPreferredSize(new Dimension(100, 256)); // filler panel
        eastPanel.add(fillerPanel);
        eastPanel.setBackground(beige);
        eastPanel.setPreferredSize(new Dimension(100, 340));

        JButton cancelFlight = new JButton("Cancel Flight"); // cancel flight button
        cancelFlight.setFont(new Font("calibri", 1, 12));
        cancelFlight.setBackground(cornflowerBlue);
        cancelFlight.setOpaque(true);
        cancelFlight.setBorderPainted(true);
        cancelFlight.setBorder(BorderFactory.createLineBorder(brightBlue, 0));
        cancelFlight.setPreferredSize(new Dimension(95, 24));
        cancelFlight.addActionListener(this);
        cancelFlight.setActionCommand("cancelFlight");
        eastPanel.add(cancelFlight);

        JButton done = new JButton("Done"); // done button
        done.setFont(new Font("calibri", 1, 12));
        done.setBackground(cornflowerBlue);
        done.setOpaque(true);
        done.setBorderPainted(true);
        done.setBorder(BorderFactory.createLineBorder(brightBlue, 0));
        done.setPreferredSize(new Dimension(95, 24));
        done.addActionListener(this);
        done.setActionCommand("menu");
        eastPanel.add(done);

        JPanel content = new JPanel(); // adds panels to bigger panel
        content.setBackground(beige);
        content.setLayout(new BorderLayout());
        content.add(westPanel, BorderLayout.WEST);
        content.add(eastPanel, BorderLayout.EAST);
        mainPanel.add(content); // adds bigger panel to GUI panel
    }

    /**
     * method to export flight info
     */
    private void export() {
        mainPanel.removeAll();
        mainPanel.revalidate();
        mainPanel.repaint();

        JPanel titlePanel = new JPanel(); // creates panel containing the title part of the frame
        titlePanel.setPreferredSize(new Dimension(690, 133)); // sets panel size to 690 by 50
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS)); // sets layout to boxlayout and aligns
                                                                           // components vertically
        titlePanel.setBackground(beige); // sets background to beige

        JLabel logo = new JLabel(this.logo); // logo
        titlePanel.add(logo);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel header = new JLabel(flights.get(flightIndex).getPlane() + " August " + tempDate + ", 2022"); // creates
                                                                                                            // title
                                                                                                            // label
        header.setAlignmentX(Component.CENTER_ALIGNMENT); // aligns title to the center
        Font title = this.title.deriveFont(Font.PLAIN, 32f); // creates title font
        header.setFont(title); // sets header font to title
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0)); // creates border for header
        titlePanel.add(header); // adds header to title panel

        JLabel subHeader = new JLabel("Export"); // creates subheading label
        subHeader.setFont(new Font("calibri", 0, 14)); // modifeis its font
        subHeader.setAlignmentX(Component.CENTER_ALIGNMENT); // aligns subheader to the center
        subHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // creates border for subHeader
        titlePanel.add(subHeader); // adds subheader to title panel
        mainPanel.add(titlePanel); // adds title panel to main panel

        JPanel dataPanel = new JPanel(); // data panel
        dataPanel.setPreferredSize(new Dimension(700, 190));

        String[] temp; // For swapping in the algorithm

        for (int j = 0; j < 10; j++) {
            printList[j] = flights.get(flightIndex).getString(j);
        }

        for (int j = 0; j < printList.length - 1; j++) {
            for (int i = j + 1; i < printList.length; i++) {
                if (!flights.get(flightIndex).getAvailable(j)) {
                    if (orderLast(printList[j][2], printList[i][2], printList[j][1], printList[i][1])) {
                        temp = printList[j];
                        printList[j] = printList[i];
                        printList[i] = temp;
                    }
                }
            }
        }

        String[][] info = new String[10][];

        for (int i = 0; i < 10; i++) {
            if (displaySorted) {
                info[i] = printList[i];
            } else {
                info[i] = flights.get(flightIndex).getString(i);
            }
        }

        JLabel sortLabel = new JLabel(); // sort label
        if (displaySorted) {
            sortLabel = new JLabel("Alphabetical (A-Z)");
        } else if (displaySorted == false) {
            sortLabel = new JLabel("Numerical (Seat #)");
        }

        Font labels1 = new Font("calibri", Font.BOLD, 12);
        sortLabel.setFont(labels1);
        sortLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sortLabel.setPreferredSize(new Dimension(700, 15));
        dataPanel.add(sortLabel);

        JPanel seatPanel = new JPanel(), firstPanel = new JPanel(), lastPanel = new JPanel(), dobPanel = new JPanel(),
                emailPanel = new JPanel(), phonePanel = new JPanel(), agePanel = new JPanel(); // panels for each
                                                                                               // category of
                                                                                               // information related to
                                                                                               // seats
        seatPanel.setBackground(beige);
        firstPanel.setBackground(beige);
        lastPanel.setBackground(beige);
        dobPanel.setBackground(beige);
        emailPanel.setBackground(beige);
        phonePanel.setBackground(beige);
        agePanel.setBackground(beige);

        seatPanel.setLayout(new BoxLayout(seatPanel, BoxLayout.Y_AXIS));
        firstPanel.setLayout(new BoxLayout(firstPanel, BoxLayout.Y_AXIS));
        lastPanel.setLayout(new BoxLayout(lastPanel, BoxLayout.Y_AXIS));
        dobPanel.setLayout(new BoxLayout(dobPanel, BoxLayout.Y_AXIS));
        emailPanel.setLayout(new BoxLayout(emailPanel, BoxLayout.Y_AXIS));
        phonePanel.setLayout(new BoxLayout(phonePanel, BoxLayout.Y_AXIS));
        agePanel.setLayout(new BoxLayout(agePanel, BoxLayout.Y_AXIS));

        seatPanel.setPreferredSize(new Dimension(40, 240));
        firstPanel.setPreferredSize(new Dimension(90, 240));
        lastPanel.setPreferredSize(new Dimension(90, 240));
        dobPanel.setPreferredSize(new Dimension(90, 240));
        emailPanel.setPreferredSize(new Dimension(160, 240));
        phonePanel.setPreferredSize(new Dimension(130, 240));
        agePanel.setPreferredSize(new Dimension(40, 240));

        Font label = this.title.deriveFont(Font.PLAIN, 18f); // creates label font
        JLabel seatLabel = new JLabel("Seat:"); // creates labels for each category
        seatLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        seatLabel.setFont(label);
        seatPanel.add(seatLabel);

        JLabel firstLabel = new JLabel("First Name:");
        firstLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        firstLabel.setFont(label);
        firstPanel.add(firstLabel);

        JLabel lastLabel = new JLabel("Last Name:");
        lastLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        lastLabel.setFont(label);
        lastPanel.add(lastLabel);

        JLabel dobLabel = new JLabel("DOB:");
        dobLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        dobLabel.setFont(label);
        dobPanel.add(dobLabel);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        emailLabel.setFont(label);
        emailPanel.add(emailLabel);

        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        phoneLabel.setFont(label);
        phonePanel.add(phoneLabel);

        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        ageLabel.setFont(label);
        agePanel.add(ageLabel);

        JLabel[] seat = new JLabel[10];
        JLabel[] first = new JLabel[10];
        JLabel[] last = new JLabel[10];
        JLabel[] dob = new JLabel[10];
        JLabel[] email = new JLabel[10];
        JLabel[] phone = new JLabel[10];
        JLabel[] age = new JLabel[10];

        Font subLabel = new Font("calibri", Font.BOLD, 12); // sub label font

        for (int i = 0; i < 10; i++) { // for loop to display flight information for each selected seat
            seat[i] = new JLabel(info[i][0]);
            seat[i].setFont(subLabel);
            seatPanel.add(seat[i]);

            first[i] = new JLabel(info[i][1]);
            first[i].setFont(subLabel);
            firstPanel.add(first[i]);

            last[i] = new JLabel(info[i][2]);
            last[i].setFont(subLabel);
            lastPanel.add(last[i]);

            dob[i] = new JLabel(info[i][3]);
            dob[i].setFont(subLabel);
            dobPanel.add(dob[i]);

            email[i] = new JLabel(info[i][4]);
            email[i].setFont(subLabel);
            emailPanel.add(email[i]);

            phone[i] = new JLabel(info[i][5]);
            phone[i].setFont(subLabel);
            phonePanel.add(phone[i]);

            age[i] = new JLabel(info[i][6]);
            age[i].setFont(subLabel);
            agePanel.add(age[i]);
        }

        dataPanel.add(seatPanel); // adds panels to frame
        dataPanel.add(firstPanel);
        dataPanel.add(lastPanel);
        dataPanel.add(dobPanel);
        dataPanel.add(emailPanel);
        dataPanel.add(phonePanel);
        dataPanel.add(agePanel);
        dataPanel.setBackground(beige);

        JPanel buttonPanel = new JPanel(); // creates button panel
        buttonPanel.setBackground(beige);

        JButton exportData = new JButton("Export data"); // export Data button
        exportData.setBackground(backgroundBlue);
        exportData.setOpaque(true);
        exportData.setBorderPainted(true);
        exportData.setBorder(BorderFactory.createLineBorder(brightBlue, 2));
        exportData.setFont(button);
        exportData.setPreferredSize(new Dimension(100, 35));
        exportData.addActionListener(this);
        exportData.setActionCommand("exportData");
        buttonPanel.add(exportData); // adds export data button to button panel

        JButton sort = new JButton("Switch Display (A-Z / #)"); // sort button
        sort.setBackground(backgroundBlue);
        sort.setOpaque(true);
        sort.setBorderPainted(true);
        sort.setBorder(BorderFactory.createLineBorder(brightBlue, 2));
        sort.setFont(button);
        sort.setPreferredSize(new Dimension(200, 35));
        sort.addActionListener(this);
        sort.setActionCommand("exportSort");
        buttonPanel.add(sort); // adds sort button to button panel

        JButton done = new JButton("Done"); // done button
        done.setBackground(backgroundBlue);
        done.setOpaque(true);
        done.setBorderPainted(true);
        done.setBorder(BorderFactory.createLineBorder(brightBlue, 2));
        done.setFont(button);
        done.setPreferredSize(new Dimension(70, 35));
        done.addActionListener(this);
        done.setActionCommand("menu");
        buttonPanel.add(done); // adds done button to button panel

        mainPanel.add(dataPanel); // adds panels to ain GUI panel
        mainPanel.add(buttonPanel);
    }

    /**
     * nested drawing class for graphics
     */
    public class Drawing extends JComponent {
        String screen; // variable to store what screen to display
        Font menuTitle = new Font("Serif", 1, 56); // font for the menuTitle

        /**
         * Constructor of the Drawing class
         * 
         * @param s string storing the screen to display
         */
        public Drawing(String s) {
            screen = s; // sets screen to s

            // got the following from stack overflow:
            // https://stackoverflow.com/questions/5652344/how-can-i-use-a-custom-font-in-java
            try {
                // creates the menuTitle font
                menuTitle = Font.createFont(Font.TRUETYPE_FONT, new File("MRSEAV_7.TTF"))
                        .deriveFont(58f);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                // register the font
                ge.registerFont(title);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (FontFormatException e) {
                e.printStackTrace();
            }
        }

        /**
         * Paint method to display graphics
         * 
         * @param g instance of Graphics class
         */
        public void paint(Graphics g) {
            g.setColor(Color.black); // sets colour to black
            if (screen.equals("splash-l1")) { // draws top left corner of splash screeen
                for (int i = 0; i < 3; i++) {
                    g.drawLine(26, 26 + i, 33, 26 + i);
                    g.drawLine(26 + i, 26, 26 + i, 330);
                    g.drawLine(31 + i, 31, 31 + i, 330);
                }
            } else if (screen.equals("splash-t")) { // draws top of splash screen
                for (int i = 0; i < 3; i++) {
                    g.drawLine(0, 26 + i, 585, 26 + i);
                    g.drawLine(0, 31 + i, 585, 31 + i);
                }
                splash(g); // calls on splash method containing more graphics
            } else if (screen.equals("splash-r1")) { // draws top right corner of splash screen
                for (int i = 0; i < 3; i++) {
                    g.drawLine(0, 26 + i, 4, 26 + i);
                    g.drawLine(5 + i, 26, 5 + i, 330);
                    g.drawLine(0 + i, 31, 0 + i, 330);
                }
            } else if (screen.equals("splash-l2")) { // draws middle left corner of splash screen
                for (int i = 0; i < 3; i++) {
                    g.drawLine(26 + i, 0, 26 + i, 34);
                    g.drawLine(31 + i, 0, 31 + i, 34);
                }
            } else if (screen.equals("splash-r2")) { // draws middle right corner of splash screen
                for (int i = 0; i < 3; i++) {
                    g.drawLine(0 + i, 0, 0 + i, 34);
                    g.drawLine(5 + i, 0, 5 + i, 34);
                }
            } else if (screen.equals("splash-l3")) { // draws bottom left corner of splash screen
                for (int i = 0; i < 3; i++) {
                    g.drawLine(26, 12 + i, 34, 12 + i);
                    g.drawLine(26 + i, 0, 26 + i, 14);
                    g.drawLine(31 + i, 0, 31 + i, 9);
                }
            } else if (screen.equals("splash-b")) { // draws bottom of splash screen
                for (int i = 0; i < 3; i++) {
                    g.drawLine(0, 7 + i, 585, 7 + i);
                    g.drawLine(0, 12 + i, 585, 12 + i);
                }
            } else if (screen.equals("splash-r3")) { // draws bottom right corner of splash screen
                for (int i = 0; i < 3; i++) {
                    g.drawLine(0, 12 + i, 5, 12 + i);
                    g.drawLine(5 + i, 0, 5 + i, 14);
                    g.drawLine(0 + i, 0, 0 + i, 9);
                }
            } else if (screen.equals("menu")) { // draws menu graphics
                menu(g);
            } else if (screen.equals("border-l1")) { // draws border in top left corner
                g.setColor(brightBlue);
                for (int i = 0; i < 3; i++) {
                    g.drawLine(25, 23 + i, 31, 23 + i);
                    g.drawLine(29 + i, 28, 29 + i, 31);
                    g.drawLine(23 + i, 23, 23 + i, 31);
                }
            } else if (screen.equals("border-t")) { // draws border at top
                g.setColor(brightBlue);
                for (int i = 0; i < 3; i++) {
                    g.drawLine(0, 30 - i, 711, 30 - i);
                    g.drawLine(0, 25 - i, 711, 25 - i);
                }
            } else if (screen.equals("border-r1")) { // draws border in top right corner
                g.setColor(brightBlue);
                for (int i = 0; i < 3; i++) {
                    g.drawLine(0, 23 + i, 5, 23 + i);
                    g.drawLine(0 + i, 28, 0 + i, 31);
                    g.drawLine(5 + i, 23, 5 + i, 31);
                }
            } else if (screen.equals("border-l2")) { // draws border in center left
                g.setColor(brightBlue);
                for (int i = 0; i < 3; i++) {
                    g.drawLine(29 + i, 0, 29 + i, 440);
                    g.drawLine(23 + i, 0, 23 + i, 440);
                }
            } else if (screen.equals("border-r2")) { // draws border in center right
                g.setColor(brightBlue);
                for (int i = 0; i < 3; i++) {
                    g.drawLine(0 + i, 0, 0 + i, 440);
                    g.drawLine(5 + i, 0, 5 + i, 440);
                }
            } else if (screen.equals("border-l3")) { // draws border in bottom left corner
                g.setColor(brightBlue);
                for (int i = 0; i < 3; i++) {
                    g.drawLine(25, 5 + i, 31, 5 + i);
                    g.drawLine(29 + i, 0, 29 + i, 2);
                    g.drawLine(23 + i, 0, 23 + i, 7);
                }
            } else if (screen.equals("border-b")) { // draws border at bottom
                g.setColor(brightBlue);
                for (int i = 0; i < 3; i++) {
                    g.drawLine(0, 0 + i, 711, 0 + i);
                    g.drawLine(0, 5 + i, 711, 5 + i);
                }
            } else if (screen.equals("border-r3")) { // draws border at bottom right corner
                g.setColor(brightBlue);
                for (int i = 0; i < 3; i++) {
                    g.drawLine(0, 5 + i, 5, 5 + i);
                    g.drawLine(0 + i, 0, 0 + i, 2);
                    g.drawLine(5 + i, 0, 5 + i, 7);
                }
            }
        }

        /**
         * method to draw graphics of splash screen
         * 
         * @param g instance of Graphics class
         */
        public void splash(Graphics g) {
            g.setColor(beige); // sets cloud colours of the clouds

            g.fillOval(10, 70, 50, 50); // draws clouds on the left side
            g.fillOval(40, 50, 60, 80);
            g.fillOval(75, 60, 70, 70);
            g.fillOval(115, 75, 50, 40);

            g.fillOval(50, 160, 50, 50);
            g.fillOval(80, 140, 60, 80);
            g.fillOval(115, 150, 70, 70);
            g.fillOval(155, 165, 50, 40);

            g.fillOval(10, 240, 50, 50);
            g.fillOval(40, 220, 60, 80);
            g.fillOval(75, 230, 70, 70);
            g.fillOval(115, 245, 50, 40);

            g.fillOval(getWidth() - 60, 70, 50, 50); // draws clouds on the right side
            g.fillOval(getWidth() - 100, 50, 60, 80);
            g.fillOval(getWidth() - 145, 60, 70, 70);
            g.fillOval(getWidth() - 165, 65, 50, 40);

            g.fillOval(getWidth() - 100, 160, 50, 50);
            g.fillOval(getWidth() - 140, 140, 60, 80);
            g.fillOval(getWidth() - 185, 150, 70, 70);
            g.fillOval(getWidth() - 205, 165, 50, 40);

            g.fillOval(getWidth() - 60, 240, 50, 50);
            g.fillOval(getWidth() - 100, 220, 60, 80);
            g.fillOval(getWidth() - 145, 230, 70, 70);
            g.fillOval(getWidth() - 165, 245, 50, 40);

            try {
                BufferedImage splashScreenLogo = ImageIO.read(new File("Logo.png")); // imports the airplane logo
                g.drawImage(splashScreenLogo, 195, 110, null);
            } catch (IOException e) {
            }

            g.setFont(title); // sets font to title font
            g.setColor(Color.black); // sets colour to black
            g.drawString("Fly-Away Airlines", 127, 105); // draws title
        }

        /**
         * Method to draw menu graphics
         * 
         * @param g instance of Graphics class
         */
        public void menu(Graphics g) {
            for (int i = 0; i < 3; i++) { // draws border
                g.setColor(new Color(171, 196, 255));
                g.drawRect(40 + i, 40 + i, menuFrame.getWidth() - 80 - (i * 2), 460 - 80 - (i * 2));
                g.drawRect(45 + i, 45 + i, menuFrame.getWidth() - 90 - (i * 2), 460 - 90 - (i * 2));
            }
            try {
                BufferedImage menuLogo = ImageIO.read(new File("menuLogo.png")); // imports the airplane logo
                g.drawImage(menuLogo, 17, 5, null);
            } catch (IOException e) {
            }

            g.setColor(backgroundBlue);
            g.fillRoundRect(92, 152, 470, 140, 80, 80);
            g.setColor(Color.black);
            for (int i = 1; i < 3; i++) {
                g.drawRoundRect(97 + i, 157 + i, 460 - (i * 2), 130 - (i * 2), 80 - (i * 2), 80 - (i * 2));
            }
            g.setFont(menuTitle);
            g.setColor(Color.black);
            g.drawString("Lucyfer's Airlines", 115, 225); // displays title
            g.setFont(new Font("calibri", 0, 18));
            g.setColor(Color.black);
            g.drawString("-Database Software-", 240, 260);
            g.setFont(new Font("calibri", 0, 14));
            g.drawString("By: Nadia Bousselmi, Jing Wang, & Redlazer8", 190, 330);

            try {
                BufferedImage flightAttendent = ImageIO.read(new File("flightAttendent.png")); // imports the airplane
                                                                                               // logo
                g.drawImage(flightAttendent, 580, 100, null);
            } catch (IOException e) {
            }
        }
    }

    /**
     * actionPerformed method to allow buttons to do things
     */
    public void actionPerformed(ActionEvent evt) {
        if (evt.getActionCommand().equals("book")) {
            menuFrame.dispose(); // disposes menu frame
            flight1Temp = -1; // resets temp flight variables
            flight2Temp = -1;
            selectDate(); // calls selectDate
            mode = 1; // sets mode to 1
            mainFrame.setTitle("Booking");
        } else if (evt.getActionCommand().equals("view")) {
            menuFrame.dispose(); // disposes menu frame
            flight1Temp = -1; // resets temp flight variables
            flight2Temp = -1;
            selectDate(); // calls select date
            mode = 2; // sets mode to 2
            mainFrame.setTitle("View Data");
        } else if (evt.getActionCommand().equals("exp")) {
            menuFrame.dispose(); // disposes menu frame
            mode = 3; // sets mode to 3
            flight1Temp = -1; // resets temp flight variables
            flight2Temp = -1;
            selectDate(); // calls on selectDate
            mainFrame.setTitle("Export");
        } else if (evt.getActionCommand().equals("help")) {
            try {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().open(new File("User Manual.pdf"));
                    } catch (IOException ex) {
                    }
                }
            } catch (Exception e) {
                System.out.println("link redirect error");
            }
        } else if (evt.getActionCommand().equals("exit")) {
            int result = JOptionPane.showConfirmDialog(null, "Do you want to quit the program?", "Exit?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0); // ends program
            } else if (result == JOptionPane.NO_OPTION) {}
        }

        if (evt.getActionCommand().equals("menu")) {
            mainFrame.dispose();
            if (cancel == true) { // flight was cancelled removes flight
                flights.remove(flightIndex);
                saveData();
                cancel = false; // resets cancel variable
            }
            menu(); // calls menu
        }

        for (int i = 0; i < 31; i++) {
            if (evt.getActionCommand().equals("bookDate" + (i + 1))) { // calls selectFlight()
                tempDate = i + 1; // sets temp date variable as the calandar button selected
                flightIndex = -1; // resets flight index
                flightChosen = 0; // resets flight chosen
                selectFlight();
            }
        }

        if (evt.getActionCommand().equals("bookFlights1")) { // if first flight button selected
            flightIndex = flight1Temp; // sets flight index as flight1Temp
            flightChosen = 1; // sets flight chosen as 1
            if (mode == 1) {
                tempSeat.clear(); // if in booking mode proceeds to select seat
                selectSeat();
            } else if (mode == 2) { // if in view mode proceeds to view flight
                viewFlight();
            } else if (mode == 3) { // if in export mode proceeds to export
                export();
            }
        } else if (evt.getActionCommand().equals("bookFlights2")) { // if second flight button is selected
            flightChosen = 2;
            flightIndex = flight2Temp;
            if (mode == 1) {
                tempSeat.clear();
                selectSeat();
            } else if (mode == 2) {
                viewFlight();
            } else if (mode == 3) {
                export();
            }
        }

        for (int i = 0; i < 10; i++) {
            if (evt.getActionCommand().equals("bookSeats" + (i + 1))) {
                if (tempSeatSelected[i]) // if seat was already selected
                {
                    for (int j = 0; j < tempSeat.size(); j++) // scans the tempSeat array
                    {
                        if (tempSeat.get(j) == i) // when seat number is found
                        {
                            tempSeat.remove(j); // remove seat from tempSeat
                            tempSeatSelected[i] = false;
                            seats[i].setBackground(backgroundBlue);

                        }
                    }
                } else // if seat was selected
                {
                    tempSeat.add(Integer.valueOf(i));
                    tempSeatSelected[i] = true;
                    seats[i].setBackground(beige);
                }
            }
        }

        if (evt.getActionCommand().equals("bookSeatSubmit")) { // submit seat button
            if (tempSeat.size() == 0) {
                JOptionPane.showMessageDialog(null, "Please select one seat minimum.", "Error!",
                        JOptionPane.ERROR_MESSAGE); // displays error message if user does not select a seat
            } else {
                enterInfo(); // calls enter info
            }
        }

        if (evt.getActionCommand().equals("submitInfo")) { // enterInfo submit button
            if (DataCheck.firstNameCheck(firstField.getText())
                    && DataCheck.lastNameCheck(lastField.getText())
                    && DataCheck.dobCheck(dobField.getText()) && DataCheck.emailCheck(emailField.getText())
                    && DataCheck.phoneCheck(phoneField.getText())) {

                if (currentCounter < tempSeat.size()) {
                    flights.get(flightIndex).setFirstName(firstField.getText(), tempSeat.get(currentCounter));
                    flights.get(flightIndex).setLastName(lastField.getText(), tempSeat.get(currentCounter));
                    flights.get(flightIndex).setDOB(dobField.getText(), tempSeat.get(currentCounter));
                    flights.get(flightIndex).setEmail(emailField.getText(), tempSeat.get(currentCounter));
                    flights.get(flightIndex).setPhone(phoneField.getText(), tempSeat.get(currentCounter));
                    flights.get(flightIndex).setAvailable(false, tempSeat.get(currentCounter));
                    currentCounter++;
                    if (currentCounter < tempSeat.size()) {
                        enterInfo();
                    } else if (currentCounter == tempSeat.size()) {
                        currentCounter = 0;
                        saveData();
                        summary();
                    }

                } else {
                    currentCounter = 0;
                    saveData();
                    summary();
                }
            } else {
                if (DataCheck.firstNameCheck(firstField.getText()) == false
                        || firstField.getText() == null) {
                    JOptionPane.showMessageDialog(null, "Inavlid entry. Please enter a valid firstname.", "Error!",
                            JOptionPane.ERROR_MESSAGE);
                    firstField.setText("");
                }
                if (DataCheck.lastNameCheck(lastField.getText()) == false
                        || lastField.getText() == null) {
                    JOptionPane.showMessageDialog(null, "Inavlid entry. Please enter a valid lastname.", "Error!",
                            JOptionPane.ERROR_MESSAGE);
                    lastField.setText("");
                }
                if (DataCheck.dobCheck(dobField.getText()) == false || dobField.getText() == null) {
                    JOptionPane.showMessageDialog(null,
                            "Inavlid entry. Please enter a valid date of birth in the format dd/mm/yyyy.", "Error!",
                            JOptionPane.ERROR_MESSAGE);
                    dobField.setText("");
                }
                if (DataCheck.emailCheck(emailField.getText()) == false || emailField.getText() == null) {
                    JOptionPane.showMessageDialog(null, "Inavlid entry. Please enter a valid email.", "Error!",
                            JOptionPane.ERROR_MESSAGE);
                    emailField.setText("");
                }
                if (DataCheck.phoneCheck(phoneField.getText()) == false || phoneField.getText() == null) {
                    JOptionPane.showMessageDialog(null,
                            "Inavlid entry. Please enter a valid phone number in the format XXX-XXX-XXXX.", "Error!",
                            JOptionPane.ERROR_MESSAGE);
                    phoneField.setText("");
                }
            }
        }
        for (int i = 0; i < 10; i++) {
            if (evt.getActionCommand().equals("cancelSeat" + (i + 1))) {
                flights.get(flightIndex).cancelSeat(i);
                viewFlight();
                saveData();
            }
        }

        if (evt.getActionCommand().equals("cancelFlight")) {
            int result = JOptionPane.showConfirmDialog(null, "Are you sure?", "Cancel Flight",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
                export();
                cancel = true;
            } else if (result == JOptionPane.NO_OPTION) {
            }
        }

        if (evt.getActionCommand().equals("exportData")) {
            exportData(flightIndex);
        }

        if (evt.getActionCommand().equals("exportSort")) {
            if (displaySorted == true) {
                displaySorted = false;
            } else {
                displaySorted = true;
            }
            export();
        }
    }

    private void saveData() {
        PrintWriter output;
        try {
            output = new PrintWriter(new FileWriter("FlightSchedule.fly"));

            for (int i = 0; i < flights.size(); i++) {
                output.println("plane: [] " + flights.get(i).getDepart() + " [] " + flights.get(i).getDestination()
                        + " [] " + flights.get(i).getDate() + " [] " + flights.get(i).getPlane()); // print out flight
                                                                                                   // data
                for (int j = 0; j < 10; j++) {
                    if (!flights.get(i).getAvailable(j)) // if seat is taken by a client
                    {
                        output.println(
                                "seat: [] " + flights.get(i).getAvailable(j) + " [] " + flights.get(i).getFirstName(j)
                                        + " [] " + flights.get(i).getLastName(j) + " [] " + flights.get(i).getEmail(j)
                                        + " [] " + flights.get(i).getPhone(j) + " [] " + flights.get(i).getDOB(j)); // print
                                                                                                                    // out
                                                                                                                    // seat
                                                                                                                    // data
                    } else // if seat is not taken by client
                    {
                        output.println("seat: [] " + flights.get(i).getAvailable(j));
                    }
                }
            }

            output.println("END OF FILE");
            output.close();
        } catch (Exception e) {
            System.out.println("save error");
        }
    }

    public static boolean orderLast(String name1, String name2, String name3, String name4) {
        if (name1.charAt(0) > name2.charAt(0)) {
            return true;
        } else if (name1.charAt(0) < name2.charAt(0)) {
            return false;
        } else if (name1.charAt(0) == name2.charAt(0)) {
            if (name1.length() > 1 && name2.length() > 1) {
                if (orderLast(name1.substring(1), name2.substring(1), name3, name4)) {
                    return true;
                } else {
                    return false;
                }
            }

            else if (name1.length() < name2.length()) {
                return false;
            } else if (name1.length() > name2.length()) {
                return true;
            } else {
                if (orderFirst(name3, name4)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public static boolean orderFirst(String name1, String name2) {
        if (name1.charAt(0) > name2.charAt(0)) {
            return true;
        } else if (name1.charAt(0) < name2.charAt(0)) {
            return false;
        } else {
            if (name1.length() > 1 && name2.length() > 1) {
                if (orderFirst(name1.substring(1), name2.substring(1))) {
                    return true;
                } else {
                    return false;
                }
            }

            if (name1.length() < name2.length()) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Loads all necessary data from the fly file
     * Created: William Cheng, June 13 at 1350
     */
    private void loadData() {
        BufferedReader input;
        try {
            input = new BufferedReader(new FileReader("FlightSchedule.fly"));
            while (true) {
                String lineInfo = input.readLine();

                if (lineInfo.equals("END OF FILE")) {
                    break;
                }

                String[] parts = lineInfo.split(" \\[] ");

                if (parts[0].trim().equals("plane:")) {
                    flights.add(new Flight(parts[1].trim(), parts[2].trim(), Integer.parseInt(parts[3].trim()),
                            parts[4].trim()));
                    for (int i = 0; i < 10; i++) {
                        String lineInfo2 = input.readLine();
                        String[] info = lineInfo2.split(" \\[] ");
                        if (info[0].trim().equals("seat:")) {
                            if (!Boolean.parseBoolean(info[1].trim())) {
                                flights.get(flights.size() - 1).setAvailable(Boolean.parseBoolean(info[1].trim()), i);
                                flights.get(flights.size() - 1).setFirstName(info[2].trim(), i);
                                flights.get(flights.size() - 1).setLastName(info[3].trim(), i);
                                flights.get(flights.size() - 1).setEmail(info[4].trim(), i);
                                flights.get(flights.size() - 1).setPhone(info[5].trim(), i);
                                flights.get(flights.size() - 1).setDOB(info[6].trim(), i);
                            }
                        } else {
                            System.out.println("not enough seats");
                            break;
                        }
                    }
                } else {
                    System.out.println("broken file");
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("error");
        }
    }

    private void exportData(int flight) // flight starts from 0 to match ArrayList index
    {
        PrintWriter output;

        try {
            output = new PrintWriter(new FileWriter(
                    "August " + flights.get(flight).getDate() + " 2022 " + flights.get(flight).getPlane()
                            + " Data.txt")); // we
                                             // will
                                             // have

            output.println("Passenger Manifest for Flight");
            output.println(" ");
            output.println("Flight details: ");
            output.println("Date of departure: August " + flights.get(flight).getDate() + ", 2022");
            output.println("Departing from: " + flights.get(flight).getDepart());
            output.println("Travelling to: " + flights.get(flight).getDestination());
            output.println("Plane: " + flights.get(flight).getPlane());
            output.println(" ");
            output.println("-------------------------------------------------------------------");
            output.println(" ");

            if (displaySorted) {
                output.println("Sorted Alphabetically");
                output.println(" ");

                for (int i = 0; i < 10; i++) {
                    String[] data = printList[i];

                    output.println("Seat#: " + data[0]);
                    output.println("Name: " + data[1] + " " + data[2]);
                    output.println("Date of birth (dd/mm/yyyy): " + data[3]);
                    output.println("Email: " + data[4]);
                    output.println("Phone number: " + data[5]);
                    output.println("Age: " + data[6] + " years");
                    output.println(" ");
                }
            } else if (displaySorted == false) {
                output.println("Ordered by Seat #");
                output.println(" ");

                for (int i = 0; i < 10; i++) {
                    if (i == 9) {
                        output.println("Seat#: 10");
                    } else {
                        output.println("Seat#: 0" + (i + 1));
                    }
                    output.println(
                            "Name: " + flights.get(flight).getFirstName(i) + " " + flights.get(flight).getLastName(i));
                    output.println("Email: " + flights.get(flight).getEmail(i));
                    output.println("Phone number: " + flights.get(flight).getPhone(i));
                    output.println("Date of birth (dd/mm/yyyy): " + flights.get(flight).getDOB(i));
                    output.println("Age: " + flights.get(flight).getAge(i) + " years");
                    output.println(" ");
                }
            }

            output.println("-------------------------------------------------------------------");
            output.println(" ");
            output.println("Lucyfer's Airlines");
            output.close();
        } catch (Exception e) {
            System.out.println("export error");
        }
    }

    public static void main(String[] args) {
        new Database();
    }
}