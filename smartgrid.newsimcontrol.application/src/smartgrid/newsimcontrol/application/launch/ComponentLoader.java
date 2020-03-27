package smartgrid.newsimcontrol.application.launch;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;


@Component()
public class ComponentLoader implements IComponentLoader {

	private final List<IComponentInformation> availableComponents = new LinkedList<IComponentInformation>();
	
    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    public void bindComponent(ILoadMe component, Map<String, String> serviceProperties) {
        availableComponents.add(new ComponentInformation(component, serviceProperties));
    }



    public void unbindComponent(ILoadMe component) {
    	for(int i = 0; i < this.availableComponents.size(); ++i) {
    		if(this.availableComponents.get(i).getComponent().equals(component)) {
    			this.availableComponents.remove(i);
    		}
    	}
    }



    public void updatedComponent(ILoadMe component, Map<String, String> serviceProperties) {

        unbindComponent(component);

        bindComponent(component, serviceProperties);

    }



    public Collection<IComponentInformation> getComponents() {

        return Collections.unmodifiableList(availableComponents);

    }
}
