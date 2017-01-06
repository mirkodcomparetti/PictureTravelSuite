package org.mdcomparetti;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import javax.swing.JFrame;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.Box;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JProgressBar;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.border.TitledBorder;
import javax.swing.text.*;
import javax.swing.JLabel;

public class PictureTravelSuiteClass extends JPanel
		implements
			ActionListener,
			PropertyChangeListener {

	private static final long serialVersionUID = 3L;
	private static final long serialSubVersionUID = 5L;
	private static final String softwareAuthor = "Mirko D. Comparetti";
	private static final String softwareAuthorShort = "MDC";
	private static final String copyrightSymbol = "\uc2a9";
	private static final String softwareNameShort = "PictureTravelSuite";
	private static final String softwareName = "Picture Travel Suite";
	private static final String softwareInfo = softwareAuthor + " - "
			+ softwareName + " " + serialVersionUID + "." + serialSubVersionUID;
	private static final String softwareInfoLabel = copyrightSymbol + " "
			+ softwareAuthor + " - - - " + softwareName + " " + serialVersionUID
			+ "." + serialSubVersionUID;
	private static final String softwareInfoTitle = softwareAuthorShort + " - "
			+ softwareName;
	private static final String[] configurationBoolean = {"true", "false"};
	private static final int numProcessors = Runtime.getRuntime()
			.availableProcessors();
	private JFrame mainFrame;
	/**
	 * @wbp.nonvisual location=12,-6
	 */
	private final JFileChooser fileChooser = new JFileChooser();

	private JPanel controlsPanel;
	private JTextField photographerText;
	private JButton startBtn;
	private JButton cancelBtn;
	private JButton saveBtn;

	private JPanel foldersPanel;
	private File folderInput;
	private JButton selectInputFolderBtn;
	private JTextField folderInputText;
	private File folderOutput;
	private JButton selectOutputFolderBtn;
	private JTextField folderOutputText;

	Map<String, File> cmdExecutables;

	private JPanel commandsPanel;
	private JPanel watermarkPanel;
	private JCheckBox watermarkChckbx;
	private JComboBox<Float> watermarkSize;
	private JComboBox<String> watermarkPosition;
	private JComboBox<String> watermarkFont;
	private List<String> watermarkFontArray;

	private JPanel resizePanel;
	private JCheckBox resizeChckbx;
	private DefaultComboBoxModel<String> resizeValueModel;
	private JComboBox<String> resizeValue;
	private JComboBox<String> resizeEdge;
	private JCheckBox resizeFullHDChckbx;
	private JCheckBox resizeUltraHDChckbx;

	private JPanel framePanel;
	private JCheckBox frameChckbx;
	private JComboBox<String> frameColor;
	private JComboBox<String> frameType;
	private JComboBox<Float> frameSize;
	private JComboBox<Float> frameThickness;

	private JPanel colorProfilePanel;
	private JCheckBox colorProfileChckbx;
	private JComboBox<String> colorProfileFile;
	private JComboBox<String> colorProfileIntent;
	private List<String> colorProfileFileArray;

	private JPanel miscPanel;
	private JCheckBox copyrightChckbx;
	private JCheckBox commentChckbx;
	private JCheckBox cleanExifChckbx;

	private JPanel progressPanel;
	private JButton exitBtn;
	private JProgressBar progressBarProcess;
	
	private JPanel bottomPanel;
	private JLabel infoLbl;
	private JLabel errorMsg;

	private Dimension singleObjectDimension = new Dimension(125, 40);
	private Dimension singleSpacerDimension = new Dimension(10, 10);

	private File configFile;
	private FileInputStream configReader;
	private FileOutputStream configWriter;
	private Properties configPropsDefault;
	private Properties configProps;
	private boolean isRunning;

	private Task progressTask;
	
	private MyJTextArea logText;

	private DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	private String tmpFolderPath;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		JComponent.setDefaultLocale(new Locale("it"));
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PictureTravelSuiteClass window = new PictureTravelSuiteClass();
					window.mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PictureTravelSuiteClass() {
		initialize();
		tmpFolderPath = System.getProperty("java.io.tmpdir");
		tmpFolderPath += softwareNameShort + "Tmp";
		GetConfigProps();
		WatermarkDataEnable(configProps.getProperty("watermark")
				.compareTo(configurationBoolean[0]) == 0);
		ResizeDataEnable(configProps.getProperty("resize")
				.compareTo(configurationBoolean[0]) == 0);
		FrameDataEnable(configProps.getProperty("frame")
				.compareTo(configurationBoolean[0]) == 0);
		ColorProfileDataEnable(configProps.getProperty("cprofile")
				.compareTo(configurationBoolean[0]) == 0);

		if (!cmdExecutables.containsKey("exiftool")) {
			addToLog("ERROR: exiftool not found", Color.RED);
			OnlyExit();
		}
		if (!cmdExecutables.containsKey("convert")) {
			addToLog("ERROR: ImageMagick not found", Color.RED);
			OnlyExit();
		}
	}

	private void OnlyExit() {
		exitBtn.setEnabled(true);
		startBtn.setEnabled(false);
		cancelBtn.setEnabled(false);
		selectInputFolderBtn.setEnabled(false);
		selectOutputFolderBtn.setEnabled(false);
		saveBtn.setEnabled(false);
		watermarkChckbx.setEnabled(false);
		resizeChckbx.setEnabled(false);
		frameChckbx.setEnabled(false);
		colorProfileChckbx.setEnabled(false);
		copyrightChckbx.setEnabled(false);
		commentChckbx.setEnabled(false);
		cleanExifChckbx.setEnabled(false);

		WatermarkDataEnable(false);
		ResizeDataEnable(false);
		FrameDataEnable(false);
		ColorProfileDataEnable(false);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		cmdExecutables = new HashMap<String, File>();
		configFile = new File("PictureTravelSuite.cfg");
		configPropsDefault = new Properties();
		ResetConfigProps(true);
		configProps = new Properties(configPropsDefault);
		LoadConfiguration();

		folderInput = null;
		folderOutput = null;
		isRunning = false;

		mainFrame = new JFrame();
		mainFrame.setIconImage(Toolkit.getDefaultToolkit()
				.getImage(PictureTravelSuiteClass.class.getResource(
						"/org/mdcomparetti/resources/icona_tricolore.png")));
		mainFrame.setTitle(softwareInfoTitle);
		mainFrame.setLocation(0, 0);
		mainFrame.setBounds(0, 0,
				(int) (5 * singleObjectDimension.getWidth()
						+ 6 * singleSpacerDimension.getWidth()),
				(int) (8 * singleObjectDimension.getHeight()
						+ 10 * singleSpacerDimension.getHeight()));
		mainFrame.addWindowListener(new WindowEventHandler());
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(null);
		mainFrame.setResizable(false);
		// mainFrame.setLocationRelativeTo(null);
		mainFrame.setLocationByPlatform(true);

		Action details = fileChooser.getActionMap().get("viewTypeDetails");
		details.actionPerformed(null);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileSelectionMode(1);
		fileChooser.setCurrentDirectory(new java.io.File("."));
		fileChooser.setDialogTitle("Select files and folders");

		foldersPanel = new JPanel();
		foldersPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		foldersPanel.setBounds((int) singleSpacerDimension.getWidth(),
				(int) singleSpacerDimension.getHeight(),
				(int) (5 * (singleObjectDimension.getWidth()
						+ singleSpacerDimension.getWidth())
						- singleSpacerDimension.getWidth()),
				(int) (2 * singleObjectDimension.getHeight()
						+ singleSpacerDimension.getHeight()));
		mainFrame.getContentPane().add(foldersPanel);
		foldersPanel.setLayout(null);

		folderInputText = new JTextField();
		folderInputText.setLocation(0, 0);
		folderInputText.setToolTipText("Show the selected source folder");
		folderInputText.setColumns(26);
		folderInputText.setEditable(false);
		folderInputText.setText("Source folder not selected");

		selectInputFolderBtn = new JButton("Select");
		selectInputFolderBtn.setActionCommand("folder");
		selectInputFolderBtn.addActionListener(this);
		selectInputFolderBtn
				.setLocation((int) (foldersPanel.getSize().getWidth()
						- singleObjectDimension.getWidth()), 0);

		folderOutputText = new JTextField();
		folderOutputText.setLocation(0, (int) (singleObjectDimension.getHeight()
				+ singleSpacerDimension.getHeight()));
		folderOutputText.setToolTipText("Show the selected target folder");
		folderOutputText.setText("Target folder not selected, using source folder");
		folderOutputText.setEditable(false);
		folderOutputText.setColumns(26);

		selectOutputFolderBtn = new JButton("Scegli");
		selectOutputFolderBtn.setActionCommand("folder");
		selectOutputFolderBtn.addActionListener(this);
		selectOutputFolderBtn.setLocation(
				(int) (foldersPanel.getSize().getWidth()
						- singleObjectDimension.getWidth()),
				(int) (singleObjectDimension.getHeight()
						+ singleSpacerDimension.getHeight()));
		selectOutputFolderBtn.setEnabled(false);

		folderInputText.setSize(
				(int) (4 * singleObjectDimension.getWidth()
						+ 3 * singleSpacerDimension.getWidth()),
				(int) singleObjectDimension.getHeight());
		folderInputText.setMaximumSize(new Dimension(
				(int) (4 * singleObjectDimension.getWidth()
						+ 3 * singleSpacerDimension.getWidth()),
				(int) singleObjectDimension.getHeight()));
		selectInputFolderBtn.setSize(singleObjectDimension);
		selectInputFolderBtn.setMaximumSize(singleObjectDimension);
		folderOutputText.setSize(
				(int) (4 * singleObjectDimension.getWidth()
						+ 3 * singleSpacerDimension.getWidth()),
				(int) singleObjectDimension.getHeight());
		folderOutputText.setMaximumSize(new Dimension(
				(int) (4 * singleObjectDimension.getWidth()
						+ 3 * singleSpacerDimension.getWidth()),
				(int) singleObjectDimension.getHeight()));
		selectOutputFolderBtn.setSize(singleObjectDimension);
		selectOutputFolderBtn.setMaximumSize(singleObjectDimension);

		foldersPanel.add(folderInputText);
		foldersPanel.add(selectInputFolderBtn);
		foldersPanel.add(folderOutputText);
		foldersPanel.add(selectOutputFolderBtn);

		controlsPanel = new JPanel();
		controlsPanel.setBounds((int) (foldersPanel.getLocation().x),
				(int) (foldersPanel.getLocation().y
						+ foldersPanel.getSize().getHeight()
						+ singleSpacerDimension.getHeight()),
				(int) (5 * (singleObjectDimension.getWidth()
						+ singleSpacerDimension.getWidth())
						- singleSpacerDimension.getWidth()),
				(int) (singleObjectDimension.getHeight()));
		mainFrame.getContentPane().add(controlsPanel);
		controlsPanel.setLayout(null);

		photographerText = new JTextField();
		photographerText.setColumns(20);

		startBtn = new JButton("Start");
		startBtn.setActionCommand("start");
		startBtn.addActionListener(this);
		startBtn.setEnabled(false);

		cancelBtn = new JButton("Stop");
		cancelBtn.setActionCommand("stop");
		cancelBtn.addActionListener(this);
		cancelBtn.setEnabled(false);

		saveBtn = new JButton("Save");
		saveBtn.setActionCommand("save");
		saveBtn.addActionListener(this);

		photographerText.setSize(
				(int) (2 * singleObjectDimension.getWidth()
						+ singleSpacerDimension.getWidth()),
				(int) singleObjectDimension.getHeight());
		photographerText.setMaximumSize(new Dimension(
				(int) (2 * singleObjectDimension.getWidth()
						+ singleSpacerDimension.getWidth()),
				(int) singleObjectDimension.getHeight()));
		photographerText.setLocation(0, 0);
		startBtn.setSize(singleObjectDimension);
		startBtn.setMaximumSize(singleObjectDimension);
		startBtn.setLocation((int) (2 * (singleObjectDimension.getWidth()
				+ singleSpacerDimension.getWidth())), 0);
		cancelBtn.setSize(singleObjectDimension);
		cancelBtn.setMaximumSize(singleObjectDimension);
		cancelBtn.setLocation((int) (3 * (singleObjectDimension.getWidth()
				+ singleSpacerDimension.getWidth())), 0);
		saveBtn.setSize(singleObjectDimension);
		saveBtn.setMaximumSize(singleObjectDimension);
		saveBtn.setLocation((int) (4 * (singleObjectDimension.getWidth()
				+ singleSpacerDimension.getWidth())), 0);
		controlsPanel.add(photographerText);
		controlsPanel.add(startBtn);
		controlsPanel.add(cancelBtn);
		controlsPanel.add(saveBtn);

		commandsPanel = new JPanel();
		commandsPanel.setBounds((int) (controlsPanel.getLocation().x),
				(int) (controlsPanel.getLocation().y
						+ controlsPanel.getSize().getHeight()
						+ singleSpacerDimension.getHeight()),
				(int) (5 * singleObjectDimension.getWidth()
						+ 4 * singleSpacerDimension.getWidth()),
				(int) (5 * singleObjectDimension.getHeight()
						+ 4 * singleSpacerDimension.getHeight()));
		mainFrame.getContentPane().add(commandsPanel);
		commandsPanel.setLayout(null);

		miscPanel = new JPanel();
		miscPanel.setLocation(0, 0);
		miscPanel.setSize(new Dimension((int) singleObjectDimension.getWidth(),
				(int) (3 * (singleObjectDimension.getHeight()
						+ singleSpacerDimension.getHeight())
						- singleSpacerDimension.getHeight())));
		miscPanel.setMaximumSize(
				new Dimension((int) singleObjectDimension.getWidth(),
						(int) (3 * (singleObjectDimension.getHeight()
								+ singleSpacerDimension.getHeight())
						- singleSpacerDimension.getHeight())));
		miscPanel.setLayout(null);
		// miscPanel.setBorder(new TitledBorder(null, "Vari",
		// TitledBorder.LEADING, TitledBorder.TOP, null, null));

		commentChckbx = new JCheckBox("Info");
		commentChckbx.setToolTipText("Adds info about the file in the EXIF comments");

		copyrightChckbx = new JCheckBox("Copyright");
		copyrightChckbx.setToolTipText("Adds copyright info on the file");

		cleanExifChckbx = new JCheckBox("No EXIF");
		cleanExifChckbx.setToolTipText("Cleans the EXIF information: photoshop, software");

		copyrightChckbx.setLocation(0, 0);
		copyrightChckbx.setSize(singleObjectDimension);
		copyrightChckbx.setMaximumSize(singleObjectDimension);
		commentChckbx.setLocation(0, (int) (singleObjectDimension.getHeight()
				+ singleSpacerDimension.getHeight()));
		commentChckbx.setSize(singleObjectDimension);
		commentChckbx.setMaximumSize(singleObjectDimension);
		cleanExifChckbx.setLocation(0,
				(int) (2 * (singleObjectDimension.getHeight()
						+ singleSpacerDimension.getHeight())));
		cleanExifChckbx.setSize(singleObjectDimension);
		cleanExifChckbx.setMaximumSize(singleObjectDimension);
		miscPanel.add(copyrightChckbx);
		miscPanel.add(commentChckbx);
		miscPanel.add(cleanExifChckbx);

		colorProfilePanel = new JPanel();
		colorProfilePanel.setLocation((int) (singleObjectDimension.getWidth()
				+ singleSpacerDimension.getWidth()), 0);
		colorProfilePanel
				.setSize(new Dimension((int) singleObjectDimension.getWidth(),
						(int) (3 * singleObjectDimension.getHeight()
								+ 2 * singleSpacerDimension.getHeight())));
		colorProfilePanel.setMaximumSize(
				new Dimension((int) singleObjectDimension.getWidth(),
						(int) (3 * singleObjectDimension.getHeight()
								+ 2 * singleSpacerDimension.getHeight())));
		colorProfilePanel.setLayout(null);

		colorProfileChckbx = new JCheckBox("Profile");
		colorProfileChckbx.setToolTipText("Changes the color profile for the pictures");
		colorProfileChckbx.setActionCommand("colorProfile");
		colorProfileChckbx.addActionListener(this);

		colorProfileFile = new JComboBox<String>();
		colorProfileFileArray = new ArrayList<String>();
		colorProfileFileArray.add(
				"/org/mdcomparetti/resources/colorprofiles/srgb.icc");
		colorProfileFileArray.add(
				"/org/mdcomparetti/resources/colorprofiles/AdobeRGB1998.icc");
		colorProfileFileArray.add(
				"/org/mdcomparetti/resources/colorprofiles/MelissaRGB.icc");
		colorProfileFileArray.add(
				"/org/mdcomparetti/resources/colorprofiles/ProPhoto.icc");
		colorProfileFileArray.add(
				"/org/mdcomparetti/resources/colorprofiles/WideGamut.icc");
		Collections.sort(colorProfileFileArray);
		for (int iter = 0; iter < colorProfileFileArray.size(); iter++) {
			String tmp = colorProfileFileArray.get(iter);
			tmp = tmp.substring(0, (tmp.lastIndexOf(".") != -1)
					? tmp.lastIndexOf(".")
					: tmp.length());
			tmp = tmp.substring(
					(tmp.lastIndexOf("/") != -1) ? tmp.lastIndexOf("/") + 1 : 0,
					tmp.length());
			tmp = tmp.substring((tmp.lastIndexOf("\\") != -1)
					? tmp.lastIndexOf("\\") + 1
					: 0, tmp.length());
			colorProfileFile.addItem(tmp);
		}
		colorProfileFile.setEnabled(false);
		colorProfileIntent = new JComboBox<String>();
		colorProfileIntent.addItem("Absolute");
		colorProfileIntent.addItem("Perceptual");
		colorProfileIntent.addItem("Relative");
		colorProfileIntent.addItem("Saturation");
		colorProfileIntent.setEnabled(false);

		colorProfileChckbx.setSize(singleObjectDimension);
		colorProfileFile.setSize(singleObjectDimension);
		colorProfileIntent.setSize(singleObjectDimension);
		colorProfileChckbx.setMaximumSize(singleObjectDimension);
		colorProfileFile.setMaximumSize(singleObjectDimension);
		colorProfileIntent.setMaximumSize(singleObjectDimension);
		colorProfileChckbx.setLocation(0, 0);
		colorProfileFile.setLocation(0, (int) (singleObjectDimension.getHeight()
				+ singleSpacerDimension.getHeight()));
		colorProfileIntent.setLocation(0, (int) (2 * (singleObjectDimension.getHeight()
				+ singleSpacerDimension.getHeight())));
		colorProfilePanel.add(colorProfileChckbx);
		colorProfilePanel.add(colorProfileFile);
		colorProfilePanel.add(colorProfileIntent);

		framePanel = new JPanel();
		framePanel.setLocation((int) (2 * (singleObjectDimension.getWidth()
				+ singleSpacerDimension.getWidth())), 0);
		framePanel.setSize(new Dimension((int) singleObjectDimension.getWidth(),
				(int) (5 * singleObjectDimension.getHeight()
						+ 4 * singleSpacerDimension.getHeight())));
		framePanel.setMaximumSize(
				new Dimension((int) singleObjectDimension.getWidth(),
						(int) (5 * (singleObjectDimension.getHeight()
								+ singleSpacerDimension.getHeight()))));
		framePanel.setLayout(null);

		frameChckbx = new JCheckBox("Frame");
		frameChckbx.setToolTipText("Adds a frame to the pictures");
		frameChckbx.setActionCommand("frame");
		frameChckbx.addActionListener(this);

		frameColor = new JComboBox<String>();
		frameColor.addItem("white");
		frameColor.addItem("black");
		frameColor.addItem("red");
		frameColor.addItem("green");
		frameColor.addItem("blue");

		frameType = new JComboBox<String>();
		frameType.addItem("Plain");
		frameType.addItem("Double");
		frameType.addItem("Inner");
		frameType.addItem("Rounded");
		frameType.addItem("Outer");
		frameType.addItem("Blur");
		frameType.addItem("Italy");

		frameSize = new JComboBox<Float>();
		frameSize.addItem((float) 0.25);
		frameSize.addItem((float) 0.50);
		frameSize.addItem((float) 1.00);
		frameSize.addItem((float) 1.25);
		frameSize.addItem((float) 1.50);
		frameSize.addItem((float) 1.75);
		frameSize.addItem((float) 2.00);
		frameSize.addItem((float) 2.25);
		frameSize.addItem((float) 2.50);
		frameSize.addItem((float) 2.75);
		frameSize.addItem((float) 3.00);
		frameSize.addItem((float) 3.25);
		frameSize.addItem((float) 3.50);
		frameSize.addItem((float) 3.75);
		frameSize.addItem((float) 4.00);
		frameSize.addItem((float) 4.25);
		frameSize.addItem((float) 4.50);
		frameSize.addItem((float) 4.75);
		frameSize.addItem((float) 5.00);

		frameThickness = new JComboBox<Float>();
		frameThickness.addItem((float) 0.125);
		frameThickness.addItem((float) 0.25);
		frameThickness.addItem((float) 0.375);
		frameThickness.addItem((float) 0.5);
		frameThickness.addItem((float) 0.625);
		frameThickness.addItem((float) 0.75);
		frameThickness.addItem((float) 0.825);
		frameThickness.addItem((float) 1.0);
		frameThickness.addItem((float) 1.125);
		frameThickness.addItem((float) 1.25);
		frameThickness.addItem((float) 1.375);
		frameThickness.addItem((float) 1.5);
		frameThickness.addItem((float) 1.625);
		frameThickness.addItem((float) 1.75);
		frameThickness.addItem((float) 1.825);
		frameThickness.addItem((float) 2.0);

		frameChckbx.setSize(singleObjectDimension);
		frameChckbx.setMaximumSize(singleObjectDimension);
		frameChckbx.setLocation(0, 0);
		frameType.setSize(singleObjectDimension);
		frameType.setMaximumSize(singleObjectDimension);
		frameType.setLocation(0, (int) (1 * (singleObjectDimension.getHeight()
				+ singleSpacerDimension.getHeight())));

		frameColor.setSize(singleObjectDimension);
		frameColor.setMaximumSize(singleObjectDimension);
		frameColor.setLocation(0, (int) (2 * (singleObjectDimension.getHeight()
				+ singleSpacerDimension.getHeight())));
		frameSize.setSize(singleObjectDimension);
		frameSize.setMaximumSize(singleObjectDimension);
		frameSize.setLocation(0, (int) (3 * (singleObjectDimension.getHeight()
				+ singleSpacerDimension.getHeight())));
		frameThickness.setSize(singleObjectDimension);
		frameThickness.setMaximumSize(singleObjectDimension);
		frameThickness.setLocation(0,
				(int) (4 * (singleObjectDimension.getHeight()
						+ singleSpacerDimension.getHeight())));
		framePanel.add(frameChckbx);
		framePanel.add(frameColor);
		framePanel.add(frameType);
		framePanel.add(frameSize);
		framePanel.add(frameThickness);

		watermarkPanel = new JPanel();
		watermarkPanel.setLocation((int) (3 * (singleObjectDimension.getWidth()
				+ singleSpacerDimension.getWidth())), 0);
		watermarkPanel.setSize((int) singleObjectDimension.getWidth(),
				(int) (4 * singleObjectDimension.getHeight()
						+ 3 * singleSpacerDimension.getHeight()));
		watermarkPanel.setMaximumSize(
				new Dimension((int) singleObjectDimension.getWidth(),
						(int) (4 * (singleObjectDimension.getHeight()
								+ singleSpacerDimension.getHeight()))));
		watermarkPanel.setLayout(null);

		watermarkChckbx = new JCheckBox("Watermark");
		watermarkChckbx.setToolTipText("Add watermark to the pictures");
		watermarkChckbx.setActionCommand("watermark");
		watermarkChckbx.addActionListener(this);

		watermarkSize = new JComboBox<Float>();
		watermarkSize.addItem((float) 0.5);
		watermarkSize.addItem((float) 1);
		watermarkSize.addItem((float) 1.5);
		watermarkSize.addItem((float) 2);
		watermarkSize.addItem((float) 2.5);
		watermarkSize.addItem((float) 3);
		watermarkSize.addItem((float) 3.5);
		watermarkSize.addItem((float) 4);
		watermarkSize.addItem((float) 4.5);
		watermarkSize.addItem((float) 5);

		watermarkPosition = new JComboBox<String>();
		watermarkPosition.addItem("center");
		watermarkPosition.addItem("southeast");
		watermarkPosition.addItem("east");
		watermarkPosition.addItem("northeast");
		watermarkPosition.addItem("north");
		watermarkPosition.addItem("northwest");
		watermarkPosition.addItem("west");
		watermarkPosition.addItem("southwest");

		watermarkFont = new JComboBox<String>();
		watermarkFontArray = new ArrayList<String>();
		watermarkFontArray.add(
				"/org/mdcomparetti/resources/fonts/FFXMarquee.ttf");
		watermarkFontArray.add(
				"/org/mdcomparetti/resources/fonts/WalkwayCondensed.ttf");
		watermarkFontArray.add(
				"/org/mdcomparetti/resources/fonts/Existence-Light.otf");
		Collections.sort(watermarkFontArray);
		for (int iter = 0; iter < watermarkFontArray.size(); iter++) {
			String tmp = watermarkFontArray.get(iter);
			tmp = tmp.substring(0, (tmp.lastIndexOf(".") != -1)
					? tmp.lastIndexOf(".")
					: tmp.length());
			tmp = tmp.substring(
					(tmp.lastIndexOf("/") != -1) ? tmp.lastIndexOf("/") + 1 : 0,
					tmp.length());
			tmp = tmp.substring((tmp.lastIndexOf("\\") != -1)
					? tmp.lastIndexOf("\\") + 1
					: 0, tmp.length());
			watermarkFont.addItem(tmp);
		}
		watermarkFont.setEnabled(false);

		watermarkChckbx.setSize(singleObjectDimension);
		watermarkChckbx.setMaximumSize(singleObjectDimension);
		watermarkChckbx.setLocation(0, 0);
		watermarkPosition.setSize(singleObjectDimension);
		watermarkPosition.setMaximumSize(singleObjectDimension);
		watermarkPosition.setLocation(0,
				(int) (1 * (singleObjectDimension.getHeight()
						+ singleSpacerDimension.getHeight())));
		watermarkSize.setSize(singleObjectDimension);
		watermarkSize.setMaximumSize(singleObjectDimension);
		watermarkSize.setLocation(0,
				(int) (2 * (singleObjectDimension.getHeight()
						+ singleSpacerDimension.getHeight())));
		watermarkFont.setSize(singleObjectDimension);
		watermarkFont.setMaximumSize(singleObjectDimension);
		watermarkFont.setLocation(0,
				(int) (3 * (singleObjectDimension.getHeight()
						+ singleSpacerDimension.getHeight())));
		watermarkPanel.add(watermarkChckbx);
		watermarkPanel.add(watermarkSize);
		watermarkPanel.add(watermarkPosition);
		watermarkPanel.add(watermarkFont);

		resizePanel = new JPanel();
		resizePanel.setLocation((int) (4 * (singleObjectDimension.getWidth()
				+ singleSpacerDimension.getWidth())), 0);
		resizePanel
				.setSize(new Dimension((int) singleObjectDimension.getWidth(),
						(int) (5 * singleObjectDimension.getHeight()
								+ 4 * singleSpacerDimension.getHeight())));
		resizePanel.setMaximumSize(
				new Dimension((int) singleObjectDimension.getWidth(),
						(int) (5 * (singleObjectDimension.getHeight()
								+ singleSpacerDimension.getHeight()))));
		resizePanel.setLayout(null);

		resizeChckbx = new JCheckBox("Resize");
		resizeChckbx.setToolTipText("Resize pictures");
		resizeChckbx.setActionCommand("resize");
		resizeChckbx.addActionListener(this);
		
		resizeValueModel = new DefaultComboBoxModel<String>(
				new String[]{"800", "1000", "1200", "2000"});
		resizeValue = new JComboBox<String>(resizeValueModel);
		resizeValue.setEditable(true);
		resizeValue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ("comboBoxEdited".equals(e.getActionCommand())) {
					Object newValue = resizeValueModel.getSelectedItem();
					resizeValueModel.addElement((String) newValue);
					resizeValue.setSelectedItem(newValue);
				}
			}
		});

		resizeEdge = new JComboBox<String>();
		resizeEdge.addItem("Lungo");
		resizeEdge.addItem("Corto");

		resizeFullHDChckbx = new JCheckBox("Full HD");
		resizeFullHDChckbx.setToolTipText("FullHD size");
		resizeFullHDChckbx.setActionCommand("fullhd");
		resizeFullHDChckbx.addActionListener(this);
		resizeUltraHDChckbx = new JCheckBox("Ultra HD");
		resizeUltraHDChckbx.setToolTipText("Ultra HD 4k size");
		resizeUltraHDChckbx.setActionCommand("ultrahd");
		resizeUltraHDChckbx.addActionListener(this);
		
		resizeChckbx.setSize(singleObjectDimension);
		resizeChckbx.setMaximumSize(singleObjectDimension);
		resizeChckbx.setLocation(0, 0);
		resizeValue.setSize(singleObjectDimension);
		resizeValue.setMaximumSize(singleObjectDimension);
		resizeValue.setLocation(0, (int) (1 * (singleObjectDimension.getHeight()
				+ singleSpacerDimension.getHeight())));
		resizeEdge.setSize(singleObjectDimension);
		resizeEdge.setMaximumSize(singleObjectDimension);
		resizeEdge.setLocation(0, (int) (2 * (singleObjectDimension.getHeight()
				+ singleSpacerDimension.getHeight())));
		resizeFullHDChckbx.setSize(singleObjectDimension);
		resizeFullHDChckbx.setMaximumSize(singleObjectDimension);
		resizeFullHDChckbx.setLocation(0, (int) (3 * (singleObjectDimension.getHeight()
				+ singleSpacerDimension.getHeight())));
		resizeUltraHDChckbx.setSize(singleObjectDimension);
		resizeUltraHDChckbx.setMaximumSize(singleObjectDimension);
		resizeUltraHDChckbx.setLocation(0, (int) (4 * (singleObjectDimension.getHeight()
				+ singleSpacerDimension.getHeight())));
		resizePanel.add(resizeChckbx);
		resizePanel.add(resizeValue);
		resizePanel.add(resizeEdge);
		resizePanel.add(resizeFullHDChckbx);
		resizePanel.add(resizeUltraHDChckbx);

		commandsPanel.add(miscPanel);
		commandsPanel.add(colorProfilePanel);
		commandsPanel.add(framePanel);
		commandsPanel.add(watermarkPanel);
		commandsPanel.add(resizePanel);

		logText = new MyJTextArea();
		// logText.setColumns(5);
		logText.setMargin(new Insets(5, 5, 5, 5));
		logText.setLocation(0, 0);
		logText.setSize(new Dimension((int) commandsPanel.getSize().getWidth(),
				(int) (15 * singleObjectDimension.getHeight())));
		logText.setPreferredSize(
				new Dimension((int) commandsPanel.getSize().getWidth(),
						(int) (15 * singleObjectDimension.getHeight())));
		logText.setMaximumSize(
				new Dimension((int) commandsPanel.getSize().getWidth(),
						(int) (15 * singleObjectDimension.getHeight())));
		logText.setEditable(false);
		//logText.setLineWrap(true);
		//logText.setWrapStyleWord(true);
		logText.setTopInsertion(false);

		progressPanel = new JPanel();
		progressPanel.setBounds((int) (commandsPanel.getLocation().x),
				(int) (commandsPanel.getLocation().y
						+ commandsPanel.getSize().getHeight()
						+ singleSpacerDimension.getHeight()),
				(int) commandsPanel.getSize().getWidth(),
				(int) (singleObjectDimension.getHeight()));
		progressPanel.setMaximumSize(
				new Dimension((int) commandsPanel.getSize().getWidth(),
						(int) (singleObjectDimension.getHeight())));
		mainFrame.getContentPane().add(progressPanel);
		progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.X_AXIS));

		progressBarProcess = new JProgressBar(0, 100);
		progressBarProcess.setToolTipText("Progresso");
		progressBarProcess.setValue(progressBarProcess.getMinimum());
		progressBarProcess.setStringPainted(true);

		exitBtn = new JButton("Esci");
		exitBtn.setToolTipText("Chiudi il programma");
		exitBtn.setActionCommand("exit");
		exitBtn.addActionListener(this);

		progressBarProcess.setPreferredSize(new Dimension(
				(int) (commandsPanel.getSize().getWidth()
						- singleObjectDimension.getWidth()
						- singleSpacerDimension.getWidth()),
				(int) singleObjectDimension.getWidth()));
		progressBarProcess.setMaximumSize(new Dimension(
				(int) (commandsPanel.getSize().getWidth()
						- singleObjectDimension.getWidth()
						- singleSpacerDimension.getWidth()),
				(int) singleObjectDimension.getWidth()));
		exitBtn.setPreferredSize(singleObjectDimension);
		exitBtn.setMaximumSize(singleObjectDimension);
		progressPanel.add(progressBarProcess);
		progressPanel.add(Box.createHorizontalGlue());
		progressPanel.add(exitBtn);
		
		bottomPanel = new JPanel();
		bottomPanel.setBounds((int) (progressPanel.getLocation().x),
				(int) (progressPanel.getLocation().y
						+ progressPanel.getSize().getHeight()
						+ singleSpacerDimension.getHeight()),
				(int) commandsPanel.getSize().getWidth(),
				(int) (singleObjectDimension.getHeight()));
		bottomPanel.setMaximumSize(
				new Dimension((int) commandsPanel.getSize().getWidth(),
						(int) (singleObjectDimension.getHeight())));
		mainFrame.getContentPane().add(bottomPanel);
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

		infoLbl = new JLabel(softwareInfoLabel, SwingConstants.RIGHT);
		infoLbl.setFont(new Font("FFX Marquee", Font.PLAIN, 13));
		infoLbl.setSize((int) (2.5 * singleObjectDimension.getWidth()),
				(int) singleObjectDimension.getHeight());
		
		errorMsg = new JLabel("Activity area", SwingConstants.LEFT);
		errorMsg.setFont(new Font(errorMsg.getFont().getName(), Font.PLAIN, 10));
		errorMsg.setSize((int) (3 * singleObjectDimension.getWidth()),
				(int) singleObjectDimension.getHeight());
		bottomPanel.add(errorMsg);
		bottomPanel.add(Box.createHorizontalGlue());
		bottomPanel.add(infoLbl);
		
		mainFrame.setSize(
				(int) (bottomPanel.getLocation().x + bottomPanel.getSize().getWidth()
						+ 2 * singleSpacerDimension.getWidth()),
				(int) (bottomPanel.getLocation().y + bottomPanel.getSize().getHeight()
						+ 4 * singleSpacerDimension.getHeight()));
		GetProgramPaths();
	}

	private void GetProgramPaths() {
		Map<String, String> env = System.getenv();

		String pathvar = new String();
		for (String envName : env.keySet()) {
			if (envName.toLowerCase().equals("path")) {
				pathvar = env.get(envName);
				continue;
			}
		}
		OsDetector OSrelated = new OsDetector();
		/*
		 * String[] separated = pathvar .split(OSrelated.getPathSeparator());
		 */
		List<String> separated = Arrays
				.asList(pathvar.split(OSrelated.getPathSeparator()));
		File cmdFile;
		cmdFile = searchProgram("exiftool", separated, OSrelated);
		if (cmdFile != null) {
			cmdExecutables.put("exiftool", cmdFile);
			addToLog("Found exiftool version " + executeCommand(new String[]{cmdExecutables.get("exiftool").toString(), "-ver"}));
		}
		cmdFile = searchProgram("convert", separated, OSrelated);
		if (cmdFile != null) {
			cmdExecutables.put("convert", cmdFile);
			String versionNumber = executeCommand(new String[]{cmdExecutables.get("convert").toString(), "-version"});
			addToLog("Found convert version " + versionNumber.substring(versionNumber.indexOf("ImageMagick ") + ((String) "ImageMagick ").length(), versionNumber.indexOf(" http://")));
		}
	}

	private File searchProgram(String searchString, List<String> separated,
			OsDetector OS) {
		File testFile;
		for (String singlePath : separated) {
			singlePath = singlePath.trim();
			singlePath += File.separator;
			singlePath += searchString;
			if (OS.isWindows())
				singlePath += ".exe";
			testFile = new File(singlePath);
			if (testFile.exists()) {
				switch (searchString) {
					case "convert":
						if (executeCommand(
								new String[]{testFile.toString(), "-version"})
										.contains("ImageMagick"))
							return testFile;
						break;
					case "exiftool":
						try {
							if (Float.parseFloat(executeCommand(new String[]{
									testFile.toString(), "-ver"})) > 9.0)
								return testFile;
						} catch (Exception ex) {
							break;
						}
						break;
					default :
						break;
				}
			}
		}
		return null;
	}
	private void GetConfigProps() {
		photographerText.setText(configProps.getProperty("author"));
		copyrightChckbx.setSelected(configProps.getProperty("copyright")
				.compareTo(configurationBoolean[0]) == 0);
		commentChckbx.setSelected(configProps.getProperty("info")
				.compareTo(configurationBoolean[0]) == 0);
		colorProfileChckbx.setSelected(configProps.getProperty("cprofile")
				.compareTo(configurationBoolean[0]) == 0);
		cleanExifChckbx.setSelected(configProps.getProperty("noexif")
				.compareTo(configurationBoolean[0]) == 0);
		colorProfileFile.setSelectedItem(
				(String) configProps.getProperty("cprofileFile"));
		watermarkChckbx.setSelected(configProps.getProperty("watermark")
				.compareTo(configurationBoolean[0]) == 0);
		watermarkSize.setSelectedItem(Float
				.parseFloat(configProps.getProperty("watermarkSize")));
		watermarkFont.setSelectedItem(
				(String) (configProps.getProperty("watermarkFont")));
		watermarkPosition.setSelectedItem(
				(String) (configProps.getProperty("watermarkPosition")));
		frameChckbx.setSelected(configProps.getProperty("frame")
				.compareTo(configurationBoolean[0]) == 0);
		frameColor.setSelectedItem(
				(String) (configProps.getProperty("frameColor")));
		frameType.setSelectedItem(
				(String) (configProps.getProperty("frameType")));
		frameSize.setSelectedItem(
				Float.parseFloat(configProps.getProperty("frameSize")));
		frameThickness.setSelectedItem(
				Float.parseFloat(configProps.getProperty("frameThickness")));
		resizeChckbx.setSelected(configProps.getProperty("resize")
				.compareTo(configurationBoolean[0]) == 0);
		resizeEdge.setSelectedItem(
				(String) (configProps.getProperty("resizeEdge")));
		resizeValue.setSelectedItem(
				(String) (configProps.getProperty("resizeSize")));
		resizeFullHDChckbx.setSelected(false);
		resizeUltraHDChckbx.setSelected(false);
	}

	private void ResetConfigProps(boolean defaultValues) {
		if (defaultValues) {
			configPropsDefault.setProperty("author", "Mirko D. Comparetti");
			configPropsDefault.setProperty("copyright",configurationBoolean[0]);
			configPropsDefault.setProperty("info", configurationBoolean[0]);
			configPropsDefault.setProperty("noexif", configurationBoolean[1]);
			configPropsDefault.setProperty("cprofile", configurationBoolean[0]);
			configPropsDefault.setProperty("cprofileFile", "srgb");
			configPropsDefault.setProperty("frame", configurationBoolean[1]);
			configPropsDefault.setProperty("frameColor", "white");
			configPropsDefault.setProperty("frameType", "Inner");
			configPropsDefault.setProperty("frameSize", "2.0");
			configPropsDefault.setProperty("frameThickness", "0.25");
			configPropsDefault.setProperty("watermark",configurationBoolean[1]);
			configPropsDefault.setProperty("watermarkSize", "2.0");
			configPropsDefault.setProperty("watermarkFont", "FFXMarquee");
			configPropsDefault.setProperty("watermarkPosition", "southeast");
			configPropsDefault.setProperty("resize",configurationBoolean[1]);
			configPropsDefault.setProperty("resizeEdge", "Lungo");
			configPropsDefault.setProperty("resizeSize", "1200");
		} else {
			configProps.setProperty("author", photographerText.getText());
			configProps.setProperty("copyright", copyrightChckbx.isSelected()
					? configurationBoolean[0]
					: configurationBoolean[1]);
			configProps.setProperty("info", commentChckbx.isSelected()
					? configurationBoolean[0]
					: configurationBoolean[1]);
			configProps.setProperty("noexif", cleanExifChckbx.isSelected()
					? configurationBoolean[0]
					: configurationBoolean[1]);
			configProps.setProperty("cprofile", colorProfileChckbx.isSelected()
					? configurationBoolean[0]
					: configurationBoolean[1]);
			configProps.setProperty("cprofileFile",
					(String) colorProfileFile.getSelectedItem());
			configProps.setProperty("frame", frameChckbx.isSelected()
					? configurationBoolean[0]
					: configurationBoolean[1]);
			configProps.setProperty("frameColor",
					(String) (frameColor.getSelectedItem()));
			configProps.setProperty("frameType",
					(String) (frameType.getSelectedItem()));
			configProps.setProperty("frameSize",
					frameSize.getSelectedItem().toString());
			configProps.setProperty("frameThickness",
					frameThickness.getSelectedItem().toString());
			configProps.setProperty("watermark", watermarkChckbx.isSelected()
					? configurationBoolean[0]
					: configurationBoolean[1]);
			configProps.setProperty("watermarkSize",
					watermarkSize.getSelectedItem().toString());
			configProps.setProperty("watermarkFont",
					(String) (watermarkFont.getSelectedItem()));
			configProps.setProperty("watermarkPosition",
					(String) (watermarkPosition.getSelectedItem()));
			configProps.setProperty("resize", resizeChckbx.isSelected()
					? configurationBoolean[0]
					: configurationBoolean[1]);
			configProps.setProperty("resizeEdge",
					(String) (resizeEdge.getSelectedItem()));
			configProps.setProperty("resizeSize",
					(String) resizeValue.getSelectedItem());
		}
	}

	private void LoadConfiguration() {
		try {
			configReader = new FileInputStream(configFile);
			configProps.loadFromXML(configReader);
			configReader.close();

		} catch (FileNotFoundException exRead) {
			addToLog("Configuration file not found, default values are saved", false);
			configProps = configPropsDefault;
			SaveConfiguration(true);

		} catch (IOException exRead) {
			addToLog("Configuration file read error", false);
		}
	}

	private void SaveConfiguration() {
		SaveConfiguration(false);
	}

	private void SaveConfiguration(boolean defaultValues) {
		try {
			configWriter = new FileOutputStream(configFile);
			if (defaultValues)
				configPropsDefault.storeToXML(configWriter,
						"Default configuration");
			else {
				ResetConfigProps(false);
				configProps.storeToXML(configWriter, "Personal configuration");
			}

			configWriter.close();
			addToLog("Configuration saved");
		} catch (FileNotFoundException exWrite) {
			addToLog("Configuration file not found", Color.RED);
		} catch (IOException exWrite) {
			addToLog("Configuration file write error", Color.RED);
		}
	}

	private float Max(float f, float g) {
		return ((f > g) ? f : g);
	}

	private float Min(float f, float g) {
		return ((f > g) ? g : f);
	}

	private File[] SelectFiles(List<String> allowedExtensions,
			boolean caseSensitive) {
		/*
		 * File[] listOfFiles = this.folderInput.listFiles(new FilenameFilter()
		 * { public boolean accept(File dir, String name) { return
		 * ((caseSensitive) ? name.endsWith("." + fileExtension) :
		 * name.toLowerCase().endsWith("." + fileExtension)); } }); return
		 * listOfFiles;
		 */
		return this.folderInput.listFiles(
				new ExtensionsFilter((String[]) allowedExtensions.toArray()));
	}

	private File[] SelectFiles(List<String> allowedExtensions) {
		return SelectFiles(allowedExtensions, false);
	}

	/*private String[] FilesToString(File[] fileList) {
		String[] filesToProcess = new String[fileList.length];
		for (int iter = 0; iter < fileList.length; iter++) {
			if (fileList[iter].isFile()) {
				filesToProcess[iter] = fileList[iter].getName();
			}
		}
		return filesToProcess;
	}*/

	private String[] FilesToString(List<File> fileList) {
		String[] filesToProcess = new String[fileList.size()];
		for (int iter = 0; iter < fileList.size(); iter++) {
			if (fileList.get(iter).isFile()) {
				filesToProcess[iter] = fileList.get(iter).getName();
			}
		}
		return filesToProcess;
	}

	private Dimension GetRawImageSize(String workingFile, File folderRef) {
		int width = Integer.parseInt(executeCommand(new String[]{cmdExecutables.get("exiftool").toString(),"-ImageWidth", "-S", "-s",folderRef + File.separator + workingFile}));
		int height = Integer.parseInt(executeCommand(new String[]{cmdExecutables.get("exiftool").toString(),"-ImageHeight", "-S", "-s",folderRef + File.separator + workingFile}));
		return (new Dimension(width, height));
	}

	private Dimension GetImageSize(String workingFile, File folderRef) {
		Dimension RawImageSize = GetRawImageSize(workingFile, folderRef);
		if (!Orientation(workingFile, folderRef).isEmpty() && !IsHorizontal(workingFile, folderRef)) {
			//addToLog("Switching coordinates", false);
			return (new Dimension((int) Math.round(RawImageSize.getHeight()), (int) Math.round(RawImageSize.getWidth())));
		}
		return (new Dimension((int) RawImageSize.getWidth(), (int) RawImageSize.getHeight()));
	}

	private boolean IsHorizontal(String workingFile, File folderRef) {
		String OrientStr = Orientation(workingFile, folderRef);
		if (OrientStr.isEmpty()) {
			addToLog("Orient not defined", false);
			Dimension imgSize = GetRawImageSize(workingFile, folderRef);
			if (imgSize.width <= imgSize.height)
				return false;
			return true;
		}
		/*int Orient = Integer.parseInt(OrientStr);
		if (Orient == 6 || Orient == 8 || Orient == 5 || Orient == 7) {*/
		if (OrientStr.equals("6") || OrientStr.equals("8") || OrientStr.equals("5") || OrientStr.equals("7")) {
			return false;
		}
		return true;
	}

	private String Orientation(String workingFile, File folderRef) {
		return executeCommand(new String[]{cmdExecutables.get("exiftool").toString(),"-Orientation", "-s3", "-n",folderRef + File.separator + workingFile});
	}

	private Point ScalePoint(Point offsetCorner, float[] scaleFactor) {
		return (new Point((int) Math.round(offsetCorner.x * scaleFactor[0]),
				(int) Math.round(offsetCorner.y * scaleFactor[1])));
	}

	private Point addPoint(Point a, Point b) {
		a.translate(b.x, b.y);
		return a;
	}

	private int GetShape(Dimension imgSize) {
		if (imgSize.getWidth() == imgSize.getHeight())
			return 0;
		return ((imgSize.getWidth() > imgSize.getHeight()) ? 1 : -1);
	}

	private void WatermarkDataEnable(boolean enable) {
		watermarkSize.setEnabled(enable);
		watermarkPosition.setEnabled(enable);
		watermarkFont.setEnabled(enable);
	}

	private void ResizeDataEnable(boolean enable) {
		resizeFullHDChckbx.setEnabled(enable && !resizeUltraHDChckbx.isSelected());
		resizeUltraHDChckbx.setEnabled(enable && !resizeFullHDChckbx.isSelected());
		if (enable) {
			ResizeCustomEnable(!(resizeFullHDChckbx.isSelected() || resizeUltraHDChckbx.isSelected()));
		}
		else {
			resizeValue.setEnabled(enable);
			resizeEdge.setEnabled(enable);			
		}
	}

	private void ResizeCustomEnable(boolean enable) {
		resizeValue.setEnabled(enable);
		resizeEdge.setEnabled(enable);
	}

	private void ResizeCustomEnable(boolean enable, String checkbox) {
		ResizeCustomEnable(enable);
		switch (checkbox) {
			case "fullhd":
				resizeFullHDChckbx.setEnabled(enable);
				break;
			case "ultrahd":
				resizeUltraHDChckbx.setEnabled(enable);
				break;
			default:
	            break;
		}
	}

	private void FrameDataEnable(boolean enable) {
		frameColor.setEnabled(enable);
		frameSize.setEnabled(enable);
		frameThickness.setEnabled(enable);
		frameType.setEnabled(enable);
	}

	private void ColorProfileDataEnable(boolean enable) {
		colorProfileFile.setEnabled(enable);
		colorProfileIntent.setEnabled(enable);
	}

	private String GetOffset(String textPosition, Point offsetPoint) {
		if (offsetPoint == null)
			return "+0+0";
		addPoint(offsetPoint, new Point((int) Math.round(0.1 * offsetPoint.x),
				(int) Math.round(0.1 * offsetPoint.y)));
		String offset = new String();
		switch (textPosition) {
			case "southwest":
			case "southeast":
			case "northeast":
			case "northwest":
				offset = "+" + offsetPoint.x + "+" + offsetPoint.y;
				break;
			case "south":
			case "north":
				offset = "+0" + "+" + offsetPoint.y;
				break;
			case "east":
			case "west":
				offset = "+" + offsetPoint.x + "+0";
				break;
			case "center":
			default :
				offset = "+0+0";
				break;
		}
		return offset;
	}

	private void WatermarkCommand(String workingFile, Point offsetCorner) {
		if (!isRunning)
			return;
		addToLog("Adding watermark to " + workingFile, false);

		String[] command;
		String tmpdata;

		Dimension imgSize = GetImageSize(workingFile, folderOutput);

		float dimension = (int) Max((float) imgSize.getWidth(),
				(float) imgSize.getHeight());

		dimension = (float) (((float) dimension) / 100.0
				* ((float) watermarkSize.getSelectedItem()));
		dimension = (float) ((dimension < 15) ? 15.0 : dimension);

		String watermark = copyrightSymbol + " " + photographerText.getText();
		command = new String[]{cmdExecutables.get("exiftool").toString(), "-d",
				"%Y", "-DateTimeOriginal", "-S", "-s",
				folderOutput + File.separator + workingFile};

		tmpdata = executeCommand(command);
		watermark += (!tmpdata.isEmpty()) ? (" | " + tmpdata) : ("");

		float scaleFactor[] = {1, 1};
		switch (GetShape(imgSize)) {
			case 1 : // Horizontal
				scaleFactor[0] += (float) 0.1 * imgSize.getWidth()
						/ imgSize.getHeight();
				scaleFactor[1] += (float) 0.1 * imgSize.getHeight()
						/ imgSize.getWidth();
				break;
			case -1 : // Vertical
				scaleFactor[0] += (float) 0.1 * imgSize.getWidth()
						/ imgSize.getHeight();
				scaleFactor[1] += (float) 0.1 * imgSize.getHeight()
						/ imgSize.getWidth();
				break;
			default : // Square
				scaleFactor[0] += (float) 0.1;
				scaleFactor[1] += (float) 0.1;
				break;
		}
		offsetCorner = ScalePoint(offsetCorner, scaleFactor);

		String offset = GetOffset(
				watermarkPosition.getSelectedItem().toString(), offsetCorner);

		try {
			command = new String[]{cmdExecutables.get("convert").toString(),
					folderOutput + File.separator + workingFile, "-font",
					ExportResource(watermarkFontArray
							.get(watermarkFont.getSelectedIndex())),
					"-pointsize", Float.toString(dimension), "-gravity",
					watermarkPosition.getSelectedItem().toString(), "-stroke",
					"#000C", "-strokewidth",
					Float.toString((float) (dimension / 10.0)), "-annotate",
					offset, watermark, "-stroke", "none", "-fill", "white",
					"-annotate", offset, watermark,
					folderOutput + File.separator + workingFile};
			executeCommand(command);
		} catch (Exception e) {
			addToLog(e.getMessage(), Color.RED, false);
		}
	}

	private void ResizeCommand(String workingFile) {
		if (!isRunning)
			return;
		addToLog("Changing dimensions of " + workingFile);

		String[] command;

		Dimension imgSize = GetImageSize(workingFile, folderOutput);

		if (resizeFullHDChckbx.isSelected()) {
			String size = "1920x1080";
			command = new String[]{cmdExecutables.get("convert").toString(),
					folderOutput + File.separator + workingFile, "-resize",
					size, folderOutput + File.separator + workingFile};
		}
		else if (resizeUltraHDChckbx.isSelected()) {
				String size = "3840x2160";
				command = new String[]{cmdExecutables.get("convert").toString(),
						folderOutput + File.separator + workingFile, "-resize",
						size, folderOutput + File.separator + workingFile};
			}
		else {
			float resizeRatio = 100;
			int dimensionRef = 1000;
			dimensionRef = (int) Max((float) imgSize.getWidth(),
					(float) imgSize.getHeight());
			if (resizeEdge.getSelectedItem().toString()
					.compareToIgnoreCase("corto") == 0)
				dimensionRef = (int) Min((float) imgSize.getWidth(),
						(float) imgSize.getHeight());
			try {
				resizeRatio = (float) (((float) Integer
						.parseInt(resizeValue.getSelectedItem().toString()))
						/ ((float) dimensionRef) * 100.0);
			} catch (Exception e) {
				addToLog("Exception");
			}
			command = new String[]{cmdExecutables.get("convert").toString(),
					folderOutput + File.separator + workingFile, "-resize",
					Float.toString(resizeRatio) + "%",
					folderOutput + File.separator + workingFile};
		}

		executeCommand(command);
	}

	private Point FrameCommand(String workingFile) {
		if (!isRunning)
			return null;
		addToLog("Adding frame to " + workingFile);

		float size = (float) frameSize.getSelectedItem();
		String mode = frameType.getSelectedItem().toString();
		String color = frameColor.getSelectedItem().toString();

		Point offset = new Point();

		switch (mode) {
			case "Italy":
				offset = FrameCommand(workingFile, "Plain", size, "red");
				addPoint(offset,
						FrameCommand(workingFile, "Plain", size, "white"));
				addPoint(offset,
						FrameCommand(workingFile, "Plain", size, "green"));
				break;
			case "Double":
				offset = FrameCommand(workingFile, "Plain",
						(int) (0.75 * ((float) (size))), "white");
				addPoint(offset,
						FrameCommand(workingFile, "Plain", size, "black"));
				break;
			case "Outer":
				offset = FrameCommand(workingFile, "Plain", size, "black");
				FrameCommand(workingFile, "Inner",
						(int) Math.round(((float) (size)) / 1.5), "white");
				break;
			case "Blur":
				ArrayList<Point> corners = getCorners(
						GetImageSize(workingFile, folderOutput), size);
				String command[] = new String[]{
						cmdExecutables.get("convert").toString(), "(",
						folderOutput + File.separator + workingFile, "-blur",
						"0x20", ")", "(",
						folderOutput + File.separator + workingFile, "-crop",
						Math.round(corners.get(1).x - corners.get(0).x) + "x"
								+ Math.round(
										corners.get(1).y - corners.get(0).y)
								+ "+" + Math.round(corners.get(0).x) + "+"
								+ Math.round(corners.get(0).y),
						"+repage", ")", "-geometry",
						"+" + Math.round(corners.get(0).x) + "+"
								+ Math.round(corners.get(0).y),
						"-composite",
						folderOutput + File.separator + workingFile};
				executeCommand(command);
				offset = FrameCommand(workingFile, "Inner", size, color);
				break;
			case "Inner":
			case "Rounded":
			case "Plain":
				offset = FrameCommand(workingFile, mode, size, color);
				break;
			default :
				break;
		}
		return offset;
	}

	private Point FrameCommand(String workingFile, String mode, float size,
			String color) {
		if (!isRunning)
			return new Point(0, 0);

		String[] command;
		float thick = (float) frameThickness.getSelectedItem();

		Dimension imgSize = GetImageSize(workingFile, folderOutput);

		ArrayList<Point> corners = getCorners(imgSize, size);

		thick = (float) (Max((float) Math.round(imgSize.getWidth()),
				(float) Math.round(imgSize.getHeight())) * (thick / 100.0));

		switch (mode) {
			case "Inner":
				command = new String[]{cmdExecutables.get("convert").toString(),
						folderOutput + File.separator + workingFile, "-fill",
						"none", "-stroke", color, "-strokewidth",
						Float.toString(thick), "-draw",
						"\"rectangle " + Math.round(corners.get(0).x) + ","
								+ Math.round(corners.get(0).y) + " "
								+ Math.round(corners.get(1).x) + ","
								+ Math.round(corners.get(1).y) + "\"",
						folderOutput + File.separator + workingFile};
				executeCommand(command);
				break;
			case "Rounded":
				command = new String[]{cmdExecutables.get("convert").toString(),
						folderOutput + File.separator + workingFile, "-fill",
						"none", "-stroke", color, "-strokewidth",
						Float.toString(thick), "-draw",
						"\"roundrectangle " + Math.round(corners.get(0).x) + ","
								+ Math.round(corners.get(0).y) + " "
								+ Math.round(corners.get(1).x) + ","
								+ Math.round(corners.get(1).y) + " "
								+ Math.round(
										Max(corners.get(0).x, corners.get(0).y))
								+ ","
								+ Math.round(
										Max(corners.get(0).x, corners.get(0).y))
								+ "\"",
						folderOutput + File.separator + workingFile};
				executeCommand(command);
				break;
			case "Plain":
				command = new String[]{cmdExecutables.get("convert").toString(),
						folderOutput + File.separator + workingFile,
						"-bordercolor", color, "-border",
						size + "%x" + size + "%",
						folderOutput + File.separator + workingFile};
				executeCommand(command);
				break;
			default :
				break;
		}
		return corners.get(0);
	}

	private ArrayList<Point> getCorners(Dimension imgSize, float size) {
		// NorthWest
		ArrayList<Point> corners = new ArrayList<Point>();
		corners.add(
				new Point((int) Math.round(imgSize.getWidth() * size / 100.0),
						(int) Math.round(imgSize.getHeight() * size / 100.0)));
		// SouthEast
		corners.add(new Point(
				(int) Math.round(imgSize.getWidth() - corners.get(0).getX()),
				(int) Math.round(imgSize.getHeight() - corners.get(0).getY())));
		return corners;
	}

	private void ColorProfileCommand(String workingFile) {
		if (!isRunning)
			return;
		addToLog("Changing color profile to " + workingFile);

		try {
			String[] command = new String[]{
					cmdExecutables.get("convert").toString(),
					folderOutput + File.separator + workingFile,
					"-intent", colorProfileIntent.getSelectedItem().toString(), "-profile",
					ExportResource(colorProfileFileArray.get(colorProfileFile.getSelectedIndex())),
					folderOutput + File.separator + workingFile};
			//addToLog("Command " + Arrays.toString(command));
			executeCommand(command);
		} catch (Exception e) {
			addToLog(e.getMessage(), Color.RED);
		}
	}

	private void UpdateCommentExif(String workingFile) {
		if (!isRunning)
			return;
		addToLog("Updating EXIF info for " + workingFile);

		String[] command;
		String commentname = workingFile.substring(0,
				workingFile.lastIndexOf('.'));
		command = new String[]{cmdExecutables.get("exiftool").toString(), "-d",
				"%Y", folderOutput + File.separator + workingFile,
				"-comment=" + commentname, "-UserComment=" + commentname,
				"-overwrite_original"};
		executeCommand(command);
	}

	private void UpdateCopyrightExif(String workingFile) {
		if (!isRunning)
			return;
		addToLog("Updating copyright info for " + workingFile);

		String[] command;
		String tmpdata;
		String copyright = photographerText.getText();
		command = new String[]{cmdExecutables.get("exiftool").toString(), "-d",
				"%Y", "-DateTimeOriginal", "-S", "-s",
				folderOutput + File.separator + workingFile};
		tmpdata = executeCommand(command);
		if (!tmpdata.isEmpty())
			copyright += ", " + tmpdata;
		copyright += ". All rights reserved.";

		command = new String[]{cmdExecutables.get("exiftool").toString(),
				folderOutput + File.separator + workingFile,
				"-artist=" + photographerText.getText(),
				"-creator=" + photographerText.getText(),
				"-xpauthor=" + photographerText.getText(),
				"-copyright=" + copyrightSymbol + " " + copyright,
				"-ownername=" + photographerText.getText(),
				/*"-PropertyReleaseStatus='PR-NON'",
				"-XMP-iptcExt:DigitalSourceType=http://cv.iptc.org/newscodes/digitalsourcetype/digitalCapture",*/
				"-XMP-dc:Rights=" + copyright, "-XMP-xmpRights:Marked=True",
				"-CopyrightFlag=True",
				"-XMP-xmpRights:UsageTerms=This picture and it's metadata cannot be used and modified without permission. Every use must be explicitly authorized by the author. Any violations will be persecuted according to laws.",
				"-overwrite_original"};
		executeCommand(command);
	}

	private void CleanExifCommand(String workingFile) {
		if (!isRunning)
			return;
		addToLog("Removing private EXIF info of " + workingFile);

		String[] command = new String[]{
				cmdExecutables.get("exiftool").toString(),
				folderOutput + File.separator + workingFile, "-software=",
				"-creatortool=", "-XMP-xmp:all=", "-XMP-xmpMM:all=",
				"-XMP-photoshop:all=", "-XMP-aux:all=", "-photoshop:all=",
				"-overwrite_original"};
		executeCommand(command);
	}

	private void AddSoftwareEditor(String workingFile) {
		if (!isRunning)
			return;
		addToLog("Adding software info on " + workingFile);

		String[] command = new String[]{
				cmdExecutables.get("exiftool").toString(),
				folderOutput + File.separator + workingFile,
				"-software=" + softwareInfo, "-creatortool=" + softwareInfo,
				"-overwrite_original"};
		executeCommand(command);
	}

	private String executeCommand(String[] command) {
		ProcessBuilder ps = new ProcessBuilder(command);
		ps.directory(folderOutput);
		ps.redirectErrorStream(true);
		StringBuilder builder = new StringBuilder();
		String line;
		Process pr = null;
		try {
			pr = ps.start();
			// System.out.println(ps.command());
			// addToLog("Comando: " + ps.command().toString());
			BufferedReader in = new BufferedReader(
					new InputStreamReader(pr.getInputStream()));
			while ((line = in.readLine()) != null) {
				builder.append(line);
			}
			pr.waitFor();
			in.close();
			if (pr.exitValue() != 0) {
				addToLog("Process terminated with errors!\n" + builder.toString(),Color.RED);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		return builder.toString();
	}

	class WindowEventHandler extends WindowAdapter {
		public void windowClosing(WindowEvent evt) {
			DeleteResource(tmpFolderPath);
			System.exit(0);
		}
	}

	class MyJTextArea extends JTextArea {
		/**
		 * 
		 */
		private static final long serialVersionUID = -1607704083480562163L;

		private boolean topInsert = false;
		private DefaultCaret caret;

		public void setTopInsertion(boolean topInsert) {
			this.topInsert = topInsert;
			if (!topInsert) {
				caret = (DefaultCaret) this.getCaret();
				caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
			}
		}

		public boolean getTopInsertion() {
			return this.topInsert;
		}

		public void addLine(String str, Color textColor, boolean carriageReturn) {
			str = new String("[" + dateFormat
					.format(Calendar.getInstance().getTime()).toString() + "] ")
					+ str.trim();
			if (carriageReturn)
				str += "\n";
			if (this.getTopInsertion())
				this.insert(str, 0);
			else
				this.append(str);
			this.setForeground(Color.BLACK);
		}

		public void addLine(String str, Color textColor) {
			this.addLine(str, textColor, true);
		}

		public void addLine(String str, boolean carriageReturn) {
			this.addLine(str, Color.BLACK, carriageReturn);
		}

		public void addLine(String str) {
			this.addLine(str, Color.BLACK, true);
		}
	}

	private boolean isAThreadAlive(List<RunnableActions> threads) {
		boolean isAlive = false;
		for (int threadIterator = 0; threadIterator < threads
				.size(); threadIterator++) {
			isAlive |= threads.get(threadIterator).isAlive();
		}
		return isAlive;
	}

	private int numFilesProcessed(List<RunnableActions> threads) {
		int files = 0;
		for (int threadIterator = 0; threadIterator < threads
				.size(); threadIterator++) {
			files += threads.get(threadIterator).processedFiles();
		}
		return files;
	}

	private void addToLog(String str, Color textColor, boolean activityBar) {
		logText.addLine(str, textColor);
		if (activityBar)
			errorMsg.setText(str);
	}

	private void addToLog(String str, Color textColor) {
		addToLog(str, Color.BLACK, true);
	}
	
	private void addToLog(String str, boolean activityBar) {
		addToLog(str, Color.BLACK, activityBar);
	}
	
	private void addToLog(String str) {
		addToLog(str, Color.BLACK);
	}

	class Task extends SwingWorker<Void, Void> {
		/*
		 * Main task. Executed in background thread.
		 */
		public Void doInBackground() {
			setProgress(progressBarProcess.getMinimum());
			progressBarProcess.setValue(progressBarProcess.getMinimum());
			addToLog("Process started.");
			if (folderInput == null) {
				addToLog("ERROR: Select a source folder first!", Color.RED, false);
				return null;
			}
			if (!(watermarkChckbx.isSelected() || resizeChckbx.isSelected()
					|| frameChckbx.isSelected() || copyrightChckbx.isSelected()
					|| colorProfileChckbx.isSelected()
					|| cleanExifChckbx.isSelected()
					|| commentChckbx.isSelected())) {
				addToLog("ERROR: No process selected.", Color.RED, false);
				return null;
			}

			List<String> allowedExtensions = Arrays.asList(
					new String[]{".png", ".jpg", ".jpeg", ".tif", ".tiff"});
			Collections.sort(allowedExtensions);
			File[] listOfFiles = SelectFiles(allowedExtensions);
			String tmpStr = "";
			for (int iter = 0; iter < allowedExtensions.size(); iter++) {
				tmpStr += allowedExtensions.get(iter);
				tmpStr = (iter == allowedExtensions.size())
						? tmpStr
						: tmpStr + ", ";
			}
			addToLog("Processing files with extensions: " + tmpStr);

			if (folderInput != folderOutput) {
				addToLog("Start, copy of files from source to target", false);
				try {
					for (int iter = 0; (iter < listOfFiles.length
							&& isRunning); iter++) {
						if (listOfFiles[iter].isFile()) {
							Files.copy(listOfFiles[iter].toPath(),
									new File(folderOutput.toString()
											+ File.separator
											+ listOfFiles[iter].getName())
													.toPath(),
									REPLACE_EXISTING);
						}
					}
				} catch (IOException ex) {
					addToLog(ex.getMessage());
					return null;
				}
				addToLog("End, copy of files from source to target", false);
			}

			int numThreads = ((listOfFiles.length > numProcessors)
					? numProcessors
					: listOfFiles.length);

			addToLog("Process using " + numThreads + " threads.");

			List<RunnableActions> processThreads = new ArrayList<RunnableActions>(
					numThreads);
			List<List<File>> filePerThread = new ArrayList<List<File>>(
					numThreads);

			int quotientThread = listOfFiles.length / numThreads;
			int remainderThread = listOfFiles.length % numThreads;

			for (int threadIterator = 0; threadIterator < numThreads; threadIterator++) {
				filePerThread.add(threadIterator,
						new ArrayList<File>(quotientThread
								+ ((threadIterator >= remainderThread)
										? 0
										: 1)));
				for (int fileIterator = 0; fileIterator < (quotientThread
						+ ((threadIterator >= remainderThread)
								? 0
								: 1)); fileIterator++) {
					filePerThread.get(threadIterator).add(fileIterator,
							listOfFiles[threadIterator * quotientThread
									+ fileIterator
									+ ((threadIterator >= remainderThread)
											? remainderThread
											: threadIterator)]);
				}
				processThreads.add(threadIterator,
						new RunnableActions(filePerThread.get(threadIterator)));
			}

			for (int threadIterator = 0; threadIterator < numThreads; threadIterator++) {
				processThreads.get(threadIterator)
						.setName("Thread-" + (threadIterator + 1));
				processThreads.get(threadIterator).start();
			}

			while (isAThreadAlive(processThreads)) {
				try {
					Thread.sleep(10);
					setProgress(
							(int) (((float) (numFilesProcessed(processThreads)))
									/ ((float) (listOfFiles.length)) * 100f));
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
			}

			if (isRunning && (numFilesProcessed(
					processThreads) == listOfFiles.length)) {
				setProgress(progressBarProcess.getMaximum());
			}
			if (!isRunning) {
				setProgress(progressBarProcess.getMinimum());
			}

			return null;
		}

		/*
		 * Executed in event dispatch thread
		 */
		public void done() {
			if (isRunning)
				Toolkit.getDefaultToolkit().beep();
			CommandsEnable(true);
			if (isRunning)
				addToLog("Process executed successfully.", Color.GREEN);
			else
				addToLog("Processo executed with errors.", Color.BLUE);
		}
	}

	class OsDetector {

		private final String OS = System.getProperty("os.name").toLowerCase();

		public String getPathSeparator() {
			return System.getProperty("path.separator");
		}

		public boolean isWindows() {
			return (OS.indexOf("win") >= 0);
		}
		public boolean isMac() {
			return (OS.indexOf("mac") >= 0);
		}
		public boolean isUnix() {
			return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0
					|| OS.indexOf("aix") > 0);
		}
		public boolean isSolaris() {
			return (OS.indexOf("sunos") >= 0);
		}
	}

	private void CommandsEnable(boolean enabled) {
		startBtn.setEnabled(enabled);
		exitBtn.setEnabled(enabled);
		saveBtn.setEnabled(enabled);
		cancelBtn.setEnabled(!enabled);
		selectInputFolderBtn.setEnabled(enabled);
		selectOutputFolderBtn.setEnabled(enabled);

		colorProfileChckbx.setEnabled(enabled);
		watermarkChckbx.setEnabled(enabled);
		frameChckbx.setEnabled(enabled);
		resizeChckbx.setEnabled(enabled);
		colorProfileChckbx.setEnabled(enabled);
		copyrightChckbx.setEnabled(enabled);
		commentChckbx.setEnabled(enabled);
		cleanExifChckbx.setEnabled(enabled);

		if (enabled) {
			WatermarkDataEnable(watermarkChckbx.isSelected());
			ResizeDataEnable(resizeChckbx.isSelected());
			FrameDataEnable(frameChckbx.isSelected());
			ColorProfileDataEnable(colorProfileChckbx.isSelected());
		} else {
			WatermarkDataEnable(false);
			ResizeDataEnable(false);
			FrameDataEnable(false);
			ColorProfileDataEnable(false);
		}
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		switch (evt.getActionCommand()) {
			case "start":
				ResetConfigProps(false);
				CommandsEnable(false);
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				// Instances of javax.swing.SwingWorker are not reusuable, so
				// we create new instances as needed.
				Task currentTask = new Task();
				progressTask = currentTask;
				currentTask.addPropertyChangeListener(
						(PropertyChangeListener) this);
				isRunning = true;
				currentTask.execute();
				break;
			case "exit":
				DeleteResource(tmpFolderPath);
				System.exit(0);
				break;
			case "stop":
				isRunning = false;
				progressTask.cancel(false);
				break;
			case "save":
				SaveConfiguration();
				break;
			case "folder":
				if (evt.getSource() == selectInputFolderBtn) {
					if (fileChooser.showOpenDialog(
							mainFrame) == JFileChooser.APPROVE_OPTION) {
						folderInput = fileChooser.getSelectedFile();
						folderOutput = folderInput;
						folderInputText.setText(folderInput.toString());
						startBtn.setEnabled(true);
						selectOutputFolderBtn.setEnabled(true);
					} else {
						addToLog("Source folder not selected.", false);
					}
				}
				if (evt.getSource() == selectOutputFolderBtn) {
					if (fileChooser.showOpenDialog(
							mainFrame) == JFileChooser.APPROVE_OPTION) {
						folderOutput = fileChooser.getSelectedFile();
						folderOutputText.setText(folderOutput.toString());
					} else {
						addToLog("Target folder not selected: overwriting files in the source folder.", false);
					}
				}
				break;
			case "colorProfile":
				if (evt.getSource() == colorProfileChckbx) {
					ColorProfileDataEnable(colorProfileChckbx.isSelected());
				}
				break;
			case "watermark":
				if (evt.getSource() == watermarkChckbx) {
					WatermarkDataEnable(watermarkChckbx.isSelected());
				}
				break;
			case "frame":
				if (evt.getSource() == frameChckbx) {
					FrameDataEnable(frameChckbx.isSelected());
				}
				break;
			case "resize":
				if (evt.getSource() == resizeChckbx) {
					ResizeDataEnable(resizeChckbx.isSelected());
				}
				break;
			case "fullhd":
				if (evt.getSource() == resizeFullHDChckbx) {
					ResizeCustomEnable(!resizeFullHDChckbx.isSelected(), "ultrahd");
				}
				break;
			case "ultrahd":
				if (evt.getSource() == resizeUltraHDChckbx) {
					ResizeCustomEnable(!resizeUltraHDChckbx.isSelected(), "fullhd");
				}
				break;
			default :
				addToLog("Action not recognized.");
				break;

		}
	}

	public String ExportResource(String resourceName) throws Exception {
		if (tmpFolderPath.isEmpty() || tmpFolderPath == null)
			return null;
		InputStream stream = null;
		OutputStream resStreamOut = null;

		String jarFolderResource = new File(tmpFolderPath).getPath();
		jarFolderResource += File.separator + (resourceName.startsWith("/")
				? resourceName.substring(1)
				: resourceName);
		jarFolderResource = jarFolderResource.replace('\\', File.separatorChar)
				.replace('/', File.separatorChar);

		try {
			stream = PictureTravelSuiteClass.class
					.getResourceAsStream(resourceName);
			if (stream == null)
				throw new Exception("Impossibile ottenere la risorsa \""
						+ resourceName + "\" dal Jar.");

			int readBytes;
			byte[] buffer = new byte[4096];

			Path pathToFile = Paths.get(jarFolderResource);
			Files.createDirectories(pathToFile.getParent());
			// Files.createFile(pathToFile);

			if (new File(jarFolderResource).exists())
				return jarFolderResource;

			resStreamOut = new FileOutputStream(jarFolderResource);
			while ((readBytes = stream.read(buffer)) > 0) {
				resStreamOut.write(buffer, 0, readBytes);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (!new File(jarFolderResource).exists()) {
				stream.close();
				resStreamOut.close();
			}
		}
		return jarFolderResource;
	}
	public boolean DeleteResource(String pathStr) {
		return DeleteResource(new File(pathStr));
	}
	public boolean DeleteResource(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					DeleteResource(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			progressBarProcess.setValue(progress);
		}
	}

	public class ExtensionsFilter implements FileFilter {
		private char[][] extensions;

		private ExtensionsFilter(String[] extensions) {
			int length = extensions.length;
			this.extensions = new char[length][];
			for (String s : extensions) {
				this.extensions[--length] = s.toCharArray();
			}
		}

		@Override
		public boolean accept(File file) {
			char[] path = file.getPath().toCharArray();
			for (char[] extension : extensions) {
				if (extension.length > path.length) {
					continue;
				}
				int pStart = path.length - 1;
				int eStart = extension.length - 1;
				boolean success = true;
				for (int i = 0; i <= eStart; i++) {
					if ((path[pStart - i] | 0x20) != (extension[eStart - i]
							| 0x20)) {
						success = false;
						break;
					}
				}
				if (success)
					return true;
			}
			return false;
		}
	}

	class RunnableActions implements Runnable {
		private Thread t;
		List<File> filesList;
		private String[] filesToProcess;
		private String threadName;
		private int numFilesProcessed = 0;

		RunnableActions(List<File> files) {
			filesList = files;
			filesToProcess = FilesToString(filesList);
		}
		public void run() {
			addToLog(t.getName() + " - Queued " + filesToProcess.length + " files", Color.GREEN, false);
			try {
				for (int iter = 0; (iter < filesToProcess.length
						&& isRunning); iter++) {
					addToLog(t.getName() + " - Work on " + filesToProcess[iter] + " started.", false);

					boolean allOk = true;

					Point offsetCorner = new Point();
					if (colorProfileChckbx.isSelected())
						ColorProfileCommand(filesToProcess[iter]);
					if (frameChckbx.isSelected())
						offsetCorner = FrameCommand(filesToProcess[iter]);
					if (watermarkChckbx.isSelected())
						WatermarkCommand(filesToProcess[iter], offsetCorner);
					if (resizeChckbx.isSelected())
						ResizeCommand(filesToProcess[iter]);
					if (cleanExifChckbx.isSelected())
						CleanExifCommand(filesToProcess[iter]);
					if (commentChckbx.isSelected())
						UpdateCommentExif(filesToProcess[iter]);
					if (copyrightChckbx.isSelected())
						UpdateCopyrightExif(filesToProcess[iter]);
					AddSoftwareEditor(filesToProcess[iter]);

					addToLog("Working on " + filesToProcess[iter] + " finished.", false);
					numFilesProcessed = iter + 1;
					if (!allOk) {
						throw new InterruptedException();
					}
				}
				if (!isRunning)
					throw new InterruptedException();
			} catch (InterruptedException e) {
				addToLog(t.getName() + " interrupted.", Color.RED);
				t.interrupt();
			}
			if (isRunning)
				addToLog(t.getName() + " processed " + numFilesProcessed + " files.", Color.GREEN, false);
			else
				addToLog(t.getName() + " interrupted by the user.", Color.GREEN);
		}

		public int processedFiles() {
			return numFilesProcessed;
		}

		public boolean isAlive() {
			return ((t != null) && t.isAlive());
		}

		public void setName(String name) {
			threadName = name;
			if (t != null)
				t.setName(name);
		}

		public void start() {
			if (t == null) {
				t = new Thread(this, threadName);
				t.start();
				addToLog(t.getName() + " started!", Color.GREEN, false);
			}
		}
	}
}