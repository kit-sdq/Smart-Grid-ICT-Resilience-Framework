package smartgrid.simcontrol.ui;

import java.io.File;
import java.util.List;
// Resource Files
import java.util.ResourceBundle;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
// OpenFile Diolog MS Style
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import smartgrid.helper.ReceiveAnalysesHelper;
import smartgrid.simcontrol.baselib.Constants;
import smartgrid.simcontrol.baselib.GenerationStyle;
import smartgrid.simcontrol.baselib.HackingStyle;
import smartgrid.simcontrol.interfaces.IAttackerSimulation;
import smartgrid.simcontrol.interfaces.IImpactAnalysis;
import smartgrid.simcontrol.interfaces.IPowerLoadSimulation;
import smartgrid.simcontrol.interfaces.ITerminationCondition;
import smartgrid.simcontrol.interfaces.ITimeProgressor;

/**
 * This Class provides the SimController Configuration Tab
 * 
 * @author Christian (Modified by)
 * @extends {@link AbstractLaunchConfigurationTab}
 */
public class SimControlLaunchConfigurationTab extends AbstractLaunchConfigurationTab {

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

    // Comboboxes for Simulation settings
    Combo comboPowerLoadSimulation;
    Combo comboKritisSimulation;
    Combo comboImpactAnalysis;
    Combo comboAttackSimulation;
    Combo comboTerminationCondition;
    Combo comboProgressor;
    private Text hackingSpeedText;

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
        fd_group.left = new FormAttachment(0, 10);
        fd_group.top = new FormAttachment(0, 10);
        fd_group.bottom = new FormAttachment(0, 123);
        group.setLayoutData(fd_group);

        group.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages").getString(
                "SimControlLaunchConfigurationTab.group.text"));

        inputTextbox = new Text(group, SWT.READ_ONLY | SWT.H_SCROLL | SWT.CANCEL);
        inputTextbox.setTouchEnabled(true);

        inputTextbox.setBounds(10, 22, 553, 41);
        inputTextbox.addModifyListener(e -> propertyChanged());
        inputTextbox.setEditable(true);
        inputTextbox.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages").getString(
                "SimControlLaunchConfigurationTab.inputTextbox.text"));
        org.eclipse.swt.widgets.Button SelectInputPathButton = new org.eclipse.swt.widgets.Button(group, SWT.TOGGLE);
        SelectInputPathButton.setTouchEnabled(true);
        SelectInputPathButton.setBounds(569, 22, 146, 32);

        SelectInputPathButton.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages").getString(
                "SimControlLaunchConfigurationTab.SelectInputPathButton.text"));

        topologyTextbox = new Text(group, SWT.READ_ONLY | SWT.H_SCROLL | SWT.CANCEL);
        topologyTextbox.setTouchEnabled(true);
        topologyTextbox.setBounds(10, 69, 553, 41);
        topologyTextbox.addModifyListener(e -> propertyChanged());
        topologyTextbox.setEditable(true);
        topologyTextbox
                .setText(ResourceBundle
                        .getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.inputTextbox.text")); //$NON-NLS-1$ //$NON-NLS-2$

        org.eclipse.swt.widgets.Button SelectTopologyPathButton = new org.eclipse.swt.widgets.Button(group, SWT.TOGGLE);
        SelectTopologyPathButton.setTouchEnabled(true);
        SelectTopologyPathButton.setBounds(569, 60, 153, 32);
        SelectTopologyPathButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            }
        });
        SelectTopologyPathButton.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages").getString(
                "SimControlLaunchConfigurationTab.SelectTopologyPathButton.text"));

        Group grpOutputFiles = new Group(container, SWT.NONE);
        FormData fd_grpOutputFiles = new FormData();
        fd_grpOutputFiles.left = new FormAttachment(group, 0, SWT.LEFT);
        fd_grpOutputFiles.bottom = new FormAttachment(group, 103, SWT.BOTTOM);
        fd_grpOutputFiles.top = new FormAttachment(group, 6);
        fd_grpOutputFiles.right = new FormAttachment(group, 0, SWT.RIGHT);
        grpOutputFiles.setLayoutData(fd_grpOutputFiles);

        grpOutputFiles.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages").getString(
                "SimControlLaunchConfigurationTab.grpOutputFiles.text"));
        org.eclipse.swt.widgets.Button SelectOutputPathButton = new org.eclipse.swt.widgets.Button(grpOutputFiles,
                SWT.TOGGLE);
        SelectOutputPathButton.setTouchEnabled(true);
        SelectOutputPathButton.setBounds(569, 22, 149, 32);
        SelectOutputPathButton.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages").getString(
                "SimControlLaunchConfigurationTab.SelectOutputPathButton.text"));

        outputTextbox = new Text(grpOutputFiles, SWT.READ_ONLY | SWT.H_SCROLL | SWT.CANCEL);
        outputTextbox.setTouchEnabled(true);
        outputTextbox.setEditable(true);
        outputTextbox.setBounds(10, 22, 553, 41);
        outputTextbox.addModifyListener(e -> propertyChanged());
        outputTextbox.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages").getString(
                "SimControlLaunchConfigurationTab.inputTextbox.text"));

        Group OptionsGroup = new Group(container, SWT.NONE);

        Button copyPathButton = new Button(grpOutputFiles, SWT.NONE);
        copyPathButton.setTouchEnabled(true);
        copyPathButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseUp(MouseEvent e) {
            }

            @Override
            public void mouseDown(MouseEvent e) {

                // Copy Inputpath as Output Dir
                outputTextbox.setText(new File(inputTextbox.getText()).getParent());
            }
        });
        copyPathButton.setBounds(10, 62, 189, 32);
        copyPathButton
                .setText(ResourceBundle
                        .getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.copyPathButton.text")); //$NON-NLS-1$ //$NON-NLS-2$
        OptionsGroup.setLayout(null);
        FormData fd_OptionsGroup = new FormData();
        fd_OptionsGroup.left = new FormAttachment(group, 0, SWT.LEFT);
        fd_OptionsGroup.bottom = new FormAttachment(100, -10);
        fd_OptionsGroup.right = new FormAttachment(100);
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

        ignoreLogicalConButton = new Button(OptionsGroup, SWT.CHECK | SWT.CENTER);
        ignoreLogicalConButton.setTouchEnabled(true);

        ignoreLogicalConButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                // ignoreLogicalConnections =
                // ignoreLogicalConButton.getEnabled();
                propertyChanged();

                if (ignoreLogicalConButton.getSelection()) {
                    hackingStyleCombo.setText(HackingStyle.FULLY_MESHED_HACKING.name());
                }

            }
        });

        ignoreLogicalConButton.setBounds(10, 63, 304, 24);
        ignoreLogicalConButton
                .setText(ResourceBundle
                        .getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.ignoreLogicalConButton.text")); //$NON-NLS-1$ //$NON-NLS-2$

        // Building Items List for hackingStyleCombo
        HackingStyle[] hackingStyles = HackingStyle.values();
        String[] hackinStyleEnumStrings = new String[hackingStyles.length];

        for (int i = 0; i < hackingStyles.length; i++) {
            hackinStyleEnumStrings[i] = hackingStyles[i].name();
        }

        // Building Items List for hackingStyleCombo
        GenerationStyle[] genStyles = GenerationStyle.values();
        String[] genStyleEnumStrings = new String[genStyles.length];

        for (int i = 0; i < genStyles.length; i++) {
            genStyleEnumStrings[i] = genStyles[i].name();
        }

        Group GenerateGroup = new Group(OptionsGroup, SWT.NONE);
        GenerateGroup.setEnabled(true);
        GenerateGroup
                .setText(ResourceBundle
                        .getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.GenerateGroup.text")); //$NON-NLS-1$ //$NON-NLS-2$
        GenerateGroup.setBounds(10, 219, 680, 186);

        generateInputCheckBox = new Button(GenerateGroup, SWT.CHECK | SWT.CENTER);
        generateInputCheckBox.setEnabled(false);
        generateInputCheckBox.setLocation(10, 24);
        generateInputCheckBox.setSize(304, 24);
        generateInputCheckBox
                .setText(ResourceBundle
                        .getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.GenerateInputCheckBox.text")); //$NON-NLS-1$ //$NON-NLS-2$

        Composite GenerarationStyleComposite = new Composite(GenerateGroup, SWT.NONE);
        GenerarationStyleComposite.setLocation(10, 61);
        GenerarationStyleComposite.setSize(304, 24);

        generationStyleCombo = new Combo(GenerarationStyleComposite, SWT.READ_ONLY);
        generationStyleCombo.setEnabled(false);

        generationStyleCombo.setItems(genStyleEnumStrings);

        generationStyleCombo.setBounds(123, 0, 181, 23);

        Label GenerationStyleLabel = new Label(GenerarationStyleComposite, SWT.NONE);
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
        smartMeterCountTextBox.setEnabled(false);

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
        controlCenterCountTextBox.setEnabled(false);

        Group grpAnalyses = new Group(container, SWT.NONE);
        fd_OptionsGroup.top = new FormAttachment(grpAnalyses, 6);

        Group grpCyberAttackSimulation = new Group(OptionsGroup, SWT.NONE);
        grpCyberAttackSimulation.setBounds(10, 93, 680, 120);
        grpCyberAttackSimulation.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages").getString(
                "SimControlLaunchConfigurationTab.grpCyberAttackSimulation.text"));

        Composite HackingStyleComposite = new Composite(grpCyberAttackSimulation, SWT.NONE);
        HackingStyleComposite.setBounds(10, 24, 660, 24);

        Label hackingStyleLabel = new Label(HackingStyleComposite, SWT.NONE);
        hackingStyleLabel.setBounds(0, 0, 131, 24);
        hackingStyleLabel.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages").getString(
                "SimControlLaunchConfigurationTab.hackingStyleLabel.text"));

        hackingStyleCombo = new Combo(HackingStyleComposite, SWT.READ_ONLY);
        hackingStyleCombo.setBounds(137, -3, 329, 28);
        hackingStyleCombo.setTouchEnabled(true);

        hackingStyleCombo.setItems(hackinStyleEnumStrings);

        Composite RootNodeComposite = new Composite(grpCyberAttackSimulation, SWT.NONE);
        RootNodeComposite.setBounds(10, 54, 660, 24);

        rootNodeTextbox = new Text(RootNodeComposite, SWT.BORDER | SWT.CENTER);
        rootNodeTextbox
                .setText(ResourceBundle
                        .getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.inputTextbox.text")); //$NON-NLS-1$ //$NON-NLS-2$
        rootNodeTextbox.setBounds(176, 0, 128, 24);

        Label rootNodeLabel = new Label(RootNodeComposite, SWT.NONE);
        rootNodeLabel.setBounds(0, 0, 147, 24);
        rootNodeLabel
                .setText(ResourceBundle
                        .getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.rootNodeLabel.text")); //$NON-NLS-1$ //$NON-NLS-2$

        Composite hackingSpeedComposite = new Composite(grpCyberAttackSimulation, SWT.NONE);
        hackingSpeedComposite.setBounds(10, 88, 660, 29);

        Label labelHackingSpeed = new Label(hackingSpeedComposite, SWT.NONE);
        labelHackingSpeed.setBounds(0, 0, 147, 24);
        labelHackingSpeed
                .setText(ResourceBundle
                        .getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.labelHackingSpeed.text")); //$NON-NLS-1$ //$NON-NLS-2$

        hackingSpeedText = new Text(hackingSpeedComposite, SWT.BORDER | SWT.CENTER);
        hackingSpeedText.setText("[Sample Remove]");
        hackingSpeedText.setBounds(175, -3, 128, 24);
        hackingSpeedText.addModifyListener(e -> propertyChanged());

        rootNodeTextbox.addModifyListener(e -> propertyChanged());

        hackingStyleCombo.addModifyListener(e -> propertyChanged());
        grpAnalyses
                .setText(ResourceBundle
                        .getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.grpAnalyses.text")); //$NON-NLS-1$ //$NON-NLS-2$
        FormData fd_grpAnalyses = new FormData();
        fd_grpAnalyses.bottom = new FormAttachment(grpOutputFiles, 209, SWT.BOTTOM);
        fd_grpAnalyses.top = new FormAttachment(grpOutputFiles, 6);
        fd_grpAnalyses.left = new FormAttachment(0, 10);
        fd_grpAnalyses.right = new FormAttachment(0, 735);
        grpAnalyses.setLayoutData(fd_grpAnalyses);

        Label lblNewLabel = new Label(grpAnalyses, SWT.NONE);
        lblNewLabel.setBounds(10, 24, 156, 20);
        lblNewLabel
                .setText(ResourceBundle
                        .getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.lblNewLabel.text_1")); //$NON-NLS-1$ //$NON-NLS-2$

        Label lblKritisSimulation = new Label(grpAnalyses, SWT.NONE);
        lblKritisSimulation.setBounds(10, 53, 137, 23);
        lblKritisSimulation
                .setText(ResourceBundle
                        .getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.lblKritisSimulation.text")); //$NON-NLS-1$ //$NON-NLS-2$

        Label lblImpactAnalysis = new Label(grpAnalyses, SWT.NONE);
        lblImpactAnalysis.setBounds(10, 82, 137, 23);
        lblImpactAnalysis
                .setText(ResourceBundle
                        .getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.lblImpactAnalysis.text")); //$NON-NLS-1$ //$NON-NLS-2$

        Label lblCyberAttackSimulation = new Label(grpAnalyses, SWT.NONE);
        lblCyberAttackSimulation.setBounds(10, 111, 137, 23);
        lblCyberAttackSimulation
                .setText(ResourceBundle
                        .getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.lblCyberAttackSimulation.text")); //$NON-NLS-1$ //$NON-NLS-2$

        Label lblTerminationCondition = new Label(grpAnalyses, SWT.NONE);
        lblTerminationCondition.setBounds(10, 140, 137, 23);
        lblTerminationCondition
                .setText(ResourceBundle
                        .getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.lblTerminationCondition.text")); //$NON-NLS-1$ //$NON-NLS-2$

        Label lblProgressor = new Label(grpAnalyses, SWT.NONE);
        lblProgressor.setBounds(10, 169, 137, 25);
        lblProgressor
                .setText(ResourceBundle
                        .getBundle("smartgrid.simcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.lblProgressor.text")); //$NON-NLS-1$ //$NON-NLS-2$

        comboPowerLoadSimulation = new Combo(grpAnalyses, SWT.READ_ONLY);
        comboPowerLoadSimulation.setBounds(213, 21, 202, 23);
        comboPowerLoadSimulation.addModifyListener(e -> propertyChanged());

        comboKritisSimulation = new Combo(grpAnalyses, SWT.READ_ONLY);
        comboKritisSimulation.setBounds(213, 50, 202, 23);
        comboKritisSimulation.addModifyListener(e -> propertyChanged());

        comboImpactAnalysis = new Combo(grpAnalyses, SWT.READ_ONLY);
        comboImpactAnalysis.setBounds(213, 79, 202, 23);
        comboImpactAnalysis.addModifyListener(e -> propertyChanged());

        comboAttackSimulation = new Combo(grpAnalyses, SWT.READ_ONLY);
        comboAttackSimulation.setBounds(213, 108, 202, 23);
        comboAttackSimulation.addModifyListener(e -> propertyChanged());

        comboTerminationCondition = new Combo(grpAnalyses, SWT.READ_ONLY);
        comboTerminationCondition.setBounds(213, 137, 202, 23);
        comboTerminationCondition.addModifyListener(e -> propertyChanged());

        comboProgressor = new Combo(grpAnalyses, SWT.READ_ONLY);
        comboProgressor.setBounds(213, 166, 202, 23);
        comboProgressor.addModifyListener(e -> propertyChanged());

        addElementsToCombos();

        SelectOutputPathButton.addMouseListener(new MouseListener() {

            @Override
            public void mouseUp(MouseEvent e) {
            }

            @Override
            public void mouseDown(MouseEvent e) {
                // Get Text to show in Open File Dialog from Resource File
                final String message = ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages").getString(
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
                final String message = ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages").getString(
                        "SimControlLaunchConfigurationTab.OpenFileDialog.SELECT_INPUT_TOPOLOGY_PATH");
                String topologyPath = getFilePath(message, Constants.TOPO_EXTENSION);

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
                final String message = ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages").getString(
                        "SimControlLaunchConfigurationTab.OpenFileDialog.SELECT_INPUT_PATH");
                String inputPath = getFilePath(message, Constants.INPUT_EXTENSION);

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
        configuration.setAttribute(Constants.TOPOLOGY_PATH_KEY, Constants.DEFAULT_TOPO_PATH);
        configuration.setAttribute(Constants.INPUT_PATH_KEY, Constants.DEFAULT_INPUT_PATH);
        configuration.setAttribute(Constants.OUTPUT_PATH_KEY, Constants.DEFAULT_OUTPUT_PATH);
        configuration.setAttribute(Constants.TIMESTEPS_KEY, Constants.DEFAULT_TIME_STEPS);
        configuration.setAttribute(Constants.IGNORE_LOC_CON_KEY, Constants.DEFAULT_IGNORE_LOC_CON);
        configuration.setAttribute(Constants.ROOT_NODE_ID_KEY, Constants.DEFAULT_ROOT_NODE_ID);
        configuration.setAttribute(Constants.GEN_SYNTHETIC_INPUT_KEY, Constants.DEFAULT_GEN_SYNTHETIC_INPUT);
        configuration.setAttribute(Constants.GENERATION_STYLE_KEY, Constants.DEFAULT_GENERATION_STYLE);
        configuration.setAttribute(Constants.SMART_METER_COUNT_KEY, Constants.DEFAULT_SMART_METER_COUNT);
        configuration.setAttribute(Constants.CONTROL_CENTER_COUNT_KEY, Constants.DEFAULT_CONTROL_CENTER_COUNT);
        configuration.setAttribute(Constants.HACKING_SPEED_KEY, Constants.DEFAULT_HACKING_SPEED);
    }

    /**
     * 
     */
    @Override
    public void initializeFrom(ILaunchConfiguration configuration) {
        try {
            String topoPath = configuration.getAttribute(Constants.TOPOLOGY_PATH_KEY, Constants.DEFAULT_TOPO_PATH);
            String inpPath = configuration.getAttribute(Constants.INPUT_PATH_KEY, Constants.DEFAULT_INPUT_PATH);
            String outPath = configuration.getAttribute(Constants.OUTPUT_PATH_KEY, Constants.DEFAULT_OUTPUT_PATH);
            String timeSteps = configuration.getAttribute(Constants.TIMESTEPS_KEY, Constants.DEFAULT_TIME_STEPS);
            String logiCon = configuration.getAttribute(Constants.IGNORE_LOC_CON_KEY, Constants.DEFAULT_IGNORE_LOC_CON);
            String rootNodeID = configuration.getAttribute(Constants.ROOT_NODE_ID_KEY, Constants.DEFAULT_ROOT_NODE_ID);

            String hackingStyleS = configuration.getAttribute(Constants.HACKING_STYLE_KEY,
                    Constants.DEFAULT_HACKING_STYLE);

            String genInputString = configuration.getAttribute(Constants.GEN_SYNTHETIC_INPUT_KEY,
                    Constants.DEFAULT_GEN_SYNTHETIC_INPUT);

            String genStyleString = configuration.getAttribute(Constants.GENERATION_STYLE_KEY,
                    Constants.DEFAULT_GENERATION_STYLE);

            String smartCountString = configuration.getAttribute(Constants.SMART_METER_COUNT_KEY,
                    Constants.DEFAULT_ROOT_NODE_ID);

            String controlCountString = configuration.getAttribute(Constants.CONTROL_CENTER_COUNT_KEY,
                    Constants.DEFAULT_CONTROL_CENTER_COUNT);

            // Setting to UI Elementes
            hackingSpeedText.setText(configuration.getAttribute(Constants.HACKING_SPEED_KEY,
                    Constants.DEFAULT_HACKING_SPEED));
            topologyTextbox.setText(topoPath);
            inputTextbox.setText(inpPath);
            outputTextbox.setText(outPath);
            timeStepsTextBox.setText(timeSteps);
            rootNodeTextbox.setText(rootNodeID);
            hackingStyleCombo.setText(hackingStyleS);
            generationStyleCombo.setText(genStyleString);
            smartMeterCountTextBox.setText(smartCountString);
            controlCenterCountTextBox.setText(controlCountString);

            ignoreLogicalConButton.setSelection(logiCon.contentEquals(Constants.TRUE));

            generateInputCheckBox.setSelection(genInputString.contentEquals(Constants.TRUE));

            String attackSimulationString = configuration.getAttribute(Constants.ATTACKER_SIMULATION_CONFIG, "");
            for (int i = 0; i < comboAttackSimulation.getItems().length; i++) {
                if (comboAttackSimulation.getItem(i).equals(attackSimulationString)) {
                    comboAttackSimulation.select(i);
                    break;
                }
            }
            String powerLoadSimulationString = configuration.getAttribute(Constants.POWER_LOAD_SIMULATION_CONFIG, "");
            for (int i = 0; i < comboPowerLoadSimulation.getItems().length; i++) {
                if (comboPowerLoadSimulation.getItem(i).equals(powerLoadSimulationString)) {
                    comboPowerLoadSimulation.select(i);
                    break;
                }
            }
            String impactAnalysisString = configuration.getAttribute(Constants.IMPACT_ANALYSIS_SIMULATION_CONFIG, "");
            for (int i = 0; i < comboImpactAnalysis.getItems().length; i++) {
                if (comboPowerLoadSimulation.getItem(i).equals(impactAnalysisString)) {
                    comboPowerLoadSimulation.select(i);
                    break;
                }
            }
            String terminationConditionString = configuration.getAttribute(Constants.TERMINATION_CONDITION_SIMULATION_CONFIG, "");
            for (int i = 0; i < comboTerminationCondition.getItems().length; i++) {
                if (comboTerminationCondition.getItem(i).equals(terminationConditionString)) {
                    comboTerminationCondition.select(i);
                    break;
                }
            }
            String progressorString = configuration.getAttribute(Constants.TIME_PROGRESSOR_SIMULATION_CONFIG, "");
            for (int i = 0; i < comboProgressor.getItems().length; i++) {
                if (comboProgressor.getItem(i).equals(progressorString)) {
                    comboProgressor.select(i);
                    break;
                }
            }

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

        String hackingStyleString = hackingStyleCombo.getItem(hackingStyleCombo.getSelectionIndex());

        String generationStyleString = generationStyleCombo.getItem(generationStyleCombo.getSelectionIndex());

        // Add to config
        configuration.setAttribute(Constants.TOPOLOGY_PATH_KEY, topoPath);
        configuration.setAttribute(Constants.INPUT_PATH_KEY, inPath);
        configuration.setAttribute(Constants.OUTPUT_PATH_KEY, outPath);
        configuration.setAttribute(Constants.TIMESTEPS_KEY, timeSteps);
        configuration.setAttribute(Constants.IGNORE_LOC_CON_KEY, logiCon);
        configuration.setAttribute(Constants.ROOT_NODE_ID_KEY, rNodeID);
        configuration.setAttribute(Constants.HACKING_STYLE_KEY, hackingStyleString);
        configuration.setAttribute(Constants.GEN_SYNTHETIC_INPUT_KEY, genInput);
        configuration.setAttribute(Constants.GENERATION_STYLE_KEY, generationStyleString);
        configuration.setAttribute(Constants.SMART_METER_COUNT_KEY, smartCount);
        configuration.setAttribute(Constants.CONTROL_CENTER_COUNT_KEY, controlCount);
        configuration.setAttribute(Constants.HACKING_SPEED_KEY, hackingSpeedText.getText());

        if (comboAttackSimulation.getSelectionIndex() != -1) {
            configuration.setAttribute(Constants.ATTACKER_SIMULATION_CONFIG,
                    comboAttackSimulation.getItem(comboAttackSimulation.getSelectionIndex()));
        }
        if (comboPowerLoadSimulation.getSelectionIndex() != -1) {
            configuration.setAttribute(Constants.POWER_LOAD_SIMULATION_CONFIG,
                    comboPowerLoadSimulation.getItem(comboPowerLoadSimulation.getSelectionIndex()));
        }
        if (comboTerminationCondition.getSelectionIndex() != -1) {
            configuration.setAttribute(Constants.TERMINATION_CONDITION_SIMULATION_CONFIG,
                    comboTerminationCondition.getItem(comboTerminationCondition.getSelectionIndex()));
        }
        if (comboProgressor.getSelectionIndex() != -1) {
            configuration.setAttribute(Constants.TIME_PROGRESSOR_SIMULATION_CONFIG,
                    comboProgressor.getItem(comboProgressor.getSelectionIndex()));
        }
        if (comboImpactAnalysis.getSelectionIndex() != -1) {
            configuration.setAttribute(Constants.IMPACT_ANALYSIS_SIMULATION_CONFIG,
                    comboImpactAnalysis.getItem(comboImpactAnalysis.getSelectionIndex()));
        }
        if (comboKritisSimulation.getSelectionIndex() != -1) {
            // configuration.setAttribute(Constants., value);
        }

        // Now Tab is Clean again
        this.setDirty(false);
    }

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
        return !areTextBoxesEmpty() && !areFileExtensionsWrong() && !areOptionsWrong();
    }

    /*
     * Opens OpenFileDialog und returns the Filepath as String
     */
    private String getFilePath(String dialogMessage, String extension) {

        final FileDialog dialog = new FileDialog(this.container.getShell(), SWT.OPEN);

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

        final DirectoryDialog dialog = new DirectoryDialog(this.container.getShell(), SWT.OPEN);

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
        final boolean topoWrong = !topologyTextbox.getText().endsWith(Constants.TOPO_EXTENSION);
        final boolean inWrong = !inputTextbox.getText().endsWith(Constants.INPUT_EXTENSION);

        /*
         * --> Check wheter Output path is the same as inputpath So the Input Files and Output are
         * on the same branch Needed because otherwise the Output Model gets broken
         */
        final boolean outWrong = !outputTextbox.getText().contains(new File(inputTextbox.getText()).getParent());

        return topoWrong || inWrong || outWrong;
        // return false;
    }

    private boolean areOptionsWrong() {
        // TODO Add coming checks for the options section of the Ui here
        final boolean timeStepsWrong = !this.isUInt(this.timeStepsTextBox.getText());
        final boolean rootNodeWrong = !(this.isUInt(this.rootNodeTextbox.getText()) || this.rootNodeTextbox.getText()
                .equals(Constants.DEFAULT_ROOT_NODE_ID));
        final boolean smartCountWrong = !this.isUInt(this.smartMeterCountTextBox.getText());

        final boolean controlCountWrong = !this.isUInt(this.controlCenterCountTextBox.getText());

        // return timeStepsWrong || rootNodeWrong || smartCountWrong || controlCountWrong;
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

    private void addElementsToCombos() {
        ReceiveAnalysesHelper helper = new ReceiveAnalysesHelper();

        try {
            addPowerLoadElements(helper);
            addAttackerSimulationElements(helper);
            addKritisSimulationElements(null);
            addTerminationConditionElements(helper);
            addProgressorElements(helper);
            addImpactAnalysisElements(helper);
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }

    private void addPowerLoadElements(ReceiveAnalysesHelper helper) throws CoreException {
        List<IPowerLoadSimulation> list = helper.getPowerLoadElements();
        for (IPowerLoadSimulation ele : list) {
            comboPowerLoadSimulation.add(ele.getName());
        }
        if (comboPowerLoadSimulation.getItemCount() > 0) {
            comboPowerLoadSimulation.select(0);
        }
    }

    private void addAttackerSimulationElements(ReceiveAnalysesHelper helper) throws CoreException {
        List<IAttackerSimulation> list = helper.getAttackerSimulationElements();
        for (IAttackerSimulation ele : list) {
            comboAttackSimulation.add(ele.getName());
        }
        if (comboAttackSimulation.getItemCount() > 0) {
            comboAttackSimulation.select(0);
        }
    }

    private void addKritisSimulationElements(IExtensionRegistry registry) {
        // TODO: Implement this when ready
        comboKritisSimulation.add("");
    }

    private void addTerminationConditionElements(ReceiveAnalysesHelper helper) throws CoreException {
        List<ITerminationCondition> list = helper.getTerminationConditionElements();
        for (ITerminationCondition ele : list) {
            comboTerminationCondition.add(ele.getName());
        }
        if (comboTerminationCondition.getItemCount() > 0) {
            comboTerminationCondition.select(0);
        }
    }

    private void addProgressorElements(ReceiveAnalysesHelper helper) throws CoreException {
        List<ITimeProgressor> list = helper.getProgressorElements();
        for (ITimeProgressor ele : list) {
            comboProgressor.add(ele.getName());
        }
        if (comboProgressor.getItemCount() > 0) {
            comboProgressor.select(0);
        }
    }

    private void addImpactAnalysisElements(ReceiveAnalysesHelper helper) throws CoreException {
        List<IImpactAnalysis> list = helper.getImpactAnalysisElements();
        for (IImpactAnalysis ele : list) {
            comboImpactAnalysis.add(ele.getName());
        }
        if (comboImpactAnalysis.getItemCount() > 0) {
            comboImpactAnalysis.select(0);
        }
    }
}
