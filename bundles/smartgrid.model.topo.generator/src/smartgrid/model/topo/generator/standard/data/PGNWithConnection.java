package smartgrid.model.topo.generator.standard.data;

import java.util.ArrayList;

import smartgrid.model.topo.generator.standard.data.PGNWithConnection;
import smartgrid.model.topo.generator.standard.data.SmartMeter;

/**
 * A Power Grid node with a List of connected Smart Meters and a List of
 * other Power Grid nodes it is connected to.
 * Used for calculation of the Topology only.
 * @author Eric Klappert
 *
 */
public class PGNWithConnection{
	
	private ArrayList<PGNWithConnection> connectedPGN;
	private ArrayList<SmartMeter> connectedSM;
	
	private String id;
	
	public PGNWithConnection() {
		super();
		connectedPGN = new ArrayList<PGNWithConnection>();
		connectedSM = new ArrayList<SmartMeter>();
	}

	public void addConnection(PGNWithConnection pgn) {
		connectedPGN.add(pgn);
	}
	
	public void addConnection(SmartMeter sm) {
		connectedSM.add(sm);
	}
	
	public ArrayList<PGNWithConnection> getConnectedPGN() {
		return connectedPGN;
	}
	
	public ArrayList<SmartMeter> getConnectedSM() {
		return connectedSM;
	}

	public String getId() {
		return id;
	}

	public void setId(String value) {
		id = value;
	}
}
