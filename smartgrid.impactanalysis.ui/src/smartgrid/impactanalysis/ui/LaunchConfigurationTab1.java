package smartgrid.impactanalysis.ui;

//Resource Files
import java.util.ResourceBundle;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
//OpenFile Diolog MS Style
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

/**
 * @author Thorsten
 * @author Christian (Modified by)
 */
public class LaunchConfigurationTab1 extends AbstractLaunchConfigurationTab {

    /*
     * Constants for the KEYs of the Working Copy Dict
     */
    // TODO Change to Dict implementation friendly keys?
    private static final String OUTPUT_PATH_KEY = "outputPath";
    private static final String TOPOLOGY_PATH_KEY = "topologyPath";
    private static final String INPUT_PATH_KEY = "inputPath";
    private static final String TIMESTEPS_KEY = "timeSteps";

    /*
     * Constants for the Default Input/Output File Paths
     */
    private static final String DEFAULT_PATH = "";
    private static final String DEFAULT_INPUT_PATH = DEFAULT_PATH;
    private static final String DEFAULT_OUTPUT_PATH = DEFAULT_PATH;
    private static final String DEFAULT_TOPO_PATH = DEFAULT_PATH;
    private static final String DEFAULT_TIME_STEPS = "2";

    /*
     * Constants for valid Extensions
     */
    private static final String TOPO_EXTENSION = "smartgridtopo";
    private static final String INPUT_EXTENSION = "smartgridinput";
    private static final String OUTPUT_EXTENSION = "smartgridoutput";

    /*
     * UI Variables
     */
    private Composite container;
    private Text outputTextbox;
    private Text inputTextbox;
    private Text topologyTextbox;

    /**
     * Creates the UI Control
     *
     * @wbp.parser.entryPoint
     */
    @Override
    public void createControl(final Composite parent) {

        this.container = new Composite(parent, SWT.NONE);
        this.setControl(this.container);
        this.container.setLayout(new FormLayout());

        final Group group = new Group(this.container, SWT.NONE);
        final FormData fd_group = new FormData();
        fd_group.left = new FormAttachment(0, 10);
        fd_group.top = new FormAttachment(0, 10);
        group.setLayoutData(fd_group);

        group.setText(ResourceBundle.getBundle("smartgrid.impactanalysis.ui.messages").getString("LaunchConfigurationTab1.group.text"));

        this.inputTextbox = new Text(group, SWT.READ_ONLY | SWT.H_SCROLL | SWT.CANCEL);

        this.inputTextbox.setBounds(10, 22, 553, 41);
        this.inputTextbox.addModifyListener(e -> this.propertyChanged());
        this.inputTextbox.setEditable(true);
        this.inputTextbox.setText(ResourceBundle.getBundle("smartgrid.impactanalysis.ui.messages").getString("LaunchConfigurationTab1.inputTextbox.text"));
        final org.eclipse.swt.widgets.Button SelectInputPathButton = new org.eclipse.swt.widgets.Button(group, SWT.TOGGLE);
        SelectInputPathButton.setBounds(569, 22, 143, 32);

        SelectInputPathButton.setText(ResourceBundle.getBundle("smartgrid.impactanalysis.ui.messages").getString("LaunchConfigurationTab1.SelectInputPathButton.text"));

        this.topologyTextbox = new Text(group, SWT.READ_ONLY | SWT.H_SCROLL | SWT.CANCEL);
        this.topologyTextbox.setBounds(10, 69, 553, 41);
        this.topologyTextbox.addModifyListener(e -> this.propertyChanged());
        this.topologyTextbox.setEditable(true);
        this.topologyTextbox.setText(ResourceBundle.getBundle("smartgrid.impactanalysis.ui.messages") //$NON-NLS-1$
                .getString("LaunchConfigurationTab1.inputTextbox.text")); //$NON-NLS-1$

        final org.eclipse.swt.widgets.Button SelectTopologyPathButton = new org.eclipse.swt.widgets.Button(group, SWT.TOGGLE);
        SelectTopologyPathButton.setBounds(569, 60, 161, 32);
        SelectTopologyPathButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(final SelectionEvent e) {
            }
        });
        SelectTopologyPathButton.setText(ResourceBundle.getBundle("smartgrid.impactanalysis.ui.messages").getString("LaunchConfigurationTab1.SelectTopologyPathButton.text"));

        final Group grpOutputFiles = new Group(this.container, SWT.NONE);
        final FormData fd_grpOutputFiles = new FormData();
        fd_grpOutputFiles.top = new FormAttachment(group, 6);
        fd_grpOutputFiles.left = new FormAttachment(0, 10);
        grpOutputFiles.setLayoutData(fd_grpOutputFiles);

        grpOutputFiles.setText(ResourceBundle.getBundle("smartgrid.impactanalysis.ui.messages").getString("LaunchConfigurationTab1.grpOutputFiles.text"));
        final org.eclipse.swt.widgets.Button SelectOutputPathButton = new org.eclipse.swt.widgets.Button(grpOutputFiles, SWT.TOGGLE);
        SelectOutputPathButton.setBounds(569, 22, 141, 32);
        SelectOutputPathButton.setText(ResourceBundle.getBundle("smartgrid.impactanalysis.ui.messages").getString("LaunchConfigurationTab1.SelectOutputPathButton.text"));

        this.outputTextbox = new Text(grpOutputFiles, SWT.READ_ONLY | SWT.H_SCROLL | SWT.CANCEL);
        this.outputTextbox.setBounds(10, 22, 553, 41);
        this.outputTextbox.addModifyListener(e -> this.propertyChanged());
        this.outputTextbox.setEditable(true);
        this.outputTextbox.setText(ResourceBundle.getBundle("smartgrid.impactanalysis.ui.messages").getString("LaunchConfigurationTab1.inputTextbox.text"));

        SelectOutputPathButton.addMouseListener(new MouseListener() {

            @Override
            public void mouseUp(final MouseEvent e) {
            }

            @Override
            public void mouseDown(final MouseEvent e) {
                // Get Text to show in Open File Dialog from Resource File
                final String message = ResourceBundle.getBundle("smartgrid.impactanalysis.ui.messages").getString("LaunchConfigurationTab1.OpenFileDialog.SELECT_OUTPUT_PATH");
                final String outputPath = LaunchConfigurationTab1.this.getFilePath(message, LaunchConfigurationTab1.OUTPUT_EXTENSION);

                if (outputPath != null) {
                    LaunchConfigurationTab1.this.outputTextbox.setText(outputPath);
                }
            }

            @Override
            public void mouseDoubleClick(final MouseEvent e) {
            }
        });
        SelectTopologyPathButton.addMouseListener(new MouseListener() {

            @Override
            public void mouseUp(final MouseEvent e) {
            }

            @Override
            public void mouseDown(final MouseEvent e) {
                // Get Text to show in Open File Dialog from Resource File
                final String message = ResourceBundle.getBundle("smartgrid.impactanalysis.ui.messages").getString("LaunchConfigurationTab1.OpenFileDialog.SELECT_INPUT_TOPOLOGY_PATH");
                final String topologyPath = LaunchConfigurationTab1.this.getFilePath(message, LaunchConfigurationTab1.TOPO_EXTENSION);

                if (topologyPath != null) {
                    LaunchConfigurationTab1.this.topologyTextbox.setText(topologyPath);
                }
            }

            @Override
            public void mouseDoubleClick(final MouseEvent e) {
            }
        });
        SelectInputPathButton.addMouseListener(new MouseListener() {

            @Override
            public void mouseUp(final MouseEvent e) {
            }

            @Override
            public void mouseDown(final MouseEvent e) {
                // Get Text to show in Open File Dialog from Resource File
                // Gets the Filepath
                final String message = ResourceBundle.getBundle("smartgrid.impactanalysis.ui.messages").getString("LaunchConfigurationTab1.OpenFileDialog.SELECT_INPUT_PATH");
                final String inputPath = LaunchConfigurationTab1.this.getFilePath(message, LaunchConfigurationTab1.INPUT_EXTENSION);

                if (inputPath != null) {
                    LaunchConfigurationTab1.this.inputTextbox.setText(inputPath);
                }
            }

            @Override
            public void mouseDoubleClick(final MouseEvent e) {
            }
        });
    }

    /*
     * Opens OpenFileDialog und returns the Filepath as String
     */
    private String getFilePath(final String dialogMessage, final String extension) {

        final FileDialog dialog = new FileDialog(this.container.getShell(), SWT.OPEN);

        dialog.setText(dialogMessage);
        final String extensionFilter = "*." + extension;
        dialog.setFilterExtensions(new String[] { extensionFilter });
        final String filename = dialog.open();

        return filename;
    }

    @Override
    public void setDefaults(final ILaunchConfigurationWorkingCopy configuration) {
        configuration.setAttribute(TOPOLOGY_PATH_KEY, LaunchConfigurationTab1.DEFAULT_TOPO_PATH);
        configuration.setAttribute(INPUT_PATH_KEY, LaunchConfigurationTab1.DEFAULT_INPUT_PATH);
        configuration.setAttribute(OUTPUT_PATH_KEY, LaunchConfigurationTab1.DEFAULT_OUTPUT_PATH);
        configuration.setAttribute(TIMESTEPS_KEY, LaunchConfigurationTab1.DEFAULT_TIME_STEPS);
    }

    @Override
    public void initializeFrom(final ILaunchConfiguration configuration) {
        try {
            final String topoPath = configuration.getAttribute(TOPOLOGY_PATH_KEY, LaunchConfigurationTab1.DEFAULT_TOPO_PATH);
            final String inpPath = configuration.getAttribute(INPUT_PATH_KEY, LaunchConfigurationTab1.DEFAULT_INPUT_PATH);
            final String outPath = configuration.getAttribute(OUTPUT_PATH_KEY, LaunchConfigurationTab1.DEFAULT_OUTPUT_PATH);

            this.topologyTextbox.setText(topoPath);
            this.inputTextbox.setText(inpPath);
            this.outputTextbox.setText(outPath);

        } catch (final CoreException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void performApply(final ILaunchConfigurationWorkingCopy configuration) {
        final String topoPath = this.topologyTextbox.getText();
        final String inPath = this.inputTextbox.getText();
        final String outPath = this.outputTextbox.getText();

        // Add to config
        configuration.setAttribute(TOPOLOGY_PATH_KEY, topoPath);
        configuration.setAttribute(INPUT_PATH_KEY, inPath);
        configuration.setAttribute(OUTPUT_PATH_KEY, outPath);

    }

    // @Override
    // public String getMessage() {
    // return "42";
    // }

    private boolean areTextBoxesEmpty() {
        final boolean topoEmpty = this.topologyTextbox.getText().equals("");
        final boolean inEmpty = this.inputTextbox.getText().equals("");
        final boolean outEmpty = this.outputTextbox.getText().equals("");
        return topoEmpty || inEmpty || outEmpty;
    }

    /**
     *
     * @return True if topology or input or output file extensions are wrong
     */
    private boolean areFileExtensionsWrong() {
        final boolean topoWrong = !this.topologyTextbox.getText().endsWith(LaunchConfigurationTab1.TOPO_EXTENSION);
        final boolean inWrong = !this.inputTextbox.getText().endsWith(LaunchConfigurationTab1.INPUT_EXTENSION);
        final boolean outWrong = !this.outputTextbox.getText().endsWith(LaunchConfigurationTab1.OUTPUT_EXTENSION);
        return topoWrong || inWrong || outWrong;
    }

    /*
     * This Methods shows Error Messages AND NO Error Messages !!
     *
     * (non-Javadoc)
     *
     * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#getErrorMessage()
     */
    @Override
    public String getErrorMessage() {

        String Message = "";

        if (this.areTextBoxesEmpty()) {
            Message = ResourceBundle.getBundle("smartgrid.impactanalysis.ui.messages") //$NON-NLS-1$
                    .getString("LaunchConfigurationTab1.ErrorMessages.NO_PATH"); //$NON-NLS-1$
        } else if (this.areFileExtensionsWrong()) {
            Message = ResourceBundle.getBundle("smartgrid.impactanalysis.ui.messages") //$NON-NLS-1$
                    .getString("LaunchConfigurationTab1.ErrorMessages.WRONG_EXTENSIONS"); //$NON-NLS-1$
        } else {
            // NO Error Case !!
            return null;
            // NO Error Case
        }

        // Message = ResourceBundle
        // .getBundle("smartgrid.impactanalysis.ui.messages").getString("LaunchConfigurationTab1.ErrorMessages.NO_ERROR");
        // //$NON-NLS-1$ //$NON-NLS-2$
        //

        return Message;
    }

    @Override
    public boolean isValid(final ILaunchConfiguration launchConfig) {
        return !this.areTextBoxesEmpty() && !this.areFileExtensionsWrong();
    }

    @Override
    public boolean canSave() {
        return true;
    }

    @Override
    public String getName() {
        // Read Resource File
        return ResourceBundle.getBundle("smartgrid.impactanalysis.ui.messages") //$NON-NLS-1$
                .getString("LaunchConfigurationTab1.Tab.RegisterText"); //$NON-NLS-1$
    }

    private void propertyChanged() {
        this.updateLaunchConfigurationDialog();
    }

}
