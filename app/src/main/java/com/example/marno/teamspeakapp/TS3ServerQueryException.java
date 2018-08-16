package com.example.marno.teamspeakapp;

/**
 * TS3ServerQueryException represents an TS3 server query exception and will be used by the JTS3ServerQuery library.
 * @author Stefan Martens
 * @since 2.0
 */
public class TS3ServerQueryException extends Exception
{
	String apiMethodName;
	int errorID;
	String errorMessage;
	String extraErrorMessage;
	int failedPermissionID;
	
	/**
	 * Only use this constructor to create a new TS3ServerQueryException! All values of a TS3 server query error will be saved here.
	 * @param apiMethodName Free text with further information where the exception was thrown. Not important for this exception!
	 * @param errorID Always the <code>id</code> value of the TS3 server query error!
	 * @param errorMessage Always the <code>msg</code> value of the TS3 server query error!
	 * @param extraErrorMessage Always the <code>extra_msg</code> value of the TS3 server query error!
	 * @param failedPermissionID Always the <code>failed_permid</code> value of the TS3 server query error!
	 */
	public TS3ServerQueryException(String apiMethodName, String errorID, String errorMessage, String extraErrorMessage, String failedPermissionID)
	{
		super("ServerQuery Error " + errorID + ": " + errorMessage + (extraErrorMessage != null ? " - " + extraErrorMessage : "") + (failedPermissionID != null ? " - Permission ID: " + failedPermissionID : ""));
		this.apiMethodName = apiMethodName;
		try { this.errorID = Integer.parseInt(errorID); } catch (NumberFormatException nfe) { this.errorID = -1; }
		this.errorMessage = errorMessage;
		this.extraErrorMessage = extraErrorMessage;
		try { this.failedPermissionID = Integer.parseInt(failedPermissionID); } catch (NumberFormatException nfe) { this.failedPermissionID = -1; }
	}

	/**
	 * JTS3ServerQuery method where the exception was thrown.
	 * @return the apiMethodName
	 */
	public String getApiMethodName()
	{
		return apiMethodName;
	}

	/**
	 * Error ID from TS3 server
	 * @return the errorID
	 */
	public int getErrorID()
	{
		return errorID;
	}

	/**
	 * Error message from TS3 server
	 * @return the errorMessage
	 */
	public String getErrorMessage()
	{
		return errorMessage;
	}

	/**
	 * Extra error message from TS3 server. Is <code>null</code> in most cases.
	 * @return the extraErrorMessage
	 */
	public String getExtraErrorMessage()
	{
		return extraErrorMessage;
	}

	/**
	 * Failed permission ID from TS3 server. Is -1 if a failed permission is not the reason of the error.
	 * @return the failedPermissionID
	 */
	public int getFailedPermissionID()
	{
		return failedPermissionID;
	}
}
