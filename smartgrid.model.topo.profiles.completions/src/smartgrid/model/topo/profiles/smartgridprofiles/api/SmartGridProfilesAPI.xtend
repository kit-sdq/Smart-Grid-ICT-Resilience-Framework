package smartgrid.model.topo.profiles.smartgridprofiles.api

import org.eclipse.emf.ecore.resource.Resource
import static extension org.palladiosimulator.mdsdprofiles.api.ProfileAPI.*

class SmartGridProfilesAPI {
	
	val public static String SMART_GRID_API_PROFILE_NAME = "SmartGridProfiles"
	
	private new(){
	}
	
	def public static boolean isSmartGridProfileApplied(Resource resource){
		return isProfileApplied(resource, SMART_GRID_API_PROFILE_NAME)
	}
	
}