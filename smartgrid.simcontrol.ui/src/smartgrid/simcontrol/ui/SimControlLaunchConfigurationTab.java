package smartgrid.simcontrol.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

//OpenFile Diolog MS Style
import org.eclipse.swt.widgets.FileDialog;

import java.io.File;
//Resource Files
import java.util.ResourceBundle;

import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.widgets.Combo;

import smartgrid.simcontroller.baselibary.Constants;
import smartgrid.simcontroller.baselibary.GenerationStyle;
import smartgrid.simcontroller.baselibary.HackingStyle;

/**
 * This Class provides the SimController Configuration Tab
 * 
 * @author Christian (Modified by)
 * @extends {@link AbstractLaunchConfigurationTab}
 */
public class SimControlLaunchConfigurationTab extends
		AbstractLaunchConfigurationTab {

	/*
	 * UI Variables
	 */
	private Composite container;
	private Text outputTextbox;
	private Text inputTextbox;
	private Text topologyTextbox;
	private Text timeStepsTextBox;
	// private boolean ignoreLogicalConnections;
	private Text rootNodeTextbox;
	private Button ignoreLogicalConButton;
	private Combo hackingStyleCombo;
	private Combo generationStyleCombo;
	private Text smartMeterCountTextBox;
	private Text controlCenterCountTextBox;
	private Button generateInputCheckBox;

	/**
	 * Creates the UI Control
	 * 
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void createControl(Composite parent) {

		container = new Composite(parent, SWT.NONE);
		this.setControl(container);
		container.setLayout(new FormLayout());

		Group group = new Group(container, SWT.NONE);
		FormData fd_group = new FormData();
		fd_group.bottom = new FormAttachment(0, 114);
		fd_group.right = new FormAttachment(0, 710);
		fd_group.top = new FormAttachment(0, 10);
		fd_group.left = new FormAttachment(0, 10);
		group.setLayoutData(fd_group);

		group.setText(ResourceBundle.getBundle(
				"smartgrid.simcontrol.ui.messages").getString(
				"SimControlLaunchConfigurationTab.group.text"));

		inputTextbox = new Text(group, SWT.READ_ONLY | SWT.H_SCROLL
				| SWT.CANCEL);
		inputTextbox.setTouchEnabled(true);

		inputTextbox.setBounds(10, 22, 553, 32);
		inputTextbox.addModifyListener(e -> propertyChanged());
		inputTextbox.setEditable(true);
		inputTextbox.setText(ResourceBundle.getBundle(
				"smartgrid.simcontrol.ui.messages").getString(
				"SimControlLaunchConfigurationTab.inputTextbox.text"));
		org.eclipse.swt.widgets.Button SelectInputPathButton = new org.eclipse.swt.widgets.Button(
				group, SWT.TOGGLE);
		SelectInputPathButton.setTouchEnabled(true);
		SelectInputPathButton.setBounds(569, 22, 121, 32);

		SelectInputPathButton.setText(ResourceBundle.getBundle(
				"smartgrid.simcontrol.ui.messages").getString(
				"SimControlLaunchConfigurationTab.SelectInputPathButton.text"));

		topologyTextbox = new Text(group, SWT.READ_ONLY | SWT.H_SCROLL
				| SWT.CANCEL);
		topologyTextbox.setTouchEnabled(true);
		topologyTextbox.setBounds(10, 60, 553, 32);
		topologyTextbox.addModifyListener(e -> propertyChanged());
		topologyTextbox.setEditable(true);
		topologyTextbox
				.setText(ResourceBundle
						.getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.inputTextbox.text")); //$NON-NLS-1$ //$NON-NLS-2$

		org.eclipse.swt.widgets.Button SelectTopologyPathButton = new org.eclipse.swt.widgets.Button(
				group, SWT.TOGGLE);
		SelectTopologyPathButton.setTouchEnabled(true);
		SelectTopologyPathButton.setBounds(569, 60, 121, 32);
		SelectTopologyPathButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		SelectTopologyPathButton
				.setText(ResourceBundle
						.getBundle("smartgrid.simcontrol.ui.messages")
						.getString(
								"SimControlLaunchConfigurationTab.SelectTopologyPathButton.text"));

		Group grpOutputFiles = new Group(container, SWT.NONE);
		FormData fd_grpOutputFiles = new FormData();
		fd_grpOutputFiles.top = new FormAttachment(group, 6);
		fd_grpOutputFiles.right = new FormAttachment(0, 710);
		fd_grpOutputFiles.left = new FormAttachment(0, 10);
		grpOutputFiles.setLayoutData(fd_grpOutputFiles);

		grpOutputFiles.setText(ResourceBundle.getBundle(
				"smartgrid.simcontrol.ui.messages").getString(
				"SimControlLaunchConfigurationTab.grpOutputFiles.text"));
		org.eclipse.swt.widgets.Button SelectOutputPathButton = new org.eclipse.swt.widgets.Button(
				grpOutputFiles, SWT.TOGGLE);
		SelectOutputPathButton.setTouchEnabled(true);
		SelectOutputPathButton.setBounds(569, 22, 121, 32);
		SelectOutputPathButton
				.setText(ResourceBundle
						.getBundle("smartgrid.simcontrol.ui.messages")
						.getString(
								"SimControlLaunchConfigurationTab.SelectOutputPathButton.text"));

		outputTextbox = new Text(grpOutputFiles, SWT.READ_ONLY | SWT.H_SCROLL
				| SWT.CANCEL);
		outputTextbox.setTouchEnabled(true);
		outputTextbox.setEditable(true);
		outputTextbox.setBounds(10, 22, 553, 32);
		outputTextbox.addModifyListener(e -> propertyChanged());
		outputTextbox.setText(ResourceBundle.getBundle(
				"smartgrid.simcontrol.ui.messages").getString(
				"SimControlLaunchConfigurationTab.inputTextbox.text"));

		Group OptionsGroup = new Group(container, SWT.NONE);
		fd_grpOutputFiles.bottom = new FormAttachment(100, -385);

		Button copyPathButton = new Button(grpOutputFiles, SWT.NONE);
		copyPathButton.setTouchEnabled(true);
		copyPathButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {

				// Copy Inputpath as Output Dir
				outputTextbox.setText(new File(inputTextbox.getText())
						.getParent());
			}
		});
		copyPathButton.setBounds(10, 62, 155, 32);
		copyPathButton
				.setText(ResourceBundle
						.getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.copyPathButton.text")); //$NON-NLS-1$ //$NON-NLS-2$
		OptionsGroup.setLayout(null);
		FormData fd_OptionsGroup = new FormData();
		fd_OptionsGroup.top = new FormAttachment(grpOutputFiles, 2);
		fd_OptionsGroup.left = new FormAttachment(group, 0, SWT.LEFT);
		fd_OptionsGroup.right = new FormAttachment(group, 0, SWT.RIGHT);
		fd_OptionsGroup.bottom = new FormAttachment(100, -42);
		OptionsGroup.setLayoutData(fd_OptionsGroup);
		OptionsGroup
				.setText(ResourceBundle
						.getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.OptionsGroup.text")); //$NON-NLS-1$ //$NON-NLS-2$

		Composite TimeStepsComposite = new Composite(OptionsGroup, SWT.NONE);
		TimeStepsComposite.setBounds(10, 28, 304, 24);
		TimeStepsComposite.setLayout(null);

		Label timeStepsLabel = new Label(TimeStepsComposite, SWT.NONE);
		timeStepsLabel.setBounds(0, 3, 148, 21);
		timeStepsLabel
				.setText(ResourceBundle
						.getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.timeStepsLabel.text")); //$NON-NLS-1$ //$NON-NLS-2$

		timeStepsTextBox = new Text(TimeStepsComposite, SWT.BORDER | SWT.CENTER);
		timeStepsTextBox.setTouchEnabled(true);
		timeStepsTextBox.setBounds(189, 0, 105, 21);
		timeStepsTextBox
				.setText(ResourceBundle
						.getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.inputTextbox.text")); //$NON-NLS-1$ //$NON-NLS-2$
		timeStepsTextBox.addModifyListener(e -> propertyChanged());

		ignoreLogicalConButton = new Button(OptionsGroup, SWT.CHECK
				| SWT.CENTER);
		ignoreLogicalConButton.setTouchEnabled(true);

		ignoreLogicalConButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// ignoreLogicalConnections =
				// ignoreLogicalConButton.getEnabled();
				propertyChanged();

				if (ignoreLogicalConButton.getSelection()) {
					hackingStyleCombo.setText(HackingStyle.FULLY_MESHED_HACKING
							.name());
				}

			}
		});

		ignoreLogicalConButton.setBounds(365, 28, 304, 24);
		ignoreLogicalConButton
				.setText(ResourceBundle
						.getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.ignoreLogicalConButton.text")); //$NON-NLS-1$ //$NON-NLS-2$

		Composite HackingStyleComposite = new Composite(OptionsGroup, SWT.NONE);
		HackingStyleComposite.setBounds(10, 72, 316, 24);

		Label hackingStyleLabel = new Label(HackingStyleComposite, SWT.NONE);
		hackingStyleLabel.setBounds(0, 0, 111, 24);
		hackingStyleLabel.setText(ResourceBundle.getBundle(
				"smartgrid.simcontrol.ui.messages").getString(
				"SimControlLaunchConfigurationTab.hackingStyleLabel.text"));

		hackingStyleCombo = new Combo(HackingStyleComposite, SWT.READ_ONLY);
		hackingStyleCombo.setTouchEnabled(true);

		// Building Items List for hackingStyleCombo
		HackingStyle[] hackingStyles = HackingStyle.values();
		String[] hackinStyleEnumStrings = new String[hackingStyles.length];

		for (int i = 0; i < hackingStyles.length; i++) {
			hackinStyleEnumStrings[i] = hackingStyles[i].name();
		}

		hackingStyleCombo.setItems(hackinStyleEnumStrings);
		hackingStyleCombo.setBounds(111, 1, 205, 23);

		Composite RootNodeComposite = new Composite(OptionsGroup, SWT.NONE);
		RootNodeComposite.setBounds(10, 112, 304, 24);

		Label rootNodeLabel = new Label(RootNodeComposite, SWT.NONE);
		rootNodeLabel.setBounds(0, 0, 147, 24);
		rootNodeLabel
				.setText(ResourceBundle
						.getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.rootNodeLabel.text")); //$NON-NLS-1$ //$NON-NLS-2$

		rootNodeTextbox = new Text(RootNodeComposite, SWT.BORDER | SWT.CENTER);
		rootNodeTextbox
				.setText(ResourceBundle
						.getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.inputTextbox.text")); //$NON-NLS-1$ //$NON-NLS-2$
		rootNodeTextbox.setBounds(176, 0, 128, 24);

		// Building Items List for hackingStyleCombo
		GenerationStyle[] genStyles = GenerationStyle.values();
		String[] genStyleEnumStrings = new String[genStyles.length];

		for (int i = 0; i < genStyles.length; i++) {
			genStyleEnumStrings[i] = genStyles[i].name();
		}

		Group GenerateGroup = new Group(OptionsGroup, SWT.NONE);
		GenerateGroup
				.setText(ResourceBundle
						.getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.GenerateGroup.text")); //$NON-NLS-1$ //$NON-NLS-2$
		GenerateGroup.setBounds(354, 72, 336, 186);

		generateInputCheckBox = new Button(GenerateGroup, SWT.CHECK
				| SWT.CENTER);
		generateInputCheckBox.setLocation(10, 24);
		generateInputCheckBox.setSize(304, 24);
		generateInputCheckBox
				.setText(ResourceBundle
						.getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.GenerateInputCheckBox.text")); //$NON-NLS-1$ //$NON-NLS-2$

		Composite GenerarationStyleComposite = new Composite(GenerateGroup,
				SWT.NONE);
		GenerarationStyleComposite.setLocation(10, 61);
		GenerarationStyleComposite.setSize(304, 24);

		generationStyleCombo = new Combo(GenerarationStyleComposite,
				SWT.READ_ONLY);

		generationStyleCombo.setItems(genStyleEnumStrings);

		generationStyleCombo.setBounds(123, 0, 181, 23);

		Label GenerationStyleLabel = new Label(GenerarationStyleComposite,
				SWT.NONE);
		GenerationStyleLabel.setLocation(0, 0);
		GenerationStyleLabel.setSize(103, 24);
		GenerationStyleLabel
				.setText(ResourceBundle
						.getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.GenerationStyleLabel.text")); //$NON-NLS-1$ //$NON-NLS-2$

		Composite SMCountComposite = new Composite(GenerateGroup, SWT.NONE);
		SMCountComposite.setLocation(10, 106);
		SMCountComposite.setSize(314, 24);

		Label SmartMeterLabel = new Label(SMCountComposite, SWT.NONE);
		SmartMeterLabel.setBounds(0, 0, 152, 24);
		SmartMeterLabel
				.setText(ResourceBundle
						.getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.SmartMeterLabel.text")); //$NON-NLS-1$ //$NON-NLS-2$

		smartMeterCountTextBox = new Text(SMCountComposite, SWT.BORDER);
		smartMeterCountTextBox
				.setText(ResourceBundle
						.getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.inputTextbox.text")); //$NON-NLS-1$ //$NON-NLS-2$
		smartMeterCountTextBox.setBounds(158, 0, 156, 24);

		Composite CCCountComposite = new Composite(GenerateGroup, SWT.NONE);
		CCCountComposite.setLocation(10, 146);
		CCCountComposite.setSize(314, 24);

		Label ControlCenterLabel = new Label(CCCountComposite, SWT.NONE);
		ControlCenterLabel
				.setText(ResourceBundle
						.getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.ControlCenterLabel.text")); //$NON-NLS-1$ //$NON-NLS-2$
		ControlCenterLabel.setBounds(0, 0, 152, 24);

		controlCenterCountTextBox = new Text(CCCountComposite, SWT.BORDER);
		controlCenterCountTextBox
				.setText(ResourceBundle
						.getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.inputTextbox.text")); //$NON-NLS-1$ //$NON-NLS-2$
		controlCenterCountTextBox.setBounds(158, 0, 156, 24);
		rootNodeTextbox.addModifyListener(e -> propertyChanged());

		hackingStyleCombo.addModifyListener(e -> propertyChanged());

		SelectOutputPathButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// Get Text to show in Open File Dialog from Resource File
				final String message = ResourceBundle
						.getBundle("smartgrid.simcontrol.ui.messages")
						.getString(
								"SimControlLaunchConfigurationTab.OpenFileDialog.SELECT_OUTPUT_PATH");
				// String outputPath = getFilePath(message,
				// Constants.OUTPUT_EXTENSION);
				String outputPath = getDirPath(message);

				if (outputPath != null)
					outputTextbox.setText(outputPath);
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
		SelectTopologyPathButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// Get Text to show in Open File Dialog from Resource File
				final String message = ResourceBundle
						.getBundle("smartgrid.simcontrol.ui.messages")
						.getString(
								"SimControlLaunchConfigurationTab.OpenFileDialog.SELECT_INPUT_TOPOLOGY_PATH");
				String topologyPath = getFilePath(message,
						Constants.TOPO_EXTENSION);

				if (topologyPath != null)
					topologyTextbox.setText(topologyPath);
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
		SelectInputPathButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
				// Get Text to show in Open File Dialog from Resource File
				// Gets the Filepath
				final String message = ResourceBundle
						.getBundle("smartgrid.simcontrol.ui.messages")
						.getString(
								"SimControlLaunchConfigurationTab.OpenFileDialog.SELECT_INPUT_PATH");
				String inputPath = getFilePath(message,
						Constants.INPUT_EXTENSION);

				if (inputPath != null)
					inputTextbox.setText(inputPath);
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
	}

	/**
     * 
     * 
     */
	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(Constants.TOPOLOGY_PATH_KEY,
				Constants.DEFAULT_TOPO_PATH);
		configuration.setAttribute(Constants.INPUT_PATH_KEY,
				Constants.DEFAULT_INPUT_PATH);
		configuration.setAttribute(Constants.OUTPUT_PATH_KEY,
				Constants.DEFAULT_OUTPUT_PATH);
		configuration.setAttribute(Constants.TIMESTEPS_KEY,
				Constants.DEFAULT_TIME_STEPS);
		configuration.setAttribute(Constants.IGNORE_LOC_CON_KEY,
				Constants.DEFAULT_IGNORE_LOC_CON);
		configuration.setAttribute(Constants.ROOT_NODE_ID_KEY,
				Constants.DEFAULT_ROOT_NODE_ID);
		configuration.setAttribute(Constants.GEN_SYNTHETIC_INPUT_KEY,
				Constants.DEFAULT_GEN_SYNTHETIC_INPUT);
		configuration.setAttribute(Constants.GENERATION_STYLE_KEY,
				Constants.DEFAULT_GENERATION_STYLE);
		configuration.setAttribute(Constants.SMART_METER_COUNT_KEY,
				Constants.DEFAULT_SMART_METER_COUNT);
		configuration.setAttribute(Constants.CONTROL_CENTER_COUNT_KEY,
				Constants.DEFAULT_CONTROL_CENTER_COUNT);

	}

	/**
     * 
     */
	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			String topoPath = configuration.getAttribute(
					Constants.TOPOLOGY_PATH_KEY, Constants.DEFAULT_TOPO_PATH);
			String inpPath = configuration.getAttribute(
					Constants.INPUT_PATH_KEY, Constants.DEFAULT_INPUT_PATH);
			String outPath = configuration.getAttribute(
					Constants.OUTPUT_PATH_KEY, Constants.DEFAULT_OUTPUT_PATH);
			String timeSteps = configuration.getAttribute(
					Constants.TIMESTEPS_KEY, Constants.DEFAULT_TIME_STEPS);
			String logiCon = configuration.getAttribute(
					Constants.IGNORE_LOC_CON_KEY,
					Constants.DEFAULT_IGNORE_LOC_CON);
			String rootNodeID = configuration.getAttribute(
					Constants.ROOT_NODE_ID_KEY, Constants.DEFAULT_ROOT_NODE_ID);

			String hackingStyleS = configuration.getAttribute(
					Constants.HACKING_STYLE_KEY,
					Constants.DEFAULT_HACKING_STYLE);

			String genInputString = configuration.getAttribute(
					Constants.GEN_SYNTHETIC_INPUT_KEY,
					Constants.DEFAULT_GEN_SYNTHETIC_INPUT);

			String genStyleString = configuration.getAttribute(
					Constants.GENERATION_STYLE_KEY,
					Constants.DEFAULT_GENERATION_STYLE);

			String smartCountString = configuration.getAttribute(
					Constants.SMART_METER_COUNT_KEY,
					Constants.DEFAULT_ROOT_NODE_ID);

			String controlCountString = configuration.getAttribute(
					Constants.CONTROL_CENTER_COUNT_KEY,
					Constants.DEFAULT_CONTROL_CENTER_COUNT);

			// Setting to UI Elementes
			topologyTextbox.setText(topoPath);
			inputTextbox.setText(inpPath);
			outputTextbox.setText(outPath);
			timeStepsTextBox.setText(timeSteps);
			rootNodeTextbox.setText(rootNodeID);
			hackingStyleCombo.setText(hackingStyleS);
			generationStyleCombo.setText(genStyleString);
			smartMeterCountTextBox.setText(smartCountString);
			controlCenterCountTextBox.setText(controlCountString);

			ignoreLogicalConButton.setSelection(logiCon
					.contentEquals(Constants.TRUE));

			generateInputCheckBox.setSelection(genInputString
					.contentEquals(Constants.TRUE));

		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/**
     * 
     */
	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {

		// Text Box reading

		String topoPath = topologyTextbox.getText();
		String inPath = inputTextbox.getText();
		String outPath = outputTextbox.getText();
		String timeSteps = timeStepsTextBox.getText();
		String rNodeID = rootNodeTextbox.getText();
		String smartCount = smartMeterCountTextBox.getText();
		String controlCount = controlCenterCountTextBox.getText();

		// Check Box Parsing

		String logiCon;
		if (ignoreLogicalConButton.getSelection()) {
			logiCon = Constants.TRUE;
		} else {
			logiCon = Constants.FALSE;
		}

		String genInput;
		if (generateInputCheckBox.getSelection()) {
			genInput = Constants.TRUE;
		} else {
			genInput = Constants.FALSE;
		}

		// Combo Box Parsing

		String hackingStyleString = hackingStyleCombo.getItem(hackingStyleCombo
				.getSelectionIndex());

		String generationStyleString = generationStyleCombo
				.getItem(generationStyleCombo.getSelectionIndex());

		// Add to config
		configuration.setAttribute(Constants.TOPOLOGY_PATH_KEY, topoPath);
		configuration.setAttribute(Constants.INPUT_PATH_KEY, inPath);
		configuration.setAttribute(Constants.OUTPUT_PATH_KEY, outPath);
		configuration.setAttribute(Constants.TIMESTEPS_KEY, timeSteps);
		configuration.setAttribute(Constants.IGNORE_LOC_CON_KEY, logiCon);
		configuration.setAttribute(Constants.ROOT_NODE_ID_KEY, rNodeID);
		configuration.setAttribute(Constants.HACKING_STYLE_KEY,
				hackingStyleString);
		configuration.setAttribute(Constants.GEN_SYNTHETIC_INPUT_KEY, genInput);
		configuration.setAttribute(Constants.GENERATION_STYLE_KEY,
				generationStyleString);
		configuration.setAttribute(Constants.SMART_METER_COUNT_KEY, smartCount);
		configuration.setAttribute(Constants.CONTROL_CENTER_COUNT_KEY,
				controlCount);

		// Now Tab is Clean again
		this.setDirty(false);
	}

	// @Override
	// public String getMessage() {
	// return "42";
	// }

	/*
	 * This Methods shows Error Messages AND NO Error Messages !!
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.debug.ui.AbstractLaunchConfigurationTab#getErrorMessage()
	 */

	/**
     * 
     */
	@Override
	public String getErrorMessage() {

		String Message = "";

		if (areTextBoxesEmpty())
			Message = ResourceBundle
					.getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.ErrorMessages.NO_PATH"); //$NON-NLS-1$ //$NON-NLS-2$
		else if (areFileExtensionsWrong())
			Message = ResourceBundle
					.getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.ErrorMessages.WRONG_EXTENSIONS"); //$NON-NLS-1$ //$NON-NLS-2$
		else if (areOptionsWrong()) {

			Message = ResourceBundle
					.getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.ErrorMessages.WRONG_OPTIONS"); //$NON-NLS-1$ //$NON-NLS-2$

		} else {

			// NO Error Case !!
			return null;
			// NO Error Case
		}


		return Message;
	}

	/**
     * 
     */
	@Override
	public boolean isValid(ILaunchConfiguration launchConfig) {
		// Add launchConfig Validation Checks here if needed
		return validateInput();
	}

	/**
     * 
     */
	@Override
	public boolean canSave() {

		return validateInput();
	}

	@Override
	public String getName() {
		// Read Resource File
		return ResourceBundle
				.getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.Tab.RegisterText"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*
	 * Private Methods
	 */

	// @Override
	// public Image getImage() {
	// // TODO Insert small SimController Icon here
	// return null;
	// }

	/**
	 * property Changed
	 */
	/* Visibility package to avoid synthetic methods */
	void propertyChanged() {
		this.setDirty(true);
		this.updateLaunchConfigurationDialog();

	}

	/**
	 * @return True if all Input Parameter are correct
	 */
	private boolean validateInput() {
		return !areTextBoxesEmpty() && !areFileExtensionsWrong()
				&& !areOptionsWrong();
	}

	/*
	 * Opens OpenFileDialog und returns the Filepath as String
	 */
	private String getFilePath(String dialogMessage, String extension) {

		final FileDialog dialog = new FileDialog(this.container.getShell(),
				SWT.OPEN);

		dialog.setText(dialogMessage);
		final String extensionFilter = "*." + extension;
		dialog.setFilterExtensions(new String[] { extensionFilter });
		String filename = dialog.open();

		return filename;
	}

	/*
	 * Opens the Directory Dialog Chooser
	 */
	private String getDirPath(String dialogMessage) {

		final DirectoryDialog dialog = new DirectoryDialog(
				this.container.getShell(), SWT.OPEN);

		dialog.setMessage(dialogMessage);

		// For User Convinience jump to Input Files Location
		if (!inputTextbox.getText().isEmpty()) {
			dialog.setFilterPath(new File(inputTextbox.getText()).getParent());

		}

		String dirName = new File(dialog.open()).getPath();

		return dirName;
	}

	private boolean areTextBoxesEmpty() {
		final boolean topoEmpty = topologyTextbox.getText().equals("");
		final boolean inEmpty = inputTextbox.getText().equals("");
		final boolean outEmpty = outputTextbox.getText().equals("");
		return topoEmpty || inEmpty || outEmpty;
	}

	/*
	 * 
	 * @return True if topology or input or output file extensions are wrong
	 */
	private boolean areFileExtensionsWrong() {
		final boolean topoWrong = !topologyTextbox.getText().endsWith(
				Constants.TOPO_EXTENSION);
		final boolean inWrong = !inputTextbox.getText().endsWith(
				Constants.INPUT_EXTENSION);

		/*
		 * --> Check wheter Output path is the same as inputpath So the Input
		 * Files and Output are on the same branch Needed because otherwise the
		 * Output Model gets broken
		 */
		final boolean outWrong = !outputTextbox.getText().contains(
				new File(inputTextbox.getText()).getParent());

		return topoWrong || inWrong || outWrong;
		// return false;
	}

	private boolean areOptionsWrong() {
		// TODO Add coming checks for the options section of the Ui here
		final boolean timeStepsWrong = !this.isUInt(this.timeStepsTextBox
				.getText());
		final boolean rootNodeWrong = !(this.isUInt(this.rootNodeTextbox
				.getText()) || this.rootNodeTextbox.getText().equals(
				Constants.DEFAULT_ROOT_NODE_ID));
		final boolean smartCountWrong = !this.isUInt(this.smartMeterCountTextBox.getText());
		
		final boolean controlCountWrong = !this.isUInt(this.controlCenterCountTextBox.getText());

		//return timeStepsWrong || rootNodeWrong || smartCountWrong || controlCountWrong;
		return false;
	}

	// @Override
	// public Image getImage() {
	// // TODO Insert small SimController Icon here
	// return null;
	// }

	/*
	 * 
	 * @param possibleInt the string to be tested to be an positive Integer
	 * 
	 * @return true if the string can be parsed as positive Integer
	 */
	private boolean isUInt(String possibleInt) {

		boolean valid;

		try {
			Integer.parseUnsignedInt(possibleInt);
			valid = true;
		} catch (NumberFormatException e) {
			valid = false;
		}

		return valid;
	}
}
