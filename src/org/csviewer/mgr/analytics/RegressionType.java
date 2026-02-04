package org.csviewer.mgr.analytics;

public enum RegressionType {
	Linear, Log, Stepwise;
	
	public String[] getParameterNames() {
		String[] parameterNames;
		
		switch(this.ordinal()) {
		case 0 : //Linear
			parameterNames = new String[] {"Intercept", "Slope"};
			break; 
		case 1: //Logarithm
			parameterNames = new String[] {"Factor", "Constant"};
			break; 
		case 2: //Stepwise
			parameterNames = new String[] {"AvgAtBirth", "OntogenyAge", "AvgAtOntogeny", "PlateauSlope"};
			break; 
		default:
			parameterNames = null;
		}

		return parameterNames;
	}

}
