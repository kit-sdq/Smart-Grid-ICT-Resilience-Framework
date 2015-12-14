/**
 * 
 */
package smartgrid.helper;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

//import smartgridoutput.ScenarioResult;

/**
 * @author Christian
 *
 */
public final class FileSystem {

	/**
	 * This Method saves a Scenario Result on the File System at the given Path
	 * 
	 * 
	 * @param result
	 *            Scenario Result to be written on File System
	 * @param path
	 *            Path there the File will be written to
	 */
	public static void saveToFileSystem(EObject result, String path) {
		/*
		 * Save Data as Smartgridoutput on HDD under "path" location
		 */
		ResourceSet resSet = new ResourceSetImpl();

		Resource resource = resSet.createResource(URI.createFileURI(path));
		resource.getContents().add(result);
		// EcoreUtil.resolveAll(resSet);

		try {
			resource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * QUICK AND DIRTY !
	 * 
	 * This Method saves a Scenario Result on the File System at the given Path
	 * 
	 * 
	 * @param result
	 *            Scenario Result to be written on File System
	 * @param path
	 *            Path there the File will be written to
	 */
	public static void saveToFileSystemAll	(final String dirPath, EObject result,
			EObject topo, EObject input) {
		/*
		 * Save Data as Smartgridoutput on HDD under "path" location
		 */
		
		
		
		
		ResourceSet resSet = new ResourceSetImpl();

		Resource resourceResult = resSet.createResource(URI.createFileURI(dirPath + "result"));
		resourceResult.getContents().add(result);
		
		
		resSet = new ResourceSetImpl();

		Resource resourceTopo = resSet.createResource(URI.createFileURI(dirPath + "toto"));
		resourceTopo.getContents().add(topo);
		
		
		resSet = new ResourceSetImpl();

		Resource resourceInput = resSet.createResource(URI.createFileURI(dirPath + "Input"));
		resourceInput.getContents().add(input);
		
		
		
		//EcoreUtil.resolveAll(resSet);

		try {
			resourceResult.save(Collections.EMPTY_MAP);
			resourceTopo.save(Collections.EMPTY_MAP);
			resourceInput.save(Collections.EMPTY_MAP);
		} catch (IOException e) {

			e.printStackTrace();
		}
		/*
		 * End Save..
		 */
	}

	
	
	
	
	/**
	 * Saves all File Resources belonging to this PcmInstance to the given
	 * location and updates the references of the models to the new files.
	 * 
	 * @param dirPath
	 *            path to the directory in which to save
	 */
	public static void saveModel(final String dirPath, EObject result,
			EObject topo, EObject input) {

		ResourceSet resourceSet = new ResourceSetImpl();

		Resource reResult = resourceSet.createResource(URI
				.createFileURI(dirPath));

		reResult.getContents().add(result);
		reResult.getContents().add(input);
		reResult.getContents().add(topo);

		resourceSet.getResources().add(reResult);

		// EcoreUtil.resolveAll(resourceSet);

		final URIConverter uriConverter = resourceSet.getURIConverter();

		// get immutable copy of resources, because we have to modify the
		// collection
		final Resource[] resources = (Resource[]) resourceSet.getResources()
				.toArray();

		// update URIs
		for (final Resource res : resources) {
			final URI sourceUri = uriConverter.normalize(res.getURI());
			if (sourceUri.isFile()) {
				final String filePath = sourceUri.toFileString();
				final String fileName = new File(filePath).getName();
				final URI targetUri = URI.createFileURI(dirPath + fileName);

				res.setURI(targetUri);
			}
		}

		// save all to new locations
		for (final Resource res : resourceSet.getResources()) {
			final URI sourceUri = uriConverter.normalize(res.getURI());
			if (sourceUri.isFile()) {
				try {
					res.save(null);
				} catch (final IOException e) {
					throw new RuntimeException(
							"Failed to save the PCM Instance to the new location",
							e);
				}
			}
		}

	}

	// /**
	// * This Method saves a Scenario Result on the File System at the given
	// Path
	// *
	// *
	// * @param result Scenario Result to be written on File System
	// * @param path Path there the File will be written to
	// */
	// public static void saveToFileSystem(ScenarioResult result, String path) {
	// /*
	// * Save Data as Smartgridoutput on HDD under "path" location
	// */
	// ResourceSet resSet = new ResourceSetImpl();
	//
	// Resource resource = resSet.createResource(URI.createFileURI(path));
	// resource.getContents().add(result);
	// try {
	// resource.save(Collections.EMPTY_MAP);
	// } catch (IOException e) {
	//
	// e.printStackTrace();
	// }
	// /*
	// * End Save..
	// */
	// }

}
