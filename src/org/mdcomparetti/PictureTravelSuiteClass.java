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
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import javax.swing.JFrame;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.Box;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JProgressBar;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.text.*;
import javax.swing.JLabel;

public class PictureTravelSuiteClass extends JPanel implements ActionListener, PropertyChangeListener {

	private static final long serialVersionUID = 2L;
	private static final long serialSubVersionUID = 0L;
	private static final String softwareAuthor = "Mirko D. Comparetti";
	private static final String softwareAuthorShort = "MDC";
	private static final String copyrightSymbol = "©";
	private static final String softwareNameShort = "PictureTravelSuite";
	private static final String softwareName = "Picture Travel Suite";
	private static final String softwareInfo = "MDC - " + softwareName + " " + serialVersionUID + "."
			+ serialSubVersionUID;
	private static final String softwareInfoLabel = copyrightSymbol + " " + softwareAuthor + " - - - " + softwareName
			+ " " + serialVersionUID + "." + serialSubVersionUID;
	private static final String softwareInfoTitle = softwareAuthorShort + " - " + softwareName;
	private static final String[] configurationBoolean = { "true", "false" };
	private static final int numProcessors = Runtime.getRuntime().availableProcessors();
	private JFrame mainFrame;
	/**
	 * @wbp.nonvisual location=12,-6
	 */
	private final JFileChooser picture_fileChooser = new JFileChooser();
	private final JFileChooser travel_fileChooser = new JFileChooser();

	private JTabbedPane mainTab;

	private JPanel picturePanel;
	private JPanel travelPanel;

	private JPanel picture_controlsPanel;
	private JTextField picture_photographerText;

	private JPanel picture_foldersPanel;
	private File picture_folderInput;
	private JButton picture_selectInputFolderBtn;
	private JTextField picture_folderInputText;
	private File picture_folderOutput;
	private JButton picture_selectOutputFolderBtn;
	private JTextField picture_folderOutputText;

	Map<String, File> cmdExecutables;

	private JPanel picture_commandsPanel;
	private JPanel picture_watermarkPanel;
	private JCheckBox picture_watermarkChckbx;
	private JComboBox<Float> picture_watermarkSize;
	private JComboBox<String> picture_watermarkPosition;
	private JComboBox<String> picture_watermarkFont;
	private List<String> picture_watermarkFontArray;

	private JPanel picture_resizePanel;
	private JCheckBox picture_resizeChckbx;
	private DefaultComboBoxModel<String> picture_resizeValueModel;
	private JComboBox<String> picture_resizeValue;
	private JComboBox<String> picture_resizeEdge;
	private JCheckBox picture_resizeFullHDChckbx;
	private JCheckBox picture_resizeUltraHDChckbx;

	private JPanel picture_framePanel;
	private JCheckBox picture_frameChckbx;
	private JComboBox<String> picture_frameColor;
	private JComboBox<String> picture_frameType;
	private JComboBox<Float> picture_frameSize;
	private JComboBox<Float> picture_frameThickness;

	private JPanel picture_colorProfilePanel;
	private JCheckBox picture_colorProfileChckbx;
	private JComboBox<String> picture_colorProfileFile;
	private JComboBox<String> picture_colorProfileIntent;
	private List<String> picture_colorProfileFileArray;

	private JPanel picture_miscPanel;
	private JCheckBox picture_copyrightChckbx;
	private JCheckBox picture_commentChckbx;
	private JCheckBox picture_cleanExifChckbx;

	private JPanel travel_filesPanel;
	private File travel_fileInput;
	private JButton travel_selectInputFileBtn;
	private JTextField travel_fileInputText;
	private File travel_fileOutput;
	private JButton travel_selectOutputFileBtn;
	private JTextField travel_fileOutputText;

	private JPanel travel_commandsPanel;

	private JPanel travel_mergePanel;
	private JCheckBox travel_mergeChckbx;

	private JPanel travel_filterPanel;
	private JCheckBox travel_filterChckbx;
	private DefaultComboBoxModel<String> travel_filterValueModel;
	private JComboBox<String> travel_filterValue;

	private JPanel travel_simplifyPanel;
	private JCheckBox travel_simplifyChckbx;
	private DefaultComboBoxModel<String> travel_simplifyValueModel;
	private JComboBox<String> travel_simplifyValue;
	private JCheckBox travel_simplifyGmapsChckbx;

	private JPanel travel_timePanel;
	private JCheckBox travel_timeChckbx;

	private Dimension singleObjectDimension = new Dimension(125, 40);
	private Dimension singleSpacerDimension = new Dimension(10, 10);

	private JPanel progressPanel;
	private JButton startBtn;
	private JButton cancelBtn;
	private JButton exitBtn;
	private JButton saveBtn;
	private JProgressBar progressBarProcess;

	private JPanel bottomPanel;
	private JLabel infoLbl;
	private JLabel errorMsg;

	private File configFile;
	private FileInputStream configReader;
	private FileOutputStream configWriter;
	private Properties configPropsDefault;
	private Properties configProps;
	private boolean isRunning;

	private Task progressTask;

	private DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	private String tmpFolderPath;

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

	public PictureTravelSuiteClass() {
		initialize();
		tmpFolderPath = System.getProperty("java.io.tmpdir");
		tmpFolderPath += softwareNameShort + "Tmp";
		GetConfigProps();
		picture_WatermarkDataEnable(
				configProps.getProperty("picture_watermark").compareTo(configurationBoolean[0]) == 0);
		picture_ResizeDataEnable(configProps.getProperty("picture_resize").compareTo(configurationBoolean[0]) == 0);
		picture_FrameDataEnable(configProps.getProperty("picture_frame").compareTo(configurationBoolean[0]) == 0);
		picture_ColorProfileDataEnable(
				configProps.getProperty("picture_cprofile").compareTo(configurationBoolean[0]) == 0);

		travel_FilterDataEnable(configProps.getProperty("travel_filter").compareTo(configurationBoolean[0]) == 0);
		travel_SimplifyDataEnable(configProps.getProperty("travel_simplify").compareTo(configurationBoolean[0]) == 0);

		if (!cmdExecutables.containsKey("exiftool")) {
			System.out.println("ERROR: exiftool not found");
			addToLog("ERROR: exiftool not found", Color.RED);
			disablePicture();
			OnlyExit();
		}
		if (!cmdExecutables.containsKey("convert")) {
			System.out.println("ERROR: ImageMagick not found");
			addToLog("ERROR: ImageMagick not found", Color.RED);
			disablePicture();
			OnlyExit();
		}
		if (!cmdExecutables.containsKey("gpsbabel")) {
			System.out.println("ERROR: GPSbabel not found");
			addToLog("ERROR: GPSbabel not found", Color.RED);
			disableTravel();
			OnlyExit();
		}
	}

	private void OnlyExit() {
		saveBtn.setEnabled(false);
		exitBtn.setEnabled(true);
		startBtn.setEnabled(false);
		cancelBtn.setEnabled(false);
	}

	private void disablePicture() {
		picturePanel.setEnabled(false);
		picture_selectInputFolderBtn.setEnabled(false);
		picture_selectOutputFolderBtn.setEnabled(false);
		picture_watermarkChckbx.setEnabled(false);
		picture_resizeChckbx.setEnabled(false);
		picture_frameChckbx.setEnabled(false);
		picture_colorProfileChckbx.setEnabled(false);
		picture_copyrightChckbx.setEnabled(false);
		picture_commentChckbx.setEnabled(false);
		picture_cleanExifChckbx.setEnabled(false);

		picture_WatermarkDataEnable(false);
		picture_ResizeDataEnable(false);
		picture_FrameDataEnable(false);
		picture_ColorProfileDataEnable(false);
	}

	private void disableTravel() {
		travelPanel.setEnabled(false);
		travel_selectInputFileBtn.setEnabled(false);
		travel_selectOutputFileBtn.setEnabled(false);
		travel_mergeChckbx.setEnabled(false);
		travel_filterChckbx.setEnabled(false);
		travel_simplifyChckbx.setEnabled(false);
		travel_timeChckbx.setEnabled(false);
	}

	private void initialize() {
		cmdExecutables = new HashMap<String, File>();
		configFile = new File("PictureTravelSuite.cfg");
		configPropsDefault = new Properties();
		ResetConfigProps(true);
		configProps = new Properties(configPropsDefault);
		LoadConfiguration();

		picture_folderInput = null;
		picture_folderOutput = null;
		travel_fileInput = null;
		travel_fileOutput = null;
		this.isRunning = false;

		mainFrame = new JFrame();
		mainFrame.setIconImage(Toolkit.getDefaultToolkit()
				.getImage(getClass().getResource("/org/mdcomparetti/resources/icona_tricolore.png")));
		mainFrame.setTitle(softwareInfoTitle);
		mainFrame.addWindowListener(new WindowEventHandler());
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainFrame.getContentPane().setLayout(null);
		mainFrame.setResizable(false);
		mainFrame.setLocationByPlatform(true);
		mainFrame.setLocation(0, 0);
		mainFrame.setBounds(0, 0, (int) (5 * singleObjectDimension.getWidth() + 8 * singleSpacerDimension.getWidth()),
				(int) (8 * singleObjectDimension.getHeight() + 11 * singleSpacerDimension.getHeight()));

		mainTab = new JTabbedPane();
		mainTab.setVisible(true);
		mainTab.setLocation(0, 0);
		mainTab.setBounds(0, 0, (int) (mainFrame.getWidth()), (int) (mainFrame.getHeight()));

		picturePanel = new JPanel();
		picturePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		picturePanel.setLocation(0, 0);
		picturePanel.setBounds(0, 0,
				(int) (5 * singleObjectDimension.getWidth() + 6 * singleSpacerDimension.getWidth()),
				(int) (8 * singleObjectDimension.getHeight() + 8 * singleSpacerDimension.getHeight()));
		picturePanel.setLayout(null);
		mainFrame.getContentPane().add(picturePanel);

		travelPanel = new JPanel();
		travelPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		travelPanel.setLocation(0, 0);
		travelPanel.setBounds(0, 0, (int) (picturePanel.getWidth()), (int) (picturePanel.getHeight()));
		travelPanel.setLayout(null);

		Action picture_details = picture_fileChooser.getActionMap().get("viewTypeDetails");
		picture_details.actionPerformed(null);
		picture_fileChooser.setAcceptAllFileFilterUsed(false);
		picture_fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		picture_fileChooser.setCurrentDirectory(new java.io.File("."));
		picture_fileChooser.setDialogTitle("Select files and folders");

		picture_foldersPanel = new JPanel();
		picture_foldersPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		picture_foldersPanel.setBounds((int) singleSpacerDimension.getWidth(), (int) singleSpacerDimension.getHeight(),
				(int) (5 * (singleObjectDimension.getWidth() + singleSpacerDimension.getWidth())
						- singleSpacerDimension.getWidth()),
				(int) (2 * singleObjectDimension.getHeight() + singleSpacerDimension.getHeight()));
		picturePanel.add(picture_foldersPanel);
		picture_foldersPanel.setLayout(null);

		picture_folderInputText = new JTextField();
		picture_folderInputText.setLocation(0, 0);
		picture_folderInputText.setToolTipText("Show the selected source folder");
		picture_folderInputText.setColumns(26);
		picture_folderInputText.setEditable(false);
		picture_folderInputText.setText("Source folder not selected");

		picture_selectInputFolderBtn = new JButton("Select");
		picture_selectInputFolderBtn.setActionCommand("folder");
		picture_selectInputFolderBtn.addActionListener(this);
		picture_selectInputFolderBtn
				.setLocation((int) (picture_foldersPanel.getSize().getWidth() - singleObjectDimension.getWidth()), 0);

		picture_folderOutputText = new JTextField();
		picture_folderOutputText.setLocation(0,
				(int) (singleObjectDimension.getHeight() + singleSpacerDimension.getHeight()));
		picture_folderOutputText.setToolTipText("Show the selected target folder");
		picture_folderOutputText.setText("Target folder not selected, using source folder");
		picture_folderOutputText.setEditable(false);
		picture_folderOutputText.setColumns(26);

		picture_selectOutputFolderBtn = new JButton("Select");
		picture_selectOutputFolderBtn.setActionCommand("folder");
		picture_selectOutputFolderBtn.addActionListener(this);
		picture_selectOutputFolderBtn.setLocation(
				(int) (picture_foldersPanel.getSize().getWidth() - singleObjectDimension.getWidth()),
				(int) (singleObjectDimension.getHeight() + singleSpacerDimension.getHeight()));
		picture_selectOutputFolderBtn.setEnabled(false);

		picture_folderInputText.setSize(
				(int) (4 * singleObjectDimension.getWidth() + 3 * singleSpacerDimension.getWidth()),
				(int) singleObjectDimension.getHeight());
		picture_folderInputText.setMaximumSize(
				new Dimension((int) (4 * singleObjectDimension.getWidth() + 3 * singleSpacerDimension.getWidth()),
						(int) singleObjectDimension.getHeight()));
		picture_selectInputFolderBtn.setSize(singleObjectDimension);
		picture_selectInputFolderBtn.setMaximumSize(singleObjectDimension);
		picture_folderOutputText.setSize(
				(int) (4 * singleObjectDimension.getWidth() + 3 * singleSpacerDimension.getWidth()),
				(int) singleObjectDimension.getHeight());
		picture_folderOutputText.setMaximumSize(
				new Dimension((int) (4 * singleObjectDimension.getWidth() + 3 * singleSpacerDimension.getWidth()),
						(int) singleObjectDimension.getHeight()));
		picture_selectOutputFolderBtn.setSize(singleObjectDimension);
		picture_selectOutputFolderBtn.setMaximumSize(singleObjectDimension);

		picture_foldersPanel.add(picture_folderInputText);
		picture_foldersPanel.add(picture_selectInputFolderBtn);
		picture_foldersPanel.add(picture_folderOutputText);
		picture_foldersPanel.add(picture_selectOutputFolderBtn);

		picture_controlsPanel = new JPanel();
		picture_controlsPanel.setBounds((int) (picture_foldersPanel.getLocation().x),
				(int) (picture_foldersPanel.getLocation().y + picture_foldersPanel.getSize().getHeight()
						+ singleSpacerDimension.getHeight()),
				(int) (5 * (singleObjectDimension.getWidth() + singleSpacerDimension.getWidth())
						- singleSpacerDimension.getWidth()),
				(int) (singleObjectDimension.getHeight()));
		picturePanel.add(picture_controlsPanel);
		picture_controlsPanel.setLayout(null);

		picture_photographerText = new JTextField();
		picture_photographerText.setColumns(20);

		picture_photographerText.setSize(
				(int) (5 * singleObjectDimension.getWidth() + 4 * singleSpacerDimension.getWidth()),
				(int) singleObjectDimension.getHeight());
		picture_photographerText.setMaximumSize(
				new Dimension((int) (5 * singleObjectDimension.getWidth() + 4 * singleSpacerDimension.getWidth()),
						(int) singleObjectDimension.getHeight()));
		picture_photographerText.setLocation(0, 0);
		picture_controlsPanel.add(picture_photographerText);

		picture_commandsPanel = new JPanel();
		picture_commandsPanel.setBounds((int) (picture_controlsPanel.getLocation().x),
				(int) (picture_controlsPanel.getLocation().y + picture_controlsPanel.getSize().getHeight()
						+ singleSpacerDimension.getHeight()),
				(int) (5 * singleObjectDimension.getWidth() + 4 * singleSpacerDimension.getWidth()),
				(int) (5 * singleObjectDimension.getHeight() + 4 * singleSpacerDimension.getHeight()));
		picturePanel.add(picture_commandsPanel);
		picture_commandsPanel.setLayout(null);

		picture_miscPanel = new JPanel();
		picture_miscPanel.setLocation(0, 0);
		picture_miscPanel.setSize(new Dimension((int) singleObjectDimension.getWidth(),
				(int) (3 * (singleObjectDimension.getHeight() + singleSpacerDimension.getHeight())
						- singleSpacerDimension.getHeight())));
		picture_miscPanel.setMaximumSize(new Dimension((int) singleObjectDimension.getWidth(),
				(int) (3 * (singleObjectDimension.getHeight() + singleSpacerDimension.getHeight())
						- singleSpacerDimension.getHeight())));
		picture_miscPanel.setLayout(null);

		picture_commentChckbx = new JCheckBox("Info");
		picture_commentChckbx.setToolTipText("Adds info about the file in the EXIF comments");
		picture_commentChckbx.setLocation(0,
				(int) (singleObjectDimension.getHeight() + singleSpacerDimension.getHeight()));
		picture_commentChckbx.setSize(singleObjectDimension);
		picture_commentChckbx.setMaximumSize(singleObjectDimension);
		picture_miscPanel.add(picture_commentChckbx);

		picture_copyrightChckbx = new JCheckBox("Copyright");
		picture_copyrightChckbx.setToolTipText("Adds copyright info on the file");
		picture_copyrightChckbx.setLocation(0, 0);
		picture_copyrightChckbx.setSize(singleObjectDimension);
		picture_copyrightChckbx.setMaximumSize(singleObjectDimension);
		picture_miscPanel.add(picture_copyrightChckbx);

		picture_cleanExifChckbx = new JCheckBox("No EXIF");
		picture_cleanExifChckbx.setToolTipText("Cleans the EXIF information: photoshop, software");
		picture_cleanExifChckbx.setLocation(0,
				(int) (2 * (singleObjectDimension.getHeight() + singleSpacerDimension.getHeight())));
		picture_cleanExifChckbx.setSize(singleObjectDimension);
		picture_cleanExifChckbx.setMaximumSize(singleObjectDimension);
		picture_miscPanel.add(picture_cleanExifChckbx);

		picture_colorProfilePanel = new JPanel();
		picture_colorProfilePanel
				.setLocation((int) (singleObjectDimension.getWidth() + singleSpacerDimension.getWidth()), 0);
		picture_colorProfilePanel.setSize(new Dimension((int) singleObjectDimension.getWidth(),
				(int) (3 * singleObjectDimension.getHeight() + 2 * singleSpacerDimension.getHeight())));
		picture_colorProfilePanel.setMaximumSize(new Dimension((int) singleObjectDimension.getWidth(),
				(int) (3 * singleObjectDimension.getHeight() + 2 * singleSpacerDimension.getHeight())));
		picture_colorProfilePanel.setLayout(null);

		picture_colorProfileChckbx = new JCheckBox("Profile");
		picture_colorProfileChckbx.setToolTipText("Changes the color profile for the pictures");
		picture_colorProfileChckbx.setActionCommand("picture_colorProfile");
		picture_colorProfileChckbx.addActionListener(this);

		picture_colorProfileFile = new JComboBox<String>();
		picture_colorProfileFileArray = new ArrayList<String>();
		picture_colorProfileFileArray.add("/org/mdcomparetti/resources/colorprofiles/srgb.icc");
		picture_colorProfileFileArray.add("/org/mdcomparetti/resources/colorprofiles/AdobeRGB1998.icc");
		picture_colorProfileFileArray.add("/org/mdcomparetti/resources/colorprofiles/MelissaRGB.icc");
		picture_colorProfileFileArray.add("/org/mdcomparetti/resources/colorprofiles/ProPhoto.icc");
		picture_colorProfileFileArray.add("/org/mdcomparetti/resources/colorprofiles/WideGamut.icc");
		Collections.sort(picture_colorProfileFileArray);
		for (int iter = 0; iter < picture_colorProfileFileArray.size(); iter++) {
			String tmp = picture_colorProfileFileArray.get(iter);
			tmp = tmp.substring(0, (tmp.lastIndexOf(".") != -1) ? tmp.lastIndexOf(".") : tmp.length());
			tmp = tmp.substring((tmp.lastIndexOf("/") != -1) ? tmp.lastIndexOf("/") + 1 : 0, tmp.length());
			tmp = tmp.substring((tmp.lastIndexOf("\\") != -1) ? tmp.lastIndexOf("\\") + 1 : 0, tmp.length());
			picture_colorProfileFile.addItem(tmp);
		}
		picture_colorProfileFile.setEnabled(false);
		picture_colorProfileIntent = new JComboBox<String>();
		picture_colorProfileIntent.addItem("Absolute");
		picture_colorProfileIntent.addItem("Perceptual");
		picture_colorProfileIntent.addItem("Relative");
		picture_colorProfileIntent.addItem("Saturation");
		picture_colorProfileIntent.setEnabled(false);

		picture_colorProfileChckbx.setSize(singleObjectDimension);
		picture_colorProfileFile.setSize(singleObjectDimension);
		picture_colorProfileIntent.setSize(singleObjectDimension);
		picture_colorProfileChckbx.setMaximumSize(singleObjectDimension);
		picture_colorProfileFile.setMaximumSize(singleObjectDimension);
		picture_colorProfileIntent.setMaximumSize(singleObjectDimension);
		picture_colorProfileChckbx.setLocation(0, 0);
		picture_colorProfileFile.setLocation(0,
				(int) (singleObjectDimension.getHeight() + singleSpacerDimension.getHeight()));
		picture_colorProfileIntent.setLocation(0,
				(int) (2 * (singleObjectDimension.getHeight() + singleSpacerDimension.getHeight())));
		picture_colorProfilePanel.add(picture_colorProfileChckbx);
		picture_colorProfilePanel.add(picture_colorProfileFile);
		picture_colorProfilePanel.add(picture_colorProfileIntent);

		picture_framePanel = new JPanel();
		picture_framePanel
				.setLocation((int) (2 * (singleObjectDimension.getWidth() + singleSpacerDimension.getWidth())), 0);
		picture_framePanel.setSize(new Dimension((int) singleObjectDimension.getWidth(),
				(int) (5 * singleObjectDimension.getHeight() + 4 * singleSpacerDimension.getHeight())));
		picture_framePanel.setMaximumSize(new Dimension((int) singleObjectDimension.getWidth(),
				(int) (5 * (singleObjectDimension.getHeight() + singleSpacerDimension.getHeight()))));
		picture_framePanel.setLayout(null);

		picture_frameChckbx = new JCheckBox("Frame");
		picture_frameChckbx.setToolTipText("Adds a frame to the pictures");
		picture_frameChckbx.setActionCommand("picture_frame");
		picture_frameChckbx.addActionListener(this);

		picture_frameColor = new JComboBox<String>();
		picture_frameColor.addItem("white");
		picture_frameColor.addItem("black");
		picture_frameColor.addItem("red");
		picture_frameColor.addItem("green");
		picture_frameColor.addItem("blue");

		picture_frameType = new JComboBox<String>();
		picture_frameType.addItem("Plain");
		picture_frameType.addItem("Double");
		picture_frameType.addItem("Inner");
		picture_frameType.addItem("Rounded");
		picture_frameType.addItem("Outer");
		picture_frameType.addItem("Blur");
		picture_frameType.addItem("Italy");

		picture_frameSize = new JComboBox<Float>();
		picture_frameSize.addItem((float) 0.25);
		picture_frameSize.addItem((float) 0.50);
		picture_frameSize.addItem((float) 1.00);
		picture_frameSize.addItem((float) 1.25);
		picture_frameSize.addItem((float) 1.50);
		picture_frameSize.addItem((float) 1.75);
		picture_frameSize.addItem((float) 2.00);
		picture_frameSize.addItem((float) 2.25);
		picture_frameSize.addItem((float) 2.50);
		picture_frameSize.addItem((float) 2.75);
		picture_frameSize.addItem((float) 3.00);
		picture_frameSize.addItem((float) 3.25);
		picture_frameSize.addItem((float) 3.50);
		picture_frameSize.addItem((float) 3.75);
		picture_frameSize.addItem((float) 4.00);
		picture_frameSize.addItem((float) 4.25);
		picture_frameSize.addItem((float) 4.50);
		picture_frameSize.addItem((float) 4.75);
		picture_frameSize.addItem((float) 5.00);

		picture_frameThickness = new JComboBox<Float>();
		picture_frameThickness.addItem((float) 0.125);
		picture_frameThickness.addItem((float) 0.25);
		picture_frameThickness.addItem((float) 0.375);
		picture_frameThickness.addItem((float) 0.5);
		picture_frameThickness.addItem((float) 0.625);
		picture_frameThickness.addItem((float) 0.75);
		picture_frameThickness.addItem((float) 0.825);
		picture_frameThickness.addItem((float) 1.0);
		picture_frameThickness.addItem((float) 1.125);
		picture_frameThickness.addItem((float) 1.25);
		picture_frameThickness.addItem((float) 1.375);
		picture_frameThickness.addItem((float) 1.5);
		picture_frameThickness.addItem((float) 1.625);
		picture_frameThickness.addItem((float) 1.75);
		picture_frameThickness.addItem((float) 1.825);
		picture_frameThickness.addItem((float) 2.0);

		picture_frameChckbx.setSize(singleObjectDimension);
		picture_frameChckbx.setMaximumSize(singleObjectDimension);
		picture_frameChckbx.setLocation(0, 0);
		picture_frameType.setSize(singleObjectDimension);
		picture_frameType.setMaximumSize(singleObjectDimension);
		picture_frameType.setLocation(0,
				(int) (1 * (singleObjectDimension.getHeight() + singleSpacerDimension.getHeight())));

		picture_frameColor.setSize(singleObjectDimension);
		picture_frameColor.setMaximumSize(singleObjectDimension);
		picture_frameColor.setLocation(0,
				(int) (2 * (singleObjectDimension.getHeight() + singleSpacerDimension.getHeight())));
		picture_frameSize.setSize(singleObjectDimension);
		picture_frameSize.setMaximumSize(singleObjectDimension);
		picture_frameSize.setLocation(0,
				(int) (3 * (singleObjectDimension.getHeight() + singleSpacerDimension.getHeight())));
		picture_frameThickness.setSize(singleObjectDimension);
		picture_frameThickness.setMaximumSize(singleObjectDimension);
		picture_frameThickness.setLocation(0,
				(int) (4 * (singleObjectDimension.getHeight() + singleSpacerDimension.getHeight())));
		picture_framePanel.add(picture_frameChckbx);
		picture_framePanel.add(picture_frameColor);
		picture_framePanel.add(picture_frameType);
		picture_framePanel.add(picture_frameSize);
		picture_framePanel.add(picture_frameThickness);

		picture_watermarkPanel = new JPanel();
		picture_watermarkPanel
				.setLocation((int) (3 * (singleObjectDimension.getWidth() + singleSpacerDimension.getWidth())), 0);
		picture_watermarkPanel.setSize((int) singleObjectDimension.getWidth(),
				(int) (4 * singleObjectDimension.getHeight() + 3 * singleSpacerDimension.getHeight()));
		picture_watermarkPanel.setMaximumSize(new Dimension((int) singleObjectDimension.getWidth(),
				(int) (4 * (singleObjectDimension.getHeight() + singleSpacerDimension.getHeight()))));
		picture_watermarkPanel.setLayout(null);

		picture_watermarkChckbx = new JCheckBox("Watermark");
		picture_watermarkChckbx.setToolTipText("Add watermark to the pictures");
		picture_watermarkChckbx.setActionCommand("picture_watermark");
		picture_watermarkChckbx.addActionListener(this);

		picture_watermarkSize = new JComboBox<Float>();
		picture_watermarkSize.addItem((float) 0.5);
		picture_watermarkSize.addItem((float) 1);
		picture_watermarkSize.addItem((float) 1.5);
		picture_watermarkSize.addItem((float) 2);
		picture_watermarkSize.addItem((float) 2.5);
		picture_watermarkSize.addItem((float) 3);
		picture_watermarkSize.addItem((float) 3.5);
		picture_watermarkSize.addItem((float) 4);
		picture_watermarkSize.addItem((float) 4.5);
		picture_watermarkSize.addItem((float) 5);

		picture_watermarkPosition = new JComboBox<String>();
		picture_watermarkPosition.addItem("center");
		picture_watermarkPosition.addItem("southeast");
		picture_watermarkPosition.addItem("east");
		picture_watermarkPosition.addItem("northeast");
		picture_watermarkPosition.addItem("north");
		picture_watermarkPosition.addItem("northwest");
		picture_watermarkPosition.addItem("west");
		picture_watermarkPosition.addItem("southwest");

		picture_watermarkFont = new JComboBox<String>();
		picture_watermarkFontArray = new ArrayList<String>();
		picture_watermarkFontArray.add("/org/mdcomparetti/resources/fonts/FFXMarquee.ttf");
		picture_watermarkFontArray.add("/org/mdcomparetti/resources/fonts/WalkwayCondensed.ttf");
		picture_watermarkFontArray.add("/org/mdcomparetti/resources/fonts/Existence-Light.otf");
		Collections.sort(picture_watermarkFontArray);
		for (int iter = 0; iter < picture_watermarkFontArray.size(); iter++) {
			String tmp = picture_watermarkFontArray.get(iter);
			tmp = tmp.substring(0, (tmp.lastIndexOf(".") != -1) ? tmp.lastIndexOf(".") : tmp.length());
			tmp = tmp.substring((tmp.lastIndexOf("/") != -1) ? tmp.lastIndexOf("/") + 1 : 0, tmp.length());
			tmp = tmp.substring((tmp.lastIndexOf("\\") != -1) ? tmp.lastIndexOf("\\") + 1 : 0, tmp.length());
			picture_watermarkFont.addItem(tmp);
		}
		picture_watermarkFont.setEnabled(false);

		picture_watermarkChckbx.setSize(singleObjectDimension);
		picture_watermarkChckbx.setMaximumSize(singleObjectDimension);
		picture_watermarkChckbx.setLocation(0, 0);
		picture_watermarkPosition.setSize(singleObjectDimension);
		picture_watermarkPosition.setMaximumSize(singleObjectDimension);
		picture_watermarkPosition.setLocation(0,
				(int) (1 * (singleObjectDimension.getHeight() + singleSpacerDimension.getHeight())));
		picture_watermarkSize.setSize(singleObjectDimension);
		picture_watermarkSize.setMaximumSize(singleObjectDimension);
		picture_watermarkSize.setLocation(0,
				(int) (2 * (singleObjectDimension.getHeight() + singleSpacerDimension.getHeight())));
		picture_watermarkFont.setSize(singleObjectDimension);
		picture_watermarkFont.setMaximumSize(singleObjectDimension);
		picture_watermarkFont.setLocation(0,
				(int) (3 * (singleObjectDimension.getHeight() + singleSpacerDimension.getHeight())));
		picture_watermarkPanel.add(picture_watermarkChckbx);
		picture_watermarkPanel.add(picture_watermarkSize);
		picture_watermarkPanel.add(picture_watermarkPosition);
		picture_watermarkPanel.add(picture_watermarkFont);

		picture_resizePanel = new JPanel();
		picture_resizePanel
				.setLocation((int) (4 * (singleObjectDimension.getWidth() + singleSpacerDimension.getWidth())), 0);
		picture_resizePanel.setSize(new Dimension((int) singleObjectDimension.getWidth(),
				(int) (5 * singleObjectDimension.getHeight() + 4 * singleSpacerDimension.getHeight())));
		picture_resizePanel.setMaximumSize(new Dimension((int) singleObjectDimension.getWidth(),
				(int) (5 * (singleObjectDimension.getHeight() + singleSpacerDimension.getHeight()))));
		picture_resizePanel.setLayout(null);

		picture_resizeChckbx = new JCheckBox("Resize");
		picture_resizeChckbx.setToolTipText("Resize pictures");
		picture_resizeChckbx.setActionCommand("picture_resize");
		picture_resizeChckbx.addActionListener(this);

		picture_resizeValueModel = new DefaultComboBoxModel<String>(new String[] { "1200", "1600", "2000", "2400" });
		picture_resizeValue = new JComboBox<String>(picture_resizeValueModel);
		picture_resizeValue.setEditable(true);
		picture_resizeValue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ("comboBoxEdited".equals(e.getActionCommand())) {
					Object newValue = picture_resizeValueModel.getSelectedItem();
					picture_resizeValueModel.addElement((String) newValue);
					picture_resizeValue.setSelectedItem(newValue);
				}
			}
		});

		picture_resizeEdge = new JComboBox<String>();
		picture_resizeEdge.addItem("Long");
		picture_resizeEdge.addItem("Short");

		picture_resizeFullHDChckbx = new JCheckBox("Full HD");
		picture_resizeFullHDChckbx.setToolTipText("FullHD size");
		picture_resizeFullHDChckbx.setActionCommand("picture_fullhd");
		picture_resizeFullHDChckbx.addActionListener(this);
		picture_resizeUltraHDChckbx = new JCheckBox("Ultra HD");
		picture_resizeUltraHDChckbx.setToolTipText("Ultra HD 4k size");
		picture_resizeUltraHDChckbx.setActionCommand("picture_ultrahd");
		picture_resizeUltraHDChckbx.addActionListener(this);

		picture_resizeChckbx.setSize(singleObjectDimension);
		picture_resizeChckbx.setMaximumSize(singleObjectDimension);
		picture_resizeChckbx.setLocation(0, 0);
		picture_resizeValue.setSize(singleObjectDimension);
		picture_resizeValue.setMaximumSize(singleObjectDimension);
		picture_resizeValue.setLocation(0,
				(int) (1 * (singleObjectDimension.getHeight() + singleSpacerDimension.getHeight())));
		picture_resizeEdge.setSize(singleObjectDimension);
		picture_resizeEdge.setMaximumSize(singleObjectDimension);
		picture_resizeEdge.setLocation(0,
				(int) (2 * (singleObjectDimension.getHeight() + singleSpacerDimension.getHeight())));
		picture_resizeFullHDChckbx.setSize(singleObjectDimension);
		picture_resizeFullHDChckbx.setMaximumSize(singleObjectDimension);
		picture_resizeFullHDChckbx.setLocation(0,
				(int) (3 * (singleObjectDimension.getHeight() + singleSpacerDimension.getHeight())));
		picture_resizeUltraHDChckbx.setSize(singleObjectDimension);
		picture_resizeUltraHDChckbx.setMaximumSize(singleObjectDimension);
		picture_resizeUltraHDChckbx.setLocation(0,
				(int) (4 * (singleObjectDimension.getHeight() + singleSpacerDimension.getHeight())));
		picture_resizePanel.add(picture_resizeChckbx);
		picture_resizePanel.add(picture_resizeValue);
		picture_resizePanel.add(picture_resizeEdge);
		picture_resizePanel.add(picture_resizeFullHDChckbx);
		picture_resizePanel.add(picture_resizeUltraHDChckbx);

		picture_commandsPanel.add(picture_miscPanel);
		picture_commandsPanel.add(picture_colorProfilePanel);
		picture_commandsPanel.add(picture_framePanel);
		picture_commandsPanel.add(picture_watermarkPanel);
		picture_commandsPanel.add(picture_resizePanel);

		Action travel_details = picture_fileChooser.getActionMap().get("viewTypeDetails");
		travel_details.actionPerformed(null);
		FileNameExtensionFilter travel_fileFilter = new FileNameExtensionFilter("GPS files", "gpx", "GPX");
		travel_fileChooser.setFileFilter(travel_fileFilter);
		travel_fileChooser.setAcceptAllFileFilterUsed(false);
		travel_fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		travel_fileChooser.setCurrentDirectory(new java.io.File("."));
		travel_fileChooser.setDialogTitle("Select files and folders");

		travel_filesPanel = new JPanel();
		travel_filesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		travel_filesPanel.setBounds((int) singleSpacerDimension.getWidth(), (int) singleSpacerDimension.getHeight(),
				(int) (5 * (singleObjectDimension.getWidth() + singleSpacerDimension.getWidth())
						- singleSpacerDimension.getWidth()),
				(int) (2 * singleObjectDimension.getHeight() + singleSpacerDimension.getHeight()));
		travelPanel.add(travel_filesPanel);
		travel_filesPanel.setLayout(null);

		travel_fileInputText = new JTextField();
		travel_fileInputText.setLocation(0, 0);
		travel_fileInputText.setToolTipText("Show the selected source file");
		travel_fileInputText.setColumns(26);
		travel_fileInputText.setEditable(false);
		travel_fileInputText.setText("Source file not selected");

		travel_selectInputFileBtn = new JButton("Select");
		travel_selectInputFileBtn.setActionCommand("file");
		travel_selectInputFileBtn.addActionListener(this);
		travel_selectInputFileBtn
				.setLocation((int) (travel_filesPanel.getSize().getWidth() - singleObjectDimension.getWidth()), 0);

		travel_fileOutputText = new JTextField();
		travel_fileOutputText.setLocation(0,
				(int) (singleObjectDimension.getHeight() + singleSpacerDimension.getHeight()));
		travel_fileOutputText.setToolTipText("Show the selected target file");
		travel_fileOutputText.setText("Target file not selected, adding new file in source folder");
		travel_fileOutputText.setEditable(false);
		travel_fileOutputText.setColumns(26);

		travel_selectOutputFileBtn = new JButton("Select");
		travel_selectOutputFileBtn.setActionCommand("file");
		travel_selectOutputFileBtn.addActionListener(this);
		travel_selectOutputFileBtn.setLocation(
				(int) (travel_filesPanel.getSize().getWidth() - singleObjectDimension.getWidth()),
				(int) (singleObjectDimension.getHeight() + singleSpacerDimension.getHeight()));
		travel_selectOutputFileBtn.setEnabled(false);

		travel_fileInputText.setSize(
				(int) (4 * singleObjectDimension.getWidth() + 3 * singleSpacerDimension.getWidth()),
				(int) singleObjectDimension.getHeight());
		travel_fileInputText.setMaximumSize(
				new Dimension((int) (4 * singleObjectDimension.getWidth() + 3 * singleSpacerDimension.getWidth()),
						(int) singleObjectDimension.getHeight()));
		travel_selectInputFileBtn.setSize(singleObjectDimension);
		travel_selectInputFileBtn.setMaximumSize(singleObjectDimension);
		travel_fileOutputText.setSize(
				(int) (4 * singleObjectDimension.getWidth() + 3 * singleSpacerDimension.getWidth()),
				(int) singleObjectDimension.getHeight());
		travel_fileOutputText.setMaximumSize(
				new Dimension((int) (4 * singleObjectDimension.getWidth() + 3 * singleSpacerDimension.getWidth()),
						(int) singleObjectDimension.getHeight()));
		travel_selectOutputFileBtn.setSize(singleObjectDimension);
		travel_selectOutputFileBtn.setMaximumSize(singleObjectDimension);

		travel_filesPanel.add(travel_fileInputText);
		travel_filesPanel.add(travel_selectInputFileBtn);
		travel_filesPanel.add(travel_fileOutputText);
		travel_filesPanel.add(travel_selectOutputFileBtn);

		travel_commandsPanel = new JPanel();
		travel_commandsPanel.setBounds((int) (travel_filesPanel.getLocation().x),
				(int) (travel_filesPanel.getLocation().y + travel_filesPanel.getSize().getHeight()
						+ singleSpacerDimension.getHeight()),
				(int) (5 * singleObjectDimension.getWidth() + 4 * singleSpacerDimension.getWidth()),
				(int) (5 * singleObjectDimension.getHeight() + 4 * singleSpacerDimension.getHeight()));
		travelPanel.add(travel_commandsPanel);
		travel_commandsPanel.setLayout(null);

		travel_mergePanel = new JPanel();
		travel_mergePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		travel_mergePanel.setLocation(0, 0);
		travel_mergePanel.setSize(
				new Dimension((int) singleObjectDimension.getWidth(), (int) singleObjectDimension.getHeight()));
		travel_mergePanel.setMaximumSize(
				new Dimension((int) singleObjectDimension.getWidth(), (int) singleObjectDimension.getHeight()));
		travel_mergePanel.setLayout(null);

		travel_mergeChckbx = new JCheckBox("Merge");
		travel_mergeChckbx.setToolTipText("Merge all tracks in the selected folder");
		travel_mergePanel.add(travel_mergeChckbx);
		travel_mergeChckbx.setSize(singleObjectDimension);
		travel_mergeChckbx.setMaximumSize(singleObjectDimension);
		travel_mergeChckbx.setLocation(0, 0);
		travel_mergePanel.add(travel_mergeChckbx);
		travel_commandsPanel.add(travel_mergePanel);

		travel_filterPanel = new JPanel();
		travel_filterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		travel_filterPanel.setLocation((int) (singleObjectDimension.getWidth() + 2 * singleSpacerDimension.getWidth()),
				0);
		travel_filterPanel.setSize(new Dimension((int) singleObjectDimension.getWidth(),
				(int) (3 * singleObjectDimension.getHeight() + 4 * singleSpacerDimension.getHeight())));
		travel_filterPanel.setMaximumSize(new Dimension((int) singleObjectDimension.getWidth(),
				(int) (3 * (singleObjectDimension.getHeight() + singleSpacerDimension.getHeight()))));
		travel_filterPanel.setLayout(null);
		travel_commandsPanel.add(travel_filterPanel);

		travel_filterChckbx = new JCheckBox("Filter");
		travel_filterChckbx.setToolTipText("Filter track points");
		travel_filterChckbx.setActionCommand("travel_filter");
		travel_filterChckbx.addActionListener(this);
		travel_filterChckbx.setSize(singleObjectDimension);
		travel_filterChckbx.setMaximumSize(singleObjectDimension);
		travel_filterChckbx.setLocation(0, 0);
		travel_filterPanel.add(travel_filterChckbx);

		travel_filterValueModel = new DefaultComboBoxModel<String>(
				new String[] { "10", "25", "50", "100", "150", "200", "250" });
		travel_filterValue = new JComboBox<String>(travel_filterValueModel);
		travel_filterValue.setEditable(true);
		travel_filterValue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ("comboBoxEdited".equals(e.getActionCommand())) {
					Object newValue = travel_filterValueModel.getSelectedItem();
					travel_filterValueModel.addElement((String) newValue);
					travel_filterValue.setSelectedItem(newValue);
				}
			}
		});
		travel_filterValue.setSize(singleObjectDimension);
		travel_filterValue.setMaximumSize(singleObjectDimension);
		travel_filterValue.setLocation(0,
				(int) (1 * (singleObjectDimension.getHeight() + singleSpacerDimension.getHeight())));
		travel_filterPanel.add(travel_filterValue);

		travel_simplifyPanel = new JPanel();
		travel_simplifyPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		travel_simplifyPanel
				.setLocation((int) (2 * singleObjectDimension.getWidth() + 3 * singleSpacerDimension.getWidth()), 0);
		travel_simplifyPanel.setSize(new Dimension((int) singleObjectDimension.getWidth(),
				(int) (3 * singleObjectDimension.getHeight() + 4 * singleSpacerDimension.getHeight())));
		travel_simplifyPanel.setMaximumSize(new Dimension((int) singleObjectDimension.getWidth(),
				(int) (3 * (singleObjectDimension.getHeight() + singleSpacerDimension.getHeight()))));
		travel_simplifyPanel.setLayout(null);
		travel_commandsPanel.add(travel_simplifyPanel);

		travel_simplifyChckbx = new JCheckBox("Simplify");
		travel_simplifyChckbx.setToolTipText("Simplify track points");
		travel_simplifyChckbx.setActionCommand("travel_simplify");
		travel_simplifyChckbx.addActionListener(this);
		travel_simplifyChckbx.setSize(singleObjectDimension);
		travel_simplifyChckbx.setMaximumSize(singleObjectDimension);
		travel_simplifyChckbx.setLocation(0, 0);
		travel_simplifyPanel.add(travel_simplifyChckbx);

		travel_simplifyValueModel = new DefaultComboBoxModel<String>(
				new String[] { "5000", "10000", "15000", "20000", "25000" });
		travel_simplifyValue = new JComboBox<String>(travel_simplifyValueModel);
		travel_simplifyValue.setEditable(true);
		travel_simplifyValue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ("comboBoxEdited".equals(e.getActionCommand())) {
					Object newValue = travel_simplifyValueModel.getSelectedItem();
					travel_simplifyValueModel.addElement((String) newValue);
					travel_simplifyValue.setSelectedItem(newValue);
				}
			}
		});
		travel_simplifyValue.setSize(singleObjectDimension);
		travel_simplifyValue.setMaximumSize(singleObjectDimension);
		travel_simplifyValue.setLocation(0,
				(int) (1 * (singleObjectDimension.getHeight() + singleSpacerDimension.getHeight())));
		travel_simplifyPanel.add(travel_simplifyValue);

		travel_simplifyGmapsChckbx = new JCheckBox("Google Maps");
		travel_simplifyGmapsChckbx.setToolTipText("Google Maps predefined numbero of points (1000)");
		travel_simplifyGmapsChckbx.setActionCommand("travel_simplifygmaps");
		travel_simplifyGmapsChckbx.addActionListener(this);
		travel_simplifyGmapsChckbx.setSize(singleObjectDimension);
		travel_simplifyGmapsChckbx.setMaximumSize(singleObjectDimension);
		travel_simplifyGmapsChckbx.setLocation(0,
				(int) (2 * (singleObjectDimension.getHeight() + singleSpacerDimension.getHeight())));
		travel_simplifyPanel.add(travel_simplifyGmapsChckbx);

		travel_timePanel = new JPanel();
		travel_timePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		travel_timePanel
				.setLocation((int) (3 * singleObjectDimension.getWidth() + 4 * singleSpacerDimension.getWidth()), 0);
		travel_timePanel.setSize(
				new Dimension((int) singleObjectDimension.getWidth(), (int) singleObjectDimension.getHeight()));
		travel_timePanel.setMaximumSize(
				new Dimension((int) singleObjectDimension.getWidth(), (int) singleObjectDimension.getHeight()));
		travel_timePanel.setLayout(null);

		travel_timeChckbx = new JCheckBox("Fake time");
		travel_timeChckbx.setToolTipText("Recalculate the track with a fake time");
		travel_timePanel.add(travel_timeChckbx);
		travel_timeChckbx.setSize(singleObjectDimension);
		travel_timeChckbx.setMaximumSize(singleObjectDimension);
		travel_timeChckbx.setLocation(0, 0);
		travel_timePanel.add(travel_timeChckbx);
		travel_commandsPanel.add(travel_timePanel);

		progressPanel = new JPanel();
		progressPanel.setBounds((int) (mainTab.getLocation().x),
				(int) (mainTab.getLocation().y + mainTab.getSize().getHeight() + singleSpacerDimension.getHeight()),
				(int) mainTab.getSize().getWidth(), (int) (singleObjectDimension.getHeight()));
		progressPanel.setMaximumSize(
				new Dimension((int) mainTab.getSize().getWidth(), (int) (singleObjectDimension.getHeight())));
		mainFrame.getContentPane().add(progressPanel);
		progressPanel.setLayout(null);

		progressBarProcess = new JProgressBar(0, 100);
		progressBarProcess.setToolTipText("Progress");
		progressBarProcess.setValue(progressBarProcess.getMinimum());
		progressBarProcess.setStringPainted(true);

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

		exitBtn = new JButton("Exit");
		exitBtn.setToolTipText("Shut down the program");
		exitBtn.setActionCommand("exit");
		exitBtn.addActionListener(this);

		progressBarProcess.setSize(singleObjectDimension);
		progressBarProcess.setMaximumSize(singleObjectDimension);
		progressBarProcess.setLocation((int) singleSpacerDimension.getWidth(), 0);

		startBtn.setSize(singleObjectDimension);
		startBtn.setMaximumSize(singleObjectDimension);
		startBtn.setLocation((int) (3 * (singleObjectDimension.getWidth() + singleSpacerDimension.getWidth())
				+ singleSpacerDimension.getWidth()), 0);

		cancelBtn.setSize(singleObjectDimension);
		cancelBtn.setMaximumSize(singleObjectDimension);
		cancelBtn.setLocation((int) (2 * (singleObjectDimension.getWidth() + singleSpacerDimension.getWidth())
				+ singleSpacerDimension.getWidth()), 0);

		saveBtn.setSize(singleObjectDimension);
		saveBtn.setMaximumSize(singleObjectDimension);
		saveBtn.setLocation((int) ((singleObjectDimension.getWidth() + singleSpacerDimension.getWidth())
				+ singleSpacerDimension.getWidth()), 0);

		exitBtn.setSize(singleObjectDimension);
		exitBtn.setMaximumSize(singleObjectDimension);
		exitBtn.setLocation((int) (4 * (singleObjectDimension.getWidth() + singleSpacerDimension.getWidth())
				+ singleSpacerDimension.getWidth()), 0);

		progressPanel.add(progressBarProcess);
		progressPanel.add(startBtn);
		progressPanel.add(cancelBtn);
		progressPanel.add(saveBtn);
		progressPanel.add(exitBtn);

		bottomPanel = new JPanel();
		bottomPanel.setBounds((int) (progressPanel.getLocation().x),
				(int) (progressPanel.getLocation().y + progressPanel.getSize().getHeight()
						+ singleSpacerDimension.getHeight()),
				(int) picture_commandsPanel.getSize().getWidth(), (int) (singleObjectDimension.getHeight()));
		bottomPanel.setMaximumSize(new Dimension((int) picture_commandsPanel.getSize().getWidth(),
				(int) (singleObjectDimension.getHeight())));
		mainFrame.getContentPane().add(bottomPanel);
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

		infoLbl = new JLabel(softwareInfoLabel, SwingConstants.RIGHT);
		infoLbl.setFont(new Font("FFX Marquee", Font.PLAIN, 13));
		infoLbl.setSize((int) (2.5 * singleObjectDimension.getWidth()), (int) singleObjectDimension.getHeight());

		errorMsg = new JLabel("Activity area", SwingConstants.LEFT);
		errorMsg.setFont(new Font(errorMsg.getFont().getName(), Font.PLAIN, 10));
		errorMsg.setSize((int) (3 * singleObjectDimension.getWidth()), (int) singleObjectDimension.getHeight());
		bottomPanel.add(errorMsg);
		bottomPanel.add(Box.createHorizontalGlue());
		bottomPanel.add(infoLbl);

		mainFrame.setSize(
				(int) (bottomPanel.getLocation().x + bottomPanel.getSize().getWidth()
						+ 3 * singleSpacerDimension.getWidth()),
				(int) (bottomPanel.getLocation().y + bottomPanel.getSize().getHeight()
						+ 4 * singleSpacerDimension.getHeight()));

		mainTab.setSize((int) (picturePanel.getWidth()), (int) (picturePanel.getHeight() + 32));

		mainTab.addTab("Picture", picturePanel);
		mainTab.addTab("Travel", travelPanel);
		mainTab.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				startBtn.setEnabled(false);
				switch (mainTab.getSelectedIndex()) {
				case 0: // Picture
					if (picture_folderInput != null)
						startBtn.setEnabled(true);
					break;
				case 1: // Travel
					if (travel_fileInput != null)
						startBtn.setEnabled(true);
					break;
				default:
					break;
				}
			}
		});
		mainFrame.getContentPane().add(mainTab);
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
		List<String> separated = Arrays.asList(pathvar.split(OSrelated.getPathSeparator()));
		File cmdFile;
		cmdFile = searchProgram("exiftool", separated, OSrelated);
		if (cmdFile != null) {
			cmdExecutables.put("exiftool", cmdFile);
			addToLog("Found exiftool version "
					+ executeCommand(new String[] { cmdExecutables.get("exiftool").toString(), "-ver" }));
		}
		cmdFile = searchProgram("convert", separated, OSrelated);
		if (cmdFile != null) {
			cmdExecutables.put("convert", cmdFile);
			String versionNumber = executeCommand(
					new String[] { cmdExecutables.get("convert").toString(), "-version" });
			addToLog("Found convert version " + versionNumber.substring(
					versionNumber.indexOf("ImageMagick ") + ((String) "ImageMagick ").length(),
					versionNumber.indexOf(" http")));
		}

		cmdFile = searchProgram("gpsbabel", separated, OSrelated);
		if (cmdFile != null) {
			cmdExecutables.put("gpsbabel", cmdFile);
			String versionNumber = executeCommand(new String[] { cmdExecutables.get("gpsbabel").toString(), "-V" });
			addToLog("Found gpsbabel version " + versionNumber
					.substring(versionNumber.indexOf("GPSBabel Version ") + ((String) "GPSBabel Version ").length()));
		}

	}

	private File searchProgram(String searchString, List<String> separated, OsDetector OS) {
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
					if (executeCommand(new String[] { testFile.toString(), "-version" }).contains("ImageMagick"))
						return testFile;
					break;
				case "exiftool":
					try {
						if (Float.parseFloat(executeCommand(new String[] { testFile.toString(), "-ver" })) > 9.0)
							return testFile;
					} catch (Exception ex) {
						break;
					}
					break;
				case "gpsbabel":
					if (executeCommand(new String[] { testFile.toString(), "-V" }).contains("GPSBabel"))
						return testFile;
					break;
				default:
					break;
				}
			}
		}
		return null;
	}

	private void GetConfigProps() {
		picture_photographerText.setText(configProps.getProperty("picture_author"));
		picture_copyrightChckbx
				.setSelected(configProps.getProperty("picture_copyright").compareTo(configurationBoolean[0]) == 0);
		picture_commentChckbx
				.setSelected(configProps.getProperty("picture_info").compareTo(configurationBoolean[0]) == 0);
		picture_colorProfileChckbx
				.setSelected(configProps.getProperty("picture_cprofile").compareTo(configurationBoolean[0]) == 0);
		picture_cleanExifChckbx
				.setSelected(configProps.getProperty("picture_noexif").compareTo(configurationBoolean[0]) == 0);
		picture_colorProfileFile.setSelectedItem((String) configProps.getProperty("picture_cprofileFile"));
		picture_watermarkChckbx
				.setSelected(configProps.getProperty("picture_watermark").compareTo(configurationBoolean[0]) == 0);
		picture_watermarkSize.setSelectedItem(Float.parseFloat(configProps.getProperty("picture_watermarkSize")));
		picture_watermarkFont.setSelectedItem((String) (configProps.getProperty("picture_watermarkFont")));
		picture_watermarkPosition.setSelectedItem((String) (configProps.getProperty("picture_watermarkPosition")));
		picture_frameChckbx
				.setSelected(configProps.getProperty("picture_frame").compareTo(configurationBoolean[0]) == 0);
		picture_frameColor.setSelectedItem((String) (configProps.getProperty("picture_frameColor")));
		picture_frameType.setSelectedItem((String) (configProps.getProperty("picture_frameType")));
		picture_frameSize.setSelectedItem(Float.parseFloat(configProps.getProperty("picture_frameSize")));
		picture_frameThickness.setSelectedItem(Float.parseFloat(configProps.getProperty("picture_frameThickness")));
		picture_resizeChckbx
				.setSelected(configProps.getProperty("picture_resize").compareTo(configurationBoolean[0]) == 0);
		picture_resizeEdge.setSelectedItem((String) (configProps.getProperty("picture_resizeEdge")));
		picture_resizeValue.setSelectedItem((String) (configProps.getProperty("picture_resizeSize")));
		picture_resizeFullHDChckbx
				.setSelected(configProps.getProperty("picture_resizeSizeFHD").compareTo(configurationBoolean[0]) == 0);
		picture_resizeUltraHDChckbx
				.setSelected(configProps.getProperty("picture_resizeSizeUHD").compareTo(configurationBoolean[0]) == 0);

		travel_filterChckbx
				.setSelected(configProps.getProperty("travel_filter").compareTo(configurationBoolean[0]) == 0);
		travel_filterValue.setSelectedItem((String) (configProps.getProperty("travel_filterValue")));
		travel_simplifyChckbx
				.setSelected(configProps.getProperty("travel_simplify").compareTo(configurationBoolean[0]) == 0);
		travel_simplifyValue.setSelectedItem((String) (configProps.getProperty("travel_simplifyValue")));
		travel_simplifyGmapsChckbx
				.setSelected(configProps.getProperty("travel_simplifyGmaps").compareTo(configurationBoolean[0]) == 0);
		travel_timeChckbx
				.setSelected(configProps.getProperty("travel_faketime").compareTo(configurationBoolean[0]) == 0);
	}

	private void ResetConfigProps(boolean defaultValues) {
		if (defaultValues) {
			configPropsDefault.setProperty("picture_author", "Mirko D. Comparetti");
			configPropsDefault.setProperty("picture_copyright", configurationBoolean[0]);
			configPropsDefault.setProperty("picture_info", configurationBoolean[0]);
			configPropsDefault.setProperty("picture_noexif", configurationBoolean[1]);
			configPropsDefault.setProperty("picture_cprofile", configurationBoolean[0]);
			configPropsDefault.setProperty("picture_cprofileFile", "srgb");
			configPropsDefault.setProperty("picture_frame", configurationBoolean[1]);
			configPropsDefault.setProperty("picture_frameColor", "white");
			configPropsDefault.setProperty("picture_frameType", "Inner");
			configPropsDefault.setProperty("picture_frameSize", "2.0");
			configPropsDefault.setProperty("picture_frameThickness", "0.25");
			configPropsDefault.setProperty("picture_watermark", configurationBoolean[1]);
			configPropsDefault.setProperty("picture_watermarkSize", "2.0");
			configPropsDefault.setProperty("picture_watermarkFont", "FFXMarquee");
			configPropsDefault.setProperty("picture_watermarkPosition", "southeast");
			configPropsDefault.setProperty("picture_resize", configurationBoolean[1]);
			configPropsDefault.setProperty("picture_resizeEdge", "Long");
			configPropsDefault.setProperty("picture_resizeSize", "2000");
			configPropsDefault.setProperty("picture_resizeSizeFHD", configurationBoolean[1]);
			configPropsDefault.setProperty("picture_resizeSizeUHD", configurationBoolean[1]);

			configPropsDefault.setProperty("travel_filter", configurationBoolean[1]);
			configPropsDefault.setProperty("travel_filterValue", "50");
			configPropsDefault.setProperty("travel_simplify", configurationBoolean[1]);
			configPropsDefault.setProperty("travel_simplifyValue", "15000");
			configPropsDefault.setProperty("travel_simplifyGmaps", configurationBoolean[1]);
			configPropsDefault.setProperty("travel_faketime", configurationBoolean[1]);
		} else {
			configProps.setProperty("picture_author", picture_photographerText.getText());
			configProps.setProperty("picture_copyright",
					picture_copyrightChckbx.isSelected() ? configurationBoolean[0] : configurationBoolean[1]);
			configProps.setProperty("picture_info",
					picture_commentChckbx.isSelected() ? configurationBoolean[0] : configurationBoolean[1]);
			configProps.setProperty("picture_noexif",
					picture_cleanExifChckbx.isSelected() ? configurationBoolean[0] : configurationBoolean[1]);
			configProps.setProperty("picture_cprofile",
					picture_colorProfileChckbx.isSelected() ? configurationBoolean[0] : configurationBoolean[1]);
			configProps.setProperty("picture_cprofileFile", (String) picture_colorProfileFile.getSelectedItem());
			configProps.setProperty("picture_frame",
					picture_frameChckbx.isSelected() ? configurationBoolean[0] : configurationBoolean[1]);
			configProps.setProperty("picture_frameColor", (String) (picture_frameColor.getSelectedItem()));
			configProps.setProperty("picture_frameType", (String) (picture_frameType.getSelectedItem()));
			configProps.setProperty("picture_frameSize", picture_frameSize.getSelectedItem().toString());
			configProps.setProperty("picture_frameThickness", picture_frameThickness.getSelectedItem().toString());
			configProps.setProperty("picture_watermark",
					picture_watermarkChckbx.isSelected() ? configurationBoolean[0] : configurationBoolean[1]);
			configProps.setProperty("picture_watermarkSize", picture_watermarkSize.getSelectedItem().toString());
			configProps.setProperty("picture_watermarkFont", (String) (picture_watermarkFont.getSelectedItem()));
			configProps.setProperty("picture_watermarkPosition",
					(String) (picture_watermarkPosition.getSelectedItem()));
			configProps.setProperty("picture_resize",
					picture_resizeChckbx.isSelected() ? configurationBoolean[0] : configurationBoolean[1]);
			configProps.setProperty("picture_resizeEdge", (String) (picture_resizeEdge.getSelectedItem()));
			configProps.setProperty("picture_resizeSize", (String) picture_resizeValue.getSelectedItem());
			configProps.setProperty("picture_resizeSizeFHD",
					picture_resizeFullHDChckbx.isSelected() ? configurationBoolean[0] : configurationBoolean[1]);
			configProps.setProperty("picture_resizeSizeUHD",
					picture_resizeUltraHDChckbx.isSelected() ? configurationBoolean[0] : configurationBoolean[1]);

			configProps.setProperty("travel_filter",
					travel_filterChckbx.isSelected() ? configurationBoolean[0] : configurationBoolean[1]);
			configProps.setProperty("travel_filterValue", (String) travel_filterValue.getSelectedItem());
			configProps.setProperty("travel_simplify",
					travel_simplifyChckbx.isSelected() ? configurationBoolean[0] : configurationBoolean[1]);
			configProps.setProperty("travel_simplifyValue", (String) travel_simplifyValue.getSelectedItem());
			configProps.setProperty("travel_simplifyGmaps",
					travel_simplifyGmapsChckbx.isSelected() ? configurationBoolean[0] : configurationBoolean[1]);
			configProps.setProperty("travel_faketime",
					travel_timeChckbx.isSelected() ? configurationBoolean[0] : configurationBoolean[1]);
		}
	}

	private void LoadConfiguration() {
		try {
			configReader = new FileInputStream(configFile);
			configProps.loadFromXML(configReader);
			configReader.close();

		} catch (FileNotFoundException exRead) {
			System.out.println("Configuration file not found, default values are saved");
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
				configPropsDefault.storeToXML(configWriter, "Default configuration");
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

	private File addSuffixFile(File workfile) {
		return addSuffixFile(workfile, UUID.randomUUID().toString().substring(8, 13));
	}

	private File addSuffixFile(File workfile, String suffix) {
		String outname = workfile.getName();
		return (new File(workfile.getParent() + File.separator
				+ outname.substring(0, outname.lastIndexOf("."))
				+ suffix
				+ outname.substring(outname.lastIndexOf("."))));
	}

	private void printStringList(String prefix, List<String> allowedExtensions) {
		Collections.sort(allowedExtensions);
		String tmpStr = "";
		for (String iterExt : allowedExtensions) {
			tmpStr += iterExt + ", ";
		}
		tmpStr = tmpStr.substring(0, tmpStr.lastIndexOf(","));
		System.out.println(prefix + tmpStr);
		addToLog(prefix + tmpStr);
	}

	private Optional<String> getExtension(String filename) {
		return Optional.ofNullable(filename).filter(f -> f.contains("."))
				.map(f -> f.substring(filename.lastIndexOf(".") + 1));
	}

	private List<File> filterFilesExtension(File[] listOfFiles, List<String> allowedExtensions, boolean caseSensitive) {
		List<File> fileList = new ArrayList<File>();
		List<String> extList = allowedExtensions;
		if (caseSensitive) {
			extList.clear();
			for (String selectedExt : allowedExtensions) {
				extList.add(selectedExt.toLowerCase());
			}
		}
		System.out.println(extList);
		String extension;
		Optional<String> optionextension;
		for (File selectedFile : listOfFiles) {
			optionextension = getExtension(selectedFile.toString());
			if (optionextension.isPresent()) {
				extension = "." + getExtension(selectedFile.toString()).get();
				extension = caseSensitive ? extension : extension.toLowerCase();
				if (!extension.isEmpty() && extList.contains(extension))
					fileList.add(selectedFile);
				else
					System.out.println("NotAdded");
			} else
				System.out.println("NotAdded-> Folder");
		}
		System.out.println(fileList);
		return fileList;
	}

	private List<File> SelectFiles(File workfolder, List<String> allowedExtensions, boolean caseSensitive) {
		return filterFilesExtension(workfolder.listFiles(), allowedExtensions, caseSensitive);
	}

	private List<File> SelectFiles(File workfolder, List<String> allowedExtensions) {
		return SelectFiles(workfolder, allowedExtensions, false);
	}

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
		int width = Integer.parseInt(executeCommand(new String[] { cmdExecutables.get("exiftool").toString(),
				"-ImageWidth", "-S", "-s", folderRef + File.separator + workingFile }));
		int height = Integer.parseInt(executeCommand(new String[] { cmdExecutables.get("exiftool").toString(),
				"-ImageHeight", "-S", "-s", folderRef + File.separator + workingFile }));
		return (new Dimension(width, height));
	}

	private Dimension GetImageSize(String workingFile, File folderRef) {
		Dimension RawImageSize = GetRawImageSize(workingFile, folderRef);
		if (!Orientation(workingFile, folderRef).isEmpty() && !IsHorizontal(workingFile, folderRef)) {
			return (new Dimension((int) Math.round(RawImageSize.getHeight()),
					(int) Math.round(RawImageSize.getWidth())));
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
		/*
		 * int Orient = Integer.parseInt(OrientStr); if (Orient == 6 || Orient == 8 ||
		 * Orient == 5 || Orient == 7) {
		 */
		if (OrientStr.equals("6") || OrientStr.equals("8") || OrientStr.equals("5") || OrientStr.equals("7")) {
			return false;
		}
		return true;
	}

	private String Orientation(String workingFile, File folderRef) {
		return executeCommand(new String[] { cmdExecutables.get("exiftool").toString(), "-Orientation", "-s3", "-n",
				folderRef + File.separator + workingFile });
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

	private void picture_WatermarkDataEnable(boolean enable) {
		picture_watermarkSize.setEnabled(enable);
		picture_watermarkPosition.setEnabled(enable);
		picture_watermarkFont.setEnabled(enable);
	}

	private void picture_ResizeDataEnable(boolean enable) {
		picture_resizeFullHDChckbx.setEnabled(enable && !picture_resizeUltraHDChckbx.isSelected());
		picture_resizeUltraHDChckbx.setEnabled(enable && !picture_resizeFullHDChckbx.isSelected());
		if (enable) {
			picture_ResizeCustomEnable(
					!(picture_resizeFullHDChckbx.isSelected() || picture_resizeUltraHDChckbx.isSelected()));
		} else {
			picture_resizeValue.setEnabled(enable);
			picture_resizeEdge.setEnabled(enable);
		}
	}

	private void picture_ResizeCustomEnable(boolean enable) {
		picture_resizeValue.setEnabled(enable);
		picture_resizeEdge.setEnabled(enable);
	}

	private void picture_ResizeCustomEnable(boolean enable, String checkbox) {
		picture_ResizeCustomEnable(enable);
		switch (checkbox) {
		case "fullhd":
			picture_resizeFullHDChckbx.setEnabled(enable);
			break;
		case "ultrahd":
			picture_resizeUltraHDChckbx.setEnabled(enable);
			break;
		default:
			break;
		}
	}

	private void picture_FrameDataEnable(boolean enable) {
		picture_frameColor.setEnabled(enable);
		picture_frameSize.setEnabled(enable);
		picture_frameThickness.setEnabled(enable);
		picture_frameType.setEnabled(enable);
	}

	private void picture_ColorProfileDataEnable(boolean enable) {
		picture_colorProfileFile.setEnabled(enable);
		picture_colorProfileIntent.setEnabled(enable);
	}

	private void travel_FilterDataEnable(boolean enable) {
		travel_filterValue.setEnabled(enable);
	}

	private void travel_SimplifyDataEnable(boolean enable) {
		travel_simplifyGmapsChckbx.setEnabled(enable);
		if (enable) {
			travel_SimplifyCustomEnable(!(travel_simplifyGmapsChckbx.isSelected()));
		} else {
			travel_simplifyValue.setEnabled(enable);
		}
	}

	private void travel_SimplifyCustomEnable(boolean enable) {
		travel_simplifyValue.setEnabled(enable);
	}

	private String GetOffset(String textPosition, Point offsetPoint) {
		if (offsetPoint == null)
			return "+0+0";
		addPoint(offsetPoint, new Point((int) Math.round(0.1 * offsetPoint.x), (int) Math.round(0.1 * offsetPoint.y)));
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
		default:
			offset = "+0+0";
			break;
		}
		return offset;
	}

	private ArrayList<Point> getCorners(Dimension imgSize, float size) {
		// NorthWest
		ArrayList<Point> corners = new ArrayList<Point>();
		corners.add(new Point((int) Math.round(imgSize.getWidth() * size / 100.0),
				(int) Math.round(imgSize.getHeight() * size / 100.0)));
		// SouthEast
		corners.add(new Point((int) Math.round(imgSize.getWidth() - corners.get(0).getX()),
				(int) Math.round(imgSize.getHeight() - corners.get(0).getY())));
		return corners;
	}

	private void command_PictureWatermark(String workingFile, Point offsetCorner) {
		if (!this.isRunning)
			return;
		addToLog("Adding watermark to " + workingFile, false);

		String[] command;
		String tmpdata;

		Dimension imgSize = GetImageSize(workingFile, picture_folderOutput);

		float dimension = (int) Max((float) imgSize.getWidth(), (float) imgSize.getHeight());

		dimension = (float) (((float) dimension) / 100.0 * ((float) picture_watermarkSize.getSelectedItem()));
		dimension = (float) ((dimension < 15) ? 15.0 : dimension);

		String watermark = copyrightSymbol + " " + picture_photographerText.getText();
		command = new String[] { cmdExecutables.get("exiftool").toString(), "-d", "%Y", "-DateTimeOriginal", "-S", "-s",
				picture_folderOutput + File.separator + workingFile };

		tmpdata = executeCommand(command);
		watermark += (!tmpdata.isEmpty()) ? (" | " + tmpdata) : ("");

		float scaleFactor[] = { 1, 1 };
		switch (GetShape(imgSize)) {
		case 1: // Horizontal
			scaleFactor[0] += (float) 0.1 * imgSize.getWidth() / imgSize.getHeight();
			scaleFactor[1] += (float) 0.1 * imgSize.getHeight() / imgSize.getWidth();
			break;
		case -1: // Vertical
			scaleFactor[0] += (float) 0.1 * imgSize.getWidth() / imgSize.getHeight();
			scaleFactor[1] += (float) 0.1 * imgSize.getHeight() / imgSize.getWidth();
			break;
		default: // Square
			scaleFactor[0] += (float) 0.1;
			scaleFactor[1] += (float) 0.1;
			break;
		}
		offsetCorner = ScalePoint(offsetCorner, scaleFactor);

		String offset = GetOffset(picture_watermarkPosition.getSelectedItem().toString(), offsetCorner);

		try {
			command = new String[] { cmdExecutables.get("convert").toString(),
					picture_folderOutput + File.separator + workingFile, "-font",
					ExportResource(picture_watermarkFontArray.get(picture_watermarkFont.getSelectedIndex())),
					"-pointsize", Float.toString(dimension), "-gravity",
					picture_watermarkPosition.getSelectedItem().toString(), "-stroke", "#000C", "-strokewidth",
					Float.toString((float) (dimension / 10.0)), "-annotate", offset, watermark, "-stroke", "none",
					"-fill", "white", "-annotate", offset, watermark,
					picture_folderOutput + File.separator + workingFile };
			executeCommand(command);
		} catch (Exception e) {
			addToLog(e.getMessage(), Color.RED, false);
		}
	}

	private void command_PictureResize(String workingFile) {
		if (!this.isRunning)
			return;
		addToLog("Changing dimensions of " + workingFile);

		String[] command;

		Dimension imgSize = GetImageSize(workingFile, picture_folderOutput);

		if (picture_resizeFullHDChckbx.isSelected()) {
			String size = "1920x1080";
			command = new String[] { cmdExecutables.get("convert").toString(),
					picture_folderOutput + File.separator + workingFile, "-resize", size,
					picture_folderOutput + File.separator + workingFile };
		} else if (picture_resizeUltraHDChckbx.isSelected()) {
			String size = "3840x2160";
			command = new String[] { cmdExecutables.get("convert").toString(),
					picture_folderOutput + File.separator + workingFile, "-resize", size,
					picture_folderOutput + File.separator + workingFile };
		} else {
			float resizeRatio = 100;
			int dimensionRef = 1000;
			dimensionRef = (int) Max((float) imgSize.getWidth(), (float) imgSize.getHeight());
			if (picture_resizeEdge.getSelectedItem().toString().compareToIgnoreCase("short") == 0)
				dimensionRef = (int) Min((float) imgSize.getWidth(), (float) imgSize.getHeight());
			try {
				resizeRatio = (float) (((float) Integer.parseInt(picture_resizeValue.getSelectedItem().toString()))
						/ ((float) dimensionRef) * 100.0);
			} catch (Exception e) {
				addToLog("Exception");
			}
			command = new String[] { cmdExecutables.get("convert").toString(),
					picture_folderOutput + File.separator + workingFile, "-resize", Float.toString(resizeRatio) + "%",
					picture_folderOutput + File.separator + workingFile };
		}

		executeCommand(command);
	}

	private Point command_PictureFrame(String workingFile) {
		if (!this.isRunning)
			return null;
		addToLog("Adding frame to " + workingFile);

		float size = (float) picture_frameSize.getSelectedItem();
		String mode = picture_frameType.getSelectedItem().toString();
		String color = picture_frameColor.getSelectedItem().toString();

		Point offset = new Point();

		switch (mode) {
		case "Italy":
			offset = command_PictureFrame(workingFile, "Plain", size, "red");
			addPoint(offset, command_PictureFrame(workingFile, "Plain", size, "white"));
			addPoint(offset, command_PictureFrame(workingFile, "Plain", size, "green"));
			break;
		case "Double":
			offset = command_PictureFrame(workingFile, "Plain", (int) (0.75 * ((float) (size))), "white");
			addPoint(offset, command_PictureFrame(workingFile, "Plain", size, "black"));
			break;
		case "Outer":
			offset = command_PictureFrame(workingFile, "Plain", size, "black");
			command_PictureFrame(workingFile, "Inner", (int) Math.round(((float) (size)) / 1.5), "white");
			break;
		case "Blur":
			ArrayList<Point> corners = getCorners(GetImageSize(workingFile, picture_folderOutput), size);
			String command[] = new String[] { cmdExecutables.get("convert").toString(), "(",
					picture_folderOutput + File.separator + workingFile, "-blur", "0x20", ")", "(",
					picture_folderOutput + File.separator + workingFile, "-crop",
					Math.round(corners.get(1).x - corners.get(0).x) + "x"
							+ Math.round(corners.get(1).y - corners.get(0).y) + "+" + Math.round(corners.get(0).x) + "+"
							+ Math.round(corners.get(0).y),
					"+repage", ")", "-geometry",
					"+" + Math.round(corners.get(0).x) + "+" + Math.round(corners.get(0).y), "-composite",
					picture_folderOutput + File.separator + workingFile };
			executeCommand(command);
			offset = command_PictureFrame(workingFile, "Inner", size, color);
			break;
		case "Inner":
		case "Rounded":
		case "Plain":
			offset = command_PictureFrame(workingFile, mode, size, color);
			break;
		default:
			break;
		}
		return offset;
	}

	private Point command_PictureFrame(String workingFile, String mode, float size, String color) {
		if (!this.isRunning)
			return new Point(0, 0);

		String[] command;
		float thick = (float) picture_frameThickness.getSelectedItem();

		Dimension imgSize = GetImageSize(workingFile, picture_folderOutput);

		ArrayList<Point> corners = getCorners(imgSize, size);

		thick = (float) (Max((float) Math.round(imgSize.getWidth()), (float) Math.round(imgSize.getHeight()))
				* (thick / 100.0));

		switch (mode) {
		case "Inner":
			command = new String[] { cmdExecutables.get("convert").toString(),
					picture_folderOutput + File.separator + workingFile, "-fill", "none", "-stroke", color,
					"-strokewidth", Float.toString(thick), "-draw",
					"\"rectangle " + Math.round(corners.get(0).x) + "," + Math.round(corners.get(0).y) + " "
							+ Math.round(corners.get(1).x) + "," + Math.round(corners.get(1).y) + "\"",
					picture_folderOutput + File.separator + workingFile };
			executeCommand(command);
			break;
		case "Rounded":
			command = new String[] { cmdExecutables.get("convert").toString(),
					picture_folderOutput + File.separator + workingFile, "-fill", "none", "-stroke", color,
					"-strokewidth", Float.toString(thick), "-draw",
					"\"roundrectangle " + Math.round(corners.get(0).x) + "," + Math.round(corners.get(0).y) + " "
							+ Math.round(corners.get(1).x) + "," + Math.round(corners.get(1).y) + " "
							+ Math.round(Max(corners.get(0).x, corners.get(0).y)) + ","
							+ Math.round(Max(corners.get(0).x, corners.get(0).y)) + "\"",
					picture_folderOutput + File.separator + workingFile };
			executeCommand(command);
			break;
		case "Plain":
			command = new String[] { cmdExecutables.get("convert").toString(),
					picture_folderOutput + File.separator + workingFile, "-bordercolor", color, "-border",
					size + "%x" + size + "%", picture_folderOutput + File.separator + workingFile };
			executeCommand(command);
			break;
		default:
			break;
		}
		return corners.get(0);
	}

	private void command_PictureColorProfile(String workingFile) {
		if (!this.isRunning)
			return;
		addToLog("Changing color profile to " + workingFile);

		try {
			String[] command = new String[] { cmdExecutables.get("convert").toString(),
					picture_folderOutput + File.separator + workingFile, "-intent",
					picture_colorProfileIntent.getSelectedItem().toString(), "-profile",
					ExportResource(picture_colorProfileFileArray.get(picture_colorProfileFile.getSelectedIndex())),
					picture_folderOutput + File.separator + workingFile };
			executeCommand(command);
		} catch (Exception e) {
			addToLog(e.getMessage(), Color.RED);
		}
	}

	private void command_PictureUpdateCommentExif(String workingFile) {
		if (!this.isRunning)
			return;
		addToLog("Updating EXIF info for " + workingFile);

		String[] command;
		String commentname = workingFile.substring(0, workingFile.lastIndexOf('.'));
		command = new String[] { cmdExecutables.get("exiftool").toString(), "-d", "%Y",
				picture_folderOutput + File.separator + workingFile, "-comment=" + commentname,
				"-UserComment=" + commentname, "-overwrite_original" };
		executeCommand(command);
	}

	private void command_PictureUpdateCopyrightExif(String workingFile) {
		if (!this.isRunning)
			return;
		addToLog("Updating copyright info for " + workingFile);

		String[] command;
		String tmpdata;
		String copyright = picture_photographerText.getText();
		command = new String[] { cmdExecutables.get("exiftool").toString(), "-d", "%Y", "-DateTimeOriginal", "-S", "-s",
				picture_folderOutput + File.separator + workingFile };
		tmpdata = executeCommand(command);
		if (!tmpdata.isEmpty())
			copyright += ", " + tmpdata;
		copyright += ". All rights reserved.";

		command = new String[] { cmdExecutables.get("exiftool").toString(),
				picture_folderOutput + File.separator + workingFile, "-artist=" + picture_photographerText.getText(),
				"-creator=" + picture_photographerText.getText(), "-xpauthor=" + picture_photographerText.getText(),
				"-copyright=" + copyrightSymbol + " " + copyright, "-ownername=" + picture_photographerText.getText(),
				"-XMP-dc:Rights=" + copyright, "-XMP-xmpRights:Marked=True", "-CopyrightFlag=True",
				"-XMP-xmpRights:UsageTerms=This picture and its metadata cannot be used and modified without permission. Every use must be explicitly authorized by the author. Any violations will be persecuted according to laws.",
				"-overwrite_original" };
		executeCommand(command);
	}

	private void command_PictureCleanExif(String workingFile) {
		if (!this.isRunning)
			return;
		addToLog("Removing private EXIF info of " + workingFile);

		String[] command = new String[] { cmdExecutables.get("exiftool").toString(),
				picture_folderOutput + File.separator + workingFile, "-software=", "-creatortool=", "-XMP-xmp:all=",
				"-XMP-xmpMM:all=", "-XMP-photoshop:all=", "-XMP-aux:all=", "-photoshop:all=", "-overwrite_original" };
		executeCommand(command);
	}

	private void command_PictureAddSoftwareEditor(String workingFile) {
		if (!this.isRunning)
			return;
		addToLog("Adding software info on " + workingFile);

		String[] command = new String[] { cmdExecutables.get("exiftool").toString(),
				picture_folderOutput + File.separator + workingFile, "-software=" + softwareInfo,
				"-creatortool=" + softwareInfo, "-overwrite_original" };
		executeCommand(command);
	}

	private String gpsTypeFromExtension(File selectedFile) {
		switch (getExtension(selectedFile.toString()).get()) {
		case "loc":
			return "geo";
		case "wpt":
			return "pcx";
		case "anr":
			return "saroute";
		default:
			break;
		}
		return getExtension(selectedFile.toString()).get();
	}

	private void command_TravelMerge(File inFile, File outFile) {
		if (!this.isRunning)
			return;
		addToLog("Merging all GPS files");

		List<String> allowedExtensions = Arrays.asList(new String[] { ".gpx" });
		List<File> listOfFiles = SelectFiles(inFile.getParentFile(), allowedExtensions);
		printStringList("Processing files with extensions: ", allowedExtensions); 

		List<String> commandArray = new ArrayList<String>();
		commandArray.add(cmdExecutables.get("gpsbabel").toString());
		for (File selectedFile : listOfFiles) {
			commandArray.add("-i");
			commandArray.add(gpsTypeFromExtension(selectedFile));
			commandArray.add("-f");
			commandArray.add(selectedFile.getAbsolutePath());
		}
		commandArray.add("-o");
		commandArray.add(gpsTypeFromExtension(outFile));
		commandArray.add("-F");
		commandArray.add(outFile.getAbsolutePath());

		executeCommand(commandArray.toArray(new String[0]));
	}

	private void command_TravelFilter(File inFile, File outFile) {
		if (!this.isRunning)
			return;
		addToLog("Filtering datapoints of " + travel_fileInput.toString());

		List<String> commandArray = new ArrayList<String>();
		commandArray.add(cmdExecutables.get("gpsbabel").toString());
		commandArray.add("-i");
		commandArray.add(gpsTypeFromExtension(inFile));
		commandArray.add("-f");
		commandArray.add(inFile.getAbsolutePath());
		commandArray.add("-x");
		commandArray.add("simplify,crosstrack,error="
				+ ((double) Integer.parseInt(travel_filterValue.getSelectedItem().toString()) / 1000.0) + "k");
		commandArray.add("-o");
		commandArray.add(gpsTypeFromExtension(outFile));
		commandArray.add("-F");
		commandArray.add(outFile.getAbsolutePath());

		executeCommand(commandArray.toArray(new String[0]));
	}

	private void command_TravelSimplify(File inFile, File outFile) {
		if (!this.isRunning)
			return;
		String countPoints = travel_simplifyValue.getSelectedItem().toString();
		if (travel_simplifyGmapsChckbx.isSelected())
			countPoints = "1000";
		addToLog("Changing datapoints of " + travel_fileInput.toString() + " to " + countPoints);

		List<String> commandArray = new ArrayList<String>();
		commandArray.add(cmdExecutables.get("gpsbabel").toString());
		commandArray.add("-i");
		commandArray.add(gpsTypeFromExtension(inFile));
		commandArray.add("-f");
		commandArray.add(inFile.getAbsolutePath());
		commandArray.add("-x");
		commandArray.add("simplify,count=" + countPoints);
		commandArray.add("-o");
		commandArray.add(gpsTypeFromExtension(outFile));
		commandArray.add("-F");
		commandArray.add(outFile.getAbsolutePath());

		executeCommand(commandArray.toArray(new String[0]));
	}

	private void command_TravelFaketime(File inFile, File outFile) {
		if (!this.isRunning)
			return;
		addToLog("Changing date time with fake datetime for " + travel_fileInput.toString());

		List<String> commandArray = new ArrayList<String>();
		commandArray.add(cmdExecutables.get("gpsbabel").toString());
		commandArray.add("-i");
		commandArray.add(gpsTypeFromExtension(inFile));
		commandArray.add("-f");
		commandArray.add(inFile.getAbsolutePath());
		commandArray.add("-x");
		commandArray.add("track,faketime=f19700101000000+2");
		commandArray.add("-o");
		commandArray.add(gpsTypeFromExtension(outFile));
		commandArray.add("-F");
		commandArray.add(outFile.getAbsolutePath());

		executeCommand(commandArray.toArray(new String[0]));
	}

	private String executeCommand(String[] command) {
		ProcessBuilder ps = new ProcessBuilder(command);
		ps.directory(picture_folderOutput);
		ps.redirectErrorStream(true);
		StringBuilder builder = new StringBuilder();
		String line;
		Process pr = null;
		try {
			pr = ps.start();
			System.out.println(ps.command());
			BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			while ((line = in.readLine()) != null) {
				builder.append(line);
			}
			pr.waitFor();
			in.close();
			if (pr.exitValue() != 0) {
				System.out.println("Process terminated with errors!\n" + builder.toString());
				addToLog("Process terminated with errors!\n" + builder.toString(), Color.RED);
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
			str = new String("[" + dateFormat.format(Calendar.getInstance().getTime()).toString() + "] ") + str.trim();
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
		for (int threadIterator = 0; threadIterator < threads.size(); threadIterator++) {
			isAlive |= threads.get(threadIterator).isAlive();
		}
		return isAlive;
	}

	private int numFilesProcessed(List<RunnableActions> threads) {
		int files = 0;
		for (int threadIterator = 0; threadIterator < threads.size(); threadIterator++) {
			files += threads.get(threadIterator).processedFiles();
		}
		return files;
	}

	private void addToLog(String str, Color textColor, boolean activityBar) {
		if (activityBar)
			errorMsg.setText(str);
	}

	private void addToLog(String str, Color textColor) {
		addToLog(str, textColor, true);
	}

	private void addToLog(String str, boolean activityBar) {
		addToLog(str, Color.BLACK, activityBar);
	}

	private void addToLog(String str) {
		addToLog(str, Color.BLACK);
	}

	class Task extends SwingWorker<Void, Void> {
		public Void doInBackground() {
			setProgress(progressBarProcess.getMinimum());
			progressBarProcess.setValue(progressBarProcess.getMinimum());
			addToLog("Process started.");
			switch (mainTab.getSelectedIndex()) {
			case 0: // Picture
				if (!checkWorkPicture()) {
					isRunning = false;
					return null;
				}

				List<String> allowedExtensions = Arrays
						.asList(new String[] { ".jpg", ".jpeg", ".tif", ".tiff", ".png" });
				List<File> listOfFiles = SelectFiles(picture_folderInput, allowedExtensions);
				printStringList("Processing files with extensions: ", allowedExtensions);

				if (picture_folderInput != picture_folderOutput) {
					addToLog("Copying files from source to target", false);
					try {
						for (File selectFile : listOfFiles) {
							if (selectFile.isFile() && isRunning) {
								Files.copy(selectFile.toPath(),
										new File(
												picture_folderOutput.toString() + File.separator + selectFile.getName())
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

				int numThreads = ((listOfFiles.size() > numProcessors) ? numProcessors : listOfFiles.size());

				addToLog("Process using " + numThreads + " threads.");

				List<RunnableActions> processThreads = new ArrayList<RunnableActions>(numThreads);
				List<List<File>> filePerThread = new ArrayList<List<File>>(numThreads);

				int quotientThread = listOfFiles.size() / numThreads;
				int remainderThread = listOfFiles.size() % numThreads;

				for (int threadIterator = 0; threadIterator < numThreads; threadIterator++) {
					filePerThread.add(threadIterator,
							new ArrayList<File>(quotientThread + ((threadIterator >= remainderThread) ? 0 : 1)));
					for (int fileIterator = 0; fileIterator < (quotientThread
							+ ((threadIterator >= remainderThread) ? 0 : 1)); fileIterator++) {
						filePerThread.get(threadIterator).add(fileIterator,
								listOfFiles.get(threadIterator * quotientThread + fileIterator
										+ ((threadIterator >= remainderThread) ? remainderThread : threadIterator)));
					}
					processThreads.add(threadIterator, new RunnableActions(filePerThread.get(threadIterator)));
				}

				for (int threadIterator = 0; threadIterator < numThreads; threadIterator++) {
					processThreads.get(threadIterator).setName("Thread-" + (threadIterator + 1));
					processThreads.get(threadIterator).start();
				}

				while (isAThreadAlive(processThreads)) {
					try {
						Thread.sleep(10);
						setProgress((int) (((float) (numFilesProcessed(processThreads)))
								/ ((float) (listOfFiles.size())) * 100f));
					} catch (InterruptedException ex) {
						Thread.currentThread().interrupt();
					}
				}

				if (isRunning && (numFilesProcessed(processThreads) == listOfFiles.size())) {
					setProgress(progressBarProcess.getMaximum());
				}
				break;
			case 1: // Travel
				if (!checkWorkTravel()) {
					isRunning = false;
					return null;
				}

				File workFile = travel_fileInput;
				File outFile = travel_fileOutput;

				try {
					boolean allOk = true;
					if (travel_mergeChckbx.isSelected()) {
						outFile = addSuffixFile(workFile, UUID.randomUUID().toString().substring(8, 13) + "merge");
						command_TravelMerge(workFile, outFile);
						if (workFile != travel_fileInput)
							workFile.delete();
						workFile = outFile;
					}
					if (travel_filterChckbx.isSelected()) {
						outFile = addSuffixFile(workFile, UUID.randomUUID().toString().substring(8, 13) + "filter");
						command_TravelFilter(workFile, outFile);
						if (workFile != travel_fileInput)
							workFile.delete();
						workFile = outFile;
					}
					if (travel_simplifyChckbx.isSelected()) {
						outFile = addSuffixFile(workFile, UUID.randomUUID().toString().substring(8, 13) + "simplify");
						command_TravelSimplify(workFile, outFile);
						if (workFile != travel_fileInput)
							workFile.delete();
						workFile = outFile;
					}
					if (travel_timeChckbx.isSelected()) {
						outFile = addSuffixFile(workFile, UUID.randomUUID().toString().substring(8, 13) + "time");
						command_TravelFaketime(workFile, outFile);
						if (workFile != travel_fileInput)
							workFile.delete();
						workFile = outFile;
					}
					if (travel_fileOutput.exists())
						travel_fileOutput = addSuffixFile(travel_fileOutput, UUID.randomUUID().toString().substring(8, 13));
					workFile.renameTo(travel_fileOutput);
					travel_fileOutputText.setText("Proposed file: " + travel_fileOutput.toString());

					if (!allOk) {
						throw new InterruptedException();
					}
					if (!isRunning)
						throw new InterruptedException();
				} catch (InterruptedException e) {
				}
				break;
			default:
				break;
			}
			if (!isRunning) {
				setProgress(progressBarProcess.getMinimum());
			}

			return null;
		}

		public void done() {
			if (isRunning)
				Toolkit.getDefaultToolkit().beep();
			CommandsEnable(true);
			if (isRunning)
				addToLog("Process executed successfully.", Color.GREEN);
			else
				addToLog("Process executed with errors.", Color.BLUE);
		}

		public boolean checkWorkPicture() {
			if (picture_folderInput == null) {
				addToLog("ERROR: Select a source folder first!", Color.RED, false);
				return false;
			}
			if (!(picture_watermarkChckbx.isSelected() || picture_resizeChckbx.isSelected()
					|| picture_frameChckbx.isSelected() || picture_copyrightChckbx.isSelected()
					|| picture_colorProfileChckbx.isSelected() || picture_cleanExifChckbx.isSelected()
					|| picture_commentChckbx.isSelected())) {
				System.out.println("No process selected");
				addToLog("ERROR: No process selected.", Color.RED, false);
				return false;
			}
			return true;
		}

		public boolean checkWorkTravel() {
			if (travel_fileInput == null) {
				addToLog("ERROR: Select a source file first!", Color.RED, false);
				return false;
			}
			if (!(travel_mergeChckbx.isSelected() || travel_filterChckbx.isSelected()
					|| travel_simplifyChckbx.isSelected() || travel_timeChckbx.isSelected()
					|| travel_simplifyChckbx.isSelected())) {
				System.out.println("No process selected");
				addToLog("ERROR: No process selected.", Color.RED, false);
				return false;
			}
			return true;
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
			return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
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

		picture_selectInputFolderBtn.setEnabled(enabled);
		picture_selectOutputFolderBtn.setEnabled(enabled);
		picture_colorProfileChckbx.setEnabled(enabled);
		picture_watermarkChckbx.setEnabled(enabled);
		picture_frameChckbx.setEnabled(enabled);
		picture_resizeChckbx.setEnabled(enabled);
		picture_colorProfileChckbx.setEnabled(enabled);
		picture_copyrightChckbx.setEnabled(enabled);
		picture_commentChckbx.setEnabled(enabled);
		picture_cleanExifChckbx.setEnabled(enabled);

		travel_mergeChckbx.setEnabled(enabled);
		travel_filterChckbx.setEnabled(enabled);
		travel_simplifyChckbx.setEnabled(enabled);
		travel_timeChckbx.setEnabled(enabled);

		if (enabled) {
			picture_WatermarkDataEnable(picture_watermarkChckbx.isSelected());
			picture_ResizeDataEnable(picture_resizeChckbx.isSelected());
			picture_FrameDataEnable(picture_frameChckbx.isSelected());
			picture_ColorProfileDataEnable(picture_colorProfileChckbx.isSelected());

			travel_FilterDataEnable(travel_filterChckbx.isSelected());
			travel_SimplifyDataEnable(travel_simplifyChckbx.isSelected());
		} else {
			picture_WatermarkDataEnable(false);
			picture_ResizeDataEnable(false);
			picture_FrameDataEnable(false);
			picture_ColorProfileDataEnable(false);

			travel_FilterDataEnable(false);
			travel_SimplifyDataEnable(false);
		}
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		switch (evt.getActionCommand()) {
		case "start":
			ResetConfigProps(false);
			CommandsEnable(false);
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			Task currentTask = new Task();
			progressTask = currentTask;
			currentTask.addPropertyChangeListener((PropertyChangeListener) this);
			this.isRunning = true;
			currentTask.execute();
			break;
		case "exit":
			DeleteResource(tmpFolderPath);
			System.exit(0);
			break;
		case "stop":
			this.isRunning = false;
			progressTask.cancel(false);
			break;
		case "save":
			SaveConfiguration();
			break;
		case "folder":
			if (evt.getSource() == picture_selectInputFolderBtn) {
				if (picture_fileChooser.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
					picture_folderInput = picture_fileChooser.getSelectedFile();
					picture_folderOutput = picture_folderInput;
					picture_folderInputText.setText(picture_folderInput.toString());
					startBtn.setEnabled(true);
					picture_selectOutputFolderBtn.setEnabled(true);
				} else {
					addToLog("Source folder not selected.", false);
				}
			}
			if (evt.getSource() == picture_selectOutputFolderBtn) {
				if (picture_fileChooser.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
					picture_folderOutput = picture_fileChooser.getSelectedFile();
					picture_folderOutputText.setText(picture_folderOutput.toString());
				} else {
					addToLog("Target folder not selected: overwriting files in the source folder.", false);
				}
			}
			break;
		case "file":
			if (evt.getSource() == travel_selectInputFileBtn) {
				if (travel_fileChooser.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
					travel_fileInput = travel_fileChooser.getSelectedFile();
					travel_fileOutput = travel_fileInput;
					travel_fileInputText.setText(travel_fileInput.toString());
					travel_fileOutput = addSuffixFile(travel_fileInput);
					travel_fileOutputText.setText("Proposed file: " + travel_fileOutput.toString());
					startBtn.setEnabled(true);
					travel_selectOutputFileBtn.setEnabled(true);
				} else {
					addToLog("Source file not selected.", false);
				}
			}
			if (evt.getSource() == travel_selectOutputFileBtn) {
				if (travel_fileChooser.showOpenDialog(mainFrame) == JFileChooser.APPROVE_OPTION) {
					travel_fileOutput = travel_fileChooser.getSelectedFile();
					travel_fileOutputText.setText(travel_fileOutput.toString());
				} else {
					addToLog("Target file not selected: adding a new file in the same folder.", false);
				}
			}
			break;
		case "picture_colorProfile":
			if (evt.getSource() == picture_colorProfileChckbx) {
				picture_ColorProfileDataEnable(picture_colorProfileChckbx.isSelected());
			}
			break;
		case "picture_watermark":
			if (evt.getSource() == picture_watermarkChckbx) {
				picture_WatermarkDataEnable(picture_watermarkChckbx.isSelected());
			}
			break;
		case "picture_frame":
			if (evt.getSource() == picture_frameChckbx) {
				picture_FrameDataEnable(picture_frameChckbx.isSelected());
			}
			break;
		case "picture_resize":
			if (evt.getSource() == picture_resizeChckbx) {
				picture_ResizeDataEnable(picture_resizeChckbx.isSelected());
			}
			break;
		case "picture_fullhd":
			if (evt.getSource() == picture_resizeFullHDChckbx) {
				picture_ResizeCustomEnable(!picture_resizeFullHDChckbx.isSelected(), "ultrahd");
			}
			break;
		case "picture_ultrahd":
			if (evt.getSource() == picture_resizeUltraHDChckbx) {
				picture_ResizeCustomEnable(!picture_resizeUltraHDChckbx.isSelected(), "fullhd");
			}
			break;
		case "travel_filter":
			if (evt.getSource() == travel_filterChckbx) {
				travel_FilterDataEnable(travel_filterChckbx.isSelected());
			}
			break;
		case "travel_simplify":
			if (evt.getSource() == travel_simplifyChckbx) {
				travel_SimplifyDataEnable(travel_simplifyChckbx.isSelected());
			}
			break;
		case "travel_simplifygmaps":
			if (evt.getSource() == travel_simplifyGmapsChckbx) {
				travel_SimplifyCustomEnable(!travel_simplifyGmapsChckbx.isSelected());
			}
			break;
		default:
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
		jarFolderResource += File.separator + (resourceName.startsWith("/") ? resourceName.substring(1) : resourceName);
		jarFolderResource = jarFolderResource.replace('\\', File.separatorChar).replace('/', File.separatorChar);

		try {
			stream = PictureTravelSuiteClass.class.getResourceAsStream(resourceName);
			if (stream == null)
				throw new Exception("Impossibile ottenere la risorsa \"" + resourceName + "\" dal Jar.");

			int readBytes;
			byte[] buffer = new byte[4096];

			Path pathToFile = Paths.get(jarFolderResource);
			Files.createDirectories(pathToFile.getParent());

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
					if ((path[pStart - i] | 0x20) != (extension[eStart - i] | 0x20)) {
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
				for (int iter = 0; (iter < filesToProcess.length && isRunning); iter++) {
					addToLog(t.getName() + " - Work on " + filesToProcess[iter] + " started.", false);

					boolean allOk = true;

					Point offsetCorner = new Point();
					if (picture_colorProfileChckbx.isSelected())
						command_PictureColorProfile(filesToProcess[iter]);
					if (picture_frameChckbx.isSelected())
						offsetCorner = command_PictureFrame(filesToProcess[iter]);
					if (picture_watermarkChckbx.isSelected())
						command_PictureWatermark(filesToProcess[iter], offsetCorner);
					if (picture_resizeChckbx.isSelected())
						command_PictureResize(filesToProcess[iter]);
					if (picture_cleanExifChckbx.isSelected())
						command_PictureCleanExif(filesToProcess[iter]);
					if (picture_commentChckbx.isSelected())
						command_PictureUpdateCommentExif(filesToProcess[iter]);
					if (picture_copyrightChckbx.isSelected())
						command_PictureUpdateCopyrightExif(filesToProcess[iter]);
					command_PictureAddSoftwareEditor(filesToProcess[iter]);

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
