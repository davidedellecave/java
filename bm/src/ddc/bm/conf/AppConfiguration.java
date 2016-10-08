package ddc.bm.conf;

public class AppConfiguration {
	private boolean maintenanceMode = false;

	public boolean isMaintenanceMode() {
		return maintenanceMode;
	}

	public void setMaintenanceMode(boolean maintenanceMode) {
		this.maintenanceMode = maintenanceMode;
	}
}
