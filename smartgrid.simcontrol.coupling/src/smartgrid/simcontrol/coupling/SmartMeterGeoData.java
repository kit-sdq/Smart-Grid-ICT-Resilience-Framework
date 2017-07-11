package smartgrid.simcontrol.coupling;

import java.io.Serializable;

public class SmartMeterGeoData implements Serializable {

    private static final long serialVersionUID = -1219989950279411992L;

    private double lon;
    private double lat;

    public double getLat() {
        return lat;
    }

    public void setLat(double _lat) {
        lat = _lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double _lon) {
        lon = _lon;
    }
}
