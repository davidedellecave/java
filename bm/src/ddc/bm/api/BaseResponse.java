package ddc.bm.api;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.databind.JsonNode;

import ddc.task.TaskExitCode;

@XmlRootElement(name = "response")
public class BaseResponse {
	private String task = "";
    private String message = "";
    private TaskExitCode exitCode = TaskExitCode.Unknown;
    private JsonNode results = null;

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public TaskExitCode getExitCode() {
		return exitCode;
	}

	public void setExitCode(TaskExitCode exitCode) {
		this.exitCode = exitCode;
	}

	public JsonNode getResults() {
		return results;
	}

	public void setResults(JsonNode results) {
		this.results = results;
	}

	@Override
    public String toString() {
        return message + "|" + exitCode;
    }
}