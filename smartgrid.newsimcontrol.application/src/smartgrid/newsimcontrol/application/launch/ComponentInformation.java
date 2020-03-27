package smartgrid.newsimcontrol.application.launch;

import java.util.Map;
import java.util.Objects;


public class ComponentInformation implements IComponentInformation {
	
	private static final String ID = "id";
	private final ILoadMe component;
	private final Map<String, String> serviceProperties;
	
	ComponentInformation(ILoadMe component, Map<String, String> serviceProperties) throws IllegalArgumentException{
		this.component = component;
		checkPropertiesExist(serviceProperties);
		this.serviceProperties = serviceProperties;
	}
	
	private void checkPropertiesExist(Map<String, String> serviceProperties) throws IllegalArgumentException {
		if(!serviceProperties.containsKey(ID)) {
			throw new IllegalArgumentException("Component service properties needs to have an id.");
		}
	}

	@Override
	public ILoadMe getComponent() {
		return this.component;
	}

	@Override
	public String getId() {
		return this.serviceProperties.get(ID);
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!ComponentInformation.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        final ComponentInformation other = (ComponentInformation) obj;
        if ((this.getComponent() == null) ? (other.getComponent() != null) : !this.getComponent().equals(other.getComponent())) {
            return false;
        }

        if ((this.getId() == null) ? (other.getId() != null) : !this.getId().equals(other.getId())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
    	return Objects.hash(this.getComponent(), this.getId());
    }

}
