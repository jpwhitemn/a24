package us.msrs.aurora24.model;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public abstract class Job {

	private JobDefinition definition;
	private UUID id;
	private List<String> parameters;
	private JobState jobState;
	private String submitter;
	private OffsetDateTime lastUpdated;
	
	public Job(JobDefinition definition, UUID id, List<String> parameters, JobState jobState, String submitter,
			OffsetDateTime lastUpdated) {
		super();
		this.definition = definition;
		this.id = id;
		this.parameters = parameters;
		this.jobState = jobState;
		this.submitter = submitter;
		this.lastUpdated = lastUpdated;
	}

	public JobDefinition getDefinition() {
		return definition;
	}

	public void setDefinition(JobDefinition definition) {
		this.definition = definition;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public List<String> getParameters() {
		return parameters;
	}

	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}

	public JobState getJobState() {
		return jobState;
	}

	public void setJobState(JobState jobState) {
		if (transition(jobState))
			this.jobState = jobState;
		else
			System.out.println("Improper Job state transition");
	}

	public String getSubmitter() {
		return submitter;
	}

	public void setSubmitter(String submitter) {
		this.submitter = submitter;
	}

	public OffsetDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(OffsetDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public boolean transition(JobState newState) {
		switch (newState) {
			case Submitted:
				if (jobState != null)
					return false;
				break;
			case Running:
				if (jobState != JobState.Submitted || jobState != JobState.Paused)
					return false;
				break;
			case Paused:
				if (jobState != JobState.Running)
					return false;
				break;
			case Complete:
				if (jobState != JobState.Running)
					return false;
				break;
			case Failed:
				if (jobState != JobState.Running)
					return false;
				break;
		}
		jobState = newState;
		return true;
	}

}
