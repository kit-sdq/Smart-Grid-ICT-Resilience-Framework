package smartgrid.newsimcontrol.rcp;

public enum SimControlCommands {

   INIT_TOPO("INIT_TOPO"),
   
   GET_MODIFIED_POWERSPECS("GET_MODIFIED_POWERSPECS");
   
   private String description;
	
   SimControlCommands(String description){
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
		
}