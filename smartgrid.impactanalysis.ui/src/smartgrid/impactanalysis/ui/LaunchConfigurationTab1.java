package smartgrid.impactanalysis.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

//OpenFile Diolog MS Style
import org.eclipse.swt.widgets.FileDialog;



//Resource Files
import java.util.ResourceBundle;

import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

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
        
        group.setText(ResourceBundle
                .getBundle("smartgrid.impactanalysis.ui.messages").getString("LaunchConfigurationTab1.group.text"));

        inputTextbox = new Text(group, SWT.READ_ONLY | SWT.H_SCROLL | SWT.CANCEL);
        
        inputTextbox.setBounds(10, 22, 553, 32);
        inputTextbox.addModifyListener(e -> propertyChanged());
        inputTextbox.setEditable(true);
        inputTextbox.setText(ResourceBundle.getBundle("smartgrid.impactanalysis.ui.messages").getString(
                "LaunchConfigurationTab1.inputTextbox.text"));
        org.eclipse.swt.widgets.Button SelectInputPathButton = new org.eclipse.swt.widgets.Button(group, SWT.TOGGLE);
        SelectInputPathButton.setBounds(569, 22, 121, 32);

        SelectInputPathButton.setText(ResourceBundle.getBundle("smartgrid.impactanalysis.ui.messages").getString(
                "LaunchConfigurationTab1.SelectInputPathButton.text"));

        topologyTextbox = new Text(group, SWT.READ_ONLY | SWT.H_SCROLL | SWT.CANCEL);
        topologyTextbox.setBounds(10, 60, 553, 32);
        topologyTextbox.addModifyListener(e -> propertyChanged());
        topologyTextbox.setEditable(true);
        topologyTextbox
                .setText(ResourceBundle.getBundle("smartgrid.impactanalysis.ui.messages").getString("LaunchConfigurationTab1.inputTextbox.text")); //$NON-NLS-1$ //$NON-NLS-2$

        org.eclipse.swt.widgets.Button SelectTopologyPathButton = new org.eclipse.swt.widgets.Button(group, SWT.TOGGLE);
        SelectTopologyPathButton.setBounds(569, 60, 121, 32);
        SelectTopologyPathButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            }
        });
        SelectTopologyPathButton.setText(ResourceBundle.getBundle("smartgrid.impactanalysis.ui.messages").getString(
                "LaunchConfigurationTab1.SelectTopologyPathButton.text"));

        Group grpOutputFiles = new Group(container, SWT.NONE);
        FormData fd_grpOutputFiles = new FormData();
        fd_grpOutputFiles.bottom = new FormAttachment(0, 184);
        fd_grpOutputFiles.right = new FormAttachment(0, 710);
        fd_grpOutputFiles.top = new FormAttachment(0, 120);
        fd_grpOutputFiles.left = new FormAttachment(0, 10);
        grpOutputFiles.setLayoutData(fd_grpOutputFiles);

        grpOutputFiles
                .setText(ResourceBundle
                        .getBundle("smartgrid.impactanalysis.ui.messages").getString("LaunchConfigurationTab1.grpOutputFiles.text"));
        org.eclipse.swt.widgets.Button SelectOutputPathButton = new org.eclipse.swt.widgets.Button(grpOutputFiles,
                SWT.TOGGLE);
        SelectOutputPathButton.setBounds(569, 22, 121, 32);
        SelectOutputPathButton.setText(ResourceBundle.getBundle("smartgrid.impactanalysis.ui.messages").getString(
                "LaunchConfigurationTab1.SelectOutputPathButton.text"));

        outputTextbox = new Text(grpOutputFiles, SWT.READ_ONLY | SWT.H_SCROLL | SWT.CANCEL);
        outputTextbox.setBounds(10, 22, 553, 32);
        outputTextbox.addModifyListener(e -> propertyChanged());
        outputTextbox.setEditable(true);
        outputTextbox.setText(ResourceBundle.getBundle("smartgrid.impactanalysis.ui.messages").getString("LaunchConfigurationTab1.inputTextbox.text"));

        

        
        SelectOutputPathButton.addMouseListener(new MouseListener() {

            @Override
            public void mouseUp(MouseEvent e) {
            }

            @Override
            public void mouseDown(MouseEvent e) {
                // Get Text to show in Open File Dialog from Resource File
                final String message = ResourceBundle.getBundle("smartgrid.impactanalysis.ui.messages").getString(
                        "LaunchConfigurationTab1.OpenFileDialog.SELECT_OUTPUT_PATH");
                String outputPath = getFilePath(message, LaunchConfigurationTab1.OUTPUT_EXTENSION);

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
                final String message = ResourceBundle.getBundle("smartgrid.impactanalysis.ui.messages").getString(
                        "LaunchConfigurationTab1.OpenFileDialog.SELECT_INPUT_TOPOLOGY_PATH");
                String topologyPath = getFilePath(message, LaunchConfigurationTab1.TOPO_EXTENSION);

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
                final String message = ResourceBundle.getBundle("smartgrid.impactanalysis.ui.messages").getString(
                        "LaunchConfigurationTab1.OpenFileDialog.SELECT_INPUT_PATH");
                String inputPath = getFilePath(message, LaunchConfigurationTab1.INPUT_EXTENSION);

                if (inputPath != null)
                    inputTextbox.setText(inputPath);
            }

            @Override
            public void mouseDoubleClick(MouseEvent e) {
            }
        });
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

    @Override
    public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
        configuration.setAttribute(TOPOLOGY_PATH_KEY, LaunchConfigurationTab1.DEFAULT_TOPO_PATH);
        configuration.setAttribute(INPUT_PATH_KEY, LaunchConfigurationTab1.DEFAULT_INPUT_PATH);
        configuration.setAttribute(OUTPUT_PATH_KEY, LaunchConfigurationTab1.DEFAULT_OUTPUT_PATH);
        configuration.setAttribute(TIMESTEPS_KEY, LaunchConfigurationTab1.DEFAULT_TIME_STEPS);
    }

    @Override
    public void initializeFrom(ILaunchConfiguration configuration) {
        try {
            String topoPath = configuration.getAttribute(TOPOLOGY_PATH_KEY, LaunchConfigurationTab1.DEFAULT_TOPO_PATH);
            String inpPath = configuration.getAttribute(INPUT_PATH_KEY, LaunchConfigurationTab1.DEFAULT_INPUT_PATH);
            String outPath = configuration.getAttribute(OUTPUT_PATH_KEY, LaunchConfigurationTab1.DEFAULT_OUTPUT_PATH);
           

           topologyTextbox.setText(topoPath);
           inputTextbox.setText(inpPath);
           outputTextbox.setText(outPath);
           
           
           
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void performApply(ILaunchConfigurationWorkingCopy configuration) {
        String topoPath = topologyTextbox.getText();
        String inPath = inputTextbox.getText();
        String outPath = outputTextbox.getText();
        
        
        //Add to config
        configuration.setAttribute(TOPOLOGY_PATH_KEY, topoPath);
        configuration.setAttribute(INPUT_PATH_KEY, inPath);
        configuration.setAttribute(OUTPUT_PATH_KEY, outPath);
        
        
    }

     //@Override
    // public String getMessage() {
  //   return "42";
//     }

    private boolean areTextBoxesEmpty() {
        final boolean topoEmpty = topologyTextbox.getText().equals("");
        final boolean inEmpty = inputTextbox.getText().equals("");
        final boolean outEmpty = outputTextbox.getText().equals("");
        return topoEmpty || inEmpty || outEmpty;
    }

    /**
     * 
     * @return True if topology or input or output file extensions are wrong
     */
    private boolean areFileExtensionsWrong() {
        final boolean topoWrong = !topologyTextbox.getText().endsWith(LaunchConfigurationTab1.TOPO_EXTENSION);
        final boolean inWrong = !inputTextbox.getText().endsWith(LaunchConfigurationTab1.INPUT_EXTENSION);
        final boolean outWrong = !outputTextbox.getText().endsWith(LaunchConfigurationTab1.OUTPUT_EXTENSION);
        return topoWrong || inWrong || outWrong;
    }
    
    


    /*
     * This Methods shows Error Messages AND NO Error Messages !!
     * 
     * (non-Javadoc)
     * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#getErrorMessage()
     */
    @Override
    public String getErrorMessage() {
        
    	String Message = "";
    	
    	
    	if (areTextBoxesEmpty())
            Message = ResourceBundle
                    .getBundle("smartgrid.impactanalysis.ui.messages").getString("LaunchConfigurationTab1.ErrorMessages.NO_PATH"); //$NON-NLS-1$ //$NON-NLS-2$
        else if (areFileExtensionsWrong())
        	Message = ResourceBundle
            .getBundle("smartgrid.impactanalysis.ui.messages").getString("LaunchConfigurationTab1.ErrorMessages.WRONG_EXTENSIONS"); //$NON-NLS-1$ //$NON-NLS-2$
        else
        	
      //NO Error Case !!
        	return null;
        	//NO Error Case
        	
//        	Message = ResourceBundle
//            .getBundle("smartgrid.impactanalysis.ui.messages").getString("LaunchConfigurationTab1.ErrorMessages.NO_ERROR"); //$NON-NLS-1$ //$NON-NLS-2$
//    	
    	
    	
    	
    	
    	return Message;
    }

    @Override
    public boolean isValid(ILaunchConfiguration launchConfig) {
        return !areTextBoxesEmpty() && !areFileExtensionsWrong();
    }



	@Override
    public boolean canSave() {
        return true;
    }

    @Override
    public String getName() {
        // Read Resource File
        return ResourceBundle
                .getBundle("smartgrid.impactanalysis.ui.messages").getString("LaunchConfigurationTab1.Tab.RegisterText"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    // @Override
    // public Image getImage() {
    // // TODO Insert small Icon here
    // return null;
    // }

    private void propertyChanged() {
        //this.setDirty(true);
        this.updateLaunchConfigurationDialog();
        
    }
    
    

    
}
