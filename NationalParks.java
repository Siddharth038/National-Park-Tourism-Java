package national;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class NationalParks extends JFrame implements ActionListener {
    // UI elements
    private JLabel welcomeLabel;
    private JButton startButton;
    private JLabel parksLabel;
    private JComboBox<String> parksComboBox;
    private JLabel parkDetailsLabel;
    private JTextArea parkDetailsTextArea;
    private JLabel timingLabel;
    private JTextArea timingTextArea;
    private JLabel feeLabel;
    private JTextArea feeTextArea;
    private JLabel vehicleLabel;
    private JTextArea vehicleTextArea;
    private JButton exitButton;
    private JButton bookVisitButton;

    // Data about the national parks

    private String[][] parksData;

    public NationalParks() {
        fetchParksDataFromDatabase();

        // Set up the frame
        setTitle("National Parks of India");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Add the parks label and combo box
        parksLabel = new JLabel("Select a national park:");
        parksLabel.setBounds(50, 50, 150, 20);
        add(parksLabel);

        parksComboBox = new JComboBox<String>();
        // Add the park names to the combo box
        for (int i = 0; i < parksData.length; i++) {
            parksComboBox.addItem(parksData[i][1]);
        }
        parksComboBox.setBounds(200, 50, 200, 20);
        add(parksComboBox);


        // Add an action listener to the combo box
        parksComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the selected park index
                int parkIndex = parksComboBox.getSelectedIndex();

                // Update the text in the JTextAreas
                parkDetailsTextArea.setText(parksData[parkIndex][2]);
                timingTextArea.setText(parksData[parkIndex][3]);
                feeTextArea.setText(parksData[parkIndex][4]);
                vehicleTextArea.setText(parksData[parkIndex][5]);
            }
        });

        // Add the park details label and text area
        parkDetailsLabel = new JLabel("Park details:");
        parkDetailsLabel.setBounds(50, 100, 150, 20);
        add(parkDetailsLabel);

        parkDetailsTextArea = new JTextArea();
        parkDetailsTextArea.setEditable(false);
        parkDetailsTextArea.setLineWrap(true);
        parkDetailsTextArea.setWrapStyleWord(true);
        parkDetailsTextArea.setText(parksData[0][2]);
        JScrollPane parkDetailsScrollPane = new JScrollPane(parkDetailsTextArea);
        parkDetailsScrollPane.setBounds(200, 100, 200, 80);
        add(parkDetailsScrollPane);

        // Add the timing label and text area
        timingLabel = new JLabel("Timing:");
        timingLabel.setBounds(50, 200, 150, 20);
        add(timingLabel);

        timingTextArea = new JTextArea();
        timingTextArea.setEditable(false);
        timingTextArea.setLineWrap(true);
        timingTextArea.setWrapStyleWord(true);
        timingTextArea.setText(parksData[0][3]);
        JScrollPane timingScrollPane = new JScrollPane(timingTextArea);
        timingScrollPane.setBounds(200, 200, 200, 80);
        add(timingScrollPane);

        // Add the fee label and text area
        feeLabel = new JLabel("Entry fee:");
        feeLabel.setBounds(50, 300, 150, 20);
        add(feeLabel);

        feeTextArea = new JTextArea();
        feeTextArea.setEditable(false);
        feeTextArea.setLineWrap(true);
        feeTextArea.setWrapStyleWord(true);
        feeTextArea.setText(parksData[0][4]);
        JScrollPane feeScrollPane = new JScrollPane(feeTextArea);
        feeScrollPane.setBounds(200, 300, 200, 80);
        add(feeScrollPane);

        // Add the vehicle label and text area
        vehicleLabel = new JLabel("Vehicle options:");
        vehicleLabel.setBounds(450, 100, 150, 20);
        add(vehicleLabel);

        vehicleTextArea = new JTextArea();
        vehicleTextArea = new JTextArea();
        vehicleTextArea.setEditable(false);
        vehicleTextArea.setLineWrap(true);
        vehicleTextArea.setWrapStyleWord(true);
        vehicleTextArea.setText(parksData[0][5]);
        JScrollPane vehicleScrollPane = new JScrollPane(vehicleTextArea);
        vehicleScrollPane.setBounds(600, 100, 200, 80);
        add(vehicleScrollPane);

        // Add the exit button
        // Add the exit button
        exitButton = new JButton("Exit");
        exitButton.setBounds(600, 300, 100, 20);
        add(exitButton);
        exitButton.addActionListener(this);

// Add the book visit button
        bookVisitButton = new JButton("Book Visit");
        bookVisitButton.setBounds(450, 300, 100, 20);
        add(bookVisitButton);
        bookVisitButton.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exitButton) {
            // Exit the program
            System.exit(0);
        } else if (e.getSource() == bookVisitButton) {
            // Open the book visit window
            BookVisitWindow bookVisitWindow = new BookVisitWindow(parksData[parksComboBox.getSelectedIndex()][1]);
        }

    }
    private void fetchParksDataFromDatabase() {

        final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://localhost:3306/NATIONALPARK";


        final String USER = "root";
        final String PASS = "9758";


        // Database connection
        try (   Connection connection = DriverManager.getConnection(DB_URL,USER,PASS);
        ) {
            // SQL query to fetch park data
            String query = "SELECT * FROM Parks";

            try (Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
                try (ResultSet resultSet = statement.executeQuery(query)) {
                    // Count the number of rows in the result set
                    int rowCount = 0;
                    while (resultSet.next()) {
                        rowCount++;
                    }

                    // Initialize the parksData array with the correct size
                    parksData = new String[rowCount][6];

                    // Reset the result set to the beginning
                    resultSet.beforeFirst();

                    // Populate the parksData array
                    int index = 0;
                    while (resultSet.next()) {
                        parksData[index][0] = resultSet.getString("id");
                        parksData[index][1] = resultSet.getString("name");
                        parksData[index][2] = resultSet.getString("details");
                        parksData[index][3] = resultSet.getString("timing");
                        parksData[index][4] = resultSet.getString("fee");
                        parksData[index][5] = resultSet.getString("vehicle_options");

                        index++;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while fetching park data. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Create an instance of the NationalParks class
        NationalParks nationalParks = new NationalParks();
    }

    class BookVisitWindow extends JFrame implements ActionListener {
        private JLabel nameLabel;
        private JTextField nameTextField;
        private JLabel emailLabel;
        private JTextField emailTextField;
        private JLabel phoneLabel;
        private JTextField phoneTextField;
        private JLabel membersLabel;
        private JSpinner membersSpinner;
        private JLabel safariLabel;
        private JRadioButton yesSafariRadioButton;
        private JRadioButton noSafariRadioButton;
        private JComboBox<String> safariComboBox;
        private JButton bookButton;
        private JButton cancelButton;

        private String parkName;

        public BookVisitWindow(String parkName) {
            this.parkName = parkName;
            setTitle("Book Visit");
            setBounds(100, 100, 400, 400);
            setLayout(null);

            // Add the name label and text field
            nameLabel = new JLabel("Name:");
            nameLabel.setBounds(50, 50, 100, 20);
            add(nameLabel);

            nameTextField = new JTextField();
            nameTextField.setBounds(150, 50, 200, 20);
            add(nameTextField);

            // Add the email label and text field
            emailLabel = new JLabel("Email:");
            emailLabel.setBounds(50, 100, 100, 20);
            add(emailLabel);

            emailTextField = new JTextField();
            emailTextField.setBounds(150, 100, 200, 20);
            add(emailTextField);

            // Add the phone label and text field
            phoneLabel = new JLabel("Phone:");
            phoneLabel.setBounds(50, 150, 100, 20);
            add(phoneLabel);

            phoneTextField = new JTextField();
            phoneTextField.setBounds(150, 150, 200, 20);
            add(phoneTextField);

            // Add the members label and spinner
            membersLabel = new JLabel("Members:");
            membersLabel.setBounds(50, 200, 100, 20);
            add(membersLabel);

            SpinnerModel membersSpinnerModel = new SpinnerNumberModel(1, 1, 20, 1);
            membersSpinner = new JSpinner(membersSpinnerModel);
            membersSpinner.setBounds(150, 200, 50, 20);
            add(membersSpinner);

            // Add the safari label, radio buttons, and combo box
            safariLabel = new JLabel("Safari:");
            safariLabel.setBounds(50, 250, 100, 20);
            add(safariLabel);

            ButtonGroup safariButtonGroup = new ButtonGroup();
            yesSafariRadioButton = new JRadioButton("Yes");
            yesSafariRadioButton.setBounds(150, 250, 50, 20);
            safariButtonGroup.add(yesSafariRadioButton);
            add(yesSafariRadioButton);

            noSafariRadioButton = new JRadioButton("No");
            noSafariRadioButton.setBounds(200, 250, 50, 20);
            safariButtonGroup.add(noSafariRadioButton);
            add(noSafariRadioButton);

            safariComboBox = new JComboBox<String>(new String[]{"Select safari", "Jeep safari", "Elephant safari", "Boat safari"});
            safariComboBox.setBounds(150, 280, 150, 20);
            add(safariComboBox);

            // Add the book and cancel buttons
            bookButton = new JButton("Book");
            bookButton.setBounds(150, 320, 100, 20);
            add(bookButton);
            bookButton.addActionListener(this);

            cancelButton = new JButton("Cancel");
            cancelButton.setBounds(260, 320, 100, 20);
            add(cancelButton);
            cancelButton.addActionListener(this);

            setVisible(true);
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == bookButton) {
                // Validate input and book visit
                try {
                    String name = nameTextField.getText();
                    String email = emailTextField.getText();
                    String phone = phoneTextField.getText();
                    int members = (int) membersSpinner.getValue();
                    boolean wantSafari = yesSafariRadioButton.isSelected();
                    String safariType = (String) safariComboBox.getSelectedItem();

                    // Validate input
                    if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (!email.contains("@") || !email.contains(".")) {
                        JOptionPane.showMessageDialog(this, "Invalid email address", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (phone.length() != 10) {
                        JOptionPane.showMessageDialog(this, "Phone number should be 10 digits", "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (wantSafari && safariType.equals("Select safari")) {
                        JOptionPane.showMessageDialog(this, "Please select a safari type", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        // Calculate charges
                        // Add a new variable to store the park entry fee
                        int parkEntryFee = 0;

// Retrieve the selected park's entry fee based on the index
                        switch (parksComboBox.getSelectedIndex()) {
                            case 0:
                                parkEntryFee = 500;
                                break;
                            case 1:
                                parkEntryFee = 250;
                                break;
                            case 2:
                                parkEntryFee = 400;
                                break;
                        }

// Calculate charges
                        int baseCharge = 200;
                        int safariCharge = 0;
                        if (wantSafari) {
                            if (safariType.equals("Jeep safari")) {
                                safariCharge = 3000;
                            } else if (safariType.equals("Elephant safari")) {
                                safariCharge = 4000;
                            } else if (safariType.equals("Boat safari")) {
                                safariCharge = 1500;
                            }
                        }
                        int totalCharge = baseCharge + safariCharge + (parkEntryFee * members);

                        // Display charges
                        JOptionPane.showMessageDialog(this, "Your booking for " + parkName + " has been confirmed.\n" +
                                "Total charges: " + totalCharge, "Booking Confirmation", JOptionPane.INFORMATION_MESSAGE);

                        // Clear input fields
                        nameTextField.setText("");
                        emailTextField.setText("");
                        phoneTextField.setText("");
                        membersSpinner.setValue(1);
                        noSafariRadioButton.setSelected(true);
                        safariComboBox.setSelectedIndex(0);

                        final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
                        final String DB_URL = "jdbc:mysql://localhost:3306/NATIONALPARK";


                        final String USER = "root";
                        final String PASS = "9758";


                        // Database connection
                        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
                            // SQL query to insert data
                            String insertQuery = "INSERT INTO Bookings (name, email, phone, members, safari) VALUES (?, ?, ?, ?, ?)";

                            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                                // Set parameters
                                preparedStatement.setString(1, name);
                                preparedStatement.setString(2, email);
                                preparedStatement.setString(3, phone);
                                preparedStatement.setInt(4, members);
                                preparedStatement.setString(5, safariType);

                                // Execute the insert query
                                preparedStatement.executeUpdate();
                            }

                            // Display charges
                            JOptionPane.showMessageDialog(this, "Your booking for " + parkName + " has been confirmed.\n" +
                                    "Total charges: " + totalCharge, "Booking Confirmation", JOptionPane.INFORMATION_MESSAGE);

                            // Clear input fields
                            nameTextField.setText("");
                            emailTextField.setText("");
                            phoneTextField.setText("");
                            membersSpinner.setValue(1);
                            noSafariRadioButton.setSelected(true);
                            safariComboBox.setSelectedIndex(0);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(this, "Error occurred while booking. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                        }


                    }
                } finally {

                }
            }
            else if (e.getSource() == cancelButton) {
                dispose();
            }
        }
    }
}
