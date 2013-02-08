package de.rueckemann.energystats.mongo;

public class DBException extends Exception {
	private static final long serialVersionUID = 1L;

	public DBException(String error) {
		super(error);
	}

	
}
