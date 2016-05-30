package smartgrid.simcontrol.ui;

import java.io.File;
import java.util.List;
// Resource Files
import java.util.ResourceBundle;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
import smartgrid.simcontrol.interfaces.IKritisSimulation;
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
    private Text iterationCountTextbox;
    // private boolean ignoreLogicalConnections;
    private Text rootNodeTextbox;
    private Button ignoreLogicalConButton;
    private Combo hackingStyleCombo;
    private Combo generationStyleCombo;
    private Text smartMeterCountTextBox;
    private Text controlCenterCountTextBox;
    private Button generateInputCheckBox;

    // Comboboxes for Simulation settings
    private Combo comboPowerLoadSimulation;
    private Combo comboKritisSimulation;
    private Combo comboImpactAnalysis;
    private Combo comboAttackSimulation;
    private Combo comboTerminationCondition;
    private Combo comboProgressor;
    private Text hackingSpeedText;

    /**
     * Creates the UI Control
     * 
     * @wbp.parser.entryPoint
     */
    @Override
    public void createControl(Composite parent) {

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
        
        container = new Composite(parent, SWT.NONE);
        this.setControl(container);
        container.setLayout(new FormLayout());

        Group group = new Group(container, SWT.NONE);
        FormData fd_group = new FormData();
        fd_group.left = new FormAttachment(0, 10);
        fd_group.top = new FormAttachment(0, 10);
        fd_group.bottom = new FormAttachment(0, 123);
        group.setLayoutData(fd_group);

        group.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages")
                .getString("SimControlLaunchConfigurationTab.group.text"));

        inputTextbox = new Text(group, SWT.READ_ONLY | SWT.H_SCROLL | SWT.CANCEL);
        inputTextbox.setTouchEnabled(true);

        inputTextbox.setBounds(10, 22, 553, 41);
        inputTextbox.addModifyListener(e -> propertyChanged());
        inputTextbox.setEditable(true);
        inputTextbox.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages")
                .getString("SimControlLaunchConfigurationTab.inputTextbox.text"));
        org.eclipse.swt.widgets.Button SelectInputPathButton = new org.eclipse.swt.widgets.Button(group, SWT.TOGGLE);
        SelectInputPathButton.setTouchEnabled(true);
        SelectInputPathButton.setBounds(569, 22, 146, 32);

        SelectInputPathButton.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages")
                .getString("SimControlLaunchConfigurationTab.SelectInputPathButton.text"));

        topologyTextbox = new Text(group, SWT.READ_ONLY | SWT.H_SCROLL | SWT.CANCEL);
        topologyTextbox.setTouchEnabled(true);
        topologyTextbox.setBounds(10, 69, 553, 41);
        topologyTextbox.addModifyListener(e -> propertyChanged());
        topologyTextbox.setEditable(true);
        topologyTextbox.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.inputTextbox.text")); //$NON-NLS-1$

        org.eclipse.swt.widgets.Button SelectTopologyPathButton = new org.eclipse.swt.widgets.Button(group, SWT.TOGGLE);
        SelectTopologyPathButton.setTouchEnabled(true);
        SelectTopologyPathButton.setBounds(569, 69, 146, 32);
        SelectTopologyPathButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            }
        });
        SelectTopologyPathButton.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages")
                .getString("SimControlLaunchConfigurationTab.SelectTopologyPathButton.text"));

        Group grpOutputFiles = new Group(container, SWT.NONE);
        FormData fd_grpOutputFiles = new FormData();
        fd_grpOutputFiles.left = new FormAttachment(group, 0, SWT.LEFT);
        fd_grpOutputFiles.bottom = new FormAttachment(group, 103, SWT.BOTTOM);
        fd_grpOutputFiles.top = new FormAttachment(group, 6);
        fd_grpOutputFiles.right = new FormAttachment(group, 0, SWT.RIGHT);
        grpOutputFiles.setLayoutData(fd_grpOutputFiles);

        grpOutputFiles.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages")
                .getString("SimControlLaunchConfigurationTab.grpOutputFiles.text"));
        org.eclipse.swt.widgets.Button SelectOutputPathButton = new org.eclipse.swt.widgets.Button(grpOutputFiles,
                SWT.TOGGLE);
        SelectOutputPathButton.setTouchEnabled(true);
        SelectOutputPathButton.setBounds(569, 22, 145, 32);
        SelectOutputPathButton.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages")
                .getString("SimControlLaunchConfigurationTab.SelectOutputPathButton.text"));

        outputTextbox = new Text(grpOutputFiles, SWT.READ_ONLY | SWT.H_SCROLL | SWT.CANCEL);
        outputTextbox.setTouchEnabled(true);
        outputTextbox.setEditable(true);
        outputTextbox.setBounds(10, 22, 553, 41);
        outputTextbox.addModifyListener(e -> propertyChanged());
        outputTextbox.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages")
                .getString("SimControlLaunchConfigurationTab.inputTextbox.text"));

        Group optionsGroup = new Group(container, SWT.NONE);

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
        copyPathButton.setBounds(569, 60, 145, 32);
        copyPathButton.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.copyPathButton.text")); //$NON-NLS-1$
        optionsGroup.setLayout(null);
        FormData fd_OptionsGroup = new FormData();
        fd_OptionsGroup.bottom = new FormAttachment(100, -162);
        fd_OptionsGroup.right = new FormAttachment(group, 0, SWT.RIGHT);
        fd_OptionsGroup.left = new FormAttachment(group, 0, SWT.LEFT);
        optionsGroup.setLayoutData(fd_OptionsGroup);
        optionsGroup.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.OptionsGroup.text")); //$NON-NLS-1$

        ignoreLogicalConButton = new Button(optionsGroup, SWT.CHECK | SWT.CENTER);
        ignoreLogicalConButton.setAlignment(SWT.LEFT);
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
        
        ignoreLogicalConButton.setBounds(10, 24, 304, 24);
        ignoreLogicalConButton.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.ignoreLogicalConButton.text")); //$NON-NLS-1$
        
        Group grpCyberAttackSimulation = new Group(optionsGroup, SWT.NONE);
        grpCyberAttackSimulation.setBounds(10, 54, 698, 120);
        grpCyberAttackSimulation.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages")
                .getString("SimControlLaunchConfigurationTab.grpCyberAttackSimulation.text"));
        
        Composite HackingStyleComposite = new Composite(grpCyberAttackSimulation, SWT.NONE);
        HackingStyleComposite.setBounds(10, 24, 660, 24);
        
        Label hackingStyleLabel = new Label(HackingStyleComposite, SWT.NONE);
        hackingStyleLabel.setBounds(0, 0, 131, 24);
        hackingStyleLabel.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages")
                .getString("SimControlLaunchConfigurationTab.hackingStyleLabel.text"));
        
        hackingStyleCombo = new Combo(HackingStyleComposite, SWT.READ_ONLY);
        hackingStyleCombo.setBounds(193, 0, 202, 23);
        hackingStyleCombo.setTouchEnabled(true);
        
        hackingStyleCombo.setItems(hackinStyleEnumStrings);
        
        Composite RootNodeComposite = new Composite(grpCyberAttackSimulation, SWT.NONE);
        RootNodeComposite.setBounds(10, 54, 660, 24);
        
        rootNodeTextbox = new Text(RootNodeComposite, SWT.BORDER);
        rootNodeTextbox.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.inputTextbox.text")); //$NON-NLS-1$
        rootNodeTextbox.setBounds(193, 0, 202, 24);
        
        Label rootNodeLabel = new Label(RootNodeComposite, SWT.NONE);
        rootNodeLabel.setBounds(0, 0, 147, 24);
        rootNodeLabel.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.rootNodeLabel.text")); //$NON-NLS-1$
        
        Composite hackingSpeedComposite = new Composite(grpCyberAttackSimulation, SWT.NONE);
        hackingSpeedComposite.setBounds(10, 84, 660, 24);
        
        Label labelHackingSpeed = new Label(hackingSpeedComposite, SWT.NONE);
        labelHackingSpeed.setBounds(0, 0, 147, 24);
        labelHackingSpeed.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.labelHackingSpeed.text")); //$NON-NLS-1$
        
        hackingSpeedText = new Text(hackingSpeedComposite, SWT.BORDER);
        hackingSpeedText.setText("[Sample Remove]");
        hackingSpeedText.setBounds(193, 0, 202, 24);
        hackingSpeedText.addModifyListener(e2 -> propertyChanged());
        
        rootNodeTextbox.addModifyListener(e5 -> propertyChanged());
        
        hackingStyleCombo.addModifyListener(e4 -> propertyChanged());
        Group terminationConditionGroup = new Group(optionsGroup, SWT.NONE);
        terminationConditionGroup.setEnabled(true);
        terminationConditionGroup.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.TerminationCondition.text")); //$NON-NLS-1$
        terminationConditionGroup.setBounds(10, 180, 698, 87);
        terminationConditionGroup.setLayout(null);
        
        Label timeStepsLabel = new Label(terminationConditionGroup, SWT.NONE);
        timeStepsLabel.setLocation(10, 24);
        timeStepsLabel.setSize(148, 24);
        timeStepsLabel.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.timeStepsLabel.text")); //$NON-NLS-1$
        
        timeStepsTextBox = new Text(terminationConditionGroup, SWT.BORDER);
        timeStepsTextBox.setLocation(203, 24);
        timeStepsTextBox.setSize(202, 24);
        timeStepsTextBox.setTouchEnabled(true);
        timeStepsTextBox.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.inputTextbox.text")); //$NON-NLS-1$
        
        iterationCountTextbox = new Text(terminationConditionGroup, SWT.BORDER);
        iterationCountTextbox.setLocation(203, 54);
        iterationCountTextbox.setSize(202, 24);
        iterationCountTextbox.setTouchEnabled(true);
        iterationCountTextbox.setText("2");
        Label iterationCountLabel = new Label(terminationConditionGroup, SWT.NONE);
        iterationCountLabel.setBounds(10, 54, 121, 24);
        iterationCountLabel.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages")
                .getString("SimControlLaunchConfigurationTab.IterationCount.text"));
        iterationCountTextbox.addModifyListener(e6 -> propertyChanged());
        timeStepsTextBox.addModifyListener(e3 -> propertyChanged());

        Group grpAnalyses = new Group(container, SWT.NONE);
        fd_OptionsGroup.top = new FormAttachment(grpAnalyses, 6);

        Group GenerateGroup = new Group(optionsGroup, SWT.NONE);
        GenerateGroup.setEnabled(false);
        GenerateGroup.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.GenerateGroup.text")); //$NON-NLS-1$
        GenerateGroup.setBounds(10, 273, 698, 150);

        generateInputCheckBox = new Button(GenerateGroup, SWT.CHECK | SWT.CENTER);
        generateInputCheckBox.setAlignment(SWT.LEFT);
        generateInputCheckBox.setEnabled(false);
        generateInputCheckBox.setLocation(10, 24);
        generateInputCheckBox.setSize(304, 24);
        generateInputCheckBox.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.GenerateInputCheckBox.text")); //$NON-NLS-1$

        Composite GenerarationStyleComposite = new Composite(GenerateGroup, SWT.NONE);
        GenerarationStyleComposite.setLocation(10, 54);
        GenerarationStyleComposite.setSize(660, 24);

        generationStyleCombo = new Combo(GenerarationStyleComposite, SWT.READ_ONLY);
        generationStyleCombo.setEnabled(false);

        generationStyleCombo.setItems(genStyleEnumStrings);

        generationStyleCombo.setBounds(193, 1, 202, 23);

        Label GenerationStyleLabel = new Label(GenerarationStyleComposite, SWT.NONE);
        GenerationStyleLabel.setLocation(0, 0);
        GenerationStyleLabel.setSize(103, 24);
        GenerationStyleLabel.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.GenerationStyleLabel.text")); //$NON-NLS-1$

        Composite SMCountComposite = new Composite(GenerateGroup, SWT.NONE);
        SMCountComposite.setLocation(10, 84);
        SMCountComposite.setSize(660, 24);

        Label SmartMeterLabel = new Label(SMCountComposite, SWT.NONE);
        SmartMeterLabel.setBounds(0, 0, 152, 24);
        SmartMeterLabel.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.SmartMeterLabel.text")); //$NON-NLS-1$

        smartMeterCountTextBox = new Text(SMCountComposite, SWT.BORDER);
        smartMeterCountTextBox.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.inputTextbox.text")); //$NON-NLS-1$
        smartMeterCountTextBox.setBounds(193, 0, 202, 24);
        smartMeterCountTextBox.setEnabled(false);

        Composite CCCountComposite = new Composite(GenerateGroup, SWT.NONE);
        CCCountComposite.setLocation(10, 114);
        CCCountComposite.setSize(660, 24);

        Label ControlCenterLabel = new Label(CCCountComposite, SWT.NONE);
        ControlCenterLabel.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.ControlCenterLabel.text")); //$NON-NLS-1$
        ControlCenterLabel.setBounds(0, 0, 152, 24);

        controlCenterCountTextBox = new Text(CCCountComposite, SWT.BORDER);
        controlCenterCountTextBox.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.inputTextbox.text")); //$NON-NLS-1$
        controlCenterCountTextBox.setBounds(193, 0, 202, 24);
        controlCenterCountTextBox.setEnabled(false);
        grpAnalyses.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.grpAnalyses.text")); //$NON-NLS-1$

        FormData fd_grpAnalyses = new FormData();
        fd_grpAnalyses.right = new FormAttachment(group, 0, SWT.RIGHT);
        fd_grpAnalyses.bottom = new FormAttachment(grpOutputFiles, 209, SWT.BOTTOM);
        fd_grpAnalyses.top = new FormAttachment(grpOutputFiles, 6);
        fd_grpAnalyses.left = new FormAttachment(0, 10);
        grpAnalyses.setLayoutData(fd_grpAnalyses);

        Label lblNewLabel = new Label(grpAnalyses, SWT.NONE);
        lblNewLabel.setBounds(10, 24, 156, 20);
        lblNewLabel.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.lblNewLabel.text_1")); //$NON-NLS-1$

        Label lblKritisSimulation = new Label(grpAnalyses, SWT.NONE);
        lblKritisSimulation.setBounds(10, 53, 137, 23);
        lblKritisSimulation.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.lblKritisSimulation.text")); //$NON-NLS-1$

        Label lblImpactAnalysis = new Label(grpAnalyses, SWT.NONE);
        lblImpactAnalysis.setBounds(10, 82, 137, 23);
        lblImpactAnalysis.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.lblImpactAnalysis.text")); //$NON-NLS-1$

        Label lblCyberAttackSimulation = new Label(grpAnalyses, SWT.NONE);
        lblCyberAttackSimulation.setBounds(10, 111, 137, 23);
        lblCyberAttackSimulation.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.lblCyberAttackSimulation.text")); //$NON-NLS-1$

        Label lblTerminationCondition = new Label(grpAnalyses, SWT.NONE);
        lblTerminationCondition.setBounds(10, 140, 137, 23);
        lblTerminationCondition.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.lblTerminationCondition.text")); //$NON-NLS-1$

        Label lblProgressor = new Label(grpAnalyses, SWT.NONE);
        lblProgressor.setBounds(10, 169, 137, 25);
        lblProgressor.setText(ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.lblProgressor.text")); //$NON-NLS-1$

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
        comboAttackSimulation.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                ReceiveAnalysesHelper helper = new ReceiveAnalysesHelper();

                try {
                    List<IAttackerSimulation> list = helper.getAttackerSimulationElements();
                    IAttackerSimulation sim = null;
                    for (IAttackerSimulation attack : list) {
                        if (attack.getName()
                                .equals(comboAttackSimulation.getItem(comboAttackSimulation.getSelectionIndex()))) {
                            sim = attack;
                            break;
                        }
                    }
                    if (sim.enableLogicalConnections()) {
                        grpCyberAttackSimulation.setEnabled(true);
                        ignoreLogicalConButton.setEnabled(true);
                    } else {
                        grpCyberAttackSimulation.setEnabled(false);
                        ignoreLogicalConButton.setEnabled(false);
                    }
                    if (sim.enableRootNode()) {
                        rootNodeTextbox.setEnabled(true);
                    } else {
                        rootNodeTextbox.setEnabled(false);
                    }
                    if (sim.enableHackingSpeed()) {
                        hackingSpeedText.setEnabled(true);
                    } else {
                        hackingSpeedText.setEnabled(false);
                    }
                } catch (CoreException e1) {
                    e1.printStackTrace();
                }
                propertyChanged();
            }
        });

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
                final String message = ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages")
                        .getString("SimControlLaunchConfigurationTab.OpenFileDialog.SELECT_OUTPUT_PATH");
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
                final String message = ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages")
                        .getString("SimControlLaunchConfigurationTab.OpenFileDialog.SELECT_INPUT_TOPOLOGY_PATH");
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
                final String message = ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages")
                        .getString("SimControlLaunchConfigurationTab.OpenFileDialog.SELECT_INPUT_PATH");
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
        configuration.setAttribute(Constants.ITERATION_COUNT_KEY, Constants.DEFAULT_ITERATION_COUNT);
    }

    /**
     * 
     */
    @Override
    public void initializeFrom(ILaunchConfiguration configuration) {
        try {
            hackingSpeedText
                    .setText(configuration.getAttribute(Constants.HACKING_SPEED_KEY, Constants.DEFAULT_HACKING_SPEED));
            topologyTextbox
                    .setText(configuration.getAttribute(Constants.TOPOLOGY_PATH_KEY, Constants.DEFAULT_TOPO_PATH));
            inputTextbox.setText(configuration.getAttribute(Constants.INPUT_PATH_KEY, Constants.DEFAULT_INPUT_PATH));
            outputTextbox.setText(configuration.getAttribute(Constants.OUTPUT_PATH_KEY, Constants.DEFAULT_OUTPUT_PATH));
            timeStepsTextBox.setText(configuration.getAttribute(Constants.TIMESTEPS_KEY, Constants.DEFAULT_TIME_STEPS));
            iterationCountTextbox.setText(
                    configuration.getAttribute(Constants.ITERATION_COUNT_KEY, Constants.DEFAULT_ITERATION_COUNT));
            rootNodeTextbox
                    .setText(configuration.getAttribute(Constants.ROOT_NODE_ID_KEY, Constants.DEFAULT_ROOT_NODE_ID));
            hackingStyleCombo
                    .setText(configuration.getAttribute(Constants.HACKING_STYLE_KEY, Constants.DEFAULT_HACKING_STYLE));
            generationStyleCombo.setText(
                    configuration.getAttribute(Constants.GENERATION_STYLE_KEY, Constants.DEFAULT_GENERATION_STYLE));
            smartMeterCountTextBox.setText(
                    configuration.getAttribute(Constants.SMART_METER_COUNT_KEY, Constants.DEFAULT_ROOT_NODE_ID));
            controlCenterCountTextBox.setText(configuration.getAttribute(Constants.CONTROL_CENTER_COUNT_KEY,
                    Constants.DEFAULT_CONTROL_CENTER_COUNT));

            ignoreLogicalConButton.setSelection(
                    configuration.getAttribute(Constants.IGNORE_LOC_CON_KEY, Constants.DEFAULT_IGNORE_LOC_CON)
                            .contentEquals(Constants.TRUE));

            generateInputCheckBox.setSelection(
                    configuration.getAttribute(Constants.GEN_SYNTHETIC_INPUT_KEY, Constants.DEFAULT_GEN_SYNTHETIC_INPUT)
                            .contentEquals(Constants.TRUE));

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
                if (comboImpactAnalysis.getItem(i).equals(impactAnalysisString)) {
                    comboImpactAnalysis.select(i);
                    break;
                }
            }
            String terminationConditionString = configuration
                    .getAttribute(Constants.TERMINATION_CONDITION_SIMULATION_CONFIG, "");
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
            String kritisString = configuration.getAttribute(Constants.KRITIS_SIMULATION_CONFIG, "");
            for (int i = 0; i < comboKritisSimulation.getItems().length; i++) {
                if (comboKritisSimulation.getItem(i).equals(kritisString)) {
                    comboKritisSimulation.select(i);
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
        configuration.setAttribute(Constants.ITERATION_COUNT_KEY, iterationCountTextbox.getText());

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
            configuration.setAttribute(Constants.KRITIS_SIMULATION_CONFIG,
                    comboKritisSimulation.getItem(comboKritisSimulation.getSelectionIndex()));
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
            Message = ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages") //$NON-NLS-1$
                    .getString("SimControlLaunchConfigurationTab.ErrorMessages.NO_PATH"); //$NON-NLS-1$
        else if (areFileExtensionsWrong())
            Message = ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages") //$NON-NLS-1$
                    .getString("SimControlLaunchConfigurationTab.ErrorMessages.WRONG_EXTENSIONS"); //$NON-NLS-1$
        else if (areOptionsWrong()) {

            Message = ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages") //$NON-NLS-1$
                    .getString("SimControlLaunchConfigurationTab.ErrorMessages.WRONG_OPTIONS"); //$NON-NLS-1$

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
        return ResourceBundle.getBundle("smartgrid.simcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.Tab.RegisterText"); //$NON-NLS-1$
    }

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
        final boolean rootNodeWrong = !(this.isUInt(this.rootNodeTextbox.getText())
                || this.rootNodeTextbox.getText().equals(Constants.DEFAULT_ROOT_NODE_ID));
        final boolean smartCountWrong = !this.isUInt(this.smartMeterCountTextBox.getText());

        final boolean controlCountWrong = !this.isUInt(this.controlCenterCountTextBox.getText());

        // return timeStepsWrong || rootNodeWrong || smartCountWrong ||
        // controlCountWrong;
        return false;
    }

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
            addKritisSimulationElements(helper);
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

    private void addKritisSimulationElements(ReceiveAnalysesHelper helper) throws CoreException {
        List<IKritisSimulation> list = helper.getKritisSimulationElements();
        for (IKritisSimulation ele : list) {
            comboKritisSimulation.add(ele.getName());
        }
        if (comboKritisSimulation.getItemCount() > 0) {
            comboKritisSimulation.select(0);
        }
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
