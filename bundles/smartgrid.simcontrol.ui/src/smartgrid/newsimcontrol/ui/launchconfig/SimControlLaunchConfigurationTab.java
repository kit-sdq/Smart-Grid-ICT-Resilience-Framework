package smartgrid.newsimcontrol.ui.launchconfig;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import smartgrid.helper.TestSimulationExtensionPointHelper;
import smartgrid.simcontrol.test.baselib.Constants;
import smartgrid.simcontrol.test.baselib.GenerationStyle;
import smartgrid.simcontrol.test.baselib.HackingType;
import smartgrid.simcontrol.test.baselib.coupling.IAttackerSimulation;
import smartgrid.simcontrol.test.baselib.coupling.IImpactAnalysis;
//import smartgrid.simcontrol.test.baselib.coupling.IKritisSimulationWrapper;
//import smartgrid.simcontrol.test.baselib.coupling.IPowerLoadSimulationWrapper;
import smartgrid.simcontrol.test.baselib.coupling.ISimulationComponent;
import smartgrid.simcontrol.test.baselib.coupling.ITimeProgressor;

public class SimControlLaunchConfigurationTab extends AbstractLaunchConfigurationTab {

    private static final Logger LOG = Logger.getLogger(SimControlLaunchConfigurationTab.class);

    private String noHackingChoice = ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages")
            .getString("SimControlLaunchConfigurationTab.NoHackingChoiceLabel.text");

    private String[] localHackingStyles = new String[] {noHackingChoice, "BFS_HACKING", "DFS_HACKING", "FULLY_MESHED_HACKING"};
    private String[] viralHackingStyles = new String[] {noHackingChoice, "STANDARD_HACKING", "FULLY_MESHED_HACKING"};

    private Composite container;
    private Text outputTextbox;
    private Text inputTextbox;
    private Text topologyTextbox;
    private Text timeStepsTextBox;
    private Text iterationCountTextbox;
    private Text rootNodeTextbox;
    private Button ignoreLogicalConCheckBox;
    private Combo hackingStyleCombo;
    private Combo comboPowerLoadSimulation;
    private Combo comboKritisSimulation;
    private Combo comboImpactAnalysis;
    private Combo comboAttackSimulation;
    private Combo comboTerminationCondition;
    private Combo comboProgressor;
    private Text hackingSpeedText;
    private Button generateTopoCheckBox;
    private Button selectInputPathButton;
    private Button selectTopologyPathButton;
    private Group cyberAttackSimulationGroup;
    private Button completionCheckBox;

    /**
     * @wbp.parser.entryPoint
     */
    @Override
    public void createControl(final Composite parent) {

        // Building Items List for hackingStyleCombo
        
              
        final HackingType[] hackingStyles = HackingType.values();
        final String[] hackinStyleEnumStrings = new String[hackingStyles.length];

        for (int i = 0; i < hackingStyles.length; i++) {
            hackinStyleEnumStrings[i] = hackingStyles[i].name();
        }

        // Building Items List for hackingStyleCombo
        final GenerationStyle[] genStyles = GenerationStyle.values();
        final String[] genStyleEnumStrings = new String[genStyles.length];

        for (int i = 0; i < genStyles.length; i++) {
            genStyleEnumStrings[i] = genStyles[i].name();
        }

        container = new Composite(parent, SWT.NONE);
        setControl(container);
        RowLayout rl_container = new RowLayout(SWT.VERTICAL);
        rl_container.fill = true;
        container.setLayout(rl_container);

        Group inputGroup = new Group(container, SWT.NONE);

        inputGroup.setText(ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.group.text"));

        inputTextbox = new Text(inputGroup, SWT.READ_ONLY | SWT.H_SCROLL | SWT.CANCEL);
        inputTextbox.setTouchEnabled(true);

        inputTextbox.setBounds(10, 22, 553, 41);
        inputTextbox.addModifyListener(e -> propertyChanged());
        inputTextbox.setEditable(true);
        inputTextbox.setText(ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.inputTextbox.text"));
        selectInputPathButton = new org.eclipse.swt.widgets.Button(inputGroup, SWT.TOGGLE);
        selectInputPathButton.setTouchEnabled(true);
        selectInputPathButton.setBounds(569, 22, 146, 32);

        selectInputPathButton.setText(ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.SelectInputPathButton.text"));

        topologyTextbox = new Text(inputGroup, SWT.READ_ONLY | SWT.H_SCROLL | SWT.CANCEL);
        topologyTextbox.setTouchEnabled(true);
        topologyTextbox.setBounds(10, 69, 553, 41);
        topologyTextbox.addModifyListener(e -> propertyChanged());
        topologyTextbox.setEditable(true);
        topologyTextbox.setText(ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.inputTextbox.text")); //$NON-NLS-1$

        selectTopologyPathButton = new org.eclipse.swt.widgets.Button(inputGroup, SWT.TOGGLE);
        selectTopologyPathButton.setTouchEnabled(true);
        selectTopologyPathButton.setBounds(569, 69, 146, 32);
        selectTopologyPathButton.setText(ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.SelectTopologyPathButton.text"));

        final Group outputGroup = new Group(container, SWT.NONE);

        outputGroup.setText(ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.grpOutputFiles.text"));
        final org.eclipse.swt.widgets.Button SelectOutputPathButton = new org.eclipse.swt.widgets.Button(outputGroup, SWT.TOGGLE);
        SelectOutputPathButton.setTouchEnabled(true);
        SelectOutputPathButton.setBounds(569, 22, 145, 32);
        SelectOutputPathButton.setText(ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.SelectOutputPathButton.text"));

        outputTextbox = new Text(outputGroup, SWT.READ_ONLY | SWT.H_SCROLL | SWT.CANCEL);
        outputTextbox.setTouchEnabled(true);
        outputTextbox.setEditable(true);
        outputTextbox.setBounds(10, 22, 553, 41);
        outputTextbox.addModifyListener(e -> propertyChanged());
        outputTextbox.setText(ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.inputTextbox.text"));

        final Group grpAnalyses = new Group(container, SWT.NONE);
        grpAnalyses.setText(ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.grpAnalyses.text"));

        final Label lblNewLabel = new Label(grpAnalyses, SWT.NONE);
        lblNewLabel.setBounds(10, 24, 156, 23);
        lblNewLabel.setText(ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.lblNewLabel.text_1")); //$NON-NLS-1$

        final Label lblKritisSimulation = new Label(grpAnalyses, SWT.NONE);
        lblKritisSimulation.setBounds(10, 53, 137, 23);
        lblKritisSimulation.setText(ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.lblKritisSimulation.text")); //$NON-NLS-1$

        final Label lblImpactAnalysis = new Label(grpAnalyses, SWT.NONE);
        lblImpactAnalysis.setBounds(10, 82, 137, 23);
        lblImpactAnalysis.setText(ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.lblImpactAnalysis.text")); //$NON-NLS-1$

        final Label lblCyberAttackSimulation = new Label(grpAnalyses, SWT.NONE);
        lblCyberAttackSimulation.setBounds(10, 111, 137, 23);
        lblCyberAttackSimulation.setText(ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.lblCyberAttackSimulation.text")); //$NON-NLS-1$

        final Label lblTerminationCondition = new Label(grpAnalyses, SWT.NONE);
        lblTerminationCondition.setBounds(10, 140, 137, 23);
        lblTerminationCondition.setText(ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.lblTerminationCondition.text")); //$NON-NLS-1$

        final Label lblProgressor = new Label(grpAnalyses, SWT.NONE);
        lblProgressor.setBounds(10, 169, 137, 23);
        lblProgressor.setText(ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.lblProgressor.text")); //$NON-NLS-1$

        comboPowerLoadSimulation = new Combo(grpAnalyses, SWT.READ_ONLY);
        comboPowerLoadSimulation.setBounds(213, 21, 202, 23);
        comboPowerLoadSimulation.addModifyListener(e -> {

            // iip opf simulation has special needs
            String selectedPowerLoadSim = getSelectionOfComboBox(comboPowerLoadSimulation);
            if (selectedPowerLoadSim.equals(Constants.IIP_OPF_NAME)) {
                // select kritis simulation
                setSelectionOfComboBox(comboKritisSimulation, Constants.KRITIS_SIMULATION_NAME);
            }
            propertyChanged();
        });

        comboKritisSimulation = new Combo(grpAnalyses, SWT.READ_ONLY);
        comboKritisSimulation.setBounds(213, 50, 202, 23);
        comboKritisSimulation.addModifyListener(e -> {

            // KRITIS Sim supports IIP OPF and topo generation
            String selectedKtitisSim = getSelectionOfComboBox(comboKritisSimulation);
            if (!selectedKtitisSim.equals(Constants.KRITIS_SIMULATION_NAME)) {
                // select kritis simulation
                setSelectionOfComboBoxToOther(comboPowerLoadSimulation, Constants.IIP_OPF_NAME);
                generateTopoCheckBox.setSelection(false);
            }
            propertyChanged();
        });

        comboImpactAnalysis = new Combo(grpAnalyses, SWT.READ_ONLY);
        comboImpactAnalysis.setBounds(213, 79, 202, 23);
        comboImpactAnalysis.addModifyListener(e -> propertyChanged());

        comboAttackSimulation = new Combo(grpAnalyses, SWT.READ_ONLY);
        comboAttackSimulation.setBounds(213, 108, 202, 23);
        comboAttackSimulation.addModifyListener(e -> {

            try {
                final List<IAttackerSimulation> list = TestSimulationExtensionPointHelper.getAttackerSimulationExtensions();
                IAttackerSimulation sim = null;
                for (final IAttackerSimulation attack : list) {
                    if (attack.getName().equals(getSelectionOfComboBox(comboAttackSimulation))) {
                        sim = attack;
                        break;
                    }
                }
                if (sim.enableLogicalConnections()) {
                    cyberAttackSimulationGroup.setEnabled(true);
                    ignoreLogicalConCheckBox.setEnabled(true);
                } else {
                    cyberAttackSimulationGroup.setEnabled(false);
                    setHackingStyleText("No Attacker Simulation");
                }
                if (sim.enableRootNode()) {
                    rootNodeTextbox.setEnabled(true);
                } else {
                    rootNodeTextbox.setEnabled(false);
                }
                
                if (sim.getName().equals("Local Hacker")) {
                    setHackingStyleText("Local Hacker");
                }
                else if (sim.getName().equals("Viral Hacker")) {
                    setHackingStyleText("Viral Hacker");
                }
                if (sim.enableHackingSpeed()) {
                    hackingSpeedText.setEnabled(true);
                } else {
                    hackingSpeedText.setEnabled(false);
                }
            } catch (final CoreException e1) {
                e1.printStackTrace();
            }
            SimControlLaunchConfigurationTab.this.propertyChanged();
        });

        comboTerminationCondition = new Combo(grpAnalyses, SWT.READ_ONLY);
        comboTerminationCondition.setBounds(213, 137, 202, 23);
        comboTerminationCondition.addModifyListener(e -> propertyChanged());

        comboProgressor = new Combo(grpAnalyses, SWT.READ_ONLY);
        comboProgressor.setBounds(213, 166, 202, 23);
        comboProgressor.addModifyListener(e -> propertyChanged());

        final Group optionsGroup = new Group(container, SWT.NONE);

        final Button copyPathButton = new Button(outputGroup, SWT.NONE);
        copyPathButton.setTouchEnabled(true);
        copyPathButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseUp(final MouseEvent e) {
            }

            @Override
            public void mouseDown(final MouseEvent e) {

                // Copy Inputpath as Output Dir
                outputTextbox.setText(new File(inputTextbox.getText()).getParent() + "/");
            }
        });
        copyPathButton.setBounds(569, 60, 145, 32);
        copyPathButton.setText(ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.copyPathButton.text"));
        RowLayout rl_optionsGroup = new RowLayout(SWT.VERTICAL);
        rl_optionsGroup.fill = true;
        optionsGroup.setLayout(rl_optionsGroup);
        optionsGroup.setText(ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.OptionsGroup.text")); //$NON-NLS-1$

        ignoreLogicalConCheckBox = new Button(optionsGroup, SWT.CHECK | SWT.CENTER);
        ignoreLogicalConCheckBox.setToolTipText(ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.ignoreLogicalConButton.toolTipText")); //$NON-NLS-1$ //$NON-NLS-2$
        ignoreLogicalConCheckBox.setAlignment(SWT.LEFT);
        ignoreLogicalConCheckBox.setTouchEnabled(true);

        ignoreLogicalConCheckBox.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
                propertyChanged();

                if (ignoreLogicalConCheckBox.getSelection()) {
                    if (!comboAttackSimulation.getText().equals("No Attack Simulation")) {
                        hackingStyleCombo.setText(HackingType.FULLY_MESHED_HACKING.name());
                    }
                    hackingStyleCombo.setEnabled(false);
                } else {
                     if (comboAttackSimulation.getText().equals("Local Hacker")) {
                        setHackingStyleText("Local Hacker");
                    } else if (comboAttackSimulation.getText().equals("Viral Hacker")) {
                        setHackingStyleText("Viral Hacker");
                    } else  {
                        setHackingStyleText("No Attack Simulation");
                    }
                }
            }
        });
        ignoreLogicalConCheckBox.setText(ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.ignoreLogicalConButton.text")); //$NON-NLS-1$

        completionCheckBox = new Button(optionsGroup, SWT.CHECK);
        completionCheckBox.setTouchEnabled(true);
        completionCheckBox.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                propertyChanged();
            }
        });
        completionCheckBox.setText(ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.btnCheckButton.text")); //$NON-NLS-1$ //$NON-NLS-2$

        generateTopoCheckBox = new Button(optionsGroup, SWT.CHECK);
        generateTopoCheckBox.setTouchEnabled(true);
        generateTopoCheckBox.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                propertyChanged();
                boolean generateTopo = generateTopoCheckBox.getSelection();
                setPathTextFieldsEnabled(!generateTopo);
                if (generateTopo) {
                    setSelectionOfComboBox(comboKritisSimulation, Constants.KRITIS_SIMULATION_NAME);
                }
            }
        });
        generateTopoCheckBox.setToolTipText(ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.btnGenerateTopo.toolTipText")); //$NON-NLS-1$ //$NON-NLS-2$
        generateTopoCheckBox.setText(ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.btnCheckButton_1.text")); //$NON-NLS-1$ //$NON-NLS-2$

        cyberAttackSimulationGroup = new Group(optionsGroup, SWT.NONE);
        cyberAttackSimulationGroup.setText(ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.grpCyberAttackSimulation.text"));

        final Composite HackingStyleComposite = new Composite(cyberAttackSimulationGroup, SWT.NONE);
        HackingStyleComposite.setBounds(10, 24, 660, 24);

        final Label hackingStyleLabel = new Label(HackingStyleComposite, SWT.NONE);
        hackingStyleLabel.setBounds(0, 0, 131, 24);
        hackingStyleLabel.setText(ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.hackingStyleLabel.text"));

        hackingStyleCombo = new Combo(HackingStyleComposite, SWT.READ_ONLY);
        hackingStyleCombo.setBounds(193, 0, 202, 23);
        hackingStyleCombo.setTouchEnabled(true);
        
        if (comboAttackSimulation.getText().equals("Local Hacker")) {
            setHackingStyleText("Local Hacker");
        } else if (comboAttackSimulation.getText().equals("Viral Hacker")) {
            setHackingStyleText("Viral Hacker");
        } else  {
            setHackingStyleText("No Attack Simulation");
        }

        final Composite RootNodeComposite = new Composite(cyberAttackSimulationGroup, SWT.NONE);
        RootNodeComposite.setBounds(10, 54, 660, 24);

        rootNodeTextbox = new Text(RootNodeComposite, SWT.BORDER);
        rootNodeTextbox.setText(ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.inputTextbox.text")); //$NON-NLS-1$
        rootNodeTextbox.setBounds(193, 0, 202, 24);

        final Label rootNodeLabel = new Label(RootNodeComposite, SWT.NONE);
        rootNodeLabel.setBounds(0, 0, 147, 24);
        rootNodeLabel.setText(ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.rootNodeLabel.text")); //$NON-NLS-1$

        final Composite hackingSpeedComposite = new Composite(cyberAttackSimulationGroup, SWT.NONE);
        hackingSpeedComposite.setBounds(10, 84, 660, 24);

        final Label labelHackingSpeed = new Label(hackingSpeedComposite, SWT.NONE);
        labelHackingSpeed.setBounds(0, 0, 147, 24);
        labelHackingSpeed.setText(ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.labelHackingSpeed.text")); //$NON-NLS-1$

        hackingSpeedText = new Text(hackingSpeedComposite, SWT.BORDER);
        hackingSpeedText.setText("[Sample Remove]");
        hackingSpeedText.setBounds(193, 0, 202, 24);
        hackingSpeedText.addModifyListener(e2 -> propertyChanged());

        rootNodeTextbox.addModifyListener(e5 -> propertyChanged());

        hackingStyleCombo.addModifyListener(e4 -> propertyChanged());
        final Group terminationConditionGroup = new Group(optionsGroup, SWT.NONE);
        terminationConditionGroup.setEnabled(true);
        terminationConditionGroup.setText(ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.TerminationCondition.text"));
        terminationConditionGroup.setLayout(null);

        final Label timeStepsLabel = new Label(terminationConditionGroup, SWT.NONE);
        timeStepsLabel.setLocation(10, 24);
        timeStepsLabel.setSize(148, 24);
        timeStepsLabel.setText(ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.timeStepsLabel.text")); //$NON-NLS-1$

        timeStepsTextBox = new Text(terminationConditionGroup, SWT.BORDER);
        timeStepsTextBox.setLocation(203, 24);
        timeStepsTextBox.setSize(202, 24);
        timeStepsTextBox.setTouchEnabled(true);
        timeStepsTextBox.setText(ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.inputTextbox.text")); //$NON-NLS-1$

        iterationCountTextbox = new Text(terminationConditionGroup, SWT.BORDER);
        iterationCountTextbox.setLocation(203, 54);
        iterationCountTextbox.setSize(202, 24);
        iterationCountTextbox.setTouchEnabled(true);
        iterationCountTextbox.setText("2");
        final Label iterationCountLabel = new Label(terminationConditionGroup, SWT.NONE);
        iterationCountLabel.setBounds(10, 54, 121, 24);
        iterationCountLabel.setText(ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.IterationCount.text"));
        iterationCountTextbox.addModifyListener(e6 -> propertyChanged());
        timeStepsTextBox.addModifyListener(e3 -> propertyChanged());

        addElementsToCombos();

        SelectOutputPathButton.addMouseListener(new MouseListener() {

            @Override
            public void mouseUp(final MouseEvent e) {
            }

            @Override
            public void mouseDown(final MouseEvent e) {
                // Get Text to show in Open File Dialog from Resource File
                final String message = ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.OpenFileDialog.SELECT_OUTPUT_PATH");
                final String outputPath = SimControlLaunchConfigurationTab.this.getDirPath(message);

                if (outputPath != null) {
                    outputTextbox.setText(outputPath);
                }
            }

            @Override
            public void mouseDoubleClick(final MouseEvent e) {
            }
        });
        selectTopologyPathButton.addMouseListener(new MouseListener() {

            @Override
            public void mouseUp(final MouseEvent e) {
            }

            @Override
            public void mouseDown(final MouseEvent e) {
                // Get Text to show in Open File Dialog from Resource File
                final String message = ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.OpenFileDialog.SELECT_INPUT_TOPOLOGY_PATH");
                final String topologyPath = SimControlLaunchConfigurationTab.this.getFilePath(message, Constants.TOPO_EXTENSION);

                if (topologyPath != null) {
                    topologyTextbox.setText(topologyPath);
                }
            }

            @Override
            public void mouseDoubleClick(final MouseEvent e) {
            }
        });
        selectInputPathButton.addMouseListener(new MouseListener() {

            @Override
            public void mouseUp(final MouseEvent e) {
            }

            @Override
            public void mouseDown(final MouseEvent e) {
                // Get Text to show in Open File Dialog from Resource File
                // Gets the Filepath
                final String message = ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages").getString("SimControlLaunchConfigurationTab.OpenFileDialog.SELECT_INPUT_PATH");
                final String inputPath = SimControlLaunchConfigurationTab.this.getFilePath(message, Constants.INPUT_EXTENSION);

                if (inputPath != null) {
                    inputTextbox.setText(inputPath);
                }
            }

            @Override
            public void mouseDoubleClick(final MouseEvent e) {
            }
        });
    }
    private void setHackingStyleText(String comboAttacker) {
        if (comboAttacker.equals("Local Hacker")) {
            hackingStyleCombo.setItems(localHackingStyles);
            hackingStyleCombo.setEnabled(true);
            hackingStyleCombo.setText(noHackingChoice);
        } else if (comboAttacker.equals("Viral Hacker")) {
            hackingStyleCombo.setItems(viralHackingStyles);
            hackingStyleCombo.setEnabled(true);
            hackingStyleCombo.setText(noHackingChoice);
        } else {
            hackingStyleCombo.setItems(noHackingChoice);
            hackingStyleCombo.setEnabled(false);
        }
    }
    private void setSelectionOfComboBoxToOther(Combo comboBox, String entry) {
        comboBox.deselect(comboBox.indexOf(entry));
    }

    private void setSelectionOfComboBox(Combo comboBox, String entry) {
        int indexOfKritisSim = comboBox.indexOf(entry);
        if (indexOfKritisSim != -1)
            comboBox.select(indexOfKritisSim);
    }

    @Override
    public void setDefaults(final ILaunchConfigurationWorkingCopy configuration) {
        configuration.setAttribute(Constants.TOPO_PATH_KEY, Constants.DEFAULT_TOPO_PATH);
        configuration.setAttribute(Constants.INPUT_PATH_KEY, Constants.DEFAULT_INPUT_PATH);
        configuration.setAttribute(Constants.OUTPUT_PATH_KEY, Constants.DEFAULT_OUTPUT_PATH);
        configuration.setAttribute(Constants.TIME_STEPS_KEY, Constants.DEFAULT_TIME_STEPS);
        configuration.setAttribute(Constants.IGNORE_LOC_CON_KEY, Constants.DEFAULT_IGNORE_LOC_CON);
        configuration.setAttribute(Constants.ROOT_NODE_ID_KEY, Constants.DEFAULT_ROOT_NODE_ID);
        configuration.setAttribute(Constants.HACKING_SPEED_KEY, Constants.DEFAULT_HACKING_SPEED);
        configuration.setAttribute(Constants.ITERATION_COUNT_KEY, Constants.DEFAULT_ITERATION_COUNT);
        configuration.setAttribute(Constants.SMARTMETER_COMPLETION_KEY, Constants.DEFAULT_SMARTMETER_COMPLETION);
        configuration.setAttribute(Constants.TOPO_GENERATION_KEY, Constants.DEFAULT_TOPO_GENERATION);
    }

    @Override
    public void initializeFrom(final ILaunchConfiguration configuration) {
        try {
            boolean generateTopo = configuration.getAttribute(Constants.TOPO_GENERATION_KEY, Constants.DEFAULT_TOPO_GENERATION);
            setPathTextFieldsEnabled(!generateTopo);

            hackingSpeedText.setText(configuration.getAttribute(Constants.HACKING_SPEED_KEY, Constants.DEFAULT_HACKING_SPEED));
            topologyTextbox.setText(configuration.getAttribute(Constants.TOPO_PATH_KEY, Constants.DEFAULT_TOPO_PATH));
            inputTextbox.setText(configuration.getAttribute(Constants.INPUT_PATH_KEY, Constants.DEFAULT_INPUT_PATH));
            outputTextbox.setText(configuration.getAttribute(Constants.OUTPUT_PATH_KEY, Constants.DEFAULT_OUTPUT_PATH));
            timeStepsTextBox.setText(configuration.getAttribute(Constants.TIME_STEPS_KEY, Constants.DEFAULT_TIME_STEPS));
            iterationCountTextbox.setText(configuration.getAttribute(Constants.ITERATION_COUNT_KEY, Constants.DEFAULT_ITERATION_COUNT));
            rootNodeTextbox.setText(configuration.getAttribute(Constants.ROOT_NODE_ID_KEY, Constants.DEFAULT_ROOT_NODE_ID));
            
            ignoreLogicalConCheckBox.setSelection(configuration.getAttribute(Constants.IGNORE_LOC_CON_KEY, Constants.DEFAULT_IGNORE_LOC_CON).contentEquals(Constants.TRUE));
            generateTopoCheckBox.setSelection(generateTopo);
            completionCheckBox.setSelection(configuration.getAttribute(Constants.SMARTMETER_COMPLETION_KEY, Constants.DEFAULT_SMARTMETER_COMPLETION).contentEquals(Constants.TRUE));

            setSelectionOfComboBox(comboAttackSimulation, configuration, Constants.ATTACKER_SIMULATION_KEY);
            setSelectionOfComboBox(comboPowerLoadSimulation, configuration, Constants.POWER_LOAD_SIMULATION_KEY);
            setSelectionOfComboBox(comboImpactAnalysis, configuration, Constants.IMPACT_ANALYSIS_SIMULATION_KEY);
            setSelectionOfComboBox(comboTerminationCondition, configuration, Constants.TERMINATION_CONDITION_SIMULATION_KEY);
            setSelectionOfComboBox(comboProgressor, configuration, Constants.TIME_PROGRESSOR_SIMULATION_KEY);
            setSelectionOfComboBox(comboKritisSimulation, configuration, Constants.KRITIS_SIMULATION_KEY);
            
            hackingStyleCombo.setText(configuration.getAttribute(Constants.HACKING_STYLE_KEY, noHackingChoice));

        } catch (final CoreException e) {
            LOG.error("An error occured when trying to read the launch configuration.", e);
        }
    }

    private void setSelectionOfComboBox(Combo comboBox, final ILaunchConfiguration configuration, String simulationModuleKey) throws CoreException {
        final String configValue = configuration.getAttribute(simulationModuleKey, "");
        for (int i = 0; i < comboBox.getItems().length; i++) {
            if (comboBox.getItem(i).equals(configValue)) {
                comboBox.select(i);
                break;
            }
        }
    }

    @Override
    public void performApply(final ILaunchConfigurationWorkingCopy configuration) {

        // Text Box reading
        final String topoPath = topologyTextbox.getText();
        final String inPath = inputTextbox.getText();
        final String outPath = outputTextbox.getText();
        final String timeSteps = timeStepsTextBox.getText();
        final String rNodeID = rootNodeTextbox.getText();

        // Check Box Parsing
        String ignoreLogicalConnections = parseCheckBox(ignoreLogicalConCheckBox);
        String smartMeterCompletion = parseCheckBox(completionCheckBox);
        boolean generateTopo = generateTopoCheckBox.getSelection();


        // Add to config
        configuration.setAttribute(Constants.TOPO_PATH_KEY, topoPath);
        configuration.setAttribute(Constants.INPUT_PATH_KEY, inPath);
        configuration.setAttribute(Constants.OUTPUT_PATH_KEY, outPath);
        configuration.setAttribute(Constants.TIME_STEPS_KEY, timeSteps);
        configuration.setAttribute(Constants.IGNORE_LOC_CON_KEY, ignoreLogicalConnections);
        configuration.setAttribute(Constants.ROOT_NODE_ID_KEY, rNodeID);
        configuration.setAttribute(Constants.HACKING_SPEED_KEY, hackingSpeedText.getText());
        configuration.setAttribute(Constants.ITERATION_COUNT_KEY, iterationCountTextbox.getText());
        configuration.setAttribute(Constants.SMARTMETER_COMPLETION_KEY, smartMeterCompletion);
        configuration.setAttribute(Constants.TOPO_GENERATION_KEY, generateTopo);
        
        if (comboAttackSimulation.getSelectionIndex() != -1) {
            configuration.setAttribute(Constants.ATTACKER_SIMULATION_KEY, getSelectionOfComboBox(comboAttackSimulation));
        }
        if (comboPowerLoadSimulation.getSelectionIndex() != -1) {
            configuration.setAttribute(Constants.POWER_LOAD_SIMULATION_KEY, getSelectionOfComboBox(comboPowerLoadSimulation));
        }
        if (comboTerminationCondition.getSelectionIndex() != -1) {
            configuration.setAttribute(Constants.TERMINATION_CONDITION_SIMULATION_KEY, getSelectionOfComboBox(comboTerminationCondition));
        }
        if (comboProgressor.getSelectionIndex() != -1) {
            configuration.setAttribute(Constants.TIME_PROGRESSOR_SIMULATION_KEY, getSelectionOfComboBox(comboProgressor));
        }
        if (comboImpactAnalysis.getSelectionIndex() != -1) {
            configuration.setAttribute(Constants.IMPACT_ANALYSIS_SIMULATION_KEY, getSelectionOfComboBox(comboImpactAnalysis));
        }
        if (comboKritisSimulation.getSelectionIndex() != -1) {
            configuration.setAttribute(Constants.KRITIS_SIMULATION_KEY, getSelectionOfComboBox(comboKritisSimulation));
        }

        // Combo Box Parsing
        final String hackingStyleString = getSelectionOfComboBox(hackingStyleCombo);
        configuration.setAttribute(Constants.HACKING_STYLE_KEY, hackingStyleString);

        // Now Tab is Clean again
        setDirty(false);
    }

    private String getSelectionOfComboBox(Combo comboBox) {
        int selectionIndex = comboBox.getSelectionIndex();
        if (selectionIndex == -1) {
            return null;
        } else {
            return comboBox.getItem(selectionIndex);
        }
    }

    private String parseCheckBox(Button checkBox) {
        if (checkBox.getSelection()) {
            return Constants.TRUE;
        } else {
            return Constants.FALSE;
        }
    }

    @Override
    public String getErrorMessage() {

        String Message;
        if (inputBoxEmpty()) {
            Message = ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                    .getString("SimControlLaunchConfigurationTab.ErrorMessages.NO_INPUT_PATH"); //$NON-NLS-1$
        } else if (topoBoxEmpty()) {
            Message = ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                    .getString("SimControlLaunchConfigurationTab.ErrorMessages.NO_TOPO_PATH"); //$NON-NLS-1$
        } else if (otuputBoxEmpty()) {
            Message = ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                    .getString("SimControlLaunchConfigurationTab.ErrorMessages.NO_OUTPUT_PATH"); //$NON-NLS-1$
        } else if (inputExtensionWrong()) {
            Message = ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                    .getString("SimControlLaunchConfigurationTab.ErrorMessages.WRONG_INPUT_EXTENSION"); //$NON-NLS-1$
        } else if (topoExtensionWrong()) {
            Message = ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                    .getString("SimControlLaunchConfigurationTab.ErrorMessages.WRONG_TOPO_EXTENSION"); //$NON-NLS-1$
        } else if (!outputIsParent()) {
            Message = ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                    .getString("SimControlLaunchConfigurationTab.OutputIsNotParent.text"); //$NON-NLS-1$
        } else if (areOptionsWrong()) {
            Message = ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                    .getString("SimControlLaunchConfigurationTab.ErrorMessages.WRONG_OPTIONS"); //$NON-NLS-1$
        } else if (inputNotExists()) {
            Message = ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                    .getString("SimControlLaunchConfigurationTab.ErrorMessages.INPUT_PATH"); //$NON-NLS-1$
        } else if (topoNotExists()) {
            Message = ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                    .getString("SimControlLaunchConfigurationTab.ErrorMessages.TOPO_PATH"); //$NON-NLS-1$
        } else if (outputNotExists()) {
            Message = ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                    .getString("SimControlLaunchConfigurationTab.ErrorMessages.OUTPUT_PATH"); //$NON-NLS-1$
        } else if (hackingEmpty()) {
            Message = ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                    .getString("SimControlLaunchConfigurationTab.NoHackingChoiceLabel.text"); //$NON-NLS-1$
        } else if (roteIdIsNotNumeric()) {
            Message = ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                    .getString("SimControlLaunchConfigurationTab.ErrorMessages.ROOTNODE_NOT_NUMERIC"); //$NON-NLS-1$
        } else if (hackingSpeedIsNotNumeric()) {
            Message = ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                    .getString("SimControlLaunchConfigurationTab.ErrorMessages.HACKINGSPEED_NOT_NUMERIC"); //$NON-NLS-1$
        } else if (timeStepsIsNotNumeric()) {
            Message = ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                    .getString("SimControlLaunchConfigurationTab.ErrorMessages.TIMESTEPS_NOT_NUMERIC"); //$NON-NLS-1$
        } else if (iterationCountIsNotNumeric()) {
            Message = ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                    .getString("SimControlLaunchConfigurationTab.ErrorMessages.ITERATIONS_NOT_NUMERIC"); //$NON-NLS-1$
        } else {
            // everything ok
            return null;
        }

        return Message;
    }

    @Override
    public boolean isValid(final ILaunchConfiguration launchConfig) {
        // Add launchConfig Validation Checks here if needed
        return validateInput();
    }

    @Override
    public boolean canSave() {
        return true;
    }

    @Override
    public String getName() {
        // Read Resource File
        return ResourceBundle.getBundle("smartgrid.newsimcontrol.ui.messages") //$NON-NLS-1$
                .getString("SimControlLaunchConfigurationTab.Tab.RegisterText"); //$NON-NLS-1$
    }

    private void propertyChanged() {
        setDirty(true);
        updateLaunchConfigurationDialog();
    }

    /**
     * @return True if all Input Parameter are correct
     */
    private boolean validateInput() {
        return !areTextBoxesEmpty() && !areFileExtensionsWrong() &&! hackingEmpty() && 
                outputIsParent() && !areOptionsWrong() && !areFilesNotExisting() && !checkNumericfails();
    }

    /**
     * Opens OpenFileDialog und returns the Filepath as String
     */
    private String getFilePath(final String dialogMessage, final String extension) {

        final FileDialog dialog = new FileDialog(container.getShell(), SWT.OPEN);

        dialog.setText(dialogMessage);
        final String extensionFilter = "*." + extension;
        dialog.setFilterExtensions(new String[] { extensionFilter });
        final String filename = dialog.open();

        return filename;
    }

    /**
     * Opens the Directory Dialog Chooser
     */
    private String getDirPath(final String dialogMessage) {

        final DirectoryDialog dialog = new DirectoryDialog(container.getShell(), SWT.OPEN);

        dialog.setMessage(dialogMessage);

        // For User Convinience jump to Input Files Location
        if (!inputTextbox.getText().isEmpty()) {
            dialog.setFilterPath(new File(inputTextbox.getText()).getParent());
        }

        /*
         * removed by Misha: this seems unnecessary, as dialog.open() already returns a String. It
         * is also prone to NP exceptions
         */
//        final String dirName = new File(dialog.open()).getPath(); // 

        final String dirName = dialog.open();
        return dirName;
    }

    private boolean areFilesNotExisting() {
        return outputNotExists() || topoNotExists() || outputNotExists();
    }
    
    private boolean outputNotExists() {
        String outputPath = outputTextbox.getText();
        if (Files.notExists(Paths.get(outputPath))) {
            return true;
        }
        return false;
    }

    private boolean topoNotExists() {
        String topoPath = topologyTextbox.getText();
        if (Files.notExists(Paths.get(topoPath))) {
            return true;
        }
        return false;
    }

    private boolean inputNotExists() {
        String inputPath = inputTextbox.getText();
        if (Files.notExists(Paths.get(inputPath))) {
            return true;
        }
        return false;
    }

    private boolean hackingEmpty() {
        return (hackingStyleCombo.getText().equals(noHackingChoice)
                && !comboAttackSimulation.getText().equals("No Attack Simulation"));
    }
    
    private boolean areTextBoxesEmpty() {
        final boolean topoEmpty = topologyTextbox.getText().equals("");
        final boolean inEmpty = inputTextbox.getText().equals("");
        final boolean outEmpty = outputTextbox.getText().equals("");
        return topoEmpty || inEmpty || outEmpty;
    }
    
    private boolean inputBoxEmpty() {
        final boolean inEmpty = inputTextbox.getText().equals("");
        return inEmpty;
    }
    
    private boolean topoBoxEmpty() {
        final boolean inEmpty = topologyTextbox.getText().equals("");
        return inEmpty;
    }

    private boolean otuputBoxEmpty() {
        final boolean inEmpty = outputTextbox.getText().equals("");
        return inEmpty;
    }


    /**
     * @return True if topology or input or output file extensions are wrong
     */
    private boolean areFileExtensionsWrong() {
        final boolean topoWrong = !topologyTextbox.getText().endsWith(Constants.TOPO_EXTENSION);
        final boolean inWrong = !inputTextbox.getText().endsWith(Constants.INPUT_EXTENSION);

        /*
         * --> Check wheter Output path is the same as inputpath So the Input Files and Output are
         * on the same branch Needed because otherwise the Output Model gets broken
         */
        String parentOfInput = new File(inputTextbox.getText()).getParent();
        if (parentOfInput == null) {
            return true;
        }
        //final boolean outWrong = !outputTextbox.getText().contains(parentOfInput);

        return topoWrong || inWrong ;
    }
    
    private boolean topoExtensionWrong() {
        final boolean topoWrong = !topologyTextbox.getText().endsWith(Constants.TOPO_EXTENSION);
        return topoWrong;
    }
    
    private boolean inputExtensionWrong() {
        final boolean inputWrong = !inputTextbox.getText().endsWith(Constants.INPUT_EXTENSION);
        return inputWrong;
    }

    private boolean outputIsParent() {
        String parentOfInput = new File(inputTextbox.getText()).getParent();
        if (parentOfInput == null) {
            return true;
        }
        final boolean outWrong = outputTextbox.getText().contains(parentOfInput);
        return outWrong;
    }
    @SuppressWarnings("unused")
    private boolean areOptionsWrong() {
        final boolean timeStepsWrong = !isUInt(timeStepsTextBox.getText());
        final boolean rootNodeWrong = !(isUInt(rootNodeTextbox.getText()) || rootNodeTextbox.getText().equals(Constants.DEFAULT_ROOT_NODE_ID));

        // return timeStepsWrong || rootNodeWrong || smartCountWrong ||
        // controlCountWrong;
        return false;
    }

    /**
     * @param possibleInt
     *            the string to be tested to be an positive Integer
     *
     * @return true if the string can be parsed as positive Integer
     */
    private boolean isUInt(final String possibleInt) {

        boolean valid;

        try {
            Integer.parseUnsignedInt(possibleInt);
            valid = true;
        } catch (final NumberFormatException e) {
            valid = false;
        }

        return valid;
    }

    private void addElementsToCombos() {


        try {
            final List<IAttackerSimulation> attackSimExtensions = TestSimulationExtensionPointHelper.getAttackerSimulationExtensions();
            fillSimulationComboBox(attackSimExtensions, comboAttackSimulation);
        } catch (final CoreException e) {
            LOG.error("Could not read Attack Simulation extensions.", e);
        }
        
        try {
            final List<ITimeProgressor> progressorExtensions = TestSimulationExtensionPointHelper.getProgressorExtensions();
            fillSimulationComboBox(progressorExtensions, comboProgressor);
        } catch (final CoreException e) {
            LOG.error("Could not read Time Progressor extensions.", e);
        }

        try {
            final List<IImpactAnalysis> impactAnalysisExtensions = TestSimulationExtensionPointHelper.getImpactAnalysisExtensions();
            fillSimulationComboBox(impactAnalysisExtensions, comboImpactAnalysis);
        } catch (final CoreException e) {
            LOG.error("Could not read Impact Analysis extensions.", e);
        }
    }
    private boolean checkNumericfails() {
        return roteIdIsNotNumeric() || hackingSpeedIsNotNumeric() ||
                timeStepsIsNotNumeric() || iterationCountIsNotNumeric();
    }
    private boolean roteIdIsNotNumeric()  
    {  
      if (comboAttackSimulation.getText().equals("Local Hacker")){
          try  
          {  
            @SuppressWarnings("unused")
            double d = Double.parseDouble(rootNodeTextbox.getText());  
          }  
          catch(NumberFormatException nfe)  
          {  
            return true;  
          }  
      }
      return false;  
    }
    
    private boolean hackingSpeedIsNotNumeric()  
    {  
        if (!comboAttackSimulation.getText().equals("No Attack Simulation")){
          try  
          {  
            @SuppressWarnings("unused")
            double d = Double.parseDouble(hackingSpeedText.getText());  
          }  
          catch(NumberFormatException nfe)  
          {  
            return true;  
          }  
        }
      return false;  
    }
    
    private boolean timeStepsIsNotNumeric()  
    {  
        if (!comboAttackSimulation.getText().equals("No Attack Simulation")){
          try  
          {  
            @SuppressWarnings("unused")
            double d = Double.parseDouble(timeStepsTextBox.getText());  
          }  
          catch(NumberFormatException nfe)  
          {  
            return true;  
          }  
        }
      return false;  
    }
    
    private boolean iterationCountIsNotNumeric()  
    {  
        if (!comboAttackSimulation.getText().equals("No Attack Simulation")){
          try  
          {  
            @SuppressWarnings("unused")
            double d = Double.parseDouble(iterationCountTextbox.getText());  
          }  
          catch(NumberFormatException nfe)  
          {  
            return true;  
          }  
        }
      return false;  
    }
    
    
    private void fillSimulationComboBox(final List<? extends ISimulationComponent> simulationComponents, Combo comboBox) {
        for (final ISimulationComponent ele : simulationComponents) {
            comboBox.add(ele.getName());
        }
        if (comboBox.getItemCount() > 0) {
            comboBox.select(0);
        }
    }

    private void setPathTextFieldsEnabled(boolean inputEnabled) {
        inputTextbox.setEnabled(inputEnabled);
        selectInputPathButton.setEnabled(inputEnabled);
        topologyTextbox.setEnabled(inputEnabled);
        selectTopologyPathButton.setEnabled(inputEnabled);
    }
}